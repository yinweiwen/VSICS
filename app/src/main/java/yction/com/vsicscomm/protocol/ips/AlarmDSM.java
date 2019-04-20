package yction.com.vsicscomm.protocol.ips;

import yction.com.vsicscomm.Global;
import yction.com.vsicscomm.protocol.ByteBufferUnsigned;
import yction.com.vsicscomm.protocol.p808.Protocol;

import java.text.ParseException;
import java.util.Date;

/**
 * 驾驶员状态监测系统报警信息
 */
public class AlarmDSM implements ReportExtra {
    public short Id = 0x65;

    public long 报警ID = Global.alarmNo();
    public byte 标志状态;
    public byte 报警事件类型;
    public byte 报警级别;
    public byte 疲劳程度;
    //-- 保留4
    public byte 车速;
    public int 高程;
    public double 纬度;
    public double 经度;
    public Date 日期时间 = new Date();
    public int 车辆状态;
    public AlarmTag 报警标识号 = new AlarmTag();


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
        bb.raw().put(疲劳程度);
        bb.raw().put((byte) 0x00);
        bb.raw().put((byte) 0x00);
        bb.raw().put((byte) 0x00);
        bb.raw().put((byte) 0x00);
        bb.raw().put(车速);
        bb.putUnsignedShort(高程);
        bb.putUnsignedInt((long) (纬度 * 1e6));
        bb.putUnsignedInt((long) (经度 * 1e6));
        bb.raw().put(Protocol.date2Bcd(日期时间));
        bb.putUnsignedShort(车辆状态);
        bb.raw().put(报警标识号.toBytes());
        return bb.raw().array();
    }

    public static AlarmDSM fromBytes(byte[] data) throws ParseException {
        ByteBufferUnsigned bb = new ByteBufferUnsigned(data);
        AlarmDSM res = new AlarmDSM();
        res.报警ID = bb.getUnsignedInt();
        res.标志状态 = bb.raw().get();
        res.报警事件类型 = bb.raw().get();
        res.报警级别 = bb.raw().get();
        res.疲劳程度 = bb.raw().get();
        bb.raw().get();
        bb.raw().get();
        bb.raw().get();
        bb.raw().get();
        res.车速 = bb.raw().get();
        res.高程 = bb.getUnsignedShort();
        res.纬度 = bb.getUnsignedInt() / 1e6;
        res.经度 = bb.getUnsignedInt() / 1e6;
        byte[] dbts = new byte[6];
        bb.raw().get(dbts);
        res.日期时间 = Protocol.bcd2Date(dbts);
        res.车辆状态 = bb.getUnsignedShort();
        byte[] at = new byte[16];
        bb.raw().get(at);
        res.报警标识号 = AlarmTag.fromBytes(at);
        return res;
    }
}
