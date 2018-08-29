package com.younle.younle624.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.domain.waimai.ScreenOnBean;
import com.younle.younle624.myapplication.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class KeepAliveActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        LogUtils.Log("KeepAliveActivity   onCreate()");
        setContentView(R.layout.activity_keep_alive);
        Window window = getWindow();
        window.setGravity(Gravity.LEFT|Gravity.TOP);
        WindowManager.LayoutParams windowLayoutParams = window.getAttributes(); // 获取对话框当前的参数值
        windowLayoutParams.x=1;
        windowLayoutParams.y=1;
        windowLayoutParams.width = 1; // 宽度设置为屏幕的0.95
        windowLayoutParams.height = 1; // 高度设置为屏幕的0.6
        windowLayoutParams.alpha = 0f;// 设置透明度
    }
    @Subscribe
    public void onEventMainThread(ScreenOnBean event) {
        LogUtils.Log("KeepAliveActivity   收到消息");
        finish();
        EventBus.getDefault().cancelEventDelivery(event);
    }

    @Override
    protected void onDestroy() {
        LogUtils.Log("KeepAliveActivity   onDestory()");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
