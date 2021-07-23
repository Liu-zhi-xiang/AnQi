package com.motorbike.anqi.activity.my;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.motorbike.anqi.R;
import com.motorbike.anqi.adapter.SelectAddAdapter;
import com.motorbike.anqi.bean.AddressBean;
import com.motorbike.anqi.bean.UserCarBean;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.util.UserPreference;
import com.motorbike.anqi.view.CustomDialogView;
import com.motorbike.anqi.view.SlideListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理收货地址
 */
public class ManageAddActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, SlideListView.ILoadListener {
    private SlideListView slideListView;
    private Map<String,Object> addressMap,delMap,setDefautMap;
    private UserPreference preference;
    private SelectAddAdapter addAdapter;
    private List<AddressBean> addressBeanList;
    private List<AddressBean> newList;
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
        addAdapter=new SelectAddAdapter(this);
        addressMap=new HashMap<>();
        http(preference.getUserId());
        slideListView.setAdapter(addAdapter);
        slideListView.setOnItemClickListener(this);
        slideListView.setOnILoadListener(this);
        addAdapter.notifyDataSetChanged();
        newList=new ArrayList<>();
    }

    /**
     * 查询所有地址
     * @param userId
     */
    private void http(String userId){
        showLoading();
        if (addressMap!=null){
            addressMap.clear();
        }
        addressMap.put("userId",userId);
        okHttp(this, BaseRequesUrl.AllAddress, HttpTagUtil.AllAddress,addressMap);
    }

    /**
     * 删除收货地址
     * @param addressId
     */
    private void httpDelAddress(String addressId){
        showLoading();
        delMap=new HashMap<>();
        if (delMap!=null){
            delMap.clear();
        }
        delMap.put("addressId",addressId);
        okHttp(this,BaseRequesUrl.DelAddress,HttpTagUtil.DelAddress,delMap);
    }

    /**
     * 设置默认地址
     * @param userId
     * @param addressId
     */
    private void httpSetDefault(String userId,String addressId){
        showLoading();
        setDefautMap=new HashMap<>();
        if (setDefautMap!=null){
            setDefautMap.clear();
        }
        setDefautMap.put("userId",userId);
        setDefautMap.put("addressId",addressId);
        okHttp(this,BaseRequesUrl.SetAddrDeafult,HttpTagUtil.SetAddrDeafult,setDefautMap);
    }
    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag){
            case HttpTagUtil.AllAddress:
                dismissLoading();
                if (data!=null){
                    addressBeanList=new Gson().fromJson(data ,new TypeToken<List<AddressBean>>(){}.getType());
                    if (addressBeanList!=null){
                        addAdapter.setList(addressBeanList);
                        addAdapter.notifyDataSetChanged();
                        slideListView.loadFinish();
                    }
                    slideListView.loadFinish();
                }
                break;
            case HttpTagUtil.DelAddress:
                dismissLoading();
                showTip(mag);

                break;
            case HttpTagUtil.SetAddrDeafult:
                dismissLoading();
                showTip(mag);
                if (newList!=null){
                    newList.clear();
                }
                newList.addAll(addressBeanList);
                addAdapter.setList(newList);
                addAdapter.notifyDataSetChanged();

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
                startActivity(new Intent(this,HarvestAddressActivity.class));
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
        Intent intent=new Intent();
        intent.putExtra("data",addressBeanList.get(position).getAddress()+addressBeanList.get(position).getAddressDetail());
        this.setResult(RESULT_OK,intent);
        finish();
    }

    private int xx=0;

    @Override
    public void loadData() {
        http(preference.getUserId());
        slideListView.loadFinish();
        addAdapter.notifyDataSetChanged();
    }

    public class SelectAddAdapter extends BaseAdapter {
        private Context context;
        private List<AddressBean> list;

        public SelectAddAdapter(Context context) {
            this.context = context;
        }

        public void setList(List<AddressBean> list) {
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
                convertView= LayoutInflater.from(context).inflate(R.layout.address_layout,parent,false);
                viewHolder.tvName=convertView.findViewById(R.id.tvName);
                viewHolder.tvPhone=convertView.findViewById(R.id.tvPhone);
                viewHolder.tvAddress=convertView.findViewById(R.id.tvAddress);
                viewHolder.tvEdit=convertView.findViewById(R.id.tvEdit);
                viewHolder.tvDel=convertView.findViewById(R.id.tvDel);
                viewHolder.llMoRen=convertView.findViewById(R.id.llMoRen);
                viewHolder.ivMoRen=convertView.findViewById(R.id.ivMoRen);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            AddressBean addressBean = list.get(position);
            final String isdefault=addressBean.getIsdefaultStr();

            if (!TextUtils.isEmpty(isdefault)){
                if (isdefault.equals("1")){
                    xx=position;
                    viewHolder.ivMoRen.setImageResource(R.mipmap.selected);
                }else {
                    viewHolder.ivMoRen.setImageResource(R.mipmap.not_select);
                }
            }
            viewHolder.tvName.setText(addressBean.getReceiver());
            viewHolder.tvPhone.setText(addressBean.getPhone());
            viewHolder.tvAddress.setText(addressBean.getAddress()+addressBean.getAddressDetail());
            viewHolder.tvDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlertDialog(list.get(position).getIdStr(),position);
                }
            });
            viewHolder.tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, ModifyAddressActivity.class);
                    intent.putExtra("name",list.get(position).getReceiver());
                    intent.putExtra("phone",list.get(position).getPhone());
                    intent.putExtra("address",list.get(position).getAddress());
                    intent.putExtra("addressDetail",list.get(position).getAddressDetail());
                    intent.putExtra("addressId",list.get(position).getIdStr());
                    intent.putExtra("isDefault",list.get(position).getIsdefaultStr());
                    context.startActivity(intent);
                }
            });
            viewHolder.llMoRen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isdefault.equals("0")){
                        list.get(position).setIsdefaultStr("1");
                        viewHolder.ivMoRen.setImageResource(R.mipmap.selected);
                        httpSetDefault(BaseRequesUrl.uesrId,list.get(position).getIdStr());
                        if (xx!=position){
                            list.get(xx).setIsdefaultStr("0");
                        }
                        notifyDataSetChanged();
                    }

                }
            });
            return convertView;
        }
        class ViewHolder{
            TextView tvName,tvPhone,tvAddress,tvEdit,tvDel;
            LinearLayout llMoRen;
            ImageView ivMoRen;
        }

        /**
         * 自定义dialog提示框
         *
         * @param
         */
        public void showAlertDialog(final String addressId, final int position)
        {
            final CustomDialogView dialog = new CustomDialogView(context);
            dialog.setTitle("提示信息");
            dialog.setMessage("确认删除该地址?");
            dialog.setYesOnclickListener("确定", new CustomDialogView.onYesOnclickListener() {
                @Override
                public void onYesClick() {
                    httpDelAddress(addressId);
                    if (addressBeanList!=null){
                        addressBeanList.remove(position);
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
