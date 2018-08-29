package com.yongle.letuiweipad.selfinterface;

/**
 * Created by BurNing.Wong on 2018/7/19 0019.
 * 邮箱：tt090423@126.com
 */

public interface BleWeighListener {
    void onWeighDel();
    void onWeighConnect();
    void onWeighDisconnect();
    void onWeighConnecting();
    void onWeighDataChange(int version, double weighAcc,String weighMode,String zf,int status,String weighUnite);
}
