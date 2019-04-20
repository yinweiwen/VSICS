package yction.com.vsicscomm;

import java.util.Date;

public class Global {

    // 分包大小
    public static final int FRAME_SIZE = 4096;

    // 数据加密方式
    public static int encrypt = 0;

    // 平台地址
    public static String host = "59.36.96.75";

    // 平台端口
    public static int port = 10001;

    // 终端手机号码
    public static String phone = "10059107644";

    // 终端ID
    public static String terminalId = "0000000";

    // 车牌号
    public static String vehicleIdentification = "粤S80999";


    // 系统当前流水号
    private static int SerialNumber = 0;

    /**
     * 自动生成流水号
     *
     * @return 流水号
     */
    public static synchronized int serialNo() {
        int n = SerialNumber + 1;
        if (n > 65535) n = 1;
        SerialNumber = n;
        return n;
    }

    /**
     * 自动生成告警ID
     *
     * @return 告警ID
     */
    public static synchronized long alarmNo() {
        return new Date().getTime();
    }
}
