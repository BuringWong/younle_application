package com.younle.younle624.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.createorder.AddServiceGoodActivity;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.GoodBean;
import com.younle.younle624.myapplication.domain.orderbean.OrderBean;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.younle.younle624.myapplication.R.id.tv_num;

/**
 * Created by bert_dong on 2017/3/24 0024.
 * 邮箱：18701038771@163.com
 */
public class ItemContentAdapter extends BaseAdapter {

    private static final String TAG = "ItemContentAdapter";
    private boolean ever_set_weigher;
    private Handler handler;
    private Activity context;
    private List<GoodBean> entityGoodsList;
    private OrderBean orderBean;
    private int fir_pos = 0;
    private AddServiceGoodActivity.OnWeighGoodChangeListener onWeighChangeListener;
    private boolean showMemberPrice;

    public ItemContentAdapter(Activity context, Handler handler) {
        this.context=context;
        this.handler=handler;
    }

    public void setData(OrderBean orderBean) {
        this.orderBean = orderBean;
        entityGoodsList = orderBean.getGoodList();
    }
    public void setEverWeigher(boolean everWeigher) {
        this.ever_set_weigher = everWeigher;
    }

    public void setShowMemberPrice(boolean showmemberPrice) {
        this.showMemberPrice =showmemberPrice;
        LogUtils.Log("itemContentAdapter中是否展示会员价："+this.showMemberPrice);
    }

    @Override
    public int getCount() {
        int count = 0;
        for(int i=0;i<entityGoodsList.size();i++){
            if(entityGoodsList.get(i).getGoodsNum()>0){
                count++;
            }
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        return entityGoodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null) {
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.order_detail_gird_item,null);
            holder.tv_detail_name= (TextView) convertView.findViewById(R.id.tv_detail_name);
            holder.tv_item_acc= (TextView) convertView.findViewById(R.id.tv_item_acc);
            holder.tv_num= (TextView) convertView.findViewById(tv_num);
            holder.btn_add= (ImageView) convertView.findViewById(R.id.btn_add);
            holder.btn_minus= (ImageView) convertView.findViewById(R.id.btn_minus);
            holder.lv_sec_multi_goods= (ListView) convertView.findViewById(R.id.lv_sec_multi_goods);
            holder.arl_item= (RelativeLayout) convertView.findViewById(R.id.arl_item);
            holder.member_price_logo= (ImageView) convertView.findViewById(R.id.member_price_logo);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        GoodBean realGoodsBean = entityGoodsList.get(position);
        if(realGoodsBean.getGoodsNum()>0){
            holder.tv_detail_name.setText(realGoodsBean.getGoodsName());
            holder.tv_num.setText(Utils.formatPrice(realGoodsBean.getGoodsNum()));
//            holder.tv_item_acc.setText("￥" + Utils.keepTwoDecimal("" + realGoodsBean.getGoodsPrice() * realGoodsBean.getGoodsNum()));
        }else{
            entityGoodsList.remove(position);
            notifyDataSetChanged();
        }
        double vipPrice = realGoodsBean.getVipPrice();
        LogUtils.Log("普通商品："+showMemberPrice+ "  vipprice："+vipPrice);
        if(showMemberPrice&&vipPrice>=0) {
            //显示会员价，原价
            holder.member_price_logo.setVisibility(View.VISIBLE);
            holder.tv_item_acc.setText("￥" + Utils.dropZero(Utils.keepTwoDecimal(Double.valueOf(vipPrice) * realGoodsBean.getGoodsNum() + "")));
        }else {
            //仅显示原价
            holder.member_price_logo.setVisibility(View.GONE);
            holder.tv_item_acc.setText("￥" + Utils.dropZero(Utils.keepTwoDecimal("" + realGoodsBean.getGoodsPrice() * realGoodsBean.getGoodsNum())));
        }

        setBtnState(realGoodsBean,holder.btn_minus,holder.btn_add);
        holder.btn_minus.setOnClickListener(new AddMinusOnClickListener(position, 0, null,realGoodsBean));
        holder.btn_add.setOnClickListener(new AddMinusOnClickListener(position, 0, null,realGoodsBean));
        holder.tv_num.setOnClickListener(new EditTextOnClickListener(context,position,realGoodsBean));
        fir_pos = position;
        if(realGoodsBean.getGoodsList()!=null&&realGoodsBean.getGoodsList().size()>0){
            holder.lv_sec_multi_goods.setVisibility(View.VISIBLE);
            //LogUtils.e(TAG,"----------1------------"+Utils.getCurrentTimeMill());
            List<GoodBean.SizeListBean> goodsList = new ArrayList<>();
            for(int i=0;i<realGoodsBean.getGoodsList().size();i++){
                if(realGoodsBean.getGoodsList().get(i).getSizeNum()>0){
                    goodsList.add(realGoodsBean.getGoodsList().get(i));
                }
            }
            //LogUtils.e(TAG,"----------2------------"+Utils.getCurrentTimeMill());
            holder.lv_sec_multi_goods.setAdapter(new MultiGoodsAdapter(realGoodsBean,goodsList, realGoodsBean.getGoodsName()));
            holder.arl_item.setVisibility(View.GONE);
        }else{
            holder.lv_sec_multi_goods.setVisibility(View.GONE);
            holder.arl_item.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public void setOnWeighChangeListener(AddServiceGoodActivity.OnWeighGoodChangeListener onWeighChangeListener) {
        this.onWeighChangeListener = onWeighChangeListener;
    }
    /**
     * 多规格商品adapter
     */
    class MultiGoodsAdapter extends BaseAdapter {

        private List<GoodBean.SizeListBean> goodsList;
        private String goodName;
        private GoodBean realGoodsBean;

        public MultiGoodsAdapter(GoodBean realGoodsBean,List<GoodBean.SizeListBean> goodsList,String goodName) {
            this.goodsList = goodsList;
            this.goodName = goodName;
            this.realGoodsBean = realGoodsBean;
        }

        @Override
        public int getCount() {
            return goodsList.size();
        }

        @Override
        public Object getItem(int position) {
            return goodsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //LogUtils.e(TAG,"MultiGoodsAdapter getView() 测试adapter时间 开始："+position+":"+Utils.getCurrentTimeMill());
            ViewHolder holder;
            if(convertView==null) {
                holder = new ViewHolder();
                convertView=View.inflate(context, R.layout.shoppintcart_pop_item,null);
                holder.all_sec_item= (RelativeLayout) convertView.findViewById(R.id.all_sec_item);
                holder.tv_detail_name= (TextView) convertView.findViewById(R.id.tv_detail_name);
                holder.tv_item_acc= (TextView) convertView.findViewById(R.id.tv_item_acc);
                holder.tv_num= (TextView) convertView.findViewById(tv_num);
                holder.btn_add= (ImageView) convertView.findViewById(R.id.btn_add);
                holder.btn_minus= (ImageView) convertView.findViewById(R.id.btn_minus);
                holder.member_price_logo= (ImageView) convertView.findViewById(R.id.member_price_logo);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }

            GoodBean.SizeListBean sizeListBean = goodsList.get(position);
            double vipPrice = sizeListBean.getVipPrice();
            if(showMemberPrice&&vipPrice>=0) {
                //显示会员价，原价
                holder.member_price_logo.setVisibility(View.VISIBLE);
                holder.tv_item_acc.setText("￥"+Utils.dropZero(Utils.keepTwoDecimal(Double.valueOf(vipPrice) * sizeListBean.getSizeNum() + "")));
            }else {
                //仅显示原价
                holder.member_price_logo.setVisibility(View.GONE);
                holder.tv_item_acc.setText("￥"+Utils.dropZero(Utils.keepTwoDecimal(sizeListBean.getSizePrice() * sizeListBean.getSizeNum() + "")));
            }

            if(sizeListBean.getSizeNum()>0){
                holder.tv_detail_name.setText(goodName+ "-" +sizeListBean.getSizeName());
                holder.tv_num.setText(Utils.formatPrice(sizeListBean.getSizeNum()));
//                holder.tv_item_acc.setText("￥" + Utils.keepTwoDecimal("" + sizeListBean.getSizePrice() * sizeListBean.getSizeNum()));
                setBtnState(realGoodsBean,holder.btn_minus,holder.btn_add);
                holder.btn_minus.setOnClickListener(new AddMinusOnClickListener(position,1,sizeListBean,realGoodsBean));
                holder.btn_add.setOnClickListener(new AddMinusOnClickListener(position,1,sizeListBean,realGoodsBean));
                holder.tv_num.setOnClickListener(new MulitEditTextOnClickListener(context,position,sizeListBean,goodName,realGoodsBean));
            }
            if(fir_pos==entityGoodsList.size()&&position==goodsList.size()){
                handler.sendEmptyMessage(1002);
            }
            return convertView;
        }


    }
    private void setBtnState(GoodBean realGoodsBean,ImageView btn_minus,ImageView btn_add) {
        if("0".equals(realGoodsBean.getIs_weigh())||!ever_set_weigher) {
            btn_minus.setImageResource(R.drawable.raduce_party_icon);
            btn_add.setImageResource(R.drawable.add_party_icon);
        }else {
            btn_minus.setImageResource(R.drawable.weigh_del);
            if(realGoodsBean.getGoodsNum()>0) {
                btn_add.setImageResource(R.drawable.add_party_icon);
            }else {
                btn_add.setImageResource(R.drawable.weigh);
            }
        }
    }
    class AddMinusOnClickListener implements View.OnClickListener{
        private  GoodBean goodBean;
        private  int position;
        private  int tag;
        private  GoodBean.SizeListBean sizeListBean;

        public AddMinusOnClickListener(int position,int tag,GoodBean.SizeListBean sizeListBean,GoodBean goodBean) {
            this.position=position;
            this.tag = tag;
            this.sizeListBean = sizeListBean;
            this.goodBean=goodBean;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_add :
                    add(position,tag,sizeListBean,goodBean);
                    break;
                case R.id.btn_minus:
                    minus(position,tag,sizeListBean,goodBean);
                    break;
            }
        }
    }

    private void minus(int position,int tag,GoodBean.SizeListBean sizeListBean,GoodBean goodBean) {
        double changeNum=0;
        switch (tag){
            case 0://没有多规格
                if(!ever_set_weigher||"0".equals(goodBean.getIs_weigh())) {//非称重
                    changeNum=1;
                    if(goodBean.getGoodsNum()>1) {
                        goodBean.setGoodsNum(goodBean.getGoodsNum()-1);
                    }else {
                        goodBean.setGoodsNum(0);
                    }
                }else {//称重商品
                    changeNum=goodBean.getGoodsNum();
                    goodBean.setGoodsNum(0);
                }
                if(showMemberPrice&&goodBean.getVipPrice()>=0) {
                    UpdatePriceNum(-changeNum,-goodBean.getVipPrice()*changeNum,0);
                }else {
                    UpdatePriceNum(-changeNum,-goodBean.getGoodsPrice()*changeNum,0);
                }
                break;
            case 1://有多规格 判断减少的是哪个过规格的商品进行计减操作 数量为1的数量计为0
                LogUtils.e(TAG,"有多规格 判断减少的是哪个过规格的商品进行计减操作 数量为1的数量计为0");
                if(!ever_set_weigher||"0".equals(goodBean.getIs_weigh())) {//非称重
                    changeNum=1;
                    if(goodBean.getGoodsNum()>1) {
                        goodBean.setGoodsNum(goodBean.getGoodsNum()-1);
                        if(sizeListBean.getSizeNum()>1) {
                            sizeListBean.setSizeNum(sizeListBean.getSizeNum()-1);
                        }else {
                            sizeListBean.setSizeNum(0);
                        }
                    }else {
                        goodBean.setGoodsNum(0);
                        sizeListBean.setSizeNum(0);
                    }
                }else {//称重商品
                    changeNum=sizeListBean.getSizeNum();
                    if(goodBean.getGoodsList().size()>1) {
                        goodBean.setGoodsNum(goodBean.getGoodsNum()-sizeListBean.getSizeNum());
                        sizeListBean.setSizeNum(0);
                    }else {
                        goodBean.setGoodsNum(0);
                        sizeListBean.setSizeNum(0);
                    }
                }
                if(showMemberPrice&&sizeListBean.getVipPrice()>=0) {
                    UpdatePriceNum(-changeNum,-sizeListBean.getVipPrice()*changeNum,0);
                }else {
                    UpdatePriceNum(-changeNum,-sizeListBean.getSizePrice()*changeNum,0);
                }
                break;
        }
    }

    private void add(int position,int tag,GoodBean.SizeListBean sizeListBean,GoodBean goodBean) {
        switch (tag){
            case 0://没有多规格
                //增加商品数量并且更新主视图
                if (ever_set_weigher&&"1".equals(goodBean.getIs_weigh())&&onWeighChangeListener!=null) {//称重
                    onWeighChangeListener.weigh(goodBean,-1);
                }else {
                    for(int i = 0; i< Constant.localOrderBean.getGoodList().size(); i++){
                        if(entityGoodsList.get(position).getGoodsId().equals(Constant.localOrderBean.getGoodList().get(i).getGoodsId())){
                            LogUtils.e(TAG, "i==" + i + "  equals");
                            double num = Constant.localOrderBean.getGoodList().get(i).getGoodsNum();
                            LogUtils.e(TAG, "num==" + num);
                            Constant.localOrderBean.getGoodList().get(i).setGoodsNum(num + 1);
                            if(showMemberPrice&&Constant.localOrderBean.getGoodList().get(i).getVipPrice()>=0) {
                                UpdatePriceNum(1,Constant.localOrderBean.getGoodList().get(i).getVipPrice(),0);
                            }else {
                                UpdatePriceNum(1,Constant.localOrderBean.getGoodList().get(i).getGoodsPrice(),0);
                            }
                        }
                    }
                }
                break;
            case 1://有多规格：判断增加的是哪个过规格的商品进行计加操作
                LogUtils.e(TAG,"有多规格：判断增加的是哪个过规格的商品进行计加操作");
                if(sizeListBean!=null){
                    if (ever_set_weigher&&"1".equals(goodBean.getIs_weigh())&&onWeighChangeListener!=null) {
                        onWeighChangeListener.weigh(goodBean,goodBean.getGoodsList().indexOf(sizeListBean));
                    }else {
                        for(int i=0;i<Constant.localOrderBean.getGoodList().size();i++){
                            if(Constant.localOrderBean.getGoodList().get(i).getGoodsList()!=null&&Constant.localOrderBean.getGoodList().get(i).getGoodsList().size()>0){
                                for(int j=0;j<Constant.localOrderBean.getGoodList().get(i).getGoodsList().size();j++){
                                    if(sizeListBean.getSizeId().equals(Constant.localOrderBean.getGoodList().get(i).getGoodsList().get(j).getSizeId())){
                                        double sizeNum = Constant.localOrderBean.getGoodList().get(i).getGoodsList().get(j).getSizeNum();
                                        double goodsNum = Constant.localOrderBean.getGoodList().get(i).getGoodsNum();
                                        Constant.localOrderBean.getGoodList().get(i).getGoodsList().get(j).setSizeNum(sizeNum + 1);
                                        Constant.localOrderBean.getGoodList().get(i).setGoodsNum(goodsNum + 1);
                                        if(showMemberPrice&&Constant.localOrderBean.getGoodList().get(i).getGoodsList().get(j).getVipPrice()>=0) {
                                            UpdatePriceNum(1,Constant.localOrderBean.getGoodList().get(i).getGoodsList().get(j).getVipPrice(),0);
                                        }else {
                                            UpdatePriceNum(1,Constant.localOrderBean.getGoodList().get(i).getGoodsList().get(j).getSizePrice(),0);
                                        }
                                    }
                                }
                            }
                        }
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
            final PopupWindow popupWindow = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            setAlpha(0.6f);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(false);
            popupWindow.setFocusable(true);
            popupWindow.showAtLocation(mActivity.findViewById(R.id.add_service_good), Gravity.CENTER, 0, 0);
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
                            minus(position,0,null,goodsListBean);
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
                        add(position, 1, null,goodsListBean);
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

    class MulitEditTextOnClickListener implements View.OnClickListener{

        private int position;
        private double pop_goods_num;
        private Activity mActivity;
        private GoodBean.SizeListBean goodsListBean;
        private String goodName;
        private GoodBean realGoodsBean;

        public MulitEditTextOnClickListener(Activity mActivity,int position,GoodBean.SizeListBean sizeListBean, String goodName, GoodBean realGoodsBean){
            this.position = position;
            this.mActivity = mActivity;
            this.goodsListBean = sizeListBean;
            this.goodName = goodName;
            this.realGoodsBean = realGoodsBean;
        }

        @Override
        public void onClick(View v) {
            //显示PopWindow
            final LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popView = inflater.inflate(R.layout.pop_edit_goods_num_for_shopcart, null);
            ImageView btn_minus = (ImageView) popView.findViewById(R.id.btn_minus);
            final EditText et_goods_num = (EditText) popView.findViewById(R.id.et_goods_num);
            //获取分商品数量：
            pop_goods_num = goodsListBean.getSizeNum();
            et_goods_num.setText(String.valueOf(pop_goods_num));
            ImageView btn_add = (ImageView) popView.findViewById(R.id.btn_add);
            TextView tv_return = (TextView) popView.findViewById(R.id.tv_return);
            TextView tv_determine = (TextView) popView.findViewById(R.id.tv_determine);
            popView.setFocusableInTouchMode(true);
            final PopupWindow popupWindow = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(false);
            popupWindow.setFocusable(true);
            popupWindow.showAtLocation(mActivity.findViewById(R.id.add_service_good), Gravity.CENTER, 0, 0);
            popupWindow.update();

            btn_minus.setOnClickListener(new View.OnClickListener() {//减少商品
                @Override
                public void onClick(View v) {

                    String str_goods_num = et_goods_num.getText().toString();
                    if(!"".equals(str_goods_num)&&!".".equals(str_goods_num)&&Double.valueOf(str_goods_num)>=0){
                        if(pop_goods_num != Double.valueOf(str_goods_num)){
                            pop_goods_num = Double.valueOf(str_goods_num);
                            goodsListBean.setSizeNum(pop_goods_num);
                        }
                        if(pop_goods_num>0){
                            if(pop_goods_num>0&&pop_goods_num<1){
                                pop_goods_num = 0;
                                et_goods_num.setText("0");
                            }else{
                                pop_goods_num--;
                                et_goods_num.setText(String.valueOf(pop_goods_num));
                            }
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
                            goodsListBean.setSizeNum(pop_goods_num);
                        }
                        pop_goods_num++;
                        et_goods_num.setText(String.valueOf(pop_goods_num));
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
                    //存储商品到本地订单；更新显示数据
                    String str_goods_num = et_goods_num.getText().toString();
                    if("".equals(str_goods_num)||".".equals(str_goods_num)||Double.valueOf(str_goods_num)<0){
                        Toast.makeText(mActivity,"商品数量输入有误！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //设置同步显示
                    double sizeNum = goodsListBean.getSizeNum();
                    double dif = Double.valueOf(str_goods_num) - sizeNum;
                    goodsListBean.setSizeNum(Double.valueOf(str_goods_num));
                    for(int i=0;i<Constant.localOrderBean.getGoodList().size();i++){
                        for(int j=0;j<Constant.localOrderBean.getGoodList().get(i).getGoodsList().size();j++){
                            if(goodsListBean.getSizeId().equals(Constant.localOrderBean.getGoodList().get(i).getGoodsList().get(j).getSizeId())){
                                Constant.localOrderBean.getGoodList().get(i).getGoodsList().get(j).setSizeNum(Double.valueOf(str_goods_num));
                            }
                        }
                    }
                    if(showMemberPrice&&goodsListBean.getVipPrice()>=0) {
                        UpdatePriceNum(dif, goodsListBean.getVipPrice(), 1);
                    }else {
                        UpdatePriceNum(dif,goodsListBean.getSizePrice(),1);
                    }
                    if(popupWindow!=null){
                        popupWindow.dismiss();
                    }
                }
            });
        }
    }

    private void UpdatePriceNum(double num,double price,int isEditClick) {
        Message mes = Message.obtain();
        mes.what = Constant.TOTAL_ACCOUNT;
        Bundle bundle = new Bundle();
        bundle.putInt("e_c",isEditClick);
        bundle.putDouble("g_p",price);
        bundle.putDouble("g_n",num);
        mes.setData(bundle);
        handler.sendMessage(mes);
    }

    class ViewHolder{
        RelativeLayout all_sec_item;
        TextView tv_detail_name;
        TextView tv_item_acc;
        ImageView btn_minus;
        ImageView btn_add;
        TextView tv_num;
        ListView lv_sec_multi_goods;
        RelativeLayout arl_item;
        ImageView member_price_logo;
    }
}
