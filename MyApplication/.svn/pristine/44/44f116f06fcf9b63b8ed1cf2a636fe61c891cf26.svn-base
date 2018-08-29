package com.younle.younle624.myapplication.utils.wm;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.AppLetReceiveBean;
import com.younle.younle624.myapplication.domain.SaveAppletOrderNoBean;
import com.younle.younle624.myapplication.myservice.PrintService;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.SpUtils;
import com.younle.younle624.myapplication.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by bert_dong on 2017/6/13 0013.
 * 邮箱：18701038771@163.com
 */
public class AppletHandleUtils {

    private static final String TAG = "AppletHandleUtils";
    private static AppletHandleUtils instance;
    private Context context;
    public AppletHandleUtils(Context context) {
        this.context = context;
    }
    public static AppletHandleUtils getInstance(Context context){
        if(instance == null){
            instance = new AppletHandleUtils(context);
        }
        return instance;
    }

    public void handleOrder(String json){
        LogUtils.e(TAG,"handleOrder json="+json);
        Gson gson = new Gson();
        synchronized (this) {
            try {
                AppLetReceiveBean appLetReceiveBean = gson.fromJson(json, AppLetReceiveBean.class);
                String isOutOfLimit = appLetReceiveBean.getIsOutOfLimit();
                if ("0".equals(isOutOfLimit)) {//非超限订单
                    LogUtils.e(TAG, "handleOrder 非超限订单 order_no="+appLetReceiveBean.getOrderNo());
                    boolean isAdd = true;//ture的时候是未添加到内存或者已经打印过
                    for (int i = 0; i < Constant.applet_data.size(); i++) {
                        //判断接收到的订单是否已经在内存中了
                        LogUtils.e(TAG,"Constant.applet_data.get(i).getOrderNo()="+Constant.applet_data.get(i).getOrderNo());
                        if (appLetReceiveBean.getOrderNo().equals(Constant.applet_data.get(i).getOrderNo())) {
                            LogUtils.e(TAG,"appLetReceiveBean.getOrderNo()="+appLetReceiveBean.getOrderNo());
                            LogUtils.e(TAG,"saveData.get(j).getOrderNo()="+Constant.applet_data.get(i).getOrderNo());
                            isAdd = false;
                            break;
                        }
                    }
                    LogUtils.e(TAG, "handleOrder 已支付1 isAdd="+isAdd);
                    String has_print_applet = SpUtils.getInstance(context).getString("has_print_applet", "");
                    if(isAdd){//比对已经打印的订单号
                        if(!"".equals(has_print_applet)){//还没有存储最近24小时的订单
                            try {
                                SaveAppletOrderNoBean saveAppletOrderNoBean = gson.fromJson(has_print_applet, SaveAppletOrderNoBean.class);
                                List<SaveAppletOrderNoBean.MsgBean.SaveDataBean> saveData = saveAppletOrderNoBean.getMsg().getSaveData();
                                if(saveData!=null&&saveData.size()>0){
                                    for(int j=0;j<saveData.size();j++){
                                        LogUtils.e(TAG,"saveData.get(j).getOrderNo()="+saveData.get(j).getOrderNo());
                                        if(appLetReceiveBean.getOrderNo().equals(saveData.get(j).getOrderNo())){
                                            LogUtils.e(TAG,"appLetReceiveBean.getOrderNo()="+appLetReceiveBean.getOrderNo());
                                            LogUtils.e(TAG,"saveData.get(j).getOrderNo()="+saveData.get(j).getOrderNo());
                                            isAdd = false;
                                            break;
                                        }
                                    }
                                }
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    LogUtils.e(TAG, "handleOrder 已支付2 isAdd="+isAdd);
                    if (isAdd) {
                        String printItems = SpUtils.getInstance(context).getString(Constant.print_permission, "");
                        if(printItems.contains("8")||printItems.contains("9")){
                            Constant.applet_data.add(appLetReceiveBean);
                        }else{
                            if(!"".equals(has_print_applet)){
                                LogUtils.e(TAG,"saveLocalAndDelete() 存储的有已经打印的订单...");
                                SaveAppletOrderNoBean saveAppletOrderNoBean = gson.fromJson(has_print_applet, SaveAppletOrderNoBean.class);
                                List<SaveAppletOrderNoBean.MsgBean.SaveDataBean> saveData = saveAppletOrderNoBean.getMsg().getSaveData();
                                Iterator it = saveData.iterator();
                                while(it.hasNext()){
                                    SaveAppletOrderNoBean.MsgBean.SaveDataBean next = (SaveAppletOrderNoBean.MsgBean.SaveDataBean) it.next();
                                    if(Utils.getCurrentTimeMill()-next.getTime() > 86400000){
                                        it.remove();
                                    }
                                }
                                SaveAppletOrderNoBean.MsgBean.SaveDataBean saveBean = new SaveAppletOrderNoBean.MsgBean.SaveDataBean
                                        (appLetReceiveBean.getOrderNo(),Utils.getCurrentTimeMill(),appLetReceiveBean.getUnionid());
                                saveAppletOrderNoBean.getMsg().getSaveData().add(saveBean);
                                SpUtils.getInstance(context).save("has_print_applet",gson.toJson(saveAppletOrderNoBean));
                            }else{
                                LogUtils.e(TAG,"saveLocalAndDelete() 未存储已打印的订单...");
                                SaveAppletOrderNoBean saveAppletOrderNoBean = new SaveAppletOrderNoBean();
                                saveAppletOrderNoBean.setMsg(new SaveAppletOrderNoBean.MsgBean());
                                SaveAppletOrderNoBean.MsgBean.SaveDataBean saveBean = new SaveAppletOrderNoBean.MsgBean.SaveDataBean(appLetReceiveBean.getOrderNo(),Utils.getCurrentTimeMill(),appLetReceiveBean.getUnionid());
                                List<SaveAppletOrderNoBean.MsgBean.SaveDataBean> list = new ArrayList<>();
                                list.add(saveBean);
                                saveAppletOrderNoBean.getMsg().setSaveData(list);
                                SpUtils.getInstance(context).save("has_print_applet",gson.toJson(saveAppletOrderNoBean));
                            }
                        }
                        String btprintItems = SpUtils.getInstance(context).getString(Constant.bt_print_permission, "");
                        if(btprintItems.contains("8")||btprintItems.contains("9")){
                            Constant.bt_applet_data.add(appLetReceiveBean);
                        }
                    }
                    if (isAdd) {
                        //判断打印服务是否开启：
                        if (Utils.isServiceWork(context, "com.younle.younle624.myapplication.myservice.PrintService")) {
                            EventBus.getDefault().post(appLetReceiveBean);
                        } else {
                            //服务没有开启：开启服务
                            context.startService(new Intent(context, PrintService.class));
                            EventBus.getDefault().post(appLetReceiveBean);
                        }
                    }
                } else {
                    LogUtils.e(TAG, "handleOrder 超限订单");
                    //超限订单:请求接口得到数据添加到小程序订单数据中
                    Utils.getAppletDataFromNet(appLetReceiveBean, context);
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
    }
}
