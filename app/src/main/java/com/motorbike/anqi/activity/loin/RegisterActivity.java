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

import com.google.gson.Gson;
import com.motorbike.anqi.R;
import com.motorbike.anqi.bean.RegisterBean;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.util.FileUtil;
import com.motorbike.anqi.util.TimeCount;
import com.motorbike.anqi.util.UserPreference;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private TextView titleTv,codeTv;
    private EditText phoneEt,passtwoEt,codeEt,erji_et,erji_type_et,passone_et,etInvite;
    private String phone,verifyNum,erji,eijiType,passOne,passTwo,getCode,inviyeNum;
    private Map<String, Object> codeMap,registerMap;
    private UserPreference preference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        preference=UserPreference.getUserPreference(this);
        initView();
    }

    private void initView()
    {
        findViewById(R.id.back_layout).setOnClickListener(this);
        findViewById(R.id.zhuce_ll).setOnClickListener(this);
        titleTv=findViewById(R.id.title_text);
        codeTv= findViewById(R.id.getcode_text);
        titleTv.setText("注册");
        codeTv.setOnClickListener(this);
        phoneEt=findViewById(R.id.phone_et);
        codeEt=findViewById(R.id.code_et);
        erji_et=findViewById(R.id.erji_et);
        erji_type_et=findViewById(R.id.erji_type_et);
        etInvite=findViewById(R.id.etInvite);
        passone_et=findViewById(R.id.passone_et);
        passtwoEt=findViewById(R.id.passtwo_et);
        phoneEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phone=s.toString();
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
                verifyNum=s.toString();
            }
        });
        erji_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                erji=s.toString();
            }
        });
        erji_type_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                eijiType=s.toString();
            }
        });
        passone_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                passOne=s.toString();
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
                passTwo=s.toString();
            }
        });
        etInvite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                inviyeNum=s.toString();
            }
        });
        codeMap=new HashMap<>();
        registerMap=new HashMap<>();
    }
    /**
     * but计时
     */
    private TimeCount count;
    private void countDown()
    {
        count=new TimeCount(60000,1000,codeTv,this);
        count.start();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                finish();
                break;
            case R.id.zhuce_ll:
                if (!TextUtils.isEmpty(phone)){
                    if (!TextUtils.isEmpty(passOne)){
                        if (passOne.equals(passTwo)){
                            if (TextUtils.isEmpty(inviyeNum)){
                                inviyeNum="";
                            }
                            register(phone,passTwo,verifyNum,erji,eijiType,inviyeNum,BaseRequesUrl.jpushId);
                        }else {
                            showTip("输入的密码不一致");
                        }
                    }else {
                        showTip("密码不能为空");
                    }
                }else {
                    showTip("手机号不能为空");
                }
                break;
            case R.id.getcode_text:
                if (!TextUtils.isEmpty(phone)){
                    if (FileUtil.isMobileExact(phone)){
                        countDown();
                        httpCode(phone);
                    }else {
                       showTip("请检查手机号");
                    }
                }else {
                    showTip("手机号不能为空");
                }
                break;
        }
    }

    /**
     * 获取验证码接口
     * @param phone
     */
    private void httpCode(String phone){
        if (!TextUtils.isEmpty(phone)){
            if (codeMap != null) {
                codeMap.clear();
            }
            codeMap.put("phone",phone);
            codeMap.put("type","0");
            okHttp(this, BaseRequesUrl.GETCODE, HttpTagUtil.GETCODE,codeMap);
        }else {
            showTip("请输入手机号");
        }
    }


    private void register(String phone,String password,String authCode,String erji,String erjiType,String invitedCode,String registrationId){
        if (!TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(password)){
            if (registerMap!=null){
                registerMap.clear();
            }
            registerMap.put("phone",phone);
            registerMap.put("authCode",authCode);
            registerMap.put("password",password);
            registerMap.put("earphone",erji);
            registerMap.put("earphoneType",erjiType);
            registerMap.put("invitedCode",invitedCode);
            registerMap.put("registrationId",registrationId);
            registerMap.put("phoneType","1");
            okHttp(this,BaseRequesUrl.register,HttpTagUtil.REGISTER,registerMap);
        }else {
            showTip("信息不能为空");
        }
    }
    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag){
            case HttpTagUtil.GETCODE://获取验证码
                if (data!=null){
                Log.e("aaa",data);
                   getCode=data;
//                   showTip(getCode);
                }
                break;
            case HttpTagUtil.REGISTER://注册
                if (data!=null){
                    RegisterBean registerBean=new Gson().fromJson(data,RegisterBean.class);
                    if (registerBean!=null){
                       preference.setToken(registerBean.getToken());
                       preference.setAccid(registerBean.getAccid());
                       preference.setRyToken(registerBean.getRyToken());
                       preference.save();
                    }
                    finish();
                }
                break;
        }
    }


}
