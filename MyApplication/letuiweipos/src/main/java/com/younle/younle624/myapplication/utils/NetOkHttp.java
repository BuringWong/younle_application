package com.younle.younle624.myapplication.utils;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

/**
 * Created by 我是奋斗 on 2016/5/27.
 * 微信/e-mail:tt090423@126.com
 */
public  class NetOkHttp<T> {




    public static void OkPost(Callback callback, String url, String version, String token){
        OkHttpUtils
                .post()
                .url(url)
        .addParams("version", version)
        .addParams("token", token)
        .build()
        .execute(callback);/* {
            @Override
            public Object parseNetworkResponse(Response response) throws Exception {
                LogUtils.Log("praseNetworkResponse"+response.toString());

                return null;
            }

            @Override
            public void onError(Call call, Exception e) {
                LogUtils.Log("onError"+e);
            }

            @Override
            public void onResponse(Object response) {
                LogUtils.Log("onResponse");
            }
        });*/


    }

}
