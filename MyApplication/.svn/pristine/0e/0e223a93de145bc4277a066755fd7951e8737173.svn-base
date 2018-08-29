package com.younle.younle624.myapplication.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.domain.Sender;

import java.util.List;

/**
 * Created by bert_dong on 2018/4/23 0023.
 * 邮箱：18701038771@163.com
 */

public class SenderAdapter extends BaseAdapter {
    private Context context;
    private List<Sender.DataBean> data;

    public SenderAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Sender.DataBean> data) {
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
        ViewHolder holder=null;
        if(holder==null) {
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.sender_item,null);
            holder.name= (TextView) convertView.findViewById(R.id.sender_name);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        Sender.DataBean sender = data.get(position);
        holder.name.setText(sender.getName());
        if(sender.isSelected()) {
            holder.name.setTextColor(Color.parseColor("#3f88ce"));
        }else {
            holder.name.setTextColor(Color.parseColor("#333333"));
        }
        return convertView;
    }
    class ViewHolder{
        TextView name;
    }
}
