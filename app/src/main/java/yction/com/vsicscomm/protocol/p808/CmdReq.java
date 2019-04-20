package yction.com.vsicscomm.protocol.p808;

import yction.com.vsicscomm.protocol.AcrCode;

/**
 * 请求命令构造基类
 * 信令数据文件格式
 * <p>
 * 请求命令多数派生自此类
 * 1. 继承构造函数，构造函数参数包含此命令需要的参数，构造函数内生成消息体_body
 * 2. 继承onMsg处理；同步消息返回的内容在此方法中处理
 */
public abstract class CmdReq {
    public AcrCode result = AcrCode.Error;

    // 消息ID
    protected MID _mid;
    // 生成的消息内容
    protected Msg _msg;

    // 同步请求后的返回(可空)(保留字段)
    public Msg Ack;

    public CmdReq(MID m) {
        _mid = m;
    }

    public Msg msg() {
        if (_msg == null) {
            MsgFrame[] mf = Protocol.encode(_mid.getCode(), toBytes());
            _msg = new Msg(mf);
        }
        return _msg;
    }

    protected abstract byte[] toBytes();

    /**
     * 返回消息处理函数
     *
     * @param msg 返回消息
     */
    public void onMsg(Msg msg) {
        try {
            result = Protocol.checkCommAck(_msg, msg);
            if (result == null) result = AcrCode.Error;
        } catch (Exception ex) {
            result = AcrCode.Error;
        }
    }
}
