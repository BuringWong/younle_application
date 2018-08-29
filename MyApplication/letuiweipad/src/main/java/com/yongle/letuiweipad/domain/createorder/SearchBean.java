package com.yongle.letuiweipad.domain.createorder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */

public class SearchBean implements Serializable{
    /** 简拼 */
    public String simpleSpell = "";
    /** 全拼 */
    public String wholeSpell = "";
    /** 中文全名 */
    public String chName = "";
    /**
     * 中文拆分为单个汉字后的包含所有汉字拼音的list
     * @return
     */
    public List<String> singlePinyinList=new ArrayList<>();

    public int kindsPosition;
    public int goodsPosition;
    public SearchBean() {
    }

    public SearchBean(String simpleSpell, String wholeSpell, String chName, List<String> singlePinyinList) {
        this.simpleSpell = simpleSpell;
        this.wholeSpell = wholeSpell;
        this.chName = chName;
        this.singlePinyinList = singlePinyinList;
    }

    public String getSimpleSpell() {
        return simpleSpell;
    }

    public void setSimpleSpell(String simpleSpell) {
        this.simpleSpell = simpleSpell;
    }

    public String getWholeSpell() {
        return wholeSpell;
    }

    public void setWholeSpell(String wholeSpell) {
        this.wholeSpell = wholeSpell;
    }

    public String getChName() {
        return chName;
    }

    public void setChName(String chName) {
        this.chName = chName;
    }

    public List<String> getSinglePinyinList() {
        return singlePinyinList;
    }

    public void setSinglePinyinList(List<String> singlePinyinList) {
        this.singlePinyinList = singlePinyinList;
    }

    public int getKindsPosition() {
        return kindsPosition;
    }

    public void setKindsPosition(int kindsPosition) {
        this.kindsPosition = kindsPosition;
    }

    public int getGoodsPosition() {
        return goodsPosition;
    }

    public void setGoodsPosition(int goodsPosition) {
        this.goodsPosition = goodsPosition;
    }

    @Override
    public String toString() {
        return "SearchBean{" +
                "simpleSpell='" + simpleSpell + '\'' +
                ", wholeSpell='" + wholeSpell + '\'' +
                ", chName='" + chName + '\'' +
                ", singlePinyinList=" + singlePinyinList +
                ", kindsPosition=" + kindsPosition +
                ", goodsPosition=" + goodsPosition +
                '}';
    }
}
