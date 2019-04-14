package yction.com.vsicscomm.protocol.p808;

import org.junit.Test;

import java.util.Objects;

import yction.com.vsicscomm.utils.Utils;

import static org.junit.Assert.*;

public class MsgFrameTest {
    @Test
    public void toBytes() throws Exception {
        MsgFrame[] frames = Protocol.encode(MID.C_Ack.getCode(), new byte[]{0x00, 0x01, 0x7d, 0x7e, 0x00});
        assert (frames.length == 1);
        assert (Objects.equals("7e00010005013300000001000100017d017d0200347e",
                Utils.bytesToHexString(frames[0].toBytes())));
    }

    @Test
    public void fromBytes() throws Exception {
        byte[] bs = Utils.hexStringToByteArray("7e00010005013300000001000100017d017d0200347e");
        MsgFrame frame = MsgFrame.fromBytes(bs);
        assert frame != null;
        Head hd = frame.getHead();
        assert (hd.msgId == MID.C_Ack.getCode());
        assert (hd.msgNo == 1);
        assert (Objects.equals(hd.phone, Protocol.phone));
        assert (!hd.property.SP);
        assert (hd.property.encrypt == Protocol.encrypt);
        assert (hd.property.size == 5);
        System.out.println(Utils.bytesToHexString(frame.getBody()));
    }

}