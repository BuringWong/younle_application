package com.younle.younle624.myapplication.constant;

import android.os.Build;

import com.younle.younle624.myapplication.domain.AppLetReceiveBean;
import com.younle.younle624.myapplication.domain.orderbean.OrderBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by 我是奋斗 on 2016/6/3.
 * 微信/e-mail:tt090423@126.com
 */
public class Constant {
    public static final String AUTO_LOGIN = "auto_login";
    public static final String LOCAL_PRE_OBO = "local_pre_obo";
    public static final String LOCAL_APPLET_OBO = "local_applet_obo";
    public static final String LOCAL_COMMUNITY_OBO = "local_community_obo";

    public static final String BT_PRE_OBO = "bt_pre_obo";
    public static final String BT_APPLET_OBO = "bt_applet_obo";
    public static final String BT_COMMUNITY_OBO = "bt_community_obo";
    public static final String MERCHANT_COMMONPART = "merchant_common";

    public static String[] bottomData={"收银SaaS系统提供商：门店派","联系电话:400-9600-567",
            "乐推微网址:tui.younle.com"};
    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_STATE_CHANGE = 111;
    public static final int WAIT_BLUETOOTH_OPEN = 112;

    public static final int BLUETOOTH_OPENING = 35;
    public static final int VOUCHER_PAGER = 36;
    public static final int MEMBER_CUSTOMER_CONFIRM = 37;
    public static final int LEFT_RIGHT_FILTERDATA = 38;
    public static final int CHART_ENTITY_LEFT = 39;
    public static final int MEMBER_DIRECT_PAY = 40;
    public static final int MEMBER_CHARGE = 41;
    public static final int MEMBER_CHARGE_PAY = 42;
    public static final int ORDER_PAGER_DETAIL = 43;
    public static final int FINISH_PRINT = 124;
    public static final int ONE_BY_ONE_YUN_PRINT=202;
    public static final int YUN_PRINT_ERROR = 204;

    public static final int YUN_PRINT_FINISH =2 ;

    /**
     * 电子秤相关
     */
    public static final String WEIGHER_NAME = "younlescale";
    public static String SAVED_WEIGH_NAME = "";

    /**
     * 会员卡余额直接扫码收款
     */
    public static final int MEME_PAY_DIC = 44;
    public static final String IS_JIEDAN_OPEN = "is_jiedan_open";
    /**
     * 设置的各个打印权限
     */
    public static final String print_permission = "print_permisson";
    public static final String bt_print_permission = "bt_print_permisson";
    /**
     * 代表该打印数据为顾客联
     */
    public static final String FOR_CUSTOMER = "customer";
    public static final String FOR_MERCHANT = "merchant";
    /**
     * pos订单筛选的物品类型
     */
    public static final String GOOD_TYPE = "good_type";
    public static final int FINIH_ACTIVITY = 46;
    public static final String SAVED_BLUETOOTH_WEIGHER = "saved_bluetooth_weigher";
    public static final String BT_PRINTER_GROUP = "bt_printer_group";
    public static final long ONEBYONE_PRINT_DELAYTIME = 0;
    public static final String LOCAL_PRINTER = "local_printer";
    public static final String BT_PRINTER = "bt_printer";
    public static Double order_price = 0.00;
    public static Double order_goods_num = 0.00;

    public static final String MEMBER_FREE_MONEY = "member_free_money";
    /**
     * "0":商米工程
     * "1":世麦工程
     */
    public static final String APPLICATION_TYPE = "0";
    public static final String WM_PRINT_SERVICE = "com.younle.younle624.myapplication.myservice.PrintService";
    /**
     * 未连接
     */
    public static final int SCANNER_DISCONNECT = 0;
    /**
     * 连接中
     */
    public static final int SCANNER_CONNECTING = 1;
    /**
     * 已连接
     */
    public static final int SCANNER_CONNECTED = 2;
    public static  int VERSION_CODE = 0;


    public static String REST_ID = "0";
    /**
     * 上次请求的时间
     */
    public static  String LAST_REQUEST_TIME = "123";

    public static DecimalFormat numDf = new DecimalFormat("0.00");


    /**
     * 下单页面的会员实体类
     */
    public static final String MEMBER_BEAN ="member_bean" ;
    /**
     * 小票中的会员部分数据
     */
    public static final String MEMBER_DISCOUNT_BEAN = "member_discount_bean";
    public static final String UNPAY_ORDER_ID = "unpay_order_id";

    /**
     * 当前操作订单的query_num
     */
    public static String CURRENT_ORDER_NUM ="";
    /**
     * 当前操作订单的房间号
     */
    public static String CURRENT_ORDER_ROOM ="";
    /**
     * 是否是第一次进入订单详情的唯一标识 用来标识是否自动打印订单
     */
    public static  boolean FIRST_COMMIT = false;
    public static  boolean CHARGE_SUCCESS = false;
    public static int IS_ACTIVE =0;
    public static int DEVICE_ADV_NUM =1;
    public static String PASSWORD;
    //门店品牌
    public static String STORE_P;
    //门店名
    public static String STORE_M;
    //登录账号id
    public static String ACCOUNT_ID;
    public static int USER_ACCOUNT_KIND;

    /**
     * 0:未绑定任何收银通道
     * 1：开通了畅捷通道
     * 2：开通了付临门通道
     */
    public static int UNION_PAY_STATUS = 0;


    /**
     * 门店电话
     */
    public static  String STORE_TEL="未获取" ;
    /**
     * 门店派帐号模块开通权限
     */
    public static List<String> OPENED_PERMISSIONS = new ArrayList<>();
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
     * 0：下单支付
     * 1：直接收银支付
     * 2：会员充值支付
     */
    public static String UNKNOWN_COMMODITY;
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
    public static String DEVICE_MODEL = Build.MODEL;
    //设备名称
    public static String DEVICE_NAME = "商米";
    //是否已经激活过账号
    public static String IS_ALREADY_ACTIVATION = "already_activation";
    //登录
    public static final String PARAMS_NAME_POSTOKEN = "POS_token";
    public static final String PARAMS_NAME_USERACCOUNT = "useraccount";
    public static final String PARAMS_NAME_PASSWORDS = "passwords";
    public static final String PARAMS_NAME_IMEI = "numimei";
    public static final String PARAMS_NAME_VERSIONCODE = "versionCode";
    public static final String PARAMS_NAME_MODEL = "posmod";
    public static final String PARAMS_NAME_DEVICENAME = "devicenm";
    public static final String PARAMS_NAME_TIMEAUTH = "timeauth";
    public static final String PARAMS_NEME_UID = "uid";
    public static final String PARAMS_NEME_ACCOUNT_ID ="accountid" ;
    public static final String PARAMS_NEME_STORE_ID = "storeid";
    public static final String PARAMS_ADV_ID = "adv_id";

    public static final String PARAMS_NEME_PASSDORD_MD5 = "userkey";
    public static final int POS_DATA = 1;
    public static final int SERVICE_DATA = 2;
    public static final int ENTITY_DATA = 3;
    public static final int POS_KINDS = 4;
    public static final int POS_KINDS_DETAIL = 5;
    public static final int ABOUT_VOUCHER = 6;

    public static final int KINDS_CHOOSE = 7;
    public static final String FROME_WHERE = "fromewhere";
    public static final int GOODS_SETTING = 8;
    public static final int SERVICE_SETTING = 9;
    public static final int ROOM_SETTING = 10;
    public static final int MAN_SETTING = 11;
    public static final String KIND_LIST = "kind_list";
    public static final int ORDER_DETAIL = 12;
    public static final int ORDER_DETAIL_ALL = 13;
    public static final int ORDER_PAGER = 14;
    /**
     * 输入代金券后，开始检索
     */
    public static final int START_SEARCH = 15;
    /**
     * 联网检索代金券失败
     */
    public static final int SEARCH_ERROR = 16;
    public static final String VOUCHER_READ_BEAN = "voucher_read_bean";
    public static final String VOUCHER_CARD_BEAN = "voucher_card_bean";
    /**
     * 下单，相应房间bean的传递
     */
    public static final int TOTAL_ACCOUNT = 17;
    public static final int GOOD_DATA = 19;
    public static final int ROOM_DATA = 20;
    /**
     * 订单管理界面，订单数据的打印传递的orderid
     */
    public static final String ORDER_ID = "order_id";
    public static final int ORDER_ENTITY_LEFT = 22;
    public static final int ORDER_SERVICE_LIST_FILTER = 29;
    public static final int DIALOG_DATA = 23;
    public static final int SALEMAN_DATA = 24;
    public static final int FINISH_BIND = 25;
    public static final String ORDER_BEAN = "order_bean";
    public static final int CHART_SERVICE_LIST_FILTER = 30;
    public static final int POS_ORDER_CHOOSE_GOODS = 33;
    public static final int POS_ORDER_CHOOSE_GOODS_KINDS = 34;
    public static final int POS_ORDER_CHOOSE_GOODS_GOODS = 35;
    public static final int POS_ORDER_CHOOSE_CHART = 36;
    public static final int POS_ORDER_CHOOSE_CHART_KINDS = 37;
    public static final int POS_ORDER_CHOOSE_CHART_GOODS = 38;
    public static final int POS_ORDER_CHOOSE_SERVICE = 39;
    public static final int POS_ORDER_CHOOSE_SERVICE_GOODS = 40;
    public static final int POS_ORDER_CHOOSE_SERVICE_KINDS = 41;
    public static final int POS_ORDER_CHOOSE_ENTITY = 42;
    public static final int POS_ORDER_CHOOSE_ENTITY_GOODS = 43;
    public static final int POS_ORDER_CHOOSE_ENTITY_KINDS = 44;
    public static final int KINDS_CHOOSE_RIGHT = 45;
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String GOOD_ID = "good_id";
    public static final String BLUETOOTH_DEVICE = "bluetooth_device";
    public static final String SAVED_BLUETOOTH_SCANNER = "saved_bluetooth_scanner";

    public static final String YUN_PRINTERS = "yun_printer";
    public static final String YUN_PRINTER_PARTNER_ID = "4896";
    public static final String YUN_PRINTER_API_KEY = "9eec450a263e1fc37b42e75407b53e0274b75ff3";

    public static final String CONTAIN_BLUETHOOTH_DEVICE = "contain_bluetooth_device";
    public static final String CHANGE_BLUETOOTH_NAME = "change_bluetooth_name";
    public static final String CHANGE_EDITEXT_INFO = "change_editext_info";
    public static final String WHICH_PRINTER = "which_printer";
    public static final String POS_UNAME = "pos_uname";
    public static final String TELNUMBER = "telnumber";

    public static String ADV_NAME = "";
    public static String SERVICE_UPDATE_FAIL_OR_CASHCARD = "com.younle.younle624.myapplication.myservice.FailOrderService";
    public static OrderBean localOrderBean = new OrderBean();
    public static final int ADD_FROM_DETAILS = 1;
    public static final int ADD_FROM_NORMAl_NUM = 2;
    public static final int ORDER_DETAIL_CHANGE_TOTALFEE = 1;
    public static final int ORDER_ROOM_NO_FEE = 0;
    public static final int ORDER_ROOM_FEE_HOUR = 1;
    public static final int ORDER_ROOM_FEE_DAY = 2;
    public static final int NEW_ORDER_TO_DETAIL = 1;
    public static final int CHANGE_ROOM_TO_DETAIL = 2;
    public static final int JUST_HAS_ROOM_NO_GOODS = 3;
    public static boolean NOT_CHOOSE_ROOM = true;
    /**
     * 订单结账完毕是否finish掉OrderDetailActivity
     */
    public static boolean IS_CLOSE_DETAIL_ACTIVITY = false;
    /**
     * 是否有房间商品
     */
    public static String LAST_ORDER_NO = "";//每次调起支付前需初始化为""
    public static boolean IS_JUST_NOW_OPEN_ORDER = false;
    /**
     * 会员充值实际到账金额
     */
    public static double toaccount;
    public static double currentLeft;
    /**
     * 识别会员时扫到的码，cardid vip_cardi用乐自己存的
     */
    public static String MEMBER_ID;
    /**
     * 是否已经开通外卖版块（登录时获取），支付后更改状态
     */
    public static boolean OPEN_WM =true ;
    public static boolean OPEN_APPLET =false ;
     /**
      * 保存订单的集合
      */
    public static List<Map> wm_data=new ArrayList<>();
    public static List<Map> bt_wm_data=new ArrayList<>();
    public static List<Map> yun_wm_data=new ArrayList<>();

    public static int count=0;
    public static int pre_size=0;
    public static int bt_pre_size=0;
     /**
      * 外卖单是否正在打印中
      */
     public static boolean isPrinting;
     public static boolean bt_isPrinting;
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
     /**
      * 各个外卖平台开通状态
      * 1.美团
      * 2.饿了么
      * 3.百度
      */
     public static List<Integer> WM_STATUS = new ArrayList<>();
    /**
     * 外卖打印记录上次的打印位置
     */
    public static final String PARA_PRE_SIZE = "pre_size";
    public static final String BT_PARA_PRE_SIZE = "bt_pre_size";

    /**
     * 续费的alert
     */
     public static  String WM_PAY_AGAIN_ALERT = "";
    /**
     * 外卖到期或者剩5天的提示
     */
    public static final String ALERT = "alert";

    /**
     * 已绑定的外卖门店名称
     */
    public static String mt_store_name="未获取";
    /**
     * 从哪进入绑定mt页面
     * 0：bandnum
     * 1：accountsetting
        public static int bind_wm_fromwhere=-1;
     */

    public static String elm_store_name="未获取";
    public static String bd_store_name="未获取";
    public static boolean wm_is_dead;
    /**
     * 会员充值后的查询余额二维码
     */
    public static String query_member_left = "";
    public static int RECLEN_TIME = 30;//单位是秒
    public static boolean login=false;
    public static String ever_set_printer="ever_set_printer";
    /**
     * 外卖订单截取，上次的orderid,和保留上次的json
     */
    public static int scanner_state=0;
    public static final int MEMBER_CARDS_VOUCHER = 46;
    public static boolean MESSAGE_ALREADYLOOKED = false;//小程序推的单是否已查看
    public static int PAGER_POSITION = 0;

    //小程序订单数据
    public static List<AppLetReceiveBean> applet_data = Collections.synchronizedList(new ArrayList());//非超限订单数据
    public static List<AppLetReceiveBean> bt_applet_data = Collections.synchronizedList(new ArrayList());//非超限订单蓝牙打印数据
    public static List<AppLetReceiveBean> applet_out_limit_data = Collections.synchronizedList(new ArrayList());//超限订单数据

    public static final int APPLET_INFO_DATA = 47;
    public static final int COMMUNITY_INFO_DATA = 48;

    public static boolean isfinishAddNewAc = false;//是否关闭AddNewDeviceActivity
    public static boolean IS_FIRST_SELECT_TIME = false;
    public static final String REAMRK_PAY_STATEMENT = "reamrk_pay_statement";
    /**
     * 一菜一单顾客联的groupid
     */
    public static final String GROUP_CUSTOMER = "customer";

}
