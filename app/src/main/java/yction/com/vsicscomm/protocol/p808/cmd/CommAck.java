package yction.com.vsicscomm.protocol.p808.cmd;

import yction.com.vsicscomm.protocol.AcrCode;
import yction.com.vsicscomm.protocol.ByteBufferUnsigned;
import yction.com.vsicscomm.protocol.p808.Acr;
import yction.com.vsicscomm.protocol.p808.MID;
import yction.com.vsicscomm.protocol.p808.MsgFrame;
import yction.com.vsicscomm.protocol.p808.Protocol;

public class CommAck {
    public AcrCode code;
    public int msgId;
    public int msgNo;

    public CommAck() {

    }

    public CommAck(AcrCode ac, int mi, int mn) {
        code = ac;
        msgId = mi;
        msgNo = mn;
    }

    /**
     * 终端通用应答
     *
     * @return
     */
    public MsgFrame toFrame() {
        ByteBufferUnsigned bb = new ByteBufferUnsigned(5);
        bb.putUnsignedShort(msgNo);
        bb.putUnsignedShort(msgId);
        bb.putUnsignedByte(code.getCode());
        MsgFrame[] frames = Protocol.encode(MID.C_Ack.getCode(), bb.raw().array());
        return frames[0];
    }
}