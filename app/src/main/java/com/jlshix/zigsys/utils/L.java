package com.jlshix.zigsys.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Leo on 2016/6/14.
 * 常用工具
 */
public class L {

    // 简洁Toast
    public static void toast(Context c, String s) {
        Toast.makeText(c, s, Toast.LENGTH_LONG).show();
    }
}
