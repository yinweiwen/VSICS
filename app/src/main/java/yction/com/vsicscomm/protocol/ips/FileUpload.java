package yction.com.vsicscomm.protocol.ips;

import yction.com.vsicscomm.protocol.ByteBufferUnsigned;
import yction.com.vsicscomm.utils.Utils;

import java.text.ParseException;

/**
 * 表 4 21文件上传指令数据格式
 */
public class FileUpload {
    public String ip;
    public int tcpPort;
    public int udpPort;
    public AlarmTag alarmTag = new AlarmTag();
    public String alarmId;

    public static FileUpload fromBytes(byte[] data) throws ParseException {
        FileUpload fu = new FileUpload();
        ByteBufferUnsigned bb = new ByteBufferUnsigned(data);
        byte len = bb.raw().get();
        byte[] ipbts = new byte[len];
        bb.raw().get(ipbts);
        fu.ip = Utils.getString(ipbts);
        fu.tcpPort = bb.getUnsignedShort();
        fu.udpPort = bb.getUnsignedShort();
        byte[] at = new byte[16];
        bb.raw().get(at);
        fu.alarmTag = AlarmTag.fromBytes(at);
        byte[] abts = new byte[32];
        bb.raw().get(abts);
        fu.alarmId = Utils.getString(abts);
        return fu;
    }

    @Override
    public String toString() {
        return String.format("ip:%s,tcp:%d,udp:%d", ip, tcpPort, udpPort);
    }
}
