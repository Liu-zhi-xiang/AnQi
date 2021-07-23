package com.motorbike.anqi.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.os.Parcelable;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.motorbike.anqi.MainActivityTwo;
import com.motorbike.anqi.R;
import com.motorbike.anqi.activity.moments.MomentsAutoActivity;
import com.motorbike.anqi.activity.voice.RacingvoiceHomeActivity;
import com.motorbike.anqi.bean.CheyouquanBean;
import com.motorbike.anqi.bean.Voiceroom;
import com.motorbike.anqi.beseAdapter.CommonAdapter;
import com.motorbike.anqi.beseAdapter.MultiItemTypeAdapter;
import com.motorbike.anqi.beseAdapter.base.ViewHolder;
import com.motorbike.anqi.handler.BaseHandlerOperate;
import com.motorbike.anqi.handler.BaseHandlerUpDate;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.main.che.CityPickerActivity;
import com.motorbike.anqi.main.che.ReleaseAutoActivity;
import com.motorbike.anqi.util.PermissionsConstans;
import com.motorbike.anqi.xrecyclerview.XRecyclerView;
import com.motorbike.anqi.xrecyclerview.progressindicator.ProgressStyle;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 车友圈
 */
public class AutoMomentsActivity extends BaseActivity implements View.OnClickListener, MultiItemTypeAdapter.OnItemClickListener, XRecyclerView.LoadingListener, BaseHandlerUpDate {
    private XRecyclerView recyclerView;
    private MyAdapter myadapter;
    private LinearLayout rightlayout;
    private static final int REQUEST_CODE_PICK_CITY = 233;
    private List<CheyouquanBean> cheyouquanLsit;
    private String cityStr="";
    private TextView cityTV;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_moments);
        BaseHandlerOperate.getBaseHandlerOperate().addKeyHandler(AutoMomentsActivity.class,this);
        initView();
    }

    private void initView()
    {
        cheyouquanLsit=new ArrayList<>();
        findViewById(R.id.back_layout).setOnClickListener(this);
        rightlayout= findViewById(R.id.add_layout);
        rightlayout.setOnClickListener(this);
        rightlayout.setVisibility(View.VISIBLE);
        cityTV=findViewById(R.id.city_tv);
        findViewById(R.id.gengduo_layout).setOnClickListener(this);
        recyclerView= findViewById(R.id.auto_xrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        myadapter=new MyAdapter(this,R.layout.automobile_friends_item,cheyouquanLsit);
        recyclerView.setAdapter(myadapter);
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.setLoadingMoreEnabled(true);
        myadapter.notifyDataSetChanged();
        myadapter.setOnItemClickListener(this);
        recyclerView.setLoadingListener(this);
        showLoading();
        if (!BaseRequesUrl.uesrCity.equals(""))
        {
            cityStr=BaseRequesUrl.uesrCity;
            cityTV.setText(cityStr);
            requestDta("0",cityStr);
        }else {
            requestDta("0",cityStr);
        }
        getData();
    }
    private void getData(){
        Map<String,Object> dataMap=new HashMap<>();
        dataMap.put("userId",BaseRequesUrl.uesrId);
        dataMap.put("pageCount","10");
        dataMap.put("pageNum","0");
        okHttp(this,BaseRequesUrl.cheyouquan,HttpTagUtil.cheyouquan,dataMap);

    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.back_layout:
                finish();
                break;
            case R.id.add_layout:
                type="2";
                if (!hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)){
                    requestPermission(PermissionsConstans.LOCATION_CODE, Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION);
                }else {
                    doDINGWEIPermission();
                }
                break;
            case R.id.gengduo_layout:
                type="1";
                if (!hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)){
                    requestPermission(PermissionsConstans.LOCATION_CODE, Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION);
                }else {
                    doDINGWEIPermission();
                }
                break;
        }
    }

    private String type="1";
    @Override
    protected void doDINGWEIPermission()
    {
        super.doDINGWEIPermission();

        if (type.equals("1")) {
            Intent iu = new Intent(this, CityPickerActivity.class);
            startActivityForResult(iu, HttpTagUtil.CYQ_CITY);
        }else {
            startActivity(new Intent(this, ReleaseAutoActivity.class));
        }
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position){
        Intent intent=new Intent(this, MomentsAutoActivity.class);
        Bundle bundle=new Bundle();
        bundle.putParcelable("data",cheyouquanLsit.get(position-1));
        bundle.putString("type","1");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        dismissLoading();
        if (!isHttp){
            prompt(mag);
            return;
        }
        switch (mTag){
            case HttpTagUtil.CYQ_CITY:
            case HttpTagUtil.CHTYOU_QUAN:
                if (shuxxin){
                    cheyouquanLsit.clear();
                    recyclerView.refreshComplete();
                    shuxxin=false;
                }else {
                    recyclerView.loadMoreComplete();
                }
                List<CheyouquanBean>  aaa=new Gson().fromJson(data ,new TypeToken<List<CheyouquanBean>>(){}.getType());
                cheyouquanLsit.addAll(aaa);
                myadapter.notifyDataSetChanged();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK&&requestCode==HttpTagUtil.CYQ_CITY){
            String city=data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
            cityTV.setText(city);
            cityStr=city;
            BaseRequesUrl.uesrCity=cityStr;
            shuxxin=true;
            requestDta("0",cityStr);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (myadapter!=null)
        {
            myadapter.notifyDataSetChanged();
        }
    }

    //刷新
    private boolean shuxxin=false;
    @Override
    public void onRefresh()
    {
        if (!shuxxin)
        {
            requestDta("0",cityStr);
            shuxxin=true;
        }
    }
    //加载更多
    @Override
    public void onLoadMore() {
        int x=(cheyouquanLsit.size()+9)/10;
        requestDta(x+"",cityStr);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case HttpTagUtil.CHTYOU_FABU:
                if (!shuxxin){
                    requestDta("0",cityStr);
                    shuxxin=true;
                }
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseHandlerOperate.getBaseHandlerOperate().removeKeyData(AutoMomentsActivity.class);
    }

    public class MyAdapter extends CommonAdapter<CheyouquanBean>
    {
        public MyAdapter(Context context, int layoutId, List<CheyouquanBean> datas) {
            super(context, layoutId, datas);
        }
        @Override
        protected void convert(ViewHolder holder, final CheyouquanBean voiceroom, int position) {
            holder.setIsRecyclable(false);
            holder.setText(R.id.wen_text,voiceroom.getTitle());
            holder.setText(R.id.name_tv,voiceroom.getNickname());
            ImageView headimg=holder.getView(R.id.head_iv);
            ImageView tu=holder.getView(R.id.tu_img);
            if (voiceroom.getNoteImgList().size()>0){
                tu.setBackgroundResource(R.mipmap.tupian);
            }else {
                tu.setBackgroundResource(R.mipmap.wutupian);
            }
            if (!TextUtils.isEmpty(voiceroom.getHeaderImg())) {
                Picasso.with(getBaseContext())
                        .load(voiceroom.getHeaderImg())
                        .centerCrop()
                        .error(R.mipmap.sy_tx1)
                        .fit()
                        .into(headimg);
            }
        }
    }


    private void requestDta(String pageNum,String area)
    {
        Map<String,Object> map=new ArrayMap<>();
        map.put("userId", BaseRequesUrl.uesrId);
        map.put("area",area);
        map.put("pageCount","10");
        map.put("pageNum",pageNum);
        okHttp(this,BaseRequesUrl.CYQ_CITY_DTA, HttpTagUtil.CHTYOU_QUAN,map);
    }
}
