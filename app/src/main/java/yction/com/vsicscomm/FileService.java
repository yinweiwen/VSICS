package yction.com.vsicscomm;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import yction.com.vsicscomm.protocol.AcrCode;
import yction.com.vsicscomm.protocol.Errors;
import yction.com.vsicscomm.protocol.ips.cmd.FileFinish;
import yction.com.vsicscomm.protocol.ips.cmd.FileInfo;
import yction.com.vsicscomm.protocol.ips.upload.FileFrame;
import yction.com.vsicscomm.protocol.ips.upload.UploadContent;
import yction.com.vsicscomm.protocol.p808.Msg;
import yction.com.vsicscomm.protocol.p808.Protocol;
import yction.com.vsicscomm.utils.Utils;

public class FileService {
    private static final String TAG = "FileService";

    // 上传队列个数
    private static final int QueueCapacity = 100;
    // 附件上传时间间隔
    private static final long FileSendInterval = 10 * 1000;
    // 数据文件异步报文发送间隔
    private static final long SocketAsyncSendInterval = 5 * 1000;
    // 数据文件上报完成 与 开始上报完成消息 之间的时间间隔
    private static final long CmdGapInterval = 3 * 1000;
    // 附件上传尝试次数限制
    private static final int AttachmentUploadTryLimits = 3;

    private boolean run = false;

    private BlockingQueue<UploadContent> files = new LinkedBlockingQueue<UploadContent>(QueueCapacity);

    private NetService netService;

    public FileService(NetService ns) {
        netService = ns;
    }

    public void start() {
        run = true;
        new Thread(task).start();
    }

    public void stop() {
        run = false;
    }

    /**
     * 待上传附件加入上传列表
     *
     * @param file 待上传附件
     * @return 成功/失败
     */
    public boolean put(UploadContent file) {
        try {
            return files.offer(file, 5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            while (run) {
                try {
                    UploadContent file = files.peek();
                    if (file != null) {
                        Log.i(TAG, "uploading attachment " + file);
                        SendResult result = sendFile(file);

                        if (result == SendResult.Success) {
                            files.take();
                            Thread.sleep(FileSendInterval);
                        } else if (result == SendResult.Error) {
                            files.take();
                            Log.w(TAG, "attachment garbaged." + file);
                        } else if (result == SendResult.Timeout) {
                            file.tryCount++;
                            if (file.tryCount >= AttachmentUploadTryLimits) {
                                files.take();
                                Log.w(TAG, "attachment try count over limits, remove." + file);
                            }
                            Log.i(TAG, "attachment timeout,wait re-upload." + file);
                            Thread.sleep(30 * 1000);
                        } else if (result == SendResult.UnConnected) {
                            Thread.sleep(60 * 1000);
                        }
                    }
                } catch (InterruptedException e) {
                    Log.i(TAG, "queue interrupted");
                } finally {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }
    };

    enum SendResult {
        Success(0, 1000),
        Error(1, 1000),
        Timeout(2, 1000),
        UnConnected(3, 30 * 1000);

        public int state;
        public long sleep;

        SendResult(int s, long sleep) {
            state = s;
            this.sleep = sleep;
        }
    }

    private SendResult sendFile(UploadContent content) {

        TcpClient tcpClient = new TcpClient(new TcpClientListener() {
            @Override
            public void onConnection() {
                Log.i(TAG, "file client connected");
            }

            @Override
            public void onClose(String error) {
                Log.i(TAG, "file client closed");
            }

            @Override
            public void onError(String error) {
                Log.i(TAG, "file client error:" + error);
            }

            @Override
            public Msg onMsg(Msg msg) {
                return null;
            }

            @Override
            public Msg onSeperatePackage(Msg msg) {
                return Protocol.commAck(msg, AcrCode.Success);
            }
        });
        tcpClient.setHost(content.ip, content.port);
        tcpClient.setWithoutAckMsgNoCheck(true);
        try {
            if (tcpClient.connect()) {
                // 报警附件消息
                Errors err = tcpClient.send(content.alarmAttachmentInfo);
                if (content.alarmAttachmentInfo.result != AcrCode.Success) {
                    Log.w(TAG, "upload alarm attachment info failed." + content.alarmAttachmentInfo);
                    return err == Errors.Timeout ? SendResult.Timeout : SendResult.Error;
                } else {
                    for (FileInfo fi : content.fileInfos) {
                        // 文件信息上传
                        Errors err2 = tcpClient.send(fi);
                        if (fi.result != AcrCode.Success) {
                            Log.w(TAG, "upload file info failed." + fi.fileName);
                            return err2 == Errors.Timeout ? SendResult.Timeout : SendResult.Error;
                        } else {
                            // 发送文件主体
                            try {
                                Log.i(TAG, "uploading file," + fi.fileName);
                                // InputStream fis = new FileInputStream(fi.filePath);
                                InputStream fis = netService.getResources().openRawResource(fi.fileId);
                                int LEN = 64 * 1024; // 64k
                                int offset = 0;
                                int len = 0; // 实际读取流长度
                                byte[] tmp = new byte[LEN];
                                while (len != -1) {
                                    len = fis.read(tmp, 0, LEN);
                                    if(len!=-1){
                                        byte[] frame = Utils.sub(tmp, 0, len - 1);

                                        byte[] tosend = FileFrame.encode(fi.fileName, offset, frame);
                                        if (!tcpClient.write(tosend)) {
                                            return SendResult.Error;
                                        }
                                        offset += len;

                                        Thread.sleep(SocketAsyncSendInterval);
                                    }
                                }

                                Thread.sleep(CmdGapInterval);
                                // 发送文件上传完成消息
                                FileFinish ff = new FileFinish();
                                ff.fileName = fi.fileName;
                                ff.at = fi.at;
                                ff.fileSize = fi.fileSize;
                                Log.i(TAG, "uploading file finish." + fi.fileName);
                                tcpClient.send(ff);
                                // 消息处理失败
                                if (ff.ack == null) {
                                    Log.w(TAG, "upload file finish ack null." + fi.fileName);
                                    return SendResult.Error;
                                }
                                // 文件上传成功
                                if (ff.ack.result == 0) {
                                    Log.i(TAG, "upload file finished." + fi.fileName);
                                } else if (ff.ack.result == 1 && ff.ack.reUploads != null && ff.ack.reUploads.length > 0) {
                                    Log.i(TAG, "re-upload requests:" + ff.ack.reUploads.length);
                                } else {
                                    Log.w(TAG, "upload file failed." + fi.fileName);
                                }
                            } catch (FileNotFoundException e) {
                                Log.w(TAG, "file not found." + fi.fileName);
                            } catch (IOException e) {
                                Log.w(TAG, "file io exception." + fi.fileName);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        } // end send file body
                    }// foreach file info
                }
                return SendResult.Success;
            } else {
                Log.w(TAG, "upload connect failed.");
                return SendResult.UnConnected;
            }
        } catch (Exception ex) {
            Log.w(TAG, "upload attachments failed:" + ex);
            return SendResult.Error;
        } finally {
            tcpClient.shutdown();
        }
    }

//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return binder;
//    }
//
//    private FileBinder binder = new FileBinder();
//
//    public class FileBinder extends Binder {
//        public FileService getService() {
//            return FileService.this;
//        }
//    }


//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        new Thread(task).start();
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        Notification notification = new Notification.Builder(this)
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setTicker("文件上传")
//                .setWhen(System.currentTimeMillis())
//                .setContentText("等待文件上传")
//                .build();
//
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//        startForeground(1, notification);
//    }
}
