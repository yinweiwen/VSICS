package yction.com.vsicscomm.protocol.p808;

public enum MID {
    C_Ack(0x0001, "终端通用应答"),
    C_Heart(0x0002, "终端心跳"),
    C_Registry(0x0100, "终端注册"),
    C_Logout(0x0003, "终端注销"),
    C_Auth(0x0102, "终端鉴权"),
    C_Ack_ReqParam(0x0104, "查询终端参数应答"),
    C_Ack_ReqProperty(0x0107, "查询终端属性应答"),
    C_Notify_Upgrade(0x0108, "终端升级结果通知"),
    C_Notify_Location(0x0200, "位置信息汇报"),
    C_Ack_ReqLocation(0x0201, "位置信息查询应答"),
    C_Notify_Event(0x0301, "事件报告"),
    C_Notify_Question(0x0302, "提问应答"),
    C_Notify_InfoOnDemand(0x0303, "信息点播/取消"),
    C_Ack_VehicleControl(0x0500, "车辆控制应答"),
    C_Notify_DrivingRecord(0x0700, "行驶记录仪数据上传"),
    C_Notify_WayBill(0x0701, "电子运单上报"),
    C_Notify_DriverIdentity(0x0702, "驾驶员身份信息采集上报"),
    C_Notify_LocationBatch(0x0704, "定位数据批量上传"),
    C_Notify_CAN(0x0705, "CAN 总线数据上传"),
    C_Notify_MediaEvent(0x0800, "多媒体事件信息上传"),
    C_Notify_Media(0x0801, "多媒体数据上传"),
    C_Ack_CameraShoot(0x0805, "摄像头立即拍摄命令应答"),
    C_Ack_MediaSearch(0x0802, "存储多媒体数据检索应答"),
    C_Notify_Passthrough(0x0900, "数据上行透传"),
    C_Notify_Compress(0x0901, "数据压缩上报"),
    C_RSA(0x0A00, "终端 RSA 公钥"),

    C_AlarmAttachmentInfo(0x1210,"报警附件信息上传"),
    C_AlarmFileInfo(0x1211,"文件信息上传"),
    C_AlarmFileUploadFinish(0x1212,"文件上传完成"),

    P_ACK(0x8001, "平台通用应答"),
    P_RESEND(0x8003, "补传分包请求"),
    P_ACK_Registry(0x8100, "终端注册应答"),
    P_SET_Param(0x8103, "设置终端参数"),
    P_GET_Param(0x8104, "查询终端参数"),
    P_Control(0x8105, "终端控制"),
    P_GET_SpecialParam(0x8106, "查询指定终端参数"),
    P_GET_Property(0x8107, "查询终端属性"),
    P_DIS_UpgradeFile(0x8108, "下发终端升级包"),
    P_GET_Location(0x8201, "位置信息查询"),
    P_SET_TmpLocation(0x8202, "临时位置跟踪控制"),
    P_Alarm_Confirm(0x8203, "人工确认报警消息"),
    P_DIS_Text(0x8300, "文本信息下发"),
    P_SET_Event(0x8301, "事件设置"),
    P_SET_Question(0x8302, "提问下发"),
    P_SET_InfoOnDemand(0x8303, "信息点播菜单设置"),
    P_Infomation(0x8304, "信息服务"),
    P_PhoneCallback(0x8400, "电话回拨"),
    P_SET_TelephoneBook(0x8401, "设置电话本"),
    P_CTL_Vehicle(0x8500, "车辆控制"),
    P_SET_RegionCicle(0x8600, "设置圆形区域"),
    P_DEL_RegionCicle(0x8601, "删除圆形区域"),
    P_SET_RegionRect(0x8602, "设置矩形区域"),
    P_DEL_RegionRect(0x8603, "删除矩形区域"),
    P_SET_RegionPolygon(0x8604, "设置多边形区域"),
    P_DEL_RegionPolygon(0x8605, "删除多边形区域"),
    P_SET_RegionRoute(0x8606, "设置路线"),
    P_DEL_RegionRoute(0x8607, "删除路线"),
    P_REQ_DrivingRecord(0x8700, "行驶记录仪数据采集命令"),
    P_SET_DrivingRecorder(0x8701, "行驶记录仪参数下传命令"),
    P_REQ_DriverIdentity(0x8702, "上报驾驶员身份信息请求"),
    P_ACK_MediaUpload(0x8800, "多媒体数据上传应答"),
    P_CTL_CameraShoot(0x8801, "摄像头立即拍摄命令"),
    P_REQ_MeidaSearch(0x8802, "存储多媒体数据检索"),
    P_REQ_MediaUpload(0x8803, "存储多媒体数据上传"),
    P_CTL_Recording(0x8804, "录音开始命令"),
    P_REQ_MeidaSearchUpload(0x8805, "单条存储多媒体数据检索上传命令"),
    P_Passthrough(0x8900, "数据下行透传"),
    P_RSA(0x8A00, "平台 RSA 公钥"),

    P_REQ_AlarmAttachUpload(0x9208,"报警附件上传"),
    P_ACK_AlarmFileUploadFinish(0x9212,"文件上传完成消息应答");

    private int code;
    private String name;

    MID(int code, String desc) {
        this.code = code;
        name = desc;
    }


    public static MID valueOf(int code) {
        for (MID x : MID.values()) {
            if (x.code == code)
                return x;
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return name;
    }
}
