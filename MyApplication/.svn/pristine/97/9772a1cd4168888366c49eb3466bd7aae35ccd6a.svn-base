package com.yongle.letuiweipad.pagers.manager.printersetting;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.adapter.RecyclerAdapter;
import com.yongle.letuiweipad.basepager.ManagerBasePager;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.domain.BlueToothBean;
import com.yongle.letuiweipad.domain.printer.SavedPrinter;
import com.yongle.letuiweipad.selfinterface.RecyclerItemClickListener;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.NoticePopuUtils;
import com.yongle.letuiweipad.utils.SaveUtils;
import com.yongle.letuiweipad.utils.SpUtils;
import com.yongle.letuiweipad.utils.Utils;
import com.yongle.letuiweipad.utils.bluetoothprinter.AppInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/29 0029.
 */

public class AddBtPrinterPager extends ManagerBasePager {
    private static final String TAG = "AddBtPrinterPager";
    @BindView(R.id.tv_right)TextView tvRight;
    @BindView(R.id.tv_left) TextView tv_left;
    @BindView(R.id.tv_status) TextView tv_status;
    @BindView(R.id.pb_searching) ProgressBar pb_searching;
    @BindView(R.id.bt_list) RecyclerView bt_list;
    @BindView(R.id.al_research) LinearLayout al_research;
    private BluetoothAdapter bluetoothAdapter;
    private ArrayList<BlueToothBean> deviceList;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.e(TAG,"onReceive");
            if(!acceptreceiver) {
                return;
            }
            String action = intent.getAction();
            // 获得已经搜索到的蓝牙设备
            if(action.equals(BluetoothAdapter.STATE_ON)) {
                bluetoothAdapter.startDiscovery();
                Utils.pbAnimation(mActivity,pb_searching);

            }else if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                LogUtils.Log("找到设备："+device.getName()+" address="+device.getAddress());
                if(contains(device.getAddress())) {
                    return;
                }
                BlueToothBean blueToothBean=new BlueToothBean();
                blueToothBean.setDevice(device);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    blueToothBean.setState("已配对");
                }else if(device.getBondState()==BluetoothDevice.BOND_NONE) {
                    blueToothBean.setState("未配对");
                }
                deviceList.add(blueToothBean);
                adapter.notifyDataSetChanged();

            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                if(pb_searching!=null) {
                    pb_searching.clearAnimation();
                    pb_searching.setVisibility(View.GONE);
                }
                if(al_research!=null) {
                    al_research.setEnabled(true);
                }
                if(tv_status!=null) {
                    tv_status.setText("请选择要绑定的设备或者点击重新搜索");
                }
            }else if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                if(btDev!=null&&btDev.getBondState()==BluetoothDevice.BOND_BONDING) {
                    Utils.showWaittingDialog(mActivity,"正在和"+btDev.getName()+"配对,请耐心等待");
                }
                if(btDev!=null&&btDev.getBondState()==BluetoothDevice.BOND_BONDED) {
                    Utils.dismissWaittingDialog();
                    bluetoothAdapter.cancelDiscovery();
                    //输入备注的dialog
                    showRemarkDia();
                }
            }
        }
    };


    private RecyclerAdapter adapter;
    private BluetoothDevice btDev;
    private RecyclerItemClickListener onItemClickListener=new RecyclerItemClickListener() {
        @Override
        public void onItemClick(View view, int postiont) {
            pb_searching.clearAnimation();
            bluetoothAdapter.cancelDiscovery();
            btDev=deviceList.get(postiont).getDevice();
            startBond(btDev);
        }
    };
    private boolean acceptreceiver;

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
            showRemarkDia();
        }
    }

    private void showRemarkDia() {
        NoticePopuUtils.showInPutDia(mActivity, "请填写打印机备注名称",null, true, new NoticePopuUtils.OnFinishInput() {
            @Override
            public void onFinishInput(String msg) {
                SavedPrinter printDevices=new SavedPrinter();
                printDevices.setName(msg);
                printDevices.setType(1);
                printDevices.setBlueToothAdd(btDev.getAddress());
                printDevices.setPrintGroupId("0");
                printDevices.setPrintPermission("23457");
                printDevices.setPrintGroupName("全部分组");
                SaveUtils.saveObject(mActivity, Constant.BT_PRINTER, printDevices);
                AppInfo.btAddress=printDevices.getBlueToothAdd();
                SpUtils.getInstance(mActivity).save(Constant.bt_print_permission, "23457");
                setFragment(new AddedPrinterPager());
            }

            @Override
            public void onCancelInput() {

            }
        });

    }

    @Override
    public View initView() {
        LogUtils.e(TAG,"initView()");
        View totalView = View.inflate(mActivity, R.layout.add_btprinter_layout, null);
        return totalView;
    }

    @Override
    public void initData(int position) {
        acceptreceiver=true;
        LogUtils.e(TAG,"initData()");
        tvRight.setVisibility(View.GONE);
        tv_left.setText("打印设置");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("返回");

        LinearLayoutManager llManager = new LinearLayoutManager(mActivity);
        llManager.setOrientation(LinearLayoutManager.VERTICAL);
        bt_list.setLayoutManager(llManager);
        bt_list.setHasFixedSize(true);
        bt_list.addItemDecoration(new DividerItemDecoration(mActivity, LinearLayoutManager.VERTICAL));
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        deviceList=new ArrayList<>();
        adapter = new RecyclerAdapter(mActivity);
        adapter.setData(deviceList,RecyclerAdapter.BT_DEVICES);
        adapter.setItemClickListener(onItemClickListener);
        bt_list.setAdapter(adapter);
        regisitReceiver();
        if(!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }else {
            bluetoothAdapter.startDiscovery();
            Utils.pbAnimation(mActivity,pb_searching);
        }
    }
    /**
     *  注册用以接收到已搜索到的蓝牙设备的receiver
     */
    private void regisitReceiver() {
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        mFilter.addAction(BluetoothDevice.ACTION_FOUND);
        mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        mActivity.registerReceiver(mReceiver,mFilter);
    }
    @OnClick(R.id.al_research)
    void reSearch(View v){
        Utils.pbAnimation(mActivity,pb_searching);
        al_research.setEnabled(false);
        tv_status.setText("正在搜索周边蓝牙设备...");
        pb_searching.setVisibility(View.VISIBLE);
        bluetoothAdapter.startDiscovery();
        deviceList.clear();
        adapter.notifyDataSetChanged();
    }
    boolean contains(String address){
        if(deviceList==null||deviceList.size()<=0) {
            return false;
        }
        for (int i = 0; i < deviceList.size(); i++) {
            boolean equals = TextUtils.equals(address, deviceList.get(i).getDevice().getAddress());
            if(equals) {
                return true;
            }
        }
        return false;
    }
    @OnClick(R.id.tv_right)
    void backForward(View view){
        acceptreceiver=false;
        bluetoothAdapter.cancelDiscovery();
        setFragment(new AddedPrinterPager());
    }

    @Override
    public void onDestroy() {
        acceptreceiver=false;
        super.onDestroy();
    }
}
