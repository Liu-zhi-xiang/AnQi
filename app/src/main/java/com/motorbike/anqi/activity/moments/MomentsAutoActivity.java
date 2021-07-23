package com.motorbike.anqi.activity.moments;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.motorbike.anqi.R;
import com.motorbike.anqi.activity.my.DynamicActivity;
import com.motorbike.anqi.bean.CheyouDetailsBean;
import com.motorbike.anqi.bean.CheyouquanBean;
import com.motorbike.anqi.bean.ForumBean;
import com.motorbike.anqi.bean.NoteImgListBean;
import com.motorbike.anqi.bean.ReplyListBean;
import com.motorbike.anqi.bean.TalkListBean;
import com.motorbike.anqi.bean.Voiceroom;
import com.motorbike.anqi.beseAdapter.CommonAdapter;
import com.motorbike.anqi.beseAdapter.MultiItemTypeAdapter;
import com.motorbike.anqi.beseAdapter.base.ItemViewDelegate;
import com.motorbike.anqi.beseAdapter.base.ViewHolder;
import com.motorbike.anqi.handler.BaseHandlerOperate;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.main.AutoMomentsActivity;
import com.motorbike.anqi.view.RoundedImageView;
import com.motorbike.anqi.view.bgabanner.BGABanner;
import com.motorbike.anqi.xrecyclerview.XRecyclerView;
import com.motorbike.anqi.xrecyclerview.progressindicator.ProgressStyle;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 车友圈详情
 */
public class MomentsAutoActivity extends BaseActivity implements View.OnClickListener {
    private XRecyclerView xRecyclerView;
    private ImageView zanImg;
    private TextView zanTv;
    private MultiItemTypeAdapter adapter;
    private List<ForumBean> forumBeanList;
    private LinearLayout editLayout,pinglinLayout,pinglunAndzanLayout;
    private EditText edittext;
    private CheyouquanBean cheyouquanBean;
    private String puStr,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moments_auto);
        cheyouquanBean=getIntent().getParcelableExtra("data");
        type=getIntent().getStringExtra("type");
        initView();
        if (cheyouquanBean!=null){
            if (cheyouquanBean.getIszan().equals("1")){
                zanImg.setBackgroundResource(R.mipmap.zan);
            }else {
                zanImg.setBackgroundResource(R.mipmap.zan_an);
            }
            zanTv.setText(cheyouquanBean.getZan()+"");
            showLoading();
            requestData(cheyouquanBean.getPostId());
        }
    }

    private void initView()
    {
//        View headview= LayoutInflater.from(this).inflate(R.layout.monents_auto_recycle_head,null);
//        xRecyclerView.addHeaderView(headview);
        zanImg=findViewById(R.id.xq_zan_img);
        zanTv=findViewById(R.id.xq_zan_tv);
        findViewById(R.id.xq_zan_ll).setOnClickListener(this);
        findViewById(R.id.jubao_ll).setOnClickListener(this);
        editLayout=  findViewById(R.id.edit_layout);
        findViewById(R.id.send_layout).setOnClickListener(this);
        pinglunAndzanLayout= findViewById(R.id.pinglun_and_zan);
        findViewById(R.id.pinglun_layout).setOnClickListener(this);
        findViewById(R.id.back_layout).setOnClickListener(this);
        edittext= findViewById(R.id.message_edit);

        xRecyclerView= findViewById(R.id.moments_auto_xrecyclerview);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(false);

        forumBeanList=new ArrayList<>();

        adapter=new MultiItemTypeAdapter(this,forumBeanList);
        adapter.addItemViewDelegate(new MsgFourumItemDelagate());
        adapter.addItemViewDelegate(new MsgFourumItemDelagateOne());
        adapter.addItemViewDelegate(new MsgFourumItemDelagateTwo());
        xRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                puStr=s.toString();

            }
        });
        xRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (iskai){
                    iskai=false;
                    hiedEdit();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }
    private void  requestData(String noteId){
        Map<String,Object> map=new ArrayMap<>();
        map.put("userId", BaseRequesUrl.uesrId);
        map.put("noteId",noteId);
        okHttp(this,BaseRequesUrl.CYQ_DETAILs, HttpTagUtil.CYQ_XQ,map);
    }

    private int jiLuPosition;
    private int onClickType=0;
    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.back_layout:
                finish();
                break;
            case R.id.pinglun_layout:
                    if (!iskai) {
                        popupEdit();
                        onClickType = 0;
                        iskai = true;
                    } else {
                        hiedEdit();
                    }
                break;
            case R.id.xq_zan_ll:
                if (cheyouDetailsBean!=null) {
                    int shu = Integer.valueOf(cheyouDetailsBean.getZan());
                    if (cheyouDetailsBean.getIszan().equals("1")) {
                        zanImg.setBackgroundResource(R.mipmap.zan_an);
                        cheyouDetailsBean.setIszan("0");
                        if (shu>0) {
                            cheyouDetailsBean.setZan((shu - 1) + "");
                        }else {
                            cheyouDetailsBean.setZan("0");
                        }
                        requestZan(cheyouquanBean.getPostId() + "", "1");
                        BaseHandlerOperate.getBaseHandlerOperate().putMessageKey(DynamicActivity.class,HttpTagUtil.CYQ_ZAN,"0");
                    } else {
                        zanImg.setBackgroundResource(R.mipmap.zan);
                        cheyouDetailsBean.setIszan("1");
                        cheyouDetailsBean.setZan((shu + 1) + "");
                        requestZan(cheyouquanBean.getPostId() + "", "0");
                        BaseHandlerOperate.getBaseHandlerOperate().putMessageKey(DynamicActivity.class,HttpTagUtil.CYQ_ZAN,"1");
                    }
                    zanTv.setText(cheyouDetailsBean.getZan() + "");
                }
                break;
            case  R.id.send_layout:
                if (!TextUtils.isEmpty(puStr)){
                    serConter=puStr;
                    hiedEdit();
                    showLoading();
                    if (onClickType == 0){
                        fabupinglun(cheyouquanBean.getPostId(), serConter);
                    }else if (onClickType==2){
                        String aa=forumBeanList.get(jiLuPosition - 1).getTalk();
                        ReplyListBean talksBean = forumBeanList.get(jiLuPosition - 1).getTalksBean();
                        if (talksBean!=null){
                            huifupinglun(aa, talksBean.getFromUid(), serConter);
                        }
                    }else if (onClickType == 1){
                        TalkListBean luntanBean = forumBeanList.get(jiLuPosition - 1).getLuntanBean();
                        if (luntanBean!=null)
                        {
                            huifupinglun(luntanBean.getTalkId(), luntanBean.getFromUid(), serConter);
                        }
                    }
                } else {
                    prompt("请输入内容");
                }
                break;
            case R.id.jubao_ll:
                showAlertDialog("提示","举报该条信息？");
                break;
        }
    }
    private CheyouDetailsBean cheyouDetailsBean;


    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp)
    {

        dismissLoading();
        if (!isHttp){
            prompt(mag);
            return;
        }
        switch (mTag){
            case HttpTagUtil.CYQ_XQ:
                cheyouDetailsBean=new Gson().fromJson(data ,CheyouDetailsBean.class);
                if (cheyouDetailsBean.getIszan().equals("1")){
                    zanImg.setBackgroundResource(R.mipmap.zan);
                }else {
                    zanImg.setBackgroundResource(R.mipmap.zan_an);
                }
                zanTv.setText(cheyouDetailsBean.getZan()+"");
                sortOutData(cheyouDetailsBean,0);
                break;
            case HttpTagUtil.CYQ_Pl:
                    if (!TextUtils.isEmpty(data)){
                        TalkListBean TalkListBean = new TalkListBean(BaseRequesUrl.uesrHead
                                , BaseRequesUrl.uesrName
                                , serConter
                                , 0
                                , "刚刚"
                                , data
                                , BaseRequesUrl.uesrId
                                , ""
                                , null);
                        forumBeanList.add(new ForumBean(1, null, TalkListBean, null, "0",0));
                        adapter.notifyDataSetChanged();
                    }
                BaseHandlerOperate.getBaseHandlerOperate().putMessageKey(DynamicActivity.class,HttpTagUtil.CYQ_PlHF,(forumBeanList.size()-1)+"");
                break;
            case HttpTagUtil.CYQ_PlHF:
                if (onClickType==1)
                {
                    TalkListBean luntanBean = forumBeanList.get(jiLuPosition - 1).getLuntanBean();
                    ReplyListBean replyListBean=new ReplyListBean(BaseRequesUrl.uesrHead
                            ,BaseRequesUrl.uesrName
                            ,serConter
                            ,"刚刚"
                            ,luntanBean.getTalkId()
                            ,BaseRequesUrl.uesrId
                            ,luntanBean.getFromUid()
                            ,luntanBean.getNickname());
                    int jj=-1;
                    if (forumBeanList.size()>jiLuPosition) {
                        for (int z = jiLuPosition ; z < forumBeanList.size(); z++) {
                            ForumBean forumBean = forumBeanList.get(z);
                            if (forumBean.getType() == 1) {
                                jj = z;
                                break;
                            }
                        }
                    }
                    if (jj==-1) {
                        forumBeanList.add(new ForumBean(2, null, null, replyListBean, luntanBean.getTalkId(), 0));
                    }else {
                        forumBeanList.add(jj,new ForumBean(2, null, null, replyListBean, luntanBean.getTalkId(), 0));
                    }
                }else if (onClickType==2)
                {
                    String id= forumBeanList.get(jiLuPosition - 1).getTalk();
                    ReplyListBean replyL = forumBeanList.get(jiLuPosition - 1).getTalksBean();
                    ReplyListBean replyListBean=new ReplyListBean(BaseRequesUrl.uesrHead
                            ,BaseRequesUrl.uesrName
                            ,serConter
                            ,"刚刚"
                            ,id
                            ,BaseRequesUrl.uesrId
                            ,replyL.getFromUid()
                            ,replyL.getNickname());
                    int jj=-1;
                    if (forumBeanList.size()>jiLuPosition) {
                        for (int z = jiLuPosition; z < forumBeanList.size(); z++) {
                            ForumBean forumBean = forumBeanList.get(z);
                            if (forumBean.getType() == 1) {
                                jj = z;
                                break;
                            }
                        }
                    }
                    if ( jj==-1){
                        forumBeanList.add(new ForumBean(2, null, null, replyListBean, id, 0));
                    }else {
                        forumBeanList.add(jj,new ForumBean(2, null, null, replyListBean, id, 0));
                    }
                }
                BaseHandlerOperate.getBaseHandlerOperate().putMessageKey(DynamicActivity.class,HttpTagUtil.CYQ_PlHF,(forumBeanList.size()-1)+"");
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void onclickDialog() {
        super.onclickDialog();
        jubao();
    }

    private void sortOutData(CheyouDetailsBean detailsBean, int pase)
    {
        if (detailsBean==null){
            return;
        }
        if (pase==0){
            if (forumBeanList==null){
                forumBeanList=new ArrayList<>();
            }else {
                forumBeanList.clear();
            }
        }
        String talkNum=detailsBean.getTalk();
        cheyouquanBean.setTalk(talkNum);
        forumBeanList.add(new ForumBean(0,cheyouquanBean,null,null,talkNum,0));
        if (!TextUtils.isEmpty(talkNum)&&!talkNum.equals("0")) {
            List<TalkListBean> talkList = detailsBean.getTalkList();
            for (int x=0;x<talkList.size();x++)
            {
                int wwezi=forumBeanList.size();
                TalkListBean talkListBean= talkList.get(x);
                String id=talkListBean.getTalkId();
                forumBeanList.add(new ForumBean(1,null,talkListBean,null,id,wwezi+1));
                List<ReplyListBean> replyList = talkListBean.getReplyList();
                if (replyList!=null&&replyList.size()>0){
                    for (int z=0;z<replyList.size();z++){
                        forumBeanList.add(new ForumBean(2,null,null,replyList.get(z),id,wwezi));
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public class MsgFourumItemDelagate implements ItemViewDelegate<ForumBean>
    {

        @Override
        public int getItemViewLayoutId(){
            return R.layout.monents_auto_recycle_head;
        }

        @Override
        public boolean isForViewType(ForumBean item, int position) {
            return item.getType() == 0;
        }
        private View getData(final String xxx)
        {
            if (TextUtils.isEmpty(xxx))
            {
                return null;
            }
           final RoundedImageView  roundedImageView= (RoundedImageView) LayoutInflater.from(MomentsAutoActivity.this).inflate(R.layout.lunbolaayout,null);
            Picasso.with(getBaseContext())
                    .load(xxx)
                    .centerCrop()
                    .error(R.mipmap.dt_bg)
                    .fit()
                    .into(roundedImageView);
//            roundedImageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        Intent intent=new Intent(MomentsAutoActivity.this,PhotoActivity.class);
//                        intent.putExtra("url",xxx);
//                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MomentsAutoActivity.this,roundedImageView,"myimg").toBundle());
//                    }else {
//                        Intent intent=new Intent(MomentsAutoActivity.this,PhotoActivity.class);
//                        intent.putExtra("url",xxx);
//                        startActivity(intent);
//                    }
//                }
//            });
            return roundedImageView;
        }
        @Override
        public void convert(final ViewHolder holder,final ForumBean chatMessage, final int position) {
          final   CheyouquanBean cheyouquanBean=chatMessage.getCheyouquanBean();
            if (cheyouquanBean!=null) {
                TextView textView = holder.getView(R.id.content_tv);
                textView.setText(cheyouquanBean.getContent());
                TextView textViewtwo = holder.getView(R.id.time_tv);
                textViewtwo.setText(cheyouquanBean.getPublishTimeStr());
                TextView textViewthree = holder.getView(R.id.name_tv);
                textViewthree.setText(cheyouquanBean.getNickname());
                TextView textViewfour = holder.getView(R.id.city_tv);
                textViewfour.setText(cheyouquanBean.getCity());
                ImageView headimg = holder.getView(R.id.head_img);

                List<NoteImgListBean> noteImgList = cheyouquanBean.getNoteImgList();
                BGABanner banner = holder.getView(R.id.banner_cheyouq);
                if (noteImgList != null && noteImgList.size() > 0) {
                    List<View> views = new ArrayList<>();
                    for (int x = 0; x < noteImgList.size(); x++) {
                        if (!TextUtils.isEmpty(noteImgList.get(x).getImgUrl())) {
                            views.add(getData(noteImgList.get(x).getImgUrl()));
                        }
                    }
                    banner.setAutoPlayAble(true);
                    banner.setData(views);
                    banner.setDelegate(new BGABanner.Delegate<ImageView, String>() {
                        @Override
                        public void onBannerItemClick(BGABanner banner, ImageView itemView, String model, int position) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Intent intent = new Intent(MomentsAutoActivity.this, PhotoActivity.class);
                                intent.putExtra("url", cheyouquanBean.getNoteImgList().get(position).getImgUrl());
                                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MomentsAutoActivity.this, itemView, "myimg").toBundle());
                            } else {
                                Intent intent = new Intent(MomentsAutoActivity.this, PhotoActivity.class);
                                intent.putExtra("url", cheyouquanBean.getNoteImgList().get(position).getImgUrl());
                                startActivity(intent);
                            }
                        }
                    });
                    if (!TextUtils.isEmpty(cheyouquanBean.getHeaderImg())) {
                        Picasso.with(getBaseContext())
                                .load(cheyouquanBean.getHeaderImg())
                                .centerCrop()
                                .config(Bitmap.Config.RGB_565)
                                .fit()
                                .into(headimg);
                    }
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("aaaa", "========" + position);
                            jiLuPosition = position;
                            onClickType = 0;
                        }
                    });
                } else {
                    banner.setVisibility(View.GONE);
                }
            }
        }
    }
    public class MsgFourumItemDelagateOne implements ItemViewDelegate<ForumBean>
    {

        @Override
        public int getItemViewLayoutId()
        {
            return R.layout.auto_pinglin_item;
        }

        @Override
        public boolean isForViewType(ForumBean item, int position) {
            return item.getType() == 1;
        }

        @Override
        public void convert(final ViewHolder holder,final ForumBean chatMessage, final int position) {
            final TalkListBean luntanBean = chatMessage.getLuntanBean();
            View pxview=holder.getView(R.id.view);
            if (position==2&&forumBeanList.size()==position) {
                pxview.setVisibility(View.GONE);
                holder.itemView.setBackgroundResource(R.drawable.baise_bg_yuan);
            }else if (position==2){
                pxview.setVisibility(View.GONE);
                holder.itemView.setBackgroundResource(R.drawable.baise_bg_yuan_shang);
            }else if (forumBeanList.size()==position){
                pxview.setVisibility(View.GONE);
                holder.itemView.setBackgroundResource(R.drawable.baise_bg_yuan_xia);
            }else {
                pxview.setVisibility(View.VISIBLE);
                holder.itemView.setBackgroundResource(R.color.base_bg_yuan);
            }
            if (luntanBean!=null) {
                TextView textView = holder.getView(R.id.pl_name_tv);
                textView.setText(luntanBean.getNickname());
                TextView textViewtwo = holder.getView(R.id.pl_time_tv);
                textViewtwo.setText(luntanBean.getPublishTimeStr());
                TextView textViewthree = holder.getView(R.id.pl_content_tv);
                textViewthree.setText(luntanBean.getTalkContent());
                ImageView headimg=holder.getView(R.id.pl_head_iv);
                Picasso.with(getBaseContext())
                        .load(luntanBean.getHeaderImg())
                        .centerCrop()
                        .resize(90,90)
                        .into(headimg);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("aaaa", "========" + position);
                        if (!luntanBean.getFromUid().equals(BaseRequesUrl.uesrId)) {
                            jiLuPosition = position;
                            onClickType = 1;
                            if (!iskai) {
                                popupEdit();
                                iskai = true;
                            }
                        }else {
                            prompt("不能回复自己");
                        }
                    }
                });
            }
        }

    }
    public class MsgFourumItemDelagateTwo implements ItemViewDelegate<ForumBean>
    {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.auto_huifu_pinglin_item;
        }

        @Override
        public boolean isForViewType(ForumBean item, int position) {
            return item.getType() == 2;
        }

        @Override
        public void convert(final ViewHolder holder,final ForumBean chatMessage, final int position) {
           final ReplyListBean talksBean = chatMessage.getTalksBean();
            if (forumBeanList.size()==position){
                holder.itemView.setBackgroundResource(R.drawable.baise_bg_yuan_xia);
            }else {
                holder.itemView.setBackgroundResource(R.color.base_bg_yuan);
            }
            if (talksBean!=null){
                TextView textView = holder.getView(R.id._pl_name_tv);
                textView.setText(talksBean.getNickname());
                TextView text = holder.getView(R.id.b_pl_name_tv);
                text.setText(talksBean.getToreplyName());
                TextView textViewtwo = holder.getView(R.id._pl_time_tv);
                textViewtwo.setText(talksBean.getPublishTimeStr());
                TextView textViewthree = holder.getView(R.id._pl_content_tv);
                textViewthree.setText(talksBean.getContent());
                ImageView headimg=holder.getView(R.id.b_pl_head_iv);
                Picasso.with(getBaseContext())
                        .load(talksBean.getHeaderImg())
                        .centerCrop()
                        .resize(90,90)
                        .into(headimg);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("aaaa","========"+position);
                        if (!talksBean.getFromUid().equals(BaseRequesUrl.uesrId)) {
                        jiLuPosition=position;
                        onClickType=2;
                        if (!iskai){
                            popupEdit();
                            iskai=true;
                        }
                        }else {
                            prompt("不能回复自己");
                        }
                    }
                });
            }
        }

    }

    private boolean  iskai=false;
    private String serConter;
    private void jubao()
    {
        if (cheyouquanBean!=null)
        {
            Map<String, Object> map = new ArrayMap<>();
            map.put("userId", BaseRequesUrl.uesrId);
            map.put("noteId", cheyouquanBean.getPostId());
            okHttp(this, BaseRequesUrl.jubao, HttpTagUtil.JUBAO, map);
        }
    }
    private void fabupinglun(String noteId,String content)
    {
        Map<String,Object> map=new ArrayMap<>();
        map.put("userId", BaseRequesUrl.uesrId);
        map.put("noteId",noteId);
        map.put("content",content);
        okHttp(this,BaseRequesUrl.CYQ_PINGLUN, HttpTagUtil.CYQ_Pl,map);
    }
    private void huifupinglun(String talkId,String replyUseId,String content)
    {
        Map<String,Object> map=new ArrayMap<>();
        map.put("userId", BaseRequesUrl.uesrId);
        map.put("talkId",talkId);
        map.put("replyUseId",replyUseId);
        map.put("content",content);
        okHttp(this,BaseRequesUrl.CYQ_HF_PINGLUN, HttpTagUtil.CYQ_PlHF,map);
    }
    private void requestZan(String noteId,String zanType)
    {
        Map<String,Object> map=new ArrayMap<>();
        map.put("userId", BaseRequesUrl.uesrId);
        map.put("noteId",noteId);
        map.put("zanType",zanType);
        okHttp(this,BaseRequesUrl.CYQ_Zan, HttpTagUtil.CYQ_ZAN,map);
    }

    private void popupEdit()
    {
        editLayout.setVisibility(View.VISIBLE);
        pinglunAndzanLayout.setVisibility(View.GONE);
        edittext.setFocusable(true);
        edittext.setFocusableInTouchMode(true);
        edittext.requestFocus();
        edittext.findFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edittext, InputMethodManager.SHOW_FORCED);// 显示输入法

    }
    private void hiedEdit()
    {
        edittext.setText("");
        iskai=false;
        edittext.setFocusable(false);
        editLayout.setVisibility(View.GONE);
        pinglunAndzanLayout.setVisibility(View.VISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

    }
}
