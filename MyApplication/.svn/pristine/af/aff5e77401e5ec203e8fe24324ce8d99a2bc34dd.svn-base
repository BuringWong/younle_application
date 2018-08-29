package com.yongle.letuiweipad.adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.domain.createorder.GoodBean;
import com.yongle.letuiweipad.domain.createorder.OrderBean;
import com.yongle.letuiweipad.domain.createorder.UnPayDetailsBean;
import com.yongle.letuiweipad.selfinterface.DetailOnDelListener;
import com.yongle.letuiweipad.selfinterface.OnWeighGoodChangeListener;
import com.yongle.letuiweipad.utils.ClicKUtils;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bert_dong on 2017/3/24 0024.
 * 邮箱：18701038771@163.com
 */
public class ItemContentAdapter<T> extends BaseAdapter {

    private static final String TAG = "ItemContentAdapter";
    private boolean ever_set_weigher;
    private Handler handler;
    private Activity context;
    private List<GoodBean> entityGoodsList;
    private OrderBean orderBean;
    private UnPayDetailsBean unpayOrderBean;
    private int fir_pos = 0;
    private OnWeighGoodChangeListener onWeighChangeListener;
    private boolean showMemberPrice;
    private int handlePosition;
    private int type=0;
    private List<UnPayDetailsBean.MsgBean.OrderGoodsBean> unPayOrderGoodList;
    public ItemContentAdapter(Activity context, Handler handler) {
        this.context=context;
        this.handler=handler;
    }

    public int getType() {
        return type;
    }

    public void setData(T t) {
        if(t instanceof OrderBean) {
            type=0;
            orderBean = (OrderBean) t;
            entityGoodsList = orderBean.getGoodList();
        }else {
            type=1;
            unpayOrderBean= (UnPayDetailsBean) t;
        }
    }
    public void setEverWeigher(boolean everWeigher) {
        this.ever_set_weigher = everWeigher;
        LogUtils.e(TAG,"ever_set_weigher:"+ever_set_weigher);
    }

    public void setShowMemberPrice(boolean showmemberPrice) {
        LogUtils.e("itemcontentAdapter","showmemberPrice=="+showmemberPrice);
        this.showMemberPrice =showmemberPrice;
    }

    @Override
    public int getCount() {
        int count = 0;
        if(type==0) {
            if(entityGoodsList!=null&&entityGoodsList.size()>0) {
                for(int i=0;i<entityGoodsList.size();i++){
                    if(entityGoodsList.get(i).getGoodsNum()>0){
                        count++;
                    }
                }
            }
        }else {
            unPayOrderGoodList = unpayOrderBean.getMsg().getOrder_goods();
            if(unPayOrderGoodList !=null&& unPayOrderGoodList.size()>0) {
                count= unPayOrderGoodList.size();
            }
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        ViewHolder holder;
        if(convertView==null) {
            convertView=inflater.inflate(R.layout.order_detail_gird_item,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        if(type==0) {//未提交，本地
            GoodBean realGoodsBean = entityGoodsList.get(position);
            selectedOrderInfo(position, holder, realGoodsBean);
        }else {//已提交，联网获取
            UnPayDetailsBean.MsgBean.OrderGoodsBean unPayGoodBean = unPayOrderGoodList.get(position);
            unPayOrderGoodInfo(position, holder, unPayGoodBean);
        }
        int[] wh=new int[2];
        Utils.getRelativeWH(context,100,85,wh);
        convertView.setMinimumHeight(wh[1]);
        return convertView;
    }

    private void selectedOrderInfo(int position, ViewHolder holder, GoodBean realGoodsBean) {
        if(realGoodsBean.getGoodsNum()>0){
            holder.tv_detail_name.setText(realGoodsBean.getGoodsName());
            holder.tv_num.setText(Utils.numdf.format(realGoodsBean.getGoodsNum()));
//            holder.tv_item_acc.setText("￥" + Utils.keepTwoDecimal("" + realGoodsBean.getGoodsPrice() * realGoodsBean.getGoodsNum()));
        }else{
            entityGoodsList.remove(position);
            notifyDataSetChanged();
        }
        double vipPrice = realGoodsBean.getVipPrice();
        if(showMemberPrice) {
            //显示会员价，
            holder.rl_member.setVisibility(View.VISIBLE);
            if(vipPrice>=0) {
                holder.tv_member_price.setText(Utils.keepTwoDecimal(vipPrice* realGoodsBean.getGoodsNum() + ""));
            }else {
                holder.tv_member_price.setText(Utils.keepTwoDecimal("" + realGoodsBean.getGoodsPrice() * realGoodsBean.getGoodsNum()));
            }
        }else {
            //仅显示原价
            holder.rl_member.setVisibility(View.GONE);
        }
//        holder.tv_item_acc.setText(Utils.dropZero(Utils.keepTwoDecimal("" + realGoodsBean.getGoodsPrice() * realGoodsBean.getGoodsNum())));
        holder.tv_item_acc.setText(Utils.keepTwoDecimal("" + realGoodsBean.getGoodsPrice() * realGoodsBean.getGoodsNum()));

        setBtnState(realGoodsBean,holder.btn_minus,holder.btn_add);
        holder.btn_minus.setOnClickListener(new AddMinusOnClickListener(position, 0, realGoodsBean));
        holder.btn_add.setVisibility(View.VISIBLE);
        holder.btn_add.setOnClickListener(new AddMinusOnClickListener(position, 0, realGoodsBean));
        holder.iv_del.setOnClickListener(new AddMinusOnClickListener(position,0,realGoodsBean));

        holder.tv_num.setOnClickListener(new EditTextOnClickListener(context,position,realGoodsBean));
        fir_pos = position;
        /*if(realGoodsBean.getGoodsList()!=null&&realGoodsBean.getGoodsList().size()>0){
            holder.lv_sec_multi_goods.setVisibility(View.VISIBLE);
            List<GoodBean.SizeListBean> goodsList = new ArrayList<>();
            for(int i=0;i<realGoodsBean.getGoodsList().size();i++){
                if(realGoodsBean.getGoodsList().get(i).getSizeNum()>0){
                    goodsList.add(realGoodsBean.getGoodsList().get(i));
                }
            }
            holder.lv_sec_multi_goods.setAdapter(new MultiGoodsAdapter(realGoodsBean,goodsList, realGoodsBean.getGoodsName()));
            holder.arl_item.setVisibility(View.GONE);
        }else{
            holder.lv_sec_multi_goods.setVisibility(View.GONE);
            holder.arl_item.setVisibility(View.VISIBLE);
        }*/
        holder.lv_sec_multi_goods.setVisibility(View.GONE);
        holder.arl_item.setVisibility(View.VISIBLE);
    }
    private DetailOnDelListener detailOnDelListener;

    public void setDetailOnDelListener(DetailOnDelListener detailOnDelListener) {
        this.detailOnDelListener = detailOnDelListener;
    }

    private void unPayOrderGoodInfo(int position, ViewHolder holder, final UnPayDetailsBean.MsgBean.OrderGoodsBean realGoodsBean) {

        if(realGoodsBean.getGoods_num()>0){
            holder.tv_detail_name.setText(realGoodsBean.getGoods_name());
            holder.tv_num.setText(Utils.formatPrice(realGoodsBean.getGoods_num()));
//            holder.tv_item_acc.setText("￥" + Utils.keepTwoDecimal("" + realGoodsBean.getGoodsPrice() * realGoodsBean.getGoodsNum()));
        }else{
            unPayOrderGoodList.remove(position);
            notifyDataSetChanged();
        }
        if(showMemberPrice) {
            double vipPrice = realGoodsBean.getGoods_price();
            //显示会员价，
            holder.rl_member.setVisibility(View.VISIBLE);
            if(vipPrice>=0) {
                holder.tv_member_price.setText(Utils.keepTwoDecimal(Utils.keepTwoDecimal(vipPrice* realGoodsBean.getGoods_num() + "")));
            }else {
                holder.tv_member_price.setText("--.--");
            }
        }else {
            //仅显示原价
            holder.rl_member.setVisibility(View.GONE);
        }
        holder.tv_item_acc.setText(Utils.keepTwoDecimal(Utils.keepTwoDecimal("" + Double.valueOf(realGoodsBean.getGoods_ori_price()) * realGoodsBean.getGoods_num())));
        if(ever_set_weigher&& TextUtils.equals("1",realGoodsBean.getIs_weigh())) {//称重
            holder.btn_minus.setVisibility(View.GONE);
        }else {
            holder.btn_minus.setVisibility(View.VISIBLE);
        }
        holder.btn_add.setVisibility(View.GONE);
        holder.tv_num.setOnClickListener(null);
        //直接提交到后台处理
        holder.btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ClicKUtils.isFastDoubleClick()) {
                    return;
                }
                startDelMinus(realGoodsBean,1);
            }
        });
        holder.iv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ClicKUtils.isFastDoubleClick()) {
                    return;
                }
                startDelMinus(realGoodsBean,realGoodsBean.getGoods_num());
            }
        });
        fir_pos = position;
    }

    private void startDelMinus(UnPayDetailsBean.MsgBean.OrderGoodsBean realGoodsBean, double num) {
        OrderBean changeBean=new OrderBean();
        changeBean.setTradeNum(unpayOrderBean.getMsg().getOrderid()+"");
        changeBean.setPrimaryKeyId(unpayOrderBean.getMsg().getOrderid()+"");
        List<GoodBean> changeGoodList=new ArrayList<>();
        GoodBean goodBean=new GoodBean();
        goodBean.setGoodsId(realGoodsBean.getId());
        goodBean.setSize_id(realGoodsBean.getSize_id());
        goodBean.setSubNum(num);
        goodBean.setGoodsNum(realGoodsBean.getGoods_num()-num);
        changeGoodList.add(goodBean);

        changeBean.setGoodList(changeGoodList);
        if(detailOnDelListener!=null) {
            String commitJson = new Gson().toJson(changeBean);
            detailOnDelListener.onMinus(commitJson);
        }
    }

    public void setOnWeighChangeListener(OnWeighGoodChangeListener onWeighChangeListener) {
        this.onWeighChangeListener = onWeighChangeListener;
    }
    /**
     * 多规格商品adapter
     */
    private void setBtnState(GoodBean realGoodsBean,ImageView btn_minus,ImageView btn_add) {
        /*btn_minus.setImageResource(R.drawable.minus);
        btn_add.setImageResource(R.drawable.add);*/
        /*if("0".equals(realGoodsBean.getIs_weigh())||!ever_set_weigher) {
            btn_minus.setImageResource(R.drawable.minus);
            btn_add.setImageResource(R.drawable.add);
        }else {
            btn_minus.setImageResource(R.drawable.weigh_del);
            if(realGoodsBean.getGoodsNum()>0) {
                btn_add.setImageResource(R.drawable.add_party_icon);
            }else {
                btn_add.setImageResource(R.drawable.weigh);
            }
        }*/
    }
    class AddMinusOnClickListener implements View.OnClickListener{
        private  GoodBean goodBean;
        private  int position;
        private  int tag;

        public AddMinusOnClickListener(int position,int tag,GoodBean goodBean) {
            this.position=position;
            this.tag = tag;
            this.goodBean=goodBean;
        }

        @Override
        public void onClick(View v) {
            handlePosition=position;
            switch (v.getId()) {
                case R.id.btn_add :
                    add(position,tag,goodBean);
                    break;
                case R.id.btn_minus:
                    minus(position,tag,goodBean);
                    break;
                case R.id.iv_del:
                    del(position,tag,goodBean);
                    break;
            }
        }
    }
    /**
     * 直接删除某个已选商品
     * @param position
     * @param tag
     * @param goodBean
     */
    private void del(int position, int tag, GoodBean goodBean) {
        double changeNum=0;
        switch (tag){
            case 0://没有多规格
                changeNum=goodBean.getGoodsNum();
                goodBean.setGoodsNum(0);
                Constant.localOrderBean.getGoodList().remove(goodBean);

                if(showMemberPrice&&goodBean.getVipPrice()>=0) {
                    UpdatePriceNum(-changeNum,-goodBean.getVipPrice()*changeNum,0);
                }else {
                    UpdatePriceNum(-changeNum,-goodBean.getGoodsPrice()*changeNum,0);
                }
                break;
        }
    }

    /**
     * 减少某个已选商品
     * @param position
     * @param tag
     * @param goodBean
     */
    private void minus(int position,int tag,GoodBean goodBean) {
        double changeNum=0;
        switch (tag){
            case 0://没有多规格
                if(!ever_set_weigher||"0".equals(goodBean.getIs_weigh())) {//非称重
                    changeNum=1;
                    if(goodBean.getGoodsNum()>1) {
                        goodBean.setGoodsNum(goodBean.getGoodsNum()-1);
                    }else {
                        goodBean.setGoodsNum(0);
                        Constant.localOrderBean.getGoodList().remove(goodBean);

                    }
                }else {//称重商品
                    changeNum=goodBean.getGoodsNum();
                    goodBean.setGoodsNum(0);
                    Constant.localOrderBean.getGoodList().remove(goodBean);

                }

                if(showMemberPrice&&goodBean.getVipPrice()>=0) {
                    UpdatePriceNum(-changeNum,-goodBean.getVipPrice()*changeNum,0);
                }else {
                    UpdatePriceNum(-changeNum,-goodBean.getGoodsPrice()*changeNum,0);
                }
                break;
        }
    }

    private void add(int position,int tag,GoodBean goodBean) {
        switch (tag){
            case 0://没有多规格
                //增加商品数量并且更新主视图

                if (ever_set_weigher&&"1".equals(goodBean.getIs_weigh())&&onWeighChangeListener!=null) {//称重
                    onWeighChangeListener.weigh(goodBean,-1,-1);
                }else {
                    goodBean.setGoodsNum(goodBean.getGoodsNum()+1);
                    if(showMemberPrice&&goodBean.getVipPrice()>=0) {
                        UpdatePriceNum(1,goodBean.getVipPrice(),0);
                    }else {
                        UpdatePriceNum(1,goodBean.getGoodsPrice(),0);
                    }
                }
                break;
        }
    }

    class EditTextOnClickListener implements View.OnClickListener{

        private int position;
        private double pop_goods_num;
        private Activity mActivity;
        private GoodBean goodsListBean;

        public EditTextOnClickListener(Activity mActivity,int position,GoodBean goodsListBean){
            this.position = position;
            this.mActivity = mActivity;
            this.goodsListBean = goodsListBean;
        }

        @Override
        public void onClick(View v) {

            final LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popView = inflater.inflate(R.layout.pop_edit_goods_num_for_shopcart, null);
            ImageView btn_minus = (ImageView) popView.findViewById(R.id.btn_minus);
            final EditText et_goods_num = (EditText) popView.findViewById(R.id.et_goods_num);
            pop_goods_num = goodsListBean.getGoodsNum();
            et_goods_num.setText(String.valueOf(pop_goods_num));
            ImageView btn_add = (ImageView) popView.findViewById(R.id.btn_add);
            TextView tv_return = (TextView) popView.findViewById(R.id.tv_return);
            TextView tv_determine = (TextView) popView.findViewById(R.id.tv_determine);
            popView.setFocusableInTouchMode(true);
            final PopupWindow popupWindow = new PopupWindow(popView,600,480);
            setAlpha(0.6f);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(false);
            popupWindow.setFocusable(true);
            popupWindow.showAtLocation(mActivity.findViewById(R.id.order_pager_layout), Gravity.CENTER, 0, 0);
            popupWindow.update();
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    setAlpha(1);
                }
            });
            btn_minus.setOnClickListener(new View.OnClickListener() {//减少商品
                @Override
                public void onClick(View v) {
                    String str_goods_num = et_goods_num.getText().toString();
                    if(!"".equals(str_goods_num)&&!".".equals(str_goods_num)&&Double.valueOf(str_goods_num)>=0){
                        if(pop_goods_num != Double.valueOf(str_goods_num)){
                            pop_goods_num = Double.valueOf(str_goods_num);
                            goodsListBean.setGoodsNum(pop_goods_num);
                        }
                        if(pop_goods_num>0){
                            if(pop_goods_num>0&&pop_goods_num<1){
                                pop_goods_num = 0;
                                et_goods_num.setText("0");
                            }else{
                                pop_goods_num--;
                                et_goods_num.setText(String.valueOf(pop_goods_num));
                            }
                            minus(position,0,goodsListBean);
                        }else{
                            Toast.makeText(mActivity,"商品数量已经减少到0！",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(mActivity,"商品数量输入有误！",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            btn_add.setOnClickListener(new View.OnClickListener() {//增加商品
                @Override
                public void onClick(View v) {
                    String str_goods_num = et_goods_num.getText().toString();
                    if(!"".equals(str_goods_num)&&!".".equals(str_goods_num)&&Double.valueOf(str_goods_num)>=0){
                        if(pop_goods_num != Double.valueOf(str_goods_num)){
                            pop_goods_num = Double.valueOf(str_goods_num);
                            goodsListBean.setGoodsNum(pop_goods_num);
                        }
                        pop_goods_num++;
                        et_goods_num.setText(String.valueOf(pop_goods_num));
                        add(position, 0, goodsListBean);
                    }else{
                        Toast.makeText(mActivity,"商品数量输入有误！",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            tv_return.setOnClickListener(new View.OnClickListener() {//返回
                @Override
                public void onClick(View v) {
                    if(popupWindow!=null){
                        popupWindow.dismiss();
                    }
                }
            });
            tv_determine.setOnClickListener(new View.OnClickListener() {//确定
                @Override
                public void onClick(View v) {
                    String str_goods_num = et_goods_num.getText().toString();
                    if("".equals(str_goods_num)||".".equals(str_goods_num)||Double.valueOf(str_goods_num)<0){
                        Toast.makeText(mActivity,"商品数量输入有误！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    double sizeNum = goodsListBean.getGoodsNum();
                    double dif = Double.valueOf(str_goods_num) - sizeNum;
                    goodsListBean.setGoodsNum(Double.valueOf(str_goods_num));
                    if(showMemberPrice&&goodsListBean.getVipPrice()>=0) {
                        UpdatePriceNum(dif, goodsListBean.getVipPrice(), 1);
                    }else {
                        UpdatePriceNum(dif,goodsListBean.getGoodsPrice(),1);
                    }
                    if(popupWindow!=null){
                        popupWindow.dismiss();
                    }
                }
            });
        }

        private void setAlpha(float alpha) {
            WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
            params.alpha = alpha;
            mActivity.getWindow().setAttributes(params);
        }
    }

    private void UpdatePriceNum(double num,double price,int isEditClick) {
        Message mes = Message.obtain();
        mes.what = Constant.TOTAL_ACCOUNT;
        Bundle bundle = new Bundle();
        bundle.putInt("e_c",isEditClick);
        bundle.putDouble("g_p",price);
        bundle.putDouble("g_n",num);
        bundle.putInt("adapter_position",-1);
        bundle.putInt("target_poisition",handlePosition);
        mes.setData(bundle);
        handler.sendMessage(mes);
    }

    class ViewHolder{
        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
        @BindView(R.id.all_sec_item)
        @Nullable
        RelativeLayout all_sec_item;
        @BindView(R.id.tv_detail_name)
        @Nullable
        TextView tv_detail_name;
        @BindView(R.id.tv_item_acc)
        @Nullable
        TextView tv_item_acc;
        @BindView(R.id.btn_minus)
        @Nullable
        ImageView btn_minus;
        @BindView(R.id.btn_add)
        @Nullable
        ImageView btn_add;
        @BindView(R.id.tv_num)
        @Nullable
        TextView tv_num;
        @BindView(R.id.lv_sec_multi_goods)
        @Nullable
        ListView lv_sec_multi_goods;
        @BindView(R.id.arl_item)
        @Nullable
        LinearLayout arl_item;
        @BindView(R.id.iv_del)
        @Nullable
        ImageView iv_del;
        @BindView(R.id.rl_member)
        @Nullable
        RelativeLayout rl_member;
        @BindView(R.id.tv_member_price)
        @Nullable
        TextView tv_member_price;
    }
}
