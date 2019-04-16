package yction.com.vsicscomm.protocol.ips.cmd;

import yction.com.vsicscomm.protocol.ips.FileUpload;
import yction.com.vsicscomm.protocol.p808.CmdResp;
import yction.com.vsicscomm.protocol.p808.MID;
import yction.com.vsicscomm.protocol.p808.Msg;

import java.text.ParseException;

/**
 * 4.5 报警附件上传指令
 * 消息ID：0x9208
 */
public class AlarmAttachmentUploadReq extends CmdResp {
    public AlarmAttachmentUploadReq() {
        super(MID.P_REQ_AlarmAttachUpload);
    }

    @Override
    public Msg onMsg(Msg msg) {
        try {
            FileUpload fu = FileUpload.fromBytes(msg.body());
            System.out.println(fu.toString());
            // todo handle upload request
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return msg;
    }
}
