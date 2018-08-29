package com.younle.younle624.myapplication.utils.autocallback;

import com.google.gson.Gson;
import com.younle.younle624.myapplication.domain.VersionUpdateInfo;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * Created by 我是奋斗 on 2016/5/30.
 * 微信/e-mail:tt090423@126.com
 */
public  abstract class VersionUpdateInfoCallBack extends Callback<VersionUpdateInfo> {

    @Override
    public VersionUpdateInfo parseNetworkResponse(Response response) throws Exception {

        String result = response.body().toString();
        Gson gson=new Gson();
        VersionUpdateInfo vuInfo = gson.fromJson(result, VersionUpdateInfo.class);
        return vuInfo;
    }
}
