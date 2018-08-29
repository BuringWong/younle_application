package com.younle.younle624.myapplication.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.printsetting.PrintDevices;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.SaveUtils;

public class BlueToothReceiver extends BroadcastReceiver {
    String TAG="BlueToothReceiver";
    public BlueToothReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.e(TAG,"onReceive()");
        String action = intent.getAction();
        PrintDevices savedScanner= (PrintDevices) SaveUtils.getObject(context, Constant.SAVED_BLUETOOTH_SCANNER);
        // 获得已经搜索到的蓝牙设备
       switch (action) {
           case BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED :
               int conectState = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, BluetoothAdapter.ERROR);
               LogUtils.e(TAG, "connectState==" + conectState);
                BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
               if(savedScanner!=null&&savedScanner.getBlueToothAdd().equals(device.getAddress())) {
                   if(conectState==0) {
                       LogUtils.e(TAG,"未连接");
                       Constant.scanner_state=Constant.SCANNER_DISCONNECT;
                   }else if(conectState==1) {
                       Constant.scanner_state=Constant.SCANNER_CONNECTING;
                       LogUtils.e(TAG,"连接中。。。");
                   }else if(conectState==2) {
                       Constant.scanner_state=Constant.SCANNER_CONNECTED;
                       LogUtils.e(TAG, "已连接");
                   }
               }
               break;
       }
    }

}
