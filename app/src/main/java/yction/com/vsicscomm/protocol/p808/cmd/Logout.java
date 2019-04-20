package yction.com.vsicscomm.protocol.p808.cmd;

import yction.com.vsicscomm.protocol.p808.CmdReq;
import yction.com.vsicscomm.protocol.p808.MID;
import yction.com.vsicscomm.protocol.p808.Msg;

/**
 * 终端注销
 * 消息 ID： 0x0003。
 */
public class Logout extends CmdReq {
    public Logout() {
        super(MID.C_Logout);
    }

    @Override
    protected byte[] toBytes() {
        return new byte[0];
    }
}
