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

    // 报警ID
    public long alarmId;
    // 标志状态
    public byte tag;
    // 报警/事件类型
    public AlarmADASType type;
    // 报警级别
    public byte level;
    // 前车车速
    public byte frontCarSpeed;
    // 前车行人距离
    public byte frontPedestrianDistance;
    // 偏离类型
    public byte deviationType;
    // 道路标志识别类型
    public byte roadSignRecognitionType;
    // 道路标志识别数据
    public byte roadSignRecognitionData;
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

    public AlarmADAS(ReportComm comm) {
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
        bb.raw().put(frontCarSpeed);
        bb.raw().put(frontPedestrianDistance);
        bb.raw().put(deviationType);
        bb.raw().put(roadSignRecognitionType);
        bb.raw().put(roadSignRecognitionData);
        bb.raw().put(speed);
        bb.putUnsignedShort(height);
        bb.putUnsignedInt((long) (latitude * 1e6));
        bb.putUnsignedInt((long) (longitude * 1e6));
        bb.raw().put(Protocol.date2Bcd(date));
        bb.putUnsignedShort(vehicleStatus);
        bb.raw().put(alarmTag.toBytes());
        return bb.raw().array();
    }
}
