package com.yongle.letuiweipad.pagers.manager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.activity.MainActivity;
import com.yongle.letuiweipad.activity.manager.weigher.BluetoothLeService;
import com.yongle.letuiweipad.activity.manager.weigher.WeighManager;
import com.yongle.letuiweipad.basepager.ManagerBasePager;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.domain.PrintDevices;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.NoticePopuUtils;
import com.yongle.letuiweipad.utils.SaveUtils;
import com.yongle.letuiweipad.utils.Utils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/24 0024.
 */

public class WeigherSettingPager extends ManagerBasePager {

    @BindView(R.id.tv_left)TextView tvLeft;
    @BindView(R.id.tv_right)TextView tvRight;
    @BindView(R.id.tv1)TextView tv1;
    @BindView(R.id.step_one)TextView stepOne;
    @BindView(R.id.step_two)TextView stepTwo;
    @BindView(R.id.add_device)TextView addDevice;
    @BindView(R.id.ll_howto_add)AutoLinearLayout llHowtoAdd;
    @BindView(R.id.tv_added_devices) TextView tvAddedDevices;
    @BindView(R.id.scanner_base)AutoRelativeLayout scannerBase;
    @BindView(R.id.weigh_tare) TextView weighReset;
    private View baseView;
    private BluetoothAdapter bluetoothAdapter;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(Constant.MANAGER_POSITION!=4) {
                return;
            }
            String action = intent.getAction();
            // 获得已经搜索到的蓝牙设备
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                handleDevice(device);
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                dismissDia();
                LogUtils.Log("停止搜索");
                mActivity.setProgressBarIndeterminateVisibility(false);

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
        if(findDev) {
            return;
        }
        String name = device.getName();
        LogUtils.Log("找到了蓝牙设备:"+ name +" address="+device.getAddress());
        if(name!=null&&name.length()>4) {
            boolean targert= name.startsWith("ST-BL")||TextUtils.equals("FSRK",name.substring(0, 4))||TextUtils.equals("PRIS",name.substring(0,4))||name.contains(Constant.WEIGHER_NAME);
            if(targert) {
                scanLeDevice(false);
                findDev=true;
                PrintDevices weigh  = new PrintDevices(name, device.getAddress(), "蓝牙电子秤");
                SaveUtils.saveObject(mActivity, Constant.SAVED_BLUETOOTH_WEIGHER,weigh);
                Constant.SAVED_WEIGH_NAME =name;
                if(Constant.SAVED_WEIGH_NAME==null) {
                    Constant.SAVED_WEIGH_NAME="";
                }
//                bluetoothAdapter.cancelDiscovery();
                updateView(device);
                ((MainActivity)mActivity).initBleWeigher();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    bluetoothAdapter.stopLeScan(new BluetoothAdapter.LeScanCallback() {
                        @Override
                        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                            LogUtils.e("stop","stopLeScan=="+device.getName());
                        }
                    });
                }
            }
        }
    }

    private boolean findDev;
    private PrintDevices savedDevice;

    @Override
    public View initView() {
        baseView = View.inflate(mActivity, R.layout.scanner_setting_layout, null);
        return baseView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    private Handler mHandler;
    @Override
    public void initData(int position) {
        LogUtils.Log("电子秤initData() position="+position);
        mHandler=new Handler();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        tvLeft.setText("电子秤设置");
        tv1.setText("如何添加蓝牙电子秤");
        stepOne.setText("第一步：启动电子秤，进入配对模式（请参照电子秤的说明书）");
        stepTwo.setText("第二步：点击下方“添加电子秤设备”，等待连接成功。");
        addDevice.setText("+添加电子秤设备");

        regisitReceiver();
        if(!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
        savedDevice = (PrintDevices) SaveUtils.getObject(mActivity, Constant.SAVED_BLUETOOTH_WEIGHER);
        if(savedDevice ==null) {
            tvRight.setVisibility(View.GONE);
            llHowtoAdd.setVisibility(View.VISIBLE);
            tvAddedDevices.setVisibility(View.GONE);
        }else {
            llHowtoAdd.setVisibility(View.GONE);
            tvRight.setVisibility(View.VISIBLE);
            tvAddedDevices.setVisibility(View.VISIBLE);
            String name = bluetoothAdapter.getRemoteDevice(savedDevice.getBlueToothAdd()).getName();
            tvAddedDevices.setText("已绑定设备："+name);
        }
    }
    private void updateView(BluetoothDevice device) {
        //1.popuwindow消失
        llHowtoAdd.setVisibility(View.GONE);
        tvRight.setVisibility(View.VISIBLE);
        tvAddedDevices.setVisibility(View.VISIBLE);
        tvAddedDevices.setText("已绑定设备："+device.getName());
        savedDevice = (PrintDevices) SaveUtils.getObject(mActivity, Constant.SAVED_BLUETOOTH_WEIGHER);
        dismissDia();

    }

    public final String TAG="weigherSettingPager";
    private void regisitReceiver() {
        IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        mFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        mActivity.registerReceiver(mReceiver, mFilter);
    }

    @OnClick({R.id.tv_right, R.id.add_device,R.id.weigh_tare,R.id.weigh_zero,R.id.weigh_reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                showDelPup("确定要删除该设备吗?");
                break;
            case R.id.add_device:
                findDev=false;
//                showScannerStateDia();
                Utils.showWaittingDialog(mActivity,"正在搜索电子秤...");
//                boolean startDiscovery = bluetoothAdapter.startDiscovery();
                scanLeDevice(true);
                break;
            case R.id.weigh_tare:
                WeighManager.getInstance().resetWeigher(WeighManager.TARE);
                break;
            case R.id.weigh_zero:
                WeighManager.getInstance().resetWeigher(WeighManager.ZERO);
                break;
            case R.id.weigh_reset:
                WeighManager.getInstance().resetWeigher(WeighManager.RESET);
                break;
        }
    }

    /**
     *搜索或者停止搜素
     */
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bluetoothAdapter.stopLeScan(mLeScanCallback);
                    dismissDia();
                }
            }, 10000);

            bluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            bluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }
    private BluetoothAdapter.LeScanCallback mLeScanCallback =new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    handleDevice(device);
                }
            };


    public void showDelPup(String msg) {
        NoticePopuUtils.showBindPup(mActivity, msg, R.id.scanner_base, new NoticePopuUtils.OnClickCallBack() {
            @Override
            public void onClickYes() {
                unBindBluetooth();
                SaveUtils.saveObject(mActivity, Constant.SAVED_BLUETOOTH_WEIGHER, null);
                tvRight.setVisibility(View.GONE);
                llHowtoAdd.setVisibility(View.VISIBLE);
                tvAddedDevices.setVisibility(View.GONE);
                ((MainActivity)mActivity).weigherDel();
            }
            @Override
            public void onClickNo() {
                LogUtils.Log("取消删除");
            }
        });
    }
    private void unBindBluetooth() {
        WeighManager.getInstance().disConnect();
        Set<BluetoothDevice> bondedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        Iterator<BluetoothDevice> iterator = bondedDevices.iterator();
        while (iterator.hasNext()){
            BluetoothDevice bluetoothDevice = iterator.next();
            String blueToothAdd = savedDevice.getBlueToothAdd();
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
    private void dismissDia() {
        if(!findDev) {
            Utils.showToast(mActivity,"未检索到蓝牙电子秤，请确认电子处于开启状态",2000);
        }
        Utils.dismissWaittingDialog();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mReceiver!=null) {
            mActivity.unregisterReceiver(mReceiver);
        }
    }
}
