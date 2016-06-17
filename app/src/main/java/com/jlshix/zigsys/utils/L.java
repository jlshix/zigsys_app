package com.jlshix.zigsys.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Leo on 2016/6/14.
 * 常用工具
 */
public class L {

    //请求网址
    public static String URL_GET_MSG = "http://jlshix.com/zigsys/get_msg.php/";
    public static String URL_GET = "http://jlshix.com/zigsys/get.php/";
    public static String URL_SET = "http://jlshix.com/zigsys/set.php/";
    // 简洁Toast
    public static void toast(Context c, String s) {
        Toast.makeText(c, s, Toast.LENGTH_LONG).show();
    }
}
