package com.motorbike.anqi.activity.my;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.motorbike.anqi.MainActivityTwo;
import com.motorbike.anqi.R;
import com.motorbike.anqi.activity.moments.MomentsAutoActivity;
import com.motorbike.anqi.activity.voice.RacingvoiceHomeActivity;
import com.motorbike.anqi.bean.CheyouquanBean;
import com.motorbike.anqi.bean.DynamicBean;
import com.motorbike.anqi.bean.NoteImgListBean;
import com.motorbike.anqi.bean.UserCarBean;
import com.motorbike.anqi.bean.Voiceroom;
import com.motorbike.anqi.beseAdapter.CommonAdapter;
import com.motorbike.anqi.beseAdapter.MultiItemTypeAdapter;
import com.motorbike.anqi.beseAdapter.base.ViewHolder;
import com.motorbike.anqi.handler.BaseHandlerOperate;
import com.motorbike.anqi.handler.BaseHandlerUpDate;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.util.UserPreference;
import com.motorbike.anqi.view.CircleImageView;
import com.motorbike.anqi.xrecyclerview.XRecyclerView;
import com.motorbike.anqi.xrecyclerview.progressindicator.ProgressStyle;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态
 */
public class DynamicActivity extends BaseActivity implements View.OnClickListener, MultiItemTypeAdapter.OnItemClickListener, BaseHandlerUpDate {
    private LinearLayout llBack;
    private TextView tvTitle;
    private XRecyclerView xRecyclerView;
    private MyAdapter myAdapter;
    private UserPreference preference;
    private Map<String,Object> dynamicMap;
    List<CheyouquanBean> dynamicBeans;
    private String shuli;
    private int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);
        BaseHandlerOperate.getBaseHandlerOperate().addKeyHandler(DynamicActivity.class,this);
        preference=UserPreference.getUserPreference(this);
        initView();
    }

    private void initView()
    {
        llBack=  findViewById(R.id.llBack);
        llBack.setOnClickListener(this);
        tvTitle=  findViewById(R.id.tvTitle);
        tvTitle.setText("动态");
        xRecyclerView=  findViewById(R.id.recyclerView);

        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setPullRefreshEnabled(false);

        http(preference.getUserId());
    }

    private void http(String userId)
    {
        showLoading();
        dynamicMap=new HashMap<>();
        if (dynamicMap!=null){
            dynamicMap.clear();
        }
        dynamicMap.put("userId",userId);
        okHttp(this, BaseRequesUrl.MyDynamic, HttpTagUtil.MyDynamic,dynamicMap);
    }

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag){
            case HttpTagUtil.MyDynamic:
                dismissLoading();
                if (data!=null){
                    dynamicBeans=new Gson().fromJson(data,new TypeToken<List<CheyouquanBean>>(){}.getType());
                    myAdapter=new MyAdapter(this,R.layout.dynamic_item_layout,dynamicBeans);
                    xRecyclerView.setAdapter(myAdapter);
                    myAdapter.setOnItemClickListener(this);
                    myAdapter.notifyDataSetChanged();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        http(preference.getUserId());
        if (myAdapter!=null){
            myAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case HttpTagUtil.CYQ_ZAN:
                String aa= (String) msg.obj;
                if (aa.equals("1")){

                }else if (aa.equals("0")){

                }
                break;
            case HttpTagUtil.CYQ_PlHF:
                shuli= (String) msg.obj;

                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseHandlerOperate.getBaseHandlerOperate().removeKeyData(DynamicActivity.class);
    }

    public class MyAdapter extends CommonAdapter<CheyouquanBean>
    {

        public MyAdapter(Context context, int layoutId, List<CheyouquanBean> datas) {
            super(context, layoutId, datas);
        }
        @Override
        protected void convert(ViewHolder holder, final CheyouquanBean dynamicBean, final int position) {
            LinearLayout llPicture=holder.getView(R.id.llPicture);
            LinearLayout llTitle=holder.getView(R.id.llTitle);
            CircleImageView headImg=holder.getView(R.id.headImg);
            TextView tvNickname=holder.getView(R.id.tvNickname);
            TextView tvAddress=holder.getView(R.id.tvAddress);
            TextView tvTime=holder.getView(R.id.tvTime);
            TextView tvTheme=holder.getView(R.id.tvTheme);
            TextView tvPinLun=holder.getView(R.id.tvPinLun);
            TextView tvZan=holder.getView(R.id.tvZan);
            ImageView ivBackground=holder.getView(R.id.ivBackground);

            if (dynamicBean!=null){
                tvNickname.setText(dynamicBean.getNickname());
                tvAddress.setText(dynamicBean.getCity());
                tvTime.setText(dynamicBean.getPublishTimeStr());
                tvTheme.setText(dynamicBean.getTitle());
                tvPinLun.setText(dynamicBean.getTalk());
                tvZan.setText(dynamicBean.getZan());
                Picasso.with(DynamicActivity.this)
                        .load(dynamicBean.getHeaderImg())
                        .config(Bitmap.Config.RGB_565)
                        .centerCrop()
                        .fit()
                        .networkPolicy(NetworkPolicy.NO_STORE)
                        .into(headImg);
                if (!TextUtils.isEmpty(shuli)){
                    if (index==(position-1)){
                        tvPinLun.setText(shuli);
                    }
                }
                List<NoteImgListBean> noteImgListBeans=dynamicBean.getNoteImgList();
                if (noteImgListBeans!=null){
                    String noteBackImg=noteImgListBeans.get(0).getImgUrl();
                    if (!TextUtils.isEmpty(noteBackImg)){
                        Picasso.with(DynamicActivity.this)
                                .load(noteBackImg)
                                .config(Bitmap.Config.RGB_565)
                                .centerCrop()
                                .fit()
                                .networkPolicy(NetworkPolicy.NO_STORE)
                                .into(ivBackground);
                    }
                }else {
                    ivBackground.setVisibility(View.GONE);
                }
            }
            llPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dynamicBeans!=null){
                        index=position-1;
                        Intent intent=new Intent(DynamicActivity.this, MomentsAutoActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putParcelable("data",dynamicBeans.get(position-1));
                        bundle.putString("type","2");
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            });
            llTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dynamicBeans!=null){
                        index=position-1;
                        Intent intent=new Intent(DynamicActivity.this, MomentsAutoActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putParcelable("data",dynamicBeans.get(position-1));
                        bundle.putString("type","2");
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            });
        }
    }


}
