package com.motorbike.anqi.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.motorbike.anqi.R;
import com.motorbike.anqi.adapter.MessagesFragmentAdapter;
import com.motorbike.anqi.fragment.AllUserFragment;
import com.motorbike.anqi.fragment.OnlyUserFragment;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.util.ScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * TOP榜单
 */
public class TopListActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llBack,llOnly,llAll;
    private TextView tvTitle,tvOnly,tvAll;
    private ImageView ivOnly,ivAll;
    private int selectpage;
    private List<Fragment> listFragment;
    private ScrollViewPager viewPager;
    private MessagesFragmentAdapter messagesFragmentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_list);
        initView();
    }

    private void initView() {
        llBack= findViewById(R.id.llBack);
        llBack.setOnClickListener(this);
        llOnly= findViewById(R.id.llOnly);
        llOnly.setOnClickListener(this);
        llAll= findViewById(R.id.llAll);
        llAll.setOnClickListener(this);
        tvTitle= findViewById(R.id.tvTitle);
        tvTitle.setText("行程记录");
        tvOnly= findViewById(R.id.tvOnly);
        tvAll= findViewById(R.id.tvAll);
        ivOnly= findViewById(R.id.ivOnly);
        ivAll= findViewById(R.id.ivAll);
        viewPager= findViewById(R.id.viewPager);


        listFragment = new ArrayList<>();
        OnlyUserFragment onlyUserFragment=new OnlyUserFragment();
        AllUserFragment allUserFragment=new AllUserFragment();
        listFragment.add(onlyUserFragment);
        listFragment.add(allUserFragment);
        FragmentManager fm = getSupportFragmentManager();
        messagesFragmentAdapter = new MessagesFragmentAdapter(fm, listFragment);
        viewPager.setAdapter(messagesFragmentAdapter);
        viewPager.setCanScroll(false);
        selectpage = 1;
        select(selectpage);
        viewPager.setCurrentItem(0);
    }

    /**
     * 选择某一项 进行的操作
     * @param num
     */
    public void select(int num) {
        clean();
        switch (num) {
            case 1://选中已完成
                ivAll.setVisibility(View.GONE);
                ivOnly.setVisibility(View.VISIBLE);
                tvOnly.setTextColor(getResources().getColor(R.color.top_color));
                tvAll.setTextColor(getResources().getColor(R.color.white));
                break;
            case 2://选中未完成
                ivOnly.setVisibility(View.GONE);
                ivAll.setVisibility(View.VISIBLE);
                tvAll.setTextColor(getResources().getColor(R.color.top_color));
                tvOnly.setTextColor(getResources().getColor(R.color.white));
                break;

        }
    }
    /**
     *
     * 清除选择的状态
     */
    public void clean() {
        tvOnly.setTextColor(getResources().getColor(R.color.top_color));
        tvAll .setTextColor(getResources().getColor(R.color.white));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llBack:
                finish();
                break;
            case R.id.llOnly:
                selectpage = 1;
                select(selectpage);
                viewPager.setCurrentItem(0, true);
                break;
            case R.id.llAll:
                selectpage = 2;
                select(selectpage);
                viewPager.setCurrentItem(1, true);
                break;

        }
    }
}
