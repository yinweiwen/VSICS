package yction.com.vsicscomm.protocol.ips.cmd;

import yction.com.vsicscomm.protocol.p808.CmdResp;
import yction.com.vsicscomm.protocol.p808.MID;
import yction.com.vsicscomm.protocol.p808.Msg;

/**
 * 查询指定终端参数
 * 消息ID：0x8106。
 */
public class ParamQuerySpecialReq extends CmdResp {
    public ParamQuerySpecialReq() {
        super(MID.P_GET_SpecialParam);
    }

    @Override
    public Msg onMsg(Msg msg) {
        return null;
    }
}
