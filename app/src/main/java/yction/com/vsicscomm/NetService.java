package yction.com.vsicscomm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.Date;

import yction.com.vsicscomm.protocol.ips.AlarmADAS;
import yction.com.vsicscomm.protocol.ips.ReportComm;
import yction.com.vsicscomm.protocol.ips.cmd.Report;

public class NetService extends Service {
    private static final String TAG = "NetService";
    private TcpClient _client;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        String host = "59.36.96.75";
        int port = 10001;

        ClientListener cl = new ClientListener();
        try {
            _client = new TcpClient(cl);
            _client.setHost(host, port);
//            _client.setHost("192.168.0.106", 9001);
            _client.province = 0;
            _client.city = 0;
            _client.manufacturerId = new byte[5];
            _client.terminalModel = new byte[20];
            _client.terminalId = new byte[]{0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x32};
            _client.licenseColor = 0x01;
            _client.vehicleIdentification = "粤S80997";
            _client.start();
            Log.i(TAG, "创建TcpClient成功!");

        } catch (IOException e) {
            Log.e(TAG, "创建TcpClient错误!" + e.getMessage());
        } catch (NumberFormatException e) {
            Log.e(TAG, "服务器地址填写错误!" + host);
        } catch (Exception e) {
            Log.e(TAG, "服务器地址填写错误!" + host);
        }
    }

    public void reportADAS(){
        try {
            System.out.println("report ...");
            ReportComm comm = new ReportComm();
            comm.alarmTag = 0;
            comm.stateTag = 0;
            comm.latitude = 32.1314312971;
            comm.longitude = 119.5449829102;
            comm.height = 12;
            comm.speed = 60;
            comm.direction = 30;
            comm.date = new Date();
            AlarmADAS adas = new AlarmADAS();
            adas.报警ID = 0;
            Report report = new Report(comm, adas);
            report.send();
        } catch (Exception ex) {
            System.out.println("report failed, " + ex.getMessage());
        } finally {
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private NetBinder binder = new NetBinder();

    public class NetBinder extends android.os.Binder {
        public NetService getService() {
            return NetService.this;
        }
    }
}
