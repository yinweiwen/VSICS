package yction.com.vsicscomm.protocol.p808.cmd;

import yction.com.vsicscomm.protocol.AcrCode;
import yction.com.vsicscomm.protocol.p808.CmdReq;
import yction.com.vsicscomm.protocol.p808.MID;
import yction.com.vsicscomm.protocol.p808.Msg;
import yction.com.vsicscomm.protocol.p808.Protocol;
import yction.com.vsicscomm.utils.Utils;

/**
 * 终端鉴权
 * 消息ID：0x0102。
 */
public class Auth extends CmdReq {
    public AcrCode ack;

    private String token;

    public Auth(String token) {
        super(MID.C_Auth);
        this.token = token;
    }

    @Override
    protected byte[] toBytes() {
        return Utils.getBytes(token);
    }

    @Override
    public void onMsg(Msg msg) {
        ack = Protocol.checkCommAck(_msg, msg);
        if (ack == null) ack = AcrCode.Error;
    }
}
