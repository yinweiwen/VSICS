package yction.com.vsicscomm.protocol.ips;

import java.util.EnumSet;
import java.util.Set;

public enum AlarmTPMSType {

    // 胎压（定时上报）
    Timing(1),
    // bit1：胎压过高报警
    PressureHigh(1 << 1),
    // bit2：胎压过低报警
    PressureLow(1 << 2),
    // bit3：胎温过高报警
    TemperatureHigh(1 << 3),
    // bit4：传感器异常报警
    SensorAbnormal(1 << 4),
    // bit5：胎压不平衡报警
    PressureImbalance(1 << 5),
    // bit6：慢漏气报警
    SlowAirLeak(1 << 6),
    // bit7：电池电量低报警
    LowPower(1 << 7),;

    private int state;

    AlarmTPMSType(int state) {
        this.state = state;
    }

    public static EnumSet<AlarmTPMSType> getStatusFlags(int statusValue) {
        EnumSet statusFlags = EnumSet.noneOf(AlarmTPMSType.class);
        for (AlarmTPMSType s : AlarmTPMSType.values()) {
            int flagValue = s.state;
            if ((flagValue & statusValue) == flagValue) {
                statusFlags.add(s);
            }
        }
        return statusFlags;
    }

    public static int getStatusValue(Set<AlarmTPMSType> flags) {
        int value = 0;
        for (AlarmTPMSType statusFlag : flags) {
            value |= statusFlag.state;
        }
        return value;
    }

    public static int status(AlarmTPMSType... args) {
        int value = 0;
        for (AlarmTPMSType statusFlag : args) {
            value |= statusFlag.state;
        }
        return value;
    }
}
