package com.younle.younle624.myapplication.activity.regist.resetpassword;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.LoginActivity;

public class FinishResetPassWord extends Activity {
    private Button btn_to_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finishreset_password);
        btn_to_login = (Button)findViewById(R.id.btn_to_login);

        btn_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FinishResetPassWord.this,LoginActivity.class));
            }
        });

    }
}
