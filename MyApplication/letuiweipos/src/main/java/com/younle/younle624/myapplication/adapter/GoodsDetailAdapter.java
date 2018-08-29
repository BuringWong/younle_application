package com.younle.younle624.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.manager.ordersetting.SettingItemDetailActivity;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.domain.setting.Goods;
import com.younle.younle624.myapplication.domain.setting.ManSettingBean;
import com.younle.younle624.myapplication.domain.setting.RoomSettingBean;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.view.SlideLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;

/**
 * Created by 我是奋斗 on 2016/6/16.
 * 微信/e-mail:tt090423@126.com
 */
public class GoodsDetailAdapter<T> extends BaseAdapter {
    private Context context;
    private List<T> data;
    private SlideLayout openedLayout;
    private SlideLayout.OnStateChangeListener onStateChangeListener=new SlideLayout.OnStateChangeListener() {
        @Override
        public void onOpen(SlideLayout layout) {
            openedLayout=layout;//保存打开的item
        }

        @Override
        public void onClose(SlideLayout layout) {
            if(layout==openedLayout) {
                openedLayout=null;//将保存Item置空
            }
        }
        @Override
        public void onDown(SlideLayout layout) {
            if (openedLayout != null && layout != openedLayout) { //如果按下不是打开的item
                openedLayout.closeMenu(); //将其关闭
            }
        }
    };
    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position= (int) v.getTag();
            switch (v.getId()) {
                case R.id.tv_item_menu :
                    data.remove(position);
                    notifyDataSetChanged();
                    if(openedLayout!=null) {
                        openedLayout.closeMenu();
                    }
                    break;
                case R.id.tv_item_content:

                    break;
            }
        }
    };
    private int type=-1;
    private String paraId;
    private String paraUsable;

    public GoodsDetailAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<T> data) {
        this.data=data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null) {
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.setting_item_layout,null);
            holder.goodName= (TextView) convertView.findViewById(R.id.tv_item_content);
            holder.delete= (TextView) convertView.findViewById(R.id.tv_item_menu);
            holder.mark= (TextView) convertView.findViewById(R.id.tv_item_mark);
            holder.tv_item_price= (TextView) convertView.findViewById(R.id.tv_item_price);
            holder.rl_item_total_layout= (RelativeLayout) convertView.findViewById(R.id.rl_item_total_layout);
            holder.tv_aviable= (TextView) convertView.findViewById(R.id.tv_aviable);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        switch (type) {
            case  Constant.GOOD_DATA:
            case Constant.SERVICE_DATA:
                Goods.MsgBean.SortGoodsBean sortGoodsBean = (Goods.MsgBean.SortGoodsBean) data.get(position);
                holder.goodName.setText(sortGoodsBean.getName());
                holder.tv_item_price.setText("￥" + sortGoodsBean.getPrice());
                if("1".equals(sortGoodsBean.getUsable())) {//可用
                    holder.tv_aviable.setText("可用");
                    holder.tv_aviable.setTextColor(Color.rgb(63, 136, 206));
                    holder.mark.setText("设置为不可用");
                }else {
                    holder.tv_aviable.setText("不可用");
                    holder.tv_aviable.setTextColor(Color.rgb(153, 153, 153));
                    holder.mark.setText("设置为可用");
                }
                holder.delete.setVisibility(View.GONE);
                break;
            case Constant.ROOM_DATA:
              RoomSettingBean.MsgBean.SortGoodsBean roomBean= (RoomSettingBean.MsgBean.SortGoodsBean) data.get(position);
                holder.goodName.setText(roomBean.getName());
                if("1".equals(roomBean.getUsable())) {//可用
                    holder.tv_aviable.setText("可用");
                    holder.tv_aviable.setTextColor(Color.rgb(63, 136, 206));
                    holder.mark.setText("设置为不可用");
                }else {
                    holder.tv_aviable.setText("不可用");
                    holder.tv_aviable.setTextColor(Color.rgb(153, 153, 153));
                    holder.mark.setText("设置为可用");
                }
                holder.tv_item_price.setVisibility(View.GONE);
                holder.delete.setVisibility(View.GONE);
                break;
            case Constant.SALEMAN_DATA:
                ManSettingBean.MsgBean.SortGoodsBean manBean= (ManSettingBean.MsgBean.SortGoodsBean) data.get(position);
                holder.goodName.setText(manBean.getPname());
                if("1".equals(manBean.getUsable())) {//可用
                    holder.tv_aviable.setText("可用");
                    holder.tv_aviable.setTextColor(Color.rgb(63, 136, 206));
                    holder.mark.setText("设置为不可用");
                }else {
                    holder.tv_aviable.setText("不可用");
                    holder.tv_aviable.setTextColor(Color.rgb(153, 153, 153));
                    holder.mark.setText("设置为可用");
                }
                holder.tv_item_price.setVisibility(View.GONE);
                holder.delete.setVisibility(View.GONE);
                break;
        }
        holder.mark.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //先提交到网络
                switch (type) {
                    case Constant.GOOD_DATA:
                    case Constant.SERVICE_DATA:
                        Goods.MsgBean.SortGoodsBean sortGoodsBean = (Goods.MsgBean.SortGoodsBean) data.get(position);
                        paraId = sortGoodsBean.getId();
                        if("1".equals(sortGoodsBean.getUsable())) {
                            paraUsable="0";
                        }else {
                            paraUsable="1";
                        }
                        break;
                    case Constant.ROOM_DATA:
                        RoomSettingBean.MsgBean.SortGoodsBean roomBean= (RoomSettingBean.MsgBean.SortGoodsBean) data.get(position);
                        paraId=roomBean.getId();
                        if("1".equals(roomBean.getUsable())) {
                            paraUsable="0";
                        }else {
                            paraUsable="1";
                        }
                        break;
                    case Constant.SALEMAN_DATA:
                        ManSettingBean.MsgBean.SortGoodsBean manBean= (ManSettingBean.MsgBean.SortGoodsBean) data.get(position);
                        paraId=manBean.getId();
                        if("1".equals(manBean.getUsable())) {
                            paraUsable="0";
                        }else {
                            paraUsable="1";
                        }
                        break;
                }
                //1.将选择的上传至服务器
                OkHttpUtils.post()
                        .url(UrlConstance.CHANGE_SETTING_STATUS)
                        .addParams("storeid",Constant.STORE_ID)
                        .addParams("goodsid",paraId)
                        .addParams("usable",paraUsable)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {
                                Utils.showToast(context, "网络异常，更改状态失败");
                            }
                            @Override
                            public void onResponse(String response) {
                                boolean toNextStep = Utils.checkSaveToken((Activity) context, response);
                                if (toNextStep){
                                    praseJson(response, position);
                                }
                            }
                        });

            }
        });
        holder.rl_item_total_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SettingItemDetailActivity.class);
                switch (type) {
                    case Constant.GOOD_DATA:
                        Goods.MsgBean.SortGoodsBean sortGoodsBean = (Goods.MsgBean.SortGoodsBean) data.get(position);
                        intent.putExtra(Constant.FROME_WHERE, Constant.GOOD_DATA);
                        intent.putExtra(Constant.ORDER_BEAN,sortGoodsBean);
                        break;
                    case Constant.SERVICE_DATA:
                        sortGoodsBean = (Goods.MsgBean.SortGoodsBean) data.get(position);
                        intent.putExtra(Constant.FROME_WHERE, Constant.SERVICE_DATA);
                        intent.putExtra(Constant.ORDER_BEAN,sortGoodsBean);
                        break;
                    case Constant.ROOM_DATA:
                        RoomSettingBean.MsgBean.SortGoodsBean roomBean= (RoomSettingBean.MsgBean.SortGoodsBean) data.get(position);
                        intent.putExtra(Constant.FROME_WHERE, Constant.ROOM_DATA);
                        intent.putExtra(Constant.ORDER_BEAN,roomBean);
                        break;
                    case Constant.SALEMAN_DATA:
                        ManSettingBean.MsgBean.SortGoodsBean manBean= (ManSettingBean.MsgBean.SortGoodsBean) data.get(position);
                        intent.putExtra(Constant.FROME_WHERE, Constant.SALEMAN_DATA);
                        intent.putExtra(Constant.ORDER_BEAN,manBean);
                        break;
                }
                context.startActivity(intent);
            }
        });
        SlideLayout slideLayout= (SlideLayout) convertView;
        slideLayout.setOnStateChangeListener(onStateChangeListener);
        if(openedLayout!=null) {
            openedLayout.closeMenu();
        }
        return convertView;
    }

    private void changeStatus(int position) {
        switch (type) {
            case Constant.GOOD_DATA:
                Goods.MsgBean.SortGoodsBean sortGoodsBean = (Goods.MsgBean.SortGoodsBean) data.get(position);
                if("1".equals(sortGoodsBean.getUsable())) {
                    sortGoodsBean.setUsable("0");
                }else {
                    sortGoodsBean.setUsable("1");
                }
                break;
            case Constant.SERVICE_DATA:
                sortGoodsBean = (Goods.MsgBean.SortGoodsBean) data.get(position);
                if("1".equals(sortGoodsBean.getUsable())) {
                    sortGoodsBean.setUsable("0");
                }else {
                    sortGoodsBean.setUsable("1");
                }
                break;
            case Constant.ROOM_DATA:
                RoomSettingBean.MsgBean.SortGoodsBean roomBean= (RoomSettingBean.MsgBean.SortGoodsBean) data.get(position);
                if("1".equals(roomBean.getUsable())) {
                    roomBean.setUsable("0");
                }else {
                    roomBean.setUsable("1");
                }
                break;
            case Constant.SALEMAN_DATA:
                ManSettingBean.MsgBean.SortGoodsBean manBean= (ManSettingBean.MsgBean.SortGoodsBean) data.get(position);
                if("1".equals(manBean.getUsable())) {
                    manBean.setUsable("0");
                }else {
                    manBean.setUsable("1");
                }
                break;
        }
        openedLayout.closeMenu();
        GoodsDetailAdapter.this.notifyDataSetChanged();
    }

    private void praseJson(String json, int position) {
        try {
            JSONObject jsonObject=new JSONObject(json);
            int code = jsonObject.getInt("code");
            if(200==code) {
                changeStatus(position);
            }else {
                String msg = jsonObject.getString("msg");
                Utils.showToast(context,msg);
                openedLayout.closeMenu();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            if(openedLayout!=null&&openedLayout.isShown()) {
                openedLayout.closeMenu();
            }
        }

    }

    public void setDataType(int type) {
        this.type=type;
    }

    class ViewHolder{
        TextView goodName;
        TextView tv_item_price;
        TextView delete;
        TextView mark;
        RelativeLayout rl_item_total_layout;
        TextView tv_aviable;
    }

}
