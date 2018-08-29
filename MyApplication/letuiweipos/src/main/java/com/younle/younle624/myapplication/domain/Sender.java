package com.younle.younle624.myapplication.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bert_dong on 2018/4/23 0023.
 * 邮箱：18701038771@163.com
 */

public class Sender implements Serializable{
    private List<DataBean> msg;

    public List<DataBean> getData() {
        return msg;
    }

    public void setData(List<DataBean> data) {
        this.msg = data;
    }

    public static class DataBean implements Serializable {
        /**
         * id : 13
         * name : 张三
         */

        private String id;
        private String name;
        private boolean selected;


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

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", selected=" + selected +
                    '}';
        }
    }


    /*private String name;
    private boolean selected;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "Sender{" +
                "name='" + name + '\'' +
                ", selected=" + selected +
                '}';
    }*/
}
