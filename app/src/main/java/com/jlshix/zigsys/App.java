package com.jlshix.zigsys;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Leo on 2016/6/14.
 * App类
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(false);
    }
}
