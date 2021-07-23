package com.motorbike.anqi.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.motorbike.anqi.R;
import com.motorbike.anqi.bean.PrivateLeterBean;
import com.motorbike.anqi.bean.StorkeRecordBean;
import com.motorbike.anqi.bean.UserCenterBean;
import com.motorbike.anqi.bean.UsreBean;
import com.motorbike.anqi.greendao.gen.DBManager;
import com.motorbike.anqi.init.BaseFragment;
import com.motorbike.anqi.init.HimInfoHttpReques;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.interfaces.HttpAysnResultInterface;
import com.motorbike.anqi.interfaces.HttpAysnTaskInterface;
import com.motorbike.anqi.util.TimeUtils;
import com.motorbike.anqi.view.CircleImageView;
import com.motorbike.anqi.view.SlideListView;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

import static com.motorbike.anqi.view.SlideListView.MOD_FORBID;
import static com.motorbike.anqi.view.SlideListView.MOD_RIGHT;

/**
 * Created by Administrator on 2018/1/30.
 * 私信
 */

public class PrivateLeterFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private SlideListView slideListView;
    private PrivateLeterAdapter adapter;
    private List<Conversation> conversationList = new ArrayList<Conversation>();
    BroadcastReceiver bReceiver;
    IntentFilter iFilter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.privateleter_layout, container, false);
        iFilter = new IntentFilter();
        iFilter.addAction("myMessage");
        iFilter.setPriority(100);
        bReceiver = new ReceiveBroadCast();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(bReceiver, iFilter);
        initView(view);
        return view;
    }
    private void getData()
    {
        if (RongIM.getInstance()!=null){
            RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                @Override
                public void onSuccess(List<Conversation> conversations) {
                    if (conversations!=null&&conversations.size()>0){
                        if (conversationList!=null){
                            conversationList.clear();
                        }
                        conversationList.addAll(conversations);
                        adapter.setBeanList(conversationList);
                        adapter.notifyDataSetChanged();
                        Log.e("111111", "11111111111");
                        Log.e("111111", conversationList.size() + ">>>>>>");
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }

    }
    private void initView(View view) {
        slideListView=view.findViewById(R.id.slideListView);
        slideListView.initSlideMode(MOD_RIGHT);
        adapter=new PrivateLeterAdapter(getActivity());
        slideListView.setAdapter(adapter);
        slideListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (conversationList!=null&&conversationList.size()>position){
            String userId=conversationList.get(position).getTargetId();
            List<UsreBean> list=DBManager.getInstance(getActivity()).queryUserList(userId);
            if (list!=null&&list.size()>0){
                if (list.get(0).getUserId().equals(userId)){
                    if (RongIM.getInstance() != null){
                        RongIM.getInstance().startConversation(getActivity(), Conversation.ConversationType.PRIVATE, userId, list.get(0).getUserName());
                    }
                }
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (isVisible()){
            // 发起网络请求, 刷新界面数据
            getData();
        }
    }
    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);
        // 这里的 isResumed() 判断就是为了避免与 onResume() 方法重复发起网络请求
        if (isVisible() && isResumed()) {
            getData();
        }
        Log.e("aaaaa","home===="+hidden);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            getData();
        }
    }

    private class PrivateLeterAdapter extends BaseAdapter {
        private Context context;
        private List<Conversation> conversations;
        private String userId,userName;
        public PrivateLeterAdapter(Context context) {
            this.context = context;
        }
        public void setBeanList(List<Conversation> list){
            this.conversations=list;
        }
        @Override
        public int getCount() {
            if (conversations!=null)
                return conversations.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return conversations.get(position);
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
                convertView=LayoutInflater.from(context).inflate(R.layout.privateleter_item_layout,parent,false);
                viewHolder.tvDate=convertView.findViewById(R.id.tvDate);
                viewHolder.tvName=convertView.findViewById(R.id.tvName);
                viewHolder.tvContent=convertView.findViewById(R.id.tvContent);
                viewHolder.image=convertView.findViewById(R.id.image);
                viewHolder.tvDel=convertView.findViewById(R.id.tvDel);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            if (conversations != null) {
                viewHolder.tvName.setText(conversations.get(position).getConversationTitle());
            }
            final Conversation conversation = conversations.get(position);
            if (conversation != null) {
                String contenttext = null;
                String jsonStr = null;

                userId = conversation.getTargetId();
                List<UsreBean> userInfoList = null;
                try {
                    userInfoList = DBManager.getInstance(context).queryUserList(userId);
                } catch (NumberFormatException e) {
                    e.printStackTrace();

                }
                if (userInfoList != null&&userInfoList.size()>0) {
                    UsreBean usreBean = userInfoList.get(0);
                    if (usreBean.getUserId().equals(userId)) {
                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(userId, usreBean.getUserName(), Uri.parse(usreBean.getHeanImg())));
                        //清理缓存
                        Picasso.with(context).invalidate(new File(usreBean.getHeanImg()));
                        Picasso.with(context)
                                .load(usreBean.getHeanImg())
                                .config(Bitmap.Config.RGB_565)
                                .centerCrop()
                                .fit()
                                .networkPolicy(NetworkPolicy.NO_STORE)
                                .into(viewHolder.image);
                        if (usreBean.getUserName() == null) {
                            viewHolder.tvName.setText("未知好友");
                        } else {
                            viewHolder.tvName.setText(usreBean.getUserName());
                        }
                        userName = usreBean.getUserName();
                    }
                }else {
                    new HimInfoHttpReques(context, HttpTagUtil.PersonalCenter, new HttpAysnTaskInterface() {
                        @Override
                        public void requestComplete(Object tag, Object result, boolean complete) {
                            if (complete&&result!=null)
                            {
                                UserCenterBean usersBean= (UserCenterBean) result;
                                RongIM.getInstance().refreshUserInfoCache(new UserInfo(usersBean.getUserId(), usersBean.getNickname(), Uri.parse(usersBean.getHeaderImg() + "")));
                                //将返回的信息保存至数据库
                                DBManager.getInstance(context).insertUser(new UsreBean(usersBean.getNickname(), usersBean.getUserId(), usersBean.getHeaderImg() + ""));
                                notifyDataSetChanged();
                            }
                        }
                    }).getModify(userId);
                }
                String objectName = conversation.getObjectName();
//                viewHolder.tvName.setText(TimeUtils.convertTimeToFormat(conversation.getSentTime()));
                if (objectName.equals("RC:ImgMsg")) {
                    contenttext = "[图片]";
                } else if (objectName.equals("RC:TxtMsg")) {
                    byte[] encode = conversation.getLatestMessage().encode();
                    try {
                        jsonStr = new String(encode, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        if (jsonObj.has("content"))
                            contenttext = jsonObj.optString("content");
                    } catch (JSONException e) {

                    }
                } else if (objectName.equals("RC:VcMsg")) {
                    contenttext = "[语音]";
                } else if (objectName.equals("RC:LBSMsg")) {
                    contenttext = "[地理位置]";
                } else {
                    contenttext = "[...]";
                }
                if (contenttext != null) {
                    viewHolder.tvContent.setText(contenttext);
                }

            }
            viewHolder.tvDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    conversations.remove(position);
                    RongIMClient.getInstance().removeConversation(Conversation.ConversationType.PRIVATE, conversation.getTargetId(), null);
                    notifyDataSetChanged();
                    slideListView.slideBack();
                }
            });
            return convertView;
        }

        public class ViewHolder{
            TextView tvDate,tvContent,tvName,tvDel;
            CircleImageView image;
        }


    }

    class ReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String maa=intent.getStringExtra("message");
            Log.e("xiaoxi","maa=="+maa);
            if (!TextUtils.isEmpty(maa)) {
                if (adapter != null) {
                    getData();
                }
            }
        }
    }
}
