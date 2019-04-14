package yction.com.vsicscomm;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.AsyncSocket;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.ConnectCallback;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.future.Cancellable;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import yction.com.vsicscomm.protocol.AckRes;
import yction.com.vsicscomm.protocol.ConnectErr;
import yction.com.vsicscomm.protocol.Errors;
import yction.com.vsicscomm.protocol.AcrCode;
import yction.com.vsicscomm.protocol.p808.CmdReq;
import yction.com.vsicscomm.protocol.p808.Msg;
import yction.com.vsicscomm.protocol.p808.MsgBuffer;
import yction.com.vsicscomm.protocol.p808.MsgFrame;
import yction.com.vsicscomm.protocol.p808.MsgFrameBuffer;
import yction.com.vsicscomm.protocol.p808.Protocol;
import yction.com.vsicscomm.protocol.p808.cmd.Auth;
import yction.com.vsicscomm.protocol.p808.cmd.Hearbeat;
import yction.com.vsicscomm.protocol.p808.cmd.Registry;
import yction.com.vsicscomm.utils.Log;
import yction.com.vsicscomm.utils.Utils;

import static yction.com.vsicscomm.utils.Utils.bytesToHexString;

/*
    通信协议采用 TCP 或 UDP，平台
    作为服务器端，终端作为客户端。当数据通信链路异常时，终端可以采用 SMS 消息方式进
    行通信。

    1. 建立连接 消息鉴权
    2. 连接维持（心跳）
    3. 连接断开判断：链路断开、重试多次无应答

    终端向平台发送消息后无应答，需要重发，重发超时和次数公式：
    TN+1=TN ×（N+1）
    未发送的消息保存待重发

 */
public class TcpClient {

    private final static String LOG_TAG = "TCP_CLIENT";
    private WeakReference<TcpClientListener> mListener;
    private AsyncSocket mSocket;

    private static final int mHeartbeatInterval = 30 * 1000;
    private static final int mConnectTimeout = 30 * 1000000;
    private static final int mConnectRetryDelay = 10 * 1000;
    private static final int mSendTryLimit = 3;
    private static final int mSendRetryLag = 100;
    private static final int mSendSyncTimeout = 3000000;
    private static final int mSendBlock = 2048;
    private static final int mSendAsyncInterval = 1000;

    // 终端上线(建立连接/鉴权成功)
    private boolean mIsOnline = false;
    private boolean mIsConnecting = false;
    private Cancellable cancellable;

    // ****必备参数****
    // 连接参数
    private String host;
    private Integer port;
    // 注册参数
    public int province;
    public int city;
    public byte[] manufacturerId;
    public byte[] terminalModel;
    public byte[] terminalId;
    public byte licenseColor;
    public String vehicleIdentification;
    // ***************

    private boolean isStarted;
    private boolean isShutdown;
    private long lastActive = new Date().getTime();
    private long heatbeatCnt = 0;
    private int heatbeatLogStep = 100;
    private byte[] mEncrypted = null;
    private ConnectErr mConnectErr = ConnectErr.None;

    private final Object connectMonitor = new Object();

    private SyncCallback mSyncCallback;

    private final ReentrantLock writeLock = new ReentrantLock();

    private MsgBuffer _buffer = new MsgBuffer();

    public TcpClient(TcpClientListener listener) throws IOException {
        mListener = new WeakReference<TcpClientListener>(listener);
        CmdReq._client = this;
    }

    public boolean getStatus() {
        return mIsOnline;
    }

    public void setHost(String h, Integer p) {
        host = h;
        port = p;
        try {
            mSocket.close();
        } catch (Exception ignored) {
        }
    }

    public void shutdown() {
        try {
            isShutdown = true;
            mSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if (isStarted) return;
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                if (isShutdown) break;
                                if (!mIsOnline) {
                                    synchronized (connectMonitor) {
                                        mIsConnecting = true;
                                        connect(host, port);
                                        Log.d(LOG_TAG, String.format("connecting %s:%d...", host, port));
                                        connectMonitor.wait(mConnectTimeout);
                                        if (mIsConnecting && cancellable != null) {
                                            cancellable.cancel();
                                        }
                                        Log.d(LOG_TAG, "connect " + (mIsOnline ? "success" : "failed"));
                                        if (!mIsOnline) {
                                            ResetConnection();
                                            Thread.sleep(mConnectErr.sleepPolicy());
                                            mConnectErr = ConnectErr.None;
                                        }
                                    }
                                } else {
                                    if (!mSocket.isOpen())
                                        throw new Exception("error connection");
                                    if (new Date().getTime() - lastActive > mHeartbeatInterval) {
                                        heartbeat();
                                        if (heatbeatCnt++ % heatbeatLogStep == 1) {
                                            Log.i(LOG_TAG, "heatbeat sent");
                                        }
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                ResetConnection();
                                Log.w(LOG_TAG, "tcp error: " + ex.getMessage());
                            } finally {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        Log.e(LOG_TAG, "connection thread exit!");
                    }
                }
        ).start();

        isStarted = true;
    }

    private void connect(final String host, final Integer port) throws UnknownHostException {
        final InetSocketAddress socketAddress;
        if (host != null) {
            socketAddress = new InetSocketAddress(InetAddress.getByName(host), port);
        } else {
            socketAddress = new InetSocketAddress(port);
        }
        cancellable = AsyncServer.getDefault().connectSocket(socketAddress, new ConnectCallback() {
            @Override
            public void onConnectCompleted(Exception ex, final AsyncSocket socket) {
                if (ex == null) { // 连接建立
                    try {
                        mSocket = socket;
                        socket.setClosedCallback(new CompletedCallback() {
                            @Override
                            public void onCompleted(Exception ex) {
                                Log.i(LOG_TAG, "socket close");
                                mIsOnline = false;
                                mListener.get().onClose(ex == null ? null : ex.getMessage());
                            }
                        });

                        socket.setEndCallback(new CompletedCallback() {
                            @Override
                            public void onCompleted(Exception ex) {
                                Log.i(LOG_TAG, "socket end");
                                mListener.get().onError(ex == null ? null : ex.getMessage());
                                socket.close();
                            }
                        });

                        socket.setDataCallback(new DataCallback() {
                            @Override
                            public void onDataAvailable(DataEmitter emitter, ByteBufferList bb) {
                                byte[] data = bb.getAllByteArray();
                                Log.d(LOG_TAG, "rec:" + bytesToHexString(data));

                                List<MsgFrame> frames = MsgFrameBuffer.TryDecode(data);
                                for (MsgFrame frame : frames) {
                                    Msg msg = _buffer.AddAndGet(frame);
                                    onData(msg);
                                }
                            }
                        });

                        StartLogin();
                    } catch (Exception inex) {
                        Log.w(LOG_TAG, "handle connect error:" + inex.getMessage());
                        inex.printStackTrace();
                    }

                } else {
                    Log.d(LOG_TAG, "Connect err:" + ex.getMessage());
                    mSocket = null;
                    // 通知主线程连接操作完成
                    synchronized (connectMonitor) {
                        mConnectErr = ConnectErr.TcpErr;
                        mIsConnecting = false;
                        connectMonitor.notify();
                    }
                }
            }
        });
    }

    public void StartLogin() {
        Log.i(LOG_TAG, "start login..");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (login()) {
                    mIsOnline = true;
                    mListener.get().onConnection();
                }
                synchronized (connectMonitor) {
                    mConnectErr = ConnectErr.Refuse;
                    mIsConnecting = false;
                    connectMonitor.notify();
                }
            }
        }).start();
    }

    /**
     * 接收消息处理
     *
     * @param msg 接收的消息
     */
    private void onData(Msg msg) {
        try {
            Log.i(LOG_TAG,"on msg:"+msg.toString());
            if (!msg.isReady()) { // 分包应答
                Msg ack = mListener.get().onSeperatePackage(msg);
                if (ack != null) {
                    sendAck(ack);
                }
            }
            // 同步消息答复
            if (mSyncCallback != null && mSyncCallback.onData(msg)) {
                return;
            }

            Msg ack = mListener.get().onMsg(msg);
            if (ack != null) {
                sendAck(ack);
            }
        } catch (Exception ex) {
            Log.w(LOG_TAG, "处理接收数据错误," + msg.toString()
                    + "，" + ex.getMessage());
        }
    }

    /**
     * 登录鉴权
     *
     * @return 成功/失败
     */
    private boolean login() {
        Registry registry = new Registry(province, city, manufacturerId,
                terminalId, terminalModel, licenseColor, vehicleIdentification);
        Log.i(LOG_TAG, "发送注册命令");
        registry.send();
        if (registry.ack != null) {
            if (registry.ack.result == Registry.Result.OK ||
                    registry.ack.result == Registry.Result.VehicleRegisted ||
                    registry.ack.result == Registry.Result.TerminalRegisted) {
                Auth auth = new Auth(registry.ack.AuthToken);
                auth.send();
                if (auth.ack == AcrCode.Success) {
                    return true;
                } else {
                    Log.w(LOG_TAG, "终端登录失败:" + auth.ack.getDesc());
                    return false;
                }
            } else {
                Log.i(LOG_TAG, "终端注册失败");
                return false;
            }
        }
        return false;
    }

    /**
     * 心跳
     */
    private void heartbeat() {
        new Hearbeat().sendAsyn();
    }

    /**
     * 同步请求
     *
     * @param msg 发送的命令请求
     * @return 返回的消息; 发送失败时返回空
     */
    public Msg send(final Msg msg) {
        Log.i(LOG_TAG,"send:"+msg.toString());
        Msg res = null;
        for (int i = 0; i < msg.frames().length; i++) {
            AckRes ar = writeSync(msg.frames()[i]);
            if (ar.error != Errors.Success) {
                Log.w(LOG_TAG, "同步发送错误:" + ar.error.getDesc());
                break;
            }
            if (i != msg.frames().length - 1) { // 如果分包发送,判断分包应答是否正确
                AcrCode ac = Protocol.checkCommAck(msg, ar.ack);
                if (ac == AcrCode.Success) {
                    Log.w(LOG_TAG, "分包应答错误:" + ac.getDesc());
                    break;
                }
            } else {
                res = ar.ack;
            }
        }
        return res;
    }

    /**
     * 异步答复 单帧/多帧答复消息
     *
     * @param msg 答复消息
     * @return 答复发送成功/失败
     */
    public boolean sendAck(final Msg msg) {
        Log.i(LOG_TAG,"send ack:"+msg.toString());
        boolean flag = true;
        for (int i = 0; i < msg.frames().length; i++) {
            MsgFrame frame = msg.frames()[i];
            if (i != msg.frames().length - 1) {
                AckRes ar = writeSync(frame);
                if (ar.error != Errors.Success) {
                    Log.w(LOG_TAG, "多包回复发送错误:" + ar.error.getDesc());
                    flag = false;
                    break;
                }
                AcrCode ac = Protocol.checkCommAck(msg, ar.ack);
                if (ac == AcrCode.Success) {
                    Log.w(LOG_TAG, "多包回复应答错误:" + ac.getDesc());
                    flag = false;
                    break;
                }
            } else {
                flag = write(frame.toBytes());
            }
        }
        return flag;
    }

    /**
     * 异步消息发送
     *
     * @param msg 待发送消息
     */
    public boolean sendAsync(final Msg msg) {
        Log.i(LOG_TAG,"send async:"+msg.toString());
        for (MsgFrame frame : msg.frames()) {
            if (!write(frame.toBytes())) {
                return false;
            }
            try {
                Thread.sleep(mSendAsyncInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 消息帧同步发送控制
     *
     * @param frame 待发送的单包消息
     * @return 同步发送结果，包含成功/失败结果和接收数据报文
     */
    private AckRes writeSync(final MsgFrame frame) {
        try {
            int cnt = 0;
            AckRes ar;
            do {
                if (cnt > 0) Thread.sleep(mSendRetryLag);
                ar = writeSync(frame, timeout(cnt));
                cnt++;
            } while (ar.error == Errors.Timeout && cnt < mSendTryLimit);
            ar.retryCnt = cnt;
            return ar;
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            return new AckRes(Errors.Default);
        }
    }

    /**
     * 应答超时时间和重传次数由平台参数指定，
     * 每次重传后的应答超时时间的计算公式见式
     * TN+1=TN ×（N+1）
     * TN+1——每次重传后的应答超时时间；
     * TN——前一次的应答超时时间；
     * N——重传次数
     *
     * @param n 重传次数
     * @return 超时时间ms
     */
    private int timeout(int n) {
        if (n == 0) return mSendSyncTimeout;
        return timeout(n - 1) * (n + 1);
    }

    /**
     * 同步写入方法
     *
     * @param msg       写入消息
     * @param msTimeout 超时时间
     * @return 写入结果:超时/忙碌/成功
     * @throws InterruptedException
     */
    private AckRes writeSync(final MsgFrame msg, final int msTimeout) throws InterruptedException {
        boolean flag = writeLock.tryLock(200, TimeUnit.MICROSECONDS);
        if (!flag) return new AckRes(Errors.Busy);
        try {
            if (!write(msg.toBytes())) {
                return new AckRes(Errors.Default, "socket write error");
            }
            final Msg[] recMsg = {null};
            final Monitor monitor = new Monitor();
            final int mSyncMN = msg.getHead().msgNo;
            mSyncCallback = new SyncCallback() {
                @Override
                public boolean onData(Msg m) {
                    synchronized (monitor) {
                        // 应答消息体中第一个WORD字符是请求消息的流水号
                        if (m.readAckMsgNo() == mSyncMN) {
                            recMsg[0] = m;
                            monitor.flag = true;
                            monitor.notifyAll();
                            return true;
                        }
                        return false;
                    }
                }
            };
            synchronized (monitor) {
                while (true) {
                    monitor.wait(msTimeout);
                    if (monitor.flag) {
                        monitor.flag = false;
                        if (recMsg[0].isReady()) {
                            return new AckRes(Errors.Success, recMsg[0]);
                        }
                        //else 继续等到余下分包到达
                    } else {
                        return new AckRes(Errors.Timeout);
                    }
                }
            }
        } catch (Exception e) {
            Log.d(LOG_TAG, "syncWrite err: " + e.getMessage());
            return new AckRes(Errors.Default, e.getMessage());
        } finally {
            mSyncCallback = null;
            writeLock.unlock();
        }
    }

    interface SyncCallback {
        // 同步通信中已处理 -> true
        boolean onData(Msg msg);
    }

    class Monitor {
        boolean flag;
    }

    private boolean write(final byte[] data) {
        try {
            Log.d(LOG_TAG, "send:" + bytesToHexString(data));
            mSocket.write(new ByteBufferList(data));
            lastActive = new Date().getTime();
            return true;
        } catch (Exception ex) {
            Log.w(LOG_TAG, "socket write error:" + ex.getMessage());
            return false;
        }
    }

    // 连接失败时重置连接
    private void ResetConnection() {
        try {
            if (mSocket != null && mSocket.isOpen()) {
                mSocket.close();
                mSocket = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}