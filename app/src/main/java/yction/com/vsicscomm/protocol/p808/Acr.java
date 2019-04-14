package yction.com.vsicscomm.protocol.p808;

import java.nio.ByteBuffer;
import java.util.Map;

import yction.com.vsicscomm.protocol.AcrCode;
import yction.com.vsicscomm.protocol.ByteBufferUnsigned;
import yction.com.vsicscomm.protocol.p808.MID;
import yction.com.vsicscomm.protocol.p808.MsgFrame;
import yction.com.vsicscomm.protocol.p808.Protocol;

/**
 * Created by yww08 on 2019/3/24.
 * 说明:
 *
 * 应答答复类
 *
 * 弃用
 */

@Deprecated
public abstract class Acr {
    protected Msg msg;

    public Acr(Msg m) {
        msg = m;
    }

    public abstract MsgFrame[] toFrames();
}
