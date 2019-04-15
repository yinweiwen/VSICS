package yction.com.vsicscomm.protocol.p808;

import java.nio.ByteBuffer;

import static yction.com.vsicscomm.utils.Utils.cat;
import static yction.com.vsicscomm.utils.Utils.sub;

/**
 * 消息组成的一包
 */
public class MsgFrame {
    private Head _head;
    private byte[] _body;

    public MsgFrame(Head h, byte[] b) {
        _head = h;
        _body = b;
    }

    public Head getHead() {
        return _head;
    }

    public byte[] getBody() {
        return _body;
    }

    public boolean isOk() {
        return true;
    }

    public byte[] toBytes() {
        byte[] hs = _head.toBytes();
        ByteBuffer bb = ByteBuffer.allocate(hs.length + _body.length);
        bb.put(hs);
        bb.put(_body);
        byte xor = Protocol.xor(bb.array());
        ByteBuffer bb2 = ByteBuffer.allocate(hs.length + _body.length + 1);
        bb2.put(bb.array());
        bb2.put(xor);
        byte[] bd = Protocol.escape(bb2.array());
        ByteBuffer bb3 = ByteBuffer.allocate(bd.length + 2);
        bb3.put(Protocol.F_MARK);
        bb3.put(bd);
        bb3.put(Protocol.F_MARK);
        return bb3.array();
    }

    public static MsgFrame fromBytes(byte[] data) {
        if (data[0] != Protocol.F_MARK || data[data.length - 1] != Protocol.F_MARK) {
            System.out.println("标志位错误");
            return null;
        }
        try {
            byte[] udata = Protocol.unescape(data);
            if (udata == null || udata.length < Protocol.LEAST_LENGTH) {
                System.out.println("消息长度错误");
                return null;
            }
            byte[] tmp = sub(udata, 1);
            Head head = new Head(tmp);
            if (head.property.size != udata.length - 3 - head.size()) {
                System.out.println("消息长度与定义不符");
                return null;
            }
            byte[] body = new byte[head.property.size];
            System.arraycopy(udata, 1 + head.size(), body, 0, body.length);
            byte x = Protocol.xor(udata, 1, head.size() + body.length);
            if (x != udata[head.size() + body.length + 1]) {
                System.out.println("校验码错误");
                return null;
            }
            return new MsgFrame(head, body);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
