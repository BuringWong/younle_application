package com.younle.younle624.myapplication.activity.manager.orderpager.entity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.manager.orderpager.BaseActivity;
import com.younle.younle624.myapplication.activity.manager.orderpager.ShowChartActivity;
import com.younle.younle624.myapplication.adapter.ordermanager.EntityOrderAdapter;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.domain.EntityOrderBean;
import com.younle.younle624.myapplication.domain.PosOrderKinds;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetworkUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.view.SelfLinearLayout;
import com.younle.younle624.myapplication.view.XListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.MD5;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class EntityEcActivity extends BaseActivity implements SelfLinearLayout.ClickToReload, XListView.IXListViewListener {

//    private TextView tv_sale;
    private TextView tv_finish;
    private View show_chart;
    private EntityOrderAdapter orderAdapter;
    private EntityOrderBean entityOrderBean;
    private String goodId = "";
    private boolean isLoadMore;
    private String TAG = "EntityEcActivity";
    /**
     * 分页加载中的页码
     */
    private int pagerIndex = 1;
    /**
     * 完成或核销，接口参数
     */
    private String sell_finish="0";
    private List<EntityOrderBean.ListBean> xListData;
    private List<EntityOrderBean.GoodsNameBean> leftSelectData;

    @Override
    public void getPrintData(String startTime, String endTime) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitle() {
        tv_title.setText("H5电商实物订单统计");
    }
    @Override
    public View initHeadView() {
        tv_right_select.setText("售出订单");
        tv_select_left.setText("全部商品");
        View headView = View.inflate(this, R.layout.service_order_header, null);
        show_chart = headView.findViewById(R.id.show_chart);
//        tv_sale = (TextView) headView.findViewById(R.id.tv_3);
        tv_finish = (TextView) headView.findViewById(R.id.tv_4);
        return headView;
    }
    @Override
    public void setListener() {
        show_chart.setOnClickListener(new ShowChart());
        //lv_for_filter.setOnItemClickListener(new SellFinishItemOnClickListener());
        xl_pos_order.setXListViewListener(this);
        ll_loading.setClickToReload(this);
        super.setListener();
    }

    /**
     * 售出和完成的item的点击监听
     */
    /*class SellFinishItemOnClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           if(close1) {//右侧打开
               sell_finish = rightSelectData.get(position).getId();
               tv_right_select.setText(rightSelectData.get(position).getName());
               Toast.makeText(EntityEcActivity.this, rightSelectData.get(position).getName(), Toast.LENGTH_SHORT).show();
               changeCurrentState(2);
           }else {
               LogUtils.Log("左侧打开状态");
               tv_select_left.setText(leftSelectData.get(position).getName());
               goodId=leftSelectData.get(position).getId();
               Toast.makeText(EntityEcActivity.this, leftSelectData.get(position).getName(), Toast.LENGTH_SHORT).show();
               changeCurrentState(1);
           }
            getDataFormNet();
        }
    }*/

    @Override
    public void ClickToReload() {
        initData();
    }

    /**
     * xlistview的下拉刷新，上拉加载更多
     */
    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void onLoadMore() {
        getMoreData();
    }

    @Override
    public void initData() {
        initRightSelectData();
        getDataFormNet();
    }

    @Override
    public void getChooseDataFromNet(String sizeId,String id, int isLeft,String goodsType,String payTool) {
        //点击的回调
        if(isLeft==0){//点击左侧
            LogUtils.Log("左侧打开状态");
            if(Integer.valueOf(id)>-1){
                tv_select_left.setText(leftSelectData.get(Integer.valueOf(id)).getName());
                goodId=leftSelectData.get(Integer.valueOf(id)).getId();
                Toast.makeText(EntityEcActivity.this, leftSelectData.get(Integer.valueOf(id)).getName(), Toast.LENGTH_SHORT).show();
                changeCurrentState(1);
            }
        }else if(isLeft==1){//点击右侧
            if(Integer.valueOf(id)>-1){
                sell_finish = rightSelectData.get(Integer.valueOf(id)).getId();
                tv_right_select.setText(rightSelectData.get(Integer.valueOf(id)).getName());
                Toast.makeText(EntityEcActivity.this, rightSelectData.get(Integer.valueOf(id)).getName(), Toast.LENGTH_SHORT).show();
                changeCurrentState(2);
            }
        }else{
            LogUtils.e(TAG, "getChooseDataFromNet 点击最最最右侧");
        }
        getDataFormNet();
    }

    /**
     *售出和完成
     */
    private void initRightSelectData() {
        rightSelectData =new ArrayList<>();
        rightSelectData.add(new PosOrderKinds("售出订单", "0"));
        rightSelectData.add(new PosOrderKinds("完成订单", "1"));
        setRightFilterData(rightSelectData, Constant.POS_ORDER_CHOOSE_ENTITY);
    }

    private void getDataFormNet() {
        reLoad();
        pagerIndex=1;
        boolean available = NetworkUtils.isAvailable(this);
        if(!available) {
            netError();
        }else {
            String currentTime = Utils.getCurrentTime();
            String token = Utils.getToken(currentTime, this);

            LogUtils.e(TAG, "Constant.USER_ACCOUNT=" + Constant.USER_ACCOUNT);
            LogUtils.e(TAG, "Constant.DEVICE_NAME=" + Constant.DEVICE_NAME);
            LogUtils.e(TAG, "Constant.DEVICE_MODEL=" + Constant.DEVICE_MODEL);
            LogUtils.e(TAG, "uid=" + Constant.ADV_ID);
            LogUtils.e(TAG, "page=" + pagerIndex);
            LogUtils.e(TAG, "goodsid=" + goodId);
            OkHttpUtils.post()
                    .url(UrlConstance.ENTITY_LIST)
                    .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                    .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                    .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                    .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                    .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                    .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                    .addParams("userkey", MD5.md5(MD5.md5(Constant.PASSWORD)))//密码
                    .addParams("uid", Constant.ADV_ID)
                    .addParams("select", Utils.getCurrentTime1())
                    .addParams("start",startTime+" "+startHour+":"+startMinute)
                    .addParams("end",endTime+" "+endHour+":"+endMinute)
                    .addParams("page", pagerIndex + "")
                    .addParams("goodsid", goodId + "")
                    .addParams("orderType",sell_finish)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            loadFailure("请稍后再试！");
                        }
                        @Override
                        public void onResponse(String response) {
                            LogUtils.e(TAG, "onResponse=" + response);
                            //加载视图消失
                            ll_loading.setVisibility(View.GONE);
                            boolean toNextStep = Utils.checkSaveToken(EntityEcActivity.this, response);
                            if(toNextStep){
                                parseJson(response);
                            }
                        }
                    });
        }
    }

    /**
     * 加载更多数据
     */
    private void getMoreData(){
        boolean available = NetworkUtils.isAvailable(this);
        if(!available) {
            netError();
        }else {

            String currentTime = Utils.getCurrentTime();
            String token = Utils.getToken(currentTime, this);

            LogUtils.e(TAG, "token=" + token + ",currentTime=" + currentTime);
            LogUtils.e(TAG, "Constant.USER_ACCOUNT=" + Constant.USER_ACCOUNT);
            LogUtils.e(TAG, "Constant.DEVICE_IMEI=" + Constant.DEVICE_IMEI);
            LogUtils.e(TAG, "Constant.DEVICE_NAME=" + Constant.DEVICE_NAME);
            LogUtils.e(TAG, "Constant.DEVICE_MODEL=" + Constant.DEVICE_MODEL);
            LogUtils.e(TAG, "uid=" + Constant.ADV_ID);
            LogUtils.e(TAG, "select=" + Utils.getCurrentTime1());
            LogUtils.e(TAG, "start=" + startTime);
            LogUtils.e(TAG, "end=" + endTime);
            LogUtils.e(TAG, "page=" + pagerIndex);
            LogUtils.e(TAG, "goodsid=" + goodId);
            OkHttpUtils.post()
                    .url(UrlConstance.ENTITY_LIST)
                    .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                    .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                    .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                    .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                    .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                    .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                    .addParams("userkey", MD5.md5(MD5.md5(Constant.PASSWORD)))//密码
                    .addParams("uid", Constant.ADV_ID)
                    .addParams("select", Utils.getCurrentTime1())
                    .addParams("start",startTime)
                    .addParams("end",endTime)
                    .addParams("page", (pagerIndex+1) + "")
                    .addParams("goodsid", goodId + "")
                    .addParams("orderType", sell_finish)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            loadFailure("请稍后再试！");
                        }

                        @Override
                        public void onResponse(String response) {
                            //加载视图消失
                            LogUtils.Log("加载更多的返回：" + response);
                            boolean toNextStep = Utils.checkSaveToken(EntityEcActivity.this, response);
                            if(toNextStep){
                                parseMoreJson(response);
                            }
                        }
                    });
        }
    }

    /**
     * 解析加载更多的数据
     * @param json
     */
    private void parseMoreJson(String json) {
        try {
            JSONObject jsonObject=new JSONObject(json);
            int code = jsonObject.getInt("code");
            String msg = jsonObject.getString("msg");
            LogUtils.Log("msg==" + msg);
            if(code==200) {
                Gson gson=new Gson();
                EntityOrderBean moreBean= gson.fromJson(jsonObject.getString("msg"), EntityOrderBean.class);
                if(moreBean.getList()!=null&&moreBean.getList().size()>0) {
                    pagerIndex++;
                    xListData.addAll(moreBean.getList());
                    xl_pos_order.stopLoadMore();
                    orderAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(EntityEcActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                    xl_pos_order.noMoreData();
                    xl_pos_order.stopLoadMore();
                }
            }else {
                xl_pos_order.noMoreData();
                xl_pos_order.stopLoadMore();
                Toast.makeText(EntityEcActivity.this, "当前网络异常", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

/*    *//**
     * 加载更多数据
     *//*
    private void getMoreData(){
        isLoadMore=true;
        boolean available = NetworkUtils.isAvailable(this);
        if(!available) {
            netError();
        }else {
            //获取时间戳 1473410972494并截取前10位
            String currentTime = Utils.getCurrentTime().substring(0,10);
            //获取token
            String token = Utils.getToken(currentTime);
            LogUtils.e(TAG,"token="+token+",currentTime="+currentTime);
            LogUtils.e(TAG,"Constant.USER_ACCOUNT="+Constant.USER_ACCOUNT);
            LogUtils.e(TAG,"Constant.DEVICE_IMEI="+Constant.DEVICE_IMEI);
            LogUtils.e(TAG,"Constant.DEVICE_NAME="+Constant.DEVICE_NAME);
            LogUtils.e(TAG,"Constant.DEVICE_MODEL="+Constant.DEVICE_MODEL);
            LogUtils.e(TAG,"uid="+Constant.ADV_ID);
            LogUtils.e(TAG,"select="+Utils.getCurrentTime());
            LogUtils.e(TAG,"start="+startTime);
            LogUtils.e(TAG,"end="+endTime);
            LogUtils.e(TAG,"page="+pagerIndex);
            LogUtils.e(TAG,"goodsid="+goodId);

            OkHttpUtils.post()
                    .url(UrlConstance.ENTITY_LIST)
                    .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                    .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                    .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                    .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                    .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                    .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                    .addParams("userkey", MD5.md5(MD5.md5(Constant.PASSWORD)))//密码
                    .addParams("uid", Constant.ADV_ID)
                    .addParams("select", Utils.getCurrentTime())
                    .addParams("start",startTime)
                    .addParams("end",endTime)
                    .addParams("page", (pagerIndex+1) + "")
                    .addParams("goodsid", goodId + "")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            loadFailure("请稍后再试！");
                        }

                        @Override
                        public void onResponse(String response) {
                            //加载视图消失
                            ll_loading.setVisibility(View.GONE);
                            LogUtils.e(TAG, "onResponse=" + response);
                            boolean toNextStep = Utils.checkSaveToken(EntityEcActivity.this, response);
                            if(toNextStep){
                                parseJson(response);
                            }
                        }
                    });
        }
    }*/

    private void parseJson(String json) {
        try {
            JSONObject jsonObject=new JSONObject(json);
            int code = jsonObject.getInt("code");
            String msg = jsonObject.getString("msg");
            LogUtils.Log("msg==" + msg);
            if(code==200) {
                Gson gson=new Gson();
                entityOrderBean = gson.fromJson(jsonObject.getString("msg"), EntityOrderBean.class);
                xListData = entityOrderBean.getList();
                showData();

                //设置顶部筛选左侧数据
                leftSelectData = entityOrderBean.getGoodsName();
                if(leftSelectData!=null&&leftSelectData.size()>0){
                    setLeftFilterData(leftSelectData, Constant.POS_ORDER_CHOOSE_ENTITY);
                }else{
                    LogUtils.e(TAG, "左侧没有得到数据不填充...");
                }
            }else {
                ll_nodata.setVisibility(View.VISIBLE);
                xl_pos_order.setVisibility(View.GONE);
                tv_date_exception.setText(R.string.nodata);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析json
     *
     * @param json
    private void parseJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                Gson gson = new Gson();
                if(isLoadMore) {
                    pagerIndex++;
                    EntityOrderBean entityOrderBeanMore = gson.fromJson(json, EntityOrderBean.class);
                    fromPos = entityOrderBean.getMsg().getList().size();
                    entityOrderBean.getMsg().setList(entityOrderBeanMore.getMsg().getList());
                    if(entityOrderBeanMore.getMsg().getList().size()==0) {
                        Toast.makeText(EntityEcActivity.this, getString(R.string.nodata_after_request), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    entityOrderBean = gson.fromJson(json, EntityOrderBean.class);
                }
                xl_pos_order.stopLoadMore();
                xl_pos_order.setVisibility(View.VISIBLE);
                showData();
            } else {
                if(isLoadMore) {
                    isLoadMore = false;
                    xl_pos_order.stopLoadMore();
                    return;
                }
                ll_nodata.setVisibility(View.VISIBLE);
                noData=true;
                xl_pos_order.setVisibility(View.GONE);
                tv_date_exception.setText(R.string.nodata);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 展示数据
     */
    private void showData() {
        if(xListData.size()<=0) {//没有数据
            show_chart.setVisibility(View.GONE);
            ll_nodata.setVisibility(View.VISIBLE);
            xl_pos_order.setVisibility(View.GONE);
            tv_date_exception.setText(R.string.nodata);
        }else {//headview的数据填充
            show_chart.setVisibility(View.VISIBLE);
            xl_pos_order.setVisibility(View.VISIBLE);
            ll_nodata.setVisibility(View.GONE);
            inflateHeaderData();
            orderAdapter =new EntityOrderAdapter(this);
            orderAdapter.setData(xListData);
            xl_pos_order.setAdapter(orderAdapter);
        }

        //数据不足，隐藏加载更多
        if(xListData.size()<10) {
            xl_pos_order.setPullLoadEnable(false);
        }else {
            xl_pos_order.setPullLoadEnable(true);
        }
    }

    /**
     * 装载头部数据
     */
    private void inflateHeaderData() {
        switch (sell_finish) {
            case "0"://售出订单
                tv_finish.setText("售出订单:" + entityOrderBean.getSaled() + "  预期收入:" + entityOrderBean.getIncome());
                break;
            case "1"://完成订单
                tv_finish.setText("完成订单:" + entityOrderBean.getSaled() + "  实际收入:" + entityOrderBean.getIncome());
                break;
        }
    }
    private class ShowChart implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.show_chart://本月总计
                    String time = tv_title_date.getText().toString();
                    String s_time=time.substring(0,time.indexOf("至"));
                    String e_time=time.substring(time.indexOf("至")+1,time.length());
                    Intent intent = new Intent(EntityEcActivity.this, ShowChartActivity.class);
                    intent.putExtra("from", Constant.ENTITY_DATA);
                    intent.putExtra(Constant.START_DATE,s_time);
                    intent.putExtra(Constant.END_DATE,e_time);
                    intent.putExtra(Constant.GOOD_ID,goodId);
                    intent.putExtra("left_title",tv_select_left.getText());
                    intent.putExtra("right_selected",sell_finish);
                    startActivity(intent);
                    break;
            }
        }
    }

    /**
     * 获取筛选的数据
     */
    /*private void initFilterData() {
        List<EntityOrderBean.GoodsNameBean> goodsNameBeans = entityOrderBean.getGoodsName();
        if(goodsNameBeans !=null&& goodsNameBeans.size()>0) {
            filterAdapter=new FilterAdapter(this);
            filterAdapter.setData(goodsNameBeans);
            filterAdapter.setDataFrom(Constant.POS_ORDER_CHOOSE_ENTITY);
            lv_all_store.setAdapter(filterAdapter);
        }
    }*/
}
