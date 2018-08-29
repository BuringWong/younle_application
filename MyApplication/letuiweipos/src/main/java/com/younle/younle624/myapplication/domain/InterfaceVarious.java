package com.younle.younle624.myapplication.domain;

import com.younle.younle624.myapplication.utils.LogUtils;

/**
 * Created by Administrator on 2016/7/11.
 */
public class InterfaceVarious implements ResultListener {
    @Override
    public void onFailByReturnCodeError(ScanPayResData scanPayResData) {
        LogUtils.Log("scanPayResData"+scanPayResData);
    }

    @Override
    public void onFailByReturnCodeFail(ScanPayResData scanPayResData) {

    }

    @Override
    public void onFailBySignInvalid(ScanPayResData scanPayResData) {

    }

    @Override
    public void onFailByAuthCodeExpire(ScanPayResData scanPayResData) {

    }

    @Override
    public void onFailByAuthCodeInvalid(ScanPayResData scanPayResData) {

    }

    @Override
    public void onFailByMoneyNotEnough(ScanPayResData scanPayResData) {

    }

    @Override
    public void onFail(ScanPayResData scanPayResData) {

    }

    @Override
    public void onSuccess(ScanPayResData scanPayResData) {

    }
}
