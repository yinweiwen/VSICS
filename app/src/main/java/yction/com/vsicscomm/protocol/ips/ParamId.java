package yction.com.vsicscomm.protocol.ips;

/**
 * ADAS：高级驾驶辅助系统 (Advanced Driver Assistant System)
 * DSM：驾驶员状态监测 (Driving State Monitoring)
 * TPMS：轮胎气压监测系统（Tire Pressure Monitoring Systems）
 * BSD:盲点监测（Blind Spot Detection）
 * CAN：控制器局域网络（Controller Area Network）
 */
public enum ParamId {

    ADAS(0xF364, "高级驾驶辅助系统"),
    DSM(0xF365, "驾驶员状态监测"),
    TPMS(0xF366, "轮胎气压监测系统"),
    BSD(0xF367, "盲点监测"),;

    public long getState() {
        return state;
    }

    public String getDesc() {
        return desc;
    }

    private long state;
    private String desc;

    ParamId(long s, String d) {
        state = s;
        desc = d;
    }

    public static ParamId valueOf(long code) {
        for (ParamId x : ParamId.values()) {
            if (x.state == code)
                return x;
        }
        return null;
    }
}
