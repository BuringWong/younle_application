package com.younle.younle624.myapplication.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.domain.orderbean.WaiterLeavelBean;

import java.util.List;

/**
 * Created by 我是奋斗 on 2016/7/11.
 * 微信/e-mail:tt090423@126.com
 * 选择服务，绑定服务人员的dialog的右侧的listview的adapter
 */
public class WaiterRightAdapter extends BaseAdapter {
    private  Activity context;
    private int type=-1;
    private List<WaiterLeavelBean.Waiter> data;
    private String orderId;

    public WaiterRightAdapter(Activity context) {
        this.context=context;
    }

    public void setDataType(int type) {
        this.type=type;
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
        if(convertView==null) {
            convertView = View.inflate(context, R.layout.waiter_choose_item, null);
            holder=new ViewHolder();
            holder.tv_waiter_name= (TextView) convertView.findViewById(R.id.tv_waiter_name);
            holder.tv_live_item= (TextView) convertView.findViewById(R.id.tv_live_item);
            holder.tv_live_order_time= (TextView) convertView.findViewById(R.id.tv_live_order_time);
            holder.btn_add= (Button) convertView.findViewById(R.id.btn_add);
            holder.btn_minus= (Button) convertView.findViewById(R.id.btn_minus);
            holder.tv_num= (TextView) convertView.findViewById(R.id.tv_num);
            holder.tv_isusing= (TextView) convertView.findViewById(R.id.tv_isusing);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        WaiterLeavelBean.Waiter waiter = data.get(position);
        holder.tv_waiter_name.setText(waiter.getName());

        if(!waiter.isAviable()) {//不可用
            holder.tv_isusing.setText("占用");
            holder.tv_isusing.setTextColor(Color.WHITE);
            holder.btn_minus.setVisibility(View.GONE);
            holder.tv_num.setVisibility(View.GONE);
            holder.btn_add.setVisibility(View.GONE);
            holder.tv_live_item.setVisibility(View.VISIBLE);
            holder.tv_live_order_time.setVisibility(View.VISIBLE);
            holder.tv_live_item.setText("项目：" + waiter.getServiceItem());
            holder.tv_live_order_time.setText("下单时间：" + waiter.getOrderTime());
            holder.tv_isusing.setBackgroundColor(Color.rgb(153, 153, 153));
            convertView.setBackgroundColor(Color.rgb(240, 240, 240));
        }else {//可用
            if(waiter.getCount()==0) {
                holder.tv_num.setVisibility(View.GONE);
                holder.btn_minus.setVisibility(View.GONE);
            }else {
                holder.btn_minus.setVisibility(View.VISIBLE);
                holder.tv_num.setVisibility(View.VISIBLE);
                holder.tv_num.setText(waiter.getCount()+"");
            }
            holder.btn_add.setVisibility(View.VISIBLE);
            holder.tv_isusing.setTextColor(Color.WHITE);
            holder.tv_isusing.setBackgroundColor(Color.rgb(71, 202, 235));
            holder.tv_isusing.setText("可选");
            holder.tv_live_item.setVisibility(View.GONE);
            holder.tv_live_order_time.setVisibility(View.GONE);
            convertView.setBackgroundColor(Color.WHITE);
        }
        holder.btn_add.setOnClickListener(new AddMinusOnClickListener(position));
        holder.btn_minus.setOnClickListener(new AddMinusOnClickListener(position));
        return convertView;
    }

    public void setData(List<WaiterLeavelBean.Waiter> waiterLeavelBeanList) {
        this.data=waiterLeavelBeanList;
    }

    public void setServerId(String orderId) {
        this.orderId=orderId;
    }

    class AddMinusOnClickListener implements View.OnClickListener{
        private  int position;
        public AddMinusOnClickListener(int position) {
            this.position=position;
        }
        @Override
        public void onClick(View v) {
            WaiterLeavelBean.Waiter waiter = data.get(position);
            switch (v.getId()) {
                case R.id.btn_add :
                    waiter.setCount(waiter.getCount()+1);
                    break;
                case R.id.btn_minus:
                    waiter.setCount(waiter.getCount() - 1);
                    break;
            }
                //改服务人员正在服务的订单id
                if(waiter.getCount()>0) {
                    waiter.setServerId(orderId);
                }else {
                    waiter.setServerId(null);
                }
                WaiterRightAdapter.this.notifyDataSetChanged();
        }
    }

    class ViewHolder{
        TextView tv_waiter_name;
        TextView tv_live_item;
        TextView tv_live_order_time;
        Button btn_add;
        TextView tv_num;
        Button btn_minus;
        TextView tv_isusing;
    }
}
