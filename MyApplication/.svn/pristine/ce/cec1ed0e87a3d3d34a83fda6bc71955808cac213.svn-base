package com.younle.younle624.myapplication.activity.manager.weigher;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.ImageView;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by Administrator on 2017/7/11.
 */

public  class WeighManager {
    private BluetoothLeService mBluetoothLeService;
    private String btAdd;
    private OnWeighChangeListener weighChangeListener;
    public static WeighManager instance;
//    public boolean isServiceConnected;
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            LogUtils.Log("onServiceConnected");
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
            }
            LogUtils.Log("电子秤连接状态：" + mBluetoothLeService.mConnectionState);
            connectWeigh(btAdd);
            BluetoothAdapter.getDefaultAdapter().getRemoteDevice(btAdd).getBondState();
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
    public static WeighManager getInstance(){
        if(instance==null) {
            instance=new WeighManager();
        }
        return instance;
    }
    public static String Service_uuid = "0000ffe0-0000-1000-8000-00805f9b34fb";
    public static String Characteristic_uuid_TX = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static String Characteristic_uuid_RX = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static final int TARE = 0;
    public static final int ZERO = 1;
    public static final int RESET= 2;

    public void resetWeigher(int action){
        if(mBluetoothLeService!=null) {
            List<BluetoothGattService> services = mBluetoothLeService.getSupportedGattServices();
            for (BluetoothGattService bluetoothGattService :services){
                if((bluetoothGattService.getUuid().toString()).equals(Service_uuid)) {
                    List<BluetoothGattCharacteristic> characteristics = bluetoothGattService.getCharacteristics();
                    for(BluetoothGattCharacteristic characteristic:characteristics){
                        if((characteristic.getUuid().toString()).equals(Characteristic_uuid_TX)) {
                            //去皮
                            byte[] qpBytes = new byte[4];
                            qpBytes[0]=0x44;
                            qpBytes[1]=0x54;
                            qpBytes[2]=0x0d;
                            qpBytes[3]=0x0a;
                            //志玲
                            byte[] zlBytes = new byte[4];
                            zlBytes[0]=0x44;
                            zlBytes[1]=0x5a;
                            zlBytes[2]=0x0d;
                            zlBytes[3]=0x0a;
                            //标定
                            byte[] bdBytes = new byte[4];
                            bdBytes[0]=0x44;
                            bdBytes[1]=0x57;
                            bdBytes[2]=0x0d;
                            bdBytes[3]=0x0a;
                            if(action==TARE) {
                                characteristic.setValue(qpBytes);
                            }else if(action==ZERO) {
                                characteristic.setValue(zlBytes);
                            }else if(action==RESET) {
                                characteristic.setValue(bdBytes);
                            }
                            mBluetoothLeService.writeCharacteristic(characteristic);
                        }
                    }
                }
            }
        }
    }
    public void initWigher(Context context,String btAdd,OnWeighChangeListener listener){
        LogUtils.Log("initWigher()");
        this.btAdd=btAdd;
        weighChangeListener=listener;
        Intent gattServiceIntent = new Intent(context, BluetoothLeService.class);
        context.bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }
    public final  int DISCONNECTED=0;
    public final  int CONNECTING=1;
    public final  int CONNECTED=2;
    public void connectWeigh(String btAdd){
        if(mBluetoothLeService!=null) {
            mBluetoothLeService.connect(btAdd);
        }
    }
    public int getWeighState(){
        if(mBluetoothLeService==null) {
            return DISCONNECTED;
        }else  {
            return mBluetoothLeService.mConnectionState;
        }
    }
    public void registReceiver(Context context){
        context.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }
    public void unRegistReceiver(Context context){
        if(mGattUpdateReceiver!=null) {
            context.unregisterReceiver(mGattUpdateReceiver);
        }
    }

    public void unBindService(Context context){
        context.unbindService(mServiceConnection);
    }
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
    /**
     * 接收重量展示
     */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                LogUtils.Log("已连接");
                weighChangeListener.onWeighConnected();
                /*mConnected = true;
                updateConnectionState("已连接");*/

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                weighChangeListener.onWeighDiconnected();
                LogUtils.Log("未连接");
                /*mConnected = false;
                updateConnectionState("未连接");
                clearUI();*/
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {

                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =new ArrayList<BluetoothGattCharacteristic>();
            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                gattCharacteristicGroupData.add(currentCharaData);
                String targeUuid1="0000fff6";
                String targeUuid2="0000ffe1";
                if(gattCharacteristic.getUuid().toString().contains(targeUuid1)||gattCharacteristic.getUuid().toString().contains(targeUuid2)) {
                    mBluetoothLeService.setCharacteristicNotification(
                            gattCharacteristic, true);
                }
            }
        }
    }
    private void displayData(String data) {
        if (data != null&&weighChangeListener!=null) {
            weighChangeListener.onWeighChange(data);
        }
    }
    public  void initState(Context context, TextView notice, ImageView icon, int weightState){
        if(weightState==2) {
            notice.setText("已连接蓝牙电子秤");
            icon.setBackgroundResource(R.drawable.scanner_connected);
            icon.clearAnimation();
        }else if(weightState==0){
            notice.setText("连接失败,请检查电子秤是否开启");
            icon.setBackgroundResource(R.drawable.scanner_disconnected);
            icon.clearAnimation();
        }else if(weightState==1) {
            notice.setText("正在连接蓝牙电子秤");
            icon.setBackgroundResource(R.drawable.scanner_connecting);
            Utils.pbAnimation(context, icon);
        }
    }

}
