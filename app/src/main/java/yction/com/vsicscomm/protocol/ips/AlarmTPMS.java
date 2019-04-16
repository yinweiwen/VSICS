package yction.com.vsicscomm.protocol.ips;

import yction.com.vsicscomm.Global;
import yction.com.vsicscomm.protocol.ByteBufferUnsigned;
import yction.com.vsicscomm.protocol.p808.Protocol;

import java.util.Date;

/**
 * 胎压监测系统报警信息
 */
public class AlarmTPMS implements ReportExtra {
    public short Id = 0x66;

    public long 报警ID = Global.alarmNo();
    public byte 标志状态;
    public byte 车速;
    public int 高程;
    public double 纬度;
    public double 经度;
    public Date 日期时间 = new Date();
    public int 车辆状态;
    public AlarmTag 报警标识号 = new AlarmTag();
    public byte 报警事件列表总数;
    public AlarmTPMSItem[] 报警事件信息列表;

    @Override
    public short getId() {
        return Id;
    }

    @Override
    public byte[] getBytes() {
        ByteBufferUnsigned bb = new ByteBufferUnsigned(4096);

        bb.putUnsignedInt(报警ID);
        bb.raw().put(标志状态);
        bb.raw().put(车速);
        bb.putUnsignedShort(高程);
        bb.putUnsignedInt((long) (纬度 * 1e6));
        bb.putUnsignedInt((long) (经度 * 1e6));
        bb.raw().put(Protocol.date2Bcd(日期时间));
        bb.putUnsignedShort(车辆状态);
        bb.raw().put(报警标识号.toBytes());
        bb.raw().put(报警事件列表总数);
        for (int i = 0; i < 报警事件列表总数; i++) {
            bb.raw().put(报警事件信息列表[i].toBytes());
        }
        int len = bb.raw().position();
        byte[] temp = new byte[len];
        System.arraycopy(bb.raw().array(), 0, temp, 0, len);
        return temp;
    }
}
