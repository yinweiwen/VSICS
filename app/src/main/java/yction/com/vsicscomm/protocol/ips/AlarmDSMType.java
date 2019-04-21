package yction.com.vsicscomm.protocol.ips;

public enum AlarmDSMType {
    FatigueDriving(0x01),
    Calling(0x02),
    Smoking(0x03),
    DistractedDriving(0x04),
    DriverAbnormal(0x05),
    AutoCaptureEvent(0x10),
    DriverChangeEvent(0x11),;


    private byte state;

    AlarmDSMType(int b) {
        state = (byte) b;
    }

    public byte getState() {
        return state;
    }

    public static AlarmDSMType valueOf(int code) {
        for (AlarmDSMType x : AlarmDSMType.values()) {
            if (x.state == code)
                return x;
        }
        return null;
    }
}
