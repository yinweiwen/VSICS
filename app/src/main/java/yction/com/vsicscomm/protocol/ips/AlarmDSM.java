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

    // 报警ID
    public long alarmId;
    // 标志状态
    public byte tag;
    // 报警/事件类型
    public AlarmDSMType type;
    // 报警级别
    public byte level;
    // 疲劳程度
    public byte fatigueDegree;

    // 车速
    public byte speed;
    // 高程
    public int height;
    // 纬度
    public double latitude;
    // 经度
    public double longitude;
    // 日期时间
    public Date date;
    // 车辆状态
    public int vehicleStatus;
    // 报警标识号
    public AlarmTag alarmTag;

    public AlarmDSM(ReportComm comm) {
        alarmId = Global.alarmNo();
        alarmTag = new AlarmTag();
        date = new Date();
        if (comm != null) {
            speed = (byte) comm.speed;
            height = comm.height;
            latitude = comm.latitude;
            longitude = comm.longitude;
        }
    }

    @Override
    public short getId() {
        return Id;
    }

    @Override
    public byte[] getBytes() {
        ByteBufferUnsigned bb = new ByteBufferUnsigned(47);
        bb.putUnsignedInt(alarmId);
        bb.raw().put(tag);
        bb.raw().put(type.getState());
        bb.raw().put(level);
        bb.raw().put(fatigueDegree);
        bb.raw().put((byte) 0x00);
        bb.raw().put((byte) 0x00);
        bb.raw().put((byte) 0x00);
        bb.raw().put((byte) 0x00);
        bb.raw().put(speed);
        bb.putUnsignedShort(height);
        bb.putUnsignedInt((long) (latitude * 1e6));
        bb.putUnsignedInt((long) (longitude * 1e6));
        bb.raw().put(Protocol.date2Bcd(date));
        bb.putUnsignedShort(vehicleStatus);
        bb.raw().put(alarmTag.toBytes());
        return bb.raw().array();
    }

    public static AlarmDSM fromBytes(byte[] data) throws ParseException {
        ByteBufferUnsigned bb = new ByteBufferUnsigned(data);
        AlarmDSM res = new AlarmDSM(null);
        res.alarmId = bb.getUnsignedInt();
        res.tag = bb.raw().get();
        res.type = AlarmDSMType.valueOf(bb.raw().get());
        res.level = bb.raw().get();
        res.fatigueDegree = bb.raw().get();
        bb.raw().get();
        bb.raw().get();
        bb.raw().get();
        bb.raw().get();
        res.speed = bb.raw().get();
        res.height = bb.getUnsignedShort();
        res.latitude = bb.getUnsignedInt() / 1e6;
        res.longitude = bb.getUnsignedInt() / 1e6;
        byte[] dbts = new byte[6];
        bb.raw().get(dbts);
        res.date = Protocol.bcd2Date(dbts);
        res.vehicleStatus = bb.getUnsignedShort();
        byte[] at = new byte[16];
        bb.raw().get(at);
        res.alarmTag = AlarmTag.fromBytes(at);
        return res;
    }
}
