package yction.com.vsicscomm.protocol.ips;

public class AlarmTagState {
    // 0 1：紧急报警，触动报警开关后触发
    public static final int Emergency = 1;
    //1 1：超速报警
    public static final int Overspeed = 1 << 1;
    //2 1：疲劳驾驶
    public static final int FatigueDriving = 1 << 2;
    //3 1：危险预警
    public static final int Danger = 1 << 3;
    //4 1：GNSS 模块发生故障
    public static final int GNSSFailure = 1 << 4;
    //5 1：GNSS 天线未接或被剪断
    public static final int GNNSAntennaOff = 1 << 5;
    //6 1：GNSS 天线短路
    public static final int GNNSAntennaShort = 1 << 6;
    //7 1：终端主电源欠压
    public static final int Undervoltage = 1 << 7;
    //8 1：终端主电源掉电
    public static final int PowerFault = 1 << 8;
    //9 1：终端 LCD 或显示器故障
    public static final int DisplayFault = 1 << 9;
    //10 1：TTS 模块故障
    public static final int TTS = 1 << 10;
    //11 1：摄像头故障
    public static final int Camera = 1 << 11;
    //12 1：道路运输证 IC 卡模块故障
    public static final int TransportCID = 1 << 12;
    //13 1：超速预警
    public static final int PreOverspeed = 1 << 13;
    //14 1：疲劳驾驶预警
    public static final int PreFatigueDriving = 1 << 14;
    //18 1：当天累计驾驶超时
    public static final int AccumulatedDrivingOvertime = 1 << 18;
    //19 1：超时停车
    public static final int OvertimeStop = 1 << 19;
    //20 1：进出区域
    public static final int InOutArea = 1 << 20;
    //21 1：进出路线
    public static final int InOutRoute = 1 << 21;
    //22 1：路段行驶时间不足/过长
    public static final int DrivingTimeInsufficientOrLong = 1 << 22;
    //23 1：路线偏离报警
    public static final int RouteDeviation = 1 << 23;
    //24 1：车辆 VSS 故障
    public static final int VSS = 1 << 24;
    //25 1：车辆油量异常
    public static final int Oil = 1 << 25;
    //26 1：车辆被盗(通过车辆防盗器)
    public static final int Stolen = 1 << 26;
    //27 1：车辆非法点火
    public static final int IllegalIgnition = 1 << 27;
    //28 1：车辆非法位移
    public static final int IllegalMove = 1 << 28;
    //29 1：碰撞预警
    public static final int Crush = 1 << 29;
    //30 1：侧翻预警
    public static final int Rollover = 1 << 30;
    //31 1：非法开门报警（终端未设置区域时，不判断非法开门）
    public static final int IllegalOpenDoor = 1 << 31;
}
