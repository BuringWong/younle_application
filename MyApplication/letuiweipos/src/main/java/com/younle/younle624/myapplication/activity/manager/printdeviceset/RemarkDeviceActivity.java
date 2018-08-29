package com.younle.younle624.myapplication.activity.manager.printdeviceset;


import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.manager.orderpager.bluetoothprinter.AppInfo;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.printsetting.SavedPrinter;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.SaveUtils;
import com.younle.younle624.myapplication.utils.SpUtils;
import com.younle.younle624.myapplication.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 从设置界面帮定了其他的蓝牙
 */
public class RemarkDeviceActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "RemarkDeviceActivity";
    private TextView tv_device_name;
    private BluetoothDevice bluetoothDevice;
    private TextView tv_title;
    private EditText et_newname;
    private int fromwhere = 0;
    private int which_printer = 0;////1本地打印机 2蓝牙打印机 3云打印机
    private String terminal_name = "";
    private String terminal_number = "";
    private String terminal_key = "";
    private SavedPrinter printDevices;
    private Button btn_confirm;
    private LinearLayout ll_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark_device);
        Utils.initToolBarState(this);
        getDeviceInfo();
        initView();
        setListener();
    }

    private void getDeviceInfo() {
        bluetoothDevice = getIntent().getParcelableExtra(Constant.BLUETOOTH_DEVICE);
        which_printer = getIntent().getIntExtra(Constant.WHICH_PRINTER,0);
        fromwhere = getIntent().getIntExtra(Constant.CHANGE_EDITEXT_INFO,0);
        if(fromwhere==1) {//修改名称
            if(which_printer==3){//云打印修改名称
                terminal_name = getIntent().getStringExtra("terminal_name");
                terminal_number = getIntent().getStringExtra("terminal_number");
                terminal_key = getIntent().getStringExtra("terminal_key");
            }else {//蓝牙修改名称
                LogUtils.e(TAG,"getDeviceInfo ()蓝牙修改名称");
                printDevices = (SavedPrinter) SaveUtils.getObject(this, Constant.BT_PRINTER);
            }
        }else if(fromwhere==2){//云打印修改终端号
            terminal_name = getIntent().getStringExtra("terminal_name");
            terminal_number = getIntent().getStringExtra("terminal_number");
            terminal_key = getIntent().getStringExtra("terminal_key");
        }else if(fromwhere==3){//云打印修改密钥
            terminal_name = getIntent().getStringExtra("terminal_name");
            terminal_number = getIntent().getStringExtra("terminal_number");
            terminal_key = getIntent().getStringExtra("terminal_key");
        }
    }

    private void initView() {

        et_newname = (EditText)findViewById(R.id.et_newname);
        tv_device_name = (TextView)findViewById(R.id.tv_device_name);
        btn_confirm = (Button)findViewById(R.id.btn_confirm);
        ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);

        if(fromwhere==1){
            btn_confirm.setText("确认修改");
        }else if(fromwhere==2){
            btn_confirm.setText("确认修改");
        }else if(fromwhere==3){
            btn_confirm.setText("确认修改");
        }else{
            btn_confirm.setText("确认添加");
        }

        if(fromwhere==1){
            if(which_printer==3){
                tv_device_name.setText(terminal_name);
            }else {
                tv_device_name.setText(printDevices.getName());
            }
        }else if(fromwhere==2){
            et_newname.setHint("输入打印机终端号");
            tv_device_name.setText(terminal_number);
        }else if(fromwhere==3){
            et_newname.setHint("输入打印机密钥");
            tv_device_name.setText(terminal_key);
        }else{
            tv_device_name.setText(bluetoothDevice.getName());
        }

        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("打印设置");
    }

    private void setListener() {
        btn_confirm.setOnClickListener(this);
        ll_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_cancel ://返回管理
                finish();
                break;
            case R.id.btn_confirm:
                if(TextUtils.isEmpty(et_newname.getText().toString())){
                    if(fromwhere==1){
                        Toast.makeText(this, "请输入打印机名称", Toast.LENGTH_SHORT).show();
                    }else if(fromwhere==2){
                        Toast.makeText(this, "请输入终端号", Toast.LENGTH_SHORT).show();
                    }else if(fromwhere==3){
                        Toast.makeText(this, "请输入秘钥", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "请输入打印机名称", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    ChangeSaveStatus();
                }
                break;
        }
    }

    private void ChangeSaveStatus() {
        boolean saveSuccess = false;
        if(fromwhere==1) {//更改名称
            if(which_printer==3){
                List<SavedPrinter> savedPrinters = (List<SavedPrinter>) SaveUtils.getObject(this, Constant.YUN_PRINTERS);
                if(TextUtils.isEmpty(et_newname.getText().toString())) {
                }else{
                    if(savedPrinters != null){
                        for(int i = 0; i< savedPrinters.size(); i++){
                            if(terminal_number.equals(savedPrinters.get(i).getPrinterId())){
                                savedPrinters.get(i).setName(et_newname.getText().toString().trim());
                                terminal_name = et_newname.getText().toString().trim();
                            }
                        }
                    }
                }
                saveSuccess = SaveUtils.saveObject(this, Constant.YUN_PRINTERS, savedPrinters);
            }else {
                if(TextUtils.isEmpty(et_newname.getText().toString())) {
                    printDevices.setName(tv_device_name.getText().toString());
                }else {
                    printDevices.setName(et_newname.getText().toString());
                }
                saveSuccess = SaveUtils.saveObject(this, Constant.BT_PRINTER, printDevices);
            }
        }else if(fromwhere==2){
            List<SavedPrinter> savedPrinters = (List<SavedPrinter>) SaveUtils.getObject(this, Constant.YUN_PRINTERS);
            if(TextUtils.isEmpty(et_newname.getText().toString())) {
            }else{
                if(savedPrinters != null){
                    for(int i = 0; i< savedPrinters.size(); i++){
                        if(terminal_number.equals(savedPrinters.get(i).getPrinterId())){
                            savedPrinters.get(i).setPrinterId(et_newname.getText().toString().trim());
                            terminal_number = et_newname.getText().toString().trim();
                        }
                    }
                }
            }
            saveSuccess = SaveUtils.saveObject(this, Constant.YUN_PRINTERS, savedPrinters);
        }else if(fromwhere==3){
            List<SavedPrinter> savedPrinters = (List<SavedPrinter>) SaveUtils.getObject(this, Constant.YUN_PRINTERS);
            if(TextUtils.isEmpty(et_newname.getText().toString())) {
            }else{
                if(savedPrinters != null){
                    for(int i = 0; i< savedPrinters.size(); i++){
                        if(terminal_number.equals(savedPrinters.get(i).getPrinterId())){
                            savedPrinters.get(i).setPrinterKey(et_newname.getText().toString().trim());
                            terminal_key = et_newname.getText().toString().trim();
                        }
                    }
                }
            }
            saveSuccess = SaveUtils.saveObject(this, Constant.YUN_PRINTERS, savedPrinters);
        }else {//新添加
            printDevices=new SavedPrinter();
            if(TextUtils.isEmpty(et_newname.getText().toString())) {
                printDevices.setName(tv_device_name.getText().toString());
            }else {
                printDevices.setName(et_newname.getText().toString());
            }
            printDevices.setType("蓝牙打印机");
            printDevices.setBlueToothAdd(bluetoothDevice.getAddress());
            printDevices.setPrintGroupId(new ArrayList<String>());
            printDevices.setPrintGroupName("全部分组");
            saveSuccess = SaveUtils.saveObject(this, Constant.BT_PRINTER, printDevices);
            AppInfo.btAddress=printDevices.getBlueToothAdd();
            SpUtils.getInstance(this).save(Constant.bt_print_permission, "23457");
        }

        if(saveSuccess) {
            if(fromwhere==1||fromwhere==2||fromwhere==3) {
                Intent intent=new Intent(this,TicketSettingActivity.class);
                intent.putExtra("which_printer",which_printer);
                intent.putExtra("terminal_name",terminal_name);
                intent.putExtra("terminal_number",terminal_number);
                intent.putExtra("terminal_key",terminal_key);
                startActivity(intent);
                finish();
                Constant.isfinishAddNewAc = true;
                LogUtils.e(TAG,"to TicketSettingActivity页面");
            }else {
                //startActivity(new Intent(this,PrintDeviceSetting.class));
                AppInfo.btAddress=printDevices.getBlueToothAdd();
                Constant.isfinishAddNewAc = true;
                finish();
                LogUtils.e(TAG,"to PrintDeviceSetting页面");
            }
        }else {
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }
}
