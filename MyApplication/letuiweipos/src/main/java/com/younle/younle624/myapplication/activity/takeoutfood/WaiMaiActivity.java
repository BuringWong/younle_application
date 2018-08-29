package com.younle.younle624.myapplication.activity.takeoutfood;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.MainActivity;
import com.younle.younle624.myapplication.activity.orderguide.UpgradeAccountActivity;
import com.younle.younle624.myapplication.adapter.waimai.WmAdapter;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.membercharge.ChargeItem;
import com.younle.younle624.myapplication.domain.printsetting.SavedPrinter;
import com.younle.younle624.myapplication.domain.waimai.ElmOrderBean;
import com.younle.younle624.myapplication.domain.waimai.MtOrderDetail;
import com.younle.younle624.myapplication.domain.waimai.NewElmOrderBean;
import com.younle.younle624.myapplication.domain.waimai.Overdue;
import com.younle.younle624.myapplication.domain.waimai.WmPintData;
import com.younle.younle624.myapplication.myservice.BluetoothService;
import com.younle.younle624.myapplication.myservice.PrintService;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.utils.SaveUtils;
import com.younle.younle624.myapplication.utils.SpUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.utils.printmanager.BTPrintUtils;
import com.younle.younle624.myapplication.utils.printmanager.PrintUtils;
import com.younle.younle624.myapplication.utils.printmanager.YunPrintUtils;
import com.younle.younle624.myapplication.view.XListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

/**
 * 外卖接单并自动打印界面
 */
public  class WaiMaiActivity <T> extends Activity implements View.OnClickListener, PrintAgainListener {
    private static final String TAG = "WaiMaiActivity";
    private static final int WAIT_BLUETOOTH_OPEN = 1;
    private LinearLayout ll_cancel;
    private TextView tv_title;
    private TextView tv_cancel;
    private RelativeLayout rl_jiedan_notices;
    private ImageView iv_close_notices;
    private ListView lv_wm_order;
    private RelativeLayout rl_wmaccount_setting;
    private WmAdapter adapter=new WmAdapter(this,this);
    private List<WmPintData> printData;
    private List<WmPintData> btprintData;
    private Map printYunWMData;
    private IWoyouService iWoyouService;
    private TextView tv_today_wm_total;
    public ServiceConnection connService = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iWoyouService = IWoyouService.Stub.asInterface(service);

        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            iWoyouService = null;
        }
    };
    //分线程
    private ICallback printCallBack=new ICallback.Stub() {
        @Override
        public void onRunResult(boolean isSuccess) throws RemoteException {
            LogUtils.Log("onRunResult");
            if(isSuccess) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setFinishPrint(true);
                        adapter.doAnimation(false);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
        @Override
        public void onReturnString(String result) throws RemoteException {
        }

        @Override
        public void onRaiseException(int code, String msg) throws RemoteException {
        }
    };
   /* private List<Map> savedData;
    private List<Map> savedBtData;
    private int saved_pre_size;*/
    private TextView pay_again_title;

    /**
     * 顶部的三个图标
     */
    private ImageView iv_bd;
    private ImageView iv_elm;
    private ImageView iv_mt;
    private ToggleButton tb_stop_start;

    private View line2;

    private NetWorks netWork;

    private BluetoothService mService;
    /**
     * 改变外卖状态的联网返回
     */
    private NetWorks.OnNetCallBack callBack=new NetWorks.OnNetCallBack() {
        @Override
        public void onError(Exception e, int flag) {
            changeTbStatus();
        }

        @Override
        public void onResonse(String response, int flag) {
            LogUtils.Log("开关外卖的response==" + response);
            praseJson(response);
        }
    };
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case Constant.WAIT_BLUETOOTH_OPEN://等待蓝牙打开
                    BTPrintUtils.getInstance().connectBtPrinterTest(mService,WaiMaiActivity.this,handler);
                    break;
                case Constant.MESSAGE_STATE_CHANGE:
                    LogUtils.e(TAG,"收到了连接状态变更的消息");
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            LogUtils.e(TAG, "已连接");
                            Toast.makeText(WaiMaiActivity.this, "蓝牙已连接", Toast.LENGTH_SHORT).show();
                            PrintByBluetooth();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            LogUtils.e(TAG, "蓝牙正在连接");
                            Toast.makeText(WaiMaiActivity.this, "蓝牙正在连接", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothService.STATE_LISTEN:
                            break;
                        case BluetoothService.STATE_NONE:
                            LogUtils.e(TAG, "蓝牙无连接，请重新配置蓝牙打印机");
//                            Toast.makeText(PrintService.this, "无连接", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
            }
        }
    };
    private PopupWindow popupWindow;
    private String btPrintItems="";
    private String printItems="";
    private BluetoothService.BtPrintFinshCallBack btCallBack=new BluetoothService.BtPrintFinshCallBack() {
        @Override
        public void onError() {

        }

        @Override
        public void onFinish() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.setFinishPrint(true);
                    adapter.doAnimation(false);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    };

    private void praseJson(String json) {
        try {
            JSONObject jsonObject=new JSONObject(json);
            int code = jsonObject.getInt("code");
            if(code==200) {
                Boolean is_jiedan_open = SpUtils.getInstance(this).getBoolean(Constant.IS_JIEDAN_OPEN, false);
                LogUtils.Log("改变接单状态成功");
                if(is_jiedan_open) {
                    SpUtils.getInstance(this).save(Constant.IS_JIEDAN_OPEN,false);
                    tb_stop_start.setBackgroundResource(R.drawable.wm_off);
                }else {
                    SpUtils.getInstance(this).save(Constant.IS_JIEDAN_OPEN,true);
                    tb_stop_start.setBackgroundResource(R.drawable.wm_on);
                    Toast.makeText(WaiMaiActivity.this, "成功开启自动接单模式", Toast.LENGTH_SHORT).show();
                }
            }else {
                changeTbStatus();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void changeTbStatus() {
        Boolean is_jiedan_open = SpUtils.getInstance(this).getBoolean(Constant.IS_JIEDAN_OPEN, false);
        if(!is_jiedan_open) {
            showChangeWmStatusErr("当前网络异常，开启接单模式失败！");
//            Toast.makeText(WaiMaiActivity.this, "", Toast.LENGTH_SHORT).show();
            tb_stop_start.setBackgroundResource(R.drawable.wm_off);
        }else {
            showChangeWmStatusErr("当前网络异常，关闭接单模式失败！");
//            Toast.makeText(WaiMaiActivity.this, "当前网络异常，关闭接单模式失败！", Toast.LENGTH_SHORT).show();
            tb_stop_start.setBackgroundResource(R.drawable.wm_on);
        }
    }

    private void showChangeWmStatusErr(String msg) {
        new AlertDialog.Builder(this)
                .setTitle(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                    })
                    .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wai_mai);
        EventBus.getDefault().register(this);
        String today = Utils.getToday();
        netWork = new NetWorks(this);
        /*savedData = (List< Map >) SaveUtils.getObject(this, today);
        savedBtData = (List< Map >) SaveUtils.getObject(this, "bt"+today);
        saved_pre_size = SpUtils.getInstance(this).getInt(Constant.PARA_PRE_SIZE, -1);*/

        btPrintItems = SpUtils.getInstance(this).getString(Constant.bt_print_permission, "");
        printItems = SpUtils.getInstance(this).getString(Constant.print_permission, "");
        mService = BluetoothService.getInstance();
        mService.setHandler(handler);

        PrintUtils.getInstance().initService(this, connService);
        Utils.initToolBarColor(this, "#3f88ce");
        initView();
        initThreeIcon();
        setListener();
        initData();
    }

    private void initView() {
        ll_cancel = (LinearLayout)findViewById(R.id.ll_cancel);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("外卖接单");
        tv_cancel = (TextView)findViewById(R.id.tv_cancel);
        tv_cancel.setText("管理");
        rl_jiedan_notices = (RelativeLayout)findViewById(R.id.rl_jiedan_notices);
        iv_close_notices = (ImageView)findViewById(R.id.iv_close_notices);
        lv_wm_order = (ListView)findViewById(R.id.lv_wm_order);
        rl_wmaccount_setting = (RelativeLayout)findViewById(R.id.rl_wmaccount_setting);
        tv_today_wm_total = (TextView)findViewById(R.id.tv_today_wm_total);
        pay_again_title = (TextView)findViewById(R.id.pay_again_title);

        iv_bd = (ImageView)findViewById(R.id.iv_bd);
        iv_elm = (ImageView)findViewById(R.id.iv_elm);
        iv_mt = (ImageView)findViewById(R.id.iv_mt);

        line2 = findViewById(R.id.line2);
        //续费
        initPayAgain();
        //开始接单，停止接单
        tb_stop_start = (ToggleButton)findViewById(R.id.tb_stop_start);
        Boolean is_jiedan_open = SpUtils.getInstance(this).getBoolean(Constant.IS_JIEDAN_OPEN, false);
        if(is_jiedan_open) {
            tb_stop_start.setBackgroundResource(R.drawable.wm_on);
        }else {
            tb_stop_start.setBackgroundResource(R.drawable.wm_off);
        }

    }

    private void initPayAgain() {
        String alert = SpUtils.getInstance(this).getString(Constant.ALERT, "");
        LogUtils.Log("alert=="+alert);
        if(!TextUtils.isEmpty(alert)) {
            pay_again_title.setVisibility(View.VISIBLE);
            line2.setVisibility(View.VISIBLE);
            //图文混排，spanable
            SpannableString ssp = new SpannableString(alert);
            ssp.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setUnderlineText(false);
                }
                @Override
                public void onClick(View widget) {
                    Intent intent=new Intent(WaiMaiActivity.this, UpgradeAccountActivity.class);
                    ChargeItem payItem=new ChargeItem("wm");//最后一个字段需加上
                    intent.putExtra("pay_item",payItem);
                    startActivity(intent);
                }
            }, alert.length()-5, alert.length()-1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            ssp.setSpan(new ForegroundColorSpan(Color.rgb(51, 102, 255)), alert.length()-5, alert.length()-1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            pay_again_title.setHighlightColor(Color.TRANSPARENT);
            pay_again_title.setText(ssp);
            pay_again_title.setMovementMethod(LinkMovementMethod.getInstance());
        }else {
            pay_again_title.setVisibility(View.GONE);
            line2.setVisibility(View.GONE);
        }
    }

    private void setListener() {
        iv_close_notices.setOnClickListener(this);
        ll_cancel.setOnClickListener(this);
        lv_wm_order.setAdapter(new WmAdapter(this, this));
        rl_wmaccount_setting.setOnClickListener(this);
        lv_wm_order.setOnScrollListener(new XlistScrollListener());
        tb_stop_start.setOnCheckedChangeListener(new TbOnStatusChangeListener());
    }

    /**
     * 开始接单，暂停接单
     */
    class TbOnStatusChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            Boolean is_jiedan_open = SpUtils.getInstance(WaiMaiActivity.this).getBoolean(Constant.IS_JIEDAN_OPEN, false);
            if(!is_jiedan_open) {
                LogUtils.Log("开启");
                netWork.switchWmStatus(callBack, 0, 1);
                boolean wmPrint = Utils.isServiceWork(WaiMaiActivity.this, "com.younle.younle624.myapplication.myservice.PrintService");
                if(!wmPrint) {
                    WaiMaiActivity.this.startService(new Intent(WaiMaiActivity.this, PrintService.class));
                }
            }else {
                LogUtils.Log("关闭");
                showClosePup();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close_notices :
                rl_jiedan_notices.setVisibility(View.GONE);
                break;
            case R.id.ll_cancel:
                Intent intent=new Intent(this, MainActivity.class);
                intent.putExtra("position",4);
                startActivity(intent);
                finish();
                break;
            case R.id.rl_wmaccount_setting:
                intent=new Intent(this,WmAccountSettingActivity.class);
                startActivity(intent);
                break;
        }
    }
    private void initData() {
        tv_today_wm_total.setText("今日订单：" + Constant.wm_data.size() + "笔");
        adapter.setData(Constant.wm_data);
//        adapter.setData(savedData);
        lv_wm_order.setAdapter(adapter);
    }

    @Override
    protected void onRestart() {
        initThreeIcon();
        tv_today_wm_total.setText("今日订单：" + Constant.wm_data.size() + "笔");
        adapter.notifyDataSetChanged();
        lv_wm_order.setSelection(0);
        super.onRestart();
    }

    private void initThreeIcon() {
        if(Constant.WM_STATUS.contains(1)) {
            iv_mt.setImageResource(R.drawable.icon_mei);
        }else {
            iv_mt.setImageResource(R.drawable.icon_mt_gray);
        }
        if(Constant.WM_STATUS.contains(2)) {
            iv_elm.setImageResource(R.drawable.icon_e);
        }else {
            iv_elm.setImageResource(R.drawable.icon_elm_gray);
        }
        if(Constant.WM_STATUS.contains(3)) {
            iv_bd.setImageResource(R.drawable.icon_bai);
        }else {
            iv_bd.setImageResource(R.drawable.icon_bd_gray);
        }
    }

    /**
     * 美团
     * @param mtOrderbean
     */
    @Subscribe
    public void onEventMainThread(MtOrderDetail mtOrderbean) {
        LogUtils.Log("activity中接到了recivier传递的订单");
        LogUtils.WmLog("activity中接到了recivier传递的美团订单");
        refreshView(true);
    }
    /**
     * 饿了么
     * @param elmOrderbean
     */
    @Subscribe
    public void onEventMainThread(ElmOrderBean elmOrderbean) {
        LogUtils.Log("收到了饿了么订单");
        LogUtils.WmLog("activity中接到了recivier传递的饿了么订单");

        refreshView(true);
    }
    /**
     * 新的 饿了么
     * @param newElmOrderBean
     */
    @Subscribe
    public void onEventMainThread(NewElmOrderBean newElmOrderBean) {
        LogUtils.WmLog("activity中接到了recivier传递的新的 饿了么订单");
        LogUtils.Log("收到了饿了么订单");
        refreshView(true);
    }
    /**
     * 打印完成
     * @param finishPoi
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Boolean finishPoi) {
       LogUtils.Log("activity中收到了打印完成的消息");
       LogUtils.WmLog("activity中收到了打印完成的消息");
        if(finishPoi) {
            refreshView(false);
        }
//        EventBus.getDefault().cancelEventDelivery(finishPoi);
    }

    /**
     * 到期或者剩余5天UI处理
     * @param overdue
     */
    @Subscribe
    public void onEventMainThread(Overdue overdue) {
        if(0==overdue.getType()) {
            netWork.switchWmStatus(callBack,0,0);
        }
        initPayAgain();
        EventBus.getDefault().cancelEventDelivery(overdue);
    }
    /**
     * 刷新界面
     */
    private void refreshView(boolean needAnimation) {
        tv_today_wm_total.setText("今日订单：" + Constant.wm_data.size() + "笔");
        adapter.doAnimation(needAnimation);
        adapter.notifyDataSetChanged();
        lv_wm_order.setSelection(0);
    }
    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        unbindService(connService);
        super.onDestroy();
    }
    /**
     * 再次打印的回调
     */
    @Override
    public void onCLickPrintAgain(int position) {
        Map map = Constant.wm_data.get(position);
        String type= (String) map.get("type");
        Object bean = map.get("bean");
        map.put("print", true);
        if("MT".equals(type)) {
            printData = PrintUtils.getInstance().formatPrintDataWithSize((MtOrderDetail) bean, null, null, WaiMaiActivity.this, false);
            btprintData = PrintUtils.getInstance().formatPrintDataWithSize((MtOrderDetail) bean, null, null, WaiMaiActivity.this, true);
            printYunWMData = YunPrintUtils.formatYunPrintDataWM((MtOrderDetail) bean, null, null, this, false);
        }
        if("ELM".equals(type)) {
            printData = PrintUtils.getInstance().formatPrintDataWithSize(null, (ElmOrderBean) bean, null, WaiMaiActivity.this, false);
            btprintData = PrintUtils.getInstance().formatPrintDataWithSize(null, (ElmOrderBean) bean, null, WaiMaiActivity.this, true);
            printYunWMData = YunPrintUtils.formatYunPrintDataWM(null, (ElmOrderBean) bean, null, this, false);
        }
        if("NEW_ELM".equals(type)) {
            printData = PrintUtils.getInstance().formatPrintDataWithSize(null, null, (NewElmOrderBean) bean, WaiMaiActivity.this, false);
            btprintData = PrintUtils.getInstance().formatPrintDataWithSize(null,null,(NewElmOrderBean)bean,WaiMaiActivity.this,true);
            printYunWMData = YunPrintUtils.formatYunPrintDataWM(null,null,(NewElmOrderBean)bean, this, false);
        }
        if(printItems.contains("6")||printItems.contains("7")||btPrintItems.contains("6")||printItems.contains("7")||yunContains()) {
            if(iWoyouService!=null) {
                PrintUtils.getInstance().printWm(iWoyouService,printData,printCallBack);
            }
            judgeBtPrint();
            startYunPrint();
        }else {
            PrintUtils.getInstance().showCanNotPrintDia(WaiMaiActivity.this);
        }
    }
    private  void  startYunPrint() {
        YunPrintUtils.formateWMOrderData(this,printYunWMData);
    }

    private boolean yunContains(){
        List<SavedPrinter> savedPrinters= (List<SavedPrinter>) SaveUtils.getObject(this,Constant.YUN_PRINTERS);
        if(savedPrinters!=null&&savedPrinters.size()>0) {
            for (int i = 0; i < savedPrinters.size(); i++) {
                if(savedPrinters.get(i).getPrintPermission().contains("6")||savedPrinters.get(i).getPrintPermission().contains("7")) {
                    return true;
                }
            }
        }
        return false;
    }


    private void judgeBtPrint() {
        if(btPrintItems.contains("6")||btPrintItems.contains("7")) {
            if(mService.getState()== BluetoothService.STATE_CONNECTED) {
                PrintByBluetooth();
            }else {
                BTPrintUtils.getInstance().connectBtPrinterTest(mService, this, handler);
            }
        }
    }
    /**
     * 蓝牙打印（输入输出流）
     */
    private void PrintByBluetooth() {
       if(mService!=null&&mService.getState()== BluetoothService.STATE_CONNECTED&&btprintData!=null) {
          BTPrintUtils.getInstance().wmBtPrint(mService, btprintData, this,btCallBack);
       }
    }

    /**
     * 滚动监听，屏蔽动画
     */
    class XlistScrollListener implements XListView.OnXScrollListener{
        @Override
        public void onXScrolling(View view) {
        }
        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {
        }
        @Override
        public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            adapter.doAnimation(false);
        }
    }
    /**
     * 绑定饿了么的pupwindow
     */
    public void showClosePup() {
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.close_open_wm, null);
        TextView tv_sure = (TextView) view.findViewById(R.id.tv_sure);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tv_bind_notices = (TextView) view.findViewById(R.id.tv_bind_notices);

        tv_bind_notices.setText(R.string.close_wm_notices);
        popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha=0.4f;
        getWindow().setAttributes(params);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(findViewById(R.id.wm_actitivy), Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1;
                getWindow().setAttributes(params);
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                netWork.switchWmStatus(callBack, 0, 0);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }
}