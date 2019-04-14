package yction.com.vsicscomm.protocol.ips;

/**
 * 表 4 13
 * 盲区监测系统参数
 */
public class ParamBSD {
    // 单位秒，取值范围1~10
    public byte 后方接近报警时间阈值;
    // 单位秒，取值范围1~10
    public byte 侧后方接近报警时间阈值;

    public ParamBSD() {
    }

    public ParamBSD(byte[] data) {
        后方接近报警时间阈值 = data[0];
        侧后方接近报警时间阈值 = data[1];
    }

    public byte[] toBytes() {
        return new byte[]{后方接近报警时间阈值,
                侧后方接近报警时间阈值};
    }
}
