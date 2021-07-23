package com.motorbike.anqi.activity.voice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.motorbike.anqi.R;
import com.motorbike.anqi.activity.map.RouteChooseActivity;
import com.motorbike.anqi.bean.RoomXqBean;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * 路线规划
 */
public class RoutePlanningActivity extends BaseActivity implements View.OnClickListener {
    private TextView title_tv,time_tv,beizhu_tv,city_tv;
    private TextView starttion,destination,jihe_et;
    private TimePickerView pvTime;
    private String timeStr,city,type,roomId,roomName;
    private RoomXqBean roomXqBean;
    private EditText roomEdit;
    private LinearLayout dingweilayout,roomLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_planning);
        initView();
    }

    private void initView()
    {
        roomXqBean=new RoomXqBean();
        type=getIntent().getStringExtra("type");
        roomEdit=findViewById(R.id.roomname_tv);
        dingweilayout=findViewById(R.id.dingwei_ll);
        roomLayout=findViewById(R.id.room_layout);
        findViewById(R.id.back_layout).setOnClickListener(this);
        findViewById(R.id.queding_layout).setOnClickListener(this);
        findViewById(R.id.jihe_layout).setOnClickListener(this);
        findViewById(R.id.cfd_ll).setOnClickListener(this);
        findViewById(R.id.mdd_ll).setOnClickListener(this);
        title_tv= (TextView) findViewById(R.id.title_text);
        time_tv= (TextView) findViewById(R.id.time_tv);
        beizhu_tv= (TextView) findViewById(R.id.beizhu_tv);
        city_tv= (TextView) findViewById(R.id.dingwei_city_tv);

        starttion= findViewById(R.id.chufa_et);
        destination=  findViewById(R.id.mudi_et);
        jihe_et= findViewById(R.id.jihe_et);
        title_tv.setText("设置团队路线");
        time_tv.setOnClickListener(this);
        beizhu_tv.setOnClickListener(this);
        roomEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                roomName=s.toString();
            }
        });
        if (type.equals("2"))
        {
            roomName = getIntent().getStringExtra("roomName");
            roomId = getIntent().getStringExtra("roomId");
            dingweilayout.setVisibility(View.GONE);
            roomLayout.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(roomName))
            roomEdit.setText(roomName);
            title_tv.setText("修改团队路线");
        }else {
            dingweilayout.setVisibility(View.VISIBLE);
            roomLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.back_layout:
                roomXqBean=null;
                finish();
                break;
            case R.id.queding_layout:
                fanhui();
                break;
            case R.id.time_tv:
                timeSelector();
                break;
            case R.id.beizhu_tv:
                Intent ia = new Intent(this, RoutRemarkActivity.class);
                String aaa=beizhu_tv.getText().toString();
                if (!TextUtils.isEmpty(aaa)) {
                    ia.putExtra("str", aaa);
                }
                startActivityForResult(ia, HttpTagUtil.TeamTripInfo);
                break;
            case R.id.jihe_layout:
                gomapDian("3");
                break;
            case R.id.mdd_ll:
                gomapDian("2");
                break;
            case R.id.cfd_ll:
                gomapDian("1");
                break;
        }
    }


    private void gomapDian(String type)
    {
        Intent ir = new Intent(this, RouteChooseActivity.class);
        ir.putExtra("type",type);
        startActivityForResult(ir, HttpTagUtil.AddAddress);
    }

    private void fanhui()
    {
        if (cfdBool) {
            if (mddBool) {
                if (jjdBool) {
                    if (sjBool) {
                        if (bzBool) {
                            if (type.equals("1")) {
                                Intent i = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("aaa", roomXqBean);
                                bundle.putString("city", city);
                                i.putExtras(bundle);
                                setResult(RESULT_OK, i);
                                finish();
                            }else {
                                gairoomInfo();
                            }
                        }else{
                            prompt("请填写备注");
                        }
                    }else{
                        prompt("请填写时间");
                    }
                }else{
                    prompt("请填写集结地");
                }
            }else{
                prompt("请填写目的地");
            }
        }else{
            prompt("请填写出发地");
        }
    }

    /**
     * 会议时间
     */
    private void timeSelector()
    {

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        boolean isOpen=imm.isActive();
//        Log.e("aaaaa","dakai=="+isOpen);
//        if (isOpen){
//            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//        }
        imm.hideSoftInputFromWindow(destination.getWindowToken(), 0);
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        //时间选择器
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                timeStr = getTime(date);
                Log.e("+++++++++++++++++", timeStr);
                time_tv.setText(timeStr);
                roomXqBean.setSetTime(timeStr);
                sjBool=true;
            }
        })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY_HOUR_MIN)//默认全部显示
                .setDate(selectedDate)
                .setDividerColor(0xFFB4E317)
                .setBgColor(0xFF242424)
                .setTextColorCenter(0xFFffffff)
                .setTextColorOut(0xFF999999)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        TextView ivCancel = (TextView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime.returnData();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime.dismiss();
                            }
                        });
                    }
                })
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .setDividerColor(Color.RED)
                .build();

        pvTime.show();

    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    private boolean cfdBool=false,mddBool=false,jjdBool=false,bzBool=false,sjBool=false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
        {
            return;
        }
        if (requestCode==HttpTagUtil.AddAddress){
            String type=data.getStringExtra("type");
            if (type.equals("1")){
                roomXqBean.setLatitudeStart(data.getStringExtra("lat"));
                roomXqBean.setLongitudeStart(data.getStringExtra("lon"));
                roomXqBean.setStartAddress(data.getStringExtra("address"));
                starttion.setText(data.getStringExtra("address"));
                city=data.getStringExtra("city");
                city_tv.setText(city);
                cfdBool=true;
            } else if (type.equals("2")){
                roomXqBean.setLatitudeDestination(data.getStringExtra("lat"));
                roomXqBean.setLongitudeDestination(data.getStringExtra("lon"));
                roomXqBean.setEndAddress(data.getStringExtra("address"));
                destination.setText(data.getStringExtra("address"));
                mddBool=true;
            }else if (type.equals("3")){
                roomXqBean.setLatitudeGather(data.getStringExtra("lat"));
                roomXqBean.setLongitudeGather(data.getStringExtra("lon"));
                roomXqBean.setSetAddress(data.getStringExtra("address"));
                jihe_et.setText(data.getStringExtra("address"));
                jjdBool=true;
            }
        }else if (requestCode==HttpTagUtil.TeamTripInfo){
            roomXqBean.setRemark(data.getStringExtra("beizhu"));
            beizhu_tv.setText(data.getStringExtra("beizhu"));
            bzBool=true;
        }
    }

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        if (mTag==HttpTagUtil.XUIGAIROOMINFO)
        {
            prompt("修改成功");
            finish();
        }
    }

    private void gairoomInfo()
    {
        showLoading();
        Map<String,Object> map=new ArrayMap<>();
        map.put("roomNo",roomId);
        map.put("startAddress",roomXqBean.getStartAddress());
        map.put("endAddress",roomXqBean.getEndAddress());
        map.put("setTime",roomXqBean.getSetTime());
        map.put("setAddress",roomXqBean.getSetAddress());
        map.put("longitudeStart",roomXqBean.getLongitudeStart());
        map.put("latitudeStart",roomXqBean.getLatitudeStart());
        map.put("longitudeDestination",roomXqBean.getLongitudeDestination());
        map.put("latitudeDestination",roomXqBean.getLatitudeDestination());
        map.put("longitudeGather",roomXqBean.getLongitudeGather());
        map.put("latitudeGather",roomXqBean.getLatitudeGather());
        map.put("remark",roomXqBean.getRemark());
        map.put("theme",roomName);
        okHttp(this, BaseRequesUrl.xuigaiRoomINfo,HttpTagUtil.XUIGAIROOMINFO,map);
    }

}
