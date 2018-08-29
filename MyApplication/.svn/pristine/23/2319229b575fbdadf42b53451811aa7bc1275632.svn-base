package com.younle.younle624.myapplication.basepager;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;

/**
 * Created by 我是奋斗 on 2016/5/9.
 * 微信/e-mail:tt090423@126.com
 *
 * 架构的基类
 */
public abstract class BasePager extends Fragment {
    //用于传递给子类，以便加载试图
    public final Activity mActivity;
    public View rootView;
    public boolean isInit;

    public BasePager(Activity activity) {
        this.mActivity = activity;
        rootView = initView();
    }

    /**
     * 具体如何加载视图，交给子类自己做
     * @return
     */
    public abstract View initView();

    /**
     * 具体要初始化那些数据，由子类自己决定
     */
    public void initData(){

    }
    /**
     * 子类可以调用，移除消息，解注册等功能
     */
    @Override
    public void onDestroy() {
    }

    public void Permissions(){

    }
    public void showMessageArrive(){

    }

}
