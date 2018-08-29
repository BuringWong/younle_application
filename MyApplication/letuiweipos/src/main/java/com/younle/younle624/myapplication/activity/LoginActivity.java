package com.younle.younle624.myapplication.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.domain.LoginInfo;
import com.younle.younle624.myapplication.domain.VersionUpdateInfo;
import com.younle.younle624.myapplication.domain.printsetting.SavedPrinter;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.utils.SaveUtils;
import com.younle.younle624.myapplication.utils.SpUtils;
import com.younle.younle624.myapplication.utils.UserCallback;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.view.SelfImageView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.MD5;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.modificator.waterwave_progress.WaterWaveProgress;
import okhttp3.Call;



public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";
    private Button btn_login;
    private EditText et_login_username;
    private EditText et_login_password;
    private TextView tv_login_findpassword;
    private TextView tv_login_regist;
    private LinearLayout ll_user_input;
    private String currentVersion;
    private String version;
    private ProgressBar pb_loading;
    private SelfImageView self_image;
    private LinearLayout ll_update;
//    private NumberCircleProgressBar rise_water;
    private WaterWaveProgress wp_progress;
    private LinearLayout ll_rise;
    private TencentLocationManager tencentLocationManager;
    private TencentLocationRequest request;
    private TencentLocationListener locationListener;
    private double latitude;
    private double longitude;
    private SpannableString ssp;
    private LoginInfo loginInfo;
    private String pass_words;
    private String user_id;
    private String imei;
    private String sn;
    private String start_from;
    private String savedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Utils.initToolBarState(this);
        if(BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            BluetoothAdapter.getDefaultAdapter().disable();
        }
        start_from = getIntent().getStringExtra("start_from");
        locationListener = new MyTencentLocationListener();
        SpUtils.getInstance(this).save(Constant.IS_JIEDAN_OPEN,false);
        //本地打印机的打印分组初始化
        SavedPrinter savedPrinter = (SavedPrinter) SaveUtils.getObject(this, Constant.LOCAL_PRINTER);
        if(savedPrinter ==null) {
            savedPrinter =new SavedPrinter();
            savedPrinter.setPrintGroupName("全部分组");
            savedPrinter.setPrintGroupId(new ArrayList<String>());
            SaveUtils.saveObject(this,Constant.LOCAL_PRINTER,savedPrinter);
        }
        initView();
        getSpData();
        setListener();
        //3.检查版本信息
        checkVersion();
    }


    /**
     * 读取sp存储的信息，登录信息
     */
    private void getSpData() {
        user_id = SpUtils.getInstance(this).getString(Constant.PARAMS_NAME_USERACCOUNT, "");
        savedUser=user_id;
        
        pass_words = SpUtils.getInstance(this).getString(Constant.PARAMS_NAME_PASSWORDS, "");
        if(user_id !=null&&!TextUtils.isEmpty(user_id)) {
            et_login_username.setText(user_id);
        }
        if(pass_words !=null&& !TextUtils.isEmpty(pass_words)) {
            et_login_password.setText(pass_words);
        }
    }

    private void initView() {
        ll_rise = (LinearLayout) findViewById(R.id.ll_rise);
//        rise_water = (NumberCircleProgressBar) findViewById(R.id.rise_water);
        wp_progress = (WaterWaveProgress) findViewById(R.id.wp_progress);
        wp_progress.setShowProgress(false);
        ll_update = (LinearLayout) findViewById(R.id.ll_update);
        self_image = (SelfImageView) findViewById(R.id.self_image);
        btn_login = (Button) findViewById(R.id.btn_login);
        et_login_username = (EditText) findViewById(R.id.et_login_username);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
        tv_login_findpassword = (TextView) findViewById(R.id.tv_login_findpassword);
        tv_login_regist = (TextView) findViewById(R.id.tv_login_regist);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
        ll_user_input = (LinearLayout)findViewById(R.id.ll_user_input);
        //图文混排，spanable
        ssp = new SpannableString(getString(R.string.how_to_create_account));
        ssp.setSpan(new ClickableSpan() {
           @Override
           public void updateDrawState(TextPaint ds) {
               ds.setUnderlineText(false);
           }
           @Override
           public void onClick(View widget) {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this,HowToCreateAccountActivity.class));
//                LoginActivity.this.startActivity(new Intent(LoginActivity.this, InputStoreInfoActivity.class));
           }
        },8,19,Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ssp.setSpan(new ForegroundColorSpan(Color.rgb(51, 102, 255)),8,19,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ForegroundColorSpan fcs = new ForegroundColorSpan();
//        ssp.setSpan(fcs, 8, 19, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tv_login_regist.setHighlightColor(Color.TRANSPARENT);
        tv_login_regist.setText(ssp);
        tv_login_regist.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /*
     * 检查版本信息
     */
    private void checkVersion() {
        String version = getVersion();
        int versionCode = getVersionCode();
        if ("未知版本".equals(version)) {
            Utils.showToast(this, "版本号有误！");
        } else {
            //使用okhttp
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
                            Utils.showToast(LoginActivity.this, "连接超时！");
                            ll_update.setVisibility(View.GONE);
                            self_image.setVisibility(View.GONE);
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
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            Constant.VERSION_CODE=versionCode;
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
            ll_update.setVisibility(View.GONE);
            ll_rise.setVisibility(View.VISIBLE);
            downLoadApk(vuInfo);
        } else {//直接登陆
            self_image.setVisibility(View.GONE);
            ll_update.setVisibility(View.GONE);
            //用户名和密码不为空，自动登录
            boolean autoLogin = getIntent().getBooleanExtra(Constant.AUTO_LOGIN, true);
            if(autoLogin&&user_id !=null&&!TextUtils.isEmpty(user_id)&&pass_words !=null&& !TextUtils.isEmpty(pass_words)) {
                checkFromNet(user_id,pass_words);
            }
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
                            self_image.setVisibility(View.GONE);
                            installApk();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        LogUtils.Log("异常" + e);
                        Utils.showToast(LoginActivity.this, "连接超时！");
                        self_image.setVisibility(View.GONE);
                        ll_update.setVisibility(View.GONE);
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
        startActivity(intent);
    }

    /**
     * 获取当前版本号
     */
    private String getVersion() {
        currentVersion = "未知版本";
        PackageManager manager = getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
            currentVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            //e.printStackTrace(); //如果找不到对应的应用包信息, 就返回"未知版本"
        }
        return currentVersion;
    }

    private void setListener() {
        btn_login.setOnClickListener(new LoginListener());
    }
    /**
     * 登陆button的监听
     */
    private class LoginListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //1.获取用户输入信息
            user_id = et_login_username.getText().toString().trim();
            Constant.USER_ACCOUNT = user_id;
             pass_words = et_login_password.getText().toString().trim();
            //2.判空
            if (TextUtils.isEmpty(user_id)) {
                Utils.showToast(LoginActivity.this, "请输入您的用户名!");
            } else if (!TextUtils.isEmpty(user_id) && TextUtils.isEmpty(pass_words)) {
                Utils.showToast(LoginActivity.this, "请输入您的密码!");
            } else {
                //1.联网判断是否可以登录
                checkFromNet(user_id, pass_words);
            }
        }
    }

    private void checkFromNet(String userid, String password) {

        //1.改变button状态
        Constant.login=true;
        Constant.PASSWORD=password;
        Constant.USER_ACCOUNT = user_id;
        SpUtils.getInstance(this).save(Constant.PARAMS_NAME_PASSWORDS, pass_words);
        btn_login.setText("登录中...");
        SpUtils.getInstance(this).save(Constant.PARAMS_NAME_PASSWORDS, pass_words);
        ll_user_input.requestFocusFromTouch();
        btn_login.setEnabled(false);
        String currentTime = Utils.getCurrentTime1();
        LogUtils.Log("currenttime:"+currentTime);
        sn = Build.SERIAL;
        LogUtils.Log("sn=="+ sn);
        //获取token
        String token = Utils.getToken(currentTime,this);



        Map<String,String> params=new HashMap<>();
        params.put(Constant.PARAMS_NAME_USERACCOUNT,userid);
        params.put(Constant.PARAMS_NAME_PASSWORDS,MD5.md5(MD5.md5(password)));
        params.put(Constant.PARAMS_NAME_IMEI,sn);
        params.put(Constant.PARAMS_NAME_MODEL,Constant.DEVICE_MODEL);
        params.put(Constant.PARAMS_NAME_TIMEAUTH,currentTime);
        params.put(Constant.PARAMS_NAME_POSTOKEN,token);
        NetWorks netWorks=new NetWorks(this);

        netWorks.Request(UrlConstance.LOG_IN, params,5000, 0,new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                Utils.showToast(LoginActivity.this, "连接超时，请检查网络！");
                LogUtils.Log("错误" + e);
                btn_login.setEnabled(true);
                btn_login.setText("登录");
            }

            @Override
            public void onResonse(String response, int flag) {
                LogUtils.e(TAG,"登录返回值response=="+response);
                parseJson(response);
            }
        });
    }
    /**
     * 处理登录返回的信息
     *
     * @param json
     */
    private void parseJson(String json) {
        try {
            JSONObject jsonObject=new JSONObject(json);
            int error = jsonObject.getInt("code");
            String msg = jsonObject.getString("msg");
            if(error==0) {
                Gson gson=new Gson();
                loginInfo = gson.fromJson(json, LoginInfo.class);
                saveLoginInfo();
            }else {
                Utils.showToast(LoginActivity.this,msg);
            }
        } catch (JSONException e) {
            LogUtils.Log("e=="+e);
            e.printStackTrace();
        }
        btn_login.setEnabled(true);
        btn_login.setText("登录");
    }

    /**
     * 保存登录的返回的一些全局变量
     */
    private void saveLoginInfo() {
        //设置默认打印权限
        String printItems = SpUtils.getInstance(this).getString(Constant.print_permission, "");
        boolean everSetPrinter = SpUtils.getInstance(this).getBoolean(Constant.ever_set_printer, false);
        if(TextUtils.isEmpty(printItems)&&!everSetPrinter) {
            SpUtils.getInstance(this).save(Constant.print_permission, "23457ab");
            SpUtils.getInstance(this).save(Constant.ever_set_printer,true);
        }

        Constant.UNION_PAY_STATUS=0;
        if(loginInfo.getData().getYlstatuscj()==1){
            Constant.UNION_PAY_STATUS = 1;
        }else if(loginInfo.getData().getYlstatusflm()==1){
            Constant.UNION_PAY_STATUS = 2;
        }


        Constant.ACCOUNT_ID=loginInfo.getData().getAccountd();
        Constant.USER_ACCOUNT_KIND=loginInfo.getData().getAccount();
        Constant.ADV_ID =loginInfo.getData().getAdv();
        Constant.STORE_ID=loginInfo.getData().getStore_id();
        Constant.OPEN_ALIPAY=loginInfo.getData().getAlipay();
        Constant.OPEN_WXPAY=loginInfo.getData().getCertifite();
        Constant.OPEN_WXPAY_MICRO=loginInfo.getData().getCertifite_ty();
        Constant.ADV_NAME =loginInfo.getData().getAdvname();
        Constant.OPENED_PERMISSIONS = loginInfo.getData().getAuth();
        Constant.USER_ACCOUNT=user_id;
        Constant.DEVICE_IMEI=sn;
        Constant.IS_ACTIVE = loginInfo.getData().getLogintype();
        LogUtils.e(TAG,"获取的品牌主设备编号："+loginInfo.getData().getDevicenum());
        Constant.DEVICE_ADV_NUM = loginInfo.getData().getDevicenum();
        Constant.STORE_M=loginInfo.getData().getStoreM();
        Constant.STORE_P=loginInfo.getData().getStoreP();
        Constant.STORE_TEL=loginInfo.getData().getStore_tel();
        Constant.LAST_REQUEST_TIME=loginInfo.getData().getReturnsys();
//        SpUtils.getInstance(this).save(Constant.LAST_REQUEST_TIME,loginInfo.getData().getReturnsys());
        SpUtils.getInstance(this).save(Constant.PARAMS_NAME_USERACCOUNT,user_id);
        SpUtils.getInstance(this).save(Constant.PARAMS_NAME_PASSWORDS, pass_words);
        if(!TextUtils.equals(savedUser,user_id)) {
            SpUtils.getInstance(this).save("LAST_TIME_STAMP", null);
        }

            String endad_content = loginInfo.getData().getEndad_content();
        if(endad_content!=null&&!TextUtils.isEmpty(endad_content)) {
            String[] split = endad_content.split("<br/>");
            if(split!=null&&split.length>0) {
                Constant.bottomData=split;
            }
        }

        if(loginInfo.getData().getOut_endDay()>0&&loginInfo.getData().getOut_endDay()<=5) {
            //到期时间
            String dead_time = loginInfo.getData().getOut_endtime();
            String alert="您购买的“外卖多平台自动接单”模块即将在"+dead_time+"过期，为避免影响正常使用，请您及时续费。";
            SpUtils.getInstance(this).save(Constant.ALERT,alert);
        }else if(loginInfo.getData().getOut_endDay()==0) {
            //已经过期
            Constant.wm_is_dead=true;
            String deadAlert="您购买的“外卖多平台自动接单”模块已过期，为避免影响正常使用，请您及时续费。";
            SpUtils.getInstance(this).save(Constant.ALERT,deadAlert);
        }else {
            SpUtils.getInstance(this).save(Constant.ALERT,"");
        }
        LogUtils.Log("保存的token:" + Constant.ACCESS_TOKEN);
        toMainActivity();
    }
    /**
     * 登录成功后的跳转
     */
    private void toMainActivity() {
//        Intent intent=new Intent(this,InputStoreInfoActivity.class);
        Intent intent=new Intent(this,MainActivity.class);
        if("jpush_receiver".equals(start_from)) {
            intent.putExtra("start_from",start_from);
        }
        intent.putExtra("action",loginInfo.getData().getXcx_isuse());
        startActivity(intent);
        finish();
    }

    /**
     * 首次进入定位的监听
     */
    class MyTencentLocationListener implements TencentLocationListener {
        @Override
        public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
            //移除定位监听
            tencentLocationManager.removeUpdates(this);
            latitude = tencentLocation.getLatitude();
            longitude = tencentLocation.getLongitude();
        }

        @Override
        public void onStatusUpdate(String s, int i, String s1) {

        }
    }

    /**
     * 屏蔽back键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode==KeyEvent.KEYCODE_BACK;
    }
}
