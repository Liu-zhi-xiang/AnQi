package com.motorbike.anqi.activity.trip;

import android.graphics.Bitmap;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.motorbike.anqi.R;
import com.motorbike.anqi.activity.my.UserCenterActivity;
import com.motorbike.anqi.adapter.TripDetailOwnAdapter;
import com.motorbike.anqi.bean.OwnTripDetaiBean;
import com.motorbike.anqi.bean.RidingDataListBean;
import com.motorbike.anqi.bean.TripDetailBean;
import com.motorbike.anqi.bean.TripInfoStrBean;
import com.motorbike.anqi.beseAdapter.MultiItemTypeAdapter;
import com.motorbike.anqi.beseAdapter.base.ItemViewDelegate;
import com.motorbike.anqi.beseAdapter.base.ViewHolder;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.util.UserPreference;
import com.motorbike.anqi.view.CircleImageView;
import com.motorbike.anqi.view.SlideListView;
import com.motorbike.anqi.xrecyclerview.XRecyclerView;
import com.motorbike.anqi.xrecyclerview.progressindicator.ProgressStyle;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 行程详情
 */
public class TripDetailActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private TextView tvTitle;
    private XRecyclerView recyclerView;
    private MultiItemTypeAdapter adapter;
    private List<TripDetailBean> list;
    private String routeId,tripId,type;
    private Map<String,Object> oweDetailMap,teamMap;
    private UserPreference preference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        preference=UserPreference.getUserPreference(this);
        routeId=getIntent().getStringExtra("routeId");
        tripId=getIntent().getStringExtra("tripId");
        type=getIntent().getStringExtra("type");
        initView();
    }

    private void initView()
    {
        llBack = findViewById(R.id.llBack);
        llBack.setOnClickListener(this);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("行程详情");
        recyclerView =  findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setPullRefreshEnabled(false);
        recyclerView.setLoadingMoreEnabled(false);
        list = new ArrayList<>();
        if (!TextUtils.isEmpty(type)){
            if (type.equals("1")){
                if (!TextUtils.isEmpty(tripId)){
                    http(preference.getUserId(),tripId);
                }
            }else {
                if (!TextUtils.isEmpty(routeId)&&!TextUtils.isEmpty(tripId)){
                    httpTeam(routeId,preference.getUserId());
                }
            }
        }
    }

    /**
     * 个人历史行程
     * @param userId
     * @param
     * @param tripId
     */
    private void http(String userId,String tripId)
    {
        showLoading();
        oweDetailMap=new HashMap<>();
        if (oweDetailMap!=null){
            oweDetailMap.clear();
        }
        oweDetailMap.put("userId",userId);
        oweDetailMap.put("tripId",tripId);
        okHttp(this, BaseRequesUrl.OwnTripInfo, HttpTagUtil.OwnTripInfo,oweDetailMap);
    }

    /**
     * 团队历史行程
     * @param routeId
     */
    private void httpTeam(String routeId,String userId){
        showLoading();
        teamMap=new HashMap<>();
        if (teamMap!=null){
            teamMap.clear();
        }
        teamMap.put("routeId",routeId);
        teamMap.put("userId",userId);
        okHttp(this,BaseRequesUrl.TeamTripInfo,HttpTagUtil.TeamTripInfo,teamMap);
    }
    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag){
            case HttpTagUtil.OwnTripInfo:
            case HttpTagUtil.TeamTripInfo:
                dismissLoading();
                if (data!=null){
                    OwnTripDetaiBean detaiBean=new Gson().fromJson(data,OwnTripDetaiBean.class);
                    if (detaiBean!=null){
                        TripInfoStrBean infoStrBean=detaiBean.getTripInfoStr();
                        List<RidingDataListBean> listBeans=detaiBean.getRidingDataList();
                        if (infoStrBean!=null){
                            list.add(new TripDetailBean(0,infoStrBean,null));
                        }
                        if (listBeans!=null){
                            list.add(new TripDetailBean(1,null,listBeans));
                        }
                        adapter = new MultiItemTypeAdapter(this, list);
                        adapter.addItemViewDelegate(new TripDetailOne());
                        adapter.addItemViewDelegate(new TripDetailTwo());
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }
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


    public class TripDetailOne implements ItemViewDelegate<TripDetailBean> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.tripdetail_one_layout;
        }

        @Override
        public boolean isForViewType(TripDetailBean item, int position) {
            return item.getType() == 0;
        }

        @Override
        public void convert(ViewHolder holder, TripDetailBean tripDetailBean, int position) {
            CircleImageView headImage=holder.getView(R.id.headImage);
            TextView tvNickname=holder.getView(R.id.tvNickname);
            TextView tvAddress=holder.getView(R.id.tvAddress);
            TextView tvDate=holder.getView(R.id.tvDate);
            TextView tvCarType=holder.getView(R.id.tvCarType);
            TextView tvStartTime=holder.getView(R.id.tvStartTime);
            TextView tvAllTime=holder.getView(R.id.tvAllTime);
            TextView tvKm=holder.getView(R.id.tvKm);
            TripInfoStrBean tripInfoStrBean=tripDetailBean.getTopThreeBean();
            if (tripInfoStrBean!=null){
                tvNickname.setText(tripInfoStrBean.getNickname());
                tvAddress.setText(tripInfoStrBean.getCity());
                tvDate.setText(tripInfoStrBean.getStartTime());
                tvKm.setText(tripInfoStrBean.getRidingKm()+"km");
                tvStartTime.setText(tripInfoStrBean.getStartTime1());
                tvAllTime.setText(tripInfoStrBean.getRidingTime());
                tvCarType.setText(tripInfoStrBean.getCarType());
                Picasso.with(TripDetailActivity.this)
                        .load(tripInfoStrBean.getHeaderImg())
                        .config(Bitmap.Config.RGB_565)
                        .centerCrop()
                        .fit()
                        .networkPolicy(NetworkPolicy.NO_STORE)
                        .into(headImage);
            }

        }
    }

    public class TripDetailTwo implements ItemViewDelegate<TripDetailBean> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.tripdetail_two_layout;
        }

        @Override
        public boolean isForViewType(TripDetailBean item, int position) {
            return item.getType() == 1;
        }

        @Override
        public void convert(ViewHolder holder, TripDetailBean tripDetailBean, int position) {
            List<RidingDataListBean> listBeanList=tripDetailBean.getTopOtherBean();
//            CircleImageView headImage=holder.getView(R.id.headImage);
//            TextView tvJiSu=holder.getView(R.id.tvJiSu);
//            TextView tvJunSu=holder.getView(R.id.tvJunSu);
//            TextView tvJiaSu=holder.getView(R.id.tvJiaSu);
//            TextView tvJiaoDu=holder.getView(R.id.tvJiaoDu);
            SlideListView slideListView=holder.getView(R.id.slideListView);
            TripDetailOwnAdapter ownAdapter=new TripDetailOwnAdapter(TripDetailActivity.this);
            slideListView.setAdapter(ownAdapter);
            if (listBeanList!=null){
                ownAdapter.setList(listBeanList);
                ownAdapter.setType(type);
            }


        }
    }


}
