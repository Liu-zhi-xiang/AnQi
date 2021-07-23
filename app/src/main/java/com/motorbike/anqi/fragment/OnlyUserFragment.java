package com.motorbike.anqi.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.motorbike.anqi.R;
import com.motorbike.anqi.activity.trip.TripDetailActivity;
import com.motorbike.anqi.bean.OwnTripListBean;
import com.motorbike.anqi.bean.StorkeRecordBean;
import com.motorbike.anqi.bean.TripBean;
import com.motorbike.anqi.init.BaseFragment;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.util.UserPreference;
import com.motorbike.anqi.view.SlideListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.motorbike.anqi.view.SlideListView.MOD_FORBID;
import static com.motorbike.anqi.view.SlideListView.MOD_RIGHT;

/**
 * 个人行程
 * Created by Administrator on 2018/1/24.
 */

public class OnlyUserFragment extends BaseFragment implements AdapterView.OnItemClickListener, SlideListView.ILoadListener {
    private SlideListView slideListView;
    private OnlyUserAdapter adapter;
    private UserPreference preference;
    private Map<String,Object> oweTripMap;
    private List<OwnTripListBean> listBeans;
    private int index = 0;
    private List<OwnTripListBean> datalist=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.top_only_layout, container, false);
        preference=UserPreference.getUserPreference(getActivity());
        initView(view);
        return view;
    }

    private void initView(View view) {
        slideListView=view.findViewById(R.id.slideListView);
        slideListView.initSlideMode(MOD_FORBID);

        adapter=new OnlyUserAdapter(getActivity());
        slideListView.setAdapter(adapter);
        slideListView.setOnILoadListener(this);
        slideListView.setOnItemClickListener(this);
        httpMap(preference.getUserId(),"10",index+"");
    }

    private void httpMap(String userId,String pageCount,String pageNum){
        showLoading();
        oweTripMap=new HashMap<>();
        if (oweTripMap!=null){
            oweTripMap.clear();
        }
        oweTripMap.put("userId",userId);
        oweTripMap.put("pageCount",pageCount);
        oweTripMap.put("pageNum",pageNum);
        okHttp(BaseRequesUrl.OwnTrip, HttpTagUtil.OwnTrip,oweTripMap);
    }

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag){
            case HttpTagUtil.OwnTrip:
                if (data!=null){
                    dismissLoading();
                    TripBean tripBean=new Gson().fromJson(data,TripBean.class);
                    if (tripBean!=null){
                        listBeans=tripBean.getOwnTripList();
                        if (index==0){
                            datalist.clear();
                        }
                        if (listBeans!=null){
                            if (index>0){
                                if (datalist.size()==0){
                                    showToastTwo("没有更多内容");
                                }
                            }
                            datalist.addAll(listBeans);
                            adapter.setBeanList(datalist);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                slideListView.loadFinish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(getActivity(),TripDetailActivity.class);
        intent.putExtra("roomId",datalist.get(position).getRoomId());
        intent.putExtra("tripId",datalist.get(position).getTripId());
        intent.putExtra("type","1");
        startActivity(intent);
    }

    @Override
    public void loadData() {
        //获得加载数据
        index=(datalist.size()+9)/10;//补全算法
        httpMap(preference.getUserId(),"10",index+"");
        //然后通知MyListView刷新界面
        adapter.notifyDataSetChanged();

        //然后通知加载数据已经完成了

        slideListView.loadFinish();
    }


    private class OnlyUserAdapter extends BaseAdapter{
        private Context context;
        private List<OwnTripListBean> beanList;
        public OnlyUserAdapter(Context context) {
            this.context = context;
        }
        public void setBeanList(List<OwnTripListBean> list){
            this.beanList=list;
        }
        @Override
        public int getCount() {
            if (beanList!=null)
                return beanList.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return beanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView==null){
                viewHolder=new ViewHolder();
                convertView=LayoutInflater.from(context).inflate(R.layout.top_jilu_item,parent,false);
                viewHolder.tvDate=convertView.findViewById(R.id.tvDate);
                viewHolder.tvAddress=convertView.findViewById(R.id.tvAddress);
                viewHolder.tvNum=convertView.findViewById(R.id.tvNum);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            if (beanList!=null){
                viewHolder.tvDate.setText(beanList.get(position).getStartTime());
                viewHolder.tvAddress.setText(beanList.get(position).getStartAddress()+"-"+beanList.get(position).getEndAddress());
                viewHolder.tvNum.setText(beanList.get(position).getRidingKm());
            }
            return convertView;
        }

        public class ViewHolder{
            TextView tvDate,tvAddress,tvNum;
        }
    }
}
