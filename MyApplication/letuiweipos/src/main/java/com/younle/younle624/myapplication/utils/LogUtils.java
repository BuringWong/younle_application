package com.younle.younle624.myapplication.utils;

import android.util.Log;

/**
 * Created by 我是奋斗 on 2016/4/8.
 * 控制打印输出的类
 */
public class LogUtils {
    public static final boolean canLog=false;
    public static final boolean isPrint=false;
    public static final boolean logwm=true;
    public static final boolean write=true;

    public static void Log(String message){
        if(canLog) {
            Log.e("TAG", message);
            if(write) {
                try {
                    SaveUtils.writeLogtoFile("TAG", message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void WmLog(String message){
        if(logwm) {
            if(write) {
                try {
                    SaveUtils.writeLogtoFile("TAG", message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * dzw 0727
     * @param TAG
     * @param message
     */
    public static void e(String TAG,String message){
        if(isPrint) {
            Log.e(TAG, message);
            if (message.length() > 4000) {
                for (int i = 0; i < message.length(); i += 4000) {
                    if (i + 4000 < message.length())
                        Log.e(TAG + "-" + i, message.substring(i, i + 4000));
                    else
                        Log.e(TAG + "-" + i, message.substring(i, message.length()));
                }
            }
        }

        /*if(isPrint) {
            Log.e(TAG, message);*//*

            if(message.length() > 4000) {
                for(int i=0;i<message.length();i+=4000){
                    if(i+4000<message.length())
                        Log.e(TAG +"-"+ i, message.substring(i, i + 4000));
                    else
                        Log.e(TAG +"-"+ i, message.substring(i, message.length()));
                }
            } else{
                Log.e(TAG, message);
                if(write) {
                    try {
                        SaveUtils.writeLogtoFile(TAG,message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }*/
    }
}
