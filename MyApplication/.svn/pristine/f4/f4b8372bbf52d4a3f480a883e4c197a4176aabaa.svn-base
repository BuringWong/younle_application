package com.younle.younle624.myapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 我是奋斗 on 2016/5/21.
 * 微信/e-mail:tt090423@126.com
 *
 * 订单界面筛选数据的适配器
 */
public class PosFilterAdapter<T> extends BaseAdapter {
    Context context;
    List<T> data=new ArrayList();
    public PosFilterAdapter(Context context) {
        this.context=context;
    }
    public void setData(List data){
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null) {
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.choose_store_item,null);
            holder.tv_store= (TextView) convertView.findViewById(R.id.tv_store);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        if(position==0) {
         convertView.setBackgroundColor(Color.rgb(216,216,216));
        }
            holder.tv_store.setText(data.get(position)+"");
        return convertView;
    }
    class ViewHolder{
        TextView tv_store;
    }
}

