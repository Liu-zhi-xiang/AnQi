package com.motorbike.anqi.activity.my;

import android.content.Intent;

import android.os.Bundle;
import android.view.PointerIcon;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.motorbike.anqi.R;
import com.motorbike.anqi.adapter.IntegralMallAdapter;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.util.StatusBarUtil;
import com.motorbike.anqi.view.bgabanner.BGABanner;

import java.util.ArrayList;
import java.util.List;

/**
 * 积分商城---已废弃
 */
public class IntegralMallActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private LinearLayout llBack,llIntegralRecord,llExchangeRecord;
    private TextView tvTitle;
    private BGABanner bgaBanner;
    private GridView gridView;
    private IntegralMallAdapter adapter;
    private List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_mall);
        initView();
        processLogic();
    }
    @Override
    protected void initSystemBarTint()
    {
        StatusBarUtil.transparencyBar(this);
    }
    private void initView() {
        llBack= (LinearLayout) findViewById(R.id.llBack);
        llBack.setOnClickListener(this);
        llExchangeRecord= (LinearLayout) findViewById(R.id.llExchangeRecord);
        llExchangeRecord.setOnClickListener(this);
        llIntegralRecord= (LinearLayout) findViewById(R.id.llIntegralRecord);
        llIntegralRecord.setOnClickListener(this);
        tvTitle= (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("积分商城");
        bgaBanner= (BGABanner) findViewById(R.id.banner);
        bgaBanner.setAutoPlayAble(true);
        bgaBanner.setAutoPlayInterval(3000);
        list=new ArrayList<>();
        gridView= (GridView) findViewById(R.id.gridView);
        adapter=new IntegralMallAdapter(this);
        for (int i=0;i<8;i++){
            list.add("机车头盔");
        }
        gridView.setAdapter(adapter);
//        adapter.setList(list);
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llBack:
                finish();
                break;
            case R.id.llExchangeRecord://兑换记录
                startActivity(new Intent(this,IntegralRecordActivity.class));
                break;
            case R.id.llIntegralRecord://积分记录
                startActivity(new Intent(this,IntegralRecordActivity.class));
                break;
        }
    }

    private void processLogic()
    {
        // 设置数据源
//        bgaBanner.setData(R.mipmap.uoko_guide_background_1, R.mipmap.uoko_guide_background_2, R.mipmap.uoko_guide_background_3);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(this,ExchangeActivity.class));
    }
}
