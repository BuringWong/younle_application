package com.younle.younle624.myapplication.domain;

/**
 * Created by Administrator on 2016/7/11.
 */
 interface ResultListener {
    //API返回ReturnCode不合法，支付请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问
    void onFailByReturnCodeError(ScanPayResData scanPayResData);

    //API返回ReturnCode为FAIL，支付API系统返回失败，请检测Post给API的数据是否规范合法
    void onFailByReturnCodeFail(ScanPayResData scanPayResData);

    //支付请求API返回的数据签名验证失败，有可能数据被篡改了
    void onFailBySignInvalid(ScanPayResData scanPayResData);


    //用户用来支付的二维码已经过期，提示收银员重新扫一下用户微信“刷卡”里面的二维码
    void onFailByAuthCodeExpire(ScanPayResData scanPayResData);

    //授权码无效，提示用户刷新一维码/二维码，之后重新扫码支付"
    void onFailByAuthCodeInvalid(ScanPayResData scanPayResData);

    //用户余额不足，换其他卡支付或是用现金支付
    void onFailByMoneyNotEnough(ScanPayResData scanPayResData);

    //支付失败
    void onFail(ScanPayResData scanPayResData);

    //支付成功
    void onSuccess(ScanPayResData scanPayResData);
}
