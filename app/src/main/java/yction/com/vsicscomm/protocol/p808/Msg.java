package yction.com.vsicscomm.protocol.p808;

import android.annotation.SuppressLint;

import yction.com.vsicscomm.protocol.ByteBufferUnsigned;
import yction.com.vsicscomm.utils.Utils;

import java.nio.ByteBuffer;
import java.util.Date;

public class Msg {
    private int FI = 0;

    // 消息生成/接收到首包时间
    private long _time = new Date().getTime();

    // 消息帧数组
    private MsgFrame[] _frames;

    long getTime() {
        return _time;
    }

    // 消息方向 true-up(客户端->平台) false-down
    private boolean direction;

    /**
     * 消息构造函数
     *
     * @param frame  消息首帧
     * @param direct 方向 true-向上 false-向下
     */
    public Msg(MsgFrame frame, boolean direct) {
        direction = direct;
        int FC = frame.getHead().FC;
        if (frame.getHead().property.SP) {
            FI = frame.getHead().FI;
            _frames = new MsgFrame[FC];
        } else {
            FI = 1;
            _frames = new MsgFrame[1];
        }
        _frames[FI - 1] = frame;
    }

    /**
     * 消息构造函数2
     *
     * @param f 消息帧数组
     */
    public Msg(MsgFrame[] f) {
        _frames = f;
        direction = true;
    }

    public boolean add(MsgFrame frame) {
        FI = frame.getHead().FI;
        if (FI == 0) FI = 1;
        _frames[FI - 1] = frame;
        return true;
    }

    // 消息是否就绪
    public boolean isReady() {
        for (MsgFrame f : _frames) {
            if (f == null) return false;
        }
        return true;
    }

    public MsgFrame first() {
        return _frames[0];
    }

    public MsgFrame last() {
        return _frames[FI - 1];
    }

    public MsgFrame[] frames() {
        return _frames;
    }

    public int msgId() {
        return first().getHead().msgId;
    }

    public int msgNo() {
        return first().getHead().msgNo;
    }

    public byte[] body() {
        if (_frames == null || _frames.length == 0 || !isReady()) {
            return new byte[0];
        }
        if (_frames.length == 1) {
            return first().getBody();
        }
        int cap = 0;
        for (MsgFrame f : _frames) {
            cap += f.getBody().length;
        }
        ByteBuffer bb = ByteBuffer.allocate(cap);
        for (MsgFrame f :
                _frames) {
            bb.put(f.getBody());
        }
        return bb.array();
    }

    /**
     * 读取应答返回的第一个WORD
     * 应答流水号 WORD 对应请求消息的流水号
     *
     * @return 对应请求消息的流水号
     */
    public int readAckMsgNo() {
        try {
            ByteBufferUnsigned bb = new ByteBufferUnsigned(body());
            return bb.getUnsignedShort();
        } catch (Exception ex) {
            return -1;
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        try {
            return String.format("D:%s MID:%s MN:%s %s", direction ? "up" : "down",
                    Utils.int2HexString(msgId()),
                    Utils.int2HexString(msgNo()),
                    Utils.bytesToHexString(body()));
        } catch (Exception ex) {
            return "NA";
        }
    }
}
