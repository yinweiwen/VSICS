package yction.com.vsicscomm.protocol.ips.upload;

import yction.com.vsicscomm.protocol.ips.cmd.AlarmAttachmentInfo;
import yction.com.vsicscomm.protocol.ips.cmd.FileInfo;

/**
 * 附件上传队列中的内容
 */
public class UploadContent {
    // 附件服务器地址
    public String ip;
    // 附件服务器端口
    public int port;
    // 报警附件信息
    public AlarmAttachmentInfo alarmAttachmentInfo;
    // 文件信息
    public FileInfo[] fileInfos;

    // variable: 当前尝试次数(针对发送超时的情况)
    public int tryCount;

    @Override
    public String toString() {
        return alarmAttachmentInfo.toString();
    }
}
