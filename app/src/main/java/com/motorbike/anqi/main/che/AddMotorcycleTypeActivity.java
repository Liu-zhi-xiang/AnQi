package com.motorbike.anqi.main.che;

import android.content.Context;

import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.motorbike.anqi.R;
import com.motorbike.anqi.adapter.BrandAdapter;
import com.motorbike.anqi.adapter.ModelAdapter;
import com.motorbike.anqi.adapter.UserCarAdapter;
import com.motorbike.anqi.bean.BrandBean;
import com.motorbike.anqi.bean.ModelBean;
import com.motorbike.anqi.bean.UserCarBean;
import com.motorbike.anqi.bean.Voiceroom;
import com.motorbike.anqi.beseAdapter.CommonAdapter;
import com.motorbike.anqi.beseAdapter.base.ViewHolder;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.init.MyApplication;
import com.motorbike.anqi.main.AutoMomentsActivity;
import com.motorbike.anqi.util.PixelFormat;
import com.motorbike.anqi.util.UserPreference;
import com.motorbike.anqi.xrecyclerview.XRecyclerView;
import com.motorbike.anqi.xrecyclerview.progressindicator.ProgressStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddMotorcycleTypeActivity extends BaseActivity implements View.OnClickListener{
    private XRecyclerView recyclerView;
    private LinearLayout chexingLayout,pinpaiLayout;
    private TextView titleTv,pinpai_tv,chexing_tv;
    private ImageView xia_pinpai_img,xia_jixing_img;
    private int  marginHorizontal=0;
    private MyAdapter myadapter;
//    private EditText pinpaiEt,xinghaoEt;
    private Map<String,Object> brandMap,modelMap,userCarMap,addCarMap;
    private BrandAdapter brandAdapter;
    private List<BrandBean> brandBeanList;
    private List<ModelBean> modelBeanList;
    private ModelAdapter modelAdapter;
    private String brandId,modelId,carBrand,carModels;
    private UserPreference preference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_motorcycle_type);
        preference=UserPreference.getUserPreference(this);
        getUserCarHttp(preference.getUserId());
        initView();
    }

    private void initView()
    {
        findViewById(R.id.back_layout).setOnClickListener(this);
        findViewById(R.id.queding_layout).setOnClickListener(this);
        pinpaiLayout=findViewById(R.id.add_pinpai_layout);
        chexingLayout=findViewById(R.id.add_chexing_layout);
        xia_pinpai_img=findViewById(R.id.xia_pinpai_imgview);
        xia_jixing_img=findViewById(R.id.xia_chexing_imgview);
        titleTv=findViewById(R.id.title_text);
        titleTv.setText("添加车型");
        pinpai_tv=findViewById(R.id.pinpai_tv);
        chexing_tv=findViewById(R.id.chexing_tv);
//        pinpaiEt=findViewById(R.id.pinpaiEt);
//        xinghaoEt=findViewById(R.id.xinghaoEt);
        pinpaiLayout.setOnClickListener(this);
        chexingLayout.setOnClickListener(this);
        recyclerView=findViewById(R.id.tianjia_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.back_layout:
                finish();
                break;
            case R.id.add_pinpai_layout:
                brandHtpp();
                break;
            case R.id.add_chexing_layout:
                if (!TextUtils.isEmpty(brandId)){
                    modelHttp(brandId);
                }else {
                    showTip("请选择品牌");
                }
                break;
            case R.id.queding_layout://确定
//                String brandContent=pinpaiEt.getText().toString().trim();
//                String modelContent=xinghaoEt.getText().toString().trim();
//                addCarHttp(preference.getUserId(),carBrand,carModels,"","");
                if ((!TextUtils.isEmpty(carBrand)&&!TextUtils.isEmpty(carModels))) {
                    addCarHttp(preference.getUserId(), carBrand, carModels, "", "");
                }else {
                    prompt("请选择车型");
                }
                break;
        }
    }

    private PopupWindow popWnd;
    private View popuview;
    private ListView cheList;


    private void shouPopuWodBrand(View fuview,final ImageView imageView,String type)
    {

        if (popWnd == null){
            popWnd = new PopupWindow(this);
        }
        popWnd.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss()
            {
                imageView.setBackgroundResource(R.mipmap.xia);
            }
        });
        if (popuview == null) {
            popuview = LayoutInflater.from(this).inflate(R.layout.ppopuview_chexing, null);
            cheList = popuview.findViewById(R.id.popu_list);
        }
        if (type.equals("1")) {
            if (brandAdapter == null) {
                brandAdapter = new BrandAdapter(this);
            }
            brandAdapter.setList(brandBeanList);
            ViewGroup.LayoutParams params = cheList.getLayoutParams();
            int x = ((brandBeanList.size() < 5) ? brandBeanList.size() : 5);
            params.height = PixelFormat.dip2px(this, x * 50);
            cheList.setLayoutParams(params);
            cheList.setAdapter(brandAdapter);

            cheList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    brandId = brandBeanList.get(position).getId();
                    carBrand = brandBeanList.get(position).getCarBrand();
                    carModels="";
                    chexing_tv.setText("");
                    pinpai_tv.setText(carBrand);
                    popWnd.dismiss();
                }
            });
            brandAdapter.notifyDataSetChanged();
        }else {
            if (modelAdapter==null){
                modelAdapter=new ModelAdapter(this);
            }
            modelAdapter.setList(modelBeanList);
            ViewGroup.LayoutParams params = cheList.getLayoutParams();
            int x = ((modelBeanList.size() < 5) ? modelBeanList.size() : 5);
            params.height = PixelFormat.dip2px(this, x * 50);
            cheList.setLayoutParams(params);
            cheList.setAdapter(modelAdapter);
            cheList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    carModels=modelBeanList.get(position).getCarModels();
                    chexing_tv.setText(carModels);
                    modelId=modelBeanList.get(position).getId();
                    popWnd.dismiss();
                }
            });
            modelAdapter.notifyDataSetChanged();
        }
        popWnd.setContentView(popuview);
        popWnd.setWidth(MyApplication.mScreenWidth - marginHorizontal);
        popWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popWnd.setAnimationStyle(R.style.contextMenuAnim);

        popWnd.setFocusable(true);
        popWnd.setOutsideTouchable(true);
        popWnd.showAsDropDown(fuview);

        imageView.setBackgroundResource(R.mipmap.shang);

    }


    private void getUserCarHttp(String userId)
    {
        showLoading();
        userCarMap=new HashMap<>();
        if (userCarMap!=null){
            userCarMap.clear();
        }
        userCarMap.put("userId",userId);
        okHttp(this,BaseRequesUrl.UserCarType,HttpTagUtil.UserCarType,userCarMap);
    }

    /**
     * 获取所有车型品牌
     */
    private void brandHtpp()
    {
        showLoading();
        brandMap=new HashMap<>();
        if (brandMap!=null){
            brandMap.clear();
        }
        okHttp(this, BaseRequesUrl.GetALlBrand, HttpTagUtil.GetAllBrand,brandMap);
    }

    /**
     * 根据品牌获取车型
     * @param brandId
     */
    private void modelHttp(String brandId){
        modelMap=new HashMap<>();
        if (modelMap!=null){
            modelMap.clear();
        }
        modelMap.put("brandId",brandId);
        okHttp(this,BaseRequesUrl.BrandTypeModel,HttpTagUtil.BrandTypeModel,modelMap);
    }

    /**
     * 添加车型接口
     * @param userId
     * @param brand
     * @param models
     * @param brandInput
     * @param modelsInput
     */
    private void addCarHttp(String userId,String brand,String models,String brandInput,String modelsInput){
        showLoading();
        addCarMap=new HashMap<>();
        if (addCarMap!=null){
            addCarMap.clear();
        }
        if (TextUtils.isEmpty(brand)||TextUtils.isEmpty(models)){
            brand="";
            models="";
        }
        addCarMap.put("userId",userId);
        addCarMap.put("brand",brand);
        addCarMap.put("models",models);
        addCarMap.put("brandInput",brandInput);
        addCarMap.put("modelsInput",modelsInput);
        okHttp(this,BaseRequesUrl.AddCar,HttpTagUtil.AddCar,addCarMap);
    }
    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag){
            case HttpTagUtil.GetAllBrand:
                dismissLoading();
                if (data!=null){
                    if (brandBeanList!=null){
                        brandBeanList.clear();
                    }
                    brandBeanList=new Gson().fromJson(data,new TypeToken<List<BrandBean>>(){}.getType());
                    if (brandBeanList!=null&&brandBeanList.size()>0){
                        shouPopuWodBrand(pinpaiLayout,xia_pinpai_img,"1");
                        Log.e("aaa",brandBeanList.size()+"     brandSize");
                    }
                }
                break;
            case HttpTagUtil.BrandTypeModel:
                if (data!=null){
                    if (modelBeanList!=null){
                        modelBeanList.clear();
                    }
                    modelBeanList=new Gson().fromJson(data,new TypeToken<List<ModelBean>>(){}.getType());
                    if (modelBeanList!=null&&modelBeanList.size()>0){
                        shouPopuWodBrand(chexingLayout,xia_jixing_img,"2");
                        Log.e("aaa",modelBeanList.size()+"     modelSize");
                    }
                }
                break;
            case HttpTagUtil.UserCarType://获取用户已有车型
                dismissLoading();
                if (data!=null){
                    List<UserCarBean> carBeanList=new Gson().fromJson(data ,new TypeToken<List<UserCarBean>>(){}.getType());
                    if (carBeanList!=null){
                        Log.e("aaa",carBeanList.size()+"");
                        myadapter=new MyAdapter(this,R.layout.add_chexing_recycle_item,carBeanList);
                        recyclerView.setAdapter(myadapter);
                        recyclerView.setPullRefreshEnabled(false);
                        recyclerView.setLoadingMoreEnabled(false);
                        myadapter.notifyDataSetChanged();
                        marginHorizontal= PixelFormat.dip2px(this,56);
                    }
                }
                break;
            case HttpTagUtil.AddCar:
                dismissLoading();
                if (data!=null){
                    showTip(mag);
                    preference.setCarType(carBrand+carModels);
                    preference.save();
                    finish();
                }
                break;
        }
    }

    public class MyAdapter extends CommonAdapter<UserCarBean>
    {

        public MyAdapter(Context context, int layoutId, List<UserCarBean> datas) {
            super(context, layoutId, datas);
        }
        @Override
        protected void convert(ViewHolder holder,  UserCarBean userCarBean, int position) {
            TextView tvBrand=holder.getView(R.id.tvBrand);
            TextView tvModel=holder.getView(R.id.tvModel);
            if (userCarBean!=null){
                tvBrand.setText(userCarBean.getBrand());
                tvModel.setText(userCarBean.getModels());
            }

        }
    }

}
