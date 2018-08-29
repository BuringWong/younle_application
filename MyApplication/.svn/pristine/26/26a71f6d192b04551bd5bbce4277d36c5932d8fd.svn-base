package com.yongle.letuiweipad.pagers.manager.printersetting;

import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.adapter.RecyclerAdapter;
import com.yongle.letuiweipad.basepager.ManagerBasePager;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.domain.printer.SavedPrinter;
import com.yongle.letuiweipad.selfinterface.RecyclerItemClickListener;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.NoticePopuUtils;
import com.yongle.letuiweipad.utils.SaveUtils;
import com.yongle.letuiweipad.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/2 0002.
 */

public class AddedPrinterPager extends ManagerBasePager {
    private static final String TAG = "AddedPrinterPager";
    @BindView(R.id.rl_loacal_printer)RelativeLayout localPrinter;
    @BindView(R.id.local_printer_name)TextView localPrinterName;
    @BindView(R.id.rl_bt_printer) RelativeLayout btPrinter;
    @BindView(R.id.bt_printer_name) TextView btPrinterName;
    @BindView(R.id.rlv_yunprinters) RecyclerView rlvYunPinters;
    @BindView(R.id.ll_added)LinearLayout llAdded;
    @BindView(R.id.tv_right) TextView tvRight;
    @BindView(R.id.tv_left) TextView tv_left;

    private List<SavedPrinter> yunPrinters;
    private RecyclerAdapter adapter;
    private RecyclerItemClickListener printerOnItemClick=new RecyclerItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            SavedPrinter savedPrinter = yunPrinters.get(position);
            toHandlePager(savedPrinter,position);
            LogUtils.e(TAG, savedPrinter.toString());
        }
    };
    private SavedPrinter btDevice;
    private SavedPrinter localPrintDevice;

    private void toHandlePager(SavedPrinter savedPrinter,int yunPosition) {
        HandlePrinterPager handlePrinterPager = new HandlePrinterPager();
        handlePrinterPager.setPrinter(savedPrinter,yunPosition);
        setFragment(handlePrinterPager);
    }

    @Override
    public View initView() {
        LogUtils.e(TAG,"initView()");
        View totalView = View.inflate(mActivity, R.layout.added_printer_layout, null);
        return totalView;
    }

    @Override
    public void initData(int position) {
        LogUtils.e(TAG,"initData()");
        tvRight.setVisibility(View.GONE);
        tv_left.setText("打印设置");
        tvRight.setVisibility(View.GONE);
        LinearLayoutManager llManager = new LinearLayoutManager(mActivity);
        llManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlvYunPinters.setLayoutManager(llManager);
        rlvYunPinters.setHasFixedSize(true);
        initState();
    }

    @OnClick({R.id.add_printer,R.id.rl_bt_printer,R.id.rl_loacal_printer})
    void onClick(View view){
        switch (view.getId()) {
            case R.id.add_printer :
                NoticePopuUtils.addWhatPrinter(mActivity, "请选择打印机类型", new NoticePopuUtils.OnClickCallBack() {
                    @Override
                    public void onClickYes() {
                        if(btDevice==null) {
                            setFragment(new AddBtPrinterPager());
                        }else {
                            Utils.showToast(mActivity,"蓝牙打印机仅能添加一台，您已经添加了一了蓝牙打印机，无法再添加。");
                        }
                    }

                    @Override
                    public void onClickNo() {
                        setFragment(new AddYunPrinterPager());
                    }
                });
                break;
            case R.id.rl_bt_printer:
                toHandlePager(btDevice,-1);
                break;
            case R.id.rl_loacal_printer:
                toHandlePager(localPrintDevice,-1);
                break;
        }
    }

    public void initState(){
        //1.本地打印机
        if(Build.MODEL.contains("t1")||Build.MODEL.contains("S2")) {
            localPrintDevice = (SavedPrinter) SaveUtils.getObject(mActivity, Constant.LOCAL_PRINTER);
            localPrinter.setVisibility(View.VISIBLE);
            if(localPrintDevice !=null) {
                localPrinterName.setText(localPrintDevice.getName()+"(本地打印机)");
            }else {
                localPrinterName.setText("(本地打印机)");
            }
        }else {

            try {
                localPrinter.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //2.蓝牙打印机
        btDevice = (SavedPrinter) SaveUtils.getObject(mActivity, Constant.BT_PRINTER);
        if(btDevice !=null) {
            btPrinter.setVisibility(View.VISIBLE);
            btPrinterName.setText(btDevice.getName()+"(蓝牙设备)");
        }else {
            btPrinter.setVisibility(View.GONE);
        }
        //3.是否有云打印
        yunPrinters = (List<SavedPrinter>) SaveUtils.getObject(mActivity, Constant.YUN_PRINTERS);
        if(yunPrinters!=null) {
            LogUtils.e(TAG,"yunPrinters=="+yunPrinters.size());
        }
        if(yunPrinters !=null&& yunPrinters.size()>0) {
            rlvYunPinters.setVisibility(View.VISIBLE);
            adapter = new RecyclerAdapter(mActivity);
            adapter.setData(yunPrinters,RecyclerAdapter.YUN_PRINTERS);
            rlvYunPinters.setAdapter(adapter);
            adapter.setItemClickListener(printerOnItemClick);
        }else {
            rlvYunPinters.setVisibility(View.GONE);
        }
    }
}
