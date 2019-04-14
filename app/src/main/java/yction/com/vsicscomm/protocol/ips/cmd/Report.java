package yction.com.vsicscomm.protocol.ips.cmd;

import java.nio.ByteBuffer;
import java.util.Date;

import yction.com.vsicscomm.protocol.ByteBufferUnsigned;
import yction.com.vsicscomm.protocol.ips.ReportComm;
import yction.com.vsicscomm.protocol.ips.ReportExtra;
import yction.com.vsicscomm.protocol.p808.CmdReq;
import yction.com.vsicscomm.protocol.p808.MID;
import yction.com.vsicscomm.protocol.p808.Msg;
import yction.com.vsicscomm.protocol.p808.Protocol;

/**
 * 位置信息汇报
 * 0x0200
 * 位置基本信息 位置附加信息项列表
 */
public class Report extends CmdReq {
    public Report(ReportComm comm, ReportExtra extra) {
        super(MID.C_Notify_Location);
        int len;
        byte[] a, b = null;
        a = comm.toBytes();
        len = a.length;
        if (extra != null) {
            b = extra.getBytes();
            len += b.length;
        }
        ByteBuffer bb = ByteBuffer.allocate(len);
        bb.put(a);
        if (b != null) {
            bb.put(b);
        }
        _body = bb.array();
    }

    @Override
    protected void onMsg(Msg msg) {

    }

    public class StateTag {
        //0 0：ACC 关；1： ACC 开
        public static final int ACC = 1;
        //1 0：未定位；1：定位
        public static final int Location = 1 << 1;
        //2 0：北纬；1：南纬
        public static final int NorthSouth = 1 << 2;
        //3 0：东经；1：西经
        public static final int EastWest = 1 << 3;
        //4 0：运营状态；1：停运状态
        public static final int OperationState = 1 << 4;
        //5 0：经纬度未经保密插件加密；1：经纬度已经保密插件加密
        public static final int LongitudeLatitudeEncrypted = 1 << 5;
        //8-9 00：空车；01：半载；10：保留；11：满载
        public static final int Load = 1 << 8;
        //10 0：车辆油路正常；1：车辆油路断开
        public static final int Oil = 1 << 10;
        //11 0：车辆电路正常；1：车辆电路断开
        public static final int Circuit = 1 << 11;
        //12 0：车门解锁；1：车门加锁
        public static final int Lock = 1 << 12;
        //13 0：门 1 关；1：门 1 开（前门）
        public static final int Door1 = 1 << 13;
        //14 0：门 2 关；1：门 2 开（中门）
        public static final int Door2 = 1 << 14;
        //15 0：门 3 关；1：门 3 开（后门）
        public static final int Door3 = 1 << 15;
        //16 0：门 4 关；1：门 4 开（驾驶席门）
        public static final int Door4 = 1 << 16;
        //17 0：门 5 关；1：门 5 开（自定义）
        public static final int Door5 = 1 << 17;
        //18 0：未使用 GPS 卫星进行定位；1：使用 GPS 卫星进行定位
        public static final int GPS = 1 << 18;
        //19 0：未使用北斗卫星进行定位；1：使用北斗卫星进行定位
        public static final int Beidou = 1 << 19;
        //20 0：未使用 GLONASS 卫星进行定位；1：使用 GLONASS 卫星进行定位
        public static final int GLONASS = 1 << 20;
        //21 0：未使用 Galileo 卫星进行定位；1：使用 Galileo 卫星进行定位
        public static final int Galileo = 1 << 21;
    }

    public class AlarmTag {
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
}
