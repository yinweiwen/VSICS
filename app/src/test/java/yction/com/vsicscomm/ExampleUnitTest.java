package yction.com.vsicscomm;

import org.junit.Ignore;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import yction.com.vsicscomm.protocol.ips.AttachmentFileName;
import yction.com.vsicscomm.protocol.ips.AttachmentType;
import yction.com.vsicscomm.protocol.ips.ReportComm;
import yction.com.vsicscomm.protocol.ips.cmd.Report;
import yction.com.vsicscomm.protocol.p808.Msg;
import yction.com.vsicscomm.protocol.p808.MsgFrame;

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

    class Monitor {
        boolean flag;
    }
}