package com.motorbike.anqi.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.motorbike.anqi.R;
import com.motorbike.anqi.activity.my.MessageDetailActivity;
import com.motorbike.anqi.bean.MsgListBean;
import com.motorbike.anqi.bean.PrivateLeterBean;
import com.motorbike.anqi.init.BaseFragment;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.util.UserPreference;
import com.motorbike.anqi.view.CircleImageView;
import com.motorbike.anqi.view.SlideListView;
import com.motorbike.anqi.view.XToast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.motorbike.anqi.view.SlideListView.MOD_FORBID;
import static com.motorbike.anqi.view.SlideListView.MOD_RIGHT;

/**
 * Created by Administrator on 2018/1/30.
 * 系统消息
 */

public class SystemMsgFragment extends BaseFragment implements AbsListView.OnScrollListener, SlideListView.ILoadListener {
    private SlideListView slideListView;
    private SystemMsgAdapter adapter;
    private Map<String,Object> msgMap,delMap;
    private UserPreference preference;
    private List<MsgListBean> dataList=new ArrayList<>();
    private int index=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.privateleter_layout, container, false);
        preference=UserPreference.getUserPreference(getActivity());
        http(preference.getUserId(),index+"","10");
        initView(view);
        return view;
    }

    private void initView(View view)
    {
        slideListView=view.findViewById(R.id.slideListView);
        slideListView.initSlideMode(MOD_RIGHT);
        adapter=new SystemMsgAdapter(getActivity());
        slideListView.setAdapter(adapter);
        slideListView.setOnILoadListener(this);
        slideListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), MessageDetailActivity.class);
                intent.putExtra("msgId",dataList.get(position).getId());
                startActivity(intent);
            }
        });

    }
    private void http(String userId,String pageNum,String pageCount){
        showLoading();
        msgMap=new HashMap<>();
        if (msgMap!=null){
            msgMap.clear();
        }
        msgMap.put("userId",userId);
        msgMap.put("pageNum",pageNum);
        msgMap.put("pageCount",pageCount);
        okHttp( BaseRequesUrl.MsgList, HttpTagUtil.MsgList,msgMap);
    }
    private void delMsg(String id){
        showLoading();
        delMap=new HashMap<>();
        if (delMap!=null){
            delMap.clear();
        }
        delMap.put("messageId",id);
        okHttp(BaseRequesUrl.DelMsg,HttpTagUtil.DelMsg,delMap);
    }
    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag){
            case HttpTagUtil.MsgList:
                dismissLoading();
                if (data!=null){
                    List<MsgListBean> msgListBean=new Gson().fromJson(data,new TypeToken<List<MsgListBean>>(){}.getType() );
                    if (msgListBean!=null&&msgListBean.size()>0){
                        Log.d("aaa",msgListBean.size()+" msg  size");
                        if (index==0){
                            dataList.clear();
                        }
                        if (index>0){
                            if (dataList.size()==0){
                                showToast("没有更多内容");
                            }
                        }
                        dataList.addAll(msgListBean);
                        adapter.setBeanList(dataList);
                        adapter.notifyDataSetChanged();
                    }
                }
                slideListView.loadFinish();
                break;
            case HttpTagUtil.DelMsg:
                dismissLoading();
                XToast.custom(mag);
                slideListView.slideBack();
                break;

        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        slideListView.loadFinish();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        slideListView.loadFinish();
    }

    @Override
    public void loadData() {
        //获得加载数据
        index=(dataList.size()+9)/10;//补全算法
        http(preference.getUserId(),index+"","10");
        //然后通知MyListView刷新界面
        adapter.notifyDataSetChanged();

        //然后通知加载数据已经完成了
        slideListView.loadFinish();
    }


    private class SystemMsgAdapter extends BaseAdapter {
        private Context context;
        private List<MsgListBean> beanList;
        public SystemMsgAdapter(Context context) {
            this.context = context;
        }
        public void setBeanList(List<MsgListBean> list){
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView==null){
                viewHolder=new ViewHolder();
                convertView=LayoutInflater.from(context).inflate(R.layout.systemmsg_item_layout,parent,false);
                viewHolder.tvDate=convertView.findViewById(R.id.tvDate);
                viewHolder.tvContent=convertView.findViewById(R.id.tvContent);
                viewHolder.ivImage=convertView.findViewById(R.id.ivImage);
                viewHolder.tvDel=convertView.findViewById(R.id.tvDel);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            if (beanList!=null){
                viewHolder.tvDate.setText(beanList.get(position).getMessageTime());
                viewHolder.tvContent.setText(beanList.get(position).getMessageContent());
            }
            viewHolder.tvDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delMsg(beanList.get(position).getId());
                    beanList.remove(position);
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        public class ViewHolder{
            TextView tvDate,tvContent,tvName,tvDel;
            ImageView ivImage;
        }
    }
}
