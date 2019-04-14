package yction.com.vsicscomm.protocol.ips;

public enum PeripheralState {

    State_Normal(0x01, "正常工作"),
    State_Standby(0x02, "待机状态"),
    State_UpgradeMaintenance(0x03, "升级维护"),
    State_Abnormal(0x04, "设备异常"),
    State_Disconnect(0x10, "断开连接");

    public short getState() {
        return state;
    }

    public String getDesc() {
        return desc;
    }

    private short state;
    private String desc;

    PeripheralState(int s, String d) {
        state = (short) s;
        desc = d;
    }

    public static PeripheralState valueOf(int code) {
        for (PeripheralState x : PeripheralState.values()) {
            if (x.state == code)
                return x;
        }
        return null;
    }
}
