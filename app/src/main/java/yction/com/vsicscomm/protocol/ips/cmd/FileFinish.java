package yction.com.vsicscomm.protocol.ips.cmd;

import yction.com.vsicscomm.protocol.ByteBufferUnsigned;
import yction.com.vsicscomm.protocol.ips.AttachmentType;
import yction.com.vsicscomm.protocol.ips.upload.FileFinishAck;
import yction.com.vsicscomm.protocol.ips.upload.FileFinishAckItem;
import yction.com.vsicscomm.protocol.p808.CmdReq;
import yction.com.vsicscomm.protocol.p808.MID;
import yction.com.vsicscomm.protocol.p808.Msg;
import yction.com.vsicscomm.utils.Utils;

/**
 * 文件上传完成消息
 * 消息ID：0x1212。
 * 报文类型：信令数据报文
 */
public class FileFinish extends CmdReq {

    // 文件名称
    public String fileName;
    // 文件类型
    public AttachmentType at;
    // 文件大小
    public long fileSize;

    // 应答解析
    public FileFinishAck ack;

    public FileFinish() {
        super(MID.C_AlarmFileUploadFinish);
    }

    @Override
    protected byte[] toBytes() {
        byte[] fnbs = Utils.getBytes(fileName);
        ByteBufferUnsigned bb = new ByteBufferUnsigned(6 + fnbs.length);
        bb.raw().put((byte) fnbs.length);
        bb.raw().put(fnbs);
        bb.raw().put(at.state);
        bb.putUnsignedInt(fileSize);
        return bb.raw().array();
    }

    @Override
    public void onMsg(Msg msg) {
        if (msg.msgId() == MID.P_ACK_AlarmFileUploadFinish.getCode()) {
            ack = new FileFinishAck();
            ByteBufferUnsigned bb = new ByteBufferUnsigned(msg.body());
            byte fileNameLen = bb.raw().get();
            byte[] fileNameBts = new byte[fileNameLen];
            bb.raw().get(fileNameBts);
            byte fileType = bb.raw().get();
            ack.result = bb.raw().get();
            if (ack.result == 0x01) {
                byte reUploadCnt = bb.raw().get();
                ack.reUploads = new FileFinishAckItem[reUploadCnt];
                for (int i = 0; i < reUploadCnt; i++) {
                    ack.reUploads[i].offset = bb.getUnsignedInt();
                    ack.reUploads[i].size = bb.getUnsignedInt();
                }
            }
        } else {
            ack = new FileFinishAck();
            ack.result = 0x02;
        }
    }
}
