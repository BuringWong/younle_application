package com.yongle.letuiweipad.pagers.manager.printersetting;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.adapter.RecyclerAdapter;
import com.yongle.letuiweipad.basepager.ManagerBasePager;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.constant.UrlConstance;
import com.yongle.letuiweipad.domain.printer.GroupInfoBean;
import com.yongle.letuiweipad.domain.printer.PrintGroupBean;
import com.yongle.letuiweipad.domain.printer.SavedPrinter;
import com.yongle.letuiweipad.selfinterface.RecyclerItemClickListener;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.NetWorks;
import com.yongle.letuiweipad.utils.NoticePopuUtils;
import com.yongle.letuiweipad.utils.SaveUtils;
import com.yongle.letuiweipad.utils.SpUtils;
import com.yongle.letuiweipad.utils.Utils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/12/29 0029.
 */

public class HandlePrinterPager extends ManagerBasePager {
    @BindView(R.id.rl_yun_terminal_number)AutoRelativeLayout rlYunTerminalNumber;
    @BindView(R.id.rl_yun_secret_key)AutoRelativeLayout rlYunSecretKey;
    @BindView(R.id.tv_printer_terminal_number)TextView tvPrinterTerminalNumber;
    @BindView(R.id.tv_yun_secret_key)TextView tvYunSecretKey;
    @BindView(R.id.tv_printer_name)TextView tvPrinterName;
    @BindView(R.id.rl_printer_remark)AutoRelativeLayout rlPrinterRemark;
    @BindView(R.id.ll_yunprinter_msg) AutoLinearLayout llYunPrinterMsg;
    @BindView(R.id.tv_group_name)TextView tvGroupName;
    @BindView(R.id.rl_print_groupsetting)AutoRelativeLayout rlPrintGroupsetting;
    @BindView(R.id.print_pre_auto)ToggleButton printPreAuto;
    @BindView(R.id.print_pre_b)ToggleButton printPreB;
    @BindView(R.id.print_pre_c) ToggleButton printPreC;
    @BindView(R.id.member_print_setting) AutoLinearLayout memberPrintSetting;
    @BindView(R.id.print_pay_b)ToggleButton printPayB;
    @BindView(R.id.print_pay_c)ToggleButton printPayC;
    @BindView(R.id.print_wm_b)ToggleButton printWmB;
    @BindView(R.id.print_wm_c) ToggleButton printWmC;
    @BindView(R.id.wm_print_setting)AutoLinearLayout wmPrintSetting;
    @BindView(R.id.print_applet_b)ToggleButton printAppletB;
    @BindView(R.id.print_applet_c) ToggleButton printAppletC;
    @BindView(R.id.applet_print_setting) AutoLinearLayout appletPrintSetting;
    @BindView(R.id.btn_update_printer) Button btnUpdatePrinter;
    private SavedPrinter printer;
    @BindView(R.id.tv_right)TextView tvRight;
    @BindView(R.id.tv_left)TextView tv_left;
    private String printerSettings="";
    private List<PrintGroupBean> groupData;
    private RecyclerAdapter adapter;

    public HandlePrinterPager() {
    }

    public void setPrinter(SavedPrinter savedPrinter,int yunPosition) {
        this.printer = savedPrinter;
        this.yunPosition=yunPosition;
    }

    @Override
    public View initView() {
        View totalView = View.inflate(mActivity, R.layout.handle_printer_layout, null);
        return totalView;
    }

    @Override
    public void initData(int position) {
        LogUtils.e(TAG,"位置："+yunPosition);
        LogUtils.e(TAG,printer.toString());
        tvRight.setVisibility(View.GONE);
        tv_left.setText("打印设置");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("返回");
        initState();
    }
    void initState(){
        if(printer==null) {
            return;
        }
        if(printer.getType()==2) {
            llYunPrinterMsg.setVisibility(View.VISIBLE);
            tvPrinterTerminalNumber.setText("终端号："+printer.getPrinterId());
            tvYunSecretKey.setText("密钥："+printer.getPrinterKey());
        }else {
            llYunPrinterMsg.setVisibility(View.GONE);
        }
        tvPrinterName.setText("打印机备注："+printer.getName());
        tvGroupName.setText("打印分组设置："+printer.getPrintGroupName());
        switch (printer.getType()) {
            case 0 :
                printerSettings = SpUtils.getInstance(mActivity).getString(Constant.print_permission, "");
                btnUpdatePrinter.setVisibility(View.GONE);
                rlPrinterRemark.setVisibility(View.GONE);
                break;
            case 1:
                printerSettings = SpUtils.getInstance(mActivity).getString(Constant.bt_print_permission, "");
                break;
            case 2:
//                printerSettings = SpUtils.getInstance(TicketSettingActivity.this).getString(Constant.yun_print_permission, "");
                printerSettings = printer.getPrintPermission();
                break;
        }
        printer.getPrintPermission();
        if(printerSettings.contains("1")) {
            printPreAuto.setChecked(true);
        }else {
            printPreAuto.setChecked(false);
        }
        //2.预接单商家联
        if(printerSettings.contains("2")) {
            printPreB.setChecked(true);
        }else {
            printPreB.setChecked(false);
        }
        //3.预接单顾客联
        if(printerSettings.contains("3")) {
            printPreC.setChecked(true);
        }else {
            printPreC.setChecked(false);
        }
        //4.收款成功打票商家联
        if(printerSettings.contains("4")) {
            printPayB.setChecked(true);
        }else {
            printPayB.setChecked(false);
        }
        //5.收款成功打票顾客联
        if(printerSettings.contains("5")) {
            printPayC.setChecked(true);
        }else {
            printPayC.setChecked(false);
        }
        //6.外卖接单商家联
        if(printerSettings.contains("6")) {
            printWmB.setChecked(true);
        }else {
            printWmB.setChecked(false);
        }
        //7.外卖接单顾客联
        if(printerSettings.contains("7")) {
            printWmC.setChecked(true);
        }else {
            printWmC.setChecked(false);
        }
        //8小程序订单商家联
        if(printerSettings.contains("8")) {
            printAppletB.setChecked(true);
        }else {
            printAppletB.setChecked(false);
        }
        //9小程序订单顾客联
        if(printerSettings.contains("9")) {
            printAppletC.setChecked(true);
        }else {
            printAppletC.setChecked(false);
        }

    }
    @OnClick(R.id.tv_right)
    void backForward(View view) {
        setFragment(new AddedPrinterPager());
    }

    @OnCheckedChanged({R.id.print_pre_auto,R.id.print_pre_b,R.id.print_pre_c,R.id.print_pay_b,R.id.print_pay_c,R.id.print_wm_b,R.id.print_wm_c,R.id.print_applet_b,R.id.print_applet_c })
    void switchPermission(ToggleButton toggleButton,boolean checked){
        String handlerPermission="";
        switch (toggleButton.getId()) {
            case R.id.print_pre_auto :
                handlerPermission="1";
                break;
            case R.id.print_pre_b :
                handlerPermission="2";
                break;
            case R.id.print_pre_c :
                handlerPermission="3";
                break;
            case R.id.print_pay_b :
                handlerPermission="4";
                break;
            case R.id.print_pay_c :
                handlerPermission="5";
                break;
        }
        if(checked) {
            if(!printerSettings.contains(handlerPermission)) {
                printerSettings=printerSettings+handlerPermission;
            }
        }else {
            if(printerSettings.contains(handlerPermission)) {
                printerSettings=printerSettings.replace(handlerPermission+"","");
            }
        }
        LogUtils.Log("printSetting=="+printerSettings);
        printer.setPrintPermission(printerSettings);
        if(printer.getType()==0) {
            SpUtils.getInstance(mActivity).save(Constant.print_permission, printerSettings);
            SaveUtils.saveObject(mActivity,Constant.LOCAL_PRINTER,printer);
        }else if(printer.getType()==1) {
            SpUtils.getInstance(mActivity).save(Constant.bt_print_permission, printerSettings);
            SaveUtils.saveObject(mActivity,Constant.BT_PRINTER,printer);
        }else if(printer.getType()==2){
            List<SavedPrinter> yundata= (List<SavedPrinter>) SaveUtils.getObject(mActivity,Constant.YUN_PRINTERS);
            yundata.get(yunPosition).setPrintPermission(printerSettings);
            SaveUtils.saveObject(mActivity,Constant.YUN_PRINTERS,yundata);
        }
    }
    @OnClick({R.id.btn_update_printer,R.id.rl_yun_terminal_number,R.id.rl_yun_secret_key,R.id.rl_printer_remark,R.id.rl_print_groupsetting})
    void onClick(View view){
        switch (view.getId()) {
            case R.id.btn_update_printer :
                NoticePopuUtils.showBindPup(mActivity, "确定要移除该打印机吗？",R.id.handle_printer, new NoticePopuUtils.OnClickCallBack() {
                    @Override
                    public void onClickYes() {
                        removeDevice();
                    }

                    @Override
                    public void onClickNo() {

                    }
                });
                break;
            case R.id.rl_yun_terminal_number :
                changePrinterMsg("请填写打印机终端号",printer.getPrinterId(),0);
                break;
            case R.id.rl_yun_secret_key :
                changePrinterMsg("请填写打印机密钥",printer.getPrinterKey(),1);
                break;
            case R.id.rl_printer_remark :
                changePrinterMsg("请填写打印机备注名称",printer.getName(),2);
                break;
            case R.id.rl_print_groupsetting :
                getBranchInfo();
                break;
        }
    }
    /**
     * 移除设备
     */
    private void removeDevice() {
        switch (printer.getType()){
            case 1://移除蓝牙设备
                unBindBluetooth();
                SaveUtils.saveObject(mActivity, Constant.BT_PRINTER, null);
                break;
            case 2://移除云设备
                List<SavedPrinter> savedPrinters = (List<SavedPrinter>) SaveUtils.getObject(mActivity, Constant.YUN_PRINTERS);
                savedPrinters.remove(position);
                SaveUtils.saveObject(mActivity, Constant.YUN_PRINTERS, savedPrinters);
                break;
        }
        setFragment(new AddedPrinterPager());

    }
    private void changePrinterMsg(String msg, String currentMsg,final int which) {
        NoticePopuUtils.showInPutDia(mActivity, msg, currentMsg,true, new NoticePopuUtils.OnFinishInput() {
            @Override
            public void onFinishInput(String msg) {
                switch (which) {
                    case 0 :
                        List<SavedPrinter> yundata= (List<SavedPrinter>) SaveUtils.getObject(mActivity,Constant.YUN_PRINTERS);
                        yundata.get(yunPosition).setPrinterId(msg);
                        SaveUtils.saveObject(mActivity,Constant.YUN_PRINTERS,yundata);
                        tvPrinterTerminalNumber.setText("终端号："+msg);
                        break;
                    case 1:
                        yundata= (List<SavedPrinter>) SaveUtils.getObject(mActivity,Constant.YUN_PRINTERS);
                        yundata.get(yunPosition).setPrinterKey(msg);
                        SaveUtils.saveObject(mActivity,Constant.YUN_PRINTERS,yundata);
                        tvYunSecretKey.setText("密钥："+msg);
                        break;
                    case 2:
                        printer.setName(msg);
                        if(printer.getType()==0) {
                            SaveUtils.saveObject(mActivity,Constant.LOCAL_PRINTER,printer);
                        }else if(printer.getType()==1) {
                            SaveUtils.saveObject(mActivity,Constant.BT_PRINTER,printer);
                        }else if(printer.getType()==2) {
                            yundata= (List<SavedPrinter>) SaveUtils.getObject(mActivity,Constant.YUN_PRINTERS);
                            yundata.get(yunPosition).setName(msg);
                            SaveUtils.saveObject(mActivity,Constant.YUN_PRINTERS,yundata);
                        }
                        tvPrinterName.setText("打印机备注："+printer.getName());
                        break;
                }
            }

            @Override
            public void onCancelInput() {

            }
        });
    }

    private void unBindBluetooth() {
        Set<BluetoothDevice> bondedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        Iterator<BluetoothDevice> iterator = bondedDevices.iterator();
        while (iterator.hasNext()){
            BluetoothDevice bluetoothDevice = iterator.next();
            String blueToothAdd = printer.getBlueToothAdd();
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

    /**
     * 联网获取数据
     */
    private void getBranchInfo() {
        NetWorks netWorks=new NetWorks(mActivity);
        Map<String, String> params = netWorks.getPublicParams();
        params.put("store_id", Constant.STORE_ID);
        params.put("advid",Constant.ADV_ID);
        netWorks.Request(UrlConstance.GOODS_SPLIT_INFO, true,"正在获取分组信息",params, 5000, 0, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                Utils.showToast(mActivity,"网络异常",2000);
            }

            @Override
            public void onResonse(String response, int flag) {
                LogUtils.Log("获取分组信息 onResponse():"+response.toString());
                praseJson(response);
            }
        });
    }


    private void praseJson(String json) {
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            if(code==200) {
                GroupInfoBean groupInfoBean = new Gson().fromJson(json, GroupInfoBean.class);
                handldeData(groupInfoBean);
            }else {
                String msg = jsonObject.getString("msg");
                NoticePopuUtils.showOfflinDia(mActivity,msg,null);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void handldeData(GroupInfoBean groupInfoBean) {
        PrintGroupBean msgBean=new PrintGroupBean();
        msgBean.setChecked(false);
        msgBean.setGroup_name("全部");
        msgBean.setId("0");
        groupData = groupInfoBean.getMsg();
        groupData.add(0,msgBean);
        for (int i = 0; i < groupData.size(); i++) {
            groupData.get(i).setChecked(groupData.get(i).getId().equals(printer.getPrintGroupId()));
        }
        showBranchInfo();
    }
    private void showBranchInfo() {
        View view = View.inflate(mActivity, R.layout.print_groupsetting_layout, null);
        TextView tvNo=ButterKnife.findById(view,R.id.tv_no);
        TextView tvYes=ButterKnife.findById(view,R.id.tv_yes);
        RecyclerView recyclerView=ButterKnife.findById(view,R.id.rlv_groups);
        LinearLayoutManager llManager = new LinearLayoutManager(mActivity);
        llManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(mActivity, LinearLayoutManager.VERTICAL));
        adapter = new RecyclerAdapter(mActivity);
        adapter.setData(groupData,RecyclerAdapter.PRINT_GROUP_INFO);
        recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(printGroupListOnItemClick);

        final AlertDialog dialog = new AlertDialog.Builder(mActivity)
                .setView(view)
                .show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        int[] wh=new int[2];
        Utils.getRelativeWH(mActivity,844,959,wh);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width=wh[0];
        params.height=wh[1];
        dialog.getWindow().setAttributes(params);
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存打印分组信息
                dialog.dismiss();
                getCheckedBean();
            }
        });
    }
    private void getCheckedBean() {
        PrintGroupBean checkedBean=null;
        if(groupData!=null&&groupData.size()>0) {
            for (int i = 0; i < groupData.size(); i++) {
                if(groupData.get(i).isChecked()) {
                    checkedBean = groupData.get(i);
                    break;
                }
            }
            setPrinterState(checkedBean.getId(),checkedBean.getGroup_name());
        }else{
            setPrinterState("0","全部分组");
        }
    }

    private static final String TAG = "HandlePrinterPager";
    private void setPrinterState(String printGroup,String groupName) {
        tvGroupName.setText("打印分组设置："+groupName);
        printer.setPrintGroupId(printGroup);
        printer.setPrintGroupName(groupName);
        switch (printer.getType()) {
            case 0 :
                SaveUtils.saveObject(mActivity,Constant.LOCAL_PRINTER,printer);
                break;
            case 1 :
                SaveUtils.saveObject(mActivity,Constant.BT_PRINTER,printer);
                break;
            case 2 :
                List<SavedPrinter> yunList= (List<SavedPrinter>) SaveUtils.getObject(mActivity,Constant.YUN_PRINTERS);
                yunList.get(yunPosition).setPrintGroupId(printGroup);
                yunList.get(yunPosition).setPrintGroupName(groupName);
                LogUtils.e(TAG,yunList.toString());
                SaveUtils.saveObject(mActivity,Constant.YUN_PRINTERS,yunList);
                break;
        }
    }

    private RecyclerItemClickListener printGroupListOnItemClick=new RecyclerItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            for (int j = 0; j < groupData.size(); j++) {
                if(j==position) {
                    groupData.get(j).setChecked(true);
                }else {
                    groupData.get(j).setChecked(false);
                }
            }
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
