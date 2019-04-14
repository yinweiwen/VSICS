package yction.com.vsicscomm.protocol.ips.cmd;

import yction.com.vsicscomm.protocol.AcrCode;
import yction.com.vsicscomm.protocol.ByteBufferUnsigned;
import yction.com.vsicscomm.protocol.ips.ParamADAS;
import yction.com.vsicscomm.protocol.ips.ParamBSD;
import yction.com.vsicscomm.protocol.ips.ParamDSM;
import yction.com.vsicscomm.protocol.ips.ParamId;
import yction.com.vsicscomm.protocol.ips.ParamTPMS;
import yction.com.vsicscomm.protocol.p808.CmdResp;
import yction.com.vsicscomm.protocol.p808.MID;
import yction.com.vsicscomm.protocol.p808.Msg;
import yction.com.vsicscomm.protocol.p808.Protocol;

/*
 *  参数设置指令
 *  0x8103
 */
public class ParamSetReq extends CmdResp {

    public ParamSetReq() {
        super(MID.P_SET_Param);
    }

    @Override
    public Msg onMsg(Msg msg) {
        ByteBufferUnsigned bb = new ByteBufferUnsigned(msg.body());
        ParamId pid = ParamId.valueOf(bb.getUnsignedInt());
        short len = bb.getUnsignedByte();
        byte[] data = new byte[len];
        bb.raw().get(data);
        switch (pid) {
            case ADAS:
                ParamADAS paramADAS = new ParamADAS(data);
                // todo setParamADAS
                break;
            case DSM:
                ParamDSM param = new ParamDSM(data);
                break;
            case TPMS:
                ParamTPMS paramTPMS = new ParamTPMS(data);
                break;
            case BSD:
                ParamBSD paramBSD = new ParamBSD(data);
                break;
            default:
                System.out.println("not support param id");
                break;
        }
        return Protocol.commAck(msg, AcrCode.Success);
    }
}
