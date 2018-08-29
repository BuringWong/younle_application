package com.younle.younle624.myapplication.activity.manager.printdeviceset;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.printsetting.SavedPrinter;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.utils.SaveUtils;
import com.younle.younle624.myapplication.utils.Utils;


/**
 * 添加新的打印设备的界面
 */
public class AddNewDeviceActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "AddNewDeviceActivity";
    private RelativeLayout arl_choose_yun_printer;
    private RelativeLayout arl_choose_bt_printer;
    private LinearLayout ll_cancel;
    private TextView tv_title;
    private ImageView iv_title;
    private View device_line;
    private NetWorks netWorks;
    private SavedPrinter printDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_device);
        netWorks = new NetWorks(this);
        Utils.initToolBarState(this);
        printDevice = (SavedPrinter) SaveUtils.getObject(this, Constant.BT_PRINTER);
        initView();
    }

    private void initView() {
        device_line = findViewById(R.id.device_line);
        arl_choose_bt_printer = (RelativeLayout)findViewById(R.id.arl_choose_bt_printer);
        arl_choose_yun_printer = (RelativeLayout)findViewById(R.id.arl_choose_yun_printer);
        ll_cancel = (LinearLayout)findViewById(R.id.ll_cancel);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("打印设置");
        iv_title = (ImageView)findViewById(R.id.iv_title);
        iv_title.setVisibility(View.VISIBLE);
        if(printDevice!=null) {
            arl_choose_bt_printer.setVisibility(View.GONE);
            device_line.setVisibility(View.GONE);
        }else {
            arl_choose_bt_printer.setVisibility(View.VISIBLE);
            device_line.setVisibility(View.VISIBLE);
        }
        ll_cancel.setOnClickListener(this);
        arl_choose_bt_printer.setOnClickListener(this);
        arl_choose_yun_printer.setOnClickListener(this);
    }

    /*@Override
    protected void onRestart() {
        if(printDevice!=null) {
            arl_choose_bt_printer.setVisibility(View.GONE);
            device_line.setVisibility(View.GONE);
        }else {
            arl_choose_bt_printer.setVisibility(View.VISIBLE);
            device_line.setVisibility(View.VISIBLE);
        }
        super.onRestart();
    }*/

    @Override
    protected void onResume() {
        printDevice = (SavedPrinter) SaveUtils.getObject(this, Constant.BT_PRINTER);
        if(printDevice!=null) {
            arl_choose_bt_printer.setVisibility(View.GONE);
            device_line.setVisibility(View.GONE);
        }else {
            arl_choose_bt_printer.setVisibility(View.VISIBLE);
            device_line.setVisibility(View.VISIBLE);
        }
        if(Constant.isfinishAddNewAc){
            Constant.isfinishAddNewAc = false;
            this.finish();
        }else{
            //重新取数据
        }
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_cancel:
                finish();
                break;
            case R.id.arl_choose_bt_printer ://添加蓝牙设备
                Intent intent = new Intent(this, AddBluetoothActivity.class);
                startActivity(intent);
                if(!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                    BluetoothAdapter.getDefaultAdapter().enable();
                }
                //finish();
                break;
            case R.id.arl_choose_yun_printer ://添加云设备
                startActivity(new Intent(this,AddRomateDeviceActivity.class));
                //finish();
                break;
        }
    }
}
