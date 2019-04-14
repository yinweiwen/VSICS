/*
 * Copyright 2009 Cedric Priscal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android_serialport_api;

import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SerialPort {

    private static final String TAG = "SerialPort";

    private static final String DEFAULT_SU_PATH = "/system/bin/su";

    private static String sSuPath = DEFAULT_SU_PATH;

    /**
     * Set the su binary path, the default su binary path is {@link #DEFAULT_SU_PATH}
     *
     * @param suPath su binary path
     */
    public static void setSuPath(String suPath) {
        if (suPath == null) {
            return;
        }
        sSuPath = suPath;
    }

    /*
     * Do not remove or rename the field mFd: it is used by native method close();
     */
    private FileDescriptor mFd;
    private InputStream mFileInputStream;
    private OutputStream mFileOutputStream;

    public SerialPort(String devicePath, int baudrate) throws SecurityException, IOException {

        // 允许TCP转串口测试
        if(devicePath.indexOf(":")>0){
            CreateSocketPort(devicePath);
            return;
        }
        File device=new File(devicePath);

		/* Check access permission */
        if (!device.canRead() || !device.canWrite()) {
            try {
                /* Missing read/write permission, trying to chmod the file */
                Process su;
                su = Runtime.getRuntime().exec(sSuPath);
                String cmd = "chmod 666 " + device.getAbsolutePath() + "\n" + "exit\n";
                su.getOutputStream().write(cmd.getBytes());
                if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
                    throw new SecurityException();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SecurityException();
            }
        }

        mFd = open(device.getAbsolutePath(), baudrate, 0);
        if (mFd == null) {
            Log.e(TAG, "native open returns null");
            throw new IOException();
        }
        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
    }

    // ----------增加socket消息转发com (测试使用，仅当deviceFile配置为[IP]:[port]格式时生效)----------
    private Socket mSocket;
    public void CreateSocketPort(String devicePath) throws IOException {
        String addr=devicePath.split(":")[0];
        Integer port= Integer.parseInt(devicePath.split(":")[1]);
        mSocket=new Socket(addr,port);
        mFileInputStream= mSocket.getInputStream();
        mFileOutputStream= mSocket.getOutputStream();

    }

    public void safeClose(){
        if(mFd!=null){
            close();
        }else if(mSocket!=null){
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // ------------------------------------------------------------------------------------------

    // Getters and setters
    public InputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    // JNI
    private native static FileDescriptor open(String path, int baudrate, int flags);
    public native void close();
    static {
        System.loadLibrary("serial_port");
    }
}
