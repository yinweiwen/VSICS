package yction.com.vsicscomm.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by yww08 on 2017-12-09.
 * 工具类集合
 */

public class Utils {
    // sub-array
    public static byte[] sub(byte[] data, int start, int end) {
        byte[] res = new byte[end - start + 1];
        System.arraycopy(data, start, res, 0, res.length);
        return res;
    }

    public static byte[] sub(byte[] data, int start) {
        return sub(data, start, data.length - 1);
    }

    // cat-array
    public static byte[] cat(byte[] data1, byte[] data2) {
        byte[] res = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, res, 0, data1.length);
        System.arraycopy(data2, 0, res, data1.length, data2.length);
        return res;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] b = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            b[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return b;
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static String int2HexString(int value) {
        return int2HexString(value, "0x");
    }

    public static String int2HexString(int value, String perfix) {
        StringBuilder sb = new StringBuilder();
        String hex = Integer.toHexString(value);
        sb.append(perfix);
        if (hex.length() % 2 == 1) {
            sb.append('0');
        }
        sb.append(hex);
        return sb.toString();
    }

    public static byte[] combineBytes(byte[]... bts) {
        byte[] res = bts[0];
        for (int i = 1; i < bts.length; i++) {
            res = concat(res, bts[i]);
        }
        return res;
    }

    public static int getInt(byte[] data, int start) {
        return data[start + 3] & 0xFF |
                (data[start + 2] & 0xFF) << 8 |
                (data[start + 1] & 0xFF) << 16 |
                (data[start] & 0xFF) << 24;
    }

    public static short getShort(byte[] data, int start) {
        return (short) (data[start + 1] & 0xff |
                (data[start] & 0xff) << 8);
    }

    public static byte[] getBytes(int value) {
        return new byte[]{
                (byte) ((value >> 24) & 0xff),
                (byte) ((value >> 16) & 0xff),
                (byte) ((value >> 8) & 0xff),
                (byte) (value & 0xff)};
    }

    public static byte[] getBytes(String s) {
        try {
            return s.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static String getString(byte[] data) {
        return new String(data, Charset.forName("GBK"));
    }

    public static byte[] getBytes(short value) {
        return new byte[]{
                (byte) ((value >> 8) & 0xff),
                (byte) (value & 0xff)};
    }

    public static byte[] concat(byte[] first, byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static String strings2Md5(List<String> veinTags) {

        Collator coll = Collator.getInstance(Locale.getDefault());
        coll.setStrength(Collator.PRIMARY);
        Collections.sort(veinTags, coll);
        String tag = TextUtils.join("", veinTags);
        return Encrypt.md5(tag);
    }

    public static Bitmap drawBg4Bitmap(int color, Bitmap orginBitmap) {
        Paint paint = new Paint();
        paint.setColor(color);
        Bitmap bitmap = Bitmap.createBitmap(orginBitmap.getWidth(),
                orginBitmap.getHeight(), orginBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, orginBitmap.getWidth(), orginBitmap.getHeight(), paint);
        canvas.drawBitmap(orginBitmap, 0, 0, paint);
        return bitmap;
    }

    public static List<String> jsonArrayToList(Object jsonObject) throws JSONException {
        ArrayList<String> list = new ArrayList<String>();
        JSONArray jsonArray = (JSONArray) jsonObject;
        if (jsonArray != null) {
            int len = jsonArray.length();
            for (int i = 0; i < len; i++) {
                list.add(jsonArray.get(i).toString());
            }
        }
        return list;
    }

    public static short checkSumX(byte[] ptr, int size) {
        if (size % 2 != 0) {
            size--;
        }
        int cksum = 0;
        int index = 0;
        while (index < size) {
            cksum += ptr[index + 1] & 0xff;
            cksum += ((ptr[index] & 0xff) << 8);
            index += 2;
        }
        while (cksum > 0xffff) {
            cksum = (cksum >> 16) + (cksum & 0xffff);
        }
        return (short) (~cksum);
    }
}
