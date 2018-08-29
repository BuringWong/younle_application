package com.younle.younle624.myapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.RoomsInfoBean;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.view.XListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by 我是奋斗 on 2016/6/23.
 * 微信/e-mail:tt090423@126.com
 */
public class RoomChooseAdapter<T> extends BaseAdapter/* implements View.OnClickListener*/ {

    private static final String TAG = "RoomChooseAdapter<T>";
    private Context context;
    private List<T> data;
    private int type=-1;
    private int room_fee_rule=-1;
    private PopupWindow popupWindow;
    private View popView;
    private ScaleAnimation scaleAnimation;
    private Handler mHandler ;

    public RoomChooseAdapter(Context context,Handler mHandler) {
        this.context=context;
        this.mHandler=mHandler;
    }
    public void setData(List<T> data) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null) {
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.room_choose_item_new,null);
            holder.tv_room_name = (TextView) convertView.findViewById(R.id.tv_room_name);//房间名称
            holder.tv_on_useing = (TextView) convertView.findViewById(R.id.tv_on_useing);//正在使用中
            //holder.iv_choose_arrow = (ImageView) convertView.findViewById(R.id.iv_choose_arrow);//更多计费规则指示
            holder.tv_more_room_fee = (TextView) convertView.findViewById(R.id.tv_more_room_fee);//更多
            holder.iv_choose_state = (ImageView) convertView.findViewById(R.id.iv_choose_state);//房间选中状态
            holder.ll_room_fee = (LinearLayout) convertView.findViewById(R.id.ll_room_fee);//费用信息布局
            holder.tv_room_fee = (TextView) convertView.findViewById(R.id.tv_room_fee);//房间费用显示
            holder.tv_deposit = (TextView) convertView.findViewById(R.id.tv_deposit);//押金
            holder.tv_min_consume = (TextView) convertView.findViewById(R.id.tv_min_consume);//低消
            holder.member_price_logo= (ImageView) convertView.findViewById(R.id.member_price_logo);//会员价图标
            holder.isShowpop = false;
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.isShowpop = false;
        final RoomsInfoBean.MsgBean.RoominfoBean.RoomlistBean roomBean = (RoomsInfoBean.MsgBean.RoominfoBean.RoomlistBean) data.get(position);

        isAviable(roomBean.getUsed(), holder, convertView,holder.tv_room_name,holder.tv_room_fee,holder.tv_deposit,holder.tv_min_consume);
        if(roomBean.isChecked()) {
            holder.iv_choose_state.setImageResource(R.drawable.room_has_choosed);
        }else{
            holder.iv_choose_state.setImageResource(R.drawable.room_not_choosed);
        }

        holder.tv_room_name.setText("" + roomBean.getRoomname());
        if(Double.valueOf(roomBean.getRoomdeposit())>0){
            holder.tv_deposit.setVisibility(View.VISIBLE);
            holder.tv_deposit.setText("押金："+roomBean.getRoomdeposit());
        }else{
            holder.tv_deposit.setVisibility(View.GONE);
        }
        if(Double.valueOf(roomBean.getMinconsum())>0){
            holder.tv_min_consume.setVisibility(View.VISIBLE);
            holder.tv_min_consume.setText("低消：" + roomBean.getMinconsum());
        }else{
            holder.tv_min_consume.setVisibility(View.GONE);
        }

        switch (room_fee_rule) {
            case Constant.ORDER_ROOM_NO_FEE://不计费
                holder.isShowpop = false;
                holder.tv_room_fee.setText("不计费");
                //holder.iv_choose_arrow.setVisibility(View.GONE);
                holder.tv_more_room_fee.setVisibility(View.GONE);
                holder.member_price_logo.setVisibility(View.GONE);
                break;
            case Constant.ORDER_ROOM_FEE_HOUR://按时计费
                List<RoomsInfoBean.MsgBean.RoominfoBean.RoomlistBean.RuleBean> rule_hour = roomBean.getRule();
                if(rule_hour!=null&&rule_hour.size()==1){
                    if(rule_hour.get(0).getInfo()!=null&&rule_hour.get(0).getInfo().size()>1){
                        showMoreMuiltHour(holder, rule_hour);
                    }else{
                        holder.isShowpop = false;
                        holder.tv_more_room_fee.setVisibility(View.GONE);
                        int isvip = rule_hour.get(0).getInfo().get(0).getIsvip();

                        showMemberPriceLogo(holder,isvip);

                        holder.tv_room_fee.setText(getWeekOrDate(rule_hour.get(0).getWday()) + " " + rule_hour.get(0).getInfo().get(0).getTimes() + " " +
                                rule_hour.get(0).getInfo().get(0).getCost() + "元/小时");
                    }
                }else if(rule_hour!=null&&rule_hour.size()>1){
                    showMoreMuiltHour(holder, rule_hour);
                }else{
                    holder.isShowpop = false;
                    holder.tv_room_fee.setText("暂无计费规则");
                    holder.tv_more_room_fee.setVisibility(View.GONE);
                }
                break;
            case Constant.ORDER_ROOM_FEE_DAY://按天计费
                List<RoomsInfoBean.MsgBean.RoominfoBean.RoomlistBean.RuleBean> rule_day = roomBean.getRule();
                if(rule_day!=null&&rule_day.size()==1){
                    if(rule_day.get(0).getInfo()!=null&&rule_day.get(0).getInfo().size()>1){
                        showMoreMuiltDay(holder, rule_day);
                    }else{
                        holder.isShowpop = false;
                        holder.tv_more_room_fee.setVisibility(View.GONE);
                        int isvip = rule_day.get(0).getInfo().get(0).getIsvip();
                        showMemberPriceLogo(holder,isvip);
                        holder.tv_room_fee.setText(getWeekOrDate(rule_day.get(0).getInfo().get(0).getWday()) + " " + rule_day.get(0).getInfo().get(0).getCost()+"元/天");
                    }
                }else if(rule_day!=null&&rule_day.size()>1){
                    showMoreMuiltDay(holder, rule_day);
                }else{
                    holder.isShowpop = false;
                    holder.tv_room_fee.setText("暂无计费规则");
                    holder.tv_more_room_fee.setVisibility(View.GONE);
                }
                break;
        }
        holder.iv_choose_state.setOnClickListener(new MyChooseListener(roomBean,position));
        return convertView;
    }

    private void showMoreMuiltDay(ViewHolder holder, List<RoomsInfoBean.MsgBean.RoominfoBean.RoomlistBean.RuleBean> rule_day) {
        holder.isShowpop = true;
        holder.tv_more_room_fee.setVisibility(View.VISIBLE);
        int isvip = rule_day.get(0).getInfo().get(0).getIsvip();
        showMemberPriceLogo(holder, isvip);
        holder.tv_room_fee.setText(getWeekOrDate(rule_day.get(0).getInfo().get(0).getWday()) + " " + rule_day.get(0).getInfo().get(0).getCost() + "元/天");
        holder.tv_more_room_fee.setOnClickListener(new MyRoomFeeListener(rule_day, holder.isShowpop, Constant.ORDER_ROOM_FEE_DAY));
    }

    private void showMoreMuiltHour(ViewHolder holder, List<RoomsInfoBean.MsgBean.RoominfoBean.RoomlistBean.RuleBean> rule_hour) {
        holder.isShowpop = true;
        holder.tv_more_room_fee.setVisibility(View.VISIBLE);

        int isvip = rule_hour.get(0).getInfo().get(0).getIsvip();
        showMemberPriceLogo(holder, isvip);

        holder.tv_room_fee.setText(getWeekOrDate(rule_hour.get(0).getWday()) + " " + rule_hour.get(0).getInfo().get(0).getTimes() + " " +
                rule_hour.get(0).getInfo().get(0).getCost() + "元/小时");
        holder.tv_more_room_fee.setOnClickListener(new MyRoomFeeListener(rule_hour,holder.isShowpop, Constant.ORDER_ROOM_FEE_HOUR));
    }

    private void showMemberPriceLogo(ViewHolder holder, int isvip) {
        if(0==isvip) {
            holder.member_price_logo.setVisibility(View.GONE);
        }else {
            holder.member_price_logo.setVisibility(View.VISIBLE);
        }
    }

    private class MyRoomFeeListener implements View.OnClickListener {

        private List<RoomsInfoBean.MsgBean.RoominfoBean.RoomlistBean.RuleBean> rule;
        private boolean isPop;
        private int fee_rule;

        public MyRoomFeeListener(List<RoomsInfoBean.MsgBean.RoominfoBean.RoomlistBean.RuleBean> rule,boolean isPop,int fee_rule) {
            this.rule = rule;
            this.isPop = isPop;
            this.fee_rule = fee_rule;
        }

        @Override
        public void onClick(View v) {
            if(!isPop){
                return;
            }
            if(rule!=null&&rule.size()>0){
                if(rule.size()<=1){
                    if(rule.get(0)!=null&&rule.get(0).getInfo().size()<=1){
                        return;
                    }
                }
            }
            if(popupWindow == null){
                popView = View.inflate(context, R.layout.room_fee_layered_pop, null);
                int px_height = Utils.dip2px(context, 150);
                int px_weight = Utils.dip2px(context, 232);
                //popupWindow = new PopupWindow(popView, v.getWidth()-px_weight, px_height);//设置不同高度
                popupWindow = new PopupWindow(popView, px_weight, px_height);//设置不同高度
                //只有设置了popupWindow的BackgroundDrawable，才能显示动画
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setOutsideTouchable(true);

                //LinearLayout ll_more_fee_rules = (LinearLayout) popView.findViewById(R.id.ll_more_fee_rules);
                XListView xl_accounting_rules = (XListView) popView.findViewById(R.id.xl_accounting_rules);
                xl_accounting_rules.setVerticalScrollBarEnabled(false);
                xl_accounting_rules.setPullRefreshEnable(false);
                xl_accounting_rules.setPullLoadEnable(false);
                PopFeeInfoAdapter popRoomFeeAdapter = new PopFeeInfoAdapter(context);
                popRoomFeeAdapter.setFeeRule(fee_rule);
                popRoomFeeAdapter.setData(rule);
                xl_accounting_rules.setAdapter(popRoomFeeAdapter);

                //判断属于屏幕上半部分还是下半部分：
                int[] ma = new int[2];
                v.getLocationOnScreen(ma);
                if(600<ma[1]){
                    scaleAnimation = new ScaleAnimation(1, 1, 0, 1, Animation.ABSOLUTE, 0, Animation.RELATIVE_TO_SELF, 1);
                    scaleAnimation.setDuration(500);
                    popView.setBackgroundResource(R.drawable.blue_roo_layered_down);
                }else{
                    scaleAnimation = new ScaleAnimation(1, 1, 0, 1, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
                    scaleAnimation.setDuration(500);
                    popView.setBackgroundResource(R.drawable.blue_roo_layered_up);
                }
                popView.startAnimation(scaleAnimation);
                /**
                 * 显示popupWindow，同时指明其显示的位置.
                 * 参数1：指明作用的view
                 * 参数2/参数3：指明popupWindow显示的位置
                 */
                int px_w = Utils.dip2px(context, -20);
                //根据视图判断显示哪一个背景:计算方法：如果剩余的空间不够显示pop就往上边显示

                //显示位置需要计算pop的高度进行放置
                popupWindow.showAsDropDown(v, px_w, 0);
            }else {
                popDismiss();
            }
        }
    }

    private String getWeekOrDate(int date) {
        String week_str = "";
        switch (date){
            case 0:
                week_str = "周日";
                break;
            case 1:
                week_str = "周一";
                break;
            case 2:
                week_str = "周二";
                break;
            case 3:
                week_str = "周三";
                break;
            case 4:
                week_str = "周四";
                break;
            case 5:
                week_str = "周五";
                break;
            case 6:
                week_str = "周六";
                break;
        }
        if("".equals(week_str)){
            DateFormat df4 = new SimpleDateFormat("MM月dd日");
            week_str = df4.format(Long.valueOf(date + "000"));
        }
        return week_str;
    }

    private void popDismiss() {
        popupWindow.dismiss();
        popupWindow=null;
    }

    private void isAviable(String aviable, ViewHolder holder, View convertView,TextView tv_room_name,TextView tv_room_fee,TextView tv_deposit,TextView tv_min_consume) {
        if("1".equals(aviable)) {//不可用
            holder.iv_choose_state.setVisibility(View.GONE);
            holder.tv_on_useing.setVisibility(View.VISIBLE);
            tv_room_name.setTextColor(Color.parseColor("#999999"));
            tv_room_fee.setTextColor(Color.parseColor("#999999"));
            convertView.setBackgroundColor(Color.parseColor("#e3e3e3"));
        }else {//可用
            holder.iv_choose_state.setVisibility(View.VISIBLE);
            holder.tv_on_useing.setVisibility(View.GONE);
            tv_room_name.setTextColor(Color.parseColor("#292929"));
            tv_room_fee.setTextColor(Color.parseColor("#292929"));
            convertView.setBackgroundColor(Color.WHITE);
        }
    }

    public void setRoomFeeRule(int room_fee_rule) {
        this.room_fee_rule=room_fee_rule;
    }

    class ViewHolder{
        ImageView iv_choose_state;
        //ImageView iv_choose_arrow;
        TextView tv_more_room_fee;
        TextView tv_room_name;
        TextView tv_on_useing;
        LinearLayout ll_room_fee;
        TextView tv_room_fee;
        TextView tv_deposit;
        TextView tv_min_consume;
        boolean isShowpop = false;
        ImageView member_price_logo;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 点击图片进行状态切换
     */
    public class MyChooseListener implements View.OnClickListener {

        public RoomsInfoBean.MsgBean.RoominfoBean.RoomlistBean roomBean;
        public int position;

        public MyChooseListener(RoomsInfoBean.MsgBean.RoominfoBean.RoomlistBean roomBean,int position) {
            this.roomBean = roomBean;
            this.position = position;
        }

        @Override
        public void onClick(View v) {

            /*if ("1".equals(roomBean.getUsed())) {//占用
                Utils.showToast(context, "该房间使用中，请选择其他房间！");
                return;
            }*/

            List<RoomsInfoBean.MsgBean.RoominfoBean.RoomlistBean> rightDataChoosed = (List<RoomsInfoBean.MsgBean.RoominfoBean.RoomlistBean>) data;
            for(int i = 0; i < rightDataChoosed.size(); i++){
                if (position == i) {
                    if(rightDataChoosed.get(i).isChecked()) {
                        rightDataChoosed.get(i).setChecked(false);
                        mHandler.sendEmptyMessage(1);
                    }else {
                        rightDataChoosed.get(i).setChecked(true);
                        String choosed_room_id = rightDataChoosed.get(i).getId();
                        String choosed_room_name = rightDataChoosed.get(i).getRoomname();
                        Message tempMsg = mHandler.obtainMessage();
                        tempMsg.what = 0;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("choosed_room_id",choosed_room_id);
                        bundle.putSerializable("room_name",choosed_room_name);
                        tempMsg.setData(bundle);
                        mHandler.sendMessage(tempMsg);
                    }
                } else {
                    rightDataChoosed.get(i).setChecked(false);
                }
            }
            notifyDataSetChanged();
        }
    }
}
