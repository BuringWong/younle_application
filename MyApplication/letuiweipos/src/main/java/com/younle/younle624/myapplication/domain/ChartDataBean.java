package com.younle.younle624.myapplication.domain;

import java.util.List;

/**
 * Created by 我是奋斗 on 2016/6/24.
 * 微信/e-mail:tt090423@126.com
 * 图形列表数据对应的实体类
 */
public class ChartDataBean {
    /**
     * auth : ["1","2","3","4","5","6"]
     * code : 200
     * msg : {"bar":[{"income":0,"saled":0,"x":"1-7"},{"income":0,"saled":0,"x":"1-8"},{"income":0,"saled":0,"x":"1-9"},{"income":0,"saled":0,"x":"1-10"},{"income":1.03,"saled":4,"x":"1-11"},{"income":0,"saled":0,"x":"1-12"},{"income":0.03,"saled":3,"x":"1-13"},{"income":0,"saled":0,"x":"1-14"},{"income":0,"saled":0,"x":"1-15"},{"income":0,"saled":0,"x":"1-16"},{"income":0.01,"saled":1,"x":"1-17"},{"income":1,"saled":1,"x":"1-18"},{"income":0.01,"saled":1,"x":"1-19"},{"income":0,"saled":0,"x":"1-20"},{"income":0,"saled":0,"x":"1-21"},{"income":0,"saled":0,"x":"1-22"},{"income":0,"saled":0,"x":"1-23"},{"income":0,"saled":0,"x":"1-24"},{"income":0,"saled":0,"x":"1-25"},{"income":0,"saled":0,"x":"1-26"},{"income":0,"saled":0,"x":"1-27"},{"income":0,"saled":0,"x":"1-28"},{"income":0,"saled":0,"x":"1-29"},{"income":0,"saled":0,"x":"1-30"},{"income":0,"saled":0,"x":"1-31"},{"income":0,"saled":0,"x":"2-1"},{"income":0,"saled":0,"x":"2-2"},{"income":0,"saled":0,"x":"2-3"},{"income":0,"saled":0,"x":"2-4"},{"income":0,"saled":0,"x":"2-5"},{"income":0,"saled":0,"x":"2-6"},{"income":0,"saled":0,"x":"2-7"}],"goodsName":[{"id":"0","name":"全部商品"},{"id":"253","name":"服务购买数据测试0.01 会员专属"},{"id":"254","name":"服务购买数据测试 1"}],"income":2.08,"incomeTop5":[{"goodsId":"253","goodsName":"服务购买数据测试0.01 会员专属","income":"0.08"},{"goodsId":"254","goodsName":"服务购买数据测试 1","income":"2.00"}],"saled":10,"saledTop5":[{"goodsId":"254","goodsName":"服务购买数据测试 1","saled":"2"},{"goodsId":"253","goodsName":"服务购买数据测试0.01 会员专属","saled":"8"}],"sectorChannel":{"othersIncome":0.04,"othersIncomeRate":0.0192,"selfIncome":2.04,"selfIncomeRate":0.9808}}
     */

    private int code;
    /**
     * bar : [{"income":0,"saled":0,"x":"1-7"},{"income":0,"saled":0,"x":"1-8"},{"income":0,"saled":0,"x":"1-9"},{"income":0,"saled":0,"x":"1-10"},{"income":1.03,"saled":4,"x":"1-11"},{"income":0,"saled":0,"x":"1-12"},{"income":0.03,"saled":3,"x":"1-13"},{"income":0,"saled":0,"x":"1-14"},{"income":0,"saled":0,"x":"1-15"},{"income":0,"saled":0,"x":"1-16"},{"income":0.01,"saled":1,"x":"1-17"},{"income":1,"saled":1,"x":"1-18"},{"income":0.01,"saled":1,"x":"1-19"},{"income":0,"saled":0,"x":"1-20"},{"income":0,"saled":0,"x":"1-21"},{"income":0,"saled":0,"x":"1-22"},{"income":0,"saled":0,"x":"1-23"},{"income":0,"saled":0,"x":"1-24"},{"income":0,"saled":0,"x":"1-25"},{"income":0,"saled":0,"x":"1-26"},{"income":0,"saled":0,"x":"1-27"},{"income":0,"saled":0,"x":"1-28"},{"income":0,"saled":0,"x":"1-29"},{"income":0,"saled":0,"x":"1-30"},{"income":0,"saled":0,"x":"1-31"},{"income":0,"saled":0,"x":"2-1"},{"income":0,"saled":0,"x":"2-2"},{"income":0,"saled":0,"x":"2-3"},{"income":0,"saled":0,"x":"2-4"},{"income":0,"saled":0,"x":"2-5"},{"income":0,"saled":0,"x":"2-6"},{"income":0,"saled":0,"x":"2-7"}]
     * goodsName : [{"id":"0","name":"全部商品"},{"id":"253","name":"服务购买数据测试0.01 会员专属"},{"id":"254","name":"服务购买数据测试 1"}]
     * income : 2.08
     * incomeTop5 : [{"goodsId":"253","goodsName":"服务购买数据测试0.01 会员专属","income":"0.08"},{"goodsId":"254","goodsName":"服务购买数据测试 1","income":"2.00"}]
     * saled : 10
     * saledTop5 : [{"goodsId":"254","goodsName":"服务购买数据测试 1","saled":"2"},{"goodsId":"253","goodsName":"服务购买数据测试0.01 会员专属","saled":"8"}]
     * sectorChannel : {"othersIncome":0.04,"othersIncomeRate":0.0192,"selfIncome":2.04,"selfIncomeRate":0.9808}
     */

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

    public static class MsgBean {
        private double income;
        private int saled;
        /**
         * othersIncome : 0.04
         * othersIncomeRate : 0.0192
         * selfIncome : 2.04
         * selfIncomeRate : 0.9808
         */

        private SectorChannelBean sectorChannel;
        /**
         * income : 0
         * saled : 0
         * x : 1-7
         */

        private List<BarBean> bar;
        /**
         * id : 0
         * name : 全部商品
         */

        private List<GoodsNameBean> goodsName;
        /**
         * goodsId : 253
         * goodsName : 服务购买数据测试0.01 会员专属
         * income : 0.08
         */

        private List<IncomeTop5Bean> incomeTop5;
        /**
         * goodsId : 254
         * goodsName : 服务购买数据测试 1
         * saled : 2
         */

        private List<SaledTop5Bean> saledTop5;

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

        public SectorChannelBean getSectorChannel() {
            return sectorChannel;
        }

        public void setSectorChannel(SectorChannelBean sectorChannel) {
            this.sectorChannel = sectorChannel;
        }

        public List<BarBean> getBar() {
            return bar;
        }

        public void setBar(List<BarBean> bar) {
            this.bar = bar;
        }

        public List<GoodsNameBean> getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(List<GoodsNameBean> goodsName) {
            this.goodsName = goodsName;
        }

        public List<IncomeTop5Bean> getIncomeTop5() {
            return incomeTop5;
        }

        public void setIncomeTop5(List<IncomeTop5Bean> incomeTop5) {
            this.incomeTop5 = incomeTop5;
        }

        public List<SaledTop5Bean> getSaledTop5() {
            return saledTop5;
        }

        public void setSaledTop5(List<SaledTop5Bean> saledTop5) {
            this.saledTop5 = saledTop5;
        }

        public static class SectorChannelBean {
            private double othersIncome;
            private double othersIncomeRate;
            private double selfIncome;
            private double selfIncomeRate;

            public double getOthersIncome() {
                return othersIncome;
            }

            public void setOthersIncome(double othersIncome) {
                this.othersIncome = othersIncome;
            }

            public double getOthersIncomeRate() {
                return othersIncomeRate;
            }

            public void setOthersIncomeRate(double othersIncomeRate) {
                this.othersIncomeRate = othersIncomeRate;
            }

            public double getSelfIncome() {
                return selfIncome;
            }

            public void setSelfIncome(double selfIncome) {
                this.selfIncome = selfIncome;
            }

            public double getSelfIncomeRate() {
                return selfIncomeRate;
            }

            public void setSelfIncomeRate(double selfIncomeRate) {
                this.selfIncomeRate = selfIncomeRate;
            }
        }

        public static class BarBean {
            private double income;
            private int saled;
            private String x;

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

            public String getX() {
                return x;
            }

            public void setX(String x) {
                this.x = x;
            }
        }

        public static class GoodsNameBean {
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

        public static class IncomeTop5Bean {
            private String goodsId;
            private String goodsName;
            private String income;

            public String getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(String goodsId) {
                this.goodsId = goodsId;
            }

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }

            public String getIncome() {
                return income;
            }

            public void setIncome(String income) {
                this.income = income;
            }
        }

        public static class SaledTop5Bean {
            private String goodsId;
            private String goodsName;
            private String saled;

            public String getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(String goodsId) {
                this.goodsId = goodsId;
            }

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }

            public String getSaled() {
                return saled;
            }

            public void setSaled(String saled) {
                this.saled = saled;
            }
        }
    }


    /**
     * code : 200
     * msg : {"bar":[{"income":0,"saled":0,"x":"6-2"},{"income":0,"saled":0,"x":"6-3"},{"income":3552,"saled":4,"x":"6-4"},{"income":0,"saled":0,"x":"6-5"},{"income":2664,"saled":3,"x":"6-6"},{"income":0,"saled":0,"x":"6-7"},{"income":0,"saled":0,"x":"6-8"},{"income":0,"saled":0,"x":"6-9"},{"income":0,"saled":0,"x":"6-10"},{"income":0,"saled":0,"x":"6-11"},{"income":0,"saled":0,"x":"6-12"},{"income":0,"saled":0,"x":"6-13"},{"income":0,"saled":0,"x":"6-14"},{"income":0,"saled":0,"x":"6-15"},{"income":0,"saled":0,"x":"6-16"},{"income":0,"saled":0,"x":"6-17"},{"income":0,"saled":0,"x":"6-18"},{"income":0,"saled":0,"x":"6-19"},{"income":1183.38,"saled":3,"x":"6-20"},{"income":0,"saled":0,"x":"6-21"},{"income":0,"saled":0,"x":"6-22"},{"income":0,"saled":0,"x":"6-23"},{"income":0,"saled":0,"x":"6-24"},{"income":0,"saled":0,"x":"6-25"},{"income":0,"saled":0,"x":"6-26"},{"income":0,"saled":0,"x":"6-27"},{"income":0,"saled":0,"x":"6-28"},{"income":0,"saled":0,"x":"6-29"},{"income":0,"saled":0,"x":"6-30"}],"goodsName":[{"id":"0","name":"全部商品"},{"id":"1","name":"紫苑餐厅双人套餐"},{"id":"3","name":"龙皇KTV夜场券"},{"id":"5","name":"朝阳公园五一音乐节门票"},{"id":"6","name":"十三陵一日游"},{"id":"7","name":"50元话费直冲"},{"id":"12","name":"精品婚纱照"}],"income":7399.38,"saled":10,"sectorChannel":{"othersIncome":70.14,"othersIncomeRate":0.01,"selfIncome":7329.24,"selfIncomeRate":0.99},"sectorIncome":[{"id":"6","income":1128.36,"name":"十三陵一日游","rate":0.153},{"id":"7","income":1776,"name":"50元话费直冲","rate":0.24},{"id":"8","income":1776,"name":"QQ超级会员","rate":0.24},{"id":"12","income":958.14,"name":"精品婚纱照","rate":0.129},{"id":"1","income":1760.88,"name":"紫苑餐厅双人套餐","rate":0.238}]}
     */

    /*private int code;
    *//**
     * bar : [{"income":0,"saled":0,"x":"6-2"},{"income":0,"saled":0,"x":"6-3"},{"income":3552,"saled":4,"x":"6-4"},{"income":0,"saled":0,"x":"6-5"},{"income":2664,"saled":3,"x":"6-6"},{"income":0,"saled":0,"x":"6-7"},{"income":0,"saled":0,"x":"6-8"},{"income":0,"saled":0,"x":"6-9"},{"income":0,"saled":0,"x":"6-10"},{"income":0,"saled":0,"x":"6-11"},{"income":0,"saled":0,"x":"6-12"},{"income":0,"saled":0,"x":"6-13"},{"income":0,"saled":0,"x":"6-14"},{"income":0,"saled":0,"x":"6-15"},{"income":0,"saled":0,"x":"6-16"},{"income":0,"saled":0,"x":"6-17"},{"income":0,"saled":0,"x":"6-18"},{"income":0,"saled":0,"x":"6-19"},{"income":1183.38,"saled":3,"x":"6-20"},{"income":0,"saled":0,"x":"6-21"},{"income":0,"saled":0,"x":"6-22"},{"income":0,"saled":0,"x":"6-23"},{"income":0,"saled":0,"x":"6-24"},{"income":0,"saled":0,"x":"6-25"},{"income":0,"saled":0,"x":"6-26"},{"income":0,"saled":0,"x":"6-27"},{"income":0,"saled":0,"x":"6-28"},{"income":0,"saled":0,"x":"6-29"},{"income":0,"saled":0,"x":"6-30"}]
     * goodsName : [{"id":"0","name":"全部商品"},{"id":"1","name":"紫苑餐厅双人套餐"},{"id":"3","name":"龙皇KTV夜场券"},{"id":"5","name":"朝阳公园五一音乐节门票"},{"id":"6","name":"十三陵一日游"},{"id":"7","name":"50元话费直冲"},{"id":"12","name":"精品婚纱照"}]
     * income : 7399.38
     * saled : 10
     * sectorChannel : {"othersIncome":70.14,"othersIncomeRate":0.01,"selfIncome":7329.24,"selfIncomeRate":0.99}
     * sectorIncome : [{"id":"6","income":1128.36,"name":"十三陵一日游","rate":0.153},{"id":"7","income":1776,"name":"50元话费直冲","rate":0.24},{"id":"8","income":1776,"name":"QQ超级会员","rate":0.24},{"id":"12","income":958.14,"name":"精品婚纱照","rate":0.129},{"id":"1","income":1760.88,"name":"紫苑餐厅双人套餐","rate":0.238}]
     *//*

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

    public static class MsgBean {
        private double income;
        private int saled;
        *//**
         * othersIncome : 70.14
         * othersIncomeRate : 0.01
         * selfIncome : 7329.24
         * selfIncomeRate : 0.99
         *//*

        private SectorChannelBean sectorChannel;
        *//**
         * income : 0
         * saled : 0
         * x : 6-2
         *//*

        private List<BarBean> bar;
        *//**
         * id : 0
         * name : 全部商品
         *//*

        private List<GoodsNameBean> goodsName;
        *//**
         * id : 6
         * income : 1128.36
         * name : 十三陵一日游
         * rate : 0.153
         *//*

        private List<SectorIncomeBean> sectorIncome;

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

        public SectorChannelBean getSectorChannel() {
            return sectorChannel;
        }

        public void setSectorChannel(SectorChannelBean sectorChannel) {
            this.sectorChannel = sectorChannel;
        }

        public List<BarBean> getBar() {
            return bar;
        }

        public void setBar(List<BarBean> bar) {
            this.bar = bar;
        }

        public List<GoodsNameBean> getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(List<GoodsNameBean> goodsName) {
            this.goodsName = goodsName;
        }

        public List<SectorIncomeBean> getSectorIncome() {
            return sectorIncome;
        }

        public void setSectorIncome(List<SectorIncomeBean> sectorIncome) {
            this.sectorIncome = sectorIncome;
        }

        public static class SectorChannelBean {
            private double othersIncome;
            private double othersIncomeRate;
            private double selfIncome;
            private double selfIncomeRate;

            public double getOthersIncome() {
                return othersIncome;
            }

            public void setOthersIncome(double othersIncome) {
                this.othersIncome = othersIncome;
            }

            public double getOthersIncomeRate() {
                return othersIncomeRate;
            }

            public void setOthersIncomeRate(double othersIncomeRate) {
                this.othersIncomeRate = othersIncomeRate;
            }

            public double getSelfIncome() {
                return selfIncome;
            }

            public void setSelfIncome(double selfIncome) {
                this.selfIncome = selfIncome;
            }

            public double getSelfIncomeRate() {
                return selfIncomeRate;
            }

            public void setSelfIncomeRate(double selfIncomeRate) {
                this.selfIncomeRate = selfIncomeRate;
            }
        }

        public static class BarBean {
            private double income;
            private double saled;
            private String x;

            public double getIncome() {
                return income;
            }

            public void setIncome(double income) {
                this.income = income;
            }

            public double getSaled() {
                return saled;
            }

            public void setSaled(double saled) {
                this.saled = saled;
            }

            public String getX() {
                return x;
            }

            public void setX(String x) {
                this.x = x;
            }
        }

        public static class GoodsNameBean {
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

        public static class SectorIncomeBean {
            private String id;
            private double income;
            private String name;
            private double rate;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public double getIncome() {
                return income;
            }

            public void setIncome(double income) {
                this.income = income;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public double getRate() {
                return rate;
            }

            public void setRate(double rate) {
                this.rate = rate;
            }
        }
    }*/
}
