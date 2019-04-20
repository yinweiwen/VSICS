package yction.com.vsicscomm.protocol.ips.cmd;

import java.nio.ByteBuffer;
import java.util.Map;

import yction.com.vsicscomm.protocol.ByteBufferUnsigned;
import yction.com.vsicscomm.protocol.ips.PassthroughType;
import yction.com.vsicscomm.protocol.ips.DeviceId;
import yction.com.vsicscomm.protocol.ips.PeripheralState;
import yction.com.vsicscomm.protocol.p808.CmdReq;
import yction.com.vsicscomm.protocol.p808.MID;

/**
 * 上传基本信息
 * 0x0900消息
 */
public class BasicInfo extends CmdReq {

    PassthroughType type;

    byte[] body;

    protected BasicInfo() {
        super(MID.C_Notify_Passthrough);
    }

    /**
     * 状态查询命令构造
     *
     * @param states 外设状态
     */
    public static BasicInfo fromStates(Map<DeviceId, State> states) {
        BasicInfo bi = new BasicInfo();
        bi.type = PassthroughType.State;

        ByteBuffer bb = ByteBuffer.allocate(40960);
        bb.put((byte) bi.type.getState());
        bb.put((byte) states.size());
        for (DeviceId pid : states.keySet()) {
            bb.put((byte) pid.getState());
            byte[] bts = states.get(pid).toBytes();
            bb.put((byte) bts.length);
            bb.put(bts);
        }
        int len = bb.position();
        bi.body = new byte[len];
        System.arraycopy(bb.array(), 0, bi.body, 0, len);
        return bi;
    }

    /**
     * 信息查询命令构造
     */
    public static BasicInfo fromInfos(Map<DeviceId, Info> infos) {
        BasicInfo bi = new BasicInfo();
        bi.type = PassthroughType.Info;
        ByteBuffer bb = ByteBuffer.allocate(40960);
        bb.put((byte) bi.type.getState());
        bb.put((byte) infos.size());
        for (DeviceId pid : infos.keySet()) {
            bb.put((byte) pid.getState());
            byte[] bts = infos.get(pid).toBytes();
            bb.put((byte) bts.length);
            bb.put(bts);
        }
        int len = bb.position();
        bi.body = new byte[len];
        System.arraycopy(bb.array(), 0, bi.body, 0, len);
        return bi;
    }

    @Override
    protected byte[] toBytes() {
        return body;
    }

    /**
     * 外设状态信息
     */
    public class State {
        private PeripheralState WorkState;
        private AlarmState AlarmState;

        public byte[] toBytes() {
            ByteBufferUnsigned bb = new ByteBufferUnsigned(5);
            bb.putUnsignedByte(WorkState.getState());
            bb.putUnsignedInt(AlarmState.toDword());
            return bb.raw().array();
        }
    }

    public class AlarmState {
        // bit0：摄像头异常
        public boolean camera;
        // bit1：主存储器异常
        public boolean main_mem;
        // bit2：辅存储器异常
        public boolean sec_mem;
        // bit3：红外补光异常
        public boolean infrared_light;
        // bit4：扬声器异常
        public boolean speaker;
        // bit5：电池异常
        public boolean battery;
        // bit10：通讯模块异常
        public boolean communication;
        // bit11：定位模块异常
        public boolean position;

        public long toDword() {
            long a = 0;
            if (camera) {
                a |= 1;
            }
            if (main_mem) {
                a |= 1 << 1;
            }
            if (sec_mem) {
                a |= 1 << 2;
            }
            if (infrared_light) {
                a |= 1 << 3;
            }
            if (speaker) {
                a |= 1 << 4;
            }
            if (battery) {
                a |= 1 << 5;
            }
            if (communication) {
                a |= 1 << 10;
            }
            if (position) {
                a |= 1 << 11;
            }
            return a;
        }

    }

    /**
     * 外设系统信息
     */
    public class Info {
        public String company;
        public String product_model;
        public String hardware_version;
        public String software_version;
        public String device_id;
        public String client_code;

        public byte[] toBytes() {
            byte[] cbs = company.getBytes();
            byte[] pbs = product_model.getBytes();
            byte[] hbs = hardware_version.getBytes();
            byte[] sbs = software_version.getBytes();
            byte[] dbs = device_id.getBytes();
            byte[] cibs = client_code.getBytes();
            int cnt = 6 + cbs.length + pbs.length
                    + hbs.length + sbs.length
                    + dbs.length + cibs.length;
            ByteBuffer bb = ByteBuffer.allocate(cnt);
            bb.put((byte) cbs.length);
            bb.put(cbs);
            bb.put((byte) pbs.length);
            bb.put(pbs);
            bb.put((byte) hbs.length);
            bb.put(hbs);
            bb.put((byte) sbs.length);
            bb.put(sbs);
            bb.put((byte) dbs.length);
            bb.put(dbs);
            bb.put((byte) cibs.length);
            bb.put(cibs);
            return bb.array();
        }
    }
}
