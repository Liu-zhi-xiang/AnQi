package com.motorbike.anqi.activity.my;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.google.gson.Gson;
import com.motorbike.anqi.R;
import com.motorbike.anqi.adapter.IntegralMallAdapter;
import com.motorbike.anqi.bean.GoodslistBean;
import com.motorbike.anqi.bean.MallBean;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.util.UserPreference;
import com.motorbike.anqi.view.MyGridView;
import com.motorbike.anqi.view.bgabanner.BGABanner;
import com.motorbike.anqi.xrecyclerview.XRecyclerView;
import com.motorbike.anqi.xrecyclerview.progressindicator.ProgressStyle;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 积分商城
 */
public class IntegralMallTwoActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack;
    private TextView tvTitle;
    private XRecyclerView recyclerView;
    private UserPreference preference;
    private Map<String,Object> mallMap;
    private List<String> goodsImgList;
    private List<GoodslistBean> goodslist;
    private  List<DelegateAdapter.Adapter> adapters;
    private  DelegateAdapter delegateAdapter;
    private MallBean mallBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_mall_two);
        preference=UserPreference.getUserPreference(this);
        http(preference.getUserId());
        initView();
    }

    private void initView() {
        llBack= findViewById(R.id.llBack);
        llBack.setOnClickListener(this);
        tvTitle= findViewById(R.id.tvTitle);
        tvTitle.setText("积分商城");

        recyclerView= findViewById(R.id.recyclerView);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 20);
        VirtualLayoutManager layoutManager = new VirtualLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        delegateAdapter = new DelegateAdapter(layoutManager, true);
        recyclerView.setAdapter(delegateAdapter);
        adapters = new LinkedList<>();
        adapters.add(new LunboAdapter(this, new LinearLayoutHelper(), 1));
        adapters.add(new GoodsAdapter(this,new GridLayoutHelper(2),1));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setPullRefreshEnabled(false);
        recyclerView.setLoadingMoreEnabled(false);


    }

    private void http(String userId)
    {
        showLoading();
        mallMap=new HashMap<>();
        if (mallMap!=null){
            mallMap.clear();
        }
        mallMap.put("userId",userId);
        okHttp(this, BaseRequesUrl.IntegralMall, HttpTagUtil.IntegralMall,mallMap);
    }

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag){
            case HttpTagUtil.IntegralMall:
                dismissLoading();
                if (data!=null)
                {
                    mallBean=new Gson().fromJson(data,MallBean.class);
                    if (mallBean!=null){
                        goodsImgList=mallBean.getGoodsImgList();
                        goodslist=mallBean.getGoodslist();
//                        Log.e("aaa",goodsImgList.size()+"  image");
                        delegateAdapter.setAdapters(adapters);
                        recyclerView.getAdapter().notifyDataSetChanged();

                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.llBack:
                finish();
                break;
        }
    }
    class LunboAdapter extends DelegateAdapter.Adapter<LuoBoHolder> {

        private Context mContext;

        private LayoutHelper mLayoutHelper;


        private VirtualLayoutManager.LayoutParams mLayoutParams;
        private int mCount = 0;


        public LunboAdapter(Context context, LayoutHelper layoutHelper, int count) {
            this(context, layoutHelper, count, new VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
        }

        public LunboAdapter(Context context, LayoutHelper layoutHelper, int count, @NonNull VirtualLayoutManager.LayoutParams layoutParams) {
            this.mContext = context;
            this.mLayoutHelper = layoutHelper;
            this.mCount = count;
            this.mLayoutParams = layoutParams;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public LuoBoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LuoBoHolder(LayoutInflater.from(mContext).inflate(R.layout.integralmall_one_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(LuoBoHolder holder, int position) {
            if (mallBean!=null){
                holder.tvJIfen.setText(mallBean.getUserIntegral()+"积分");
                List<View> list=new ArrayList<>();
                List<String> goodsImgList =mallBean.getGoodsImgList();
                if (goodsImgList!=null){
                    for (int x=0;x<goodsImgList.size();x++){
                        ImageView imageView=new ImageView(IntegralMallTwoActivity.this);
                        Picasso.with(IntegralMallTwoActivity.this)
                                .load(goodsImgList.get(x))
                                .config(Bitmap.Config.RGB_565)
                                .centerCrop()
                                .fit()
                                .networkPolicy(NetworkPolicy.NO_STORE)
                                .into(imageView);
                        list.add(imageView);
                    }
                    holder.banner.setAutoPlayAble(true);
                    holder.banner.setAutoPlayInterval(3000);
                    holder.banner.setData(list);
//                    holder.banner.setDelegate(new BGABanner.Delegate() {
//                        @Override
//                        public void onBannerItemClick(BGABanner banner, View itemView, Object model, int position) {
//                            Intent intent=new Intent(IntegralMallTwoActivity.this,ExchangeActivity.class);
//                            intent.putExtra("goodId",mallBean.getGoodslist().get(position).getGoodsId());
//                            startActivity(intent);
//                        }
//                    });
                }
            }
        }
        @Override
        protected void onBindViewHolderWithOffset(LuoBoHolder holder, int position, int offsetTotal) {
            holder.llExchangeRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(IntegralMallTwoActivity.this,ExchangeRecordActivity.class));
                }
            });
            holder.llIntegralRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(IntegralMallTwoActivity.this,IntegralRecordActivity.class));
                }
            });

        }

        @Override
        public int getItemCount()
        {
            return mCount;
        }
    }
    static class LuoBoHolder extends RecyclerView.ViewHolder {
        LinearLayout llExchangeRecord,llIntegralRecord;
        BGABanner banner;
        TextView tvJIfen;
        public LuoBoHolder(View itemView) {
            super(itemView);
            llExchangeRecord=itemView.findViewById(R.id.llExchangeRecord);
            llIntegralRecord=itemView.findViewById(R.id.llIntegralRecord);
            banner= itemView.findViewById(R.id.banner);
            tvJIfen=itemView.findViewById(R.id.tvJIfen);
        }

        @Override
        protected void finalize() throws Throwable {

            super.finalize();
        }
    }

    class GoodsAdapter extends DelegateAdapter.Adapter<GoodHolder> {

        private Context mContext;
        private GridLayoutHelper mLayoutHelper;
        private VirtualLayoutManager.LayoutParams mLayoutParams;
        private int mCount = 0;

        public GoodsAdapter(Context context, GridLayoutHelper layoutHelper, int count) {
            this(context, layoutHelper, count, new VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
        }
        public GoodsAdapter(Context context, GridLayoutHelper layoutHelper, int count, @NonNull VirtualLayoutManager.LayoutParams layoutParams) {
            this.mContext = context;
            this.mLayoutHelper = layoutHelper;
            this.mCount = count;
            this.mLayoutParams = layoutParams;
        }
        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public GoodHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new GoodHolder(LayoutInflater.from(mContext).inflate(R.layout.integralmall_two_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(GoodHolder holder, int position) {
            if (mallBean!=null){
                Log.e("aaa",goodslist.size()+"");
                IntegralMallAdapter adapter=new IntegralMallAdapter(IntegralMallTwoActivity.this);
                holder.gridView.setAdapter(adapter);
                adapter.setList(mallBean.getGoodslist());
                holder.setIsRecyclable(false);
                holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent=new Intent(IntegralMallTwoActivity.this,ExchangeActivity.class);
                        intent.putExtra("goodId",mallBean.getGoodslist().get(position).getGoodsId());
                        startActivity(intent);
//                        finish();
                    }
                });
            }
        }
        @Override
        protected void onBindViewHolderWithOffset(GoodHolder holder, int position, int offsetTotal) {

        }

        @Override
        public int getItemCount()
        {
            return mCount;
        }
    }
    static class GoodHolder extends RecyclerView.ViewHolder {
        MyGridView gridView;
        public GoodHolder(View itemView) {
            super(itemView);
            gridView=itemView.findViewById(R.id.gridView);

        }

        @Override
        protected void finalize() throws Throwable {

            super.finalize();
        }
    }




}
