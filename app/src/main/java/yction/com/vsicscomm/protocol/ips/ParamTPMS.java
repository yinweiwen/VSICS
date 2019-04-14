package yction.com.vsicscomm.protocol.ips;

import yction.com.vsicscomm.protocol.ByteBufferUnsigned;

/**
 * 表 4 - 12
 * 胎压监测系统参数
 */
public class ParamTPMS {
    // 例：195/65R1591V 12个字符，用ASCII表述。默认值“900R20”
    public byte[] 轮胎规格型号 = new byte[12];
    public int 胎压单位;
    public int 正常胎压值;
    public int 胎压不平衡门限;
    public int 慢漏气门限;
    public int 低压阈值;
    public int 高压阈值;
    public int 高温阈值;
    public int 电压阈值;
    public int 定时上报时间间隔;
    // 保留项 6

    public ParamTPMS() {
    }

    public ParamTPMS(byte[] data) {
        ByteBufferUnsigned bb = new ByteBufferUnsigned(data);
        轮胎规格型号 = new byte[12];
        bb.raw().get(轮胎规格型号);
        胎压单位 = bb.getUnsignedShort();
        正常胎压值 = bb.getUnsignedShort();
        胎压不平衡门限 = bb.getUnsignedShort();
        慢漏气门限 = bb.getUnsignedShort();
        低压阈值 = bb.getUnsignedShort();
        高压阈值 = bb.getUnsignedShort();
        高温阈值 = bb.getUnsignedShort();
        电压阈值 = bb.getUnsignedShort();
        定时上报时间间隔 = bb.getUnsignedShort();
    }

    public byte[] toBytes() {
        ByteBufferUnsigned bb = new ByteBufferUnsigned(36);
        bb.raw().put(轮胎规格型号);
        bb.putUnsignedShort(胎压单位);
        bb.putUnsignedShort(正常胎压值);
        bb.putUnsignedShort(胎压不平衡门限);
        bb.putUnsignedShort(慢漏气门限);
        bb.putUnsignedShort(低压阈值);
        bb.putUnsignedShort(高压阈值);
        bb.putUnsignedShort(高温阈值);
        bb.putUnsignedShort(电压阈值);
        bb.putUnsignedShort(定时上报时间间隔);
        return bb.raw().array();
    }
}
