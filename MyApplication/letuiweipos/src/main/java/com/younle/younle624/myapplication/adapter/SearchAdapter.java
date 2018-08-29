package com.younle.younle624.myapplication.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tencent.lbssearch.object.result.SearchResultObject;
import com.younle.younle624.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 我是奋斗 on 2016/5/17.
 * 微信/e-mail:tt090423@126.com
 *
 * 搜索内容得adapter
 */
public class SearchAdapter extends BaseAdapter {

    List<SearchResultObject.SearchResultData> data;
    private Context context;

    public SearchAdapter(Context context) {
        this.context=context;
        data=new ArrayList<>();
    }

    public void setData(List<SearchResultObject.SearchResultData> data) {
        this.data=data;
    }
    public void addData(List<SearchResultObject.SearchResultData> data) {
        this.data.addAll(data);
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
            convertView=View.inflate(context, R.layout.around_item,null);
            holder=new ViewHolder();
            holder.around_add= (TextView) convertView.findViewById(R.id.around_add);
            holder.around_name= (TextView) convertView.findViewById(R.id.around_name);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.around_name.setText(data.get(position).title);
        if(!TextUtils.isEmpty(data.get(position).address)) {
            holder.around_add.setText(data.get(position).address);
        }
        return convertView;
    }
    class ViewHolder{
        TextView around_name;
        TextView around_add;
    }

}
