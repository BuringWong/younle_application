package com.younle.younle624.myapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.domain.ChartData;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.MChatUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 我是奋斗 on 2016/5/25.
 * 微信/e-mail:tt090423@126.com
 */
public class ChartAdapter extends BaseAdapter {
    private Context context;
    private MChatUtils mChatUtils;
    private List<ChartData> charDatas;

    public ChartAdapter(Context context) {
        this.context=context;
        mChatUtils=new MChatUtils(context);
    }



    @Override
    public int getCount() {
        return charDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return charDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LogUtils.Log("getView()");
        ViewHolder holder;
        if(convertView==null) {
            holder = new ViewHolder();
            convertView=View.inflate(context, R.layout.chart_view,null);
            convertView.setVisibility(View.VISIBLE);
//            holder.left_store= (TextView) convertView.findViewById(R.id.left_store);
            holder.left_store.setVisibility(View.VISIBLE);
            holder.chart1= (CombinedChart) convertView.findViewById(R.id.chart1);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        ChartData chartData = charDatas.get(position);//某一个店的json对象
        List<ChartData.ChartDetailEntity> details = chartData.getDetails();//某一个店某一个时间点对应的数据

//        mChatUtils.initMchat(holder.chart1);

        holder.left_store.setText(charDatas.get(position).getStore());
        List xData = getXData(details);

        List<BarEntry> barEntities=getBarEntries(details);
        List<Entry> entryList=getLinEntryList(details);
        mChatUtils.setData(holder.chart1, xData,barEntities,entryList);
        return convertView;
    }

    /**
     * 获取折线的数据
     * @param details
     * @return
     */
    private List<Entry> getLinEntryList(List<ChartData.ChartDetailEntity> details) {
        List<Entry> entryList=new ArrayList<>();
        for (int i = 0; i < details.size(); i++) {
            Entry entry=new Entry((float) details.get(i).getDayOrHourEntity().getOrderNUm(),i);
            entryList.add(entry);
        }

        return entryList;
    }

    /**
     * 获取柱形图的数据
     * @param details
     * @return
     */
    private List<BarEntry> getBarEntries(List<ChartData.ChartDetailEntity> details) {
        List<BarEntry> barEntries=new ArrayList<>();
        for (int i = 0; i < details.size(); i++) {
            BarEntry barEntry=new BarEntry((float) details.get(i).getDayOrHourEntity().getTotalAcc(),i);
            barEntries.add(barEntry);
        }
        return barEntries;
    }

    private List getXData(List<ChartData.ChartDetailEntity> details) {

        List XData=new ArrayList();
        for (int i = 0; i < details.size(); i++) {
            ChartData.ChartDetailEntity chartDetailEntity = details.get(i);
            String date = chartDetailEntity.getDayOrHourEntity().getDate();
            XData.add(date);
        }
        return XData;
    }

    public void setData(List<ChartData> charDatas) {
        this.charDatas=charDatas;
    }

    class ViewHolder{
        TextView left_store;
        CombinedChart chart1;
    }
}
