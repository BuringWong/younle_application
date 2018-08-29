package com.younle.younle624.myapplication.utils;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by 我是奋斗 on 2016/5/27.
 * 微信/e-mail:tt090423@126.com
 */
public abstract class UserCallback<T> extends Callback<T>
{
    private Class<T> aClass;

    public UserCallback(Class<T> aClass) {
        this.aClass = aClass;
    }

    //非UI线程，支持任何耗时操作
    @Override
    public T parseNetworkResponse(Response response) throws IOException
    {

        String string = response.body().string();
         T t = new Gson().fromJson(string, aClass);
        return t;
    }
}
