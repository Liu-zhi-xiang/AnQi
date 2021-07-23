package com.motorbike.anqi.activity.moments;


import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.motorbike.anqi.R;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.MyApplication;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import io.rong.photoview.PhotoView;
import io.rong.photoview.PhotoViewAttacher;

public class PhotoActivity extends BaseActivity {
    private PhotoView photoView;
    private String imgurl;
    PhotoViewAttacher mAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        imgurl=getIntent().getStringExtra("url");
        photoView=findViewById(R.id.photo_img);
        photoView.setAllowParentInterceptOnEdge(true);
        Picasso.with(this).load(imgurl)
                .error(R.mipmap.dt_bg)
                .into(photoView);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            photoView=null;
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
