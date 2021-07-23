package com.motorbike.anqi.activity.loin;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.motorbike.anqi.R;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.util.FileUtil;
import com.motorbike.anqi.util.TimeCount;


import java.util.HashMap;
import java.util.Map;

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {
    private TextView titleTv, codeTv;
    private EditText phonnEt, codeEt, passEt, passtwoEt;
    private String phone, verifyNum, passOne, passTwo,getCode;
    private Map<String, Object> codeMap, forgetMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initView();
    }

    private void initView() {
        findViewById(R.id.back_layout).setOnClickListener(this);
        findViewById(R.id.queren_ll).setOnClickListener(this);
        titleTv = findViewById(R.id.title_text);
        codeTv = findViewById(R.id.code_tv);
        codeTv.setOnClickListener(this);
        phonnEt = findViewById(R.id.phone_et);
        codeEt = findViewById(R.id.code_et);
        passEt = findViewById(R.id.passone_et);
        passtwoEt = findViewById(R.id.passtwo_et);
        titleTv.setText("找回密码");
        codeTv.setOnClickListener(this);
        phonnEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phone = s.toString();
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
                verifyNum = s.toString();
            }
        });
        passEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                passOne = s.toString();
            }
        });
        passtwoEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                passTwo = s.toString();
            }
        });
        codeMap = new HashMap<>();
        forgetMap = new HashMap<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.code_tv:
                if (!TextUtils.isEmpty(phone)) {
                    if (FileUtil.isMobileExact(phone)) {
                        countDown();
                        httpCode(phone);
                    } else {
                        showTip("请检查手机号");
                    }
                } else {
                    showTip("手机号不能为空");
                }
                break;
            case R.id.queren_ll:
                if (!TextUtils.isEmpty(phone)) {
                    if (!TextUtils.isEmpty(verifyNum)){
                        if (!TextUtils.isEmpty(passOne)) {
                            if (passOne.equals(passTwo)) {
                                forgetPassword(phone,verifyNum,passTwo);
                            } else {
                                showTip("输入的密码不一致");
                            }
                        } else {
                            showTip("密码不能为空");
                        }
                    }else {
                        showTip("验证码不能为空");
                    }
                } else {
                    showTip("手机号不能为空");
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

    /**
     * 获取验证码接口
     *
     * @param phone
     */
    private void httpCode(String phone) {
        if (!TextUtils.isEmpty(phone)) {
            if (codeMap != null) {
                codeMap.clear();
            }
            codeMap.put("phone", phone);
            codeMap.put("type","1");
            okHttp(this, BaseRequesUrl.GETCODE, HttpTagUtil.GETCODE, codeMap);
        } else {
            showTip("请输入手机号");
        }
    }

    private void forgetPassword(String phone,String authCode, String password) {
        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)) {
            if (forgetMap != null) {
                forgetMap.clear();
            }
            forgetMap.put("phone", phone);
            forgetMap.put("authCode",authCode);
            forgetMap.put("password", password);
            okHttp(this, BaseRequesUrl.FORGETPASSWORD, HttpTagUtil.FORGETPASSWORD, forgetMap);
        } else {
            showTip("信息不能为空");
        }
    }

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag) {
            case HttpTagUtil.GETCODE:
                if (data != null) {
                    Log.e("aaa", data);
                    getCode = data;
                    showTip(getCode);
                }
                break;
            case HttpTagUtil.FORGETPASSWORD:
                if (data!=null){
                    Log.e("aaa", data);
                    showTip(mag);
                    finish();
                }
                break;
        }
    }


}
