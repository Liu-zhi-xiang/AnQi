package com.motorbike.anqi.activity.my;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.motorbike.anqi.R;
import com.motorbike.anqi.activity.loin.LoginActivity;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.util.DataCleanManager;
import com.motorbike.anqi.util.UserPreference;

import java.util.HashMap;
import java.util.Map;

/**
 * 设置
 */
public class SetActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack,llCache,llFeedback,llAboutWe;
    private TextView tvCache,tvSignOut,tvTitle;
    private DataCleanManager data;
    private Map<String,Object> signOutMap;
    private UserPreference preference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        preference=UserPreference.getUserPreference(this);
        initView();
    }

    private void initView() {
        llBack= findViewById(R.id.llBack);
        llBack.setOnClickListener(this);
        llCache= findViewById(R.id.llCache);
        llCache.setOnClickListener(this);
        llFeedback= findViewById(R.id.llFeedback);
        llFeedback.setOnClickListener(this);
        llAboutWe=  findViewById(R.id.llAboutWe);
        llAboutWe.setOnClickListener(this);
        tvCache= findViewById(R.id.tvCache);
        tvSignOut=  findViewById(R.id.tvSignOut);
        tvSignOut.setOnClickListener(this);
        tvTitle= findViewById(R.id.tvTitle);
        tvTitle.setText("设置");

        try {
            tvCache.setText(data.getTotalCacheSize(SetActivity.this));
        } catch (Exception e1) {
            e1.printStackTrace();
        } // 获取大缓存大小
        signOutMap=new HashMap<>();
    }

    private void http(String userId){
        showLoading();
        if (signOutMap!=null){
            signOutMap.clear();
        }
        signOutMap.put("userId",userId);
        okHttp(this, BaseRequesUrl.Loginout, HttpTagUtil.Loginout,signOutMap);
    }

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag){
            case HttpTagUtil.Loginout:
                dismissLoading();
                showTip(mag);
                BaseRequesUrl.uesrId="";
                BaseRequesUrl.account="";
                BaseRequesUrl.uesrName="";
                BaseRequesUrl.uesrHead="";
                preference.clear();
                preference.setJpushId(BaseRequesUrl.jpushId);
                preference.save();
                startActivity(new Intent(this, LoginActivity.class));
                this.finish();
                break;
        }
    }

    /**
     * 清楚应用缓存
     */
    private void ClearCache() {

        try {
            if (data.getTotalCacheSize(SetActivity.this).equals("0K")) {
                showToast("暂不需要清理缓存大小");
            } else {
                data.clearAllCache(SetActivity.this);
                tvCache.setText("0M");
                showTip("清理缓存成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llBack:
                finish();
                break;
            case R.id.llCache://清除缓存
                ClearCache();
                break;
            case R.id.llFeedback://意见反馈
                startActivity(new Intent(this,FeedbackActivity.class));
                break;
            case R.id.llAboutWe://关于我们
                startActivity(new Intent(this,AboutWeActivity.class));
                break;
            case R.id.tvSignOut://退出登录
                showAlertDialog("提示信息","确认退出登录?");

                break;
        }
    }

    @Override
    protected void onclickDialog() {
        super.onclickDialog();
        http(preference.getUserId());
    }
}
