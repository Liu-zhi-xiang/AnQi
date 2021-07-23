package com.motorbike.anqi.activity.loin;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.motorbike.anqi.MainActivityTwo;
import com.motorbike.anqi.R;
import com.motorbike.anqi.bean.GuideBean;
import com.motorbike.anqi.init.GuideReques;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.interfaces.HttpAysnResultInterface;
import com.motorbike.anqi.interfaces.HttpRequestTag;

import com.motorbike.anqi.view.CustomProgressDialog;
import com.motorbike.anqi.view.RectIndicator;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class GuideActivity extends FragmentActivity implements View.OnClickListener, HttpAysnResultInterface {
    private ViewPager viewPager;
    private TextView tv_time;
    private List<RelativeLayout> mImageLists;
    private RelativeLayout activity_guide;
    private RectIndicator rectIndicator;
    public CustomProgressDialog customProgressDialog;
    private List<GuideBean.DataBean> dataBeanList;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        http();
        ViewPage();
    }


    private void initView() {
        mImageLists = new ArrayList<RelativeLayout>();
        viewPager = findViewById(R.id.viewPager);
        rectIndicator = findViewById(R.id.rectIndicator);
        tv_time = findViewById(R.id.tv_time);
        tv_time.setOnClickListener(this);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        customProgressDialog =new  CustomProgressDialog(this);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customProgressDialog != null) {
            customProgressDialog.dismiss();
        }
    }

    private void http() {
        showLoading();
        GuideReques guideReques = new GuideReques(this, HttpTagUtil.GuidePage, this);
        guideReques.addPoicomment();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_time://跳过
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.popshow_anim, R.anim.pophidden_anim);
                finish();
                break;
        }
    }



    private void ViewPage() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (isNetworkAvailable(GuideActivity.this)) {
                    if (dataBeanList.size() == (position + 1)) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.popshow_anim, R.anim.pophidden_anim);
                                finish();
                            }
                        }, 1000);

                    }
                } else {
//                    if (ids.length==(position+1))
//                    {
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                Intent intent=new Intent(GuideActivity.this,MainActivity.class);
//                                startActivity(intent);
//                                overridePendingTransition(R.anim.popshow_anim, R.anim.pophidden_anim);
//                                finish();
//                            }
//                        }, 1000);
//
//                    }
                }

            }

            @Override
            public void onPageSelected(int position) {

            }


            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    public void showLoading() {
        if (customProgressDialog != null && !customProgressDialog.isShowing()) {
//            customProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            customProgressDialog.show();
        }
    }

    public void dismissLoading() {
        if (customProgressDialog != null && customProgressDialog.isShowing()) {
            customProgressDialog.dismiss();
        }
    }

    /**
     * 检查当前网络是否可用
     *
     * @param
     * @return
     */
    public boolean isNetworkAvailable(Activity activity)
    {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void dataCallBack(Object tag, Object result) {
        switch ((Integer) tag) {
            case HttpTagUtil.GuidePage:
                dismissLoading();
                if (result != null) {
                    GuideBean guideBean = (GuideBean) result;
                    if (guideBean != null) {
                        dataBeanList = guideBean.getData();
                        Log.e("vvvv", dataBeanList.size() + "");
                        for (int i = 0; i < dataBeanList.size(); i++) {
                            activity_guide = new RelativeLayout(GuideActivity.this);
                            imageView = new ImageView(GuideActivity.this);
                            try {

                                Picasso.with(this)
                                        .load(dataBeanList.get(i).getImgUrl())
                                        .config(Bitmap.Config.RGB_565)
                                        .centerCrop()
                                        .fit()
                                        .networkPolicy(NetworkPolicy.NO_STORE)
                                        .into(imageView);
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            }
                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            activity_guide.addView(imageView, lp);

                            if (i == dataBeanList.size() - 1) {
                                activity_guide.setOnClickListener(GuideActivity.this);
                            }
                            mImageLists.add(activity_guide);
                        }
                        ViewPagerAdapter adapter = new ViewPagerAdapter();
                        viewPager.setAdapter(adapter);
                    }

                }
                break;
        }
    }
    private class ViewPagerAdapter extends PagerAdapter {
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            container.addView(mImageLists.get(position));

            return mImageLists.get(position);
        }

        @Override
        public int getCount() {
            return mImageLists.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }





}
