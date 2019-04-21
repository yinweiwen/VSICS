package yction.com.vsicscomm.protocol.ips;

import yction.com.vsicscomm.protocol.ByteBufferUnsigned;

/**
 * 表 4 19胎压监测系统报警/事件信息列表格式
 */
public class AlarmTPMSItem {
    // 报警轮胎位置编号
    public byte tireNum;
    /*
    bit0：胎压（定时上报）
    bit1：胎压过高报警
    bit2：胎压过低报警
    bit3：胎温过高报警
    bit4：传感器异常报警
    bit5：胎压不平衡报警
    bit6：慢漏气报警
    bit7：电池电量低报警
     */
    // 报警事件类型
    public int type;
    // 胎压 单位 Kpa
    public int tirePressure;
    // 胎温 单位 ℃
    public int tireTemperature;
    // 电池电量 单位 %
    public int power;

    public AlarmTPMSItem(int num, int pres, int temp, int power, AlarmTPMSType... types) {
        tireNum = (byte) num;
        tirePressure = pres;
        tireTemperature = temp;
        this.power = power;
        this.type = AlarmTPMSType.status(types);
    }

    public byte[] toBytes() {
        ByteBufferUnsigned bb = new ByteBufferUnsigned(9);
        bb.raw().put(tireNum);
        bb.putUnsignedShort(type);
        bb.putUnsignedShort(tirePressure);
        bb.putUnsignedShort(tireTemperature);
        bb.putUnsignedShort(power);
        return bb.raw().array();
    }
}
