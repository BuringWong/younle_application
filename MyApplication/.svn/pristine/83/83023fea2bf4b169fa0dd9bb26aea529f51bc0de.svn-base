package com.yongle.letuiweipad.domain.createorder;

import java.util.List;

/**
 * Created by bert_dong on 2016/10/19 0019.
 * 邮箱：18701038771@163.com
 */
public class AllGoodsInfoBean {

    /**
     * auth : ["1","2","3","4","5","6"]
     * code : 200
     * msg : {"allGoodsId":[{"id":"126","name":"辣条中包"},{"id":"127","name":"乐事薯片"},{"id":"125","name":"每日C"},
     * {"id":"128","name":"勇闯天涯听装"}],
     * "goodsInfo":[{"goodsList":[{"goodsId":"126","goodsName":"辣条中包","goodsNum":0,"goodsPrice":"6.68","goodsUnit":"","hasStock":0,"stock":"-1","stockWarning":0},{"goodsId":"127","goodsName":"乐事薯片","goodsNum":0,"goodsPrice":"7.90","goodsUnit":"","hasStock":1,"stock":"20","stockWarning":1}],"typeName":"零食"},{"goodsList":[{"goodsId":"125","goodsName":"每日C","goodsNum":0,"goodsPrice":"5.80","goodsUnit":"","hasStock":0,"stock":"-1","stockWarning":0}],"typeName":"冷饮"},{"goodsList":[{"goodsId":"128","goodsName":"勇闯天涯听装","goodsNum":0,"goodsPrice":"3.00","goodsUnit":"","hasStock":1,"stock":"18","stockWarning":0}],"typeName":"酒水"}]}
     * returnsys : d*wIF*dYYdC4qAAHQLxpKGh1TTKO
     */

    private int code;
    private MsgBean msg;
    private String returnsys;
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

    public String getReturnsys() {
        return returnsys;
    }

    public void setReturnsys(String returnsys) {
        this.returnsys = returnsys;
    }

    public List<String> getAuth() {
        return auth;
    }

    public void setAuth(List<String> auth) {
        this.auth = auth;
    }

    public static class MsgBean {
        /**
         * id : 126
         * name : 辣条中包
         */

        private List<AllGoodsIdBean> allGoodsId;
        /**
         * goodsList : [{"goodsId":"126","goodsName":"辣条中包","goodsNum":0,"goodsPrice":"6.68","goodsUnit":"","hasStock":0,"stock":"-1",
         * "stockWarning":0},{"goodsId":"127","goodsName":"乐事薯片","goodsNum":0,"goodsPrice":"7.90","goodsUnit":"","hasStock":1,
         * "stock":"20","stockWarning":1}]
         * typeName : 零食
         */

        private List<GoodsInfoBean> goodsInfo;

        public List<AllGoodsIdBean> getAllGoodsId() {
            return allGoodsId;
        }

        public void setAllGoodsId(List<AllGoodsIdBean> allGoodsId) {
            this.allGoodsId = allGoodsId;
        }

        public List<GoodsInfoBean> getGoodsInfo() {
            return goodsInfo;
        }

        public void setGoodsInfo(List<GoodsInfoBean> goodsInfo) {
            this.goodsInfo = goodsInfo;
        }

        public static class AllGoodsIdBean {
            private String id;
            private String name;

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
        }
        public static class GoodsInfoBean {
            private String typeName;
            private boolean isChecked;
            private int typeNum;
            private int typeId;

            public int getTypeId() {
                return typeId;
            }

            public void setTypeId(int typeId) {
                this.typeId = typeId;
            }

            public int getTypeNum() {
                return typeNum;
            }

            public void setTypeNum(int typeNum) {
                this.typeNum = typeNum;
            }

            /**
             * goodsId : 126
             * goodsName : 辣条中包
             * goodsNum : 0
             * goodsPrice : 6.68
             * goodsUnit :
             * hasStock : 0
             * stock : -1
             * stockWarning : 0
             */
            private List<GoodBean> goodsList;
            public boolean isChecked() {
                return isChecked;
            }
            public void setIsChecked(boolean isChecked) {
                this.isChecked = isChecked;
            }
            public String getTypeName() {
                return typeName;
            }
            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }
            public List<GoodBean> getGoodsList() {
                return goodsList;
            }
            public void setGoodsList(List<GoodBean> goodsList) {
                this.goodsList = goodsList;
            }

            @Override
            public String toString() {
                return "GoodsInfoBean{" +
                        "typeName='" + typeName + '\'' +
                        ", isChecked=" + isChecked +
                        ", goodsList=" + goodsList +
                        '}';
            }
        }
    }
}
