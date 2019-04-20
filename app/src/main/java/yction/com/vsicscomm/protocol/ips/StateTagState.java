package yction.com.vsicscomm.protocol.ips;

import java.util.EnumSet;
import java.util.Set;

public enum StateTagState {
    //0 0：ACC 关；1： ACC 开
    ACC(1),
    //1 0：未定位；1：定位
    Location(1 << 1),
    //2 0：北纬；1：南纬
    NorthSouth(1 << 2),
    //3 0：东经；1：西经
    EastWest(1 << 3),
    //4 0：运营状态；1：停运状态
    OperationState(1 << 4),
    //5 0：经纬度未经保密插件加密；1：经纬度已经保密插件加密
    LongitudeLatitudeEncrypted(1 << 5),
    //8-9 00：空车；01：半载；10：保留；11：满载
    Load(1 << 8),
    //10 0：车辆油路正常；1：车辆油路断开
    Oil(1 << 10),
    //11 0：车辆电路正常；1：车辆电路断开
    Circuit(1 << 11),
    //12 0：车门解锁；1：车门加锁
    Lock(1 << 12),
    //13 0：门 1 关；1：门 1 开（前门）
    Door1(1 << 13),
    //14 0：门 2 关；1：门 2 开（中门）
    Door2(1 << 14),
    //15 0：门 3 关；1：门 3 开（后门）
    Door3(1 << 15),
    //16 0：门 4 关；1：门 4 开（驾驶席门）
    Door4(1 << 16),
    //17 0：门 5 关；1：门 5 开（自定义）
    Door5(1 << 17),
    //18 0：未使用 GPS 卫星进行定位；1：使用 GPS 卫星进行定位
    GPS(1 << 18),
    //19 0：未使用北斗卫星进行定位；1：使用北斗卫星进行定位
    Beidou(1 << 19),
    //20 0：未使用 GLONASS 卫星进行定位；1：使用 GLONASS 卫星进行定位
    GLONASS(1 << 20),
    //21 0：未使用 Galileo 卫星进行定位；1：使用 Galileo 卫星进行定位
    Galileo(1 << 21);


    private long state;

    StateTagState(long state) {
        this.state = state;
    }

    public static EnumSet<StateTagState> getStatusFlags(long statusValue) {
        EnumSet statusFlags = EnumSet.noneOf(StateTagState.class);
        for (StateTagState s : StateTagState.values()) {
            long flagValue = s.state;
            if ((flagValue & statusValue) == flagValue) {
                statusFlags.add(s);
            }
        }
        return statusFlags;
    }

    public static long getStatusValue(Set<StateTagState> flags) {
        long value = 0;
        for (StateTagState statusFlag : flags) {
            value |= statusFlag.state;
        }
        return value;
    }


    public static long status(StateTagState... args){
        long value = 0;
        for (StateTagState statusFlag : args) {
            value |= statusFlag.state;
        }
        return value;
    }
}
