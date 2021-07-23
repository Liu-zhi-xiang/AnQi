package com.motorbike.anqi.activity.my;


import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.motorbike.anqi.R;
import com.motorbike.anqi.adapter.FirendsListAdapter;
import com.motorbike.anqi.bean.MemberListBean;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.util.UserPreference;
import com.motorbike.anqi.view.SlideListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.motorbike.anqi.view.SlideListView.MOD_FORBID;

/**
 * 好友列表
 */
public class FirendsListActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private TextView tvTitle;
    private SlideListView slideListView;
    private FirendsListAdapter adapter;
    private Map<String,Object> friendListMap;
    private UserPreference preference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firends_list);
        preference=UserPreference.getUserPreference(this);
        http(preference.getUserId());
        initView();
    }

    private void initView() {
        llBack= findViewById(R.id.llBack);
        llBack.setOnClickListener(this);
        tvTitle= findViewById(R.id.tvTitle);
        tvTitle.setText("好友列表");
        slideListView= findViewById(R.id.slideListView);
        slideListView.initSlideMode(MOD_FORBID);

        adapter=new FirendsListAdapter(this);
        slideListView.setAdapter(adapter);

    }
    private void http(String userId){
        showLoading();
        friendListMap=new HashMap<>();
        if (friendListMap!=null){
            friendListMap.clear();
        }
        friendListMap.put("userId",userId);
        okHttp(this, BaseRequesUrl.FriendList, HttpTagUtil.FriendList,friendListMap);
    }

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag){
            case HttpTagUtil.FriendList:
                dismissLoading();
                if (data!=null){
                    List<MemberListBean> memberListBeans=new Gson().fromJson(data,new TypeToken<List<MemberListBean>>(){}.getType());
                    if (memberListBeans!=null){
                        adapter.setList(memberListBeans);
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llBack:
                finish();
                break;
        }
    }
}
