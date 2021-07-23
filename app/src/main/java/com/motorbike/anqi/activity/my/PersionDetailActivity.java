package com.motorbike.anqi.activity.my;


import android.content.Context;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.motorbike.anqi.R;
import com.motorbike.anqi.adapter.MessagesFragmentAdapter;
import com.motorbike.anqi.adapter.PersionActionAdapter;
import com.motorbike.anqi.adapter.PersionTripAdapter;
import com.motorbike.anqi.bean.OtherInfoBean;
import com.motorbike.anqi.fragment.AllUserFragment;
import com.motorbike.anqi.fragment.OnlyUserFragment;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.util.ScrollViewPager;
import com.motorbike.anqi.util.UserPreference;
import com.motorbike.anqi.view.SlideListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人详情---已废弃
 */
public class PersionDetailActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack,llOnly,llAll;
    private TextView tvTitle,tvOnly,tvAll;
    private ImageView ivOnly,ivAll;
    private int selectpage=1;
    private SlideListView slideListView;
    private PersionActionAdapter actionAdapter;
    private PersionTripAdapter tripAdapter;
    private List<String> list;
    private String friendId;
    private UserPreference preference;
    private Map<String,Object> otherMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persion_detail);
        preference=UserPreference.getUserPreference(this);
        friendId=getIntent().getStringExtra("friendId");
        initView();
    }

    private void initView() {
        llBack= findViewById(R.id.llBack);
        llBack.setOnClickListener(this);
        llOnly=findViewById(R.id.llOnly);
        llOnly.setOnClickListener(this);
        llAll=findViewById(R.id.llAll);
        llAll.setOnClickListener(this);
        tvTitle= findViewById(R.id.tvTitle);
        tvTitle.setText("个人详情");
        tvOnly= (TextView) findViewById(R.id.tvOnly);
        tvAll= (TextView) findViewById(R.id.tvAll);
        ivOnly= (ImageView) findViewById(R.id.ivOnly);
        ivAll= (ImageView) findViewById(R.id.ivAll);
        slideListView=findViewById(R.id.slideListView);
        list=new ArrayList<>();
        for (int i=0;i<20;i++){
            list.add("新收机车");
        }
        actionAdapter=new PersionActionAdapter(this);
        actionAdapter.setList(list);
        slideListView.setAdapter(actionAdapter);
        setListViewHeightBasedOnChildren(slideListView);
        if (!TextUtils.isEmpty(friendId)){
            http(preference.getUserId(),friendId);
        }
    }
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

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag){
            case HttpTagUtil.OtherInfo:
                dismissLoading();
                if (data!=null){
                    OtherInfoBean otherInfoBean=new Gson().fromJson(data,OtherInfoBean.class);
                    if (otherInfoBean!=null){

                    }
                }
                break;
        }
    }

    /**
     * 选择某一项 进行的操作
     * @param num
     */
    public void select(int num) {
        clean();
        switch (num) {
            case 1://选中已完成
                ivAll.setVisibility(View.GONE);
                ivOnly.setVisibility(View.VISIBLE);
                tvOnly.setTextColor(getResources().getColor(R.color.top_color));
                tvAll.setTextColor(getResources().getColor(R.color.white));
                break;
            case 2://选中未完成
                ivOnly.setVisibility(View.GONE);
                ivAll.setVisibility(View.VISIBLE);
                tvAll.setTextColor(getResources().getColor(R.color.top_color));
                tvOnly.setTextColor(getResources().getColor(R.color.white));
                break;

        }
    }

    /**
     *
     * 清除选择的状态
     */
    public void clean() {
        tvOnly.setTextColor(getResources().getColor(R.color.top_color));
        tvAll .setTextColor(getResources().getColor(R.color.white));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llBack:
                finish();
                break;
            case R.id.llOnly:
                selectpage = 1;
                select(selectpage);
                actionAdapter=new PersionActionAdapter(this);
                actionAdapter.setList(list);
                slideListView.setAdapter(actionAdapter);
                setListViewHeightBasedOnChildren(slideListView);
                break;
            case R.id.llAll:
                selectpage = 2;
                select(selectpage);
                tripAdapter=new PersionTripAdapter(this);
                tripAdapter.setList(list);
                slideListView.setAdapter(tripAdapter);
                setListViewHeightBasedOnChildren(slideListView);
                break;
        }
    }
    /**
     * 动态设置ListView的高度
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


}
