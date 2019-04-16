package yction.com.vsicscomm.protocol.p808;

import yction.com.vsicscomm.Global;
import yction.com.vsicscomm.protocol.AcrCode;
import yction.com.vsicscomm.protocol.ByteBufferUnsigned;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/*
1. 数据类型:
    BYTE - 8 字节
    WORD - 16 字(2字节) unsigned short
    DWORD - 32 双字(4字节) unsigned long
    BYTE[n] - n 字节
    BCD[n] - 8421码 n字节
    STRING - GBK编码
    * 大端模式(big-endian)
2. 消息结构
    2.1 标志位
        0x7e
        非标志位处转义 0x7e -> 0x7d 0x02  0x7d -> 0x7d 0x01, 填充校验码后转义
    2.2 消息头 12~16字节
        消息ID    消息体属性   终端手机号   消息流水号   消息包封装项(分包)
        WORD      WORD       BCD[6]      WORD        WORD 总包数 WORD 包序号(1+)
        消息体属性:
        bit   15 14   13        12 11 10     9 8 7 6 5 4 3 2 1 0
              保留    分包0/1    数据加密方式   消息体长度
    2.3 消息体
    2.4 检验码
        校验码指从消息头开始，同后一字节异或，直到校验码前一个字节，占用一个字节
    2.5 标志位

3. 数据格式
    终端通用应答
    消息ID：0x0001 消息体: 流水号 + 应答的消息ID + 结果 (0：成功/确认；1：失败；2：消息有误；3：不支持)
    终端心跳
    消息ID:0x0002  消息体: 空
    补传分包请求
    消息ID：0x8003 消息体： 原始消息流水号 + 重传包数n(Byte) + 重传ID列表(Byte[2*n])
    终端注册
    消息ID：0x0100 消息体： / 应答： 0x8100  流水号 + 结果(0成功) + 鉴权码
    终端注销
    消息ID:0x0003  消息体: 空
    终端鉴权
    消息ID:0x0102  消息体: 鉴权码
 */
public class Protocol {

    public static final byte F_MARK = 0x7e;

    // 协议最小长度
    public static final int LEAST_LENGTH = 15;

    // 分包大小
    private static final int FRAME_SIZE = 4096;

    // 数据加密方式
    public static int encrypt = 0;

    // 终端手机号码
    public static String phone = "13300000001";

    /**
     * 心跳包构造
     *
     * @return 心跳包
     */
    public static MsgFrame heartbeat() {
        Head head = new Head(MID.C_Heart.getCode(),
                new Property(false, encrypt, 0), phone, 0, 0, 0);
        return new MsgFrame(head, new byte[0]);
    }

    /**
     * 消息帧构造
     *
     * @param msgId 消息ID
     * @param data  消息体内容
     * @return 消息帧封装
     */
    public static MsgFrame[] encode(int msgId, byte[] data) {
        List<byte[]> contents = wrap(data);
        int len = contents.size();
        boolean sp = len > 1;
        int sn = Global.serialNo();
        int fi = 1;
        MsgFrame[] res = new MsgFrame[len];
        for (byte[] content : contents) {
            Head head = new Head(msgId, new Property(sp, encrypt, content.length), phone, sn, len, fi);
            res[fi - 1] = new MsgFrame(head, content);
            fi++;
        }
        return res;
    }

    // 通用应答验证
    public static AcrCode checkCommAck(Msg send, Msg recv) {
        if (recv.msgId() == MID.P_ACK.getCode()) {
            ByteBufferUnsigned buff = new ByteBufferUnsigned(recv.body());
            int mn = buff.getUnsignedShort();
            int mid = buff.getUnsignedShort();
            int res = buff.getUnsignedByte();
            if (mn != send.msgNo() || mid != send.msgId()) {
                return AcrCode.ErrCmd;
            }
            return AcrCode.valueOf(res);
        }
        System.out.println("当前为非通用应答");
        return AcrCode.ErrCmd;
    }

    // 客户端通用应答
    public static Msg commAck(Msg msg, AcrCode ac) {
        ByteBufferUnsigned bb = new ByteBufferUnsigned(5);
        bb.putUnsignedShort(msg.msgNo());
        bb.putUnsignedShort(msg.msgId());
        bb.putUnsignedByte(ac.getCode());
        MsgFrame[] frames = Protocol.encode(MID.C_Ack.getCode(), bb.raw().array());
        return new Msg(frames);
    }

    private static int indexOf(byte[] data, int start) {
        int idx = -1;
        int len = data.length;
        for (int i = start; i < len; i++) {
            if (data[i] == F_MARK) {
                idx = i;
                break;
            }
        }
        return idx;
    }


    private static List<byte[]> wrap(byte[] data) {
        List<byte[]> res = new ArrayList<>();
        int pkgcnt = (int) Math.ceil(data.length * 1.0 / FRAME_SIZE);
        int index = 1;
        while (index <= pkgcnt) {
            int len = FRAME_SIZE;
            if (index == pkgcnt) {
                len = data.length - FRAME_SIZE * (index - 1);
            }
            byte[] as = new byte[len];
            System.arraycopy(data, (index - 1) * FRAME_SIZE, as, 0, len);
            res.add(as);
            index++;
        }
        return res;
    }

    /**
     * 异或校验
     *
     * @param buff 待校验字节数组
     * @return 校验计算结果
     */
    public static byte xor(byte[] buff) {
        return xor(buff, 0, buff.length - 1);
    }

    /**
     * 异或校验
     *
     * @param buff  带校验字节数组
     * @param start 开始索引（包含）
     * @param end   结束索引(包含)
     * @return 校验计算结果
     */
    public static byte xor(byte[] buff, int start, int end) {
        byte temp = buff[start];

        for (int i = start + 1; i <= end; i++) {
            temp ^= buff[i];
        }
        return temp;
    }

    /**
     * 字节转义
     *
     * @param buff 原始字节数组
     * @return 转义后的字节数组
     */
    public static byte[] escape(byte[] buff) {
        List<Byte> a = new ArrayList<>();
        for (byte b : buff) {
            if (b == 0x7e) {
                a.add((byte) 0x7d);
                a.add((byte) 0x02);
            } else if (b == 0x7d) {
                a.add((byte) 0x7d);
                a.add((byte) 0x01);
            } else {
                a.add(b);
            }
        }
        byte[] res = new byte[a.size()];
        int i = 0;
        for (Byte anA : a) {
            res[i] = anA;
            i++;
        }
        return res;
    }

    /**
     * 逃逸字符反转义
     *
     * @param buff 原始字节数组
     * @return 反转义后的字节数组
     */
    public static byte[] unescape(byte[] buff) {
        List<Byte> a = new ArrayList<>();
        int i = 0;
        while (i < buff.length) {
            if (buff[i] == 0x7d && i + 1 < buff.length) {
                if (buff[i + 1] == 0x01) {
                    a.add((byte) 0x7d);
                    i += 1;
                } else if (buff[i + 1] == 0x02) {
                    a.add((byte) 0x7e);
                    i += 1;
                } else {
                    a.add(buff[i]);
                }
            } else {
                a.add(buff[i]);
            }
            i += 1;
        }
        byte[] res = new byte[a.size()];
        i = 0;
        for (Byte anA : a) {
            res[i] = anA;
            i++;
        }
        return res;
    }

    /**
     * BCD码转10进制字符串
     */
    public static String bcd2Str(byte[] bytes) {
        StringBuilder temp = new StringBuilder(bytes.length * 2);
        for (byte aByte : bytes) {
            temp.append((byte) ((aByte & 0xf0) >>> 4));
            temp.append((byte) (aByte & 0x0f));
        }
        return temp.toString();
    }

    public static byte[] date2Bcd(Date date) {
        SimpleDateFormat bjSdf = new SimpleDateFormat("yyMMddHHmmss");
        bjSdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String str = bjSdf.format(date);
        return str2Bcd(str, 6);
    }

    public static Date bcd2Date(byte[] data) throws ParseException {
        String str = bcd2Str(data);
        SimpleDateFormat bjSdf = new SimpleDateFormat("yyMMddHHmmss");
        bjSdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return bjSdf.parse(str);
    }

    /**
     * 10进制串转为BCD码
     *
     * @param asc    待编码字符串
     * @param length BCD码长度(字符串长度不足2倍时左侧补齐0)
     * @return BCD码
     */
    public static byte[] str2Bcd(String asc, int length) {
        int strLen = length * 2;
        if (asc.length() > strLen) {
            asc = asc.substring(0, strLen);
        } else {
            StringBuilder ascBuilder = new StringBuilder(asc);
            while (ascBuilder.length() < strLen) {
                ascBuilder.insert(0, "0");
            }
            asc = ascBuilder.toString();
        }
        byte bbt[] = new byte[length];
        byte[] abt = asc.getBytes();
        int j, k;
        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }
            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }
}
