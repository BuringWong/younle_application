package com.younle.younle624.myapplication.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.domain.FunctionsBean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/18.
 */

public class FunctionsAdapter extends BaseAdapter {
    private List<FunctionsBean.AuthtxtBean> data;
    private Context context;

    public FunctionsAdapter(Context context) {
        this.context = context;
    }
    public void setData(List<FunctionsBean.AuthtxtBean> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null) {
            view=View.inflate(context, R.layout.functions_list_items,null);
            holder=new ViewHolder();
            holder.tv_function_name= (TextView) view.findViewById(R.id.tv_function_name);
            holder.iv_xiadan= (ImageView) view.findViewById(R.id.iv_xiadan);
            holder.iv_zhihui= (ImageView) view.findViewById(R.id.iv_zhihui);
            view.setTag(holder);
        }else {
            holder= (ViewHolder) view.getTag();
        }
        FunctionsBean.AuthtxtBean authtxtBean = data.get(i);
        holder.tv_function_name.setText(authtxtBean.getName());
        if(authtxtBean.getWisdom()==1) {
            holder.iv_zhihui.setBackgroundResource(R.drawable.contains);
        }else {
            holder.iv_zhihui.setBackgroundResource(R.drawable.dis_contains);
        }

        if(authtxtBean.getXd()==1) {
            holder.iv_xiadan.setBackgroundResource(R.drawable.contains);
        }else {
            holder.iv_xiadan.setBackgroundResource(R.drawable.dis_contains);
        }


        if(i%2==0) {
            view.setBackgroundColor(Color.rgb(247,247,247));
        }else {
            view.setBackgroundColor(Color.WHITE);
        }
        return view;
    }
    class ViewHolder{
        TextView tv_function_name;
        ImageView iv_xiadan;
        ImageView iv_zhihui;
    }
}
