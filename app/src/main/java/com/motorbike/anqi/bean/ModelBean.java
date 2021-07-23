package com.motorbike.anqi.bean;

/**
 * Created by Administrator on 2018/4/23.
 */

public class ModelBean {


    /**
     * id : 1
     * carModels : A1
     * brandId : 1
     * createTime : null
     */

    private String id;
    private String carModels;
    private String brandId;
    private Object createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarModels() {
        return carModels;
    }

    public void setCarModels(String carModels) {
        this.carModels = carModels;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }
}
