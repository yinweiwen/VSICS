package yction.com.vsicscomm.protocol.ips;

public enum AlarmBSDType {
    Rear(0x01),
    RearLeft(0x02),
    RearRight(0x03);

    private byte state;

    AlarmBSDType(int b) {
        state = (byte) b;
    }

    public byte getState() {
        return state;
    }


    public static AlarmBSDType valueOf(int code) {
        for (AlarmBSDType x : AlarmBSDType.values()) {
            if (x.state == code)
                return x;
        }
        return null;
    }
}
