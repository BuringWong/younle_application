package com.younle.younle624.myapplication.utils.printmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.utils.Utils;

/**
 * 作者：Create by 我是奋斗 on2016/12/20 14:16
 * 邮箱：tt090423@126.com
 * 微信：17346512596
 */
public class PrintDia {
    public static PrintDia instance;
    public static PrintDia getInstance(){
        if(instance==null) {
            instance=new PrintDia();
        }
        return instance;
    }
    /**
     * 提示打印第二联的dialog
     */
    public AlertDialog showPrintSecondDia(Context context,TextView tv_left_printtime,String pName,String nName, final OnHandleListener diaCallBack) {
       AlertDialog waitPrintSecDia = new AlertDialog.Builder(context)
                .setView(tv_left_printtime)
                .setPositiveButton(pName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        diaCallBack.onPositveButton();
                    }
                })
                .setNegativeButton(nName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        diaCallBack.onNegativeButton();
                    }
                })
                .show();
        waitPrintSecDia.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, Utils.dip2px(context, 140));
        waitPrintSecDia.setCanceledOnTouchOutside(false);
        waitPrintSecDia.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK;
            }
        });
        return waitPrintSecDia;
    }
    public  interface OnHandleListener{
        void onPositveButton();
        void onNegativeButton();
    }
    public  interface PrintingCallBacK{
        void close();
    }
    public AlertDialog showPrintingDia(Context context, final PrintingCallBacK printingCallBacK) {
        View printingDiaView = View.inflate(context, R.layout.printing_dialog, null);
        ImageView iv_close = (ImageView) printingDiaView.findViewById(R.id.iv_close);
        AlertDialog printIngDia = new AlertDialog.Builder(context)
                .setView(printingDiaView)
                .show();
        printIngDia.setCanceledOnTouchOutside(false);
        Window window = printIngDia.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.alpha = 0.9f;
        window.setAttributes(attributes);
        window.setLayout(Utils.dip2px(context, 212), LinearLayout.LayoutParams.WRAP_CONTENT);
        printIngDia.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK;
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              printingCallBacK.close();
            }
        });
        return printIngDia;
    }
}
