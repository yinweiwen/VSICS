package yction.com.vsicscomm.protocol.ips;

import java.nio.ByteBuffer;

import yction.com.vsicscomm.protocol.ByteBufferUnsigned;

/**
 * 表 4 - 10
 * 高级驾驶辅助系统参数
 */
public class ParamADAS {

    public short 报警判断速度阈值;
    public short 报警提示音量;
    public short 主动拍照策略;
    public int 主动定时拍照时间间隔;
    public int 主动定距拍照距离间隔;
    public short 单次主动拍照张数;
    public short 单次主动拍照时间间隔;
    public short 拍照分辨率;
    public short 视频录制分辨率;
    public long 报警使能;
    public long 事件使能;
    public short 预留字段;
    public short 障碍物报警距离阈值;
    public short 障碍物报警分级速度阈值;
    public short 障碍物报警前后视频录制时间;
    public short 障碍物报警拍照张数;
    public short 障碍物报警拍照间隔;
    public short 频繁变道报警判断时间段;
    public short 频繁变道报警判断次数;
    public short 频繁变道报警分级速度阈值;
    public short 频繁变道报警前后视频录制时间;
    public short 频繁变道报警拍照张数;
    public short 频繁变道报警拍照间隔;
    public short 车道偏离报警分级速度阈值;
    public short 车道偏离报警前后视频录制时间;
    public short 车道偏离报警拍照张数;
    public short 车道偏离报警拍照间隔;
    public short 前向碰撞报警时间阈值;
    public short 前向碰撞报警分级速度阈值;
    public short 前向碰撞报警前后视频录制时间;
    public short 前向碰撞报警拍照张数;
    public short 前向碰撞报警拍照间隔;
    public short 行人碰撞报警时间阈值;
    public short 行人碰撞报警使能速度阈值;
    public short 行人碰撞报警前后视频录制时间;
    public short 行人碰撞报警拍照张数;
    public short 行人碰撞报警拍照间隔;
    public short 车距监控报警距离阈值;
    public short 车距监控报警分级速度阈值;
    public short 车距过近报警前后视频录制时间;
    public short 车距过近报警拍照张数;
    public short 车距过近报警拍照间隔;
    public short 道路标志识别拍照张数;
    public short 道路标志识别拍照间隔;
    public short 保留字段1;
    public short 保留字段2;
    public short 保留字段3;
    public short 保留字段4;

    public ParamADAS(){}

    public ParamADAS(byte[] data){
        ByteBufferUnsigned bb = new ByteBufferUnsigned(data);
        报警判断速度阈值=bb.getUnsignedByte();
        报警提示音量=bb.getUnsignedByte();
        主动拍照策略=bb.getUnsignedByte();
        主动定时拍照时间间隔=bb.getUnsignedShort();
        主动定距拍照距离间隔=bb.getUnsignedShort();
        单次主动拍照张数=bb.getUnsignedByte();
        单次主动拍照时间间隔=bb.getUnsignedByte();
        拍照分辨率=bb.getUnsignedByte();
        视频录制分辨率=bb.getUnsignedByte();
        报警使能=bb.getUnsignedInt();
        事件使能=bb.getUnsignedInt();
        预留字段=bb.getUnsignedByte();
        障碍物报警距离阈值=bb.getUnsignedByte();
        障碍物报警分级速度阈值=bb.getUnsignedByte();
        障碍物报警前后视频录制时间=bb.getUnsignedByte();
        障碍物报警拍照张数=bb.getUnsignedByte();
        障碍物报警拍照间隔=bb.getUnsignedByte();
        频繁变道报警判断时间段=bb.getUnsignedByte();
        频繁变道报警判断次数=bb.getUnsignedByte();
        频繁变道报警分级速度阈值=bb.getUnsignedByte();
        频繁变道报警前后视频录制时间=bb.getUnsignedByte();
        频繁变道报警拍照张数=bb.getUnsignedByte();
        频繁变道报警拍照间隔=bb.getUnsignedByte();
        车道偏离报警分级速度阈值=bb.getUnsignedByte();
        车道偏离报警前后视频录制时间=bb.getUnsignedByte();
        车道偏离报警拍照张数=bb.getUnsignedByte();
        车道偏离报警拍照间隔=bb.getUnsignedByte();
        前向碰撞报警时间阈值=bb.getUnsignedByte();
        前向碰撞报警分级速度阈值=bb.getUnsignedByte();
        前向碰撞报警前后视频录制时间=bb.getUnsignedByte();
        前向碰撞报警拍照张数=bb.getUnsignedByte();
        前向碰撞报警拍照间隔=bb.getUnsignedByte();
        行人碰撞报警时间阈值=bb.getUnsignedByte();
        行人碰撞报警使能速度阈值=bb.getUnsignedByte();
        行人碰撞报警前后视频录制时间=bb.getUnsignedByte();
        行人碰撞报警拍照张数=bb.getUnsignedByte();
        行人碰撞报警拍照间隔=bb.getUnsignedByte();
        车距监控报警距离阈值=bb.getUnsignedByte();
        车距监控报警分级速度阈值=bb.getUnsignedByte();
        车距过近报警前后视频录制时间=bb.getUnsignedByte();
        车距过近报警拍照张数=bb.getUnsignedByte();
        车距过近报警拍照间隔=bb.getUnsignedByte();
        道路标志识别拍照张数=bb.getUnsignedByte();
        道路标志识别拍照间隔=bb.getUnsignedByte();
    }

    public byte[] toBytes() {
        ByteBufferUnsigned bb = new ByteBufferUnsigned(56);

        bb.putUnsignedByte(报警判断速度阈值);
        bb.putUnsignedByte(报警提示音量);
        bb.putUnsignedByte(主动拍照策略);
        bb.putUnsignedShort(主动定时拍照时间间隔);
        bb.putUnsignedShort(主动定距拍照距离间隔);
        bb.putUnsignedByte(单次主动拍照张数);
        bb.putUnsignedByte(单次主动拍照时间间隔);
        bb.putUnsignedByte(拍照分辨率);
        bb.putUnsignedByte(视频录制分辨率);
        bb.putUnsignedInt(报警使能);
        bb.putUnsignedInt(事件使能);
        bb.putUnsignedByte(预留字段);
        bb.putUnsignedByte(障碍物报警距离阈值);
        bb.putUnsignedByte(障碍物报警分级速度阈值);
        bb.putUnsignedByte(障碍物报警前后视频录制时间);
        bb.putUnsignedByte(障碍物报警拍照张数);
        bb.putUnsignedByte(障碍物报警拍照间隔);
        bb.putUnsignedByte(频繁变道报警判断时间段);
        bb.putUnsignedByte(频繁变道报警判断次数);
        bb.putUnsignedByte(频繁变道报警分级速度阈值);
        bb.putUnsignedByte(频繁变道报警前后视频录制时间);
        bb.putUnsignedByte(频繁变道报警拍照张数);
        bb.putUnsignedByte(频繁变道报警拍照间隔);
        bb.putUnsignedByte(车道偏离报警分级速度阈值);
        bb.putUnsignedByte(车道偏离报警前后视频录制时间);
        bb.putUnsignedByte(车道偏离报警拍照张数);
        bb.putUnsignedByte(车道偏离报警拍照间隔);
        bb.putUnsignedByte(前向碰撞报警时间阈值);
        bb.putUnsignedByte(前向碰撞报警分级速度阈值);
        bb.putUnsignedByte(前向碰撞报警前后视频录制时间);
        bb.putUnsignedByte(前向碰撞报警拍照张数);
        bb.putUnsignedByte(前向碰撞报警拍照间隔);
        bb.putUnsignedByte(行人碰撞报警时间阈值);
        bb.putUnsignedByte(行人碰撞报警使能速度阈值);
        bb.putUnsignedByte(行人碰撞报警前后视频录制时间);
        bb.putUnsignedByte(行人碰撞报警拍照张数);
        bb.putUnsignedByte(行人碰撞报警拍照间隔);
        bb.putUnsignedByte(车距监控报警距离阈值);
        bb.putUnsignedByte(车距监控报警分级速度阈值);
        bb.putUnsignedByte(车距过近报警前后视频录制时间);
        bb.putUnsignedByte(车距过近报警拍照张数);
        bb.putUnsignedByte(车距过近报警拍照间隔);
        bb.putUnsignedByte(道路标志识别拍照张数);
        bb.putUnsignedByte(道路标志识别拍照间隔);
        bb.putUnsignedByte(保留字段1);
        bb.putUnsignedByte(保留字段2);
        bb.putUnsignedByte(保留字段3);
        bb.putUnsignedByte(保留字段4);
        return bb.raw().array();
    }
}
