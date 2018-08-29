package com.yongle.letuiweipad.domain;

/**
 * Created by bert_dong on 2018/4/13 0013.
 * 邮箱：18701038771@163.com
 */

public class SmScannerVoucherBean {
    private boolean attach;
    private String id;

    public SmScannerVoucherBean(String id) {
        this.id = id;
    }

    public boolean isAttach() {
        return attach;
    }

    public void setAttach(boolean attach) {
        this.attach = attach;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
