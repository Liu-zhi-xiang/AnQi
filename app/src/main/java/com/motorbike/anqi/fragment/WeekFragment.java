package com.motorbike.anqi.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.motorbike.anqi.R;
import com.motorbike.anqi.activity.my.UserCenterActivity;
import com.motorbike.anqi.bean.BangdanBean;
import com.motorbike.anqi.bean.MonthTopListBean;


import com.motorbike.anqi.bean.TripTopBean;
import com.motorbike.anqi.bean.WeekTopListBean;
import com.motorbike.anqi.beseAdapter.MultiItemTypeAdapter;
import com.motorbike.anqi.beseAdapter.base.ItemViewDelegate;
import com.motorbike.anqi.beseAdapter.base.ViewHolder;
import com.motorbike.anqi.init.BaseFragment;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.view.CircleImageView;
import com.motorbike.anqi.xrecyclerview.XRecyclerView;
import com.motorbike.anqi.xrecyclerview.progressindicator.ProgressStyle;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/31.
 * 周
 */

public class WeekFragment extends BaseFragment {
    private XRecyclerView recyclerView;
    private MultiItemTypeAdapter adapter;
    private List<TripTopBean> list;
    private String type;

    public static WeekFragment newInstance(String type) {
        WeekFragment fragment = new WeekFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getString("type");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.top_week_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setPullRefreshEnabled(false);
        recyclerView.setLoadingMoreEnabled(false);
        httpTop(type);
        list = new ArrayList<>();
        adapter = new MultiItemTypeAdapter(getActivity(), list);
        adapter.addItemViewDelegate(new TopThreeItem());
        adapter.addItemViewDelegate(new TopListItem());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void httpTop(String type) {
        Map<String, Object> map = new ArrayMap<>();
        map.put("type", type);
        okHttp(BaseRequesUrl.TripTop, HttpTagUtil.LEADERBOAR, map);
    }

    private List<MonthTopListBean> monthTopList;
    private List<WeekTopListBean> weekTopList;

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag) {
            case HttpTagUtil.LEADERBOAR:
                BangdanBean bangdanBean = new Gson().fromJson(data, BangdanBean.class);
                if (type.equals("0")) {
                    weekTopList = bangdanBean.getWeekTopList();
                }
                if (type.equals("1")) {
                    monthTopList = bangdanBean.getMonthTopList();
                }
                if (weekTopList != null) {
                    list.add(new TripTopBean(0, null, weekTopList.get(0)));
                    for (int x = 0; x < weekTopList.size(); x++) {
                        list.add(new TripTopBean(1, null, weekTopList.get(x)));
                    }
                }
                if (monthTopList != null) {
                    list.add(new TripTopBean(0, monthTopList.get(0), null));
                    for (int x = 0; x < monthTopList.size(); x++) {
                        list.add(new TripTopBean(1, monthTopList.get(x), null));
                    }
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }

    public class TopThreeItem implements ItemViewDelegate<TripTopBean> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.week_top_layout;
        }

        @Override
        public boolean isForViewType(TripTopBean item, int position) {
            return item.getType() == 0;
        }

        @Override
        public void convert(ViewHolder holder, TripTopBean tripTopBean, int position) {
            CircleImageView headImageOne = holder.getView(R.id.headImageOne);
            CircleImageView headImageTwo = holder.getView(R.id.headImageTwo);
            CircleImageView headImageThree = holder.getView(R.id.headImageThree);
            TextView tvName = holder.getView(R.id.tvName);
            TextView tvOneTrip = holder.getView(R.id.tvOneTrip);
            TextView tvTwoTrip = holder.getView(R.id.tvTwoTrip);
            TextView tvThreeTrip = holder.getView(R.id.tvThreeTrip);
            Log.e("aaa", list.size() + "zzxzxzxzzx");
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (!TextUtils.isEmpty(type)) {
                        if (type.equals("0")) {
                            WeekTopListBean weekTopListBean = list.get(i).getTopOtherBean();
                            if (weekTopListBean != null) {
                                String rank = weekTopListBean.getRank();
                                if (!TextUtils.isEmpty(rank)) {
                                    if (rank.equals("1")) {
                                        tvName.setText(weekTopListBean.getNickname());
                                        tvOneTrip.setText(weekTopListBean.getRidingKm());
                                        Picasso.with(getActivity())
                                                .load(weekTopListBean.getHeaderImg())
                                                .config(Bitmap.Config.RGB_565)
                                                .centerCrop()
                                                .fit()
                                                .networkPolicy(NetworkPolicy.NO_STORE)
                                                .into(headImageOne);
                                    } else if (rank.equals("2")) {
                                        tvTwoTrip.setText(weekTopListBean.getRidingKm());
                                        Picasso.with(getActivity())
                                                .load(weekTopListBean.getHeaderImg())
                                                .config(Bitmap.Config.RGB_565)
                                                .centerCrop()
                                                .fit()
                                                .networkPolicy(NetworkPolicy.NO_STORE)
                                                .into(headImageTwo);
                                    } else if (rank.equals("3")) {
                                        tvThreeTrip.setText(weekTopListBean.getRidingKm());
                                        Picasso.with(getActivity())
                                                .load(weekTopListBean.getHeaderImg())
                                                .config(Bitmap.Config.RGB_565)
                                                .centerCrop()
                                                .fit()
                                                .networkPolicy(NetworkPolicy.NO_STORE)
                                                .into(headImageThree);
                                    }
                                }
                            }
                        } else {
                            MonthTopListBean monthTopListBean = list.get(i).getTopThreeBean();
                            if (monthTopListBean != null) {
                                String rank = monthTopListBean.getRank();
                                if (!TextUtils.isEmpty(rank)) {
                                    if (rank.equals("1")) {
                                        tvName.setText(monthTopListBean.getNickname());
                                        tvOneTrip.setText(monthTopListBean.getRidingKm());
                                        Picasso.with(getActivity())
                                                .load(monthTopListBean.getHeaderImg())
                                                .config(Bitmap.Config.RGB_565)
                                                .centerCrop()
                                                .fit()
                                                .networkPolicy(NetworkPolicy.NO_STORE)
                                                .into(headImageOne);
                                    } else if (rank.equals("2")) {
                                        tvTwoTrip.setText(monthTopListBean.getRidingKm());
                                        Picasso.with(getActivity())
                                                .load(monthTopListBean.getHeaderImg())
                                                .config(Bitmap.Config.RGB_565)
                                                .centerCrop()
                                                .fit()
                                                .networkPolicy(NetworkPolicy.NO_STORE)
                                                .into(headImageTwo);
                                    } else if (rank.equals("3")) {
                                        tvThreeTrip.setText(monthTopListBean.getRidingKm());
                                        Picasso.with(getActivity())
                                                .load(monthTopListBean.getHeaderImg())
                                                .config(Bitmap.Config.RGB_565)
                                                .centerCrop()
                                                .fit()
                                                .networkPolicy(NetworkPolicy.NO_STORE)
                                                .into(headImageThree);
                                    }
                                }
                            }
                        }
                    }


                }
            }

        }
    }

    public class TopListItem implements ItemViewDelegate<TripTopBean> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.week_list_layout;
        }

        @Override
        public boolean isForViewType(TripTopBean item, int position) {
            return item.getType() == 1;
        }

        @Override
        public void convert(ViewHolder holder, TripTopBean tripTopBean, int position) {
            TextView tvNum = holder.getView(R.id.tvNum);
            CircleImageView headImage = holder.getView(R.id.headImage);
            TextView tvName = holder.getView(R.id.tvName);
            TextView tvClass = holder.getView(R.id.tvClass);
            TextView tvTotalNum = holder.getView(R.id.tvTotalNum);
            if (!TextUtils.isEmpty(type)) {
                if (type.equals("0")) {
                    WeekTopListBean weekTopListBean = tripTopBean.getTopOtherBean();
                    if (weekTopListBean != null) {
                        tvNum.setText(weekTopListBean.getRank());
                        tvName.setText(weekTopListBean.getNickname());
                        tvClass.setText(weekTopListBean.getLevel() + "级");
                        tvTotalNum.setText(weekTopListBean.getRidingKm());
                        Picasso.with(getActivity())
                                .load(weekTopListBean.getHeaderImg())
                                .config(Bitmap.Config.RGB_565)
                                .centerCrop()
                                .fit()
                                .networkPolicy(NetworkPolicy.NO_STORE)
                                .into(headImage);
                    }
                } else {
                    MonthTopListBean monthTopListBean = tripTopBean.getTopThreeBean();
                    if (monthTopListBean != null) {
                        tvNum.setText(monthTopListBean.getRank());
                        tvName.setText(monthTopListBean.getNickname());
                        tvClass.setText(monthTopListBean.getLevel() + "级");
                        tvTotalNum.setText(monthTopListBean.getRidingKm());
                        Picasso.with(getActivity())
                                .load(monthTopListBean.getHeaderImg())
                                .config(Bitmap.Config.RGB_565)
                                .centerCrop()
                                .fit()
                                .networkPolicy(NetworkPolicy.NO_STORE)
                                .into(headImage);
                    }
                }
            }

        }
    }


}
