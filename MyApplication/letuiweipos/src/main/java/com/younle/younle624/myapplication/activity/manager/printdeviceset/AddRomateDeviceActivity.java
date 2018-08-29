package com.younle.younle624.myapplication.activity.manager.printdeviceset;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.printsetting.SavedPrinter;
import com.younle.younle624.myapplication.utils.SaveUtils;
import com.younle.younle624.myapplication.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class AddRomateDeviceActivity extends Activity implements View.OnClickListener {

    private TextView tv_title;
    private EditText et_key;
    private EditText et_id;
    private EditText et_newname;
    private List<SavedPrinter> savedPrinterList;
    private LinearLayout ll_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_romate_device);
//        setContentView(R.layout.activity_add_romate_device2);
        Utils.initToolBarState(this);
        savedPrinterList = (List<SavedPrinter>) SaveUtils.getObject(this, Constant.YUN_PRINTERS);
        initView();
        setListener();
    }

    private void initView() {
        ll_cancel = (LinearLayout)findViewById(R.id.ll_cancel);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("打印设置");
        et_id = (EditText)findViewById(R.id.et_id);
        et_key = (EditText)findViewById(R.id.et_key);
        et_newname = (EditText)findViewById(R.id.et_newname);
    }

    private void setListener() {
        ll_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_cancel:
                finish();
                break;
        }
    }

    /**
     * 添加新云打印机的设置的监听
     * @param v
     */
    public void save(View v) {
        if(TextUtils.isEmpty(et_id.getText().toString())){
            Toast.makeText(AddRomateDeviceActivity.this, "请输入终端号", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(et_key.getText().toString())){
            Toast.makeText(AddRomateDeviceActivity.this, "请输入密钥", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(et_newname.getText().toString())){
            Toast.makeText(AddRomateDeviceActivity.this, "请输入打印机名称", Toast.LENGTH_SHORT).show();
            return;
        }
        addNewYunPrinter();
    }

    /**
     * 添加新云打印机
     */
    private void addNewYunPrinter() {
        //1.新建云打印存储对象 2.进行本地存储
        SavedPrinter savedPrinter = new SavedPrinter(et_id.getText().toString(),et_key.getText().toString(),et_newname.getText().toString());
        savedPrinter.setPrintGroupName("全部分组");
        savedPrinter.setPrintGroupId(new ArrayList<String>());
        savedPrinter.setPrintPermission("23457");
        if(savedPrinterList !=null) {
            savedPrinterList.add(savedPrinter);
        }else{
            savedPrinterList = new ArrayList<>();
            savedPrinterList.add(savedPrinter);
        }
        saveResult();
    }

    private void saveResult() {
        boolean success = SaveUtils.saveObject(this, Constant.YUN_PRINTERS, savedPrinterList);
        if(success) {
//            SpUtils.getInstance(this).save(Constant.yun_print_permission, "23457");
            //startActivity(new Intent(this,PrintDeviceSetting.class));
            Constant.isfinishAddNewAc = true;
            finish();
        }else {
            Toast.makeText(AddRomateDeviceActivity.this, "保存失败，请稍后重试！", Toast.LENGTH_SHORT).show();
        }
    }
}