package yction.com.vsicscomm.protocol.p808;

import java.util.ArrayList;
import java.util.List;

import static yction.com.vsicscomm.protocol.p808.Protocol.F_MARK;
import static yction.com.vsicscomm.utils.Utils.cat;
import static yction.com.vsicscomm.utils.Utils.sub;

/**
 * 消息帧缓存处理类
 * 静态类
 */
public class MsgFrameBuffer {

    private static byte[] _cache;

    //  处理tcp分包连包
    public static synchronized List<MsgFrame> TryDecode(byte[] buff) {
        List<MsgFrame> msgs = new ArrayList<>();
        if (_cache != null) {
            buff = cat(_cache, buff);
            _cache = null;
        }
        int index = 0;
        while (index < buff.length) {
            // head index
            int hi = iof(buff, index);
            if (hi != -1) {
                // tail index
                int ti = iof(buff, index + 1);
                if (ti != -1) {
                    if (ti - hi >= Protocol.LEAST_LENGTH - 1) {
                        MsgFrame msg = MsgFrame.fromBytes(sub(buff, hi, ti));
                        if (msg != null)
                            msgs.add(msg);
                        index = ti + 1;
                    } else {
                        index = ti;
                    }
                } else {
                    _cache = sub(buff, hi, buff.length - 1);
                    break;
                }
            } else break;
        }
        return msgs;
    }

    // index of flag
    private static int iof(byte[] data, int start) {
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
}
