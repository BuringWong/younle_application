package com.younle.younle624.myapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.RoomsInfoBean;
import com.younle.younle624.myapplication.view.MyListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 分层计费的 PopWindow 专用adapter
 */
public class PopRoomFeeAdapter extends BaseAdapter {

    private List<RoomsInfoBean.MsgBean.RoominfoBean.RoomlistBean.RuleBean> data;
    private int fee_rule=-1;
    private Context context;
    //private String TAG = "PopRoomFeeAdapter";
    public PopRoomFeeAdapter(Context context) {
        this.context=context;
    }

    public void setFeeRule(int fee_rule) {
        this.fee_rule = fee_rule;
    }

    public void setData(List data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        if(data != null && data.size() > 0) {
            //LogUtils.e(TAG,"getCount() data.size()="+data.size());
            return data.size();
        }else {
            return 0;
        }
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
                String week_str = "";
                switch (orderListBean.getWday()){
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
                    String format = df4.format(Long.valueOf(orderListBean.getWday() + "000"));
                    holder.tv_week_name.setText(format);
                }else{
                    holder.tv_week_name.setText(week_str);
                }
                GridAdapter gridAdapter_hour = new GridAdapter(context,orderListBean.getInfo());
                holder.lv_pop_room_fee.setAdapter(gridAdapter_hour);
                break;
            case Constant.ORDER_ROOM_FEE_DAY:
                String week_str2 = "";
                switch (orderListBean.getWday()){
                    case 0:
                        week_str2 = "周日";
                        break;
                    case 1:
                        week_str2 = "周一";
                        break;
                    case 2:
                        week_str2 = "周二";
                        break;
                    case 3:
                        week_str2 = "周三";
                        break;
                    case 4:
                        week_str2 = "周四";
                        break;
                    case 5:
                        week_str2 = "周五";
                        break;
                    case 6:
                        week_str2 = "周六";
                        break;
                }
                if("".equals(week_str2)){
                    DateFormat df4 = new SimpleDateFormat("MM月dd日");
                    String format = df4.format(Long.valueOf(orderListBean.getWday() + "000"));
                    holder.tv_week_name.setText(format);
                }else{
                    holder.tv_week_name.setText(week_str2);
                }
                holder.tv_week_name.setVisibility(View.GONE);
                GridAdapter gridAdapter_day = new GridAdapter(context,orderListBean.getInfo());
                holder.lv_pop_room_fee.setAdapter(gridAdapter_day);
                break;
        }
        return convertView;
    }

    /**
     * 二级列表
     */
    class GridAdapter extends BaseAdapter{
        private List<RoomsInfoBean.MsgBean.RoominfoBean.RoomlistBean.RuleBean.InfoBean> detailBean;
        private Context context;

        public GridAdapter(Context context, List<RoomsInfoBean.MsgBean.RoominfoBean.RoomlistBean.RuleBean.InfoBean> detailBean) {
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
                holder.ll_infos = (LinearLayout) convertView.findViewById(R.id.ll_infos);
                holder.ll_infos.setEnabled(false);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            RoomsInfoBean.MsgBean.RoominfoBean.RoomlistBean.RuleBean.InfoBean feeInfoBean = this.detailBean.get(position);
            switch (fee_rule){
                case Constant.ORDER_ROOM_FEE_HOUR:
                    holder.tv_consumption_time_period.setText(feeInfoBean.getTimes());
                    holder.tv_consumer_price.setText(feeInfoBean.getCost()+"元/小时");
                break;
                case Constant.ORDER_ROOM_FEE_DAY:
                    holder.tv_consumption_time_period.setText(feeInfoBean.getTimes());
                    holder.tv_consumer_price.setText(feeInfoBean.getCost()+"元/天");
                break;
            }
            return convertView;
        }
    }

    class ViewHolder{
        TextView tv_week_name;
        MyListView lv_pop_room_fee;
        TextView tv_consumption_time_period;
        TextView tv_consumer_price;
        LinearLayout ll_infos;
    }
}
