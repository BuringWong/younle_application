package com.yongle.letuiweipad.selfinterface;

/**
 * Created by Administrator on 2017/12/1 0001.
 */

public interface OnPayFinish {
    void onPaySuccess(String response,String transtion_id,String payment);
    void onPayFailure(String response,String reason,String solution);
}
