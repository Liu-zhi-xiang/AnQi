package com.motorbike.anqi.activity.trip;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.motorbike.anqi.R;
import com.motorbike.anqi.activity.my.TopListActivity;

/**
 * 行程----已废弃
 */
public class TripActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        initView();
    }

    private void initView() {
        findViewById(R.id.llBack).setOnClickListener(this);
        findViewById(R.id.llRight).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llBack:
                finish();
                break;
            case R.id.llRight:
                startActivity(new Intent(this,TopListActivity.class));
                break;
        }
    }
}
