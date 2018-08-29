package com.younle.younle624.myapplication.activity.manager.printdeviceset;


import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.printsetting.SavedPrinter;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.utils.SaveUtils;
import com.younle.younle624.myapplication.utils.SpUtils;
import com.zhy.autolayout.AutoLinearLayout;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class TicketSettingActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "TicketSettingActivity";
    private TextView tv_title;
    private ToggleButton print_pre_auto;
    private ToggleButton print_pre_b;
    private ToggleButton print_pre_c,pre_onebyone;
    private ToggleButton print_pay_b;
    private ToggleButton print_pay_c;
    private ToggleButton print_wm_b;
    private ToggleButton print_wm_c;
    private ToggleButton print_applet_b;
    private ToggleButton print_applet_c,print_community_b,print_community_c,applet_onebyone,community_onebyone;
    private NetWorks netWork;
    private LinearLayout ll_cancel;
    private AutoLinearLayout member_print_setting;
    private AutoLinearLayout wm_print_setting;
    private AutoLinearLayout applet_print_setting,community_print_setting;
    private int which_printer;//1本地打印机 2蓝牙打印机 3云打印机
    private TextView tv_printer_name;
    private Button btn_update_printer;//删除打印机按钮
    private RelativeLayout rl_printer_remark;
    private LinearLayout ll_printer_remark;
    private AutoLinearLayout all_yun_terminal_number;
    private RelativeLayout rl_yun_terminal_number;
    private AutoLinearLayout all_yun_secret_key;
    private RelativeLayout rl_yun_secret_key;
    private TextView tv_printer_terminal_number;
    private TextView tv_yun_secret_key;
    private SavedPrinter printDevice;
    private String printerId;
    private String printerKey;
    private String name;
    private int position;
    private TextView tv_group_name;
    private List<SavedPrinter> savedPrinters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_setting);
        which_printer = getIntent().getIntExtra("which_printer", -1);
        netWork = new NetWorks(this);
        initView();
        setListener();
    }

    @Override
    protected void onResume() {
        SavedPrinter  printDevice1 = (SavedPrinter) SaveUtils.getObject(this, Constant.LOCAL_PRINTER);
        SavedPrinter  printDevice2 = (SavedPrinter) SaveUtils.getObject(this, Constant.BT_PRINTER);
        List<SavedPrinter> printDevice3 = (List<SavedPrinter>) SaveUtils.getObject(this, Constant.YUN_PRINTERS);
        if(printDevice1!=null) {
            LogUtils.e(TAG,"local=="+printDevice1.toString());
        }
        if(printDevice2!=null) {
            LogUtils.e(TAG,"bt=="+printDevice2.toString());
        }
        if(printDevice3!=null) {
            LogUtils.e(TAG,"yun=="+printDevice3.toString());
        }

        if(Constant.isfinishAddNewAc){
            Constant.isfinishAddNewAc = false;
            this.finish();
        }else if(which_printer==3){
            savedPrinters = (List<SavedPrinter>) SaveUtils.getObject(this, Constant.YUN_PRINTERS);
            if(savedPrinters !=null&& savedPrinters.size()>0){
                printDevice=savedPrinters.get(position);
                name = savedPrinters.get(position).getName();
                printerId = savedPrinters.get(position).getPrinterId();
                printerKey = savedPrinters.get(position).getPrinterKey();
                tv_printer_name.setText(name);
                tv_printer_terminal_number.setText(printerId);
                tv_yun_secret_key.setText(printerKey);
            }
        }else if(which_printer==2){
            try {
                printDevice = (SavedPrinter) SaveUtils.getObject(this, Constant.BT_PRINTER);
                tv_printer_name.setText(printDevice.getName());
                tv_group_name.setText(printDevice.getPrintGroupName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onResume();
    }

    private void initView() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("打印设置");

        rl_printer_remark = (RelativeLayout)findViewById(R.id.rl_printer_remark);//备注布局
        tv_printer_name = (TextView)findViewById(R.id.tv_printer_name);
        btn_update_printer = (Button)findViewById(R.id.btn_update_printer);
        ll_printer_remark = (LinearLayout)findViewById(R.id.ll_printer_remark);

        all_yun_terminal_number = (AutoLinearLayout)findViewById(R.id.all_yun_terminal_number);//终端号布局
        all_yun_secret_key = (AutoLinearLayout)findViewById(R.id.all_yun_secret_key);//密钥布局

        rl_yun_terminal_number = (RelativeLayout)findViewById(R.id.rl_yun_terminal_number);//终端号点击布局
        rl_yun_secret_key = (RelativeLayout)findViewById(R.id.rl_yun_secret_key);//密钥点击布局

        tv_printer_terminal_number = (TextView)findViewById(R.id.tv_printer_terminal_number);//终端号
        tv_yun_secret_key = (TextView)findViewById(R.id.tv_yun_secret_key);//密钥
        tv_group_name = (TextView)findViewById(R.id.tv_group_name);

        if(which_printer==1) {
            printDevice = (SavedPrinter) SaveUtils.getObject(this, Constant.LOCAL_PRINTER);
            tv_printer_name.setText("本地打印机");
            tv_group_name.setText(printDevice.getPrintGroupName());
            btn_update_printer.setVisibility(View.GONE);
            ll_printer_remark.setVisibility(View.GONE);
        }else if(which_printer==2){//蓝牙打印机
            printDevice = (SavedPrinter) SaveUtils.getObject(this, Constant.BT_PRINTER);
            tv_printer_name.setText(printDevice.getName());
            tv_group_name.setText(printDevice.getPrintGroupName());
            btn_update_printer.setVisibility(View.VISIBLE);
            ll_printer_remark.setVisibility(View.VISIBLE);
        }else if(which_printer==3){//云打印机
            position = getIntent().getIntExtra("position",0);
            savedPrinters = (List<SavedPrinter>) SaveUtils.getObject(this, Constant.YUN_PRINTERS);
            if(savedPrinters !=null&& savedPrinters.size()>0){
                printDevice= savedPrinters.get(position);
                name = savedPrinters.get(position).getName();
                printerId = savedPrinters.get(position).getPrinterId();
                printerKey = savedPrinters.get(position).getPrinterKey();
                tv_printer_name.setText(name);
                tv_printer_terminal_number.setText(printerId);
                tv_group_name.setText(printDevice.getPrintGroupName());
                tv_yun_secret_key.setText(printerKey);
            }

            btn_update_printer.setVisibility(View.VISIBLE);
            ll_printer_remark.setVisibility(View.VISIBLE);
            all_yun_terminal_number.setVisibility(View.VISIBLE);
            all_yun_secret_key.setVisibility(View.VISIBLE);
        }
        print_pre_auto = (ToggleButton)findViewById(R.id.print_pre_auto);
        print_pre_b = (ToggleButton)findViewById(R.id.print_pre_b);
        print_pre_c = (ToggleButton)findViewById(R.id.print_pre_c);
        pre_onebyone = (ToggleButton)findViewById(R.id.pre_onebyone);

        print_pay_b = (ToggleButton)findViewById(R.id.print_pay_b);
        print_pay_c = (ToggleButton)findViewById(R.id.print_pay_c);
        print_wm_b = (ToggleButton)findViewById(R.id.print_wm_b);
        print_wm_c = (ToggleButton)findViewById(R.id.print_wm_c);
        print_applet_b = (ToggleButton)findViewById(R.id.print_applet_b);
        print_applet_c = (ToggleButton)findViewById(R.id.print_applet_c);
        applet_onebyone = (ToggleButton)findViewById(R.id.applet_onebyone);

        print_community_b = (ToggleButton)findViewById(R.id.print_community_b);
        print_community_c = (ToggleButton)findViewById(R.id.print_community_c);
        community_onebyone = (ToggleButton)findViewById(R.id.community_onebyone);


        ll_cancel = (LinearLayout)findViewById(R.id.ll_cancel);


        wm_print_setting = (AutoLinearLayout)findViewById(R.id.wm_print_setting);
        member_print_setting = (AutoLinearLayout)findViewById(R.id.member_print_setting);
        applet_print_setting = (AutoLinearLayout)findViewById(R.id.applet_print_setting);
        community_print_setting = (AutoLinearLayout)findViewById(R.id.community_print_setting);

        if(Constant.OPENED_PERMISSIONS.contains("2")) {//预结单
            member_print_setting.setVisibility(View.VISIBLE);
        }else {
            member_print_setting.setVisibility(View.GONE);
        }
        if(Constant.OPENED_PERMISSIONS.contains("7")) {//外卖
            wm_print_setting.setVisibility(View.VISIBLE);
        }else {
            wm_print_setting.setVisibility(View.GONE);
        }
        if(Constant.OPENED_PERMISSIONS.contains("8")) {//小程序
            applet_print_setting.setVisibility(View.VISIBLE);
        }else {
            applet_print_setting.setVisibility(View.GONE);
        }
        if(Constant.OPENED_PERMISSIONS.contains("9")) {//社区小程序
            community_print_setting.setVisibility(View.VISIBLE);
        }else {
            community_print_setting.setVisibility(View.GONE);
        }
        setStatus();
    }
    boolean preOneByOne=false;
    boolean appletOneByOne=false;
    boolean communityOneByOne=false;
    private void setStatus() {
        String printerSettings="";
       
        switch (which_printer) {
            case 1 :
                printerSettings = SpUtils.getInstance(TicketSettingActivity.this).getString(Constant.print_permission, "");
                preOneByOne=SpUtils.getInstance(this).getBoolean(Constant.LOCAL_PRE_OBO,false);
                appletOneByOne=SpUtils.getInstance(this).getBoolean(Constant.LOCAL_APPLET_OBO,false);
                communityOneByOne=SpUtils.getInstance(this).getBoolean(Constant.LOCAL_COMMUNITY_OBO,false);
                break;
            case 2:
                printerSettings = SpUtils.getInstance(TicketSettingActivity.this).getString(Constant.bt_print_permission, "");
                preOneByOne=SpUtils.getInstance(this).getBoolean(Constant.BT_PRE_OBO,false);
                appletOneByOne=SpUtils.getInstance(this).getBoolean(Constant.BT_APPLET_OBO,false);
                communityOneByOne=SpUtils.getInstance(this).getBoolean(Constant.BT_COMMUNITY_OBO,false);
                break;
            case 3:
//                printerSettings = SpUtils.getInstance(TicketSettingActivity.this).getString(Constant.yun_print_permission, "");
                printerSettings = printDevice.getPrintPermission();
                preOneByOne=printDevice.isPreobo();
                appletOneByOne=printDevice.isAppletobo();
                communityOneByOne=printDevice.isCommunityobo();
                break;
        }
        //1.打印预接单
        if(printerSettings.contains("1")) {
            print_pre_auto.setChecked(true);
        }else {
            print_pre_auto.setChecked(false);
        }
        //2.预接单商家联
        if(printerSettings.contains("2")) {
            print_pre_b.setChecked(true);
        }else {
            print_pre_b.setChecked(false);
        }
        //3.预接单顾客联
        if(printerSettings.contains("3")) {
            print_pre_c.setChecked(true);
        }else {
            print_pre_c.setChecked(false);
        }
        //4.收款成功打票商家联
        if(printerSettings.contains("4")) {
            print_pay_b.setChecked(true);
        }else {
            print_pay_b.setChecked(false);
        }
        //5.收款成功打票顾客联
        if(printerSettings.contains("5")) {
            print_pay_c.setChecked(true);
        }else {
            print_pay_c.setChecked(false);
        }
        //6.外卖接单商家联
        if(printerSettings.contains("6")) {
            print_wm_b.setChecked(true);
        }else {
            print_wm_b.setChecked(false);
        }
        //7.外卖接单顾客联
        if(printerSettings.contains("7")) {
            print_wm_c.setChecked(true);
        }else {
            print_wm_c.setChecked(false);
        }
        //8小程序订单商家联
        if(printerSettings.contains("8")) {
            print_applet_b.setChecked(true);
        }else {
            print_applet_b.setChecked(false);
        }
        //9小程序订单顾客联
        if(printerSettings.contains("9")) {
            print_applet_c.setChecked(true);
        }else {
            print_applet_c.setChecked(false);
        }
        //a.社区小程序顾客联
        if(printerSettings.contains("a")) {
            print_community_c.setChecked(true);
        }else {
            print_community_c.setChecked(false);
        }
        //b.社区小程序商家联
        if(printerSettings.contains("b")) {
            print_community_b.setChecked(true);
        }else {
            print_community_b.setChecked(false);
        }

        pre_onebyone.setChecked(preOneByOne);
        applet_onebyone.setChecked(appletOneByOne);
        community_onebyone.setChecked(communityOneByOne);

    }

    private void setListener() {
        //pos机预结单
        print_pre_auto.setOnCheckedChangeListener(new MyToggleOnChenkedChangedListner("1"));
        print_pre_b.setOnCheckedChangeListener(new MyToggleOnChenkedChangedListner("2"));
        print_pre_c.setOnCheckedChangeListener(new MyToggleOnChenkedChangedListner("3"));


        print_pay_b.setOnCheckedChangeListener(new MyToggleOnChenkedChangedListner("4"));
        print_pay_c.setOnCheckedChangeListener(new MyToggleOnChenkedChangedListner("5"));
        print_wm_b.setOnCheckedChangeListener(new MyToggleOnChenkedChangedListner("6"));
        print_wm_c.setOnCheckedChangeListener(new MyToggleOnChenkedChangedListner("7"));

        //自助点单订单
        print_applet_b.setOnCheckedChangeListener(new MyToggleOnChenkedChangedListner("8"));
        print_applet_c.setOnCheckedChangeListener(new MyToggleOnChenkedChangedListner("9"));

        //社区小程序订单
        print_community_c.setOnCheckedChangeListener(new MyToggleOnChenkedChangedListner("a"));
        print_community_b.setOnCheckedChangeListener(new MyToggleOnChenkedChangedListner("b"));

        //一菜一单开关
        pre_onebyone.setOnCheckedChangeListener(new MyToggleOnChenkedChangedListner("pre"));
        applet_onebyone.setOnCheckedChangeListener(new MyToggleOnChenkedChangedListner("applet"));
        community_onebyone.setOnCheckedChangeListener(new MyToggleOnChenkedChangedListner("community"));

        ll_cancel.setOnClickListener(this);
        btn_update_printer.setOnClickListener(this);
        rl_printer_remark.setOnClickListener(this);
        rl_yun_terminal_number.setOnClickListener(this);
        all_yun_secret_key.setOnClickListener(this);

        findViewById(R.id.rl_print_groupsetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TicketSettingActivity.this,PrintGroupSettingActivity.class);
                intent.putExtra("which_printer",which_printer);
                intent.putExtra("position",position);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_cancel :
                finish();
                break;
            case R.id.btn_update_printer:
                showDelDia();
                break;
            case R.id.rl_printer_remark://修改备注
                Intent intent1=new Intent(this,RemarkDeviceActivity.class);
                intent1.putExtra(Constant.WHICH_PRINTER, which_printer);
                intent1.putExtra(Constant.CHANGE_EDITEXT_INFO, 1);
                if(which_printer==3){
                    intent1.putExtra("terminal_name", name);
                    intent1.putExtra("terminal_number", printerId);
                    intent1.putExtra("terminal_key", printerKey);
                }
                startActivity(intent1);
                //finish();
                break;
            case R.id.rl_yun_terminal_number://修改终端号
                Intent intent2 = new Intent(this,RemarkDeviceActivity.class);
                intent2.putExtra(Constant.WHICH_PRINTER, which_printer);
                intent2.putExtra(Constant.CHANGE_EDITEXT_INFO, 2);
                intent2.putExtra("terminal_name", name);
                intent2.putExtra("terminal_number", printerId);
                intent2.putExtra("terminal_key", printerKey);
                startActivity(intent2);
                //finish();
                break;
            case R.id.all_yun_secret_key://修改密钥
                Intent intent3 = new Intent(this,RemarkDeviceActivity.class);
                intent3.putExtra(Constant.WHICH_PRINTER, which_printer);
                intent3.putExtra(Constant.CHANGE_EDITEXT_INFO, 3);
                intent3.putExtra("terminal_name", name);
                intent3.putExtra("terminal_number", printerId);
                intent3.putExtra("terminal_key", printerKey);
                startActivity(intent3);
                //finish();
                break;
        }
    }

    /**
     * 删除当前蓝牙打印机的dia
     */
    private void showDelDia() {
        new AlertDialog.Builder(this)
                .setTitle("您确定要移除本打印机么？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeDevice();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 移除设备
     */
    private void removeDevice() {
        switch (which_printer){
            case 2://移除蓝牙设备
                unBindBluetooth();
                SaveUtils.saveObject(TicketSettingActivity.this, Constant.BT_PRINTER, null);
                //startActivity(new Intent(TicketSettingActivity.this, PrintDeviceSetting.class));
                finish();
                break;
            case 3://移除云设备
                //删除本地存储的云打印设置 printerId
                LogUtils.e(TAG,"printerId="+printerId);
                List<SavedPrinter> savedPrinters = (List<SavedPrinter>) SaveUtils.getObject(this, Constant.YUN_PRINTERS);
                if(savedPrinters != null){
                    for(int i = 0; i< savedPrinters.size(); i++){
                        LogUtils.e(TAG,"存储的printerId="+ savedPrinters.get(i).getPrinterId());
                        if(savedPrinters.get(i).getPrinterId().equals(printerId)){
                            savedPrinters.remove(i);
                            break;
                        }
                    }
                }
                boolean b = SaveUtils.saveObject(this, Constant.YUN_PRINTERS, savedPrinters);
                LogUtils.e(TAG,"删除打印机后存储结果：b="+b);
                finish();
                break;
        }
    }

    private void unBindBluetooth() {
        Set<BluetoothDevice> bondedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        Iterator<BluetoothDevice> iterator = bondedDevices.iterator();
        while (iterator.hasNext()){
            BluetoothDevice bluetoothDevice = iterator.next();
            String blueToothAdd = printDevice.getBlueToothAdd();
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

    class MyToggleOnChenkedChangedListner implements CompoundButton.OnCheckedChangeListener{
        String position="";

        public MyToggleOnChenkedChangedListner(String position) {
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            String printerSettings="";
            switch (which_printer) {
                case 1 ://本地
                    printerSettings=SpUtils.getInstance(TicketSettingActivity.this).getString(Constant.print_permission, "");
                    break;
                case 2://蓝牙
                    printerSettings=SpUtils.getInstance(TicketSettingActivity.this).getString(Constant.bt_print_permission, "");
                    break;
                case 3://云
//                    printerSettings=SpUtils.getInstance(TicketSettingActivity.this).getString(Constant.yun_print_permission, "");
                    printerSettings=printDevice.getPrintPermission();
                    break;
            }
            if(TextUtils.equals("pre",position)) {
                preOneByOne=!preOneByOne;
            }else if(TextUtils.equals("applet",position)) {
                appletOneByOne=!appletOneByOne;
            }else if(TextUtils.equals("community",position)) {
                communityOneByOne=!communityOneByOne;
            }else {
                if(printerSettings.contains(position+"")) {
                    printerSettings=printerSettings.replace(position+"","");
                }else {
                    printerSettings=printerSettings+position;
                }
                LogUtils.e(TAG,"printer_setting="+printerSettings);
            }


            if(which_printer==1) {
                SpUtils.getInstance(TicketSettingActivity.this).save(Constant.print_permission, printerSettings);
                SpUtils.getInstance(TicketSettingActivity.this).save(Constant.LOCAL_PRE_OBO, preOneByOne);
                SpUtils.getInstance(TicketSettingActivity.this).save(Constant.LOCAL_APPLET_OBO, appletOneByOne);
                SpUtils.getInstance(TicketSettingActivity.this).save(Constant.LOCAL_COMMUNITY_OBO, communityOneByOne);
            }else if(which_printer==2) {
                SpUtils.getInstance(TicketSettingActivity.this).save(Constant.bt_print_permission, printerSettings);
                SpUtils.getInstance(TicketSettingActivity.this).save(Constant.BT_PRE_OBO, preOneByOne);
                SpUtils.getInstance(TicketSettingActivity.this).save(Constant.BT_APPLET_OBO, appletOneByOne);
                SpUtils.getInstance(TicketSettingActivity.this).save(Constant.BT_COMMUNITY_OBO, communityOneByOne);
            }else if(which_printer==3){
                printDevice.setPrintPermission(printerSettings);
                printDevice.setPreobo(preOneByOne);
                printDevice.setAppletobo(appletOneByOne);
                printDevice.setCommunityobo(communityOneByOne);
                SaveUtils.saveObject(TicketSettingActivity.this,Constant.YUN_PRINTERS,savedPrinters);
               /* List<SavedPrinter> sp =( List<SavedPrinter> )SaveUtils.getObject(TicketSettingActivity.this, Constant.YUN_PRINTERS);
                String printPermission = sp.get(TicketSettingActivity.this.position).getPrintPermission();
                LogUtils.Log(sp.get(TicketSettingActivity.this.position).toString());*/
            }
            boolean result = SpUtils.getInstance(TicketSettingActivity.this).getBoolean(Constant.LOCAL_PRE_OBO, false);
            LogUtils.e(TAG,"saveResult="+result);
        }
    }
}
