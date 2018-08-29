package com.yongle.letuiweipad.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.activity.MainActivity;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.domain.PrintDevices;
import com.yongle.letuiweipad.utils.format.FormatUtils;
import com.yongle.letuiweipad.utils.scanbar.HidConncetUtil;
import com.yongle.letuiweipad.view.FlowLayout;
import com.yongle.letuiweipad.view.ScanRelativeLayout;

import butterknife.ButterKnife;

import static com.yongle.letuiweipad.utils.Utils.tf;


/**
 * 作者：Create by 我是奋斗 on2017/5/3 16:07
 * 邮箱：tt090423@126.com
 * 微信：17346512596
 */
public class NoticePopuUtils {

    public static final int MEMBER_CODE = 0;
    public static final int WX_CODE = 1;
    public static final int ZFB_CODE = 2;
    public static RelativeLayout scanner_error;
    public static TextView scanner_error_msg;
    private static TextView tv_weigh;
    private static AlertDialog setZeroDia;

    public static PopupWindow showBindPup(final Activity context, String msg, int baseView, final OnClickCallBack callBack) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.barscanner_pup, null);
        TextView tv_yes = (TextView) view.findViewById(R.id.tv_yes);
        TextView tv_no = (TextView) view.findViewById(R.id.tv_no);
        TextView tv_bind_notices = (TextView) view.findViewById(R.id.tv_notice_msg);
        tv_bind_notices.setText(msg);
        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setAlpha(context,0.4f);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        int[] wh=new int[2];
        Utils.getRelativeWH(context,606,390,wh);
        popupWindow.setWidth(wh[0]);
        popupWindow.setHeight(wh[1]);
        popupWindow.showAtLocation(context.findViewById(baseView), Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(context,1);
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if(callBack!=null) {
                    callBack.onClickYes();
                }
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if(callBack!=null) {
                    callBack.onClickNo();
                }
            }
        });
        return popupWindow;
    }
    /**
     * 订单已结账
     */
    public static void showPayedDia(final Activity context,String msg, int baseView, final OnFinishChoose callBack) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.payed_pup, null);
        TextView tv_yes = (TextView) view.findViewById(R.id.tv_yes);
//        TextView tv_no = (TextView) view.findViewById(R.id.tv_no);
        TextView tv_bind_notices = (TextView) view.findViewById(R.id.tv_notice_msg);
        tv_bind_notices.setText(msg);
        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setAlpha(context,0.4f);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        int[] wh=new int[2];
        Utils.getRelativeWH(context,606,390,wh);
        popupWindow.setWidth(wh[0]);
        popupWindow.setHeight(wh[1]);
        popupWindow.showAtLocation(context.findViewById(baseView), Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(context,1);
                callBack.onClickYes();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if(callBack!=null) {
                    callBack.onClickYes();
                }
            }
        });
    }
    public static AlertDialog showBindDia(final Activity context, String msg, final OnClickCallBack callBack) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.barscanner_pup, null);
        TextView tv_yes = (TextView) view.findViewById(R.id.tv_yes);
        TextView tv_no = (TextView) view.findViewById(R.id.tv_no);
        TextView tv_bind_notices = (TextView) view.findViewById(R.id.tv_notice_msg);
        tv_bind_notices.setText(msg);
        final AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setView(view)
                .show();
        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        int[] wh=new int[2];
        Utils.getRelativeWH(context,606,520,wh);
        params.width=wh[0];
        params.height=wh[1];
        alertDialog.getWindow().setAttributes(params);
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if(callBack!=null) {
                    callBack.onClickYes();
                }
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if(callBack!=null) {
                    callBack.onClickNo();
                }
            }
        });
        return alertDialog;
    }
    public static AlertDialog showVoucherInfo(final Activity context, String msg, final OnClickCallBack callBack) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.voucher_verification_package, null);
        TextView tv_yes = (TextView) view.findViewById(R.id.tv_yes);
        TextView tv_no = (TextView) view.findViewById(R.id.tv_no);

        final AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setView(view)
                .show();
        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        int[] wh=new int[2];
        Utils.getRelativeWH(context,606,520,wh);
        params.width=wh[0];
        params.height=wh[1];
        alertDialog.getWindow().setAttributes(params);
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if(callBack!=null) {
                    callBack.onClickYes();
                }
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if(callBack!=null) {
                    callBack.onClickNo();
                }
            }
        });
        return alertDialog;
    }
    public static AlertDialog showSetZero(final Activity context, String msg, final ThreeOnClickCallBack callBack) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.three_btn_close, null);
        TextView tv_right = (TextView) view.findViewById(R.id.tv_right);
        TextView tv_center = (TextView) view.findViewById(R.id.tv_center);
        TextView tv_left = (TextView) view.findViewById(R.id.tv_left);
        tv_weigh = (TextView) view.findViewById(R.id.tv_weigh);
        TextView tv_bind_notices = (TextView) view.findViewById(R.id.tv_notice_msg);
        tv_bind_notices.setText(msg);
        setZeroDia = new AlertDialog.Builder(context)
                .setView(view)
                .show();
        WindowManager.LayoutParams params = setZeroDia.getWindow().getAttributes();
        int[] wh=new int[2];
        Utils.getRelativeWH(context,606,520,wh);
        params.width=wh[0];
        params.height=wh[1];
        setZeroDia.getWindow().setAttributes(params);
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setZeroDia.dismiss();
            }
        });
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(callBack!=null) {
                    callBack.onClickRight();
                }
            }
        });
        tv_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callBack!=null) {
                    callBack.onClickCenter();
                }
            }
        });
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setZeroDia.dismiss();
                if(callBack!=null) {
                    callBack.onClickLeft();
                }
            }
        });
        return setZeroDia;
    }
    public static void upDateWeigh(String weigh){
        if(setZeroDia!=null&&setZeroDia.isShowing()&&tv_weigh!=null) {
            tv_weigh.setText(weigh);
        }

    }
    public static AlertDialog addWhatPrinter(final Activity context, String msg, final OnClickCallBack callBack) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.choose_add_weigher_type, null);
        TextView tv_yes = (TextView) view.findViewById(R.id.tv_yes);
        TextView tv_no = (TextView) view.findViewById(R.id.tv_no);
        TextView tv_bind_notices = (TextView) view.findViewById(R.id.tv_notice_msg);
        tv_bind_notices.setText(msg);
        final AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setView(view)
                .show();
        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        int[] wh=new int[2];
        Utils.getRelativeWH(context,606,500,wh);
        params.width=wh[0];
        params.height=wh[1];
        alertDialog.getWindow().setAttributes(params);
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if(callBack!=null) {
                    callBack.onClickYes();
                }
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if(callBack!=null) {
                    callBack.onClickNo();
                }
            }
        });
        return alertDialog;
    }
    public static AlertDialog refundErrorDia(final Activity context, String msg, String leftText, String rightText, String title, final OnClickCallBack callBack) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.refund_error_pup, null);
        TextView tv_yes = (TextView) view.findViewById(R.id.tv_yes);
        TextView tv_no = (TextView) view.findViewById(R.id.tv_no);
        TextView tv_bind_notices = (TextView) view.findViewById(R.id.tv_notice_msg);
        TextView tv_title = (TextView) view.findViewById(R.id.title);

        tv_title.setText(title);
        tv_no.setText(leftText);
        tv_yes.setText(rightText);
        tv_bind_notices.setText(msg);
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .show();
        dialog.setCanceledOnTouchOutside(true);
        int[] wh=new int[2];
        Utils.getRelativeWH(context,606,420,wh);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width=wh[0];
        params.height=wh[1];
        dialog.getWindow().setAttributes(params);
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if(callBack!=null) {
                    callBack.onClickYes();
                }
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if(callBack!=null) {
                    callBack.onClickNo();
                }
            }
        });
        return dialog;
    }

    public static PopupWindow noSetPassWordPup(final Activity context,int baseView) {

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.not_setpassword_pup, null);
        TextView tv_bind_notices = (TextView) view.findViewById(R.id.tv_notice_msg);
        View tv_yes = view.findViewById(R.id.tv_yes);

        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setAlpha(context,0.4f);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(context.findViewById(baseView), Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(context,1f);
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        return popupWindow;
    }
    public static Dialog showInputPassWordDia(final Activity context,String msg, final OnFinishpswCallBack callBack) {

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.input_password_pup, null);
        final EditText again_paypswd_pet= (EditText) view.findViewById(R.id.again_paypswd_pet);
        ImageView close= (ImageView) view.findViewById(R.id.close);
        again_paypswd_pet.setFocusable(true);
        again_paypswd_pet.setFocusableInTouchMode(true);
        again_paypswd_pet.requestFocus();
        TextView tv_bind_notices = (TextView) view.findViewById(R.id.tv_notice_msg);
        tv_bind_notices.setText(msg);

        final AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setView(view)
                .show();
        int width = alertDialog.getWindow().getWindowManager().getDefaultDisplay().getWidth();
        int height = alertDialog.getWindow().getWindowManager().getDefaultDisplay().getHeight();
        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.width= (int) (width*0.31);
        params.height= (int) (height*0.41);
        alertDialog.getWindow().setAttributes(params);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        again_paypswd_pet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                LogUtils.Log("输入密码："+again_paypswd_pet.getText().toString());
                String password = again_paypswd_pet.getText().toString().trim();
                if(password!=null&&password.length()==6) {
                    alertDialog.dismiss();
                    callBack.onFinishInput(password);
                }
            }
        });

        return alertDialog;
    }

    @NonNull
    public static void setAlpha(Activity context,float alpha) {
        WindowManager.LayoutParams params =context.getWindow().getAttributes();
        params.alpha=alpha;
        context.getWindow().setAttributes(params);
    }

    public static AlertDialog setRemakDia(final Activity context, String msg, int baseView, final SetRemakCallBack callBack) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.input_remark_dia, null);
        TextView tv_yes = (TextView) view.findViewById(R.id.tv_yes);
        TextView tv_no = (TextView) view.findViewById(R.id.tv_no);
        final EditText et_notice_msg = (EditText) view.findViewById(R.id.et_notice_msg);
        et_notice_msg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        et_notice_msg.setText(msg);
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .show();
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setLayout(Utils.dip2px(context,320), WindowManager.LayoutParams.WRAP_CONTENT);
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = et_notice_msg.getText().toString();
                dialog.dismiss();
                if(callBack!=null) {
                    callBack.onClickYes(input);
                }
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if(callBack!=null) {
                    callBack.onClickNo();
                }
            }
        });
        return dialog;
    }

    public static void showMemberTagsPop(final Activity context, View baseView, String[] tags) {
        final View popView = View.inflate(context,R.layout.check_mark_layout, null);
        FlowLayout fl_multi = (FlowLayout) popView.findViewById(R.id.fl_multi);
        for(int i=0;i<tags.length;i++){
            RelativeLayout childView = (RelativeLayout) View.inflate(context,R.layout.child_view_layout, null);
            TextView tv_mark_content = (TextView) childView.findViewById(R.id.tv_mark_content);

            tv_mark_content.setText(tags[i]);
            setBoardColor(i+1,tv_mark_content);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.rightMargin = 20;
            layoutParams.topMargin=20;
            childView.setLayoutParams(layoutParams);
            childView.setGravity(Gravity.CENTER_HORIZONTAL);
            fl_multi.addView(childView);
        }

        final PopupWindow popupWindow = new PopupWindow(popView, Utils.dip2px(context,360), WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(baseView, Gravity.CENTER, 0, 0);
        popupWindow.update();
        setAlpha(context,0.4f);
        popView.findViewById(R.id.tv_click_to_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(context,1f);
            }
        });
    }

    public static void setBoardColor(int tag,TextView tv_mark_content) {
        //计算是第几个进行不同颜色展示 position =  1 2 3 4
        int i = tag % 4;
        if(i==1){//position=0;
            tv_mark_content.setTextColor(Color.parseColor("#fa7e59"));
            tv_mark_content.setBackgroundResource(R.drawable.mark_back_shape_one);
        }else if(i==2){//position=1;
            tv_mark_content.setTextColor(Color.parseColor("#e9ac1b"));
            tv_mark_content.setBackgroundResource(R.drawable.mark_back_shape_two);
        }else if(i==3){//position=2;
            tv_mark_content.setTextColor(Color.parseColor("#18b5ba"));
            tv_mark_content.setBackgroundResource(R.drawable.mark_back_shape_three);
        }else if(i==0){//position=3;
            tv_mark_content.setTextColor(Color.parseColor("#7c9bef"));
            tv_mark_content.setBackgroundResource(R.drawable.mark_back_shape_four);
        }
    }

    /**
     * 识别会员弹出
     * @param context
     * @param baseView
     * @param callBack
     * @return
     */
    public static PopupWindow showInditifyMemberPup(final Activity context, final int baseView, PrintDevices savedScanner,int scanWhat, final OnFinishpswCallBack callBack) {
        if(scanWhat==MEMBER_CODE&&!Constant.OPENED_PERMISSIONS.contains("4")) {
            showBindPup(context,"您还没有开通相应的功能模块，请联系客服开通。",baseView,null);
            return null;
        }
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ScanRelativeLayout view = (ScanRelativeLayout) inflater.inflate(R.layout.member_inditify_pup, null);
        TextView payScanNotice=ButterKnife.findById(view,R.id.tv_payscan_notice);
        LinearLayout ll_fp_notice=ButterKnife.findById(view,R.id.ll_fp_notice);

        TextView input_notice=ButterKnife.findById(view,R.id.input_notice);
        LinearLayout input_layout=ButterKnife.findById(view,R.id.input_layout);
        View center_line=ButterKnife.findById(view,R.id.center_line);

        if(scanWhat==WX_CODE) {
            input_notice.setVisibility(View.GONE);
            input_layout.setVisibility(View.GONE);
            center_line.setVisibility(View.GONE);
            payScanNotice.setText("请扫描顾客的微信付款码");
            ll_fp_notice.setVisibility(View.GONE);
            view.requestFocus();
        }else if(scanWhat==ZFB_CODE) {
            input_notice.setVisibility(View.GONE);
            input_layout.setVisibility(View.GONE);
            center_line.setVisibility(View.GONE);
            ll_fp_notice.setVisibility(View.GONE);
            view.requestFocus();
            payScanNotice.setText("请扫描顾客的支付宝付款码");
        }else if (scanWhat==MEMBER_CODE){
            input_notice.setVisibility(View.VISIBLE);
            input_layout.setVisibility(View.VISIBLE);
            center_line.setVisibility(View.VISIBLE);
            ll_fp_notice.setVisibility(View.VISIBLE);
            payScanNotice.setText("请扫描顾客的会员卡条码");
        }
        scanner_error = (RelativeLayout) view.findViewById(R.id.scanner_error);
        scanner_error_msg = (TextView) view.findViewById(R.id.scanner_error_msg);
        view.findViewById(R.id.find_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FpUtils.showMemberVocher(context,FpUtils.MEMBER);
            }
        });
        view.findViewById(R.id.find_voucher).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FpUtils.showMemberVocher(context,FpUtils.VOUCHER);

            }
        });

        if(savedScanner==null) {
            if(Constant.XS_IN) {
                scanner_error.setVisibility(View.GONE);

            }else {
                scanner_error.setVisibility(View.VISIBLE);
                scanner_error_msg.setText(R.string.need_add_scanner);
            }

        }else {
            boolean scannerConnectState = HidConncetUtil.getScannerConnectState(BluetoothAdapter.getDefaultAdapter().getRemoteDevice(savedScanner.getBlueToothAdd()));
            if(scannerConnectState) {
                scanner_error.setVisibility(View.GONE);
            }else {
                if(Constant.XS_IN) {
                    scanner_error.setVisibility(View.GONE);

                }else {
                    scanner_error.setVisibility(View.VISIBLE);
                    scanner_error_msg.setText(R.string.scanner_disconnect);
                }
            }
        }
        ImageView close=(ImageView) view.findViewById(R.id.close);
        final EditText et_enter_num=(EditText) view.findViewById(R.id.et_enter_num);
        TextView tv_request_memberinfo= (TextView) view.findViewById(R.id.tv_request_memberinfo);

        view.setScanListener(new ScanRelativeLayout.ScanListener() {
            @Override
            public void OnScanFinish(String barcode) {
                LogUtils.Log("popuwindow中读取到条码："+barcode);
                callBack.onFinishInput(barcode);
            }
        });

        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setAlpha(context,0.4f);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        int[] wh=new int[2];
        if(scanWhat==MEMBER_CODE) {
            Utils.getRelativeWH(context,708,550,wh);
        }else {
            Utils.getRelativeWH(context,708,450,wh);
        }
        popupWindow.setWidth(wh[0]);
        popupWindow.setHeight(wh[1]);
//        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(context.findViewById(baseView), Gravity.CENTER, 0, 0);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ((MainActivity)context).startFpLb();
                setAlpha(context,1f);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        tv_request_memberinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                hideSoftKeyboard(context,baseView);
                LogUtils.Log("输入会员卡号："+et_enter_num.getText().toString());
                String password = et_enter_num.getText().toString().trim();
                if(password==null||TextUtils.isEmpty(password)){
                    Utils.showToast(context,"未检测到输入，请输入会员卡号或手机号！");
                    return;
                }
                popupWindow.dismiss();
                callBack.onFinishInput(password);
            }
        });
        return popupWindow;
    }

    private static final String TAG = "NoticePopuUtils";
    /**
     * 先充值后付款的付款弹出
     * @param context
     * @param baseView
     * @param callBack
     * @return
     */
    public static AlertDialog showInditifyMemberDia(final Activity context, final int baseView, PrintDevices savedScanner,int scanWhat, final OnFinishpswCallBack callBack) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ScanRelativeLayout view = (ScanRelativeLayout) inflater.inflate(R.layout.scanner_pay_code, null);
        TextView payScanNotice=ButterKnife.findById(view,R.id.tv_payscan_notice);
        ImageView close=(ImageView) view.findViewById(R.id.close);

        if(scanWhat==WX_CODE) {
            payScanNotice.setText("请扫描顾客的微信付款码");
            view.requestFocus();
        }else if(scanWhat==ZFB_CODE) {
            view.requestFocus();
            payScanNotice.setText("请扫描顾客的支付宝付款码");
        }else if (scanWhat==MEMBER_CODE){
            payScanNotice.setText("请扫描顾客的会员卡条码");
        }
        scanner_error = (RelativeLayout) view.findViewById(R.id.scanner_error);
        scanner_error_msg = (TextView) view.findViewById(R.id.scanner_error_msg);

        if(savedScanner==null) {
            scanner_error.setVisibility(View.VISIBLE);
            scanner_error_msg.setText(R.string.need_add_scanner);
        }else {
            boolean scannerConnectState = HidConncetUtil.getScannerConnectState(BluetoothAdapter.getDefaultAdapter().getRemoteDevice(savedScanner.getBlueToothAdd()));
            if(scannerConnectState) {
                scanner_error.setVisibility(View.GONE);
            }else {
                scanner_error.setVisibility(View.VISIBLE);
                scanner_error_msg.setText(R.string.scanner_disconnect);
            }
        }

        view.setScanListener(new ScanRelativeLayout.ScanListener() {
            @Override
            public void OnScanFinish(String barcode) {
                LogUtils.e(TAG,"dialog中读取到条码："+barcode);
                callBack.onFinishInput(barcode);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e(TAG,"onClick()");
            }
        });
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .show();
        int[] wh=new int[2];
        if(scanWhat==MEMBER_CODE) {
            Utils.getRelativeWH(context,708,550,wh);
        }else {
            Utils.getRelativeWH(context,708,450,wh);
        }

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width=wh[0];
        params.height=wh[1];
        dialog.getWindow().setAttributes(params);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ((MainActivity)context).startFpLb();

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public static AlertDialog showCashChargeDia(final Activity context, final String needAcc, final OnFinishChoose callBack) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.charge_cash_dia, null);
        final EditText etShiShou=ButterKnife.findById(view,R.id.tv_cashpay_shishou);
        final TextView tvReturn=ButterKnife.findById(view,R.id.tv_cashpay_return);
        final TextView tv_yes=ButterKnife.findById(view,R.id.tv_yes);
        ImageView close=(ImageView) view.findViewById(R.id.close);

        etShiShou.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                try{
                    Double shiShou = Double.valueOf(etShiShou.getText().toString());
                    tvReturn.setText(Utils.keepTwoDecimal((shiShou-Double.valueOf(needAcc))+""));

                }catch (Exception e){
                    Utils.showToast(context,"输入不合法，请输入合法数字");
                }
            }
        });

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .show();
        int[] wh=new int[2];
        Utils.getRelativeWH(context,708,450,wh);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width=wh[0];
        params.height=wh[1];
        dialog.getWindow().setAttributes(params);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ((MainActivity)context).startFpLb();

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callBack.onClickYes();
                dialog.dismiss();
            }
        });
        return dialog;
    }
    public static AlertDialog showCardChargeDia(final Activity context, final OnFinishChoose callBack) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.charge_card_dia, null);
        final TextView tv_yes=ButterKnife.findById(view,R.id.tv_yes);
        ImageView close=(ImageView) view.findViewById(R.id.close);



        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .show();
        int[] wh=new int[2];
        Utils.getRelativeWH(context,708,500,wh);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width=wh[0];
        params.height=wh[1];
        dialog.getWindow().setAttributes(params);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ((MainActivity)context).startFpLb();

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callBack.onClickYes();
                dialog.dismiss();
            }
        });
        return dialog;
    }


    private final int NO_PAY_RESULT = 0;
    private final int MAY_PAY_SUCCESS = 1;
    /**
     * 支付结果未知时的弹出
     * @param tag
     */
    public static void showSeeResultPopWindow(int tag, final Activity mActivity, View baseView, final OnClickCallBack callBack) {
        final LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.unknow_pay_result, null);
        TextView tv_return = (TextView) view.findViewById(R.id.tv_return);
        TextView tv_see_orders = (TextView) view.findViewById(R.id.tv_see_orders);
        TextView tv_title_pay_result = (TextView) view.findViewById(R.id.tv_title_pay_result);
        switch (tag) {
            case 0://支付结果未知
                tv_title_pay_result.setText(mActivity.getResources().getString(R.string.no_pay_result));
                break;
            case 1://可能支付成功
                tv_title_pay_result.setText(mActivity.getResources().getString(R.string.may_pay_success));
                break;
        }
        final PopupWindow resultPop = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = 0.7f;
        mActivity.getWindow().setAttributes(params);
        resultPop.setOutsideTouchable(false);
        resultPop.setFocusable(true);
        resultPop.showAtLocation(baseView,Gravity.CENTER,0,0);//设置为取景框中间
        resultPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(mActivity,1);
            }
        });
        tv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlpha(mActivity,1);
                if (resultPop != null) {
                    resultPop.dismiss();
                }
                Constant.IS_CLOSE_DETAIL_ACTIVITY = true;
            }
        });
        tv_see_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlpha(mActivity,1);
                if (resultPop != null) {
                    resultPop.dismiss();
                }
                Constant.IS_CLOSE_DETAIL_ACTIVITY = true;
                callBack.onClickYes();
            }
        });
    }

    public static void hideSoftKeyboard(Activity context,int baseView) {

        ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
//        imm.hideSoftInputFromWindow(context.findViewById(baseView).getWindowToken(), 0); //制隐藏键盘
    }

    public static AlertDialog changeMoney(final Activity context, final String payment, final OnFinishChangePrice callBack) {
        View restDialog = View.inflate(context, R.layout.reset_count_dialog, null);
        final EditText et_new_price = (EditText) restDialog.findViewById(R.id.et_new_price);
        TextView tv_current_money = (TextView) restDialog.findViewById(R.id.tv_current_money);
        TextView btn_finish_change = (TextView) restDialog.findViewById(R.id.btn_finish_change);
        TextView btn_cancel_change = (TextView) restDialog.findViewById(R.id.btn_cancel_change);
        final EditText et_modify_price_reason = (EditText) restDialog.findViewById(R.id.et_modify_price_reason);
        final TextView tv_unit = (TextView) restDialog.findViewById(R.id.tv_unit);
        //折扣
        final TextView tv_countway_notice = (TextView) restDialog.findViewById(R.id.tv_countway_notice);
        final TextView tv_change_countway = (TextView) restDialog.findViewById(R.id.tv_change_countway);
        final TextView tv_after_discount = (TextView) restDialog.findViewById(R.id.tv_after_discount);

        tv_change_countway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("按折扣改价".equals(tv_change_countway.getText().toString())) {
                    et_new_price.setText(null);
                    tv_after_discount.setVisibility(View.VISIBLE);
                    setDiscountAccount(tv_after_discount,"折后金额为--元");
                    tv_countway_notice.setText("如需直接输入金额，");
                    tv_change_countway.setText("请点此返回");
                    et_new_price.setHint("请输入折扣(1~9.9),如8折输入8");
                    FormatUtils.accuracy=1;
                    FormatUtils.setPricePoint(et_new_price, new FormatUtils.OnInputChangeListener() {
                        @Override
                        public void inputChange() {
                            String currentInput = et_new_price.getText().toString();
                            if(!TextUtils.isEmpty(currentInput)) {
                                Double total = Double.valueOf(payment);
                                if("折".equals(tv_unit.getText().toString())) {
                                    Double discount = Double.valueOf(currentInput);
                                    setDiscountAccount(tv_after_discount,"折后金额为"+Utils.keepTwoDecimal(total*discount/10+"")+"元");
                                }
                            }else {
                                setDiscountAccount(tv_after_discount,"折后金额为--元");
                            }
                        }
                    });
                    tv_unit.setText("折");
                }else {
                    et_new_price.setText(null);
                    tv_after_discount.setVisibility(View.GONE);
                    tv_countway_notice.setText("如需打折，请选择");
                    tv_change_countway.setText("按折扣改价");
                    et_new_price.setHint("请输入修改后的金额");
                    FormatUtils.accuracy=2;
                    FormatUtils.setPricePoint(et_new_price,null);
                    tv_unit.setText("元");
                }
            }
        });
        tv_current_money.setText("当前金额：" + Utils.keepTwoDecimal(payment) + "元");
        FormatUtils.accuracy=2;
        FormatUtils.setPricePoint(et_new_price,null);
        InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(restDialog, 0);

        final AlertDialog changePriceDialog = new AlertDialog.Builder(context)
                .setView(restDialog)
                .show();
        WindowManager.LayoutParams params = changePriceDialog.getWindow().getAttributes();
        int[] wh=new int[2];
        Utils.getRelativeWH(context,670,580,wh);
        params.width=wh[0];
        params.height=wh[1];
        changePriceDialog.getWindow().setAttributes(params);
        btn_finish_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = et_new_price.getText().toString();
                boolean zk = TextUtils.equals("请点此返回", tv_change_countway.getText().toString());
                String changeRemark = et_modify_price_reason.getText().toString();
                String afterGj="";
                if(input!=null&&!TextUtils.isEmpty(input)) {
                    if (zk) {
                        Double discount = Double.valueOf(input);
                        if(discount<=0||discount>=10) {
                            Utils.showToast(context,"折扣必须在0-10之间");
                            Utils.dismissWaittingDialog();
                            return;
                        }
                        Double doublePayment = Double.valueOf(payment);
                        afterGj=Utils.keepTwoDecimal((discount/10)*doublePayment+"");
                    }else {
                        afterGj = Utils.keepTwoDecimal(input);
                    }
                }else {
                    if(zk) {
                        Utils.showToast(context, "请输入折扣");
                    }else {
                        Utils.showToast(context, "请输入修改后的金额");
                    }
                    return;
                }
                callBack.onFinishInput(afterGj,changeRemark);
            }
        });
        btn_cancel_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePriceDialog.dismiss();
            }
        });
        return changePriceDialog;
    }
    public static void setDiscountAccount(TextView tv_after_discount,String text){
        SpannableStringBuilder builder=new SpannableStringBuilder(text);
        ForegroundColorSpan span = new ForegroundColorSpan(Color.rgb(233,160,60));
        builder.setSpan(span,5,text.length()-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_after_discount.setText(builder);
    }

    public static PopupWindow showPayRsesultPop(final FragmentActivity context, View baseView,String payment, String way,
                                                String orderNo,String orderTime, String remark, final OnFinishChoose callBack) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pay_result_pop, null);
        TextView tv_payment= (TextView) view.findViewById(R.id.tv_payment);
        TextView tv_payway= (TextView) view.findViewById(R.id.tv_payway);
        TextView order_no= (TextView) view.findViewById(R.id.order_no);
        TextView deal_time= (TextView) view.findViewById(R.id.deal_time);
        TextView tv_remark= (TextView) view.findViewById(R.id.tv_remark);
        tv_payment.setText("收款金额："+payment);
        tv_payway.setText("收款方式："+way);
        order_no.setText("商户单号："+orderNo);
        deal_time.setText("交易时间："+orderTime);
        tv_remark.setText("订单备注："+remark);
        TextView tv_yes = (TextView) view.findViewById(R.id.tv_yes);
        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setAlpha(context,0.4f);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        int[] wh=new int[2];
        Utils.getRelativeWH(context,600,390,wh);
        popupWindow.setWidth(wh[0]);

        popupWindow.showAtLocation(baseView, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(context,1);
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if(callBack!=null) {
                    callBack.onClickYes();
                }
            }
        });
        return popupWindow;
    }
    public static PopupWindow showPayFailPop(final FragmentActivity context, View baseView,String reason,String solution, String way,
                                                String orderNo,String orderTime, String remark, final OnFinishChoose callBack) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pay_fail_pop, null);
        TextView tv_payway= (TextView) view.findViewById(R.id.tv_payway);
        TextView order_no= (TextView) view.findViewById(R.id.order_no);
        TextView deal_time= (TextView) view.findViewById(R.id.deal_time);
        TextView tv_fail_reason= (TextView) view.findViewById(R.id.tv_fail_reason);
        TextView tv_fail_solution= (TextView) view.findViewById(R.id.tv_fail_solution);
        tv_payway.setText("收款方式："+way);
        order_no.setText("商户单号："+orderNo);
        deal_time.setText("交易时间："+orderTime);
        tv_fail_reason.setText("失败原因："+reason);
        tv_fail_solution.setText("处理方法："+solution);
        TextView tv_yes = (TextView) view.findViewById(R.id.tv_yes);
        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setAlpha(context,0.4f);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        int[] wh=new int[2];
        Utils.getRelativeWH(context,600,390,wh);
        popupWindow.setWidth(wh[0]);

        popupWindow.showAtLocation(baseView, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(context,1);
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if(callBack!=null) {
                    callBack.onClickYes();
                }
            }
        });
        return popupWindow;
    }

    public static void chooseTime(Context mActivity, String initTime,String title,String topTime,String bottomtime,int hour,int minute, final OnFinishTimeChoose callBack) {
        String[] split = initTime.substring(0,initTime.indexOf(" ")).split("-");
        int year = Integer.valueOf(split[0]);
        int month = Integer.valueOf(split[1])-1;
        final int day = Integer.valueOf(split[2]);
        LogUtils.Log("设置之前："+year+"-"+month+"-"+day);
        View pickView = View.inflate(mActivity, R.layout.date_pick, null);
        TextView tv_title = (TextView) pickView.findViewById(R.id.date_pick_title);
        tv_title.setText(title);

        final DatePicker dp= ButterKnife.findById(pickView,R.id.dp_time_picker);
        TextView tvNo=ButterKnife.findById(pickView,R.id.tv_no);
        TextView tvYes=ButterKnife.findById(pickView,R.id.tv_yes);
        final TimePicker tp=ButterKnife.findById(pickView,R.id.tp);
        tp.setIs24HourView(true);
        tp.setCurrentHour(hour);
        tp.setCurrentMinute(minute);

        dp.updateDate(year,month,day);
        final AlertDialog dialog = new AlertDialog.Builder(mActivity)
                .setView(pickView)
                .show();
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dp.clearFocus();
                int year = dp.getYear();
                int month = dp.getMonth()+1;
                int dayOfMonth = dp.getDayOfMonth();
                callBack.onFinishChoose(year+"-"+month+"-"+dayOfMonth,tp.getCurrentHour(),tp.getCurrentMinute());
                if(dialog!=null&&dialog.isShowing()) {
                    dialog.dismiss();
                }
                LogUtils.Log(year+"-"+month+"-"+dayOfMonth);
            }
        });
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog!=null&&dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        int width = dialog.getWindow().getWindowManager().getDefaultDisplay().getWidth();
        int height = dialog.getWindow().getWindowManager().getDefaultDisplay().getHeight();
        LogUtils.Log("width=="+width);
        LogUtils.Log("height=="+height);
        params.width= (int) (width*0.42);
        params.height= (int) (height*0.55);
//        params.height=Utils.px2dip(mActivity,580);
        dialog.getWindow().setAttributes(params);
    }

    public static void chooseDoubleTime(Context mActivity, String startInitTime,String endInitTime, final DoubleTimeChooseCallBack callBack) {
        String[] split = startInitTime.split("-");
        int year = Integer.valueOf(split[0]);
        int month = Integer.valueOf(split[1])-1;
        final int day = Integer.valueOf(split[2]);
        LogUtils.Log("设置之前："+year+"-"+month+"-"+day);

        String[] split1 = endInitTime.split("-");
        int end_year = Integer.valueOf(split1[0]);
        int end_month = Integer.valueOf(split1[1])-1;
        final int end_day = Integer.valueOf(split1[2]);
        LogUtils.Log("设置之后："+year+"-"+month+"-"+day);

        View pickView = View.inflate(mActivity, R.layout.double_date_pick, null);
        final DatePicker startDp= ButterKnife.findById(pickView,R.id.datePickerStart);
        final DatePicker endDp= ButterKnife.findById(pickView,R.id.datePickerEnd);
        final TimePicker tp_start= ButterKnife.findById(pickView,R.id.tp_start);
        final TimePicker tp_end= ButterKnife.findById(pickView,R.id.tp_end);
        tp_start.setIs24HourView(true);
        tp_end.setIs24HourView(true);

        tp_start.setCurrentHour(0);
        tp_start.setCurrentMinute(0);
        Button tvYes=ButterKnife.findById(pickView,R.id.print_total);
        startDp.updateDate(year,month,day);
        endDp.updateDate(end_year,end_month,end_day);
        final AlertDialog dialog = new AlertDialog.Builder(mActivity)
                .setView(pickView)
                .show();
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDp.clearFocus();
                endDp.clearFocus();
                int start_year = startDp.getYear();
                int start_month = startDp.getMonth()+1;
                int start_dayOfMonth = startDp.getDayOfMonth();
                int end_year = endDp.getYear();
                int end_month = endDp.getMonth()+1;
                int end_dayOfMonth = endDp.getDayOfMonth();

                callBack.onFinishInput(
                        start_year+"-"+start_month+"-"+start_dayOfMonth+" "+tf.format(tp_start.getCurrentHour())+":"+tf.format(tp_start.getCurrentMinute()),
                        end_year+"-"+end_month+"-"+end_dayOfMonth+" "+tf.format(tp_end.getCurrentHour())+":"+tf.format(tp_end.getCurrentMinute()));
                if(dialog!=null&&dialog.isShowing()) {
                    dialog.dismiss();
                }
                LogUtils.Log("开始："+start_year+"-"+start_month+"-"+start_dayOfMonth+" 结束:"+end_year+"-"+end_month+"-"+end_dayOfMonth);


            }
        });
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        int width = dialog.getWindow().getWindowManager().getDefaultDisplay().getWidth();
        int height = dialog.getWindow().getWindowManager().getDefaultDisplay().getHeight();
        LogUtils.Log("width=="+width);
        LogUtils.Log("height=="+height);
        params.width= (int) (width*0.42);
        params.height= (int) (height*0.92);
        dialog.getWindow().setAttributes(params);
    }

    /**
     * 其他设备登录
     * @param context
     * @param callBack
     */
    public static void showOfflinDia(final Activity context,String msg, final OnFinishChoose callBack) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.acc_offline, null);
        TextView tv_yes = (TextView) view.findViewById(R.id.tv_yes);
        TextView tv_bind_notices = (TextView) view.findViewById(R.id.tv_notice_msg);
        tv_bind_notices.setText(msg);

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        int[] wh=new int[2];
        Utils.getRelativeWH(context,606,390,wh);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width=wh[0];
        params.height=wh[1];
        dialog.getWindow().setAttributes(params);
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if(callBack!=null) {
                    callBack.onClickYes();
                }
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
    }
    /**
     * 蓝牙打印机添加备注
     * @param context
     * @param callBack
     */
    public static void showInPutDia(final Activity context, final String msg,String currentMsg, final boolean mustInput, final OnFinishInput callBack) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bt_printer_remark, null);
        TextView tv_yes = (TextView) view.findViewById(R.id.tv_yes);
        TextView tv_no = (TextView) view.findViewById(R.id.tv_no);
        final EditText et_input= (EditText) view.findViewById(R.id.et_input);
        if(currentMsg!=null) {
            et_input.setText(currentMsg);
            et_input.setSelection(et_input.getText().length());
        }
        TextView tv_bind_notices = (TextView) view.findViewById(R.id.tv_notice_msg);
        tv_bind_notices.setText(msg);

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        int[] wh=new int[2];
        Utils.getRelativeWH(context,676,482,wh);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width=wh[0];
        params.height=wh[1];
        dialog.getWindow().setAttributes(params);
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = et_input.getText().toString();
                if(mustInput&&(input==null||TextUtils.isEmpty(input))) {
                    Utils.showToast(context,"未检测到输入信息",1000);
                    return;
                }
                dialog.dismiss();
                if(callBack!=null) {
                    callBack.onFinishInput(input);
                }
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(callBack!=null) {
                    callBack.onCancelInput();
                }
            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
    }
    /**
     * 蓝牙打印机添加备注
     * @param context
     * @param callBack
     */
    public static void showRemarkInPutDia(final Activity context, final String msg, final String currentRemark, final boolean mustInput, final OnFinishInput callBack) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.order_remark, null);
        TextView tv_yes = (TextView) view.findViewById(R.id.tv_yes);
        TextView tv_no = (TextView) view.findViewById(R.id.tv_no);
        final EditText et_input= (EditText) view.findViewById(R.id.et_input);
        TextView tv_bind_notices = (TextView) view.findViewById(R.id.tv_notice_msg);
        tv_bind_notices.setText(msg);

        if(currentRemark!=null&&!TextUtils.isEmpty(currentRemark)) {
            et_input.setText(currentRemark);
        }

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        int[] wh=new int[2];
        Utils.getRelativeWH(context,676,482,wh);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width=wh[0];
        params.height=wh[1];
        dialog.getWindow().setAttributes(params);
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = et_input.getText().toString();
                if(mustInput&&(input==null||TextUtils.isEmpty(input))) {
                    Utils.showToast(context,"未检测到输入信息",1000);
                    return;
                }
                if(TextUtils.equals(input,currentRemark)) {
                    Utils.showToast(context,"备注内容未改变",1000);
                    return;
                }
                dialog.dismiss();
                if(callBack!=null) {
                    callBack.onFinishInput(input);
                }
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(callBack!=null) {
                    callBack.onCancelInput();
                }
            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
    }
    public static PopupWindow showCancelFail(final Activity context, String msg,String btnMsg, int baseView, final OnFinishChoose callBack) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.cancel_fail, null);
        TextView tv_no = (TextView) view.findViewById(R.id.tv_no);
        TextView tv_bind_notices = (TextView) view.findViewById(R.id.tv_notice_msg);
        tv_bind_notices.setText(msg);
        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setAlpha(context,0.4f);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        int[] wh=new int[2];
        Utils.getRelativeWH(context,606,390,wh);
        popupWindow.setWidth(wh[0]);
        popupWindow.setHeight(wh[1]);
        popupWindow.showAtLocation(context.findViewById(baseView), Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(context,1);
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if(callBack!=null) {
                    callBack.onClickYes();
                }
            }
        });
        return popupWindow;
    }

    public static void showWxAppPup(final Activity context, View baseView){

        View popView=View.inflate(context,R.layout.wxapp_pup_layout,null);
        final PopupWindow popupWindow = new PopupWindow(popView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        double width = context.getWindowManager().getDefaultDisplay().getWidth() * 0.31;
        double height = context.getWindowManager().getDefaultDisplay().getHeight() * 0.62;

        popupWindow.setWidth((int)width);
        popupWindow.setHeight((int) height);
        setAlpha(context,0.3f);
        popupWindow.showAtLocation(baseView, Gravity.CENTER, 0, 0);

        popView.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
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
                setAlpha(context,1);
            }
        });
    }




    public interface  OnClickCallBack{
        void onClickYes();
        void onClickNo();
    }
    public interface  ThreeOnClickCallBack{
        void onClickLeft();
        void onClickCenter();
        void onClickRight();
    }
    public interface  OnFinishChoose{
        void onClickYes();
    }
    public interface  SetRemakCallBack{
        void onClickYes(String msg);
        void onClickNo();
    }
    public interface  OnFinishpswCallBack{
        void onFinishInput(String msg);
    }
    public interface  OnFinishTimeChoose{
        void onFinishChoose(String date,int hour,int minute);
    }
    public interface  DoubleTimeChooseCallBack{
        void onFinishInput(String start,String end);
    }
    public interface  OnFinishChangePrice{
        void onFinishInput(String afterGj,String changeRemark);
    }
    public interface  OnFinishInput{
        void onFinishInput(String msg);
        void onCancelInput();
    }

}
