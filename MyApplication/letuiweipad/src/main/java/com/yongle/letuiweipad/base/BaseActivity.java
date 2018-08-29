package com.yongle.letuiweipad.base;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.utils.Utils;


/**
 * 作者：Create by 我是奋斗 on2017/4/8 13:34
 * 邮箱：tt090423@126.com
 * 微信：17346512596
 */
public  class BaseActivity extends FragmentActivity {
    public final String TAG=getClass().getSimpleName();
    public ImageView iv_title;
    public TextView tv_cancel;
    public TextView tv_title;
    public TextView tv_title_right;
    public TextView tv_left;
    @Nullable
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        Utils.initToolBarState(this);
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onStart() {
//        LogUtils.e(TAG,getClass()+"---onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
//        LogUtils.e(TAG,getClass()+"---onResume()");
        super.onResume();
    }
    @Override
    protected void onStop() {
        super.onStop();
//        LogUtils.e(TAG, getClass() + "---onStop()");
    }

    @Override
    protected void onPause() {
        super.onPause();
//        LogUtils.e(TAG, getClass() + "---onPause()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        LogUtils.e(TAG, getClass() + "---onRestart()");
    }

    @Override
    protected void onDestroy() {
//        LogUtils.e(TAG, getClass() + "---onDestroy()");
        super.onDestroy();
    }

    /** 收起键盘 */
    public void closeInputMethod() {
        // 收起键盘
        View view = getWindow().peekDecorView();// 用于判断虚拟软键盘是否是显示的
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
