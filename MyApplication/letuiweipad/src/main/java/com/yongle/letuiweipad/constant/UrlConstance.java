package com.yongle.letuiweipad.constant;

/**
 * Created by 我是奋斗 on 2016/5/17.
 * 微信/e-mail:tt090423@126.com
 */
public class UrlConstance {

    public static final String LBaseUrl="http://apis.map.qq.com/ws/geocoder/v1/?location=";
    public static final String SWITCH_PRINT_STATUS = "http://192.168.1.10/index/Index/testPush";
    /**
     * baseurl
     */
//    public static final String BASE_URL1 = "http://192.168.1.44/App3";

//    public static final String BASE_URL1 = "http://test.younle.com/App2";
    public static  final String BASE_URL1 = "http://pos.younle.com/App3";

//    public static  final String BASE_URL1 = "http://192.168.1.22/App2_3";
    /**
     * 外卖测试url
     */
//    public static final String WM_BASE_URL = "http://waimai.younle.com/index/Api/";
//    public static final String WM_BASE_URL1 = "http://waimai.younle.com/Api/Index/";

    /**
     * 外卖测试url
     */
    public static final String WM_BASE_URL = "http://waimai.younle.com/ver2_1/Api/";
    public static final String WM_BASE_URL1 = "http://waimai.younle.com/ver2_1/Index/";

    /**
     * 登录
     */
    public static final  String LOG_IN=BASE_URL1+"/PosLogin/C_User_Login";
    /**
     * 验证手机号是否已注册
     */
    public static final  String SAME_PHONE=BASE_URL1+"/User/samePhone";
    /**
     * 获取验证码
     */
    public static final  String GET_MESSAGE=BASE_URL1+"/User/get_message";
    /**
     * 提交注册
     */
    public static final  String REGISTER_INSERT=BASE_URL1+"/User/register_insert";
    /**
     * POS订单列表
     */
    public static final String POS_LIST = BASE_URL1+"/Order/posList";
    /**
     * POS订单详情
     */
    public static final String POS_DETAIL = BASE_URL1+"/Order/posDetail";
    /**
     * POS订单图表统计
     */
    public static final String POS_CHART= BASE_URL1+"/Order/posChartInfo";
    /**
     * 服务类订单列表
     */
    public static final String SERVER_LIST=BASE_URL1+"/Order/serverList";
    /**
     * 服务类订单详情
     */
    public static final String SERVER_DETAIL=BASE_URL1+"/Order/serverDetail";
    /**
     * 服务类图形列表
     */
    public static final String SERVICE_CHART = BASE_URL1+"/Order/serverChartInfo";
    /**
     * 实物类订单列表
     */
    public static final String ENTITY_LIST=BASE_URL1+"/Order/realList";
    /**
     * 实物类订单详情
     */
    public static final String ENTITY_DETAIL=BASE_URL1+"/Order/realDetail";
    /**
     * 实物类订单图形列表数据
     */
    public static final String ENTITY_CHART=BASE_URL1+"/Order/realChartInfo";
    /**
     * 设置页面的
     */
    //1.商品设置,服务设置
    public static final  String SERVICE_GOOD_SETTING=BASE_URL1+"/GoodsInfo/GoodsInfo";
    //2.房间设置
    public static final  String ROOM_SETTING=BASE_URL1+"/GoodsInfo/roomInfo";
    //3.人员设置
    public static final  String MAN_SETTING=BASE_URL1+"/GoodsInfo/employeeInfo";
    //4.已经设置过的项目
    public static final String SETTING_ITEMS=BASE_URL1+"/GoodsInfo/getSetting";
    //5.更改商品状态
    public static final String CHANGE_SETTING_STATUS=BASE_URL1+"/GoodsInfo/setUsable";
    /**
     * 版本升级
     */
    public static final String VERSION_URL = BASE_URL1+"/Version/check_update";
    /**
     * 撤销支付
     */
    public static final String PAY_WX_CANCEL =BASE_URL1+"/Pay/cancel";//20170220辛巽东弃用
    /**
     * 代金券查询url：http://192.168.2.254/App/VerSystem/search
     */
//    public static final String VOUCHER_READ_URL = BASE_URL1+"/VerSystem/search";
//    public static final String HX_VOUCHER_URL = BASE_URL1+"/VerSystem/verify";
    public static final String VOUCHER_READ_URL = BASE_URL1+"/VerSystemCard/search";//0524xin
    public static final String HX_VOUCHER_URL = BASE_URL1+"/VerSystemCard/verify";//0524xin
    /**
     * 失败订单提交到服务器
     */
    public static final String FAIL_ORDER_SUBMIT = BASE_URL1+"/OrderUpdate/update";
    public static final String CASH_ORDER_SUBMIT = BASE_URL1 + "/Payment/pay";
    /**
     * 退出
     */
    public static final String EXIT_CURRENT =BASE_URL1+"/PosLogin/C_User_Loginout" ;
    /**
     * 查询支付宝，微信绑定状态
     */
    public static final String QUERY_PAY_STATUS =BASE_URL1+"/QueryPayStatus/query" ;
    /**
     * 补充接口：查询订单状态 http://192.168.1.200:8090/App/QueryOrderStatus
     */
    public static final String QUERY_ORDER_STATUS = BASE_URL1 + "/QueryOrderStatus/query" ;

    //1.5版本接口
    /**
     * 未支付订单：
     */
    public static final String UNPAY_ORDER_LISTS = BASE_URL1 + "/NoPayment/orderList";
    /**
     * 未支付订单详情
     */
//    public static final String UNPAY_ORDER_DETAIL = BASE_URL1 + "/NoPayment/orderDetail";
    public static final String UNPAY_ORDER_DETAIL = BASE_URL1 + "/NoPayment/orderDetails";//0524xin
    /**
     * 房间信息返回
     */
    public static final String ROOMS_INFO = BASE_URL1 + "/RoomOrder/RoomList";
    /**
     * 更多的计费规则
     */
    public static final String ROOMS_COST_RULE_MORE = BASE_URL1 + "/RoomOrder/RoomCostRuleMore";
    /**
     *  更换房间
     */
    public static final String COMMIT_CHANGE_ROOM = BASE_URL1 + "/RoomOrder/Order_Update";
    /**
     * 确认选择房间
     */
    public static final String ROOM_IS_AVAILABLE = BASE_URL1 + "/RoomOrder/Room_Order_info";
    /**
     * 确认选择房间
     */
    public static final String ROOM_HAS_CHOOSED = BASE_URL1 + "/RoomOrder/RoomOrderAdd";
    /**
     * 取消房间的选择
     */
    public static final String ROOM_ORDER_CANCEL = BASE_URL1 + "/RoomOrder/Room_Cancel";
    /**
     * 商品信息
     */
    public static final String GOODS_INFO = BASE_URL1 + "/Order/getStoreGoods";
    /**
     * 提交订单
     */
    public static final String COMMIT_ORDER = BASE_URL1 + "/Order/commitOrder";
    /**
     * 查询是否设置房间、商品
     */
    public static final String QUERRY_GOODS_ROOM = BASE_URL1 + "/NoPayment/qureyRoomsGoods";
    /**
     * 获取会员信息的接口
     */
//    public static final String QUERY_MEMBERINFO = BASE_URL1 + "/RoomOrder/Card_Info";
    //public static final String QUERY_MEMBERINFO = BASE_URL1 + "/RoomOrder/CardInfo";//0524xin
    //public static final String QUERY_MEMBERINFO = BASE_URL1 + "/RoomOrder/card_coupon";//0801xin
    public static final String QUERY_MEMBERINFO = BASE_URL1 + "/RoomOrder/card_coupon";//0801xin
    public static final String NEW_QUERY_MEMBERINFO = BASE_URL1 + "/Vipinfo/card_coupon";//新的会员识别
    /**
     * 查询智慧门店是否开通
     */
    public static final String QUERY_MEMBER_IS_OPEN = BASE_URL1 + "/RoomOrder/check_store_status";
    /**
     * 查询时间范围
     */
    public static final String QUERRY_START_END_TIME = BASE_URL1 + "/RoomOrder/Room_Order_Timeinfo";
    /**
     * 修改开始结束时间
     */
    public static final String MODIFY_START_END_TIME = BASE_URL1 + "/RoomOrder/Order_TimeUpdate";
    /**
     * 修改上商品数量
     */
    //public static final String MODIFY_GOODS_NUM = BASE_URL1 + "/NoPayment/updateOrderGoodsNum";
    public static final String MODIFY_GOODS_NUM = BASE_URL1 + "/NoPayment/updateOrderInfo";
    /**
     * 修改总订单价格
     */
    public static final String MODIFY_ORDER_PRICE = BASE_URL1 + "/NoPayment/changePrice";
    /**
     * 下单结账
     */
    //public static final String PAY_ORDER_TOTALFEE = BASE_URL1 + "/Pay/pay";
//    public static final String PAY_ORDER_TOTALFEE = BASE_URL1 + "/PosPay/Pay";
    //public static final String PAY_ORDER_TOTALFEE = BASE_URL1 + "/PosMicroPay/pay";//2.4.0包以后的接口
    public static final String PAY_ORDER_TOTALFEE = BASE_URL1 + "/PosPayShiMaiCard/pay";////0524xin
//    public static final String PAY_ORDER_TOTALFEE = BASE_URL1 + "/PosPayTest/pay";////0831xin测试
    /**
     * 撤销支付订单
     */
    public static final String CANCEL_THIS_ORDER = BASE_URL1 + "/Pay/order_cancel";
    //引导开通会员营销功能
    /**
     * 获取支付宝微信二维码
     */
    public static final String GET_WX_ZFB_CODE = BASE_URL1 + "/PayScan/GetCode_topay";
    /**
     *  兑换码升级
     */
    public static final String REDEEM_CODE_TO_UPRADE = BASE_URL1 + "/PayScan/UseCode_tochange";
    /**
     *  获取版块价格
     */
    public static final String GET_MODULE_PRICE = BASE_URL1 + "/PayScan/GetCom_How";

    /**
     * 外卖获取并绑定epoiid
     */
    public static final String WM_GET_EPIIID_BIND = WM_BASE_URL+"get_ePoiId";
    /**
     * 美团更换门店产生新的epoiid
     */
    public static final  String CREATE_NEW_EPOIID=WM_BASE_URL+"create_new_ePoiId";
    /**
     * 获取饿了么授权链接
     *     public static final String WM_BASE_URL = "http://waimai.younle.com/Api/Api/ele_auth_link";
     */
    public static final  String GET_ELM_AUTHURL=WM_BASE_URL1+"ele_auth_link";
    /**
     * 查询门店账号绑定外卖店的情况，data[]:
     * 1：美团
     * 2：饿了么
     * 3：百度
     */
    public static final  String WM_STATUS=WM_BASE_URL+"store_status";
    /**
     * 商家门店地址和商家电话
     * http://waimai.younle.com/Api/Api/ele_restinfo
     */
    public static final  String ELM_ADD_TEL=WM_BASE_URL1+"Api/ele_restinfo";
    /**
     * 重新请求超限订单
     * http://waimai.younle.com/index/Api/direct_get_orderinfo
     */
    public static final  String REGET_ORDER_INFO=WM_BASE_URL+"direct_get_orderinfo";
    /**
     * 重新请求超限订单
     * http://waimai.younle.com/Api/Index/direct_get_orderinfo
     */
    public static final  String REGET_NEWELM_INFO=WM_BASE_URL1+"direct_get_orderinfo";
    /**
     * 绑定外卖所需要的销售信息
     */
    public static final  String BIND_WM_SALERINFO=BASE_URL1+"/PayScan/Get_advSale";

    /**
     * 外卖获取并绑定epoiid
     */
    public static final String CHANGE_WM_STATUS = WM_BASE_URL+"orderReceive";
    /**
     * 退款接口
     */
    public static final String REFUND = BASE_URL1+"/Order/refund";
    public static final String RE_REFUND = BASE_URL1+"/Order/markRefund";

//    public static final String UPDATE_LOG ="http://192.168.1.10/index" ;

    //    http://test.younle.com/App2/upload
    public static final String UPDATE_LOG =BASE_URL1+"/Order/upload" ;

    /**
     * 获取打印汇总数据接口
     */
    public static final String REQUEST_TOTAL_PRINT = BASE_URL1+"/OrderQuery/index";
    /**
     * 提交订单备注
     */
    public static final String UPDATE_ORDER_MARK =BASE_URL1+"/OrderQuery/set_order_info" ;

    /**
     * 获取小程序订单数据
     */
    public static final String GET_APPLET_LIST_DATA = BASE_URL1 + "/Order/wxappList" ;//applet列表数据

    /**
     * 更新权限
     */
    public static final String UPDATE_AUTH =BASE_URL1+"/PayScan/getauth" ;

    /**
     * 云打印接口
     */
    //public static final String YUN_PRINT =BASE_URL1+"/Print/printer" ;
    public static final String YUN_PRINT = "http://test.younle.com/App2/Print/printer" ;
    /**
     * 验证退款密码接口
     */
    public static final String CONFIRM_REFUND_PASSWORD = BASE_URL1 + "/Order/checkPwd";
    /**
     * 获取储值权限的接口
     */
    public static final String CHECK_RECHARGE = BASE_URL1+"/Order/checkRecharge";
    /**
     * 取消订单优惠券接口
     */
    public static final String DEL_ORDER_VOUCHER = BASE_URL1+"/NoPayment/clearOrderCoupons";
    /**
     * 获取副屏广告图片的接口
     */
    public static final String GET_BANNER = BASE_URL1+"/DeskBanner/getBannerImg";
    /**
     * 当前账号的商品分组信息
     */
    public static final String GOODS_SPLIT_INFO =BASE_URL1+ "/Order/getGoodsGroup" ;
    /**
     * 提交退款订单
     */
    public static final String COMMIT_REFUND_ORDER = BASE_URL1 + "/Order/commitRefundOrder";
}
