package com.younle.younle624.myapplication.utils.printmanager;

import android.os.RemoteException;

import woyou.aidlservice.jiuiv5.ICallback;

/**
 * 作者：Create by 我是奋斗 on2016/12/20 14:43
 * 邮箱：tt090423@126.com
 * 微信：17346512596
 */
public class YlPrintCallBack extends ICallback.Stub {
    PrintCallBack printCallBack;

    public YlPrintCallBack(PrintCallBack printCallBack) {
        this.printCallBack = printCallBack;
    }

    @Override
    public void onRunResult(boolean isSuccess) throws RemoteException {
        printCallBack.onRunResult(isSuccess);
    }

    @Override
    public void onReturnString(String result) throws RemoteException {

    }

    @Override
    public void onRaiseException(int code, String msg) throws RemoteException {

    }
    public  interface  PrintCallBack{
        void onRunResult(boolean isSuccess);
    }
}
