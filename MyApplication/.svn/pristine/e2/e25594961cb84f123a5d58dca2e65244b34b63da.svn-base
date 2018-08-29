package com.yongle.letuiweipad.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.constant.UrlConstance;
import com.yongle.letuiweipad.domain.PayParams;
import com.yongle.letuiweipad.selfinterface.ConfirmPwdListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.MD5;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 作者：Create by 我是奋斗 on2016/12/19 11:25
 * 邮箱：tt090423@126.com
 * 微信：17346512596
 * 支付的统一入口
 */
public class NetWorks {

    public final String TAG = "NetWorks";
    private Activity context;
    private Context context_yun;
    private AlertDialog loadingDia;

    public NetWorks(Context context) {
       this.context_yun = context;
   }
    public NetWorks(Activity context) {
        this.context = context;
        this.context_yun = context;
    }

    /**
     * 查询是否开通了储值功能
     */
    public void checkRecharge() {
    }

    public void YunPrint(final int flag, String printer_json, final OnNetCallBack callBack) {
        LogUtils.e(TAG,"云打印：printer_json="+ printer_json);
        if(!"".equals(printer_json)){
            OkHttpUtils.post()
                    .url(UrlConstance.YUN_PRINT)
                    .addParams("msg", printer_json)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            callBack.onError(e,flag);
                        }

                        @Override
                        public void onResponse(String response) {
                            callBack.onResonse(response,flag);
                        }
                    });
        }
    }
    public void checkVersion(int versionCode, final OnNetCallBack callBack) {
        LogUtils.e(TAG,"version=="+versionCode);
        LogUtils.e(TAG,"mode=="+Constant.DEVICE_MODEL);
        OkHttpUtils
                .post()
                .url(UrlConstance.VERSION_URL)
                .addParams("version", versionCode+"")//版本号
                .addParams("model",Constant.DEVICE_MODEL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        LogUtils.Log("异常" + e);
                        callBack.onError(e,0);
                    }
                    @Override
                    public void onResponse(String response) {
                        /*LogUtils.Log("版本检查response="+response);
                        VersionUpdateInfo vuInfo=null;
                        try {
                            vuInfo= new Gson().fromJson(response,VersionUpdateInfo.class);
                        }catch (Exception e){
                            LogUtils.e(TAG,"检查版本解析异常");
                        }*/
                        //拿到服务器上的版本号，判断是否相同，不同则下载
                        callBack.onResonse(response,0);
                    }
                });
    }



    public interface OnNetCallBack {
        void onError(Exception e, int flag);
        void onResonse(String response, int flag);
    }
    public Map<String,String> getPublicParams(){
        if(Constant.PASSWORD==null) {
            Constant.PASSWORD = SpUtils.getInstance(context).getString(Constant.PARAMS_NAME_PASSWORDS, "");
        }
        String currentTime =Utils.getCurrentTime();
        String token = Utils.getToken(currentTime, context);
        Map<String,String> params=new HashMap<>();
        params.put(Constant.PARAMS_NAME_POSTOKEN,token);
        params.put(Constant.PARAMS_NAME_TIMEAUTH, currentTime);
        params.put(Constant.PARAMS_NAME_USERACCOUNT,Constant.USER_ACCOUNT);
        params.put(Constant.PARAMS_NAME_IMEI,Constant.DEVICE_IMEI);
        params.put(Constant.PARAMS_NAME_DEVICENAME,Constant.DEVICE_NAME);
        params.put(Constant.PARAMS_NAME_MODEL,Constant.DEVICE_MODEL);
        params.put(Constant.PARAMS_NEME_PASSDORD_MD5, MD5.md5(MD5.md5(Constant.PASSWORD)));
        params.put("version",Constant.VERSION_CODE+"");
        return params;
    }

    public void Request(String url, boolean showLoading, final String loadingMsg, Map<String,String> params, long time, final int flag, final OnNetCallBack callBack){
       for (String key:params.keySet()){
           LogUtils.Log(key+"=="+params.get(key));
       }
       if(showLoading) {
//           Utils.showWaittingDialog(context,loadingMsg);
           netLoadingDia(context,loadingMsg);
       }
        OkHttpUtils.post()
                .url(url)
                .params(params)
                .build()
                .connTimeOut(time)
                .readTimeOut(time)
                .writeTimeOut(time)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Utils.dismissWaittingDialog();
                        callBack.onError(e,flag);
                    }
                    @Override
                    public void onResponse(String response) {
                        if(loadingDia!=null&&loadingDia.isShowing()) {
                            loadingDia.dismiss();
                        }
//                        Utils.dismissWaittingDialog();
                        boolean b = Utils.checkSaveToken(context, response);
                        if(b) {
                            callBack.onResonse(response,flag);
                        }
                    }
                });
    }

   public void confirmPassWord(final ConfirmPwdListener confirmPwdListener){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        NoticePopuUtils.showInputPassWordDia(context,"请输入密码，以验证身份", new NoticePopuUtils.OnFinishpswCallBack() {
            @Override
            public void onFinishInput(String msg) {
                Utils.showWaittingDialog(context,"密码验证中...");
                Map<String, String> params = getPublicParams();
                params.put("pwd", MD5.md5(msg+ Constant.ADV_ID));
                params.put("advid", Constant.ADV_ID);
                Request(UrlConstance.CONFIRM_REFUND_PASSWORD,true, "密码验证中...",params, 5000,0, new NetWorks.OnNetCallBack() {
                    @Override
                    public void onError(Exception e, int flag) {
                        Utils.dismissWaittingDialog();
                        Utils.showToast(context,"网络异常，请检查网络后重试!");
                        LogUtils.Log("验证退款密码onerror:"+e.toString());
                    }

                    @Override
                    public void onResonse(String response, int flag) {
                        Utils.dismissWaittingDialog();
                        LogUtils.e(TAG,"密码验证："+response.toString());
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int code = jsonObject.getInt("code");
                            if(code==200) {
                                if(confirmPwdListener!=null) {
                                    confirmPwdListener.onPwdPass();
                                }
                            }else {
                                String errorMsg = jsonObject.getString("msg");
                                Utils.showToast(context,errorMsg,1500);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    /**
     * 解析退款密码验证json
     * @param json
     */
    private void praseConfirmRefundPwdJson(String json) {

    }

    public void netLoadingDia(Context context,String msg){
        if(loadingDia!=null&&loadingDia.isShowing()) {
            return;
        }
        View dialogView = View.inflate(context, R.layout.waitting_dialog_commit_goods, null);
        TextView tvMsg=ButterKnife.findById(dialogView,R.id.tv_voucher_search);
        tvMsg.setText(msg);
        loadingDia = new AlertDialog.Builder(context)
                .show();

        Window window = loadingDia.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        window.setAttributes(attributes);
        window.setContentView(dialogView);
        window.setLayout(Utils.dip2px(context, 400), Utils.dip2px(context, 180));
        window.setGravity(Gravity.CENTER);
    }



    public void BannerRequest(String url,String loadingMsg, Map<String,String> params, long time,final int flag,final OnNetCallBack callBack){
       for (String key:params.keySet()){
           LogUtils.Log(key+"=="+params.get(key));
       }
        OkHttpUtils.post()
                .url(url)
                .params(params)
                .build()
                .connTimeOut(time)
                .readTimeOut(time)
                .writeTimeOut(time)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        callBack.onError(e,flag);
                    }
                    @Override
                    public void onResponse(String response) {
                        boolean b = Utils.checkSaveToken(context, response);
                        if(b) {
                            callBack.onResonse(response,flag);
                        }
                    }
                });
    }
    public void payRequest(String url,String loadingMsg, Map<String,String> params, long time,final int flag,final OnNetCallBack callBack){
       /*for (String key:params.keySet()){
           LogUtils.Log(key+"=="+params.get(key));
       }*/
       LogUtils.saveLog(TAG,"payRequest()");
        OkHttpUtils.post()
                .url(url)
                .params(params)
                .build()
                .connTimeOut(time)
                .readTimeOut(time)
                .writeTimeOut(time)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
//                        LogUtils.saveLog(TAG,"支付接口onError() e="+e.toString());
                        Utils.dismissWaittingDialog();
                        callBack.onError(e,flag);
                    }
                    @Override
                    public void onResponse(String response) {
//                        LogUtils.saveLog(TAG,"支付接口onResponse() resonse="+response.toString());
                        boolean b = Utils.checkSaveToken(context, response);
                        if(b) {
                            callBack.onResonse(response,flag);
                        }
                    }
                });
    }

    //微信和支付宝支付
    public void PayCard(PayParams params, final OnNetCallBack callBack, final int flag) {
        LogUtils.saveLog(TAG,"PayCard()");
        String store_name = "";
        if(Constant.STORE_P!=null){
            store_name = Constant.STORE_P;
        }
        if(Constant.STORE_M!=null){
            store_name += Constant.STORE_M;
        }
        Map<String, String> publicParams = getPublicParams();
        publicParams.put(Constant.PARAMS_NEME_STORE_ID,Constant.STORE_ID);
        publicParams.put("adv_id",Constant.ADV_ID);
        publicParams.put("deviceid",Constant.DEVICE_IMEI);
        publicParams.put("account_id",Constant.ACCOUNT_ID);
        publicParams.put("type",params.getType());
        publicParams.put("pay_type",params.getPayWay());
        publicParams.put("last_order_no",params.getLast_order_no());
        publicParams.put("auth_code",params.getAuthCode());
        publicParams.put("order_no",params.getOrder_no());
        publicParams.put("order_id",params.getOrder_id());
        publicParams.put("payment",params.getPayMent());
        publicParams.put("total_fee",params.getTotal_fee());
        publicParams.put("vipcard_id",params.getVipcard_id());
        publicParams.put("cardid",params.getCardId());
        publicParams.put("vipcreate_id",params.getVipcreate_id());
        publicParams.put("room",params.getRoom());
        publicParams.put("micro",params.getMicro());
        publicParams.put("vip_discount",params.getVip_discount());
        publicParams.put("store_name",store_name);
        publicParams.put("last_alipay",params.getLast_alipay());
        publicParams.put("cardinfo",params.getCardinfo());
        publicParams.put("use_package",params.getUsePackage()+"");
//        publicParams.put("Cardno",params.getCardNo());
        payRequest(UrlConstance.PAY_ORDER_TOTALFEE,null,publicParams,30000,flag,callBack);
        LogUtils.Log("支付接口:"+UrlConstance.PAY_ORDER_TOTALFEE);
    }
    /**
     * 查询支付状态
     * @param type type如果是小微商户收款为1 会员充值为2 其他情况为0
     * @param trade_num
     * @param callBack
     * @param flag
     */
    public void queryPayState(String type, String trade_num, final OnNetCallBack callBack, final int flag) {
        Map<String, String> publicParams = getPublicParams();
        publicParams.put("orderid",trade_num);
        publicParams.put("type",type);
        Request(UrlConstance.QUERY_ORDER_STATUS,false,null,publicParams,2000,flag,callBack);
    }
    /*
    public void queryPayState(String type, String trade_num, final OnNetCallBack callBack, final int flag) {

        Map<String, String> publicParams = getPublicParams();
        publicParams.put("orderid",trade_num);
        publicParams.put("type",type);
        Request(UrlConstance.QUERY_ORDER_STATUS,publicParams,2000,flag,callBack);
    }

    *//**
     * 获取订单详情
     *//*
    public void getUnpayOrderDetails(String unpay_order_no, String vipcreate_id, final OnNetCallBack callBack, final int flag) {
        Map<String, String> publicParams = getPublicParams();
        publicParams.put("order_id",unpay_order_no);
        publicParams.put("vipcreate_id",vipcreate_id);
        Request(UrlConstance.UNPAY_ORDER_DETAIL,publicParams,5000,flag,callBack);
    }
    *//**
     * 提交订单备注
     *//*
    public void upDataRemak(String from,String unpay_order_no, String msg, final OnNetCallBack callBack, final int flag) {
        Map<String, String> publicParams = getPublicParams();
        publicParams.put(Constant.PARAMS_NEME_STORE_ID,Constant.STORE_ID);
        publicParams.put(Constant.PARAMS_ADV_ID,Constant.ADV_ID);
        publicParams.put("order_no",unpay_order_no);
        publicParams.put("msginfo",msg);
        publicParams.put("from",from);
        Request(UrlConstance.UPDATE_ORDER_MARK,publicParams,5000,flag,callBack);
    }

    public void refund(String orderNo, Double askedMoney, final OnNetCallBack callback) {

        Map<String, String> publicParams = getPublicParams();
        publicParams.put(Constant.PARAMS_ADV_ID,Constant.ADV_ID);
        publicParams.put("orderNo",orderNo);
        publicParams.put("refundMoney",askedMoney+"");
        Request(UrlConstance.REFUND,publicParams,20000,0,callback);
    }
    public void MarkOrderRefund(String orderNo, Double askedMoney, final OnNetCallBack callback) {
        Map<String, String> publicParams = getPublicParams();
        publicParams.put(Constant.PARAMS_ADV_ID,Constant.ADV_ID);
        publicParams.put("orderNo",orderNo);
        publicParams.put("refundMoney",askedMoney+"");
        Request(UrlConstance.RE_REFUND,publicParams,20000,0,callback);
    }
    *//**
     * 订单详情页取消房间订单
     *//*
    public void cancelRoomOrder(UnPayDetailsBean orderBean, final Activity activity) {
        String current_room_id = "0";
        String exist_goods = "0";//传0代表没有商品  传1代表有商品
        int order_id = orderBean.getMsg().getOrderid();
        if (orderBean.getMsg().getOrder_rooms() != null && orderBean.getMsg().getOrder_rooms().size() > 0) {
            current_room_id = orderBean.getMsg().getOrder_rooms().get(0).getId();
        }
        if(orderBean.getMsg().getOrder_goods()!=null && orderBean.getMsg().getOrder_goods().size()>0){
            exist_goods = "1";
        }
        String currentTime = Utils.getCurrentTime();
        String token = Utils.getToken(currentTime, context);
        OkHttpUtils.post()
                .url(UrlConstance.ROOM_ORDER_CANCEL)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams(PARAMS_NEME_PASSDORD_MD5, MD5.md5(MD5.md5(Constant.PASSWORD)))
                .addParams("adv_id", Constant.ADV_ID)
                .addParams("storeid", Constant.STORE_ID)
                .addParams("roomid", current_room_id)
                .addParams("id", "" + order_id)
                .addParams("exist_goods", "" + exist_goods)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Utils.showToast(activity, "取消订单失败，code=onError");
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e(TAG, "取消房间订单：response=" + response);
                        boolean b = Utils.checkSaveToken(activity, response);
                        if (b) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int code = jsonObject.getInt("code");
                                if (code == 200) {
                                    Utils.showToast(activity, "订单已取消");
                                    activity.finish();
                                } else {
                                    Utils.showToast(activity, "取消订单失败!code="+code);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    *//**
     * 获取二维码：支付宝，微信，淘宝链接码
     * @param flag
     * @param payway
     * @param upgradeMoney
     * @param callBack
     *//*
    public void getQRCodepollingPayResult(final int flag, int postType,String payway, String ccy,String upgradeMoney, final OnNetCallBack callBack){
        Map<String, String> publicParams = getPublicParams();
        publicParams.put(Constant.PARAMS_NEME_STORE_ID,Constant.STORE_ID);
        publicParams.put("cca",Constant.ADV_ID);
        publicParams.put("ccd",Constant.ACCOUNT_ID);
        publicParams.put("ccy",ccy);
        publicParams.put("askty","" + postType);
        publicParams.put("ccway",payway);
        publicParams.put("ccm",upgradeMoney);
        Request(UrlConstance.GET_WX_ZFB_CODE,publicParams,5000,flag,callBack);

        LogUtils.e(TAG,"cca="+Constant.ADV_ID);
        LogUtils.e(TAG,"ccd="+Constant.ACCOUNT_ID);
        LogUtils.e(TAG,"ccy="+ccy);
        LogUtils.e(TAG,"askty="+postType);
        LogUtils.e(TAG,"ccway="+payway);
        LogUtils.e(TAG,"ccm="+upgradeMoney);
    }
    public void getModulePrice(final int flag,String whichModular, final OnNetCallBack callBack) {

        Map<String, String> publicParams = getPublicParams();
        publicParams.put("ccy",whichModular);
        Request(UrlConstance.GET_MODULE_PRICE,publicParams,5000,flag,callBack);
    }

    *//**
     * 充值卡升级
     * @param flag
     * @param code
     * @param callBack
     *//*
    public void redeemCodeToUprade(final int flag,String openWhich,String code, final OnNetCallBack callBack){
        Map<String, String> publicParams = getPublicParams();
        publicParams.put(Constant.PARAMS_NEME_STORE_ID, Constant.STORE_ID);
        publicParams.put("ccd", Constant.ACCOUNT_ID);
        publicParams.put("advid", Constant.ADV_ID);
        publicParams.put("ccy", openWhich);
        publicParams.put("code_put", code);
        Request(UrlConstance.REDEEM_CODE_TO_UPRADE,publicParams,5000,flag,callBack);
    }
    *//**
     * 获取epoiid，绑定jpush推送id的共同接口
     * @param callBack
     *//*
    public void getEpoiBindAccount(final OnNetCallBack callBack, final int flag) {
        String registrationID = JPushInterface.getRegistrationID(context);
        LogUtils.e(TAG,"registrationID="+registrationID);
        String clientid = PushManager.getInstance().getClientid(context);
        LogUtils.e(TAG,"clientid="+clientid);
        if(clientid==null) {
            clientid="";
        }
        LogUtils.Log("registrationID="+registrationID);
        LogUtils.Log("clientid="+clientid);
        Map<String,String> publicParams=new HashMap<>();
        publicParams.put("flagid", Constant.ADV_ID);
        publicParams.put("store_id", Constant.STORE_ID);
        publicParams.put("registionid", registrationID);
        publicParams.put("accountid", Constant.ACCOUNT_ID);
        publicParams.put("device", Constant.APPLICATION_TYPE);
        publicParams.put("cid",clientid);
        Request(UrlConstance.WM_GET_EPIIID_BIND,publicParams,5000,flag,callBack);

    }
    *//**
     * 获取新的epoiid，绑定jpush推送id的共同接口
     * @param callBack
     *//*
    public void BindNewEpoiId(final OnNetCallBack callBack, final  int flag) {
        String registrationID = JPushInterface.getRegistrationID(context);
        String clientid = PushManager.getInstance().getClientid(context);
        if(clientid==null) {
            clientid="";
        }

        Map<String,String> publicParams=new HashMap<>();
        publicParams.put("flagid", Constant.ADV_ID);
        publicParams.put("store_id", Constant.STORE_ID);
        publicParams.put("registionid", registrationID);
        publicParams.put("accountid", Constant.ACCOUNT_ID);
        publicParams.put("device", Constant.APPLICATION_TYPE);
        publicParams.put("cid",clientid);
        Request(UrlConstance.CREATE_NEW_EPOIID,publicParams,5000,flag,callBack);

    }

    *//**
     * 获取绑定饿了么账号的链接
     * @param callBack
     * @param flag
     *//*
    public void getElmUrl(final OnNetCallBack callBack, final int flag) {
        String registrationID = JPushInterface.getRegistrationID(context);
        String clientid = PushManager.getInstance().getClientid(context);
        if(clientid==null) {
            clientid="";
        }
        Map<String,String> publicParams=new HashMap<>();
        publicParams.put("authid", Constant.ACCOUNT_ID);
        publicParams.put("registionid", registrationID);
        publicParams.put("storeid", Constant.STORE_ID);
        publicParams.put("adv", Constant.ADV_ID);
        publicParams.put("device", Constant.APPLICATION_TYPE);
        publicParams.put("cid",clientid);
        Request(UrlConstance.GET_ELM_AUTHURL,publicParams,5000,flag,callBack);

    }

    *//**
     *  绑定门店信息
     * @param callBack
     *//*
    public void getWmStoreStatus(final OnNetCallBack callBack, final  int flag) {
        LogUtils.Log("getWmStoreStatus flagid==" + Constant.ADV_ID);
        LogUtils.Log("getWmStoreStatus store_id==" + Constant.STORE_ID);
        LogUtils.Log("getWmStoreStatus AccountId==" + Constant.ACCOUNT_ID);
        Constant.WM_STATUS.clear();
        OkHttpUtils.post()
                .url(UrlConstance.WM_STATUS)
                .addParams("flagid", Constant.ADV_ID)
                .addParams("accountid",Constant.ACCOUNT_ID)
                .build()
                .connTimeOut(10000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(context, "网络异常，获取外卖账号绑定状态失败", Toast.LENGTH_SHORT).show();
                        LogUtils.Log("外卖版块状态error：" + e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.Log("外卖版块状态response：" + response);
                        try {
                            JSONObject jObject=new JSONObject(response);
                            int code = jObject.getInt("code");
                            if(code==200) {
                                JSONObject jObject2 = jObject.getJSONObject("data");
                                getOpenStatus(jObject2);
                                JSONArray status = jObject2.getJSONArray("bindmsg");
                                if (status != null && status.length() > 0) {
                                    for (int i = 0; i < status.length(); i++) {
                                        JSONObject bindObj = (JSONObject) status.get(i);
                                        int which = bindObj.getInt("which");

                                        String storename = bindObj.getString("storename");
                                        switch (which) {
                                            case 1 :
                                                Constant.mt_store_name=storename;
                                                break;
                                            case 2:
                                                Constant.REST_ID= bindObj.getString("restid");
                                                Constant.elm_store_name=storename;
                                                getStoreAddTel();
                                                break;
                                            case 3:
                                                Constant.bd_store_name=storename;
                                                break;
                                        }
                                        Constant.WM_STATUS.add(which);
                                        LogUtils.Log(Constant.WM_STATUS.toString());
                                    }
                                }
                                LogUtils.Log("外卖版块状态111：" + Constant.WM_STATUS.toString());
                            }else {
                                Toast.makeText(context, "获取当前账号外卖信息失败！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getOpenStatus(JSONObject jObject2)  {
        int open_status = 0;
        try {
            open_status = jObject2.getInt("open_status");
            if(open_status==0) {
                SpUtils.getInstance(context).save(Constant.IS_JIEDAN_OPEN,false);
            }else if(open_status==1) {
                SpUtils.getInstance(context).save(Constant.IS_JIEDAN_OPEN,true);
            }
        } catch (JSONException e) {
            LogUtils.Log("获取账号外卖开关解析异常："+e.toString());
            e.printStackTrace();
        }

    }
    *//**
     * 获取饿了么门店地址和电话
     *//*
    public void getStoreAddTel() {
        LogUtils.Log("restid=="+Constant.REST_ID);
        OkHttpUtils.post()
                .url(UrlConstance.ELM_ADD_TEL)
                .addParams("restid", Constant.REST_ID)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        LogUtils.Log("获取门店地址失败" + e.toString());
                        Toast.makeText(context, "获取门店地址失败", Toast.LENGTH_SHORT);
                    }
                    @Override
                    public void onResponse(String response) {
                        LogUtils.Log("请求地址接口："+response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int code = jsonObject.getInt("code");
                            if(code==200) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                if(data!=null) {
                                    String address_text = data.getString("address_text");
                                    if(address_text!=null) {
                                        Constant.ELM_STORE_ADD =address_text;
//                                        SpUtils.getInstance(context).save("elm_store_add",Constant.ELM_STORE_ADD);
                                        LogUtils.Log("elm_store_add=="+Constant.ELM_STORE_ADD);
                                    }
                                    JSONArray phone_list = data.getJSONArray("phone_list");

                                    if(phone_list!=null&&phone_list.length()>0) {
                                        String tel= (String) phone_list.get(0);
                                        Constant.ELM_STORE_TEL=tel;
//                                        SpUtils.getInstance(context).save("elm_store_tel",Constant.ELM_STORE_TEL);
                                        LogUtils.Log("elm_store_tel==" + Constant.ELM_STORE_TEL);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    *//**
     * 改变外卖的接单状态
     * @param callBack
     *//*
    public void switchWmStatus(final OnNetCallBack callBack, final int flag,final int status) {

        Map<String,String> publicParams=new HashMap<>();
        publicParams.put("accountid", Constant.ACCOUNT_ID);
        publicParams.put("status",status+"");
        Request(UrlConstance.CHANGE_WM_STATUS,publicParams,5000,flag,callBack);
    }

    *//**
     * 获取销售信息
     * @param callBack
     * @param flag
     *//*
    public void getSalerInfo(final OnNetCallBack callBack,final  int flag) {
        Map<String, String> publicParams = getPublicParams();
        publicParams.put("ad",Constant.ADV_ID);
        Request(UrlConstance.BIND_WM_SALERINFO,publicParams,5000,flag,callBack);
    }
    *//**
     * 更新auth
     *//*
    public void UpDateAuth(final OnGetQueryResult callback , final String whichModule){
        Utils.showWaittingDialog(context,"功能验证中...");
        String currentTime = Utils.getCurrentTime();
        String token = Utils.getToken(currentTime,context);
        OkHttpUtils.post()
                .url(UrlConstance.UPDATE_AUTH)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams(PARAMS_NEME_PASSDORD_MD5, MD5.md5(MD5.md5(Constant.PASSWORD)))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Utils.dismissWaittingDialog();
                        Utils.showToast(context,"网络异常,查询版块开通状态失败!");
                        if (callback != null) {
                        }
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.Log("更新auth状态 response="+response.toString());
                        Utils.dismissWaittingDialog();
                        boolean b = Utils.checkSaveToken(context, response);
                        if (b) {
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                int code = jsonObj.getInt("code");
                                    if(!Constant.OPENED_PERMISSIONS.contains(whichModule)) {
                                        if(callback!=null) {
                                            callback.stateClose();
                                            if("4".equals(whichModule)) {
                                                Utils.showToast(context,"智慧门店模块未开通或已过期，无法使用会员相关功能",2000);
                                            }else if("7".equals(whichModule)) {
                                                Utils.showToast(context,"外卖多平台接单模块未开通或已过期，无法使用此功能",2000);
                                            }
                                        }
                                    }else {
                                        if(callback!=null) {
                                            callback.stateOpen();
                                        }
                                    }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

    }

    *//**
     * 查询是否开通会员
     * @param callback
     *//*
    public void querryIsOpenMember(final OnGetQueryResult callback , final String whichModule){
        Utils.showWaittingDialog(context,"功能验证中...");
        String currentTime = Utils.getCurrentTime();
        String token = Utils.getToken(currentTime,context);
        OkHttpUtils.post()
                .url(UrlConstance.QUERY_MEMBER_IS_OPEN)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams(PARAMS_NEME_PASSDORD_MD5, MD5.md5(MD5.md5(Constant.PASSWORD)))
                .addParams("account_id", Constant.ACCOUNT_ID)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Utils.dismissWaittingDialog();
                        Utils.showToast(context,"查询智慧门店开通状态失败");
                        if (callback != null) {
//                            callback.onError(e, flag);
                        }
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.Log("查询“智慧门店”开通状态 response="+response.toString());
                        Utils.dismissWaittingDialog();
                        boolean b = Utils.checkSaveToken(context, response);
                        if (b) {
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                int code = jsonObj.getInt("code");
                                if(code==200){
                                    if(!Constant.OPENED_PERMISSIONS.contains(whichModule)) {
                                        Constant.OPENED_PERMISSIONS.add(whichModule);
                                    }
                                    if(callback!=null) {
                                        callback.stateOpen();
                                    }
                                }else{
                                    if(Constant.OPENED_PERMISSIONS.contains(whichModule)) {
                                        Constant.OPENED_PERMISSIONS.remove(whichModule);
                                    }
                                    if("4".equals(whichModule)) {
                                        Utils.showToast(context,"您尚未开通智慧门店模块，无法使用会员功能",2000);
                                    }
                                    if(callback!=null) {
                                        callback.stateClose();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    public void YunPrint(final int flag, String printer_json, final OnNetCallBack callBack) {
        LogUtils.e(TAG,"云打印：printer_json="+ printer_json);
        if(!"".equals(printer_json)){
            OkHttpUtils.post()
                    .url(UrlConstance.YUN_PRINT)
                    .addParams("msg", printer_json)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            LogUtils.e(TAG,"云打印：onError Exception e="+e.toString());
                        }

                        @Override
                        public void onResponse(String response) {
                            LogUtils.e(TAG,"云打印：onError response="+response);
                        }
                    });
        }
    }

    public interface OnGetQueryResult{
        public void stateOpen();
        public void stateClose();
    }

    private void printLog(PayParams params) {
        LogUtils.e(TAG, Constant.PARAMS_NAME_USERACCOUNT + "=" + Constant.USER_ACCOUNT);
        LogUtils.e(TAG, Constant.PARAMS_NAME_IMEI + "=" + Constant.DEVICE_IMEI);
        LogUtils.e(TAG, Constant.PARAMS_NAME_DEVICENAME + "=" + Constant.DEVICE_NAME);
        LogUtils.e(TAG, Constant.PARAMS_NAME_MODEL + "=" + Constant.DEVICE_MODEL);
        LogUtils.e(TAG, "userkey=" + MD5.md5(MD5.md5(Constant.PASSWORD)));
        LogUtils.e(TAG, "storeid=" + Constant.STORE_ID);
        LogUtils.e(TAG, "adv_id=" + Constant.ADV_ID);
        LogUtils.e(TAG, "deviceid=" + Constant.DEVICE_IMEI);
        LogUtils.e(TAG, "account_id=" + Constant.ACCOUNT_ID);

        LogUtils.e(TAG, "type=" + params.getType());
        LogUtils.e(TAG, "pay_type=" + params.getPayWay());
        LogUtils.e(TAG, "auth_code=" + params.getAuthCode());
        LogUtils.e(TAG, "order_no=" + params.getOrder_no());
        LogUtils.e(TAG, "order_id=" + params.getOrder_id());
        LogUtils.e(TAG, "payment=" + params.getPayMent());
        LogUtils.e(TAG, "total_fee=" + params.getTotal_fee());
        LogUtils.e(TAG, "vipcreate_id=" + params.getVipcreate_id());
        LogUtils.e(TAG, "vipcard_id=" + params.getVipcardid());
        LogUtils.e(TAG, "cardid=" + params.getCardId());

        LogUtils.e(TAG, "last_order_no=" + params.getLast_order_no());
        LogUtils.e(TAG, "room=" + params.getRoom());
        LogUtils.e(TAG, "micro=" + params.getMicro());
        LogUtils.e(TAG, "vip_discount=" + params.getVip_discount());
        LogUtils.e(TAG, "store_name=" + Constant.STORE_M);
        LogUtils.e(TAG, "last_alipay=" + params.getLast_alipay());
        LogUtils.e(TAG,"cardinfo="+params.getCardinfo());
    }*/

}
