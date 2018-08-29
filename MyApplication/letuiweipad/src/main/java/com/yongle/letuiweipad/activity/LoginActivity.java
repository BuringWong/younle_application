package com.yongle.letuiweipad.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.sunmi.scalelibrary.ScaleManager;
import com.yongle.letuiweipad.AboutAccountActivity;
import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.constant.UrlConstance;
import com.yongle.letuiweipad.domain.LoginInfo;
import com.yongle.letuiweipad.domain.VersionUpdateInfo;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.NetWorks;
import com.yongle.letuiweipad.utils.SpUtils;
import com.yongle.letuiweipad.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.MD5;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";
    @BindView(R.id.et_login_username) EditText et_login_username;
    @BindView(R.id.et_login_password) EditText et_login_password;
    @BindView(R.id.ll_user_input) RelativeLayout ll_user_input;
    @BindView(R.id.btn_login) Button btn_login;

    private String currentVersion;
    private SpannableString ssp;
    private LoginInfo loginInfo;
    private String pass_words;
    private String user_id;
    private String imei;
    private String sn;
    private String start_from;
    private Unbinder bind;
    private ScaleManager scaleManager;
    private String name="测试this";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Utils.initLocalPrinter(this);
        bind = ButterKnife.bind(this);
        Utils.initToolBarState(this);
        start_from = getIntent().getStringExtra("start_from");
        getSpData();
        //3.检查版本信息
        checkVersion();
    }
    /**
     * 读取sp存储的信息，登录信息
     */
    private void getSpData() {
        user_id = SpUtils.getInstance(this).getString(Constant.PARAMS_NAME_USERACCOUNT, "");
        pass_words = SpUtils.getInstance(this).getString(Constant.PARAMS_NAME_PASSWORDS, "");
        if(user_id !=null&&!TextUtils.isEmpty(user_id)) {
            et_login_username.setText(user_id);
        }
        if(pass_words !=null&& !TextUtils.isEmpty(pass_words)) {
            et_login_password.setText(pass_words);
        }
    }
    /*
     * 检查版本信息
     */
    private void checkVersion() {
        Utils.showWaittingDialog(this,"正在检测版本信息...");
        String version = getVersion();
        int versionCode = getVersionCode();
        if ("未知版本".equals(version)) {
            Utils.showToast(this, "版本号有误！");
        } else {
            NetWorks netWorks=new NetWorks(this);
            netWorks.checkVersion(versionCode, new NetWorks.OnNetCallBack() {
                @Override
                public void onError(Exception e, int flag) {
                    Utils.dismissWaittingDialog();
                    Utils.showToast(LoginActivity.this,"网络异常，请检查网络后重试！");
                }

                @Override
                public void onResonse(String response, int flag) {
                    Utils.dismissWaittingDialog();
                    upDateApp(response);
                }
            });
        }
    }
    /**
     * 根据获取的版本信息进行操作
     */
    private void upDateApp(String response) {
        LogUtils.Log("版本检查response="+response);
        VersionUpdateInfo vuInfo=null;
        try {
            vuInfo= new Gson().fromJson(response,VersionUpdateInfo.class);
        }catch (Exception e){
            LogUtils.e(TAG,"检查版本解析异常");
        }

        if (vuInfo.getMsg().getUrl()!=null&&!"".equals(vuInfo.getMsg().getUrl())) {//版本不同，需要更新
            Utils.installNewVersion(LoginActivity.this,vuInfo.getMsg().getUrl(),vuInfo.getMsg().getUpdateinfo());
        } else {//直接登陆
            //用户名和密码不为空，自动登录
            boolean autoLogin = getIntent().getBooleanExtra(Constant.AUTO_LOGIN, true);
            if(autoLogin&&user_id !=null&&!TextUtils.isEmpty(user_id)&&pass_words !=null&& !TextUtils.isEmpty(pass_words)) {
                checkFromNet(user_id,pass_words);
            }
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

    @OnClick({R.id.btn_login,R.id.how_to_createacc,R.id.create_account})
    void onClick(View view){
        switch (view.getId()) {
            case R.id.btn_login :
                login();
                break;
            case R.id.how_to_createacc:
                showCreateAccDialog();
                /*Intent intent=new Intent(this, AboutAccountActivity.class);
                intent.putExtra("load_what",0);
                startActivity(intent);*/
                break;
            case R.id.create_account:
                Intent intent=new Intent(this, AboutAccountActivity.class);
                intent.putExtra("load_what",1);
                startActivity(intent);
                break;
        }
    }
    String url0="http://www.56.com/u22/v_MTQ1MjUzOTU1.html";
    /**
     *
     */
    private void showCreateAccDialog() {
        View view = View.inflate(this, R.layout.createacc_video_dialog, null);
        WebView webView=ButterKnife.findById(view,R.id.webView);
        ImageView close_video=ButterKnife.findById(view,R.id.close_video);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .show();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        int width = dialog.getWindow().getWindowManager().getDefaultDisplay().getWidth();
        int height = dialog.getWindow().getWindowManager().getDefaultDisplay().getHeight();
        LogUtils.Log("width=="+width);
        LogUtils.Log("height=="+height);
        params.width= (int) (width*0.5);
        params.height= (int) (height*0.7);
//        params.height=Utils.px2dip(mActivity,580);
        dialog.getWindow().setAttributes(params);


        Utils.initWebView(this,webView);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
        close_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        webView.loadUrl(url0);
    }


    private void login() {
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

    private void checkFromNet(String userid, String password) {

        //1.改变button状态
        Constant.login=true;
        Constant.PASSWORD=password;
        Constant.USER_ACCOUNT = user_id;
        btn_login.setText("登录中...");
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
        netWorks.Request(UrlConstance.LOG_IN,false, "正在登录...",params,5000, 0,new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                Utils.showToast(LoginActivity.this, "连接超时，请检查网络！");
                LogUtils.Log("错误" + e);
                btn_login.setEnabled(true);
                btn_login.setText("登录");
            }

            @Override
            public void onResonse(String response, int flag) {
                LogUtils.Log("登录返回："+response);
                //登录权限位置不同，单独解析
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray auth = jsonObject.getJSONObject("data").getJSONArray("auth");
                    if (auth != null && auth.length() > 0) {
                        Constant.OPENED_PERMISSIONS.clear();
                        for (int i = 0; i < auth.length(); i++) {
                            Constant.OPENED_PERMISSIONS.add((String) auth.get(i));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                parseJson(response);

            }
        });
    }
    /**
     * 处理登录返回的信息
     * @param json
     */
    private void parseJson(String json) {
        try {
            JSONObject jsonObject=new JSONObject(json);
            int code = jsonObject.getInt("code");
            String msg = jsonObject.getString("msg");
            if(code==0) {
                Gson gson=new Gson();
                loginInfo = gson.fromJson(json, LoginInfo.class);
                if(!Constant.OPENED_PERMISSIONS.contains("2")) {
                    Utils.showToast(LoginActivity.this,"您还没有开通相应的功能模块，请联系客服开通");
                    btn_login.setEnabled(true);
                    btn_login.setText("登录");
                    return;
                }
                saveLoginInfo();
            }else {
                Utils.showToast(LoginActivity.this,msg);
                btn_login.setEnabled(true);
                btn_login.setText("登录");

            }
        } catch (JSONException e) {
            LogUtils.Log("e=="+e);
            e.printStackTrace();
        }
    }

    /**
     * 保存登录的返回的一些全局变量
     */
    private void saveLoginInfo() {
        String printItems = SpUtils.getInstance(this).getString(Constant.print_permission, "");
        boolean everSetPrinter = SpUtils.getInstance(this).getBoolean(Constant.ever_set_printer, false);
        if(TextUtils.isEmpty(printItems)&&!everSetPrinter) {
            SpUtils.getInstance(this).save(Constant.print_permission, "23457");
            SpUtils.getInstance(this).save(Constant.ever_set_printer,true);
        }
        //微信支付宝
        Constant.OPEN_ALIPAY=loginInfo.getData().getAlipay();
        Constant.OPEN_WXPAY=loginInfo.getData().getCertifite();
        Constant.OPEN_WXPAY_MICRO=loginInfo.getData().getCertifite_ty();
        //银联
        int ylstatuscj = loginInfo.getData().getYlstatuscj();
        int ylstatusflm = loginInfo.getData().getYlstatusflm();
        int ylstatusxdl = loginInfo.getData().getYlstatusxdl();
        if(ylstatuscj==1||ylstatusflm==1||ylstatusxdl==1) {
            Constant.OPEN_YL=1;
        }else {
            Constant.OPEN_YL=0;
        }
        Constant.ACCOUNT_ID=loginInfo.getData().getAccountd();
        Constant.USER_ACCOUNT_KIND=loginInfo.getData().getAccount();
        Constant.ADV_ID =loginInfo.getData().getAdv();
        Constant.STORE_ID=loginInfo.getData().getStore_id();

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

    @Override
    protected void onDestroy() {
        bind.unbind();
        super.onDestroy();
    }
}
