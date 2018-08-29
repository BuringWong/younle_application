package com.yongle.letuiweipad.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sunmi.electronicscaleservice.ScaleCallback;
import com.sunmi.scalelibrary.ScaleManager;
import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.activity.manager.weigher.OnWeighChangeListener;
import com.yongle.letuiweipad.activity.manager.weigher.WeighManager;
import com.yongle.letuiweipad.application.ThreadPoolManager;
import com.yongle.letuiweipad.base.BaseActivity;
import com.yongle.letuiweipad.basepager.BasePager;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.domain.ManagerScanBean;
import com.yongle.letuiweipad.domain.PrintDevices;
import com.yongle.letuiweipad.pagers.FindOrderPager;
import com.yongle.letuiweipad.pagers.FinishedOrderPager;
import com.yongle.letuiweipad.pagers.ManagerPager;
import com.yongle.letuiweipad.pagers.OrderPgerFragment;
import com.yongle.letuiweipad.pagers.PriceTagPager;
import com.yongle.letuiweipad.pagers.ScalePager;
import com.yongle.letuiweipad.pagers.VoucherPager;
import com.yongle.letuiweipad.selfinterface.BleWeighListener;
import com.yongle.letuiweipad.selfinterface.LocalWeighListener;
import com.yongle.letuiweipad.utils.FpUtils;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.NoticePopuUtils;
import com.yongle.letuiweipad.utils.SaveUtils;
import com.yongle.letuiweipad.utils.SpUtils;
import com.yongle.letuiweipad.utils.Utils;
import com.yongle.letuiweipad.utils.printmanager.PrintUtils;
import com.yongle.letuiweipad.utils.scanbar.ScanGunKeyEventHelper;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import sunmi.ds.DSKernel;
import sunmi.ds.callback.IConnectionCallback;
import sunmi.ds.callback.IReceiveCallback;
import sunmi.ds.data.DSData;
import sunmi.ds.data.DSFile;
import sunmi.ds.data.DSFiles;
import woyou.aidlservice.jiuiv5.IWoyouService;

public class MainActivity extends BaseActivity {
    public RadioGroup rgMain;
    private int position = 0;
    private OrderPgerFragment orderPager;
    private OrderPgerFragment findOrderPager;
    private FinishedOrderPager finishedOrderPager;
    private VoucherPager voucherPager;
    private ManagerPager managerPager;
    private FragmentManager fm;
    private Fragment currentItem;
    private List<Fragment> fragmentList=new ArrayList<>();
    private TextView hh_mm,yy_mm_dd;
    private DateFormat df_hh_mm;
    private DateFormat df_yy_mm_dd;
    @BindView(R.id.tv_current_acc) TextView tv_current_acc;
    @BindView(R.id.btn_create_order) RadioButton btn_create_order;
    @BindView(R.id.btn_find_order) RadioButton btn_find_order;
    @BindView(R.id.btn_finished_order) RadioButton btn_finished_order;
    @BindView(R.id.btn_main_manager) RadioButton btn_main_manager;
    @BindView(R.id.guide_dic) LinearLayout guide_dic;
    @BindView(R.id.pricetag_line) LinearLayout pricetag_line;
    @BindView(R.id.scale_line) LinearLayout scale_line;
    @BindView(R.id.btn_scale) RadioButton btn_scale;
    @BindView(R.id.btn_ticket) RadioButton btn_ticket;


    private List<Long> picList=new ArrayList<>();

    private final int UPDATE_TIME=0;
    private final int UPDATE_PIC=1;
    private int fpPoi=0;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case UPDATE_TIME :
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setTime();
                        }
                    });
                    break;
                case UPDATE_PIC:
                    if(fpPoi>=picList.size()-1) {
                        fpPoi=0;
                    }else {
                        fpPoi++;
                    }
                    FpUtils.changFpImg(dsKernel,picList.get(fpPoi));
                    Message message = handler.obtainMessage();
                    message.what=UPDATE_PIC;
                    handler.sendMessageDelayed(message,Constant.FPPIC_UPDATE_TIME);
                    break;
            }
        }
    };
    private ScanGunKeyEventHelper scanGunKeyEventHelper;
    private PrintDevices savedDevice;
    private BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
    private IWoyouService iWoyouService;

    public ServiceConnection connService = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iWoyouService = IWoyouService.Stub.asInterface(service);//拿到打印服务的对象
            if(orderPager!=null) {
                orderPager.setPrintService(iWoyouService);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iWoyouService = null;
        }
    };
    private boolean alive=true;
    private Unbinder bind;
    public DSKernel dsKernel;
    IConnectionCallback mConnCallback = new IConnectionCallback() {// SDK链接状态回调
        @Override
        public void onDisConnect() {

        }

        @Override
        public void onConnected(final ConnState state) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    switch (state) {
                        case AIDL_CONN:
                            //与本地service的连接畅通
                            break;
                        case VICE_SERVICE_CONN:
                            //与副屏service连接畅通
                            break;
                        case VICE_APP_CONN:
                            //与副屏app连接畅通
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    };
    IReceiveCallback mReceiveCallback = new IReceiveCallback() {// 接收副屏数据的回调

        @Override
        public void onReceiveFile(DSFile arg0) {

        }

        @Override
        public void onReceiveFiles(DSFiles dsFiles) {

        }

        @Override
        public void onReceiveData(DSData data) {

        }

        @Override
        public void onReceiveCMD(DSData arg0) {

        }
    };
    private final String TAG="main";
    private ScalePager scalePager;
    private PriceTagPager priceTagPager;
    public ScaleManager scaleManager;
    private boolean everShowWeighErr;
    private final int NEW_BLE_WEIGHER=1;
    public PrintDevices weigher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);
        Constant.XS_IN=Utils.containUsbScanner(this);
        Constant.SCANNER_NAME = SpUtils.getInstance(this).getString(Constant.BT_SCANNER_NAME, null);
        //提醒更新商品
        showUpdateGoodNotice();
        //提醒使用门店小程序
        int action=getIntent().getIntExtra("action",-1);
        if(action!=1) {
            showAppletQRcode();
        }
        LinearLayout.LayoutParams params;
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.2f);
        scale_line.setVisibility(View.VISIBLE);
        pricetag_line.setVisibility(View.VISIBLE);
        btn_scale.setVisibility(View.VISIBLE);
        btn_ticket.setVisibility(View.VISIBLE);
        if(Build.MODEL.contains("t1")) {
            ThreadPoolManager.getInstance().executeTask(new Runnable() {
                @Override
                public void run() {
                    readResFile();
                }
            });
            intiFp();
        }
        guide_dic.setLayoutParams(params);
        PrintUtils.getInstance().initService(this, connService);
        tv_current_acc.setText(Constant.USER_ACCOUNT);
        df_hh_mm = new SimpleDateFormat("HH:mm:ss");
        df_yy_mm_dd = new SimpleDateFormat("yyyy/MM/dd");
        //沉浸式状态栏
        Utils.initToolBarState(this);
        initPager(savedInstanceState);
        findViews();
        //初始化电子称扫码枪
        initWeigherBarScanner();
    }

    private void showUpdateGoodNotice() {
        boolean notice= SpUtils.getInstance(this).getBoolean("notice",true);
        if(notice) {
            new AlertDialog.Builder(this)
                    .setTitle("如您有变更后台商品信息，请及时点击右上角“更新商品”，获取最新商品信息")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton("不再提示", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SpUtils.getInstance(MainActivity.this).save("notice",false);
                        }
                    })
                    .show();

        }
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
                    NoticePopuUtils.showWxAppPup(MainActivity.this,view);
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
    private void readResFile() {
        String voucher7_id=SpUtils.getInstance(this).get(Constant.VOUCHER7,null);
        String member7_id=SpUtils.getInstance(this).get(Constant.MEMBER7,null);
        String voucher14_id=SpUtils.getInstance(this).get(Constant.VOUCHER14,null);
        String member14_id=SpUtils.getInstance(this).get(Constant.MEMBER14,null);

        if(voucher7_id==null||member7_id==null||voucher14_id==null||member14_id==null) {
            SaveUtils.writeAssetTofp(this,"");
            try {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FpUtils.saveFile(dsKernel,MainActivity.this);
                    }
                },1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void intiFp() {
        try {
            dsKernel = DSKernel.newInstance();
            dsKernel.init(this,mConnCallback);
            dsKernel.addReceiveCallback(mReceiveCallback);
            startFpLb();
        }catch (Exception e){

        }
    }

    /**
     * 开启副屏轮播
     */
    public void startFpLb() {
        if(dsKernel==null) {
            return;
        }
        picList= (List<Long>) SaveUtils.getObject(this, Constant.FP_PICID);
        if(picList!=null&&picList.size()>0) {
            handler.removeMessages(UPDATE_PIC);
            FpUtils.changFpImg(dsKernel,picList.get(0));
            Message message = handler.obtainMessage();
            message.what=UPDATE_PIC;
            handler.sendMessageDelayed(message,Constant.FPPIC_UPDATE_TIME);
        }else {
            FpUtils.changFpImg(dsKernel,0);
        }
    }

    public void stopFpLb(){
        handler.removeMessages(UPDATE_PIC);
    }

    /**
     * 退出账号
     * @param
     */
    @OnClick(R.id.exit_account)
    public void exitAcount(View v) {

        LogUtils.Log("重新登录帐号");
        NoticePopuUtils.showBindPup(this, "您确定要退出当前门店派账号吗？", R.id.main_container, new NoticePopuUtils.OnClickCallBack() {
            @Override
            public void onClickYes() {
//                SpUtils.getInstance(MainActivity.this).remove(Constant.PARAMS_NAME_PASSWORDS);
//                SpUtils.getInstance(MainActivity.this).remove(Constant.PARAMS_NAME_USERACCOUNT);
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                intent.putExtra(Constant.AUTO_LOGIN,false);
                startActivity(intent);
                finish();
            }

            @Override
            public void onClickNo() {

            }
        });
    }


    private void initPager(Bundle savedInstanceState) {
        fm = getSupportFragmentManager();
        if(savedInstanceState!=null) {
            LogUtils.e("TAG","加载保存的页面");
            orderPager = (OrderPgerFragment) fm.findFragmentByTag(OrderPgerFragment.class.getSimpleName());
            findOrderPager = (OrderPgerFragment) fm.findFragmentByTag(FindOrderPager.class.getSimpleName());
            finishedOrderPager = (FinishedOrderPager) fm.findFragmentByTag(FinishedOrderPager.class.getSimpleName());
            voucherPager = (VoucherPager) fm.findFragmentByTag(VoucherPager.class.getSimpleName());
            managerPager = (ManagerPager) fm.findFragmentByTag(ManagerPager.class.getSimpleName());

            scalePager=(ScalePager)fm.findFragmentByTag(ScalePager.class.getSimpleName());
            priceTagPager=(PriceTagPager) fm.findFragmentByTag(PriceTagPager.class.getSimpleName());

            fragmentList.add(orderPager);
            fragmentList.add(findOrderPager);
            fragmentList.add(finishedOrderPager);
            fragmentList.add(voucherPager);
            fragmentList.add(scalePager);
            fragmentList.add(priceTagPager);
            fragmentList.add(managerPager);
            int last_position = savedInstanceState.getInt("last_position");
            switch (last_position) {
                case 0 :
                    fm.beginTransaction()
                            .show(orderPager)
                            .hide(findOrderPager)
                            .hide(finishedOrderPager)
                            .hide(voucherPager)
                            .hide(managerPager)
                            .hide(scalePager)
                            .hide(priceTagPager)
                            .commit();
                    break;
                case 1:
                    fm.beginTransaction()
                            .show(findOrderPager)
                            .hide(orderPager)
                            .hide(finishedOrderPager)
                            .hide(voucherPager)
                            .hide(managerPager)
                            .hide(scalePager)
                            .hide(priceTagPager)
                            .commit();
                    break;
                case 2:
                    fm.beginTransaction()
                            .show(finishedOrderPager)
                            .hide(orderPager)
                            .hide(findOrderPager)
                            .hide(voucherPager)
                            .hide(managerPager)
                            .hide(scalePager)
                            .hide(priceTagPager)
                            .commit();
                    break;
                case 3:
                    fm.beginTransaction()
                            .show(voucherPager)
                            .hide(orderPager)
                            .hide(finishedOrderPager)
                            .hide(findOrderPager)
                            .hide(managerPager)
                            .hide(scalePager)
                            .hide(priceTagPager)
                            .commit();
                    break;
                case 4:
                    fm.beginTransaction()
                            .show(scalePager)
                            .hide(orderPager)
                            .hide(finishedOrderPager)
                            .hide(findOrderPager)
                            .hide(managerPager)
                            .hide(voucherPager)
                            .hide(priceTagPager)
                            .commit();
                    break;
                case 5:
                    fm.beginTransaction()
                            .show(priceTagPager)
                            .hide(orderPager)
                            .hide(finishedOrderPager)
                            .hide(findOrderPager)
                            .hide(managerPager)
                            .hide(voucherPager)
                            .hide(scalePager)
                            .commit();
                    break;
                case 6:
                    fm.beginTransaction()
                            .show(managerPager)
                            .hide(voucherPager)
                            .hide(orderPager)
                            .hide(finishedOrderPager)
                            .hide(findOrderPager)
                            .hide(scalePager)
                            .hide(priceTagPager)
                            .commit();
                    break;
            }
        }else {
            orderPager =new OrderPgerFragment();
            findOrderPager =new OrderPgerFragment();
            finishedOrderPager =new FinishedOrderPager();
            voucherPager =new VoucherPager();
            managerPager=new ManagerPager();

            scalePager = new ScalePager();
            priceTagPager = new PriceTagPager();

            fragmentList.add(orderPager);
            fragmentList.add(findOrderPager);
            fragmentList.add(finishedOrderPager);
            fragmentList.add(voucherPager);
            fragmentList.add(scalePager);
            fragmentList.add(priceTagPager);
            fragmentList.add(managerPager);
        }
    }

    private void findViews() {
        hh_mm= (TextView) findViewById(R.id.hh_mm);
        yy_mm_dd = (TextView)findViewById(R.id.yy_mm_dd);
        setTime();

        rgMain = (RadioGroup) findViewById(R.id.rg_main);
        rgMain.setOnCheckedChangeListener(new CheckedChangeListener());
        btn_create_order.setChecked(true);
    }

    private void setTime() {
        hh_mm.setText(df_hh_mm.format(System.currentTimeMillis()));
        yy_mm_dd.setText(df_yy_mm_dd.format(System.currentTimeMillis()));
        handler.sendEmptyMessageDelayed(UPDATE_TIME,1000);

    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//        Utils.showToast(this,"mainActivity中dispatchKeyEvent（）");

        if(scanGunKeyEventHelper!=null&&scanGunKeyEventHelper.isScanGunEvent(event)) {
            scanGunKeyEventHelper.analysisKeyEvent(event);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public void weigherDel() {
        weigher=null;
        if(bleWeighListener!=null) {
            bleWeighListener.onWeighDel();
        }
    }


    class CheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            //根据checkedid从list集合中取出不同的pager对象，添加到Fragment中
            switch (checkedId) {
                default:
                    position = 0;
                    break;
                case R.id.btn_create_order:
                    position = 0;
                    break;
                case R.id.btn_find_order:
                    position = 1;
                    break;
                case R.id.btn_finished_order:
                    position = 2;
                    break;
                case R.id.btn_voucher:
                    position = 3;
                    break;
                case R.id.btn_scale:
                    position = 4;
                    break;
                case R.id.btn_ticket:
                    position = 5;
                    break;

                case R.id.btn_main_manager:
                    position = 6;
                    break;
            }
            setFragment(position);
        }
    }
    int currentIndex;
    private void setFragment(int position) {
        LogUtils.e("temp","main setFragment=="+position);
        Constant.MAIN_POSITION=position;
        int temp=position;
        if(position==1) {//下单和找单均加载orderfragment
            position=0;
        }
        if(position==4) {
            position=5;
        }

        FragmentTransaction ft = fm.beginTransaction();
        BasePager basePager = (BasePager) fragmentList.get(position);//多态
        basePager.position=position;
        if(currentItem==null) {
            LogUtils.Log("currentItem==null");
            currentItem = fm.findFragmentByTag(fragmentList.get(currentIndex).getClass().getSimpleName());
            if(currentItem==null) {
                LogUtils.Log("currentItem仍然为null");
                currentItem=fragmentList.get(currentIndex);
            }
        }
        ft.hide(currentItem);
        //1.先从事物中获取
        Fragment targetFragment = fm.findFragmentByTag(fragmentList.get(position).getClass().getSimpleName());
        //2.事物中不存在时再从集合中获取
        if(targetFragment==null) {
            targetFragment = fragmentList.get(position);
        }

        if(!targetFragment.isAdded()) {
            ft.add(R.id.fl_main, targetFragment, targetFragment.getClass().getSimpleName());
        }
        currentItem=targetFragment;
        if(currentIndex==0||currentIndex==1||currentIndex==4) {
            startFpLb();
        }
        currentIndex=position;
        ft.show(targetFragment);
        ft.commit();
        fm.executePendingTransactions();
        if(basePager.isInit) {
            LogUtils.e("temp","main   setFragment:"+position);
            if(position==0||position==1||position==2||position==6||position==5) {
                basePager.initData(temp);
            }
//            basePager.initData(temp);
        }
    }
    @Override
    protected void onDestroy() {
        try {
            bind.unbind();
            alive=false;

            handler.removeCallbacksAndMessages(null);
            if(connService!=null) {
                unbindService(connService);
            }
            try{
                unbindService(WeighManager.getInstance().mServiceConnection);
            }catch (Exception e){
                LogUtils.Log("解绑电子秤服务异常："+e.toString());
            }
            if(orderPager !=null&& orderPager.isAdded()) {
                orderPager.onDestroy();
            }
            if(findOrderPager !=null&& findOrderPager.isAdded()) {
                findOrderPager.onDestroy();
            }
            if(finishedOrderPager !=null&& finishedOrderPager.isAdded()) {
                finishedOrderPager.onDestroy();
            }
            if(voucherPager !=null&& voucherPager.isAdded()) {
                voucherPager.onDestroy();
            }
            if(managerPager!=null&&managerPager.isAdded()) {
                managerPager.onDestroy();
            }
            if(scalePager!=null&&scalePager.isAdded()) {
                scalePager.onDestroy();
            }
            WeighManager.getInstance().unRegistReceiver(this);
        } catch (Exception e) {
            LogUtils.e(TAG,"e=="+e.toString());
            e.printStackTrace();
        }
        super.onDestroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtils.e(TAG,"onKeyDown()");
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                NoticePopuUtils.showBindPup(this, "确定要回到系统主界面吗？", R.id.main_container, new NoticePopuUtils.OnClickCallBack() {
                    @Override
                    public void onClickYes() {
                        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
                        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
                        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                        startActivity(mHomeIntent);
                    }

                    @Override
                    public void onClickNo() {

                    }
                });

                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        LogUtils.e("TAG","onSaveInstanceState()");
        outState.putInt("last_position",position);
        super.onSaveInstanceState(outState);
    }

    LocalWeighListener localWeighListener;
    BleWeighListener bleWeighListener;

    public void setBleWeighListener(BleWeighListener bleWeighListener) {
        this.bleWeighListener = bleWeighListener;
    }

    public void setLocalWeighListener(LocalWeighListener localWeighListener) {
        this.localWeighListener = localWeighListener;
    }

    private void initWeigherBarScanner() {
        //蓝牙
        initBleWeigher();
        //本地
        if(Build.MODEL.contains("S2")) {
            scaleManager = ScaleManager.getInstance(this);
            scaleManager.connectService(new ScaleManager.ScaleServiceConnection() {
                @Override
                public void onServiceConnected() {
                    if(localWeighListener!=null) {
                        localWeighListener.onWeighConnect();
                    }
                    LogUtils.e(TAG,"电子称服务已连接");
                    try {
                        scaleManager.getData(new ScaleCallback.Stub() {
                            @Override
                            public void getData(final int net, final int tare, final int status) throws RemoteException {
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(localWeighListener!=null) {
                                            localWeighListener.onWeighDataChange(net,tare,status);
                                        }
                                    }
                                });
                            }
                        });
                    }catch (Exception e){
                        LogUtils.e(TAG,"电子秤数据异常：e="+e.toString());
                    }
                }

                @Override
                public void onServiceDisconnect() {
                    if(localWeighListener!=null) {
                        localWeighListener.onWeighDisconnect();
                    }
                }
            });
        }
        savedDevice = (PrintDevices) SaveUtils.getObject(this, Constant.SAVED_BLUETOOTH_SCANNER);
        connectScanner();
    }

    public void initBleWeigher() {
        weigher = (PrintDevices) SaveUtils.getObject(this, Constant.SAVED_BLUETOOTH_WEIGHER);
        if(weigher !=null) {
            BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(weigher.getBlueToothAdd());
            if(device!=null) {
                Constant.SAVED_WEIGH_NAME =weigher.getReMarkName();
                if(Constant.SAVED_WEIGH_NAME==null) {
                    Constant.SAVED_WEIGH_NAME="";
                }
            }
            WeighManager.getInstance().registReceiver(this);
            WeighManager.getInstance().initWigher(this, weigher.getBlueToothAdd(), new OnWeighChangeListener() {
                @Override
                public void onWeighChange(String data) {
                    LogUtils.e(TAG,"data="+data);
                    int weigh_version;
//                    String weigh_unit="g";
                    double weigh_acc=0;
                    String weigh_mode="";
                    String weigh_zf="+";
                    int status=0;
                    if(Constant.SAVED_WEIGH_NAME.contains(Constant.WEIGHER_NAME)) {
                         weigh_version=NEW_BLE_WEIGHER;
                        if(data.contains("E 2")||data.contains("E 1")) {
                            if(everShowWeighErr) {
                                return;
                            }
                            everShowWeighErr=true;
                            NoticePopuUtils.showOfflinDia(MainActivity.this, "电子秤异常，错误码："+data+"请联系乐推微解决！",null);
                            return;
                        }
                        try{
//                            weigh_unit = data.substring(14);
                            //数量
                            weigh_acc = Double.valueOf(data.substring(7,14));
                            weigh_mode = data.substring(3, 5);
                            //正负
                            weigh_zf = data.substring(6, 7);
                            if("ST".equals(data.substring(0, 2))) {
                                status= 1;
                            }else {
                                status=0;
                            }
                        }catch (Exception e){
                            return;
                        }
                        if(bleWeighListener!=null) {
                            bleWeighListener.onWeighDataChange(weigh_version,weigh_acc,weigh_mode,weigh_zf,status,"g");
                        }
                    }else {
                        weigh_version=0;
                        if(bleWeighListener!=null) {
                            try {
                                bleWeighListener.onWeighDataChange(weigh_version,Double.valueOf(data),null,null,1,"kg");
                            }catch (Exception e){

                            }
                        }
                    }

                }
                @Override
                public void onWeighConnected() {
                    LogUtils.e(TAG,"onWeighConnected()");
                    if(Constant.SAVED_WEIGH_NAME==null) {
                        return;
                    }
                    if(bleWeighListener!=null) {
                        bleWeighListener.onWeighConnect();
                    }
                }

                @Override
                public void onWeighDiconnected() {
                    LogUtils.e(TAG,"onWeighDiconnected()");

                    if(bleWeighListener!=null) {
                        bleWeighListener.onWeighDisconnect();
                    }
                }

                @Override
                public void onWeighConnecting() {
                    LogUtils.e(TAG,"onWeighConnecting()");

                    if(bleWeighListener!=null) {
                        bleWeighListener.onWeighConnecting();
                    }
                }
            });
        }
    }

    private void connectScanner() {
        boolean enabled = bluetoothAdapter.isEnabled();
        if(!enabled) {
            bluetoothAdapter.enable();
        }
        scanGunKeyEventHelper=new ScanGunKeyEventHelper(new ScanGunKeyEventHelper.OnScanSuccessListener() {
            @Override
            public void onScanSuccess(String barcode) {
                LogUtils.saveLog(TAG,"main中扫到条码："+barcode);
                if(barcode!=null&& !TextUtils.isEmpty(barcode)) {
                    if(position==6) {//管理扫码
                        ManagerScanBean scanBean=new ManagerScanBean(barcode);
                        EventBus.getDefault().post(scanBean);
                    }else if(position==0||position==1) {
                        EventBus.getDefault().post(barcode);
                    }
                }
            }
        });
    }

    public void connectWeigher(){
        if(weigher!=null) {
            WeighManager.getInstance().connectWeigh(weigher.getBlueToothAdd());
        }
    }

    private void registBarScanReceiver() {
        IntentFilter mFilter=new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        mFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        mFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(receiver, mFilter);
    }
    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 获得已经搜索到的蓝牙设备
            if(action.equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)){
                int conectState = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, BluetoothAdapter.ERROR);
                LogUtils.e(TAG, "connectState==" + conectState);
                BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(conectState==0||conectState==1) {
                }else if(conectState==2) {
                    if(device.getAddress().equals(savedDevice.getBlueToothAdd())) {
                        LogUtils.e(TAG, "device=="+device);
                    }
                }
            }else if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                LogUtils.e(TAG,"state=="+state);
            }
        }
    };

}
