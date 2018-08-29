package com.younle.younle624.myapplication.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.orderguide.UpgradeAccountActivity;
import com.younle.younle624.myapplication.application.MyApplication;
import com.younle.younle624.myapplication.basepager.BasePager;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.SavedFailOrder;
import com.younle.younle624.myapplication.domain.membercharge.ChargeItem;
import com.younle.younle624.myapplication.fragment.MainGuideFragment;
import com.younle.younle624.myapplication.myservice.FailOrderService;
import com.younle.younle624.myapplication.myservice.PrintService;
import com.younle.younle624.myapplication.myservice.WxAppyService;
import com.younle.younle624.myapplication.pagers.ManagerPager;
import com.younle.younle624.myapplication.pagers.OrderPager;
import com.younle.younle624.myapplication.pagers.PosPager;
import com.younle.younle624.myapplication.pagers.VoucherPager;
import com.younle.younle624.myapplication.receiver.ScreenOnOffReceiver;
import com.younle.younle624.myapplication.utils.AlertUtils;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.utils.SaveUtils;
import com.younle.younle624.myapplication.utils.SpUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.utils.scanbar.ScanGunKeyEventHelper;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 程序主界面
 */
public class MainActivity extends FragmentActivity implements NetWorks.OnNetCallBack, ScanGunKeyEventHelper.OnScanSuccessListener,MainGuideFragment.OnFragmentInteractionListener {

    private FrameLayout flMain;
    private RadioGroup rgMain;
    private ArrayList<BasePager> pagerList;
    private int position = 0;
    private PosPager homePager;
    private OrderPager orderPager;
    private ManagerPager aboutPager;
    private VoucherPager voucherPager;
    private RadioButton btn_main_order;
    private RadioButton btn_main_pos;
    private RadioButton btn_main_voucher;
    private RadioButton btn_main_manager;
    private static final int HANDLER_INIT_SERVICE = 1;
    private String TAG = "MainActivity";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_INIT_SERVICE:
                    initService();
                    break;
            }
        }
    };
    private NetWorks netWorks;
    private String start_from;
    private ScreenOnOffReceiver receiver;
    private ScanGunKeyEventHelper scanGunKeyEventHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogUtils.Log("MainActivity重新加载了");
        //沉浸式状态栏
        Utils.initToolBarState(this);
        position = getIntent().getIntExtra("position", -1);
        start_from = getIntent().getStringExtra("start_from");
        int action=getIntent().getIntExtra("action",-1);
        if(action!=1) {
            //引导用户使用门店管家小程序
            showAppletQRcode();
        }
        //1.初始化试图
        findViews();
        initPager();
        btn_main_order = (RadioButton) findViewById(R.id.btn_main_order);
        btn_main_pos = (RadioButton) findViewById(R.id.btn_main_pos);
        btn_main_manager = (RadioButton) findViewById(R.id.btn_main_manager);
        btn_main_voucher = (RadioButton) findViewById(R.id.btn_main_voucher);
        guidePermission();
        netWorks = new NetWorks(this);
        initWm();
        if("jpush_receiver".equals(start_from)) {
            Intent intent=new Intent(this, UpgradeAccountActivity.class);
            ChargeItem payItem=new ChargeItem("wm");//最后一个字段需加上
            intent.putExtra("pay_item",payItem);
            startActivity(intent);
        }
        //开启小程序接单服务
        if(Constant.OPENED_PERMISSIONS.contains("8")||Constant.OPENED_PERMISSIONS.contains("9")) {
            newWxApp();
        }
    }
    private void newWxApp() {
        Intent intent=new Intent(this, WxAppyService.class);
        startService(intent);
    }

    /**
     * 展示门店派管家二维码
     */
    public  void showAppletQRcode() {

        Runnable showPopWindowRunnable = new Runnable() {
            @Override
            public void run() {
                // 得到activity中的根元素
                View view = findViewById(R.id.main_container);
                // 如何根元素的width和height大于0说明activity已经初始化完毕
                if( view != null && view.getWidth() > 0 && view.getHeight() > 0) {
                    // 显示popwindow
                    AlertUtils.getInstance().showWxAppPup(MainActivity.this,view);
                    // 停止检测
                    handler.removeCallbacks(this);
                } else {
                    // 如果activity没有初始化完毕则等待5毫秒再次检测
                    handler.postDelayed(this, 5);
                }
            }
        };
        // 开始检测
        handler.post(showPopWindowRunnable);
    }
    /**
     * 初始化外卖版块
     */
    private void initWm() {

        //测试小程序常开此开关
        if(Constant.OPEN_WM||Constant.wm_is_dead||Constant.OPEN_APPLET) {
            receiver = new ScreenOnOffReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            registerReceiver(receiver, filter);
            //1.各外卖门店绑定授权状态
            netWorks.getWmStoreStatus(this, 1);
            //2.绑定jpushId和门店id
            netWorks.getEpoiBindAccount(this, 0);

            List<Map> savedData = (List<Map>) SaveUtils.getObject(this, Utils.getToday());
            List<Map> savedBtData = (List< Map >) SaveUtils.getObject(this, "bt"+Utils.getToday());
            if(savedData!=null) {
                Constant.wm_data=savedData;
                LogUtils.Log("main wm_data=="+Constant.wm_data.size());
            }
            if(savedBtData!=null) {
                Constant.bt_wm_data=savedBtData;
            }
            Constant.pre_size = SpUtils.getInstance(this).getInt(Constant.PARA_PRE_SIZE, -1);
            Constant.bt_pre_size=SpUtils.getInstance(this).getInt(Constant.BT_PARA_PRE_SIZE,-1);
        }
    }

    /**
     * 获取epoiid或者绑定新的jpush结果
     * @param e
     * @param flag
     */
    @Override
    public void onError(Exception e, int flag) {
        LogUtils.Log("flag=="+flag);
        Toast.makeText(MainActivity.this, "网络异常，请稍后重试！", Toast.LENGTH_SHORT).show();
        LogUtils.Log("e==" + e.toString());
    }
    @Override
    public void onResonse(String response, int flag) {
        LogUtils.Log("response=="+response);
    }

    private void guidePermission() {
        if (Constant.OPENED_PERMISSIONS.contains("3")) {
            btn_main_voucher.setVisibility(View.VISIBLE);
        } else {
            btn_main_voucher.setVisibility(View.GONE);
        }
        if(Constant.OPENED_PERMISSIONS.contains("7")) {
            Constant.OPEN_WM=true;
            boolean serviceWork = Utils.isServiceWork(this, Constant.WM_PRINT_SERVICE);
            if(!serviceWork) {
                startService(new Intent(this, PrintService.class));
            }
        }else {
            Constant.OPEN_WM=false;
        }
        if(Constant.OPENED_PERMISSIONS.contains("8")||Constant.OPENED_PERMISSIONS.contains("9")){
            Constant.OPEN_APPLET=true;
            boolean serviceWork = Utils.isServiceWork(this, Constant.WM_PRINT_SERVICE);
            LogUtils.e(TAG,"servicework=="+serviceWork);
            if(!serviceWork) {
                startService(new Intent(this, PrintService.class));
            }
        }else{
            Constant.OPEN_APPLET=false;
        }
    }

    @Override
    protected void onResume() {
        LogUtils.Log("main onResume()...");
        if (position == 1) {//当会员营销没有开通时候：加载引导开通View;已经开通的时候加载未结账列表。
            LogUtils.e(TAG, "position == 1");
            setFragment();
        }
        super.onResume();
    }
    private void initPager() {
        pagerList = new ArrayList<>();
        //homePager = new PosPager(this);//收银
        homePager = PosPager.newInstance(MainActivity.this);//Pos付款
        //OrderPager orderPager = new OrderPager(this);//下单
        orderPager = OrderPager.newInstance(MainActivity.this);//下单
        //voucherPager = new VoucherPager(this);//核销
        voucherPager = VoucherPager.newInstance(MainActivity.this);//核销
        //ManagerPager aboutPager = new ManagerPager(this);//管理
        aboutPager = ManagerPager.newInstance(MainActivity.this);//管理

        pagerList.add(homePager);
        pagerList.add(orderPager);
        pagerList.add(voucherPager);
        pagerList.add(aboutPager);

        handler.sendEmptyMessageDelayed(HANDLER_INIT_SERVICE, 1000);
    }
    private void findViews() {
        flMain = (FrameLayout) findViewById(R.id.fl_main);
        rgMain = (RadioGroup) findViewById(R.id.rg_main);

        //给radiobutton设置监听
        rgMain.setOnCheckedChangeListener(new CheckedChangeListener());
        //默认第一项选中
        if (position==1){
            rgMain.check(R.id.btn_main_order);
            setFragment();
        }else if(position==4) {
            rgMain.check(R.id.btn_main_manager);
        }else {
            rgMain.check(R.id.btn_main_pos);
        }
    }

    @Override
    public void onScanSuccess(String barcode) {
        LogUtils.Log("扫描结果：" + barcode);
    }


    class CheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            //根据checkedid从list集合中取出不同的pager对象，添加到Fragment中
            switch (checkedId) {
                default:
                    position = 0;
                    break;
                case R.id.btn_main_pos:
                    position = 0;
                    break;
                case R.id.btn_main_order:
                    position = 1;
                    break;
                case R.id.btn_main_voucher:
                    position = 2;
                    break;
                case R.id.btn_main_manager://管理
                    position = 3;
                    break;
            }
            try {
                Constant.PAGER_POSITION = position;
                setFragment();
            }catch (Exception e){
                LogUtils.Log("main e=="+e.toString());
            }
        }
    }

    private void setFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = getFragment();
        ft.replace(R.id.fl_main, fragment);
        ft.commitAllowingStateLoss();
    }
    @Override
    public View onFragmentInteraction() {
        BasePager basePager = getBasePager();
        if (basePager != null) {
            return basePager.rootView;
        }
        return null;
    }


    @NonNull
    private Fragment getFragment() {
        LogUtils.e(TAG, "初始化fragment getFragment()");
        return MainGuideFragment.newInstance();
//        return new SelfFragment();
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
       /* scanGunKeyEventHelper.analysisKeyEvent(event);
        if (scanGunKeyEventHelper.isScanGunEvent(event)) {
            return true;
        }*/
        return super.dispatchKeyEvent(event);
    }
    @SuppressLint("ValidFragment")
    public class SelfFragment extends Fragment {
        public SelfFragment() {
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            LogUtils.e(TAG,"fragment OnCreateView()");
            BasePager basePager = getBasePager();
            if (basePager != null) {
                return basePager.rootView;
            }
            return null;
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
        }
    }

    private BasePager getBasePager() {
        LogUtils.e(TAG,"getBasePager() position="+position);
        guidePermission();
        //initService();
        BasePager basePager = pagerList.get(position);//多态
        basePager.Permissions();
        if (position == 2) {
            voucherPager.clearBeforeData();
        }
        if (basePager != null && position == 2) {
            basePager.initData();//代金券核销页面的多次进入
        } else if (basePager != null && !basePager.isInit) {
            if(position!=1){
                basePager.isInit = true;
            }
            basePager.initData();
        }
        if(position==3){//管理页面
            if(Constant.MESSAGE_ALREADYLOOKED){
                Constant.MESSAGE_ALREADYLOOKED = false;
                basePager.showMessageArrive();
            }
        }
        return basePager;
    }

    @Override
    protected void onDestroy() {
        if(receiver!=null) {
            unregisterReceiver(receiver);
        }
        super.onDestroy();
        for (int i = 0; i < pagerList.size(); i++) {
            if (pagerList.get(i) != null) {
                pagerList.get(i).onDestroy();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                new AlertDialog.Builder(this)
                        .setTitle("确定要回到系统主界面吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
                                mHomeIntent.addCategory(Intent.CATEGORY_HOME);
                                mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                                startActivity(mHomeIntent);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    /**
     * 初始化服务
     */
    private void initService() {
        //如果数据库不为空则初始化服务
        MyApplication myAppinstance = MyApplication.getInstance();
        DbManager.DaoConfig daoConfig = myAppinstance.getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<SavedFailOrder> allFailLists = db.findAll(SavedFailOrder.class);
            if (allFailLists != null && allFailLists.size() > 0) {
                LogUtils.Log(allFailLists.toString());
                boolean serviceWork = Utils.isServiceWork(MainActivity.this, Constant.SERVICE_UPDATE_FAIL_OR_CASHCARD);
                if (!serviceWork) {
                    LogUtils.e(TAG, "服务之前未打开，新开服务...");
                    Intent intent = new Intent(MainActivity.this, FailOrderService.class);
                    startService(intent);
                } else {
                    LogUtils.e(TAG, "服务已经打开，正在运行...");
                }
            } else {
                LogUtils.e(TAG, "检测是否需要开启服务:没有存储失败或现金刷卡订单不需开启服务");
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}

