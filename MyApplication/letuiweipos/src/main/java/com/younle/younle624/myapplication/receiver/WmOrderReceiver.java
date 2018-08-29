package com.younle.younle624.myapplication.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.orderguide.OpenOrderActivity;
import com.younle.younle624.myapplication.activity.takeoutfood.BindMtActivity;
import com.younle.younle624.myapplication.activity.takeoutfood.WaiMaiActivity;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.waimai.ElmOrderBean;
import com.younle.younle624.myapplication.domain.waimai.FinisTop;
import com.younle.younle624.myapplication.domain.waimai.MtOrderDetail;
import com.younle.younle624.myapplication.domain.waimai.NewElmOrderBean;
import com.younle.younle624.myapplication.domain.waimai.Overdue;
import com.younle.younle624.myapplication.myservice.PrintService;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.SaveUtils;
import com.younle.younle624.myapplication.utils.SpUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.utils.wm.WmHandleUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

public class WmOrderReceiver extends BroadcastReceiver {
    private static final String TAG = "WmOrderReceiver";
    private Context context;
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            if(msg.what==1) {
                LogUtils.Log("轮询开启服务");
                String json= (String) msg.obj;
                if(Utils.isServiceWork(context,"com.younle.younle624.myapplication.myservice.PrintService")) {
                    WmHandleUtils.getInstance(context).handleOrder(json);
                }else {
                    context.startService(new Intent(context, PrintService.class));
                    handler.sendMessageDelayed(msg,0);
                }
            }
        }
    };

    public WmOrderReceiver() {
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean wmPrint = Utils.isServiceWork(context, "com.younle.younle624.myapplication.myservice.PrintService");
        if(!wmPrint) {
            context.startService(new Intent(context, PrintService.class));
        }
        this.context=context;
        Bundle bundle = intent.getExtras();
        if(JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//            playBeep(2);
            if(isRunning()) {
                String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
                String extra_json = bundle.getString(JPushInterface.EXTRA_EXTRA);
                LogUtils.Log("extra_json==" + extra_json);
                parseNotificationJson(0,extra_json,alert);
            }
        }
        if(JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
            String extra_json = bundle.getString(JPushInterface.EXTRA_EXTRA);
            LogUtils.Log("extra_json==" + extra_json);
            parseNotificationJson(1,extra_json,alert);
        }
        if(JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            synchronized (this){
                String json = bundle.getString(JPushInterface.EXTRA_EXTRA);
                LogUtils.e(TAG,"极光推送json="+json);

                if(json!=null&&json.contains("wxapp")){
                    /*LogUtils.e(TAG,"Constant.OPEN_APPLET="+Constant.OPEN_APPLET);
                    if(Constant.OPEN_APPLET){
                        Constant.MESSAGE_ALREADYLOOKED = true;
                        EventBus.getDefault().post("manager_applet_come");
                        AppletHandleUtils.getInstance(context).handleOrder(json);
                    }*/
                }else{
                    if(isRunning()) {
                        LogUtils.WmLog("极光推送json==" + json);
                        String content_type = bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE);
                        LogUtils.Log("content_type==" + content_type);
                        if("1".equals(content_type)) {//绑定的result
                            parseBindResultJson(json);
                        }else {
                            if(Utils.isServiceWork(context,"com.younle.younle624.myapplication.myservice.PrintService")) {
                                WmHandleUtils.getInstance(context).handleOrder(json);
                            }else {
                                Message message = handler.obtainMessage();
                                message.obj=json;
                                message.what=1;
                                handler.sendMessageDelayed(message,0);
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * 账号绑定结果的json
     * @param json
     */
    private void parseBindResultJson(String json) {
        try {
            JSONObject resultObj=new JSONObject(json);
            String reslut = resultObj.getJSONObject("key").getString("which");
            String storeName = resultObj.getJSONObject("key").getString("storename");
            switch (reslut) {
                case "11":
                    LogUtils.Log("mt绑定成功");
                    undateState(1);
                    Constant.mt_store_name=storeName;
                    break;
                case "21":
                    LogUtils.Log("elm绑定成功");
                    undateState(2);//饿了么门店绑定回推
                    Constant.elm_store_name=storeName;
                    break;
                case "31":
                    LogUtils.Log("bd绑定成功");
                    undateState(3);//百度门店绑定回推
                    Constant.bd_store_name=storeName;
                    break;
            }
            if(isBindMtActivityTop()) {
                FinisTop finshTop=new FinisTop(0);
                EventBus.getDefault().post(finshTop);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析外卖到期通知推送的json
     * @param extra_json
     * @param type 0：运行中收到 1：打开推送消息
     */
    private void parseNotificationJson(int type,String extra_json,String alert) {
        try {
            JSONObject jsonObject=new JSONObject(extra_json);
            int key = jsonObject.getInt("key");
            switch (key) {
                case 0 ://过期
                    if(type==1) {
                        toChargeActivity();
                    }else if(type==0) {
                        Constant.OPEN_WM=false;
                        if(Constant.OPENED_PERMISSIONS.contains("7")) {
                            Constant.OPENED_PERMISSIONS.remove("7");
                        }
                        String deadAlert=context.getString(R.string.wm_overdue);
                        Overdue overdue=new Overdue(deadAlert,0);
                        SpUtils.getInstance(context).save(Constant.ALERT,deadAlert);
                        EventBus.getDefault().post(overdue);
                    }
                    break;
                case 5://剩五天
                    if(type==1) {
                        toChargeActivity();
                    }else if(type==0) {
                        Constant.WM_PAY_AGAIN_ALERT=alert;
                        SpUtils.getInstance(context).save(Constant.ALERT,alert);
                        Overdue overdue=new Overdue(alert,1);
                        EventBus.getDefault().post(overdue);
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开通状态保存到数组中
     * @param which
     */
    private void undateState(Integer which) {
        if(!Constant.WM_STATUS.contains(which)) {
            Constant.WM_STATUS.add(which);
            LogUtils.Log("wm_status=="+Constant.WM_STATUS.toString());
        }
    }

    /**
     * 正常订单的处理
     * @param json
     * @param key
     * @param fromplat
     */
    private void handleNormalOrder(String json,JSONObject key, int fromplat) {
        Gson gson=new Gson();
        synchronized (json){
            boolean newDay = deletePre();
            LogUtils.Log("是否是新的一天：" + newDay);
            LogUtils.WmLog("是否是新的一天：" + newDay);
            if(newDay) {
                Constant.wm_data.clear();
                Constant.pre_size=0;
                Constant.bt_wm_data.clear();
                Constant.bt_pre_size=0;
            }
        }
        switch (fromplat) {
            case 1 ://美团
                MtOrderDetail mtOrderDetail = gson.fromJson(json, MtOrderDetail.class);
                addToList(mtOrderDetail,null,null);
                EventBus.getDefault().post(mtOrderDetail);
                break;
            case 2://饿了么
                ElmOrderBean elmOrderBean = gson.fromJson(key.toString(), ElmOrderBean.class);
                addToList(null,elmOrderBean,null);
                EventBus.getDefault().post(elmOrderBean);
                break;
            case 21:
                NewElmOrderBean newElmOrderBean = gson.fromJson(key.toString(), NewElmOrderBean.class);
                addToList(null,null,newElmOrderBean);
                EventBus.getDefault().post(newElmOrderBean);
                break;
            case 3://百度

                break;
        }
    }

    /**
     * 将接到的订单添加到数组中
     * @param mtOrderBean
     * @param elmOrderBean
     */
    private void addToList(MtOrderDetail mtOrderBean,ElmOrderBean elmOrderBean,NewElmOrderBean newElmOrderBean) {
        if(mtOrderBean!=null) {
            Map map=new HashMap();
            map.put("bean", mtOrderBean);
            map.put("type", new String("MT"));
            map.put("print", false);
            Constant.wm_data.add(0, map);
            Constant.bt_wm_data.add(0, map);
        }
        if(elmOrderBean!=null) {
            Map map=new HashMap();
            map.put("bean", elmOrderBean);
            map.put("type", new String("ELM"));
            map.put("print", false);
            Constant.wm_data.add(0, map);
            Constant.bt_wm_data.add(0, map);
        }

        if(newElmOrderBean!=null) {
            Map map=new HashMap();
            map.put("bean", newElmOrderBean);
            map.put("type", new String("NEW_ELM"));
            map.put("print", false);
            Constant.wm_data.add(0, map);
            Constant.bt_wm_data.add(0, map);
        }
//        SaveUtils.saveObject(context,Utils.getToday(),Constant.wm_data);
    }

    /**
     * 收到充值的推送，跳转到套餐介绍H5
     */
    private void toChargeActivity() {
        //1.判断程序是否在运行
        boolean running = isRunning();
        if(!running) {
            //2.未运行，启动程序，跳转到充值界面
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName cn = new ComponentName("com.younle.younle624.myapplication",
                    "com.younle.younle624.myapplication.activity.LoginActivity");
            intent.setComponent(cn);
            intent.putExtra("start_from", "jpush_receiver");
            context.startActivity(intent);
        }else {
            //3.在运行，跳转到充值页面
            Intent intent=new Intent(context, OpenOrderActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("which_module",1);
            context.startActivity(intent);
        }
    }

    /**
     * 判断已当前日期命名的文件，如果不存在则表示是新的一天，删除前一天的文件
     */
    public  boolean deletePre(){
        String today = Utils.getToday();
        //2.不存在，新的一天,删除前一天，pre_size置0
        if(SaveUtils.getObject(context, today)==null) {
            String preDay = Utils.getPreDay();
            SaveUtils.deleteFile(context, preDay);
            SaveUtils.deleteFile(context,"bt"+preDay);

            SpUtils.getInstance(context).save(Constant.PARA_PRE_SIZE, 0);
            SpUtils.getInstance(context).save(Constant.BT_PARA_PRE_SIZE, 0);
            SaveUtils.saveObject(context, today, Constant.wm_data);
            SaveUtils.saveObject(context, today, Constant.bt_wm_data);
            return true;
        }
        return false;
    }

    /**
     * 程序是否在运行，不分前后台
     * @return
     */
    public boolean isRunning(){
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(context.getPackageName()) &&
                    info.baseActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 程序是否在后台运行
     * @return
     */
    public  boolean isBackground() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 接单页面是否在栈顶
     * @return
     */
    private boolean isWmActivityTop() {
        ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(WaiMaiActivity.class.getName());
    }
    /**
     * 绑定美团页面是否在栈顶
     * @return
     */
    private boolean isBindMtActivityTop() {
        ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(BindMtActivity.class.getName());
    }

}
