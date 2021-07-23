package com.motorbike.anqi.activity.trip;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.motorbike.anqi.R;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.view.ArcProgressBar;
import com.motorbike.anqi.view.ProcessView;

public class XingcengBiaoActivity extends BaseActivity {
    private ArcProgressBar mArcProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xingceng_biao);
        mArcProgressBar = (ArcProgressBar) findViewById(R.id.arcProgressBar);
        Button btRestart = (Button) findViewById(R.id.button);
        btRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mArcProgressBar.restart();
            }
        });
        Button btStart = (Button) findViewById(R.id.button2);
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValueAnimator valueAnimator =ValueAnimator.ofInt(0, 100);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mArcProgressBar.setProgress((int) animation.getAnimatedValue());
                    }
                });
                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
//                        mArcProgressBar.setProgressDesc("已暂停");
                    }
                });
                valueAnimator.setDuration(5000);
                valueAnimator.start();
            }
        });

    }
}
