package yction.com.vsicscomm.protocol.ips.upload;

import yction.com.vsicscomm.protocol.ByteBufferUnsigned;
import yction.com.vsicscomm.utils.Utils;

public class FileFrame {

    public static byte[] encode(String filename, long offset, byte[] data) {
        ByteBufferUnsigned bb = new ByteBufferUnsigned(62 + data.length);
        bb.raw().put(new byte[]{0x30, 0x31, 0x63, 0x64});
        bb.raw().put(Utils.getBytes(filename, 50));
        bb.putUnsignedInt(offset);
        bb.putUnsignedInt(data.length);
        bb.raw().put(data);
        return bb.raw().array();
    }
}
