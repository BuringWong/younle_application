package com.younle.younle624.myapplication.activity.regist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.LoginActivity;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

public class RegistActivity extends Activity implements View.OnClickListener {
    private TextView tv_title;
    private   int AREDY_SEND = 1;
    /**
     * 立即注册
     */
    private Button btn_to_regist;
    /**
     * 获取验证码
     */
    private Button btn_regist_getcode;
    /**
     *
     */
    private EditText et_regist_mobilenum;
    private EditText et_regist_smscode;
    private EditText et_regist_companyname;
    private EditText et_regist_password;
    /**
     * 是否请求过验证码
     */
    private boolean everReqCode=false;

    private Handler handler = new Handler(){

        private int leftTime=60;

        public void handleMessage(Message msg){
            if(msg.what==AREDY_SEND) {
                if(leftTime>0) {
                    btn_regist_getcode.setText("已发送,"+ leftTime +"s后重新获取");
                    leftTime--;
                    handler.sendEmptyMessageDelayed(AREDY_SEND, 1000);
                }else {
                    leftTime=60;
                    handler.removeCallbacksAndMessages(AREDY_SEND);
                    btn_regist_getcode.setText("重新获取验证码");
                    btn_regist_getcode.setEnabled(true);
                }
            }
        }
    };
    private String phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        initView();
        setListener();
    }

    private void setListener() {
        btn_to_regist.setOnClickListener(this);
        btn_regist_getcode.setOnClickListener(this);
    }

    private void initView() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("注册乐推微");
        btn_to_regist = (Button)findViewById(R.id.btn_to_regist);
        btn_regist_getcode = (Button)findViewById(R.id.btn_regist_getcode);

        et_regist_password = (EditText)findViewById(R.id.et_regist_password);
        et_regist_companyname = (EditText)findViewById(R.id.et_regist_companyname);
        et_regist_smscode = (EditText)findViewById(R.id.et_regist_smscode);
        et_regist_mobilenum = (EditText)findViewById(R.id.et_regist_mobilenum);
    }

    /**
     * 发送注册请求
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_to_regist :

                btn_to_regist.setEnabled(false);
                 confingInputInfo();
/*                if(isInfoRight) {
                    showRegistSuccessDia();
//                    startActivity(new Intent(this,RegistSuccessActivity.class));
                }*/
                break;
            case R.id.btn_regist_getcode:
                //判读是否输入手机号，手机号格式是否正确，手机号是否注册了品牌主
                btn_regist_getcode.setText("正在发送...");
                btn_regist_getcode.setEnabled(false);
                everReqCode=true;
                configPhone();
                break;
        }
    }

    /**
     * 展示注册成功的dialog
     */
    private void showRegistSuccessDia() {
        View dialogView = View.inflate(this, R.layout.regist_success_dia, null);
       Button btn_regist_success = (Button) dialogView.findViewById(R.id.btn_regist_success);
        btn_regist_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistActivity.this,LoginActivity.class));
            }
        });
        AlertDialog registSuccessDia = new AlertDialog.Builder(this)
                .setView(dialogView)
                .show();
        registSuccessDia.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return true;
            }
        });
        registSuccessDia.setCancelable(false);
    }

    /**
     * 提交注册信息时判断输入信息的各种情况
     * @return
     */
    private void confingInputInfo() {
        phoneNum = et_regist_mobilenum.getText().toString().trim();//手机号信息
        String smsCode = et_regist_smscode.getText().toString().trim();//验证码信息

        String companyName = et_regist_companyname.getText().toString().trim();//公司信息
        String password = et_regist_password.getText().toString();//用户输入的密码

        if (TextUtils.isEmpty(phoneNum)) {
            Utils.showToast(this, "请输入您的手机号");
            btn_to_regist.setEnabled(true);
        } else if (!Utils.isPhone(phoneNum)) {
            Utils.showToast(this, "手机号格式不正确！");
            btn_to_regist.setEnabled(true);
        } else if (TextUtils.isEmpty(smsCode)) {
            Utils.showToast(this, "请输入验证码！");
            btn_to_regist.setEnabled(true);
        } else if (Utils.isCompanyName(companyName)) {
            Utils.showToast(this, "公司名称中不能包含除 , . 空格 以外的其他标点！");
            btn_to_regist.setEnabled(true);
        } else if (TextUtils.isEmpty(companyName)) {
            Utils.showToast(this, "请填写您的公司名称！");
            btn_to_regist.setEnabled(true);
        } else if (TextUtils.isEmpty(password)) {
            Utils.showToast(this, "请填写您的登录密码！");
            btn_to_regist.setEnabled(true);
        } else if (password.contains(" ") || TextUtils.isEmpty(password)) {
            Utils.showToast(this, "密码中不能包含空格，请重新输入!");
            btn_to_regist.setEnabled(true);
            et_regist_password.setText("");
        }else {
            registInsert(smsCode,companyName,password);
        }
    }

    /**
     * 判断输入的验证码与服务器返回的是否相同，不同提示验证码错误,反之注册
     * @param smsCode
     * @param companyName
     * @param password
     */
    private void registInsert(String smsCode, String companyName, String password) {
        btn_to_regist.setText("正在注册...");
        OkHttpUtils.post()
                .url(UrlConstance.REGISTER_INSERT)
                .addParams("uname",phoneNum)
                .addParams("verify",smsCode)
                .addParams("company",companyName)
                .addParams("pwd",password)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        netError();
                        btn_to_regist.setText("立即注册");
                        btn_to_regist.setEnabled(true);
                    }

                    @Override
                    public void onResponse(String response) {
                        btn_to_regist.setText("立即注册");
                        LogUtils.Log("response:"+response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int error = jsonObject.getInt("error");
                            String msg = jsonObject.getString("msg");
                            if(error==0) {
                                showRegistSuccessDia();
                            }else {
                                Toast.makeText(RegistActivity.this, msg, Toast.LENGTH_SHORT).show();
                                btn_to_regist.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void configPhone() {
        phoneNum = et_regist_mobilenum.getText().toString().trim();
        if(TextUtils.isEmpty(phoneNum)) {//未输入手机号
            Utils.showToast(this, "请输入您的手机号！");
            btn_regist_getcode.setText("获取验证码");
            btn_regist_getcode.setEnabled(true);
        }else if(!Utils.isPhone(phoneNum)) {//手机号格式不正确
            btn_regist_getcode.setEnabled(true);
            btn_regist_getcode.setText("获取验证码");
            Utils.showToast(this,"手机号格式不正确！");
        }else  {//发请求判断是否为注册品牌住手机号，如果是，提示手机号已注册品牌主可找回密码，请求在获取验证码时发送
            canUse();
        }
    }

    /**
     * 判断手机号是否已注册
     * @return
     */
    private void canUse() {
        OkHttpUtils.post()
                .url(UrlConstance.SAME_PHONE)
                .addParams(Constant.POS_UNAME,phoneNum)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        btn_regist_getcode.setEnabled(true);
                        btn_regist_getcode.setText("获取验证码");
                        netError();
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.Log("验证:"+response);
                        try {
                            JSONObject  jsonObject=new JSONObject(response);
                            int error = jsonObject.getInt("error");
                            String msg = jsonObject.getString("msg");
                            switch (error) {
                                case 0 ://正常，可发起注册请求
                                    getMessageCode();
                                    break;
                                case 10001:
                                    btn_regist_getcode.setText("获取验证码");
                                    btn_regist_getcode.setEnabled(true);
                                    Toast.makeText(RegistActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 获取验证码
     */
    private void getMessageCode() {
        OkHttpUtils.post()
                .url(UrlConstance.GET_MESSAGE)
                .addParams(Constant.TELNUMBER,phoneNum)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        btn_regist_getcode.setEnabled(true);
                        btn_regist_getcode.setText("获取验证码");
                        netError();
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.Log("获取验证码:"+response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int error = jsonObject.getInt("error");
                            String msg = jsonObject.getString("msg");
                            switch (error) {
                                case 0 ://验证码发送成功
                                    changeButtonState();
                                    break;
                                case 10002:
                                case 10001:
                                    btn_regist_getcode.setText("获取验证码");
                                    btn_regist_getcode.setEnabled(true);
                                    Toast.makeText(RegistActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    private void netError() {
        Toast.makeText(RegistActivity.this, "网络异常,请检查网络后重试", Toast.LENGTH_SHORT).show();
    }

    /**
     * 改变button状态模式
     */
    private void changeButtonState() {
        btn_regist_getcode.setEnabled(false);
        handler.sendEmptyMessageDelayed(AREDY_SEND,0);
    }
    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
