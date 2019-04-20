package yction.com.vsicscomm.protocol.p808.cmd;

import com.koushikdutta.async.Util;

import java.io.UnsupportedEncodingException;

import yction.com.vsicscomm.protocol.ByteBufferUnsigned;
import yction.com.vsicscomm.protocol.p808.CmdReq;
import yction.com.vsicscomm.protocol.p808.MID;
import yction.com.vsicscomm.protocol.p808.Msg;
import yction.com.vsicscomm.utils.Utils;

/**
 * 终端注册
 * 消息ID：0x0100。
 * 0 省域 ID WORD
 * 2  市县域 ID WORD
 * 4  制造商 ID BYTE[5]
 * 9  终端型号 BYTE[20]
 * 29 终端ID BYTE[7]
 * 36 车牌颜色 BYTE
 * 37 车辆标识 STRING
 */
public class Registry extends CmdReq {

    public Ack ack;

    private int province;
    private int city;
    private String manufacturerId;
    private String terminalModel;
    private String terminalId;
    private byte licenseColor;
    private String vehicleIdentification;

    /**
     * 终端注册命令构造函数
     *
     * @param province              省域
     * @param city                  市县域
     * @param manufacturerId        制造商
     * @param terminalModel         终端型号
     * @param terminalId            终端ID
     * @param licenseColor          车牌颜色
     * @param vehicleIdentification 车辆标识
     */
    public Registry(int province, int city, String manufacturerId,
                    String terminalModel, String terminalId,
                    byte licenseColor, String vehicleIdentification) {
        super(MID.C_Registry);
        this.province = province;
        this.city = city;
        this.manufacturerId = manufacturerId;
        this.terminalModel = terminalModel;
        this.terminalId = terminalId;
        this.licenseColor = licenseColor;
        this.vehicleIdentification = vehicleIdentification;
    }

    public Registry() {
        super(MID.C_Registry);
    }

    @Override
    protected byte[] toBytes() {
        byte[] sbts = Utils.getBytes(vehicleIdentification);
        ByteBufferUnsigned bb = new ByteBufferUnsigned(37 + sbts.length);
        bb.putUnsignedShort(province);
        bb.putUnsignedShort(city);
        bb.raw().put(Utils.getBytes(manufacturerId,5));
        bb.raw().put(Utils.getBytes(terminalModel,20));
        bb.raw().put(Utils.getBytes(terminalId,7));
        bb.raw().put(licenseColor);
        bb.raw().put(sbts);
        return bb.raw().array();
    }

    @Override
    public void onMsg(Msg msg) {
        try {
            byte[] bd = msg.body();
            ByteBufferUnsigned bb = new ByteBufferUnsigned(bd);
            bb.getUnsignedShort();
            Result res = Result.valueOf(bb.getUnsignedByte());
            byte[] lic = new byte[bd.length - 3];
            bb.raw().get(lic);
            String lics = new String(lic, "GBK");
            ack = new Ack();
            ack.AuthToken = lics;
            ack.result = res;
        } catch (Exception ex) {
            System.out.println("registry parse error:" + ex);
            ack = null;
        }
    }

    public class Ack {
        public Result result;
        public String AuthToken;
    }

    public enum Result {
        OK(0, "成功"),
        VehicleRegisted(1, "车辆已被注册"),
        NotMatchVehicle(2, "数据库中无该车辆"),
        TerminalRegisted(3, "终端已被注册"),
        NotMatchTerminal(4, "数据库中无该终端"),;

        private int code;
        private String name;

        Result(int code, String desc) {
            this.code = code;
            name = desc;
        }

        public static Result valueOf(int code) {
            for (Result x : Result.values()) {
                if (x.code == code)
                    return x;
            }
            return null;
        }
    }
}
