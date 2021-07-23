package com.motorbike.anqi.activity.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.motorbike.anqi.R;
import com.motorbike.anqi.adapter.FirendsListAdapter;
import com.motorbike.anqi.bean.UserCenterBean;
import com.motorbike.anqi.bean.UsreBean;
import com.motorbike.anqi.greendao.gen.DBManager;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.util.UserPreference;
import com.motorbike.anqi.view.CircleImageView;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * 个人中心
 */
public class UserCenterActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llXingCheng,llGoodFriend,llJiFenShangCheng,llDongTai,llTuiJian,llMessage,llMyCollect,llRenZheng,llSet,llBack;
    private CircleImageView head,renZheng;
    private TextView tvName,tvAddress,tvType,tvKm,tvJiFen,tvClassDetail,tvId;
    private Map<String,Object> userMap;
    private UserPreference preference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        preference=UserPreference.getUserPreference(this);
        initView();
        userMap=new HashMap<>();
        http(preference.getUserId());
//        showTip(preference.getUserId());
    }

    private void initView()
    {
        llBack=findViewById(R.id.llBack);
        llBack.setOnClickListener(this);
        llXingCheng= findViewById(R.id.llXingCheng);
        llXingCheng.setOnClickListener(this);
        llGoodFriend= findViewById(R.id.llGoodFriend);
        llGoodFriend.setOnClickListener(this);
        llJiFenShangCheng= findViewById(R.id.llJiFenShangCheng);
        llJiFenShangCheng.setOnClickListener(this);
        llDongTai=findViewById(R.id.llDongTai);
        llDongTai.setOnClickListener(this);
        llTuiJian=findViewById(R.id.llTuiJian);
        llTuiJian.setOnClickListener(this);
        llMessage= findViewById(R.id.llMessage);
        llMessage.setOnClickListener(this);
        llMyCollect= findViewById(R.id.llMyCollect);
        llMyCollect.setOnClickListener(this);
        llRenZheng=  findViewById(R.id.llRenZheng);
        llRenZheng.setOnClickListener(this);
        llSet=  findViewById(R.id.llSet);
        llSet.setOnClickListener(this);
        head=  findViewById(R.id.head);
        head.setOnClickListener(this);
        renZheng=findViewById(R.id.renZheng);
        tvName=  findViewById(R.id.tvName);
        tvAddress= findViewById(R.id.tvAddress);
        tvType=  findViewById(R.id.tvType);
        tvKm=  findViewById(R.id.tvKm);
        tvJiFen= findViewById(R.id.tvJiFen);
        tvClassDetail= findViewById(R.id.tvClassDetail);
        tvClassDetail.setOnClickListener(this);
        tvId=findViewById(R.id.tvId);

    }
    private void http(String userId){
        showLoading();
       if (!TextUtils.isEmpty(userId)){
           if (userMap!=null){
               userMap.clear();
           }
           userMap.put("userId",userId);
           okHttp(this, BaseRequesUrl.PersonalCenter, HttpTagUtil.PersonalCenter,userMap);
       }
    }

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag){
            case HttpTagUtil.PersonalCenter:
                dismissLoading();
                if (data!=null){
                    Log.e("aaa",data.toString());
                    UserCenterBean userCenterBean=new Gson().fromJson(data,UserCenterBean.class);
                    if (userCenterBean!=null){
                        tvName.setText(userCenterBean.getNickname());
                        tvAddress.setText(userCenterBean.getArea());
                        tvType.setText(userCenterBean.getCarType());
                        tvKm.setText(userCenterBean.getTotalMileage());
                        tvJiFen.setText(userCenterBean.getPointNum());
                        tvClassDetail.setText(userCenterBean.getLevel());
                        tvId.setText("ID:"+userCenterBean.getUserId());

                        //清理缓存
//                        Picasso.with(U.this).invalidate(new File(uri.toString()));
                        Picasso.with(UserCenterActivity.this)
                                .load(userCenterBean.getHeaderImg())
                                .config(Bitmap.Config.RGB_565)
                                .centerCrop()
                                .fit()
                                .networkPolicy(NetworkPolicy.NO_STORE)
                                .into(head);
                        String isConfirm=userCenterBean.getIsConfirm();
                        if (!TextUtils.isEmpty(isConfirm)){
                            if (isConfirm.equals("0")){
                                renZheng.setVisibility(View.GONE);
                            }else {
                                renZheng.setVisibility(View.VISIBLE);
                            }
                        }
                        BaseRequesUrl.uesrId=userCenterBean.getUserId();
                        BaseRequesUrl.uesrHead=userCenterBean.getHeaderImg();
                        BaseRequesUrl.uesrName=userCenterBean.getNickname();
                        BaseRequesUrl.account=userCenterBean.getPhone();

                        preference.setLoginPhone(userCenterBean.getPhone());
                        preference.setUserNickname(userCenterBean.getNickname());
                        preference.setLevel(userCenterBean.getLevel());
                        preference.setHeardImage(BaseRequesUrl.uesrHead);
                        preference.setArea(userCenterBean.getArea());
                        preference.setCarType(userCenterBean.getCarType());
                        preference.save();
                        if (RongIM.getInstance()!=null){
                            RongIM.getInstance().refreshUserInfoCache(new UserInfo(preference.getUserId(), userCenterBean.getNickname(), Uri.parse(userCenterBean.getHeaderImg())));
                            //更新数据库
                            DBManager.getInstance(UserCenterActivity.this).updateUser(new UsreBean(userCenterBean.getNickname(),BaseRequesUrl.uesrId,data));
                        }
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
            case R.id.llXingCheng://行程记录
                startActivity(new Intent(this,TopListActivity.class));
                break;
            case R.id.llGoodFriend://好友列表
                startActivity(new Intent(this,FirendsListActivity.class));
                break;
            case R.id.llJiFenShangCheng://积分商城
                startActivity(new Intent(this,IntegralMallTwoActivity.class));
                break;
            case R.id.llDongTai://动态
                startActivity(new Intent(this,DynamicActivity.class));
                break;
            case R.id.llTuiJian://推荐有奖
                startActivity(new Intent(this, TuiJianActivity.class));
                break;
            case R.id.llMessage://消息列表
                startActivity(new Intent(this,MessageCenterActivity.class));
                break;
            case R.id.llMyCollect://我的收藏
                startActivity(new Intent(this,MyCollectActivity.class));
                break;
            case R.id.llRenZheng://认证中心
                startActivity(new Intent(this,RenZhengActivity.class));
                break;
            case R.id.head://编辑个人信息
                startActivity(new Intent(this,EditUserinfoActivity.class));
                break;
            case R.id.llSet://设置
                startActivity(new Intent(this,SetActivity.class));
                break;
            case R.id.tvClassDetail://等级详情
                startActivity(new Intent(this,ClassDetailsActivity.class));
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        http(preference.getUserId());
    }
}
