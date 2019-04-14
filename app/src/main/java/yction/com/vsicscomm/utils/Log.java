package yction.com.vsicscomm.utils;

/**
 * for comm testing,replace Android log with 'sout'
 */
public class Log {

    public static boolean UseInnerAndroidLog = true;


    public static void v(String tag, String content) {
        if (UseInnerAndroidLog) android.util.Log.v(tag, content);
        System.out.println(String.format("%s:[%s] %s", "v", tag, content));
    }

    public static void d(String tag, String content) {
        if (UseInnerAndroidLog) android.util.Log.d(tag, content);
        System.out.println(String.format("%s:[%s] %s", "d", tag, content));
    }

    public static void i(String tag, String content) {
        if (UseInnerAndroidLog) android.util.Log.i(tag, content);
        System.out.println(String.format("%s:[%s] %s", "i", tag, content));
    }

    public static void w(String tag, String content) {
        if (UseInnerAndroidLog) android.util.Log.w(tag, content);
        System.out.println(String.format("%s:[%s] %s", "w", tag, content));
    }

    public static void e(String tag, String content) {
        if (UseInnerAndroidLog) android.util.Log.e(tag, content);
        System.out.println(String.format("%s:[%s] %s", "e", tag, content));
    }
}
