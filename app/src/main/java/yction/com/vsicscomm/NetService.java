package yction.com.vsicscomm;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.CircularArray;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import yction.com.vsicscomm.protocol.Errors;
import yction.com.vsicscomm.protocol.ips.AlarmADAS;
import yction.com.vsicscomm.protocol.ips.AlarmADASType;
import yction.com.vsicscomm.protocol.ips.AlarmBSD;
import yction.com.vsicscomm.protocol.ips.AlarmBSDType;
import yction.com.vsicscomm.protocol.ips.AlarmDSM;
import yction.com.vsicscomm.protocol.ips.AlarmDSMType;
import yction.com.vsicscomm.protocol.ips.AlarmTPMS;
import yction.com.vsicscomm.protocol.ips.AlarmTPMSItem;
import yction.com.vsicscomm.protocol.ips.AlarmTPMSType;
import yction.com.vsicscomm.protocol.ips.AttachmentInfo;
import yction.com.vsicscomm.protocol.ips.AttachmentType;
import yction.com.vsicscomm.protocol.ips.DeviceId;
import yction.com.vsicscomm.protocol.ips.ReportComm;
import yction.com.vsicscomm.protocol.ips.StateTagState;
import yction.com.vsicscomm.protocol.ips.cmd.AlarmAttachmentInfo;
import yction.com.vsicscomm.protocol.ips.cmd.AlarmAttachmentUploadReq;
import yction.com.vsicscomm.protocol.ips.cmd.FileInfo;
import yction.com.vsicscomm.protocol.ips.cmd.Report;
import yction.com.vsicscomm.protocol.ips.upload.AlarmAttachment;
import yction.com.vsicscomm.protocol.ips.upload.UploadAttachment;
import yction.com.vsicscomm.protocol.ips.upload.UploadContent;
import yction.com.vsicscomm.protocol.p808.cmd.Registry;

public class NetService extends Service {
    private static final String TAG = "NetService";
    private TcpClient _client;
    private FileService _fileService;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 启动文件上传服务
        _fileService = new FileService(this);
        _fileService.start();


        ClientListener cl = new ClientListener(this);
        try {
            _client = new TcpClient(cl);
            _client.setHost(Global.host, Global.port);

            Registry registry = new Registry(0, 0, "",
                    "", Global.terminalId, (byte) 0, Global.vehicleIdentification);
            _client.setRegistry(registry);

            _client.start();

            Log.i(TAG, "创建TcpClient成功!");

        } catch (NumberFormatException e) {
            Log.e(TAG, "服务器地址填写错误!" + Global.host);
        } catch (Exception e) {
            Log.e(TAG, "服务器地址填写错误!" + Global.host);
        }
    }

    // 上报正常数据
    public void report() {
        ReportComm comm = location();
        Report report = new Report(comm, null);
        _client.send(report);
    }

    // 待上传的告警附件 (Key-> 告警标识中的Date)
//    @SuppressLint("UseSparseArrays")
//    private Map<Long, AlarmAttachment> alarmAttachments = new HashMap<>();
    // 平台下发的报警附件上传指令不包含当前上传告警任何信息,
    // 暂时用上次报警(带附件)的外设ID表示待上传附件的状态
    private short lastAlarmType = 0;

    public void reportADAS() {
        try {
            ReportComm comm = location();
            AlarmADAS adas = new AlarmADAS(comm);
            adas.type = AlarmADASType.ForwardCollision;
            adas.level = 2;
            adas.frontCarSpeed = 25;
            adas.frontPedestrianDistance = 15;
            Report report = new Report(comm, adas);
            Errors err = _client.send(report);

            if (err == Errors.Success) {
                lastAlarmType = DeviceId.ADAS.getState();
            }
            System.out.println(err);
        } catch (Exception ex) {
            System.out.println("report failed, " + ex.getMessage());
        }
    }

    public void reportBSD() {
        try {
            ReportComm comm = location();
            AlarmBSD bsd = new AlarmBSD(comm);
            bsd.type = AlarmBSDType.Rear;
            Report report = new Report(comm, bsd);
            _client.send(report);
        } catch (Exception ex) {
            System.out.println("report failed, " + ex.getMessage());
        }
    }

    public void reportTPMS() {
        try {
            ReportComm comm = location();
            AlarmTPMS tpms = new AlarmTPMS(comm, new AlarmTPMSItem[]{
                    new AlarmTPMSItem(0, 120, 20, 100, AlarmTPMSType.PressureHigh)
            });
            Report report = new Report(comm, tpms);
            _client.send(report);
        } catch (Exception ex) {
            System.out.println("report failed, " + ex.getMessage());
        }
    }

    public void reportDSM() {
        try {
            ReportComm comm = location();
            AlarmDSM dsm = new AlarmDSM(comm);
            dsm.type = AlarmDSMType.FatigueDriving;
            dsm.level = 2;
            dsm.fatigueDegree = 5;
            Report report = new Report(comm, dsm);
            Errors err = _client.send(report);

            if (err == Errors.Success) {
                lastAlarmType = DeviceId.DSM.getState();
            }
            System.out.println(err);
        } catch (Exception ex) {
            System.out.println("report failed, " + ex.getMessage());
        }
    }

    public void uploadFile(AlarmAttachmentUploadReq.Content c) {
        Log.i(TAG, "request attachments upload");
        if (lastAlarmType != 0) {
            UploadContent content = new UploadContent();
            content.ip = c.ip;
            content.port = c.tcpPort;
            UploadContent uc = null;
            if (lastAlarmType == DeviceId.DSM.getState()) {
                uc = fileUploadContent(c.alarmId,
                        new UploadAttachment(R.raw.dsm01, AttachmentType.Picture, 0,
                                (byte) DeviceId.DSM.getState(), AlarmDSMType.FatigueDriving.getState(), "jpg"));
            } else if (lastAlarmType == DeviceId.ADAS.getState()) {
                uc = fileUploadContent(c.alarmId,
                        new UploadAttachment(R.raw.default1, AttachmentType.Picture, 0,
                                (byte) DeviceId.ADAS.getState(), AlarmADASType.ForwardCollision.getState(), "jpg"),
                        new UploadAttachment(R.raw.v1, AttachmentType.Video, 0,
                                (byte) DeviceId.ADAS.getState(), AlarmADASType.ForwardCollision.getState(), "h264"));
            }
            if (uc == null) {
                Log.w(TAG, "get alarm attachment upload content failed");
                return;
            }
            content.alarmAttachmentInfo = uc.alarmAttachmentInfo;
            content.fileInfos = uc.fileInfos;
            getFileService().put(content);
            lastAlarmType = 0;
        } else {
            Log.w(TAG, "there are no attachments to send;");
        }
    }

    public UploadContent fileUploadContent(String alarmId, UploadAttachment... attachments) {
        try {
            if (attachments == null || attachments.length == 0)
                return null;
            UploadContent content = new UploadContent();
            content.alarmAttachmentInfo = new AlarmAttachmentInfo();
            content.alarmAttachmentInfo.alarmId = alarmId;

            byte cnt = (byte) attachments.length;
            content.alarmAttachmentInfo.attachNum = cnt;
            content.alarmAttachmentInfo.attachmentInfos = new AttachmentInfo[cnt];
            content.fileInfos = new FileInfo[cnt];
            for (int i = 0; i < cnt; i++) {
                UploadAttachment attachment = attachments[i];
                String filename = attachment.getFileName(alarmId);

                InputStream is = getResources().openRawResource(attachment.fileId);
                long fileSize = is.available();
                is.close();
                System.out.println("file size:" + fileSize);
                if (fileSize == 0) {
                    System.out.println("file not exists");
                    break;
                }

                content.alarmAttachmentInfo.attachmentInfos[i] =
                        new AttachmentInfo(filename, fileSize);

                content.fileInfos[i] = new FileInfo();
                content.fileInfos[i].fileName = filename;
                content.fileInfos[i].filePath = attachment.filePath;
                content.fileInfos[i].fileId = attachment.fileId;
                content.fileInfos[i].at = attachment.attachmentType;
                content.fileInfos[i].fileSize = fileSize;
            }
            return content;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ReportComm location() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = LocationManager.NETWORK_PROVIDER;
        Location loc = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.w(TAG, "location permission deny");
            loc = null;
        } else if (lm != null) {
            loc = lm.getLastKnownLocation(provider);
        }
        ReportComm comm = new ReportComm();
        comm.alarmTag = 0;
        comm.stateTag = StateTagState.status(
                StateTagState.ACC,
                StateTagState.Location,
                StateTagState.GPS,
                StateTagState.Beidou
        );
        if (loc == null) {
            // default
            comm.latitude = 32.1511992097;
            comm.longitude = 119.5596599579;
            comm.height = 6;
            comm.speed = 60.0;
            comm.direction = 180;
        } else {
            comm.latitude = loc.getLatitude();
            comm.longitude = loc.getLongitude();
            comm.height = (int) loc.getAltitude();
            comm.speed = loc.getSpeed();
            comm.direction = (int) loc.getBearing();
        }
        return comm;
    }

    public FileService getFileService() {
        return _fileService;
    }

    private NetBinder binder = new NetBinder();

    public class NetBinder extends android.os.Binder {
        public NetService getService() {
            return NetService.this;
        }
    }
}
