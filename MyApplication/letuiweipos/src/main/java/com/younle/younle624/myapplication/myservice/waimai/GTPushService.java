package com.younle.younle624.myapplication.myservice.waimai;

import android.content.Context;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.wm.WmHandleUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;

import static com.igexin.sdk.GTServiceManager.context;

public class GTPushService extends GTIntentService {
    public GTPushService() {
    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        synchronized (this){
            byte[] payload = msg.getPayload();
            String data=new String(payload);
            LogUtils.WmLog("收到了个推推送的订单=="+data);

            if(data.contains("wxapp")){
                /*LogUtils.e(TAG,"Constant.OPEN_APPLET="+Constant.OPEN_APPLET);
                if(Constant.OPEN_APPLET){
                    LogUtils.e(TAG,"data.contains(\"wxapp\")...");
                    Constant.MESSAGE_ALREADYLOOKED = true;
                    EventBus.getDefault().post("manager_applet_come");
                    AppletHandleUtils.getInstance(context).handleOrder(data);
                }*/
            }else{
                WmHandleUtils.getInstance(context).handleOrder(data);
            }
        }
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        if(Constant.ADV_ID!=null) {
            getEpoiBindAccount();
        }
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        Log.e(TAG,"onReceiveCommandResult"+cmdMessage.toString());
    }
    /**
     * 获取epoiid，绑定jpush推送id的共同接口
     */
    public void getEpoiBindAccount() {
        String registrationID = JPushInterface.getRegistrationID(context);
        String clientid = PushManager.getInstance().getClientid(context);
        if(clientid==null) {
            clientid="";
        }
        LogUtils.Log("clientid=="+clientid);
        LogUtils.Log("registrationId=="+registrationID);
        LogUtils.Log("flagid=="+ Constant.ADV_ID);
        LogUtils.Log("store_id=="+Constant.STORE_ID);
        OkHttpUtils.post()
                .url(UrlConstance.WM_GET_EPIIID_BIND)
                .addParams("flagid", Constant.ADV_ID)
                .addParams("store_id", Constant.STORE_ID)
                .addParams("registionid", registrationID)
                .addParams("accountid", Constant.ACCOUNT_ID)
                .addParams("device", Constant.APPLICATION_TYPE)
                .addParams("cid",clientid)
                .build()
                .connTimeOut(10000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                    }
                });
    }
}
