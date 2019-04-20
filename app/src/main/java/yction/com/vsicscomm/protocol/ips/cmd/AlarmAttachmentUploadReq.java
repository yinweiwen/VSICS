package yction.com.vsicscomm.protocol.ips.cmd;

import android.annotation.SuppressLint;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import yction.com.vsicscomm.NetService;
import yction.com.vsicscomm.R;
import yction.com.vsicscomm.protocol.ByteBufferUnsigned;
import yction.com.vsicscomm.protocol.ips.AlarmTag;
import yction.com.vsicscomm.protocol.ips.AttachmentFileName;
import yction.com.vsicscomm.protocol.ips.AttachmentInfo;
import yction.com.vsicscomm.protocol.ips.AttachmentType;
import yction.com.vsicscomm.protocol.ips.upload.UploadContent;
import yction.com.vsicscomm.protocol.p808.CmdResp;
import yction.com.vsicscomm.protocol.p808.MID;
import yction.com.vsicscomm.protocol.p808.Msg;
import yction.com.vsicscomm.utils.Utils;

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
            Content c = new Content(msg.body());
            System.out.println(c.toString());
            // handle upload request
            netService.UploadFile(c);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public class Content {
        public String ip;
        public int tcpPort;
        public int udpPort;
        public AlarmTag alarmTag = new AlarmTag();
        public String alarmId;

        public Content(byte[] data) throws ParseException {
            ByteBufferUnsigned bb = new ByteBufferUnsigned(data);
            byte len = bb.raw().get();
            byte[] ipbts = new byte[len];
            bb.raw().get(ipbts);
            ip = Utils.getString(ipbts);
            tcpPort = bb.getUnsignedShort();
            udpPort = bb.getUnsignedShort();
            byte[] at = new byte[16];
            bb.raw().get(at);
            alarmTag = AlarmTag.fromBytes(at);
            byte[] abts = new byte[32];
            bb.raw().get(abts);
            alarmId = Utils.getString(abts);
        }

        @SuppressLint("DefaultLocale")
        @Override
        public String toString() {
            return String.format("ip:%s,tcp:%d,udp:%d", ip, tcpPort, udpPort);
        }
    }
}
