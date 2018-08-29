package com.yongle.letuiweipad.pagers.manager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.basepager.ManagerBasePager;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.domain.PrintDevices;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.NoticePopuUtils;
import com.yongle.letuiweipad.utils.SaveUtils;
import com.yongle.letuiweipad.utils.SpUtils;
import com.yongle.letuiweipad.utils.Utils;
import com.yongle.letuiweipad.utils.scanbar.HidConncetUtil;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/24 0024.
 */

public class ScannerSettingPager extends ManagerBasePager {
    @BindView(R.id.tv_left)TextView tvLeft;
    @BindView(R.id.tv_right)TextView tvRight;
    @BindView(R.id.add_device)TextView addDevice;
    @BindView(R.id.tv_added_devices)TextView tv_added_scanner;
    @BindView(R.id.ll_howto_add)LinearLayout ll_howto_add;
    private PrintDevices savedScannerDevice;
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            bluetoothAdapter.stopLeScan(callback);
            if(msg.what==1) {
                setFailure();
            }
            handler.removeMessages(1);
        }
    };
    private String bleName;
    private BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
    private BluetoothDevice btDev;
    private View baseView;
    private BluetoothAdapter.LeScanCallback callback=new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            LogUtils.Log("找到device=="+device.getName());
            isScannerDevice(device);
        }
    };

    private void setFailure() {
        Toast.makeText(mActivity, "连接失败,请重试！", Toast.LENGTH_SHORT).show();
        bluetoothAdapter.cancelDiscovery();
        Utils.dismissWaittingDialog();
    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.Log("MANAGER_POSITION=="+Constant.MANAGER_POSITION);
            if(Constant.MANAGER_POSITION!=3) {
                return;
            }
            String action = intent.getAction();
            // 获得已经搜索到的蓝牙设备
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                LogUtils.Log("找到device=="+device.getName());
                isScannerDevice(device);
            } else if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                LogUtils.Log("开始搜索周边蓝牙设备");
            }else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                LogUtils.Log("结束搜索周边蓝牙设备");
                mActivity.setProgressBarIndeterminateVisibility(false);
//                setFailure();
            }else if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice bondDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                LogUtils.Log("绑定状态变更了");
                if(bondDevice.getBondState()==BluetoothDevice.BOND_BONDING) {
                    LogUtils.Log("绑定中");
                }
                if(bondDevice.getBondState()==BluetoothDevice.BOND_BONDED) {
                    LogUtils.Log("已绑定");
                    bluetoothAdapter.cancelDiscovery();
                    connect(bondDevice);
                    success(bondDevice);
                }
                if(bondDevice.getBondState()==BluetoothDevice.BOND_NONE){
                    if(handler!=null) {
                        handler.removeCallbacksAndMessages(null);
                    }
                    setFailure();
                }
            }else if(action.equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)) {
                int conectState = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, BluetoothAdapter.ERROR);
                final BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(conectState==0) {
                }else if(conectState==1) {
                }else if(conectState==2) {
                    handler.removeCallbacksAndMessages(null);
                    //更新视图,并保存状态
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateView(device);
                        }
                    });
                }
            }
        }
    };

    private void success(BluetoothDevice device) {

        Utils.dismissWaittingDialog();
        /*boolean scannerConnectState = HidConncetUtil.getScannerConnectState(device);
        if(scannerConnectState) {
            SelfToast.showToast(mActivity,"扫码设备连接成功",1000,true);
        }*/
    }

    private void updateView(BluetoothDevice device) {
        Constant.SCANNER_NAME=device.getName();
        SpUtils.getInstance(mActivity).save(Constant.BT_SCANNER_NAME,device.getName());
        //1.popuwindow消失
        Utils.dismissWaittingDialog();
        LogUtils.Log("待保存的设备名称：" + device.getName());
        savedScannerDevice  = new PrintDevices(device.getName(), device.getAddress(), "蓝牙扫码枪");
        SaveUtils.saveObject(mActivity, Constant.SAVED_BLUETOOTH_SCANNER,savedScannerDevice);

        ll_howto_add.setVisibility(View.GONE);
        tvRight.setVisibility(View.VISIBLE);
        tv_added_scanner.setVisibility(View.VISIBLE);
        tv_added_scanner.setText("已绑定设备："+device.getName());

    }
    @Override
    public View initView() {
        baseView = View.inflate(mActivity, R.layout.scanner_setting_layout, null);
        return baseView;
    }

    @Override
    public void initData(int position) {
        LogUtils.Log("扫码枪设置 initData="+position);

        tvLeft.setText("扫码枪设置");
        savedScannerDevice = (PrintDevices) SaveUtils.getObject(mActivity, Constant.SAVED_BLUETOOTH_SCANNER);
        if(savedScannerDevice==null) {
            tvRight.setVisibility(View.GONE);
            ll_howto_add.setVisibility(View.VISIBLE);
            tv_added_scanner.setVisibility(View.GONE);
        }else {
            ll_howto_add.setVisibility(View.GONE);
            tvRight.setVisibility(View.VISIBLE);
            tv_added_scanner.setVisibility(View.VISIBLE);
            String name = bluetoothAdapter.getRemoteDevice(savedScannerDevice.getBlueToothAdd()).getName();
            tv_added_scanner.setText("已绑定设备："+name);
        }
        regisitReceiver();


    }



    @OnClick(R.id.add_device)
    public void onViewClicked() {
//        bluetoothAdapter.startLeScan(callback);
        boolean startDiscovery = bluetoothAdapter.startDiscovery();//开始搜索周边蓝牙设备
        LogUtils.Log("正在搜索扫码设备："+startDiscovery);
        Utils.showWaittingDialog(mActivity,"正在搜索扫码设备...");
        handler.sendEmptyMessageDelayed(1,20000);
    }

    @OnClick(R.id.tv_right)
    void delDevice(){
        NoticePopuUtils.showBindPup(mActivity, "确定要删除该设备吗？", R.id.scanner_base, new NoticePopuUtils.OnClickCallBack() {
            @Override
            public void onClickYes() {
                SpUtils.getInstance(mActivity).save(Constant.BT_SCANNER_NAME,null);

                unBindBluetooth();
                SaveUtils.saveObject(mActivity, Constant.SAVED_BLUETOOTH_SCANNER, null);
                tvRight.setVisibility(View.GONE);
                ll_howto_add.setVisibility(View.VISIBLE);
                tv_added_scanner.setVisibility(View.GONE);
            }

            @Override
            public void onClickNo() {
                LogUtils.Log("取消删除");
            }
        });
    }
    private void unBindBluetooth() {
        Set<BluetoothDevice> bondedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        Iterator<BluetoothDevice> iterator = bondedDevices.iterator();
        while (iterator.hasNext()){
            BluetoothDevice bluetoothDevice = iterator.next();
            String blueToothAdd = savedScannerDevice.getBlueToothAdd();
            if(bluetoothDevice.getAddress().equals(blueToothAdd)) {
                Boolean returnValue = false;
                Method createBond = null;
                try {
                    createBond = BluetoothDevice.class.getMethod("removeBond");
                    returnValue= (Boolean) createBond.invoke(bluetoothDevice);
                    LogUtils.Log("returnValue:" + returnValue);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.Log("解除绑定："+e.toString());
                }
            }
        }
    }
    private void isScannerDevice(BluetoothDevice btDev) {
        int majorDeviceClass = btDev.getBluetoothClass().getMajorDeviceClass();

        bleName +="\n"+(btDev.getName()+"   majorDeviceClass:"+majorDeviceClass);
        if(majorDeviceClass== BluetoothClass.Device.Major.PERIPHERAL) {
            bluetoothAdapter.cancelDiscovery();
            this.btDev = btDev;
            LogUtils.Log("btDev="+btDev.getName());
            Utils.dismissWaittingDialog();
            Utils.showWaittingDialog(mActivity,"正在连接扫码设备...");
            startBond(btDev);
        }
    }
    private void regisitReceiver() {
        IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        mFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        mFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        mFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        mActivity.registerReceiver(mReceiver,mFilter);
    }
    private void startBond(BluetoothDevice btDev) {
        if (btDev.getBondState() == BluetoothDevice.BOND_NONE) {
            try {
                Boolean returnValue = false;
                Method createBond = BluetoothDevice.class.getMethod("createBond");
                returnValue = (Boolean) createBond.invoke(btDev);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (btDev.getBondState() == BluetoothDevice.BOND_BONDED) {
            connect(btDev);
        }
    }
    public void connect(BluetoothDevice btDev){
        HidConncetUtil hidConncetUtil=new HidConncetUtil(mActivity);
        hidConncetUtil.connect(btDev);
    }

    @Override
    public void onDestroy() {
        HidConncetUtil hidConncetUtil=new HidConncetUtil(mActivity);
        hidConncetUtil.disConnect(btDev);
        handler.removeCallbacksAndMessages(null);
        try {
            if(mReceiver!=null) {
                mActivity.unregisterReceiver(mReceiver);
            }
        }catch (Exception e){

        }

        super.onDestroy();
    }


}
