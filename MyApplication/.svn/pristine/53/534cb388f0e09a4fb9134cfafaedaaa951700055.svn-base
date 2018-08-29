package com.yongle.letuiweipad.activity.manager.weigher;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.Utils;

import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by Administrator on 2017/7/11.
 */

public  class WeighManager {
    public static final int TARE = 0;
    public static final int ZERO = 1;
    public static final int RESET= 2;
    private BluetoothLeService mBluetoothLeService;
    private String btAdd;
    private OnWeighChangeListener weighChangeListener;
    private static final String TAG = "WeighManager";
    public static WeighManager instance;
//    public boolean isServiceConnected;
    public final ServiceConnection mServiceConnection = new ServiceConnection() {
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
    private boolean everRegist;

    public static WeighManager getInstance(){
        if(instance==null) {
            instance=new WeighManager();
        }
        return instance;
    }
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){

        }
    };
    public void initWigher(Context context, String btAdd, OnWeighChangeListener listener){
        LogUtils.Log("initWigher()");
        if(mBluetoothLeService!=null) {
            LogUtils.Log("直接连接。。");
            connectWeigh(btAdd);
            return;
        }
        this.btAdd=btAdd;
        weighChangeListener=listener;
        Intent gattServiceIntent = new Intent(context, BluetoothLeService.class);
        context.bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }
    public final  int DISCONNECTED=0;
    public final  int CONNECTING=1;
    public final  int CONNECTED=2;

    public void connectWeigh(String btAdd){
        LogUtils.Log("connectWeigh()");
        if(mBluetoothLeService!=null) {
            mBluetoothLeService.connect(btAdd);
        }
    }
    public void disConnect(){
        if(mBluetoothLeService!=null) {
            mBluetoothLeService.disconnect();
        }
    }

    public static String Service_uuid = "0000ffe0-0000-1000-8000-00805f9b34fb";
    public static String Characteristic_uuid_TX = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static String Characteristic_uuid_RX = "0000ffe1-0000-1000-8000-00805f9b34fb";

    public void resetWeigher(int action){
        LogUtils.e(TAG,"resetWeigher()");
        if(mBluetoothLeService!=null) {
            List<BluetoothGattService> services = mBluetoothLeService.getSupportedGattServices();
            for (BluetoothGattService bluetoothGattService :services){
                LogUtils.e(TAG,"service_uuid=="+bluetoothGattService.getUuid());
                if((bluetoothGattService.getUuid().toString()).equals(Service_uuid)) {
                    List<BluetoothGattCharacteristic> characteristics = bluetoothGattService.getCharacteristics();
                    for(BluetoothGattCharacteristic characteristic:characteristics){
                        LogUtils.e(TAG,"character_uuid=="+characteristic.getUuid());
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

    public int getWeighState(){
        if(mBluetoothLeService==null) {
            return DISCONNECTED;
        }else  {
            return mBluetoothLeService.mConnectionState;
        }
    }
    public void registReceiver(Context context){
        everRegist = true;
        context.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }
    public void unRegistReceiver(Context context){
        LogUtils.e(TAG,"everReister=="+everRegist);
        if(everRegist) {
            if(mGattUpdateReceiver!=null) {
                context.unregisterReceiver(mGattUpdateReceiver);
            }
        }
    }

    public void unBindService(Context context){
        try {
            if(mServiceConnection!=null) {
                context.unbindService(mServiceConnection);
            }
        }catch (Exception e){
            LogUtils.e("WeighManager",e.toString());
        }

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
            LogUtils.e(TAG,"action="+action);
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                if(weighChangeListener!=null) {
                    weighChangeListener.onWeighConnected();
                }

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                /*if(Constant.MAIN_POSITION==0||Constant.MAIN_POSITION==1) {
                    if(weighChangeListener!=null) {
                        weighChangeListener.onWeighDiconnected();
                    }
                }*/
                if(weighChangeListener!=null) {
                    weighChangeListener.onWeighDiconnected();
                }
            } else if(BluetoothLeService.ACTION_GATT_CONNECTING.equals(action)) {
                if(weighChangeListener!=null) {
                    weighChangeListener.onWeighConnecting();
                }
            }else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {

                displayGattServices(mBluetoothLeService.getSupportedGattServices());

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };
    private  void displayGattServices(List<BluetoothGattService> gattServices) {
        LogUtils.e(TAG,"displayGattServices()");
        try {
            if (gattServices == null) return;
            for (BluetoothGattService gattService : gattServices) {
                List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
                for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                    String uuid = gattCharacteristic.getUuid().toString();
                    String targeUuid1="0000fff6";
                    String targeUuid2="0000ffe1";
                    if(uuid.contains(targeUuid1)|| uuid.contains(targeUuid2)) {
                        LogUtils.e(TAG,"检测到目标");
                        mBluetoothLeService.setCharacteristicNotification(
                                gattCharacteristic, true);
                    }
                }
            }
        }catch (Exception e){
            LogUtils.e(TAG,e.toString());
        }

    }
    private void displayData(String data) {
        if (data != null&&weighChangeListener!=null) {
            weighChangeListener.onWeighChange(data);
        }
    }
    public  void initState(Context context, LinearLayout llWeigherState, ImageView weigherState, int weightState){

        if(weightState==2) {
            llWeigherState.setBackgroundResource(R.drawable.scanner_state_connect);
            weigherState.setBackgroundResource(R.drawable.connected);
            weigherState.clearAnimation();
        }else if(weightState==0){
            llWeigherState.setBackgroundResource(R.drawable.scanner_state_disconnect);
            weigherState.setBackgroundResource(R.drawable.dicconnected);
            weigherState.clearAnimation();
        }else if(weightState==1) {
            llWeigherState.setBackgroundResource(R.drawable.scanner_state_disconnect);
            weigherState.setBackgroundResource(R.drawable.connecting);
            Utils.pbAnimation(context, weigherState);
        }
    }

}
