package com.younle.younle624.myapplication.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.zxing.WriterException;
import com.younle.younle624.myapplication.activity.manager.orderpager.pos.PayQRCodeActivity;

import zxing.encoding.EncodingHandler;

/**
 * Created by bert_dong on 2017/4/20 0020.
 * 邮箱：18701038771@163.com
 *
 * 小微商户收款码生成的类
 */
public class MicroMerchantCodeUtils {

    public static String TAG = "MicroMerchantCodeUtils";

    public static void toQRActivity(Context context){
        Intent intent = new Intent(context, PayQRCodeActivity.class);
        context.startActivity(intent);
    }

    /**
     * 获取供顾客扫的二维码
     */
    public static void getCode(ImageView imageView,String linkstr){

        if (linkstr != null && (!"".equals(linkstr))) {
            Bitmap tbcode;
            try {
                tbcode = EncodingHandler.createQRCode(linkstr, 500);
                imageView.setImageBitmap(tbcode);
            } catch (WriterException e) {
                LogUtils.e(TAG, "getCode() WriterException e="+e);
                e.printStackTrace();
            }
        } else {
            LogUtils.e(TAG, "showQRCode(),linkstr==null|| .equals(linkstr))");
        }
    }

}
