package com.yongle.letuiweipad.pagers.unpayorder;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.adapter.RecyclerAdapter;
import com.yongle.letuiweipad.basepager.BasePager;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.constant.UrlConstance;
import com.yongle.letuiweipad.domain.unpayorder.UnPayBean;
import com.yongle.letuiweipad.selfinterface.RecyclerItemClickListener;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.NetWorks;
import com.yongle.letuiweipad.utils.Utils;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

/**
 * Created by Administrator on 2017/11/21 0021.
 */

public class UnPayOrderListFragment extends BasePager implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.tv_cancel_search) TextView tvCancelSearch;
    @BindView(R.id.et_search)EditText etSearch;
    @BindView(R.id.tv_current_order)TextView tvCurrentOrder;
    @BindView(R.id.tv_total_ordernum)TextView tvTotalOrdernum;
    @BindView(R.id.order_content_title) AutoLinearLayout orderContentTitle;
    @BindView(R.id.rlv_left)RecyclerView rlvLeft;
    @BindView(R.id.rlv_search) RecyclerView rlvSearch;
    @BindView(R.id.sr_layout)SwipeRefreshLayout sr_layout;
    @BindView(R.id.ll_search_null)LinearLayout ll_search_null;
    @BindView(R.id.net_error) RelativeLayout netError;
    private Handler handler = null;
    private NetWorks netWorks;
    private List<UnPayBean.MsgBean> unPayBeanLists;
    private List<UnPayBean.MsgBean> filterList;
    private UnPayBean unPayBean;
    private UnPayBean unPaySearchBean;
    private RecyclerAdapter adapter;
    private RecyclerAdapter filterAdapter;
    private int lastVisibleItemPosition;
    private int lastVisibleItemPosition_search;

    /**
     * 刷新之前的总数量
     */
    private double beforeNum;
    private LinearLayoutManager llManager1;
    private boolean isLoading;
    private boolean isSearchLoading;
    private View baseView;
    private boolean isSearch=false;
    private LinearLayoutManager llManager2;
    private String inpuStr="";
    private int selectedItem=-1;
    private int filtePagerNum=1;
    private int pagerNum=1;

    public UnPayOrderListFragment() {
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
    @Override
    public View initView() {
        baseView = View.inflate(mActivity, R.layout.unpay_orderlist_fragment_layout, null);
        return baseView;
    }

    @Override
    public void initData(int position) {
        LogUtils.Log("未结账订单列表i nitData()");
        etSearch.setHint("请输入订单号搜索");
        netWorks=new NetWorks(mActivity);
        llManager1 = new LinearLayoutManager(mActivity);
        llManager2 = new LinearLayoutManager(mActivity);
        rlvLeft.setLayoutManager(llManager1);
        rlvSearch.setLayoutManager(llManager2);

        rlvLeft.setHasFixedSize(true);
        rlvSearch.setHasFixedSize(true);

        adapter = new RecyclerAdapter(mActivity);
        filterAdapter = new RecyclerAdapter(mActivity);

        rlvLeft.addItemDecoration(new DividerItemDecoration(mActivity,LinearLayoutManager.VERTICAL));
        rlvSearch.addItemDecoration(new DividerItemDecoration(mActivity,LinearLayoutManager.VERTICAL));

        sr_layout.setProgressBackgroundColorSchemeColor(Color.RED);
        loadData("",false,false);
        setListener();
    }

    private void setListener() {
        sr_layout.setOnRefreshListener(this);

        rlvLeft.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int topRowVerticalPosition =(recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                if(topRowVerticalPosition==0) {
                    return;
                }
                LogUtils.Log("topRowVerticalPosition:"+topRowVerticalPosition);
                if(!isLoading) {
                    if(newState==RecyclerView.SCROLL_STATE_IDLE&&lastVisibleItemPosition+1==adapter.getItemCount()) {
                        LogUtils.Log("是时候加载更多了");
                        isLoading=true;
                        loadData("",true,false);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = llManager1.findLastVisibleItemPosition();
                LogUtils.Log("lastposi=="+ lastVisibleItemPosition);
            }
        });

        rlvSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int topRowVerticalPosition =(recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                if(topRowVerticalPosition==0) {
                    return;
                }
//                sr_layout.setEnabled(topRowVerticalPosition>=0);
                if(!isSearchLoading) {
                    if(newState==RecyclerView.SCROLL_STATE_IDLE&&lastVisibleItemPosition_search+1==filterAdapter.getItemCount()) {
                        LogUtils.Log("是时候加载更多了");
                        isSearchLoading=true;
                        loadData(inpuStr,true,false);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition_search = llManager2.findLastVisibleItemPosition();
            }
        });

        adapter.setItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selectedItem=position;
                String orderId=unPayBeanLists.get(position).getId();
                LogUtils.Log("orderId=="+orderId);
                for (int i = 0; i < unPayBeanLists.size(); i++) {
                    if(i==position) {
                        unPayBeanLists.get(i).setSelected(true);
                    }else {
                        unPayBeanLists.get(i).setSelected(false);
                    }
                }
                adapter.notifyDataSetChanged();
                tvCurrentOrder.setText("当前选中订单："+unPayBeanLists.get(position).getQuery_num());
                updateSelectedItem(orderId);
            }
        });
        filterAdapter.setItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String orderId=filterList.get(position).getId();
                LogUtils.Log("orderId=="+orderId);
                for (int i = 0; i < filterList.size(); i++) {
                    if(i==position) {
                        filterList.get(i).setSelected(true);
                    }else {
                        filterList.get(i).setSelected(false);
                    }
                }
                filterAdapter.notifyDataSetChanged();
                tvCurrentOrder.setText("当前选中订单："+filterList.get(position).getQuery_num());
                updateSelectedItem(orderId);
            }
        });

    }

    private static final String TAG = "UnPayOrderListFragment";
    private void updateSelectedItem(String orderId) {
        Message msg = Message.obtain();
        Bundle bundle=new Bundle();
        bundle.putString("orderId",orderId);
        msg.setData(bundle);
        msg.what=Constant.UNPAYITEM_TO_UNPAYDETAIL;
        handler.sendMessage(msg);
    }

    @OnTextChanged(R.id.et_search)
    void searchGs(Editable editable){
        inpuStr = editable.toString();
        if(inpuStr !=null&&! TextUtils.isEmpty(inpuStr)) {
            loadData(inpuStr,false,false);
        }
    }
    @OnFocusChange(R.id.et_search)
    void focusChange(View view, boolean b){
        if(b) {
            filtePagerNum=1;
            isSearch=true;
            tvCurrentOrder.setText("当前选中订单：--");
            tvTotalOrdernum.setText("共0个订单");
            rlvLeft.setVisibility(View.GONE);
            rlvSearch.setVisibility(View.VISIBLE);
            tvCancelSearch.setVisibility(View.VISIBLE);
            ll_search_null.setVisibility(View.VISIBLE);
            updateSelectedItem("-1");
        }else {
            isSearch=false;
            if(unPayBeanLists!=null&&unPayBeanLists.size()>0&&selectedItem>=0) {
                tvCurrentOrder.setText("当前选中订单："+unPayBeanLists.get(selectedItem).getQuery_num());
                updateSelectedItem(unPayBeanLists.get(selectedItem).getId());
            }
            setTitle();
            rlvLeft.setVisibility(View.VISIBLE);
            rlvSearch.setVisibility(View.GONE);
            tvCancelSearch.setVisibility(View.GONE);
            ll_search_null.setVisibility(View.GONE);
            etSearch.setText(null);
            Utils.hideKeyboard(baseView);
        }
    }
    @OnClick(R.id.tv_cancel_search)
   public void cancelSearch(){
        etSearch.clearFocus();
        if(filterList!=null) {
            filterList.clear();
        }
    }
    @OnClick(R.id.net_error_refresh)
   public void nerErrorRefresh(){
        loadData("",false,false);
    }
    @Override
    public void onRefresh() {
        LogUtils.Log("onRefresh");
        loadData("",false,true);
    }
    public void loadData(String inputStr, final boolean isGetMore, final boolean isRefresh) {
        String noticeMsg;
        if(isGetMore) {
             noticeMsg="正在加载更多数据...";
        }else {
             noticeMsg="正在加载数据...";
        }
        int pagerStr=isSearch?filtePagerNum:pagerNum;
        Map<String, String> publicParams = netWorks.getPublicParams();
        publicParams.put("storeid", Constant.STORE_ID);
        publicParams.put("pagenum", pagerStr+"");
        publicParams.put("pagesize", "10");
        publicParams.put("param", inputStr);
        netWorks.Request(UrlConstance.UNPAY_ORDER_LISTS,true,noticeMsg, publicParams, 5000, 0, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                netError.setVisibility(View.VISIBLE);
                LogUtils.Log("获取未结账订单列表异常："+e.toString());
                Utils.showToast(mActivity,"网络异常，请检查网络后重试！",1500);
                isLoading=false;
                isSearchLoading=false;
                if(isRefresh) {
                    sr_layout.setRefreshing(false);
                }
            }

            @Override
            public void onResonse(String response, int flag) {
                netError.setVisibility(View.GONE);
                LogUtils.Log("获取未结账订单列表onResonse："+response.toString());
                isLoading=false;
                isSearchLoading=false;
                if(isRefresh) {
                    sr_layout.setRefreshing(false);
                }
                parseJson(response,isGetMore,isRefresh);
            }
        });
    }


    private void parseJson(String json,boolean isGetMore,boolean isRefresh) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            Gson gson = new Gson();
            if (code == 200) {//返回成功
                //如果是刷新，当刷新到新的订单时载处理
                if(isRefresh) {
                    UnPayBean newBean = gson.fromJson(json, UnPayBean.class);
                    if(newBean!=null&&newBean.getMsg()!=null&&newBean.getMsg().size()>0) {
                        if (TextUtils.equals(newBean.getMsg().get(0).getId(),unPayBeanLists.get(0).getId())){//选中不变
                            Utils.showToast(mActivity,"没有检测到新订单！",1500);
                            return;
                        }else {//刷新到了新订单,选中清空
                            unPayBean = gson.fromJson(json, UnPayBean.class);
                            unPayBeanLists = unPayBean.getMsg();
                            updateSelectedItem("-1");
                            selectedItem=-1;
                            adapter.setData(unPayBeanLists,1);
                            rlvLeft.setAdapter(adapter);
                            tvCurrentOrder.setText("当前选中订单：--");
                            setTitle();
                            return;
                        }
                    }
                }
                //如果是加载更多，获取到新的bean的并添加到相应数组中，选中不变
                if (isGetMore) {
                    UnPayBean moreBean = gson.fromJson(json, UnPayBean.class);
                    if(isSearch){
                        if(filterList!=null&&filterList.size()>0) {
                            filtePagerNum++;
                            filterList.addAll(moreBean.getMsg());
                        }
                        filterAdapter.notifyDataSetChanged();
                    }else {
                        if (unPayBeanLists != null && unPayBeanLists.size() > 0) {
                            pagerNum++;
                            unPayBeanLists.addAll(moreBean.getMsg());
                        }
                        adapter.notifyDataSetChanged();
                    }
                }else {
                    //不是刷新也不是加载更多，初始化或者搜索,直接填充,选清空
                    if(isSearch) {
                        unPaySearchBean = gson.fromJson(json, UnPayBean.class);
                        filterList=unPaySearchBean.getMsg();
                        setSearchResult(true);
                        filterAdapter.setData(filterList,1);
                        rlvSearch.setAdapter(filterAdapter);
                    }else {
                        unPayBean = gson.fromJson(json, UnPayBean.class);
                        unPayBeanLists = unPayBean.getMsg();
                        adapter.setData(unPayBeanLists,1);
                        rlvLeft.setAdapter(adapter);
                    }
                    updateSelectedItem("-1");
                    tvCurrentOrder.setText("当前选中订单：--");
                    setTitle();
                }

            } else if (code == 10113) {//没有数据
               if (isSearch) {
                   setSearchResult(false);
                }
              /*   if(!isSearch&&!isRefresh&&!isGetMore){
                    noNotPaidOrder();
                }*/
            } else if(code == 10112){//上拉加载更多的时候没有数据
                Utils.showToast(mActivity, "没有更多数据了");
            }else{
//                noData();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setSearchResult(boolean hasData) {
        if(hasData) {
            if(rlvSearch.getVisibility()== View.GONE) {
                rlvSearch.setVisibility(View.VISIBLE);
            }
            if(ll_search_null.getVisibility()==View.VISIBLE) {
                ll_search_null.setVisibility(View.GONE);
            }
        }else {
            tvTotalOrdernum.setText("共"+0+"个未结账订单");
            if(rlvSearch.getVisibility()== View.VISIBLE) {
                rlvSearch.setVisibility(View.GONE);
            }
            if(ll_search_null.getVisibility()==View.GONE) {
                ll_search_null.setVisibility(View.VISIBLE);
            }
        }

    }
    private void setTitle() {
        if(isSearch) {
            if(unPaySearchBean!=null) {
                tvTotalOrdernum.setText("共"+unPaySearchBean.getNum()+"个未结账订单");
            }
        }else {
            if(unPayBean!=null) {
                tvTotalOrdernum.setText("共"+unPayBean.getNum()+"个未结账订单");
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public void OnNetError() {
        netError.setVisibility(View.VISIBLE);
        tvCurrentOrder.setText("当前选中订单：--");
        tvTotalOrdernum.setText("共0个订单");
        updateSelectedItem("-1");

    }

    public void OnNetOk() {
        netError.setVisibility(View.GONE);
    }
}
