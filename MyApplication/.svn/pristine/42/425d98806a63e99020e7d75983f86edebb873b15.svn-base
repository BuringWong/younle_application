package com.younle.younle624.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.utils.Utils;

public class HowToCreateAccountActivity extends Activity implements View.OnClickListener {
    private ImageView iv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_create_account);
        Utils.initToolBarState(this);
        initView();
    }

    private void initView() {
        iv_title = (ImageView)findViewById(R.id.iv_title);
        iv_title.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        toLogin();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            toLogin();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void toLogin() {
        Intent intent=new Intent(this,LoginActivity.class);
        intent.putExtra(Constant.AUTO_LOGIN,false);
        startActivity(intent);
        finish();
    }
}
