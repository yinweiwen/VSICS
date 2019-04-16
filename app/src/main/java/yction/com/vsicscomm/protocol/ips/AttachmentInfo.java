package yction.com.vsicscomm.protocol.ips;

import yction.com.vsicscomm.protocol.ByteBufferUnsigned;
import yction.com.vsicscomm.utils.Utils;

public class AttachmentInfo {
    public String filename;
    public long filesize;

    public AttachmentInfo() {
    }

    public AttachmentInfo(String fn, long size) {
        filename = fn;
        filesize = size;
    }

    public byte[] toBytes() {
        byte[] fnbs = Utils.getBytes(filename);
        ByteBufferUnsigned bb = new ByteBufferUnsigned(fnbs.length + 5);
        bb.raw().put((byte) fnbs.length);
        bb.raw().put(fnbs);
        bb.putUnsignedInt(filesize);
        return bb.raw().array();
    }
}
