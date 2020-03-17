package com.taocoder.linkcalculator;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.security.MessageDigest;

public class Utils {

    public static void showMessage(final Context context, final String msg) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void logMessage(Context context, String msg) {
        Log.i(Utils.class.getSimpleName(), msg);
    }

    public static String md5(String s) {

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());

            byte[] bytes = digest.digest();
            StringBuffer stringBuffer = new StringBuffer();

            for (int i = 0; i < bytes.length; i++) {
                stringBuffer.append(Integer.toHexString(0xFF & bytes[i]));
            }

            return stringBuffer.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
