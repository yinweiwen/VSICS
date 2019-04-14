package yction.com.vsicscomm;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import yction.com.vsicscomm.utils.Log;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("yction.com.vsicscomm", appContext.getPackageName());
    }

    private TcpClient _client;

    @Before
    public void setup() throws IOException {
        Log.UseInnerAndroidLog = false;

        ClientListener cl = new ClientListener();
        _client = new TcpClient(cl);
        _client.setHost("59.36.96.75", 10001);
//        _client.setHost("192.168.0.106", 9001);
        _client.province = 1;
        _client.city = 1;
        _client.manufacturerId = new byte[5];
        _client.terminalModel = new byte[20];
        _client.terminalId = new byte[7];
        _client.licenseColor = 0x01;
        _client.vehicleIdentification = "粤S80999";
        _client.start();
    }

    @After
    public void after() throws InterruptedException {
        while (true) {
            Thread.sleep(1000);
        }
    }


    @Test
    public void start() throws Exception {

    }
}
