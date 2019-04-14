package yction.com.vsicscomm.protocol.p808.cmd;

import yction.com.vsicscomm.protocol.p808.CmdReq;
import yction.com.vsicscomm.protocol.p808.MID;
import yction.com.vsicscomm.protocol.p808.Msg;
import yction.com.vsicscomm.protocol.p808.MsgFrame;
import yction.com.vsicscomm.protocol.p808.Protocol;

/**
 * 终端心跳：
 * 消息 ID： 0x0002。
 * 终端心跳数据消息体为空
 */
public class Hearbeat extends CmdReq {
    public Hearbeat() {
        super(MID.C_Heart);
    }

    @Override
    public Msg msg() {
        if (_msg == null) {
            _msg = new Msg(Protocol.heartbeat(), true);
        }
        return _msg;
    }

    @Override
    protected void onMsg(Msg msg) {

    }
}
