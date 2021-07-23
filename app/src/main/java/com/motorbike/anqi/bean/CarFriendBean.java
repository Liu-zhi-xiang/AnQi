package com.motorbike.anqi.bean;

/**
 * Created by Administrator on 2018/4/24.
 */

public class CarFriendBean {
    private MemberListBean memberListBean;
    private boolean xiala=false;

    public CarFriendBean(MemberListBean memberListBean, boolean xiala) {
        this.memberListBean = memberListBean;
        this.xiala = xiala;
    }

    public MemberListBean getMemberListBean() {
        return memberListBean;
    }

    public void setMemberListBean(MemberListBean memberListBean) {
        this.memberListBean = memberListBean;
    }

    public boolean isXiala() {
        return xiala;
    }

    public void setXiala(boolean xiala) {
        this.xiala = xiala;
    }
}
