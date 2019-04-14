package yction.com.vsicscomm.protocol;

public enum AcrCode {
    Success(0, "成功/确认"),
    Error(1, "失败"),
    ErrCmd(2, "消息有误"),
    UnSupport(3, "不支持"),
    AlarmConfirm(4, "报警处理确认");

    private int state;
    private String name;

    AcrCode(int code, String desc) {
        state = code;
        name = desc;
    }

    public static AcrCode valueOf(int code) {
        for (AcrCode x : AcrCode.values()) {
            if (x.state == code)
                return x;
        }
        return null;
    }

    public int getCode() {
        return state;
    }

    public String getDesc() {
        return name;
    }
}
