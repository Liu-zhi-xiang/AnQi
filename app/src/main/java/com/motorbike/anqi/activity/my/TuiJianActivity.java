package com.motorbike.anqi.activity.my;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.motorbike.anqi.R;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.util.UserPreference;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.HashMap;
import java.util.Map;

/**
 * 推荐有奖
 */
public class TuiJianActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private TextView tvTitle;
    private ImageView  ivInvitation;
    private Dialog selectPhotoDialog;
    ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
    private Map<String,Object> map;
    private UserPreference preference;
    private UMWeb web;
    private UMImage imageurl;
    private String yaoQingNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tui_jian);
        preference=UserPreference.getUserPreference(this);

        initView();
    }

    private void initView() {
        llBack=findViewById(R.id.llBack);
        llBack.setOnClickListener(this);
        tvTitle=findViewById(R.id.tvTitle);
        tvTitle.setText("邀请有奖");
        ivInvitation=findViewById(R.id.ivInvitation);
        ivInvitation.setOnClickListener(this);
        http(preference.getUserId());
    }
    private void http(String userId){
        showLoading();
        map=new HashMap<>();
        if (map!=null){
            map.clear();
        }
        map.put("userId",userId);
        okHttp(this, BaseRequesUrl.GenerateInviteCode, HttpTagUtil.GenerateInviteCode,map);
    }

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag){
            case HttpTagUtil.GenerateInviteCode:
                dismissLoading();
                if (data!=null){
                    yaoQingNum=data;
//                    showTip(data);
                }
                break;
        }
    }

    private void showPopwindow(final String shareUrl, final String title, final String content){
        //设置contentView
        View contentView = LayoutInflater.from(this).inflate(R.layout.share_popwindow, null);
        LinearLayout shareWx = contentView.findViewById(R.id.shareWx);
        LinearLayout shareWxCircle = contentView.findViewById(R.id.shareWxCircle);
        LinearLayout shareQQ = contentView.findViewById(R.id.shareQQ);
        LinearLayout shareQQZone=contentView.findViewById(R.id.shareQQZone);
        TextView tvDel =  contentView.findViewById(R.id.tvDel);
        imageurl=new UMImage(this,R.mipmap.icon);//自定义分享面板，待确认是否定制面板成功
        imageurl.compressStyle = UMImage.CompressStyle.SCALE;
        web=new UMWeb(shareUrl);
        web.setThumb(imageurl);
        web.setTitle(title);
        web.setDescription(content);

        shareWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new ShareAction(TuiJianActivity.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                        .withMedia(web)//分享内容
                        .setCallback(shareListener)//回调监听器
                        .share();
            }
        });
        shareWxCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(TuiJianActivity.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
                        .withMedia(web)//分享内容
                        .setCallback(shareListener)//回调监听器
                        .share();

            }
        });
        shareQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(TuiJianActivity.this)
                        .setPlatform(SHARE_MEDIA.QQ)//传入平台
                        .withMedia(web)//分享内容
                        .setCallback(shareListener)//回调监听器
                        .share();

            }
        });
        shareQQZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(TuiJianActivity.this)
                        .setPlatform(SHARE_MEDIA.QZONE)//传入平台
                        .withMedia(web)//分享内容
                        .setCallback(shareListener)//回调监听器
                        .share();
            }
        });
        tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhotoDialog.dismiss();
            }
        });
        selectPhotoDialog = new Dialog(this);
        selectPhotoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectPhotoDialog.setContentView(contentView);
        Window regionWindow = selectPhotoDialog.getWindow();
        regionWindow.setGravity(Gravity.BOTTOM);
        regionWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        regionWindow.setWindowAnimations(R.style.view_animation);
        regionWindow.setBackgroundDrawable(dw);
        selectPhotoDialog.setCanceledOnTouchOutside(true);
        if (!selectPhotoDialog.isShowing()){
            selectPhotoDialog.show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llBack:
                finish();
                break;
            case R.id.ivInvitation:
//                if(Build.VERSION.SDK_INT>=23){
//                    String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
//                    ActivityCompat.requestPermissions(this,mPermissionList,123);
//                }
                showPopwindow("http://app.mi.com/","注册邀请码",yaoQingNum);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 123:

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }
    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
            prompt("开始");
        }
        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            prompt("成功");
            selectPhotoDialog.dismiss();
        }
        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            prompt("失败");
                }
        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            prompt("取消");
           }
    };
}
