package com.younle.younle624.myapplication.activity.manager.weigher;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.printsetting.PrintDevices;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.SaveUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.utils.notice.NoticePopuUtils;



public class WeigherManagerActivity extends Activity {

    private PopupWindow scanner_connecting_pup;
    private BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
    private TextView tv_add_new_device,tv_added,barscanner_name,tv_delete_scanner;
    private RelativeLayout rl_device_info;
    private boolean findDev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.younle.younle624.myapplication.R.layout.activity_weigher_manager);
        regisitReceiver();
        if(!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
        ((TextView)findViewById(com.younle.younle624.myapplication.R.id.tv_title)).setText("电子秤设置");
        ((TextView)findViewById(R.id.tv_cancel)).setText("管理");
        tv_add_new_device = (TextView)findViewById(R.id.tv_add_new_device);

        PrintDevices savedDevice = (PrintDevices) SaveUtils.getObject(this, Constant.SAVED_BLUETOOTH_WEIGHER);

        tv_added = (TextView)findViewById(R.id.tv_added);
        rl_device_info = (RelativeLayout)findViewById(R.id.rl_device_info);
        barscanner_name = (TextView)findViewById(R.id.barscanner_name);
        tv_delete_scanner = (TextView)findViewById(R.id.tv_delete_scanner);
        if(savedDevice==null) {
            tv_add_new_device.setVisibility(View.VISIBLE);
            rl_device_info.setVisibility(View.GONE);
            tv_added.setVisibility(View.GONE);
        }else {
            tv_add_new_device.setVisibility(View.GONE);
            rl_device_info.setVisibility(View.VISIBLE);
            tv_added.setVisibility(View.VISIBLE);
            barscanner_name.setText(bluetoothAdapter.getRemoteDevice(savedDevice.getBlueToothAdd()).getName());
        }

        setListerner();
    }

    private void setListerner() {
        findViewById(R.id.ll_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_add_new_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findDev=false;
                showScannerStateDia();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bluetoothAdapter.cancelDiscovery();
                    }
                },10000);
                bluetoothAdapter.startDiscovery();
            }
        });
        tv_delete_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDelPup("确定要删除该设备吗");
            }
        });
    }
    public void showDelPup(String msg) {
        NoticePopuUtils.showBindPup(this, msg, R.id.al_weigher, new NoticePopuUtils.OnClickCallBack() {
            @Override
            public void onClickYes() {
                SaveUtils.saveObject(WeigherManagerActivity.this, Constant.SAVED_BLUETOOTH_WEIGHER, null);
                rl_device_info.setVisibility(View.GONE);
                tv_added.setVisibility(View.GONE);
                tv_add_new_device.setVisibility(View.VISIBLE);
            }
            @Override
            public void onClickNo() {
                LogUtils.Log("取消删除");
            }
        });
    }

    private void showScannerStateDia() {
        View scanner_state_view = View.inflate(this, R.layout.bar_scanner_setting, null);
        TextView tv_notice= (TextView) scanner_state_view.findViewById(R.id.tv_scanner_state);
        tv_notice.setText("正在匹配电子秤...");
        ImageView iv_loading= (ImageView) scanner_state_view.findViewById(R.id.iv_loading);
        Utils.pbAnimation(this, iv_loading);

        scanner_connecting_pup = new PopupWindow(scanner_state_view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha=0.4f;
        getWindow().setAttributes(params);
        scanner_connecting_pup.setBackgroundDrawable(new BitmapDrawable());
        scanner_connecting_pup.setOutsideTouchable(true);
        scanner_connecting_pup.setFocusable(true);
        scanner_connecting_pup.showAtLocation(findViewById(R.id.al_weigher), Gravity.CENTER, 0, 0);
        scanner_connecting_pup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1;
                getWindow().setAttributes(params);
            }
        });
    }
    private void regisitReceiver() {
        IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        mFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        registerReceiver(mReceiver, mFilter);
    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 获得已经搜索到的蓝牙设备
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                handleDevice(device);
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                dismissDia();
                LogUtils.Log("停止搜索");
                setProgressBarIndeterminateVisibility(false);

            }else if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                LogUtils.Log("已连接");

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                LogUtils.Log("未连接");
                /*mConnected = false;
                updateConnectionState("未连接");
                clearUI();*/
            }
        }
    };

    private void handleDevice(BluetoothDevice device) {
        String name = device.getName();
        LogUtils.Log("找到了蓝牙设备:"+ name +" address="+device.getAddress());
        if (name!=null&&name.length() > 4 ) {
            if(name.startsWith("ST-BL")||name.startsWith("FSRK")||name.startsWith(Constant.WEIGHER_NAME)) {
                findDev=true;
                PrintDevices weigh  = new PrintDevices("", device.getAddress(), "蓝牙电子秤");
                SaveUtils.saveObject(WeigherManagerActivity.this, Constant.SAVED_BLUETOOTH_WEIGHER,weigh);
                Constant.SAVED_WEIGH_NAME =name;
                bluetoothAdapter.cancelDiscovery();
                updateView(device);
            }
        }
    }

    private void updateView(BluetoothDevice device) {
        //1.popuwindow消失
        dismissDia();
        tv_added.setVisibility(View.VISIBLE);
        tv_add_new_device.setVisibility(View.GONE);
        rl_device_info.setVisibility(View.VISIBLE);
        LogUtils.Log("待保存的设备名称：" + device.getName());
        barscanner_name.setText(device.getName());
    }

    private void dismissDia() {
        if(!findDev) {
            Utils.showToast(this,"未检索到蓝牙电子秤，请确认电子处于开启状态",2000);
        }
        if(scanner_connecting_pup!=null&&scanner_connecting_pup.isShowing()) {
            scanner_connecting_pup.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        if(mReceiver!=null) {
            unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }
}
