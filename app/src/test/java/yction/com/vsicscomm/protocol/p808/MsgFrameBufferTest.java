package yction.com.vsicscomm.protocol.p808;

import org.junit.Test;

import java.util.List;

import yction.com.vsicscomm.protocol.p808.cmd.Registry;
import yction.com.vsicscomm.utils.Utils;

import static org.junit.Assert.*;

public class MsgFrameBufferTest {
    @Test
    public void tryDecode() throws Exception {
        byte[] buff = Utils.hexStringToByteArray("7e8100000a013300000001000100010030303030303030887e");
        List<MsgFrame> frames = MsgFrameBuffer.TryDecode(buff);
        System.out.println(frames.size());


        MsgBuffer _buffer = new MsgBuffer();

        Msg msg = _buffer.AddAndGet(frames.get(0));
        System.out.println(msg.toString());

        Registry reg = new Registry();
        reg.onMsg(msg);
        System.out.println(reg.ack.AuthToken);
        System.out.println(reg.ack.result);
    }

    @Test
    public void tryDecode2() throws Exception {
        byte[] buff = Utils.hexStringToByteArray("7e8100000a013300000001000100010030303030303030887e");
        List<MsgFrame> frames = MsgFrameBuffer.TryDecode(buff);
        System.out.println(frames.size());


        MsgBuffer _buffer = new MsgBuffer();

        Msg msg = _buffer.AddAndGet(frames.get(0));
        System.out.println(msg.toString());

        Registry reg = new Registry();
        reg.onMsg(msg);
        System.out.println(reg.ack.AuthToken);
        System.out.println(reg.ack.result);
    }
}