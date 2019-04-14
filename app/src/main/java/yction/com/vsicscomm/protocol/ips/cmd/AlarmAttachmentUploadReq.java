package yction.com.vsicscomm.protocol.ips.cmd;

import yction.com.vsicscomm.protocol.ips.FileUpload;
import yction.com.vsicscomm.protocol.p808.CmdReq;
import yction.com.vsicscomm.protocol.p808.MID;
import yction.com.vsicscomm.protocol.p808.Msg;

import java.text.ParseException;

/**
 * Created by yww08 on 2019/4/11.
 */
public class AlarmAttachmentUploadReq extends CmdReq {
    public AlarmAttachmentUploadReq() {
        super(MID.P_REQ_AlarmAttachUpload);
    }

    @Override
    protected void onMsg(Msg msg) {
        try {
            FileUpload fu=FileUpload.fromBytes(msg.body());
            // todo handle upload request
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
