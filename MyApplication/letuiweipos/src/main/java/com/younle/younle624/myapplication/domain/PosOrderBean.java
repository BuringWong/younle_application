package com.younle.younle624.myapplication.domain;

import java.util.List;

/**
 * Created by 我是奋斗 on 2016/5/18.
 * 微信/e-mail:tt090423@126.com
 *
 * 订单界面pos订单的实体类
 */
public class PosOrderBean {
   /**
     * auth : ["1","2","3","4","5","6","7"]
     * code : 200
     * msg : {"goodsNameList":[{"category":[{"categoryName":"水果","goodsList":[{"id":"23","name":"冬枣"},{"id":"26","name":"龙眼"}]}],"goodsType":0,"typeName":"全部"},{"category":[{"categoryName":"水果","goodsList":[{"id":"23","name":"冬枣"},{"id":"26","name":"龙眼"}]},{"categoryName":"茄果","goodsList":[{"id":"28","name":"荷兰豆"},{"id":"27","name":"蚕豆"}]},{"categoryName":"酒水","goodsList":[{"id":"39","name":"剑南春"},{"id":"40","name":"农夫山泉"}]}],"goodsType":1,"typeName":"实物"},{"category":[{"categoryName":"KTV","goodsList":[{"id":"71","name":"精品包间6小时"},{"id":"62","name":"小包2小时"}]},{"categoryName":"洗浴汗蒸","goodsList":[{"id":"56","name":"三人汗蒸"},{"id":"58","name":"单人汗蒸刮痧"}]}],"goodsType":2,"typeName":"服务"},{"category":[{"categoryName":"桌台","goodsList":[{"id":"56","name":"桌台11号"},{"id":"23","name":"桌台1号"}]},{"categoryName":"标准间","goodsList":[{"id":"39","name":"标准4号"},{"id":"37","name":"标准2号"}]}],"goodsType":3,"typeName":"房间"},{"category":[{"categoryName":"水果","goodsList":[{"id":"23","name":"冬枣"},{"id":"26","name":"龙眼"}]}],"goodsType":3,"typeName":"未知商品"}],"income":10021.47,"orderList":[{"date":"2017-01-17","detail":[{"dayOrderNum":"A-3","goodsNum":"1","income":8,"isCancel":"0","orderId":"148463434105954","payType":"现金支付","time":"14:25:42"},{"dayOrderNum":"A-2","goodsNum":"1","income":0.01,"isCancel":"0","orderId":"148463181881854","payType":"现金支付","time":"13:43:40"}],"income":8.01},{"date":"2017-01-16","detail":[{"dayOrderNum":"A-17","goodsNum":"1","income":0.8,"isCancel":"0","orderId":"148456868320735","payType":"现金支付","time":"20:11:24"},{"dayOrderNum":"A-16","goodsNum":"3","income":37,"isCancel":"0","orderId":"148455115977634","payType":"现金支付","time":"15:19:34"}],"income":37.8},{"date":"2017-01-12","detail":[{"dayOrderNum":"A-1","goodsNum":"6","income":2090,"isCancel":"0","orderId":"148419009654","payType":"现金支付","time":"11:01:56"},{"dayOrderNum":"A-5","goodsNum":"5","income":76,"isCancel":"0","orderId":"148412871354","payType":"现金支付","time":"10:58:18"}],"income":2166},{"date":"2017-01-11","detail":[{"dayOrderNum":"A-2","goodsNum":"50","income":1064.6,"isCancel":"0","orderId":"148377649437","payType":"现金支付","time":"17:10:37"},{"dayOrderNum":"A-1","goodsNum":"0","income":8,"isCancel":"0","orderId":"148412534086254","payType":"刷卡支付","time":"17:02:22"},{"dayOrderNum":"A-3","goodsNum":"69","income":3030.6,"isCancel":"0","orderId":"148377664237","payType":"现金支付","time":"17:00:36"},{"dayOrderNum":"A-4","goodsNum":"15","income":762,"isCancel":"0","orderId":"148377754637","payType":"现金支付","time":"16:57:37"},{"dayOrderNum":"A-2","goodsNum":"34","income":269.6,"isCancel":"0","orderId":"148404118154","payType":"现金支付","time":"16:43:16"},{"dayOrderNum":"A-3","goodsNum":"12","income":191.6,"isCancel":"0","orderId":"148404214454","payType":"现金支付","time":"16:41:49"},{"dayOrderNum":"A-4","goodsNum":"11","income":776,"isCancel":"0","orderId":"148404498354","payType":"现金支付","time":"16:40:11"},{"dayOrderNum":"A-5","goodsNum":"66","income":1207.3,"isCancel":"0","orderId":"148404780154","payType":"现金支付","time":"16:35:08"}],"income":7309.7},{"date":"2017-01-09","detail":[{"dayOrderNum":"B-128","goodsNum":"0","income":8,"isCancel":"0","orderId":"148396365692354","payType":"现金支付","time":"20:07:36"}],"income":8}],"saled":55}
     */

    private int code;
    /**
     * goodsNameList : [{"category":[{"categoryName":"水果","goodsList":[{"id":"23","name":"冬枣"},{"id":"26","name":"龙眼"}]}],"goodsType":0,"typeName":"全部"},{"category":[{"categoryName":"水果","goodsList":[{"id":"23","name":"冬枣"},{"id":"26","name":"龙眼"}]},{"categoryName":"茄果","goodsList":[{"id":"28","name":"荷兰豆"},{"id":"27","name":"蚕豆"}]},{"categoryName":"酒水","goodsList":[{"id":"39","name":"剑南春"},{"id":"40","name":"农夫山泉"}]}],"goodsType":1,"typeName":"实物"},{"category":[{"categoryName":"KTV","goodsList":[{"id":"71","name":"精品包间6小时"},{"id":"62","name":"小包2小时"}]},{"categoryName":"洗浴汗蒸","goodsList":[{"id":"56","name":"三人汗蒸"},{"id":"58","name":"单人汗蒸刮痧"}]}],"goodsType":2,"typeName":"服务"},{"category":[{"categoryName":"桌台","goodsList":[{"id":"56","name":"桌台11号"},{"id":"23","name":"桌台1号"}]},{"categoryName":"标准间","goodsList":[{"id":"39","name":"标准4号"},{"id":"37","name":"标准2号"}]}],"goodsType":3,"typeName":"房间"},{"category":[{"categoryName":"水果","goodsList":[{"id":"23","name":"冬枣"},{"id":"26","name":"龙眼"}]}],"goodsType":3,"typeName":"未知商品"}]
     * income : 10021.47
     * orderList : [{"date":"2017-01-17","detail":[{"dayOrderNum":"A-3","goodsNum":"1","income":8,"isCancel":"0","orderId":"148463434105954","payType":"现金支付","time":"14:25:42"},{"dayOrderNum":"A-2","goodsNum":"1","income":0.01,"isCancel":"0","orderId":"148463181881854","payType":"现金支付","time":"13:43:40"}],"income":8.01},{"date":"2017-01-16","detail":[{"dayOrderNum":"A-17","goodsNum":"1","income":0.8,"isCancel":"0","orderId":"148456868320735","payType":"现金支付","time":"20:11:24"},{"dayOrderNum":"A-16","goodsNum":"3","income":37,"isCancel":"0","orderId":"148455115977634","payType":"现金支付","time":"15:19:34"}],"income":37.8},{"date":"2017-01-12","detail":[{"dayOrderNum":"A-1","goodsNum":"6","income":2090,"isCancel":"0","orderId":"148419009654","payType":"现金支付","time":"11:01:56"},{"dayOrderNum":"A-5","goodsNum":"5","income":76,"isCancel":"0","orderId":"148412871354","payType":"现金支付","time":"10:58:18"}],"income":2166},{"date":"2017-01-11","detail":[{"dayOrderNum":"A-2","goodsNum":"50","income":1064.6,"isCancel":"0","orderId":"148377649437","payType":"现金支付","time":"17:10:37"},{"dayOrderNum":"A-1","goodsNum":"0","income":8,"isCancel":"0","orderId":"148412534086254","payType":"刷卡支付","time":"17:02:22"},{"dayOrderNum":"A-3","goodsNum":"69","income":3030.6,"isCancel":"0","orderId":"148377664237","payType":"现金支付","time":"17:00:36"},{"dayOrderNum":"A-4","goodsNum":"15","income":762,"isCancel":"0","orderId":"148377754637","payType":"现金支付","time":"16:57:37"},{"dayOrderNum":"A-2","goodsNum":"34","income":269.6,"isCancel":"0","orderId":"148404118154","payType":"现金支付","time":"16:43:16"},{"dayOrderNum":"A-3","goodsNum":"12","income":191.6,"isCancel":"0","orderId":"148404214454","payType":"现金支付","time":"16:41:49"},{"dayOrderNum":"A-4","goodsNum":"11","income":776,"isCancel":"0","orderId":"148404498354","payType":"现金支付","time":"16:40:11"},{"dayOrderNum":"A-5","goodsNum":"66","income":1207.3,"isCancel":"0","orderId":"148404780154","payType":"现金支付","time":"16:35:08"}],"income":7309.7},{"date":"2017-01-09","detail":[{"dayOrderNum":"B-128","goodsNum":"0","income":8,"isCancel":"0","orderId":"148396365692354","payType":"现金支付","time":"20:07:36"}],"income":8}]
     * saled : 55
     */

    private MsgBean msg;
    private List<String> auth;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public List<String> getAuth() {
        return auth;
    }

    public void setAuth(List<String> auth) {
        this.auth = auth;
    }

    public static class MsgBean {
        private double income;
        private int saled;
        private int hasMemberSys;
        private int selectTime;

        public int getSelectTime() {
            return selectTime;
        }

        public void setSelectTime(int selectTime) {
            this.selectTime = selectTime;
        }

        /**
         * category : [{"categoryName":"水果","goodsList":[{"id":"23","name":"冬枣"},{"id":"26","name":"龙眼"}]}]
         * goodsType : 0
         * typeName : 全部
         */

        private List<GoodsNameListBean> goodsNameList;
        /**
         * date : 2017-01-17
         * detail : [{"dayOrderNum":"A-3","goodsNum":"1","income":8,"isCancel":"0","orderId":"148463434105954",
         * "payType":"现金支付","time":"14:25:42"},{"dayOrderNum":"A-2","goodsNum":"1","income":0.01,
         * "isCancel":"0","orderId":"148463181881854","payType":"现金支付","time":"13:43:40"}]
         * income : 8.01
         */

        private List<OrderListBean> orderList;

        public double getIncome() {
            return income;
        }

        public void setIncome(double income) {
            this.income = income;
        }

        public int getSaled() {
            return saled;
        }

        public void setSaled(int saled) {
            this.saled = saled;
        }

        public int getHasMemberSys() {
            return hasMemberSys;
        }

        public void setHasMemberSys(int hasMemberSys) {
            this.hasMemberSys = hasMemberSys;
        }

        public List<GoodsNameListBean> getGoodsNameList() {
            return goodsNameList;
        }

        public void setGoodsNameList(List<GoodsNameListBean> goodsNameList) {
            this.goodsNameList = goodsNameList;
        }

        public List<OrderListBean> getOrderList() {
            return orderList;
        }

        public void setOrderList(List<OrderListBean> orderList) {
            this.orderList = orderList;
        }

        public static class GoodsNameListBean {
            private int goodsType;
            private String typeName;
            /**
             * categoryName : 水果
             * goodsList : [{"id":"23","name":"冬枣"},{"id":"26","name":"龙眼"}]
             */

            private List<CategoryBean> category;

            public int getGoodsType() {
                return goodsType;
            }

            public void setGoodsType(int goodsType) {
                this.goodsType = goodsType;
            }

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public List<CategoryBean> getCategory() {
                return category;
            }

            public void setCategory(List<CategoryBean> category) {
                this.category = category;
            }

            public static class CategoryBean {

                /**
                 * id : 23
                 * name : 冬枣
                 */
                private String categoryName;
                private boolean isCheched;
                private List<GoodsListBean> goodsList;

                public boolean isCheched() {
                    return isCheched;
                }

                public void setIsCheched(boolean isCheched) {
                    this.isCheched = isCheched;
                }

                public String getCategoryName() {
                    return categoryName;
                }

                public void setCategoryName(String categoryName) {
                    this.categoryName = categoryName;
                }

                public List<GoodsListBean> getGoodsList() {
                    return goodsList;
                }

                public void setGoodsList(List<GoodsListBean> goodsList) {
                    this.goodsList = goodsList;
                }

                public static class GoodsListBean {
                    private String id;
                    private String name;
                    private String goodsType;
                    private String sizeId;
                    private boolean isCheck;

                    public String getSizeId() {
                        return sizeId;
                    }

                    public void setSizeId(String sizeId) {
                        this.sizeId = sizeId;
                    }

                    public void setCheck(boolean check) {
                        isCheck = check;
                    }

                    public String getGoodsType() {
                        return goodsType;
                    }

                    public void setGoodsType(String goodsType) {
                        this.goodsType = goodsType;
                    }

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public boolean isCheck() {
                        return isCheck;
                    }

                    public void setIsCheck(boolean isCheck) {
                        this.isCheck = isCheck;
                    }
                }
            }
        }

        public static class OrderListBean {
            private String date;
            private double income;

            private double dayIncome;
            private String dayOrderNum;
            private String goodsNum;
            private String isCancel;
            private String orderId;
            private String payType;
            private String payTool;
            private String time;
            private String isRechargeLog;
            private String roomName;
            private String remark;

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public double getDayIncome() {
                return dayIncome;
            }

            public void setDayIncome(double dayIncome) {
                this.dayIncome = dayIncome;
            }

            public String getDayOrderNum() {
                return dayOrderNum;
            }

            public void setDayOrderNum(String dayOrderNum) {
                this.dayOrderNum = dayOrderNum;
            }

            public String getGoodsNum() {
                return goodsNum;
            }

            public void setGoodsNum(String goodsNum) {
                this.goodsNum = goodsNum;
            }

            public double getIncome() {
                return income;
            }

            public void setIncome(double income) {
                this.income = income;
            }

            public String getIsCancel() {
                return isCancel;
            }

            public void setIsCancel(String isCancel) {
                this.isCancel = isCancel;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getPayType() {
                return payType;
            }

            public void setPayType(String payType) {
                this.payType = payType;
            }

            public String getPayTool() {
                return payTool;
            }

            public void setPayTool(String payTool) {
                this.payTool = payTool;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getIsRechargeLog() {
                return isRechargeLog;
            }

            public void setIsRechargeLog(String isRechargeLog) {
                this.isRechargeLog = isRechargeLog;
            }

            public String getRoomName() {
                return roomName;
            }

            public void setRoomName(String roomName) {
                this.roomName = roomName;
            }

            @Override
            public String toString() {
                return "OrderListBean{" +
                        "date='" + date + '\'' +
                        ", dayIncome=" + dayIncome +
                        ", dayOrderNum='" + dayOrderNum + '\'' +
                        ", goodsNum='" + goodsNum + '\'' +
                        ", income=" + income +
                        ", isCancel='" + isCancel + '\'' +
                        ", orderId='" + orderId + '\'' +
                        ", payType='" + payType + '\'' +
                        ", payTool='" + payTool + '\'' +
                        ", time='" + time + '\'' +
                        ", isRechargeLog='" + isRechargeLog + '\'' +
                        ", roomName='" + roomName + '\'' +
                        '}';
            }
            /**
             * dayOrderNum : A-3
             * goodsNum : 1
             * income : 8
             * isCancel : 0
             * orderId : 148463434105954
             * payType : 现金支付
             * time : 14:25:42
             */
           /*

            private List<DetailBean> detail;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public double getIncome() {
                return income;
            }

            public void setIncome(double income) {
                this.income = income;
            }

            public List<DetailBean> getDetail() {
                return detail;
            }

            public void setDetail(List<DetailBean> detail) {
                this.detail = detail;
            }

            public static class DetailBean {


                public String getPayTool() {
                    return payTool;
                }

                public void setPayTool(String payTool) {
                    this.payTool = payTool;
                }

                public String getRoomName() {
                    return roomName;
                }

                public void setRoomName(String roomName) {
                    this.roomName = roomName;
                }

                public String getIsRechargeLog() {
                    return isRechargeLog;
                }

                public void setIsRechargeLog(String isRechargeLog) {
                    this.isRechargeLog = isRechargeLog;
                }

                public String getDayOrderNum() {
                    return dayOrderNum;
                }

                public void setDayOrderNum(String dayOrderNum) {
                    this.dayOrderNum = dayOrderNum;
                }

                public String getGoodsNum() {
                    return goodsNum;
                }

                public void setGoodsNum(String goodsNum) {
                    this.goodsNum = goodsNum;
                }

                public double getIncome() {
                    return income;
                }

                public void setIncome(double income) {
                    this.income = income;
                }

                public String getIsCancel() {
                    return isCancel;
                }

                public void setIsCancel(String isCancel) {
                    this.isCancel = isCancel;
                }

                public String getOrderId() {
                    return orderId;
                }

                public void setOrderId(String orderId) {
                    this.orderId = orderId;
                }

                public String getPayType() {
                    return payType;
                }

                public void setPayType(String payType) {
                    this.payType = payType;
                }

                public String getTime() {
                    return time;
                }

                public void setTime(String time) {
                    this.time = time;
                }
            }*/
        }
    }

}
