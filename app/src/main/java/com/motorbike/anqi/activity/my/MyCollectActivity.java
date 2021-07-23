package com.motorbike.anqi.activity.my;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.motorbike.anqi.MainActivityTwo;
import com.motorbike.anqi.R;
import com.motorbike.anqi.activity.voice.VoiceHomeActivity;
import com.motorbike.anqi.bean.CollectBean;
import com.motorbike.anqi.bean.MyCollectBean;
import com.motorbike.anqi.bean.UserCarBean;
import com.motorbike.anqi.beseAdapter.CommonAdapter;
import com.motorbike.anqi.beseAdapter.base.ViewHolder;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.util.MD5Util;
import com.motorbike.anqi.util.UserPreference;
import com.motorbike.anqi.view.InputDialog;
import com.motorbike.anqi.xrecyclerview.XRecyclerView;
import com.motorbike.anqi.xrecyclerview.progressindicator.ProgressStyle;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的收藏
 */
public class MyCollectActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private TextView tvTitle;
    private XRecyclerView recyclerView;
    private MyAdapter adapter;
    private Map<String,Object> collectMap,addRoomMap;
    private UserPreference preference;
    private String roomNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        preference=UserPreference.getUserPreference(this);
        collectMap=new HashMap<>();
        http(preference.getUserId());
        initView();
    }

    private void initView() {
        llBack= findViewById(R.id.llBack);
        llBack.setOnClickListener(this);
        tvTitle= findViewById(R.id.tvTitle);
        tvTitle.setText("我的收藏");
        recyclerView= findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);



    }

    private void http(String userId){
        showLoading();
        if (collectMap!=null){
            collectMap.clear();
        }
        collectMap.put("userId",userId);
        okHttp(this, BaseRequesUrl.Mycollect, HttpTagUtil.Mycollect,collectMap);
    }
    /**
     * 扫描二维码加入房间
     *
     * @param roomNo
     * @param password
     */
    private void jionRoom(String roomNo, String password) {
        showLoading();
        addRoomMap = new HashMap<>();
        if (addRoomMap != null) {
            addRoomMap.clear();
        }
        roomNum = roomNo;
        addRoomMap.put("userId", preference.getUserId());
        addRoomMap.put("roomNo", roomNo);
        addRoomMap.put("password", password);
        okHttp(this, BaseRequesUrl.AddRoom, HttpTagUtil.AddRoom, addRoomMap);
    }
    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag){
            case HttpTagUtil.Mycollect:
                dismissLoading();
                if (data!=null){
                    Log.e("aaa",data.toString());
                    List<CollectBean> collectBeanList=new Gson().fromJson(data ,new TypeToken<List<CollectBean>>(){}.getType());
                    if (collectBeanList!=null){
                        adapter=new MyAdapter(this,R.layout.mycollect_item_layout,collectBeanList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setPullRefreshEnabled(false);
                        recyclerView.setLoadingMoreEnabled(false);
                        adapter.notifyDataSetChanged();
                    }

                }
                break;
            case HttpTagUtil.AddRoom://扫描二维码加入房间
                dismissLoading();
                if (data != null) {
                    showTip(mag);
                    if (!TextUtils.isEmpty(roomNum)) {
                        Intent intent = new Intent(this, VoiceHomeActivity.class);
                        intent.putExtra("roomId", roomNum);
                        startActivity(intent);
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
        }
    }

    public class MyAdapter extends CommonAdapter<CollectBean> {

        public MyAdapter(Context context, int layoutId, List<CollectBean> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, final CollectBean collectBean, int position) {
            LinearLayout llSignIn=holder.getView(R.id.llSignIn);
            ImageView backGround=holder.getView(R.id.backGround);
            TextView tvName=holder.getView(R.id.tvName);
            tvName.setText(collectBean.getTheme());
            Picasso.with(MyCollectActivity.this)
                    .load(collectBean.getBackUrl())
                    .config(Bitmap.Config.RGB_565)
                    .centerCrop()
                    .fit()
                    .networkPolicy(NetworkPolicy.NO_STORE)
                    .into(backGround);
            final String status=collectBean.getStatus();

            llSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(status)){
                        if (status.equals("1")){
                            showInputDialogTwo(collectBean.getPassword(),collectBean.getRoomId());
                        }else {
                            showTip("该房间已关闭");
                        }
                    }
                }
            });
        }
    }

    private void showInputDialogTwo(final String password, final String roormId) {
        final InputDialog inputDialog = new InputDialog(this);
        inputDialog.setTitle("房间密码");
        inputDialog.setOnbutClion(new InputDialog.OnbutClion() {
            @Override
            public void onokclick(String inputstr) {
                String str = MD5Util.encrypt(inputstr);
                Log.e("ccc", str + "     cccc");
                Log.e("ccc", str + "     password");
                if (str.equals(password)) {
                    jionRoom(roormId, inputstr);
                    inputDialog.dismiss();
                } else {
                    showTip("房间密码不正确,请重新输入!");
                }
            }

            @Override
            public void onFinshclick() {

            }
        });
        inputDialog.show();
    }
}
