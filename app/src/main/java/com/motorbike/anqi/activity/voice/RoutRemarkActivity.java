package com.motorbike.anqi.activity.voice;

import android.content.Intent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.motorbike.anqi.R;
import com.motorbike.anqi.bean.RoomXqBean;
import com.motorbike.anqi.init.BaseActivity;

/**
 * 竞速备注
 */
public class RoutRemarkActivity extends BaseActivity implements View.OnClickListener {
    private TextView titleTv,tishiText;
    private EditText editText;
    private String beizhuStr,chuandeStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rout_remark);
        chuandeStr=getIntent().getStringExtra("str");
        initView();
    }

    private void initView()
    {
        findViewById(R.id.back_layout).setOnClickListener(this);
        findViewById(R.id.queding_layout).setOnClickListener(this);
        titleTv= findViewById(R.id.title_text);
        tishiText= findViewById(R.id.tishi_lent_text);
        editText= findViewById(R.id.tvMeetContent);

        titleTv.setText("竞速备注");
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str=s.toString();
                if (!TextUtils.isEmpty(str)){
                    tishiText.setText(str.length()+"/120");
                    beizhuStr=str;
                }
            }
        });

        if (!TextUtils.isEmpty(chuandeStr)){
            editText.setText(chuandeStr);
            editText.setSelection(chuandeStr.length());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                finish();
                break;
            case R.id.queding_layout:
                if (!TextUtils.isEmpty(beizhuStr)) {
                    Intent i = new Intent();
                    i.putExtra("beizhu", beizhuStr);
                    setResult(RESULT_OK, i);
                    finish();
                }else
                    {
                        prompt("备注不能为空");
                    }
                break;
        }
    }
}
