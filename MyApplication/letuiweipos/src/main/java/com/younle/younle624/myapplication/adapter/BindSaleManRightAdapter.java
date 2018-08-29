package com.younle.younle624.myapplication.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.orderbean.OrderBean;
import com.younle.younle624.myapplication.domain.orderbean.SaleMan;

import java.util.List;

/**
 * Created by 我是奋斗 on 2016/5/17.
 * 微信/e-mail:tt090423@126.com
 *
 * 联网请求地址后的适配器
 */
public class BindSaleManRightAdapter extends BaseAdapter {
    private  Handler handler;
    private  OrderBean orderBean;
    private Context context;
    private List<SaleMan.SalesBean.ContentBean> saleMen;

    public BindSaleManRightAdapter(Context context, OrderBean orderBean, Handler handler) {
        this.context=context;
        this.orderBean=orderBean;
        this.handler=handler;
    }

    public void setData(List<SaleMan.SalesBean.ContentBean> saleMen) {
        this.saleMen=saleMen;
    }
    @Override
    public int getCount() {
        return saleMen.size();
    }

    @Override
    public Object getItem(int position) {
        return saleMen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null) {
            convertView=View.inflate(context, R.layout.sale_man_right_item,null);
            holder=new ViewHolder();
            holder.tv_store= (TextView) convertView.findViewById(R.id.tv_store);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.tv_store.setText(saleMen.get(position).getSalename());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderBean.setSaleMan(saleMen.get(position));
                handler.sendEmptyMessage(Constant.FINISH_BIND);
            }
        });
        return convertView;
    }

    class ViewHolder{
        TextView tv_store;
    }

}
