package com.younle.younle624.myapplication.utils.createorder;

import android.os.Build;

import com.younle.younle624.myapplication.domain.AllGoodsInfoBean;
import com.younle.younle624.myapplication.domain.GoodBean;
import com.younle.younle624.myapplication.domain.createorder.SearchBean;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/7/19.
 */

public class SearchUtils {
    /** 中文字符串匹配 */
    String chReg = "[\\u4E00-\\u9FA5]+";
    public static SearchUtils instance;

    public static SearchUtils getInstance(){
        if(instance==null) {
            instance=new SearchUtils();
        }
        return instance;
    }

    /**
     * 解析商品名称
     * @param gotAllGoodsInfoBean
     */
    public void parseData(final AllGoodsInfoBean gotAllGoodsInfoBean) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<AllGoodsInfoBean.MsgBean.GoodsInfoBean> goodsInfo = gotAllGoodsInfoBean.getMsg().getGoodsInfo();
                for(int i=0;i<goodsInfo.size();i++){
                    List<GoodBean> goodsList = goodsInfo.get(i).getGoodsList();
                    for (int j=0; j<goodsList.size();j++){
                        SearchBean searchBean= Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP?parseSortKey(goodsList.get(j).getGoodsName().trim())
                                : parseSortKeyLollipop(goodsList.get(j).getGoodsName().trim());
                        searchBean.setKindsPosition(i);
                        searchBean.setGoodsPosition(j);
                        goodsList.get(j).setSearchBean(searchBean);
                    }
                }
            }
        }).run();

    }
    /**
     * 解析sort_key,封装简拼,全拼
     * @param sortKey
     * @return
     */
    public SearchBean parseSortKey(String sortKey) {
        SearchBean token = new SearchBean();
        if (sortKey != null && sortKey.length() > 0) {
            //其中包含的中文字符
            String[] enStrs = sortKey.replace(" ", "").split(chReg);
            for (int i = 0, length = enStrs.length; i < length; i++) {
                if (enStrs[i].length() > 0) {
                    //拼接简拼
                    token.simpleSpell += enStrs[i].charAt(0);
                    String singleSelling = CharacterParser.getInstance().getSelling(enStrs[i]);
                    token.singlePinyinList.add(singleSelling);
                    token.wholeSpell += enStrs[i];
                }
            }
        }
        token.chName=sortKey;
        return token;
    }

    /**
     * 解析sort_key,封装简拼,全拼。
     * Android 5.0 以上使用
     * @param sortKey
     * @return
     */
    public SearchBean parseSortKeyLollipop(String sortKey) {
        SearchBean token = new SearchBean();
        if (sortKey != null && sortKey.length() > 0) {
//            boolean isChinese = sortKey.matches(chReg);
            boolean isChinese = true;
            // 分割条件：中文不分割，英文以大写和空格分割
            String regularExpression = isChinese ? "" : "(?=[A-Z])|\\s";
            String[] enStrs = sortKey.split(regularExpression);
            for (int i = 0, length = enStrs.length; i < length; i++)
                if (enStrs[i].length() > 0) {
                    //拼接简拼
                    token.simpleSpell += getSortLetter(String.valueOf(enStrs[i].charAt(0)));
                    String singleSelling = CharacterParser.getInstance().getSelling(enStrs[i]);
                    token.singlePinyinList.add(singleSelling);
                    token.wholeSpell += singleSelling;
                }
        }
        token.chName=sortKey;
        return token;
    }
    /**
     * 名字转拼音,取首字母
     * @param name
     * @return
     */
    private String getSortLetter(String name) {
        String letter = "#";
        if (name == null) {
            return letter;
        }
        if(name.matches("[0-9]")) {
            return name;
        }
        //汉字转换成拼音
        String pinyin = CharacterParser.getInstance().getSelling(name);
        String sortString = pinyin.substring(0, 1).toUpperCase(Locale.CHINESE);

        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            letter = sortString.toUpperCase(Locale.CHINESE);
        }
        return letter;
    }

    public void search(String input, List<GoodBean> filterData, List<AllGoodsInfoBean.MsgBean.GoodsInfoBean> goodsInfo) {
        String inputStr = input.toLowerCase(Locale.CHINESE);
        for (int i = 0; i < goodsInfo.size(); i++) {
            List<GoodBean> goodsList = goodsInfo.get(i).getGoodsList();
            for (int j = 0; j < goodsList.size(); j++) {
                GoodBean goodBean = goodsList.get(j);
                //中文
                boolean isNameContains=goodBean.getGoodsName().toLowerCase(Locale.CHINESE).contains(inputStr);
                if (addToFilter(filterData, goodBean, isNameContains)) continue;
                //简拼
                boolean jpContains = goodBean.getSearchBean().getSimpleSpell().toLowerCase(Locale.CHINESE).contains(inputStr);
                if (addToFilter(filterData, goodBean, jpContains)) continue;
                //单个汉字拼音匹配
                boolean singeWordSpellContains=false;
                for (int k = 0; k < goodBean.getSearchBean().getSinglePinyinList().size(); k++) {
                    String singlePinyin = goodBean.getSearchBean().getSinglePinyinList().get(k).toLowerCase(Locale.CHINESE);
                    if(singlePinyin.contains(inputStr)&&singlePinyin.substring(0,1).equals(inputStr.substring(0,1))) {
                        singeWordSpellContains=true;
                        break;
                    }
                }
                if(addToFilter(filterData,goodBean,singeWordSpellContains)) continue;
                //全拼匹配
                boolean isWholeSpellCOntains=false;
                if(goodBean.getSearchBean().getWholeSpell().length()>=inputStr.length()) {
                    isWholeSpellCOntains=goodBean.getSearchBean().getWholeSpell().substring(0,inputStr.length()).toLowerCase(Locale.CHINESE).endsWith(inputStr);
                }
                if(addToFilter(filterData,goodBean,isWholeSpellCOntains)) continue;
            }
        }

    }

    private boolean addToFilter(List<GoodBean> filterData, GoodBean goodBean, boolean isNameContains) {
        if(isNameContains) {
            filterData.add(goodBean);
            return true;
        }
        return false;
    }
}
