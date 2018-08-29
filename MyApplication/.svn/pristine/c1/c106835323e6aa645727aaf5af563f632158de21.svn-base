package com.younle.younle624.myapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.domain.waimai.SalerInfo;

/**
 * 作者：Create by 我是奋斗 on2017/1/3 12:56
 * 邮箱：tt090423@126.com
 * 微信：17346512596
 */
public class AlertUtils {
    public static AlertUtils instance;
    private PopupWindow popupWindow;
    private TextView tv_sure;
    private ProgressBar pb_loading;
    private View line;
    private TextView tv_bind_notices;
    public static  AlertUtils getInstance(){
        if(instance==null) {
            instance=new AlertUtils();
        }
        return instance;
    }
    /**
     * 绑定饿了么的pupwindow
     * @param fromType 点击是否是开始使用
     */
    public PopupWindow showBindPup(int fromType,int resId,int father_layoutId ,final Activity context, final OnClickCallBack callBack) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bind_elm_pupwindow, null);
        tv_sure = (TextView) view.findViewById(R.id.tv_sure);
        line = view.findViewById(R.id.line);
        pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);
        tv_bind_notices = (TextView) view.findViewById(R.id.tv_bind_notices);
        tv_bind_notices.setText(resId);
        if(fromType==0) {
            line.setVisibility(View.VISIBLE);
            tv_sure.setVisibility(View.VISIBLE);
            pb_loading.setVisibility(View.GONE);
            tv_bind_notices.setVisibility(View.VISIBLE);
        }
        popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final WindowManager.LayoutParams params = context.getWindow().getAttributes();
        params.alpha=0.4f;
        context.getWindow().setAttributes(params);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(context.findViewById(father_layoutId), Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1;
                context.getWindow().setAttributes(params);
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onClick();
            }
        });
        return popupWindow;
    }
    public void upDateUI(boolean success,SalerInfo salerInfo,boolean isBind) {
        String head;
        if(isBind) {
            head="绑定";
        }else {
            head="更换";
        }
        pb_loading.setVisibility(View.GONE);
        tv_sure.setVisibility(View.VISIBLE);
        line.setVisibility(View.VISIBLE);
        tv_bind_notices.setVisibility(View.VISIBLE);
        if(success) {
            tv_bind_notices.setText(head+"饿了么账号需要联系您的\n客服专员 "+salerInfo.getName()
                    +" 提交资料：\n电话："+salerInfo.getTelephone()+"\nQQ："+salerInfo.getQq());
        }else {
            tv_bind_notices.setText("获取信息失败，请重试");
        }
    }
    public void showWxAppPup(final Activity context, View baseView){

        View popView=View.inflate(context,R.layout.wxapp_pup_layout,null);
        final PopupWindow popupWindow = new PopupWindow(popView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        double width = context.getWindowManager().getDefaultDisplay().getWidth() * 0.76;
        popupWindow.setWidth((int)width);
        setAlpha(0.3f,context);
        popupWindow.showAtLocation(baseView, Gravity.CENTER, 0, 0);

        popView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow!=null) {
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(1,context);
            }
        });
    }

    public  void setAlpha(float alpha,Activity context) {
        WindowManager.LayoutParams params = context.getWindow().getAttributes();
        params.alpha = alpha;
        context.getWindow().setAttributes(params);
    }

    public void bindNone(){

    }

    public interface  OnClickCallBack{
        void onClick();
    }
}
