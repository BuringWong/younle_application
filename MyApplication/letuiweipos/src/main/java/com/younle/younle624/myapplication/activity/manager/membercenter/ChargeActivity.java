package com.younle.younle624.myapplication.activity.manager.membercenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.manager.orderpager.pos.PayQRCodeActivity;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.domain.SavedFailOrder;
import com.younle.younle624.myapplication.domain.membercharge.MemberChargeInfoBean;
import com.younle.younle624.myapplication.utils.CashCardPayUtils;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetworkUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.MD5;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import zxing.activity.CaptureActivity;

public class ChargeActivity extends Activity {
    private static final String TAG = "ChargeActivity";
    private ListView lv_charge_option;
    private ChargeOptionAdapter adapter;
    private List colors=new ArrayList<>();
    private View headView;
    private PopupWindow popupWindow;
    private RelativeLayout rl_charger;
    private View popView;
    private String json;
    private MemberChargeInfoBean memberChargeInfoBean;
    private String memberCode;
    private List<MemberChargeInfoBean.MsgBean.RulesBean> rules;
    private String vipcard_id = "0";
    private ImageView iv_title;
    private TextView tv_title;
    private final double MAX_MONEY = 999999.99;
    private final double MIN_MONEY = 0.01;
    private String trade_time="";
    /**
     * 赠送的money
     */
    private String give_money="";
    private DecimalFormat df = new DecimalFormat("0.00");
    /**
     * 用戶实际支付金额
     */
    private String payMoney = "0";
    private String song_money = "0";
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){

        }
    };
    /**
     * 实际到账户余额的的金额
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);
        //获取json
        json = getIntent().getStringExtra("member_info");
        LogUtils.Log("json=="+json);
        memberCode = getIntent().getStringExtra("member_code");
        LogUtils.e(TAG,"Constant.MEMBER_ID--="+Constant.MEMBER_ID);
        initView();
        initData();
        setListener();
    }
    private void initView() {
        iv_title = (ImageView)findViewById(R.id.iv_title);
        iv_title.setVisibility(View.VISIBLE);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("会员充值");
        lv_charge_option = (ListView)findViewById(R.id.lv_charge_option);
        rl_charger = (RelativeLayout)findViewById(R.id.rl_charger);
        initHeadView();
    }
    private ImageView iv_member_icon;
    private TextView member_name;
    private TextView tv_member_num;
    private TextView tv_left_money;
    /**
     * 头布局
     */
    private void initHeadView() {
        headView = View.inflate(this, R.layout.member_charge_head, null);
        iv_member_icon = (ImageView)headView.findViewById(R.id.iv_member_icon);
        member_name = (TextView)headView.findViewById(R.id.member_name);
        tv_member_num = (TextView)headView.findViewById(R.id.tv_member_num);
        tv_left_money = (TextView)headView.findViewById(R.id.tv_left_money);
        headView.findViewById(R.id.tv_charge_notice).setVisibility(View.VISIBLE);
    }

    private void initData() {
        if(json!=null) {
            Gson gson=new Gson();
            memberChargeInfoBean = gson.fromJson(json, MemberChargeInfoBean.class);

            //初次进来保存的二维码url
            /*String url = memberChargeInfoBean.getMsg().getUrl();
            if(url!=null&& !TextUtils.isEmpty(url)) {
                Constant.query_member_left= url;
            }*/

            ImageOptions imageOptions = new ImageOptions.Builder()
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setCircular(true)
                    .setIgnoreGif(false)
                    .setCrop(true)//是否对图片进行裁剪
                    /*.setFailureDrawableId(R.mipmap.loadfailed)
                    .setLoadingDrawableId(R.mipmap.loading)*/
                    .build();

            x.image().bind(iv_member_icon, memberChargeInfoBean.getMsg().getHeadimgurl(),imageOptions);
            member_name.setText(memberChargeInfoBean.getMsg().getName());
            tv_member_num.setText("卡号：" + memberCode);
            Constant.currentLeft = memberChargeInfoBean.getMsg().getMoney();
            tv_left_money.setText("卡内余额：￥" + df.format(Constant.currentLeft));
            rules = memberChargeInfoBean.getMsg().getRules();
            vipcard_id = memberChargeInfoBean.getMsg().getId();
        }
        initColors();
        adapter=new ChargeOptionAdapter();
        lv_charge_option.setAdapter(adapter);
        lv_charge_option.addHeaderView(headView);
    }

    private void initColors() {
        int rgb1 = Color.rgb(123, 154, 209);
        int rgb2 = Color.rgb(94,202,192);
        int rgb3 = Color.rgb(94,161,202);
        int rgb4 = Color.rgb(202,94,113);
        int rgb5 = Color.rgb(94,202,115);
        colors.add(rgb1);
        colors.add(rgb2);
        colors.add(rgb3);
        colors.add(rgb4);
        colors.add(rgb5);
    }

    private void setListener() {
        iv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onRestart() {
        LogUtils.Log("充值界面onrestart");
        if(Constant.CHARGE_SUCCESS) {//充值成功
            LogUtils.Log("currentleft=="+Constant.currentLeft);
            LogUtils.Log("toaccount=="+Constant.toaccount);
            Constant.currentLeft=Constant.currentLeft+Constant.toaccount;
            LogUtils.Log("新的余额："+(Constant.currentLeft));
            tv_left_money.setText("卡内余额：￥"+df.format(Constant.currentLeft));
        }
        super.onRestart();
    }

    /**
     * 发起支付
     */
    private void pay() {
        //显示popWindow
        initPopuWindow();
        Constant.UNKNOWN_COMMODITY="2";
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(1);
            }
        });
        //取消付款
        popView.findViewById(R.id.tv_cancel_pay_money).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //微信付款
        popView.findViewById(R.id.rl_wx_pay_money).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constant.OPEN_WXPAY == 1){
                    if (!judgeNet()) {
                        return;
                    }
                    if(Constant.OPEN_WXPAY_MICRO==3){
                        toPayQRCodeActivity();
                    }else{
                        toCaptureActivity("0");
                    }
                }else{
                    //没开通微信：点击标记为微信记账
                   /* if(Utils.judgeIsFirstRemarkPay(ChargeActivity.this)){
                        Constant.payway = "8";
                        crashCardCharge();
                    }*/
                   Utils.judgeIsFirstRemarkPay(ChargeActivity.this, new Utils.OnSureToNext() {
                       @Override
                       public void onSure() {
                           Constant.payway = "8";
                           crashCardCharge();
                       }
                   });
                }
            }
        });
        //支付宝付款
        popView.findViewById(R.id.rl_zfb_pay_money).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //zfbCharge();
                if(Constant.OPEN_ALIPAY == 1){
                    if (!judgeNet()) {
                        return;
                    }
                    toCaptureActivity("1");
                }else{
                    //没开通支付宝：点击标记为支付宝记账
                    Utils.judgeIsFirstRemarkPay(ChargeActivity.this, new Utils.OnSureToNext() {
                        @Override
                        public void onSure() {
                            Constant.payway = "9";
                            crashCardCharge();
                        }
                    });
                }
            }
        });
        //现金记账
        popView.findViewById(R.id.rl_cash_pay_money).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.payway = "3";
                crashCardCharge();
            }
        });
        //刷卡记账
        popView.findViewById(R.id.rl_card_pay_money).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Constant.payway = "2";
                Constant.payway = "6";
                crashCardCharge();
            }
        });
    }

    private void initPopuWindow() {
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popView = inflater.inflate(R.layout.member_charge_pup, null);
        TextView charge_money= (TextView) popView.findViewById(R.id.tv_charge_pay_money);
        TextView tv_ali_pay= (TextView) popView.findViewById(R.id.tv_ali_pay);
        TextView tv_wx_pay= (TextView) popView.findViewById(R.id.tv_wx_pay);

        //判断是否开通微信/支付宝权限
        if(Constant.OPEN_ALIPAY != 1){
            //显示为记账
            tv_ali_pay.setText("支付宝收款(记账)");
        }
        if(Constant.OPEN_WXPAY != 1){
            //显示为记账
            tv_wx_pay.setText("微信收款(记账)");
        }
        charge_money.setText("￥" + payMoney);
        popupWindow = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setAlpha(0.5f);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.pop_animation);
        popupWindow.showAtLocation(ChargeActivity.this.findViewById(R.id.rl_charger), Gravity.BOTTOM, 0, 0);
    }

    /**
     * 去被扫码页面
     */
    private void toPayQRCodeActivity() {
        if (!judgeNet()) {
            return;
        }
        Constant.LAST_ORDER_NO = "";
        Intent intent = new Intent(ChargeActivity.this, PayQRCodeActivity.class);
        intent.putExtra("payment", payMoney);//传paymoney
        intent.putExtra("vip_card", memberChargeInfoBean.getMsg().getId());//vip_cardId
        intent.putExtra("give_money", give_money);
        intent.putExtra("total_fee", payMoney);
        intent.putExtra(Constant.FROME_WHERE, Constant.MEMBER_CHARGE_PAY);
        startActivity(intent);
        if(popupWindow!=null){
            popupWindow.dismiss();
        }
    }

    /**
     * 判断所有的支付条件
     * @return
     */
    private boolean judgeNet() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Utils.showToast(this, this.getResources().getString(R.string.no_network_please_cash_card));
            return false;
        } else {
            return true;
        }
    }

    /**
     * 现金和刷卡充值（现金和刷卡无论是否成功，均需要改变当前余额）
     */
    private void crashCardCharge() {
        popupWindow.dismiss();
        trade_time = Utils.getTradeTime();//秒
        String trade_num = Utils.getTradeNum();//"order_no"
        SavedFailOrder cashOrder=Utils.getSaveOrder("0",trade_num,"0",payMoney,"0",trade_time,"1","0",payMoney,
                vipcard_id,payMoney,song_money,"0","0","");
        CashCardPayUtils.getInstance().cashCardPay(this, cashOrder, null, null);
//        Constant.CHARGE_SUCCESS=true;
    }

    /**
     * 微信支付充值
     */
    private void wxCharge() {
        if(judgeRangeNet()) {
            if (0 == Constant.OPEN_WXPAY) { //未开通
                if (!isLimitzfbwx) {
                    isLimitzfbwx = true;
                    requireForWXZFB(0);
                }
            } else {//已开通
                toCaptureActivity("0");
            }
        }
    }
    /**
     * 支付宝充值
     */
    private void zfbCharge() {
        if(judgeRangeNet()) {
            if (0 == Constant.OPEN_ALIPAY) { //未开通
                if (!isLimitzfbwx) {
                    isLimitzfbwx = true;
                    requireForWXZFB(1);
                }
            } else {//已开通
                toCaptureActivity("1");
            }
        }
    }

    /**
     * 会与通过微信支付宝充值的order_no在CaptureActivity的getPrarms中统一生成
     * @param payWay
     */
    private void toCaptureActivity(String payWay) {
        trade_time = Utils.getTradeTime();
        Constant.payway=payWay;
        Intent intent=new Intent(ChargeActivity.this, CaptureActivity.class);
        intent.putExtra(Constant.FROME_WHERE,Constant.MEMBER_CHARGE_PAY);
        //intent.putExtra(Constant.MONEY, payMoney);//传paymoney
        intent.putExtra("payment", payMoney);//传paymoney
        intent.putExtra("vip_card", memberChargeInfoBean.getMsg().getId());//vip_cardId
        intent.putExtra("give_money", give_money);
        intent.putExtra("total_fee",payMoney);

        startActivity(intent);
        popupWindow.dismiss();
    }

    private boolean judgeRangeNet(){
        Double money = Double.valueOf(payMoney);
        if(money>MAX_MONEY) {
            Toast.makeText(this, this.getResources().getString(R.string.pay_money_max), Toast.LENGTH_SHORT).show();
            return false;
        }else if(money<MIN_MONEY) {
            Toast.makeText(this, this.getResources().getString(R.string.pay_money_min), Toast.LENGTH_SHORT).show();
            return false;
        }else if(!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, this.getResources().getString(R.string.no_network_please_cash_card), Toast.LENGTH_SHORT).show();
            return false ;
        }else {
            return true;
        }
    }
    private void setAlpha(float alpha) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha=alpha;
        getWindow().setAttributes(params);
    }
    class ChargeOptionAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if(rules!=null&&rules.size()>0) {
                return rules.size();
            }else {
                return 0;
            }
        }
        @Override
        public Object getItem(int position) {
            return rules.get(position);
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
                convertView=View.inflate(ChargeActivity.this, R.layout.charge_option_item,null);
                holder.option= (Button) convertView.findViewById(R.id.btn_content);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }
            int color = position % colors.size();
            final MemberChargeInfoBean.MsgBean.RulesBean rulesBean = rules.get(position);
            holder.option.setText("充值"+rulesBean.getMoney()+"送"+rulesBean.getSong());
            holder.option.setBackgroundColor((Integer) colors.get(color));
            holder.option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Utils.isNetworkAvailable(ChargeActivity.this)) {
                        Constant.CHARGE_SUCCESS=false;
                        payMoney = rulesBean.getMoney();
                        give_money=rulesBean.getSong();
                        song_money = rulesBean.getSong();
                        Constant.toaccount = Double.parseDouble(rulesBean.getZong());
                        pay();
                    }else {
                        Toast.makeText(ChargeActivity.this, "网络异常，请检查网络后再试!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return convertView;
        }
        class ViewHolder{
            Button option;
        }
    }
    private boolean isLimitzfbwx;
    /**
     * 请求支付宝或者微信是否开通支付
     */
    private void requireForWXZFB( final int tag) {
        //获取时间戳 1473410972494并截取前10位
        String currentTime = Utils.getCurrentTime();
        //获取token
        String token = Utils.getToken(currentTime,this);

        OkHttpUtils.post()
                .url(UrlConstance.QUERY_PAY_STATUS)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams(Constant.PARAMS_NEME_PASSDORD_MD5, MD5.md5(MD5.md5(Constant.PASSWORD)))//密码
                .addParams("adv_id", Constant.ADV_ID)
                .build()
                .writeTimeOut(5000)
                .readTimeOut(5000)
                .connTimeOut(5000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        isLimitzfbwx = false;
                        Toast.makeText(ChargeActivity.this, "当前网络异常，请稍后重试！", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(String response) {
                        isLimitzfbwx = false;
                        boolean success = Utils.checkSaveToken(ChargeActivity.this, response);
                        if (success) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int code = jsonObject.getInt("code");
                                if (code == 200) {//返回成功
                                    int certifite = jsonObject.getJSONObject("msg").getInt("certifite");
                                    int alipay = jsonObject.getJSONObject("msg").getInt("alipay");
                                    int micro = jsonObject.getJSONObject("msg").getInt("micro");
                                    Constant.OPEN_ALIPAY = alipay;
                                    Constant.OPEN_WXPAY = certifite;
                                    Constant.OPEN_WXPAY_MICRO = micro;
                                    if (tag == 1 && Constant.OPEN_ALIPAY == 1) {//支付宝
                                        toCaptureActivity("1");
                                    } else if (tag == 0 && Constant.OPEN_WXPAY == 1) {//微信
                                        if(Constant.OPEN_WXPAY_MICRO==3){
                                            toPayQRCodeActivity();
                                        }else{
                                            toCaptureActivity("0");
                                        }
                                    } else {
                                        showNoBandPay(tag);
                                    }
                                } else {
                                    showNoBandPay(tag);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
    /**
     * 展示没有绑定提示的Dialog
     * @param tag
     */
    private void showNoBandPay(int tag) {
        String dialogMessage = "";
        if(tag == 1){//支付宝
            dialogMessage = ChargeActivity.this.getResources().getString(R.string.click_zfb_no_band);
        }else if(tag == 0){//微信
            dialogMessage = ChargeActivity.this.getResources().getString(R.string.click_wx_no_band);
        }
        AlertDialog errTokenDia;
        errTokenDia = new AlertDialog.Builder(ChargeActivity.this)
                .setMessage(dialogMessage)
                .setPositiveButton(ChargeActivity.this.getResources().getString(R.string.determine), null)
                .show();
        errTokenDia.setCancelable(false);
        errTokenDia.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
    }
    /**
     * 随机生成的订单号
     */
    public String  getTradeNum() {
       return Utils.getCurrentTime1()+Constant.ACCOUNT_ID;
    }

    @Override
    protected void onDestroy() {
        Constant.MEMBER_ID="";
        super.onDestroy();
    }
}
