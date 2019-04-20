package yction.com.vsicscomm.protocol.ips.cmd;

import java.util.HashMap;

import yction.com.vsicscomm.protocol.ByteBufferUnsigned;
import yction.com.vsicscomm.protocol.ips.PassthroughType;
import yction.com.vsicscomm.protocol.ips.DeviceId;
import yction.com.vsicscomm.protocol.p808.CmdResp;
import yction.com.vsicscomm.protocol.p808.MID;
import yction.com.vsicscomm.protocol.p808.Msg;

/**
 * 查询基本信息
 * 0x8900
 */
public class BasicInfoReq extends CmdResp {
    public PassthroughType PassthroughType;
    public int IdCount;
    public DeviceId[] deviceIds;

    public BasicInfoReq() {
        super(MID.P_Passthrough);
    }

    @Override
    public Msg onMsg(Msg msg) {
        ByteBufferUnsigned bb = new ByteBufferUnsigned(msg.body());
        PassthroughType = PassthroughType.valueOf(bb.getUnsignedByte());
        IdCount = bb.getUnsignedByte();
        deviceIds = new DeviceId[IdCount];
        for (int i = 0; i < IdCount; i++) {
            deviceIds[i] = DeviceId.valueOf(bb.getUnsignedByte());
        }

        // 获取设备信息状态 todo
        BasicInfo bir =
                BasicInfo.fromInfos(new HashMap<DeviceId, BasicInfo.Info>());
        return bir.msg();
    }
}
