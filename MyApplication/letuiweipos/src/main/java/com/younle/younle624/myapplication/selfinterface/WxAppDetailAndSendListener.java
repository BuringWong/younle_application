package com.younle.younle624.myapplication.selfinterface;

/**
 * Created by bert_dong on 2018/4/23 0023.
 * 邮箱：18701038771@163.com
 */

public interface WxAppDetailAndSendListener {
    void onClickDetail(String orderId, String orderType);
    void onClickSend(String orderId);
}
