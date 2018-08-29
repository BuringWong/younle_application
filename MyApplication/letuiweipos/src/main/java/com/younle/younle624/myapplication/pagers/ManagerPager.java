package com.younle.younle624.myapplication.pagers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.LoginActivity;
import com.younle.younle624.myapplication.activity.manager.DeviceInfoActivity;
import com.younle.younle624.myapplication.activity.manager.OrderManagerActivity;
import com.younle.younle624.myapplication.activity.manager.barscanner.BandBarScannerActivity;
import com.younle.younle624.myapplication.activity.manager.messagecenter.MsCenterActivity;
import com.younle.younle624.myapplication.activity.manager.ordersetting.OrderSettingActivity;
import com.younle.younle624.myapplication.activity.manager.printdeviceset.PrintDeviceSetting;
import com.younle.younle624.myapplication.activity.manager.weigher.WeigherManagerActivity;
import com.younle.younle624.myapplication.activity.orderguide.OpenOrderActivity;
import com.younle.younle624.myapplication.activity.takeoutfood.BandNumActivity;
import com.younle.younle624.myapplication.activity.takeoutfood.WaiMaiActivity;
import com.younle.younle624.myapplication.basepager.BasePager;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.domain.VersionUpdateInfo;
import com.younle.younle624.myapplication.myservice.PrintService;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.utils.NetworkUtils;
import com.younle.younle624.myapplication.utils.SpUtils;
import com.younle.younle624.myapplication.utils.UserCallback;
import com.younle.younle624.myapplication.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.MD5;

import java.io.File;
import java.util.Map;

import cn.modificator.waterwave_progress.WaterWaveProgress;
import okhttp3.Call;
import zxing.activity.CaptureActivity;


/**
 * Created by 我是奋斗 on 2016/5/11.
 * 微信/e-mail:tt090423@126.com
 */
@SuppressLint("ValidFragment")
public class ManagerPager extends BasePager implements View.OnClickListener {
    private RelativeLayout rl_inorder_setting;
    private RelativeLayout rl_print_setting;
    private RelativeLayout rl_yun_print;
    private RelativeLayout rl_order_setting;
    private RelativeLayout rl_take_out_food;
    private RelativeLayout rl_about;
    private RelativeLayout rl_exit;
    private RelativeLayout rl_member;
    private RelativeLayout rl_message_center;
    private TextView tv_messsage_arrive;
    private TextView tv_title;
    private RelativeLayout rl_check_version;
    private AlertDialog verisionCheckDia;
    private RelativeLayout rl_scanbar_setting,rl_weigher;

    private static final int FINFSH_CHECK = 1;
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case FINFSH_CHECK :
                    diaDismiss();
                    Utils.showToast(mActivity, "当前版本已是最新！");
                    break;
            }
        }
    };
//    private NumberCircleProgressBar rise_water;
    private WaterWaveProgress wp_progress;
    private LinearLayout ll_rise;
    private String TAG = "ManagerPager";
    private NetWorks.OnNetCallBack callBack=new NetWorks.OnNetCallBack() {
        @Override
        public void onError(Exception e, int flag) {
            showCloseWMErrDia();
        }

        @Override
        public void onResonse(String response, int flag) {
            try {
                JSONObject jsonObject=new JSONObject(response);
                int code = jsonObject.getInt("code");
                if(code==200) {
                    startExit();
                }else {
                    showCloseWMErrDia();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public ManagerPager(Activity activity) {
        super(activity);
        EventBus.getDefault().register(this);
    }

    public static final ManagerPager newInstance(Activity activity){
        ManagerPager fragment = new ManagerPager(activity);
        return fragment;
    }

    @Override
    public void Permissions() {
        judgePermission();
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.manager_layout, null);

        rl_scanbar_setting = (RelativeLayout) view.findViewById(R.id.rl_scanbar_setting);
        rl_weigher = (RelativeLayout) view.findViewById(R.id.rl_weigher);
        rl_about= (RelativeLayout) view.findViewById(R.id.rl_about);
        rl_order_setting= (RelativeLayout) view.findViewById(R.id.rl_order_setting);
        rl_take_out_food= (RelativeLayout) view.findViewById(R.id.rl_take_out_food);
        rl_inorder_setting= (RelativeLayout) view.findViewById(R.id.rl_inorder_setting);
        rl_exit = (RelativeLayout) view.findViewById(R.id.rl_exit);
        rl_check_version = (RelativeLayout) view.findViewById(R.id.rl_check_version);
        rl_member = (RelativeLayout) view.findViewById(R.id.rl_member);
        rl_message_center = (RelativeLayout) view.findViewById(R.id.rl_message_center);

        tv_messsage_arrive = (TextView) view.findViewById(R.id.tv_messsage_arrive);
//        rise_water = (NumberCircleProgressBar) view.findViewById(R.id.rise_water);
        ll_rise = (LinearLayout)view.findViewById(R.id.ll_rise);
        wp_progress = (WaterWaveProgress)view.findViewById(R.id.wp_progress);
        wp_progress.setShowProgress(false);

        rl_print_setting= (RelativeLayout) view.findViewById(R.id.rl_print_setting);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText("管理");

        setListener();
        return view;
    }

    /**
     * 判断是否有权限
     */
    private void judgePermission() {
//        LogUtils.Log("订单系统："+Constant.OPENED_PERMISSIONS.contains("5"));
        if(Constant.OPENED_PERMISSIONS.contains("4")) {
            rl_inorder_setting.setVisibility(View.GONE);
        }else {
            rl_inorder_setting.setVisibility(View.GONE);
        }
        if(Constant.OPENED_PERMISSIONS.contains("5")) {
            rl_order_setting.setVisibility(View.VISIBLE);
        }else {
            rl_order_setting.setVisibility(View.GONE);
        }
        if(Constant.OPENED_PERMISSIONS.contains("6")) {
            rl_print_setting.setVisibility(View.VISIBLE);
        }else {
            rl_print_setting.setVisibility(View.GONE);
        }
        if(Constant.OPENED_PERMISSIONS.contains("8")||Constant.OPENED_PERMISSIONS.contains("9")) {
            rl_message_center.setVisibility(View.VISIBLE);
        }else {
            rl_message_center.setVisibility(View.GONE);
        }
    }

    private void setListener() {
        rl_inorder_setting.setOnClickListener(this);
        rl_order_setting.setOnClickListener(this);
        rl_take_out_food.setOnClickListener(this);
        rl_about.setOnClickListener(this);
        rl_print_setting.setOnClickListener(this);
        rl_exit.setOnClickListener(this);
        rl_check_version.setOnClickListener(this);
        rl_member.setOnClickListener(this);
        rl_scanbar_setting.setOnClickListener(this);
        rl_weigher.setOnClickListener(this);
        rl_message_center.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    @Subscribe
    public void onEventMainThread(String order_come) {
        LogUtils.e(TAG,"onEventMainThread() order_come="+order_come);
        //通知了且判断MsCenterActivity不在栈顶
        boolean isTop = isMainActivityTop();
        LogUtils.e(TAG,"(\"manager_applet_come\".equals(order_come))="+("manager_applet_come".equals(order_come)));
        LogUtils.e(TAG,"isTop="+isTop);
        if("manager_applet_come".equals(order_come) && !isTop && Constant.PAGER_POSITION==3){
            tv_messsage_arrive.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showMessageArrive() {
        LogUtils.e(TAG,"showMessageArrive...");
        tv_messsage_arrive.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_inorder_setting ://收单设置
                Intent intent=new Intent(mActivity,OrderSettingActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.rl_member://会员充值
                Constant.MEMBER_ID="";
//                toCaptureActivity();
                Utils.showWaittingDialog(mActivity,"功能验证中...");
                //判断是否开通
                NetWorks netWorks=new NetWorks(mActivity);
                Map<String, String> params = netWorks.getPublicParams();
                params.put(Constant.PARAMS_NEME_ACCOUNT_ID,Constant.ACCOUNT_ID);
                params.put("advid",Constant.ADV_ID);
                netWorks.Request(UrlConstance.CHECK_RECHARGE,params,5000,0 ,new NetWorks.OnNetCallBack() {
                @Override
                public void onError(Exception e, int flag) {
                    Utils.dismissWaittingDialog();
                    Utils.showToast(mActivity,"当前网络异常，请检查网络后重试！",2000);
                }

                @Override
                public void onResonse(String response, int flag) {
                    LogUtils.Log("查询储值功能状态："+response.toString());
                    Utils.dismissWaittingDialog();
                    parseRechargeJson(response);
                }
            });
                break;
            case R.id.rl_order_setting ://订单管理
                 intent=new Intent(mActivity,OrderManagerActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.rl_about ://关于本机
                intent=new Intent(mActivity,DeviceInfoActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.rl_print_setting ://打印机设置
                intent=new Intent(mActivity,PrintDeviceSetting.class);
                mActivity.startActivity(intent);
                break;
            case R.id.rl_exit:
                exit();
                break;
            case R.id.rl_check_version://检查新版本
                verisionCheckDia = Utils.wybDialog(mActivity, false, R.layout.vsesion_check_dialog, 0, Utils.dip2px(mActivity, 200), Utils.dip2px(mActivity, 120), "");
                checkVersion();
                break;
            case R.id.rl_take_out_food://外卖管理
//                toWmActivity();
                NetWorks netWork=new NetWorks(mActivity);
                netWork.UpDateAuth(new NetWorks.OnGetQueryResult() {
                    @Override
                    public void stateOpen() {
                        Constant.OPEN_WM=true;
                        toWmActivity();
                    }

                    @Override
                    public void stateClose() {
                        toOpenActivity(1);
                    }
                },"7");

                break;
            case R.id.rl_scanbar_setting:
                intent = new Intent(mActivity, BandBarScannerActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.rl_weigher:
                intent = new Intent(mActivity, WeigherManagerActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.rl_message_center:
                intent = new Intent(mActivity, MsCenterActivity.class);
                mActivity.startActivity(intent);
                tv_messsage_arrive.setVisibility(View.GONE);
                Constant.MESSAGE_ALREADYLOOKED = false;
                break;
        }
    }

    /**
     * 检索是否开通了储值模块
     * @param json
     */
    private void parseRechargeJson(String json) {
        try {
            JSONObject object=new JSONObject(json);
            int code = object.getInt("code");
            if(code==200) {
                int status = object.getJSONObject("msg").getInt("status");
                if(status==0) {//没有开通智慧门店
                    toOpenActivity(0);
                }else if(status==1){//没有开通储值功能
                    Utils.showToast(mActivity,"您尚未开通储值功能，开通储值功能后可为会员储值。");
                }else if(status==2) {//开通了储值功能
                    toCaptureActivity();
                }
            }else {
                Utils.showToast(mActivity,"参数有误!",1000);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void toWmActivity() {
        Intent intent;
        mActivity.startService(new Intent(mActivity, PrintService.class));
        if(Constant.WM_STATUS.size()>0){//已开通并且有绑定门店
            intent=new Intent(mActivity, WaiMaiActivity.class);
            mActivity.startActivity(intent);
        }else if(Constant.WM_STATUS.size()<=0) {
            intent=new Intent(mActivity,BandNumActivity.class);
            mActivity.startActivity(intent);
        }
    }

    private void toCaptureActivity() {
        Intent intent=new Intent(mActivity, CaptureActivity.class);
        intent.putExtra(Constant.FROME_WHERE,Constant.MEMBER_CHARGE);
        intent.putExtra("commit","2");
        mActivity.startActivity(intent);
    }
    private void toOpenActivity(int which) {
        Intent intent=new Intent(mActivity, OpenOrderActivity.class);
        intent.putExtra("which_module",which);
        mActivity.startActivity(intent);
    }
    /**
     * 退出当前账号
     */
    private void exit() {
        boolean jieDanOpen = SpUtils.getInstance(mActivity).getBoolean(Constant.IS_JIEDAN_OPEN, false);
        if(jieDanOpen) {
            exitDia(R.string.exit_wm_open,true);
        }else {
            exitDia(R.string.exit_wm_off,false);
        }
    }

    private void exitDia(int strId, final boolean jieDanOpen) {
        View exitView = View.inflate(mActivity, R.layout.exit_app_dia, null);
        TextView tv_exit_notices= (TextView) exitView.findViewById(R.id.tv_exit_notices);
        tv_exit_notices.setText(strId);
        AlertDialog exitDia = new AlertDialog.Builder(mActivity)
                .setView(exitView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Constant.OPEN_APPLET = false;
                        if (jieDanOpen) {
                            NetWorks netWork = new NetWorks(mActivity);
                            netWork.switchWmStatus(callBack, 0, 0);
                            Toast.makeText(mActivity, "外卖自动接单模式关闭中...", Toast.LENGTH_SHORT).show();
                        } else {
                            startExit();
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
        exitDia.getWindow().setGravity(Gravity.CENTER_VERTICAL);
    }

    /**
     * 关闭自动接单失败的dialog
     */
    private void showCloseWMErrDia() {
        View exitView = View.inflate(mActivity, R.layout.exit_app_dia, null);
        TextView tv_exit_notices= (TextView) exitView.findViewById(R.id.tv_exit_notices);
        tv_exit_notices.setText(R.string.close_wm_error);
        new AlertDialog.Builder(mActivity)
                .setView(exitView)
                .setPositiveButton("去关闭自动接单", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mActivity.startActivity(new Intent(mActivity, WaiMaiActivity.class));
                    }
                })
                .setNegativeButton("直接退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startExit();
                    }
                })
                .show();
    }
    private void startExit() {
        SpUtils.getInstance(mActivity).remove("charge_model");
        SpUtils.getInstance(mActivity).save(Constant.IS_JIEDAN_OPEN,false);
        Utils.stopService(mActivity);
        sendToServer();
//        SpUtils.getInstance(mActivity).remove(Constant.PARAMS_NAME_PASSWORDS);
//        SpUtils.getInstance(mActivity).remove(Constant.PARAMS_NAME_USERACCOUNT);
        toLogin();
    }

    /**
     * 跳转至登录页面
     */
    private void toLogin() {
        Intent intent=new Intent(mActivity,LoginActivity.class);
        intent.putExtra(Constant.AUTO_LOGIN,false);
        mActivity.startActivity(intent);
        mActivity.finish();
    }

    private void checkVersion() {
        String version=getVersion();
        int versionCode = getVersionCode();
        if ("未知版本".equals(version)) {
            Utils.showToast(mActivity, "版本号有误！");
        } else {
            OkHttpUtils
                    .post()
                    .url(UrlConstance.VERSION_URL)
                    .addParams("version", versionCode+"")//版本号
                    .addParams("model","v1")
                    .build()
                    .execute(new UserCallback(VersionUpdateInfo.class) {
                        @Override
                        public void onError(Call call, Exception e) {
                            LogUtils.Log("异常" + e);
                            Utils.showToast(mActivity, "连接超时！");
                            diaDismiss();
                        }
                        @Override
                        public void onResponse(Object response) {
                            VersionUpdateInfo vuInfo;
                            vuInfo = (VersionUpdateInfo) response;
                            //拿到服务器上的版本号，判断是否相同，不同则下载
                            upDateApp(vuInfo);
                        }
                    });
        }
    }
    private int  getVersionCode() {
        try {
            PackageInfo packageInfo = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 根据获取的版本信息进行操作
     *
     * @param vuInfo
     */
    private void upDateApp(VersionUpdateInfo vuInfo) {
        if (vuInfo!=null&&!"".equals(vuInfo.getMsg().getUrl())) {//版本不同，需要更新
            ll_rise.setVisibility(View.VISIBLE);
            diaDismiss();
            downLoadApk(vuInfo);
        } else {
            //版本相同
            diaDismiss();
            Toast.makeText(mActivity, "当前已是最新版本！", Toast.LENGTH_SHORT).show();
        }
    }

    private void diaDismiss() {
        if(verisionCheckDia!=null&&verisionCheckDia.isShowing()) {
            verisionCheckDia.dismiss();
        }
    }



    /**
     * 获取当前版本号
     */
    private String getVersion() {
         String currentVersion;currentVersion = "未知版本";
        PackageManager manager =mActivity. getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(mActivity.getPackageName(), 0);
            currentVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            //e.printStackTrace(); //如果找不到对应的应用包信息, 就返回"未知版本"
        }
        return currentVersion;
    }

    /**
     * 发送退出通知到服务器
     */
    private void sendToServer() {
        if(NetworkUtils.isAvailable(mActivity)) {

            //获取时间戳 1473410972494并截取前10位
            String currentTime = Utils.getCurrentTime();
            //获取token
            String token = Utils.getToken(currentTime,mActivity);

            OkHttpUtils.post()
                    .url(UrlConstance.EXIT_CURRENT)
                    .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                    .addParams(Constant.PARAMS_NAME_POSTOKEN,token)
                    .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                    .addParams("userkey", MD5.md5(MD5.md5(Constant.PASSWORD)))//密码
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {

                        }
                        @Override
                        public void onResponse(String response) {

                        }
                    });
        }else {
            return;
        }
    }
    /**
     * 下载新的apk
     *
     * @param vuInfo
     */
    private void downLoadApk(VersionUpdateInfo vuInfo) {
        OkHttpUtils//
                .get()//
                .url(vuInfo.getMsg().getUrl())//
                .build()//
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "letuiweipos.apk")//
                {
                    @Override
                    public void inProgress(float progress, long total) {
                        wp_progress.setProgress((int) (progress * 100));
                        if (wp_progress.getProgress() == 100) {
                            ll_rise.setVisibility(View.INVISIBLE);
//                            self_image.setVisibility(View.GONE);
                            installApk();
                        }
                    }
                    @Override
                    public void onError(Call call, Exception e) {
                        LogUtils.Log("异常" + e);
                        Utils.showToast(mActivity, "连接超时！");
//                        self_image.setVisibility(View.GONE);
//                        ll_update.setVisibility(View.GONE);
                        ll_rise.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onResponse(File file) {

                    }
                });
    }
    /**
     * 安装新下载的apk
     */
    private void installApk() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/letuiweipos.apk"),
                "application/vnd.android.package-archive");
        mActivity.startActivity(intent);
    }

    /**
     *
     * 判断mainactivity是否处于栈顶
     * @return  true在栈顶false不在栈顶
     */
    private boolean isMainActivityTop(){
        ActivityManager manager = (ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(MsCenterActivity.class.getName());
    }
}
