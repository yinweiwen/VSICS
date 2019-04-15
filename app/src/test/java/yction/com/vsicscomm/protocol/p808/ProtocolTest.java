package yction.com.vsicscomm.protocol.p808;

import com.koushikdutta.async.Util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import yction.com.vsicscomm.utils.Utils;

public class ProtocolTest {
    @Test
    public void heartbeat() throws Exception {
        MsgFrame frame = Protocol.heartbeat();
        System.out.println(Utils.bytesToHexString(frame.toBytes()));
    }

    @Test
    public void encode() throws Exception {
        MsgFrame[] frames = Protocol.encode(MID.C_Ack.getCode(), new byte[]{0x00, 0x01, 0x7d, 0x7e, 0x00});
        assert (frames.length == 1);
        System.out.println(Utils.bytesToHexString(frames[0].toBytes()));
    }

    @Test
    public void xor() throws Exception {
        byte[] bs = new byte[]{0x11, 0x22, 0x33, 0x44};
        byte b = Protocol.xor(bs);
        Assert.assertEquals(0x44, b);
    }

    @Test
    public void xor1() throws Exception {
        byte[] bs = new byte[]{0x02, 0x11, 0x22, 0x33, 0x44, 0x55};
        byte b = Protocol.xor(bs, 1, 4);
        Assert.assertEquals(0x44, b);
    }

    @Test
    public void escape() throws Exception {
        byte[] bs = new byte[]{0x11, 0x22, 0x7d, 0x44, 0x7e};
        byte[] bn = Protocol.escape(bs);
        Assert.assertArrayEquals(new byte[]{0x11, 0x22, 0x7d, 0x01, 0x44, 0x7d, 0x02}, bn);
    }

    @Test
    public void unescape() throws Exception {
        byte[] bs = new byte[]{0x11, 0x22, 0x7d, 0x01, 0x44, 0x7d, 0x02};
        byte[] bn = Protocol.unescape(bs);
        Assert.assertArrayEquals(new byte[]{0x11, 0x22, 0x7d, 0x44, 0x7e}, bn);
    }

    @Test
    public void bcd2Str() throws Exception {
        byte[] bs = new byte[]{0x01, 0x38, 0x00, 0x00, 0x00, 0x01};
        String s = Protocol.bcd2Str(bs);
        String se = "13800000001";
        Assert.assertEquals(se, s);
    }

    @Test
    public void str2Bcd() throws Exception {
        String s = "13800000001";
        byte[] ts = Protocol.str2Bcd(s, 6);
        System.out.println(Utils.bytesToHexString(ts));
        byte[] bs = new byte[]{0x01, 0x38, 0x00, 0x00, 0x00, 0x01};
        Assert.assertArrayEquals(bs, ts);
    }

    @Test
    public void date2Bcd(){
        Date date=new Date();
        byte[] ts=Protocol.date2Bcd(date);
        System.out.println(Utils.bytesToHexString(ts));

    }

    @Test
    public void paramQuery(){
        String s="7e810600140133000000010002010000ff005c7e";
        byte[] data= Utils.hexStringToByteArray(s);
        MsgFrame frame=MsgFrame.fromBytes(data);
        System.out.println(new Msg(frame,false));
    }

}