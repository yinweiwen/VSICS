package yction.com.vsicscomm.protocol.p808;

/**
 * 消息体属性
 */
public class Property {
    // 分包状态
    public boolean SP;
    // 数据加密方式
    public int encrypt;
    // 消息体长度
    public int size;

    public Property(boolean sp, int enc, int s) {
        SP = sp;
        encrypt = enc;
        size = s;
    }

    public Property(byte[] buff) {
        SP = (buff[1] & 0x2000) != 0;
        encrypt = buff[1] & 0x1c00;
        size = buff[1] & 0x3ff;
    }

    public byte[] toBytes() {
        byte b = 0;
        if (SP) {
            b |= 1 << 13;
        }
        b |= (encrypt & 0x07) << 10;
        b |= (size & 0x03ff);
        return new byte[]{0x00, b};
    }
}
