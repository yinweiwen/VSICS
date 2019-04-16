package yction.com.vsicscomm.protocol.ips.cmd;

import java.nio.ByteBuffer;

import yction.com.vsicscomm.protocol.ByteBufferUnsigned;
import yction.com.vsicscomm.protocol.ips.AlarmTag;
import yction.com.vsicscomm.protocol.ips.AttachmentInfo;
import yction.com.vsicscomm.protocol.p808.CmdReq;
import yction.com.vsicscomm.protocol.p808.MID;
import yction.com.vsicscomm.protocol.p808.Msg;

public class AlarmAttachmentInfo extends CmdReq {

    public byte[] terminalId = new byte[7];
    public AlarmTag alarmTag = new AlarmTag();
    public byte[] alarmId = new byte[32];
    public byte infoType;
    public byte attachNum;
    public AttachmentInfo[] attachmentInfos;

    public AlarmAttachmentInfo() {
        super(MID.C_AlarmAttachmentInfo);
    }

    protected byte[] toBytes() {
        ByteBuffer bb = ByteBuffer.allocate(40960);
        bb.put(terminalId);
        bb.put(alarmTag.toBytes());
        bb.put(alarmId);
        bb.put(infoType);
        bb.put(attachNum);
        for (int i = 0; i < attachNum; i++) {
            bb.put(attachmentInfos[i].toBytes());
        }
        int len = bb.position();
        byte[] res = new byte[len];
        System.arraycopy(bb.array(), 0, res, 0, len);
        return res;
    }

    @Override
    protected void onMsg(Msg msg) {

    }
}
