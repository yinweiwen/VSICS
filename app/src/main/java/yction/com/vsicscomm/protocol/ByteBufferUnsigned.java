package yction.com.vsicscomm.protocol;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteBufferUnsigned {
    private ByteBuffer bb;

    public ByteBufferUnsigned(int length) {
        bb = ByteBuffer.allocate(length).order(ByteOrder.BIG_ENDIAN);
    }

    public ByteBufferUnsigned(byte[] buff) {
        bb = ByteBuffer.wrap(buff).order(ByteOrder.BIG_ENDIAN);
    }

    public ByteBuffer raw(){
        return bb;
    }

    // BYTE UNSIGNED
    // ---------------------------------------------------------------
    public short getUnsignedByte() {
        return ((short) (bb.get() & 0xff));
    }

    public void putUnsignedByte(int value) {
        bb.put((byte) (value & 0xff));
    }

    public short getUnsignedByte(int position) {
        return ((short) (bb.get(position) & (short) 0xff));
    }

    public void putUnsignedByte(int position, int value) {
        bb.put(position, (byte) (value & 0xff));
    }

    // WORD
    // ---------------------------------------------------------------

    public int getUnsignedShort() {
        return (bb.getShort() & 0xffff);
    }

    public void putUnsignedShort(int value) {
        bb.putShort((short) (value & 0xffff));
    }

    public int getUnsignedShort(int position) {
        return (bb.getShort(position) & 0xffff);
    }

    public void putUnsignedShort(int position, int value) {
        bb.putShort(position, (short) (value & 0xffff));
    }

    // DWORD
    // ---------------------------------------------------------------

    public long getUnsignedInt() {
        return ((long) bb.getInt() & 0xffffffffL);
    }

    public void putUnsignedInt(long value) {
        bb.putInt((int) (value & 0xffffffffL));
    }

    public long getUnsignedInt(int position) {
        return ((long) bb.getInt(position) & 0xffffffffL);
    }

    public void putUnsignedInt(int position, long value) {
        bb.putInt(position, (int) (value & 0xffffffffL));
    }
}
