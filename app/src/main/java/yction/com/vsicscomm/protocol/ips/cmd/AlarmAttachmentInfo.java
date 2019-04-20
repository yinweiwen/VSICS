package yction.com.vsicscomm.protocol.ips.cmd;

import android.annotation.SuppressLint;

import java.nio.ByteBuffer;

import yction.com.vsicscomm.Global;
import yction.com.vsicscomm.protocol.ips.AlarmTag;
import yction.com.vsicscomm.protocol.ips.AttachmentInfo;
import yction.com.vsicscomm.protocol.p808.CmdReq;
import yction.com.vsicscomm.protocol.p808.MID;
import yction.com.vsicscomm.utils.Utils;

/**
 * 报警附件信息消息
 * 消息ID：0x1210。
 * 报文类型：信令数据报文。
 */
public class AlarmAttachmentInfo extends CmdReq {
    // 终端ID
    public String terminalId = Global.terminalId;
    // 报警标识号
    public AlarmTag alarmTag = new AlarmTag();
    public String alarmId = "";
    // 0x00：正常报警文件信息 0x01：补传报警文件信息
    public byte infoType;
    // 附件数量
    public byte attachNum;
    // 附件信息列表
    public AttachmentInfo[] attachmentInfos = new AttachmentInfo[0];

    public AlarmAttachmentInfo() {
        super(MID.C_AlarmAttachmentInfo);
    }

    protected byte[] toBytes() {
        ByteBuffer bb = ByteBuffer.allocate(40960);
        bb.put(Utils.getBytes(terminalId, 7));
        bb.put(alarmTag.toBytes());
        bb.put(Utils.getBytes(alarmId, 32));
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

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String.format("alarmId:%s attachments:%d", alarmId, attachmentInfos.length);
    }
}
