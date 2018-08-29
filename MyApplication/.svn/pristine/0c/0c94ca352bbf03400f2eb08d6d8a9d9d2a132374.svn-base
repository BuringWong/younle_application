package com.younle.younle624.myapplication.utils.refund;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.manager.orderpager.RefundActivity;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.domain.orderbean.PosPrintBean;
import com.younle.younle624.myapplication.utils.ClicKUtils;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.utils.NetworkUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.utils.format.FormatUtils;
import com.younle.younle624.myapplication.utils.notice.NoticePopuUtils;
import com.younle.younle624.myapplication.utils.notice.SelfToast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.MD5;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/9.
 */

public class RefundTranstation {
    public static RefundTranstation instance;
    public Activity context;
    public NetWorks netWorks;
    private List<Integer> toastCode= Arrays.asList(20103,20107,20108,20117,20119,20121,20133);
    public PosPrintBean posBean;
    public ImageView iv_callbacking;

    public RefundTranstation(Activity context) {
        this.context = context;
        netWorks=new NetWorks(context);

    }

    public void beginTranstration( int isSetPwd, PosPrintBean posPrintBean, ImageView iv_callbacking, OnRefundListener onRefundListener){
        if(NetworkUtils.isNetOK(context)&& !ClicKUtils.isFastDoubleClick()) {
            this.iv_callbacking=iv_callbacking;
            this.posBean=posPrintBean;
            this.onRefundListener=onRefundListener;
            confirmPassWord(isSetPwd);
        }
    }
    private void confirmPassWord(int isSetPwd) {
        //1.判断是否有设置密码
        if(isSetPwd!=1) {
            NoticePopuUtils.noSetPassWordPup(context, R.id.al_bill_detail);
        }else {
            //2.获取输入，验证密码
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            NoticePopuUtils.showInputPassWordPup(context, R.id.al_bill_detail, new NoticePopuUtils.OnFinishpswCallBack() {
                @Override
                public void onFinishInput(String msg) {
                    Utils.showWaittingDialog(context,"密码验证中...");
                    Map<String, String> params = netWorks.getPublicParams();
                    params.put("pwd", MD5.md5(msg+ Constant.ADV_ID));
                    params.put("advid", Constant.ADV_ID);
                    netWorks.Request(UrlConstance.CONFIRM_REFUND_PASSWORD, params, 5000,0, new NetWorks.OnNetCallBack() {
                        @Override
                        public void onError(Exception e, int flag) {
                            Utils.dismissWaittingDialog();
                            Utils.showToast(context,"网络异常，请检查网络后重试!");
                            LogUtils.Log("验证退款密码onerror:"+e.toString());
                        }

                        @Override
                        public void onResonse(String response, int flag) {
                            Utils.dismissWaittingDialog();
                            praseConfirmRefundPwdJson(response);
                            LogUtils.Log("验证退款密码onresponse:"+response.toString());
                        }
                    });
                }
            });
        }
    }
    /**
     * 解析退款密码验证json
     * @param json
     */
    private void praseConfirmRefundPwdJson(String json) {
        try {
            JSONObject jsonObject=new JSONObject(json);
            int code = jsonObject.getInt("code");
            if(code==200) {
                //验证成功
                if(posBean.getMsg().getResouceType()==1||posBean.getMsg().getResouceType()==2) {
                    showBindPup("一旦退款后不可撤销，您确定要退款么？");
                }else {
                    Intent intent=new Intent(context, RefundActivity.class);
                    intent.putExtra("goods_data", posBean);
                    context.startActivityForResult(intent,1);
                }
                /*Intent intent=new Intent(context, RefundActivity.class);
                intent.putExtra("goods_data",(Serializable) posBean);
                context.startActivity(intent);*/
            }else {
//                String errorMsg = jsonObject.getString("msg");
                Utils.showToast(context,"密码输入有误！",1500);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 展示退款不可撤销的pop
     * @param msg
     * @return
     */
    public PopupWindow showBindPup(String msg) {
        return NoticePopuUtils.showBindPup(context, msg, R.id.al_bill_detail,new NoticePopuUtils.OnClickCallBack() {
            @Override
            public void onClickYes() {
                setRebakMoney();
                LogUtils.Log("传递orderid，发起退款请求");
            }
            @Override
            public void onClickNo() {
                LogUtils.Log("取消删除");
            }
        });

    }

    /**
     * 设置退款金额
     */
    private void setRebakMoney() {
        final double maxMoney = posBean.getMsg().getFactPayMoney();
        View restDialog = View.inflate(context, R.layout.set_reback_dialog, null);
        final EditText et_new_price = (EditText) restDialog.findViewById(R.id.et_new_price);
        TextView tv_max_money = (TextView) restDialog.findViewById(R.id.tv_max_money);
        tv_max_money.setText(maxMoney +"");
        et_new_price.setText(maxMoney +"");

        TextView tv_no = (TextView) restDialog.findViewById(R.id.tv_no);
        TextView tv_yes = (TextView) restDialog.findViewById(R.id.tv_yes);
        FormatUtils.accuracy=2;
        FormatUtils.setPricePoint(et_new_price,null);
        final AlertDialog changePriceDialog = new AlertDialog.Builder(context)
                .setView(restDialog)
                .show();
        changePriceDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                LogUtils.Log("cancel");
            }
        });
        Window window = changePriceDialog.getWindow();
        window.setLayout(Utils.dip2px(context,300),Utils.dip2px(context,200));
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1.判断输入金额是否正确
                String input = et_new_price.getText().toString().trim();
                if(TextUtils.isEmpty(input)) {
                    Utils.showToast(context,"请输入退款金额",1000);
                    return;
                }
                Double inputMoney = Double.valueOf(input);
                if(inputMoney>maxMoney) {
                    Utils.showToast(context,"退款金额不能超过"+maxMoney+"元",1500);
                }else if(inputMoney<=0) {
                    Utils.showToast(context,"退款金额必须大于"+0+"元",1500);
                }else {
                    startCircle();
                    changePriceDialog.dismiss();
                    if(posBean.getMsg().getPayType().contains("记账")){
                        sureRefund(inputMoney);
                    }else{
                        requestBack(inputMoney);
                    }
                }
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePriceDialog.dismiss();
            }
        });
    }

    /**
     * 开始退款
     * @param inputMoney
     */
    private void requestBack(final Double inputMoney) {

        netWorks.refund(posBean.getMsg().getOrderNo(),inputMoney,new NetWorks.OnNetCallBack(){

            @Override
            public void onError(Exception e, int flag) {
                LogUtils.Log("退款 e=="+e.toString());
                stopCirle();
                Utils.showToast(context,"请求超时，请重试!",1000);
            }

            @Override
            public void onResonse(String response, int flag) {
                LogUtils.Log("退款：response=="+response);
                parseRefundJson(response,inputMoney);
            }
        });
    }
    /**
     * 解析退款结果
     * @param json
     */
    private void parseRefundJson(String json,final Double inputMoney) {

        try {
            JSONObject jsonObject=new JSONObject(json);
            int code = jsonObject.getInt("code");
            String msg = jsonObject.getString("msg");
            if(code==200) {
                //退款成功
                refunSuccess();
            }else {
                if(toastCode.contains(code)) {
                    //toast
                    stopCirle();
                    Utils.showToast(context,msg,1000);
                }else {
                    //弹出
                    final AlertDialog alertDialog = NoticePopuUtils.refundErrorDia(context, msg,"暂不退款","我已退款","款项未退还" ,new NoticePopuUtils.OnClickCallBack() {
                        @Override
                        public void onClickYes() {
                            sureRefund(inputMoney);
                        }

                        @Override
                        public void onClickNo() {
                            stopCirle();
                        }
                    });
                    alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            alertDialog.dismiss();
                            stopCirle();
                        }
                    });
                }
            }

        } catch (JSONException e) {
            LogUtils.Log("退款返回json解析异常："+e.toString());
            e.printStackTrace();
        }
    }
    /**
     * 标记为退款订单，传递退款金额
     */
    private void sureRefund(Double inputMoney) {
        netWorks.MarkOrderRefund(posBean.getMsg().getOrderNo(),inputMoney, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                LogUtils.Log("re 退款 e=="+e.toString());
                stopCirle();
                Utils.showToast(context,"请求超时，请重试!",1000);
            }

            @Override
            public void onResonse(String response, int flag) {
                LogUtils.Log("re 退款 response=="+response.toString());
                praseReReFundJson(response);
            }
        });
    }
    private void praseReReFundJson(String response) {
        try {
            JSONObject jsonObject=new JSONObject(response);
            int code = jsonObject.getInt("code");
            String msg = jsonObject.getString("msg");
            if(code==200) {
                refunSuccess();
            }else {
                Utils.showToast(context,"操作失败，请重试!",1000);
                stopCirle();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 退款成功
     */
    private void refunSuccess() {
        stopCirle();
        if(posBean.getMsg().getPayType().contains("记账")){
            SelfToast.showToast(context,"已标记退款",1000);
        }else{
            SelfToast.showToast(context,"已退款",1000);
        }
        if(onRefundListener!=null) {
            onRefundListener.onRefundSuccess();
        }
    }
    /**
     * 退款中的ui
     */
    private void startCircle() {
        iv_callbacking.setVisibility(View.VISIBLE);
        Utils.pbAnimation(context, iv_callbacking);
    }
    private void stopCirle() {
        iv_callbacking.clearAnimation();
        iv_callbacking.setVisibility(View.GONE);
    }
    public OnRefundListener onRefundListener;
    public interface OnRefundListener{
        void onRefundSuccess();
    }
}
