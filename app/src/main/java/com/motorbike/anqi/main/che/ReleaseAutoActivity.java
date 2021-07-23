package com.motorbike.anqi.main.che;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.BaseBundle;
import android.provider.MediaStore;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.motorbike.anqi.R;
import com.motorbike.anqi.activity.my.EditUserinfoActivity;
import com.motorbike.anqi.bean.MediaBean;
import com.motorbike.anqi.handler.BaseHandler;
import com.motorbike.anqi.handler.BaseHandlerMethod;
import com.motorbike.anqi.handler.BaseHandlerOperate;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.init.LuntanFabuReques;
import com.motorbike.anqi.interfaces.HttpRequestTag;
import com.motorbike.anqi.main.AutoMomentsActivity;
import com.motorbike.anqi.util.LGImgCompressor;
import com.motorbike.anqi.util.PermissionsConstans;
import com.motorbike.anqi.view.MyGridView;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.Item;
import com.zhihu.matisse.internal.entity.UncapableCause;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 发布车友圈
 */
public class ReleaseAutoActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, LGImgCompressor.CompressListener, HttpRequestTag {
    private MyGridView myGridView;
    private List<MediaBean> mPhotoList;
    private GridTwoAdapter adapter;
    private EditText releasEdit,titleEdit;

    private String titleStr,roomStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_auto);
        initView();
    }

    private void initView()
    {
        findViewById(R.id.back_layout).setOnClickListener(this);
        myGridView= (MyGridView) findViewById(R.id.noScrollgridview);
        myGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        myGridView.setOnItemClickListener(this);
        mPhotoList = new ArrayList<>();
        adapter = new GridTwoAdapter(this,9,mPhotoList);
        myGridView.setAdapter(adapter);
        releasEdit=findViewById(R.id.tvMeetContent);
        titleEdit=findViewById(R.id.title_edit);

        titleEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                titleStr=s.toString();
            }
        });
        releasEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                roomStr=s.toString();
            }
        });
        findViewById(R.id.ok_but).setOnClickListener(this);
    }
    private boolean fabu=false;
    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.back_layout:
                finish();
                break;
            case R.id.ok_but:
                if (!fabu) {
                    if (TextUtils.isEmpty(titleStr)||TextUtils.isEmpty(roomStr)){
                        prompt("说点什么吧！");
                        return;
                    }
                    if (fileList != null) {
                        fileList.clear();
                    }
                    showLoading();
                    if (mPhotoList.size()>0) {
                        for (int x = 0; x < mPhotoList.size(); x++) {
                            LGImgCompressor.getInstance(this).withListener(this).
                                    starCompress(mPhotoList.get(x).getUri().toString(), 600, 800, 500);
                        }
                    }else {
                        new LuntanFabuReques(this, HttpTagUtil.CHTYOU_FABU, this).addPoicomment(BaseRequesUrl.uesrId,roomStr,"上海市",titleStr, null);
                    }
                    fabu = true;
                }else {
                    prompt("正在发布");
                    showLoading();
                }
                break;
        }
    }
    /**
     * 打开相册
     */
    private void goToAlbum()
    {
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG,MimeType.PNG))//选择mime的类型
                .countable(true)//设置从1开始的数字
                .maxSelectable(9)//选择图片的最大数量限制
                .capture(false)//启用相机
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)//屏幕显示方向
                .thumbnailScale(0.85f) // 缩略图的比例
                .imageEngine(new PicassoEngine()) // 使用的图片加载引擎
                .theme(R.style.Matisse_Dracula) // 黑色背景
                .forResult(HttpTagUtil.REQUEST_CODE_CHOOSE); // 设置作为标记的请求码
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position==adapter.getCount()-1){
            if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                doSDCardPermission();
            }else {
                requestPermission(PermissionsConstans.WRITE_STORAGE_CODE,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    protected void doSDCardPermission()
    {
        super.doSDCardPermission();
        goToAlbum();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HttpTagUtil.REQUEST_CODE_CHOOSE && resultCode == RESULT_OK){
            if (mPhotoList!=null){
                mPhotoList.clear();
            }
            List<Uri> mSelected  = Matisse.obtainResult(data);
            Log.d("Matisse", "mSelected: " + mSelected);
            Uri uri = null;
            for (int x=0;x<mSelected.size();x++){
                mPhotoList.add(new MediaBean(x+"",mSelected.get(x)));
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCompressStart() {

    }
    private List<File> fileList;
    @Override
    public void onCompressEnd(LGImgCompressor.CompressResult compressResult) {
        Log.d("release", "onCompressEnd outPath:" + compressResult.getOutPath());
        if (compressResult.getStatus() == LGImgCompressor.CompressResult.RESULT_ERROR) {//压缩失败
            fabu = false;
            return;
        }
        File file = new File(compressResult.getOutPath());
        if (fileList==null){
            fileList=new ArrayList<>();
        }
        fileList.add(file);
        if (fileList.size()==mPhotoList.size()){
            new LuntanFabuReques(this, HttpTagUtil.CHTYOU_FABU, this).addPoicomment(BaseRequesUrl.uesrId,roomStr,"上海市",titleStr, fileList);
        }
//        Bitmap bitmap = null;
//        try {
//            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
//
//            float imageFileSize = file.length() / 1024f;
//           Log.e("img","image info width:" + bitmap.getWidth() + " \nheight:" + bitmap.getHeight() +
//                    " \nsize:" + imageFileSize + "kb" + "\nimagePath:" + file.getAbsolutePath());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void requestComplete(Integer tag, Object result, String msg, boolean complete) {
        dismissLoading();
        if (!complete){
            prompt(msg);
            if (fabu){
                fabu=false;
            }
            return;
        }
        switch (tag){
            case HttpTagUtil.CHTYOU_FABU:
                prompt("发布成功");
                BaseHandlerOperate.getBaseHandlerOperate().putMessageKey(AutoMomentsActivity.class,HttpTagUtil.CHTYOU_FABU,"0");
                finish();
                break;
        }

    }
}
