package com.younle.younle624.myapplication.adapter.waimai;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.takeoutfood.PrintAgainListener;
import com.younle.younle624.myapplication.domain.waimai.ElmOrderBean;
import com.younle.younle624.myapplication.domain.waimai.MtOrderDetail;
import com.younle.younle624.myapplication.domain.waimai.NewElmOrderBean;
import com.younle.younle624.myapplication.utils.ClicKUtils;
import com.younle.younle624.myapplication.view.NoScrollGirdView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作者：Create by 我是奋斗 on2016/12/23 17:16
 * 邮箱：tt090423@126.com
 * 微信：17346512596
 */
public class WmAdapter<T> extends BaseAdapter {
    private Context context;
    private List<Map> data;
    public  DateFormat df4 = new SimpleDateFormat("MM月dd日 HH:mm");
    private DecimalFormat numDf = new DecimalFormat("0.00");
    private boolean finishPrint;
    private PrintAgainListener printAgainListener;
    private boolean doAnimation=true;

    public WmAdapter(Context context, PrintAgainListener printAgainListener) {
        this.context = context;
        this.printAgainListener=printAgainListener;
    }
    public void setData(List<Map> data) {
        this.data=data;
    }
    public void setFinishPrint(boolean finishPrint) {
        this.finishPrint=finishPrint;

    }
    public void doAnimation(boolean doAniamtion) {
        this.doAnimation=doAniamtion;
    }
    @Override
    public int getCount() {
        if(data!=null&&data.size()>0) {
            return data.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if(convertView==null) {
            convertView=View.inflate(context, R.layout.waimai_item,null);
            holder=new ViewHolder();
            holder.al_newelm_zp = (LinearLayout) convertView.findViewById(R.id.al_newelm_zp);
            holder.tv_elm_zengpin= (NoScrollGirdView) convertView.findViewById(R.id.tv_elm_zengpin);
            holder.al_newelm_yh= (LinearLayout) convertView.findViewById(R.id.al_newelm_yh);
            holder.tv_shangjia_fee= (TextView) convertView.findViewById(R.id.tv_shangjia_fee);
            holder.tv_hongbao_fee= (TextView) convertView.findViewById(R.id.tv_hongbao_fee);

            holder.gv_order_details= (NoScrollGirdView) convertView.findViewById(R.id.gv_order_details);
            holder.gv_youhui= (NoScrollGirdView) convertView.findViewById(R.id.gv_youhui);
//            holder.gv_extra= (NoScrollGirdView) convertView.findViewById(R.id.gv_extra);
            holder.rl_order_title= (RelativeLayout) convertView.findViewById(R.id.rl_order_title);
            holder.pb_printing= (ProgressBar) convertView.findViewById(R.id.pb_printing);
            holder.tv_printing= (TextView) convertView.findViewById(R.id.tv_printing);
            holder.order_num = (TextView) convertView.findViewById(R.id.order_num);
            holder.receiver_add = (TextView) convertView.findViewById(R.id.receiver_add);
            holder.receiver_name_phone = (TextView) convertView.findViewById(R.id.receiver_name_phone);
            holder.tv_songda_time = (TextView) convertView.findViewById(R.id.tv_songda_time);
            holder.tv_xiadan_time = (TextView) convertView.findViewById(R.id.tv_xiadan_time);
            holder.tv_remark = (TextView) convertView.findViewById(R.id.tv_remark);
            holder.tv_fapiao_title = (TextView) convertView.findViewById(R.id.tv_fapiao_title);
            holder.tv_fapiao_amount = (TextView) convertView.findViewById(R.id.tv_fapiao_amount);
            holder.tv_peisong_fee = (TextView) convertView.findViewById(R.id.tv_peisong_fee);
//            holder.tv_youhui_fee = (TextView) convertView.findViewById(R.id.tv_youhui_fee);
            holder.tv_canhe_num = (TextView) convertView.findViewById(R.id.tv_canhe_num);
            holder.tv_canhe_account= (TextView) convertView.findViewById(R.id.tv_canhe_account);
            holder.tv_zongji_youhui = (TextView) convertView.findViewById(R.id.tv_zongji_youhui);
            holder.actually_pay_fee = (TextView) convertView.findViewById(R.id.actually_pay_fee);
            holder.order_bianhao = (TextView) convertView.findViewById(R.id.order_bianhao);
            holder.b_fapiao_account = (TextView) convertView.findViewById(R.id.b_fapiao_account);
            holder.b_fapiao = (TextView) convertView.findViewById(R.id.b_fapiao);
            holder.b_remark = (TextView) convertView.findViewById(R.id.b_remark);
            holder.print_again = (Button) convertView.findViewById(R.id.print_again);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        Map map = data.get(position);
        String type = (String) map.get("type");
        Object bean = map.get("bean");
        Boolean alreadyPrint= (Boolean) map.get("print");

        WMOrderDetailAdapter wmOrderDetailAdapter=new WMOrderDetailAdapter();
        YouHuiAdapter yhAdapter=new YouHuiAdapter();
        ExtraAdapter extraAdapter=new ExtraAdapter();
        switch (type) {
            case "MT" :
                holder.al_newelm_yh.setVisibility(View.GONE);
                holder.al_newelm_zp.setVisibility(View.GONE);
                holder.tv_canhe_num.setVisibility(View.VISIBLE);
                holder.gv_youhui.setVisibility(View.VISIBLE);
                inflatMtData(holder, (MtOrderDetail) bean, wmOrderDetailAdapter, yhAdapter, extraAdapter);
                break;
            case "ELM":
                holder.al_newelm_yh.setVisibility(View.GONE);
                holder.al_newelm_zp.setVisibility(View.GONE);
                holder.tv_canhe_num.setVisibility(View.VISIBLE);
                holder.gv_youhui.setVisibility(View.VISIBLE);
                inflatElmData(holder, (ElmOrderBean) bean, wmOrderDetailAdapter, yhAdapter, extraAdapter);
                break;
            case "NEW_ELM":
                holder.al_newelm_yh.setVisibility(View.VISIBLE);
                holder.al_newelm_zp.setVisibility(View.VISIBLE);
                holder.tv_canhe_num.setVisibility(View.GONE);
                holder.gv_youhui.setVisibility(View.GONE);
                inflatNewElmData(holder, (NewElmOrderBean) bean, wmOrderDetailAdapter, yhAdapter, extraAdapter);
                break;
        }
        if(alreadyPrint) {
            holder.rl_order_title.setBackgroundColor(Color.WHITE);
            holder.tv_printing.setText("已打印");
            holder.pb_printing.setVisibility(View.GONE);
        }else {
            holder.rl_order_title.setBackgroundColor(Color.rgb(207,232,255));
            holder.tv_printing.setText("打印中...");
            holder.pb_printing.setVisibility(View.VISIBLE);
        }
        holder.print_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ClicKUtils.isFastDoubleClick()) {
                    return;
                }
                printAgainListener.onCLickPrintAgain(position);
            }
        });
        /*if(doAnimation) {
            startAniamtion(convertView);
        }*/
        return convertView;
    }
    private void inflatMtData(ViewHolder holder, MtOrderDetail bean, WMOrderDetailAdapter wmOrderDetailAdapter,
                              YouHuiAdapter yhAdapter,ExtraAdapter extraAdapter) {
        MtOrderDetail mtOrderDetail= bean;
      /*  if(mtOrderDetail.getKey().isAlreadyPrint()) {
            holder.rl_order_title.setBackgroundColor(Color.WHITE);
            holder.tv_printing.setText("已打印");
            holder.pb_printing.setVisibility(View.GONE);
        }else {
            holder.rl_order_title.setBackgroundColor(Color.rgb(207,232,255));
            holder.tv_printing.setText("打印中...");
            holder.pb_printing.setVisibility(View.VISIBLE);
        }*/
        //1.顶部数据
        holder.order_num.setText("美团外卖 # "+mtOrderDetail.getKey().getDaySeq());
        String recipientAddress = mtOrderDetail.getKey().getRecipientAddress();
//        String actualAdd = recipientAddress.substring(0, recipientAddress.indexOf("@#"));
        holder.receiver_add.setText(recipientAddress);
        holder.receiver_name_phone.setText(mtOrderDetail.getKey().getRecipientName() + "  " + mtOrderDetail.getKey().getRecipientPhone());
        if(0==mtOrderDetail.getKey().getDeliveryTime()) {
            holder.tv_songda_time.setText("立即送达");
        }else {
            String formatDTime = df4.format(mtOrderDetail.getKey().getDeliveryTime()*1000);
            holder.tv_songda_time.setText(formatDTime);
        }
        String xiaDanTime = df4.format(mtOrderDetail.getKey().getCtime() * 1000);
        holder.tv_xiadan_time.setText(xiaDanTime);
        holder.order_bianhao.setText(mtOrderDetail.getKey().getOrderIdView()+"");
        String caution = mtOrderDetail.getKey().getCaution();
        /*if(TextUtils.isEmpty(caution)) {
            holder.b_remark.setVisibility(View.GONE);
            holder.tv_remark.setVisibility(View.GONE);
        }else {*/
        holder.tv_remark.setVisibility(View.VISIBLE);
        if(mtOrderDetail.getKey().getDinnersNumber()>0) {
            holder.tv_remark.setText(mtOrderDetail.getKey().getDinnersNumber()+"人用餐  "+caution);
        }else {
            holder.tv_remark.setText(caution);
        }
//        }
        int hasInvoiced = mtOrderDetail.getKey().getHasInvoiced();
        double total = mtOrderDetail.getKey().getTotal();
        if(0==hasInvoiced) {//0-不需要
            holder.tv_fapiao_amount.setVisibility(View.GONE);
            holder.tv_fapiao_title.setVisibility(View.GONE);
            holder.b_fapiao.setVisibility(View.GONE);
            holder.b_fapiao_account.setVisibility(View.GONE);
        }else {//1-需要发票；
            holder.b_fapiao.setVisibility(View.VISIBLE);
            holder.b_fapiao_account.setVisibility(View.VISIBLE);
            holder.tv_fapiao_amount.setVisibility(View.VISIBLE);
            holder.tv_fapiao_title.setVisibility(View.VISIBLE);
            holder.tv_fapiao_title.setText(mtOrderDetail.getKey().getInvoiceTitle());
            holder.tv_fapiao_amount.setText(total + "元");
        }
        //2.中间详情
        holder.tv_peisong_fee.setText(mtOrderDetail.getKey().getShippingFee()+"元");
        List<MtOrderDetail.KeyBean.DetailBean> detail = mtOrderDetail.getKey().getDetail();
        int canhe_total_num=0;
        double canhe_total_fee=0;
        if(detail!=null&&detail.size()>0) {
            for (int i = 0; i < detail.size(); i++) {
                MtOrderDetail.KeyBean.DetailBean canHeDetail = detail.get(i);
                int box_num = canHeDetail.getBox_num();
                double box_price = canHeDetail.getBox_price();
                double boxAccount = box_price * box_num;
                canhe_total_num+=box_num;
                canhe_total_fee+=boxAccount;
            }
            //餐盒费用
            /*extraAdapter.setMtData(canhe_total_num,canhe_total_fee);
            extraAdapter.setData(null,"mt");
            holder.gv_extra.setAdapter(extraAdapter);*/

            holder.tv_canhe_num.setText("X"+canhe_total_num);
            holder.tv_canhe_account.setText("￥"+canhe_total_fee);

            wmOrderDetailAdapter.setData((List<T>) detail, "mt");
            holder.gv_order_details.setAdapter(wmOrderDetailAdapter);
        }
        //3.优惠信息和总价
        List<MtOrderDetail.KeyBean.ExtrasBean> extras = mtOrderDetail.getKey().getExtras();
        double total_youhui=0;
        if(extras!=null&&extras.size()>0) {
            yhAdapter.setData((List<T>) extras,"mt");
            //优惠信息
            holder.gv_youhui.setAdapter(yhAdapter);
            //总优惠
            for (int i = 0; i <extras.size(); i++) {
                total_youhui+=extras.get(i).getReduce_fee();
            }
        }
        holder.tv_zongji_youhui.setText("总计：￥"+(total+total_youhui)+" 优惠：￥"+total_youhui);
        holder.actually_pay_fee.setText("在线支付：￥"+total);
    }

    private void inflatElmData(ViewHolder holder, ElmOrderBean bean, WMOrderDetailAdapter wmOrderDetailAdapter, YouHuiAdapter yhAdapter, ExtraAdapter extraAdapter) {
        ElmOrderBean elmOrderBean= bean;
       /* if(elmOrderBean.isAlreadyPrint()) {
            holder.rl_order_title.setBackgroundColor(Color.WHITE);
            holder.tv_printing.setText("已打印");
            holder.pb_printing.setVisibility(View.GONE);
        }else {
            holder.rl_order_title.setBackgroundColor(Color.rgb(207,232,255));
            holder.tv_printing.setText("打印中...");
            holder.pb_printing.setVisibility(View.VISIBLE);
        }*/
        //1.顶部数据
        holder.order_num.setText("饿了么 # "+ elmOrderBean.getData().getRestaurant_number());
        holder.receiver_add.setText(elmOrderBean.getData().getDelivery_poi_address());
        holder.receiver_name_phone.setText(elmOrderBean.getData().getConsignee() + "  " + elmOrderBean.getData().getPhone_list().get(0));
        if(!(elmOrderBean.getData().getDeliver_time()==null)) {
            holder.tv_songda_time.setText(elmOrderBean.getData().getDeliver_time());
        }else {
            holder.tv_songda_time.setText("立即送达");
        }

        holder.tv_xiadan_time.setText(elmOrderBean.getData().getCreated_at());
        holder.order_bianhao.setText(elmOrderBean.getData().getOrder_id() + "");
        String description = elmOrderBean.getData().getDescription();
        if(TextUtils.isEmpty(description)) {
            holder.tv_remark.setVisibility(View.GONE);
        }else {
            holder.tv_remark.setVisibility(View.VISIBLE);
            holder.tv_remark.setText(description);
        }

        int invoiced = elmOrderBean.getData().getInvoiced();
        float total_price = elmOrderBean.getData().getTotal_price();
        if(0==invoiced) {//不需要
            holder.tv_fapiao_amount.setVisibility(View.GONE);
            holder.tv_fapiao_title.setVisibility(View.GONE);
            holder.b_fapiao.setVisibility(View.GONE);
            holder.b_fapiao_account.setVisibility(View.GONE);
        }else {//1-需要发票；
            String invoice = elmOrderBean.getData().getInvoice();
            holder.b_fapiao.setVisibility(View.VISIBLE);
            holder.b_fapiao_account.setVisibility(View.VISIBLE);
            holder.tv_fapiao_amount.setVisibility(View.VISIBLE);
            holder.tv_fapiao_title.setVisibility(View.VISIBLE);
            holder.tv_fapiao_title.setText(invoice);
            holder.tv_fapiao_amount.setText(numDf.format(total_price) + "元");
        }

        holder.tv_canhe_num.setVisibility(View.GONE);
        holder.tv_canhe_account.setText("￥"+numDf.format(elmOrderBean.getData().getPackage_fee()));

        //配送费用
        holder.tv_peisong_fee.setText(numDf.format(elmOrderBean.getData().getDeliver_fee())+"元");

        // * 菜品，餐盒费用等
        ElmOrderBean.DataBean.DetailBean details = elmOrderBean.getData().getDetail();
        if(details!=null) {
            // 1. group菜品
            List<List<ElmOrderBean.DataBean.DetailBean.GroupBean>> group = details.getGroup();
            if(group!=null&&group.size()>0) {
                List<ElmOrderBean.DataBean.DetailBean.GroupBean> groupBeans=new ArrayList<>();
                for (int i = 0; i < group.size(); i++) {
                    List<ElmOrderBean.DataBean.DetailBean.GroupBean> secondGroup = group.get(i);
                    if(secondGroup!=null&&secondGroup.size()>0) {
                        for (int j = 0; j < secondGroup.size(); j++) {
                            groupBeans.add(secondGroup.get(j));
                        }
                    }
                }
                wmOrderDetailAdapter.setData((List<T>) groupBeans,"elm_order");
                holder.gv_order_details.setAdapter(wmOrderDetailAdapter);
            }
            // 2. extra优惠信息
            List<ElmOrderBean.DataBean.DetailBean.ExtraBean> extras = details.getExtra();
            if(extras!=null&&extras.size()>0) {
                for (int i = 0; i < extras.size(); i++) {
                    int category_id = extras.get(i).getCategory_id();
                    if(category_id ==2|| category_id ==102) {
                        extras.remove(extras.get(i));
                        i--;
                    }
                }
                yhAdapter.setData((List<T>) extras,"elm_order");
                holder.gv_youhui.setAdapter(yhAdapter);
               /* extraAdapter.setData((List<T>) extras,"elm_new_order");
                holder.gv_extra.setAdapter(extraAdapter);*/
            }
        }
        //原始总价
        float original_price = elmOrderBean.getData().getOriginal_price();
        float youhui = original_price - total_price;
        holder.tv_zongji_youhui.setText("总计：￥"+numDf.format(total_price)+" 优惠：￥"+numDf.format(youhui));
        holder.actually_pay_fee.setText("在线支付：￥"+numDf.format(total_price));
    }
    private void inflatNewElmData(ViewHolder holder, NewElmOrderBean bean, WMOrderDetailAdapter wmOrderDetailAdapter, YouHuiAdapter yhAdapter, ExtraAdapter extraAdapter) {
        NewElmOrderBean newElm= bean;
        //1.顶部数据
        holder.order_num.setText("饿了么 # "+ newElm.getMessage().getDaySn());
        holder.receiver_add.setText(newElm.getMessage().getAddress());
        holder.receiver_name_phone.setText(newElm.getMessage().getConsignee() + "  " + newElm.getMessage().getPhoneList().get(0));
        if(!(newElm.getMessage().getDeliverTime()==null)) {
            holder.tv_songda_time.setText(newElm.getMessage().getDeliverTime());
        }else {
            holder.tv_songda_time.setText("立即送达");
        }

        holder.tv_xiadan_time.setText(newElm.getMessage().getCreatedAt());
        holder.order_bianhao.setText(newElm.getMessage().getId()+"");
        String description = newElm.getMessage().getDescription();
        if(TextUtils.isEmpty(description)) {
            holder.tv_remark.setVisibility(View.GONE);
        }else {
            holder.tv_remark.setVisibility(View.VISIBLE);
            holder.tv_remark.setText(description);
        }
        boolean isInvoiced = newElm.getMessage().isInvoiced();
        double total_price = newElm.getMessage().getTotalPrice();
        if(!isInvoiced) {//不需要
            holder.tv_fapiao_amount.setVisibility(View.GONE);
            holder.tv_fapiao_title.setVisibility(View.GONE);
            holder.b_fapiao.setVisibility(View.GONE);
            holder.b_fapiao_account.setVisibility(View.GONE);
        }else {//1-需要发票；
            String invoice = newElm.getMessage().getInvoice();
            holder.b_fapiao.setVisibility(View.VISIBLE);
            holder.b_fapiao_account.setVisibility(View.VISIBLE);
            holder.tv_fapiao_amount.setVisibility(View.VISIBLE);
            holder.tv_fapiao_title.setVisibility(View.VISIBLE);
            holder.tv_fapiao_title.setText(invoice);
            holder.tv_fapiao_amount.setText(numDf.format(total_price) + "元");
        }

        holder.tv_canhe_num.setVisibility(View.GONE);
        holder.tv_canhe_account.setText("￥"+numDf.format(newElm.getMessage().getPackageFee()));

        //配送费用
        holder.tv_peisong_fee.setText(numDf.format(newElm.getMessage().getDeliverFee())+"元");

        // * 菜品，餐盒费用等
       List< NewElmOrderBean.MessageBean.GroupsBean> groups = newElm.getMessage().getGroups();
        if(groups!=null&&groups.size()>0) {
            for (int i = 0; i < groups.size(); i++) {
                NewElmOrderBean.MessageBean.GroupsBean groupsBean = groups.get(i);
                String type = groupsBean.getType();
                List<NewElmOrderBean.MessageBean.GroupsBean.ItemsBean> items = groupsBean.getItems();
                if ("normal".equals(type)) {//正常菜
                    wmOrderDetailAdapter.setData((List<T>) items, "new_elm_order");
                    holder.gv_order_details.setAdapter(wmOrderDetailAdapter);
                }else if("discount".equals(type)) {//赠品
                    yhAdapter.setData((List<T>) items,"new_elm_order");
                    holder.tv_elm_zengpin.setAdapter(yhAdapter);
                }
            }
        }
        //其他
        holder.tv_canhe_account.setText("￥"+newElm.getMessage().getPackageFee());
        holder.tv_peisong_fee.setText("￥"+newElm.getMessage().getDeliverFee());
        //优惠
        holder.tv_shangjia_fee.setText("￥"+newElm.getMessage().getActivityTotal());
        holder.tv_hongbao_fee.setText("￥"+newElm.getMessage().getHongbao());

        //原始总价
        double original_price = newElm.getMessage().getOriginalPrice();
        //订单活动金额
        double youhui = original_price - total_price;
        holder.tv_zongji_youhui.setText("总计：￥"+numDf.format(total_price)+" 优惠：￥"+numDf.format(youhui));
        if(newElm.getMessage().isOnlinePaid()) {
            holder.actually_pay_fee.setText("在线支付：￥"+numDf.format(total_price));
        }else {
            holder.actually_pay_fee.setText("货到付款：￥"+numDf.format(total_price));
        }
    }

    private void startAniamtion(View convertView) {
        Animation translateAnimation = new TranslateAnimation(0, 0, 0, 0,
                Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF,0);
        translateAnimation.setDuration(500);
//            translateAnimation.setFillAfter(true);
        convertView.startAnimation(translateAnimation);
    }

    /**
     * 订单内容（具体点的什么菜的adapter）
     */
    private class WMOrderDetailAdapter extends BaseAdapter {
        private List<T> detail;
        private String type;
        public void setData(List<T> detail, String mt) {
            this.detail=detail;
            this.type=mt;
        }
        @Override
        public int getCount() {
            return detail.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if(convertView==null) {
                convertView=View.inflate(context, R.layout.wm_order_detail_item,null);
                holder=new ViewHolder();
                holder.food_total_fee= (TextView) convertView.findViewById(R.id.food_total_fee);
                holder.food_name= (TextView) convertView.findViewById(R.id.food_name);
                holder.food_num= (TextView) convertView.findViewById(R.id.food_num);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }
            T t = detail.get(i);
            switch (type) {
                //美团
                case "mt" :
                    MtOrderDetail.KeyBean.DetailBean detailBean= (MtOrderDetail.KeyBean.DetailBean) t;
                    String spec = detailBean.getSpec();
                    if(spec !=null&&!TextUtils.isEmpty(spec)) {
                        holder.food_name.setText(detailBean.getFood_name()+"("+spec+")");//菜名
                    }else {
                        holder.food_name.setText(detailBean.getFood_name());//菜名
                    }
                    holder.food_num.setText("x "+detailBean.getQuantity());//数量
                    holder.food_total_fee.setText("￥"+numDf.format(detailBean.getPrice()*detailBean.getQuantity()));//金额
                    break;
                case "elm_order":
                    ElmOrderBean.DataBean.DetailBean.GroupBean groupBean= (ElmOrderBean.DataBean.DetailBean.GroupBean) t;
                    List<String> specs = groupBean.getSpecs();
                    String specsStr="";
                    if(specs!=null&&specs.size()>0) {
                        for (int j = 0; j < specs.size(); j++) {
                            specsStr+=specs.get(j)+",";
                        }
                    }
                    if(!TextUtils.isEmpty(specsStr)) {
                        holder.food_name.setText(groupBean.getName()+"["+specsStr+"]");
                    }else {
                        holder.food_name.setText(groupBean.getName());
                    }
                    holder.food_num.setText("x "+groupBean.getQuantity());
                    holder.food_total_fee.setText("￥"+numDf.format(groupBean.getPrice()*groupBean.getPrice()));
                    break;
                case "new_elm_order":
                    NewElmOrderBean.MessageBean.GroupsBean.ItemsBean itemBeans= (NewElmOrderBean.MessageBean.GroupsBean.ItemsBean) t;
                    holder.food_name.setText(itemBeans.getName());
                    holder.food_num.setText("x"+itemBeans.getQuantity());
                    holder.food_total_fee.setText("￥"+itemBeans.getTotal());
                    break;

            }
            return convertView;
        }
    }

    /**
     * 美团:优惠信息adapter
     * 饿了么：无需展示
     */
    private class YouHuiAdapter extends BaseAdapter {
        private List<T> youhuis;
        private String type;
        public void setData(List<T> youhuis, String type) {
            this.youhuis =youhuis;
            this.type=type;
        }
        @Override
        public int getCount() {
            return youhuis.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if(convertView==null) {
                convertView=View.inflate(context, R.layout.wm_order_detail_item,null);
                holder=new ViewHolder();
                holder.food_total_fee= (TextView) convertView.findViewById(R.id.food_total_fee);
                holder.food_name= (TextView) convertView.findViewById(R.id.food_name);
                holder.food_num= (TextView) convertView.findViewById(R.id.food_num);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }
            holder.food_num.setVisibility(View.GONE);
            T t = youhuis.get(i);
            switch (type) {
                //美团
                case "mt" :
                    MtOrderDetail.KeyBean.ExtrasBean extrasBean= (MtOrderDetail.KeyBean.ExtrasBean) t;
                    holder.food_name.setText(extrasBean.getRemark());//优惠名称
                    holder.food_total_fee.setText("-￥"+numDf.format(extrasBean.getReduce_fee()));//金额
                    break;
                case "elm_order":
                    ElmOrderBean.DataBean.DetailBean.ExtraBean extraBean= (ElmOrderBean.DataBean.DetailBean.ExtraBean) youhuis.get(i);
                    holder.food_name.setText(extraBean.getName());
                    holder.food_num.setText("X"+extraBean.getQuantity());
                    holder.food_total_fee.setText("￥"+numDf.format(extraBean.getPrice()*extraBean.getQuantity()));
                    break;
                case "new_elm_order":
                    holder.food_num.setVisibility(View.VISIBLE);
                    NewElmOrderBean.MessageBean.GroupsBean.ItemsBean itemBeans= (NewElmOrderBean.MessageBean.GroupsBean.ItemsBean) t;
                    holder.food_name.setText(itemBeans.getName());
                    holder.food_num.setText("x"+itemBeans.getQuantity());
                    holder.food_total_fee.setText("￥"+itemBeans.getTotal());
                    break;
            }
            return convertView;
        }
    }

    /**
     * 美团：餐盒费用的adapter
     * 饿了么：extra和优惠信息
     */
    private class ExtraAdapter extends BaseAdapter {
        private List<T> extras;
        private String type;
        private int canhe_total_num;
        private double canhe_total_fee;

        public void setData(List<T> extras, String type) {
            if(extras!=null) {
                this.extras=extras;
            }
            this.type=type;
        }
        public void setMtData(int canhe_total_num, double canhe_total_fee) {
            this.canhe_total_num=canhe_total_num;
            this.canhe_total_fee=canhe_total_fee;
        }
        @Override
        public int getCount() {
            if("mt".equals(type)) {
                return 1;
            }
            return extras.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if(convertView==null) {
                convertView=View.inflate(context, R.layout.wm_order_detail_item,null);
                holder=new ViewHolder();
                holder.food_total_fee= (TextView) convertView.findViewById(R.id.food_total_fee);
                holder.food_name= (TextView) convertView.findViewById(R.id.food_name);
                holder.food_num= (TextView) convertView.findViewById(R.id.food_num);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }
            switch (type) {
                //美团
                case "mt" :
                    holder.food_name.setText("餐盒费");
                    holder.food_num.setText("X"+canhe_total_num);
                    holder.food_total_fee.setText("￥"+numDf.format(canhe_total_fee));//金额
                    break;
                case "elm_new_order":
                    ElmOrderBean.DataBean.DetailBean.ExtraBean extraBean= (ElmOrderBean.DataBean.DetailBean.ExtraBean) extras.get(i);
                    holder.food_name.setText(extraBean.getName());
                    holder.food_num.setText("X"+extraBean.getQuantity());
                    holder.food_total_fee.setText("￥"+numDf.format(extraBean.getPrice()*extraBean.getQuantity()));
                    break;

            }
            return convertView;
        }
    }


    class ViewHolder{
        LinearLayout al_newelm_zp;
        LinearLayout al_newelm_yh;

        TextView tv_shangjia_fee;
        TextView tv_hongbao_fee;

        NoScrollGirdView gv_order_details;
        NoScrollGirdView tv_elm_zengpin;
        NoScrollGirdView gv_youhui;
        //        NoScrollGirdView gv_extra;
        RelativeLayout rl_order_title;
        TextView tv_printing;
        TextView order_num;
        TextView receiver_add;
        TextView receiver_name_phone;
        TextView tv_songda_time;
        TextView tv_xiadan_time;
        TextView tv_remark;
        TextView tv_fapiao_title;
        TextView tv_fapiao_amount;
        TextView tv_canhe_num;
        TextView tv_canhe_account;
        TextView tv_peisong_fee;
        //        TextView tv_youhui_fee;
        TextView tv_zongji_youhui;
        TextView actually_pay_fee;
        TextView order_bianhao;
        TextView food_name;
        TextView food_num;
        TextView food_total_fee;
        TextView b_remark;
        TextView b_fapiao;
        TextView b_fapiao_account;
        Button print_again;
        ProgressBar pb_printing;
    }

}
