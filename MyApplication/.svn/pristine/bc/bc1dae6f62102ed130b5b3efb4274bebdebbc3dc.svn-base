package com.yongle.letuiweipad.utils;

import android.util.Log;

/**
 * Created by bert_dong on 2017/3/3 0003.
 * 邮箱：18701038771@163.com
 */
public class LogUtils {

    public static final boolean isPrint=false;
    public static final boolean showLog=false;
    public static final boolean saveLog=true;
    
    /**
     * dzw 0727
     * @param TAG
     * @param message
     */
    public static void e(String TAG,String message){
        if(isPrint) {
            Log.e(TAG, message);
        }
    }
    public static void d(String TAG,String message){
        if(isPrint) {
            Log.d(TAG, message);
        }
    }
    public static void Log(String message){
        if(showLog) {
            Log.e("TAG", message);
        }
    }
    public static void saveLog(String TAG,String message){
        if(saveLog) {
            LogUtils.e(TAG,message);
            try {
                SaveUtils.deleLog();
                SaveUtils.writeLogtoFile(TAG, message);
            }catch (Exception e){

            }
        }
    }
}
