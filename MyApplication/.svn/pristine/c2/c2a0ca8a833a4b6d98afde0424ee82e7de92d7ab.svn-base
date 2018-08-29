package com.yongle.letuiweipad.pagers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.adapter.RecyclerAdapter;
import com.yongle.letuiweipad.application.MyApplication;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.constant.UrlConstance;
import com.yongle.letuiweipad.dao.DaoSession;
import com.yongle.letuiweipad.dao.GoodBeanDao;
import com.yongle.letuiweipad.dao.GoodsTypeInfoDao;
import com.yongle.letuiweipad.domain.createorder.AllGoodsInfoBean;
import com.yongle.letuiweipad.domain.createorder.GoodBean;
import com.yongle.letuiweipad.domain.goodinfo.GoodsTypeInfo;
import com.yongle.letuiweipad.selfinterface.RecyclerItemClickListener;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.NetWorks;
import com.yongle.letuiweipad.utils.SearchUtils;
import com.yongle.letuiweipad.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;


public class GoodsFragment extends Fragment {
    private Activity mActivity;
    @BindView(R.id.rc_good_kinds) RecyclerView goodKindsList;
    @BindView(R.id.rc_search_good) RecyclerView goodSearchList;
    @BindView(R.id.rc_goods) RecyclerView goodList;
    @BindView(R.id.tv_nogs) TextView tv_nogs;
    @BindView(R.id.net_error) RelativeLayout netError;
    @BindView(R.id.ll_content) LinearLayout ll_content;
    private LinearLayoutManager llManager;
    private GridLayoutManager gridLayoutManager1;
    private GridLayoutManager gridLayoutManager2;
    private RecyclerAdapter goodAdapter;
    private RecyclerAdapter kindsAdapter;
    private List<GoodBean> goodsData;
    private List<GoodsTypeInfo> typeList;
    private boolean filterWeigh=false;
    @BindView(R.id.et_search) EditText et_search;
    private List<GoodBean> goodBeanList;
    @BindView(R.id.tv_cancel_search) TextView tv_cancel_search;
    private View totalView;
    private List<GoodBean> filterData=new ArrayList<>();
    private RecyclerAdapter filterAdapter;
    private boolean everSet;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (Activity) context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        totalView = inflater.inflate(R.layout.goods_display_layout,null);
        ButterKnife.bind(this, totalView);

        llManager = new LinearLayoutManager(mActivity);
        llManager.setOrientation(LinearLayoutManager.VERTICAL);
        goodKindsList.setLayoutManager(llManager);
        goodKindsList.setHasFixedSize(true);
//        goodKindsList.addItemDecoration(new DividerItemDecoration(mActivity,LinearLayoutManager.VERTICAL));

        gridLayoutManager1 = new GridLayoutManager(mActivity,5);
        gridLayoutManager2 = new GridLayoutManager(mActivity,6);

        goodList.setLayoutManager(gridLayoutManager1);
        goodSearchList.setLayoutManager(gridLayoutManager2);

        goodList.setHasFixedSize(true);
        goodSearchList.setHasFixedSize(true);

        goodAdapter = new RecyclerAdapter(mActivity);
        kindsAdapter=new RecyclerAdapter(mActivity);
        filterAdapter=new RecyclerAdapter(mActivity);

        goodList.setAdapter(goodAdapter);
        goodSearchList.setAdapter(filterAdapter);
        goodKindsList.setAdapter(kindsAdapter);

        refreshGoods(-1);
        addListener();
        return totalView;
    }

    private void addListener() {
        goodAdapter.setItemClickListener(goodItemClickListener);
        filterAdapter.setItemClickListener(filterGoodItemClickListener);

        kindsAdapter.setItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int postiont) {
                refreshGoods(typeList.get(postiont).getTypeId());
            }
        });
    }
    @OnFocusChange(R.id.et_search)
    void focusChange(View view, boolean b){
        if(b) {
            if(goodBeanList==null) {
                MyApplication application=(MyApplication)mActivity.getApplication();
                DaoSession daoSession = application.getDaoSession();
                GoodBeanDao goodBeanDao = daoSession.getGoodBeanDao();
                if(filterWeigh) {
                    goodBeanList=goodBeanDao.queryBuilder().where(GoodBeanDao.Properties.Is_weigh.eq("1"))
                    .orderAsc(GoodBeanDao.Properties.Id)
                    .list();
                }else {
                    goodBeanList = goodBeanDao.loadAll();
                }
                SearchUtils.getInstance().parseData(goodBeanList);
            }
            goodKindsList.setVisibility(View.GONE);
            goodList.setVisibility(View.GONE);
            goodSearchList.setVisibility(View.VISIBLE);
            tv_cancel_search.setVisibility(View.VISIBLE);
            if(tv_nogs.getVisibility()==View.VISIBLE) {
                tv_nogs.setVisibility(View.GONE);
            }
        }else {
            goodSearchList.setVisibility(View.GONE);
            goodKindsList.setVisibility(View.VISIBLE);
            tv_cancel_search.setVisibility(View.GONE);
            et_search.setText(null);
            Utils.hideKeyboard(totalView);

            goodList.setVisibility(View.VISIBLE);
            if(goodsData!=null&&goodsData.size()>0) {
                tv_nogs.setVisibility(View.GONE);
                goodList.setVisibility(View.VISIBLE);
            }else {
                tv_nogs.setVisibility(View.VISIBLE);
                goodList.setVisibility(View.GONE);
                if(filterWeigh) {
                    tv_nogs.setText("当前类目下没有称重商品");
                }else {
                    tv_nogs.setText("当前类目下没有商品");
                }
            }
        }
    }
    @OnTextChanged(value = R.id.et_search,callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void searchGs(Editable editable){
         String inpuStr = editable.toString();

        if(inpuStr.length()<=0) {
            filterData.clear();
            filterAdapter.notifyDataSetChanged();
            return;
        }
        SearchUtils.getInstance().search(inpuStr,filterData,goodBeanList);
        if(filterData.size()<=0) {
            tv_nogs.setVisibility(View.VISIBLE);
            tv_nogs.setText("未检索到符合条件的商品");
            goodSearchList.setVisibility(View.GONE);
        }else {
            tv_nogs.setVisibility(View.GONE);
            goodSearchList.setVisibility(View.VISIBLE);
            if(!everSet) {
                everSet=true;
                filterAdapter.setData(filterData,RecyclerAdapter.GOODS_INFO);
                goodSearchList.setAdapter(filterAdapter);
            }else {
                filterAdapter.notifyDataSetChanged();
            }
        }

    }

    private void setBroadCast(GoodBean goodBean) {
        Intent intent=new Intent();
        intent.setAction(Constant.GOOD_SELECTED_RECEIVER);
        intent.putExtra(Constant.GOOD_EXTRA_NAME,goodBean);
        mActivity.sendBroadcast(intent);
    }

    private RecyclerItemClickListener goodItemClickListener=new RecyclerItemClickListener() {
        @Override
        public void onItemClick(View view, int postiont) {
            GoodBean goodBean = goodsData.get(postiont);
            setBroadCast(goodBean);
        }
    };
    private RecyclerItemClickListener filterGoodItemClickListener=new RecyclerItemClickListener() {
        @Override
        public void onItemClick(View view, int postiont) {
            GoodBean goodBean = filterData.get(postiont);
            setBroadCast(goodBean);
        }
    };


    private static final String TAG = "GoodsFragment";
    public void refreshGoods(int typeId){
        MyApplication application=(MyApplication)mActivity.getApplication();
        DaoSession daoSession = application.getDaoSession();
        GoodBeanDao goodBeanDao = daoSession.getGoodBeanDao();
        GoodsTypeInfoDao typeInfoDao = daoSession.getGoodsTypeInfoDao();

        typeList = typeInfoDao.loadAll();
        boolean moveTop=false;
        if(typeList !=null&& typeList.size()>0) {
            if(typeId>0) {//切换类目
                if(filterWeigh) {
                    goodsData = goodBeanDao.queryBuilder()
                            .where(GoodBeanDao.Properties.TypeId.eq(typeId),GoodBeanDao.Properties.Is_weigh.eq("1"))
                            .orderAsc(GoodBeanDao.Properties.Id)
                            .list();
                }else {
                    goodsData = goodBeanDao.queryBuilder()
                            .where(GoodBeanDao.Properties.TypeId.eq(typeId))
                            .orderAsc(GoodBeanDao.Properties.Id)
                            .list();
                }

                for (int i = 0; i < typeList.size(); i++) {
                    if(typeList.get(i).getTypeId()==typeId) {
                        typeList.get(i).setChecked(true);
                    }else {
                        typeList.get(i).setChecked(false);
                    }
                }
            }else {//初始化
                if(filterWeigh) {
                    goodsData =goodBeanDao.queryBuilder()
                            .where(GoodBeanDao.Properties.TypeId.eq(typeList.get(0).getTypeId()),GoodBeanDao.Properties.Is_weigh.eq("1"))
                            .orderAsc(GoodBeanDao.Properties.Id)
                            .list();
                }else {
                    goodsData =goodBeanDao.queryBuilder()
                            .where(GoodBeanDao.Properties.TypeId.eq(typeList.get(0).getTypeId()))
                            .orderAsc(GoodBeanDao.Properties.Id)
                            .list();
                }

                for (int i = 0; i < typeList.size(); i++) {
                    typeList.get(i).setChecked(false);
                }
                typeList.get(0).setChecked(true);
                moveTop=true;
            }
            upDateAdapter(moveTop);
        }
    }

    private void upDateAdapter(boolean moveTop) {
        LogUtils.e(TAG,goodsData.toString());
        goodAdapter.setData(goodsData,RecyclerAdapter.GOODS_INFO);
        kindsAdapter.setData(typeList,RecyclerAdapter.GOODS_TYPE_INFO);

        goodAdapter.notifyDataSetChanged();
        kindsAdapter.notifyDataSetChanged();

        if(goodsData==null||goodsData.size()<=0) {
            goodList.setVisibility(View.GONE);
            tv_nogs.setVisibility(View.VISIBLE);
            if(filterWeigh) {
                tv_nogs.setText("当前类目下没有称重商品");
            }else {
                tv_nogs.setText("当前类目下没有商品");
            }
        }else {
            goodList.setVisibility(View.VISIBLE);
            tv_nogs.setVisibility(View.GONE);
        }
    }
    @OnClick({R.id.tv_update_goods,R.id.net_error_refresh,R.id.tv_cancel_search})
    void onClickUpdate(View view){
        if(view.getId()==R.id.tv_update_goods||view.getId()==R.id.net_error_refresh) {
            getGsInfo();
        }else if(view.getId()==R.id.tv_cancel_search) {
            et_search.clearFocus();
        }
    }
    /**
     * 获取商品信息,1.初始化进来 2.添加商品 3.取消订单 4.完成结账
     * 已选择列表数据来源更新为orderbean
     */
    private void getGsInfo() {
        NetWorks netWorks=new NetWorks(mActivity);
        Map<String, String> params = netWorks.getPublicParams();
        params.put("storeid", Constant.STORE_ID);
        params.put("advid", Constant.ADV_ID);
        params.put("versionCode", "42");
        params.put("needVipPrice", "1");
        netWorks.Request(UrlConstance.GOODS_INFO,true,"正在获取商品信息...", params, 5000, 0, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                netError.setVisibility(View.VISIBLE);
                ll_content.setVisibility(View.GONE);
                LogUtils.Log("请求商品error:" + e.toString());
            }

            @Override
            public void onResonse(String response, int flag) {
                LogUtils.e(TAG,"获取商品："+response);
                netError.setVisibility(View.GONE);
                ll_content.setVisibility(View.VISIBLE);
                parseJson(response);
            }
        });
    }
    private void parseJson(String response) {
        Gson gson = new Gson();
        AllGoodsInfoBean gotAllGoodsInfoBean;
        gotAllGoodsInfoBean = gson.fromJson(response, AllGoodsInfoBean.class);
        if(gotAllGoodsInfoBean!=null||gotAllGoodsInfoBean.getMsg()!=null&&gotAllGoodsInfoBean.getMsg().getGoodsInfo()!=null&&gotAllGoodsInfoBean.getMsg().getGoodsInfo().size()>0) {
            MyApplication application=(MyApplication)mActivity.getApplication();
            DaoSession daoSession = application.getDaoSession();
            GoodBeanDao goodBeanDao = daoSession.getGoodBeanDao();
            GoodsTypeInfoDao typeInfoDao = daoSession.getGoodsTypeInfoDao();
            try{
                typeInfoDao.deleteAll();
                goodBeanDao.deleteAll();
            }catch (Exception e){
                LogUtils.e(TAG,"删除表数据异常");
            }
            typeList=new ArrayList<>();
            for (int i = 0; i < gotAllGoodsInfoBean.getMsg().getGoodsInfo().size(); i++) {
                AllGoodsInfoBean.MsgBean.GoodsInfoBean goodsInfoBean = gotAllGoodsInfoBean.getMsg().getGoodsInfo().get(i);
                try{
                    GoodsTypeInfo goodsTypeInfo=new GoodsTypeInfo(null, goodsInfoBean.getTypeId(),goodsInfoBean.getTypeName());
                    typeList.add(goodsTypeInfo);
                    for (int j = 0; j <goodsInfoBean.getGoodsList().size(); j++) {
                        goodsInfoBean.getGoodsList().get(j).setTypeId(goodsInfoBean.getTypeId());
                    }
                    typeInfoDao.insertOrReplaceInTx(typeList);
                    goodBeanDao.insertOrReplaceInTx(goodsInfoBean.getGoodsList());
                }catch (Exception e){
                    LogUtils.e(TAG,"写入数据库异常  e="+e.toString());
                }
            }
            typeList.get(0).setChecked(true);
            if(goodsData==null) {
                goodsData=new ArrayList<>();
            }else {
                goodsData.clear();
            }
            if(filterWeigh) {
                for (int i = 0; i < gotAllGoodsInfoBean.getMsg().getGoodsInfo().get(0).getGoodsList().size(); i++) {
                    if(gotAllGoodsInfoBean.getMsg().getGoodsInfo().get(0).getGoodsList().get(i).getIs_weigh().equals("1")) {
                        goodsData.add(gotAllGoodsInfoBean.getMsg().getGoodsInfo().get(0).getGoodsList().get(i));
                    }
                }
            }else {
                goodsData=gotAllGoodsInfoBean.getMsg().getGoodsInfo().get(0).getGoodsList();
            }
            upDateAdapter(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setFilterWeigh(boolean filterWeigh) {
        this.filterWeigh = filterWeigh;
    }
}
