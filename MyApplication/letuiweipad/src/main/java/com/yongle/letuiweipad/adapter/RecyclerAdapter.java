package com.yongle.letuiweipad.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.domain.BlueToothBean;
import com.yongle.letuiweipad.domain.PosPrintBean;
import com.yongle.letuiweipad.domain.createorder.GoodBean;
import com.yongle.letuiweipad.domain.createorder.RulesBean;
import com.yongle.letuiweipad.domain.finishedorder.PosOrderBean;
import com.yongle.letuiweipad.domain.goodinfo.GoodsTypeInfo;
import com.yongle.letuiweipad.domain.printer.PrintGroupBean;
import com.yongle.letuiweipad.domain.printer.SavedPrinter;
import com.yongle.letuiweipad.domain.unpayorder.UnPayBean;
import com.yongle.letuiweipad.selfinterface.OnRefundGoodsSelectListener;
import com.yongle.letuiweipad.selfinterface.RecyclerItemClickListener;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.NoticePopuUtils;
import com.yongle.letuiweipad.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/22 0022.
 */

public class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity context;
    private List<T> data;
    private RecyclerItemClickListener itemClickListener;
    public static final int UNPAY_TYPE = 1;
    public static final int FINISH_TYPE = 2;
    public static final int MEMBER_CHARGE_ITEM = 3;
    public static final int YUN_PRINTERS = 4;
    public static final int BT_DEVICES = 5;
    public static final int PRINT_GROUP_INFO = 6;
    public static final int PAY_MEMBER_CHARGE_ITEM = 7;
    public static final int GOODS_INFO = 8;
    public static final int GOODS_TYPE_INFO = 9;
    public static final int REFUND_GOODS = 10;
    private int type;
    private int finishListType;

    public RecyclerAdapter(Activity context) {
        this.context = context;
    }

    public void setData(List<T> data, int type) {
        this.data = data;
        this.type = type;
    }

    public void setItemClickListener(RecyclerItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = context.getLayoutInflater();
        switch (viewType) {
            case 1://未结账
                View convertView = inflater.inflate(R.layout.unpay_list_item_layout, parent, false);
                ContentViewHolder holder1 = new ContentViewHolder(convertView);
                return holder1;
            case 2://已经结账
                convertView = inflater.inflate(R.layout.finished_list_item_layout, parent, false);
                FinishedViewHolder holder2 = new FinishedViewHolder(convertView);
                return holder2;
            case 3:
                convertView = inflater.inflate(R.layout.charge_item_layout, parent, false);
                ChargeHolder holder3 = new ChargeHolder(convertView);
                return holder3;
            case 4://已添加的云打印机
                convertView=inflater.inflate(R.layout.yunprinter_item,parent,false);
                YunPrinterHolder  holder4=new YunPrinterHolder(convertView);
                return  holder4;
            case 5://蓝牙搜索列表
                convertView=inflater.inflate(R.layout.bluetooth_device_choose_item,parent,false);
                BtDeviceHolder holder5=new BtDeviceHolder(convertView);
                return holder5;
            case 6://蓝牙搜索列表
                convertView=inflater.inflate(R.layout.group_info_layout,parent,false);
                PrintGroupHolder holder6=new PrintGroupHolder(convertView);
                return holder6;
            case 7:
                convertView = inflater.inflate(R.layout.pay_charge_item_layout, parent, false);
                PayChargeHolder holder7 = new PayChargeHolder(convertView);
                return holder7;
            case 8://商品
                convertView = inflater.inflate(R.layout.good_item, parent, false);
                GoodsHolder holder8 = new GoodsHolder(convertView);
                return  holder8;
            case 9://商品类目
                convertView = inflater.inflate(R.layout.order_right_item, parent, false);
                KindsHolder holder9 = new KindsHolder(convertView);
                return  holder9;
            case 10://退款商品列表
                convertView=inflater.inflate(R.layout.refund_good_item,parent,false);
                RefundGoodHolder holder10=new RefundGoodHolder(convertView);
                return holder10;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        T t = data.get(position);
        if (holder instanceof ContentViewHolder) {
            if (t instanceof UnPayBean.MsgBean) {
                UnPayBean.MsgBean bean = (UnPayBean.MsgBean) t;
                ((ContentViewHolder) holder).orderNo.setText(bean.getQuery_num());
                String goodNum=bean.getGoods_num()==null?"0":bean.getGoods_num();
                ((ContentViewHolder) holder).goodNum.setText(Utils.dropZero(goodNum));
                Double formatPayment = Double.valueOf(bean.getPayment());
                LogUtils.e("adapter",formatPayment+"");
                ((ContentViewHolder) holder).orderFee.setText("￥" + Utils.numdf.format(formatPayment));
                ((ContentViewHolder) holder).createTime.setText(bean.getAddtime());
                if (bean.isSelected()) {
                    ((ContentViewHolder) holder).itemView.setBackgroundResource(R.drawable.unpay_item_selector);
                    ((ContentViewHolder) holder).point.setVisibility(View.VISIBLE);
                } else {
                    ((ContentViewHolder) holder).itemView.setBackgroundColor(Color.WHITE);
                    ((ContentViewHolder) holder).point.setVisibility(View.INVISIBLE);
                }
                ((ContentViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(itemClickListener!=null) {
                            itemClickListener.onItemClick(((ContentViewHolder) holder).itemView, position);
                        }
                    }
                });
            }
        } else if (holder instanceof FinishedViewHolder) {
            PosOrderBean.MsgBean.OrderListBean finishedOrderBean= (PosOrderBean.MsgBean.OrderListBean) t;

            ((FinishedViewHolder) holder).orderTime.setText(finishedOrderBean.getDate()+" "+finishedOrderBean.getTime());
            if(finishListType==0) {
                String remark = finishedOrderBean.getRemark();
                if(remark==null|| TextUtils.isEmpty(remark)) {
                    remark="无";
                }
                ((FinishedViewHolder) holder).orderNo.setVisibility(View.VISIBLE);
                ((FinishedViewHolder) holder).orderRemark.setVisibility(View.VISIBLE);
                ((FinishedViewHolder) holder).goodNum.setVisibility(View.VISIBLE);
                ((FinishedViewHolder) holder).orderWay.setVisibility(View.VISIBLE);

                ((FinishedViewHolder) holder).orderNo.setText(finishedOrderBean.getDayOrderNum());
                ((FinishedViewHolder) holder).orderRemark.setText(remark);
                ((FinishedViewHolder) holder).goodNum.setText(Utils.dropZero(finishedOrderBean.getGoodsNum()));
                ((FinishedViewHolder) holder).orderWay.setText(finishedOrderBean.getPayTool());
            }else {
                ((FinishedViewHolder) holder).orderNo.setVisibility(View.GONE);
                ((FinishedViewHolder) holder).orderRemark.setVisibility(View.GONE);
                ((FinishedViewHolder) holder).goodNum.setVisibility(View.GONE);
                ((FinishedViewHolder) holder).orderWay.setVisibility(View.GONE);
            }
            ((FinishedViewHolder) holder).payWay.setText(finishedOrderBean.getPayType());
            setPayType(finishedOrderBean.getPayType(),((FinishedViewHolder) holder).iv_payway);
            ((FinishedViewHolder) holder).orderFee.setText("￥"+finishedOrderBean.getIncome());
            ((FinishedViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemClickListener!=null) {

                        itemClickListener.onItemClick(((FinishedViewHolder) holder).itemView, position);
                    }
                }
            });
        } else if (holder instanceof RecyclerAdapter.ChargeHolder) {
            RulesBean rulesBean = (RulesBean ) t;
            ((ChargeHolder) holder).tv_chong.setText(rulesBean.getMoney()+"元");
            ((ChargeHolder) holder).tv_song.setText("赠送"+rulesBean.getSong()+"元");
            if(rulesBean.isChecked()) {
                ((ChargeHolder) holder).itemView.setBackgroundResource(R.drawable.charge_item_selected);
                ((ChargeHolder) holder).tv_chong.setTextColor(Color.WHITE);
                ((ChargeHolder) holder).tv_song.setTextColor(Color.WHITE);
            }else {
                ((ChargeHolder) holder).itemView.setBackgroundResource(R.drawable.charge_item_normal);
                ((ChargeHolder) holder).tv_chong.setTextColor(Color.parseColor("#333333"));
                ((ChargeHolder) holder).tv_song.setTextColor(Color.parseColor("#569e09"));
            }
            ((ChargeHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemClickListener!=null) {
                        itemClickListener.onItemClick(((ChargeHolder) holder).itemView, position);
                    }
                }
            });

        }else if(holder instanceof RecyclerAdapter.PayChargeHolder) {
            RulesBean rulesBean = (RulesBean ) t;
            ((PayChargeHolder) holder).tv_chong.setText(rulesBean.getMoney()+"元");
            if(position==0) {
                ((PayChargeHolder) holder).tv_temp.setVisibility(View.GONE);
                ((PayChargeHolder) holder).tv_song.setVisibility(View.GONE);
            }else {
                ((PayChargeHolder) holder).tv_song.setVisibility(View.VISIBLE);
                ((PayChargeHolder) holder).tv_song.setText(rulesBean.getSong()+"元");
            }
            ((PayChargeHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemClickListener!=null) {
                        itemClickListener.onItemClick(((PayChargeHolder) holder).itemView, position);
                    }
                }
            });
        }else if(holder instanceof RecyclerAdapter.YunPrinterHolder) {
            SavedPrinter printer= (SavedPrinter) t;
            YunPrinterHolder printerHolder= (YunPrinterHolder) holder;
            printerHolder.printer_name.setText(printer.getName()+"(云设备)");
            printerHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener!=null) {
                        itemClickListener.onItemClick(((YunPrinterHolder) holder).itemView,position);
                    }
                }
            });
        }else if(holder instanceof BtDeviceHolder) {
            BlueToothBean blueToothBean = (BlueToothBean) t;
            BtDeviceHolder btDeviceHolder= (BtDeviceHolder) holder;
            if(blueToothBean.getDevice().getName()==null) {
                btDeviceHolder.tv_bluetooth_name.setText("未命名设备");
            }else {
                btDeviceHolder.tv_bluetooth_name.setText(blueToothBean.getDevice().getName());
            }
            btDeviceHolder.tv_device_state.setText("("+blueToothBean.getState()+")");
            if("未配对".equals(blueToothBean.getState())) {
                btDeviceHolder.tv_device_state.setTextColor(Color.rgb(41,41,41));
            }else {
                btDeviceHolder.tv_device_state.setTextColor(Color.GREEN);
            }
            btDeviceHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener!=null) {
                        itemClickListener.onItemClick(((BtDeviceHolder) holder).itemView,position);
                    }
                }
            });
        }else if(holder instanceof  PrintGroupHolder) {
            PrintGroupBean printGroupBean= (PrintGroupBean) t;
            final PrintGroupHolder printGroupHolder= (PrintGroupHolder) holder;
            if(printGroupBean.isChecked()) {
                printGroupHolder.iv_group_check_logo.setBackgroundResource(R.drawable.selected);
            }else {
                printGroupHolder.iv_group_check_logo.setBackgroundResource(R.drawable.unselect);
            }
            printGroupHolder.tv_group_name.setText(printGroupBean.getGroup_name());
            printGroupHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener!=null) {
                        itemClickListener.onItemClick(printGroupHolder.itemView,position);
                    }
                }
            });
        }else if(holder instanceof  GoodsHolder) {
            GoodBean goodBean= (GoodBean) t;
            final GoodsHolder goodsHolder= (GoodsHolder) holder;
            if(goodBean.getHasStock()==1) {//有设置库存
                if(goodBean.getStock()<=0) {//售罄
                    goodsHolder.name.setTextColor(Color.parseColor("#c0c0c0"));
                    goodsHolder.goods_price.setTextColor(Color.parseColor("#c0c0c0"));
                }else {
                    goodsHolder.name.setTextColor(Color.parseColor("#7c4c21"));
                    goodsHolder.goods_price.setTextColor(Color.parseColor("#7c4c21"));
                    if(goodBean.getStockWarning()==1){//预警

                    }else{

                    }
                }
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ((GoodsHolder) goodsHolder).goods_price.getLayoutParams();
                layoutParams.setMargins(0,0,0,Utils.px2dip(context,2));
                goodsHolder.goods_price.setLayoutParams(layoutParams);
            }else{
                goodsHolder.name.setTextColor(Color.parseColor("#7c4c21"));
                goodsHolder.goods_price.setTextColor(Color.parseColor("#7c4c21"));

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ((GoodsHolder) goodsHolder).goods_price.getLayoutParams();
                layoutParams.setMargins(0,0,0,Utils.px2dip(context,6));
                goodsHolder.goods_price.setLayoutParams(layoutParams);
            }

            String goodsUnit = goodBean.getGoodsUnit();
            if(goodsUnit!=null&&!TextUtils.isEmpty(goodsUnit)) {
                goodsHolder.goods_price.setText(Utils.numdf.format(goodBean.getGoodsPrice()) + "元/" + goodsUnit);
            }else {
                goodsHolder.goods_price.setText(Utils.numdf.format(goodBean.getGoodsPrice())+"元");
            }
            String goodsName = goodBean.getGoodsName();
            if(goodsName.length()>6) {
                goodsHolder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
                goodsHolder.name.setText(goodsName);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) goodsHolder.ll_name.getLayoutParams();
                if(goodsName.length()>12) {
                    params.setMargins(0,0,0,0);
                }else {
                    params.setMargins(0,35,0,0);
                }
                goodsHolder.ll_name.setLayoutParams(params);
            }else {
                goodsHolder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                goodsHolder.name.setText(goodsName);
            }
            goodsHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener!=null) {
                        itemClickListener.onItemClick(goodsHolder.itemView,position);
                    }
                }
            });
        }else if(holder instanceof  KindsHolder) {
            GoodsTypeInfo goodsTypeInfo= (GoodsTypeInfo) t;
            final KindsHolder kindsHolder= (KindsHolder) holder;
            if (goodsTypeInfo.isChecked()) {
                kindsHolder.itemView.setBackgroundColor(Color.WHITE);
                kindsHolder.right_line.setVisibility(View.VISIBLE);
            } else {
                kindsHolder.itemView.setBackgroundColor(Color.parseColor("#ededed"));
                kindsHolder.right_line.setVisibility(View.INVISIBLE);
            }
            kindsHolder.kinds_name.setText(goodsTypeInfo.getName());
            kindsHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener!=null) {
                        itemClickListener.onItemClick(kindsHolder.itemView,position);
                    }
                }
            });
        }else if(holder instanceof RefundGoodHolder) {
            final PosPrintBean.MsgBean.GoodsInfoBean bean= (PosPrintBean.MsgBean.GoodsInfoBean) t;
            RefundGoodHolder holder1= (RefundGoodHolder) holder;
            holder1.tv_name.setText(bean.getName());
            holder1.totalnum.setText("可退款数量："+ Utils.numdf.format(bean.getLeft_num()));

            holder1.refund_num.setText(bean.getRefund_num()+"");
            holder1.refundacc.setText("退款单价：￥" + Utils.numdf.format(bean.getRefund_price()) + "元");

            if(bean.getRefund_num()>0) {
                holder1.refund_num.setVisibility(View.VISIBLE);
                holder1.btn_minus.setVisibility(View.VISIBLE);
            }else {
                holder1.refund_num.setVisibility(View.INVISIBLE);
                holder1.btn_minus.setVisibility(View.INVISIBLE);
            }
            holder1.btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(bean.getRefund_num()+1>Double.valueOf(bean.getLeft_num())) {
                        Utils.showToast(context,"不能超过最大退款数量");
                        return;
                    }
                    bean.setRefund_num(bean.getRefund_num()+1);
                    RecyclerAdapter.this.notifyItemChanged(position);
                    if(onRefundGoodsSelectListener!=null) {
                        onRefundGoodsSelectListener.onGoodsSelected();
                    }
                }
            });
            holder1.btn_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bean.setRefund_num(bean.getRefund_num()-1);
                    RecyclerAdapter.this.notifyItemChanged(position);
                    if(onRefundGoodsSelectListener!=null) {
                        onRefundGoodsSelectListener.onGoodsSelected();
                    }
                }
            });
            holder1.refund_num.setOnClickListener(new EditTextOnClickListener(context,position));
        }
    }

    private void setPayType(String payType ,ImageView view) {
        if(payType!=null) {
            switch (payType) {
                case "现金收款" :
                    view.setImageResource(R.drawable.cash_icon);
                    break;
                case "现金收款(记账)" :
                    view.setImageResource(R.drawable.cash_icon);
                    break;
                case "刷卡收款" :
                case "刷卡收款(记账)" :
                    view.setImageResource(R.drawable.card_icon);
                    break;
                case "微信收款" :
                case "微信收款(记账)" :
                    view.setImageResource(R.drawable.wx_icon);
                    break;
                case "支付宝收款" :
                case "支付宝收款(记账)" :
                    view.setImageResource(R.drawable.zfb_icon);
                    break;
                case "会员卡余额收款" :
                    view.setImageResource(R.drawable.member_card_icon);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (data != null && data.size() > 0) {
            return data.size();
        }
        return 0;
    }

    public void setFinishListType(int finishListType) {
        this.finishListType = finishListType;
    }


    static class FinishedViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.order_time)TextView orderTime;
        @BindView(R.id.order_no) TextView orderNo;
        @BindView(R.id.good_num)TextView goodNum;
        @BindView(R.id.order_way)TextView orderWay;
        @BindView(R.id.order_remark) TextView orderRemark;
        @BindView(R.id.pay_way)TextView payWay;
        @BindView(R.id.order_fee) TextView orderFee;
        @BindView(R.id.order_detail)TextView orderDetail;
        @BindView(R.id.iv_payway)ImageView iv_payway;

        View itemView;
        public FinishedViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    static class YunPrinterHolder extends RecyclerView.ViewHolder {
        View itemView;
        @BindView(R.id.printer_name)TextView printer_name;
        @BindView(R.id.printer_setting) TextView printer_setting;

        public YunPrinterHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
    static class PrintGroupHolder extends RecyclerView.ViewHolder {
        View itemView;
        @BindView(R.id.tv_group_name)TextView tv_group_name;
        @BindView(R.id.iv_group_check_logo) ImageView iv_group_check_logo;

        public PrintGroupHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
    static class BtDeviceHolder extends RecyclerView.ViewHolder {
        View itemView;
        @BindView(R.id.tv_bluetooth_name)TextView tv_bluetooth_name;
        @BindView(R.id.tv_device_state) TextView tv_device_state;

        public BtDeviceHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    static class ChargeHolder extends RecyclerView.ViewHolder {
        View itemView;
        @BindView(R.id.tv_chong)
        TextView tv_chong;
        @BindView(R.id.tv_song)
        TextView tv_song;

        public ChargeHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
    static class PayChargeHolder extends RecyclerView.ViewHolder {
        View itemView;
        @BindView(R.id.tv_chong)
        TextView tv_chong;
        @BindView(R.id.tv_song)
        TextView tv_song;
        @BindView(R.id.tv_temp)
        TextView tv_temp;

        public PayChargeHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    static class ContentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.order_no)
        TextView orderNo;
        @BindView(R.id.good_num)
        TextView goodNum;
        @BindView(R.id.order_fee)
        TextView orderFee;
        @BindView(R.id.create_time)
        TextView createTime;
        @BindView(R.id.point)
        View point;
        View itemView;

        public ContentViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

    }
    static class GoodsHolder extends RecyclerView.ViewHolder {
        View itemView;
        @BindView(R.id.tv_tight_inventory) ImageView tv_tight_inventory;
        @BindView(R.id.ll_name) LinearLayout ll_name;
        @BindView(R.id.name) TextView name;
        @BindView(R.id.goods_price) TextView goods_price;

        public GoodsHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
    static class KindsHolder extends RecyclerView.ViewHolder {
        View itemView;
        @BindView(R.id.right_line
        ) ImageView right_line;
        @BindView(R.id.kinds_name) TextView kinds_name;

        public KindsHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
    static  class RefundGoodHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView totalnum;
        TextView refundacc;
        TextView refund_num;
        ImageView btn_minus;
        ImageView btn_add;
        public RefundGoodHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            totalnum = (TextView) itemView.findViewById(R.id.totalnum);
            refundacc = (TextView) itemView.findViewById(R.id.refundacc);
            btn_minus = (ImageView) itemView.findViewById(R.id.btn_minus);
            btn_add = (ImageView) itemView.findViewById(R.id.btn_add);
            refund_num = (TextView) itemView.findViewById(R.id.refund_num);
        }
    }

    private OnRefundGoodsSelectListener onRefundGoodsSelectListener;

    public void setOnRefundGoodsSelectListener(OnRefundGoodsSelectListener onRefundGoodsSelectListener) {
        this.onRefundGoodsSelectListener = onRefundGoodsSelectListener;
    }
    class EditTextOnClickListener implements View.OnClickListener {
        private PosPrintBean.MsgBean.GoodsInfoBean goodsListBean;
        private int position;
        private double pop_goods_num;
        private Activity mActivity;

        public EditTextOnClickListener(Activity mActivity, int position) {
            this.position = position;
            this.mActivity = mActivity;
            this.goodsListBean = (PosPrintBean.MsgBean.GoodsInfoBean) data.get(position);
        }

        @Override
        public void onClick(View v) {
            final LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popView = inflater.inflate(R.layout.pop_edit_goods_num, null);
            ImageView btn_minus = (ImageView) popView.findViewById(R.id.btn_minus);
            final EditText et_goods_num = (EditText) popView.findViewById(R.id.et_goods_num);
            pop_goods_num = goodsListBean.getRefund_num();
            et_goods_num.setText(String.valueOf(Utils.numdf.format(pop_goods_num)));
            et_goods_num.requestFocus();
            ImageView btn_add = (ImageView) popView.findViewById(R.id.btn_add);
            TextView tv_return = (TextView) popView.findViewById(R.id.tv_return);
            TextView tv_determine = (TextView) popView.findViewById(R.id.tv_determine);
            popView.setFocusableInTouchMode(true);
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

//            final PopupWindow popupWindow = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            final PopupWindow popupWindow = new PopupWindow(popView,600,480);
            NoticePopuUtils.setAlpha(context, 0.8f);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(false);
            popupWindow.setFocusable(true);
            popupWindow.showAtLocation(mActivity.findViewById(R.id.refund_root), Gravity.TOP, 0, 200);
            popupWindow.update();

            btn_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pop_goods_num--;
                    if(pop_goods_num<0) {
                        pop_goods_num++;
                        Toast.makeText(mActivity,"商品数量不能小于0！",Toast.LENGTH_SHORT).show();
                    }else {
                        et_goods_num.setText(Utils.numdf.format(pop_goods_num));
                    }
                }
            });
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pop_goods_num++;
                    if(pop_goods_num>goodsListBean.getLeft_num()) {
                        pop_goods_num--;
                        Toast.makeText(mActivity,"商品数量不能大于！"+Utils.numdf.format(goodsListBean.getLeft_num()),Toast.LENGTH_SHORT).show();
                    }else {
                        et_goods_num.setText(Utils.numdf.format(pop_goods_num));
                    }
                }
            });

            tv_determine.setOnClickListener(new View.OnClickListener() {//确定
                @Override
                public void onClick(View v) {
                    Double inputNum = Double.valueOf(et_goods_num.getText().toString());
                    if (inputNum < 0 || inputNum > Double.valueOf(goodsListBean.getLeft_num())) {
                        Toast.makeText(mActivity, "请输入0~" + Utils.numdf.format(goodsListBean.getLeft_num()) + "区间的数量！", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        goodsListBean.setRefund_num(inputNum);
                    }
                    if(onRefundGoodsSelectListener!=null) {
                        onRefundGoodsSelectListener.onGoodsSelected();
                    }
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                    notifyItemChanged(position );
                }
            });
            tv_return.setOnClickListener(new View.OnClickListener() {//返回
                @Override
                public void onClick(View v) {
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                }
            });
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    NoticePopuUtils.setAlpha(context, 1);
                }
            });
        }
    }
}
