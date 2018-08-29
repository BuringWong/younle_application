package com.yongle.letuiweipad.constant;

import android.os.Build;

import com.yongle.letuiweipad.domain.createorder.OrderBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Create by 我是奋斗 on2017/4/5 16:17
 * 邮箱：tt090423@126.com
 * 微信：17346512596
 */
public class Constant {
    public static final String TENCENT_MAP_KEY="LQVBZ-XKN3F-KFCJF-JGKM6-TNDUE-FWBG2";
    public static final String TOOLBAR_COLOR="#3f88cd";
    //登录
    public static final String PARAMS_NAME_POSTOKEN = "POS_token";
    public static final String PARAMS_NAME_USERACCOUNT = "useraccount";
    public static final String PARAMS_NAME_PASSWORDS = "passwords";
    public static final String PARAMS_NAME_IMEI = "numimei";
    public static final String PARAMS_NAME_MODEL = "posmod";
    public static final String PARAMS_NAME_DEVICENAME = "devicenm";
    public static final String PARAMS_NAME_TIMEAUTH = "timeauth";
    public static final String PARAMS_NEME_UID = "uid";
    public static final String PARAMS_NEME_ACCOUNT_ID ="accountid" ;
    public static final String PARAMS_NEME_STORE_ID = "storeid";
    public static final String PARAMS_ADV_ID = "adv_id";
    public static final String SAVED_BLUETOOTH_WEIGHER = "saved_bluetooth_weigher";

    public static final String REAMRK_PAY_STATEMENT = "reamrk_pay_statement";
    public static final int UNPAYITEM_TO_UNPAYDETAIL = 11;
    public static final String FP_PICID = "fp_pic_id";
    public static final long FPPIC_UPDATE_TIME = 5000;
    public static final String FP_MEMBER = "fo_member";
    public static final String MEMBER14 = "member14";
    public static final String MEMBER7 = "member7";
    public static final String VOUCHER14 = "vocher14";
    public static final String VOUCHER7 = "vocher7";
    public static final String AUTO_LOGIN = "auto_login";
    public static final CharSequence SM_XS = "SM SM-2D PRODUCT HID KBW";
    public static final String BT_SCANNER_NAME = "bt_scanner_name";
    public static final String FIRST_USE = "first_use";
    public static final String GOOD_SELECTED_RECEIVER = "com.younle.letuiwei.GOOD_SELECTED";
    public static final String GOOD_EXTRA_NAME = "goodBean";
    public static String SCANNER_NAME = null;
    public static boolean XS_IN = false;

    public static String SAVED_WEIGH_NAME = "";
    public static final String WEIGHER_NAME = "younlescale";

//    public static final String WEIGHER_NAME = "doingBLE";
    public static final String TEMP_ORDER = "temp_order";
    public static final int WAIT_BLUETOOTH_OPEN = 112;
    public static final int MESSAGE_STATE_CHANGE = 111;
    public static final int ONE_BY_ONE_YUN_PRINT=113;

    public static final long ONEBYONE_PRINT_DELAYTIME = 0;
    public static boolean IS_FIRST_SELECT_TIME = false;
    public static boolean IS_CLOSE_DETAIL_ACTIVITY = false;
    public static final String LOCAL_PRINTER = "local_printer";
    public static final String BT_PRINTER = "bt_printer";
    public static final String YUN_PRINTERS = "yun_printer";

    /**
     * 代表该打印数据为顾客联
     */
    public static final String FOR_CUSTOMER = "customer";
    public static final String FOR_MERCHANT = "merchant";
    /**
     * 一菜一单顾客联的groupid
     */
    public static final String GROUP_CUSTOMER = "customer";
    public static DecimalFormat numDf = new DecimalFormat("0.00");
    public static final String YUN_PRINTER_PARTNER_ID = "4896";
    public static final String YUN_PRINTER_API_KEY = "9eec450a263e1fc37b42e75407b53e0274b75ff3";
    /**
     * 0:微信支付
     * 1:支付宝支付
     * 2:刷卡支付
     * 3:现金记账
     * 4:会员卡支付
     *
     * 6:刷卡记账
     * 8:微信记账
     * 9:支付宝记账
     */
    public static String payway="-1";
    /**
     * 0：下单支付
     * 1：直接收银支付
     * 2：会员充值支付
     */
    public static String UNKNOWN_COMMODITY="0";
    public static String LAST_ORDER_NO = "";//每次调起支付前需初始化为""
    /**
     * 当前操作订单的query_num
     */
    public static String CURRENT_ORDER_NUM ="";
    /**
     * 当前操作订单的房间号
     */
    public static String CURRENT_ORDER_ROOM ="";
    /**
     * 2[收银点单]
     * 4[会员营销(包含下单-2权限)]
     * 7[外卖]
     * 8[小程序点单(包含下单-2权限)]
     */
    public static List<String> OPENED_PERMISSIONS = new ArrayList<>();
    public static  int VERSION_CODE = 42;
    /**
     * 设置的各个打印权限：
     * 1下单后打印预结单
     * 2预结单中打印商家联 3预结单打印顾客联
     * 4结账小票中打印商家联 5结账小票打印顾客联
     * 6外卖小票中打印商家联 7外卖小票打印顾客联
     * 8小程序点单打印商家联 9小程序点单打印顾客联
     */
    public static final String print_permission = "print_permisson";
    public static final String bt_print_permission = "bt_print_permisson";
    public static final String yun_print_permission = "yun_print_permisson";
    /**
     * 会员充值实际到账金额
     */
    public static double toaccount;
    public static double currentLeft;
    /**
     * 外卖到期或者剩5天的提示
     */
    public static final String ALERT = "alert";
    public static boolean wm_is_dead;
    /**
     * 识别会员时扫到的码，cardid vip_cardi用乐自己存的
     */
    public static String MEMBER_ID;
    /**
     * 上次请求的时间
     */
    public static  String LAST_REQUEST_TIME = "123";
    public static String PASSWORD;
    //门店品牌
    public static String STORE_P;
    //门店名
    public static String STORE_M;
    //登录账号id
    public static String ACCOUNT_ID;
    public static int USER_ACCOUNT_KIND;
    //用户账号
    public static String USER_ACCOUNT;
    // token
    public static final String ACCESS_TOKEN = "POS_token";
    // 品牌主id
    public static String ADV_ID;
    // 门店id
    public static String STORE_ID;
    //设备IMEI
    public static String DEVICE_IMEI;
    //设备型号
//    public static String DEVICE_MODEL = Build.MODEL;
    public static String DEVICE_MODEL = "DESKTOP_"+ Build.MODEL;
    public static String MODEL_NAME = "S2";
    //设备名称
    public static String DEVICE_NAME = "商米";
    public static final String PARAMS_NEME_PASSDORD_MD5 = "userkey";
    public static String ADV_NAME = "";
    public static int IS_ACTIVE =0;
    public static int DEVICE_ADV_NUM =1;
    public static  String STORE_TEL="未获取" ;
    public static final String SAVED_BLUETOOTH_SCANNER = "saved_bluetooth_scanner";
    /**
     * 会员充值后的查询余额二维码
     */
    public static String query_member_left = "";
    public static int RECLEN_TIME = 30;//单位是秒
    public static boolean login=false;
    public static String ever_set_printer="ever_set_printer";

    /**
     * 是否开通微信支付
     * 0：未授权，不可用
     * 1：已授权，可用
     */
    public static int OPEN_WXPAY;
    public static int OPEN_WXPAY_MICRO;
    /**
     * 是否开通阿里支付
     * 0：未授权，不可用
     * 1：已授权，可用
     */
    public static int OPEN_ALIPAY;
    /**
     * 是否开通银联
     * 0：未开通
     * 1：已开通
     */
    public static int OPEN_YL = 0;

    public static Double order_price = 0.00;
    public static Double order_goods_num = 0.00;
    public static OrderBean localOrderBean = new OrderBean();
    public static final int TOTAL_ACCOUNT = 17;
    public static boolean FIRST_COMMIT=false;

    /**
     * 饿了么门店地址
     */
    public static String ELM_STORE_ADD = "";
    /**
     * 饿了么门店电话
     */
    public static String ELM_STORE_TEL = "";
    /**
     * 美团epoiid
     */
    public static String EPOIID ="";
    public static int MANAGER_POSITION=0;
    public static int MAIN_POSITION=0;

    public static final int MESSAGE_UPDATE_PARAMETER = 0x009;

}
