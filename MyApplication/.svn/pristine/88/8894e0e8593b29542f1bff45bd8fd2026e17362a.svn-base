package com.younle.younle624.myapplication.adapter.ordermanager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.ChartDataBean;
import com.younle.younle624.myapplication.domain.EntityOrderBean;
import com.younle.younle624.myapplication.domain.GoodKinds;
import com.younle.younle624.myapplication.domain.PosChartBean;
import com.younle.younle624.myapplication.domain.PosOrderBean;
import com.younle.younle624.myapplication.domain.PosOrderKinds;
import com.younle.younle624.myapplication.domain.ServerOrderBean;
import com.younle.younle624.myapplication.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 我是奋斗 on 2016/5/21.
 * 微信/e-mail:tt090423@126.com
 * <p/>
 * 订单界面筛选数据的适配器
 */
public class FilterAdapter<T> extends BaseAdapter {
    boolean firstTime = true;
    Context context;
    List<T> data = new ArrayList();
    private int dataType = -1;

    public FilterAdapter(Context context) {
        this.context = context;
    }

    public void setData(List data) {
        this.data = data;
    }

    public void setDataFrom(int dataType) {
        this.dataType = dataType;
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.choose_store_item, null);
            holder.tv_store = (TextView) convertView.findViewById(R.id.tv_store);
            holder.iv_left_line = (ImageView) convertView.findViewById(R.id.iv_left_line);
            holder.line = convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        switch (dataType) {
            case Constant.ORDER_ENTITY_LEFT://实物订单左侧筛选数据的填充
                EntityOrderBean.GoodsNameBean entityLeftBean = (EntityOrderBean.GoodsNameBean) data.get(position);
                holder.tv_store.setText(entityLeftBean.getName());
                holder.tv_store.setGravity(Gravity.CENTER);
                break;
            case Constant.POS_ORDER_CHOOSE_SERVICE_GOODS://服务订单左侧筛选数据的填充
                ServerOrderBean.MsgBean.GoodsNameBean serviceLeftBean = (ServerOrderBean.MsgBean.GoodsNameBean) data.get(position);
                holder.tv_store.setText(serviceLeftBean.getName());
                holder.tv_store.setGravity(Gravity.CENTER);
                break;
            case Constant.CHART_ENTITY_LEFT://视图图形列表右侧的筛选
                ChartDataBean.MsgBean.GoodsNameBean left_bean = (ChartDataBean.MsgBean.GoodsNameBean) data.get(position);
                holder.tv_store.setText(left_bean.getName());
                holder.tv_store.setGravity(Gravity.CENTER);
                break;
            case Constant.POS_ORDER_CHOOSE_CHART://右侧筛选的下拉列表
                PosOrderKinds payway = (PosOrderKinds) data.get(position);
                holder.tv_store.setText(payway.getName());
                holder.tv_store.setGravity(Gravity.CENTER);
                break;
            case Constant.ORDER_SERVICE_LIST_FILTER:
                ServerOrderBean.MsgBean.GoodsNameBean serviceNameBean = (ServerOrderBean.MsgBean.GoodsNameBean) data.get(position);
                holder.tv_store.setText(serviceNameBean.getName());
                break;
            case Constant.CHART_SERVICE_LIST_FILTER:
                ChartDataBean.MsgBean.GoodsNameBean detilBean1 = (ChartDataBean.MsgBean.GoodsNameBean) data.get(position);
                holder.tv_store.setText(detilBean1.getName());
                break;
          /*  case Constant.CHART_POS_LIST_FILTER:
                PosChartBean.MsgBean.SectorBean sectorBean = (PosChartBean.MsgBean.SectorBean) data.get(position);
                holder.tv_store.setText(sectorBean.getName());
                break;*/
            case Constant.POS_KINDS://设置一级目录中的种类（双listview左侧）
                firstKindsSetting(position, convertView, holder);
                break;
            case Constant.POS_KINDS_DETAIL://每个商品种类的详细，图形列表展示界面的筛选下拉
                holder.tv_store.setText(data.get(position) + "");
                break;
            case Constant.KINDS_CHOOSE://商品设置界面的种类选择列表
                GoodKinds goodKinds = (GoodKinds) data.get(position);
                holder.tv_store.setText(goodKinds.getName());
                holder.tv_store.setGravity(Gravity.CENTER);
                holder.line.setVisibility(View.GONE);
                break;
            case Constant.KINDS_CHOOSE_RIGHT://商品设置界面的种类选择列表
                PosOrderKinds goodKind = (PosOrderKinds) data.get(position);
                holder.tv_store.setText(goodKind.getName());
                holder.tv_store.setGravity(Gravity.CENTER);
                break;
            case Constant.POS_ORDER_CHOOSE_GOODS_KINDS://商品设置界面的种类选择列表
                //设置下级列表
                PosOrderBean.MsgBean.GoodsNameListBean.CategoryBean goods_kinds = (PosOrderBean.MsgBean.GoodsNameListBean.CategoryBean) data.get(position);
                if (goods_kinds.isCheched()) {
                    holder.iv_left_line.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_left_line.setVisibility(View.GONE);
                }
                holder.tv_store.setText(goods_kinds.getCategoryName());
                holder.tv_store.setGravity(Gravity.CENTER);
                break;
            case Constant.POS_ORDER_CHOOSE_GOODS_GOODS://商品设置界面的具体商品选择列表
                //设置下级列表
                PosOrderBean.MsgBean.GoodsNameListBean.CategoryBean.GoodsListBean detail_goods = (PosOrderBean.MsgBean.GoodsNameListBean.CategoryBean.GoodsListBean) data.get(position);
                if (detail_goods.isCheck()) {
                    holder.iv_left_line.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_left_line.setVisibility(View.GONE);
                }
                holder.tv_store.setText(detail_goods.getName());
                holder.tv_store.setGravity(Gravity.CENTER);
                break;
            case Constant.POS_ORDER_CHOOSE_CHART_KINDS:
                //设置下级列表
                PosChartBean.MsgBean.GoodsNameListBean.CategoryBean chart_kinds = (PosChartBean.MsgBean.GoodsNameListBean.CategoryBean) data.get(position);
                if (chart_kinds.isChecked()) {
                    holder.iv_left_line.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_left_line.setVisibility(View.GONE);
                }
                holder.tv_store.setText(chart_kinds.getCategoryName());
                holder.tv_store.setGravity(Gravity.CENTER);
                break;
            case Constant.POS_ORDER_CHOOSE_CHART_GOODS:
                //设置下级列表
                PosChartBean.MsgBean.GoodsNameListBean.CategoryBean.GoodsListBean chart_goods = (PosChartBean.MsgBean.GoodsNameListBean.CategoryBean.GoodsListBean) data.get(position);
                if (chart_goods.isChecked()) {
                    holder.iv_left_line.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_left_line.setVisibility(View.GONE);
                }
                holder.tv_store.setText(chart_goods.getName());
                holder.tv_store.setGravity(Gravity.CENTER);
                break;
            default:
                holder.tv_store.setText(data.get(position) + "");
                /*if(position==0) {
                    holder.tv_store.setText("全部商品");
                }else {
                    holder.tv_store.setText(data.get(position-1)+"");
                }
                break;*/
        }

        return convertView;

       /*
        if(dataType== Constant.POS_KINDS) {//商品的种类
            PosOrderKinds posOrderKinds= (PosOrderKinds) data.get(position);
                holder.tv_store.setText(posOrderKinds.getName());
                if(posOrderKinds.isChecked()) {
                    convertView.setBackgroundColor(Color.rgb(216, 216, 216));
                }else {
                    convertView.setBackgroundColor(Color.WHITE);
                }
        }else if(dataType==Constant.POS_KINDS_DETAIL) {//每个商品种类的详细
            holder.tv_store.setText(data.get(position)+"");
        }else if(dataType==Constant.KINDS_CHOOSE) {
            GoodKinds goodKinds= (GoodKinds) data.get(position);
            holder.tv_store.setText(goodKinds.getName());
            holder.tv_store.setGravity(Gravity.CENTER);
            holder.line.setVisibility(View.GONE);
        }else {
            if(position==0) {
                holder.tv_store.setText("全部商品");
            }else {
                holder.tv_store.setText(data.get(position-1)+"");
            }
        }*/
    }

    private void firstKindsSetting(int position, View convertView, ViewHolder holder) {
        PosOrderKinds posOrderKinds = (PosOrderKinds) data.get(position);
        holder.tv_store.setText(posOrderKinds.getName());
        if (posOrderKinds.isChecked()) {
            convertView.setBackgroundColor(Color.rgb(216, 216, 216));
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }
    }

    class ViewHolder {
        TextView tv_store;
        ImageView iv_left_line;
        View line;
    }
}

