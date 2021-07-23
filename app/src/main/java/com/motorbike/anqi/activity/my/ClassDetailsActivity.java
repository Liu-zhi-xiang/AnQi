package com.motorbike.anqi.activity.my;

import android.graphics.Bitmap;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.motorbike.anqi.R;
import com.motorbike.anqi.bean.LevelBean;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.util.UserPreference;
import com.motorbike.anqi.view.CircleImageView;
import com.motorbike.anqi.view.SpringProgressView;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * 等级详情
 */
public class ClassDetailsActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private TextView tvTitle,tvName,tvGrade,tvNowKm,tvNumOne,tvNumTwo,tvGetKm;
    private CircleImageView ivImage;
    private SpringProgressView progressView;
    private Map<String ,Object> levelMap;
    private UserPreference preference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_details);
        preference=UserPreference.getUserPreference(this);

        initView();
    }

    private void initView() {
        llBack= findViewById(R.id.llBack);
        llBack.setOnClickListener(this);
        tvTitle= findViewById(R.id.tvTitle);
        tvTitle.setText("等级详情");
        tvName= findViewById(R.id.tvName);
        tvGrade= findViewById(R.id.tvGrade);
        tvNowKm=  findViewById(R.id.tvNowKm);
        tvNumOne= findViewById(R.id.tvNumOne);
        tvNumTwo= findViewById(R.id.tvNumTwo);
        tvGetKm=findViewById(R.id.tvGetKm);
        ivImage= findViewById(R.id.ivImage);
        progressView= findViewById(R.id.progressView);

        levelMap=new HashMap<>();
        httpLevel(preference.getUserId());
    }

    private void httpLevel(String userId){
        showLoading();
        if (levelMap!=null){
            levelMap.clear();
        }
        levelMap.put("userId",userId);
        okHttp(this, BaseRequesUrl.LevelInfo, HttpTagUtil.LevelInfo,levelMap);
    }

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag){
            case HttpTagUtil.LevelInfo:
                dismissLoading();
                if (data!=null){
                    LevelBean levelBean=new Gson().fromJson(data,LevelBean.class);
                    if (levelBean!=null){
                        tvName.setText(levelBean.getNickname());
                        tvGrade.setText(levelBean.getCurrentLevel());
                        tvNowKm.setText("当前公里数"+levelBean.getLicheng());
                        tvNumOne.setText(levelBean.getCurrentLevel());
                        tvNumTwo.setText(levelBean.getNextLevel());
                        progressView.setMaxCount(Float.parseFloat(levelBean.getNextLevelKm())-Float.parseFloat(levelBean.getCurrentKm()));
                        progressView.setCurrentCount(Float.parseFloat(levelBean.getLicheng())-Float.parseFloat(levelBean.getCurrentKm()));
                        String chaKm=String.valueOf(Float.parseFloat(levelBean.getNextLevelKm())-Float.parseFloat(levelBean.getLicheng()));
                        tvGetKm.setText("所需公里数"+chaKm);
                        Picasso.with(ClassDetailsActivity.this)
                                .load(levelBean.getHeaderImg())
                                .config(Bitmap.Config.RGB_565)
                                .centerCrop()
                                .fit()
                                .networkPolicy(NetworkPolicy.NO_STORE)
                                .into(ivImage);
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
