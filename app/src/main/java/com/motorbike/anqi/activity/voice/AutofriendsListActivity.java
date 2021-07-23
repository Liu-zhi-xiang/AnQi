package com.motorbike.anqi.activity.voice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;

import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.motorbike.anqi.R;
import com.motorbike.anqi.activity.map.MapLocationOverlayActivity;
import com.motorbike.anqi.bean.CarFriendBean;
import com.motorbike.anqi.bean.MemberListBean;
import com.motorbike.anqi.bean.UserCarBean;
import com.motorbike.anqi.bean.Voiceroom;
import com.motorbike.anqi.beseAdapter.CommonAdapter;
import com.motorbike.anqi.beseAdapter.base.ViewHolder;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.util.UserPreference;
import com.motorbike.anqi.view.CircleImageView;
import com.motorbike.anqi.xrecyclerview.XRecyclerView;
import com.motorbike.anqi.xrecyclerview.progressindicator.ProgressStyle;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 车友列表
 */
public class AutofriendsListActivity extends BaseActivity implements View.OnClickListener {
    private TextView titleTv;
    private XRecyclerView recyclerView;
    private List<CarFriendBean> voicerroomList;
    private MyAdapter myadapter;
    private String roomId,leaderId;
    private Map<String,Object> friendsMap,followMap,adminMap,barleyMap,kickOutMap;
    private UserPreference preference;
    private List<MemberListBean> memberListBeans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autofriends_list);
        preference=UserPreference.getUserPreference(this);
        roomId=getIntent().getStringExtra("roomId");
        leaderId=getIntent().getStringExtra("leaderId");
        initView();
    }

    private void initView()
    {
        findViewById(R.id.back_layout).setOnClickListener(this);
        titleTv=  findViewById(R.id.title_text);
        titleTv.setText("车友列表");
        voicerroomList=new ArrayList<>();
        if (!TextUtils.isEmpty(roomId))
        {
            showLoading();
            httpFriendList(roomId,"","");
        }
        recyclerView= findViewById(R.id.auto_friend_xrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        myadapter=new MyAdapter(this,R.layout.auto_friends_item,voicerroomList);
        recyclerView.setAdapter(myadapter);
        recyclerView.setPullRefreshEnabled(false);
        recyclerView.setLoadingMoreEnabled(false);
        myadapter.notifyDataSetChanged();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch(newState)
                {
                    case 0:
                        myadapter.gengxin();
                        Log.e("isScrolling","recyclerview已经停止滚动");
//                        mHandler.postDelayed(LOAD_DATA,2000);
                        break;
                    case 1:
//                       mHandler.removeCallbacks(LOAD_DATA);
                        Log.e("isScrolling","recyclerview正在被拖拽");
                        break;
                    case 2:
                        Log.e("isScrolling","recyclerview正在依靠惯性滚动");
                        break;
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });

    }

    /**
     * 好友列表
     * @param roomId
     * @param pageCount
     * @param pageNum
     */
    private void httpFriendList(String roomId,String pageCount,String pageNum){
        showLoading();
        friendsMap=new HashMap<>();
        if (friendsMap!=null){
            friendsMap.clear();
        }
        friendsMap.put("roomNo",roomId);
        friendsMap.put("pageCount",pageCount);
        friendsMap.put("pageNum",pageNum);
        friendsMap.put("userId",BaseRequesUrl.uesrId);
        okHttp(this, BaseRequesUrl.CarFriendList, HttpTagUtil.CarFriendList,friendsMap);

    }

    /**
     * 添加关注
     */
    private void follow(String userId,String friendId,String operateType)
    {
        followMap=new HashMap<>();
        if (followMap!=null){
            followMap.clear();
        }
        followMap.put("userId",userId);
        followMap.put("friendId",friendId);
        followMap.put("operateType",operateType);
        okHttp(this,BaseRequesUrl.FollowFriends,HttpTagUtil.FollowFriends,followMap);
    }
    /**
     * 设置管理员
     */
    private void setAdmin(String roomId,String userId,String friendId,String isAdmin)
    {
        adminMap=new HashMap<>();
        if (adminMap!=null){
            adminMap.clear();
        }
        adminMap.put("roomNo",roomId);
        adminMap.put("userId",userId);
        adminMap.put("friendId",friendId);
        adminMap.put("isAdmin",isAdmin);//0取消  1 设置
        okHttp(this,BaseRequesUrl.SetAdmin,HttpTagUtil.SetAdmin,adminMap);
    }

    /**
     * 禁麦、取消禁麦
     * @param roomId
     * @param friendId
     * @param userId
     * @param microphone
     */
    private void barley(String roomId,String friendId,String userId,String microphone)
    {
        barleyMap=new HashMap<>();
        if (barleyMap!=null){
            barleyMap.clear();
        }
        barleyMap.put("roomNo",roomId);
        barleyMap.put("friendId",friendId);
        barleyMap.put("userId",userId);
        barleyMap.put("microphone",microphone);//0禁麦  1 取消禁麦
        if (microphone.equals("1")){
            Log.e("aaaaa","禁麦");
            AVChatManager.getInstance().muteRemoteAudio(microphone, true);
        }else {
            Log.e("aaaaa","解禁");
            AVChatManager.getInstance().muteRemoteAudio(microphone, false);
        }
        okHttp(this,BaseRequesUrl.Barley,HttpTagUtil.Barley,barleyMap);
    }


    private void kickOut(String userId,String friendId,String roomId)
    {
        kickOutMap=new HashMap<>();
        if (kickOutMap!=null){
            kickOutMap.clear();
        }
        kickOutMap.put("userId",userId);
        kickOutMap.put("friendId",friendId);
        kickOutMap.put("roomNo",roomId);
        okHttp(this,BaseRequesUrl.Removember,HttpTagUtil.Removember,kickOutMap);
    }

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp)
    {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag){
            case HttpTagUtil.CarFriendList:
                dismissLoading();
                if (data!=null){
                     memberListBeans=new Gson().fromJson(data,new TypeToken<List<MemberListBean>>(){}.getType());
                    if (voicerroomList!=null){
                        voicerroomList.clear();
                    }
                    if (memberListBeans!=null){
                        for (int x=0;x<memberListBeans.size();x++){
                            voicerroomList.add(new CarFriendBean(memberListBeans.get(x),false));
                        }
                        myadapter.notifyDataSetChanged();
                    }
                }
                break;
            case HttpTagUtil.FollowFriends://关注好友
                showTip(mag);
                break;
            case HttpTagUtil.SetAdmin://设置管理员
                showTip(mag);
                break;
            case HttpTagUtil.Barley://禁麦
                showTip(mag);
                break;
            case HttpTagUtil.Removember://
                showTip(mag);
                httpFriendList(roomId,"","");
                break;
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.back_layout:
                finish();
                break;
        }
    }

    public class MyAdapter extends CommonAdapter<CarFriendBean>
    {
        private int dataposition=-1,gonellposition=-1;//下拉框显示position/头像弹框position
        private ViewHolder jiluholder;
        private CarFriendBean jiluvoiceroom;

        public MyAdapter(Context context, int layoutId, List<CarFriendBean> datas) {
            super(context, layoutId, datas);
        }

        public void gengdai()
        {
            if (jiluholder!=null&&jiluvoiceroom!=null)
            {
                Log.e("aaaaa","222222222222");
                final LinearLayout caidan = jiluholder.getView(R.id.caidan_ll);
                final ImageView xiaImg = jiluholder.getView(R.id.xiala_iv);
                xiaImg.setBackgroundResource(R.mipmap.xia);
                jiluvoiceroom.setXiala(false);
                caidan.setVisibility(View.GONE);
                jiluholder=null;
                jiluvoiceroom=null;
                dataposition=-1;
            }


        }

        private void gengxin()
        {
            try {
                int size=getDatas().size();
                for (int x=0;x<size;x++){
                    voicerroomList.get(x).setXiala(false);
                }
                notifyDataSetChanged();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        protected void convert(final ViewHolder holder, final CarFriendBean voiceroom, final int position)
        {
            final LinearLayout caidan=holder.getView(R.id.caidan_ll);
            final LinearLayout xial=holder.getView(R.id.xiala_ll);
            final LinearLayout address=holder.getView(R.id.addres_ll);
            final LinearLayout youZsll=holder.getView(R.id.z_zhuant_ll);

//            final TextView ztvBarley=holder.getView(R.id.z_tv_jinmai);
            final ImageView zIvfangzhu=holder.getView(R.id.z_iv_isfangzhu);
            final ImageView zIvAdmin=holder.getView(R.id.z_iv_guanli);
            final ImageView zIvBarley=holder.getView(R.id.z_iv_jinmai);
            address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(AutofriendsListActivity.this, MapLocationOverlayActivity.class);
                    intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) memberListBeans);
                    startActivity(intent);
                }
            });
            final ImageView xiaImg=holder.getView(R.id.xiala_iv);
            final TextView tvNickname=holder.getView(R.id.tvNickname);
            CircleImageView ivHead=holder.getView(R.id.ivHead);
            if (gonellposition!=position){
                youZsll.setVisibility(View.GONE);
                tvNickname.setVisibility(View.VISIBLE);
            }else {
                youZsll.setVisibility(View.VISIBLE);
                tvNickname.setVisibility(View.INVISIBLE);
            }
            ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (youZsll.getVisibility()==View.GONE){
                        youZsll.setVisibility(View.VISIBLE);
                        tvNickname.setVisibility(View.INVISIBLE);
                        gonellposition=position;
                    }else {
                        youZsll.setVisibility(View.GONE);
                        tvNickname.setVisibility(View.VISIBLE);
                        gonellposition=-1;
                    }
                    notifyDataSetChanged();
                }
            });

            TextView tvAddress=holder.getView(R.id.tvAddress);
            LinearLayout llFollow=holder.getView(R.id.llFollow);//添加关注
            final TextView tvFollow=holder.getView(R.id.tvFollow);
            final ImageView ivFollow=holder.getView(R.id.ivFollow);//
            LinearLayout llAdmin=holder.getView(R.id.llAdmin);//设置管理员
            final TextView tvAdmin=holder.getView(R.id.tvAdmin);
            final ImageView ivAdmin=holder.getView(R.id.iv_Admin);//
            LinearLayout llBarley=holder.getView(R.id.llBarley);//禁麦
            final ImageView ivBarley=holder.getView(R.id.ivBarley);//
            final TextView tvBarley=holder.getView(R.id.tvBarley);
            LinearLayout llKickOut=holder.getView(R.id.llKickOut);//踢出房间
            if (voiceroom!=null){
                final MemberListBean memberListBean=voiceroom.getMemberListBean();
                if (memberListBean!=null){
                    tvAddress.setText(memberListBean.getArea());
                    tvNickname.setText(memberListBean.getNickname());
                    if (!TextUtils.isEmpty(memberListBean.getHeaderImg())){
                        Picasso.with(AutofriendsListActivity.this)
                                .load(memberListBean.getHeaderImg())
                                .config(Bitmap.Config.RGB_565)
                                .centerCrop()
                                .fit()
                                .networkPolicy(NetworkPolicy.NO_STORE)
                                .into(ivHead);
                    }
                    if (memberListBean.getIsAttention().equals("1")){
                        tvFollow.setText("取消关注");
                        ivFollow.setBackgroundResource(R.mipmap.quxiaoguanzhu);
                    }else {
                        tvFollow.setText("添加关注");
                        ivFollow.setBackgroundResource(R.mipmap.lb_gz);
                    }

                    if (leaderId.equals(memberListBean.getFriendId()))
                    {
                        zIvfangzhu.setBackgroundResource(R.mipmap.fangzhu);
                    }else {
                        zIvfangzhu.setBackgroundResource(R.mipmap.fangzhu2);
                    }
                        if (memberListBean.getIsAdmin().equals("1")) {//0  否   1 是
                            tvAdmin.setText("取消管理员");
                            ivAdmin.setBackgroundResource(R.mipmap.lb_gly);
                            zIvAdmin.setBackgroundResource(R.mipmap.lb_gly);
                        } else {
                            tvAdmin.setText("管理员");
                            ivAdmin.setBackgroundResource(R.mipmap.guanli2);
                            zIvAdmin.setBackgroundResource(R.mipmap.guanli2);
                        }
                    if (memberListBean.getIsForbid().equals("1")){//0 是   1  否
                        tvBarley.setText("禁麦");
                        ivBarley.setBackgroundResource(R.mipmap.jinmai2);
                        zIvBarley.setBackgroundResource(R.mipmap.jinmai2);
                    }else {
                        tvBarley.setText("取消禁麦");
                        ivBarley.setBackgroundResource(R.mipmap.lb_jm);
                        zIvBarley.setBackgroundResource(R.mipmap.lb_jm);
                    }
                    llFollow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v){
                            if (!memberListBean.getFriendId().equals(preference.getUserId())){
                                if (memberListBean.getIsAttention().equals("0")){
                                    follow(preference.getUserId(), memberListBean.getFriendId(), "0");
                                    memberListBean.setIsAttention("1");
                                    tvFollow.setText("取消关注");
                                    ivFollow.setBackgroundResource(R.mipmap.quxiaoguanzhu);
                                }else {
                                    follow(preference.getUserId(), memberListBean.getFriendId(), "1");
                                    memberListBean.setIsAttention("0");
                                    tvFollow.setText("添加关注");
                                    ivFollow.setBackgroundResource(R.mipmap.lb_gz);
                                }
                            }else {
                                showTip("自己不能关注自己");
                            }

                        }
                    });
                    llAdmin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (leaderId.equals(BaseRequesUrl.uesrId)){
                                if (!memberListBean.getFriendId().equals(preference.getUserId())){
                                    if (memberListBean.getIsAdmin().equals("0")) {
                                        setAdmin(roomId, preference.getUserId(), memberListBean.getFriendId(), "1");
                                        memberListBean.setIsAdmin("1");
                                        tvAdmin.setText("取消管理员");
                                        ivAdmin.setBackgroundResource(R.mipmap.lb_gly);
                                        zIvAdmin.setBackgroundResource(R.mipmap.lb_gly);
                                    } else {
                                        setAdmin(roomId, preference.getUserId(), memberListBean.getFriendId(), "0");
                                        memberListBean.setIsAdmin("0");
                                        tvAdmin.setText("管理员");
                                        ivAdmin.setBackgroundResource(R.mipmap.guanli2);
                                        zIvAdmin.setBackgroundResource(R.mipmap.guanli2);

                                    }
                                }else {
                                    showTip("自己不能设置自己为管理员");
                                }
                            }else {
                                prompt("无权限");
                            }

                        }
                    });
                    llBarley.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (leaderId.equals(BaseRequesUrl.uesrId)) {
                                if (!memberListBean.getFriendId().equals(preference.getUserId())){
                                    if ( memberListBean.getIsForbid().equals("0")) {
                                        barley(roomId, memberListBean.getFriendId(), preference.getUserId(), "1");
                                        tvBarley.setText("禁麦");
                                        memberListBean.setIsForbid("1");
                                        ivBarley.setBackgroundResource(R.mipmap.jinmai2);
                                        zIvBarley.setBackgroundResource(R.mipmap.jinmai2);
                                    } else {
                                        barley(roomId, memberListBean.getFriendId(), preference.getUserId(), "0");
                                        tvBarley.setText("取消禁麦");
                                        memberListBean.setIsForbid("0");
                                        ivBarley.setBackgroundResource(R.mipmap.lb_jm);
                                        zIvBarley.setBackgroundResource(R.mipmap.lb_jm);

                                    }
                                }else {
                                    showTip("自己不能禁麦自己");
                                }
                            } else if (BaseRequesUrl.RoomGL.equals("1")&&memberListBean.getIsAdmin().equals("0")&&memberListBean.getIsLeader().equals("0")){
                                if (!memberListBean.getFriendId().equals(preference.getUserId())){
                                    if ( memberListBean.getIsForbid().equals("0")) {
                                        barley(roomId, memberListBean.getFriendId(), preference.getUserId(), "1");
                                        tvBarley.setText("禁麦");
                                        memberListBean.setIsForbid("1");
                                        ivBarley.setBackgroundResource(R.mipmap.jinmai2);
                                        zIvBarley.setBackgroundResource(R.mipmap.jinmai2);
                                    } else {
                                        barley(roomId, memberListBean.getFriendId(), preference.getUserId(), "0");
                                        tvBarley.setText("取消禁麦");
                                        memberListBean.setIsForbid("0");
                                        ivBarley.setBackgroundResource(R.mipmap.lb_jm);
                                        zIvBarley.setBackgroundResource(R.mipmap.lb_jm);
                                    }
                                }else {
                                    showTip("自己不能禁麦自己");
                                }
                            }else {
                                showTip("暂无权限");
                            }
                        }
                    });
                    llKickOut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (leaderId.equals(BaseRequesUrl.uesrId)){
                                if (!memberListBean.getFriendId().equals(preference.getUserId())){
                                    showLoading();
                                    kickOut(BaseRequesUrl.uesrId, memberListBean.getFriendId(), roomId);
                                }else {
                                    showTip("自己不能踢自己");
                                }
                            }else if(BaseRequesUrl.RoomGL.equals("1")&&memberListBean.getIsAdmin().equals("0")&&memberListBean.getIsLeader().equals("0")){
                                if (!memberListBean.getFriendId().equals(preference.getUserId())){
                                    showLoading();
                                    kickOut(BaseRequesUrl.uesrId, memberListBean.getFriendId(), roomId);
                                }else {
                                    showTip("自己不能踢自己");
                                }
                            } else {
                                showTip("暂无权限");
                            }

                        }
                    });

                }
            }

            if (!voiceroom.isXiala()){
                xiaImg.setBackgroundResource(R.mipmap.xia);
                caidan.setVisibility(View.GONE);
            }else {
                xiaImg.setBackgroundResource(R.mipmap.lb_up);
                caidan.setVisibility(View.VISIBLE);
            }
            xial.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (voiceroom.isXiala()){
                        xiaImg.setBackgroundResource(R.mipmap.xia);
                        caidan.setVisibility(View.GONE);
                        voiceroom.setXiala(false);
                        if (dataposition==position){
                            dataposition=-1;
                            jiluvoiceroom=null;
                            jiluholder=null;
                        }
                    }else {
                        xiaImg.setBackgroundResource(R.mipmap.lb_up);
                        caidan.setVisibility(View.VISIBLE);
                        voiceroom.setXiala(true);
                        if (dataposition!=-1&&dataposition!=position-1){
                            CarFriendBean voiceroom= getDatas().get(dataposition);
                            voiceroom.setXiala(false);
                            gengdai();
                        }
                        dataposition=position-1;
                        jiluholder=holder;
                        jiluvoiceroom=voiceroom;
                    }
//                   notifyDataSetChanged();
                }

            });

        }

    }

}
