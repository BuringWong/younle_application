
package com.younle.younle624.myapplication.activity.manager.ordersetting;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.adapter.GoodsDetailAdapter;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.domain.GoodKinds;
import com.younle.younle624.myapplication.domain.setting.Goods;
import com.younle.younle624.myapplication.domain.setting.ManSettingBean;
import com.younle.younle624.myapplication.domain.setting.RoomSettingBean;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.view.SelfLinearLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class BaseSettingActivity extends Activity implements View.OnClickListener, SelfLinearLayout.ClickToReload {
    private ImageView iv_title;
    private TextView tv_title;
    private ListView left;
    private ListView right;
    public  List<GoodKinds> kinds;
    private int fromeWhere;
    private TextView tv_explain;
    private String url;
    public ProgressBar pb_loading;
    public TextView tv_loading;
    public SelfLinearLayout ll_loading;
    private Goods goodsBean;
    private List<Goods.MsgBean> msgBeanList;
    private String type="0";
    private RoomSettingBean roomSettingBean;
    private ManSettingBean manSettingBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_setting);
        fromWhere();
        initView();
        initData();
        setListener();

    }

    /**
     * 判断需要展示哪项设置
     */
    private void fromWhere() {
        fromeWhere = getIntent().getIntExtra(Constant.FROME_WHERE, -1);
    }

    private void initView() {
        //内容区域
        left = (ListView)findViewById(R.id.lv_setting_kinds);
        right = (ListView)findViewById(R.id.lv_setting_detail);
        //各种标题的布局
        iv_title = (ImageView)findViewById(R.id.iv_title);
        iv_title.setVisibility(View.VISIBLE);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_explain = (TextView)findViewById(R.id.tv_explain);
        //加载的布局
        pb_loading = (ProgressBar)findViewById(R.id.pb_loading);
        tv_loading = (TextView)findViewById(R.id.tv_loading);
        ll_loading = (SelfLinearLayout)findViewById(R.id.ll_loading);
    }
    private void setListener() {
        iv_title.setOnClickListener(this);
        left.setOnItemClickListener(new KindOnItemClickListener());
        ll_loading.setClickToReload(this);
    }

    /**
     * 初始化两个listview的数据
     */
    private void initData() {
        //1.初始化顶部导航栏数据
        switch (fromeWhere) {
            case  Constant.GOODS_SETTING:
                tv_title.setText("商品设置");
                tv_explain.setText(R.string.click_to_see_detail);
                url= UrlConstance.SERVICE_GOOD_SETTING;
                type="0";
                getGoodServiceDataFromNet();
                break;
            case  Constant.SERVICE_SETTING:
                tv_title.setText("服务设置");
                tv_explain.setText(R.string.click_to_see_detail);
                url= UrlConstance.SERVICE_GOOD_SETTING;
                type="1";
                getGoodServiceDataFromNet();
                break;
            case  Constant.ROOM_SETTING:
                tv_title.setText("房间设置");
                tv_explain.setText(R.string.click_to_see_detail_room);
                url= UrlConstance.ROOM_SETTING;
                getRoomManDataFromNet();
                break;
            case  Constant.MAN_SETTING:
                tv_title.setText("人员设置");
                tv_explain.setText(R.string.click_to_see_detail_man);
                url= UrlConstance.MAN_SETTING;
                getRoomManDataFromNet();
                break;
        }
    }

    /**
     * 物品和服务的，联网请求接口
     */
    private void getGoodServiceDataFromNet() {

        reLoad();
        OkHttpUtils.post()
                .url(url)
                .addParams("storeid", "1")
                .addParams("type",type)//0,代表实物；1，代表服务
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        LogUtils.Log("erroe==" + e);
                        loadFailure("网络异常，点击屏幕重新加载");
                    }

                    @Override
                    public void onResponse(String response) {
                        boolean toNextStep = Utils.checkSaveToken(BaseSettingActivity.this, response);
                        if(toNextStep){
                            goodsBean = new Gson().fromJson(response, Goods.class);
                            ll_loading.setVisibility(View.GONE);
                            devideData();
                        }
                    }
                });
    }

    /**
     * 人员和房间请求数据的接口
     */
    private void getRoomManDataFromNet() {
        reLoad();
        OkHttpUtils.post()
                .url(url)
                .addParams("storeid",Constant.STORE_ID)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        loadFailure("网络异常，点击屏幕重新加载");
                    }

                    @Override
                    public void onResponse(String response) {
                        boolean toNextStep = Utils.checkSaveToken(BaseSettingActivity.this, response);
                        if(toNextStep){
                            praseJson(response);
                        }
                    }
                });
    }

    /**
     * 解析json
     * @param json
     */
    private void praseJson(String json) {
        ll_loading.setVisibility(View.GONE);
        Gson gson=new Gson();
        switch (fromeWhere) {
            case Constant.ROOM_SETTING :
                roomSettingBean = gson.fromJson(json, RoomSettingBean.class);
                break;
            case Constant.MAN_SETTING :
                manSettingBean = gson.fromJson(json, ManSettingBean.class);
                break;
        }
        devideData();
    }


    /**
     * 抽取左侧listview的数据
     */
    private void devideData() {
        kinds = new ArrayList<>();
        switch (fromeWhere) {
            case Constant.GOODS_SETTING :
            case Constant.SERVICE_SETTING :
                msgBeanList = goodsBean.getMsg();
                for (int i = 0; i < msgBeanList.size(); i++) {
                    GoodKinds goodKinds;
                    if(i==0) {
                        goodKinds =new GoodKinds(msgBeanList.get(i).getSortName(),true);
                    }else {
                        goodKinds=new GoodKinds(msgBeanList.get(i).getSortName(),false);
                    }
                        kinds.add(goodKinds);
                }
                break;
            case Constant.ROOM_SETTING:
                List<RoomSettingBean.MsgBean> roomSettingBeanMsg = roomSettingBean.getMsg();
                for (int i = 0; i < roomSettingBeanMsg.size(); i++) {
                    GoodKinds goodKinds;
                    if(i==0) {
                        goodKinds=new GoodKinds(roomSettingBeanMsg.get(i).getSortName(),true);
                    }else {
                        goodKinds=new GoodKinds(roomSettingBeanMsg.get(i).getSortName(),false);
                    }
                    kinds.add(goodKinds);
                }
                break;
            case Constant.MAN_SETTING:
                List<ManSettingBean.MsgBean> manSettingBeanMsg = manSettingBean.getMsg();
                for (int i = 0; i < manSettingBeanMsg.size(); i++) {
                    GoodKinds goodKinds;
                    if(i==0) {
                        goodKinds=new GoodKinds(manSettingBeanMsg.get(i).getSortName(),true);
                    }else {
                        goodKinds=new GoodKinds(manSettingBeanMsg.get(i).getSortName(),false);
                    }
                    kinds.add(goodKinds);
                }
                break;
        }
        setAdapter();
    }
    /**
     * 设置适配器
     */
    private GoodsDetailAdapter rightAdapter;
    private KindsAdapter leftAdapter;
    private void setAdapter() {
        //1.左侧种类
        leftAdapter =new KindsAdapter();
        left.setAdapter(leftAdapter);
        //2.右侧详情
        rightAdapter =new GoodsDetailAdapter(this);
        switch (fromeWhere) {
            case Constant.GOODS_SETTING :
            case Constant.SERVICE_SETTING:
                rightAdapter.setData(goodsBean.getMsg().get(0).getSortGoods());
                rightAdapter.setDataType(Constant.GOOD_DATA);
                break;
            case Constant.ROOM_SETTING:
                rightAdapter.setData(roomSettingBean.getMsg().get(0).getSortGoods());
                rightAdapter.setDataType(Constant.ROOM_DATA);
                break;
            case Constant.MAN_SETTING:
                rightAdapter.setData(manSettingBean.getMsg().get(0).getSortGoods());
                rightAdapter.setDataType(Constant.SALEMAN_DATA);
                break;
        }
        right.setAdapter(rightAdapter);
    }

    @Override
    public void ClickToReload() {
        initData();
    }
    /**
     * 种类的选择监听
     */
    class KindOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            for (int i = 0; i < kinds.size(); i++) {
                if(i==position) {
                   kinds.get(i).setIsChecken(true);
                    switch (fromeWhere) {
                        case Constant.GOODS_SETTING :
                        case Constant.SERVICE_SETTING:
                            rightAdapter.setData(goodsBean.getMsg().get(position).getSortGoods());
                            rightAdapter.setDataType(Constant.GOOD_DATA);
                            break;
                        case Constant.ROOM_SETTING:
                            rightAdapter.setData(roomSettingBean.getMsg().get(position).getSortGoods());
                            rightAdapter.setDataType(Constant.ROOM_DATA);
                            break;
                        case Constant.MAN_SETTING:
                            rightAdapter.setData(manSettingBean.getMsg().get(position).getSortGoods());
                            rightAdapter.setDataType(Constant.SALEMAN_DATA);
                            break;
                    }
                }else {
                   kinds.get(i).setIsChecken(false);
                }
            }
            leftAdapter.notifyDataSetChanged();
            rightAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title :
                finish();
                break;
        }
    }
    class KindsAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return kinds.size();
        }

        @Override
        public Object getItem(int position) {
            return kinds.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null) {
                holder=new ViewHolder();
                convertView=View.inflate(BaseSettingActivity.this,R.layout.choose_store_item,null);
                holder.kindName= (TextView) convertView.findViewById(R.id.tv_store);
                holder.iv_left_line= (ImageView) convertView.findViewById(R.id.iv_left_line);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }
            GoodKinds goodKinds = kinds.get(position);
            if(goodKinds.isChecken()) {
                convertView.setBackgroundColor(Color.WHITE);
                holder.iv_left_line.setVisibility(View.VISIBLE);
            }else {
                holder.iv_left_line.setVisibility(View.GONE);
                convertView.setBackgroundColor(Color.rgb(242,242,242));
            }
            holder.kindName.setText(goodKinds.getName());
            return convertView;
        }


        class ViewHolder {
            TextView kindName;
            ImageView iv_left_line;
        }
    }
    @Override
    protected void onPause() {
//        EventBus.getDefault().post((Serializable)kinds);
        super.onPause();

    }
    public void loadFailure(String e){
        tv_loading.setText(e);
        pb_loading.setVisibility(View.GONE);
    }
    public void reLoad(){
        pb_loading.setVisibility(View.VISIBLE);
        tv_loading.setText("拼命加载中...");
    }
}
