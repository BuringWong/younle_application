package com.younle.younle624.myapplication.activity.manager.orderpager.service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.manager.orderpager.BaseActivity;
import com.younle.younle624.myapplication.activity.manager.orderpager.ShowChartActivity;
import com.younle.younle624.myapplication.adapter.ordermanager.ServiceOrderAdapter;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.domain.PosOrderKinds;
import com.younle.younle624.myapplication.domain.ServerOrderBean;
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

public class ServiceEcActivity extends BaseActivity implements View.OnClickListener, SelfLinearLayout.ClickToReload, XListView.IXListViewListener {

    private View show_chart;
    private TextView tv_hexiao;
    private ServerOrderBean serverOrderBean;
    private String goodId = "0";
    private ServiceOrderAdapter orderAdapter;
    private String TAG = "ServiceEcActivity";
    /**
     * 分页加载中的页码
     */
    private int pagerIndex = 1;
    /**
     * 完成或核销，接口参数
     */
    private String sell_finish="0";
    private List<ServerOrderBean.MsgBean.ListBean> xListData;
    private List<ServerOrderBean.MsgBean.GoodsNameBean> leftSelectData;

    @Override
    public void getPrintData(String startTime, String endTime) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitle() {
        tv_title.setText("H5电商服务订单统计");
    }

    @Override
    public View initHeadView() {
        tv_right_select.setText("售出订单");
        tv_select_left.setText("全部商品");
        View headView = View.inflate(this, R.layout.service_order_header, null);
        show_chart = headView.findViewById(R.id.show_chart);
        tv_hexiao = (TextView) show_chart.findViewById(R.id.tv_4);
        return headView;
    }

    @Override
    public void setListener() {
        show_chart.setOnClickListener(new MothDayOnClickListener());
        //lv_for_filter.setOnItemClickListener(new SellFinishItemOnClickListener());
        ll_loading.setClickToReload(this);
        xl_pos_order.setXListViewListener(this);
        super.setListener();
    }

    @Override
    public void initData() {
        pagerIndex = 1;
        initRightSelectData();
        getDataFromNet();
    }

    @Override
    public void getChooseDataFromNet(String sizeId,String id, int isLeft,String goodsType,String payTool) {
        LogUtils.e(TAG, "getChooseDataFromNet() id=" + id + ",isLeft=" + isLeft);
        if(isLeft==0){//点击左侧
            LogUtils.e(TAG, "getChooseDataFromNet() 点击左侧");
            if(Integer.valueOf(goodsType)>-1){
                goodId=leftSelectData.get(Integer.valueOf(id)).getId();
                tv_select_left.setText(leftSelectData.get(Integer.valueOf(id)).getName());
                Toast.makeText(ServiceEcActivity.this, leftSelectData.get(Integer.valueOf(id)).getName(), Toast.LENGTH_SHORT).show();
                changeCurrentState(1);
            }
        }else if(isLeft==1){//点击右侧
            LogUtils.e(TAG, "getChooseDataFromNet() 点击右侧");
            if(Integer.valueOf(goodsType)>-1){
                sell_finish = rightSelectData.get(Integer.valueOf(id)).getId();
                tv_right_select.setText(rightSelectData.get(Integer.valueOf(id)).getName());
                Toast.makeText(ServiceEcActivity.this, rightSelectData.get(Integer.valueOf(id)).getName(), Toast.LENGTH_SHORT).show();
                changeCurrentState(2);
            }
        }else{
            LogUtils.e(TAG, "getChooseDataFromNet() 点击最最最右侧");
        }
        getDataFromNet();
    }

    /**
     *售出和核销
     */
    private void initRightSelectData() {
        rightSelectData =new ArrayList<>();
        rightSelectData.add(new PosOrderKinds("售出订单", "0"));
        rightSelectData.add(new PosOrderKinds("核销订单", "1"));
        setRightFilterData(rightSelectData, Constant.KINDS_CHOOSE_RIGHT);
    }

    /**
     * 联网获取数据
     */
    private void getDataFromNet() {
        reLoad();
        pagerIndex=1;
        boolean available = NetworkUtils.isAvailable(this);
        if(!available) {
            netError();
        }else{
            String currentTime = Utils.getCurrentTime();
            String token = Utils.getToken(currentTime, this);

            LogUtils.e(TAG, "Constant.ADV_ID=" + Constant.ADV_ID);
            LogUtils.e(TAG, "startTime=" + startTime);
            LogUtils.e(TAG, "endTime=" + endTime);
            LogUtils.e(TAG, "pagerIndex=" + pagerIndex);
            LogUtils.e(TAG, "goodId=" + goodId);
            OkHttpUtils
                    .post()
                    .url(UrlConstance.SERVER_LIST)
                    .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                    .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                    .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                    .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                    .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                    .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                    .addParams(Constant.PARAMS_NEME_STORE_ID, Constant.STORE_ID)
                    .addParams("userkey", MD5.md5(MD5.md5(Constant.PASSWORD)))//密码
                    .addParams("uid", Constant.ADV_ID)//Constant.ADV_ID
                    .addParams("start",startTime+" "+startHour+":"+startMinute)
                    .addParams("end",endTime+" "+endHour+":"+endMinute)
                    .addParams("page", pagerIndex + "")
                    .addParams("goodsid", goodId + "")
                    .addParams("orderType",sell_finish)//售出和核销
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            loadFailure("请稍后再试！");
                        }
                        @Override
                        public void onResponse(String response) {
                            ll_loading.setVisibility(View.GONE);
                            LogUtils.Log("response:" + response + "结束");
                            boolean toNextStep = Utils.checkSaveToken(ServiceEcActivity.this, response);
                            if (toNextStep){
                                parseJson(response);
                            }
                        }
                    });
        }
    }

    /**
     * 解析json
     *
     * @param json
     */
    private void parseJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            Gson gson = new Gson();
            if (code == 200) {
                serverOrderBean = gson.fromJson(json, ServerOrderBean.class);
                xListData = serverOrderBean.getMsg().getList();
                showData();
                leftSelectData = serverOrderBean.getMsg().getGoodsName();
                LogUtils.e(TAG, "leftSelectData.size()==");
                if(leftSelectData!=null&&leftSelectData.size()>0){
                    LogUtils.e(TAG, "leftSelectData.size()=" + leftSelectData.size());
                    setLeftFilterData(leftSelectData, Constant.POS_ORDER_CHOOSE_SERVICE);
                }
            } else {
                ll_nodata.setVisibility(View.VISIBLE);
                xl_pos_order.setVisibility(View.GONE);
                tv_date_exception.setText(R.string.nodata);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载更多获取数据
     */
    public void getMoreData() {
        boolean available = NetworkUtils.isAvailable(this);
        if(!available) {
            netError();
        }else{
            String currentTime = Utils.getCurrentTime();
            String token = Utils.getToken(currentTime, this);
            OkHttpUtils
                    .post()
                    .url(UrlConstance.SERVER_LIST)
                    .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                    .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                    .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                    .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                    .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                    .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                    .addParams("userkey", MD5.md5(MD5.md5(Constant.PASSWORD)))//密码
                    .addParams(Constant.PARAMS_NEME_STORE_ID, Constant.STORE_ID)
                    .addParams("uid", Constant.ADV_ID)//Constant.ADV_ID
                    .addParams("start", startTime)
                    .addParams("end", endTime)
                    .addParams("page", (pagerIndex+1)+"")
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
                            LogUtils.Log("response:" + response);
                            boolean toNextStep = Utils.checkSaveToken(ServiceEcActivity.this, response);
                            if (toNextStep){
                                praseMoreJson(response);
                            }
                        }
                    });
        }
    }

    /**
     * 解析加载更多的数据
     * @param json
     */
    private void praseMoreJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                Gson gson = new Gson();
                ServerOrderBean moreBean = gson.fromJson(json, ServerOrderBean.class);
                if(moreBean.getMsg().getList().size()==0) {
                   Toast.makeText(ServiceEcActivity.this, "没有更多的数据！", Toast.LENGTH_SHORT).show();
                   xl_pos_order.noMoreData();
                   xl_pos_order.stopLoadMore();
                }else {
                    pagerIndex++;
                    xListData.addAll(moreBean.getMsg().getList());
                    xl_pos_order.stopLoadMore();
                    orderAdapter.notifyDataSetChanged();
                }
            } else {
                xl_pos_order.noMoreData();
                xl_pos_order.stopLoadMore();
                ll_loading.setVisibility(View.GONE);
                Toast.makeText(ServiceEcActivity.this, getString(R.string.nodata_after_request), Toast.LENGTH_SHORT).show();
                xl_pos_order.setVisibility(View.GONE);
                tv_date_exception.setText(R.string.nodata);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 展示数据
     */
    private void showData() {
        if(xListData==null||xListData.size()<=0) {
            show_chart.setVisibility(View.GONE);
            ll_nodata.setVisibility(View.VISIBLE);
            xl_pos_order.setVisibility(View.GONE);
            tv_date_exception.setText(R.string.nodata);
        }else {
            xl_pos_order.setVisibility(View.VISIBLE);
            show_chart.setVisibility(View.VISIBLE);
            ll_nodata.setVisibility(View.GONE);
            inflateHeaderData();
            orderAdapter = new ServiceOrderAdapter(this);
            orderAdapter.setData(xListData);
            xl_pos_order.setAdapter(orderAdapter);
        }
        if(xListData.size()<4) {
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
                tv_hexiao.setText("售出代金券:" + serverOrderBean.getMsg().getSaled() + "  预期收入:" + serverOrderBean.getMsg().getIncome());
                break;
            case "1"://核销订单
                tv_hexiao.setText("核销代金券:" + serverOrderBean.getMsg().getSaled() + "  实际收入:" + serverOrderBean.getMsg().getIncome());
                break;
        }
    }

    /**
     * xlistview的下拉刷新，
     */
    @Override
    public void onRefresh() {
        initData();//下拉刷新，什么参数都不做改变，直接重新请求
    }

    /**
     * xlistview上拉加载更多
     */
    @Override
    public void onLoadMore() {
        getMoreData();
    }

    /**
     * 点击查看图表的监听
     */
    private class MothDayOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.show_chart://本月总计
                    String time = tv_title_date.getText().toString();
                    String s_time=time.substring(0,time.indexOf("至"));
                    String e_time=time.substring(time.indexOf("至")+1,time.length());
                    Intent intent = new Intent(ServiceEcActivity.this, ShowChartActivity.class);
                    intent.putExtra("from", Constant.SERVICE_DATA);
                    intent.putExtra(Constant.START_DATE,s_time);
                    intent.putExtra(Constant.END_DATE,e_time);
                    intent.putExtra(Constant.GOOD_ID,goodId);
                    intent.putExtra("right_selected",sell_finish);//售出和完成
                    intent.putExtra("left_title",tv_select_left.getText());//左侧筛选的title仅文本
                    startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void ClickToReload() {
        initData();
    }
}