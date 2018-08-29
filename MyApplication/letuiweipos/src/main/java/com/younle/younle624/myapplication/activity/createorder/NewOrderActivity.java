package com.younle.younle624.myapplication.activity.createorder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.adapter.KindsAdapter;
import com.younle.younle624.myapplication.adapter.NumericWheelAdapter;
import com.younle.younle624.myapplication.adapter.OnWheelChangedListener;
import com.younle.younle624.myapplication.adapter.RoomChooseAdapter;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.domain.RoomIsComfirmInfo;
import com.younle.younle624.myapplication.domain.RoomsInfoBean;
import com.younle.younle624.myapplication.domain.createorder.CreateOrderMemberBean;
import com.younle.younle624.myapplication.domain.orderbean.DetailMemberBean;
import com.younle.younle624.myapplication.utils.ClicKUtils;
import com.younle.younle624.myapplication.utils.GetTimeUtil;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.utils.SpUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.utils.notice.NoticePopuUtils;
import com.younle.younle624.myapplication.view.ArrayWheelAdapter;
import com.younle.younle624.myapplication.view.SelfLinearLayout;
import com.younle.younle624.myapplication.view.WheelView;
import com.younle.younle624.myapplication.view.XListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.MD5;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import zxing.activity.CaptureActivity;

/**
 * 新建订单界面
 */
public class NewOrderActivity extends Activity implements View.OnClickListener, SelfLinearLayout.ClickToReload, XListView.IXListViewListener {

    private String TAG = "NewOrderActivity";
    private ListView lv_left;
    private XListView lv_right;
    public List<RoomsInfoBean.MsgBean.RoominfoBean> kinds;
    private TextView tv_title;
    private TextView tv_cancel;
    private TextView tv_mark_reback;
    private TextView tv_choose_start_time;
    private TextView tv_choose_end_time;
    //private TextView tv_billing_method;
    private LinearLayout ll_click_choose_start_time;
    //private LinearLayout ll_click_choose_billing_method;
    private LinearLayout ll_click_choose_end_time;
    private TextView tv_no_room_data;
    private LinearLayout ll_choose_billing_method;
    private RadioGroup rgp_charge_rules;
    private RadioButton rbtn_charge_day;
    private RadioButton rbtn_charge_hour;
    private RadioButton rbtn_charge_nofee;
    private LinearLayout ll_line_one;
    private LinearLayout ll_line_two;
    private LinearLayout ll_line_three;
    private View line_which_display_one;
    private View line_which_display_two;
    private View line_which_display_three;
    private RoomsInfoBean roomsInfoBean;
    private KindsAdapter kAdapter;
    private List<RoomsInfoBean.MsgBean.RoominfoBean.RoomlistBean> rightDataChoosed;
    private RoomsInfoBean.MsgBean.RoominfoBean.RoomlistBean roomBean;
    private RoomChooseAdapter rcadapter;
    private TextView tv_choosed_room;
    private TextView tv_has_choosed_room;
    private Button btn_ok;
    private SelfLinearLayout ll_loading;
    private String choosed_room_id = "";
    private String room_id_room = "";
    private String use_end_time = "0";
    private String trade_num;
    private String start_year = "";
    private String end_year = "";
    private View time_picker_view;
    private TextView tv_time_choose_title;
    private int clickView;
    private long FirstTimeComeInMill = System.currentTimeMillis();//进入此页面时候的系统时间
    private long currentStartMill = 0;
    private long currentEndMill = -1;
    private SimpleDateFormat yyMmDdFormat;
    //private long currentTime_init;
    String[] week_str =
            {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    String[] xiaoshi_start =
            {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                    "19", "20", "21", "22", "23"};
    String[] fenzhong_start =
            {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                    "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36",
                    "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54",
                    "55", "56", "57", "58", "59"};
    String lastweek = "周一";
    private WheelView wl_ymd;
    private WheelView wl_week;
    private WheelView wl_hour;
    private WheelView wl_min;
    private String ymdData[] = new String[7200];
    private final int END_TIME = 2;
    private final int START_TIME = 1;
    private String currentHour = "00";//滑动WheelView选动的小时值
    private String currentMin = "00";//滑动WheelView选动的分钟值
    private String currrentYMD;//滑动WheelView选动的年月日值
    private String currrentYMD_fee_day;//滑动WheelView选动的年月日值
    //private String currrentYMD_NEW;
    private int charge_model = 0;
    private final int CHARGE_BY_DAY = 2;
    private final int CHARGE_BY_HOUR = 1;
    private final int CHARGE_NO_RULE = 0;
    private SimpleDateFormat praseFormate = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    private SimpleDateFormat praseFormateNoYear = new SimpleDateFormat("MM月dd日 HH:mm");
    private SimpleDateFormat praseFor = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat format_just_yer = new SimpleDateFormat("yyyy年");
    private String formedEndTime = "0";
    private String stored_room_name = "";
    private String stored_room_id = "";
    private int fromwhere;
    private String unpay_order_id="";
    private TextView tv_come_times,memmber_average_consume,tv_member_left,tv_show_membertags,member_fav;

    /**
     * 顶部会员信息信息
     * @param savedInstanceState
     */
    private LinearLayout ll_member_info, al_member_left;
    private TextView member_name;
    //会员账户余额
    private TextView member_left_account;
    //会员平均消费
    private TextView member_average_consume,member_tag;
    private PopupWindow timePickerPop;
    private String last_room_id,last_room_name,last_room_pay_rule,start_time,default_require_time;
    private String end_time = "0";
    //private int room_fee_rule = Constant.ORDER_ROOM_NO_FEE;
    public ImageView iv_jiazai_filure;
    public TextView tv_loading;
    public ProgressBar pb_loading;
    private boolean isJustRoom;
    private boolean isFirstCome = false;
    private DetailMemberBean memberBean;
    private float dy;
    private boolean showMemberPrice;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Bundle data = msg.getData();
                    String room_name = (String) data.get("room_name");
                    choosed_room_id = (String) data.get("choosed_room_id");
                    tv_choosed_room.setText(room_name);
                    tv_has_choosed_room.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    NewOrderActivity.this.choosed_room_id = "";
                    tv_choosed_room.setText("未选择房间");
                    tv_has_choosed_room.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        EventBus.getDefault().register(this);
        memberBean= (DetailMemberBean) getIntent().getSerializableExtra(Constant.MEMBER_BEAN);
        initView();
        fromWhere();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 来自新建订单还是更换房间
     */
    private void fromWhere() {
        LogUtils.Log("fromWhere()");
        Utils.dismissWaittingDialog();
        fromwhere = getIntent().getIntExtra(Constant.FROME_WHERE, -1);

        String title_name_extra = getIntent().getStringExtra("title_name");
        String title_name="选择房间";
        if(title_name_extra!=null){
            title_name = title_name_extra;
        }
        tv_title.setText(title_name);
        currentStartMill = FirstTimeComeInMill;
        //进行初始radiobutton显示
        charge_model = SpUtils.getInstance(NewOrderActivity.this).getInt("charge_model", Constant.ORDER_ROOM_NO_FEE);
        LogUtils.e(TAG,"charge_modelcharge_model="+charge_model);
        switch (charge_model){
            case CHARGE_BY_DAY:
                currentEndMill = FirstTimeComeInMill+86400000;
                default_require_time = Utils.getCurrentM(currentEndMill);
                tv_choose_start_time.setText(Utils.getCurrentMDF(currentStartMill));
                tv_choose_end_time.setText(Utils.getCurrentMDF(currentEndMill));
                start_time = Utils.getCurrentM(currentStartMill);//刚进来初始化页面的时候 加载现在可以用的房间
                end_time = praseFor.format(FirstTimeComeInMill + 86400000);
                break;
            case CHARGE_NO_RULE:
            case CHARGE_BY_HOUR:
                String currentEndHourDefult = Utils.getCurrentM(currentStartMill);//刚进来初始化页面的时候 加载现在可以用的房间
                default_require_time = currentEndHourDefult.substring(0, 10)+" 23:59";
                start_time = Utils.getCurrentM(currentStartMill);//刚进来初始化页面的时候 加载现在可以用的房间
                tv_choose_start_time.setText(Utils.getCurrentMDF(currentStartMill));
                break;
        }

        /**
         * 刚进来初始化页面的时候 加载现在可以用的房间:都请求到当日23:59分
         */
        //为了默认请求到从当前时间到今天24点能用的房间
        LogUtils.e(TAG,"=============================================");
        LogUtils.e(TAG,"currentEndMill="+currentEndMill);
        LogUtils.e(TAG,"currentStartMill="+currentStartMill);
        LogUtils.e(TAG,"currrentYMD="+currrentYMD);
        LogUtils.e(TAG,"currentHour="+currentHour);
        LogUtils.e(TAG,"currentMin="+currentMin);
        LogUtils.e(TAG,"end_year="+end_year);
        LogUtils.e(TAG,"start_year="+start_year);
        LogUtils.e(TAG, "=============================================");

        if(isFirstCome){
            Utils.showWaittingDialog(this,"正在请求房间数据...");
        }else{
            isFirstCome=true;
        }

        switch (fromwhere){
            case Constant.NEW_ORDER_TO_DETAIL:
                initData( start_time, default_require_time, "" + charge_model);//一开始进去初始显示当前时间可用的房间
                break;
            case Constant.CHANGE_ROOM_TO_DETAIL:
                unpay_order_id = getIntent().getStringExtra("unpay_order_id");
                room_id_room = getIntent().getStringExtra("room_id_room");
                last_room_id = getIntent().getStringExtra("room_id");
                last_room_name = getIntent().getStringExtra("room_name");
                last_room_pay_rule = getIntent().getStringExtra("oldcosttype");
                int room_pay_way = getIntent().getIntExtra("room_pay_way", 0);
                LogUtils.e(TAG, "unpay_order_id=" + unpay_order_id);
                LogUtils.e(TAG, "last_room_id=" + last_room_id);
                LogUtils.e(TAG, "last_room_name=" + last_room_name);
                LogUtils.e(TAG, "start_time=" + start_time);
                LogUtils.e(TAG, "room_pay_way=" + room_pay_way);

                initData( start_time, default_require_time, "" + charge_model);
                break;
            case Constant.JUST_HAS_ROOM_NO_GOODS:
                isJustRoom = true;
                initData( start_time, default_require_time, "" + charge_model);
                break;
        }
    }

    /**
     * 提交更换房间后的房间
     * @param start_time
     * @param end_time
     */
    private void commitChangedRoom(String start_time, String end_time) {

        LogUtils.e(TAG,"id="+room_id_room);
        LogUtils.e(TAG,"oldroomid="+last_room_id);
        LogUtils.e(TAG,"oldcosttype="+last_room_pay_rule);
        LogUtils.e(TAG, "starttime=" + start_time);
        LogUtils.e(TAG,"endtime="+end_time);
        LogUtils.e(TAG,"roomid="+choosed_room_id);
        LogUtils.e(TAG, "costtype=" + charge_model);
        LogUtils.e(TAG,"roomname="+tv_choosed_room.getText().toString());
        String vipcreate_id=memberBean==null?"0":memberBean.getMsg().getVipcreate_id();
        String currentTime_change = Utils.getCurrentTime();
        String token = Utils.getToken(currentTime_change,this);
        long oldendtime = getIntent().getLongExtra("oldendtime", 0);
        OkHttpUtils.post()//提交重新选择的房间
                .url(UrlConstance.COMMIT_CHANGE_ROOM)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime_change)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams(Constant.PARAMS_NEME_PASSDORD_MD5, MD5.md5(MD5.md5(Constant.PASSWORD)))
                .addParams("id", room_id_room)
                .addParams("oldroomid", last_room_id)
                .addParams("oldcosttype", last_room_pay_rule)
                .addParams("oldendtime", Utils.getCurrentM(oldendtime))
                .addParams("starttime", start_time)//2016-10-28 15:00形式
                .addParams("endtime", end_time)
                .addParams("roomid", choosed_room_id)
                .addParams("costtype", "" + charge_model)
                .addParams("roomname", "" + tv_choosed_room.getText().toString())
                .addParams("vipcreate_id",vipcreate_id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        LogUtils.e(TAG, "Exception e=" + e);
                        Utils.showToast(NewOrderActivity.this, "更换房间失败请重新再试！");
                        Utils.dismissWaittingDialog();
                    }

                    @Override
                    public void onResponse(String response) {
                        Utils.dismissWaittingDialog();
                        LogUtils.e(TAG, "commitChangedRoom=response:" + response);
                        boolean b = Utils.checkSaveToken(NewOrderActivity.this, response);
                        if (b) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int code = jsonObject.getInt("code");
                                if (code == 200) {
                                    Intent intent = new Intent(NewOrderActivity.this, OrderDetailActivity.class);
                                    intent.putExtra(Constant.MEMBER_BEAN, memberBean);
                                    intent.putExtra("unpay_order_id", unpay_order_id);
                                    startActivity(intent);
                                    finish();
                                } else {//创建订单失败：弹出提示 和庞一鹤确认
                                    Utils.showToast(NewOrderActivity.this, "更换房间失败请重新再试！");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    /**
     * 初始化视图
     */
    private void initView() {
        iv_jiazai_filure = (ImageView) findViewById(R.id.iv_jiazai_filure);
        tv_loading = (TextView) findViewById(R.id.tv_loading);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
        ll_loading = (SelfLinearLayout) findViewById(R.id.ll_loading);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_mark_reback = (TextView) findViewById(R.id.tv_mark_reback);

        //1.5新加
        tv_choose_start_time = (TextView) findViewById(R.id.tv_choose_start_time);
        //tv_billing_method = (TextView) findViewById(R.id.tv_billing_method);
        tv_choose_end_time = (TextView) findViewById(R.id.tv_choose_end_time);
        ll_click_choose_start_time = (LinearLayout) findViewById(R.id.ll_click_choose_start_time);
        //ll_click_choose_billing_method = (LinearLayout) findViewById(R.id.ll_click_choose_billing_method);
        ll_click_choose_end_time = (LinearLayout) findViewById(R.id.ll_click_choose_end_time);
        tv_no_room_data = (TextView) findViewById(R.id.tv_no_room_data);

        ll_choose_billing_method = (LinearLayout) findViewById(R.id.ll_choose_billing_method);
        rgp_charge_rules = (RadioGroup) findViewById(R.id.rgp_charge_rules);
        ll_line_one = (LinearLayout) findViewById(R.id.ll_line_one);
        ll_line_two = (LinearLayout) findViewById(R.id.ll_line_two);
        ll_line_three = (LinearLayout) findViewById(R.id.ll_line_three);
        line_which_display_one = findViewById(R.id.line_which_display_one);
        line_which_display_two = findViewById(R.id.line_which_display_two);
        line_which_display_three = findViewById(R.id.line_which_display_three);
        rbtn_charge_day = (RadioButton) findViewById(R.id.rbtn_charge_day);
        rbtn_charge_hour = (RadioButton) findViewById(R.id.rbtn_charge_hour);
        rbtn_charge_nofee = (RadioButton) findViewById(R.id.rbtn_charge_nofee);

        tv_mark_reback.setText("识别会员");
        tv_mark_reback.setVisibility(View.VISIBLE);
        tv_cancel.setVisibility(View.VISIBLE);
        lv_left = (ListView) findViewById(R.id.lv_setting_kinds);
        lv_right = (XListView) findViewById(R.id.lv_setting_detail);
        tv_choosed_room = (TextView) findViewById(R.id.tv_choosed_room);
        tv_has_choosed_room = (TextView) findViewById(R.id.tv_has_choosed_room);
        tv_choosed_room.setText("未选择房间");
        tv_has_choosed_room.setVisibility(View.GONE);
        btn_ok = (Button) findViewById(R.id.btn_ok);

        //会员信息控件
        ll_member_info = (LinearLayout) findViewById(R.id.ll_member_info);
        al_member_left = (LinearLayout)findViewById(R.id.al_member_left);

        member_name = (TextView) findViewById(R.id.member_name);
        tv_show_membertags = (TextView)findViewById(R.id.tv_show_membertags);
        tv_come_times = (TextView)findViewById(R.id.tv_come_times);

        tv_member_left = (TextView)findViewById(R.id.tv_member_left);
        memmber_average_consume = (TextView) findViewById(R.id.member_average_consume);
        member_fav = (TextView) findViewById(R.id.member_fav);
        if(memberBean!=null) {
            showMemberPrice =true;
            showMemberInfo();
        }
    }

    private String typeId="-1";
    private int roomPager=1;
    /**
     * 初始化房间数据
     * @param start_time
     */
    private void initData( String start_time, String end_time, String is_billing) {
        String currentTime = Utils.getCurrentTime();
        String token = Utils.getToken(currentTime, this);
        LogUtils.Log("请求房间");
        LogUtils.Log("storeid:" + Constant.STORE_ID);
        LogUtils.Log( "adv_id:" + Constant.ADV_ID);
        LogUtils.Log( "starttime:" + start_time);
        LogUtils.Log( "endtime:" + end_time);
        LogUtils.Log( "is_billing:" + is_billing);
        String vip_createid=memberBean==null?"":memberBean.getMsg().getVipcreate_id();
        LogUtils.Log("vip_createid:"+vip_createid);
        if(end_time!=null&&end_time.contains("1970")){
            end_time="0";
        }
        OkHttpUtils.post()//请求数据返回房间信息填充左右两侧界面
                .url(UrlConstance.ROOMS_INFO)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams(Constant.PARAMS_NEME_PASSDORD_MD5, MD5.md5(MD5.md5(Constant.PASSWORD)))
                .addParams("storeid", Constant.STORE_ID)//只有56下有房间数据
                .addParams("adv_id", Constant.ADV_ID)
                .addParams("starttime", start_time)//2016-10-28 15:00形式
                .addParams("endtime", end_time)
                .addParams("is_billing", is_billing)//默认无需计费
                .addParams("vipcreate_id",vip_createid)
                .addParams(Constant.PARAMS_NAME_VERSIONCODE,Constant.VERSION_CODE+"")
                .addParams("type_id",typeId)
                .addParams("room_page",roomPager+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Utils.dismissWaittingDialog();
                        netError();
                    }

                    @Override
                    public void onResponse(String response) {
                        Utils.dismissWaittingDialog();
                        LogUtils.Log("下单返回房间数据：response=" + response);
                        boolean b = Utils.checkSaveToken(NewOrderActivity.this, response);
                        if (b) {
                            parseJson(response);
                        }
                    }
                });
    }
    /**
     * 初始化房间数据
     * @param start_time
     */
    private void changeKind( String start_time, String end_time, String is_billing) {
        Utils.showWaittingDialog(this,"正在获取数据...");
        String currentTime = Utils.getCurrentTime();
        String token = Utils.getToken(currentTime, this);

        LogUtils.e(TAG, "storeid:" + Constant.STORE_ID);
        LogUtils.e(TAG, "adv_id:" + Constant.ADV_ID);
        LogUtils.e(TAG, "starttime:" + start_time);
        LogUtils.e(TAG, "endtime:" + end_time);
        LogUtils.e(TAG, "is_billing:" + is_billing);
        String vip_createid=memberBean==null?"":memberBean.getMsg().getVipcreate_id();
        LogUtils.Log("vip_createid:"+vip_createid);
        if(end_time!=null&&end_time.contains("1970")){
            end_time="0";
        }
        OkHttpUtils.post()//请求数据返回房间信息填充左右两侧界面
                .url(UrlConstance.ROOMS_INFO)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams(Constant.PARAMS_NEME_PASSDORD_MD5, MD5.md5(MD5.md5(Constant.PASSWORD)))
                .addParams("storeid", Constant.STORE_ID)//只有56下有房间数据
                .addParams("adv_id", Constant.ADV_ID)
                .addParams("starttime", start_time)//2016-10-28 15:00形式
                .addParams("endtime", end_time)
                .addParams("is_billing", is_billing)//默认无需计费
                .addParams("vipcreate_id",vip_createid)
                .addParams(Constant.PARAMS_NAME_VERSIONCODE,Constant.VERSION_CODE+"")
                .addParams("type_id",typeId)
                .addParams("room_page",roomPager+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Utils.dismissWaittingDialog();
                        netError();
                    }

                    @Override
                    public void onResponse(String response) {
                        Utils.dismissWaittingDialog();
                        LogUtils.e(TAG,"更换房间类目数据返回：response=" + response);
                        boolean b = Utils.checkSaveToken(NewOrderActivity.this, response);
                        if (b) {
                            parseKindChange(response);
                        }
                    }
                });
    }

    /**
     * 初始化房间数据
     * @param start_time
     */
    private void getMoreData( String start_time, String end_time, String is_billing) {
        Utils.showWaittingDialog(this,"正在加载更多数据...");
        String currentTime = Utils.getCurrentTime();
        String token = Utils.getToken(currentTime, this);

        LogUtils.e(TAG, "storeid:" + Constant.STORE_ID);
        LogUtils.e(TAG, "adv_id:" + Constant.ADV_ID);
        LogUtils.e(TAG, "starttime:" + start_time);
        LogUtils.e(TAG, "endtime:" + end_time);
        LogUtils.e(TAG, "is_billing:" + is_billing);
        LogUtils.e(TAG,"room_page："+roomPager);
        LogUtils.e(TAG,"type_id："+typeId);
        String vip_createid=memberBean==null?"":memberBean.getMsg().getVipcreate_id();
        LogUtils.Log("vip_createid:"+vip_createid);
        if(end_time!=null&&end_time.contains("1970")){
            end_time="0";
        }
        OkHttpUtils.post()//请求数据返回房间信息填充左右两侧界面
                .url(UrlConstance.ROOMS_INFO)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams(Constant.PARAMS_NEME_PASSDORD_MD5, MD5.md5(MD5.md5(Constant.PASSWORD)))
                .addParams("storeid", Constant.STORE_ID)//只有56下有房间数据
                .addParams("adv_id", Constant.ADV_ID)
                .addParams("starttime", start_time)//2016-10-28 15:00形式
                .addParams("endtime", end_time)
                .addParams("is_billing", is_billing)//默认无需计费
                .addParams("vipcreate_id",vip_createid)
                .addParams(Constant.PARAMS_NAME_VERSIONCODE,Constant.VERSION_CODE+"")
                .addParams("type_id",typeId)
                .addParams("room_page",roomPager+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        lv_right.stopLoadMore();
                        Utils.dismissWaittingDialog();
                        netError();
                    }

                    @Override
                    public void onResponse(String response) {
                        lv_right.stopLoadMore();
                        Utils.dismissWaittingDialog();
                        LogUtils.e(TAG,"加载更多房间数据：response=" + response);
                        boolean b = Utils.checkSaveToken(NewOrderActivity.this, response);
                        if (b) {
                            parseMoreJson(response);
                        }
                    }
                });
    }
    /**
     * 解析数据
     * @param response
     */
    private void parseJson(String response) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if(code == 200){
                roomPager++;
                Gson gson = new Gson();
                roomsInfoBean = gson.fromJson(response, RoomsInfoBean.class);
                kinds = roomsInfoBean.getMsg().getRoominfo();//右侧种类数据（利用数据第一层）
                rightDataChoosed = new ArrayList<>();//初始显示数据
                List<String> costtype = roomsInfoBean.getMsg().getCosttype();
                if(costtype!=null){
                    setFeeRule(costtype);
                }
                if(kinds!=null){
                    //比对typeid,定位类目位置
                    typeId=kinds.get(0).getProduct_typeid();
                    if(kinds.get(0).getRoomlist()!=null){
                        tv_no_room_data.setVisibility(View.GONE);
                        rightDataChoosed.addAll(kinds.get(0).getRoomlist());
                    }else{//获取roomlist为空
                        tv_no_room_data.setVisibility(View.VISIBLE);
                    }
                    kinds.get(0).setIsChoosed(true);
                    setAdapter();
                }else{
                    noData();
                }
            }else if(code==30004) {
                lv_right.stopLoadMore();
                Utils.showToast(this,"没有更多数据了！");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析数据
     * @param response
     */
    private void parseKindChange(String response) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if(code == 200){
                roomPager++;
                Gson gson = new Gson();
                roomsInfoBean = gson.fromJson(response, RoomsInfoBean.class);
                kinds = roomsInfoBean.getMsg().getRoominfo();//右侧种类数据（利用数据第一层）
                rightDataChoosed = new ArrayList<>();//初始显示数据
                List<String> costtype = roomsInfoBean.getMsg().getCosttype();
                if(costtype!=null){
                    setFeeRule(costtype);
                }
                if(kinds!=null){
                    //比对typeid,定位类目位置
                    for (int i = 0; i < kinds.size(); i++) {
                        RoomsInfoBean.MsgBean.RoominfoBean roominfoBean = kinds.get(i);
                        if(roominfoBean!=null) {
                            if(TextUtils.equals(roominfoBean.getProduct_typeid(),typeId)) {
                                kinds.get(i).setIsChoosed(true);
                                if(roominfoBean.getRoomlist()!=null&&roominfoBean.getRoomlist().size()>0) {
                                    rightDataChoosed.addAll(roominfoBean.getRoomlist());
                                }else {
                                    tv_no_room_data.setVisibility(View.VISIBLE);
                                }
                            }else {
                                kinds.get(i).setIsChoosed(false);
                            }
                        }
                    }
                    kAdapter.setData(kinds);
                    kAdapter.notifyDataSetChanged();
                    rcadapter.setData(rightDataChoosed);
                    rcadapter.notifyDataSetChanged();
                    lv_right.setSelection(0);
                }else{
                    noData();
                }
            }else if(code==30004) {
                lv_right.stopLoadMore();
                Utils.showToast(this,"没有更多数据了！");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setFeeRule(List<String> costtype) {
        boolean isShow = false;
        int fee_count = 0;
        if (costtype.contains("0")) {//无需收费
            //charge_model = 0;
            rbtn_charge_nofee.setVisibility(View.VISIBLE);
            ll_line_three.setVisibility(View.VISIBLE);
            fee_count+=1;
            isShow = true;
        }else{
            if(costtype.contains("1")){//按时收费
                //charge_model = 1;
                displayLines(1);
            }else if(costtype.contains("2")){//按天收费
                //charge_model = 2;
                displayLines(0);
            }
        }

        if (costtype.contains("1")) {
            rbtn_charge_hour.setVisibility(View.VISIBLE);
            ll_line_two.setVisibility(View.VISIBLE);
            fee_count+=1;
            isShow = true;
        }
        if (costtype.contains("2")) {
            rbtn_charge_day.setVisibility(View.VISIBLE);
            ll_line_one.setVisibility(View.VISIBLE);
            fee_count+=1;
            isShow = true;
        }
        if (isShow&&fee_count>1) {
            ll_choose_billing_method.setVisibility(View.VISIBLE);
            rgp_charge_rules.setVisibility(View.VISIBLE);
        } else {
            ll_choose_billing_method.setVisibility(View.GONE);
            rgp_charge_rules.setVisibility(View.GONE);
        }

        charge_model = SpUtils.getInstance(NewOrderActivity.this).getInt("charge_model", Constant.ORDER_ROOM_NO_FEE);
        switch (charge_model){
            case Constant.ORDER_ROOM_FEE_DAY:
                displayLines(0);
                break;
            case Constant.ORDER_ROOM_FEE_HOUR:
                displayLines(1);
                break;
            case Constant.ORDER_ROOM_NO_FEE:
                displayLines(2);
                break;
        }
    }
    /**
     * 解析数据
     * @param response
     */
    private void parseMoreJson(String response) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if(code == 200){
                roomPager++;
                Gson gson = new Gson();
                RoomsInfoBean moreInfoBean = gson.fromJson(response, RoomsInfoBean.class);
                List<RoomsInfoBean.MsgBean.RoominfoBean> moreKinds = moreInfoBean.getMsg().getRoominfo();//右侧种类数据（利用数据第一层）

                if(moreKinds!=null){
                    for (int i = 0; i < moreKinds.size(); i++) {
                        if(TextUtils.equals(moreKinds.get(i).getProduct_typeid(),typeId)) {
                            rightDataChoosed.addAll(moreKinds.get(i).getRoomlist());
                            LogUtils.e(TAG,"rightData.size()=="+rightDataChoosed.size());
                            rcadapter.notifyDataSetChanged();
                            return;
                        }
                    }
                }else{
                    noData();
                }
            }else if(code==30004) {
                lv_right.stopLoadMore();
                Utils.showToast(this,"没有更多数据了！");
            }else {
                String msg= (String) jsonObject.get("msg");
                Utils.showToast(this,msg);
                noData();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setAdapter() {
        kAdapter = new KindsAdapter(this);
        kAdapter.setData(kinds);
        lv_left.setAdapter(kAdapter);

        rcadapter = new RoomChooseAdapter(this,handler);
        rcadapter.setRoomFeeRule(charge_model);
        rcadapter.setData(rightDataChoosed);
        lv_right.setAdapter(rcadapter);
        ll_loading.setVisibility(View.GONE);
        initChooseTime();
    }

    private void setListener() {
        btn_ok.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        tv_mark_reback.setOnClickListener(this);
        ll_click_choose_start_time.setOnClickListener(this);
        ll_click_choose_end_time.setOnClickListener(this);
        lv_left.setOnItemClickListener(new LeftOnItemClickListener());
        ll_loading.setClickToReload(this);
        rgp_charge_rules.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        tv_show_membertags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> tags = memberBean.getMsg().getTags();
                if(tags!=null&&tags.size()>0) {
                    NoticePopuUtils.showMemberTagsPop(NewOrderActivity.this,findViewById(R.id.rl_new_order),tags);
                }else {
                    Utils.showToast(NewOrderActivity.this,"该会员暂无标签",2000);
                }
            }
        });
        lv_right.setPullLoadEnable(true);
        lv_right.setXListViewListener(this);
    }

    @Override
    public void ClickToReload() {
        fromWhere();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        getMoreData( start_time, default_require_time, "" + charge_model);
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            roomPager=1;
            typeId="-1";
            switch (checkedId){
                case R.id.rbtn_charge_day:
                    resetTimeData(CHARGE_BY_DAY);
                    charge_model = CHARGE_BY_DAY;
                    SpUtils.getInstance(NewOrderActivity.this).save("charge_model", CHARGE_BY_DAY);
                    fromWhere();
                    break;
                case R.id.rbtn_charge_hour:
                    resetTimeData(CHARGE_BY_HOUR);
                    charge_model = CHARGE_BY_HOUR;
                    SpUtils.getInstance(NewOrderActivity.this).save("charge_model",CHARGE_BY_HOUR);
                    fromWhere();
                    break;
                case R.id.rbtn_charge_nofee:
                    resetTimeData(CHARGE_NO_RULE);
                    charge_model = CHARGE_NO_RULE;
                    SpUtils.getInstance(NewOrderActivity.this).save("charge_model", CHARGE_NO_RULE);
                    fromWhere();
                    break;
            }
        }
    }

    private void resetTimeData(int flag) {
        switch (flag){
            case CHARGE_NO_RULE:
                displayLines(2);
                currentHour = Utils.getCurrentM(FirstTimeComeInMill).substring(11,13);
                currentMin = Utils.getCurrentM(FirstTimeComeInMill).substring(14,16);
                currentEndMill = -1;
                end_time = "0";//yyyy-MM-dd HH:mm
                tv_choose_end_time.setText("未选择");
                break;
            case CHARGE_BY_HOUR:
                displayLines(1);
                currentHour = Utils.getCurrentM(FirstTimeComeInMill).substring(11,13);
                currentMin = Utils.getCurrentM(FirstTimeComeInMill).substring(14,16);
                currentEndMill = -1;
                end_time = "0";//yyyy-MM-dd HH:mm
                tv_choose_end_time.setText("未选择");
                break;
            case CHARGE_BY_DAY:
                displayLines(0);    //"yyyy-MM-dd HH:mm"
                currentHour = Utils.getCurrentM(FirstTimeComeInMill).substring(11,13);
                currentMin = Utils.getCurrentM(FirstTimeComeInMill).substring(14, 16);
                currrentYMD_fee_day = yyMmDdFormat.format(FirstTimeComeInMill);
                currentEndMill = FirstTimeComeInMill+86400000;
                end_time = praseFor.format(currentEndMill);
                tv_choose_end_time.setText(Utils.getCurrentMDF(currentEndMill));
                break;
        }
    }

    private void resetTimeData() {

        switch (charge_model){
            case CHARGE_NO_RULE:
                displayLines(2);
                switch (clickView){
                    case START_TIME:
                        currentHour = Utils.getCurrentM(FirstTimeComeInMill).substring(11,13);
                        currentMin = Utils.getCurrentM(FirstTimeComeInMill).substring(14,16);
                        currentStartMill = FirstTimeComeInMill;
                        start_time = praseFor.format(FirstTimeComeInMill);
                        break;
                    case END_TIME:
                        currentHour = Utils.getCurrentM(FirstTimeComeInMill).substring(11,13);
                        currentMin = Utils.getCurrentM(FirstTimeComeInMill).substring(14,16);
                        currentEndMill = -1;
                        //end_time = praseFor.format(FirstTimeComeInMill).substring(0,10)+" 23:59";//yyyy-MM-dd HH:mm
                        end_time = "0";//yyyy-MM-dd HH:mm
                        break;
                }
                break;
            case CHARGE_BY_HOUR:
                switch (clickView){
                    case START_TIME:
                        currentHour = Utils.getCurrentM(FirstTimeComeInMill).substring(11,13);
                        currentMin = Utils.getCurrentM(FirstTimeComeInMill).substring(14,16);
                        currentStartMill = FirstTimeComeInMill;
                        start_time = praseFor.format(FirstTimeComeInMill);
                        break;
                    case END_TIME:
                        currentHour = Utils.getCurrentM(FirstTimeComeInMill).substring(11,13);
                        currentMin = Utils.getCurrentM(FirstTimeComeInMill).substring(14,16);
                        currentEndMill = -1;
                        //end_time = praseFor.format(FirstTimeComeInMill).substring(0,10)+" 23:59";//yyyy-MM-dd HH:mm
                        end_time = "0";//yyyy-MM-dd HH:mm
                        break;
                }
                break;
            case CHARGE_BY_DAY:
                switch (clickView){
                    case START_TIME:
                        currentHour = Utils.getCurrentM(FirstTimeComeInMill).substring(11,13);
                        currentMin = Utils.getCurrentM(FirstTimeComeInMill).substring(14,16);
                        currentStartMill = FirstTimeComeInMill;
                        start_time = praseFor.format(FirstTimeComeInMill);
                        break;
                    case END_TIME:
                        //"yyyy-MM-dd HH:mm"
                        currentHour = Utils.getCurrentM(FirstTimeComeInMill).substring(11,13);
                        currentMin = Utils.getCurrentM(FirstTimeComeInMill).substring(14, 16);
                        currrentYMD_fee_day = yyMmDdFormat.format(FirstTimeComeInMill);
                        //currentEndMill = FirstTimeComeInMill+86400000;
                        //end_time = praseFor.format(currentEndMill);
                        end_time = "0";
                        break;
                }
                break;
        }
    }

    /**
     * 右侧listview被点击的监听
     */
    class RightOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            LogUtils.e(TAG,"RightOnItemClickListener position======="+position);
            roomBean = rightDataChoosed.get(position);
            if ("1".equals(roomBean.getUsed())) {//占用
                Utils.showToast(NewOrderActivity.this, "该房间使用中，请选择其他房间！");
                return;
            }
            for(int i = 0; i < rightDataChoosed.size(); i++){
                if (position == i) {
                    if(rightDataChoosed.get(i).isChecked()) {
                        rightDataChoosed.get(i).setChecked(false);
                        tv_choosed_room.setText("未选择房间");
                        tv_has_choosed_room.setVisibility(View.GONE);
                    }else {
                        rightDataChoosed.get(i).setChecked(true);
                        tv_has_choosed_room.setVisibility(View.VISIBLE);
                        tv_choosed_room.setText("" + rightDataChoosed.get(i).getRoomname());
                        choosed_room_id = rightDataChoosed.get(i).getId();
                    }
                } else {
                    rightDataChoosed.get(i).setChecked(false);
                }
            }
            rcadapter.notifyDataSetChanged();
        }
    }

    /**
     * 左侧listview被点击的监听
     */
    public class LeftOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            tv_no_room_data.setVisibility(View.GONE);
            for (int i = 0; i < kinds.size(); i++) {
                if (i == position) {
                    typeId=kinds.get(i).getProduct_typeid();
                }
            }
            roomPager=1;
            changeKind(start_time, default_require_time, "" + charge_model);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                LogUtils.e(TAG,"取消创建订单...");
                finish();
                break;
            case R.id.btn_ok:
                finishChooseRoom();
                break;
            case R.id.tv_mark_reback:
                Constant.MEMBER_ID="";
                NetWorks netWorks=new NetWorks(this);
                netWorks.UpDateAuth(new NetWorks.OnGetQueryResult() {
                    @Override
                    public void stateOpen() {
                        toCaptureActivity();
                    }

                    @Override
                    public void stateClose() {

                    }
                },"4");
                break;
            case R.id.ll_click_choose_start_time:
                if(ClicKUtils.isFastDoubleClick()){
                    return;
                }else{
                    clickView=START_TIME;
                    showTimePickerPup();
                }
                break;
            case R.id.ll_click_choose_end_time:
                if(ClicKUtils.isFastDoubleClick()){
                    return;
                }else{
                    clickView = END_TIME;
                    showTimePickerPup();
                }
                break;
            case R.id.tv_finish_choose:
                //finishChooseTime();
                finishChooseTime_copy();
                break;
            case R.id.tv_cancel_choose:
                //清空结束时间
                end_time = "0";
                currentEndMill=-1;
                tv_choose_end_time.setText("未选择");
                if(timePickerPop!=null&&timePickerPop.isShowing()){
                    timePickerPop.dismiss();
                }
                break;
        }
    }
    private void toCaptureActivity() {
        LogUtils.e(TAG, "调出扫码页面进行会员识别...");
        Intent intent=new Intent(NewOrderActivity.this,CaptureActivity.class);
        intent.putExtra(Constant.FROME_WHERE,Constant.MEMBER_CUSTOMER_CONFIRM);
        if(unpay_order_id!=null) {
            intent.putExtra(Constant.UNPAY_ORDER_ID,unpay_order_id);
        }
        startActivity(intent);
    }
    /**
     * 选择完房间 点击确定
     */
    private void finishChooseTime_copy() {
        String start_time_choose="";
        String end_time_choose="";
        switch (clickView) {
            case START_TIME :
                end_time_choose= end_year + tv_choose_end_time.getText().toString();
                start_time_choose=currrentYMD+" "+currentHour+":"+currentMin;
                LogUtils.e(TAG,"START_TIME===start_time_choose=="+start_time_choose);
                LogUtils.e(TAG,"START_TIME===end_time_choose=="+end_time_choose);
                break;
            case END_TIME:
                start_time_choose=start_year + tv_choose_start_time.getText().toString();
                end_time_choose=currrentYMD + " " + currentHour + ":" + currentMin;
                LogUtils.e(TAG,"END_TIME===start_time_choose=="+start_time_choose);
                LogUtils.e(TAG,"END_TIME===end_time_choose=="+end_time_choose);
                break;
        }

        //此时不对比开始和结束时间
        if(end_time_choose.contains("未选择")){
            String startStr = start_time_choose;
            try {
                Date startDate = praseFormate.parse(startStr);
                currentStartMill = startDate.getTime();
                start_time = praseFor.format(currentStartMill);
                LogUtils.e(TAG,"end_time_choose.contains(\"未选择\") start_time="+start_time);
                LogUtils.e(TAG,"end_time_choose.contains(\"未选择\") Utils.getTimeNoSec(FirstTimeComeInMill)="+Utils.getTimeNoSec(FirstTimeComeInMill));
                if(Utils.getTimeNoSec(FirstTimeComeInMill)>currentStartMill){
                    Utils.showToast(this,"开始时间不可早于当前时间！");
                    resetTimeData();
                    return;
                }
                LogUtils.e(TAG,"ttt 0");
                initDxTime();
                LogUtils.e(TAG, "ttt 1");
            } catch (ParseException e) {
                LogUtils.e(TAG,"ParseException 1e=="+e);
                e.printStackTrace();
            }

            //此时需要对比开始时间和结束时间：不能开始时间大于结束时间
        }else {
            String startStr = start_time_choose;
            String endStr = end_time_choose;
            try {
                Date startDate = praseFormate.parse(startStr);
                Date endDate = praseFormate.parse(endStr);
                currentStartMill=startDate.getTime();
                currentEndMill=endDate.getTime();
                start_time = praseFor.format(currentStartMill);
                end_time = praseFor.format(currentEndMill);
                if(Utils.getTimeNoSec(FirstTimeComeInMill)>currentStartMill){
                    Utils.showToast(this,"开始时间不可早于当前时间！");
                    resetTimeData();
                    return;
                }
                if(Utils.getTimeNoSec(FirstTimeComeInMill)>currentEndMill){
                    Utils.showToast(this,"结束时间不可早于当前时间！");
                    resetTimeData();
                    return;
                }
                initDxTime();
            } catch (ParseException e) {
                LogUtils.e(TAG,"ParseException 2e=="+e);
                e.printStackTrace();
            }
        }
    }

    /**
     * 会员识别的回调，拿到会员号，联网请求会员信息
     */
    @Subscribe
    public void onEventMainThread(CreateOrderMemberBean createOrderMemberBean) {
        LogUtils.Log("选房桌会员识别回调");
        if(createOrderMemberBean!=null) {
            showMemberPrice=true;
            this.memberBean=createOrderMemberBean.getDetailMemberBean();
//            upDateAdapter();
            showMemberInfo();
            fromWhere();
        }else {
            Toast.makeText(NewOrderActivity.this, "未检索到该会员信息！", Toast.LENGTH_SHORT).show();
        }
        //FIXME：这个可以保证只调用一次。
        EventBus.getDefault().cancelEventDelivery(createOrderMemberBean);
    }

    private void upDateAdapter() {
        if(rcadapter!=null) {
            rcadapter.notifyDataSetChanged();
        }
    }

    private void showMemberInfo() {
        LogUtils.Log("showMemberInfo()");
        ll_member_info.setVisibility(View.VISIBLE);
        memmber_average_consume.setText("￥"+memberBean.getMsg().getAverage());
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
     * 完成房间的选择 点击确定
     */
    private void finishChooseRoom() {
        LogUtils.e(TAG, "choosed_room_id=" + choosed_room_id);
        LogUtils.e(TAG, "tv_choosed_room.getText().toString()==" + tv_choosed_room.getText().toString());
        switch (fromwhere) {
            case Constant.NEW_ORDER_TO_DETAIL:
                if ("未选择房间".equals(tv_choosed_room.getText().toString())) {//跳过房间直接进入选择商品页面
                    Intent intent = new Intent(this, AddServiceGoodActivity.class);
                    String trade_num = Utils.getTradeNum();
                    intent.putExtra("trade_num", trade_num);
                    intent.putExtra("from_where", Constant.ADD_FROM_NORMAl_NUM);
                    intent.putExtra("first_come_time", FirstTimeComeInMill);
                    intent.putExtra(Constant.MEMBER_BEAN,memberBean);
                    startActivity(intent);
                    Constant.NOT_CHOOSE_ROOM = true;
                    finish();
                } else {
                    Constant.NOT_CHOOSE_ROOM = false;
                    Utils.showWaittingDialog(this,"正在查询房间...");
                    checkRoomAvailable();
                }
                break;
            case Constant.JUST_HAS_ROOM_NO_GOODS:
                /*Intent intent = new Intent(NewOrderActivity.this, OrderDetailActivity.class);
                intent.putExtra("unpay_order_id", ""+unpay_order_id);
                intent.putExtra(Constant.MEMBER_BEAN,memberBean);
                startActivity(intent);
                finish();*/
                if ("未选择房间".equals(tv_choosed_room.getText().toString())) {//跳过房间直接进入选择商品页面
                    Utils.showToast(NewOrderActivity.this,"您尚未选择房间！");
                    return;
                }
                Constant.NOT_CHOOSE_ROOM = false;
                Utils.showWaittingDialog(this,"正在查询房间...");
                checkRoomAvailable();
                break;
            case Constant.CHANGE_ROOM_TO_DETAIL:
                if ("未选择房间".equals(tv_choosed_room.getText().toString())) {//跳过房间直接进入选择商品页面
                    Toast.makeText(this,"请选择房间！",Toast.LENGTH_SHORT).show();
                    return;
                }
                Utils.showWaittingDialog(this, "正在更换房间...");
                checkRoomAvailable();
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //计算出来滑动距离
        float start_y =0;
        float end_y =0;
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                start_y = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                end_y = event.getY();
                break;
        }
        dy = end_y - start_y;
        LogUtils.e(TAG, "测试滑动距离：dy===========" + dy);
        return super.onTouchEvent(event);
    }



    /**
     * 查询房间是否可用
     */
    private void checkRoomAvailable() {

        String currentTime = Utils.getCurrentTime();
        String token = Utils.getToken(currentTime,this);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String start = start_year + tv_choose_start_time.getText().toString();
        String end = "0";
        if (tv_choose_end_time.getText().toString().contains("未选择")) {
            try {
                Date parseStart = simpleDateFormat.parse(start);
                long timeStart = parseStart.getTime();
                start = dateFormat.format(timeStart);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            end = end_year + tv_choose_end_time.getText().toString();
            try {
                Date parseEnd = simpleDateFormat.parse(end);
                Date parseStart = simpleDateFormat.parse(start);
                long timeEnd = parseEnd.getTime();
                long timeStart = parseStart.getTime();
                end = dateFormat.format(timeEnd);
                start = dateFormat.format(timeStart);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        LogUtils.e(TAG, "storeid:" + Constant.STORE_ID);
        LogUtils.e(TAG, "room_id:" + choosed_room_id);
        LogUtils.e(TAG, "starttime:" + start);//2016-11-11 16:17
        LogUtils.e(TAG, "endtime:" + end);//2016-11-11 16:19
        LogUtils.e(TAG, "写死的(flag代表从pos机端传的数据)：flag:" + "0");
        OkHttpUtils.post()
                .url(UrlConstance.ROOM_IS_AVAILABLE)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams(Constant.PARAMS_NEME_PASSDORD_MD5, MD5.md5(MD5.md5(Constant.PASSWORD)))
                .addParams("storeid", Constant.STORE_ID)//门店id
                .addParams("room_id", choosed_room_id)//房间id
                .addParams("starttime", start)//开始时间：2016-11-11 16:17
                .addParams("endtime", end)//结束时间：2016-11-11 16:19
                .addParams("flag", "0")//代表从pos机端传的数据：写死
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Utils.dismissWaittingDialog();
                        Utils.showToast(NewOrderActivity.this, "请求房间状态失败，请重试！");
                        //dialogMiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e(TAG, "点击确认查询房间状态返回response：" + response);
                        boolean b = Utils.checkSaveToken(NewOrderActivity.this, response);
                        if (b) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int code = jsonObject.getInt("code");
                                if (code == 200) {
                                    RoomIsComfirmInfo roomIsComfirmInfo = new Gson().fromJson(response, RoomIsComfirmInfo.class);
                                    int flag = roomIsComfirmInfo.getMsg().getFlag();
                                    if (flag == 0) {//此情况下弹窗提示：其他服务员已经选择此房间了
                                        //dialogMiss();
                                        Utils.dismissWaittingDialog();
                                        showAlertDialog(flag, "");
                                    } else if (flag == 1) {//房间确认可用
                                        toNextActivity();
                                    } else if (flag == 2) {//此情况下弹窗提示：此订单有最晚使用时间
                                        //dialogMiss();
                                        Utils.dismissWaittingDialog();
                                        String timeStamp = roomIsComfirmInfo.getMsg().getTime();//PHP时间戳格式
                                        showAlertDialog(flag, Utils.PHPTransforTimeStampToJava(timeStamp));
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    /**
     * 显示提示信息
     */
    private void showAlertDialog(int flag, final String timeStamp) {

        String info;
        if (0 == flag) {//其他服务员已经选择此房间了
            info = getString(R.string.alert_info_choosed).toString();
            Dialog alertDialog = new AlertDialog.Builder(this)
                    .setMessage(info)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //此房间设置为被选中：或者重新请求房间状态数据
                            LogUtils.e(TAG, "房间已经被选中，重新请求了(initData)房间数据");
                            String currentTime = Utils.getCurrentTime();
                            String token = Utils.getToken(currentTime,NewOrderActivity.this);

                            String start_time = Utils.getCurrentM(Long.valueOf(currentTime + "000"));//yyyy-MM-dd HH:mm
                            initData(start_time, "0", ""+charge_model);
                        }
                    }).create();
            alertDialog.show();
        } else if (2 == flag) {//此订单有最晚使用时间
            info = getString(R.string.alert_info_head).toString() + tv_choosed_room.getText().toString() + getString(R.string.alert_info_body).toString() + Utils.getDatetimeStampToString(timeStamp) + getString(R.string.alert_info_end).toString();
            Dialog alertDialog = new AlertDialog.Builder(this)
                    .setMessage(info)
                    .setPositiveButton("确认使用", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //设置最晚使用时间为返回的时间
                            use_end_time = timeStamp;
                            end_time = timeStamp;
                            toNextActivity();
                        }
                    })
                    .setNegativeButton("重新选择", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create();
            alertDialog.show();
        }
    }

    /**
     * 跳转到下一个界面，商品和服务的选择
     */
    private void toNextActivity() {
        switch (fromwhere) {
            case Constant.NEW_ORDER_TO_DETAIL:
            case Constant.JUST_HAS_ROOM_NO_GOODS:
                newOrderSubmitOrder();
                break;
            case Constant.CHANGE_ROOM_TO_DETAIL:
                LogUtils.e(TAG, "toNextActivity():提交更换选择的房间");
                commitChangedRoom(start_time, end_time);
                break;
        }
    }

    /**
     * 新订单的提交
     */
    private void newOrderSubmitOrder() {
        LogUtils.e(TAG, "toNextActivity():提交第一次选择的房间");
        getTradeNum();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String start = start_year + tv_choose_start_time.getText().toString();
        String end = "0";
        if (tv_choose_end_time.getText().toString().contains("未选择")) {
            try {
                Date parseStart = simpleDateFormat.parse(start);
                long timeStart = parseStart.getTime();
                start = dateFormat.format(timeStart);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            end = end_year + tv_choose_end_time.getText().toString();
            try {
                Date parseEnd = simpleDateFormat.parse(end);
                Date parseStart = simpleDateFormat.parse(start);
                long timeEnd = parseEnd.getTime();
                long timeStart = parseStart.getTime();
                end = dateFormat.format(timeEnd);
                start = dateFormat.format(timeStart);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (!"0".equals(use_end_time)) {
            end = use_end_time;
        }
        start_time = start;
        end_time = end;
        stored_room_id = choosed_room_id;
        stored_room_name = tv_choosed_room.getText().toString();
        //用于绑定订单的会员id
        String vipcard_id="";
        if(memberBean!=null) {
            vipcard_id = memberBean.getMsg().getVipcardid();
        }

        LogUtils.e(TAG,"选择的房间----stored_room_id："+stored_room_id);
        LogUtils.e(TAG,"----------------------------------------------------");
        LogUtils.e(TAG,"选择的房间----account_id："+Constant.ACCOUNT_ID);
        LogUtils.e(TAG,"选择的房间----adv_id："+Constant.ADV_ID);
        LogUtils.e(TAG,"选择的房间----storeid："+Constant.STORE_ID);
        LogUtils.e(TAG,"选择的房间----deviceid："+Constant.DEVICE_IMEI);
        LogUtils.e(TAG,"选择的房间----starttime："+start);
        LogUtils.e(TAG,"选择的房间----endtime："+end);
        LogUtils.e(TAG,"选择的房间----roomid："+choosed_room_id);
        LogUtils.e(TAG,"选择的房间----roomname："+tv_choosed_room.getText().toString());
        LogUtils.e(TAG,"选择的房间----costtype："+charge_model);
        LogUtils.e(TAG,"选择的房间----vipcard_id："+vipcard_id);
        LogUtils.e(TAG,"----------------------------------------------------");
        String vip_createid= memberBean==null?"":memberBean.getMsg().getVipcreate_id();
        String currentTime = Utils.getCurrentTime();
        String token = Utils.getToken(currentTime,this);
        OkHttpUtils.post() //数据返回说明：10112 添加订单失败 ; array 0总订单id;1房间订单id
                .url(UrlConstance.ROOM_HAS_CHOOSED)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams(Constant.PARAMS_NEME_PASSDORD_MD5, MD5.md5(MD5.md5(Constant.PASSWORD)))//密码
                //.addParams("order_no", trade_num)//订单号 16.12.28辛巽东取消
                .addParams("account_id", Constant.ACCOUNT_ID)
                .addParams("adv_id", Constant.ADV_ID)//品牌主id
                .addParams("storeid", Constant.STORE_ID)//门店id
                .addParams("deviceid", Constant.DEVICE_IMEI)//设备id ???
                .addParams("starttime", start)//开始时间
                .addParams("endtime", end)//结束时间
                .addParams("roomid", choosed_room_id)//房间id
                .addParams("roomname", tv_choosed_room.getText().toString() + "")//房间名称
                .addParams("costtype", "" + charge_model)
                .addParams("vipcard_id",vipcard_id)
                .addParams("vipcreate_id",vip_createid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Utils.showToast(NewOrderActivity.this, "房间状态未知，请重试！");
                        //dialogMiss();
                        Utils.dismissWaittingDialog();
                    }

                    @Override
                    public void onResponse(String response) {
                        //dialogMiss();
                        Utils.dismissWaittingDialog();
                        LogUtils.Log("点击确认选中房间返回response：" + response);
                        boolean b = Utils.checkSaveToken(NewOrderActivity.this, response);
                        if (b) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int code = jsonObject.getInt("code");
                                if (code == 200) {
                                    if (isJustRoom) {
                                        //直接进入订单详情
                                        JSONArray msg = jsonObject.getJSONArray("msg");
                                        int id = (int) msg.get(0);
                                        int room_order_id = (int) msg.get(1);
                                        Constant.FIRST_COMMIT=true;
                                        Intent intent = new Intent(NewOrderActivity.this, OrderDetailActivity.class);
                                        intent.putExtra("unpay_order_id", ""+id);
                                        intent.putExtra("room_id_room", ""+room_order_id);
                                        intent.putExtra(Constant.MEMBER_BEAN,memberBean);
                                        startActivity(intent);
                                        storeLocalInfo();
                                        finish();
                                    }else{
                                        JSONArray msg = jsonObject.getJSONArray("msg");
                                        int id = (int) msg.get(0);
                                        //int room_order_id = (int) msg.get(1);
                                        //LogUtils.e(TAG, "待用的：room_order_id=" + room_order_id);
                                        Intent intent = new Intent(NewOrderActivity.this, AddServiceGoodActivity.class);
                                        intent.putExtra("trade_num", trade_num);
                                        intent.putExtra("unpay_order_id", ""+id);
                                        //intent.putExtra("room_order_id", ""+room_order_id);
                                        intent.putExtra("room_order_id", "" + choosed_room_id);
                                        intent.putExtra(Constant.MEMBER_BEAN,memberBean);
                                        intent.putExtra("from_where", Constant.ADD_FROM_NORMAl_NUM);
                                        intent.putExtra("first_come_time", FirstTimeComeInMill);
                                        startActivity(intent);
                                        storeLocalInfo();
                                        finish();
                                    }
                                } else {
                                    Utils.showToast(NewOrderActivity.this, "选择房间失败请重新再试！");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    /**
     * 存储本地订单信息
     */
    private void storeLocalInfo() {
        Constant.localOrderBean.setCurrentStartTime(start_time);//形式：yyyy-MM-dd HH:mm
        Constant.localOrderBean.setCurrentEndTime(end_time);
        Constant.localOrderBean.setCurrentRoomId(stored_room_id);
        Constant.localOrderBean.setCurrentRoomName(stored_room_name);
    }

    /**
     * 生成订单号
     */
    public void getTradeNum() {
        trade_num = Utils.getCurrentTime1() + Constant.ACCOUNT_ID;
    }

    /**
     * wheelView选择时间
     * 1.开始时间默认为当前时间，结束时间默认为开始时间，如果结束时间选择过的话，默认为当前所选时间
     * 2.按小时计费正常显示，分钟小时都可滚动
     * 3.按天计费，小时，分钟不可以滚动，写死
     * 4.按天计费规则，开始时间为6点前的，到当天13：00算一天，开始时间为6点后的，到次日13：00算一天
     */
    private void showTimePickerPup() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        time_picker_view = inflater.inflate(R.layout.pop_item, null);
        TextView tv_finish_choose = (TextView) time_picker_view.findViewById(R.id.tv_finish_choose);
        TextView tv_cancel_choose = (TextView) time_picker_view.findViewById(R.id.tv_cancel_choose);
        //TextView tv_time_explain = (TextView) time_picker_view.findViewById(R.id.tv_time_explain);
        tv_time_choose_title = (TextView) time_picker_view.findViewById(R.id.tv_time_choose_title);
        timePickerPop = new PopupWindow(time_picker_view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setAlpha(0.6f);
        timePickerPop.setBackgroundDrawable(new BitmapDrawable());
        timePickerPop.setOutsideTouchable(true);
        timePickerPop.setFocusable(true);
        timePickerPop.setAnimationStyle(R.style.popupAnimation);
        timePickerPop.showAtLocation(findViewById(R.id.rl_new_order), Gravity.BOTTOM, 0, 0);
        timePickerPop.update();
        timePickerPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(1);
            }
        });

        switch (clickView) {
            case START_TIME:
                tv_cancel_choose.setVisibility(View.GONE);
                tv_time_choose_title.setText("开始时间");
                initWheelView(time_picker_view, currentStartMill);
                currrentYMD = yyMmDdFormat.format(currentStartMill);
                start_time = praseFor.format(currentStartMill);//"yyyy-MM-dd HH:mm"
                start_year=start_time.substring(0,4)+"年";

                //初始化开始选择的时间
                currrentYMD = yyMmDdFormat.format(currentStartMill);
                currentHour = start_time.substring(11, 13);
                currentMin = start_time.substring(14,16);
                LogUtils.e(TAG,"showTimePickerPup() currrentYMD=="+currrentYMD+",currentHour=="+currentHour+",currentMin=="+currentMin);
                break;
            case END_TIME:
                tv_time_choose_title.setText("结束时间");
                if (currentEndMill != -1) {
                    initWheelView(time_picker_view, currentEndMill);
                    currrentYMD = yyMmDdFormat.format(currentEndMill);
                    end_time = praseFor.format(currentEndMill);
                    end_year=end_time.substring(0, 4)+"年";

                    //初始化开始选择的时间
                    currrentYMD = yyMmDdFormat.format(currentEndMill);
                    currentHour = end_time.substring(11, 13);
                    currentMin = end_time.substring(14,16);
                    LogUtils.e(TAG,"showTimePickerPup() currrentYMD=="+currrentYMD+",currentHour=="+currentHour+",currentMin=="+currentMin);
                } else {
                    initWheelView(time_picker_view, currentStartMill);
                    currrentYMD = yyMmDdFormat.format(currentStartMill);
                    end_time = praseFor.format(currentStartMill);
                    end_year=end_time.substring(0, 4)+"年";

                    //初始化开始选择的时间
                    currrentYMD = yyMmDdFormat.format(currentStartMill);
                    currentHour = end_time.substring(11, 13);
                    currentMin = end_time.substring(14,16);
                }
                break;
        }
        tv_finish_choose.setOnClickListener(this);
        tv_cancel_choose.setOnClickListener(this);
    }

    private void setAlpha(float alpha) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = alpha;
        getWindow().setAttributes(params);
    }

    /**
     * 初始化时间选择控件
     * @param view
     * @param currentMill
     */
    private void initWheelView(View view, long currentMill) {
        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
        int curDate = c.get(Calendar.DATE);

        String currenthh = new SimpleDateFormat("HH").format(currentMill);
        String currentmm = new SimpleDateFormat("mm").format(currentMill);
        List<String> asList = Arrays.asList(xiaoshi_start);
        List<String> asList2 = Arrays.asList(fenzhong_start);

        wl_ymd = (WheelView) view.findViewById(R.id.wl_ymd);
        wl_week = (WheelView) view.findViewById(R.id.wl_week);
        wl_hour = (WheelView) view.findViewById(R.id.wl_hour);
        wl_min = (WheelView) view.findViewById(R.id.wl_min);

        ArrayWheelAdapter<String> weekAdapter = new ArrayWheelAdapter<>(this, ymdData);
        List<String> ymdList = Arrays.asList(ymdData);
        wl_ymd.setViewAdapter(weekAdapter);
        weekAdapter.setTextSize(18);
        wl_ymd.setCyclic(true);

        switch (clickView) {
            case START_TIME :
                wl_ymd.setCurrentItem(ymdList.indexOf(GetTimeUtil.getYMDTime(currentStartMill)));
                break;
            case END_TIME:
                if(currentEndMill==-1) {
                    wl_ymd.setCurrentItem(ymdList.indexOf(GetTimeUtil.getYMDTime(currentStartMill)));
                }else {
                    wl_ymd.setCurrentItem(ymdList.indexOf(GetTimeUtil.getYMDTime(currentEndMill)));
                }
                break;
        }
        wl_ymd.addChangingListener(new MyOnWheelChangeListener());

        ArrayWheelAdapter<String> weekAdapter2 = new ArrayWheelAdapter<>(this, week_str);
        wl_week.setViewAdapter(weekAdapter2);
        weekAdapter2.setTextSize(18);
        wl_week.setEnabled(false);
        wl_week.setCyclic(true);
        wl_week.setVisibility(View.GONE);
        changeWheelWeek(curYear, curMonth, curDate);

        initHour(currenthh, asList);
        initMinute(currentmm, asList2);
    }

    private void initWheelView_(View view, long currentMill) {
        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
        int curDate = c.get(Calendar.DATE);

        String currenthh = new SimpleDateFormat("HH").format(currentMill);
        String currentmm = new SimpleDateFormat("mm").format(currentMill);
        List<String> asList = Arrays.asList(xiaoshi_start);
        List<String> asList2 = Arrays.asList(fenzhong_start);

        wl_ymd = (WheelView) view.findViewById(R.id.wl_ymd);
        wl_week = (WheelView) view.findViewById(R.id.wl_week);
        wl_hour = (WheelView) view.findViewById(R.id.wl_hour);
        wl_min = (WheelView) view.findViewById(R.id.wl_min);

        ArrayWheelAdapter<String> weekAdapter = new ArrayWheelAdapter<>(this, ymdData);
        List<String> ymdList = Arrays.asList(ymdData);
        wl_ymd.setViewAdapter(weekAdapter);
        weekAdapter.setTextSize(18);
        wl_ymd.setCyclic(true);

        switch (clickView) {
            case START_TIME :
                wl_ymd.setCurrentItem(ymdList.indexOf(GetTimeUtil.getYMDTime(currentStartMill)));
                break;
            case END_TIME:
                if(currentEndMill==-1) {
                    wl_ymd.setCurrentItem(ymdList.indexOf(GetTimeUtil.getYMDTime(currentStartMill)));
                }else {
                    wl_ymd.setCurrentItem(ymdList.indexOf(GetTimeUtil.getYMDTime(currentEndMill)));
                }
                break;
        }
        wl_ymd.addChangingListener(new MyOnWheelChangeListener());

        ArrayWheelAdapter<String> weekAdapter2 = new ArrayWheelAdapter<>(this, week_str);
        wl_week.setViewAdapter(weekAdapter2);
        weekAdapter2.setTextSize(18);
        wl_week.setEnabled(false);
        wl_week.setCyclic(true);
        wl_week.setVisibility(View.GONE);
        changeWheelWeek(curYear, curMonth, curDate);

        initHour(currenthh, asList);
        initMinute(currentmm, asList2);
    }


    /**
     * 滚动的监听
     */
    class MyOnWheelChangeListener implements OnWheelChangedListener {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            int year, month, day;
            switch (wheel.getId()) {
                case R.id.wl_ymd :
                    String value = ymdData[newValue];
                    year = Integer.parseInt(value.substring(0, value.indexOf("-")));
                    month = Integer.parseInt(value.substring(value.indexOf("-") + 1, value.lastIndexOf("-")));
                    day = Integer.parseInt(value.substring(value.lastIndexOf("-") + 1, value.length()));
                    changeWheelWeek(year, month, day);

                    if(month<10&&day<10) {
                        currrentYMD =year+"年0"+month+"月0"+day+"日";
                    }else if(month>=10&&day>=10){
                        currrentYMD =year+"年"+month+"月"+day+"日";
                    }else if(month<10&&day>=10){
                        currrentYMD =year+"年0"+month+"月"+day+"日";
                    }else{
                        currrentYMD =year+"年"+month+"月0"+day+"日";
                    }

                    switch (clickView) {
                        case START_TIME:
                            start_year =year+"年";
                            break;
                        case END_TIME:
                            end_year =year+"年";
                            break;
                    }

                    /*if(charge_model==CHARGE_BY_DAY){
                        if(day>8) {
                            if(month>9){
                                currrentYMD_fee_day = year+"年"+month+"月"+(day+1)+"日";
                            }else{
                                currrentYMD_fee_day = year+"年0"+month+"月"+(day+1)+"日";
                            }
                        }else{
                            if(month>9){
                                currrentYMD_fee_day = year+"年"+month+"月0"+(day+1)+"日";
                            }else{
                                currrentYMD_fee_day = year+"年0"+month+"月0"+(day+1)+"日";
                            }
                        }
                    }*/
                    break;
                case R.id.wl_hour :
                    currentHour = "" + (newValue)  ;
                    break;
                case R.id.wl_min :
                    if(newValue<10) {
                        currentMin="0" + newValue;
                    }else {
                        currentMin = newValue + "";
                    }
                    break;
            }
        }
    }

    /**
     * 改变周的选择
     * @param year
     * @param month
     * @param day
     */
    private void changeWheelWeek(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        wl_week.setCurrentItem(i - 1);
        lastweek = week_str[i - 1];
    }

    /**
     * 小时的初始化
     * @param currenthh
     * @param asList
     */
    private void initHour(String currenthh, List<String> asList) {
        NumericWheelAdapter numericAdapter1=null;
        switch (charge_model) {
            case  CHARGE_BY_DAY://按天
                currentHour=String.valueOf(currenthh);
                numericAdapter1 = new NumericWheelAdapter(this, 0, 23);
                wl_hour.setViewAdapter(numericAdapter1);
                wl_hour.setCurrentItem(asList.indexOf(currenthh));
                wl_hour.setEnabled(true);
                wl_hour.setCyclic(true);

                /*if(clickView==START_TIME) {
                    currentHour=String.valueOf(currenthh);
                    numericAdapter1 = new NumericWheelAdapter(this, 0, 23);
                    wl_hour.setViewAdapter(numericAdapter1);
                    wl_hour.setCurrentItem(asList.indexOf(currenthh));
                    wl_hour.setEnabled(true);
                    wl_hour.setCyclic(true);
                }else {
                    numericAdapter1 = new NumericWheelAdapter(this, 13, 13);
                    wl_hour.setViewAdapter(numericAdapter1);
                    wl_hour.setEnabled(false);
                    wl_hour.setCyclic(false);
                }*/
                break;
            case CHARGE_NO_RULE:
            case CHARGE_BY_HOUR://按小时
                currentHour=String.valueOf(currenthh);
                numericAdapter1 = new NumericWheelAdapter(this, 0, 23);
                wl_hour.setViewAdapter(numericAdapter1);
                wl_hour.setCurrentItem(asList.indexOf(currenthh));
                wl_hour.setEnabled(true);
                wl_hour.setCyclic(true);
                break;
        }
        numericAdapter1.setLabel("时");
        numericAdapter1.setTextSize(18);
        wl_hour.addChangingListener(new MyOnWheelChangeListener());
    }

    /**
     * 4.分钟的初始化
     * @param currentmm
     * @param asList2
     */
    private void initMinute(String currentmm, List<String> asList2) {

        NumericWheelAdapter numericAdapter2=null;
        switch (charge_model) {
            case CHARGE_BY_DAY :
                currentMin=String.valueOf(currentmm);
                numericAdapter2 = new NumericWheelAdapter(this, 0, 59);
                wl_min.setViewAdapter(numericAdapter2);
                wl_min.setCurrentItem(asList2.indexOf(currentmm));
                wl_min.setEnabled(true);
                wl_min.setCyclic(true);

                /*if(clickView==START_TIME) {
                    currentMin=String.valueOf(currentmm);
                    numericAdapter2 = new NumericWheelAdapter(this, 0, 59);
                    wl_min.setViewAdapter(numericAdapter2);
                    wl_min.setCurrentItem(asList2.indexOf(currentmm));
                    wl_min.setEnabled(true);
                    wl_min.setCyclic(true);
                }else {
                    numericAdapter2 = new NumericWheelAdapter(this, 0, 0);
                    wl_min.setViewAdapter(numericAdapter2);
                    wl_min.setEnabled(false);
                    wl_min.setCyclic(false);
                }*/
                break;
            case CHARGE_NO_RULE:
            case CHARGE_BY_HOUR:
                currentMin=String.valueOf(currentmm);
                numericAdapter2 = new NumericWheelAdapter(this, 0, 59);
                wl_min.setViewAdapter(numericAdapter2);
                wl_min.setCurrentItem(asList2.indexOf(currentmm));
                wl_min.setEnabled(true);
                wl_min.setCyclic(true);
                break;
        }
        numericAdapter2.setLabel("分");
        numericAdapter2.setTextSize(18);
        wl_min.addChangingListener(new MyOnWheelChangeListener());
    }

    /**
     * 检查时间选择的正确性、格式化时长
     */
    private void initDxTime() {
        long dxMill;
        dxMill = currentEndMill - currentStartMill;
        LogUtils.e(TAG,"dxMill-="+dxMill);
        LogUtils.e(TAG,"currentStartMill-="+currentStartMill);
        LogUtils.e(TAG,"currentEndMill-="+currentEndMill);

        switch (clickView) {
            case START_TIME:
                switch (charge_model){
                    case CHARGE_BY_DAY:
                        LogUtils.e(TAG,"3");
                        if(currentEndMill!=-1){//结束时间未选择
                            LogUtils.e(TAG,"4");
                            //判断是否是大于一天 "yyyy-MM-dd HH:mm"
                            String format_end = praseFor.format(currentEndMill);
                            String format_start = praseFor.format(currentStartMill);
                            String sub_start = format_start.substring(0, 11);
                            String sub_end = format_end.substring(0, 11);
                            SimpleDateFormat prase_no_hourmin = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            try {
                                Date parse_start = prase_no_hourmin.parse(sub_start);
                                Date parse_end = prase_no_hourmin.parse(sub_end);
                                LogUtils.e(TAG,"parse_end.getTime()="+parse_end.getTime()+",parse_start.getTime()="+parse_start.getTime());
                                dxMill = parse_end.getTime()-parse_start.getTime();
                                LogUtils.e(TAG,"dxMill-=-=-=-"+dxMill);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if(dxMill<0) {
                                Toast.makeText(this, "开始时间不得晚于结束时间", Toast.LENGTH_SHORT).show();
                                resetTimeData();
                                return;
                            }else if(dxMill==0){
                                Toast.makeText(this, "开始时间不得等于结束时间", Toast.LENGTH_SHORT).show();
                                resetTimeData();
                                return;
                            }
                        }
                        break;
                    case CHARGE_NO_RULE:
                    case CHARGE_BY_HOUR:
                        LogUtils.e(TAG,"5");
                        if(currentEndMill!=-1){
                            if(dxMill<0) {
                                Toast.makeText(this, "开始时间不得晚于结束时间", Toast.LENGTH_SHORT).show();
                                resetTimeData();
                                return;
                            }else if(dxMill==0){
                                Toast.makeText(this, "开始时间不得等于结束时间", Toast.LENGTH_SHORT).show();
                                resetTimeData();
                                return;
                            }
                        }
                        break;
                }
                break;
            case END_TIME:
                switch (charge_model){
                    case CHARGE_BY_DAY:
                        //判断是否是大于一天 "yyyy-MM-dd HH:mm"
                        String format_start = praseFor.format(currentStartMill);
                        String format_end = praseFor.format(currentEndMill);
                        SimpleDateFormat prase_no_hourmin = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        try {
                            Date parse_start = prase_no_hourmin.parse(format_start);
                            Date parse_end = prase_no_hourmin.parse(format_end);
                            LogUtils.e(TAG,"parse_end.getTime()="+parse_end.getTime()+",parse_start.getTime()="+parse_start.getTime());
                            dxMill = parse_end.getTime()-parse_start.getTime();
                            LogUtils.e(TAG,"dxMill-=-=-=-"+dxMill);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        LogUtils.e(TAG,"6");
                        if(dxMill<0) {
                            Toast.makeText(this, "结束时间不得早于开始时间", Toast.LENGTH_SHORT).show();
                            resetTimeData();
                            return;
                        }else if(dxMill==0){
                            Toast.makeText(this, "结束时间不得等于开始时间", Toast.LENGTH_SHORT).show();
                            resetTimeData();
                            return;
                        }
                        break;
                    case CHARGE_NO_RULE:
                    case CHARGE_BY_HOUR:
                        LogUtils.e(TAG,"7");
                        if(dxMill<0) {
                            Toast.makeText(this, "结束时间不得早于开始时间", Toast.LENGTH_SHORT).show();
                            resetTimeData();
                            return;
                        }else if(dxMill==0){
                            Toast.makeText(this, "结束时间不得等于开始时间", Toast.LENGTH_SHORT).show();
                            resetTimeData();
                            return;
                        }
                        break;
                }
                break;
        }

        start_time = Utils.getCurrentM(currentStartMill);
        end_time = Utils.getCurrentM(currentEndMill);
        initData( start_time, end_time, "" + charge_model);

        switch (clickView) {
            case START_TIME :
                LogUtils.e(TAG, "START_TIME");
                switch (charge_model){
                    case CHARGE_BY_DAY:
                        tv_choose_start_time.setText(currrentYMD.substring(5) + " " + currentHour + ":" + currentMin);
                        break;
                    case CHARGE_NO_RULE:
                    case CHARGE_BY_HOUR:
                        tv_choose_start_time.setText(currrentYMD.substring(5) + " " + currentHour + ":" + currentMin);
                        break;
                }
                break;
            case END_TIME:
                LogUtils.e(TAG, "currrentYMD-=-=-="+currrentYMD);
                if(charge_model==CHARGE_BY_DAY) {

                    //tv_choose_end_time.setText(currrentYMD_fee_day.substring(5)+ " " +currentHour+ ":" +currentMin);
                    Date parse = new Date();
                    try {
                        parse = praseFor.parse(end_time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    tv_choose_end_time.setText(praseFormateNoYear.format(Utils.getTimeNoSec(parse.getTime())));

                    LogUtils.e(TAG, "tv_choose_end_time: test " + currrentYMD.substring(5) + " " + currentHour + ":" + currentMin);
                    LogUtils.e(TAG, "tv_choose_end_time:" + end_year + tv_choose_end_time.getText().toString());
                }else{
                    tv_choose_end_time.setText(currrentYMD.substring(5) + " " + currentHour + ":" + currentMin);
                    LogUtils.e(TAG, "tv_choose_end_time:" + end_year +tv_choose_end_time.getText().toString());
                }
                break;
        }
        if(timePickerPop!=null&&timePickerPop.isShowing()){
            timePickerPop.dismiss();
        }
    }

    /**
     * 初始化开始时间
     */
    private void initChooseTime() {
        start_year = format_just_yer.format(new Date(FirstTimeComeInMill));
        end_year = format_just_yer.format(new Date(FirstTimeComeInMill+86400000));
        yyMmDdFormat = new SimpleDateFormat("yyyy年MM月dd日");
        currrentYMD = yyMmDdFormat.format(new Date(FirstTimeComeInMill));
        final long day = 24 * 60 * 60 * 1000;
        final long lastYear = FirstTimeComeInMill - day * 360;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 7200; i++) {
                    long time = lastYear + day * i;
                    ymdData[i] = GetTimeUtil.getYMDTime(time);
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 网络错误
     */
    public void netError() {
        iv_jiazai_filure.setVisibility(View.VISIBLE);
        iv_jiazai_filure.setImageResource(R.drawable.net_error);
        pb_loading.setVisibility(View.GONE);
        tv_loading.setText("请检查网络后，点击屏幕重新加载！");
    }

    public void noData() {
        iv_jiazai_filure.setVisibility(View.GONE);
        pb_loading.setVisibility(View.GONE);
        tv_loading.setText("没有数据请重试...");
    }

    /**
     * 控制显示的下蓝线
     * @param mark 0标识天 1标识小时 2标识免费
     */
    private void displayLines(int mark) {
        switch (mark){
            case 0:
                rbtn_charge_day.setTextColor(Color.parseColor("#3f88ce"));
                rbtn_charge_hour.setTextColor(Color.parseColor("#000000"));
                rbtn_charge_nofee.setTextColor(Color.parseColor("#000000"));
                line_which_display_one.setVisibility(View.VISIBLE);
                line_which_display_two.setVisibility(View.GONE);
                line_which_display_three.setVisibility(View.GONE);
                break;
            case 1:
                rbtn_charge_day.setTextColor(Color.parseColor("#000000"));
                rbtn_charge_hour.setTextColor(Color.parseColor("#3f88ce"));
                rbtn_charge_nofee.setTextColor(Color.parseColor("#000000"));
                line_which_display_one.setVisibility(View.GONE);
                line_which_display_two.setVisibility(View.VISIBLE);
                line_which_display_three.setVisibility(View.GONE);
                break;
            case 2:
                rbtn_charge_day.setTextColor(Color.parseColor("#000000"));
                rbtn_charge_hour.setTextColor(Color.parseColor("#000000"));
                rbtn_charge_nofee.setTextColor(Color.parseColor("#3f88ce"));
                line_which_display_one.setVisibility(View.GONE);
                line_which_display_two.setVisibility(View.GONE);
                line_which_display_three.setVisibility(View.VISIBLE);
                break;
        }
    }
}