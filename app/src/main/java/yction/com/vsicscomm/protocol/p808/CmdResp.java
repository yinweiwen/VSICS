package yction.com.vsicscomm.protocol.p808;

import yction.com.vsicscomm.protocol.AcrCode;

/**
 * 响应类命令基类
 */
public abstract class CmdResp {
    // 消息ID
    protected MID _mid;

    public CmdResp(MID m) {
        _mid = m;
    }

    public abstract Msg onMsg(Msg msg);

    // ON SP Answer
    public Msg onSeperatePackage(Msg msg) {
        return Protocol.commAck(msg, AcrCode.Success);
    }
}
