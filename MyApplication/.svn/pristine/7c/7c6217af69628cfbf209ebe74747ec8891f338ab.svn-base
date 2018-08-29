//package com.younle.younle624.myapplication.utils.printmanager;
//
//import com.younle.younle624.myapplication.constant.Constant;
//import com.younle.younle624.myapplication.domain.UnPayDetailsBean;
//import com.younle.younle624.myapplication.utils.LogUtils;
//import com.younle.younle624.myapplication.utils.Utils;
//
//import java.text.DateFormat;
//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.util.List;
//
//import static com.younle.younle624.myapplication.constant.Constant.STORE_TEL;
//
///**
// * Created by Administrator on 2017/10/26.
// */
//
//public class LocalprintTrantation {
//    private String line32="--------------------------------";
//    private String line27="---------------------------";
//    private String line35="---------------------";
//    int size27=27;
//    int size22=22;
//    int size35=35;
//    int size40=43;
//    int size50=50;
//    private String[] bottomData={"收银SaaS系统提供商:门店派","联系电话:400-9600-567",
//            "乐推微网址:tui.younle.com",line27,"顾客联"};
//    public static LocalprintTrantation instance;
//    private DateFormat df4 = new SimpleDateFormat("MM/dd HH:mm");
//    private DecimalFormat numDf = new DecimalFormat("0.00");
//    private boolean isbt;
//    public int textSize=27;
//    public static LocalprintTrantation getInstance(){
//        if(instance==null) {
//            instance=new LocalprintTrantation();
//        }
//        return instance;
//    }
//
//    public void firstComitFormatData(UnPayDetailsBean orderBean, List<String> headData, boolean isbt, boolean isGroup){
//        this.isbt=isbt;
//        textSize=27;
//        //来自第一次提交到总订单，并且不是只有房间
//        headData.clear();
//        List<UnPayDetailsBean.MsgBean.OrderGoodsBean> order_entity = orderBean.getMsg().getOrder_goods();
//        List<UnPayDetailsBean.MsgBean.OrderRoomsBean> order_rooms = orderBean.getMsg().getOrder_rooms();
//        if ((order_rooms != null && order_rooms.size() > 0) && (order_entity == null || order_entity.size() <= 0)) {
//            return;
//        } else {
//            headData.add(Constant.STORE_P + Constant.STORE_M);
//            headData.add("电话：" + STORE_TEL);
//            if("".equals(Constant.CURRENT_ORDER_ROOM)) {
//                headData.add("订单序号:" + Constant.CURRENT_ORDER_NUM);
//            }else {
//                //1代表桌台 0代表房间
//                if(order_rooms.get(0).getRoom_type()!=null){
//                    if("1".equals(order_rooms.get(0).getRoom_type())){
//                        headData.add("桌台:"+Constant.CURRENT_ORDER_ROOM);
//                    }else{
//                        headData.add("房间:"+Constant.CURRENT_ORDER_ROOM);
//                    }
//                }else{
//                    headData.add("房间:"+Constant.CURRENT_ORDER_ROOM);
//                }
//            }
//            if(isbt){
//                headData.add(line32);
//            }else{
//                headData.add(line27);
//            }
//            //headData.add("下单时间:" + Utils.getCurrentMin());
//            if(orderBean.getMsg().getAddtime()!=null){
//                headData.add("下单时间:" + Utils.getPtintTime(orderBean.getMsg().getAddtime() + "000"));
//            }else{
//                headData.add("下单时间:" + Utils.getCurrentMin());
//            }
//            if(!"".equals(Constant.CURRENT_ORDER_ROOM)){
//                headData.add("订单序号:"+Constant.CURRENT_ORDER_NUM);
//            }
//            headData.add("操作员:" + Constant.USER_ACCOUNT);
//            if(orderBean.getMsg().getRemark()!=null && !"".equals(orderBean.getMsg().getRemark())){
//                headData.add("备注："+orderBean.getMsg().getRemark());
//            }
//
//            if(isGroup){//有分组
//                //在添加数据到data里前 把数据整理好分组商品:
//                String name;
//                double num;
//                double unit_price;
//                double total_fee;
//                int group_num = 0;
//                //判断是否存在有分组的商品：
//                boolean isHasGroupGoods = false;
//                if(orderBean.getMsg().getGroup_info()!=null){
//                    group_num = orderBean.getMsg().getGroup_info().size();
//                }
//                if (order_entity != null && order_entity.size() > 0) {
//                    for(int k=0;k<group_num;k++){//分组循环
//                        if(orderBean.getMsg().getGroup_info().get(k).getGroup_id()!=null&&Integer.valueOf(orderBean.getMsg().getGroup_info().get(k).getGroup_id())>0){
//
//                            for(int j=0;j<order_entity.size();j++){
//                                if(orderBean.getMsg().getGroup_info().get(k).getGroup_id().equals(order_entity.get(j).getGroup_id())){
//                                    isHasGroupGoods = true;
//                                    break;
//                                }
//                            }
//                            if(isHasGroupGoods){
//                                //添加分组头
//                                centerFormat(orderBean.getMsg().getGroup_info().get(k).getGroup_name(), "-", headData);
//                                addFormatTest(headData,"商品名称","单价","数量","金额");
//                                for (int i = 0; i < order_entity.size(); i++) {//添加分组数据循环
//                                    if(orderBean.getMsg().getGroup_info().get(k).getGroup_id().equals(order_entity.get(i).getGroup_id())/*&&!"".equals(order_entity.get(i).getGroup_id())*/){
//                                        DecimalFormat decimalFormat = new DecimalFormat("0.00");//如果小数不足2位,会以0补足.
//                                        String format_total_fee = decimalFormat.format(order_entity.get(i).getTotal_fee());
//                                        name = order_entity.get(i).getGoods_name();
//                                        unit_price = order_entity.get(i).getGoods_price();
//                                        num = order_entity.get(i).getGoods_num();
//                                        total_fee = Double.valueOf(format_total_fee);
//                                        addFormat(headData, name, unit_price+"",num, String.valueOf(total_fee));
//                                        addvipPriceNotice(headData,unit_price,order_entity.get(i).getGoods_ori_price());
//                                    }
//                                }
//                                headData.add("                ");
//                            }
//                        }
//                    }
//                    LogUtils.e("firstComitFormatData","有分组...3");
//                    boolean ishasOtherGoods = false;
//                    //循环添加完分组数据再添加未分组数据
//                    for(int h=0;h<order_entity.size();h++){
//                        if(order_entity.get(h).getGroup_id()==null||"0".equals(order_entity.get(h).getGroup_id())||"".equals(order_entity.get(h).getGroup_id())){
//                            ishasOtherGoods = true;
//                            break;
//                        }
//                    }
//                    if(ishasOtherGoods){
//                        if(group_num>0&&isHasGroupGoods){
//                            centerFormat("其他","-",headData);
//                            addFormatTest(headData, "商品名称","单价", "数量", "金额");
//                        }else{
//                            centerFormat("商品信息", "-", headData);
//                            addFormatTest(headData, "商品名称","单价", "数量", "金额");
//                        }
//                        for(int j=0;j<order_entity.size();j++){
//                            if(order_entity.get(j).getGroup_id()==null||"0".equals(order_entity.get(j).getGroup_id())||"".equals(order_entity.get(j).getGroup_id())){
//                                DecimalFormat decimalFormat = new DecimalFormat("0.00");//如果小数不足2位,会以0补足.
//                                String format_total_fee = decimalFormat.format(order_entity.get(j).getTotal_fee());
//                                name = order_entity.get(j).getGoods_name();
//                                num = order_entity.get(j).getGoods_num();
//                                unit_price = order_entity.get(j).getGoods_price();
//                                total_fee = Double.valueOf(format_total_fee);
//                                addFormat(headData, name, unit_price+"",num, String.valueOf(total_fee));
//                                addvipPriceNotice(headData,unit_price,order_entity.get(j).getGoods_ori_price());
//
//                            }
//                        }
//                    }
//                }
//            } else {//没有分组
//                centerFormat("商品信息", "-", headData);
//                addFormatTest(headData, "商品名称","单价", "数量", "金额");
//                if (order_entity != null && order_entity.size() > 0) {
//                    for (int i = 0; i < order_entity.size(); i++) {
//                        String name = order_entity.get(i).getGoods_name();
//                        double num = order_entity.get(i).getGoods_num();
//                        double unit_price = order_entity.get(i).getGoods_price();
//                        double total_fee = order_entity.get(i).getTotal_fee();
//                        addFormat(headData, name, unit_price+"",num, String.valueOf(total_fee));
//                        addvipPriceNotice(headData,unit_price,order_entity.get(i).getGoods_ori_price());
//
//                    }
//                }
//            }
//            if(isbt){
//                headData.add(line32);
//            }else{
//                headData.add(line27);
//            }
//        }
//    }
//}
