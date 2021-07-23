package com.motorbike.anqi.activity.loin;

import android.content.Intent;
import android.os.Message;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.motorbike.anqi.MainActivityTwo;
import com.motorbike.anqi.R;
import com.motorbike.anqi.activity.voice.RacingvoiceHomeActivity;
import com.motorbike.anqi.bean.LoginBean;
import com.motorbike.anqi.handler.BaseHandlerOperate;
import com.motorbike.anqi.handler.BaseHandlerUpDate;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.main.AutoMomentsActivity;
import com.motorbike.anqi.nim.voiceroom.NimVoicerImpl;
import com.motorbike.anqi.util.JcLog;
import com.motorbike.anqi.util.UserPreference;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener, BaseHandlerUpDate {
    private EditText mphone_et,mpass_et;
    private String phoneStr,passStr,nickName;
    private ImageView weixin_iv,qq_iv;
    private Map<String,Object> loginMap,getTockenMap;
    private UserPreference preference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        BaseHandlerOperate.getBaseHandlerOperate().addKeyHandler(LoginActivity.class,this);
        preference=UserPreference.getUserPreference(this);
        initView();
        mShareAPI=UMShareAPI.get(this);
        preference.setFirstLogin(true);
        preference.save();
    }

    private void initView()
    {
        findViewById(R.id.loding_ll).setOnClickListener(this);
        findViewById(R.id.zhuce_tv).setOnClickListener(this);
        findViewById(R.id.wangji_pass_tv).setOnClickListener(this);
        weixin_iv=findViewById(R.id.weixin_iv);
        weixin_iv.setOnClickListener(this);
        qq_iv=findViewById(R.id.qq_iv);
        qq_iv.setOnClickListener(this);
        mphone_et=  findViewById(R.id.phone_et);
        mpass_et=  findViewById(R.id.pass_et);

        mpass_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                passStr=s.toString();
            }
        });
        mphone_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                phoneStr=s.toString();
            }
        });
        loginMap=new HashMap<>();
        BaseRequesUrl.jpushId=preference.getJpushId();
    }

    private void httpLogin(String phone,String password,String openId,String loginType,String registrationId){
        showLoading();
        if (loginMap!=null){
            loginMap.clear();
        }
        loginMap.put("phone",phone);
        loginMap.put("password",password);
        loginMap.put("openId",openId);
        loginMap.put("loginType",loginType);//0手机  1 微信 2 QQ 3 微博
        loginMap.put("registrationId",registrationId);
        loginMap.put("phoneType","1");
        okHttp(this, BaseRequesUrl.login, HttpTagUtil.LOGIN,loginMap);
    }


    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp)
    {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag){
            case HttpTagUtil.LOGIN:
                dismissLoading();
                if (data!=null){
                    if (mag.equals("请先绑定手机号")){
                        try {
                            JSONObject   jsonObject = new JSONObject(data.toString());
                            String   status = jsonObject.getString("type");
                            if (status.equals("1")){
                                Intent intent=new Intent(LoginActivity.this,BindPhoneActivity.class);
                                intent.putExtra("type",loginType);
                                intent.putExtra("opendid",uid);
                                if (!TextUtils.isEmpty(nickName)){
                                    intent.putExtra("nickName",nickName);
                                }
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
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
                            finish();
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loding_ll:
//                startActivity(new Intent(this, MainActivityTwo.class));
//                finish();
                if (!TextUtils.isEmpty(phoneStr)){
                    if (!TextUtils.isEmpty(passStr)){
                      httpLogin(phoneStr,passStr,"","0",BaseRequesUrl.jpushId);
                      Log.e("bbb",BaseRequesUrl.jpushId+" jpushId");
                    }else {
                        showTip("密码不能为空");
                    }
                }else {
                    showTip("手机号不能为空");
                }
                break;
            case R.id.wangji_pass_tv:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
            case R.id.zhuce_tv:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.weixin_iv:
                loginType="1";
//                uid="BB7A60BF92B541024D1DC1C6452AF8CF";
//                httpLogin("","",uid,loginType);
                mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, authListener);
                break;
            case R.id.qq_iv:
                loginType="2";
//                uid="ozDOV0z_cl81ir59qtPwMX51Pv5U";
//                httpLogin("","",uid,loginType);
                mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, authListener);
                break;
        }
    }
    private UMShareAPI mShareAPI;
    private String uid,loginType="0";
    UMAuthListener authListener = new UMAuthListener(){
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }
        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//            prompt("成功了");
            uid=data.get("uid");
            nickName=data.get("name");
            showLoading();
            httpLogin("","",uid,loginType,BaseRequesUrl.jpushId);
            JcLog.e("sandeng","===="+data.toString()+"action=="+action);
        }
        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            prompt("失败: "+t.getMessage());
            JcLog.e("sandeng","===="+t.getMessage()+"action=="+action);
        }
        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            prompt("取消了");
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseHandlerOperate.getBaseHandlerOperate().removeKeyData(LoginActivity.class);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case HttpTagUtil.Loginout:
                finish();
                break;
        }
    }

}
