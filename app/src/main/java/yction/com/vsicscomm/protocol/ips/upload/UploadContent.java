package yction.com.vsicscomm.protocol.ips.upload;

import yction.com.vsicscomm.protocol.ips.cmd.AlarmAttachmentInfo;
import yction.com.vsicscomm.protocol.ips.cmd.FileInfo;

public class UploadContent {
    public String ip;
    public int port;
    public AlarmAttachmentInfo alarmAttachmentInfo;
    public FileInfo[] fileInfos;

    public int tryCount;

    @Override
    public String toString() {
        return alarmAttachmentInfo.toString();
    }
}
