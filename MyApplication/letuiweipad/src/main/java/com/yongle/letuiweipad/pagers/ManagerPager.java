package com.yongle.letuiweipad.pagers;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.activity.LoginActivity;
import com.yongle.letuiweipad.basepager.BasePager;
import com.yongle.letuiweipad.basepager.ManagerBasePager;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.pagers.manager.AboutPager;
import com.yongle.letuiweipad.pagers.manager.FuPingSettingPager;
import com.yongle.letuiweipad.pagers.manager.MemberChargePager;
import com.yongle.letuiweipad.pagers.manager.PrinterSettingPager;
import com.yongle.letuiweipad.pagers.manager.ScannerSettingPager;
import com.yongle.letuiweipad.pagers.manager.VoucherHxPager;
import com.yongle.letuiweipad.pagers.manager.WeigherSettingPager;
import com.yongle.letuiweipad.selfinterface.ConfirmPwdListener;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.NetWorks;
import com.yongle.letuiweipad.utils.NoticePopuUtils;
import com.yongle.letuiweipad.utils.SpUtils;
import com.yongle.letuiweipad.utils.Utils;
import com.yongle.letuiweipad.utils.printmanager.PrintUtils;
import com.zhy.autolayout.AutoRelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

/**
 * Created by 我是奋斗 on 2016/5/11.
 * 微信/e-mail:tt090423@126.com
 */
public class ManagerPager extends BasePager {

    @BindView(R.id.no_member_notice)TextView noMemberNotice;
    @BindView(R.id.rl_nomember_header)AutoRelativeLayout rlNomemberHeader;
    @BindView(R.id.rg_manager_left)RadioGroup rgManagerLeft;
    @BindView(R.id.fr_manager_right)FrameLayout frManagerRight;
    @BindView(R.id.exit_app)RadioButton exit_app;
    @BindView(R.id.open_box)RadioButton open_box;
    @BindView(R.id.fuping_setting) RadioButton fuping_setting;
    @BindView(R.id.printer_setting) RadioButton printer_setting;
    @BindView(R.id.fp_line) View fp_line;
    @BindView(R.id.weigher_setting) RadioButton weigher_setting;
    @BindView(R.id.weighLine) View weighLine;
    private View totalView;
    private List<ManagerBasePager> pagerList=new ArrayList<>();

    private Fragment currentFragment;
    private int currentIndex;
    private PopupWindow exitPop;

    public ManagerPager() {
        super();
    }

    @Override
    public View initView() {
        totalView = View.inflate(mActivity, R.layout.mamger_pager_layout, null);
        return totalView;
    }

    private static final String TAG = "ManagerPager";
    @Override
    public void initData(int index) {
        LogUtils.e(TAG,"initData()");
        if(Build.MODEL.contains("D1")) {
            fuping_setting.setVisibility(View.GONE);
            fp_line.setVisibility(View.GONE);
        }
        if(Build.MODEL.contains("S2")) {
            weigher_setting.setVisibility(View.GONE);
            weighLine.setVisibility(View.GONE);
        }
        int height = mActivity.getWindow().getWindowManager().getDefaultDisplay().getHeight();
        rgManagerLeft.setMinimumHeight((int) (height*0.55));
        MemberChargePager memberChargePager=new MemberChargePager();
        VoucherHxPager voucherHxPager=new VoucherHxPager();
        PrinterSettingPager printerSettingPager=new PrinterSettingPager();
        ScannerSettingPager scannerSettingPager=new ScannerSettingPager();
        WeigherSettingPager weigherSettingPager=new WeigherSettingPager();
        FuPingSettingPager fuPingSettingPager=new FuPingSettingPager();
        AboutPager aboutPager=new AboutPager();

        pagerList.add(printerSettingPager);
        pagerList.add(memberChargePager);
        pagerList.add(voucherHxPager);
        pagerList.add(scannerSettingPager);
        pagerList.add(weigherSettingPager);
        pagerList.add(fuPingSettingPager);
        pagerList.add(aboutPager);
//        rgManagerLeft.check(R.id.printer_setting);
//        setFragmet(0);
        rgManagerLeft.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                LogUtils.e(TAG,"onCheckedChanged（）");

                switch (i) {
                    case  R.id.printer_setting:
                        setFragmet(0);
                        break;
                    case  R.id.member_charge:
                        setFragmet(1);
                        break;
                    case R.id.voucher_hx:
                        setFragmet(2);
                        break;
                    case  R.id.scanner_setting:
                        setFragmet(3);
                        break;
                    case  R.id.weigher_setting:
                        setFragmet(4);
                        break;
                    case R.id.fuping_setting:
                        setFragmet(5);
                        break;
                    case  R.id.about_this:
                        setFragmet(6);
                        break;
                    case  R.id.open_box:
                       /* openBox();
                        frManagerRight.setVisibility(View.GONE);*/
                        break;
                    case  R.id.check_version:
                        frManagerRight.setVisibility(View.GONE);
                        checkVersion();
                        break;
                    case  R.id.exit_app:
                        frManagerRight.setVisibility(View.GONE);
                        exitAcount();
                        break;
                }
            }
        });
//        rgManagerLeft.check(R.id.printer_setting);
        printer_setting.setChecked(true);
        exit_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitAcount();
            }
        });
        open_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBox();
                frManagerRight.setVisibility(View.GONE);
            }
        });
    }

    private IWoyouService iWoyouService;
    public ServiceConnection connService = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iWoyouService = IWoyouService.Stub.asInterface(service);//拿到打印服务的对象
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iWoyouService = null;
        }
    };
    /**
     * 打开钱箱
     */
    private void openBox() {
        PrintUtils.getInstance().initService(mActivity, connService);
        NetWorks netWorks=new NetWorks(mActivity);
        netWorks.confirmPassWord(new ConfirmPwdListener() {
            @Override
            public void onPwdPass() {
                try {
                    iWoyouService.openDrawer(new ICallback.Stub() {
                        @Override
                        public void onRunResult(boolean isSuccess, int code, String msg) throws RemoteException {
                            LogUtils.e(TAG,"isSuccess="+isSuccess+" code="+code+ " msg="+msg);
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 解析退款密码验证json
     * @param json
     */
    private void praseConfirmRefundPwdJson(String json) {
        try {
            JSONObject jsonObject=new JSONObject(json);
            int code = jsonObject.getInt("code");
            if(code==200) {
                try {
                    iWoyouService.openDrawer(new ICallback.Stub() {
                        @Override
                        public void onRunResult(boolean isSuccess, int code, String msg) throws RemoteException {
                            LogUtils.e(TAG,"isSuccess="+isSuccess+" code="+code+ " msg="+msg);
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }else {
                String errorMsg = jsonObject.getString("msg");
                Utils.showToast(mActivity,errorMsg,1500);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        LogUtils.e(TAG,"onHiddenChanged:"+hidden);
        if(!hidden) {
            printer_setting.setChecked(true);
        }
    }

    /*
             * 检查版本信息
             */
    private void checkVersion() {
        Utils.showWaittingDialog(mActivity,"正在检测版本信息...");
        int versionCode = Constant.VERSION_CODE;
        NetWorks netWorks=new NetWorks(mActivity);
        netWorks.checkVersion(versionCode, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                Utils.dismissWaittingDialog();
                Utils.showToast(mActivity,"网络异常，请检查网络后重试！");
            }

            @Override
            public void onResonse(String response, int flag) {
                Utils.dismissWaittingDialog();
                upDateApp(response);
            }
        });
    }
    /**
     * 根据获取的版本信息进行操作
     */
    private void upDateApp(String response) {
        try {
            JSONObject jsonObject=new JSONObject(response);
           String url= jsonObject.getJSONObject("msg").getString("url");
           String updateinfo= jsonObject.getJSONObject("msg").getString("updateinfo");
            if (url!=null&&!"".equals(url)) {//版本不同，需要更新
                Utils.installNewVersion(mActivity,url,updateinfo);
            } else {
                Utils.showToast(mActivity,"当前已是最新版本！");
            }
        } catch (JSONException e) {

        }


    }
    public void exitAcount() {
        LogUtils.Log("重新登录帐号");
        if(exitPop!=null&&exitPop.isShowing()) {
            return;
        }
        exitPop = NoticePopuUtils.showBindPup(mActivity, "您确定要退出当前门店派账号吗？", R.id.main_container, new NoticePopuUtils.OnClickCallBack() {
            @Override
            public void onClickYes() {
//                SpUtils.getInstance(mActivity).remove(Constant.PARAMS_NAME_PASSWORDS);
//                SpUtils.getInstance(mActivity).remove(Constant.PARAMS_NAME_USERACCOUNT);
                SpUtils.getInstance(mActivity).remove(Constant.VOUCHER7);
                SpUtils.getInstance(mActivity).remove(Constant.MEMBER7);
                SpUtils.getInstance(mActivity).remove(Constant.VOUCHER14);
                SpUtils.getInstance(mActivity).remove(Constant.MEMBER14);
                Intent intent=new Intent(mActivity,LoginActivity.class);
                intent.putExtra(Constant.AUTO_LOGIN,false);
                startActivity(intent);


                mActivity.finish();
            }

            @Override
            public void onClickNo() {

            }
        });
    }

    private void setFragmet(int position) {
        LogUtils.e(TAG,"setFragment() position="+position);
        if(frManagerRight.getVisibility()==View.GONE) {
            frManagerRight.setVisibility(View.VISIBLE);
        }
        ManagerBasePager basePager = pagerList.get(position);//多态
        Constant.MANAGER_POSITION=position;

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(currentFragment==null) {
            currentFragment=fm.findFragmentByTag(pagerList.get(currentIndex).getClass().getSimpleName());
            if(currentFragment==null) {
                currentFragment=pagerList.get(currentIndex);
            }
        }
        if(currentFragment!=null) {
            ft.hide(currentFragment);
        }


        Fragment targetFragment = fm.findFragmentByTag(pagerList.get(position).getClass().getSimpleName());
        if(targetFragment==null) {
            targetFragment=pagerList.get(position);
        }
        if(!targetFragment.isAdded()) {
            ft.add(R.id.fr_manager_right,targetFragment,targetFragment.getClass().getSimpleName());
        }
        currentFragment=targetFragment;
        currentIndex=position;

        ft.show(targetFragment);
        ft.commit();
        if(basePager.isInit) {
            basePager.initData(position);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}