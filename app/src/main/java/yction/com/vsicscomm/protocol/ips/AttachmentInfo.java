package yction.com.vsicscomm.protocol.ips;

import yction.com.vsicscomm.protocol.ByteBufferUnsigned;
import yction.com.vsicscomm.utils.Utils;

/**
 * 附属 0x1210 报警附件信息
 * 附件信息
 */
public class AttachmentInfo {
    // 文件名称
    public String filename;
    // 文件大小
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
