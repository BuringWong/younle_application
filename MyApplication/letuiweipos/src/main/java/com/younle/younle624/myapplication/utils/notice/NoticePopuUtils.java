package com.younle.younle624.myapplication.utils.notice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.adapter.SenderAdapter;
import com.younle.younle624.myapplication.domain.Sender;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.view.FlowLayout;

import java.util.List;

/**
 * 作者：Create by 我是奋斗 on2017/5/3 16:07
 * 邮箱：tt090423@126.com
 * 微信：17346512596
 */
public class NoticePopuUtils {

    private static TextView tv_weigh;
    private static AlertDialog setZeroDia;
    private static Sender.DataBean selectedSender;

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
     * 展示订单详情的popuwindow
     */
    public static void chooseSender(final Activity context, int rootId, final List<Sender.DataBean> senderData, final OnFinishpswCallBack callback) {
        selectedSender=null;
        View popView = View.inflate(context, R.layout.select_sender, null);
        ListView lv_order_content= (ListView) popView.findViewById(R.id.lv_order_content);
        TextView tv_cancel= (TextView) popView.findViewById(R.id.tv_cancel);
        TextView tv_sure= (TextView) popView.findViewById(R.id.tv_sure);
        final SenderAdapter senderAdapter=new SenderAdapter(context);

        senderAdapter.setData(senderData);
        lv_order_content.setAdapter(senderAdapter);
        final PopupWindow detailPup = new PopupWindow(popView);

        BitmapDrawable bitmapDrawable = new BitmapDrawable();
        bitmapDrawable.setAlpha(5);
        detailPup.setBackgroundDrawable(bitmapDrawable);
        detailPup.setFocusable(true);
        detailPup.setOutsideTouchable(true);
        detailPup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);

        if(senderData.size()>5) {
            detailPup.setHeight(540);
        }else {
            detailPup.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        setAlpha(context,0.7f);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailPup.dismiss();
            }
        });
        detailPup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(context,1);
            }
        });
        detailPup.showAtLocation(context.findViewById(rootId), Gravity.BOTTOM, 0, 0);
        lv_order_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedSender = senderData.get(position);
                for (int i = 0; i < senderData.size(); i++) {
                    if(i==position) {
                        senderData.get(position).setSelected(true);
                    }else {
                        senderData.get(i).setSelected(false);
                    }
                }
                senderAdapter.notifyDataSetChanged();
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedSender==null) {
                    Utils.showToast(context,"请选择配送员",1500);
                }else {
                    detailPup.dismiss();
                    if(callback!=null) {
                        callback.onFinishInput(selectedSender.getId());
                    }
                }
            }
        });
    }

    public static AlertDialog refundErrorDia(final Activity context, String msg,String leftText,String rightText, String title,final OnClickCallBack callBack) {
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
        Window window = dialog.getWindow();
        window.setLayout(Utils.dip2px(context,270), WindowManager.LayoutParams.WRAP_CONTENT);
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
        params.width=Utils.dip2px(context,295);
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
    public static PopupWindow showInputPassWordPup(final Activity context,int baseView, final OnFinishpswCallBack callBack) {

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.input_password_pup, null);
        final EditText again_paypswd_pet= (EditText) view.findViewById(R.id.again_paypswd_pet);
        ImageView close= (ImageView) view.findViewById(R.id.close);
        again_paypswd_pet.setFocusable(true);
        again_paypswd_pet.setFocusableInTouchMode(true);
        again_paypswd_pet.requestFocus();
        TextView tv_bind_notices = (TextView) view.findViewById(R.id.tv_notice_msg);
        tv_bind_notices.setText("请输入退款密码，以验证身份");

        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setAlpha(context,0.4f);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
//        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(context.findViewById(baseView), Gravity.TOP, 0, 200);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(context,1f);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
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
                String password = again_paypswd_pet.getText().toString().trim();
                if(password!=null&&password.length()==6) {
                    popupWindow.dismiss();
                    callBack.onFinishInput(password);
                }
            }
        });

        return popupWindow;
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

    public static void showMemberTagsPop(final Activity context, View baseView, List<String> tags) {
        final View popView = View.inflate(context,R.layout.check_mark_layout, null);
        FlowLayout fl_multi = (FlowLayout) popView.findViewById(R.id.fl_multi);
        for(int i=0;i<tags.size();i++){
            RelativeLayout childView = (RelativeLayout) View.inflate(context,R.layout.child_view_layout, null);
            TextView tv_mark_content = (TextView) childView.findViewById(R.id.tv_mark_content);

            tv_mark_content.setText(tags.get(i));
            setBoardColor(i+1,tv_mark_content);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.rightMargin = 20;
            layoutParams.topMargin=20;
            childView.setLayoutParams(layoutParams);
            childView.setGravity(Gravity.CENTER_HORIZONTAL);
            fl_multi.addView(childView);
        }

        final PopupWindow popupWindow = new PopupWindow(popView, Utils.dip2px(context,280), WindowManager.LayoutParams.WRAP_CONTENT);
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

    public interface  ThreeOnClickCallBack{
        void onClickLeft();
        void onClickCenter();
        void onClickRight();
    }
    public interface  OnClickCallBack{
        void onClickYes();
        void onClickNo();
    }
    public interface  SetRemakCallBack{
        void onClickYes(String msg);
        void onClickNo();
    }
    public interface  OnFinishpswCallBack{
        void onFinishInput(String msg);
    }

}
