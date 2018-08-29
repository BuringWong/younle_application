package com.younle.younle624.myapplication.utils;

/**
 * 作者：Create by 我是奋斗 on2016/12/16 17:10
 * 邮箱：tt090423@126.com
 * 微信：17346512596
 */
public class ClicKUtils {
    public static long lastClickTime;
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < 1000) {
            LogUtils.Log("点击的太快了");
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
