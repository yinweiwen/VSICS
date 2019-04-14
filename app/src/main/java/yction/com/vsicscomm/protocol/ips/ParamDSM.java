package yction.com.vsicscomm.protocol.ips;

import yction.com.vsicscomm.protocol.ByteBufferUnsigned;

/**
 * 表 4 - 11
 * 驾驶员状态监测系统参数
 */
public class ParamDSM {
    public short 报警判断速度阈值;
    public short 报警音量;
    public short 主动拍照策略;
    public int 主动定时拍照时间间隔;
    public int 主动定距拍照距离间隔;
    public short 单次主动拍照张数;
    public short 单次主动拍照时间间隔;
    public short 拍照分辨率;
    public short 视频录制分辨率;
    public long 报警使能;
    public long 事件使能;
    public int 吸烟报警判断时间间隔;
    public int 接打电话报警判断时间间隔;
    // 预留字段 3;
    public short 疲劳驾驶报警分级速度阈值;
    public short 疲劳驾驶报警前后视频录制时间;
    public short 疲劳驾驶报警拍照张数;
    public short 疲劳驾驶报警拍照间隔时间;
    public short 接打电话报警分级速度阈值;
    public short 接打电话报警前后视频录制时间;
    public short 接打电话报警拍驾驶员面部特征照片张数;
    public short 接打电话报警拍驾驶员面部特征照片间隔时间;
    public short 抽烟报警分级车速阈值;
    public short 抽烟报警前后视频录制时间;
    public short 抽烟报警拍驾驶员面部特征照片张数;
    public short 抽烟报警拍驾驶员面部特征照片间隔时间;
    public short 分神驾驶报警分级车速阈值;
    public short 分神驾驶报警前后视频录制时间;
    public short 分神驾驶报警拍照张数;
    public short 分神驾驶报警拍照间隔时间;
    public short 驾驶行为异常分级速度阈值;
    public short 驾驶行为异常视频录制时间;
    public short 驾驶行为异常抓拍照片张数;
    public short 驾驶行为异常拍照间隔;
    public short 驾驶员身份识别触发;
    // 保留字段 2;

    public ParamDSM() {
    }

    public ParamDSM(byte[] data) {
        ByteBufferUnsigned bb = new ByteBufferUnsigned(data);
        报警判断速度阈值 = bb.getUnsignedByte();
        报警音量 = bb.getUnsignedByte();
        主动拍照策略 = bb.getUnsignedByte();
        主动定时拍照时间间隔 = bb.getUnsignedShort();
        主动定距拍照距离间隔 = bb.getUnsignedShort();
        单次主动拍照张数 = bb.getUnsignedByte();
        单次主动拍照时间间隔 = bb.getUnsignedByte();
        拍照分辨率 = bb.getUnsignedByte();
        视频录制分辨率 = bb.getUnsignedByte();
        报警使能 = bb.getUnsignedInt();
        事件使能 = bb.getUnsignedInt();
        吸烟报警判断时间间隔 = bb.getUnsignedShort();
        接打电话报警判断时间间隔 = bb.getUnsignedShort();
        bb.raw().get();
        bb.raw().get();
        bb.raw().get();
        疲劳驾驶报警分级速度阈值 = bb.getUnsignedByte();
        疲劳驾驶报警前后视频录制时间 = bb.getUnsignedByte();
        疲劳驾驶报警拍照张数 = bb.getUnsignedByte();
        疲劳驾驶报警拍照间隔时间 = bb.getUnsignedByte();
        接打电话报警分级速度阈值 = bb.getUnsignedByte();
        接打电话报警前后视频录制时间 = bb.getUnsignedByte();
        接打电话报警拍驾驶员面部特征照片张数 = bb.getUnsignedByte();
        接打电话报警拍驾驶员面部特征照片间隔时间 = bb.getUnsignedByte();
        抽烟报警分级车速阈值 = bb.getUnsignedByte();
        抽烟报警前后视频录制时间 = bb.getUnsignedByte();
        抽烟报警拍驾驶员面部特征照片张数 = bb.getUnsignedByte();
        抽烟报警拍驾驶员面部特征照片间隔时间 = bb.getUnsignedByte();
        分神驾驶报警分级车速阈值 = bb.getUnsignedByte();
        分神驾驶报警前后视频录制时间 = bb.getUnsignedByte();
        分神驾驶报警拍照张数 = bb.getUnsignedByte();
        分神驾驶报警拍照间隔时间 = bb.getUnsignedByte();
        驾驶行为异常分级速度阈值 = bb.getUnsignedByte();
        驾驶行为异常视频录制时间 = bb.getUnsignedByte();
        驾驶行为异常抓拍照片张数 = bb.getUnsignedByte();
        驾驶行为异常拍照间隔 = bb.getUnsignedByte();
        驾驶员身份识别触发 = bb.getUnsignedByte();
    }

    public byte[] toBytes() {
        ByteBufferUnsigned bb = new ByteBufferUnsigned(49);
        bb.putUnsignedByte(报警判断速度阈值);
        bb.putUnsignedByte(报警音量);
        bb.putUnsignedByte(主动拍照策略);
        bb.putUnsignedShort(主动定时拍照时间间隔);
        bb.putUnsignedShort(主动定距拍照距离间隔);
        bb.putUnsignedByte(单次主动拍照张数);
        bb.putUnsignedByte(单次主动拍照时间间隔);
        bb.putUnsignedByte(拍照分辨率);
        bb.putUnsignedByte(视频录制分辨率);
        bb.putUnsignedInt(报警使能);
        bb.putUnsignedInt(事件使能);
        bb.putUnsignedShort(吸烟报警判断时间间隔);
        bb.putUnsignedShort(接打电话报警判断时间间隔);
        bb.putUnsignedByte(0);
        bb.putUnsignedByte(0);
        bb.putUnsignedByte(0);
        bb.putUnsignedByte(疲劳驾驶报警分级速度阈值);
        bb.putUnsignedByte(疲劳驾驶报警前后视频录制时间);
        bb.putUnsignedByte(疲劳驾驶报警拍照张数);
        bb.putUnsignedByte(疲劳驾驶报警拍照间隔时间);
        bb.putUnsignedByte(接打电话报警分级速度阈值);
        bb.putUnsignedByte(接打电话报警前后视频录制时间);
        bb.putUnsignedByte(接打电话报警拍驾驶员面部特征照片张数);
        bb.putUnsignedByte(接打电话报警拍驾驶员面部特征照片间隔时间);
        bb.putUnsignedByte(抽烟报警分级车速阈值);
        bb.putUnsignedByte(抽烟报警前后视频录制时间);
        bb.putUnsignedByte(抽烟报警拍驾驶员面部特征照片张数);
        bb.putUnsignedByte(抽烟报警拍驾驶员面部特征照片间隔时间);
        bb.putUnsignedByte(分神驾驶报警分级车速阈值);
        bb.putUnsignedByte(分神驾驶报警前后视频录制时间);
        bb.putUnsignedByte(分神驾驶报警拍照张数);
        bb.putUnsignedByte(分神驾驶报警拍照间隔时间);
        bb.putUnsignedByte(驾驶行为异常分级速度阈值);
        bb.putUnsignedByte(驾驶行为异常视频录制时间);
        bb.putUnsignedByte(驾驶行为异常抓拍照片张数);
        bb.putUnsignedByte(驾驶行为异常拍照间隔);
        bb.putUnsignedByte(驾驶员身份识别触发);
        bb.putUnsignedByte(0);
        bb.putUnsignedByte(0);
        return bb.raw().array();
    }
}
