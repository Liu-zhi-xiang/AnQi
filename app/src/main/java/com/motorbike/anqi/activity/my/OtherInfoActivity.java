package com.motorbike.anqi.activity.my;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.motorbike.anqi.R;
import com.motorbike.anqi.activity.trip.TripDetailActivity;
import com.motorbike.anqi.bean.NoteImgListBean;
import com.motorbike.anqi.bean.NoteListBean;
import com.motorbike.anqi.bean.OtherDetailBean;
import com.motorbike.anqi.bean.OtherInfoBean;
import com.motorbike.anqi.bean.OtherInfoTwoBean;
import com.motorbike.anqi.bean.TripDetailBean;
import com.motorbike.anqi.bean.TripInfoStrBean;
import com.motorbike.anqi.bean.TripListBean;
import com.motorbike.anqi.bean.UsreBean;
import com.motorbike.anqi.beseAdapter.MultiItemTypeAdapter;
import com.motorbike.anqi.beseAdapter.base.ItemViewDelegate;
import com.motorbike.anqi.beseAdapter.base.ViewHolder;
import com.motorbike.anqi.greendao.gen.DBManager;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.util.UserPreference;
import com.motorbike.anqi.view.CircleImageView;
import com.motorbike.anqi.xrecyclerview.XRecyclerView;
import com.motorbike.anqi.xrecyclerview.progressindicator.ProgressStyle;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

public class OtherInfoActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private TextView tvTitle;
    private XRecyclerView recyclerView;
    private MultiItemTypeAdapter adapter;
    private Map<String,Object> otherMap,followMap;
    private String friendId;
    private UserPreference preference;
    private List<OtherInfoTwoBean> twoBeanList;
    private List<OtherInfoTwoBean> notionList;
    private List<OtherInfoTwoBean> tripList;
    private OtherDetailBean otherDetailBean;
    private boolean isFollow=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_info);
        preference=UserPreference.getUserPreference(this);
        friendId=getIntent().getStringExtra("friendId");
        if (!TextUtils.isEmpty(friendId)){
            http(preference.getUserId(),friendId);
        }
        initView();
    }

    private void initView() {
        llBack = findViewById(R.id.llBack);
        llBack.setOnClickListener(this);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("个人详情");
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setPullRefreshEnabled(false);
        recyclerView.setLoadingMoreEnabled(false);
        twoBeanList=new ArrayList<>();
        notionList=new ArrayList<>();
        tripList=new ArrayList<>();
        if (twoBeanList!=null){
            adapter=new MultiItemTypeAdapter(this,twoBeanList);
            adapter.addItemViewDelegate(new OtherInfoOne());
            adapter.addItemViewDelegate(new OtherInfoTwo());
            adapter.addItemViewDelegate(new OtherInfoThree());
            recyclerView.setAdapter(adapter);
        }

    }

    /**
     * 个人详情接口
     * @param userId
     * @param friendId
     */
    private void http(String userId,String friendId){
        showLoading();
        otherMap=new HashMap<>();
        if (otherMap!=null){
            otherMap.clear();
        }
        otherMap.put("userId",userId);
        otherMap.put("friendId",friendId);
        okHttp(this, BaseRequesUrl.OtherInfo, HttpTagUtil.OtherInfo,otherMap);
    }



    /**
     *
     * 关注好友
     *
     * @param userId
     * @param friendId
     * @param type
     */
    private void httpFollow(String userId,String friendId,String type)
    {
        followMap=new HashMap<>();
        if (followMap!=null){
            followMap.clear();
        }
        followMap.put("userId",userId);
        followMap.put("friendId",friendId);
        followMap.put("operateType",type);
        okHttp(this,BaseRequesUrl.FollowFriends,HttpTagUtil.FollowFriends,followMap);
    }

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag){
            case HttpTagUtil.OtherInfo:
                dismissLoading();
                if (data!=null){
                    OtherInfoBean otherInfoBean=new Gson().fromJson(data,OtherInfoBean.class);
                    if (otherInfoBean!=null){
                        otherDetailBean=new OtherDetailBean(
                                otherInfoBean.getUserId()
                                ,otherInfoBean.getNickname()
                                ,otherInfoBean.getArea()
                                ,otherInfoBean.getHeaderImg()
                                ,otherInfoBean.getSex()
                                ,otherInfoBean.getAge()
                                ,otherInfoBean.getLevel()
                                ,otherInfoBean.getCarType()
                                ,otherInfoBean.getTotalMileage()
                                ,otherInfoBean.getBirthday()
                                ,otherInfoBean.getAttention()
                                ,otherInfoBean.getPointNum()
                                ,otherInfoBean.getPhone());
                        twoBeanList.add(new OtherInfoTwoBean(0,null,null,otherDetailBean));
//                        tripList.add(new OtherInfoTwoBean(0,null,null,otherDetailBean));
                        List<NoteListBean> noteListBeans=otherInfoBean.getNoteList();
                        Log.e("aaa",noteListBeans.size()+"  aaasize");
                        if (noteListBeans!=null){
                            for (int x=0;x<noteListBeans.size();x++){
                                notionList.add(new OtherInfoTwoBean(1,noteListBeans.get(x),null,null));
                            }
                        }
                        twoBeanList.addAll(notionList);
                        List<TripListBean> tripListBeans=otherInfoBean.getTripList();
                        Log.e("aaa",tripListBeans.size()+"  aaasize");
                        if (tripListBeans!=null){
                            for (int i=0;i<tripListBeans.size();i++){
                                tripList.add(new OtherInfoTwoBean(2,null,tripListBeans.get(i),null));
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
            case HttpTagUtil.FollowFriends:
                showTip(mag);
                break;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llBack:
                finish();
                break;
        }
    }

    public class OtherInfoOne implements ItemViewDelegate<OtherInfoTwoBean> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.otherinfo_one;
        }

        @Override
        public boolean isForViewType(OtherInfoTwoBean item, int position) {
            return item.getType() == 0;
        }

        @Override
        public void convert(ViewHolder holder, final OtherInfoTwoBean otherInfoTwoBean, int position) {
            CircleImageView headImage=holder.getView(R.id.headImage);
            TextView tvNickname=holder.getView(R.id.tvNickname);
            ImageView ivSex=holder.getView(R.id.ivSex);
            TextView tvAddress=holder.getView(R.id.tvAddress);
            TextView tvAge=holder.getView(R.id.tvAge);
            TextView tvCarType=holder.getView(R.id.tvType);
            TextView tvKm=holder.getView(R.id.tvKm);
            TextView tvClassDetail=holder.getView(R.id.tvClassDetail);
            final TextView tvFollow=holder.getView(R.id.tvFollow);
            TextView tvLiaoTian=holder.getView(R.id.tvLiaoTian);
            TextView tvJiFen=holder.getView(R.id.tvJiFen);
            LinearLayout llOnly=holder.getView(R.id.llOnly);//动态
            LinearLayout llAll=holder.getView(R.id.llAll);
            final TextView tvOnly=holder.getView(R.id.tvOnly);
            final ImageView ivOnly=holder.getView(R.id.ivOnly);
            final TextView tvAll=holder.getView(R.id.tvAll);
            final ImageView ivAll=holder.getView(R.id.ivAll);
            final OtherDetailBean otherDetailBean=otherInfoTwoBean.getOtherDetailBean();

            if (otherDetailBean!=null){
                Picasso.with(OtherInfoActivity.this)
                        .load(otherDetailBean.getHeaderImg())
                        .config(Bitmap.Config.RGB_565)
                        .centerCrop()
                        .fit()
                        .networkPolicy(NetworkPolicy.NO_STORE)
                        .into(headImage);
                tvNickname.setText(otherDetailBean.getNickname());
                tvAddress.setText(otherDetailBean.getArea());
                tvAge.setText(otherDetailBean.getAge());
                if (!TextUtils.isEmpty(otherDetailBean.getCarType())){
                    tvCarType.setText(otherDetailBean.getCarType());
                }
                tvClassDetail.setText(otherDetailBean.getLevel());
                String sex=otherDetailBean.getSex();
                if (!TextUtils.isEmpty(sex)){
                    if (sex.equals("1")){
                        ivSex.setImageResource(R.mipmap.grxq_nan);
                    }else {
                        ivSex.setImageResource(R.mipmap.grxq_nv);
                    }
                }
                tvKm.setText(otherDetailBean.getTotalMileage());
                tvJiFen.setText(otherDetailBean.getPointNum());
                String attention=otherDetailBean.getAttention();//0未关注   1 已关注
                if (!TextUtils.isEmpty(attention)){
                    if (attention.equals("0")){
                        tvFollow.setText("未关注");
                        isFollow=false;
                    }else {
                        tvFollow.setText("已关注");
                        isFollow=true;
                    }
                }
            }
            //动态
            llOnly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ivAll.setVisibility(View.GONE);
                    ivOnly.setVisibility(View.VISIBLE);
                    tvOnly.setTextColor(getResources().getColor(R.color.top_color));
                    tvAll.setTextColor(getResources().getColor(R.color.white));
                    if (twoBeanList!=null){
                        twoBeanList.clear();
                    }
                    if (otherDetailBean!=null){
                        twoBeanList.add(new OtherInfoTwoBean(0,null,null,otherDetailBean));
                    }
                    twoBeanList.addAll(notionList);
                    adapter.notifyDataSetChanged();
                }
            });
            //行程
            llAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ivOnly.setVisibility(View.GONE);
                    ivAll.setVisibility(View.VISIBLE);
                    tvAll.setTextColor(getResources().getColor(R.color.top_color));
                    tvOnly.setTextColor(getResources().getColor(R.color.white));
                    twoBeanList.clear();
                    if (otherDetailBean!=null){
                        twoBeanList.add(new OtherInfoTwoBean(0,null,null,otherDetailBean));
                    }
                    twoBeanList.addAll(tripList);
                    adapter.notifyDataSetChanged();
                }
            });

            tvFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFollow ==false&&otherDetailBean.getAttention().equals("0")){
                        httpFollow(preference.getUserId(),friendId,"0");
                        tvFollow.setText("已关注");
                        otherDetailBean.setAttention("1");
                        isFollow=true;
                    }else {
                        httpFollow(preference.getUserId(),friendId,"1");
                        tvFollow.setText("未关注");
                        otherDetailBean.setAttention("0");
                        isFollow=false;
                    }
                }
            });

            tvLiaoTian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (RongIM.getInstance() != null) {
                        RongIM.getInstance().startConversation(OtherInfoActivity.this, Conversation.ConversationType.PRIVATE, friendId , otherInfoTwoBean.getOtherDetailBean().getNickname());
                        DBManager.getInstance(OtherInfoActivity.this).insertUser(new UsreBean(otherInfoTwoBean.getOtherDetailBean().getNickname()
                                ,friendId
                                ,otherInfoTwoBean.getOtherDetailBean().getHeaderImg()));

                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(friendId,otherInfoTwoBean.getOtherDetailBean().getNickname(), Uri.parse(otherInfoTwoBean.getOtherDetailBean().getHeaderImg())));
                    }
                }
            });
        }
    }
    public class OtherInfoTwo implements ItemViewDelegate<OtherInfoTwoBean> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.persionaction_layout;
        }

        @Override
        public boolean isForViewType(OtherInfoTwoBean item, int position) {
            return item.getType() == 1;
        }

        @Override
        public void convert(ViewHolder holder, OtherInfoTwoBean otherInfoTwoBean, int position) {
            final RelativeLayout rlBackGround=holder.getView(R.id.rlBackGround);
            TextView tvName=holder.getView(R.id.tvName);
            TextView tvDate=holder.getView(R.id.tvDate);
            ImageView ivBackground=holder.getView(R.id.ivBackground);
            NoteListBean noteListBean=otherInfoTwoBean.getNoteListBean();
            if (noteListBean!=null){
                tvName.setText(noteListBean.getTitle());
                tvDate.setText(noteListBean.getPublishTimeStr());
                List<NoteImgListBean> noteListBeans=otherInfoTwoBean.getNoteListBean().getNoteImgList();
                if (noteListBeans!=null&&noteListBeans.size()>0){
                    String image=noteListBeans.get(0).getImgUrl();
                    if (!TextUtils.isEmpty(image)){
                        Picasso.with(OtherInfoActivity.this)
                                .load(image)
                                .config(Bitmap.Config.RGB_565)
                                .centerCrop()
                                .fit()
                                .networkPolicy(NetworkPolicy.NO_STORE)
                                .into(ivBackground);
//                        Picasso.with(OtherInfoActivity.this).load(image).into(new Target() {
//                            @Override
//                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                                if (android.os.Build.VERSION.SDK_INT > 15) {
//                                    rlBackGround.setBackground(new BitmapDrawable(getBaseContext().getResources(), bitmap));
//                                } else {
//                                    rlBackGround.setBackgroundDrawable(new BitmapDrawable(getBaseContext().getResources(), bitmap));
//                                }
//                            }
//                            @Override
//                            public void onBitmapFailed(final Drawable errorDrawable) {
//                            }
//
//                            @Override
//                            public void onPrepareLoad(final Drawable placeHolderDrawable) {
//                            }
//                        });
                    }
                }

//                if (noteListBean.getNoteImgList()!=null&&noteListBean.getNoteImgList().size()>0){
//                    for (int i=0;i<noteListBean.getNoteImgList().size();i++){
//                        String backGroud=noteListBean.getNoteImgList().get(i).getImgUrl();
//                        if (!TextUtils.isEmpty(backGroud)){
//                            Picasso.with(OtherInfoActivity.this).load(backGroud).into(new Target() {
//                                @Override
//                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                                    if (android.os.Build.VERSION.SDK_INT > 15) {
//                                        rlBackGround.setBackground(new BitmapDrawable(getBaseContext().getResources(), bitmap));
//                                    } else {
//                                        rlBackGround.setBackgroundDrawable(new BitmapDrawable(getBaseContext().getResources(), bitmap));
//                                    }
//                                }
//                                @Override
//                                public void onBitmapFailed(final Drawable errorDrawable) {
//                                }
//
//                                @Override
//                                public void onPrepareLoad(final Drawable placeHolderDrawable) {
//                                }
//                            });
//                        }
//                    }
//                }
            }

        }
    }
    public class OtherInfoThree implements ItemViewDelegate<OtherInfoTwoBean> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.persiontrip_layout;
        }

        @Override
        public boolean isForViewType(OtherInfoTwoBean item, int position) {
            return item.getType() == 2;
        }

        @Override
        public void convert(ViewHolder holder, OtherInfoTwoBean otherInfoTwoBean, int position) {
            TextView tvDate=holder.getView(R.id.tvDate);
            TextView tvStartEndTime=holder.getView(R.id.tvStartEndTime);
            TextView tvShiChang=holder.getView(R.id.tvShiChang);
            TextView tvKm=holder.getView(R.id.tvKm);
            TextView tvJiSu=holder.getView(R.id.tvJiSu);
            TextView tvJunSu=holder.getView(R.id.tvJunSu);
            TextView tvJiaSu=holder.getView(R.id.tvJiaSu);
            TextView tvJiaoDu=holder.getView(R.id.tvJiaoDu);
            TripListBean tripListBean=otherInfoTwoBean.getTripListBean();
            if (tripListBean!=null){
                tvDate.setText(tripListBean.getStartTime());
                tvShiChang.setText(tripListBean.getRidingTime());
                tvKm.setText(tripListBean.getRidingKm()+"km");
                tvJiSu.setText(tripListBean.getExtreme());
                tvJunSu.setText(tripListBean.getAvgSpeed());
                tvJiaSu.setText(tripListBean.getAccelerate100km());
                tvJiaoDu.setText(tripListBean.getBend());
            }

        }
    }
}
