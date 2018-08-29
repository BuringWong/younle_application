package com.younle.younle624.myapplication.activity.manager.printdeviceset;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.printsetting.SavedPrinter;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.SaveUtils;
import com.younle.younle624.myapplication.utils.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * 选择蓝牙以及云打印设备
 */
public class PrintDeviceSetting extends Activity implements View.OnClickListener {

    private static final String TAG = PrintDeviceSetting.class.getName();
    private LinearLayout ll_cancel;
    private TextView tv_title;
    private TextView tv_add_new;
    private SavedPrinter printDevice;
    private List<SavedPrinter> savedPrinters;
    private ListView lv_yun_print;
    private TextView tv_print_local;
    private TextView tv_print_bluetooth;
    private View headView;
    private boolean hasDecice;
    private TextView tv_none_devices;
    private View device_line;
    private RelativeLayout rl_local_printer;
    private RelativeLayout rl_bt_printer;
    private TextView tv_added;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_device_setting);
        Utils.initToolBarState(this);
        initView();
        initData();
        setListener();
        showNotice();
    }

    @Override
    protected void onRestart() {
        initData();
        super.onRestart();
    }

    /**
     * 是否展示没有打印设备的提示
     */
    private void showNotice() {
        if(hasDecice) {
            tv_none_devices.setVisibility(View.GONE);
        }else {
            tv_none_devices.setVisibility(View.GONE);
        }
    }

    private void initView() {
        tv_add_new = (TextView)findViewById(R.id.tv_add_new_device);
        ll_cancel = (LinearLayout)findViewById(R.id.ll_cancel);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("打印设置");
        lv_yun_print = (ListView)findViewById(R.id.lv_yun_print);
        tv_none_devices = (TextView)findViewById(R.id.tv_none_devices);
        tv_added = (TextView)findViewById(R.id.tv_added);
        initHeadView();
    }

    /**
     * 头布局，本地打印机和蓝牙打印机
     */
    private void initHeadView() {
        headView = View.inflate(this, R.layout.add_printdevice_header, null);
        rl_local_printer = (RelativeLayout)headView.findViewById(R.id.rl_local_printer);
        rl_bt_printer = (RelativeLayout)headView.findViewById(R.id.rl_bt_printer);
        device_line = headView.findViewById(R.id.device_line);
        tv_print_local = (TextView) headView.findViewById(R.id.tv_print_local);
        tv_print_bluetooth = (TextView) headView.findViewById(R.id.tv_print_bluetooth);
        //Build.MODEL= V1 M1
        if(Build.MODEL.contains("V1")){
            rl_local_printer.setVisibility(View.VISIBLE);
        }else{
            rl_local_printer.setVisibility(View.GONE);
        }
        lv_yun_print.addHeaderView(headView);
    }

    private void initData() {

        //1.是否有蓝牙打印
        printDevice = (SavedPrinter) SaveUtils.getObject(this, Constant.BT_PRINTER);
        if(printDevice!=null) {
            LogUtils.e(TAG, printDevice.toString());
            tv_print_bluetooth.setText(printDevice.getName()+"(蓝牙设备)");
            rl_bt_printer.setVisibility(View.VISIBLE);
            device_line.setVisibility(View.VISIBLE);
        }else {
            rl_bt_printer.setVisibility(View.GONE);
            device_line.setVisibility(View.GONE);
        }
        //3.是否有云打印
        savedPrinters = (List<SavedPrinter>) SaveUtils.getObject(this, Constant.YUN_PRINTERS);
        if(savedPrinters ==null|| savedPrinters.size()<=0) {
            savedPrinters =new ArrayList<>();
        }else {
            hasDecice=true;
        }
        lv_yun_print.setAdapter(new PrintDeviceAdapter());
    }

    private void setListener() {
        tv_add_new.setOnClickListener(this);
        rl_local_printer.setOnClickListener(this);
        rl_bt_printer.setOnClickListener(this);
        ll_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_new_device:
                /*Intent intent=new Intent(this,AddBluetoothActivity.class);
                intent.putExtra(Constant.CONTAIN_BLUETHOOTH_DEVICE,false);
                startActivity(intent);
                finish();*/

                Intent intent1 = new Intent(this,AddNewDeviceActivity.class);
                startActivity(intent1);
                break;
            case R.id.ll_cancel:
                finish();
                break;
            case R.id.rl_local_printer:
                LogUtils.e(TAG,"点击了本地打印设置");
                Intent intent3=new Intent(this,TicketSettingActivity.class);
                intent3.putExtra(Constant.WHICH_PRINTER,1);
                startActivity(intent3);
                break;
            case R.id.rl_bt_printer://更换或删除蓝牙设备名称
                LogUtils.e(TAG,"点击了蓝牙打印设置");
                Intent intent2=new Intent(this,TicketSettingActivity.class);
                intent2.putExtra(Constant.WHICH_PRINTER,2);
                startActivity(intent2);
                break;
        }
    }

    class PrintDeviceAdapter extends BaseAdapter {

       @Override
       public int getCount() {
           return savedPrinters.size();
       }

       @Override
       public Object getItem(int position) {
           return savedPrinters.get(position);
       }

       @Override
       public long getItemId(int position) {
           return 0;
       }

       @Override
       public View getView(final int position, View convertView, ViewGroup parent) {
           ViewHolder holder;
           if(convertView==null) {
               holder=new ViewHolder();
               convertView = View.inflate(PrintDeviceSetting.this,R.layout.yunprinter_bind_item,null);
               holder.tv_yun_printer_name= (TextView) convertView.findViewById(R.id.tv_yun_printer_name);
               convertView.setTag(holder);
           }else {
               holder= (ViewHolder) convertView.getTag();
           }
           holder.tv_yun_printer_name.setText(savedPrinters.get(position).getName()+"(云设备)");
           convertView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {//点击进入到修改云打印机页面
                   LogUtils.e(TAG,"点击了云打印设置");
                   Intent intent = new Intent(PrintDeviceSetting.this,TicketSettingActivity.class);
                   intent.putExtra("position",position);
                   intent.putExtra(Constant.WHICH_PRINTER,3);
                   startActivity(intent);
               }
           });
           return convertView;
       }

       class ViewHolder{
           TextView tv_yun_printer_name;
       }
   }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
