package yction.com.vsicscomm.protocol.ips;

import yction.com.vsicscomm.Global;
import yction.com.vsicscomm.protocol.p808.Protocol;
import yction.com.vsicscomm.utils.Utils;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.Date;

/**
 * 报警识别号定义见表4-16
 */
public class AlarmTag {
    // 7个字节，由大写字母和数字组成
    public String terminalId = Global.terminalId;
    // YY-MM-DD-hh-mm-ss （GMT+8时间）
    public Date date = new Date();
    // 同一时间点报警的序号，从0循环累加
    public byte num;
    // 附件数量
    public byte attachNum;

    public byte[] toBytes() {
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.put(Utils.getBytes(terminalId, 7));
        bb.put(Protocol.date2Bcd(date));
        bb.put(num);
        bb.put(attachNum);
        return bb.array();
    }

    public static AlarmTag fromBytes(byte[] data) throws ParseException {
        AlarmTag tag = new AlarmTag();
        ByteBuffer bb = ByteBuffer.wrap(data);
        byte[] tbts = new byte[7];
        bb.get(tbts);
        tag.terminalId = Utils.getString(tbts);
        byte[] dateBts = new byte[6];
        bb.get(dateBts);
        tag.date = Protocol.bcd2Date(dateBts);
        tag.num = bb.get();
        tag.attachNum = bb.get();
        return tag;
    }
}
