package com.younle.younle624.myapplication.activity.manager.messagecenter;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.manager.orderpager.BillDetailActivity;
import com.younle.younle624.myapplication.adapter.AppletListAdapter;
import com.younle.younle624.myapplication.adapter.SenderAdapter;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.domain.MsCenterOrderListBean;
import com.younle.younle624.myapplication.domain.Sender;
import com.younle.younle624.myapplication.selfinterface.WxAppDetailAndSendListener;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.utils.notice.NoticePopuUtils;
import com.younle.younle624.myapplication.view.SelfLinearLayout;
import com.younle.younle624.myapplication.view.XListView;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;


/**
 * 小程序订单通知 activity
 */
public class MsCenterActivity extends Activity implements View.OnClickListener, XListView.IXListViewListener, SelfLinearLayout.ClickToReload {

    private static final String TAG = "MsCenterActivity";
    public ImageView iv_jiazai_filure;
    public TextView tv_loading;
    public ProgressBar pb_loading;
    public SelfLinearLayout ll_loading;
    private TextView tv_title;
    private AutoLinearLayout ll_cancel;
    private TextView tv_cancel;
    private EditText et_order_search;
    private XListView lv_applet_order;
    private TextView tv_create_new_order;
    private TextView tv_no_applet_order;
    private boolean isRefresh = false;
    private boolean isSearch = false;
    private int pageNum = 1;
    private String remark = "";
    private MsCenterOrderListBean msCenterOrderListBean;
    private AppletListAdapter appletListAdapter;
    private TextView stop;
    private View line_which_display_selforder;
    private View line_which_display_communityorder;
    private RadioButton rb_statistics_by_selforder;
    private RadioButton rb_statistics_by_communityorder;
    private NetWorks netWorks;
    private PopupWindow detailPup;

    /*
    * 订单类型 0 已结算的自助点单订单 2 未结算的自助点单分订单 1 社区门店小程序订单
    */
    private int ordertype=0;
    private Sender sender;
    private Sender.DataBean selectedSender;//选中的配送员
    private boolean isworking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ms_center);
        EventBus.getDefault().register(this);
        netWorks = new NetWorks(this);

        initView();
        if(Constant.OPENED_PERMISSIONS.contains("9")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getSender();
                }
            },2000);
        }
        initData();
        setListener();
    }

    /**
     * 获取配送员列表
     */
    private void getSender() {
        Map<String, String> params = netWorks.getPublicParams();
        params.put("storeid",Constant.STORE_ID);
        netWorks.Request(UrlConstance.GET_STORE_SENDER, params, 5000, 0, new NetWorks.OnNetCallBack() {

            @Override
            public void onError(Exception e, int flag) {
                Utils.showToast(MsCenterActivity.this,"网络异常，获取配送员信息失败",1000);

            }

            @Override
            public void onResonse(String response, int flag) {
                LogUtils.e(TAG,"获取配送员："+response.toString());
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if(code==200) {
                        Gson gson=new Gson();
                        sender = gson.fromJson(response, Sender.class);
                    }else {
                        String msg = jsonObject.getString("msg");
                        Utils.showToast(MsCenterActivity.this,msg,1000);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView() {
        line_which_display_selforder = findViewById(R.id.line_which_display_selforder);
        line_which_display_communityorder = findViewById(R.id.line_which_display_communityorder);
        rb_statistics_by_selforder = (RadioButton)findViewById(R.id.rb_statistics_by_selforder);
        rb_statistics_by_communityorder = (RadioButton)findViewById(R.id.rb_statistics_by_communityorder);
        rb_statistics_by_selforder.setOnClickListener(this);
        rb_statistics_by_communityorder.setOnClickListener(this);
        RadioGroup rg_good_sv= (RadioGroup) findViewById(R.id.rg_good_sv);
        LinearLayout ll_tapline = (LinearLayout) findViewById(R.id.ll_tapline);
        stop = (TextView)findViewById(R.id.stop);

        if(Constant.OPENED_PERMISSIONS.contains("8")&&Constant.OPENED_PERMISSIONS.contains("9")) {
            rg_good_sv.setVisibility(View.VISIBLE);
            ll_tapline.setVisibility(View.VISIBLE);
            ordertype=0;
        }else {
            rg_good_sv.setVisibility(View.GONE);
            ll_tapline.setVisibility(View.GONE);
            if(Constant.OPENED_PERMISSIONS.contains("8")) {
                ordertype=0;
                stop.setVisibility(View.GONE);

            }else if (Constant.OPENED_PERMISSIONS.contains("9")){
                stop.setVisibility(View.VISIBLE);
                ordertype=1;
            }
        }
        iv_jiazai_filure = (ImageView) findViewById(R.id.iv_jiazai_filure);
        tv_loading = (TextView) findViewById(R.id.tv_loading);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
        ll_loading = (SelfLinearLayout) findViewById(R.id.ll_loading);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("小程序订单通知");
        ll_cancel = (AutoLinearLayout) findViewById(R.id.ll_cancel);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_cancel.setVisibility(View.GONE);
        lv_applet_order = (XListView) findViewById(R.id.lv_applet_order);
        lv_applet_order.setPullRefreshEnable(true);
        tv_create_new_order = (TextView) findViewById(R.id.tv_create_new_order);
        tv_no_applet_order = (TextView) findViewById(R.id.tv_no_applet_order);
        et_order_search = (EditText) findViewById(R.id.et_search);
        et_order_search.setHint("输入订单号或备注内容搜索");

        if(et_order_search.hasFocus()) {
            tv_create_new_order.setText("取消");
        }else {
            tv_create_new_order.setVisibility(View.GONE);
        }

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noticeMsg;
                if (TextUtils.equals("暂停营业",stop.getText().toString())){
                    isworking =true;
                    noticeMsg="暂停营业期间，客户将不能在小程序端下单购买商品";
                }else {
                    isworking =false;
                    noticeMsg="开始营业后，设备将自动为您接收并打印客户的订单";
                }
                NoticePopuUtils.showBindDia(MsCenterActivity.this, noticeMsg, new NoticePopuUtils.OnClickCallBack() {
                    @Override
                    public void onClickYes() {
                        setCommunityState();
                    }
                    @Override
                    public void onClickNo() {

                    }
                });
            }
        });
    }

    private void setCommunityState() {
        Map<String, String> params = netWorks.getPublicParams();
        params.put("storeid",Constant.STORE_ID);
        if(isworking) {
            params.put("status",0+"");
            Utils.showWaittingDialog(this,"正在暂停营业...");
        }else {
            params.put("status",1+"");
            Utils.showWaittingDialog(this,"正在开启营业...");

        }
        netWorks.Request(UrlConstance.CHANGE_WXAPP_STATUS, params, 5000, 0, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                Utils.dismissWaittingDialog();
                Utils.showToast(MsCenterActivity.this,"网络异常，请检查网络！");
            }

            @Override
            public void onResonse(String response, int flag) {
                Utils.dismissWaittingDialog();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if(code==200) {
                        if(isworking) {
                            isworking=false;
                            stop.setText("开始营业");
                        }else {
                            isworking=true;
                            stop.setText("暂停营业");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setListener() {
        ll_cancel.setOnClickListener(this);
        ll_loading.setClickToReload(this);
        tv_create_new_order.setOnClickListener(this);
        lv_applet_order.setXListViewListener(this);
        et_order_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    tv_create_new_order.setVisibility(View.VISIBLE);
                    tv_create_new_order.setText("取消");
                    isSearch = true;
                } else {
                    tv_create_new_order.setVisibility(View.GONE);
                    isSearch = false;
                }
            }
        });
        et_order_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //即时请求数据：
                if(editable != null && !"".equals(editable.toString())){
                    pageNum = 1;
                    remark = editable.toString();
                }else{
                    remark = "";
                }
                initData();
            }
        });
    }

    @Subscribe
    public void onEventMainThread(String order_come) {
        LogUtils.e(TAG,"onEventMainThread() order_come=" + order_come);
        //通知了且判断MsCenterActivity不在栈顶
        boolean isTop = isMainActivityTop();
        if("manager_applet_come".equals(order_come)&&isTop){
            initData();
        }
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        LogUtils.e(TAG,"onResume()");
        initData();
    }*/

    @Override
    public void ClickToReload() {//点击重新加载
        LogUtils.e(TAG,"ClickToReload()");
        initData();
    }

    @Override
    public void onRefresh() {//下拉刷新
        LogUtils.e(TAG,"onRefresh()");
        isRefresh = true;
        pageNum = 1;
        initData();
    }

    @Override
    public void onLoadMore() {//上拉加载更多
        LogUtils.e(TAG,"onLoadMore()");
//        pageNum++;
//        initData();
        getMOreData();
    }

    /**
     * 加载更多
     */
    private void getMOreData() {
        Map<String, String> params = netWorks.getPublicParams();
        params.put("advid", Constant.ADV_ID);//88290
        params.put("storeid",Constant.STORE_ID);//132
        params.put("page",pageNum+1+"");
        params.put("search",remark);
        params.put("ordertype",ordertype+"");
        params.put(Constant.PARAMS_NAME_VERSIONCODE,Constant.VERSION_CODE+"");
        netWorks.Request(UrlConstance.GET_APPLET_LIST_DATA, params,5000,0, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                netError();
            }
            @Override
            public void onResonse(String response, int flag) {
                parseMoreJson(response);
            }
        });
    }



    private void initData() {

        Map<String, String> params = netWorks.getPublicParams();
        params.put("advid", Constant.ADV_ID);//88290
        params.put("storeid",Constant.STORE_ID);//132
        params.put("page",""+pageNum);
        params.put("search",remark);
        params.put("ordertype",ordertype+"");
        params.put(Constant.PARAMS_NAME_VERSIONCODE,Constant.VERSION_CODE+"");
        netWorks.Request(UrlConstance.GET_APPLET_LIST_DATA, params,5000,0, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                netError();
            }
            @Override
            public void onResonse(String response, int flag) {
                parseJson(response);
            }
        });
    }

    /*@Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.e(TAG,"onRestart()");
        initData();
    }*/

    private void parseMoreJson(String response) {
        try {
            LogUtils.e(TAG,"response="+response);
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            LogUtils.e(TAG,"code="+code);
            if(code==200){
                pageNum++;
                tv_no_applet_order.setVisibility(View.GONE);
                lv_applet_order.setVisibility(View.VISIBLE);
                lv_applet_order.stopLoadMore();
                MsCenterOrderListBean getMoreData = new Gson().fromJson(response, MsCenterOrderListBean.class);
                List<MsCenterOrderListBean.MsgBean.OrderListBean> orderList = getMoreData.getMsg().getOrderList();
                if(orderList!=null&&orderList.size()>0){
                    msCenterOrderListBean.getMsg().getOrderList().addAll(orderList);
                    appletListAdapter.notifyDataSetChanged();
                }
            }else if(code==10112){////上拉加载更多的时候没有数据
                lv_applet_order.stopLoadMore();
                lv_applet_order.setPullLoadEnable(false);
                Utils.showToast(MsCenterActivity.this,"没有更多数据了");
            }else if(code==10113){//一条数据也没有
                lv_applet_order.setVisibility(View.GONE);
                ll_loading.setVisibility(View.GONE);
                tv_no_applet_order.setVisibility(View.VISIBLE);
            }else{
                Utils.showToast(this,"返回码：code="+code);
                noData();
            }
        } catch (JSONException e) {
            LogUtils.e(TAG,"JSONException e="+e.toString());
            e.printStackTrace();
        }
    }

    private void parseJson(String response) {
        try {
            LogUtils.e(TAG,"response="+response);
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            LogUtils.e(TAG,"code="+code);
            if(code==200){
                tv_no_applet_order.setVisibility(View.GONE);
                lv_applet_order.setVisibility(View.VISIBLE);

                if(isRefresh){
                    LogUtils.e(TAG,"刷新...");
                    isRefresh = false;
                    lv_applet_order.stopRefresh();
                    msCenterOrderListBean = new Gson().fromJson(response, MsCenterOrderListBean.class);
                }else{
                    msCenterOrderListBean = new Gson().fromJson(response, MsCenterOrderListBean.class);
                }
                showData();

            }else if(code==10112){////上拉加载更多的时候没有数据
                lv_applet_order.stopLoadMore();
                lv_applet_order.setPullLoadEnable(false);
                Utils.showToast(MsCenterActivity.this,"没有更多数据了");
            }else if(code==10113){//一条数据也没有
                lv_applet_order.setVisibility(View.GONE);
                ll_loading.setVisibility(View.GONE);
                tv_no_applet_order.setVisibility(View.VISIBLE);
            }else{
                Utils.showToast(this,"返回码：code="+code);
                noData();
            }
        } catch (JSONException e) {
            LogUtils.e(TAG,"JSONException e="+e.toString());
            e.printStackTrace();
        }
    }

    private void showData() {
        LogUtils.e(TAG,"showData()");
        ll_loading.setVisibility(View.GONE);
        int size = msCenterOrderListBean.getMsg().getOrderList().size();
        if(size>0){
            appletListAdapter = new AppletListAdapter(this);
            appletListAdapter.setDetailAndSendListener(detailAndSendListener);
            appletListAdapter.setData(msCenterOrderListBean);
            lv_applet_order.setAdapter(appletListAdapter);
            if (size < 6) {
                lv_applet_order.setPullLoadEnable(false);
            } else {
                lv_applet_order.setPullLoadEnable(true);
            }
        }else{
            tv_no_applet_order.setVisibility(View.VISIBLE);
        }
    }

    private List<Sender.DataBean> senderData;
    private String handlingOrderId="";//logid，提交配送员时传
    private WxAppDetailAndSendListener detailAndSendListener=new WxAppDetailAndSendListener() {
        @Override
        public void onClickDetail(String orderId,String orderType) {
            Intent intent=new Intent(MsCenterActivity.this, BillDetailActivity.class);
            intent.putExtra(Constant.FROME_WHERE, Constant.COMMUNITY_INFO_DATA);
            intent.putExtra(Constant.ORDER_ID, orderId);
            intent.putExtra("isRechargeLog", orderType);
            intent.putExtra("sender",sender);
            intent.putExtra("logid",handlingOrderId);
            startActivity(intent);
        }

        @Override
        public void onClickSend(String logId) {
            handlingOrderId=logId;
            if(sender==null||sender.getData()==null||sender.getData().size()<=0) {
                //直接提交订单配送状态
                Utils.showToast(MsCenterActivity.this,"配送员数据异常，请添加配送员！");
            }else {
                senderData = sender.getData();
                if(senderData.size()==1) {
                    upDateSender(senderData.get(0).getId());
                }else {
                    for (int i = 0; i < senderData.size(); i++) {
                        senderData.get(i).setSelected(false);
                    }
                    selectedSender=null;
                    chooseSender();
                }

            }
        }
    };

    private void upDateSender(String sendId) {
        Utils.showWaittingDialog(this,"正在提交配送员...");
        Map<String, String> params = netWorks.getPublicParams();
        params.put("storeid",Constant.STORE_ID);
        params.put("orderid",handlingOrderId);
        params.put("manid",sendId);
        netWorks.Request(UrlConstance.SET_DEVLIVERMAN, params, 5000, 0, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                Utils.dismissWaittingDialog();
                Utils.showToast(MsCenterActivity.this,"网络异常，配送员分配失败，请检查网络！");
            }

            @Override
            public void onResonse(String response, int flag) {
                Utils.dismissWaittingDialog();
                LogUtils.e(TAG,"提交配送员："+response.toString());
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    if(code==200) {
                        //刷新界面
                        List<MsCenterOrderListBean.MsgBean.OrderListBean> orderList = msCenterOrderListBean.getMsg().getOrderList();
                        int size = orderList.size();
                        for (int i = 0; i < size; i++) {
                            if(TextUtils.equals(handlingOrderId,orderList.get(i).getLogId())) {
                                orderList.get(i).setDelivery_status("1");
                                appletListAdapter.notifyDataSetChanged();
                            }
                        }
//                        pageNum=1;
//                        initData();
                    }else {
                        Utils.showToast(MsCenterActivity.this,msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 展示订单详情的popuwindow
     */
    private void chooseSender() {
        if (detailPup != null && detailPup.isShowing()) {
            detailPup.dismiss();
        } else {
            View popView = View.inflate(this, R.layout.select_sender, null);
            ListView lv_order_content= (ListView) popView.findViewById(R.id.lv_order_content);
            TextView tv_cancel= (TextView) popView.findViewById(R.id.tv_cancel);
            TextView tv_sure= (TextView) popView.findViewById(R.id.tv_sure);
            final SenderAdapter senderAdapter=new SenderAdapter(this);

            senderAdapter.setData(senderData);
            lv_order_content.setAdapter(senderAdapter);
            detailPup = new PopupWindow(popView);

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

            setbackAlpha(0.7);
            tv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(detailPup!=null&&detailPup.isShowing()) {
                        detailPup.dismiss();
                    }
                }
            });
            detailPup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    setbackAlpha(1);
                }
            });
            detailPup.showAtLocation(findViewById(R.id.ms_root), Gravity.BOTTOM, 0, 0);
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
                        Utils.showToast(MsCenterActivity.this,"请选择配送员",1500);
                    }else {
                        detailPup.dismiss();
                        upDateSender(selectedSender.getId());
                    }
                }
            });
        }
    }
    private void setbackAlpha(double alpha) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = (float)alpha;
        getWindow().setAttributes(params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_cancel://退出本頁面
                finish();
                break;
            case R.id.tv_create_new_order://取消搜索
                remark = "";
                if(et_order_search.hasFocus()) {
                    et_order_search.setText(null);
                    et_order_search.clearFocus();
                }
                initData();
                break;
            case R.id.rb_statistics_by_selforder:
                stop.setVisibility(View.GONE);
                ordertype=0;
                pageNum=1;
//                rl_select_left.setVisibility(View.VISIBLE);
                line_which_display_selforder.setVisibility(View.VISIBLE);
                line_which_display_communityorder.setVisibility(View.GONE);
                initData();
                break;
            case R.id.rb_statistics_by_communityorder:
                stop.setVisibility(View.VISIBLE);
                ordertype=1;
                pageNum=1;
//                rl_select_all_tools.setVisibility(View.GONE);
                line_which_display_selforder.setVisibility(View.GONE);
                line_which_display_communityorder.setVisibility(View.VISIBLE);
                initData();
                break;
        }
    }

    /**
     * 网络错误
     */
    public void netError() {
        iv_jiazai_filure.setVisibility(View.VISIBLE);
        iv_jiazai_filure.setImageResource(R.drawable.net_error);
        pb_loading.setVisibility(View.GONE);
        tv_loading.setText("请检查网络后，点击屏幕重新加载！");
    }

    /**
     * 暂无数据
     */
    public void noData() {
        iv_jiazai_filure.setVisibility(View.GONE);
        pb_loading.setVisibility(View.GONE);
        tv_loading.setText("没有数据请重试...");
    }

    /**
     * 判断mainactivity是否处于栈顶
     * @return  true在栈顶false不在栈顶
     */
    private boolean isMainActivityTop(){
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(MsCenterActivity.class.getName());
    }
}
