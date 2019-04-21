package yction.com.vsicscomm.protocol.ips.upload;

import android.annotation.SuppressLint;

import yction.com.vsicscomm.protocol.ips.AttachmentType;
import yction.com.vsicscomm.utils.Utils;

public class UploadAttachment {
    // 文件资源ID (和filePath二选一)
    public int fileId;
    // 文件路径 (fileId)
    public String filePath;

    // 文件类型
    public AttachmentType attachmentType;
    // 通道号
    public int channel;
    // 外设ID
    public byte deviceId;
    // 对应的模块报警类型
    public byte alarmType;
    // 后缀名
    public String suffix;

    public UploadAttachment(int fileId, AttachmentType attachmentType, int channel, byte deviceId, byte alarmType, String suffix) {
        this.fileId = fileId;
        this.attachmentType = attachmentType;
        this.channel = channel;
        this.deviceId = deviceId;
        this.alarmType = alarmType;
        this.suffix = suffix;
    }

    /**
     * 获取告警附件的文件名称
     *
     * @param alarmId 平台为告警分配的唯一ID
     * @return 名称
     */
    public String getFileName(String alarmId) {
        return toFileName(attachmentType, channel, deviceId, alarmType, 0, alarmId, suffix);
    }


    /**
     * 文件名称命名规则
     *
     * @param at            文件类型：00——图片；01——音频；02——视频；03——文本；04——其它
     * @param channel       0~37表示JT/T 1076标准中表2定义的视频通道.64表示ADAS模块视频通道。65表示DSM模块视频通道。
     * @param alarmDeviceId 外设ID  报警类型：由外设ID和对应的模块报警类型组成的编码，例如，前向碰撞报警表示为“6401”。
     * @param alarmSubType  对应的模块报警类型
     * @param serialNo      序号：用于区分相同通道、相同类型的文件编号
     * @param alarmId       报警编号：平台为报警分配的唯一编号
     * @param suffix        后缀名
     * @return 告警附件文件名
     */
    @SuppressLint("DefaultLocale")
    public static String toFileName(AttachmentType at,
                                    int channel,
                                    byte alarmDeviceId,
                                    byte alarmSubType,
                                    int serialNo,
                                    String alarmId,
                                    String suffix) {
        return String.format("%s_%s_%s%s_%d_%s.%s",
                at.fileHeadCode,
                Integer.toHexString(channel),
                Utils.int2HexString(alarmDeviceId, ""),
                Utils.int2HexString(alarmSubType, ""),
                serialNo,
                alarmId,
                suffix);
    }
}
