package yction.com.vsicscomm.protocol.ips.upload;

/**
 * 文件结束应答中包含重传的包内容
 */
public class FileFinishAckItem {
    // 需要补传的数据在文件中的偏移量
    public long offset;
    // 需要补传的数据长度
    public long size;
}
