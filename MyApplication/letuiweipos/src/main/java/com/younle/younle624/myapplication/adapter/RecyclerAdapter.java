package com.younle.younle624.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.domain.orderbean.PosPrintBean;
import com.younle.younle624.myapplication.selfinterface.OnRefundGoodsSelectListener;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.utils.notice.NoticePopuUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BurNing.Wong on 2018/7/28 0028.
 * 邮箱：tt090423@126.com
 */

public class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private  Activity context;
    private List<T> data;
    private int type;
    private static final String TAG = "RecyclerAdapter";

    public RecyclerAdapter(Activity context) {
        this.context = context;
    }

    public void setData(ArrayList<T> data, int type) {
        LogUtils.e(TAG,"data.size="+data.size()+"  type="+type);
        this.data = data;
        this.type = type;
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = context.getLayoutInflater();
        if(viewType==1) {
            LogUtils.e(TAG,"onCreateViewHolder");
            View convertView=inflater.inflate(R.layout.refund_good_item,parent,false);
            RefundGoodHolder holder1=new RefundGoodHolder(convertView);
            return holder1;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        T t = data.get(position);
        if(t instanceof  PosPrintBean.MsgBean.GoodsInfoBean) {
            final PosPrintBean.MsgBean.GoodsInfoBean bean= (PosPrintBean.MsgBean.GoodsInfoBean) t;
            RefundGoodHolder holder1= (RefundGoodHolder) holder;
            holder1.tv_name.setText(bean.getName());
            holder1.totalnum.setText("可退款数量："+ Utils.numf.format(bean.getLeft_num()));

            holder1.refund_num.setText(bean.getRefund_num()+"");
            holder1.refundacc.setText("退款单价：￥" + Utils.numf.format(bean.getRefund_price()) + "元");

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

    private OnRefundGoodsSelectListener onRefundGoodsSelectListener;

    public void setOnRefundGoodsSelectListener(OnRefundGoodsSelectListener onRefundGoodsSelectListener) {
        this.onRefundGoodsSelectListener = onRefundGoodsSelectListener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class RefundGoodHolder extends RecyclerView.ViewHolder {
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
            et_goods_num.setText(String.valueOf(Utils.numf.format(pop_goods_num)));
            et_goods_num.requestFocus();
            ImageView btn_add = (ImageView) popView.findViewById(R.id.btn_add);
            TextView tv_return = (TextView) popView.findViewById(R.id.tv_return);
            TextView tv_determine = (TextView) popView.findViewById(R.id.tv_determine);
            popView.setFocusableInTouchMode(true);
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

            final PopupWindow popupWindow = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            NoticePopuUtils.setAlpha(context, 0.6f);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(false);
            popupWindow.setFocusable(true);
            popupWindow.showAtLocation(mActivity.findViewById(R.id.refund_root), Gravity.TOP, 0, 200);
            popupWindow.update();

            btn_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtils.e(TAG,"minus position="+position);
                    pop_goods_num--;
                    if(pop_goods_num<0) {
                        pop_goods_num++;
                        Toast.makeText(mActivity,"商品数量不能小于0！",Toast.LENGTH_SHORT).show();
                    }else {
                        et_goods_num.setText(Utils.numf.format(pop_goods_num));
                    }
                }
            });
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pop_goods_num++;
                    if(pop_goods_num>goodsListBean.getLeft_num()) {
                        pop_goods_num--;
                        Toast.makeText(mActivity,"商品数量不能大于！"+Utils.numf.format(goodsListBean.getLeft_num()),Toast.LENGTH_SHORT).show();
                    }else {
                        et_goods_num.setText(Utils.numf.format(pop_goods_num));
                    }
                }
            });

            tv_determine.setOnClickListener(new View.OnClickListener() {//确定
                @Override
                public void onClick(View v) {
                    Double inputNum = Double.valueOf(et_goods_num.getText().toString());
                    if (inputNum < 0 || inputNum > Double.valueOf(goodsListBean.getLeft_num())) {
                        Toast.makeText(mActivity, "请输入0~" + Utils.numf.format(goodsListBean.getLeft_num()) + "区间的数量！", Toast.LENGTH_SHORT).show();
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
