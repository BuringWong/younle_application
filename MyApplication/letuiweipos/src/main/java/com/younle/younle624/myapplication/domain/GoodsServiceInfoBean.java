package com.younle.younle624.myapplication.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bert_dong on 2016/10/19 0019.
 * 邮箱：18701038771@163.com
 */
public class GoodsServiceInfoBean {

    /**
     * code : 200
     * msg : {"realGoods":[{"categoryName":"饮料","goodsList":[{"goodsId":"1","goodsName":"果粒橙","goodsPrice":"5.00"},{"goodsId":"2","goodsName":"雪碧","goodsPrice":"4.50"}]},{"categoryName":"粥类","goodsList":[{"goodsId":"3","goodsName":"皮蛋瘦肉粥","goodsPrice":"13.00"},{"goodsId":"4","goodsName":"绿豆粥","goodsPrice":"8.00"}]}],"serverGoods":[{"categoryName":"按摩","goodsList":[{"goodsId":"5","goodsName":"2小时按摩","goodsPrice":"188.00"},{"goodsId":"6","goodsName":"1小时按摩","goodsPrice":"99.00"}]},{"categoryName":"推拿","goodsList":[{"goodsId":"7","goodsName":"1小时推拿","goodsPrice":"320.00"},{"goodsId":"8","goodsName":"2小时推拿","goodsPrice":"588.00"}]}]}
     */

    private int code;
    private MsgBean msg;

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

    public static class MsgBean implements Serializable{

        /**
         * categoryName : 饮料
         * goodsList : [{"goodsId":"1","goodsName":"果粒橙","goodsPrice":"5.00"},{"goodsId":"2","goodsName":"雪碧","goodsPrice":"4.50"}]
         */

        private List<RealGoodsBean> realGoods;
        /**
         * categoryName : 按摩
         * goodsList : [{"goodsId":"5","goodsName":"2小时按摩","goodsPrice":"188.00"},{"goodsId":"6","goodsName":"1小时按摩","goodsPrice":"99.00"}]
         */

        private List<ServerGoodsBean> serverGoods;

        public List<RealGoodsBean> getRealGoods() {
            return realGoods;
        }

        public void setRealGoods(List<RealGoodsBean> realGoods) {
            this.realGoods = realGoods;
        }

        public List<ServerGoodsBean> getServerGoods() {
            return serverGoods;
        }

        public void setServerGoods(List<ServerGoodsBean> serverGoods) {
            this.serverGoods = serverGoods;
        }

        public static class RealGoodsBean {
            private String categoryName;
            private boolean isChecked;

            /**
             * goodsId : 1
             * goodsName : 果粒橙
             * goodsPrice : 5.00
             */

            public boolean isChecked() {
                return isChecked;
            }

            public void setIsChecked(boolean isChecked) {
                this.isChecked = isChecked;
            }

            private List<GoodBean> goodsList;

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            public List<GoodBean> getGoodsList() {
                return goodsList;
            }

            public void setGoodsList(List<GoodBean> goodsList) {
                this.goodsList = goodsList;
            }

        }

        public static class ServerGoodsBean {
            private String categoryName;
            private boolean isChecked;

            /**
             * goodsId : 5
             * goodsName : 2小时按摩
             * goodsPrice : 188.00
             */

            public boolean isChecked() {
                return isChecked;
            }

            public void setIsChecked(boolean isChecked) {
                this.isChecked = isChecked;
            }

            private List<GoodBean> goodsList;

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            public List<GoodBean> getGoodsList() {
                return goodsList;
            }

            public void setGoodsList(List<GoodBean> goodsList) {
                this.goodsList = goodsList;
            }

        }
    }
}
