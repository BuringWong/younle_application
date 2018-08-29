package com.younle.younle624.myapplication.activity.regist.resetpassword;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.utils.Utils;

/**
 * 找回密码的界面
 */
public class FindPassWordActivity extends Activity implements View.OnClickListener {
    private static final int AREDY_SEND = 1;
    private EditText et_findpassword_mobilenum;
    private EditText et_findpassword_smscode;
    private Button btn_findpassword_getcode;
    private Button btn_findpassword_next;
    private boolean everReqCode=false;
    private Handler handler = new Handler(){

        private int leftTime=10;

        public void handleMessage(Message msg){
            if(msg.what==1) {
                if(leftTime>0) {

                    btn_findpassword_getcode.setText("已发送,"+ leftTime +"s后重新获取");
                    leftTime--;
                    handler.sendEmptyMessageDelayed(AREDY_SEND, 1000);
                }else {
                    leftTime=10;
                    handler.removeCallbacksAndMessages(AREDY_SEND);
                    btn_findpassword_getcode.setText("获取验证码");
                    btn_findpassword_getcode.setClickable(true);
                }

            }
        }
    };
    private String phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pass_word);
        
        initView();
        setListener();
    }

    private void initView() {
        et_findpassword_mobilenum = (EditText)findViewById(R.id.et_findpassword_mobilenum);
        et_findpassword_smscode = (EditText)findViewById(R.id.et_findpassword_smscode);
        btn_findpassword_getcode = (Button)findViewById(R.id.btn_findpassword_getcode);
        btn_findpassword_next = (Button)findViewById(R.id.btn_findpassword_next);
    }
    
    private void setListener() {
        et_findpassword_mobilenum.setOnClickListener(this);
        et_findpassword_smscode.setOnClickListener(this);
        btn_findpassword_getcode.setOnClickListener(this);
        btn_findpassword_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_findpassword_getcode://获取验证码
                //1.判读是否输入手机号，手机号格式是否正确，手机号是否注册了品牌主
                everReqCode=true;
                boolean isRight= configPhone();
                if(isRight) {
                    changeButtonState();
                }
                break;
            case R.id.btn_findpassword_next:
                //2.判断验证码是否正确，是否有输入验证码,是否有输入手机号
                boolean isSmsRight=configSms();
                if(isSmsRight) {
                    Intent intent=new Intent(this,ResetPassWordActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    private boolean configSms() {
        String configCode = et_findpassword_smscode.getText().toString().trim();
        if(!everReqCode) {
            Utils.showToast(this, "请先获取验证码！");
            return false;
        }

        if(TextUtils.isEmpty(configCode)) {//未输入验证码
            Utils.showToast(FindPassWordActivity.this, "请输入验证码！");
            return false;
        }/*else if() {//对比输入的验证码和服务器返回的验证码是否相同，不同的话，提示验证码错误

        }*/
        return true;
    }

    private boolean configPhone() {
        phoneNum = et_findpassword_mobilenum.getText().toString().trim();
        if(TextUtils.isEmpty(phoneNum)) {//未输入手机号
            Utils.showToast(FindPassWordActivity.this, "请输入您的手机号！");
            return false;
        }else if(!Utils.isPhone(phoneNum)) {//手机号格式不正确
            Utils.showToast(FindPassWordActivity.this,"手机号格式不正确！");
            return false;
        }
        /*else if() {//发请求判断是否为注册品牌住手机号，如果不是，提示手机号尚未注册品牌主返回false，请求在获取验证码时发送

        }*/
        return true;
    }

    /**
     * 改变button状态模式
     */
    private void changeButtonState() {
        btn_findpassword_getcode.setClickable(false);
        handler.sendEmptyMessageDelayed(AREDY_SEND,0);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
