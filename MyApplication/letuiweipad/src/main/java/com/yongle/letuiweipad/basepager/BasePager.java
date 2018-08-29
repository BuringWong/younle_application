package com.yongle.letuiweipad.basepager;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yongle.letuiweipad.utils.LogUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 作者：Create by 我是奋斗 on2017/2/24 15:15
 * 邮箱：tt090423@126.com
 * 微信：17346512596
 */
public abstract class BasePager extends Fragment{
    //用于传递给子类，以便加载试图
    public FragmentActivity mActivity;
    public View rootView;
    public boolean isInit;
    public Unbinder bind;
    public int position=0;

    public BasePager() {
    }
    @Override
    public void onAttach(Context context) {
        LogUtils.e("TAG","onAttach() context=="+context.getClass().getSimpleName());
        super.onAttach(context);
        mActivity= (FragmentActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtils.e("temp","basePager  onCreateView()");
        if(rootView==null) {
            rootView=initView();
        }else {
            if(rootView.getParent()!=null) {
                ((ViewGroup)rootView.getParent()).removeView(rootView);
            }
        }
        bind = ButterKnife.bind(this, rootView);
        isInit=true;
        LogUtils.e("temp","basepager position="+position);
        if(position!=2) {
            initData(position);
        }
        return rootView;
    }

    public abstract View initView();

    public void initData(int position){
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        LogUtils.Log("onConfigurationChanged()");
        super.onConfigurationChanged(newConfig);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }
}
