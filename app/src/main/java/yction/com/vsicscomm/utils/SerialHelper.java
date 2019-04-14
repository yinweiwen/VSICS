package yction.com.vsicscomm.utils;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;

/**
 * Created by yww08 on 2018-06-28.
 * 串口类
 */

public abstract class SerialHelper {

    private final String Tag = "SerialHelper";

    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private String port;
    private int baudrate;
    private boolean isOpen;

    //----------------------------------------------------
    public SerialHelper(String sPort, int iBaudRate) {
        this.port = sPort;
        this.baudrate = iBaudRate;
    }

    //----------------------------------------------------
    public boolean open() {
        if (isOpen) close();
        try {
            mSerialPort = new SerialPort(port, baudrate);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();
            mReadThread = new ReadThread();
            mReadThread.start();
            isOpen = true;
        } catch (SecurityException e) {
            Log.w(Tag, "error_security");
            return false;
        } catch (IOException e) {
            Log.w(Tag, "error_unknown");
            return false;
        } catch (InvalidParameterException e) {
            Log.w(Tag, "error_configuration");
            return false;
        }
        return true;
    }

    //----------------------------------------------------
    public boolean close() {
        try {
            if (mReadThread != null) {
                mReadThread.interrupt();
                mReadThread = null;
            }
            if (mSerialPort != null) {
                mSerialPort.safeClose();
                mSerialPort = null;
            }
            isOpen = false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //----------------------------------------------------
    public boolean send(byte[] bOutArray) {
        try {
            mOutputStream.write(bOutArray);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //----------------------------------------------------
    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                int size;
                try {
                    byte[] buffer = new byte[512];
                    if (mInputStream == null) return;
                    size = mInputStream.read(buffer);
                    if (size > 0) {

                        byte[] tempBuf = new byte[size];
                        System.arraycopy(buffer, 0, tempBuf, 0, size);
                        onDataReceived(tempBuf, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    //----------------------------------------------------
    protected abstract void onDataReceived(byte[] buffer, int size);


    //----------------------------------------------------
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public int getBaudrate() {
        return baudrate;
    }

    public void setBaudrate(int baudrate) {
        this.baudrate = baudrate;
    }

    public boolean isOpen() {
        return isOpen;
    }

}
