package com.younle.younle624.myapplication.activity.regist.bindstore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.domain.KindsChoose;
import com.younle.younle624.myapplication.utils.Utils;

/**
 * 用户输入店铺信息的界面
 */
public class InputStoreInfoActivity extends Activity implements View.OnClickListener {
    private Button btn_setting_next;
    private TextView tv_title;
    private EditText tv_setting_chose;
    private EditText et_setting_band;
    private EditText et_setting_branch;
    private EditText et_setting_phonenum;
    private EditText et_setting_add;
    private KindsChoose kindsChoose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_store_info);
        selfTitle();
        initView();
        setListener();
    }

    private void selfTitle() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("新增门店");
    }

    private void setListener() {
        btn_setting_next.setOnClickListener(this);
        tv_setting_chose.setOnClickListener(this);
        tv_setting_chose.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    Intent intent = new Intent(InputStoreInfoActivity.this,
                            CategoryChooseActivity.class);
                    // 启动对应的Activity，并等待返回的结果，其中0为请求码(requestCode)，用于标识该请求。
                    startActivityForResult(intent, 0);
                }
            }
        });
    }

    private void initView() {
        btn_setting_next = (Button)findViewById(R.id.btn_setting_next);

        tv_setting_chose = (EditText)findViewById(R.id.tv_setting_chose);
        et_setting_add = (EditText)findViewById(R.id.et_setting_add);
        et_setting_phonenum = (EditText)findViewById(R.id.et_setting_phonenum);
        et_setting_branch = (EditText)findViewById(R.id.et_setting_branch);
        et_setting_band = (EditText)findViewById(R.id.et_setting_band);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_setting_next :
                //判断输入是否正确，跳转到绑定成功界面
                boolean isOk=judgeInput();
                if(isOk) {
//                    startActivity(new Intent(this,MarkLocationActivity.class));
                    startActivity(new Intent(this,LocationMarkActivity.class));
                }

                break;
            case R.id.tv_setting_chose:
                //类目选择,edit失去焦点
//                editLoseFocus();
//                Intent intent = new Intent(this,
//                        CategoryChooseActivity.class);
//                // 启动对应的Activity，并等待返回的结果，其中0为请求码(requestCode)，用于标识该请求。
//                startActivityForResult(intent, 0);
                break;
        }

    }



    /**
     * 类目选择完成后的回掉
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        et_setting_add.requestFocus();
        if (requestCode == 0 && resultCode == 0) {
            if(data!=null) {
                Bundle bundleData = data.getExtras();
                kindsChoose = (KindsChoose) bundleData.getSerializable("kinds");
                if(kindsChoose!=null) {
                    tv_setting_chose.setText(kindsChoose.getFirstKinds()+"-"+kindsChoose.getSecondKinds()
                            +"-"+kindsChoose.getThirdKinds());
                }
            }

        }

    }

    /**
     * 判断输入信息是否正确
     * 1.是否全部有输入
     * 2.店铺电话格式是否正确
     * 3.类目是否有选择
     * @return
     */
    private boolean judgeInput() {
        String kinds = tv_setting_chose.getText().toString();
        String bind = et_setting_band.getText().toString();
        String branch = et_setting_branch.getText().toString();
        String phone = et_setting_phonenum.getText().toString();
        String address = et_setting_add.getText().toString();
        if(TextUtils.isEmpty(kinds)||TextUtils.isEmpty(bind)||TextUtils.isEmpty(branch)||TextUtils.isEmpty(phone)||
        TextUtils.isEmpty(address)) {
            Utils.showToast(this, "信息输入不完整，请补全！");
        }else if(!Utils.isPhoneNumberValid(phone)) {//输入电话号码是否正确，不是返回false
            Utils.showToast(this,"请输入正确格式的电话号码！");
        }else {
            return true;
        }

        return false;
    }
}
