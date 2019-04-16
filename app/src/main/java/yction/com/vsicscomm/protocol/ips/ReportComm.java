package yction.com.vsicscomm.protocol.ips;

import java.util.Date;

import yction.com.vsicscomm.protocol.ByteBufferUnsigned;
import yction.com.vsicscomm.protocol.p808.Protocol;

public class ReportComm {
    public long alarmTag;
    public long stateTag;
    // 32.1314312971,119.5449829102
    public double latitude;
    public double longitude;

    public int height;
    public double speed;
    public int direction;
    public Date date = new Date();

    public byte[] toBytes() {
        ByteBufferUnsigned bb = new ByteBufferUnsigned(28);
        bb.putUnsignedInt(alarmTag);
        bb.putUnsignedInt(stateTag);
        bb.putUnsignedInt((long) (latitude * 1e6));
        bb.putUnsignedInt((long) (longitude * 1e6));
        bb.putUnsignedShort(height);
        bb.putUnsignedShort((int) (speed * 10));
        bb.putUnsignedShort(direction);
        bb.raw().put(Protocol.date2Bcd(date));
        return bb.raw().array();
    }
}
