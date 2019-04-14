package yction.com.vsicscomm.protocol.ips;

public enum PassthroughType {
    State(0xf7, "外设状态信息：外设工作状态、设备报警信息"),
    Info(0xf8, "外设传感器的基本信息");

    public short getState() {
        return state;
    }

    public String getDesc() {
        return desc;
    }

    private short state;
    private String desc;

    PassthroughType(int s, String d) {
        state = (short) s;
        desc = d;
    }

    public static PassthroughType valueOf(int code) {
        for (PassthroughType x : PassthroughType.values()) {
            if (x.state == code)
                return x;
        }
        return null;
    }


}
