package yction.com.vsicscomm.protocol;

public enum Errors {

    Success(0x0000, "成功"),
    Default(0x0001, "未知错误"),
    NoMatch(0x0002, "不匹配"),
    Timeout(0x0004, "超时"),
    Busy(0x0008, "设备忙");

    private int mState = 0;
    private String mDesc = "";

    Errors(int value, String desc) {
        mState = value;
        mDesc = desc;
    }

    public String getDesc() {
        return mDesc;
    }

    public int getmState() {
        return mState;
    }
}
