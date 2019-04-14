package yction.com.vsicscomm.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by yww08 on 2017-12-05.
 * GZIP压缩
 */

public class GZIP {

    public static byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(data);
        gzip.close();
        return out.toByteArray();
    }

    public static byte[] unCompress(byte[] data) throws IOException {
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        ByteArrayInputStream in=new ByteArrayInputStream(data);
        GZIPInputStream gzip=new GZIPInputStream(in);
        byte[] buff=new byte[256];
        int n=0;
        while ((n=gzip.read(buff))>0){
            out.write(buff,0,n);
        }
        return out.toByteArray();
    }
}
