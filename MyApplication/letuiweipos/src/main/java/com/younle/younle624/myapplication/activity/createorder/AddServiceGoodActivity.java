package com.younle.younle624.myapplication.activity.createorder;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.manager.weigher.OnWeighChangeListener;
import com.younle.younle624.myapplication.activity.manager.weigher.WeighManager;
import com.younle.younle624.myapplication.adapter.GoodServiceAdapter;
import com.younle.younle624.myapplication.adapter.ItemContentAdapter;
import com.younle.younle624.myapplication.adapter.NewOrderLeftAdapter;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.domain.AllGoodsInfoBean;
import com.younle.younle624.myapplication.domain.CommitOrderBean;
import com.younle.younle624.myapplication.domain.GoodBean;
import com.younle.younle624.myapplication.domain.GoodKinds;
import com.younle.younle624.myapplication.domain.UnPayDetailsBean;
import com.younle.younle624.myapplication.domain.createorder.CreateOrderMemberBean;
import com.younle.younle624.myapplication.domain.orderbean.DetailMemberBean;
import com.younle.younle624.myapplication.domain.printsetting.PrintDevices;
import com.younle.younle624.myapplication.utils.ClicKUtils;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.utils.SaveUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.utils.createorder.SearchUtils;
import com.younle.younle624.myapplication.utils.notice.NoticePopuUtils;
import com.younle.younle624.myapplication.utils.scanbar.HidConncetUtil;
import com.younle.younle624.myapplication.utils.scanbar.ScanGunKeyEventHelper;
import com.younle.younle624.myapplication.view.ScanRelativeLayout;
import com.younle.younle624.myapplication.view.SelfLinearLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.MD5;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import zxing.activity.CaptureActivity;

import static com.igexin.sdk.GTServiceManager.context;
import static com.younle.younle624.myapplication.utils.SaveUtils.getObject;

/**
 * 下单页面，添加商品或服务的界面
 * 左侧种类，右侧详细信息的数据都来自数据库，右侧的实体类为roombean中的servicecontent或者goodcontent
 * 当右侧的加减按钮被点击时，将所对应的对象添加到roombean的属性集合中，
 */
public class AddServiceGoodActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener, SelfLinearLayout.ClickToReload {

    private TextView tv_title,tv_cancel,tv_mark_reback,tv_gw_num,tv_total_money,tv_loading,tv_close_pop,pup_tv_total_money
            ,pup_tv_gw_num;
    private ListView left,right,lv_order_content;
    public List<GoodKinds> kinds;
    private NewOrderLeftAdapter leftAdapter;
    private RelativeLayout rl_all_Interface_container;
    private GoodServiceAdapter rightAdapter;
    private ImageView iv_gwche,pup_iv_gwche,iv_jiazai_filure;
    private Button btn_commit,pup_btn_commit;
    private ProgressBar pb_loading;
    private String TAG = "AddServiceGoodActivity";
    private LinearLayout ll_clear,gwc_pop_layout;
    private PopupWindow detailPup;
    private AllGoodsInfoBean gotAllGoodsInfoBean;
    private String trade_num = "0";
    private String primaryKeyId = "0";
    private String query_num = "0";
    private String room_order_id = "0";
    private String room_name = "无";
    private String title_name = "选择商品";
    private int intFromWhere;
    private SelfLinearLayout ll_loading;
    /**
     * 顶部会员信息信息
     */
    private LinearLayout ll_member_info, al_member_left;
    private TextView member_name,member_average_consume, member_fav,tv_show_membertags,tv_member_left,tv_come_times;
    private RelativeLayout list_header;
    private DetailMemberBean memberBean;
    private UnPayDetailsBean order_bean ;
    private static final int UPDATE_UI = 1000;
    private long FirstTimeComeInMill;
    /**
     * 扫码枪，电子秤,搜索框
     */
    private ScanGunKeyEventHelper scanGunKeyEventHelper;
    private TextView tv_barscanner_state,tv_weigher_state,tv_cancel_search,tv_current_weigh;
    private ImageView iv_scanner_state_icon,iv_weigher_state_icon;
    private RelativeLayout rl_barscanner,rl_weigher;
    private  EditText et_search;
    private ListView lv_search_result;
    private LinearLayout ll_gs_details;
    private List<GoodBean> filterData;
    private GoodServiceAdapter filterAdapter;


    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private PrintDevices savedDevice;
    private ItemContentAdapter itemContentAdapter;
    private ScanRelativeLayout orderDetailPopView;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.TOTAL_ACCOUNT:
                    handler.removeMessages(UPDATE_UI);
                    Bundle data = msg.getData();
                    int isEditClick = data.getInt("e_c");//是不是编辑(1)还是点击(0)或者清空数据(2)
                    double goodPice = data.getDouble("g_p");//传递的数量都是变化量 不是总量
                    double goodNum = data.getDouble("g_n");
                    LogUtils.e(TAG,"goodNum=="+goodNum);
                    LogUtils.e(TAG,"goodPice=="+goodPice);
                    switch (isEditClick){
                        case 0://点击
                            Constant.order_price += goodPice;
                            Constant.order_goods_num += goodNum;
                            break;
                        case 1://编辑
                            Constant.order_price += goodPice*goodNum;
                            Constant.order_goods_num += goodNum;
                            break;
                        case 2://清空数据
                            Constant.order_price = 0.00;
                            Constant.order_goods_num = 0.00;
                            break;
                    }
                    LogUtils.e(TAG,"商品数量：" + Constant.order_goods_num+",总钱数："+Constant.order_price);
                    handler.sendEmptyMessageDelayed(UPDATE_UI,10);
                    break;
                case UPDATE_UI:
                    updateUI(Constant.order_price, Constant.order_goods_num);
                    break;
            }
        }
    };
    private int newSelectIndex=-1;
    private PopupWindow weighPop;
    private TextView tv_weighgood_name;
    private String weighUnit;
    private double simpePrice;
    private boolean everRegist=false;
    private PrintDevices savedWeigher;
    private boolean weigher_connected;
    private boolean showMemberPrice;
    private int needVipPrice;

    private String weigh_unit;
    private String weigh_mode;
    private String weigh_zf;
    private Double weigh_acc;
    private int weigh_version=0;
    private boolean everShowWeighErr=false;
    private TextView tv_weigh_mode;
    private ImageView iv_close;
    private TextView tv_center;
    private LinearLayout ll_weigh_state;
    private AlertDialog setZeroDia;


    private void updateUI(double total_money, double goods_num) {

        tv_total_money.setText("￥" + Utils.keepTwoDecimal("" + total_money));
        if (pup_tv_total_money != null) {
            pup_tv_total_money.setText("￥" + Utils.keepTwoDecimal(String.valueOf(total_money)));
        }
        if (goods_num <= 0) {
            tv_gw_num.setVisibility(View.GONE);
        } else {
            tv_gw_num.setVisibility(View.VISIBLE);
            //主页面小球
            if(goods_num>=100) {
                tv_gw_num.setText("99+");
            }else {
                tv_gw_num.setText(Utils.formatPrice(goods_num));
            }
            //pop页面小球
            if (pup_tv_gw_num != null) {
                if(goods_num>=100) {
                    pup_tv_gw_num.setText("99+");
                }else {
                    pup_tv_gw_num.setText(Utils.formatPrice(goods_num));
                }
            }
        }
        if(rightAdapter!=null){
            rightAdapter.notifyDataSetChanged();
        }
        if(itemContentAdapter!=null){
            itemContentAdapter.notifyDataSetChanged();
            if(newSelectIndex!=-1) {
                if(lv_order_content!=null) {
                    lv_order_content.setSelection(newSelectIndex);
                    newSelectIndex=-1;
                }
            }
        }
        if(filterAdapter!=null) {
            filterAdapter.notifyDataSetChanged();
        }
        if (detailPup != null && detailPup.isShowing() && (goods_num <= 0)) {
            detailPup.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service_good);
        Constant.order_price = 0.00;
        Constant.order_goods_num = 0.00;
        savedWeigher = (PrintDevices) SaveUtils.getObject(this, Constant.SAVED_BLUETOOTH_WEIGHER);
        if(savedWeigher!=null) {
            BluetoothDevice remoteDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(savedWeigher.getBlueToothAdd());
            if(remoteDevice!=null) {
                Constant.SAVED_WEIGH_NAME=remoteDevice.getName();
                if(Constant.SAVED_WEIGH_NAME==null) {
                    Constant.SAVED_WEIGH_NAME="";
                }
            }
        }
        EventBus.getDefault().register(this);
        getIntentData();
        initView();
        initWigher();
        initData();
        setListener();
        initBarScanner();
    }

    private void initBarScanner() {
        registBarScanReceiver();
        savedDevice = (PrintDevices) getObject(AddServiceGoodActivity.this, Constant.SAVED_BLUETOOTH_SCANNER);
        if(savedDevice!=null) {
            rl_barscanner.setVisibility(View.VISIBLE);
            connectScanner();
        }else {
            rl_barscanner.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化电子秤的一下操作
     */
    private void initWigher() {
        everShowWeighErr=false;
        if(savedWeigher!=null) {
            rl_weigher.setVisibility(View.VISIBLE);
            WeighManager.getInstance().registReceiver(this);
            everRegist=true;
            int weighState = WeighManager.getInstance().getWeighState();
            if(weighState==2) {
                weigher_connected=true;
            }else {
                weigher_connected=false;
            }
            WeighManager.getInstance().initState(this,tv_weigher_state,iv_weigher_state_icon,weighState);
            WeighManager.getInstance().initWigher(this, savedWeigher.getBlueToothAdd(), new OnWeighChangeListener() {
                @Override
                public void onWeighChange(String data) {
                    LogUtils.Log("onWeighChange data=="+data);
                    if(weigher_connected) {
                        if(Constant.SAVED_WEIGH_NAME.contains(Constant.WEIGHER_NAME)) {//普瑞讯
                            weigh_version=1;
                            tv_current_weigh.setVisibility(View.VISIBLE);
                            if (handleNewWeighData(data)) return;
                        }else {//博图
                            weigh_version=0;
                            tv_current_weigh.setVisibility(View.GONE);
                        }

                        upDateZeroDia(null);
                        if(weighPop!=null&&weighPop.isShowing()) {
                            updataWeighPup(data);
                        }
                    }
                }

                @Override
                public void onWeighConnected() {
                    weigher_connected=true;
                    updateAdapter();
                    WeighManager.getInstance().initState(AddServiceGoodActivity.this,tv_weigher_state,iv_weigher_state_icon,2);
                }

                @Override
                public void onWeighDiconnected() {
                    weigher_connected=false;
                    updateAdapter();
                    WeighManager.getInstance().initState(AddServiceGoodActivity.this,tv_weigher_state,iv_weigher_state_icon,0);
                    WeighManager.getInstance().connectWeigh(savedWeigher.getBlueToothAdd());
                }

                @Override
                public void onWeighConnecting() {

                }
            });
        }else {
            rl_weigher.setVisibility(View.GONE);
        }
    }

    private boolean handleNewWeighData(String data) {
        if(data.contains("E 2")||data.contains("E 1")) {
            if(everShowWeighErr) {
                return true;
            }
            everShowWeighErr=true;
            NoticePopuUtils.showBindPup(this, "电子秤异常，错误码："+data+"请联系乐推微解决！",R.id.add_service_good,null);
            return true;
        }

        try{
            weigh_unit = data.substring(14);
            //数量
            weigh_acc = Double.valueOf(data.substring(7,14));
            weigh_mode = data.substring(3, 5);
            //正负
            weigh_zf = data.substring(6, 7);
            if(TextUtils.equals("g", weigh_unit)) {
                tv_current_weigh.setText(Html.fromHtml("<u>"+weigh_zf + weigh_acc /1000+"kg"+"</u>"));
            }else if (TextUtils.equals("kg", weigh_unit)){
                tv_current_weigh.setText(Html.fromHtml("<u>"+weigh_zf + weigh_acc +"kg"+"</u>"));
            }
        }catch (Exception e){
            weigh_mode="";
            weigh_zf="";
            if(weighPop!=null&&weighPop.isShowing()) {
                tv_weigh.setText("------");
            }
            upDateZeroDia("------");
            return true;
        }
        return false;
    }

    private void updateAdapter() {
        LogUtils.Log("更新adapter");
        if(rightAdapter!=null) {
            rightAdapter.setEverWeigher(weigher_connected);
            rightAdapter.setShowMemberPrice(showMemberPrice);
            rightAdapter.notifyDataSetChanged();
        }
        if(itemContentAdapter!=null){
            itemContentAdapter.setEverWeigher(weigher_connected);
            itemContentAdapter.setShowMemberPrice(showMemberPrice);
            itemContentAdapter.notifyDataSetChanged();
        }
        if(filterAdapter!=null) {
            filterAdapter.setEverWeigher(weigher_connected);
            filterAdapter.setShowMemberPrice(showMemberPrice);
            filterAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取IntentData
     */
    private void getIntentData() {
        String trade_num_intent = getIntent().getStringExtra("trade_num");
        String id_intent = getIntent().getStringExtra("unpay_order_id");
        String query_num_intent = getIntent().getStringExtra("query_num");
        String choose_room_id = getIntent().getStringExtra("room_order_id");
        int from_where_intent = getIntent().getIntExtra("from_where", 0);
        order_bean = (UnPayDetailsBean) getIntent().getSerializableExtra("order_bean");
        memberBean = (DetailMemberBean) getIntent().getSerializableExtra(Constant.MEMBER_BEAN);
        needVipPrice = getIntent().getIntExtra("needVipPrice", 1);
        showMemberPrice =memberBean==null?false:true;
        FirstTimeComeInMill = getIntent().getLongExtra("first_come_time", 0);
        if (trade_num_intent != null) {
            trade_num = trade_num_intent;
        }
        if (id_intent != null) {
            primaryKeyId = id_intent;
        }
        if (query_num_intent != null) {
            query_num = query_num_intent;
        }
        if (choose_room_id != null) {
            room_order_id = choose_room_id;
        }
        if (id_intent != null) {
            room_name = getIntent().getStringExtra("room_name");
            Constant.localOrderBean.setCurrentRoomName(room_name);
        }
        if (from_where_intent != 0) {
            intFromWhere = from_where_intent;
        }
        String title_name_extra = getIntent().getStringExtra("title_name");
        if (title_name_extra != null) {
            title_name = title_name_extra;
        }
    }

    private void initView() {
        //扫码枪
        rl_barscanner = (RelativeLayout)findViewById(R.id.rl_barscanner);
        tv_barscanner_state = (TextView)findViewById(R.id.tv_barscanner_state);
        iv_scanner_state_icon = (ImageView)findViewById(R.id.iv_scanner_state_icon);
        //电子秤
        rl_weigher = (RelativeLayout)findViewById(R.id.rl_weigher);
        tv_weigher_state = (TextView)findViewById(R.id.tv_weigher_state);
        iv_weigher_state_icon = (ImageView)findViewById(R.id.iv_weigher_state_icon);
        tv_current_weigh = (TextView)findViewById(R.id.tv_current_weigh);
        ll_weigh_state = (LinearLayout)findViewById(R.id.ll_weigh_state);
        
        //搜索
        et_search = (EditText)findViewById(R.id.et_search);
        et_search.setHint("请输入商品首字母或全拼或中英文搜索");
        lv_search_result = (ListView)findViewById(R.id.lv_search_result);
        ll_gs_details = (LinearLayout)findViewById(R.id.ll_gs_details);
        tv_cancel_search = (TextView)findViewById(R.id.tv_cancel_search);
        //加载中视图
        iv_jiazai_filure = (ImageView) findViewById(R.id.iv_jiazai_filure);
        ll_loading = (SelfLinearLayout) findViewById(R.id.ll_loading);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
        tv_loading = (TextView) findViewById(R.id.tv_loading);

        rl_all_Interface_container = (RelativeLayout) findViewById(R.id.rl_all_Interface_container);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("添加商品");
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_cancel.setVisibility(View.VISIBLE);
        tv_mark_reback = (TextView) findViewById(R.id.tv_mark_reback);
        tv_mark_reback.setText("识别会员");
        tv_mark_reback.setVisibility(View.VISIBLE);
        left = (ListView) findViewById(R.id.lv_setting_kinds);
        right = (ListView) findViewById(R.id.lv_setting_detail);

        iv_gwche = (ImageView) findViewById(R.id.iv_gwche);
        tv_gw_num = (TextView) findViewById(R.id.tv_gw_num);
        tv_total_money = (TextView) findViewById(R.id.tv_total_money);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);

        //会员信息控件
        ll_member_info = (LinearLayout) findViewById(R.id.ll_member_info);
        al_member_left = (LinearLayout)findViewById(R.id.al_member_left);
        tv_come_times = (TextView)findViewById(R.id.tv_come_times);
        member_average_consume = (TextView)findViewById(R.id.member_average_consume);
        tv_member_left = (TextView)findViewById(R.id.tv_member_left);
        member_name = (TextView) findViewById(R.id.member_name);
        tv_show_membertags = (TextView)findViewById(R.id.tv_show_membertags);
        member_average_consume = (TextView) findViewById(R.id.member_average_consume);
        member_fav = (TextView) findViewById(R.id.member_fav);


        if (memberBean != null) {
            showMemberPrice =true;
            LogUtils.Log("memberBean=="+memberBean.toString());
            showMemberInfo();
        }
        tv_title.setText(title_name);
        if(intFromWhere==Constant.ADD_FROM_DETAILS){
            tv_cancel.setText("返回");
        }
    }

    private void initData() {
        clearLocalData();
        String currentTime = Utils.getCurrentTime();
        String token = Utils.getToken(currentTime, this);
        LogUtils.Log("请求商品storeid=" + Constant.STORE_ID);
        LogUtils.Log("请求商品advid=" + Constant.ADV_ID);
        LogUtils.Log("请求商品needVipPrice=" + needVipPrice);

        OkHttpUtils.post()
                .url(UrlConstance.GOODS_INFO)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams(Constant.PARAMS_NEME_PASSDORD_MD5, MD5.md5(MD5.md5(Constant.PASSWORD)))
                .addParams("storeid", Constant.STORE_ID)
                .addParams("advid", Constant.ADV_ID)
                .addParams("versionCode", Constant.VERSION_CODE+"")
                .addParams("needVipPrice",needVipPrice+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        LogUtils.e(TAG, "initData()---Exception e:" + e.toString());
                        netError();
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.Log("获取商品列表()---response:"+response);
                        boolean b = Utils.checkSaveToken(AddServiceGoodActivity.this, response);
                        if (b) {
                            parseJson(response);
                        }
                    }
                });
    }

    /**
     * 解析返回数据:如果有服务或者实物一种商品,隐藏另外一个RadioButton
     * @param response
     */
    private void parseJson(String response) {
        Gson gson = new Gson();
        gotAllGoodsInfoBean = gson.fromJson(response, AllGoodsInfoBean.class);
        //解析检索数据
        SearchUtils.getInstance().parseData(gotAllGoodsInfoBean);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < gotAllGoodsInfoBean.getMsg().getGoodsInfo().size(); i++) {
                    LogUtils.Log(gotAllGoodsInfoBean.getMsg().getGoodsInfo().get(i).toString());
                }
            }
        },1000);
        setAdapter(gotAllGoodsInfoBean.getMsg().getGoodsInfo());
    }

    /**
     * 设置适配器
     */
    private void setAdapter(List<AllGoodsInfoBean.MsgBean.GoodsInfoBean> goodKindsBean) {
        if(ll_loading!=null){
            ll_loading.setVisibility(View.GONE);
        }
        if (goodKindsBean != null && goodKindsBean.size() > 0) {
            AllGoodsInfoBean.MsgBean.GoodsInfoBean goodsConcreteBean = goodKindsBean.get(0);
            goodsConcreteBean.setIsChecked(true);//设置左侧初始显示
            dataChangShow(goodsConcreteBean.getGoodsList(), goodKindsBean);
        }else{
            noData();
        }
    }

    private void dataChangShow(List data ,List leftData) {

        //左侧大的种类的数据
        if (leftAdapter == null) {
            leftAdapter = new NewOrderLeftAdapter(AddServiceGoodActivity.this);
            leftAdapter.setData(leftData);
            left.setVisibility(View.VISIBLE);
            left.setAdapter(leftAdapter);
        } else {
            leftAdapter.setData(leftData);
            left.setVisibility(View.VISIBLE);
            leftAdapter.notifyDataSetChanged();
        }

        //右侧详细数据的adapter的设置
        if (rightAdapter == null) {
            rightAdapter = new GoodServiceAdapter(AddServiceGoodActivity.this,
                    handler, rl_all_Interface_container, iv_gwche, tv_gw_num);
            rightAdapter.setData(data);
            rightAdapter.setEverWeigher(weigher_connected);
            rightAdapter.setShowMemberPrice(showMemberPrice);
            rightAdapter.setWeighListener(onWeighGoodChangeListener);
            right.setAdapter(rightAdapter);
        } else {
            rightAdapter.setShowMemberPrice(showMemberPrice);
            rightAdapter.setEverWeigher(weigher_connected);
            rightAdapter.setData(data);
            rightAdapter.notifyDataSetInvalidated();
        }
        //搜索的adapter
        if(filterAdapter==null) {
            filterAdapter = new GoodServiceAdapter(AddServiceGoodActivity.this,
                    handler, rl_all_Interface_container, iv_gwche, tv_gw_num);
            filterAdapter.setEverWeigher(weigher_connected);
            filterAdapter.setShowMemberPrice(showMemberPrice);
            filterAdapter.setWeighListener(onWeighGoodChangeListener);
        }else {
            filterAdapter.setShowMemberPrice(showMemberPrice);
            filterAdapter.setEverWeigher(weigher_connected);
            filterAdapter.notifyDataSetChanged();
        }
        //已添加订单详情的adapter
        if(itemContentAdapter==null) {
            itemContentAdapter = new ItemContentAdapter(this,  handler);
        }
    }
    private OnWeighGoodChangeListener onWeighGoodChangeListener=new OnWeighGoodChangeListener() {
        @Override
        public void weigh(GoodBean goodBean, int position) {
            LogUtils.Log("position=="+position);
            weighUnit=goodBean.getGoodsUnit();
            String weighGoodName;
            boolean isDgg;
            if(goodBean.getGoodsList()!=null&&goodBean.getGoodsList().size()>0) {
                isDgg=true;
                if (showMemberPrice && goodBean.getGoodsList().get(position).getVipPrice()>=0) {
                    simpePrice = goodBean.getGoodsList().get(position).getVipPrice();
                }else {
                    simpePrice=goodBean.getGoodsList().get(position).getSizePrice();
                }
                weighGoodName = goodBean.getGoodsList().get(position).getSizeName();
            }else {
                isDgg=false;
                if (showMemberPrice && goodBean.getVipPrice()>=0) {
                    simpePrice = goodBean.getVipPrice();
                }else {
                    simpePrice=goodBean.getGoodsPrice();
                }
                weighGoodName=goodBean.getGoodsName();
            }
            showWeighPop(goodBean,weighGoodName,isDgg,position);
        }
    };
    /**
     * 设置监听
     */
    private void setListener() {
        left.setOnItemClickListener(new LeftOnItemClickListener());
        iv_gwche.setOnClickListener(this);
        tv_mark_reback.setOnClickListener(this);
        if(ll_loading!=null){
            ll_loading.setClickToReload(this);
        }
        tv_cancel.setOnClickListener(this);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterData=new ArrayList<GoodBean>();
                String input = et_search.getText().toString().trim();
                if(input.length()>0) {
                    SearchUtils.getInstance().search(input,filterData,gotAllGoodsInfoBean.getMsg().getGoodsInfo());
                }
                filterAdapter.setData(filterData);
                filterAdapter.setEverWeigher(weigher_connected);
                lv_search_result.setAdapter(filterAdapter);
            }
        });
        et_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(et_search.hasFocus()) {
                    ll_gs_details.setVisibility(View.GONE);
                    lv_search_result.setVisibility(View.VISIBLE);
                    tv_cancel_search.setVisibility(View.VISIBLE);
                }else {
                    ll_gs_details.setVisibility(View.VISIBLE);
                    lv_search_result.setVisibility(View.GONE);
                    tv_cancel_search.setVisibility(View.GONE);
                    et_search.setText(null);
                    ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(AddServiceGoodActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        tv_cancel_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_search.clearFocus();
            }
        });
        tv_show_membertags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> tags = memberBean.getMsg().getTags();
                if(tags!=null&&tags.size()>0) {
                    NoticePopuUtils.showMemberTagsPop(AddServiceGoodActivity.this,findViewById(R.id.add_service_good),tags);
                }else {
                    Utils.showToast(AddServiceGoodActivity.this,"该会员暂无标签",2000);
                }

            }
        });
        ll_weigh_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_current_weigh.getVisibility()==View.VISIBLE) {
                    onClickWeighTitle();
                }
            }
        });
    }
    private void onClickWeighTitle() {
        setZeroDia = NoticePopuUtils.showSetZero(this, "如需置零，请移开电子秤上的物品，并点下方“置零”按钮。", new NoticePopuUtils.ThreeOnClickCallBack() {
            @Override
            public void onClickLeft() {//取消

            }

            @Override
            public void onClickCenter() {//去皮
                tare();
            }

            @Override
            public void onClickRight() {//置零
                setZero();
            }
        });
        upDateZeroDia(null);
    }
    private void upDateZeroDia(String str) {
        if(setZeroDia!=null&&setZeroDia.isShowing()) {
            if(str!=null) {
                NoticePopuUtils.upDateWeigh(str);
            }else {
                double tempWeigh=weigh_acc;
                if(TextUtils.equals("g",weigh_unit)) {
                    tempWeigh=weigh_acc/1000;
                }
                if(TextUtils.equals(weigh_zf,"+")) {
                    NoticePopuUtils.upDateWeigh(tempWeigh+"kg");
                }else {
                    NoticePopuUtils.upDateWeigh(weigh_zf+tempWeigh+"kg");
                }
            }
        }
    }
    private void tare() {
        if(TextUtils.equals("-",weigh_zf)&&TextUtils.equals("GS",weigh_mode)) {
            Utils.showToast(this,getResources().getString(R.string.tare_weigh_error),7000);
        }else if(TextUtils.isEmpty(weigh_zf)) {
        }else {
            WeighManager.getInstance().resetWeigher(WeighManager.TARE);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                cancelOrder();
                break;
            case R.id.iv_gwche://查看购物车
                if (Constant.order_goods_num <= 0) {
                    Utils.showToast(this, "请至少选择一项消费项目");
                    return;
                }
                showOrderDetailPopu(Constant.order_goods_num);
                break;
            case R.id.ll_clear:
                showClearDialog();
                break;
            case R.id.tv_close_pop:
                if (detailPup != null && detailPup.isShowing()) {
                    detailPup.dismiss();
                    if (list_header != null) {
                        list_header.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.pup_btn_commit:
            case R.id.btn_commit:
                if (ClicKUtils.isFastDoubleClick()) {
                    return;
                } else {
                    confirm_commit();
                    //测试：提交的json
                    /*LogUtils.e(TAG,"测试：提交的json...");
                    LogUtils.e(TAG,"Constant.localOrderBean.toJson:"+new Gson().toJson(Constant.localOrderBean));
                    getCommitOrderJsonForMulti();*/
                }
                break;
            case R.id.tv_back://返回
                break;
            case R.id.tv_sure://跳过，打印到后厨
                break;
            case R.id.tv_mark_reback://识别会员
                Constant.MEMBER_ID="";
                NetWorks netWorks=new NetWorks(this);
                netWorks.UpDateAuth(new NetWorks.OnGetQueryResult() {
                    @Override
                    public void stateOpen() {
                        Intent intent = new Intent(AddServiceGoodActivity.this, CaptureActivity.class);
                        intent.putExtra(Constant.FROME_WHERE, Constant.MEMBER_CUSTOMER_CONFIRM);
                        if (primaryKeyId != null) {
                            intent.putExtra(Constant.UNPAY_ORDER_ID, primaryKeyId);
                        }
                        startActivity(intent);
                    }
                    @Override
                    public void stateClose() {

                    }
                }, "4");
                break;
        }
    }

    private void cancelOrder() {
        if (!"0".equals(room_order_id)) {
            showCancelDialog();
        } else {
            clearData();
            clearLocalOrderBean();
            finish();
        }
    }

    /**
     * 点击提交订单按钮
     */
    private void confirm_commit() {
        double size = 0;
        if (Constant.localOrderBean.getGoodList() != null) {
            for(int i=0;i<Constant.localOrderBean.getGoodList().size();i++){
                size += Constant.localOrderBean.getGoodList().get(i).getGoodsNum();
            }
        }
        if (Constant.NOT_CHOOSE_ROOM) {
            if (size <= 0) {
                Utils.showToast(this, "请至少选择一项消费项目");
                return;
            }

            toNextActivity();
        } else {
            if (size > 0) {
                toNextActivity();
            } else {
                if (memberBean != null) {
                    String vipcard_id = memberBean.getMsg().getVipcardid();
                }
                Constant.FIRST_COMMIT = true;
                Intent intent = new Intent(AddServiceGoodActivity.this, OrderDetailActivity.class);
                intent.putExtra("unpay_order_id", "" + primaryKeyId);
                intent.putExtra(Constant.MEMBER_BEAN, memberBean);
                intent.putExtra("first_come_time", FirstTimeComeInMill);
                startActivity(intent);
                finish();
            }
        }
    }

    /**
     * 会员识别的回调，拿到会员号，联网请求会员信息
     */
    @Subscribe
    public void onEventMainThread(CreateOrderMemberBean createOrderMemberBean) {
        LogUtils.Log("添加商品的会员识别");
        if (createOrderMemberBean != null) {
            showMemberPrice =true;
            this.memberBean = createOrderMemberBean.getDetailMemberBean();
            updateAdapter();
            countTotal();
            showMemberInfo();
        } else {
            Toast.makeText(AddServiceGoodActivity.this, "未检索到该会员信息！", Toast.LENGTH_SHORT).show();
        }
        //FIXME：这个可以保证只调用一次。
        EventBus.getDefault().cancelEventDelivery(createOrderMemberBean);
    }

    /**
     * 识别会员后重新计算价格
     */
    private void countTotal() {
        double total=0;
        List<GoodBean> goodList = Constant.localOrderBean.getGoodList();
        LogUtils.Log("已添加的数据："+Constant.localOrderBean.toString());
        if(goodList!=null&&goodList.size()>0) {
            for (int i = 0; i < goodList.size(); i++) {
                GoodBean goodBean = goodList.get(i);
                List<GoodBean.SizeListBean> sizeLists = goodBean.getGoodsList();
                if(sizeLists !=null&& sizeLists.size()>0) {//多规格
                    for (int j = 0; j < sizeLists.size(); j++) {
                        GoodBean.SizeListBean sizeBean = sizeLists.get(j);
                        if (sizeBean.getVipPrice()<0) {//非会员价商品
                            total+= sizeBean.getSizeNum()* sizeBean.getSizePrice();
                        }else {//会员价商品
                            total += sizeBean.getSizeNum() * sizeBean.getVipPrice();
                        }
                    }
                }else {//非多规格
                    if(goodBean.getVipPrice()<0) {//非会员价商品
                        total+=goodBean.getGoodsNum()*goodBean.getGoodsPrice();
                    }else {//会员价商品
                        total+=goodBean.getGoodsNum()*goodBean.getVipPrice();
                    }
                }
            }
        }
        Constant.order_price=total;
        tv_total_money.setText("￥" + Utils.keepTwoDecimal("" + total));
        if (pup_tv_total_money != null) {
            pup_tv_total_money.setText("￥" + Utils.keepTwoDecimal(String.valueOf(total)));
        }
//        updateUI(total,Constant.order_goods_num);
    }

    /**
     * 展示会员信息
     */
    private void showMemberInfo() {
        ll_member_info.setVisibility(View.VISIBLE);
        tv_member_left.setText("￥"+memberBean.getMsg().getMoney());
        member_average_consume.setText("￥"+memberBean.getMsg().getAverage());
        LogUtils.Log("上月到店次数："+memberBean.getMsg().getMonthnum());
        tv_come_times.setText(memberBean.getMsg().getMonthnum()+"次");
        member_name.setText(memberBean.getMsg().getName());
        member_fav.setText(memberBean.getMsg().getTop5());
        if(memberBean.getMsg().isSupply()) {
            al_member_left.setVisibility(View.VISIBLE);
            tv_member_left.setText("￥"+memberBean.getMsg().getMoney());
        }else {
            al_member_left.setVisibility(View.GONE);
        }
    }

    /**
     * 显示取消订单Dialo
     */
    private void showCancelDialog() {
        new AlertDialog.Builder(this)
                .setTitle("取消订单")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelRoomOrder();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 取消房间订单
     */
    private void cancelRoomOrder() {

        LogUtils.e(TAG, "adv_id=" + Constant.ADV_ID);
        LogUtils.e(TAG, "storeid=" + Constant.STORE_ID);
        LogUtils.e(TAG, "roomid=" + room_order_id);
        LogUtils.e(TAG, "id=" + primaryKeyId);
        String currentTime = Utils.getCurrentTime();
        String token = Utils.getToken(currentTime, this);
        OkHttpUtils.post()
                .url(UrlConstance.ROOM_ORDER_CANCEL)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams(Constant.PARAMS_NEME_PASSDORD_MD5, MD5.md5(MD5.md5(Constant.PASSWORD)))
                .addParams("adv_id", Constant.ADV_ID)
                .addParams("storeid", Constant.STORE_ID)
                .addParams("roomid", room_order_id)
                .addParams("id", primaryKeyId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Utils.showToast(AddServiceGoodActivity.this, "取消已选房间失败，请到未结账订单取消房间占用状态");
                        clearData();
                        clearLocalOrderBean();
                        finish();
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e(TAG, "取消房间订单：response=" + response);
                        boolean b = Utils.checkSaveToken(AddServiceGoodActivity.this, response);
                        if (b) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int code = jsonObject.getInt("code");
                                if (code == 200) {
                                    Utils.showToast(AddServiceGoodActivity.this, "已选房间已取消");
                                    clearData();
                                    clearLocalOrderBean();
                                    finish();
                                } else {
                                    Utils.showToast(AddServiceGoodActivity.this, "取消已选房间失败，请检查网络后再试");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    public void netError() {
        iv_jiazai_filure.setVisibility(View.VISIBLE);
        iv_jiazai_filure.setImageResource(R.drawable.net_error);
        pb_loading.setVisibility(View.GONE);
        tv_loading.setText("请检查网络后，点击屏幕重新加载！");
    }

    public void noData() {
        tv_loading.setText("没有数据，点击屏幕重新加载！");
        pb_loading.setVisibility(View.GONE);
        iv_jiazai_filure.setVisibility(View.GONE);
    }

    /**
     * 清空购物车，orderbean、的内容全部置空，右侧的填充数据也要发生变化
     */
    private void showClearDialog() {
        new AlertDialog.Builder(this)
                .setTitle("确定要清空本订单吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearData();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 清除本地信息
     */
    private void clearLocalData() {
        Constant.order_goods_num = 0.00;
        Constant.localOrderBean.setGoodList(null);
    }

    /**
     * 清除adapter的数据
     */
    private void clearData() {
        clearLocalData();
        if(gotAllGoodsInfoBean!=null){
            List<AllGoodsInfoBean.MsgBean.GoodsInfoBean> realGoods = gotAllGoodsInfoBean.getMsg().getGoodsInfo();
            if (realGoods != null) {
                for (int i = 0; i < realGoods.size(); i++) {
                    for (int j = 0; j < realGoods.get(i).getGoodsList().size(); j++) {
                        if(realGoods.get(i).getGoodsList().get(j).getGoodsList()!=null&&realGoods.get(i).getGoodsList().get(j).getGoodsList().size() > 0){
                            for(int k=0;k<realGoods.get(i).getGoodsList().get(j).getGoodsList().size();k++){
                                realGoods.get(i).getGoodsList().get(j).getGoodsList().get(k).setSizeNum(0);
                            }
                        }
                        realGoods.get(i).getGoodsList().get(j).setGoodsNum(0);
                    }
                }
            }
        }
        if (detailPup != null) {
            detailPup.dismiss();
            detailPup = null;
        }
        UpdatePriceNum(0,0,2);
    }

    //更新价格和商品数量
    private void UpdatePriceNum(double num,double price,int isClearData) {
        Message mes = Message.obtain();
        mes.what = Constant.TOTAL_ACCOUNT;
        Bundle bundle = new Bundle();
        bundle.putInt("e_c",isClearData);
        bundle.putDouble("g_p",price);
        bundle.putDouble("g_n",num);
        mes.setData(bundle);
        handler.sendMessage(mes);
    }

    private AlertDialog alertDialog;
    private void showDialog() {
        alertDialog = Utils.wybDialog(this, false, R.layout.waitting_dialog_commit_goods, 0, Utils.dip2px(this, 250), Utils.dip2px(this, 180), "");
        TextView viewById = (TextView) alertDialog.findViewById(R.id.tv_voucher_search);
        viewById.setText("正在提交订单...");
        this.alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * Dialog设置消失
     */
    private void dialogMiss() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    /**
     * 展示订单详情的popuwindow
     */
    private void showOrderDetailPopu(double size) {
        LogUtils.e(TAG,"1showOrderDetailPopu:"+Utils.getCurrentTimeMill());

        if (detailPup != null && detailPup.isShowing()) {
            detailPup.dismiss();
            if (list_header != null) {
                list_header.setVisibility(View.GONE);
            }
            if (size <= 0) {
                tv_gw_num.setVisibility(View.GONE);
            }
        } else {
            orderDetailPopView = initPupView();
            orderDetailPopView.setScanListener(new ScanRelativeLayout.ScanListener() {
                @Override
                public void OnScanFinish(String barcode) {
                    findScanGood(barcode);
                    if (itemContentAdapter != null) {
                        itemContentAdapter.notifyDataSetChanged();
                    }
                }
            });
            detailPup = new PopupWindow(orderDetailPopView);
            BitmapDrawable bitmapDrawable = new BitmapDrawable();
            bitmapDrawable.setAlpha(5);
            detailPup.setBackgroundDrawable(bitmapDrawable);
            detailPup.setFocusable(true);
            detailPup.setOutsideTouchable(true);
            detailPup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setbackAlpha(0.7);
            detailPup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    setbackAlpha(1);
                }
            });
            detailPup.setHeight(550);
            //inAnimation();//进入动画
            detailPup.showAtLocation(tv_gw_num, Gravity.BOTTOM, 0, 0);
            LogUtils.e(TAG,"2showOrderDetailPopu:"+Utils.getCurrentTimeMill());
        }
    }

    private void setbackAlpha(double alpha) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = (float)alpha;
        getWindow().setAttributes(params);
    }

    /**
     * 初始化pup视图
     * @param
     * @return
     */
    @NonNull
    private ScanRelativeLayout initPupView() {
        ScanRelativeLayout popView = (ScanRelativeLayout) View.inflate(this, R.layout.gwc_pop, null);
        gwc_pop_layout = (LinearLayout) popView.findViewById(R.id.gwc_pop_layout);
        lv_order_content = (ListView) popView.findViewById(R.id.lv_order_content);
        pup_iv_gwche = (ImageView) popView.findViewById(R.id.pup_iv_gwche);
        pup_tv_total_money = (TextView) popView.findViewById(R.id.pup_tv_total_money);
        list_header = (RelativeLayout) popView.findViewById(R.id.list_header);
        ll_clear = (LinearLayout) popView.findViewById(R.id.ll_clear);
        tv_close_pop = (TextView) popView.findViewById(R.id.tv_close_pop);

        pup_tv_total_money.setText(tv_total_money.getText());
        pup_btn_commit = (Button) popView.findViewById(R.id.pup_btn_commit);
        pup_btn_commit.setOnClickListener(this);
        pup_tv_gw_num = (TextView) popView.findViewById(R.id.pup_tv_gw_num);
        pup_tv_gw_num.setVisibility(View.VISIBLE);
        if (!"99+".equals(tv_gw_num.getText().toString())) {
            pup_tv_gw_num.setText(Utils.formatPrice(Double.valueOf(tv_gw_num.getText().toString())));
        }else {
            pup_tv_gw_num.setText("99+");
        }
        pupSetListener();
        LogUtils.e(TAG,"itemContentAdapter:"+new Gson().toJson(Constant.localOrderBean));
        itemContentAdapter = new ItemContentAdapter(this,  handler);
        itemContentAdapter.setOnWeighChangeListener(onWeighGoodChangeListener);
        itemContentAdapter.setEverWeigher(weigher_connected);
        itemContentAdapter.setShowMemberPrice(showMemberPrice);
        itemContentAdapter.setData(Constant.localOrderBean);
        lv_order_content.setAdapter(itemContentAdapter);
        return popView;
    }

    /**
     * 点击购物车展示pupwindow上的监听
     */
    private void pupSetListener() {
        ll_clear.setOnClickListener(this);
        tv_close_pop.setOnClickListener(this);
        pup_iv_gwche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e(TAG,"pupSetListener:"+Utils.getCurrentTimeMill());
                detailPup.dismiss();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void ClickToReload() {
        initData();
    }

    /**
     * 左侧列表的选择监听
     */
    public class LeftOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            for (int i = 0; i < gotAllGoodsInfoBean.getMsg().getGoodsInfo().size(); i++) {
                if (i == position) {
                    gotAllGoodsInfoBean.getMsg().getGoodsInfo().get(i).setIsChecked(true);
                } else {
                    gotAllGoodsInfoBean.getMsg().getGoodsInfo().get(i).setIsChecked(false);
                }
            }
            leftAdapter.notifyDataSetChanged();
            //右边数据显示切换
            List<GoodBean> realGoodsBean = gotAllGoodsInfoBean.getMsg().getGoodsInfo().get(position).getGoodsList();
            //dataChangShow(realGoodsBean, gotAllGoodsInfoBean.getMsg().getGoodsInfo());
            if(rightAdapter!=null) {
                rightAdapter.setData(realGoodsBean);
                rightAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 跳转到下一个界面，需要根据是否是添加商品进来的跳转到本次订单明细或者总订单
     */
    private void toNextActivity() {
        showDialog();
        switch (intFromWhere) {
            case 0://一般流程的添加商品
            case Constant.ADD_FROM_NORMAl_NUM:
                commitOrder();
                break;
            case Constant.ADD_FROM_DETAILS://详情 -> 添加商品
                enterToSubOrderDetail();
                break;
        }
    }

    /**
     * 进入分订单详情
     */
    private void enterToSubOrderDetail() {
        //String commitOrderJson = getCommitOrderJson();
        String commitOrderJson = getCommitOrderJsonForMulti();
        LogUtils.e(TAG, "advid:" + Constant.ADV_ID);
        LogUtils.e(TAG, "storeid:" + Constant.STORE_ID);
        LogUtils.e(TAG, "orderid:" + primaryKeyId);

        LogUtils.e(TAG, "orderNo:" + trade_num);
        LogUtils.e(TAG,"orderId:"+primaryKeyId);
        LogUtils.e(TAG,"action:"+"1");
        LogUtils.e(TAG, "goodsNum:" + commitOrderJson);
        String vipcard_id = "";
        if (memberBean != null) {
            vipcard_id = memberBean.getMsg().getVipcardid();
        }
        LogUtils.e(TAG,"vip_cardid=="+vipcard_id);
        String currentTime = Utils.getCurrentTime();
        String token = Utils.getToken(currentTime, this);
        OkHttpUtils.post()
                .url(UrlConstance.COMMIT_ORDER)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams(Constant.PARAMS_NEME_PASSDORD_MD5, MD5.md5(MD5.md5(Constant.PASSWORD)))
                .addParams("advid", Constant.ADV_ID)
                .addParams("storeid", Constant.STORE_ID)
                .addParams("account_id", Constant.ACCOUNT_ID)
                .addParams("orderid", primaryKeyId)
                .addParams("orderNo", trade_num)
                .addParams("goodsNum", commitOrderJson)
                .addParams("vipcard_id", vipcard_id)
                .addParams("action", "1")
                .addParams("versionCode",Constant.VERSION_CODE + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        LogUtils.e(TAG, "Exception e=" + e);
                        Utils.showToast(AddServiceGoodActivity.this, "提交分订单失败，请检查网络后再试");
                        dialogMiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        dialogMiss();
                        LogUtils.e(TAG, "提交分订单onResponse():response=" + response);
                        boolean b = Utils.checkSaveToken(AddServiceGoodActivity.this, response);
                        if (b) {
                            parseCommitJson(response);
                        }
                    }
                });
    }

    /**
     * 提交订单
     */
    private void commitOrder() {

        //存储交易编号 还要记录主键id
        Constant.localOrderBean.setTradeNum(trade_num);
        Constant.localOrderBean.setPrimaryKeyId(primaryKeyId);
        //String commitOrderJson = getCommitOrderJson();
        String commitOrderJson = getCommitOrderJsonForMulti();
        LogUtils.e(TAG, "orderid:" + primaryKeyId);
        LogUtils.e(TAG, "orderNo:" + trade_num);
        LogUtils.e(TAG, "storeid:" + Constant.STORE_ID);
        LogUtils.e(TAG, "advid:" + Constant.ADV_ID);
        LogUtils.e(TAG, "提交的商品goodsNum:" + commitOrderJson);
        String vipcard_id = "";
        if (memberBean != null) {
            vipcard_id = memberBean.getMsg().getVipcardid();
        }
        String action = "1";
        if(Constant.NOT_CHOOSE_ROOM){
            action = "0";
        }
        LogUtils.e(TAG, "action:" + action);
        LogUtils.e(TAG,"vip_cardid=="+vipcard_id);
        String currentTime = Utils.getCurrentTime();
        String token = Utils.getToken(currentTime, this);
        int versionCode = Utils.getVersionCode(AddServiceGoodActivity.this);
        OkHttpUtils.post()
                .url(UrlConstance.COMMIT_ORDER)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams(Constant.PARAMS_NEME_PASSDORD_MD5, MD5.md5(MD5.md5(Constant.PASSWORD)))
                .addParams("advid", Constant.ADV_ID)
                .addParams("storeid", Constant.STORE_ID)
                .addParams("account_id", Constant.ACCOUNT_ID)
                .addParams("orderid", primaryKeyId)
                .addParams("orderNo", trade_num)
                .addParams("goodsNum", commitOrderJson)
                .addParams("vipcard_id", vipcard_id)
                .addParams("action", action)//action=0代表新建 1代表增加
                .addParams("versionCode", String.valueOf(versionCode))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        LogUtils.e(TAG, "Exception e=" + e);
                        Utils.showToast(AddServiceGoodActivity.this, "提交订单失败，请检查网络后再试");
                        dialogMiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        dialogMiss();
                        LogUtils.e(TAG, "新订单onResponse():response=" + response);
                        boolean b = Utils.checkSaveToken(AddServiceGoodActivity.this, response);
                        if (b) {
                            parseCommitJson(response);
                        }
                    }
                });
    }

    /**
     * 解析提交订单返回数据
     *
     * @param json
     */
    private void parseCommitJson(String json) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                Intent intent;
                switch (intFromWhere) {
                    case 0:
                    case Constant.ADD_FROM_NORMAl_NUM://此时本地存储的订单为分订单
                        //第一次创建订单，进入总订单
                        Constant.FIRST_COMMIT = true;
                        intent = new Intent(AddServiceGoodActivity.this, OrderDetailActivity.class);
                        if ("0".equals(primaryKeyId)) {
                            int orderId = jsonObject.getJSONObject("msg").getInt("orderId");
                            primaryKeyId = "" + orderId;
                        }
                        intent.putExtra("unpay_order_id", primaryKeyId);
                        intent.putExtra(Constant.FROME_WHERE, Constant.ADD_FROM_NORMAl_NUM);
                        intent.putExtra(Constant.MEMBER_BEAN, memberBean);
                        intent.putExtra("first_come_time", FirstTimeComeInMill);
                        startActivity(intent);
                        clearLocalOrderBean();
                        finish();
                        break;
                    case Constant.ADD_FROM_DETAILS:
                        intent = new Intent(AddServiceGoodActivity.this, AddOrderDetailActivity.class);
                        intent.putExtra("unpay_order_id", primaryKeyId);
                        intent.putExtra("query_num", query_num);
                        intent.putExtra("order_bean", order_bean);
                        intent.putExtra(Constant.MEMBER_BEAN, memberBean);
                        startActivity(intent);
                        finish();
                        break;
                }
            } else {
                Utils.showToast(AddServiceGoodActivity.this, "提交订单失败，请检查网络后再试");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到提交的json数据
     * @return
     */
    private String getCommitOrderJsonForMulti() {
        List<CommitOrderBean> commitOrderBeanList = new ArrayList<>();
        List<GoodBean> goodList = Constant.localOrderBean.getGoodList();
        if (goodList != null) {
            for (int i = 0; i < goodList.size(); i++) {
                //有多规格商品：
                if(goodList.get(i).getGoodsList()!=null&&goodList.get(i).getGoodsList().size()>0){
                    for(int j=0;j<goodList.get(i).getGoodsList().size();j++){
                        if(goodList.get(i).getGoodsList().get(j).getSizeNum()>0){
                            CommitOrderBean commitOrderBean = new CommitOrderBean();
                            //转换提交商品数据
                            commitOrderBean.setId(goodList.get(i).getGoodsId());
                            commitOrderBean.setNum(goodList.get(i).getGoodsList().get(j).getSizeNum());
                            commitOrderBean.setSize_id(goodList.get(i).getGoodsList().get(j).getSizeId());
                            commitOrderBeanList.add(commitOrderBean);
                        }
                    }
                }else{//没有多规格商品：
                    if(goodList.get(i).getGoodsNum()>0){
                        CommitOrderBean commitOrderBean = new CommitOrderBean();
                        commitOrderBean.setId(goodList.get(i).getGoodsId());
                        commitOrderBean.setNum(goodList.get(i).getGoodsNum());
                        commitOrderBeanList.add(commitOrderBean);
                    }
                }
            }
        }
        LogUtils.e(TAG,"commitOrderBeanList.toJson="+(new Gson().toJson(commitOrderBeanList)));
        return new Gson().toJson(commitOrderBeanList);
    }


    /**
     * 清除本地存储的订单
     */
    private void clearLocalOrderBean() {
        if (Constant.localOrderBean.getGoodList() != null) {
            Constant.localOrderBean.setGoodList(null);
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if(everRegist) {
            WeighManager.getInstance().unRegistReceiver(this);
            WeighManager.getInstance().unBindService(this);
        }
        if(receiver!=null) {
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }


    private void registBarScanReceiver() {
        IntentFilter mFilter=new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        mFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        mFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(receiver, mFilter);
    }
  /*int STATE_CONNECTED = 2;
    int STATE_CONNECTING = 1;
    int STATE_DISCONNECTED = 0;
    int STATE_DISCONNECTING = 3;*/

    private void connectScanner() {
        boolean enabled = bluetoothAdapter.isEnabled();
        if(!enabled) {
            bluetoothAdapter.enable();
        }
        scanGunKeyEventHelper=new ScanGunKeyEventHelper(new ScanGunKeyEventHelper.OnScanSuccessListener() {
            @Override
            public void onScanSuccess(String barcode) {
                if(barcode!=null&& !TextUtils.isEmpty(barcode)) {
                    boolean isWeighGood = findScanGood(barcode);

                    if(!isWeighGood&&Constant.localOrderBean.getGoodList()!=null&&(detailPup ==null||!detailPup.isShowing())) {
                       showOrderDetailPopu(Constant.localOrderBean.getGoodList().size());
                    }
                }
            }
        });
        HidConncetUtil hidConncetUtil=new HidConncetUtil(this);
        hidConncetUtil.connect(bluetoothAdapter.getRemoteDevice(savedDevice.getBlueToothAdd()));
        HidConncetUtil.initState(this,tv_barscanner_state,iv_scanner_state_icon,"已连接扫码枪,您可以扫描商品条码点选商品");
    }

    private boolean findScanGood(String barcode) {
        if(gotAllGoodsInfoBean!=null) {
            List<AllGoodsInfoBean.MsgBean.GoodsInfoBean> goodsInfo = gotAllGoodsInfoBean.getMsg().getGoodsInfo();
            if(goodsInfo!=null&&goodsInfo.size()>0) {
                for (int i = 0; i < goodsInfo.size(); i++) {
                    List<GoodBean> goodsList = goodsInfo.get(i).getGoodsList();
                    if(goodsList!=null&&goodsList.size()>0) {
                        for (int k = 0; k < goodsList.size(); k++) {
                            GoodBean goodTotalBean = goodsList.get(k);
                            List<GoodBean.SizeListBean> sizeList = goodTotalBean.getGoodsList();
                            if(sizeList!=null&&sizeList.size()>0) {//多规格
                                for (int a = 0; a < sizeList.size(); a++) {
                                    String sizeCode = sizeList.get(a).getSizeCode();
                                    if(barcode.equals(sizeCode)) {
                                        GoodBean.SizeListBean singleSizeBean = sizeList.get(a);
                                        if (!weigher_connected||"0".equals(goodTotalBean.getIs_weigh())) {//非称重
                                            scanAdd(singleSizeBean,goodTotalBean,1);
                                            return false;
                                        }else {//称重
                                            onWeighGoodChangeListener.weigh(goodTotalBean,a);
                                            return true;
                                        }
                                    }
                                }
                            }else if(barcode.equals(goodTotalBean.getGoodsCode())) {//非多规格
                                if(!weigher_connected||"0".equals(goodTotalBean.getIs_weigh())) {
                                    GoodBean goodBean = goodTotalBean;
                                    scanAdd(goodBean,1);
                                    return false;
                                }else {
                                    onWeighGoodChangeListener.weigh(goodTotalBean,-1);
                                    return true;
                                }
                            }

                        }
                    }
                }
            }
        }
        Utils.showToast(this,"未检索到该商品",800);
        return false;
    }

    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 获得已经搜索到的蓝牙设备
            if(action.equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)){
                int conectState = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, BluetoothAdapter.ERROR);
                LogUtils.e(TAG, "connectState==" + conectState);
                BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(conectState==0||conectState==1) {
                    tv_barscanner_state.setText("连接失败,请按码枪上的扳机,启动扫码枪");
                    iv_scanner_state_icon.setBackgroundResource(R.drawable.scanner_disconnected);
                    iv_scanner_state_icon.clearAnimation();
                    /*HidConncetUtil hidConncetUtil=new HidConncetUtil(AddServiceGoodActivity.this);
                    hidConncetUtil.connect(bluetoothAdapter.getRemoteDevice(savedDevice.getBlueToothAdd()));*/
                }/*else if(conectState==1) {
                    tv_barscanner_state.setText("正在连接蓝牙扫码枪");
                    iv_scanner_state_icon.setBackgroundResource(R.drawable.scanner_connecting);
                    Utils.pbAnimation(AddServiceGoodActivity.this, iv_scanner_state_icon);
                }*/else if(conectState==2) {
                    if(device.getAddress().equals(savedDevice.getBlueToothAdd())) {
                        LogUtils.e(TAG, "device=="+device);
                        tv_barscanner_state.setText("已连接扫码枪,您可以扫描商品条码点选商品");
                        iv_scanner_state_icon.setBackgroundResource(R.drawable.scanner_connected);
                        iv_scanner_state_icon.clearAnimation();
                    }
                }
            }else if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                LogUtils.e(TAG,"state=="+state);
                /*PrintDevices   device = (PrintDevices) getObject(AddServiceGoodActivity.this, Constant.SAVED_BLUETOOTH_SCANNER);
                if(state==BluetoothAdapter.STATE_ON&&device!=null) {
                    BluetoothService.getInstance().connect(bluetoothAdapter.getRemoteDevice(device.getBlueToothAdd()));
                }*/
            }
        }
    };
    /**
     * 添加实物或者服务
     */
    public void scanAdd(GoodBean goodBean,double weiAcc) {
        List<GoodBean> goodList = Constant.localOrderBean.getGoodList();

        if(goodList!=null) {
            if(goodList.contains(goodBean)) {
                goodBean.setGoodsNum(goodBean.getGoodsNum()+weiAcc);
            }else {
                goodBean.setGoodsNum(weiAcc);
                goodList.add(0,goodBean);
            }
        }else {
            goodBean.setGoodsNum(weiAcc);
            List<GoodBean> list=new ArrayList<>();
            list.add(0,goodBean);
            Constant.localOrderBean.setGoodList(list);
        }
        newSelectIndex = Constant.localOrderBean.getGoodList().indexOf(goodBean);
        if(showMemberPrice&&goodBean.getVipPrice()>=0) {
            UpdatePriceNum(weiAcc,goodBean.getVipPrice()*weiAcc,0);
        }else {
            UpdatePriceNum(weiAcc,goodBean.getGoodsPrice()*weiAcc,0);
        }
    }
    /**
     * 添加实物
     */
    public void scanAdd(GoodBean.SizeListBean sizeBean,GoodBean totalBean,double weiAcc) {
        List<GoodBean> goodList = Constant.localOrderBean.getGoodList();
        if(goodList!=null) {
            if(goodList.contains(totalBean)) {
                totalBean.setGoodsNum(totalBean.getGoodsNum()+weiAcc);
                List<GoodBean.SizeListBean> sizeListBeen = totalBean.getGoodsList();
                if(sizeListBeen.contains(sizeBean)) {
                    sizeBean.setSizeNum(sizeBean.getSizeNum()+weiAcc);
                    sizeListBeen.remove(sizeBean);
                    sizeListBeen.add(0,sizeBean);
                }else {
                    sizeBean.setSizeNum(weiAcc);
                    sizeListBeen.add(0,sizeBean);
                }
            }else {
                totalBean.setGoodsNum(weiAcc);
                sizeBean.setSizeNum(weiAcc);
                goodList.add(0,totalBean);
            }
        }else {
            totalBean.setGoodsNum(weiAcc);
            sizeBean.setSizeNum(weiAcc);

            List<GoodBean> list=new ArrayList<>();
            list.add(0,totalBean);
            Constant.localOrderBean.setGoodList(list);
        }
        newSelectIndex = Constant.localOrderBean.getGoodList().indexOf(totalBean);
        //handler.sendEmptyMessage(Constant.TOTAL_ACCOUNT);
        if(showMemberPrice&&sizeBean.getVipPrice()>=0) {
            UpdatePriceNum(weiAcc,sizeBean.getVipPrice()*weiAcc,0);
        }else {
            UpdatePriceNum(weiAcc,sizeBean.getSizePrice()*weiAcc,0);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if(scanGunKeyEventHelper!=null&&scanGunKeyEventHelper.isScanGunEvent(event)) {
            scanGunKeyEventHelper.analysisKeyEvent(event);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
//----------------------------------------------------------称重相关----------------------------------------------------------
    public interface OnWeighGoodChangeListener{
        void weigh(GoodBean goodBean,int position);
    }
    RelativeLayout rl_weigh_price;
    LinearLayout ll_count_result,ll_no_weigh,ll_bottom_btn;
    TextView tv_single_price,tv_weigh,tv_good_total,tv_no,tv_yes;
    DecimalFormat priceFormat = new DecimalFormat("0.00");

    public void showWeighPop(final GoodBean goodBean, String goodsName, final boolean isDgg, final int position){
        int weighState = WeighManager.getInstance().getWeighState();
        if(weighState!=2) {
            Utils.showToast(this,"电子秤未连接",1500);
            return;
        }

        //隐藏输入框
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(AddServiceGoodActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen=imm.isActive();
        LogUtils.Log("isOpen=="+isOpen);

        if(detailPup!=null&&detailPup.isShowing()) {
            detailPup.getContentView().setAlpha(0.8f);
        }
        View weighView= View.inflate(context,R.layout.weigh_popview,null);
        rl_weigh_price= (RelativeLayout) weighView.findViewById(R.id.rl_weigh_price);//单价，数量
        ll_count_result= (LinearLayout) weighView.findViewById(R.id.ll_count_result);//总计金额
        ll_no_weigh= (LinearLayout) weighView.findViewById(R.id.ll_no_weigh);//未放置
        ll_bottom_btn= (LinearLayout) weighView.findViewById(R.id.ll_bottom_btn);//底部按钮
        tv_weighgood_name = (TextView) weighView.findViewById(R.id.tv_weighgood_name);
        tv_weigh_mode = (TextView) weighView.findViewById(R.id.tv_weigh_mode);
        iv_close = (ImageView) weighView.findViewById(R.id.iv_close);

        tv_single_price =(TextView)weighView.findViewById(R.id.tv_single_price);
        tv_weigh =(TextView)weighView.findViewById(R.id.tv_weigh);
        tv_good_total =(TextView)weighView.findViewById(R.id.tv_good_total);

        tv_yes= (TextView) weighView.findViewById(R.id.tv_right);
        tv_center= (TextView) weighView.findViewById(R.id.tv_center);
        tv_no= (TextView) weighView.findViewById(R.id.tv_left);
        iv_close= (ImageView) weighView.findViewById(R.id.iv_close);

        tv_single_price.setText("￥"+simpePrice+"/"+goodBean.getGoodsUnit());
        tv_weighgood_name.setText("称重商品："+goodsName);
        if(!Constant.SAVED_WEIGH_NAME.contains(Constant.WEIGHER_NAME)) {
            tv_center.setVisibility(View.GONE);
            tv_no.setText("取消");
        }else {
            iv_close.setVisibility(View.VISIBLE);
            tv_center.setVisibility(View.VISIBLE);
            tv_no.setText("置零");
        }
        weighPop = new PopupWindow(weighView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        weighPop.setBackgroundDrawable(new BitmapDrawable());
        weighPop.setTouchable(true);
        weighPop.setOutsideTouchable(true);
        setAlpha(0.4f);
        weighPop.showAtLocation(findViewById(R.id.add_service_good), Gravity.CENTER, 0, 0);
        weighPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if(detailPup!=null&&detailPup.isShowing()) {
                    detailPup.getContentView().setAlpha(1f);
                }else {
                    setAlpha(1);
                }
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weighPop.dismiss();
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Constant.SAVED_WEIGH_NAME.contains(Constant.WEIGHER_NAME)) {
                    weighPop.dismiss();
                }else {//置零
                    NoticePopuUtils.showBindDia(AddServiceGoodActivity.this, "为保证称重准确，请保证称台上没有物品。", new NoticePopuUtils.OnClickCallBack() {
                        @Override
                        public void onClickYes() {
                            setZero();
                        }

                        @Override
                        public void onClickNo() {

                        }
                    });
                }
            }
        });
        tv_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tare();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightAdapter.startAnimation(view);
                String weigh = tv_weigh.getText().toString();
                double weiAcc= "g".equals(weighUnit) ? Double.valueOf(weigh.substring(0, weigh.indexOf("g"))) : Double.valueOf(weigh.substring(0, weigh.indexOf("kg")));
                if(isDgg) {
                    scanAdd(goodBean.getGoodsList().get(position),goodBean,weiAcc);
                }else {
                    scanAdd(goodBean,weiAcc);
                }
                weighPop.dismiss();
            }
        });
    }

    private void setZero() {
        double currentWeigh=0.000;
        if(TextUtils.equals("g",weigh_unit)) {
            currentWeigh=weigh_acc/1000;
        }else if(TextUtils.equals("kg",weigh_unit)) {
            currentWeigh=weigh_acc;
        }
        if(TextUtils.equals("NT",weigh_mode)) {
            Utils.showToast(AddServiceGoodActivity.this,getResources().getString(R.string.zero_weigh_mode_error),7000);
        }else if(currentWeigh>=0.6) {
            Utils.showToast(AddServiceGoodActivity.this,getResources().getString(R.string.zero_weigh_outof_range),7000);
        }else if(TextUtils.isEmpty(weigh_mode)) {
        }else {
            WeighManager.getInstance().resetWeigher(WeighManager.ZERO);
        }
    }

    private void updataWeighPup(String weigh) {
        //ST NT 0000.204kg
        //ST NT 00000204 g

        try{
            if(weigh_version==1) {
                if(TextUtils.equals("NT",weigh_mode)) {
                    tv_weigh_mode.setText("重量(净重)");
                }else if(TextUtils.equals("GS",weigh_mode)) {
                    tv_weigh_mode.setText("重量(毛重)");
                }else {
                    tv_weigh_mode.setText("重量");
                }
                rl_weigh_price.setVisibility(View.VISIBLE);
                ll_count_result.setVisibility(View.VISIBLE);
                ll_bottom_btn.setVisibility(View.VISIBLE);
                double tempWeigh=weigh_acc;
                if("g".equals(weighUnit)) {
                    tv_weigh.setText(weigh_zf+weigh_acc+"g");
                }else {
                    tv_weigh.setText(weigh_zf+weigh_acc/1000+"kg");
                    tempWeigh=weigh_acc/1000;
                }
                if(TextUtils.equals("+",weigh_zf)) {
                    tv_good_total.setText(priceFormat.format(tempWeigh * simpePrice));
                }else {
                    tv_good_total.setText(weigh_zf+priceFormat.format(tempWeigh * simpePrice));
                }
            }else {
                Double doubleWeigh = Double.valueOf(weigh);
                if(doubleWeigh<=0) {
                    ll_no_weigh.setVisibility(View.VISIBLE);
                    rl_weigh_price.setVisibility(View.GONE);
                    ll_count_result.setVisibility(View.GONE);
                    ll_bottom_btn.setVisibility(View.GONE);
                    iv_close.setVisibility(View.VISIBLE);
                }else {
                    ll_no_weigh.setVisibility(View.GONE);
                    rl_weigh_price.setVisibility(View.VISIBLE);
                    ll_count_result.setVisibility(View.VISIBLE);
                    ll_bottom_btn.setVisibility(View.VISIBLE);
                    iv_close.setVisibility(View.GONE);
                    if("g".equals(weighUnit)) {
                        tv_weigh.setText(doubleWeigh*1000+"g");
                        doubleWeigh=doubleWeigh*1000;
                    }else {
                        tv_weigh.setText(doubleWeigh+"kg");
                    }
                    tv_good_total.setText(priceFormat.format(doubleWeigh * simpePrice));
                }
            }
        }catch (Exception e){
            tv_weigh.setText(weigh);
            return;
        }
    }
    private void setAlpha(float alpha) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = alpha;
        getWindow().setAttributes(params);
    }
}
