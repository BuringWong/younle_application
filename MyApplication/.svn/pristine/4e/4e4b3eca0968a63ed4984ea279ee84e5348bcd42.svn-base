package com.yongle.letuiweipad.domain.createorder;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * Created by bert_dong on 2016/11/1 0001.
 * 邮箱：18701038771@163.com
 */
@Entity
public class GoodBean implements Serializable {
    private static final  long serialVersionUID=1L;
    @Id(autoincrement = true)
    private Long id;
    private String goodsId;
    private String goodsName;
    private double goodsPrice;
    private double goodsNum;
    private double stock;
    private int hasStock;
    private int stockWarning;
    private String goodsUnit;
    private String kinds;
    private boolean isChecked;
    private double subNum;//提交到服务器的：需要减少的数量
    @Property(nameInDb = "size_id")
    private String size_id;
    @Property(nameInDb = "sizeId")
    private String sizeId;
    private String groupId;
    private String groupName;
    private String goodsCode;
    private String is_weigh;//0：非称重 1：称重
//    private List<SizeListBean> sizeList;
    @Transient
    private SearchBean searchBean;
    private double vipPrice;
    private int typeId;

    @Generated(hash = 174114430)
    public GoodBean(Long id, String goodsId, String goodsName, double goodsPrice,
            double goodsNum, double stock, int hasStock, int stockWarning,
            String goodsUnit, String kinds, boolean isChecked, double subNum,
            String size_id, String sizeId, String groupId, String groupName,
            String goodsCode, String is_weigh, double vipPrice, int typeId) {
        this.id = id;
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.goodsNum = goodsNum;
        this.stock = stock;
        this.hasStock = hasStock;
        this.stockWarning = stockWarning;
        this.goodsUnit = goodsUnit;
        this.kinds = kinds;
        this.isChecked = isChecked;
        this.subNum = subNum;
        this.size_id = size_id;
        this.sizeId = sizeId;
        this.groupId = groupId;
        this.groupName = groupName;
        this.goodsCode = goodsCode;
        this.is_weigh = is_weigh;
        this.vipPrice = vipPrice;
        this.typeId = typeId;

    }


    @Generated(hash = 1348485518)
    public GoodBean() {
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public SearchBean getSearchBean() {
        return searchBean;
    }

    public void setSearchBean(SearchBean searchBean) {
        this.searchBean = searchBean;
    }

    public Long getId() {
        return id;
    }

    public String getGoodsId() {
            return this.goodsId;
        }


        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }


        public String getGoodsName() {
            return this.goodsName;
        }


        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }


        public double getGoodsPrice() {
            return this.goodsPrice;
        }


        public void setGoodsPrice(double goodsPrice) {
            this.goodsPrice = goodsPrice;
        }


        public double getGoodsNum() {
            return this.goodsNum;
        }


        public void setGoodsNum(double goodsNum) {
            this.goodsNum = goodsNum;
        }


        public double getStock() {
            return this.stock;
        }


        public void setStock(double stock) {
            this.stock = stock;
        }


        public int getHasStock() {
            return this.hasStock;
        }


        public void setHasStock(int hasStock) {
            this.hasStock = hasStock;
        }


        public int getStockWarning() {
            return this.stockWarning;
        }


        public void setStockWarning(int stockWarning) {
            this.stockWarning = stockWarning;
        }


        public String getGoodsUnit() {
            return this.goodsUnit;
        }


        public void setGoodsUnit(String goodsUnit) {
            this.goodsUnit = goodsUnit;
        }


        public String getKinds() {
            return this.kinds;
        }


        public void setKinds(String kinds) {
            this.kinds = kinds;
        }


        public boolean getIsChecked() {
            return this.isChecked;
        }


        public void setIsChecked(boolean isChecked) {
            this.isChecked = isChecked;
        }


        public double getSubNum() {
            return this.subNum;
        }


        public void setSubNum(double subNum) {
            this.subNum = subNum;
        }


        public String getSize_id() {
            return this.size_id;
        }


        public void setSize_id(String size_id) {
            this.size_id = size_id;
        }


        public String getSizeId() {
            return this.sizeId;
        }


        public void setSizeId(String sizeId) {
            this.sizeId = sizeId;
        }


        public String getGroupId() {
            return this.groupId;
        }


        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }


        public String getGroupName() {
            return this.groupName;
        }


        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }


        public String getGoodsCode() {
            return this.goodsCode;
        }


        public void setGoodsCode(String goodsCode) {
            this.goodsCode = goodsCode;
        }


        public String getIs_weigh() {
            return this.is_weigh;
        }


        public void setIs_weigh(String is_weigh) {
            this.is_weigh = is_weigh;
        }


        public double getVipPrice() {
            return this.vipPrice;
        }


        public void setVipPrice(double vipPrice) {
            this.vipPrice = vipPrice;
        }


        public void setId(Long id) {
            this.id = id;
        }


        public int getTypeId() {
            return this.typeId;
        }


        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

    @Override
    public String toString() {
        return "GoodBean{" +
                "goodsId='" + goodsId + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", goodsNum=" + goodsNum +
                ", stock=" + stock +
                ", hasStock=" + hasStock +
                ", stockWarning=" + stockWarning +
                ", goodsUnit='" + goodsUnit + '\'' +
                ", kinds='" + kinds + '\'' +
                ", isChecked=" + isChecked +
                ", subNum=" + subNum +
                ", sizeId='" + size_id + '\'' +
                ", groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", goodsCode='" + goodsCode + '\'' +
                ", is_weigh='" + is_weigh + '\'' +
                ", vipPrice='" + vipPrice + '\'' +
                '}';
    }
}
