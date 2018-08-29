package com.younle.younle624.myapplication.receiver;

import android.app.ActivityManager;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.younle.younle624.myapplication.activity.KeepAliveActivity;
import com.younle.younle624.myapplication.domain.waimai.ScreenOnBean;
import com.younle.younle624.myapplication.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

public class ScreenOnOffReceiver extends BroadcastReceiver {
    private static final String TAG = "ScreenOnOffReceiver";

    public ScreenOnOffReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(BluetoothDevice.ACTION_ACL_CONNECTED)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            LogUtils.e(TAG, device.getName() + " 蓝牙连接上了");
        } else if (intent.getAction().equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            LogUtils.e(TAG, device.getName() + " 蓝牙断开了");
        }


        LogUtils.Log("ScreenOnOffReceiver  onReceive()");
        if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {//开屏
            EventBus.getDefault().post(new ScreenOnBean("开屏了"));
            LogUtils.Log("开屏了");
        }else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            context.startActivity(new Intent(context, KeepAliveActivity.class));
            LogUtils.Log("锁屏了");
        }else if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            LogUtils.Log("用户操作解锁了");
        }
    }
    private boolean isCurrentActivityTop(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(KeepAliveActivity.class.getName());
    }
    private void removeActivity(Context context) {
    }
}
