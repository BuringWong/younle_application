package com.yongle.letuiweipad.utils.printmanager;


import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.domain.AppLetReceiveBean;
import com.yongle.letuiweipad.domain.PosPrintBean;
import com.yongle.letuiweipad.domain.PrintTotalBean;
import com.yongle.letuiweipad.domain.WmPintData;
import com.yongle.letuiweipad.domain.createorder.DiscountInfo;
import com.yongle.letuiweipad.domain.createorder.GoodBean;
import com.yongle.letuiweipad.domain.createorder.OrderBean;
import com.yongle.letuiweipad.domain.createorder.UnPayDetailsBean;
import com.yongle.letuiweipad.domain.printer.SavedPrinter;
import com.yongle.letuiweipad.domain.printer.YunPrintBean;
import com.yongle.letuiweipad.domain.printer.YunPrintGroupBean;
import com.yongle.letuiweipad.domain.waimai.ElmOrderBean;
import com.yongle.letuiweipad.domain.waimai.MtOrderDetail;
import com.yongle.letuiweipad.domain.waimai.NewElmOrderBean;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.NetWorks;
import com.yongle.letuiweipad.utils.SaveUtils;
import com.yongle.letuiweipad.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yongle.letuiweipad.constant.Constant.FOR_MERCHANT;
import static com.yongle.letuiweipad.constant.Constant.STORE_TEL;

/**
 * Created by bert_dong on 2017/7/27 0027.
 * 邮箱：18701038771@163.com
 */

public class YunPrintUtils {

    private static final String TAG = "YunPrintUtils";
    private static final int LINE_SIZE_K4_32 = 32;
    private static final int LINE_SIZE_K4_24 = 24;
    private static final int LINE_SIZE_K4_16 = 16;
    static final int size27=27;
    static final int size35=35;
    static final int size40=43;
    static final int size50=50;
    private static String line32 = "--------------------------------";
    private static String line27="--------------------------------";
    public static DecimalFormat numDf = new DecimalFormat("0.00");
    public static DateFormat df4 = new SimpleDateFormat("MM月dd日 HH:mm");

    /**
     * 结完账详情页(包括：小程序订单详情页、结完账订单详情页、会员充值详情页) 数据
     * @param context
     * @param posBean
     * @param discountInfo
     * @param fromWhere
     * @param isWxapp
     */
    public static List<YunPrintGroupBean> formateBillDetailData(Context context, PosPrintBean posBean, DiscountInfo discountInfo, int fromWhere, String isWxapp){
        List<WmPintData> data = new ArrayList<>();
        ArrayList<YunPrintGroupBean> oneByOneYunList=new ArrayList<>();
        formatAppletDetailData(data,posBean,discountInfo,oneByOneYunList);
        return oneByOneYunList;

    }

    public static void printBillDetailNormalOrder(Context context,Map<String,List<WmPintData>> map){
        List<WmPintData> cData = map.get(Constant.FOR_CUSTOMER);
        List<WmPintData> mData = map.get(FOR_MERCHANT);
        String cstr = getYunLineData(cData,true,0);
        String mstr = getYunLineData(mData,true,0);
        HashMap<String,String> printData=new HashMap<>();
        printData.put(Constant.FOR_CUSTOMER,cstr);
        printData.put(FOR_MERCHANT,mstr);
        YunToPrintNew(context,printData,"5","4");
    }

    /**
     * 分订单详情页数据
     * @param noGroupData 有商品分组商品
     */
    public static List<YunPrintGroupBean> formateSplitOrderDetailData(List<String> noGroupData, UnPayDetailsBean bean, OrderBean branchBean,boolean total){
        List<YunPrintGroupBean> list=new ArrayList<>();

        //1.格式化顾客联数据2.格式化商家联数据
        String yunPrintData = "";
        //判断云打印权限
        yunPrintData = formateYunDataForOrderDetail(yunPrintData,noGroupData,0);
        yunPrintData += "\n\n";
        list.add(0,new YunPrintGroupBean(yunPrintData,Constant.GROUP_CUSTOMER));

        formatOneByOneYunPrintData(bean,branchBean,list,total);
        return list;
        /*if(!"".equals(yunPrintData)){
            YunToPrint(context,yunPrintData);
        }*/
    }

    /**
     * 预结单的一菜一单商家联
     * @param orderBean
     * @param printList
     */
    public static  void formatOneByOneYunPrintData(UnPayDetailsBean orderBean,OrderBean branchBean, List<YunPrintGroupBean> printList,boolean total){
        //3.添加数据
        List<GoodBean> goodList=null;
        List<UnPayDetailsBean.MsgBean.OrderGoodsBean> order_goods=null;
        if(total&&orderBean!=null) {
            order_goods=orderBean.getMsg().getOrder_goods();
        }else if(branchBean!=null){
            goodList=branchBean.getGoodList();
        }

        if(total) {
            if(order_goods==null||order_goods.size()<=0) {
                return;
            }
        }else {
            if(goodList==null||goodList.size()<=0) {
                return;
            }
        }
        if(total) {
            for (int i = 0; i < order_goods.size() ; i++) {
                String printMsg="";
                printMsg+= typefacesize3(order_goods.get(i).getGoods_name())+"\n";
                printMsg+= typefacesize3("数量:"+ Utils.dropZero(order_goods.get(i).getGoods_num()+""))+"\n";
                printMsg=addOrderOrRoomDesk(printMsg,orderBean.getMsg().getOrder_rooms());
                if(orderBean.getMsg().getRemark()!=null&&!TextUtils.isEmpty(orderBean.getMsg().getRemark())) {
                    printMsg+=typefacesize3("备注:"+orderBean.getMsg().getRemark())+"\n";
                }
                if(orderBean.getMsg().getAddtime()!=null){
                    printMsg+="下单时间:" + Utils.getPtintTime(orderBean.getMsg().getAddtime() + "000")+"\n";
                }else{
                    printMsg+="下单时间:" + Utils.getCurrentMin()+"\n";
                }
                printMsg+="\n";
                printMsg+=line32;
                printMsg+="\n\n";
                printList.add(new YunPrintGroupBean(printMsg,order_goods.get(i).getGroup_id()));
            }
        }else {
            for (int i = 0; i < goodList.size() ; i++) {
                String printMsg="";
                printMsg+= typefacesize3(goodList.get(i).getGoodsName())+"\n";
                printMsg+= typefacesize3("数量:"+ Utils.dropZero(goodList.get(i).getGoodsNum()+""))+"\n";
                printMsg=addOrderOrRoomDesk(printMsg,orderBean.getMsg().getOrder_rooms());
                if(orderBean.getMsg().getRemark()!=null&&!TextUtils.isEmpty(orderBean.getMsg().getRemark())) {
                    printMsg+=typefacesize3("备注:"+orderBean.getMsg().getRemark())+"\n";
                }
                if(orderBean.getMsg().getAddtime()!=null){
                    printMsg+="下单时间:" + Utils.getPtintTime(orderBean.getMsg().getAddtime() + "000")+"\n";
                }else{
                    printMsg+="下单时间:" + Utils.getCurrentMin()+"\n";
                }
                printMsg+="\n";
                printMsg+=line32;
                printMsg+="\n\n";
                printList.add(new YunPrintGroupBean(printMsg,goodList.get(i).getGroupId()));
            }
        }

    }
    /**
     * 小程序推单的一菜一单商家联
     * @param orderBean
     * @param printList
     */
    public static  void formatOneByOneYunPrintData(AppLetReceiveBean orderBean, List<YunPrintGroupBean> printList){
        //3.添加数据
        List<AppLetReceiveBean.GoodsInfoBean> goodList = orderBean.getGoodsInfo();
        //订单序号
        String orderNo;
        if(orderBean.getRoomName()!=null&&!TextUtils.isEmpty(orderBean.getRoomName())) {
            orderNo=orderBean.getRoomName();
        }else {
            orderNo="订单序号："+orderBean.getDayOrderNum();
        }
        //时间
        String addTime;
        if(orderBean.getAddTime()!=null){
            addTime="下单时间:" + orderBean.getAddTime()+"\n";
        }else{
            addTime="下单时间:" + Utils.getCurrentMin()+"\n";
        }
        if(goodList!=null&&goodList.size()>0) {
            for (int i = 0; i < goodList.size() ; i++) {
                String printMsg="";
                printMsg+= typefacesize3(goodList.get(i).getName())+"\n";
                if(goodList.get(i).getSize_name()!=null&&!TextUtils.isEmpty(goodList.get(i).getSize_name())) {
                    printMsg+= typefacesize3("规格:"+goodList.get(i).getSize_name())+"\n";
                }
                printMsg+= typefacesize3("数量:"+Utils.dropZero(goodList.get(i).getNum()+""))+"\n";
                printMsg+=typefacesize3(orderNo)+"\n";
                printMsg+=addTime+"\n";
                printMsg+="\n"+line32;
                printMsg+="\n\n";
                printList.add(new YunPrintGroupBean(printMsg,goodList.get(i).getGroup_id()));
            }
        }
    }
    /**
     * 小程序历史订单一菜一单商家联
     * @param orderBean
     * @param printList
     */
    public static  void formatOneByOneYunPrintData(PosPrintBean orderBean, List<YunPrintGroupBean> printList){
        //3.添加数据
        List<PosPrintBean.MsgBean.GoodsInfoBean> goodList = orderBean.getMsg().getGoodsInfo();
        //订单序号
        String orderNo;
        if(orderBean.getMsg().getRoomName()!=null&&!TextUtils.isEmpty(orderBean.getMsg().getRoomName())) {
            orderNo=orderBean.getMsg().getRoomName();
        }else {
            orderNo="订单序号："+orderBean.getMsg().getDayOrderNum();
        }
        //时间
        String addTime;
        if(orderBean.getMsg().getAddTime()!=null){
            addTime="下单时间:" + orderBean.getMsg().getAddTime()+"\n";
        }else{
            addTime="下单时间:" + Utils.getCurrentMin()+"\n";
        }
        if(goodList!=null&&goodList.size()>0) {
            for (int i = 0; i < goodList.size() ; i++) {
                String printMsg="";
                printMsg+= typefacesize3(goodList.get(i).getName())+"\n";
                if(goodList.get(i).getSize_name()!=null&&!TextUtils.isEmpty(goodList.get(i).getSize_name())) {
                    printMsg+= typefacesize3("规格:"+goodList.get(i).getSize_name())+"\n";
                }
                printMsg+= typefacesize3("数量:"+Utils.dropZero(goodList.get(i).getNum()+""))+"\n";
                printMsg += typefacesize3(orderNo) + "\n";
                printMsg+=addTime+"\n";
                printMsg+="\n"+line32;
                printMsg+="\n\n";
                printList.add(new YunPrintGroupBean(printMsg,goodList.get(i).getGroup_id()));
            }
        }
    }
    /**
     * 有房间桌台就添加房间桌台，都没有的话就添加订单序号
     * @param printMsg
     * @param order_rooms
     */
    public static  String addOrderOrRoomDesk(String printMsg, List<UnPayDetailsBean.MsgBean.OrderRoomsBean> order_rooms) {
        String addMsg="";
        if("".equals(Constant.CURRENT_ORDER_ROOM)) {
            addMsg="订单序号：" + Constant.CURRENT_ORDER_NUM+"\n";
        }else {
            //1代表桌台 0代表房间
            if(order_rooms.get(0).getRoom_type()!=null){
                if("1".equals(order_rooms.get(0).getRoom_type())){
                    addMsg="桌台:"+Constant.CURRENT_ORDER_ROOM+"\n";
                }else{
                    addMsg="房间:"+Constant.CURRENT_ORDER_ROOM+"\n";
                }
            }else{
                addMsg="房间/桌台:"+Constant.CURRENT_ORDER_ROOM+"\n";
            }
        }
        printMsg+=typefacesize3(addMsg);
        return printMsg;
    }

    /**
     * 外卖 云打印机数据并调用云打印接口进行打印
     * @param context
     * @param printMap
     */
    public static void formateWMOrderData(Context context, Map printMap){
        List<WmPintData> merchant = (List<WmPintData>) printMap.get(FOR_MERCHANT);
        List<WmPintData> customer = (List<WmPintData>) printMap.get(Constant.FOR_CUSTOMER);
        HashMap<String,String> map=new HashMap<>();
        String merchantStr = getYunLineData(merchant,true,0);
        String customerStr = getYunLineData(customer,true,0);
        map.put(FOR_MERCHANT,merchantStr);
        map.put(Constant.FOR_CUSTOMER,customerStr);
        YunToPrintNew(context,map,"7","6");
    }

    /**
     * 小程序 来单自动打印
     * @param context
     * @param yunPrintData
     */
    public static void formateAppletOrder(Context context, List<WmPintData> yunPrintData){
       /* LogUtils.e(TAG,"小程序 yunPrintData.tojson="+(new Gson().toJson(yunPrintData)));
        String yunLineData = getYunLineData(yunPrintData,true,0);
        if(!"".equals(yunLineData)){
            YunToPrint(context,yunLineData);
        }*/
    }

    /**
     * 直接收银 打印数据
     */
    public static void formateDirPay(Context context, List<String> headerData, List<String> memberData){

        HashMap<String,String> yunData=new HashMap<>();
        //1.格式化顾客联数据2.格式化商家联数据
        String customerData = "";
        String merchantData = "";

        customerData = formateYunDataForCS(customerData,headerData,memberData,0);
        customerData += "\n\n";
        yunData.put(Constant.FOR_CUSTOMER,customerData);
        merchantData = formateYunDataForCS(merchantData,headerData,memberData,1);
        yunData.put(FOR_MERCHANT,merchantData);
        YunToPrintNew(context,yunData,"5","4");
    }

    public static Map<String,List<WmPintData>> formatPosDetailData(PosPrintBean posBean, DiscountInfo discountBean) {
        Map<String,List<WmPintData>> map=new HashMap<String,List<WmPintData>>();
        List<WmPintData> cData=new ArrayList<>();
        List<WmPintData> mData=new ArrayList<>();
        textSize=27;
        posDetailCommonPart(cData, posBean, true, discountBean);
        if(discountBean!=null) {
            totalPrintMemberTest(discountBean, cData);
        }else {
            wmCenterFormat("-","-", cData,size27);
        }
        cData.add(new WmPintData("收银SaaS系统提供商：门店派", size27, WmPintData.LEFT));
        cData.add(new WmPintData("联系电话：400-9600-567", size27, WmPintData.LEFT));
        cData.add(new WmPintData("乐推微网址：tui.younle.com", size27, WmPintData.LEFT));
        cData.add(new WmPintData(size27,line32));
        cData.add(new WmPintData(size27, "顾客联"));

        mData.add(new WmPintData(size27, "\n"));
        posDetailCommonPart(mData, posBean, false, discountBean);
        if(!line32.equals(mData.get(mData.size()-1).getMsg())) {
            mData.add(new WmPintData(line32,size27,WmPintData.CENTER));
        }
        mData.add(new WmPintData("商家联",size27,WmPintData.LEFT));
        map.put(Constant.FOR_CUSTOMER,cData);
        map.put(FOR_MERCHANT,mData);
        return map;
        /*if(printItems.contains("5")) {
            posDetailCommonPart(data, posBean, true, discountBean);
            if(discountBean!=null) {
                totalPrintMemberTest(discountBean, data);
            }else {
                wmCenterFormat("-","-", data,size27);
            }
            data.add(new WmPintData("收银SaaS系统提供商：门店派", size27, WmPintData.LEFT));
            data.add(new WmPintData("联系电话：400-9600-567", size27, WmPintData.LEFT));
            data.add(new WmPintData("乐推微网址：tui.younle.com", size27, WmPintData.LEFT));
            data.add(new WmPintData(size27,line32));
            data.add(new WmPintData(size27, "顾客联"));
            if(printItems.contains("4")) {
                data.add(new WmPintData(size27, "\n"));
                posDetailCommonPart(data, posBean, false, discountBean);
                if(!line32.equals(data.get(data.size()-1).getMsg())) {
                    data.add(new WmPintData(line32,size27,WmPintData.CENTER));
                }
                data.add(new WmPintData("商家联",size27,WmPintData.LEFT));
            }
        }else {
            if(printItems.contains("4")) {
                posDetailCommonPart(data, posBean, false, discountBean);
                if(!line27.equals(data.get(data.size()-1).getMsg())) {
                    data.add(new WmPintData(line27,size27,WmPintData.CENTER));
                }
                data.add(new WmPintData("商家联", size27, WmPintData.LEFT));
            }else {
                data.clear();
            }
        }*/
    }
    private static void posDetailCommonPart(List<WmPintData> data, PosPrintBean posBean, boolean needPhone, DiscountInfo discountBean) {
        data.add(new WmPintData(Constant.STORE_P + Constant.STORE_M, size27, WmPintData.CENTER));
        if (needPhone) {
            data.add(new WmPintData("电话:" + STORE_TEL, size27, WmPintData.CENTER));
        }
        String dayOrderNum = posBean.getMsg().getDayOrderNum();
        if("会员充值".endsWith(dayOrderNum)) {
            data.add(new WmPintData(dayOrderNum,size50,WmPintData.CENTER));
        }else {
            String isRefund = posBean.getMsg().getIsRefund();
            if ("1".endsWith(isRefund)) {//退款订单
                /*if (posBean.getMsg().getRoomName() != null && !TextUtils.isEmpty(posBean.getMsg().getRoomName())) {
                    data.add(new WmPintData(posBean.getMsg().getRoomName(), size35, WmPintData.CENTER));
                } else {
                    if (dayOrderNum.contains("（已退款）")) {
                        dayOrderNum = dayOrderNum.substring(0, dayOrderNum.indexOf("（已退款）"));
                    }
                    data.add(new WmPintData("订单序号：" + dayOrderNum, size50, WmPintData.CENTER));
                }
                data.add(new WmPintData("（已退款）", size50, WmPintData.CENTER));*/

                boolean isRemark=false;
                if (posBean.getMsg().getRoomName() != null && !TextUtils.isEmpty(posBean.getMsg().getRoomName())) {
                    data.add(new WmPintData(posBean.getMsg().getRoomName(), size35, WmPintData.CENTER));
                } else {
                    if (dayOrderNum.contains("（已退款）")) {
                        dayOrderNum = dayOrderNum.substring(0, dayOrderNum.indexOf("（已退款）"));
                    }
                    if (dayOrderNum.contains("（已标记退款）")) {
                        isRemark=true;
                        dayOrderNum = dayOrderNum.substring(0, dayOrderNum.indexOf("（已标记退款）"));
                    }
                    data.add(new WmPintData("订单序号：" + dayOrderNum, size50, WmPintData.CENTER));
                }
                if(isRemark){
                    data.add(new WmPintData("（已标记退款）", size50, WmPintData.CENTER));
                }else{
                    data.add(new WmPintData("（已退款）", size50, WmPintData.CENTER));
                }
            } else {//非退款订单
                if (posBean.getMsg().getRoomName() != null && !TextUtils.isEmpty(posBean.getMsg().getRoomName())) {
                    data.add(new WmPintData(posBean.getMsg().getRoomName(), size35, WmPintData.CENTER));
                } else {
                    if (dayOrderNum.contains("（已退款）")) {
                        dayOrderNum = dayOrderNum.substring(0, dayOrderNum.indexOf("（已退款）"));
                    }
                    data.add(new WmPintData("订单序号：" + dayOrderNum, size50, WmPintData.CENTER));
                }
            }
        }
        data.add(new WmPintData(size27, line27));
        data.add(new WmPintData("下单时间:" + posBean.getMsg().getAddTime(), size27, WmPintData.LEFT));
        data.add(new WmPintData("结账时间:" + posBean.getMsg().getPayTime(), size27, WmPintData.LEFT));
        data.add(new WmPintData("应付金额:" + posBean.getMsg().getShouldPayMoney() + "元", size27, WmPintData.LEFT));
        data.add(new WmPintData("实付金额:" + posBean.getMsg().getFactPayMoney() + "元", size27, WmPintData.LEFT));
        if(needPhone){
            String msg = posBean.getMsg().getPayType();
            switch (posBean.getMsg().getPayType()){
                case "刷卡收款(记账)":
                    msg = "刷卡收款";
                    break;
                case "现金收款(记账)":
                    msg = "现金收款";
                    break;
                case "微信收款(记账)":
                    msg = "微信收款";
                    break;
                case "支付宝收款(记账)":
                    msg = "支付宝收款";
                    break;
            }
            data.add(new WmPintData("支付方式:" + msg, size27, WmPintData.LEFT));
        }else{
            data.add(new WmPintData("支付方式:" + posBean.getMsg().getPayType(), size27, WmPintData.LEFT));
        }
        //data.add(new WmPintData("支付方式:" + posBean.getMsg().getPayType(), size27, WmPintData.LEFT));

        data.add(new WmPintData("商户单号:" + posBean.getMsg().getOrderNo(), size27, WmPintData.LEFT));
        if(posBean.getMsg().getTransaction_id()!=null && !"".equals(posBean.getMsg().getTransaction_id())){
            data.add(new WmPintData("交易单号:" + posBean.getMsg().getTransaction_id(), size27, WmPintData.LEFT));
        }
        data.add(new WmPintData("操作员:" + Constant.USER_ACCOUNT, size27, WmPintData.LEFT));
        if (posBean.getMsg().getOrderNote() != null && !TextUtils.isEmpty(posBean.getMsg().getOrderNote())) {
            data.add(new WmPintData("备注:" + posBean.getMsg().getOrderNote(), size27, WmPintData.LEFT));
        }
        List<PosPrintBean.MsgBean.GoodsInfoBean> goodsInfo = posBean.getMsg().getGoodsInfo();
        if (goodsInfo != null && goodsInfo.size()>0) {
            if(!"会员充值".equals(dayOrderNum)){
                wmCenterFormat("商品信息", "-", data, size27);
                addFormatWmTest(data, "商品名称", "单价","数量", "金额", size27);
                for (int i = 0; i < goodsInfo.size(); i++) {
                    PosPrintBean.MsgBean.GoodsInfoBean goodsInfoBean = goodsInfo.get(i);
                    if ("0".equals(goodsInfoBean.getType()) || "1".equals(goodsInfoBean.getType()) || "2".equals(goodsInfoBean.getType())) {
                        addFormatWmTest(data, goodsInfoBean.getName(), goodsInfoBean.getPrice()+"",goodsInfoBean.getNum(), Constant.numDf.format(Double.valueOf(goodsInfoBean.getNum()) * goodsInfoBean.getPrice()), size27);
                    }
                    addvipPriceNotice(data,goodsInfoBean.getPrice(),goodsInfoBean.getOri_price());
                }
            }
        }

        //退款
        String isRefund = posBean.getMsg().getIsRefund();
        if("1".equals(isRefund)) {
            data.add(new WmPintData(line32,size27,WmPintData.CENTER));
            data.add(new WmPintData("退款时间:"+posBean.getMsg().getRefundTime(),size27,WmPintData.LEFT));
            data.add(new WmPintData("退款金额:"+posBean.getMsg().getRefundMoney()+"元",size27,WmPintData.LEFT));
            data.add(new WmPintData("操作账号:"+posBean.getMsg().getRefundOperator(),size27,WmPintData.LEFT));
            if(!needPhone) {
                data.add(new WmPintData(line32,size27,WmPintData.CENTER));
            }
        }
    }

    private static void addvipPriceNotice(List<WmPintData> headerData, double goods_price, double ori_price) {
        if(ori_price-goods_price>0) {
            headerData.add(new WmPintData("原单价:"+ori_price+"元,会员优惠:"+ Utils.dropZero(Utils.keepTwoDecimal(ori_price-goods_price+""))+"元\n",size27, WmPintData.LEFT));
//            headerData.add("\n");
        }
    }

    //小程序消息列表详情页
    public static void formatAppletDetailData( List<WmPintData> data, PosPrintBean posBean,
                                              DiscountInfo discountBean,ArrayList<YunPrintGroupBean> oneByOneYunList) {
        textSize=27;
        //权限判断
        posDetailCommonPartForApplet(data, posBean, true, discountBean);
        if(discountBean!=null) {
            totalPrintMemberTest(discountBean, data);
        }else{
            wmCenterFormat("-","-", data,size27);
        }
        data.add(new WmPintData("收银SaaS系统提供商：门店派", size27, WmPintData.LEFT));
        data.add(new WmPintData("联系电话：400-9600-567", size27, WmPintData.LEFT));
        data.add(new WmPintData("乐推微网址：tui.younle.com", size27, WmPintData.LEFT));
        data.add(new WmPintData(size27,line27));
        data.add(new WmPintData(size27, "顾客联"));

        String yunLineData = getYunLineData(data,true,0);
        oneByOneYunList.add(0,new YunPrintGroupBean(yunLineData,Constant.GROUP_CUSTOMER));

        data.add(new WmPintData(size27, "\n"));
        formatOneByOneYunPrintData(posBean,oneByOneYunList);
    }

    /**
     * 初始化会员数据
     * @param discountBean
     * @param memberData
     */
    private static void totalPrintMemberTest(DiscountInfo discountBean, List<WmPintData> memberData) {
        //会员折扣
        String url = discountBean.getUrl();
        List<DiscountInfo.BalanceRulesBean> balance_rules = discountBean.getBalance_rules();
        List<DiscountInfo.DiscountRulesBean> discount_rules = discountBean.getDiscount_rules();

        wmCenterFormat("-","-", memberData,size27);
        //会员充值
        String storeWechat = discountBean.getStoreWechat();
        if(url!=null&&!TextUtils.isEmpty(url)) {
            memberData.add(new WmPintData("扫描下方二维码成为我店会员", size27, WmPintData.CENTER));
        }
        if(balance_rules!=null&&balance_rules.size()>0) {
            for (int i = 0; i < balance_rules.size(); i++) {
                String info = "充值" + balance_rules.get(i).getMoney() + "元赠送" + balance_rules.get(i).getSong()+"元";
                wmCenterFormat(info," ", memberData,size27);
            }
        }
        if(discount_rules!=null&&discount_rules.size()>0) {
            for (int i = 0; i < discount_rules.size(); i++) {
                String youhui = "购物满" + discount_rules.get(i).getMoney() + "元享" + discount_rules.get(i).getRate() + "折优惠";
                wmCenterFormat(youhui," ", memberData,size27);
            }
        }

        if(discountBean.getDiscount_type()==1) {
            wmCenterFormat("购物时商品可享受会员价"," ", memberData,size27);
        }

        if(url!=null&&!TextUtils.isEmpty(url)) {
            memberData.add(new WmPintData(size27, "   "));
            memberData.add(new WmPintData(4, url, WmPintData.CENTER, WmPintData.QRCODE));
        }
        //附加会员充值打印信息
        if(storeWechat!=null&&!"".equals(storeWechat)){
            memberData.add(new WmPintData("会员可使用微信扫描下方二维码", size27, WmPintData.CENTER));
            memberData.add(new WmPintData("关注商家公众号后查询余额变更明细", size27, WmPintData.CENTER));
            memberData.add(new WmPintData(size27, "   "));
            memberData.add(new WmPintData(4, storeWechat, WmPintData.CENTER, WmPintData.QRCODE));
        }
    }
    private static void posDetailCommonPartForApplet(List<WmPintData> data, PosPrintBean posBean, boolean needPhone, DiscountInfo discountBean) {
        data.add(new WmPintData(Constant.STORE_P + Constant.STORE_M,size27,WmPintData.CENTER));
        if(needPhone) {
            data.add(new WmPintData("电话:" + STORE_TEL,size27,WmPintData.CENTER));
        }
        if(posBean.getMsg().getRoomName()!=null&&!"".equals(posBean.getMsg().getRoomName())){
            data.add(new WmPintData(posBean.getMsg().getRoomName(),50,WmPintData.CENTER));
        }else{
            data.add(new WmPintData("订单序号:" + posBean.getMsg().getDayOrderNum(),50,WmPintData.CENTER));
        }
        data.add(new WmPintData("顾客自助点单",size27,WmPintData.CENTER));
        data.add(new WmPintData(size27, line27));
        data.add(new WmPintData("下单时间:" + posBean.getMsg().getAddTime(),size27,WmPintData.LEFT));
//        data.add(new WmPintData("结账时间:" + posBean.getMsg().getPayTime(),size27,WmPintData.LEFT));
//        data.add(new WmPintData("应付金额:"+posBean.getMsg().getShouldPayMoney()+"元",size27,WmPintData.LEFT));
//        data.add(new WmPintData("实付金额:" + posBean.getMsg().getFactPayMoney() + "元", size27, WmPintData.LEFT));
        if(posBean.getMsg().getOrderNote()!=null&&!TextUtils.isEmpty(posBean.getMsg().getOrderNote())) {
            data.add(new WmPintData("备注:" + posBean.getMsg().getOrderNote(), size27, WmPintData.LEFT));
        }
        List<PosPrintBean.MsgBean.GoodsInfoBean> goodsInfo = posBean.getMsg().getGoodsInfo();
        if(goodsInfo!=null){
            wmCenterFormat("商品信息","-", data,size27);
            addFormatWmTest(data,"商品名称","单价","数量","金额",size27);
            for (int i = 0; i < goodsInfo.size(); i++) {
                PosPrintBean.MsgBean.GoodsInfoBean goodsInfoBean = goodsInfo.get(i);
                if("0".equals(goodsInfoBean.getType())||"1".equals(goodsInfoBean.getType())||"2".equals(goodsInfoBean.getType())) {
                    addFormatWmTest(data, goodsInfoBean.getName(), goodsInfoBean.getPrice()+"",goodsInfoBean.getNum(), Constant.numDf.format(Double.valueOf(goodsInfoBean.getNum()) * goodsInfoBean.getPrice()),size27);
                }
            }
        }
    }
    //小程序单列表详情页商家联分组
    private static void posDetailForAppletSeller(List<WmPintData> data, PosPrintBean posBean, boolean needPhone) {

        data.add(new WmPintData(Constant.STORE_P + Constant.STORE_M,size27,WmPintData.CENTER));
        if(needPhone) {
            data.add(new WmPintData("电话:" + STORE_TEL,size27,WmPintData.CENTER));
        }
        if(posBean.getMsg().getRoomName()!=null&&!"".equals(posBean.getMsg().getRoomName())){
            data.add(new WmPintData(posBean.getMsg().getRoomName(),50,WmPintData.CENTER));
        }else{
            data.add(new WmPintData("订单序号:" + posBean.getMsg().getDayOrderNum(),50,WmPintData.CENTER));
        }
        data.add(new WmPintData("顾客自助点单",size27,WmPintData.CENTER));
        data.add(new WmPintData(size27, line27));
        data.add(new WmPintData("下单时间:" + posBean.getMsg().getAddTime(),size27,WmPintData.LEFT));
        data.add(new WmPintData("结账时间:" + posBean.getMsg().getPayTime(),size27,WmPintData.LEFT));
        data.add(new WmPintData("应付金额:"+posBean.getMsg().getShouldPayMoney()+"元",size27,WmPintData.LEFT));
        data.add(new WmPintData("实付金额:" + posBean.getMsg().getFactPayMoney() + "元", size27, WmPintData.LEFT));
        data.add(new WmPintData("操作员:" + Constant.USER_ACCOUNT, size27, WmPintData.LEFT));
        if(posBean.getMsg().getOrderNote()!=null&&!TextUtils.isEmpty(posBean.getMsg().getOrderNote())) {
            data.add(new WmPintData("备注:" + posBean.getMsg().getOrderNote(), size27, WmPintData.LEFT));
        }
        //添加分组商品信息
        addGroupGoodsInfo(posBean,data);
    }

    private static void addGroupGoodsInfo(PosPrintBean posBean, List<WmPintData> data) {
        List<PosPrintBean.MsgBean.GoodsInfoBean> goodsInfo = posBean.getMsg().getGoodsInfo();
        List<PosPrintBean.MsgBean.GroupInfoBean> groupInfo = posBean.getMsg().getGroupInfo();
        if(goodsInfo!=null){
            //分组情况：有分组商品还有未分组商品
            if(groupInfo!=null&&groupInfo.size()>0){
                for(int i=0;i<groupInfo.size();i++){
                    if(groupInfo.get(i).getId()!=null || !"".equals(groupInfo.get(i).getId()) || !"0".equals(groupInfo.get(i).getId())){
                        wmCenterFormat(groupInfo.get(i).getName(),"-", data,size27);
                        addFormatWmTest(data,"商品名称","单价","数量","金额",size27);
                        for(int j=0;j<goodsInfo.size();j++){
                            if(groupInfo.get(i).getId().equals(goodsInfo.get(j).getGroup_id())){
                                addFormatWmTest(data, goodsInfo.get(j).getName(),goodsInfo.get(j).getPrice()+"" ,goodsInfo.get(j).getNum(), Constant.numDf.format(Double.valueOf(goodsInfo.get(j).getNum()) * goodsInfo.get(j).getPrice()),size27);
                            }
                        }
                    }
                }
                //接着处理未分组商品：首先判断是否还有未分组商品；进而添加
                boolean hasNoGroupGoods = false;
                for(int n=0;n<goodsInfo.size();n++){
                    if(goodsInfo.get(n).getGroup_id()==null||"".equals(goodsInfo.get(n).getGroup_id())||"0".equals(goodsInfo.get(n).getGroup_id())){
                        hasNoGroupGoods = true;
                        break;
                    }
                }
                if(hasNoGroupGoods){
                    wmCenterFormat("其他","-", data,size27);
                    addFormatWmTest(data,"商品名称","单价","数量","金额",size27);
                    for(int n=0;n<goodsInfo.size();n++){
                        if(goodsInfo.get(n).getGroup_id()==null||"".equals(goodsInfo.get(n).getGroup_id())||"0".equals(goodsInfo.get(n).getGroup_id())){
                            addFormatWmTest(data, goodsInfo.get(n).getName(), goodsInfo.get(n).getPrice()+"",goodsInfo.get(n).getNum(), numDf.format(Double.valueOf(goodsInfo.get(n).getNum()) * goodsInfo.get(n).getPrice()),size27);
                        }
                    }
                }
            }else{
                //所有商品没分组
                wmCenterFormat("商品信息","-", data,size27);
                addFormatWmTest(data,"商品名称","单价","数量","金额",size27);
                for(int n=0;n<goodsInfo.size();n++){
                    addFormatWmTest(data, goodsInfo.get(n).getName(),goodsInfo.get(n).getPrice()+"" ,goodsInfo.get(n).getNum(), numDf.format(Double.valueOf(goodsInfo.get(n).getNum()) * goodsInfo.get(n).getPrice()),size27);
                }
            }
        }
    }

    /**
     * 格式化美团饿了么打印数据格式
     * @param mtOrder
     * @param elmBean
     * @return
     */
    public static int textSize=27;
    public static Map formatYunPrintDataWM(MtOrderDetail mtOrder, ElmOrderBean elmBean, NewElmOrderBean newElmOrderBean, Context context, boolean isbt){
        textSize=35;
        Map map=new HashMap();
        List<WmPintData> customerPart=new ArrayList<>();
        List<WmPintData> merchantPart=new ArrayList<>();

        if(mtOrder!=null) {
            merchantPart.add(new WmPintData(size27,"给商户"));
            merchantPart.add(new WmPintData(size27,line27));
            addMtGongTong(mtOrder, merchantPart);
            merchantPart.add(new WmPintData(size27,line27));
            merchantPart.add(new WmPintData(size27,"外卖多平台自动接单软件:乐推微门店派"));
            merchantPart.add(new WmPintData(size27,"\n"));

            customerPart.add(new WmPintData(size27,"给顾客"));
            customerPart.add(new WmPintData(size27,line27));
            addMtGongTong(mtOrder, customerPart);
            wmCenterFormat("商户信息", "*", customerPart, size27);
            customerPart.add(new WmPintData(size27, "商户名称:" + mtOrder.getKey().getPoiName()));
            customerPart.add(new WmPintData(size27,"商户电话:" + mtOrder.getKey().getPoiPhone()));
            customerPart.add(new WmPintData(size27,"商户地址:"+mtOrder.getKey().getPoiAddress()));
            customerPart.add(new WmPintData(size27,line27));
            customerPart.add(new WmPintData(size27,"\n"));
        }

        if(elmBean!=null) {
            merchantPart.add(new WmPintData(size27,"给商户"));
            merchantPart.add(new WmPintData(size27,line27));
            addELmGongTong(elmBean, merchantPart);
            merchantPart.add(new WmPintData(size27,line27));
            merchantPart.add(new WmPintData(size27,"外卖多平台自动接单软件:乐推微门店派"));
            merchantPart.add(new WmPintData(size27,"\n"));

            customerPart.add(new WmPintData(size27,"给顾客"));
            customerPart.add(new WmPintData(size27,line27));
            addELmGongTong(elmBean, customerPart);
            wmCenterFormat("商户信息", "*", customerPart, size27);
            customerPart.add(new WmPintData(size27,"商户名称：" + elmBean.getData().getRestaurant_name()));
            customerPart.add(new WmPintData(size27,"商户电话：" + Constant.ELM_STORE_TEL));
            customerPart.add(new WmPintData(size27,"商户地址："+Constant.ELM_STORE_ADD));
            customerPart.add(new WmPintData(size27,line27));
            customerPart.add(new WmPintData(size27,"\n"));
        }
        if(newElmOrderBean!=null) {
            merchantPart.add(new WmPintData(size27,"给商户"));
            merchantPart.add(new WmPintData(size27,line27));
            addNewELmGongTong(newElmOrderBean, merchantPart);
            merchantPart.add(new WmPintData(size27,line27));
            merchantPart.add(new WmPintData(size27,"外卖多平台自动接单软件:乐推微门店派"));
            merchantPart.add(new WmPintData(size27,"\n"));

            customerPart.add(new WmPintData(size27,"给顾客"));
            customerPart.add(new WmPintData(size27,line27));
            addNewELmGongTong(newElmOrderBean, customerPart);
            wmCenterFormat("商户信息", "*", customerPart,size27);
            customerPart.add(new WmPintData(size27,"商户名称:" + newElmOrderBean.getMessage().getShopName()));
            customerPart.add(new WmPintData(size27,"商户电话:" + Constant.ELM_STORE_TEL));
            customerPart.add(new WmPintData(size27,"商户地址:"+Constant.ELM_STORE_ADD));
            customerPart.add(new WmPintData(size27,line27));
            customerPart.add(new WmPintData(size27,"\n"));
        }
        map.put(Constant.FOR_CUSTOMER,customerPart);
        map.put(FOR_MERCHANT,merchantPart);
        return map;
    }

    /**
     * 新的饿了么商户和客户共同部分
     * @param newElmBean
     * @param pData
     */
    private static void addNewELmGongTong(NewElmOrderBean newElmBean, List<WmPintData> pData) {
        pData.add(new WmPintData(size40, "饿了么 # " + newElmBean.getMessage().getDaySn()));

        if (!newElmBean.getMessage().isOnlinePaid()) {//货到付款
            pData.add(new WmPintData(size27, "[货到付款]"));
        } else {
            pData.add(new WmPintData(size27, "[已在线支付]"));
        }
        if (!(newElmBean.getMessage().getDeliverTime() == null)) {
            pData.add(new WmPintData(size35, "期望送达时间:" + newElmBean.getMessage().getDeliverTime().replace("T", " ")));
        } else {
            pData.add(new WmPintData(size35, "期望送达时间:立即送达"));
        }
        if (!TextUtils.isEmpty(newElmBean.getMessage().getDescription())) {
            pData.add(new WmPintData(size27,line27));
            pData.add(new WmPintData(size35, "订单备注:" + newElmBean.getMessage().getDescription()));
        }
        wmCenterFormat("订单信息", "*", pData, size27);
        wmCenterFormat("菜品", "-", pData,size27);
        addFormatWmTest(pData, "菜品名称", "单价","数量", "金额",size27);
        List<NewElmOrderBean.MessageBean.GroupsBean> groups = newElmBean.getMessage().getGroups();

        if (groups != null && groups.size() > 0) {
            // 1. group菜品
            for (int i = 0; i < groups.size(); i++) {
                String type = groups.get(i).getType();
                List<NewElmOrderBean.MessageBean.GroupsBean.ItemsBean> items = groups.get(i).getItems();
                if ("normal".equals(type)) {
                    for (int j = 0; j < items.size(); j++) {
                        NewElmOrderBean.MessageBean.GroupsBean.ItemsBean itemsBean = items.get(j);
                        addFormatWmTest(pData, itemsBean.getName(), itemsBean.getPrice()+"",itemsBean.getQuantity()+"", numDf.format(itemsBean.getPrice() * itemsBean.getQuantity()),size35);
                    }
                }
            }
            for (int i = 0; i < groups.size(); i++) {
                String type = groups.get(i).getType();
                List<NewElmOrderBean.MessageBean.GroupsBean.ItemsBean> items = groups.get(i).getItems();
                if ("discount".equals(type)) {
                    wmCenterFormat("赠品", "-", pData,size27);
                    for (int a = 0; a < items.size(); a++) {
                        NewElmOrderBean.MessageBean.GroupsBean.ItemsBean itemsBean = items.get(a);
                        addFormatWmTest(pData, itemsBean.getName(), itemsBean.getPrice()+"",itemsBean.getQuantity()+"", numDf.format(itemsBean.getPrice() * itemsBean.getQuantity()),size27);
                    }
                }
            }
            for (int i = 0; i < groups.size(); i++) {
                String type = groups.get(i).getType();
                List<NewElmOrderBean.MessageBean.GroupsBean.ItemsBean> items = groups.get(i).getItems();
                if ("extra".equals(type)) {
                    wmCenterFormat("其他", "-", pData,size27);
                    for (int a = 0; a < items.size(); a++) {
                        NewElmOrderBean.MessageBean.GroupsBean.ItemsBean itemsBean = items.get(a);
                        wmAddTwoSide(pData, "配送费", newElmBean.getMessage().getDeliverFee() + "", size35);
                        addFormatWmTest(pData, itemsBean.getName(), itemsBean.getPrice()+"",itemsBean.getQuantity()+"", numDf.format(itemsBean.getPrice() * itemsBean.getQuantity()),size35);
                    }
                }

            }
            wmCenterFormat("优惠", "-", pData, size27);
            wmAddTwoSide(pData, "商家优惠", "" + newElmBean.getMessage().getActivityTotal(), size35);
            wmAddTwoSide(pData, "红包抵扣", "" + newElmBean.getMessage().getHongbao(), size35);
            pData.add(new WmPintData(size27,line27));
            double total_price = newElmBean.getMessage().getTotalPrice();
            pData.add(new WmPintData(size27, "金额总计:￥" + total_price));
            pData.add(new WmPintData(size27, "订单编号:" + newElmBean.getMessage().getId()));
            pData.add(new WmPintData(size27, "下单时间:" + newElmBean.getMessage().getCreatedAt().replace("T", " ")));


            boolean isInvoiced = newElmBean.getMessage().isInvoiced();
            if (isInvoiced) {//不需要
                pData.add(new WmPintData(size27, "发票抬头：" + newElmBean.getMessage().getInvoice()));
                pData.add(new WmPintData(size27, "发票金额：" + numDf.format(total_price) + "元"));
            }
            wmCenterFormat("客户信息", "*", pData, size27);
            pData.add(new WmPintData(size35, newElmBean.getMessage().getAddress()));
            pData.add(new WmPintData(size35, newElmBean.getMessage().getConsignee()));
            pData.add(new WmPintData(size35, "电话:" + newElmBean.getMessage().getPhoneList().get(0)));
        }
    }

    private static void wmAddTwoSide(List<WmPintData> data, String left, String right, int textSize) {
        int cnNum = Utils.getChinaNum(left);
        int leftTotal = left.length() - cnNum + cnNum * 2;

        int sideLen=0;
        if(textSize==27) {
            //sideLen=18;
            sideLen = 20;
        }else if(textSize==35) {
            //sideLen=14;
            sideLen = 16;
        }
        if(leftTotal>=sideLen) {
            data.add(new WmPintData(textSize, left));
            wmAddTwoSide(data, left, right, textSize);
        }else {
            String space="";
            int length = (left + right).length();
            int chinaNum = Utils.getChinaNum(left+right);
            int otherChar = length - chinaNum;
            int total=0;
            if(textSize==27) {
                total=32;
            }else if(textSize==35) {
                total=24;
            }
            int count=total-otherChar-chinaNum*2;

            for (int a = 0; a < count; a++) {
                space+=" ";
            }
            data.add(new WmPintData(textSize, left + space + right));
        }
    }

    /**
     * 添加饿了么商户和客户共同部分
     * @param elmBean
     * @param pData
     */
    private static void addELmGongTong(ElmOrderBean elmBean, List<WmPintData> pData) {
        pData.add(new WmPintData(size40, "饿了么 # " + elmBean.getData().getRestaurant_number()));
        if (0 == elmBean.getData().getIs_online_paid()) {//货到付款
            pData.add(new WmPintData(size27, "[货到付款]"));
        } else {
            pData.add(new WmPintData(size27, "[已在线支付]"));
        }
        if (!(elmBean.getData().getDeliver_time() == null)) {
            pData.add(new WmPintData(size35,"期望送达时间:"+elmBean.getData().getDeliver_time()));
        } else {
            pData.add(new WmPintData(size35, "期望送达时间:立即送达"));
        }
        if (!TextUtils.isEmpty(elmBean.getData().getDescription())) {
            pData.add(new WmPintData(size27,line27));
            pData.add(new WmPintData(size35, "订单备注:" + elmBean.getData().getDescription()));
        }

        wmCenterFormat("订单信息", "*", pData, size27);
        wmCenterFormat("菜品", "-", pData, size27);
        addFormatWmTest(pData, "菜品名称", "单价","数量", "金额", size27);
        ElmOrderBean.DataBean.DetailBean details = elmBean.getData().getDetail();
        if (details != null) {
            // 1. group菜品
            List<List<ElmOrderBean.DataBean.DetailBean.GroupBean>> group = details.getGroup();
            if (group != null && group.size() > 0) {
                List<ElmOrderBean.DataBean.DetailBean.GroupBean> groupBeans = new ArrayList<>();
                for (int i = 0; i < group.size(); i++) {
                    List<ElmOrderBean.DataBean.DetailBean.GroupBean> secondGroup = group.get(i);
                    if (secondGroup != null && secondGroup.size() > 0) {
                        for (int j = 0; j < secondGroup.size(); j++) {
                            ElmOrderBean.DataBean.DetailBean.GroupBean groupBean = secondGroup.get(j);
                            List<String> specs = groupBean.getSpecs();
                            String specsStr = "";
                            if (specs != null && specs.size() > 0) {
                                for (int a = 0; a < specs.size(); a++) {
                                    specsStr += specs.get(a);
                                }
                            }
                            String name;
                            if (!TextUtils.isEmpty(specsStr)) {
                                name = groupBean.getName() + "[" + specsStr + "]";
                            } else {
                                name = groupBean.getName();
                            }
                            float price = groupBean.getPrice();
                            int quantity = groupBean.getQuantity();
                            addFormatWmTest(pData, name, price+"",quantity + "", numDf.format(price * quantity), size35);
                        }
                    }
                }
            }
            // 2. extra优惠信息
            wmCenterFormat("其他", "-", pData, size27);
            if (elmBean.getData().getPackage_fee() >= 0) {
                addFormatWmTest(pData, "餐盒费", " "," ", numDf.format(elmBean.getData().getPackage_fee()), size35);
            }
            addFormatWmTest(pData, "配送费", " "," ", numDf.format(elmBean.getData().getDeliver_fee()), size35);
            List<ElmOrderBean.DataBean.DetailBean.ExtraBean> extras = details.getExtra();
            if (extras != null && extras.size() > 0) {
                for (int i = 0; i < extras.size(); i++) {
                    ElmOrderBean.DataBean.DetailBean.ExtraBean extraBean = extras.get(i);
                    int category_id = extraBean.getCategory_id();
                    if (category_id == 2 || category_id == 102) {
                        continue;
                    } else {
                        String name = extraBean.getName();
                        float price = extraBean.getPrice();
                        int quantity = extraBean.getQuantity();
                        LogUtils.Log("name==" + name + " price==" + price + "  quantity==" + quantity);
                        addFormatWmTest(pData, name, price+"",quantity + "", numDf.format(price * quantity), size27);
                    }
                }
            }
        }
        pData.add(new WmPintData(size27,line27));

        pData.add(new WmPintData(size35, "金额总计:￥" + elmBean.getData().getTotal_price()));
//        pData.add("用餐人数："+elmBean.getData().getDescription());
        pData.add(new WmPintData(size27, "订单编号:" + elmBean.getData().getOrder_id()));
        pData.add(new WmPintData(size27, "下单时间:" + elmBean.getData().getCreated_at()));
        int invoiced = elmBean.getData().getInvoiced();
        float total_price = elmBean.getData().getTotal_price();
        if (1 == invoiced) {//不需要
            pData.add(new WmPintData(size27, "发票抬头:" + elmBean.getData().getInvoice()));
            pData.add(new WmPintData(size27, "发票金额:" + numDf.format(total_price) + "元"));
        }
        wmCenterFormat("客户信息", "*", pData, size27);
        pData.add(new WmPintData(size35, elmBean.getData().getDelivery_poi_address()));
        pData.add(new WmPintData(size35, elmBean.getData().getConsignee()));
        pData.add(new WmPintData(size35, "电话:" + elmBean.getData().getPhone_list().get(0)));
    }

    /**
     * 添加美团商户和客户共同部分
     * @param mtOrder
     * @param pData
     */
    private static void addMtGongTong(MtOrderDetail mtOrder, List<WmPintData> pData) {
        pData.add(new WmPintData(size40,"美团外卖 # " + mtOrder.getKey().getDaySeq()));
        if(1==mtOrder.getKey().getPayType()) {//货到付款
            pData.add(new WmPintData(size27,"[货到付款]"));
        }else {
            pData.add(new WmPintData(size27,"已在线支付"));
        }

        if(0==mtOrder.getKey().getDeliveryTime()) {
            pData.add(new WmPintData(size35,"期望送达时间:立即送达"));
        }else {
            String formatDTime = df4.format(mtOrder.getKey().getDeliveryTime()*1000);
            pData.add(new WmPintData(size35,"期望送达时间:" + formatDTime));
        }

        if(!TextUtils.isEmpty(mtOrder.getKey().getCaution())) {
            pData.add(new WmPintData(size27,line27));
            pData.add(new WmPintData(size35,"订单备注:"+mtOrder.getKey().getCaution()));
        }
        wmCenterFormat("订单信息", "*", pData, size27);
        wmCenterFormat("菜品", "-", pData, size27);
        addFormatWmTest(pData, "菜品名称", "单价","数量", "金额", size27);
        List<MtOrderDetail.KeyBean.DetailBean> detailBeanList = mtOrder.getKey().getDetail();
        if(detailBeanList!=null&&detailBeanList.size()>0) {
            int total_box_num=0;
            double total_box_account=0;
            for (int i = 0; i < detailBeanList.size(); i++) {
                MtOrderDetail.KeyBean.DetailBean detailBean = detailBeanList.get(i);
                //计算总的餐盒数量和费用
                int box_num = detailBean.getBox_num();
                double box_price = detailBean.getBox_price();
                double boxAccount = box_price * box_num;
                total_box_num+=box_num;
                total_box_account+=boxAccount;

                int num = detailBean.getQuantity();
                String food_name = detailBean.getFood_name();
                String spec = detailBean.getSpec();
                double price = detailBean.getPrice();
                String food_property = detailBean.getFood_property();
                double total_fee = num * price;
                if(spec!=null&&!TextUtils.isEmpty(spec)) {
                    if(food_property!=null) {
                        food_property=food_property.replace(",",")(");
                        addFormatWmTest(pData, food_name+"("+spec+")"+"("+food_property+")",price+"", num + "", numDf.format(total_fee), size35);
                    }else {
                        addFormatWmTest(pData, food_name+"("+spec+")",price+"", num + "", numDf.format(total_fee), size35);
                    }
                }else {
                    if(food_property!=null) {
                        food_property=food_property.replace(",",")(");
                        addFormatWmTest(pData, food_name+"("+food_property+")",price+"", num + "", numDf.format(total_fee), size35);
                    }else {
                        addFormatWmTest(pData, food_name,price+"", num + "", numDf.format(total_fee), size35);
                    }
                }
            }
            wmCenterFormat("其他", "-", pData, size27);
            addFormatWmTest(pData, "餐盒费", " ",total_box_num + "", numDf.format(total_box_account), size35);
        }
        //配送费用
        double shippingFee = mtOrder.getKey().getShippingFee();
        addFormatWmTest(pData, "配送费", " "," ", numDf.format(shippingFee), size35);
        //优惠信息
        List<MtOrderDetail.KeyBean.ExtrasBean> extras = mtOrder.getKey().getExtras();
        if(extras!=null&&extras.size()>0) {
            for (int i = 0; i < extras.size(); i++) {
                MtOrderDetail.KeyBean.ExtrasBean extrasBean = extras.get(i);
                String youhuiName = extrasBean.getRemark();
                float reduce_fee = extrasBean.getReduce_fee();
                String youhuiAcc = numDf.format(reduce_fee);
                if(youhuiName!=null) {
                    addFormatWmTest(pData, youhuiName, "", "","-" + youhuiAcc, size27);
                }else {
                    if(reduce_fee>0) {
                        addFormatWmTest(pData, "", "","", "-" + youhuiAcc, size27);
                    }
                }
            }
        }
        pData.add(new WmPintData(size27,line27));
        pData.add(new WmPintData(size35,"金额总计:￥"+mtOrder.getKey().getTotal()));
        if(mtOrder.getKey().getDinnersNumber()>0) {
            pData.add(new WmPintData(size27,"用餐人数:"+mtOrder.getKey().getDinnersNumber()));
        }
        pData.add(new WmPintData(size27,"订单编号:"+mtOrder.getKey().getOrderIdView()));

        pData.add(new WmPintData(size27,"下单时间:"+df4.format(mtOrder.getKey().getCtime() * 1000)));

        int hasInvoiced = mtOrder.getKey().getHasInvoiced();
        double total = mtOrder.getKey().getTotal();
        if(1==hasInvoiced) {//1-需要
            pData.add(new WmPintData(size27,"发票抬头:"+mtOrder.getKey().getInvoiceTitle()));
            pData.add(new WmPintData(size27,"发票金额:" + total + "元"));
        }
        wmCenterFormat("客户信息", "*", pData, size27);
        String recipientAddress = mtOrder.getKey().getRecipientAddress();
        if(recipientAddress.contains("@#")) {
            recipientAddress=recipientAddress.substring(0,recipientAddress.indexOf("@#"));
        }
        pData.add(new WmPintData(size35,recipientAddress));
        pData.add(new WmPintData(size35,mtOrder.getKey().getRecipientName()));
        pData.add(new WmPintData(size35,"电话:" + mtOrder.getKey().getRecipientPhone()));
//        pData.add("\n");
    }

    /**
     * 获取云打印行数据
     * @param data
     * @return
     */
    private static String getYunLineData(List<WmPintData> data,boolean isCustomer,int tag) {

        String text = "";
        for(int i=0;i<data.size();i++){
            WmPintData lineData = data.get(i);
            //判断 位置(居中、右侧、左侧)、字体大小、二维码
            int alin = lineData.getAlin();
            int fontType = lineData.getTextSize();
            String line_data = lineData.getMsg();
            if(judgetosideLine(line_data)){
                if(line_data.contains("----------------------")){
                    text += line32;
                }else {
                    text += centerFormatYunSize(1,line_data,"-");
                }
            }else{
                if(line_data.contains("http://weixin.qq.com/q")){
                    LogUtils.e(TAG,"getYunLineData()     alin="+alin+",fontType="+fontType+",line_data="+line_data);
                    text += (printQRCode(line_data));
                    text += "\n";
                }else{
                    switch (alin){
                        case 0://左
                            //判断字体
                            if(fontType == size27){//默认的字体
                            }else if(fontType == size35){//设置的中号字体
                                line_data = YunPrintUtils.typefacesize1(line_data);
                            }else if(fontType == size40){//设置的大号字体
                                line_data = YunPrintUtils.typefacesize1(line_data);
                            }else if(fontType == size50){//设置的最大号字体
                                line_data = YunPrintUtils.typefacesize3(line_data);
                            }
                            line_data += "\n";
                            break;
                        case 1://居中
                            if(fontType == size27){//默认的字体
                                line_data = YunPrintUtils.centerFormatYunSize(1,line_data," ");
                            }else if(fontType == size35||fontType == size40){//设置的中号字体
                                line_data = printHorCenterAddSize2(centerFormatYunSize(1,(line_data)," "));
                            }else {//设置的大号字体
                                line_data = printHorCenterAddSize3(centerFormatYunSize(3,(line_data)," "));
                            }
                            break;
                        case 2://右侧
                            //判断字体
                            if(fontType ==size27){//默认的字体
                                line_data = printHorRight(line_data);
                            }else if(fontType == size35){//设置的中号字体
                                line_data = printHorRightaddSize1(line_data);
                            }else{//设置的大号字体
                                line_data = printHorRightaddSize2(line_data);
                            }
                            line_data += "\n";
                            break;
                    }
                    if(isCustomer){
                        text += line_data;
                    }else{
                        if(tag==1){//小程序订单需要打印商家联的订单号 传1可避免第二行不添加到text中
                            text += line_data;
                        }else{
                            if (i!=1) {
                                text += line_data;
                            }
                        }
                    }
                }
            }
        }
        return text;
    }

    public static List<YunPrintGroupBean> formatAppletYunPrintData(AppLetReceiveBean appLetBean, Context context){
        ArrayList<YunPrintGroupBean> oneByOneYunList=new ArrayList<>();
        LogUtils.Log("添加打印数据：formatPrintData()");
        List<WmPintData> yunPrintData = new ArrayList<>();
        posAppletForCustomer(yunPrintData,appLetBean);
        yunPrintData.add(new WmPintData(size27, "收银SaaS系统提供商：门店派"));
        yunPrintData.add(new WmPintData(size27, "联系电话：400-9600-567"));
        yunPrintData.add(new WmPintData(size27, "乐推微网址：tui.younle.com"));
        yunPrintData.add(new WmPintData(size27, "       "));
        yunPrintData.add(new WmPintData(size27, "顾客联"));
        yunPrintData.add(new WmPintData(size27, line32));
        yunPrintData.add(new WmPintData(size27, "\n"));
        String yunLineData = getYunLineData(yunPrintData,true,0);
        oneByOneYunList.add(0,new YunPrintGroupBean(yunLineData,Constant.GROUP_CUSTOMER));
        formatOneByOneYunPrintData(appLetBean,oneByOneYunList);
        return oneByOneYunList;
    }

    /**
     * 格式化商家联信息
     * @param data
     * @param appLetBean
     */
    private static void posAppletForSeller(List<WmPintData> data, AppLetReceiveBean appLetBean) {
        data.add(new WmPintData(Constant.STORE_P + Constant.STORE_M, size27, WmPintData.CENTER));
        if(appLetBean.getRoomName()!=null&&!"".equals(appLetBean.getRoomName())){
            data.add(new WmPintData(appLetBean.getRoomName(), size35, WmPintData.CENTER));
        }else{
            data.add(new WmPintData("订单序号:" + appLetBean.getDayOrderNum(), size35, WmPintData.CENTER));
        }
        data.add(new WmPintData("顾客自助点单",size27, WmPintData.CENTER));
        wmCenterFormat("-","-", data,size27);
        data.add(new WmPintData(size27, "下单时间:" + appLetBean.getAddTime()));
        if(appLetBean.getPayTime()!=null && "".equals(appLetBean.getPayTime())){
            data.add(new WmPintData(size27, "结账时间:" + appLetBean.getPayTime()));
        }
        data.add(new WmPintData(size27, "应付金额:" + appLetBean.getShouldPayMoney()+"元"));
        if(appLetBean.getFactPayMoney()>0){
            data.add(new WmPintData(size27, "实付金额:" + appLetBean.getFactPayMoney() + "元"));
        }

        if(appLetBean.getVoucherInfo() != null){
            switch (appLetBean.getVoucherInfo().getType()){
                case "CASH":
                    data.add(new WmPintData(size27, "优惠券:" + appLetBean.getVoucherInfo().getTitle()));
                    break;
                case "DISCOUNT":
                    data.add(new WmPintData(size27, "优惠券:" + appLetBean.getVoucherInfo().getTitle()));
                    break;
                case "MEMBER_CARD":
                    data.add(new WmPintData(size27, "会员折扣:" + appLetBean.getDiscount() * 10 + "折"));
                    break;
            }
        }else{
            if(appLetBean.getDiscount()>0) {
                data.add(new WmPintData(size27, "会员折扣:" + appLetBean.getDiscount() * 10 + "折"));
            }
        }
        if(appLetBean.getPayType()!=null&&"".equals(appLetBean.getPayType())){
            data.add(new WmPintData(size27, "支付方式:" + appLetBean.getPayType()));
        }
        data.add(new WmPintData(size27, "商户单号:" + appLetBean.getOrderNo()));

        if(appLetBean.getTransaction_id()!=null&&!TextUtils.isEmpty(appLetBean.getTransaction_id())) {
            data.add(new WmPintData(size27, "交易单号:" + appLetBean.getTransaction_id()));
        }
        data.add(new WmPintData(size27, "操作员:" + Constant.USER_ACCOUNT));
        if(appLetBean.getOrderNote()!=null&&!TextUtils.isEmpty(appLetBean.getOrderNote())) {
            data.add(new WmPintData(size27, "备注:" + appLetBean.getOrderNote()));
        }

        //商品信息
        formateGroupGoods(appLetBean,data);

        //退款
        String isRefund = appLetBean.getIsRefund();
        if("1".equals(isRefund)) {
            wmCenterFormat("-","-", data,size27);
            data.add(new WmPintData(size27, "退款时间:"+appLetBean.getRefundTime()));
            data.add(new WmPintData(size27, "退款金额:"+appLetBean.getRefundMoney()+"元"));
            data.add(new WmPintData(size27, "操作账号:"+appLetBean.getRefundOperator()));
            wmCenterFormat("-","-", data,size27);
        }
    }

    /**
     * 格式化顾客联信息
     * @param data
     * @param appLetBean
     */
    private static void posAppletForCustomer(List<WmPintData> data, AppLetReceiveBean appLetBean) {

        data.add(new WmPintData(Constant.STORE_P + Constant.STORE_M, size27, WmPintData.CENTER));
        data.add(new WmPintData("电话:" + STORE_TEL, size27, WmPintData.CENTER));
        if(appLetBean.getRoomName()!=null&&!"".equals(appLetBean.getRoomName())){
            data.add(new WmPintData(appLetBean.getRoomName(), size35, WmPintData.CENTER));
        }else{
            data.add(new WmPintData("订单序号:" + appLetBean.getDayOrderNum(), size35, WmPintData.CENTER));
        }
        data.add(new WmPintData("顾客自助点单", size27, WmPintData.CENTER));
        wmCenterFormat("-","-", data,size27);
        data.add(new WmPintData(size27, "下单时间:" + appLetBean.getAddTime()));
        if(appLetBean.getPayTime()!=null && "".equals(appLetBean.getPayTime())){
            data.add(new WmPintData(size27, "结账时间:" + appLetBean.getPayTime()));
        }
        data.add(new WmPintData(size27, "应付金额:" + appLetBean.getShouldPayMoney()+"元"));
        if(appLetBean.getFactPayMoney()>0){
            data.add(new WmPintData(size27, "实付金额:" + appLetBean.getFactPayMoney() + "元"));
        }
        if(appLetBean.getVoucherInfo() != null){
            switch (appLetBean.getVoucherInfo().getType()){
                case "CASH":
                    data.add(new WmPintData(size27, "优惠券:" + appLetBean.getVoucherInfo().getTitle()));
                    break;
                case "DISCOUNT":
                    data.add(new WmPintData(size27, "优惠券:" + appLetBean.getVoucherInfo().getTitle()));
                    break;
                case "MEMBER_CARD":
                    data.add(new WmPintData(size27, "会员折扣:" + appLetBean.getDiscount() * 10 + "折"));
                    break;
            }
        }else{
            if(appLetBean.getDiscount()>0) {
                data.add(new WmPintData(size27, "会员折扣:" + appLetBean.getDiscount() * 10 + "折"));
            }
        }
        data.add(new WmPintData(size27, "支付方式:" + appLetBean.getPayType()));
        data.add(new WmPintData(size27, "商户单号:" + appLetBean.getOrderNo()));
        if(appLetBean.getTransaction_id()!=null&&!TextUtils.isEmpty(appLetBean.getTransaction_id())) {
            data.add(new WmPintData(size27, "交易单号:" + appLetBean.getTransaction_id()));
        }
        data.add(new WmPintData(size27, "操作员:" + Constant.USER_ACCOUNT));
        if(appLetBean.getOrderNote()!=null&&!TextUtils.isEmpty(appLetBean.getOrderNote())) {
            data.add(new WmPintData(size27, "备注:" + appLetBean.getOrderNote()));
        }

        //商品信息
        List<AppLetReceiveBean.GoodsInfoBean> goodsInfo = appLetBean.getGoodsInfo();
        if(detailAppletContainWhich(goodsInfo,"2")||detailAppletContainWhich(goodsInfo,"1")||detailAppletContainWhich(goodsInfo,"0")) {
            centerFormat("商品信息","-",data, size27);
            addFormatWmTest(data,"商品名称","单价","数量","金额",size27);
            for (int i = 0; i < goodsInfo.size(); i++) {
                AppLetReceiveBean.GoodsInfoBean goodsInfoBean = goodsInfo.get(i);
                if("0".equals(goodsInfoBean.getType())||"1".equals(goodsInfoBean.getType())||"2".equals(goodsInfoBean.getType())) {
                    addFormatWmTest(data, goodsInfoBean.getName(), goodsInfoBean.getPrice()+"",goodsInfoBean.getNum(), numDf.format(Double.valueOf(goodsInfoBean.getNum()) * goodsInfoBean.getPrice()),size27);
                }
            }
        }

        centerFormat("-","-",data, size27);

        //二维码投放信息
        AppLetReceiveBean.TicketInfoBean ticketInfo = appLetBean.getTicketInfo();
        if(ticketInfo!=null) {
            chargeInfoQRCode(ticketInfo, data);
        }
    }

    private static void chargeInfoQRCode(AppLetReceiveBean.TicketInfoBean ticketInfo, List<WmPintData> data) {

        //会员折扣
        String url = ticketInfo.getUrl();
        List<AppLetReceiveBean.TicketInfoBean.BalanceRulesBean> balance_rules = ticketInfo.getBalance_rules();
        List<AppLetReceiveBean.TicketInfoBean.DiscountRulesBean> discount_rules = ticketInfo.getDiscount_rules();

        if(url!=null&&!TextUtils.isEmpty(url)) {
            wmCenterFormat("扫描下方二维码成为我店会员"," ",data,size27);
        }

        if(balance_rules!=null&&balance_rules.size()>0) {
            for (int i = 0; i < balance_rules.size(); i++) {
                String info = "充值" + balance_rules.get(i).getMoney() + "元赠送" + balance_rules.get(i).getSong()+"元";
                wmCenterFormat(info," ",data,size27);
            }
        }
        if(discount_rules!=null&&discount_rules.size()>0) {
            for (int i = 0; i < discount_rules.size(); i++) {
                String youhui = "购物满" + discount_rules.get(i).getMoney() + "元享" + discount_rules.get(i).getRate() + "折优惠";
                wmCenterFormat(youhui," ",data,size27);
            }
        }
        if(url!=null&&!TextUtils.isEmpty(url)) {
            wmCenterFormat("   "," ",data,size27);
            data.add(new WmPintData(7, url, WmPintData.CENTER, WmPintData.QRCODE));
        }
    }

    private static boolean detailAppletContainWhich(List<AppLetReceiveBean.GoodsInfoBean> goodsInfo, String which) {
        for (int i = 0; i < goodsInfo.size(); i++) {
            if(which.equals(goodsInfo.get(i).getType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 居中格式化添加数据
     * @param info
     * @param data
     */
    private static void wmCenterFormat(String info, String elseChar, List<WmPintData> data, int textSize) {
        int totalLegth;
        if(textSize==27) {
            totalLegth=32;
        }else {
            totalLegth=27;
        }
        int length = info.length();
        int chinaNum = Utils.getChinaNum(info);
        int otherChar = length - chinaNum;
        int spacesNum=(totalLegth-otherChar-chinaNum*2)/2;
        String space="";
        for (int i = 0; i < spacesNum; i++) {
            space+=elseChar;
        }
        data.add(new WmPintData(textSize,space+info+space));
    }

    //格式化商户联分组商品数据
    private static void formateGroupGoods(AppLetReceiveBean appLetBean, List<WmPintData> data) {
        List<AppLetReceiveBean.GoodsInfoBean> goodsInfo = appLetBean.getGoodsInfo();
        List<AppLetReceiveBean.groupInfoBean> groupInfo = appLetBean.getGroupInfo();

        if(groupInfo!=null&&groupInfo.size()>0){
            for(int i=0;i<groupInfo.size();i++){
                if(groupInfo.get(i).getId()!=null || !"".equals(groupInfo.get(i).getId()) || !"0".equals(groupInfo.get(i).getId())){
                    addFormatWmTest(data,"商品名称","单价","数量","金额", size27);
                    for(int j=0;j<goodsInfo.size();j++){
                        if(groupInfo.get(i).getId().equals(goodsInfo.get(j).getGroup_id())){
                            addFormatWmTest(data, goodsInfo.get(j).getName(), goodsInfo.get(j).getPrice()+"",goodsInfo.get(j).getNum(),numDf.format(Double.valueOf(goodsInfo.get(j).getNum()) * goodsInfo.get(j).getPrice()), size27);
                        }
                    }
                }
            }
            //接着处理未分组商品：首先判断是否还有未分组商品；进而添加
            boolean hasNoGroupGoods = false;
            for(int n=0;n<goodsInfo.size();n++){
                if(goodsInfo.get(n).getGroup_id()==null||"".equals(goodsInfo.get(n).getGroup_id())||"0".equals(goodsInfo.get(n).getGroup_id())){
                    hasNoGroupGoods = true;
                    break;
                }
            }

            if(hasNoGroupGoods){
                centerFormat("其他","-",data, size27);
                addFormatWmTest(data,"商品名称","单价","数量","金额",size27);
                for(int n=0;n<goodsInfo.size();n++){
                    if(goodsInfo.get(n).getGroup_id()==null||"".equals(goodsInfo.get(n).getGroup_id())||"0".equals(goodsInfo.get(n).getGroup_id())){
                        addFormatWmTest(data, goodsInfo.get(n).getName(),goodsInfo.get(n).getPrice()+"" ,goodsInfo.get(n).getNum(),numDf.format(Double.valueOf(goodsInfo.get(n).getNum()) * goodsInfo.get(n).getPrice()),size27);
                    }
                }
            }
        }else{
            //所有商品没分组
            centerFormat("商品信息","-",data, size27);
            addFormatWmTest(data,"商品名称","单价","数量","金额",size27);
            for(int n=0;n<goodsInfo.size();n++){
                addFormatWmTest(data, goodsInfo.get(n).getName(), goodsInfo.get(n).getPrice()+"",goodsInfo.get(n).getNum(),numDf.format(Double.valueOf(goodsInfo.get(n).getNum()) * goodsInfo.get(n).getPrice()),size27);
            }
        }
    }

    /**
     * 居中格式化添加数据
     * @param info
     * @param data
     */
    private static void centerFormat(String info, String elseChar,List<WmPintData> data,int textSize) {

        int totalLegth;
        if(textSize==27) {
            //totalLegth=27;
            totalLegth=32;
        }else if(textSize==35) {
            //totalLegth=21;
            totalLegth=24;
        }else {
            //totalLegth=27;
            totalLegth=32;
        }

        int length = info.length();
        int chinaNum = Utils.getChinaNum(info);
        int otherChar = length - chinaNum;
        int spacesNum=(totalLegth-otherChar-chinaNum*2)/2;
        String space="";
        for (int i = 0; i < spacesNum; i++) {
            space+=elseChar;
        }
        data.add(new WmPintData(size27,space+info+space));
    }

    /**
     * 格式化添加打印数据
     * @param data
     * @param left
     * @param center
     * @param right
     */
    public static void addFormatWmTest(List<WmPintData> data, String left, String center, String center2, String right, int textSize) {

        center2 = Utils.dropZero(center2);
        int chinaNum = Utils.getChinaNum(left);
        int nameLength = left.length() - chinaNum + chinaNum * 2;
        int sideLen;
        if(textSize==27) {
            sideLen = 13;
        }else {
            sideLen=10;
        }
        if(nameLength>=sideLen) {
            data.add(new WmPintData(textSize,left));
            addFormatWmTest(data, "", center, center2,right, textSize);
        }else {
            LogUtils.e("addFormat----","nameLength<sideLen");
            String space0="";
            String space1="";
            String space2="";

            int leftLength = (left + center).length();
            int leftChinaNum = Utils.getChinaNum(left + center);
            int leftOther = leftLength - leftChinaNum;

            int centerLength = String.valueOf(center2).length();
            int centerChinaNum = Utils.getChinaNum(center2+"");
            int centerOther = centerLength - centerChinaNum;

            int rightLength = right.length();
            int rightChinaNum = Utils.getChinaNum(right);
            int rightOther = rightLength - rightChinaNum;

            int leftTotal=0;
            int centerTotal=0;
            int rightTotal=0;

            if(textSize==27) {
                leftTotal=16;
                centerTotal=7;
                rightTotal=9;
            }else if(textSize==35) {
                leftTotal=13;
                centerTotal=4;
                rightTotal=7;
            }

            int rightSpaces = rightTotal - rightChinaNum*2 - rightOther;
            int centerSpaces = centerTotal - centerChinaNum*2 - centerOther;
            int leftSpaces = leftTotal - leftChinaNum*2 - leftOther;

            for (int a = 0; a < leftSpaces; a++) {
                space0+=" ";
            }
            for (int b = 0; b < centerSpaces; b++) {
                space1+=" ";
            }
            for (int c = 0; c < rightSpaces; c++) {
                space2+=" ";
            }
            data.add(new WmPintData(textSize,left+space0+center+space1+center2+space2+right));
        }
    }

    private static String formateYunDataForOrderDetail(String yunPrintData,List<String> headerData,int tag) {
        switch (tag){
            case 0://顾客联
                for(int i=0;i<headerData.size();i++){
                    if(judgetosideLine(headerData.get(i))){
                        if(headerData.get(i).contains("----------------------")){
                            yunPrintData += line32;
                        }else {
                            yunPrintData += centerFormatYunSize(1,headerData.get(i),"-");
                        }
                    }else if(i==0||i==1){
                        yunPrintData += centerFormatYunSize(1,headerData.get(i)," ");
                    }else if(i==2){
                        yunPrintData += printHorCenterAddSize3(centerFormatYunSize(3,(headerData.get(i))," "));
                    }else{
                        yunPrintData = addOripiceMsg(yunPrintData, headerData.get(i));
                    }
                }
                //添加底部打印信息
                yunPrintData += "收银SaaS系统提供商：门店派\n";
                yunPrintData += "联系电话：400-9600-567\n";
                yunPrintData += "乐推微网址：tui.younle.com\n";
                yunPrintData += line32;
                yunPrintData += "顾客联\n";
                break;
            case 1://商家联
                for(int i=0;i<headerData.size();i++){
                    if(judgetosideLine(headerData.get(i))){
                        if(headerData.get(i).contains("----------------------")){
                            yunPrintData += line32;
                        }else {
                            yunPrintData += centerFormatYunSize(1,headerData.get(i),"-");
                        }
                    }else{
                        if(i==0){
                            yunPrintData += centerFormatYunSize(1,headerData.get(i)," ");
                        }else if(i==1){
                        }else if(i==2){
                            yunPrintData += printHorCenterAddSize3(centerFormatYunSize(3,(headerData.get(i))," "));
                        }else{
                            yunPrintData = addOripiceMsg(yunPrintData, headerData.get(i));
                        }
                    }
                }
                //添加底部打印信息
                yunPrintData += "商家联\n";
                break;
        }
        return yunPrintData;
    }

    @NonNull
    private static String addOripiceMsg(String yunPrintData, String printMsg) {
        if(printMsg.contains("原单价")) {
            yunPrintData += printMsg+"\n\n";
        }else {
            yunPrintData += printMsg+"\n";
        }
        return yunPrintData;
    }

    private static boolean judgetosideLine(String lineData){
        if(lineData!=null){
            boolean s = lineData.startsWith("-");
            boolean e = lineData.endsWith("-");
            return (s && e);
        }else{
            return false;
        }
    }

    private static String formateYunDataForCS(String yunPrintData,List<String> headerData, List<String> memberData,int tag) {
        switch (tag){
            case 0://顾客联
                for(int i=0;i<headerData.size();i++){
                    String msg = headerData.get(i);
                    switch (msg){
                        case "支付方式:刷卡收款(记账)":
                            msg = "支付方式:刷卡收款";
                            break;
                        case "支付方式:现金收款(记账)":
                            msg = "支付方式:现金收款";
                            break;
                        case "支付方式:微信收款(记账)":
                            msg = "支付方式:微信收款";
                            break;
                        case "支付方式:支付宝收款(记账)":
                            msg = "支付方式:支付宝收款";
                            break;
                    }
                    if(judgetosideLine(msg)){
                        //yunPrintData += centerFormatYunSize(1,headerData.get(i),"-");
                        if(msg.contains("----------------------")){
                            yunPrintData += line32;
                        }else {
                            yunPrintData += centerFormatYunSize(1,msg,"-");
                        }
                    }else if(i==0||i==1){
                        yunPrintData += centerFormatYunSize(1,msg," ");
                    }else if(i==2){
                        yunPrintData += printHorCenterAddSize3(centerFormatYunSize(3,(msg)," "));
                    }else{
                        yunPrintData=addOripiceMsg(yunPrintData,headerData.get(i));
                    }
                }
                for(int i=0;i<memberData.size();i++){
                    if(memberData.get(i).contains("weixin")){
                        yunPrintData += YunPrintUtils.printQRCode(memberData.get(i));
                    }else{
                        yunPrintData += YunPrintUtils.centerFormatYunSize(1,memberData.get(i)," ");
                    }
                }
                //添加底部打印信息
                yunPrintData += "\n";
                yunPrintData += "收银SaaS系统提供商：门店派\n";
                yunPrintData += "联系电话：400-9600-567\n";
                yunPrintData += "乐推微网址：tui.younle.com\n";
                yunPrintData += line32;
                yunPrintData += "顾客联 \n";
                break;
            case 1://商家联
                for(int i=0;i<headerData.size();i++){
                    if(judgetosideLine(headerData.get(i))){
                        //yunPrintData += centerFormatYunSize(1,headerData.get(i),"-");
                        if(headerData.get(i).contains("----------------------")){
                            yunPrintData += line32;
                        }else {
                            yunPrintData += centerFormatYunSize(1,headerData.get(i),"-");
                        }
                    }else{
                        if(i==0){
                            yunPrintData += centerFormatYunSize(1,headerData.get(i)," ");
                        }else if(i==1){
                        }else if(i==2){
                            yunPrintData += printHorCenterAddSize3(centerFormatYunSize(3,(headerData.get(i))," "));
                        }else{
                            yunPrintData =addOripiceMsg(yunPrintData,headerData.get(i));
                        }
                    }
                }
                //添加底部打印信息
                yunPrintData += "商家联 \n";
                break;
        }
        return yunPrintData;
    }

    /**
     * 居中格式化添加数据
     * @param info
     */
    private static String centerFormatYunSize(int tag,String info, String elseChar) {
        int length = info.length();
        int chinaNum = Utils.getChinaNum(info);
        int otherChar = length - chinaNum;
        int spacesNum = LINE_SIZE_K4_24;
        switch (tag){
            case 1:
                spacesNum = (LINE_SIZE_K4_32 - otherChar - chinaNum*2)/2;
                break;
            case 2:
                spacesNum = (LINE_SIZE_K4_24 - otherChar - chinaNum*2)/2;
                break;
            case 3:
                spacesNum = (LINE_SIZE_K4_16 - otherChar - chinaNum*2)/2;
                break;
        }
        String space = "";
        for (int i = 0; i < spacesNum; i++) {
            space += elseChar;
        }
        return space+info+space+"\n";
    }

    /**
     * 调用云打印接口
     * @param context
     * @param printMsg
     */
    public static void YunToPrint(Context context, String printMsg){

        NetWorks netWorks = new NetWorks(context);
        try {
            LogUtils.e(TAG,"云打印：yunPrintData="+ printMsg);
            printMsg = URLEncoder.encode(printMsg,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        List<SavedPrinter> savedPrinters = (List<SavedPrinter>) SaveUtils.getObject(context, Constant.YUN_PRINTERS);
        String printer_json;
        if(savedPrinters !=null && savedPrinters.size()>0) {
            List<YunPrintBean> yun_print_list = new ArrayList<>();
            for(int i = 0; i< savedPrinters.size(); i++){
                LogUtils.e(TAG,"YunToPrint() i="+i);
                String printerId = savedPrinters.get(i).getPrinterId();
                String printerKey = savedPrinters.get(i).getPrinterKey();
                YunPrintBean yunPrintBean = new YunPrintBean();
                yunPrintBean.setPartner(Constant.YUN_PRINTER_PARTNER_ID);
                yunPrintBean.setApiKey(Constant.YUN_PRINTER_API_KEY);
                yunPrintBean.setMachine_code(printerId);
                yunPrintBean.setmKey(printerKey);
                yunPrintBean.setMsg(printMsg);
                yun_print_list.add(yunPrintBean);
            }
            printer_json = new Gson().toJson(yun_print_list);
            netWorks.YunPrint(1,printer_json,new NetWorks.OnNetCallBack(){
                @Override
                public void onError(Exception e, int flag) {
                    LogUtils.e(TAG,"云打印接口 flag="+flag+", onError()...Exception e="+e.toString());
                }

                @Override
                public void onResonse(String response, int flag) {
                    LogUtils.e(TAG,"云打印接口 flag="+flag+", onResonse() response="+response);
                }
            });
        }
    }
    /**
     * 调用云打印接口
     * @param context
     * @param printMap
     */
    public static void YunToPrintNew(Context context, Map<String,String> printMap,String customer,String merchant){

        NetWorks netWorks = new NetWorks(context);

        //获取本地存储的云打印机信息、
        List<SavedPrinter> savedPrinterses = (List<SavedPrinter>) SaveUtils.getObject(context, Constant.YUN_PRINTERS);
        if(savedPrinterses !=null && savedPrinterses.size()>0){
            for(int i = 0; i< savedPrinterses.size(); i++){
                List<YunPrintBean> yun_print_list = new ArrayList<>();
                String printer_json ="";
                String printerId = savedPrinterses.get(i).getPrinterId();
                String printerKey = savedPrinterses.get(i).getPrinterKey();
                String permission = savedPrinterses.get(i).getPrintPermission();
                String printMsg="";
                LogUtils.Log("当前打印机permission=="+permission+" key=="+printerKey+" id="+printerId+"  即将打印的权限c："+customer+" m:"+merchant);
                if(permission.contains(customer)) {
                    if(permission.contains(merchant)) {
                        //同时打印
                        printMsg =printMap.get(Constant.FOR_CUSTOMER)+printMap.get(FOR_MERCHANT);
                    }else {
                        //仅打印顾客联
                        printMsg=printMap.get(Constant.FOR_CUSTOMER);
                    }
                }else {
                    if(permission.contains(merchant)) {
                        //仅打印商家联
                        printMsg=printMap.get(FOR_MERCHANT);
                    }
                }

                if(TextUtils.equals("total",customer)) {//打印汇总
                    printMsg=printMap.get("total");
                }

                try {
                    printMsg = URLEncoder.encode(printMsg,"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                YunPrintBean yunPrintBean = new YunPrintBean();
                yunPrintBean.setPartner(Constant.YUN_PRINTER_PARTNER_ID);
                yunPrintBean.setApiKey(Constant.YUN_PRINTER_API_KEY);
                yunPrintBean.setMachine_code(printerId);
                yunPrintBean.setmKey(printerKey);
                yunPrintBean.setMsg(printMsg);
                yun_print_list.add(yunPrintBean);
                printer_json = new Gson().toJson(yun_print_list);

                requestToPrint(netWorks, printer_json);
            }
        }
    }
    private static void requestToPrint(NetWorks netWorks, String printer_json) {
        netWorks.YunPrint(1,printer_json,new NetWorks.OnNetCallBack(){
            @Override
            public void onError(Exception e, int flag) {
                LogUtils.Log("云打印接口 flag="+flag+", onError()...Exception e="+e.toString());

            }

            @Override
            public void onResonse(String response, int flag) {
                LogUtils.Log("云打印接口 flag="+flag+", onResonse() response="+response);

            }
        });
    }
    /**
     * 调用云打印接口
     * @param context
     * @param judeAuto 是否需要判断自动打印功能
     */
    public static void yunPrintOneByOneNew(Context context, List<YunPrintGroupBean> oneByOneList,
                                           String customer,String merchant,boolean judeAuto,Handler handler){
        NetWorks netWorks = new NetWorks(context);
        //获取本地存储的云打印机信息、
        List<SavedPrinter> savedPrinterses = (List<SavedPrinter>) SaveUtils.getObject(context, Constant.YUN_PRINTERS);
        if(savedPrinterses !=null && savedPrinterses.size()>0){
            for(int i = 0; i< savedPrinterses.size(); i++){
                String printPermission = savedPrinterses.get(i).getPrintPermission();
                String printGroupId = savedPrinterses.get(i).getPrintGroupId();
                if(printGroupId==null) {
                    printGroupId="0";
                }
                if(judeAuto) {//需要先判断是否自动打印
                    if(!printPermission.contains("1")) {//不需要自动打印，跳过该打印机
                        continue;
                    }else {//需要自动打印
                        needPrint(oneByOneList, customer, merchant, netWorks, savedPrinterses, i, printPermission, printGroupId,handler);
                    }
                }else {//直接判断是商家联顾客联
                    needPrint(oneByOneList, customer, merchant, netWorks, savedPrinterses, i, printPermission, printGroupId,handler);
                }
            }
        }
    }

    private static void needPrint(List<YunPrintGroupBean> oneByOneList, String customer, String merchant, NetWorks netWorks,
                                  final List<SavedPrinter> savedPrinterses, final int i, String printPermission, String printGroupId, final Handler handler) {
        String printStr="";
        if(printPermission.contains(customer)) {//需要打印顾客联
            if(printPermission.contains(merchant)) {//同时需要打印商家联
                for (int j = 0; j < oneByOneList.size(); j++) {
                    String groupId = oneByOneList.get(j).getGroupId();
                    if(TextUtils.equals(Constant.FOR_CUSTOMER,groupId)||TextUtils.equals("0",printGroupId)||TextUtils.equals(printGroupId,groupId)) {
                        printStr+=oneByOneList.get(j).getMsg();
                    }
                }
            }else {//仅打印顾客联
                printStr=oneByOneList.get(0).getMsg();
            }
        }else {
            if(printPermission.contains(merchant)) {//仅打印商家联
                for (int j = 1; j <oneByOneList.size() ; j++) {
                    if(TextUtils.equals("0",printGroupId)||TextUtils.equals(printGroupId,oneByOneList.get(j).getGroupId())) {
                        printStr+=oneByOneList.get(j).getMsg();
                    }
                }
            }
        }
        LogUtils.Log("printMsg="+printStr);
        try {
            printStr = URLEncoder.encode(printStr,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String printerId = savedPrinterses.get(i).getPrinterId();
        String printerKey = savedPrinterses.get(i).getPrinterKey();
        YunPrintBean yunPrintBean = new YunPrintBean();
        yunPrintBean.setPartner(Constant.YUN_PRINTER_PARTNER_ID);
        yunPrintBean.setApiKey(Constant.YUN_PRINTER_API_KEY);
        yunPrintBean.setMachine_code(printerId);
        yunPrintBean.setmKey(printerKey);
        yunPrintBean.setMsg(printStr);
        List<YunPrintBean> yun_print_list=new ArrayList<>();
        yun_print_list.add(yunPrintBean);
        String printer_json = new Gson().toJson(yun_print_list);
        if(!TextUtils.isEmpty(printer_json)) {
            netWorks.YunPrint(1,printer_json,new NetWorks.OnNetCallBack(){
                @Override
                public void onError(Exception e, int flag) {
                    LogUtils.Log("云打印接口 flag="+flag+", onError()...Exception e="+e.toString());

                }

                @Override
                public void onResonse(String response, int flag) {
                    if(handler!=null&&i==savedPrinterses.size()-1) {
                        handler.sendEmptyMessageDelayed(Constant.ONE_BY_ONE_YUN_PRINT,Constant.ONEBYONE_PRINT_DELAYTIME);
                    }
                    LogUtils.Log("云打印接口 flag="+flag+", onResonse() response="+response);
                }
            });
        }
    }

    /**
     * 自定义来单提醒1
     * @param mode 自定义来单提醒：0:静音，1:蜂鸣器提示，2:语音提示，3:跟随系统设定。
     * @return
     */
    public static String comeOrderRemind(int mode){
        return "<MA>"+ mode +"</MA>";
    }

    /**
     * 自定义来单提醒 2
     * @param mode
     * @return
     */
    public static String userDefinedcomeOrderRemind(int mode,int volune){
        return "<MS>"+ mode+","+ volune +"</MS>";
    }

    /**
     * 打印多联
     * @param n n=(1~9)
     * @return
     */
    public static String printCount(int n){
        return "<MN>"+n+"</MN>";
    }

    /**
     * 字体高度 内容增高为1.33倍，单行、多行均有效
     * @param str
     * @return
     */
    public static String typefaceHeigt1(String str){
        return "<FH>"+str+"</FH>";
    }
    public static String typefaceHeigt2(String str){
        return "<FH>"+str+"</FH>";
    }

    /**
     * 增宽为1.33倍，单行、多行均有效
     * @param str
     * @return
     */
    public static String typefacewight(String str){
        return "<FW>"+str+"</FW>";
    }
    //增宽为2.00倍，单行、多行均有效
    public static String typefacewight2(String str){
        return "<FW2>"+str+"</FW2>";
    }

    /**
     * 加粗内容，单行、多行均有效
     * @param str
     * @return
     */
    public static String typefacebold(String str){
        return "<FB>"+str+"</FB>";
    }

    /**
     * <FH><FW>str</FW></FH>的组合，单行、多行均有效
     * @param str
     * @return
     */
    public static String typefacesize1(String str){
        return "<FS>"+str+"</FS>";
    }
    //加大当前行
    public static String typefacesize2(String str){
        return "@@2"+str;
    }
    //<FH2><FW2>str</FW2></FH2>的组合，单行、多行均有效
    public static String typefacesize3(String str){
        return "<FS2>"+str+"</FS2>";
    }

    /**
     * 打印条码
     * @param str 条形码内容，13位数字，中文或其他字符无效，数据不足13位自动补0，补齐13位
     * @return
     */
    public static String printBarCode(String str){
        return "<BR>"+str+"</BR>";
    }

    /**
     * 打印二维码
     * @param str 二维码内容，内容不超过192个英文字符或96个汉语字符
     * @return
     */
    public static String printQRCode(String str){
        return "<QR>"+str+"</QR>";
    }

    /**
     * 居中打印
     * @param str
     * @return
     */
    public static String printHorCenter(String str){
        return "<center>"+str+"</center>";
    }
    public static String printHorCenter2(String str){
        return "<center>"+str+"</center>";
    }
    public static String printHorCenterAddSize1(String str){
        return "<FS><center>"+str+"</center></FS>";
    }
    public static String printHorCenterAddSize2(String str){
        //<FH2><FB><center>居中</center></FB></FH2>
        return "<FH2><FB>"+str+"</FB></FH2>";
    }
    public static String printHorCenterAddSize3(String str){
        //<FH2><FB><center>居中</center></FB></FH2>
        return "<FS2>"+str+"</FS2>";
    }

    /**
     * 右侧打印
     * @param str
     * @return
     */
    public static String printHorRight(String str){
        return "<right>"+str+"</right>";
    }
    public static String printHorRightaddSize1(String str){
        return "<FS><right>"+str+"</right></FS>";
    }
    public static String printHorRightaddSize2(String str){
        return "<FS2><right>"+str+"</right></FS2>";
    }

    /**
     * 13位数字，中文或其他字符无效，数据不足13位自动补0，补齐13位
     * @param str
     * @return
     */
    public static String BarCodeFormate(String str){
        int length = str.length();
        if(length < 13){
            for(int i=0;i<13-length;i++){
                str = str+"0";
            }
        }
        return str;
    }

    public static Map<String,String> yunPrintTotal(PrintTotalBean printTotalBean, String start, String end) {
        List<WmPintData> data=new ArrayList<>();
        //收款渠道
        PrintTotalBean.PricechannelBean channel = printTotalBean.getPricechannel();
        //会员充值
        PrintTotalBean.VippriceBean viplog = printTotalBean.getVipprice();
        //商品销售记录
        PrintTotalBean.GoodssaleBean goodssale = printTotalBean.getGoodssale();


        data.add(new WmPintData("经营数据汇总",size35,WmPintData.CENTER));
        data.add(new WmPintData(size27," "));
        data.add(new WmPintData("商户名称:"+Constant.STORE_P+Constant.STORE_M,size27,WmPintData.LEFT));
        data.add(new WmPintData(size27,"区间:"+start+"至"+end));
        data.add(new WmPintData(size27,"打印时间:"+Utils.getCurrentM(System.currentTimeMillis())));
        wmCenterFormat("收入概况","-",data,size27);

        data.add(new WmPintData(size27, "微信支付"));
        data.add(new WmPintData(size27,"订单数:"+ channel.getWechatnum()+"  总金额:"+channel.getWechatpay()));
        data.add(new WmPintData(size27," "));

        data.add(new WmPintData(size27, "支付宝"));
        data.add(new WmPintData(size27,"订单数:"+channel.getAlinum()+"  总金额:"+channel.getAlipay()));
        data.add(new WmPintData(size27," "));

        data.add(new WmPintData(size27, "现金收款"));
        data.add(new WmPintData(size27,"订单数:"+channel.getCashnum()+"  总金额:"+channel.getCashpay()));
        data.add(new WmPintData(size27," "));

        data.add(new WmPintData(size27, "刷卡收款"));
        data.add(new WmPintData(size27,"订单数:"+channel.getSwingnum()+"  总金额:"+channel.getSwingpay()));
        data.add(new WmPintData(size27," "));

        data.add(new WmPintData(size27, "收入汇总"));
        data.add(new WmPintData(size27, "订单数:" + channel.getTotolnum() + "  总金额:" + channel.getTotolpay()));
        if(Constant.OPENED_PERMISSIONS.contains("2")) {
            wmCenterFormat("商品销售概况","-",data,size27);
            List<PrintTotalBean.GoodssaleBean.SaletypeinfoBean> saletypeinfo = goodssale.getSaletypeinfo();
            if(saletypeinfo!=null&&saletypeinfo.size()>0) {
                for (int i = 0; i < saletypeinfo.size(); i++) {
                    PrintTotalBean.GoodssaleBean.SaletypeinfoBean saletypeinfoBean = saletypeinfo.get(i);
                    if(saletypeinfoBean!=null) {
                        data.add(new WmPintData(size27, saletypeinfoBean.getTypename()));
                        data.add(new WmPintData(size27,"售出商品数:"+saletypeinfoBean.getNum()+"  总金额:"+saletypeinfoBean.getOriprice()));
                        data.add(new WmPintData(size27," "));
                    }
                }
            }
            data.add(new WmPintData(size27, "商品售出汇总"));
            data.add(new WmPintData(size27,"售出总数:"+goodssale.getSaletotal().getTotalnum()+"  总金额:"+goodssale.getSaletotal().getTotaloriprice()));
            data.add(new WmPintData(size27,"实收金额："+goodssale.getSaletotal().getTotalprice()));
            data.add(new WmPintData(size27," "));

        }
        if(Constant.OPENED_PERMISSIONS.contains("4")) {
            wmCenterFormat("会员充值与消费","-",data,size27);
            data.add(new WmPintData(size27, "会员储值总额"));
            data.add(new WmPintData(size27,"订单数:"+ viplog.getGetnum()));
            data.add(new WmPintData(size27,"总充值:"+viplog.getGettotal()+" 总赠送:"+viplog.getTotalgive()));
            data.add(new WmPintData(size27," "));

            data.add(new WmPintData(size27, "储值消费总额"));
            data.add(new WmPintData(size27,"订单数:"+viplog.getPaynum()+"  总金额:"+viplog.getPaytotal()));
            data.add(new WmPintData(size27," "));
        }

        String yunTotalMsg = getYunLineData(data, true, 0);
        Map yunTotalMap=new HashMap();
        yunTotalMap.put("total",yunTotalMsg);
        return yunTotalMap;
        /*List<WmPintData> data=new ArrayList<>();
        data.add(new WmPintData("收款汇总单",size35,WmPintData.CENTER));
        data.add(new WmPintData(size27," "));
        data.add(new WmPintData("商户名称:"+Constant.STORE_P+Constant.STORE_M,size27,WmPintData.LEFT));
        data.add(new WmPintData(size27,"区间:"+start+"至"+end));
        data.add(new WmPintData(size27,"打印时间:"+Utils.getCurrentM(System.currentTimeMillis())));
        wmCenterFormat("总数据","-",data,size27);
        data.add(new WmPintData(size27, "总数据"));
        data.add(new WmPintData(size27, "订单数：" + printTotalBean.getT_num() + "  总金额：" + printTotalBean.getT_price()));
        wmCenterFormat("各渠道数据","-",data,size27);

        data.add(new WmPintData(size27, "微信支付"));
        data.add(new WmPintData(size27,"订单数："+printTotalBean.getWechatnum()+"  总金额："+printTotalBean.getWechatpay()));
        data.add(new WmPintData(size27," "));

        data.add(new WmPintData(size27, "支付宝"));
        data.add(new WmPintData(size27,"订单数："+printTotalBean.getAlinum()+"  总金额："+printTotalBean.getAlipay()));
        data.add(new WmPintData(size27," "));

        data.add(new WmPintData(size27, "现金收款"));
        data.add(new WmPintData(size27,"订单数："+printTotalBean.getCashnum()+"  总金额："+printTotalBean.getCashpay()));
        data.add(new WmPintData(size27," "));

        int separate = printTotalBean.getSeparate();
        if(0==separate) {//区分
            data.add(new WmPintData(size27, "银联借记卡收款"));
            data.add(new WmPintData(size27,"订单数："+printTotalBean.getDebitnum()+"  总金额："+printTotalBean.getDebitpay()));
            data.add(new WmPintData(size27," "));

            data.add(new WmPintData(size27, "银联贷记卡收款"));
            data.add(new WmPintData(size27,"订单数："+printTotalBean.getCreditnum()+"  总金额："+printTotalBean.getCreditpay()));
        }else if(1==separate) {//不区分
            data.add(new WmPintData(size27, "刷卡收款"));
            data.add(new WmPintData(size27,"订单数："+printTotalBean.getSwingnum()+"  总金额："+printTotalBean.getSwingpay()));
        }

        String yunTotalMsg = getYunLineData(data, true, 0);
        Map yunTotalMap=new HashMap();
        yunTotalMap.put("total",yunTotalMsg);
        return yunTotalMap;*/
    }
}
