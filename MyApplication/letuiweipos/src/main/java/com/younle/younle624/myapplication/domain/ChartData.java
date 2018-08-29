package com.younle.younle624.myapplication.domain;

import java.util.List;

/**
 * Created by 我是奋斗 on 2016/5/25.
 * 微信/e-mail:tt090423@126.com
 */
public class ChartData {
    private String store;
    private List<ChartDetailEntity> details;

    public ChartData() {
    }

    public ChartData(String store, List<ChartDetailEntity> details) {
        this.store = store;
        this.details = details;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public List<ChartDetailEntity> getDetails() {
        return details;
    }

    public void setDetails(List<ChartDetailEntity> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "ChartData{" +
                "store='" + store + '\'' +
                ", details=" + details +
                '}';
    }

    public static  class ChartDetailEntity{
        private DayOrHourEntity dayOrHourEntity;

        public ChartDetailEntity() {
        }

        public ChartDetailEntity(DayOrHourEntity dayOrHourEntity) {
            this.dayOrHourEntity = dayOrHourEntity;
        }

        public DayOrHourEntity getDayOrHourEntity() {
            return dayOrHourEntity;
        }

        public void setDayOrHourEntity(DayOrHourEntity dayOrHourEntity) {
            this.dayOrHourEntity = dayOrHourEntity;
        }

        @Override
        public String toString() {
            return "ChartDetailEntity{" +
                    "dayOrHourEntity=" + dayOrHourEntity +
                    '}';
        }

        public static class DayOrHourEntity {
            int OrderNUm;
            double totalAcc;
            String date;

            public DayOrHourEntity() {
            }

            public DayOrHourEntity(int orderNUm, double totalAcc, String date) {
                OrderNUm = orderNUm;
                this.totalAcc = totalAcc;
                this.date = date;
            }

            public int getOrderNUm() {
                return OrderNUm;
            }

            public void setOrderNUm(int orderNUm) {
                OrderNUm = orderNUm;
            }

            public double getTotalAcc() {
                return totalAcc;
            }

            public void setTotalAcc(double totalAcc) {
                this.totalAcc = totalAcc;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            @Override
            public String toString() {
                return "DayOrHourEntity{" +
                        "OrderNUm=" + OrderNUm +
                        ", totalAcc=" + totalAcc +
                        ", date='" + date + '\'' +
                        '}';
            }
        }
    }
}
