package yction.com.vsicscomm;

import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import yction.com.vsicscomm.protocol.p808.Msg;

import static org.junit.Assert.*;

@Ignore
public class TcpClientTest {
    private TcpClient _client;

    @Before
    public void setup() throws IOException {

        ClientListener cl = new ClientListener();
        _client = new TcpClient(cl);
//        _client.setHost("59.36.96.75", 10001);
        _client.setHost("192.168.0.106", 9001);
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