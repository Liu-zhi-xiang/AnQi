package com.motorbike.anqi.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/19.
 */

public class MallBean {

    /**
     * userIntegral : 40
     * goodsImgList : ["http://192.168.43.37:8080/img/goods/show/8c5e8ac5-7123-4afa-9a52-c29801cc15c1.jpg","http://192.168.43.37:8080/img/goods/show/924343c1-e555-42e4-88fd-c7a28cb589e8.png","http://192.168.43.37:8080/img/goods/show/924343c1-e555-42e4-88fd-c7a28cb589e8.png"]
     * goodslist : [{"goodsId":"3","goodsName":"4578II8CESHI","goodsImg":"http://192.168.43.37:8080/img/goods/show/8c5e8ac5-7123-4afa-9a52-c29801cc15c1.jpg","pointNum":"200","goodsDesc":"TJKKK678989","inventory":"20"},{"goodsId":"9","goodsName":"444","goodsImg":"http://192.168.43.37:8080/img/goods/show/924343c1-e555-42e4-88fd-c7a28cb589e8.png","pointNum":"11","goodsDesc":"描述买啥描述描述描述","inventory":"141"},{"goodsId":"8","goodsName":"4","goodsImg":"http://192.168.43.37:8080/img/goods/show/924343c1-e555-42e4-88fd-c7a28cb589e8.png","pointNum":"4","goodsDesc":"描述买啥描述描述描述","inventory":"4"},{"goodsId":"7","goodsName":"111111111111111","goodsImg":"http://192.168.43.37:8080/img/goods/show/924343c1-e555-42e4-88fd-c7a28cb589e8.png","pointNum":"20","goodsDesc":"描述买啥描述描述描述","inventory":"12"},{"goodsId":"6","goodsName":"CEAG,HH","goodsImg":"http://192.168.43.37:8080/img/goods/show/924343c1-e555-42e4-88fd-c7a28cb589e8.png","pointNum":"50","goodsDesc":"描述买啥描述描述描述","inventory":"198"},{"goodsId":"2","goodsName":"ftujkj57","goodsImg":"http://192.168.43.37:8080/img/goods/show/8c5e8ac5-7123-4afa-9a52-c29801cc15c1.jpg","pointNum":"20","goodsDesc":"gjkkjy6578","inventory":"10"},{"goodsId":"1","goodsName":"流量充值包222","goodsImg":"http://192.168.43.37:8080/img/goods/show/8c5e8ac5-7123-4afa-9a52-c29801cc15c1.jpg","pointNum":"20","goodsDesc":"dkkghhhj","inventory":"112"},{"goodsId":"4","goodsName":"22","goodsImg":"http://192.168.43.37:8080/img/goods/show/8c5e8ac5-7123-4afa-9a52-c29801cc15c1.jpg","pointNum":"22","goodsDesc":"描述买啥描述描述描述","inventory":"30"}]
     */

    private String userIntegral;
    private List<String> goodsImgList;
    /**
     * goodsId : 3
     * goodsName : 4578II8CESHI
     * goodsImg : http://192.168.43.37:8080/img/goods/show/8c5e8ac5-7123-4afa-9a52-c29801cc15c1.jpg
     * pointNum : 200
     * goodsDesc : TJKKK678989
     * inventory : 20
     */

    private List<GoodslistBean> goodslist;

    public String getUserIntegral() {
        return userIntegral;
    }

    public void setUserIntegral(String userIntegral) {
        this.userIntegral = userIntegral;
    }

    public List<String> getGoodsImgList() {
        return goodsImgList;
    }

    public void setGoodsImgList(List<String> goodsImgList) {
        this.goodsImgList = goodsImgList;
    }

    public List<GoodslistBean> getGoodslist() {
        return goodslist;
    }

    public void setGoodslist(List<GoodslistBean> goodslist) {
        this.goodslist = goodslist;
    }


}
