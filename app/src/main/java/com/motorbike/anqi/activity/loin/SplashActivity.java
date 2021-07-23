package com.motorbike.anqi.activity.loin;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.text.TextUtils;

import com.motorbike.anqi.MainActivityTwo;
import com.motorbike.anqi.R;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.util.StatusBarUtil;
import com.motorbike.anqi.util.UserPreference;

public class SplashActivity extends BaseActivity {
    private  String gunggaoimg;
    private boolean c;
    private String userId;
    private UserPreference userPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        userPreference= UserPreference.getUserPreference(this);
        c= userPreference.getFirstLogin();
        userId= userPreference.getUserId();
        IsType();
    }

    private void IsType()
    {
        if (!c){
            Intent intent=new Intent(SplashActivity.this,GuideActivity.class);
            intent.putExtra("str","");
            startActivity(intent);
            finish();

//            okHttp(this, BaseRequesUrl.welcome, HttpTagUtil.WELCOME, new ArrayMap<String, Object>());
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run(){

                    if (!TextUtils.isEmpty(userId)) {
                        startActivity(new Intent(SplashActivity.this, MainActivityTwo.class));
                        finish();
                    }else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }

//                        overridePendingTransition(R.anim.popshow_anim, R.anim.pophidden_anim);

                }
            }, 1500);
        }
    }



    @Override
    protected void httpRequestData(Integer mTag, final String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);

        switch (mTag){
            case HttpTagUtil.WELCOME:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run(){
                        Intent intent=new Intent(SplashActivity.this,GuideActivity.class);
                        intent.putExtra("str",data);
                        startActivity(intent);
                        finish();
                    }
                }, 500);
                break;
        }
    }
}
