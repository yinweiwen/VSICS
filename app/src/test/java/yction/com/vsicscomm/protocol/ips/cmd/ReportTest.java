package yction.com.vsicscomm.protocol.ips.cmd;

import org.junit.Test;

import java.util.Date;

import yction.com.vsicscomm.protocol.ips.AlarmADAS;
import yction.com.vsicscomm.protocol.ips.ReportComm;
import yction.com.vsicscomm.protocol.p808.Msg;
import yction.com.vsicscomm.utils.Utils;

public class ReportTest {
    @Test
    public void onMsg() throws Exception {
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
        AlarmADAS adas = new AlarmADAS(null);
        adas.alarmId = 0;
        Report report = new Report(comm, adas);
        Msg msg = report.msg();
        System.out.println(msg.toString());
        System.out.println(Utils.bytesToHexString(msg.body()));
    }

}