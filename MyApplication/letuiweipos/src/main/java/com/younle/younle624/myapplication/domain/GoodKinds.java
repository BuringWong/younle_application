package com.younle.younle624.myapplication.domain;

import java.io.Serializable;

/**
 * Created by 我是奋斗 on 2016/6/16.
 * 微信/e-mail:tt090423@126.com
 */
public class GoodKinds implements Serializable {
    String name;
    boolean isChecken;

    public GoodKinds() {
    }

    public GoodKinds(String name, boolean isChecken) {
        this.name = name;
        this.isChecken = isChecken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecken() {
        return isChecken;
    }

    public void setIsChecken(boolean isChecken) {
        this.isChecken = isChecken;
    }

    @Override
    public String toString() {
        return "GoodKinds{" +
                "name='" + name + '\'' +
                ", isChecken=" + isChecken +
                '}';
    }
}
