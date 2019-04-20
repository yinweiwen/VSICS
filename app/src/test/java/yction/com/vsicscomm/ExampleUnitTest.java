package yction.com.vsicscomm;

import org.junit.Ignore;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import yction.com.vsicscomm.protocol.ByteBufferUnsigned;
import yction.com.vsicscomm.protocol.ips.AlarmADAS;
import yction.com.vsicscomm.protocol.ips.AttachmentFileName;
import yction.com.vsicscomm.protocol.ips.AttachmentInfo;
import yction.com.vsicscomm.protocol.ips.AttachmentType;
import yction.com.vsicscomm.protocol.ips.ReportComm;
import yction.com.vsicscomm.protocol.ips.cmd.AlarmAttachmentInfo;
import yction.com.vsicscomm.protocol.ips.cmd.FileInfo;
import yction.com.vsicscomm.protocol.ips.cmd.Report;
import yction.com.vsicscomm.protocol.ips.upload.FileFinishAck;
import yction.com.vsicscomm.protocol.ips.upload.FileFinishAckItem;
import yction.com.vsicscomm.protocol.ips.upload.UploadContent;
import yction.com.vsicscomm.protocol.p808.Msg;
import yction.com.vsicscomm.protocol.p808.MsgFrame;
import yction.com.vsicscomm.utils.Utils;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }


    @Test
    @Ignore
    public void TestSync() throws InterruptedException {
        final Msg recMsg = null;
        final Monitor monitor = new Monitor();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (i < 5) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    synchronized (monitor) {
                        monitor.flag = true;
                        monitor.notify();
                    }
                    i++;
                }
            }
        }).start();

        synchronized (monitor) {
            while (true) {
                monitor.wait(3000);
                if (monitor.flag) {
                    monitor.flag = false;
                    System.out.println("monitor notified!");
                } else {
                    System.out.println("timeout!!");
                    break;
                }
            }
        }

        System.out.println("finished.");
    }

    @Test
    public void testHashMap() {
        Report report = new Report(new ReportComm(), null);
        System.out.println(report.msg());

        double a = 3.123;
        System.out.println((long) (a * 1e3));

        double s = 52.312;
        System.out.println((int) (s * 10));

        Map<Integer, Object> map = new HashMap<>();
        map.put(1, "123");
        map.put(1, "234");
        System.out.println(map.size());
        System.out.println(map.get(1));
        Object obj = map.get(2);
        System.out.println(obj);

        ByteBuffer bb = ByteBuffer.allocate(10);
        bb.putInt(1);
        System.out.println(bb.position());
    }

    @Test
    public void testAttachmentFileName() {
        System.out.println(AttachmentFileName.toFileName(AttachmentType.Picture,
                0,
                (byte) 0x64, (byte) 0x01, 1, "0102030123", "png"));
    }

    @Test
    public void testReportParse() throws ParseException {
        byte[] bts = Utils.hexStringToByteArray("7E0200005A010059107571072500080000000C00030160440A06C69889000600000000190416231440010400000000020200000302000014040000000015040000000016040000000017020000180200002504000000002A0200002B0400000000300149310117D47E");

        MsgFrame frame = MsgFrame.fromBytes(bts);
        Report report = Report.fromBytes(frame.getBody());
        System.out.println(report);
    }

    @Test
    public void testReportAdas() throws ParseException {
        byte[] bts = Utils.hexStringToByteArray("7E0200004D00001823033500A600000000000000030160441006C69790000000000000190418182644652F0000000C00040200000000000000000160441006C697901904181826430000000000131093001904181826430002004A7E");

        MsgFrame frame = MsgFrame.fromBytes(bts);
        Report report = Report.fromBytes(frame.getBody());
        System.out.println(report);
    }

    @Test
    public void testQueue() throws InterruptedException {
        BlockingQueue<String> files = new LinkedBlockingQueue<String>(5);
        files.offer("1");
        System.out.println(files.peek());
        files.take();
        System.out.println(files.peek());
    }

    @Test
    public void testReportEncodeDecode() throws ParseException {
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

        byte[] bts = report.msg().frames()[0].toBytes();
        System.out.println(Utils.bytesToHexString(bts));


        MsgFrame frame = MsgFrame.fromBytes(bts);
        assert frame != null;
        Report report2 = Report.fromBytes(frame.getBody());
        System.out.println(report2);
    }

    @Test
    public void TestGetBytes() {
        byte[] b1 = Utils.getBytes("123908129417293712", 2, "GBK");
        System.out.println(Utils.bytesToHexString(b1));
        byte[] b2 = Utils.getBytes("123908129417293712", 30, "GBK");
        System.out.println(Utils.bytesToHexString(b2));
    }

    @Test
    public void testUploadContentConstruct() {
        String aids = UUID.randomUUID().toString();
        UploadContent content = new UploadContent();
        content.ip = "192.168.0.1";
        content.port = 20011;
        content.alarmAttachmentInfo = new AlarmAttachmentInfo();
        content.alarmAttachmentInfo.alarmId = aids;

        byte cnt = 1;
        content.alarmAttachmentInfo.attachNum = cnt;
        content.alarmAttachmentInfo.attachmentInfos = new AttachmentInfo[cnt];

        String filepath1 = "123457452";
        long filesize1 = 102400;
        String filename1 =
                AttachmentFileName.toFileName(AttachmentType.Picture,
                        0, (byte) 0x64, (byte) 0x01, 0,
                        aids, "jpg");
        content.alarmAttachmentInfo.attachmentInfos[0] = new AttachmentInfo();
        content.alarmAttachmentInfo.attachmentInfos[0].filename = filename1;
        content.alarmAttachmentInfo.attachmentInfos[0].filesize = filesize1;

        content.fileInfos = new FileInfo[cnt];
        content.fileInfos[0] = new FileInfo();
        content.fileInfos[0].fileName = filename1;
        content.fileInfos[0].filePath = filepath1;
        content.fileInfos[0].at = AttachmentType.Picture;
        content.fileInfos[0].fileSize = filesize1;
    }

    @Test
    public void testOnFileFinished() {
        byte[] bts = Utils.hexStringToByteArray("3030305f305f363430315f305f35323733323236393331366634303430613166633838346461323466373265662e6a70670001010001000000033cfb");
        FileFinishAck ack = new FileFinishAck();
        ByteBufferUnsigned bb = new ByteBufferUnsigned(bts);
        byte fileNameLen = bb.raw().get();
        byte[] fileNameBts = new byte[fileNameLen];
        bb.raw().get(fileNameBts);
        byte fileType = bb.raw().get();
        ack.result = bb.raw().get();
        if (ack.result == 0x01) {
            byte reUploadCnt = bb.raw().get();
            ack.reUploads = new FileFinishAckItem[reUploadCnt];
            for (int i = 0; i < reUploadCnt; i++) {
                ack.reUploads[i] = new FileFinishAckItem();
                ack.reUploads[i].offset = bb.getUnsignedInt();
                ack.reUploads[i].size = bb.getUnsignedInt();
            }
        }
    }

    class Monitor {
        boolean flag;
    }
}