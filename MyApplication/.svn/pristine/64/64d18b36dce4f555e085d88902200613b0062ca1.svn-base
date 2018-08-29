package com.younle.younle624.myapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;

import java.util.List;

/**
 * Created by 我是奋斗 on 2016/6/6.
 * 微信/e-mail:tt090423@126.com
 */
public class PrintInfoAdapter extends BaseAdapter {
    private  Context context;
    private List<String> data;

    public PrintInfoAdapter(Context context) {
        this.context=context;
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
            convertView=View.inflate(context, R.layout.print_data_item,null);
            holder=new ViewHolder();
            holder.textView= (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(data.get(position));
        return convertView;
    }


    public void setData(List<String> data) {
        this.data=data;
    }
    class ViewHolder{
        TextView textView;
    }
}
