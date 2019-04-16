package yction.com.vsicscomm;

public class Global {


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

    private static long AlarmIdNumber = 0;

    /**
     * 自动生成告警ID
     *
     * @return 告警ID
     */
    public static synchronized long alarmNo() {
        return ++AlarmIdNumber;
    }
}
