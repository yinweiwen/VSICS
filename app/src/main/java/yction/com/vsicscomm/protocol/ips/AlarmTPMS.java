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

    // 报警ID
    public long alarmId;
    // 标志状态
    public byte tag;
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
    // 报警事件列表总数
    public byte eventCount;
    // 报警事件信息列表
    public AlarmTPMSItem[] eventInfos;

    public AlarmTPMS(ReportComm comm, AlarmTPMSItem[] tpmsItems) {
        alarmId = Global.alarmNo();
        alarmTag = new AlarmTag();
        date = new Date();
        if (comm != null) {
            speed = (byte) comm.speed;
            height = comm.height;
            latitude = comm.latitude;
            longitude = comm.longitude;
        }
        if (tpmsItems != null) {
            eventCount = (byte) tpmsItems.length;
            eventInfos = tpmsItems;
        }
    }

    @Override
    public short getId() {
        return Id;
    }

    @Override
    public byte[] getBytes() {
        ByteBufferUnsigned bb = new ByteBufferUnsigned(4096);

        bb.putUnsignedInt(alarmId);
        bb.raw().put(tag);
        bb.raw().put(speed);
        bb.putUnsignedShort(height);
        bb.putUnsignedInt((long) (latitude * 1e6));
        bb.putUnsignedInt((long) (longitude * 1e6));
        bb.raw().put(Protocol.date2Bcd(date));
        bb.putUnsignedShort(vehicleStatus);
        bb.raw().put(alarmTag.toBytes());
        bb.raw().put(eventCount);
        for (int i = 0; i < eventCount; i++) {
            bb.raw().put(eventInfos[i].toBytes());
        }
        int len = bb.raw().position();
        byte[] temp = new byte[len];
        System.arraycopy(bb.raw().array(), 0, temp, 0, len);
        return temp;
    }
}
