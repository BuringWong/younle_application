package com.yongle.letuiweipad.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.input.InputManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.activity.LoginActivity;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.domain.printer.SavedPrinter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.MD5;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import okhttp3.Call;

import static android.content.Context.INPUT_SERVICE;

/**
 * Created by 我是奋斗 on 2016/4/1.
 */
public class Utils {

    private static final String TAG = "Utils";
    private static String QUERY_DAY = "query_day";
    private static String QUERY_NUM = "query_num";
    private static Calendar calendar;
    public static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    public static DateFormat df2 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    public static DateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static DateFormat df4 = new SimpleDateFormat("MM月dd日 HH:mm");
    public static DateFormat df5 = new SimpleDateFormat("MM月dd日 HH:mm");
    public static AlertDialog alertDialog_loading;
    public  static DecimalFormat tf=new DecimalFormat("00");
    public  static DecimalFormat numdf=new DecimalFormat("0.00");
    public  static DecimalFormat codedf=new DecimalFormat("00000");

    private static Toast toast;

    public static UsbDevice getUsbDeviceFromName(Context context, String usbName) {
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        HashMap<String,UsbDevice> usbDeviceList = usbManager.getDeviceList();
        return usbDeviceList.get(usbName);
    }

    public static void toast(Context context, String message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    public static boolean judgeIsFirstRemarkPay(final Context context) {
        if((Constant.ACCOUNT_ID).equals(SpUtils.getInstance(context).getString(Constant.REAMRK_PAY_STATEMENT,""))){
            return true;
        }else{
            AlertDialog dia = new AlertDialog.Builder(context)
                    .setMessage(R.string.reamrk_pay_statement)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SpUtils.getInstance(context).save(Constant.REAMRK_PAY_STATEMENT,Constant.ACCOUNT_ID);
                        }
                    })
                    .show();
            dia.setCancelable(false);
            dia.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            });
            return false;
        }
    }
    public static void HideKeyboard(View v){
        InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
        if ( imm.isActive( ) ) {
            imm.hideSoftInputFromWindow( v.getApplicationWindowToken( ) , 0 );

        }
    }
    public static void getWindowSize(Activity context){
        WindowManager windowManager = context.getWindowManager();
        Point point=new Point();
        windowManager.getDefaultDisplay().getSize(point);
        LogUtils.e(TAG,"x=="+point.x);
        LogUtils.e(TAG,"y=="+point.y);

//        int daoHangHeight =getDaoHangHeight(context);
//        int statusBarHeight =getStatusBarHeight(context);
//        LogUtils.e(TAG,"daoHangHeight=="+daoHangHeight);
//        LogUtils.e(TAG,"statusBarHeight=="+statusBarHeight);

    }
    public static void setAlpha(Activity mActivity,float alpha) {
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = alpha;
        mActivity.getWindow().setAttributes(params);
    }
    /**
     * 获取对字符串进行md5加密
     *
     * @param str
     * @return
     */
    public static String getMD5Str(String str) {
        try {
            MessageDigest e = MessageDigest.getInstance("MD5");
            e.update(str.getBytes("UTF-8"));
            byte[] encryption = e.digest();
            StringBuffer strBuf = new StringBuffer();

            for (int i = 0; i < encryption.length; ++i) {
                if (Integer.toHexString(255 & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(255 & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(255 & encryption[i]));
                }
            }

            return strBuf.toString();
        } catch (NoSuchAlgorithmException var5) {
            return "";
        } catch (UnsupportedEncodingException var6) {
            return "";
        }
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dip
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 通用dialog
     *
     * @param context
     * @param canCancel
     * @param id
     * @param textid
     * @param width
     * @param height
     * @param content
     * @return
     */
    public static AlertDialog wybDialog(Context context, boolean canCancel, int id, int textid, int width, int height, String content) {
        View view = View.inflate(context, id, null);
        if (textid != 0) {
            TextView textView = (TextView) view.findViewById(textid);
            textView.setTextSize(px2sp(context, 28));
            textView.setText(content);
        }
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .show();

        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        window.setAttributes(attributes);
        window.setContentView(id);
        window.setLayout(width, height);
        window.setGravity(Gravity.CENTER);
        return alertDialog;
    }

    public static AlertDialog wybDialog2(Context context, boolean canCancel, int id, int width, int height, View.OnClickListener listener, int[] childIds) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .show();
        alertDialog.setCanceledOnTouchOutside(canCancel);
        Window window = alertDialog.getWindow();
        window.setContentView(id);
        window.setLayout(width, height);
        View contentView = View.inflate(context, id, null);
        for (int i = 0; i < childIds.length; i++) {
            Button button = (Button) contentView.findViewById(childIds[0]);
            button.setOnClickListener(listener);
        }
        return alertDialog;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 判断是否为手机号
     *
     * @param inputText
     * @return
     */
    public static boolean isPhone(String inputText) {
       /* Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,0-9]))\\d{8}$");
        Matcher m = p.matcher(inputText);
        return m.matches();*/
        String reg = "[1][34578]\\d{9}";//判断是否为手机号码
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(inputText);
        boolean b = matcher.matches();

        return b;
    }

    public static int getChinaNum(String str) {
        int amount = 0;// 创建汉字数量计数器
        String exp = "^[\u4E00-\u9FA5|\\！|\\,|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]$";
        Pattern pattern = Pattern.compile(exp);
        for (int i = 0; i < str.length(); i++) {// 遍历字符串每一个字符
            char c = str.charAt(i);
            Matcher matcher = pattern.matcher(c + "");
            if (matcher.matches()) {
                amount++;
            }
        }
        return amount;
    }

    /**
     * 判断格式是否为email
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 公司名称不能包含除 , . 空格外其他的标点符号
     *
     * @param name
     * @return
     */
    public static boolean isCompanyName(String name) {
        String regex = "^[\\s\\p{Zs}\\.\\,a-zA-Z0-9\u4E00-\u9FA5]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(name);
        boolean b = match.matches();
        return !b;
    }

    public static boolean containUsbScanner(Context context) {
        InputManager im = (InputManager) context.getSystemService(INPUT_SERVICE);
        int[] devices = im.getInputDeviceIds();
        for (int id : devices) {
            InputDevice device = im.getInputDevice(id);
            if(device!=null&&TextUtils.equals("SM SM-2D PRODUCT HID KBW",device.getName())) {
                return true;
            }
        }
        return false;
    }
    /**
     * 是否可用
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        String expression = "(^(0[0-9]{2,3}\\-)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$)|(^((\\(\\d{3}\\))|(\\d{3}\\-))?(1[358]\\d{9})$)";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 获取今天的日期 格式：yyyy-MM-dd
     *
     * @return
     */
    public static String getToday() {
        calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        String today = df.format(calendar.getTime());
        return today;
    }

    /**
     * 获取指定日期的前一天
     * @return
     */
    public static String getPreDay() {
        Date date = new Date(getCurrentTimeMill());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -3);
        date = calendar.getTime();
        String preDay = df.format(date);
        return preDay;
    }
    /**
     * 获取指定日期的后一天
     * @param date
     * @return
     */
    public Date getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        date = calendar.getTime();
        return date;
    }

    /**
     * 获取当前时间戳
     * @return
     */
    public static long getCurrentTimeMill() {
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTime().getTime();
        return time;
    }

    /**
     * 获取当前的时间戳 返回String格式
     *
     * @return
     */
    public static String getCurrentTime() {
        /*Calendar calendar = Calendar.getInstance();
        long time = calendar.getTime().getTime();
        return time / 1000 + "";*/
        return Long.valueOf(Constant.LAST_REQUEST_TIME)+1+"";
    }

    /**
     * 获取当前的时间戳 返回String格式
     *
     * @return
     */
    public static String getTradeTime() {
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTime().getTime();
        return time / 1000 + "";
    }

    /**
     * 获取当前的时间戳 返回String格式
     *
     * @return
     */
    public static String getCurrentTime1() {
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTime().getTime();
        return time + "";
    }

    /**
     * 调整日期Data为：yyyy-MM-dd 形式
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        String format = df.format(date);
        return format;
    }

    /**
     * 获取本月第一天
     *
     * @return
     */
    public static String getFirstdayofThisMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return df.format(cal.getTime());
    }

    /**
     * 获取当前时间：yyyy年MM月dd日  HH:mm 格式
     *
     * @return
     */
    public static String getCurrentMin() {
        Calendar calendar = Calendar.getInstance();
        Date calendarTime = calendar.getTime();
        String nowTime = df3.format(calendarTime);
        return nowTime;
    }

    /**
     * 根据（long型转化）字符串获取时间
     *
     * @param time
     * @return
     */
    public static String getTime(String time) {
        long longTime = Long.valueOf(time);
        String nowTime = df2.format(longTime);
        return nowTime;
    }

    /**
     * 根据（long型转化）字符串获取时间
     *
     * @param time
     * @return
     */
    public static String getPtintTime(String time) {
        long longTime = Long.valueOf(time);
        String nowTime = df3.format(longTime);
        return nowTime;
    }

    /**
     * 根据long型时间戳获取 yyyy-MM-dd HH:mm 形式时间
     *
     * @param time
     * @return
     */
    public static String getCurrentM(long time) {
        String nowTime = df3.format(time);
        return nowTime;
    }

    /**
     * 获取当前时间 MM月dd日 HH:mm 格式
     *
     * @return
     */
    public static String getCurrentMDF(long time) {
        String nowTime;
        if(time>0){
            nowTime = df5.format(time);
        }else{
            Calendar calendar = Calendar.getInstance();
            Date calendarTime = calendar.getTime();
            nowTime = df5.format(calendarTime);
        }
        return nowTime;
    }
    /*将字符串转为时间戳*/
    public static long getStringToDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try{
            date = sdf.parse(time);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }
    /**
     * 根据（long转换）时间戳转换时间 MM月dd日 HH:mm 格式
     *
     * @return
     */
    public static String getTimeFromLongString(String str_time) {
        Long long_time = Long.valueOf(str_time);
        String nowTime = df4.format(long_time);
        return nowTime;
    }

    /**
     * 获取当前时间 yyyy-MM-dd HH:mm:ss 格式的时间
     *
     * @return
     */
    public static String getNowTime() {
        DateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        Date calendarTime = calendar.getTime();
        String nowTime = df3.format(calendarTime);
        return nowTime;
    }

    /**
     * 根据时间戳获取时间字符窜
     *
     * @param time
     * @return
     */
    public static String getDatetimeStampToString(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date(time);
        return sf.format(d);
    }

    public static String getDatetimeStampToString(String time) {
        Long long_time = Long.valueOf(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date(long_time);
        return sf.format(d);
    }

    /**
     * 随机生成的订单号
     */
    public static String getTradeNum() {
        return Utils.getCurrentTime1() + Constant.ACCOUNT_ID;

    }

    /**
     * 列表关闭的动画
     *
     * @param view
     */
    public static void closeAnimation(View view) {
        TranslateAnimation ta = new TranslateAnimation(0, 0, 0, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1);
        ta.setDuration(1000);
        view.startAnimation(ta);
    }
    public static long lastClickTime;
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        LogUtils.Log("time=="+time);
        long timeD = time - lastClickTime;
        LogUtils.Log("timeD=="+timeD);
        if ( 0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
    /**
     * 从字符串转化到数并保留两位小数
     *
     * @param num
     * @return
     */
    public static String keepTwoDecimal(String num) {
        if (!num.contains("∞")) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");//如果小数不足2位,会以0补足.
            Double a = Double.valueOf(num);
            return decimalFormat.format(a);
        } else {
            return "meaningless";
        }
    }

    public static String formatPrice(double price) {

        if(String.valueOf(price).contains(".")){
            int pointIndex = String.valueOf(price).indexOf(".");
            if(String.valueOf(price).length()-(pointIndex+1)>=3){
                DecimalFormat decimalFormat = new DecimalFormat("0.000");
                String format = decimalFormat.format(price);
                return format;
            }else {
                return dropZero("" + price);
            }
        }else{
            return dropZero("" + price);
        }
    }

    /**
     * 验证token
     *
     * @param context
     * @param json
     * @return
     */
    public static boolean checkSaveToken(final Activity context, String json) {
        boolean token_right = true;
        int code;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            code = jsonObject.getInt("code");
            if (code == 404) {//其他设备登录该账号
                token_right = false;
                String offline = context.getResources().getString(R.string.offline_notice);
                NoticePopuUtils.showOfflinDia(context,offline, new NoticePopuUtils.OnFinishChoose() {
                    @Override
                    public void onClickYes() {
//                        SpUtils.getInstance(context).remove(Constant.PARAMS_NAME_PASSWORDS);
//                        SpUtils.getInstance(context).remove(Constant.PARAMS_NAME_USERACCOUNT);
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.putExtra(Constant.AUTO_LOGIN,false);
                        context.finish();
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
            }else if(code==701) {//版本过低,下载跟新新版本
                token_right=false;
                String url = jsonObject.getString("msg");
                String updateinfo = jsonObject.getString("updateinfo");
                installNewVersion(context,url,updateinfo);
                return false;
            } else if (code == 3003) {//密码被修改
                token_right = false;
                AlertDialog errTokenDia;
                errTokenDia = new AlertDialog.Builder(context)
                        .setMessage(R.string.abnormal_account)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SpUtils.getInstance(context).remove(Constant.PARAMS_NAME_PASSWORDS);
                                SpUtils.getInstance(context).remove(Constant.PARAMS_NAME_USERACCOUNT);
                                context.startActivity(new Intent(context, LoginActivity.class));
                                context.finish();
                            }
                        })
                        .show();
                errTokenDia.setCancelable(false);
                errTokenDia.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        return keyCode == KeyEvent.KEYCODE_BACK;
                    }
                });
            }
            JSONArray auth = jsonObject.getJSONArray("auth");
            if (auth != null && auth.length() > 0) {
                Constant.OPENED_PERMISSIONS.clear();
                for (int i = 0; i < auth.length(); i++) {
                    Constant.OPENED_PERMISSIONS.add((String) auth.get(i));
                }
            }
            getLastRequestTime(context,jsonObject);
        } catch (JSONException e) {
            LogUtils.Log("解析异常：" + e.toString());
            e.printStackTrace();
        }
        return token_right;
    }

    /**
     * 下载并安装新版本
     * @param context
     * @param url
     */
    public static void installNewVersion(final Activity context, final String url, String msg) {
        View updateView = View.inflate(context, R.layout.update_layout, null);
        final ProgressBar pb=ButterKnife.findById(updateView,R.id.pb_progress);
        final TextView tv_title=ButterKnife.findById(updateView,R.id.tv_title);
        final TextView tv_reinstall=ButterKnife.findById(updateView,R.id.tv_reinstall);
        final TextView update_content=ButterKnife.findById(updateView,R.id.update_content);
        update_content.setText(msg);
//        tv_reinstall.setText("立即更新");
        tv_title.setText("正在下载新版本...");
        tv_reinstall.setVisibility(View.GONE);
        pb.setProgress(0);
        ButterKnife.findById(updateView,R.id.tv_title);
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(updateView)
                .show();
        int width = dialog.getWindow().getWindowManager().getDefaultDisplay().getWidth();
        int height = dialog.getWindow().getWindowManager().getDefaultDisplay().getHeight();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width= (int) (width*0.31);
//        params.height= (int) (height*0.41);
        params.height= WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity=Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return true;
            }
        });
        pb.setVisibility(View.VISIBLE);
        OkHttpUtils//
                .get()//
                .url(url)//
                .build()//
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "letuiweipad.apk")//
                {
                    @Override
                    public void inProgress(float progress, long total) {
                        pb.setProgress((int) (progress * 100));
                        if (pb.getProgress() == 100) {
                            pb.setVisibility(View.GONE);
                            tv_reinstall.setVisibility(View.VISIBLE);
                            tv_title.setText("已获取到最新版本，请安装");
                            tv_reinstall.setText("立即安装");
                            tv_reinstall.setEnabled(true);
                            tv_reinstall.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    installApk(context);
                                }
                            });
//                            installApk(context);
                        }
                    }
                    @Override
                    public void onError(Call call, Exception e) {
                        LogUtils.Log("异常" + e);
                        Utils.showToast(context, "连接超时！");
                    }

                    @Override
                    public void onResponse(File file) {

                    }
                });
        /*tv_reinstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

    }
    /**
     * 安装新下载的apk
     */
    public static void installApk(Activity context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/letuiweipad.apk"),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 获取传递的时间戳
     * @param jsonObject
     */
    private static void getLastRequestTime(Context context,JSONObject jsonObject) throws JSONException {
        String returnsys = jsonObject.getString("returnsys");
        if(returnsys!=null&&returnsys.length()>=10) {
            String time10 = returnsys.substring(0, 10);
            String last_request_time="";
            for (int i = 0; i < time10.length(); i++) {
                switch (time10.charAt(i)) {
                    case  'F':
                        last_request_time+="0";
                        break;
                    case  'd':
                        last_request_time+="1";
                        break;
                    case  '%':
                        last_request_time+="2";
                        break;
                    case  'C':
                        last_request_time+="3";
                        break;
                    case  '*':
                        last_request_time+="4";
                        break;
                    case  'Y':
                        last_request_time+="5";
                        break;
                    case  'q':
                        last_request_time+="6";
                        break;
                    case  '#':
                        last_request_time+="7";
                        break;
                    case  'w':
                        last_request_time+="8";
                        break;
                    case  'I':
                        last_request_time+="9";
                        break;
                }
            }
//            Constant.LAST_REQUEST_TIME=last_request_time;
            SpUtils.getInstance(context).save(Constant.LAST_REQUEST_TIME,last_request_time);
        }
    }
    public boolean judgeNet(Context context){
        if (!NetworkUtils.isNetworkAvailable(context)) {
            Utils.showToast(context, context.getResources().getString(R.string.no_network_please_cash_card));
            return false;
        } else {
            return true;
        }
    }
    /**
     * 生成新Token
     *
     * @return
     */
    public static String getToken(String time, Context context) {
//        String last_request_time = SpUtils.getInstance(context).getString(Constant.LAST_REQUEST_TIME, "123");
        String tokenTime;
        if(Constant.login) {
            Constant.login=false;
            tokenTime=time;
        }else {
            Long aLong = Long.valueOf(Constant.LAST_REQUEST_TIME);
             tokenTime=(aLong+1)+"";
        }
        LogUtils.Log("tokenTime=="+tokenTime.toString());
        String pass_words = Constant.PASSWORD;
        if(pass_words==null) {
          pass_words=SpUtils.getInstance(context).getString(Constant.PARAMS_NAME_PASSWORDS, "");
        }
        String MD5Code = MD5.md5(Constant.USER_ACCOUNT + MD5.md5(MD5.md5(pass_words)) + "qm&Gr%rW#12JpZ&hjsfb2GUa" + tokenTime);
        String token = MD5.md5(MD5Code);
        return token;
    }

    /**
     * 判断某个服务是否正在运行的方法
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList==null||myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    /**
     * 获取查询编号
     *
     * @return
     */
    /*public static String getBQueryNum(Context context) {
        Integer return_num = null;
        //判断是否过了一天：存储时间戳 利用格式转换判断天数是否发生变化
        String storage_day = SpUtils.getInstance(context).getString(QUERY_DAY, "");
        if ("".equals(storage_day)) {
            SpUtils.getInstance(context).save(Utils.QUERY_DAY, Utils.getCurrentTime1());
            return_num = 1;
        } else {
            Long day = Long.valueOf(storage_day);
            String storage_day_time = Utils.getDatetimeStampToString(day);//"yyyy-MM-dd HH:mm:ss"
            LogUtils.Log("stroage_day_time==" + storage_day_time);
            String now_day_time = Utils.getDatetimeStampToString(Long.valueOf(Utils.getCurrentTime1()));

            String storage_this_day = storage_day_time.substring(0, 10);
            String now_this_day = now_day_time.substring(0, 10);

            if (storage_this_day.equals(now_this_day)) {
                //获取存储的序号加1
                String query_num = SpUtils.getInstance(context).getString(QUERY_NUM, "");
                if ("".equals(query_num)) {
                    return_num = 1;
                } else {
                    return_num = Integer.valueOf(query_num);
                    return_num += 1;
                }
            } else {
                //本地查询号变为1，更改存储的时间戳
                return_num = 1;
                SpUtils.getInstance(context).save(Utils.QUERY_DAY, Utils.getCurrentTime1());
            }
        }
        SpUtils.getInstance(context).save(QUERY_NUM, return_num + "");
        LogUtils.e("Utils", "本地存储订单：B" + Constant.DEVICE_ADV_NUM + return_num);
        return "B" + Constant.DEVICE_ADV_NUM + return_num;
    }*/

    /**
     * java时间戳转化为PHP时间戳
     *
     * @param timeStamp
     * @return
     */
    public static String javaTransforTimeToPHP(String timeStamp) {
        return Long.valueOf(timeStamp) / 1000 + "";
    }

    /**
     * PHP时间戳转化为java时间戳
     * 精度损失毫秒
     *
     * @param timeStamp
     * @return
     */
    public static String PHPTransforTimeStampToJava(String timeStamp) {
        return timeStamp + "000";
    }

    /**
     * 网络的判断
     *
     * @param activity
     * @return
     */
    public static boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

  /*  public static void showWaittingDialog(Activity mActivity,String content) {
        alertDialog_loading = Utils.wybDialog(mActivity, false, R.layout.waitting_dialog_commit_goods, 0, Utils.dip2px(mActivity, 250), Utils.dip2px(mActivity, 180), "");
        TextView viewById = (TextView) alertDialog_loading.findViewById(R.id.tv_voucher_search);
        viewById.setText(content);
        alertDialog_loading.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        return true;
                }
                return false;
            }
        });
    }*/

   /* public static void dismissWaittingDialog() {
        if (alertDialog_loading != null && alertDialog_loading.isShowing()) {
            alertDialog_loading.dismiss();
        }
    }*/

    /**
     * 初始化ToolBar状态
     *
     * @param activity
     */
    public static void initToolBarState(Activity activity) {
        setTranslucentStatus(true, activity);
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(false);
        tintManager.setTintColor(Color.parseColor("#000000"));
    }

    /**
     * 初始化ToolBar状态
     *
     * @param activity
     */
    public static void initToolBarColor(Activity activity, String color) {
        setTranslucentStatus(true, activity);
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(false);
        tintManager.setTintColor(Color.parseColor(color));
    }

    @TargetApi(19)
    private static void setTranslucentStatus(boolean on, Activity activity) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 限制 toast 多次点击
     * @param context
     * @param content
     */
    public static void showToast(Context context, String content) {

        if (toast != null) {
            toast.setText(content);
            toast.setDuration(Toast.LENGTH_SHORT);
        } else {
            toast = Toast.makeText(context,content,Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public static void showToastLong(Context context, String content) {

        if (toast != null) {
            toast.setText(content);
            toast.setDuration(Toast.LENGTH_LONG);
        } else {
            toast = Toast.makeText(context,content,Toast.LENGTH_LONG);
        }
        toast.show();
    }

    /**
     *
     * @param context
     */
    public static void getScreenHW(Context context) {

        //第一种
        WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        int width =display.getWidth();
        int height=display.getHeight();
        Log.e("width=======", String.valueOf(width));
        Log.e("height=======", String.valueOf(height));

        //第二种
        DisplayMetrics dm=new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        int width2=dm.widthPixels;
        int height2=dm.heightPixels;
        Log.e("width2=======", String.valueOf(width2));
        Log.e("height2=======", String.valueOf(height2));
    }

    /**
     * 从获取的时间上除去秒
     * @param time
     * @return
     */
    public static long getTimeNoSec(long time) {

        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String format_start = parse.format(time);
        long dxTime = 0;
        try {
            dxTime = parse.parse(format_start).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dxTime;
    }

    public static void showToast(final Activity activity, final String word, final long time){
        activity.runOnUiThread(new Runnable() {
            public void run() {
                final Toast toast = Toast.makeText(activity, word, Toast.LENGTH_LONG);
                WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
                int height = wm.getDefaultDisplay().getHeight();
                toast.setGravity(Gravity.CENTER,0,1*height/3);
                toast.show();
                LogUtils.e(TAG,toast.toString());
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        toast.cancel();
                    }
                }, time);
            }
        });
    }

    public static void showToastTop(final Activity activity, final String word, final long time){

        activity.runOnUiThread(new Runnable() {
            public void run() {
                final Toast toast = Toast.makeText(activity, word, Toast.LENGTH_LONG);
                WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
                int height = wm.getDefaultDisplay().getHeight();
                toast.setGravity(Gravity.TOP,0,height/5);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        toast.cancel();
                    }
                }, time);
            }
        });
    }
    public static void showWaittingDialog(Activity mActivity,String content) {
        if(alertDialog_loading!=null&&alertDialog_loading.isShowing()) {
            return;
        }
        alertDialog_loading = Utils.wybDialog(mActivity, true, R.layout.waitting_dialog_commit_goods, 0, Utils.dip2px(mActivity, 400), Utils.dip2px(mActivity, 180), "");
        TextView viewById = (TextView) alertDialog_loading.findViewById(R.id.tv_voucher_search);
        ProgressBar pb_voucher_search = (ProgressBar) alertDialog_loading.findViewById(R.id.pb_voucher_search);
        pbAnimation(mActivity,pb_voucher_search);
        if(TextUtils.isEmpty(content)) {
            viewById.setVisibility(View.GONE);
        }else {
            viewById.setVisibility(View.VISIBLE);
            viewById.setText(content);
        }
        alertDialog_loading.setOnKeyListener(new DialogInterface.OnKeyListener() {
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
    public static void showWaittingDialogNoPb(Activity mActivity,String content) {
        if(alertDialog_loading!=null&&alertDialog_loading.isShowing()) {
            return;
        }
        alertDialog_loading = Utils.wybDialog(mActivity, true, R.layout.waitting_dialog_commit_goods, 0, Utils.dip2px(mActivity, 400), Utils.dip2px(mActivity, 180), "");
        TextView viewById = (TextView) alertDialog_loading.findViewById(R.id.tv_voucher_search);
        ProgressBar pb_voucher_search = (ProgressBar) alertDialog_loading.findViewById(R.id.pb_voucher_search);
        pb_voucher_search.setVisibility(View.GONE);
        pbAnimation(mActivity,pb_voucher_search);
        if(TextUtils.isEmpty(content)) {
            viewById.setVisibility(View.GONE);
        }else {
            viewById.setVisibility(View.VISIBLE);
            viewById.setText(content);
        }
        alertDialog_loading.setOnKeyListener(new DialogInterface.OnKeyListener() {
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

    public static void dismissWaittingDialog() {
        if (alertDialog_loading != null && alertDialog_loading.isShowing()) {
            alertDialog_loading.dismiss();
        }
    }


    /**
     * 获取版本号
     * @param mActivity
     * @return
     */
    public static int  getVersionCode(Context mActivity) {
        try {
            PackageInfo packageInfo = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 显示加载的动画
     */
   /* public static void pbAnimationPro(Context context,ImageView iv, boolean isShow) {
        LogUtils.e(TAG,"pbAnimationPro()");
        AnimationSet ra = (AnimationSet) AnimationUtils.loadAnimation(context, R.anim.loading_rotate);
        ra.setInterpolator(new LinearInterpolator());
        if(iv!=null){
            iv.startAnimation(ra);
            if(isShow){
                iv.setVisibility(View.VISIBLE);
            }
        }
    }*/

    /**
     * 停止动画的显示
     */
    public static void pbAnimationStopPro(ImageView iv,boolean isGone) {
        LogUtils.e(TAG,"pbAnimationStopPro()");
        if(iv!=null){
            iv.clearAnimation();
            if(isGone){
                iv.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 舍零
     * @param price 判断结尾有几个0舍去
     * @return
     */
    public static String dropZero(String price){
        if(price==null) {
            return "";
        }
        int length = price.length();
        if(price.contains(".")){
            for(int i=0;i<length;i++){
                if(price.endsWith("0")){
                    if(price.endsWith(".0")){
                        int length_d = price.length();
                        price = price.substring(0,length_d-2);
                        break;
                    }else{
                        int length_d = price.length();
                        price = price.substring(0,length_d-1);
                    }
                }else {
                    break;
                }
            }
        }
        return price;
    }


    //检查打印服务是否开启：如果开启就让其继续请求订单信息并打印
  /*  public static void checkPrintServiceWork(Context context) {
        if(!Utils.isServiceWork(context,"com.younle.younle624.myapplication.myservice.PrintService")){
            context.startService(new Intent(context, PrintService.class));
            //EventBus.getDefault().post(new AppLetReceiveBean());
        }
    }*/
    public static  void pbAnimation(Context context,View view) {
        AnimationSet ra = (AnimationSet) AnimationUtils.loadAnimation(context, R.anim.loading_rotate);
        ra.setInterpolator(new LinearInterpolator());
        view.startAnimation(ra);
    }
    public static  void transAnimation(View view) {
        TranslateAnimation ta=new TranslateAnimation(Animation.RELATIVE_TO_PARENT,0,Animation.RELATIVE_TO_PARENT,-1,
                Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0);
        ta.setDuration(10000);
        ta.setFillAfter(false);
        view.startAnimation(ta);
    }

    /**
     * 获取查询编号
     * @return
     */
    public static String getBQueryNum(Context context) {
        Integer return_num = null;
        //判断是否过了一天：存储时间戳 利用格式转换判断天数是否发生变化
        String storage_day = SpUtils.getInstance(context).getString(QUERY_DAY, "");
        if ("".equals(storage_day)) {
            SpUtils.getInstance(context).save(Utils.QUERY_DAY, Utils.getCurrentTime1());
            return_num = 1;
        } else {
            Long day = Long.valueOf(storage_day);
            String storage_day_time = Utils.getDatetimeStampToString(day);//"yyyy-MM-dd HH:mm:ss"
            LogUtils.Log("stroage_day_time==" + storage_day_time);
            String now_day_time = Utils.getDatetimeStampToString(Long.valueOf(Utils.getCurrentTime1()));

            String storage_this_day = storage_day_time.substring(0, 10);
            String now_this_day = now_day_time.substring(0, 10);

            if (storage_this_day.equals(now_this_day)) {
                //获取存储的序号加1
                String query_num = SpUtils.getInstance(context).getString(QUERY_NUM, "");
                if ("".equals(query_num)) {
                    return_num = 1;
                } else {
                    return_num = Integer.valueOf(query_num);
                    return_num += 1;
                }
            } else {
                //本地查询号变为1，更改存储的时间戳
                return_num = 1;
                SpUtils.getInstance(context).save(Utils.QUERY_DAY, Utils.getCurrentTime1());
            }
        }
        SpUtils.getInstance(context).save(QUERY_NUM, return_num + "");
        LogUtils.e("Utils", "本地存储订单：B" + Constant.DEVICE_ADV_NUM + return_num);
        return "B" + Constant.DEVICE_ADV_NUM + return_num;
    }

    public static void hideKeyboard(View v) {
        InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
        if ( imm.isActive( ) ) {
            imm.hideSoftInputFromWindow( v.getApplicationWindowToken( ) , 0 );

        }
    }

    public static void getRelativeWH(Activity context, double oriw, double orih, int[] wh) {
        int width = context.getWindow().getWindowManager().getDefaultDisplay().getWidth();
        int height = context.getWindow().getWindowManager().getDefaultDisplay().getHeight();
        double xPercent = oriw / 1920;
        double yPercent = orih / 1080;
        double afterW = width * xPercent;
        double afterH = height * yPercent;
        wh[0]= (int) (afterW);
        wh[1]= (int) (afterH);
    }

    public static void initWebView(Activity context,WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setSupportZoom(true);//支持缩放
        settings.setBuiltInZoomControls(true);  //启用内置缩放装置
        settings.setJavaScriptEnabled(true);    //启用JS脚本
        settings.setUseWideViewPort(true);// 这个很关键
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(false);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());

        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if(mDensity == 120) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else if (mDensity == DisplayMetrics.DENSITY_TV){
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else{
            settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }

        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                LogUtils.Log("页面加载完成了");
                super.onPageFinished(view, url);
            }
            //重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
    }

    /**
     * 初始化本地打印机
     * @param context
     */
    public static void initLocalPrinter(Context context) {
        SavedPrinter savedPrinter = (SavedPrinter) SaveUtils.getObject(context, Constant.LOCAL_PRINTER);
        if(savedPrinter ==null&& (Build.MODEL.contains("t1")||Build.MODEL.contains("S2"))) {
            savedPrinter =new SavedPrinter();
            savedPrinter.setType(0);
            savedPrinter.setPrintGroupName("全部分组");
            savedPrinter.setPrintPermission("234567");
            savedPrinter.setName("本地打印机");
            savedPrinter.setPrintGroupId("0");
            SaveUtils.saveObject(context,Constant.LOCAL_PRINTER,savedPrinter);
            SpUtils.getInstance(context).save(Constant.print_permission,"234567");
        }
    }
}

