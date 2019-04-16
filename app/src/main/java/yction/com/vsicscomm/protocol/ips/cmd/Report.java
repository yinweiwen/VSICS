package yction.com.vsicscomm.protocol.ips.cmd;

import java.nio.ByteBuffer;
import java.util.Date;

import yction.com.vsicscomm.protocol.ByteBufferUnsigned;
import yction.com.vsicscomm.protocol.ips.ReportComm;
import yction.com.vsicscomm.protocol.ips.ReportExtra;
import yction.com.vsicscomm.protocol.p808.CmdReq;
import yction.com.vsicscomm.protocol.p808.MID;
import yction.com.vsicscomm.protocol.p808.Msg;
import yction.com.vsicscomm.protocol.p808.Protocol;

/**
 * 位置信息汇报
 * 0x0200
 * 位置基本信息 位置附加信息项列表
 */
public class Report extends CmdReq {
    private ReportComm comm;
    private ReportExtra extra;

    public Report(ReportComm comm, ReportExtra extra) {
        super(MID.C_Notify_Location);
        this.comm = comm;
        this.extra = extra;
    }

    @Override
    protected byte[] toBytes() {
        int len;
        byte[] a, b = null;
        a = comm.toBytes();
        len = a.length;
        if (extra != null) {
            b = extra.getBytes();
            len += b.length;
        }
        ByteBuffer bb = ByteBuffer.allocate(len);
        bb.put(a);
        if (b != null) {
            bb.put((byte) extra.getId());
            bb.put((byte) b.length);
            bb.put(b);
        }
        return bb.array();
    }

    @Override
    protected void onMsg(Msg msg) {

    }
}
