package com.younle.younle624.myapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.younle.younle624.myapplication.activity.LoginActivity;
import com.younle.younle624.myapplication.utils.LogUtils;

/**
 * Created by 我是奋斗 on 2016/5/11.
 * 微信/e-mail:tt090423@126.com
 *
 * 接收开机广播，自动启动程序
 */
public class BootReceiver extends BroadcastReceiver{
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(ACTION.equals(intent.getAction())) {
            LogUtils.Log("开机自启");
            Intent startIntent = new Intent(context, LoginActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(startIntent);
        }
    }
}
