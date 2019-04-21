package yction.com.vsicscomm.protocol.ips.upload;

import java.util.Date;

public class AlarmAttachment {
    // 告警发生的时刻
    public Date date;
    // 告警附件信息
    public UploadAttachment[] uploadAttachment;

    public AlarmAttachment(Date date, UploadAttachment... attachment) {
        this.date = date;
        this.uploadAttachment = attachment;
    }
}
