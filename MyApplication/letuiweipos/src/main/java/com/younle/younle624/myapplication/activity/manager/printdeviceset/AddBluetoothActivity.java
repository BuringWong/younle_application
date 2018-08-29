package com.younle.younle624.myapplication.activity.manager.printdeviceset;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.printsetting.BlueToothBean;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.view.SelfLinearLayout;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 添加蓝牙打印机的界面
 */
public class AddBluetoothActivity extends Activity implements View.OnClickListener {
    private  int BLUETOOTH_OPENING = 1;
    private ListView lv_device_name;
    private List<BlueToothBean> deviceList=new ArrayList<>();
    private List<BluetoothDevice> testList=new ArrayList<>();
    private TextView tv_status;
    private ProgressBar pb_searching;
    private ProgressBar pb_loading;
    private TextView tv_title;
    private ImageView iv_title;
    private TextView tv_loading;
    private SelfLinearLayout ll_loading;
    private String TAG="AddBluetoothActivity";
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 获得已经搜索到的蓝牙设备
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BlueToothBean blueToothBean=new BlueToothBean();
                blueToothBean.setDevice(device);
                LogUtils.Log("找到设备："+device.getName()+" address="+device.getAddress());
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    blueToothBean.setState("已配对");
                }else if(device.getBondState()==BluetoothDevice.BOND_NONE) {
                    blueToothBean.setState("未配对");
                }
                if(!deviceList.contains(blueToothBean)) {
                    deviceList.add(blueToothBean);
                    adapter.notifyDataSetChanged();
/*
                    if(AddBluetoothActivity.this.action==0||AddBluetoothActivity.this.action==1) {
                    }*/
                }
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                al_research.setEnabled(true);
                tv_status.setText("请选择要绑定的设备或者点击重新搜索");
                pb_searching.setVisibility(View.GONE);
                setProgressBarIndeterminateVisibility(false);
                setTitle("搜索蓝牙设备");
            }else if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                if(btDev!=null&&btDev.getBondState()==BluetoothDevice.BOND_BONDING) {
                    ll_loading.setVisibility(View.VISIBLE);
                    tv_loading.setText("正在和"+btDev.getName()+"配对,请耐心等待");
                }
                if(btDev!=null&&btDev.getBondState()==BluetoothDevice.BOND_BONDED) {
                    ll_loading.setVisibility(View.GONE);
                    bluetoothAdapter.cancelDiscovery();
                    toRemarkDeviceActivity();
                }
            }
        }
    };

    private BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
    private DeviceChooseAdapter adapter;
    private BluetoothDevice btDev;
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case Constant.BLUETOOTH_OPENING :
                    int state = bluetoothAdapter.getState();
                    if(state==BluetoothAdapter.STATE_ON) {
                        if(btOpeningDia!=null&&btOpeningDia.isShowing()) {
                            btOpeningDia.dismiss();
                        }
                        initData();
                        handler.removeMessages(BLUETOOTH_OPENING);
                    }else {
                        handler.sendEmptyMessageDelayed(Constant.BLUETOOTH_OPENING,200);
                    }
                    break;
            }
        }
    };
    private AlertDialog btOpeningDia;
    private LinearLayout al_research;
    private int action;
    private BluetoothHeadset headSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bluetooth);
        Utils.initToolBarState(this);
        action = getIntent().getIntExtra("action", -1);
        initView();
        setListener();
        openBluetooth();

    }

    private void openBluetooth() {
        if(!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
            btOpeningDia= Utils.wybDialog(AddBluetoothActivity.this, false, R.layout.bluetooth_opening_dia, 0,
                    Utils.dip2px(AddBluetoothActivity.this, 200), Utils.dip2px(AddBluetoothActivity.this, 124), "蓝牙开启中");
            handler.sendEmptyMessageDelayed(Constant.BLUETOOTH_OPENING,200);
        }else {
            initData();
        }
    }

    private void initData() {
        adapter = new DeviceChooseAdapter();
        regisitReceiver();
        lv_device_name.setAdapter(adapter);
    }

    private void initView() {
        lv_device_name = (ListView)findViewById(R.id.lv_device_name);
        tv_status = (TextView)findViewById(R.id.tv_status);
        pb_searching = (ProgressBar)findViewById(R.id.pb_searching);
        pb_loading = (ProgressBar)findViewById(R.id.pb_loading);
        ll_loading = (SelfLinearLayout)findViewById(R.id.ll_loading);
        ll_loading.setVisibility(View.GONE);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("蓝牙设备");
        iv_title = (ImageView)findViewById(R.id.iv_title);
        iv_title.setVisibility(View.VISIBLE);
        tv_loading = (TextView)findViewById(R.id.tv_loading);
        al_research = (LinearLayout) findViewById(R.id.al_research);
        al_research.setEnabled(false);
    }

    private void setListener() {
        iv_title.setOnClickListener(this);
        al_research.setOnClickListener(this);
    }
    /**
     *  注册用以接收到已搜索到的蓝牙设备的receiver
     */
    private void regisitReceiver() {
        IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, mFilter);
        // 注册搜索完时的receiver
        mFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, mFilter);
        mFilter=new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mReceiver,mFilter);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.startDiscovery();//开始搜索周边蓝牙设备
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title :
                finish();
                break;
            case R.id.al_research:
                //重新搜索
                al_research.setEnabled(false);
                tv_status.setText("正在搜索周边蓝牙设备...");
                pb_searching.setVisibility(View.VISIBLE);
                bluetoothAdapter.startDiscovery();
                deviceList.clear();
                testList.clear();
                break;
        }
    }
    class DeviceChooseAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return deviceList.size();
        }

        @Override
        public Object getItem(int position) {
            return deviceList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null) {
                convertView = View.inflate(AddBluetoothActivity.this, R.layout.bluetooth_device_choose_item, null);
                holder=new ViewHolder();
                holder.tv_bluetooth_name= (TextView) convertView.findViewById(R.id.tv_bluetooth_name);
                holder.bluetooth_devided_line=convertView.findViewById(R.id.bluetooth_devided_line);
                holder.tv_device_state= (TextView) convertView.findViewById(R.id.tv_device_state);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }
            if(position==deviceList.size()-1) {
                holder.bluetooth_devided_line.setVisibility(View.GONE);
            }else {
                holder.bluetooth_devided_line.setVisibility(View.VISIBLE);
            }
            final BlueToothBean blueToothBean = deviceList.get(position);
            if(blueToothBean.getDevice().getName()==null) {
                holder.tv_bluetooth_name.setText("未命名设备");
            }else {
                holder.tv_bluetooth_name.setText(blueToothBean.getDevice().getName());
            }
            holder.tv_device_state.setText("("+blueToothBean.getState()+")");
            if("未配对".equals(blueToothBean.getState())) {
                holder.tv_device_state.setTextColor(Color.rgb(41,41,41));
            }else {
                holder.tv_device_state.setTextColor(Color.GREEN);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bluetoothAdapter.cancelDiscovery();
                    btDev = bluetoothAdapter.getRemoteDevice(blueToothBean.getDevice().getAddress());
                    startBond(btDev);
                }
            });
            return convertView;
        }
        class ViewHolder{
            TextView tv_bluetooth_name;
            View bluetooth_devided_line;
            TextView tv_device_state;
        }
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
            toRemarkDeviceActivity();
        }
    }

    private void toRemarkDeviceActivity() {
        Intent toNextIntent = new Intent(AddBluetoothActivity.this, RemarkDeviceActivity.class);
        toNextIntent.putExtra(Constant.BLUETOOTH_DEVICE, btDev);
        toNextIntent.putExtra("action",action);
        AddBluetoothActivity.this.startActivity(toNextIntent);
        AddBluetoothActivity.this.finish();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            bluetoothAdapter.cancelDiscovery();
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        //解除注册
        handler.removeCallbacksAndMessages(null);
        if(mReceiver!=null) {
            unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }
}
