package com.jlshix.zigsys;

import android.app.Application;
import android.content.SharedPreferences;

import org.xutils.x;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Leo on 2016/6/14.
 * App类
 */
public class App extends Application {

    public static SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();

        // xUtils
        x.Ext.init(this);
        x.Ext.setDebug(false);

        //JPush
//        JPushInterface.setDebugMode(false);
//        JPushInterface.init(this);
//        Set<String> tags = new HashSet<>();
//        tags.add("jlshix");
//        JPushInterface.setTags(getApplicationContext(), tags, null);

        sp = getSharedPreferences("conf", MODE_PRIVATE);

    }
}
