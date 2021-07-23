package com.motorbike.anqi.bean;

/**
 * Created by Administrator on 2018/5/8.
 */

public class ExchangeDetailBean {

    /**
     * goodsId : null
     * goodsName : 444
     * goodsImg : http://192.168.191.5:8080/img/goods/show/668989a6-d6f9-4a98-8b3c-a1d29a23679f.png
     * pointNum : 11
     * goodsDesc : <p>描述买啥描述描述描述</p>

     * inventory : 133
     * isexchange : 0
     * defaultAddress : 河北省石家庄市长安区1弄1号101
     */

    private String goodsId;
    private String goodsName;
    private String goodsImg;
    private String pointNum;
    private String goodsDesc;
    private String inventory;
    private String isexchange;
    private String defaultAddress;

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

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public String getPointNum() {
        return pointNum;
    }

    public void setPointNum(String pointNum) {
        this.pointNum = pointNum;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getIsexchange() {
        return isexchange;
    }

    public void setIsexchange(String isexchange) {
        this.isexchange = isexchange;
    }

    public String getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(String defaultAddress) {
        this.defaultAddress = defaultAddress;
    }
}
