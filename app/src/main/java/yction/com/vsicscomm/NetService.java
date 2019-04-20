package yction.com.vsicscomm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import yction.com.vsicscomm.protocol.Errors;
import yction.com.vsicscomm.protocol.ips.AlarmADAS;
import yction.com.vsicscomm.protocol.ips.AlarmDSM;
import yction.com.vsicscomm.protocol.ips.AlarmTag;
import yction.com.vsicscomm.protocol.ips.AttachmentFileName;
import yction.com.vsicscomm.protocol.ips.AttachmentInfo;
import yction.com.vsicscomm.protocol.ips.AttachmentType;
import yction.com.vsicscomm.protocol.ips.ReportComm;
import yction.com.vsicscomm.protocol.ips.StateTagState;
import yction.com.vsicscomm.protocol.ips.cmd.AlarmAttachmentInfo;
import yction.com.vsicscomm.protocol.ips.cmd.AlarmAttachmentUploadReq;
import yction.com.vsicscomm.protocol.ips.cmd.FileInfo;
import yction.com.vsicscomm.protocol.ips.cmd.Report;
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

    public void reportADAS() {
        try {
            System.out.println("report ...");
            ReportComm comm = new ReportComm();
            comm.alarmTag = 0;
            comm.stateTag = StateTagState.status(
                    StateTagState.ACC,
                    StateTagState.Location,
                    StateTagState.GPS,
                    StateTagState.Beidou
            );
            comm.latitude = 32.1511992097;
            comm.longitude = 119.5596599579;
            comm.height = 6;
            comm.speed = 60.0;
            comm.direction = 180;
            comm.date = new Date();
            AlarmDSM dsm = new AlarmDSM();
            dsm.报警事件类型 = 0x01;
            dsm.报警级别 = 0x02;
            dsm.疲劳程度 = 5;
            dsm.车速 = 30;
            dsm.高程 = 5;
            dsm.纬度 = 32.1511992097;
            dsm.经度 = 119.5596599579;
            dsm.日期时间 = new Date();
            dsm.报警标识号 = new AlarmTag();
            dsm.报警标识号.terminalId = Global.terminalId;
            dsm.报警标识号.attachNum = 2;
            Report report = new Report(comm, dsm);
            Errors err = _client.send(report);
            System.out.println(err);
        } catch (Exception ex) {
            System.out.println("report failed, " + ex.getMessage());
        }
    }

    public void UploadFile(AlarmAttachmentUploadReq.Content c) {
        try {
            UploadContent content = new UploadContent();
            content.ip = c.ip;
            content.port = c.tcpPort;
            content.alarmAttachmentInfo = new AlarmAttachmentInfo();
            content.alarmAttachmentInfo.alarmId = c.alarmId;

            byte cnt = 1;
            content.alarmAttachmentInfo.attachNum = cnt;
            content.alarmAttachmentInfo.attachmentInfos = new AttachmentInfo[cnt];

//            String file1 = "res://raw/" + R.raw.default1;
//            String filepath1 = Uri.parse(file1).getPath();
//            long filesize1 =new File(netService.getFilesDir(), filepath1).length();
            String filepath1 = "";
            int fileId1 = R.raw.dsm01;
            InputStream is = getResources().openRawResource(fileId1);
            long filesize1 = is.available();
            is.close();
            System.out.println("filesize:" + filesize1);
            if (filesize1 == 0) {

                System.out.println("file not exists");
            } else {

                String filename1 =
                        AttachmentFileName.toFileName(AttachmentType.Picture,
                                0, (byte) 0x65, (byte) 0x01, 0,
                                c.alarmId, "jpg");
                content.alarmAttachmentInfo.attachmentInfos[0] =
                        new AttachmentInfo(filename1, filesize1);

                content.fileInfos = new FileInfo[cnt];
                content.fileInfos[0] = new FileInfo();
                content.fileInfos[0].fileName = filename1;
                content.fileInfos[0].filePath = filepath1;
                content.fileInfos[0].fileId = fileId1;
                content.fileInfos[0].at = AttachmentType.Picture;
                content.fileInfos[0].fileSize = filesize1;

                getFileService().put(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
