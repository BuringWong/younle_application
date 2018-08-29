package com.younle.younle624.myapplication.utils.wm;

import android.content.Context;

import com.google.gson.Gson;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.domain.waimai.ElmOrderBean;
import com.younle.younle624.myapplication.domain.waimai.MtOrderDetail;
import com.younle.younle624.myapplication.domain.waimai.NewElmOrderBean;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.SaveUtils;
import com.younle.younle624.myapplication.utils.SpUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/5/22.
 * 处理外卖订单
 * 流程：
 * 1.判断，接单开关是否开启，是否是新的一天
 * 2.如果是新的一天，创建新的数组，并添加数据，同时删除之前的数据
 * 3.如果不是新的一天，解析数据，判断是否已添加至数组，如果已经添加不予处理
 * 4.保存数据，发送到打印页面做最后处理
 */

public  class WmHandleUtils {
    private static WmHandleUtils instance;
    private Context context;

    public WmHandleUtils(Context context) {
        this.context=context;
    }

    public static WmHandleUtils getInstance(Context context) {
        if(instance==null) {
            instance=new WmHandleUtils(context);
        }
        return instance;
    }

    public void handleOrder( String json) {
        boolean is_jiedan_open = SpUtils.getInstance(context).getBoolean(Constant.IS_JIEDAN_OPEN, false);
        LogUtils.WmLog("WmHandleUtils开始处理外卖订单  接单状态"+is_jiedan_open);
        if(is_jiedan_open) {
            parser(json);
        }

    }
    /**
     * 解析外卖订单的json
     * @param json
     */
    public  void parser(String json){
        try {
            JSONObject jsonObject=new JSONObject(json);
            JSONObject key = jsonObject.getJSONObject("key");
            int fromplat = key.getInt("fromplat");
            int order_type = key.getInt("indirect_push");
            if(1==order_type) {//超限订单
                String restaurant="";
                String orderId="";
                if(fromplat==1) {
                    restaurant = key.getString("restaurant");
                    long redirect_push = key.getLong("redirect_push");
                    orderId=redirect_push+"";
                }else if(fromplat==2) {
                    long elmStoreId = key.getLong("restaurant");
                    restaurant=elmStoreId+"";
                    orderId = key.getString("redirect_push");
                }else if(fromplat==21) {
                    long newElmStoreId = key.getLong("restaurant");
                    restaurant=newElmStoreId+"";
                    orderId = key.getString("redirect_push");
                }
                LogUtils.WmLog("超限订单:fromplat=="+fromplat+" orderId=="+orderId+"  restaurant=="+restaurant);
                reGetOrderInfo(fromplat,orderId,restaurant);
            }else {
                handleNormalOrder(json, key, fromplat);
            }
        } catch (JSONException e) {
            LogUtils.Log("解析错误：" + e.toString());
            e.printStackTrace();
        }
    }
    private void reGetOrderInfo(final int type, final String orderid, final String restaurant) {
        LogUtils.Log("重新请求订单  type=="+type);
        LogUtils.Log("重新请求订单  orderid=="+orderid);
        LogUtils.Log("重新请求订单  restaurant=="+restaurant);
        String url="";
        if (type==21){
            url= UrlConstance.REGET_NEWELM_INFO;
        }else {
            url=UrlConstance.REGET_ORDER_INFO;
        }
        LogUtils.Log("重新请求订单  url=="+url);
        OkHttpUtils.post()
                .url(url)
                .addParams("type", type + "")
                .addParams("orderid", orderid)
                .addParams("restaurant", restaurant)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                reGetOrderInfo(type,orderid,restaurant);
                LogUtils.WmLog("重新请求订单：onerror()==" + e.toString());
            }

            @Override
            public void onResponse(String response) {
                LogUtils.WmLog("重新请求订单：onResponse()==" + response.toString());
                handleOrder(response);
            }
        });
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
                break;
            case 2://饿了么
                ElmOrderBean elmOrderBean = gson.fromJson(key.toString(), ElmOrderBean.class);
                addToList(null,elmOrderBean,null);
                break;
            case 21:
                NewElmOrderBean newElmOrderBean = gson.fromJson(key.toString(), NewElmOrderBean.class);
                addToList(null,null,newElmOrderBean);
                break;
            case 3://百度

                break;
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
            SaveUtils.deleLog();

            SpUtils.getInstance(context).save(Constant.PARA_PRE_SIZE, 0);
            SpUtils.getInstance(context).save(Constant.BT_PARA_PRE_SIZE, 0);
            SaveUtils.saveObject(context, today, Constant.wm_data);
            SaveUtils.saveObject(context, today, Constant.bt_wm_data);
            return true;
        }
        return false;
    }
    /**
     * 将接到的订单添加到数组中
     * @param mtOrderBean
     * @param elmOrderBean
     */
    private void addToList(MtOrderDetail mtOrderBean,ElmOrderBean elmOrderBean,NewElmOrderBean newElmOrderBean) {
        if(mtOrderBean!=null) {
            if(!booleanContains(mtOrderBean.getKey().getOrderId() + "","MT")) {
                LogUtils.WmLog("已有订单不包含该订单");
                Map map=new HashMap();
                map.put("bean", mtOrderBean);
                map.put("type", new String("MT"));
                map.put("print", false);
                Constant.wm_data.add(0, map);
                Constant.bt_wm_data.add(0, map);
                EventBus.getDefault().post(mtOrderBean);
            }
        }
        if(elmOrderBean!=null) {
            if(!booleanContains(elmOrderBean.getData().getOrder_id(),"ELM")) {
                LogUtils.WmLog("已有订单不包含该订单");
                Map map=new HashMap();
                map.put("bean", elmOrderBean);
                map.put("type", new String("ELM"));
                map.put("print", false);
                Constant.wm_data.add(0, map);
                Constant.bt_wm_data.add(0, map);
                EventBus.getDefault().post(elmOrderBean);
            }
        }

        if(newElmOrderBean!=null) {
            if(!booleanContains(newElmOrderBean.getMessage().getId(),"NEW_ELM")) {
                LogUtils.WmLog("已有订单不包含该订单");
                Map map=new HashMap();
                map.put("bean", newElmOrderBean);
                map.put("type", new String("NEW_ELM"));
                map.put("print", false);
                Constant.wm_data.add(0, map);
                Constant.bt_wm_data.add(0, map);
                EventBus.getDefault().post(newElmOrderBean);
            }
        }
//        SaveUtils.saveObject(context,Utils.getToday(),Constant.wm_data);
    }

    private boolean booleanContains(String id,String orderType) {
        LogUtils.WmLog("保存的订单："+Constant.wm_data.size());
        for (int i = 0; i < Constant.wm_data.size(); i++) {
            Map map = Constant.wm_data.get(i);
            String type= (String) map.get("type");
            Object bean = map.get("bean");
            /*if(!type.equals(orderType)) {
                continue;
            }*/

            if("MT".equals(type)) {
                LogUtils.WmLog("保存的订单：" + ((MtOrderDetail) bean).getKey().getOrderId() + " 新的订单：" + id);
                if(id.equals(((MtOrderDetail) bean).getKey().getOrderId()+"")) {
                    return true;
                }
            }
            if("NEW_ELM".equals(type)) {
                if(id.equals(((NewElmOrderBean) bean).getMessage().getId())) {
                    return true;
                }
            }
            if("ELM".equals(type)) {
                if(id.equals(((ElmOrderBean) bean).getData().getOrder_id())) {
                    return true;
                }
            }

        }
        return false;
    }

}
