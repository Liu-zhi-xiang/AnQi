package com.motorbike.anqi.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.motorbike.anqi.R;
import com.motorbike.anqi.adapter.IntegralRecordAdapter;
import com.motorbike.anqi.bean.ExchangeBean;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.util.UserPreference;
import com.motorbike.anqi.view.SlideListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 兑换记录
 */
public class ExchangeRecordActivity extends BaseActivity implements View.OnClickListener,  AbsListView.OnScrollListener {
    private LinearLayout llBack,llMore;
    private TextView tvTitle;
    private SlideListView slideListView;
    private IntegralRecordAdapter adapter;
    private Map<String,Object> exchangeMap;
    private UserPreference preference;
    private int index=0;
    private List<ExchangeBean> datalist=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_record);
        preference=UserPreference.getUserPreference(this);
        initView();
    }

    private void initView() {
        llBack= findViewById(R.id.llBack);
        llBack.setOnClickListener(this);
        llMore= findViewById(R.id.llMore);
        llMore.setOnClickListener(this);
        tvTitle= findViewById(R.id.tvTitle);
        tvTitle.setText("兑换记录");
        slideListView= findViewById(R.id.slideListView);
        adapter=new IntegralRecordAdapter(this);
        slideListView.setOnScrollListener(this);
        slideListView.setAdapter(adapter);

        http(preference.getUserId(),index+"","10");
    }

    private void http(String userId,String pageNum,String pageCount){
        showLoading();
        exchangeMap=new HashMap<>();
        if (exchangeMap!=null){
            exchangeMap.clear();
        }
        exchangeMap.put("userId",userId);
        exchangeMap.put("pageNum",pageNum);
        exchangeMap.put("pageCount",pageCount);
        okHttp(this, BaseRequesUrl.ExchangeHistory, HttpTagUtil.ExchangeHistory,exchangeMap);
    }

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag){
            case HttpTagUtil.ExchangeHistory:
                dismissLoading();
                if (data!=null){
                    List<ExchangeBean> list=new Gson().fromJson(data,new TypeToken<List<ExchangeBean>>(){}.getType() );
                    if (list!=null&&list.size()>0){
                        if (index==0){
                            datalist.clear();
                        }
                        if (index>0){
                            if (datalist.size()==0){
                                showTip("没有更多内容");
                            }
                        }
                        datalist.addAll(list);
                        adapter.setList(datalist);
                        adapter.notifyDataSetChanged();
                        slideListView.loadFinish();
                    }
                }
                slideListView.loadFinish();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llBack:
                finish();
                break;
            case R.id.llMore://查看更多记录
                //获得加载数据
                index=(datalist.size()+9)/10;//补全算法
                http(preference.getUserId(),index+"","10");
                //然后通知MyListView刷新界面
                adapter.notifyDataSetChanged();

                //然后通知加载数据已经完成了

                slideListView.loadFinish();
                break;
        }
    }



    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        slideListView.loadFinish();
    }
}
