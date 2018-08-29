package com.younle.younle624.myapplication.activity.manager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.File;

import okhttp3.Call;

public class DeviceInfoActivity extends Activity implements View.OnClickListener {
    private TextView imei;
    private TextView userid;
    private TextView store;
    private TextView version;
    private Button version_check;
    private static final int FINFSH_CHECK = 1;
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case FINFSH_CHECK :
                    alertDialog.dismiss();
                    Utils.showToast(DeviceInfoActivity.this,"当前版本已是最新！");
                    break;
            }
        }
    };
    private AlertDialog alertDialog;
    private TextView tv_title;
    private ImageView iv_title;
    private String TAG="DeviceInfoActivity";
    private TextView tv_mark_reback;
    private AlertDialog upLoadDia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        EventBus.getDefault().register(this);
        //Utils.initToolBarColor(this, "#3f88ce");
        Utils.initToolBarState(this);
        initView();
        setListener();
        initData();

    }
    /**
     * 扫描结束后的回调
     * @param msg
     */
    @Subscribe
    public void onEventMainThread(String msg) {
        LogUtils.e(TAG, "msg==" + msg);
        //FIXME：这个可以保证只调用一次。
        OkHttpUtils.post()
                .url(UrlConstance.SWITCH_PRINT_STATUS)
                .addParams("json", msg)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        LogUtils.e(TAG,e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e(TAG,"response=="+response.toString());
                    }
                });
        EventBus.getDefault().cancelEventDelivery(msg);
    }

    private void setListener() {
        version_check.setOnClickListener(this);
        tv_mark_reback.setOnClickListener(this);
    }

    private void initView() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("关于本机");
        iv_title = (ImageView)findViewById(R.id.iv_title);
        iv_title.setVisibility(View.VISIBLE);
        imei = (TextView)findViewById(R.id.imei);
        userid = (TextView) findViewById(R.id.userid);
        store = (TextView)findViewById(R.id.store);
        version = (TextView)findViewById(R.id.version);
        version_check = (Button)findViewById(R.id.version_check);
        iv_title.setOnClickListener(this);
        version_check.setOnClickListener(this);
        tv_mark_reback = (TextView)findViewById(R.id.tv_mark_reback);
        tv_mark_reback.setText("上传日志");
        tv_mark_reback.setVisibility(View.VISIBLE);
    }

    private void initData() {
        userid.setText("绑定的品牌主账号： "+Constant.ADV_NAME);
        switch (Constant.USER_ACCOUNT_KIND){
            case 1:
                store.setText("当前使用的门店派账号： "+Constant.USER_ACCOUNT);
                break;
            case 2:
                store.setText("当前使用的门店派账号： "+Constant.USER_ACCOUNT);
                break;
        }

        int checkSelfPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if(checkSelfPermission!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},0x88);
        }
       String currentVersion = "未知版本";
        PackageManager manager = getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
            currentVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            //e.printStackTrace(); //如果找不到对应的应用包信息, 就返回"未知版本"
        }
        version.setText("当前软件版本：" + currentVersion+"."+Constant.VERSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==0x88) {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String deviceId = tm.getDeviceId();
            imei.setText("当前设备IMEI： "+deviceId);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title :
                finish();
                break;
            case R.id.version_check:
                testPay();
                break;
            case R.id.tv_mark_reback:
                upDateLog();

                break;
        }
    }

    private void upDateLog()  {
        LogUtils.Log("开始上传");
        showUpLoadDia();
        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),Utils.getToday()+"wmLog.txt");
        OkHttpUtils.post()
                .addFile("log","wmLog.txt",file)
                .addParams(Constant.PARAMS_NEME_ACCOUNT_ID,Constant.ACCOUNT_ID)
                .url(UrlConstance.UPDATE_LOG)
                .build()
                .connTimeOut(60000)
                .readTimeOut(60000)
                .writeTimeOut(60000)
                .execute(new StringCallback() {
                    @Override
                    public void inProgress(float progress) {
                        super.inProgress(progress);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        LogUtils.Log("error=="+e.toString());
                        Utils.showToast(DeviceInfoActivity.this,"日志上传失败，请重试!",1000);
                        dismissDia();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("TAG","undata response=="+response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int code = jsonObject.getInt("code");
                            if(code==200) {
                                Utils.showToast(DeviceInfoActivity.this,"成功上传日志!",1000);
                                dismissDia();
                            }else {
                                Utils.showToast(DeviceInfoActivity.this,"日志上传失败，请重试!",1000);
                                dismissDia();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });

    }

    private void dismissDia() {
        if(upLoadDia!=null&&upLoadDia.isShowing()) {
            upLoadDia.dismiss();
        }
    }

    private void showUpLoadDia() {
        upLoadDia = Utils.wybDialog(DeviceInfoActivity.this, true, R.layout.uploading_dia, 0,
                Utils.dip2px(DeviceInfoActivity.this, 200), Utils.dip2px(DeviceInfoActivity.this, 124), "日志上传中");
    }


    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 测试
     */
    public void testPay() {
   /*     JSONArray printTest = new JSONArray();
        try {
           *//* com.pos.sdk.printer.PosPrinter.Parameters params = ServiceManager.getInstence().getPrinter().getParameters();
            params.setFontSize(30);
            //靠左
            params.setPrintAlign(0);
            //居中
            params.setPrintAlign(1);
            //靠右
            params.setPrintAlign(2);
            //加粗斜体
            params.setFontEffet(3);
            //加粗
            params.setFontEffet(1);
            //斜体
            params.setFontEffet(2);
            //正常
            params.setFontEffet(0);
            //设置打印机参数
            ServiceManager.getInstence().getPrinter().setParameters(params);*//*
            ServiceManager.getInstence().getPrinter().addText("以后AAAAAA");
            ServiceManager.getInstence().getPrinter().beginPrint(printer_callback);

        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
    @Override
    protected void onActivityResult(int requestCode
            , int resultCode, Intent data) {
        switch(resultCode) {
            case Activity.RESULT_CANCELED:
                String reason = data.getStringExtra("reason");
                LogUtils.Log("reason=="+reason);
                if(reason != null) {
                    Toast.makeText(this, reason,Toast.LENGTH_SHORT).show();
                }
                break;
            case Activity.RESULT_OK:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
