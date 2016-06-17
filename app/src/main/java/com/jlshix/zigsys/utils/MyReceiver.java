package com.jlshix.zigsys.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jlshix.zigsys.DeviceActivity;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Leo on 2016/6/17.
 * JPush Receiver
 */
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果收到通知消息
        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Intent i = new Intent(context, DeviceActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
