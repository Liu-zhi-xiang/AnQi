package com.motorbike.anqi.activity.my;

import android.graphics.Bitmap;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.motorbike.anqi.R;
import com.motorbike.anqi.activity.voice.VoiceHomeActivity;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * 房间二维码
 */
public class QRCodeActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private TextView tvTitle;
    private ImageView ivCode;
    private String roomId;
    private Map<String,Object> codeMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        roomId=getIntent().getStringExtra("roomId");
        initView();
    }

    private void initView() {
        llBack=findViewById(R.id.llBack);
        llBack.setOnClickListener(this);
        tvTitle=findViewById(R.id.tvTitle);
        tvTitle.setText("房间二维码");
        ivCode=findViewById(R.id.ivCode);
        codeMap=new HashMap<>();
        if (!TextUtils.isEmpty(roomId)){
            codeHttp(roomId);
        }
    }

    private void codeHttp(String roomId){
        showLoading();
        if (codeMap!=null){
            codeMap.clear();
        }
        codeMap.put("roomNo",roomId);
        okHttp(this, BaseRequesUrl.RoomQRCode, HttpTagUtil.RoomQRCode,codeMap);
    }

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag){
            case HttpTagUtil.RoomQRCode:
                dismissLoading();
                if (data!=null){
                    Picasso.with(QRCodeActivity.this)
                            .load(data)
                            .config(Bitmap.Config.RGB_565)
                            .centerCrop()
                            .fit()
                            .networkPolicy(NetworkPolicy.NO_STORE)
                            .into(ivCode);
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
