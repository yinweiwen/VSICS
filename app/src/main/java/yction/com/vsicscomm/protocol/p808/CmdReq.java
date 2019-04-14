package yction.com.vsicscomm.protocol.p808;

import yction.com.vsicscomm.TcpClient;

/**
 * 请求命令构造基类
 * <p>
 * 请求命令多数派生自此类
 * 1. 继承构造函数，构造函数参数包含此命令需要的参数，构造函数内生成消息体_body
 * 2. 继承onMsg处理；同步消息返回的内容在此方法中处理
 */
public abstract class CmdReq {

    // 终端TCP控制类(静态量,终端创建的时候指定)
    public static TcpClient _client;

    // 消息ID
    protected MID _mid;
    // 消息部分
    protected byte[] _body;
    // 生成的消息内容
    protected Msg _msg;

    // 同步请求后的返回(可空)(保留字段)
    public Msg Ack;

    public CmdReq(MID m) {
        _mid = m;
    }

    public Msg msg() {
        if (_msg == null) {
            MsgFrame[] mf = Protocol.encode(_mid.getCode(), _body);
            _msg = new Msg(mf);
        }
        return _msg;
    }

    /**
     * 同步发送
     */
    public void send() {
        Ack = _client.send(msg());
        if (Ack != null)
            onMsg(Ack);
    }

    /**
     * 异步分送
     *
     * @return 发送是否成功
     */
    public boolean sendAsyn() {
        return _client.sendAsync(msg());
    }

    /**
     * 返回消息处理函数
     *
     * @param msg 返回消息
     */
    protected abstract void onMsg(Msg msg);
}
