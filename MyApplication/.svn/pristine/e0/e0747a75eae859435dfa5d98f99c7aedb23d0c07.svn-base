package com.yongle.letuiweipad.utils.pay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.activity.MainActivity;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.domain.PayParams;
import com.yongle.letuiweipad.domain.ResultQueryBean;
import com.yongle.letuiweipad.selfinterface.OnPayFinish;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.NetWorks;
import com.yongle.letuiweipad.utils.NoticePopuUtils;
import com.yongle.letuiweipad.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/11/21 0021.
 */

public class PayUtils {
    public static final int PAY_FLAG_QERY_PAY_STATE = 2;
    /**
     * 直接直接收款
     */
    public static  final int PAY_FLAG_DIR_COLL = 1;
    public static  final int QUERY_ORDER_STATE = 6;
    public static final int CUT_DOWN_TIMMER = 5;
    public static final int MAY_PAY_SUCCESS = 1;

    private static PopupWindow waittingPop;
    @SuppressLint("HandlerLeak")
    public static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case QUERY_ORDER_STATE:
                    queryOrderState();
                    break;
                case CUT_DOWN_TIMMER:
                    cutDownTimer();
                    break;
            }
        }
    };
    private static String trade_num;
    private static NetWorks netWork;
    public static OnPayFinish payFinish;
    private static void payFail(String response) {
        LogUtils.saveLog(TAG,"payFail()");
        try {
            JSONObject jsonObject=new JSONObject(response);
            String failReason = jsonObject.getString("code_des");
            String solution = jsonObject.getString("solution");
            payFinish.onPayFailure(response,failReason,solution);
        } catch (JSONException e) {
            payFinish.onPayFailure(response,"数据解析异常","请联系乐推微客服！");
            e.printStackTrace();
        }
    }

    private static TextView tv_cout_timmer;
    private static Activity mActivity;

    public static boolean isTimeGone=false;
    public static int recLen = Constant.RECLEN_TIME;
    public static boolean isClickCancel=false;
    public static View baseView;
    /**
     *
     * @param context
     * @param baseView 跟布局
     * @param payment 实际支付金额
     * @param barcode 扫到的条码，可能是支付码，也可能是会员码
     * @param orderId 订单主键id,针对下单中未支付订单
     * @param totalFee 订单原价
     * @param vipcard_id 会员主键id
     * @param vipcreate_id 会员卡主键id
     * @param cardid 优惠券id
     * @param vip_discount 会员对象中的memberBean.getMsg().getVip_discount()
     * @param cardinfo_pay 会员卡信息对象转json
     * @param room 房间对象转json
     * @param onPayFinish 回调
     * @param usePackage 1.使用设定的充值方案 0.不使用充值方案
     */
    public static void toPay(Activity context,View baseView,String tradeNum,String payment,String totalFee,String barcode,
                             String orderId,String vipcard_id,String vipcreate_id,String cardid,
                             String vip_discount,String cardinfo_pay,String room,int usePackage,OnPayFinish onPayFinish){
        LogUtils.saveLog(TAG,"toPay()");
        payFinish=onPayFinish;
        mActivity=context;
        netWork=new NetWorks(context);
        PayUtils.baseView =baseView;
        Constant.LAST_ORDER_NO = "";
        PayParams payParams = new PayParams();
        trade_num =tradeNum;
        payParams.setType(Constant.UNKNOWN_COMMODITY);
        payParams.setPayWay(Constant.payway);
        if(barcode!=null) {
            payParams.setAuthCode(barcode);
        }
        if(trade_num !=null) {
            payParams.setOrder_no(trade_num);
        }
        if(orderId!=null) {
            payParams.setOrder_id(orderId);
        }

        /*if(payment!=null) {
            payParams.setPayMent(payment);
        }*/
        payParams.setPayMent(payment);
        if(totalFee!=null) {
            payParams.setTotal_fee(totalFee);
        }

        payParams.setVipcard_id(vipcard_id==null?"0":vipcard_id);
        payParams.setCardId(cardid==null?"0":cardid);
        payParams.setVipcreate_id(vipcreate_id==null?"0":vipcreate_id);
        payParams.setLast_order_no(Constant.LAST_ORDER_NO);
        payParams.setVip_discount(vip_discount==null?"0":vip_discount);
        payParams.setCardinfo(cardinfo_pay==null?"0":cardinfo_pay);
        payParams.setUsePackage(usePackage);

        if(room!=null) {
            payParams.setRoom(room);
        }
        showWaittingPopWindow(baseView);
        LogUtils.saveLog(TAG,"支付参数：" + payParams.toString());
        netWork.PayCard(payParams, callBack, PAY_FLAG_DIR_COLL);

    }

    /**
     * 显示等待的PopWindow
     */
    public static void showWaittingPopWindow(final View totalView) {
        recLen = Constant.RECLEN_TIME;
        //增加PopWindow
        final LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_waitting_layout, null);
        tv_cout_timmer = (TextView) view.findViewById(R.id.tv_cout_timmer);
        LinearLayout ll_return_pre = (LinearLayout) view.findViewById(R.id.ll_return_pre);

        waittingPop = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = 0.7f;
        mActivity.getWindow().setAttributes(params);
        waittingPop.setOutsideTouchable(false);
        waittingPop.setFocusable(true);
        waittingPop.showAtLocation(totalView, Gravity.CENTER_HORIZONTAL, 0, Utils.dip2px(mActivity, -100));//设置为取景框中间
        handler.sendEmptyMessage(CUT_DOWN_TIMMER);

        ll_return_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClickCancel = true;
                showUnKnow();
            }
        });
    }

    private static void showUnKnow() {
        dismissWaittingPopWindow();
        setAlpha(mActivity,1);
        NoticePopuUtils.showSeeResultPopWindow(MAY_PAY_SUCCESS, mActivity, baseView, new NoticePopuUtils.OnClickCallBack() {
            @Override
            public void onClickYes() {
                ((MainActivity)mActivity).rgMain.check(R.id.btn_finished_order);
//                Utils.showToast(mActivity, "功能尚未开放，敬请期待！");
            }
            @Override
            public void onClickNo() {

            }
        });
    }

    public static void setAlpha(Activity mActivity,float alpha) {
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = alpha;
        mActivity.getWindow().setAttributes(params);
    }

    /**
     * 轮询发起入口
     * 1.支付接口返回onerror
     * 2.轮询返回onerror
     * 3.轮询返回等待支付
     * 4.轮询返回其他非200结果
     */
    public static void queryOrderState() {
        LogUtils.saveLog(TAG,"queryOrderState()");
        if (!isClickCancel) {
            if (recLen < Constant.RECLEN_TIME && 1 < recLen) {
                LogUtils.e(TAG,"轮询");
                netWork.queryPayState("0", trade_num, callBack, PAY_FLAG_QERY_PAY_STATE);
            }
        }
    }
private static final String TAG="payUtils";
    public static NetWorks.OnNetCallBack callBack = new NetWorks.OnNetCallBack() {
        @Override
        public void onError(Exception e, int flag) {
            switch (flag) {
                case PAY_FLAG_DIR_COLL:
                    LogUtils.saveLog(TAG,"直接收error:"+e.toString());
                    dealpayError();
                    break;
                case PAY_FLAG_QERY_PAY_STATE:
                    LogUtils.saveLog(TAG,"查询状态error:"+e.toString());
                    dealQueryError();
                    break;
            }
        }

        @Override
        public void onResonse(String response, int flag) {
            switch (flag) {
                case PAY_FLAG_DIR_COLL:
                    LogUtils.saveLog(TAG,"直接收onResonse:"+response.toString());
                    dealpayRes(response);
                    break;
                case PAY_FLAG_QERY_PAY_STATE:
                    LogUtils.saveLog(TAG,"查询状态onResonse:"+response.toString());
                    dealQueryRes(response);
                    break;
            }
        }
    };
    public static void dismissWaittingPopWindow() {
        if (!mActivity.isFinishing() && waittingPop != null) {
            handler.removeMessages(CUT_DOWN_TIMMER);
            waittingPop.dismiss();
            WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
            params.alpha = 1f;
            mActivity.getWindow().setAttributes(params);
        }
    }

    public static void cutDownTimer() {
        recLen--;
        tv_cout_timmer.setText("" + recLen);
        if (recLen < Constant.RECLEN_TIME && 0 < recLen) {
            Message message = handler.obtainMessage(CUT_DOWN_TIMMER);
            handler.sendMessageDelayed(message, 1000);      // send message
        } else {
            isTimeGone = true;
            showUnKnow();
        }
    }

    /**
     * 支付返回error
     */
    public static void dealpayError() {
        LogUtils.saveLog(TAG,"dealpayError()");
        if (!isClickCancel && !isTimeGone && recLen < Constant.RECLEN_TIME) {
            if (1 < recLen) {
                handler.sendEmptyMessage(QUERY_ORDER_STATE);
            }
        }
    }

    public static void dealQueryError() {
        if (recLen < Constant.RECLEN_TIME && 1 < recLen) {
            handler.sendEmptyMessageDelayed(QUERY_ORDER_STATE, 2000);
        }
    }

    public static void dealpayRes(String response) {
        LogUtils.e(TAG, "dealpayRes()刷卡支付返回数据：" + response);
        LogUtils.saveLog(TAG,"dealpayRes()");
        if (!isClickCancel && !isTimeGone) {
            dismissWaittingPopWindow();
            try {
                JSONObject payResponse = new JSONObject(response);
                int mCode = payResponse.getInt("code");
                if (30000 == mCode) {//支付失败
                    LogUtils.e(TAG, "支付失败mCode.equals(\"FAIL\")进入收款失败界面");
                    String failreason = payResponse.getString("code_des");
                    String solveway = payResponse.getString("solution");
                    Utils.showToast(mActivity, failreason + solveway, 3000);
                    payFail(response);
                } else if (200 == mCode) {
                   String transaction_id = payResponse.getString("transaction_id");
                    try {
                        String QRCode_url = payResponse.getJSONObject("ticketInfo").getString("url");
                        if (QRCode_url != null && !"".equals(QRCode_url)) {
                            Constant.query_member_left = QRCode_url;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String payment="";
                    try {
                        payment = payResponse.getString("payment");
                    } catch (JSONException e) {
                        LogUtils.e(TAG, e.toString());
                        e.printStackTrace();
                    }
                    Constant.IS_CLOSE_DETAIL_ACTIVITY = true;
                    //2.非会员充值的话，解析折扣信息
                    payFinish.onPaySuccess(response,transaction_id,payment);
                    //3.跳转至成功界面
                }
            } catch (JSONException e) {
                LogUtils.saveLog(TAG,"dealpayRes() JSONException e="+e.toString());
                showUnKnow();
                LogUtils.e(TAG, e.toString());
                e.printStackTrace();
            }
        }
    }

    public static void dealQueryRes(String response) {
        LogUtils.e(TAG, "轮询：response" + response);
        LogUtils.saveLog(TAG,"dealQueryRes()");

        if (recLen < Constant.RECLEN_TIME && 1 < recLen && !isClickCancel) {
            try {
                JSONObject jsonObject = new JSONObject(response);

                int code = jsonObject.getInt("code");
                if (code == 200) {
                    ResultQueryBean resultQueryBean = new Gson().fromJson(response, ResultQueryBean.class);
                    if ("0".equals(resultQueryBean.getMsg().getSuccess())) {//等待支付
                        handler.sendEmptyMessageDelayed(QUERY_ORDER_STATE, 2000);
                    } else if ("1".equals(resultQueryBean.getMsg().getSuccess())) {//确定成功
                        String transaction_id = resultQueryBean.getMsg().getTransaction_id();
                        String payment="";
                        if (resultQueryBean.getMsg().getPayment() != 0) {
                            payment = String.valueOf(resultQueryBean.getMsg().getPayment());
                        }
                        dismissWaittingPopWindow();
                        payFinish.onPaySuccess(response,transaction_id,payment);
                    } else if ("2".equals(resultQueryBean.getMsg().getSuccess())) {//确认失败
                        //弹出失败页面
                        dismissWaittingPopWindow();
                        payFail(response);
                    }
                } else {
                    //轮训查直到到成功为止
                    handler.sendEmptyMessageDelayed(QUERY_ORDER_STATE, 2000);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
/*
    public interface OnPayFinish{
        void paySuccess(String payResponse,String transtion_id,String payment);
        void payFail(String response,String reason,String solution);
    }*/

}
