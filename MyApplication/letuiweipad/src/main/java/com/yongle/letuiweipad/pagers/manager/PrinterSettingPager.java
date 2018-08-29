package com.yongle.letuiweipad.pagers.manager;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.basepager.ManagerBasePager;
import com.yongle.letuiweipad.pagers.manager.printersetting.AddBtPrinterPager;
import com.yongle.letuiweipad.pagers.manager.printersetting.AddYunPrinterPager;
import com.yongle.letuiweipad.pagers.manager.printersetting.AddedPrinterPager;
import com.yongle.letuiweipad.pagers.manager.printersetting.HandlePrinterPager;
import com.yongle.letuiweipad.utils.LogUtils;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/24 0024.
 */

public class PrinterSettingPager extends ManagerBasePager {
//    @BindView(R.id.tv_right) TextView tvRight;
//    @BindView(R.id.tv_left) TextView tv_left;
    @BindView(R.id.fl_printer_setting)
    public FrameLayout fl_printer_setting;

    private static final String TAG = "PrinterSettingPager";


    @Override
    public View initView() {
        View baseView = View.inflate(mActivity, R.layout.printer_pager_layout, null);
        return baseView;
    }
    @Override
    public void initData(int position) {
        LogUtils.Log("关于本机initData position="+position);
//        tvRight.setVisibility(View.GONE);
//        tv_left.setText("打印设置");
        AddedPrinterPager addedPrinterPager=new AddedPrinterPager();
        AddBtPrinterPager addBtPrinterPager=new AddBtPrinterPager();
        AddYunPrinterPager addYunPrinterPager=new AddYunPrinterPager();
        HandlePrinterPager handlePrinterPager=new HandlePrinterPager();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_printer_setting,addedPrinterPager,addedPrinterPager.getClass().getSimpleName());
        ft.commit();
    }

}
