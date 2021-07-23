package com.motorbike.anqi.activity.my;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.motorbike.anqi.R;
import com.motorbike.anqi.bean.JsonBean;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.util.GetJsonDataUtil;
import com.motorbike.anqi.util.UserPreference;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 修改收货地址
 */
public class ModifyAddressActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llSave,llBack;
    private RelativeLayout rlAddress;
    private EditText etName,etPhone,etAddress;
    private TextView tvAddress,tvTitle;
    private ImageView ivMoRen;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Map<String,Object> addMap;
    private UserPreference preference;
    private String tx,name,phone,address,addressDetail,addressId,moRen="0",isDefault;
    private boolean isOpen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harvest_address);
        preference=UserPreference.getUserPreference(this);
        name=getIntent().getStringExtra("name");
        phone=getIntent().getStringExtra("phone");
        address=getIntent().getStringExtra("address");
        addressDetail=getIntent().getStringExtra("addressDetail");
        addressId=getIntent().getStringExtra("addressId");
        isDefault=getIntent().getStringExtra("isDefault");
        initView();
    }

    private void initView()
    {
        llBack=findViewById(R.id.llBack);
        llBack.setOnClickListener(this);
        llSave=findViewById(R.id.llSave);
        llSave.setOnClickListener(this);
        rlAddress=findViewById(R.id.rlAddress);
        rlAddress.setOnClickListener(this);
        etName=findViewById(R.id.etName);
        etPhone=findViewById(R.id.etPhone);
        etAddress=findViewById(R.id.etAddress);
        tvAddress=findViewById(R.id.tvAddress);
        tvTitle=findViewById(R.id.tvTitle);
        tvTitle.setText("编辑收货地址");
        ivMoRen=findViewById(R.id.ivMoRen);
        ivMoRen.setOnClickListener(this);
        initJsonData();
        if (!TextUtils.isEmpty(name)){
            etName.setText(name);
        }
        if (!TextUtils.isEmpty(phone)){
            etPhone.setText(phone);
        }
        if (!TextUtils.isEmpty(address)){
            tvAddress.setText(address);
            tx=address;
        }
        if (!TextUtils.isEmpty(addressDetail)){
            etAddress.setText(addressDetail);
        }
        if (!TextUtils.isEmpty(isDefault)){
            if (isDefault.equals("1")){
                ivMoRen.setImageResource(R.mipmap.xz);
                isOpen=true;
            }else {
                ivMoRen.setImageResource(R.mipmap.wxz);
                isOpen=false;
            }
        }
    }
    private void http(String userId,String receiver,String phone,String addressDetail,String address,String isdefault,String addressId){
        showLoading();
        addMap=new HashMap<>();
        if (addMap!=null){
            addMap.clear();
        }
        addMap.put("userId",userId);
        addMap.put("receiver",receiver);
        addMap.put("phone",phone);
        addMap.put("addressDetail",addressDetail);
        addMap.put("address",address);
        addMap.put("isdefault",isdefault);
        addMap.put("addressId",addressId);
        okHttp(this, BaseRequesUrl.ModifyAddress, HttpTagUtil.ModifyAddress,addMap);

    }

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag){
            case HttpTagUtil.ModifyAddress:
                dismissLoading();
                if (data!=null){
                    showTip(mag);
                    finish();
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
            case R.id.llSave://保存
               String name=etName.getText().toString().trim();
               String phone=etPhone.getText().toString().trim();
               String addDetail=etAddress.getText().toString().trim();
                if (!TextUtils.isEmpty(name)){
                    if (!TextUtils.isEmpty(phone)){
                        if (!TextUtils.isEmpty(tx)){
                            if (!TextUtils.isEmpty(addDetail)){
                                http(preference.getUserId(),name,phone,addDetail,tx,moRen,addressId);
                            }else {
                                showTip("详细地址不能为空");
                            }
                        }else {
                            showTip("请选择地址");
                        }
                    }else {
                        showTip("联系方式不能为空");
                    }
                }else {
                    showTip("收货人不能为空");
                }
                break;
            case R.id.rlAddress://选择地址
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                showAddress();
                break;
            case R.id.ivMoRen:
                if (isOpen==false){
                    ivMoRen.setImageResource(R.mipmap.wxz);
                    isOpen=true;
                    moRen="0";
                }else {
                    ivMoRen.setImageResource(R.mipmap.xz);
                    isOpen=false;
                    moRen="1";
                }
                break;
        }
    }

    private void showAddress(){
        //条件选择器
        OptionsPickerView pvOptions = new  OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                //返回的分别是三个级别的选中位置
                tx = options1Items.get(options1).getPickerViewText()
                        + options2Items.get(options1).get(option2)
                        + options3Items.get(options1).get(option2).get(options3);
                Log.e("aaaa",tx);
                tvAddress.setText(tx);
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(0xFFB4E317)//确定按钮文字颜色
                .setCancelColor(0xFF999999)//取消按钮文字颜色
                .setTitleBgColor(0xFF000000)//标题背景颜色 Night mode
                .setBgColor(0xFF242424)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setTextColorCenter(0xFFffffff)
                .setTextColorOut(0xFF999999)
                .setLinkage(true)//设置是否联动，默认true
                .setCyclic(false, false, false)//循环与否
                .build();
        pvOptions.setPicker(options1Items, options2Items, options3Items);
        pvOptions.show();
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    City_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

    }


    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return detail;
    }

}
