package yction.com.vsicscomm.protocol.ips.upload;

public class FileFinishAck {
    // 是否上传成功 0x00：完成 0x01：需要补传 其他:错误
    public byte result;

    public FileFinishAckItem[] reUploads;

}
