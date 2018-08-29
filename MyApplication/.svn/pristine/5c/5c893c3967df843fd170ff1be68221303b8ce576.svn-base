package com.younle.younle624.myapplication.activity.regist.bindstore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.MainActivity;

/**
 * 用户绑定门店成功的界面
 */
public class BindStoreSuccessActivity extends AppCompatActivity {
    private TextView tv_title;
    private Button btn_usepos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_store_success);
        initView();

    }

    private void initView() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("新增门店");
        btn_usepos = (Button)findViewById(R.id.btn_usepos);

        btn_usepos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BindStoreSuccessActivity.this,MainActivity.class));
            }
        });
    }
}
