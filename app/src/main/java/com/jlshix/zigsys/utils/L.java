package com.jlshix.zigsys.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Leo on 2016/6/14.
 * 常用工具
 */
public class L {

    private static final String TAG = "UTIL_L";
    //请求网址
    public static String URL_GET_MSG = "http://jlshix.com/zigsys/get_msg.php/";
    public static String URL_GET = "http://jlshix.com/zigsys/get.php/";
    public static String URL_SET = "http://jlshix.com/zigsys/set.php/";
    public static String URL_PUSH = "http://jlshix.com/zigsys/togate.php/";
    public static String GATE_TAG = "zzh";
    // 简洁Toast
    public static void toast(Context c, String s) {
        Toast.makeText(c, s, Toast.LENGTH_LONG).show();
    }

    public static void send2Gate(final String tag, final String msg) {

        RequestParams params = new RequestParams(URL_PUSH + "?tag=" + tag + "&msg=" + msg);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.optString("code").equals("1")) {
                        Log.e(TAG, msg + " to " + tag);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "PUSH_ERR" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
