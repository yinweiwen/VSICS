package yction.com.vsicscomm.protocol.ips;

import yction.com.vsicscomm.Global;
import yction.com.vsicscomm.protocol.ByteBufferUnsigned;
import yction.com.vsicscomm.protocol.p808.Protocol;

import java.util.Date;

/**
 * 高级驾驶辅助系统报警信息
 */
public class AlarmADAS implements ReportExtra {
    public short Id = 0x64;

    public long 报警ID = Global.alarmNo();
    public byte 标志状态;
    public byte 报警事件类型;
    public byte 报警级别;
    public byte 前车车速;
    public byte 前车行人距离;
    public byte 偏离类型;
    public byte 道路标志识别类型;
    public byte 道路标志识别数据;
    public byte 车速;
    public int 高程;
    public double 纬度;
    public double 经度;
    public Date 日期时间 = new Date();
    public int 车辆状态;
    public AlarmTag 报警标识号 = new AlarmTag();

    public AlarmADAS() {

    }

    @Override
    public short getId() {
        return Id;
    }

    @Override
    public byte[] getBytes() {
        ByteBufferUnsigned bb = new ByteBufferUnsigned(47);
        bb.putUnsignedInt(报警ID);
        bb.raw().put(标志状态);
        bb.raw().put(报警事件类型);
        bb.raw().put(报警级别);
        bb.raw().put(前车车速);
        bb.raw().put(前车行人距离);
        bb.raw().put(偏离类型);
        bb.raw().put(道路标志识别类型);
        bb.raw().put(道路标志识别数据);
        bb.raw().put(车速);
        bb.putUnsignedShort(高程);
        bb.putUnsignedInt((long) (纬度 * 1e6));
        bb.putUnsignedInt((long) (经度 * 1e6));
        bb.raw().put(Protocol.date2Bcd(日期时间));
        bb.putUnsignedShort(车辆状态);
        bb.raw().put(报警标识号.toBytes());
        return bb.raw().array();
    }
}
