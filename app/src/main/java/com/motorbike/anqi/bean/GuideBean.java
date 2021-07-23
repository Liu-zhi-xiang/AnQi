package com.motorbike.anqi.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/5/17.
 */

public class GuideBean {


    /**
     * status : 0
     * msg : 获取引导页成功
     * data : [{"name":"01","imgUrl":"http://172.20.10.2:8080/img/yindaoye/0d37f1e3-c23d-4638-b163-6aa464fe9ead.jpg","sort":"1","status":null},{"name":"02","imgUrl":"http://172.20.10.2:8080/img/yindaoye/2c396429-fa6d-4e75-9797-92da0d919074.png","sort":"2","status":null},{"name":"04","imgUrl":"http://172.20.10.2:8080/img/yindaoye/7113e5aa-9573-44f8-9885-f4b4debab863.jpg","sort":"3","status":null},{"name":"05","imgUrl":"http://172.20.10.2:8080/img/yindaoye/001.png","sort":"4","status":null}]
     */

    private int status;
    private String msg;
    /**
     * name : 01
     * imgUrl : http://172.20.10.2:8080/img/yindaoye/0d37f1e3-c23d-4638-b163-6aa464fe9ead.jpg
     * sort : 1
     * status : null
     */

    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String name;
        private String imgUrl;
        private String sort;
        private Object status;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }
    }
}
