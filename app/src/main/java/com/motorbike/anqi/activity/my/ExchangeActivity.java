package com.motorbike.anqi.activity.my;

import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.motorbike.anqi.R;
import com.motorbike.anqi.bean.AddressBean;
import com.motorbike.anqi.bean.ExchangeDetailBean;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.util.UserPreference;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 积分兑换页面
 */
public class ExchangeActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack,llAddress,llSuccess,llReturn,llGoodLayout;
    private TextView tvName,tvJiFenNum,tvNum,tvAddress,tvJianjie,tvExChange,tvTitle;
    private ImageView ivImage;
    private String goodId,pointNum,address,isExchange;
    private Map<String,Object> goodMap,detailMap,addressMap;
    private UserPreference preference;
    private List<AddressBean> addressBeanList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        preference=UserPreference.getUserPreference(this);
        goodId=getIntent().getStringExtra("goodId");
        initView();
    }

    private void initView() {
        llBack= findViewById(R.id.llBack);
        llBack.setOnClickListener(this);
        llAddress= findViewById(R.id.llAddress);
        llAddress.setOnClickListener(this);
        tvTitle=  findViewById(R.id.tvTitle);

        tvName= findViewById(R.id.tvName);
        tvJiFenNum= findViewById(R.id.tvJiFenNum);
        tvNum= findViewById(R.id.tvNum);
        tvAddress=  findViewById(R.id.tvAddress);
        tvJianjie= findViewById(R.id.tvJianjie);
        tvExChange=  findViewById(R.id.tvExChange);
        tvExChange.setOnClickListener(this);
        ivImage=findViewById(R.id.ivImage);
        llGoodLayout=findViewById(R.id.llGoodLayout);
        llSuccess=findViewById(R.id.llSuccess);
        llReturn=findViewById(R.id.llReturn);
        llReturn.setOnClickListener(this);
        if (!TextUtils.isEmpty(goodId)){
            httpDetail(preference.getUserId(),goodId);
        }

    }
    private void httpDetail(String userId,String goodsId){
        showLoading();
        detailMap=new HashMap<>();
        if (detailMap!=null){
            detailMap.clear();
        }
        detailMap.put("userId",userId);
        detailMap.put("goodsId",goodsId);
        okHttp(this,BaseRequesUrl.ExchangeDetails,HttpTagUtil.ExchangeDetails,detailMap);
    }
    private void http(String userId,String goodId,String orderPoint,String address){
        showLoading();
        goodMap=new HashMap<>();
        if (goodMap!=null){
            goodMap.clear();
        }
        goodMap.put("userId",userId);
        goodMap.put("goodsId",goodId);
        goodMap.put("orderPoint",orderPoint);
        goodMap.put("goodsNum","1");
        goodMap.put("address",address);
        okHttp(this, BaseRequesUrl.IntegralExchange, HttpTagUtil.IntegralExchange,goodMap);
    }
    /**
     * 查询所有地址
     * @param userId
     */
    private void http(String userId){
        showLoading();
        addressMap=new HashMap<>();
        if (addressMap!=null){
            addressMap.clear();
        }
        addressMap.put("userId",userId);
        okHttp(this, BaseRequesUrl.AllAddress, HttpTagUtil.AllAddress,addressMap);
    }
    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag){
            case HttpTagUtil.IntegralExchange:
                dismissLoading();
                if (data!=null){
                    llGoodLayout.setVisibility(View.GONE);
                    llSuccess.setVisibility(View.VISIBLE);
                }
                break;
            case HttpTagUtil.ExchangeDetails:
                dismissLoading();
                if (data!=null){
                    ExchangeDetailBean detailBean=new Gson().fromJson(data,ExchangeDetailBean.class);
                    if (detailBean!=null){
                        tvTitle.setText(detailBean.getGoodsName());
                        tvName.setText(detailBean.getGoodsName());
                        pointNum=detailBean.getPointNum();
                        tvJiFenNum.setText(detailBean.getPointNum());
                        tvNum.setText("数量:"+detailBean.getInventory());
                        tvJianjie.setText("名称:"+detailBean.getGoodsDesc());
                        isExchange=detailBean.getIsexchange();
                        if (!TextUtils.isEmpty(isExchange)){
                            if (isExchange.equals("0")){//0 不可以  1 可以
                                tvExChange.setClickable(false);
                                tvExChange.setBackgroundResource(R.color.home_but_text_an);
                            }else {
                                tvExChange.setClickable(true);
                                tvExChange.setBackgroundResource(R.color.top_color);
                            }
                        }
                        if (!TextUtils.isEmpty(detailBean.getGoodsImg())){
                            Picasso.with(ExchangeActivity.this)
                                    .load(detailBean.getGoodsImg())
                                    .config(Bitmap.Config.RGB_565)
                                    .centerCrop()
                                    .fit()
                                    .networkPolicy(NetworkPolicy.NO_STORE)
                                    .into(ivImage);
                        }
                        address=detailBean.getDefaultAddress();
                        tvAddress.setText(detailBean.getDefaultAddress());
                    }
                }
                break;
            case HttpTagUtil.AllAddress:
                dismissLoading();
                if (data!=null){
                    addressBeanList=new Gson().fromJson(data ,new TypeToken<List<AddressBean>>(){}.getType());
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
            case R.id.tvExChange://马上兑换
                showAlertDialog("确认使用"+pointNum+"积分兑换？","");
                break;
            case R.id.llAddress://选择地址
//                preference.setGoodaddress("");
//                preference.save();
                startActivityForResult(new Intent(this,ManageAddActivity.class),102);
                break;
            case R.id.llReturn://回积分商城
                startActivity(new Intent(this,IntegralMallTwoActivity.class));
                break;
        }
    }

    @Override
    public void showAlertDialog(String title, String masg) {
        super.showAlertDialog(title, masg);

    }

    @Override
    protected void onclickDialog() {
        super.onclickDialog();
        if (!TextUtils.isEmpty(address)){
            http(preference.getUserId(),goodId,pointNum,address);
        }else {
            showTip("请选择收货地址");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!TextUtils.isEmpty(preference.getGoodaddress())){
            if (addressBeanList!=null){
                for (int i=0;i<addressBeanList.size();i++){
                    Log.e("kkk",preference.getGoodaddress()+"   BaseRequesUrl.address");
                    String add=addressBeanList.get(i).getAddress()+addressBeanList.get(i).getAddressDetail();
                    if (!preference.getGoodaddress().equals(add)){
                        Log.e("kkk",add+"   add");
                        tvAddress.setText("");
                    }
                }
            }
        }else {
            tvAddress.setText("");
        }
        if (resultCode == 102){
            address=data.getExtras().getString("data");
            preference.setGoodaddress(address);
            preference.save();
            tvAddress.setText(address);
            Log.e("kkk","1111111");
        }

        Log.e("kkk","kkkkkk");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        http(preference.getUserId());
    }
}
