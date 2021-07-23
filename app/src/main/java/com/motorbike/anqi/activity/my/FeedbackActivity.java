package com.motorbike.anqi.activity.my;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.motorbike.anqi.R;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.util.UserPreference;

import java.util.HashMap;
import java.util.Map;

/**
 *意见反馈
 */
public class FeedbackActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack,llTiJiao;
    private TextView tvTitle;
    private EditText etContent;
    private Map<String,Object> feedMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initView();
    }

    private void initView() {
        llBack= findViewById(R.id.llBack);
        llBack.setOnClickListener(this);
        llTiJiao= findViewById(R.id.llTiJiao);
        llTiJiao.setOnClickListener(this);
        tvTitle= findViewById(R.id.tvTitle);
        tvTitle.setText("意见反馈");
        etContent= findViewById(R.id.etContent);
        feedMap=new HashMap<>();
    }

    private void http(String content){
        showLoading();
        if (feedMap!=null){
            feedMap.clear();
        }
        feedMap.put("content",content);
        okHttp(this, BaseRequesUrl.FeedBack, HttpTagUtil.FeedBack,feedMap);
    }

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag){
            case HttpTagUtil.FeedBack:
                dismissLoading();
                showTip(mag);
                finish();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llBack:
                finish();
                break;
            case R.id.llTiJiao:
                String content=etContent.getText().toString().trim();
                if (!TextUtils.isEmpty(content)){
                    http(content);
                }else {
                    showTip("说点什么吧。。。。");
                }
                break;
        }
    }
}
