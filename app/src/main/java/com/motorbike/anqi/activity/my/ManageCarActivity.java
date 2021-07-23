package com.motorbike.anqi.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.motorbike.anqi.R;
import com.motorbike.anqi.bean.UserCarBean;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.main.che.AddMotorcycleTypeActivity;
import com.motorbike.anqi.util.UserPreference;
import com.motorbike.anqi.view.CustomDialogView;
import com.motorbike.anqi.view.SlideListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.motorbike.anqi.view.SlideListView.MOD_RIGHT;

/**
 * 管理已有车型
 */
public class ManageCarActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener {
    private SlideListView slideListView;
    private TextView tvTitle,tvAdd;
    private Map<String,Object> addressMap,delMap,defaultMap;
    private UserPreference preference;
    private SelectAddAdapter addAdapter;
    private List<UserCarBean> carBeanList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_add);
        preference=UserPreference.getUserPreference(this);
        initView();
    }

    private void initView() {
        slideListView=findViewById(R.id.slideListView);
        findViewById(R.id.llBack).setOnClickListener(this);
        findViewById(R.id.llAddress).setOnClickListener(this);
        tvTitle=findViewById(R.id.tvTitle);
        tvTitle.setText("管理车型");
        tvAdd=findViewById(R.id.tvAdd);
        tvAdd.setText("添加");
        addAdapter=new SelectAddAdapter(this);
        addressMap=new HashMap<>();
        http(preference.getUserId());
        slideListView.initSlideMode(MOD_RIGHT);
        slideListView.setOnScrollListener(this);
        slideListView.setAdapter(addAdapter);
//        slideListView.setOnItemClickListener(this);
        addAdapter.notifyDataSetChanged();
    }

    /**
     * 查询用户已有车型
     * @param userId
     */
    private void http(String userId){
        showLoading();
        if (addressMap!=null){
            addressMap.clear();
        }
        addressMap.put("userId",userId);
        okHttp(this, BaseRequesUrl.UserCarType, HttpTagUtil.UserCarType,addressMap);
    }

    /**
     * 删除车型
     */
    private void httpDelAddress(String brand,String userId,String carModels){
        showLoading();
        delMap=new HashMap<>();
        if (delMap!=null){
            delMap.clear();
        }
        delMap.put("brand",brand);
        delMap.put("userId",userId);
        delMap.put("carModels",carModels);
        okHttp(this,BaseRequesUrl.DelUserBrand,HttpTagUtil.DelUserBrand,delMap);
    }

    /**
     * 设置默认车型
     */
    private void setDefaultCar(String carTypeId,String isdefault){
        showLoading();
        defaultMap=new HashMap<>();
        if (defaultMap!=null){
            defaultMap.clear();
        }
        defaultMap.put("userId",preference.getUserId());
        defaultMap.put("carTypeId",carTypeId);
        defaultMap.put("isdefault",isdefault);//1 设置   0 取消
        okHttp(this,BaseRequesUrl.SetDefaultCar,HttpTagUtil.SetDefaultCar,defaultMap);
    }

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag){
            case HttpTagUtil.UserCarType:
                dismissLoading();
                if (data!=null){
                    carBeanList=new Gson().fromJson(data ,new TypeToken<List<UserCarBean>>(){}.getType());
                    if (carBeanList!=null){
                        addAdapter.setList(carBeanList);
                        addAdapter.notifyDataSetChanged();
                    }
                }
                break;
            case HttpTagUtil.DelUserBrand:
                dismissLoading();
                showTip(mag);
                slideListView.slideBack();
                break;
            case HttpTagUtil.SetDefaultCar:
                dismissLoading();

                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llBack:
                finish();
                break;
            case R.id.llAddress:
                startActivity(new Intent(this,AddMotorcycleTypeActivity.class));
                break;

        }
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        http(preference.getUserId());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        slideListView.loadFinish();
    }

    private int xx=0;
    public class SelectAddAdapter extends BaseAdapter {
        private Context context;
        private List<UserCarBean> list;

        public SelectAddAdapter(Context context) {
            this.context = context;
        }

        public void setList(List<UserCarBean> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list!=null)
                return list.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView==null){
                viewHolder=new ViewHolder();
                convertView= LayoutInflater.from(context).inflate(R.layout.car_type,parent,false);
                viewHolder.tvBrand=convertView.findViewById(R.id.tvBrand);
                viewHolder.tvModel=convertView.findViewById(R.id.tvModel);
                viewHolder.tvDel=convertView.findViewById(R.id.tvDel);
                viewHolder.llMoRen=convertView.findViewById(R.id.llMoRen);
                viewHolder.llSelectCar=convertView.findViewById(R.id.llSelectCar);
                viewHolder.ivMoRen=convertView.findViewById(R.id.ivMoRen);
                viewHolder.tvSelect=convertView.findViewById(R.id.tvSelect);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            final String isDefault=list.get(position).getIsdefault();
            if (!TextUtils.isEmpty(isDefault)){
                if (isDefault.equals("1")){
                    xx=position;
                    viewHolder.ivMoRen.setImageResource(R.mipmap.selected);
                    preference.setCarType(list.get(position).getBrand()+list.get(position).getModels());
                    preference.save();
                }else {
                    viewHolder.ivMoRen.setImageResource(R.mipmap.not_select);
                }
            }
            viewHolder.tvBrand.setText(list.get(position).getBrand());
            viewHolder.tvModel.setText(list.get(position).getModels());
            viewHolder.tvDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlertDialog(list.get(position).getBrand(),list.get(position).getModels(),list.get(position).getUserIdStr(),position);
                }
            });
            viewHolder.tvSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.putExtra("carBand",carBeanList.get(position).getBrand());
                    intent.putExtra("carModels",carBeanList.get(position).getModels());
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });
            viewHolder.ivMoRen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isDefault.equals("0")){
                        list.get(position).setIsdefault("1");
                        viewHolder.ivMoRen.setImageResource(R.mipmap.selected);
                        setDefaultCar(list.get(position).getIdStr(),"1");
                        preference.setCarType(list.get(position).getBrand()+list.get(position).getModels());
                        preference.save();
                        Log.e("vvvv",xx+"     xxxxxxxxx22222");
                        if (xx!=position){
                            list.get(xx).setIsdefault("0");
                        }
                        notifyDataSetChanged();
                    }

                }
            });
            return convertView;
        }
        class ViewHolder{
            TextView tvBrand,tvModel,tvDel,tvSelect;
            LinearLayout llMoRen,llSelectCar;
            ImageView ivMoRen;
        }

        /**
         * 自定义dialog提示框
         *
         * @param
         */
        public void showAlertDialog(final String brand, final String model, final String userid, final int position)
        {
            final CustomDialogView dialog = new CustomDialogView(context);
            dialog.setTitle("提示信息");
            dialog.setMessage("是否确认删除?");
            dialog.setYesOnclickListener("确定", new CustomDialogView.onYesOnclickListener() {
                @Override
                public void onYesClick() {
                    httpDelAddress(brand,userid,model);
                    if (carBeanList!=null){
                        carBeanList.remove(position);
                        addAdapter.notifyDataSetChanged();
                    }
                    dialog.dismiss();

                }
            });
            dialog.setNoOnclickListener("取消", new CustomDialogView.onNoOnclickListener() {
                @Override
                public void onNoClick() {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

    }
}
