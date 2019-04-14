package yction.com.vsicscomm.protocol.ips;

/**
 * 外设ID
 */
public enum PeripheralId {

    ADAS(0x64, "高级驾驶辅助系统"),
    DSM(0x65, "驾驶员状态监控系统"),
    TPMS(0x66, "轮胎气压监测系统"),
    BSD(0x67, "盲点监测系统");

    public short getState() {
        return state;
    }

    public String getDesc() {
        return desc;
    }

    private short state;
    private String desc;

    PeripheralId(int s, String d) {
        state = (short) s;
        desc = d;
    }

    public static PeripheralId valueOf(int code) {
        for (PeripheralId x : PeripheralId.values()) {
            if (x.state == code)
                return x;
        }
        return null;
    }

}
