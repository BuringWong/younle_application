package com.younle.younle624.myapplication.activity.manager.ordersetting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.setting.Goods;
import com.younle.younle624.myapplication.domain.setting.ManSettingBean;
import com.younle.younle624.myapplication.domain.setting.RoomSettingBean;

public class SettingItemDetailActivity extends Activity {
    private ImageView iv_title;
    private TextView tv_title;
    private RelativeLayout rl_1;
    private RelativeLayout rl_2;
    private RelativeLayout rl_3;
    private RelativeLayout rl_4;
    private TextView tv_name1;
    private TextView tv_name2;
    private TextView tv_name3;
    private TextView tv_name4;
    private TextView tv_content1;
    private TextView tv_content2;
    private TextView tv_content3;
    private TextView tv_content4;
    private int fromWhere;
    private Goods.MsgBean.SortGoodsBean  goodsBean;
    private RoomSettingBean.MsgBean.SortGoodsBean roomBean;
    private  ManSettingBean.MsgBean.SortGoodsBean manBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_item_detail);
        getDataFromIntent();
        initView();
        initData();
    }

    private void getDataFromIntent() {
        fromWhere = getIntent().getIntExtra(Constant.FROME_WHERE, -1);
        switch (fromWhere) {
            case Constant.SERVICE_DATA :
                goodsBean = (Goods.MsgBean.SortGoodsBean) getIntent().getSerializableExtra(Constant.ORDER_BEAN);
                break;
            case Constant.GOOD_DATA :
                goodsBean = (Goods.MsgBean.SortGoodsBean) getIntent().getSerializableExtra(Constant.ORDER_BEAN);
                break;
            case Constant.ROOM_DATA :
                roomBean = (RoomSettingBean.MsgBean.SortGoodsBean) getIntent().getSerializableExtra(Constant.ORDER_BEAN);
                break;
            case Constant.SALEMAN_DATA :
                manBean = (ManSettingBean.MsgBean.SortGoodsBean) getIntent().getSerializableExtra(Constant.ORDER_BEAN);
                break;
        }
    }

    private void initView() {
        iv_title = (ImageView)findViewById(R.id.iv_title);
        iv_title.setVisibility(View.VISIBLE);
        tv_title = (TextView)findViewById(R.id.tv_title);
        rl_1 = (RelativeLayout)findViewById(R.id.rl_1);
        rl_2 = (RelativeLayout)findViewById(R.id.rl_2);
        rl_3 = (RelativeLayout)findViewById(R.id.rl_3);
        rl_4 = (RelativeLayout)findViewById(R.id.rl_4);
        tv_name1 = (TextView)findViewById(R.id.tv_name1);
        tv_name2 = (TextView)findViewById(R.id.tv_name2);
        tv_name3 = (TextView)findViewById(R.id.tv_name3);
        tv_name4 = (TextView)findViewById(R.id.tv_name4);
        tv_content1 = (TextView)findViewById(R.id.tv_content1);
        tv_content2 = (TextView)findViewById(R.id.tv_content2);
        tv_content3 = (TextView)findViewById(R.id.tv_content3);
        tv_content4 = (TextView)findViewById(R.id.tv_content4);
    }
    private void initData() {
        switch (fromWhere) {
            case Constant.SERVICE_DATA :
                tv_title.setText("服务商品详情");
                tv_name1.setText("服务名称：");
                tv_content1.setText(goodsBean.getName());
                tv_name2.setText("服务价格：");
                tv_content2.setText(goodsBean.getPrice());
                rl_3.setVisibility(View.GONE);
                rl_4.setVisibility(View.GONE);
                break;
            case Constant.GOOD_DATA :
                tv_title.setText("实物商品详情 ");
                tv_name1.setText("商品名称：");
                tv_content1.setText(goodsBean.getName());
                tv_name2.setText("商品价格：");
                tv_content2.setText(goodsBean.getPrice());
                rl_3.setVisibility(View.GONE);
                rl_4.setVisibility(View.GONE);
                break;
            case Constant.ROOM_DATA :
                tv_title.setText("房间详情");
                tv_name1.setText("房间名称：");
                tv_content1.setText(roomBean.getName());
                tv_name2.setText("房间价格：");
                tv_content2.setText(roomBean.getPrice());
                tv_name3.setText("最低消费：");
                tv_content3.setText(roomBean.getMinConsume());
                tv_name4.setText("押金：");
                tv_content4.setText("￥"+roomBean.getDeposit());
                break;
            case Constant.SALEMAN_DATA :
                tv_title.setText("人员详情");
                tv_name1.setText("人员名称：");
                tv_content1.setText(manBean.getPname());
                tv_name2.setText("人员收费标准：");
                tv_content2.setText(manBean.getSname());
                rl_3.setVisibility(View.GONE);
                rl_4.setVisibility(View.GONE);
                break;
        }
    }
}
