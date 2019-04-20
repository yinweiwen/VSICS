package yction.com.vsicscomm.protocol.ips.upload;

/**
 * 文件上传完成消息应答
 * 消息ID：0x9212。
 * 报文类型：信令数据报文
 */
public class FileFinishAck {
    // 是否上传成功 0x00：完成 0x01：需要补传 其他:错误
    public byte result;

    // 补传的数据包
    public FileFinishAckItem[] reUploads;

}
