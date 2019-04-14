package yction.com.vsicscomm.protocol.p808;

import yction.com.vsicscomm.protocol.ByteBufferUnsigned;

import static yction.com.vsicscomm.protocol.p808.Protocol.bcd2Str;
import static yction.com.vsicscomm.protocol.p808.Protocol.str2Bcd;

/**
 * 2.2 消息头
 */
public class Head {
    // 普通消息包头长度
    private static final int LEN = 12;
    // 带分包消息的头长度
    private static final int LEN_WITH_SP = 16;

    // 消息ID
    public int msgId;
    // 消息体属性
    public Property property;
    // 终端手机号
    public String phone;
    // 消息流水号
    public int msgNo;
    // 消息包封装项-总包数
    public int FC;
    // 消息包封装项-包序号
    public int FI;

    public Head(int mi, Property prop, String ph, int mn, int fc, int fi) {
        msgId = mi;
        property = prop;
        phone = ph;
        msgNo = mn;
        FC = fc;
        FI = fi;
    }

    public Head(byte[] buff) {
        ByteBufferUnsigned bbu = new ByteBufferUnsigned(buff);
        msgId = bbu.getUnsignedShort();
        property = new Property(new byte[]{bbu.raw().get(), bbu.raw().get()});
        byte[] tmp = new byte[12];
        bbu.raw().get(tmp);
        String temp = bcd2Str(tmp);
        phone = temp.substring(0, 1).equalsIgnoreCase("0") ?
                temp.substring(1) : temp;
        msgNo = bbu.getUnsignedShort();
        if (property.SP) {
            FC = bbu.getUnsignedShort();
            FI = bbu.getUnsignedShort();
        }
    }

    public byte[] toBytes() {
        int len = size();
        ByteBufferUnsigned bbu = new ByteBufferUnsigned(len);
        bbu.putUnsignedShort(msgId);
        bbu.raw().put(property.toBytes());
        bbu.raw().put(str2Bcd(phone, 6));
        bbu.putUnsignedShort(msgNo);
        if (property.SP) {
            bbu.putUnsignedShort(FC);
            bbu.putUnsignedShort(FI);
        }
        return bbu.raw().array();
    }

    public int size() {
        if (property.SP) return LEN_WITH_SP;
        else return LEN;
    }
}
