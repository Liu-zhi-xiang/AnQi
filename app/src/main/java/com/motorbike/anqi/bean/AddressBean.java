package com.motorbike.anqi.bean;

/**
 * Created by Administrator on 2018/4/25.
 */

public class AddressBean {

    /**
     * id : 1
     * idStr : 1
     * userId : 1012
     * receiver : 倪泽贺
     * phone : 13696453666
     * address : 北京/市北京市东城区
     * addressDetail : 58弄666号110
     * isdefault : null
     * isdefaultStr : null
     */

    private int id;
    private String idStr;
    private int userId;
    private String receiver;
    private String phone;
    private String address;
    private String addressDetail;
    private Object isdefault;
    private String isdefaultStr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public Object getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(Object isdefault) {
        this.isdefault = isdefault;
    }

    public String getIsdefaultStr() {
        return isdefaultStr;
    }

    public void setIsdefaultStr(String isdefaultStr) {
        this.isdefaultStr = isdefaultStr;
    }
}
