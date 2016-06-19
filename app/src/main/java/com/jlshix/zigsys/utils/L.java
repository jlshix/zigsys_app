package com.jlshix.zigsys.utils;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.jlshix.zigsys.App;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.DecimalFormat;

/**
 * Created by Leo on 2016/6/14.
 * 常用工具
 */
public class L {

    private static final String TAG = "UTIL_L";
    public static final int SCAN_REQUEST = 1000;
    public static final int SCAN_RETURN = 2000;
    //请求网址
    public static String URL_GET_MSG = "http://jlshix.com/zigsys/get_msg.php/";
    public static String URL_GET = "http://jlshix.com/zigsys/get.php/";
    public static String URL_SET = "http://jlshix.com/zigsys/set.php/";
    public static String URL_PUSH = "http://jlshix.com/zigsys/togate.php/";
    public static String URL_SET_GATE = "http://jlshix.com/zigsys/set_gate.php/";
    public static final java.lang.String URL_WEATHER = "https://api.caiyunapp.com/v2/X6f3oc9bahTuV6Bv/";
    public static final java.lang.String URL_GATE = "http://jlshix.com/zigsys/get_gate.php/";
    public static final java.lang.String URL_GATE_BIND = "http://jlshix.com/zigsys/bind_gate.php/";


    /**
     * 客户端目前只有一个，设为zzh 后期可拓展
     */
    public static String GATE_TAG = "zzh";
    public static String GATE_IMEI = "4718";
    public static boolean BIND = false;
    public static String MAIL = "jlshix@163.com";

    public static String getGateImei() {
        return App.sp.getString("gateImei", "x");
    }

    public static void setGateImei(String gateImei) {
        GATE_IMEI = gateImei;
        App.sp.edit().putString("gateImei", gateImei).apply();
    }

    public static boolean isBIND() {
        return App.sp.getBoolean("bind", false);
    }

    public static void setBIND(boolean BIND) {
        L.BIND = BIND;
        App.sp.edit().putBoolean("bind", BIND).apply();
    }

    public static String getMAIL() {
        return App.sp.getString("mail", "x");
    }

    public static void setMAIL(String MAIL) {
        L.MAIL = MAIL;
        App.sp.edit().putString("mail", MAIL).apply();
    }

    public static String getGateTag() {
        return GATE_TAG;
    }

    public static void setGateTag(String gateTag) {
        GATE_TAG = gateTag;
    }



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

    public static String getGPS(Context c) {
        String mDefault = "120.1227,36.0001";
        //
        LocationManager locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {

            // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.e("Map", "onStatusChanged");
            }

            // Provider被enable时触发此函数，比如GPS被打开
            @Override
            public void onProviderEnabled(String provider) {
                Log.e("Map", "onProviderEnabled");
            }

            // Provider被disable时触发此函数，比如GPS被关闭

            @Override
            public void onProviderDisabled(String provider) {
                Log.e("Map", "onProviderDisabled");
//                loc_flag = "" + 1;
            }

            //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    Log.e("Map", "Location changed : Lat: " + location.getLatitude() + " Lng: " + location.getLongitude());
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            L.toast(c, "LOCATION_PERMISSION_DENIED");
            return null;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location != null){
//            loc_flag = ""+2;
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            DecimalFormat df = new DecimalFormat("#.0000");
            df.format(longitude);
            df.format(latitude);
            Log.e(TAG, "getGPS: " + longitude + "---" + latitude);
            // TODO 使用 SharedPreferences 保存
            return longitude + "," + latitude;
        }
        return mDefault;
    }
}
