package com.younle.younle624.myapplication.activity.regist.resetpassword;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.utils.Utils;

public class ResetPassWordActivity extends Activity {
    private EditText et_password_new;
    private EditText et_password_repeat;
    private Button btn_reset_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initView();
    }

    private void initView() {
        et_password_repeat = (EditText)findViewById(R.id.et_password_repeat);
        et_password_new = (EditText)findViewById(R.id.et_password_new);
        btn_reset_next = (Button)findViewById(R.id.btn_reset_next);

        btn_reset_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.判断是否正确输入两次密码，两次输入的密码是否相同
                boolean isSame = judgeInput();
                if(isSame) {
                    //2.将密码加密保存到本地sp


                    //3.发请求，将新密码保存到服务器
                    startActivity(new Intent(ResetPassWordActivity.this, FinishResetPassWord.class));
                }

            }


        });
    }

    /**
     * 判断两次输入是否相同
     * @return
     */
    private boolean judgeInput() {
        String firstInput = et_password_new.getText().toString();
        String secondInput = et_password_repeat.getText().toString();
        if(TextUtils.isEmpty(firstInput)&&TextUtils.isEmpty(secondInput)) {
            Utils.showToast(this, "请您填写新密码!");
            return false;
        }else if(firstInput.contains(" ")) {
            Utils.showToast(this, "密码中不能包含空格，请重新输入！");
            et_password_new.setText("");
            return false;
        }else if(TextUtils.isEmpty(firstInput)&&!TextUtils.isEmpty(secondInput)) {
            Utils.showToast(this, "两次输入的密码不一致！");
            return false;
        }else if(!TextUtils.isEmpty(firstInput)&&TextUtils.isEmpty(secondInput)) {
            Utils.showToast(this, "请再次输入密码！");
            return false;
        }else if(!firstInput.equals(secondInput)) {
            Utils.showToast(this,"两次输入的密码不一致！");
            return false;
        }else {
            return true;
        }
    }
}
