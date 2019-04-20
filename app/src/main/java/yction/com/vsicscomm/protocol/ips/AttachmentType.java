package yction.com.vsicscomm.protocol.ips;

/**
 * 附件文件类型
 */
public enum AttachmentType {
    Picture(0, "00", "图片"),
    Audio(1, "01", "音频"),
    Video(2, "02", "视频"),
    Text(3, "03", "文本"),
    Other(4, "04", "其他"),;

    public String fileHeadCode;
    public String desc;
    public byte state;

    AttachmentType(int s, String fhc, String d) {
        state = (byte) s;
        fileHeadCode = fhc;
        desc = d;
    }
}
