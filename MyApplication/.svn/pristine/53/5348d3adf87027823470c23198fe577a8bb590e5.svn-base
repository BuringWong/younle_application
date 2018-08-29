package com.younle.younle624.myapplication.domain;

import com.younle.younle624.myapplication.utils.LogUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengliang on 2016/6/15 0015.
 */
public class Compute {

    public static String count(String text) {
        // 规则，只能出现数字和加减乘除符号，最前和最后都是数字，即字符串能有效计算的
        String te = text.substring(0, 1);

        /**
         * 处理第一个输入的是符号:
         * 如(+4+5) 处理结果为:(0*4+5);
         * 如(*4+5) 处理结果为:(1*4+5);
         */
        if (te.equals("+") || te.equals("-")) {
            text = "0" + text;
        } else if (te.equals("×") || te.equals("÷")) {
            text = "1" + text;
        }

        if (text.indexOf("+")<0||text.indexOf("-")<0||text.indexOf("×")<0||text.indexOf("÷")<0){
            text = text+"×1";
        }

        // 计算内容分割
        List<String> numList = new ArrayList<String>();
        int splitIndex = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '+' || c == '-' || c == '×' || c == '÷') {
                numList.add(text.substring(splitIndex, i));
                numList.add(c + "");
                splitIndex = i + 1;
            }
        }
        // 因为使用符号做判断，增加前一位和符号，所以最后一位数字不会在循环里处理
        numList.add(text.substring(splitIndex, text.length()));

        for (int i = 0; i < numList.size(); i++) {
            LogUtils.Log("打印numList("+i+")="+numList.get(i));
        }

        // 先做乘除计算
        List<String> list = new ArrayList<String>();
        Double temp = null; // 用于做乘除计算临时变量
        for (int i = 1; i < numList.size(); i += 2) { // 这里只循环运算符号
            if ("+".equals(numList.get(i)) || "-".equals(numList.get(i))) {
                if (null != temp) { // 存在临时变量，说明前面进行过乘除计算
                    list.add(temp.toString());
                    temp = null;
                } else {
                    list.add(numList.get(i - 1));
                }
                list.add(numList.get(i)); // 把符号加进去
//                LogUtils.Log("numList.get("+i+")"+numList.get(i));
                if (i == numList.size() - 2) { // 处理到最后时遇到直接处理

                    list.add(numList.get(i + 1));


                }
            } else if ("×".equals(numList.get(i))) {
                if (null == temp) {
                    temp = Double.parseDouble(numList.get(i - 1)) * Double.parseDouble(numList.get(i + 1));//偶发一个double解析bug
                } else {
                    if(numList.get(i+1)!=null) {
                        temp = temp * Double.parseDouble(numList.get(i + 1));
                    }
                }
                if (i == numList.size() - 2) { // 处理到最后时遇到直接处理
                    list.add(temp.toString());
                    temp = null;
                }
            } else if ("÷".equals(numList.get(i))) {
                if (null == temp) {
                    temp = Double.parseDouble(numList.get(i - 1)) / Double.parseDouble(numList.get(i + 1));
                } else {
                    temp = temp / Double.parseDouble(numList.get(i + 1));
                }
                if (i == numList.size() - 2) { // 处理到最后时遇到直接处理
                    list.add(temp.toString());
                    temp = null;
                }
            }
        }
        for (int i = 0; i < list.size(); i++) {
        }


        // 再做加减计算
        Double sum = Double.parseDouble(list.get(0)); // 第一位不会在循环里处理
        for (int i = 1; i < list.size(); i += 2) { // 这里只循环运算符号
            if ("+".equals(list.get(i))) {
                sum += Double.parseDouble(list.get(i + 1));
            } else if ("-".equals(list.get(i))) {
                sum -= Double.parseDouble(list.get(i + 1));
            }
        }

        DecimalFormat df = new DecimalFormat("#.##");
        String result = df.format(sum);//对结果进行四舍五入并保留两位小数

        return result;

    }
}
