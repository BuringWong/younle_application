package com.younle.younle624.myapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.RoomsInfoBean;
import com.younle.younle624.myapplication.view.MyListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by bert_dong on 2017/3/28 0028.
 * 邮箱：18701038771@163.com
 * 房间桌台多种计费规则的展示
 */
public class PopFeeInfoAdapter extends BaseAdapter {

    private Context context;
    private int fee_rule;
    private List<RoomsInfoBean.MsgBean.RoominfoBean.RoomlistBean.RuleBean> data;
    private boolean showMemberPrice;

    public PopFeeInfoAdapter(Context context) {
        this.context = context;
    }
    public void setFeeRule(int fee_rule) {
        this.fee_rule = fee_rule;
    }
    public void setData(List<RoomsInfoBean.MsgBean.RoominfoBean.RoomlistBean.RuleBean> data) {
        this.data = data;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.pop_first_level_week, null);
            holder.tv_week_name = (TextView) convertView.findViewById(R.id.tv_week_name);
            holder.lv_pop_room_fee = (MyListView) convertView.findViewById(R.id.lv_pop_room_fee);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        RoomsInfoBean.MsgBean.RoominfoBean.RoomlistBean.RuleBean orderListBean = data.get(position);
        switch (fee_rule){
            case Constant.ORDER_ROOM_FEE_HOUR:
                holder.tv_week_name.setText(getWeekOrDate(orderListBean.getWday()));
                SecAdapter adapter_hour = new SecAdapter(context,orderListBean.getInfo());
                holder.lv_pop_room_fee.setAdapter(adapter_hour);
                break;
            case Constant.ORDER_ROOM_FEE_DAY:
                holder.tv_week_name.setVisibility(View.GONE);
                //holder.tv_week_name.setText(getWeekOrDate(orderListBean.getWday()));
                SecAdapter adapter_day = new SecAdapter(context,orderListBean.getInfo());
                holder.lv_pop_room_fee.setAdapter(adapter_day);
                break;
        }
        return convertView;
    }

    /**
     * 二级列表
     */
    class SecAdapter extends BaseAdapter{
        private List<RoomsInfoBean.MsgBean.RoominfoBean.RoomlistBean.RuleBean.InfoBean> detailBean;
        private Context context;

        public SecAdapter(Context context, List<RoomsInfoBean.MsgBean.RoominfoBean.RoomlistBean.RuleBean.InfoBean> detailBean) {
            this.detailBean=detailBean;
            this.context=context;
        }

        @Override
        public int getCount() {
            return detailBean.size();
        }

        @Override
        public Object getItem(int position) {
            return detailBean.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.pop_sec_level_fee_info, null);
                holder.tv_consumption_time_period = (TextView) convertView.findViewById(R.id.tv_consumption_time_period);
                holder.tv_consumer_price = (TextView) convertView.findViewById(R.id.tv_consumer_price);
                holder.member_price_logo= (ImageView) convertView.findViewById(R.id.member_price_logo);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            RoomsInfoBean.MsgBean.RoominfoBean.RoomlistBean.RuleBean.InfoBean feeInfoBean = this.detailBean.get(position);

            if(0==feeInfoBean.getIsvip()) {
                holder.member_price_logo.setVisibility(View.GONE);
            }else {
                holder.member_price_logo.setVisibility(View.VISIBLE);
            }
            switch (fee_rule){
                case Constant.ORDER_ROOM_FEE_HOUR:
                    holder.tv_consumption_time_period.setText(feeInfoBean.getTimes());
                    holder.tv_consumer_price.setText(feeInfoBean.getCost()+"元/小时");
                    break;
                case Constant.ORDER_ROOM_FEE_DAY:
                    holder.tv_consumption_time_period.setText(getWeekOrDate(feeInfoBean.getWday()));
                    holder.tv_consumer_price.setText(feeInfoBean.getCost()+"元/天");
                    break;
            }
            return convertView;
        }
    }

    private String getWeekOrDate(int date) {
        String week_str = "";
        switch (date){
            case 0:
                week_str = "周日";
                break;
            case 1:
                week_str = "周一";
                break;
            case 2:
                week_str = "周二";
                break;
            case 3:
                week_str = "周三";
                break;
            case 4:
                week_str = "周四";
                break;
            case 5:
                week_str = "周五";
                break;
            case 6:
                week_str = "周六";
                break;
        }
        if("".equals(week_str)){
            DateFormat df4 = new SimpleDateFormat("MM月dd日");
            week_str = df4.format(Long.valueOf(date + "000"));
        }
        return week_str;
    }
    class ViewHolder{
        TextView tv_week_name;
        MyListView lv_pop_room_fee;
        TextView tv_consumption_time_period;
        TextView tv_consumer_price;
        ImageView member_price_logo;
    }
}
