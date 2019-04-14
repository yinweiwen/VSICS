package yction.com.vsicscomm.protocol.ips.cmd;

import yction.com.vsicscomm.protocol.p808.CmdResp;
import yction.com.vsicscomm.protocol.p808.MID;
import yction.com.vsicscomm.protocol.p808.Msg;

/**
 * 查询终端参数
 * 消息 ID： 0x8104
 */
public class ParamQueryReq extends CmdResp {

    public ParamQueryReq() {
        super(MID.P_GET_Param);
    }

    @Override
    public Msg onMsg(Msg msg) {
        return null;
    }
}
