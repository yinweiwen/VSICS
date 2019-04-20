package yction.com.vsicscomm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.Date;

import yction.com.vsicscomm.protocol.Errors;
import yction.com.vsicscomm.protocol.ips.AlarmADAS;
import yction.com.vsicscomm.protocol.ips.AlarmDSM;
import yction.com.vsicscomm.protocol.ips.AlarmTag;
import yction.com.vsicscomm.protocol.ips.ReportComm;
import yction.com.vsicscomm.protocol.ips.cmd.Report;
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
            comm.alarmTag = 0x80000;
            comm.stateTag = 0xC0003;
            comm.latitude = 32.1511992097;
            comm.longitude = 119.5596599579;
            comm.height = 6;
            comm.speed = 60.0;
            comm.direction = 0;
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
