package com.example.daily_new.controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 触发时显示一个简单的Toast提醒
        Toast.makeText(context, "闹钟提醒！", Toast.LENGTH_SHORT).show();
        // 在此可以启动通知或其他操作
    }
}
