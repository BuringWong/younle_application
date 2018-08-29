package com.younle.younle624.myapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


/**
 * Created by 我是奋斗 on 2016/4/10.
 *
 * 联网请求的工具类
 */
public  class NetUtils <T>{

    private Class aClass;
    private String url;
    private T t;


    public  NetUtils(Context context,Class aClass, String url) {
        boolean available = NetworkUtils.isAvailable(context);
        if(!available) {
            String netMessage="当前网络又问题，请检查网络！";
            EventBus.getDefault().post(netMessage);
            return;
        }

        this.aClass = aClass;
        this.url = url;

        //开始联网
        RequestParams entity = new RequestParams(url);
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析Json
                LogUtils.Log("onSuccess");
                parseChangeJson(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.Log("onError"+ex);

            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.Log("onCancelled");

            }

            @Override
            public void onFinished() {
                LogUtils.Log("onFinished");

            }
        });
    }


    private void parseChangeJson(String result) {
         t = (T) new Gson().fromJson(result, aClass);
        //转换成功发送通知
        EventBus.getDefault().post(t);

    }

}
