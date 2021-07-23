package com.motorbike.anqi.activity.my;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.motorbike.anqi.R;
import com.motorbike.anqi.bean.MsgDetailBean;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.util.UserPreference;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统消息详情
 */
public class MessageDetailActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private TextView tvTitle,tvContent,tvDate;
    private String msgId;
    private Map<String,Object> msgMap;
    private UserPreference preference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        preference=UserPreference.getUserPreference(this);
        msgId=getIntent().getStringExtra("msgId");
        initView();
    }

    private void initView()
    {
        llBack=findViewById(R.id.llBack);
        llBack.setOnClickListener(this);
        tvTitle=findViewById(R.id.tvTitle);
        tvTitle.setText("系统消息");
        tvContent=findViewById(R.id.tvContent);
        tvDate=findViewById(R.id.tvDate);
        if (!TextUtils.isEmpty(msgId)){
            http(preference.getUserId(),msgId);
        }
    }

    private void http(String userid,String msgId){
        showLoading();
        msgMap=new HashMap<>();
        if (msgMap!=null){
            msgMap.clear();
        }
        msgMap.put("userId",userid);
        msgMap.put("messageId",msgId);
        okHttp(this, BaseRequesUrl.MsgInfo, HttpTagUtil.MsgInfo,msgMap);
    }

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag){
            case HttpTagUtil.MsgInfo:
                dismissLoading();
                if (data!=null){
                    MsgDetailBean msgDetailBean=new Gson().fromJson(data,MsgDetailBean.class);
                    if (msgDetailBean!=null){
                        tvContent.setText("系统消息提示:\n"+msgDetailBean.getMessageContent());
                        tvDate.setText(msgDetailBean.getSendTime());
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
