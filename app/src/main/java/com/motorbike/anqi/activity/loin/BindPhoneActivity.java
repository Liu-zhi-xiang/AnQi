package com.motorbike.anqi.activity.loin;

import android.content.Intent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.motorbike.anqi.MainActivityTwo;
import com.motorbike.anqi.R;
import com.motorbike.anqi.bean.LoginBean;
import com.motorbike.anqi.handler.BaseHandlerOperate;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.main.AutoMomentsActivity;
import com.motorbike.anqi.util.TimeCount;
import com.motorbike.anqi.util.UserPreference;

import java.util.HashMap;
import java.util.Map;

/**
 * 绑定手机号
 */
public class BindPhoneActivity extends BaseActivity implements View.OnClickListener {
    private EditText phoneEt, codeEt;
    private String phoneStr, codeStr, opendid, type,nickName;
    private TextView codeTv;
    private UserPreference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        iitView();
    }

    private void iitView() {
        preference = UserPreference.getUserPreference(this);
        opendid = getIntent().getStringExtra("opendid");
        type = getIntent().getStringExtra("type");
        nickName=getIntent().getStringExtra("nickName");
        Log.e("aaaa", opendid);
        Log.e("aaaa", type);

        findViewById(R.id.back_layout).setOnClickListener(this);
        findViewById(R.id.queren_ll).setOnClickListener(this);
        codeTv = findViewById(R.id.code_tv);
        codeTv.setOnClickListener(this);
        phoneEt = findViewById(R.id.phone_et);
        codeEt = findViewById(R.id.code_et);
        phoneEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phoneStr = s.toString();
            }
        });
        codeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                codeStr = s.toString();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                prompt("未完成手机绑定");
                finish();
                break;
            case R.id.code_tv:
                if (!TextUtils.isEmpty(phoneStr)) {
                    countDown();
                    Map<String, Object> codeMap = new HashMap<>();
                    codeMap.put("phone", phoneStr);
                    codeMap.put("type", "2");
                    okHttp(this, BaseRequesUrl.GETCODE, HttpTagUtil.GETCODE, codeMap);
                } else {
                    prompt("请输入手机号");
                }
                break;
            case R.id.queren_ll:
                if (!TextUtils.isEmpty(phoneStr)) {
                    if (!TextUtils.isEmpty(codeStr)) {
                        showLoading();
                        if (TextUtils.isEmpty(nickName)){
                            nickName=phoneStr;
                        }
                        qingquihttp(phoneStr, opendid, type, codeStr,BaseRequesUrl.jpushId);
                    } else {
                        prompt("请输入验证码");
                    }
                } else {
                    prompt("请输入手机号");
                }
                break;

        }
    }

    /**
     * but计时
     */
    private TimeCount count;

    private void countDown() {
        count = new TimeCount(60000, 1000, codeTv, this);
        count.start();
    }

    private void qingquihttp(String phone, String openid, String type, String code,String registrationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("openId", openid);
        map.put("loginType", type);
        map.put("authCode", code);
        map.put("registrationId",registrationId);
        map.put("phoneType","1");
        map.put("nickName",nickName);
        okHttp(this, BaseRequesUrl.bangdingphone, HttpTagUtil.BNAGDING, map);
    }

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        dismissLoading();
        if (!isHttp) {
            prompt(mag);
            return;
        }
        switch (mTag) {
            case HttpTagUtil.GETCODE:
                break;
            case HttpTagUtil.BNAGDING:
                LoginBean loginBean = new Gson().fromJson(data, LoginBean.class);
                if (loginBean != null) {

                    BaseRequesUrl.uesrId=loginBean.getUserId();
                    BaseRequesUrl.uesrHead=loginBean.getHeaderImg();
                    BaseRequesUrl.uesrRoomNo=loginBean.getRoomNo();
                    BaseRequesUrl.uesrName=loginBean.getNickname();
                    BaseRequesUrl.account=loginBean.getPhone();
                    preference.setUserId(BaseRequesUrl.uesrId);
                    preference.setLoginPhone(loginBean.getPhone());
                    preference.setUserNickname(loginBean.getNickname());
                    preference.setLevel(loginBean.getLevel());
                    preference.setHeardImage(BaseRequesUrl.uesrHead);
                    preference.setUserRoomNo(BaseRequesUrl.uesrRoomNo);
                    preference.setRyToken(loginBean.getRyToken());//融云
                    preference.setToken(loginBean.getToken());//网易云
                    preference.setArea(loginBean.getArea());
                    preference.setAddress(loginBean.getAddress());
                    preference.setAccid(loginBean.getAccid());
                    preference.save();
                    startActivity(new Intent(this, MainActivityTwo.class));
                    BaseHandlerOperate.getBaseHandlerOperate().putMessageKey(LoginActivity.class, HttpTagUtil.Loginout, "0");
                    finish();
                }
                break;
        }
    }
}
