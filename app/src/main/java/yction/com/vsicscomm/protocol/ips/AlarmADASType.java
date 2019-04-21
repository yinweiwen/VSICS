package yction.com.vsicscomm.protocol.ips;

public enum AlarmADASType {
    ForwardCollision(0x01),
    LaneDeparture(0x02),
    VehicleDistanceApproach(0x03),
    PedestrianCollision(0x04),
    FrequentLaneChange(0x05),
    RoadSignOverrun(0x06),
    Obstacle(0x07),
    RoadSignRecognitionEvent(0x10),
    ActivityCaptureEvent(0x11),;


    private byte state;

    AlarmADASType(int b) {
        state = (byte) b;
    }

    public byte getState() {
        return state;
    }


    public static AlarmADASType valueOf(int code) {
        for (AlarmADASType x : AlarmADASType.values()) {
            if (x.state == code)
                return x;
        }
        return null;
    }
}
