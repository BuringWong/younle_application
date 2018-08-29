package com.younle.younle624.myapplication.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tencent.lbssearch.object.result.SearchResultObject;
import com.tencent.map.geolocation.TencentPoi;
import com.younle.younle624.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 我是奋斗 on 2016/5/16.
 * 微信/e-mail:tt090423@126.com
 */
public class XlistAdapter extends BaseAdapter {

    private Context context;
    private List<SearchResultObject.SearchResultData> data;
    private List<TencentPoi> poiList;
    private List<SearchResultObject.SearchResultData> nearData;
    /**
     * 定位周边的数据
     */
    private int TYPE=1;

    public XlistAdapter(Context context) {
        this.context=context;
        data=new ArrayList<>();
    }

    public void setNearData(List<SearchResultObject.SearchResultData> nearData, int type) {
        this.nearData=nearData;
        this.TYPE=type;
    }

    public void setData(List<TencentPoi> poiList,int type){
        this.poiList=poiList;
        this.TYPE=type;
    }
    public void setData2(List<SearchResultObject.SearchResultData> poiList,int type){
        this.data=poiList;
        this.TYPE=type;
    }
    public void addData(List<SearchResultObject.SearchResultData> data, int type) {
        this.data.addAll(data);
        this.TYPE=type;
    }


    @Override
    public int getCount() {
        if(TYPE==1) {
            return poiList.size();
        }else if(TYPE==2) {
            return data.size();
        }else if(TYPE==3) {
            return nearData.size();
        }else {
            return 0;
        }

    }

    @Override
    public Object getItem(int position) {
        if(TYPE==1) {
            return poiList.get(position);
        }else if(TYPE==2) {
            return data.get(position);
        } else if(TYPE==3) {
            return nearData.get(position);
        }else {
            return 1000;
        }


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
        if(TYPE==1) {//搜索周边
            holder.around_name.setText(poiList.get(position).getName());
            holder.around_add.setText(poiList.get(position).getAddress());

        }else if(TYPE==2) {//关键字搜索
            holder.around_name.setText(data.get(position).title);
            if(!TextUtils.isEmpty(data.get(position).address)) {
                holder.around_add.setText(data.get(position).address);
            }
        }else if(TYPE==3) {//搜索附近
            holder.around_name.setText(nearData.get(position).title);
            if(!TextUtils.isEmpty(nearData.get(position).address)) {
                holder.around_add.setText(nearData.get(position).address);
            }
        }

        return convertView;
    }




    class ViewHolder{
        TextView around_name;
        TextView around_add;
    }
}
