package yction.com.vsicscomm.protocol.ips;

import java.util.EnumSet;
import java.util.Set;

public enum AlarmTagState {
    // 0 1：紧急报警，触动报警开关后触发
    Emergency(1),
    //1 1：超速报警
    Overspeed(1 << 1),
    //2 1：疲劳驾驶
    FatigueDriving(1 << 2),
    //3 1：危险预警
    Danger(1 << 3),
    //4 1：GNSS 模块发生故障
    GNSSFailure(1 << 4),
    //5 1：GNSS 天线未接或被剪断
    GNNSAntennaOff(1 << 5),
    //6 1：GNSS 天线短路
    GNNSAntennaShort(1 << 6),
    //7 1：终端主电源欠压
    Undervoltage(1 << 7),
    //8 1：终端主电源掉电
    PowerFault(1 << 8),
    //9 1：终端 LCD 或显示器故障
    DisplayFault(1 << 9),
    //10 1：TTS 模块故障
    TTS(1 << 10),
    //11 1：摄像头故障
    Camera(1 << 11),
    //12 1：道路运输证 IC 卡模块故障
    TransportCID(1 << 12),
    //13 1：超速预警
    PreOverspeed(1 << 13),
    //14 1：疲劳驾驶预警
    PreFatigueDriving(1 << 14),
    //18 1：当天累计驾驶超时
    AccumulatedDrivingOvertime(1 << 18),
    //19 1：超时停车
    OvertimeStop(1 << 19),
    //20 1：进出区域
    InOutArea(1 << 20),
    //21 1：进出路线
    InOutRoute(1 << 21),
    //22 1：路段行驶时间不足/过长
    DrivingTimeInsufficientOrLong(1 << 22),
    //23 1：路线偏离报警
    RouteDeviation(1 << 23),
    //24 1：车辆 VSS 故障
    VSS(1 << 24),
    //25 1：车辆油量异常
    Oil(1 << 25),
    //26 1：车辆被盗(通过车辆防盗器)
    Stolen(1 << 26),
    //27 1：车辆非法点火
    IllegalIgnition(1 << 27),
    //28 1：车辆非法位移
    IllegalMove(1 << 28),
    //29 1：碰撞预警
    Crush(1 << 29),
    //30 1：侧翻预警
    Rollover(1 << 30),
    //31 1：非法开门报警（终端未设置区域时，不判断非法开门）
    IllegalOpenDoor(1 << 31);

    private long state;

    AlarmTagState(long state) {
        this.state = state;
    }

    public static EnumSet<AlarmTagState> getStatusFlags(long statusValue) {
        EnumSet statusFlags = EnumSet.noneOf(AlarmTagState.class);
        for (AlarmTagState s : AlarmTagState.values()) {
            long flagValue = s.state;
            if ((flagValue & statusValue) == flagValue) {
                statusFlags.add(s);
            }
        }
        return statusFlags;
    }

    public static long getStatusValue(Set<AlarmTagState> flags) {
        long value = 0;
        for (AlarmTagState statusFlag : flags) {
            value |= statusFlag.state;
        }
        return value;
    }

    public static long status(AlarmTagState... args){
        long value = 0;
        for (AlarmTagState statusFlag : args) {
            value |= statusFlag.state;
        }
        return value;
    }
}
