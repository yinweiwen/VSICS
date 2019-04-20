package yction.com.vsicscomm.protocol.ips.cmd;

import android.net.Uri;

import yction.com.vsicscomm.NetService;
import yction.com.vsicscomm.R;
import yction.com.vsicscomm.protocol.ips.AttachmentFileName;
import yction.com.vsicscomm.protocol.ips.AttachmentInfo;
import yction.com.vsicscomm.protocol.ips.AttachmentType;
import yction.com.vsicscomm.protocol.ips.FileUpload;
import yction.com.vsicscomm.protocol.ips.upload.UploadContent;
import yction.com.vsicscomm.protocol.p808.CmdResp;
import yction.com.vsicscomm.protocol.p808.MID;
import yction.com.vsicscomm.protocol.p808.Msg;
import yction.com.vsicscomm.utils.Utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

/**
 * 4.5 报警附件上传指令
 * 消息ID：0x9208
 */
public class AlarmAttachmentUploadReq extends CmdResp {
    private NetService netService;

    public AlarmAttachmentUploadReq(NetService service) {
        super(MID.P_REQ_AlarmAttachUpload);
        netService = service;
    }

    @Override
    public Msg onMsg(Msg msg) {
        try {
            FileUpload fu = FileUpload.fromBytes(msg.body());
            System.out.println(fu.toString());
            // todo handle upload request
            UploadContent content = new UploadContent();
            content.ip = fu.ip;
            content.port = fu.tcpPort;
            content.alarmAttachmentInfo = new AlarmAttachmentInfo();
            content.alarmAttachmentInfo.alarmId = fu.alarmId;

            byte cnt = 1;
            content.alarmAttachmentInfo.attachNum = cnt;
            content.alarmAttachmentInfo.attachmentInfos = new AttachmentInfo[cnt];

//            String file1 = "res://raw/" + R.raw.default1;
//            String filepath1 = Uri.parse(file1).getPath();
//            long filesize1 =new File(netService.getFilesDir(), filepath1).length();
            String filepath1 = "";
            int fileId1 = R.raw.dsm01;
            InputStream is = netService.getResources().openRawResource(fileId1);
            BufferedInputStream bis = new BufferedInputStream(is);
            long filesize1 = bis.available();
            System.out.println("filesize:" + filesize1);
            if (filesize1 == 0) {

                System.out.println("file not exists");
            } else {

                String filename1 =
                        AttachmentFileName.toFileName(AttachmentType.Picture,
                                0, (byte) 0x65, (byte) 0x01, 0,
                                fu.alarmId, "jpg");
                content.alarmAttachmentInfo.attachmentInfos[0] = new AttachmentInfo();
                content.alarmAttachmentInfo.attachmentInfos[0].filename = filename1;
                content.alarmAttachmentInfo.attachmentInfos[0].filesize = filesize1;

                content.fileInfos = new FileInfo[cnt];
                content.fileInfos[0] = new FileInfo();
                content.fileInfos[0].fileName = filename1;
                content.fileInfos[0].filePath = filepath1;
                content.fileInfos[0].fileId = fileId1;
                content.fileInfos[0].at = AttachmentType.Picture;
                content.fileInfos[0].fileSize = filesize1;

                netService.getFileService().put(content);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
