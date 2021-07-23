package com.motorbike.anqi.activity.my;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.core.content.FileProvider;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.google.gson.Gson;
import com.motorbike.anqi.R;
import com.motorbike.anqi.bean.MediaBean;
import com.motorbike.anqi.bean.UserCenterBean;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.EditUserInfoReques;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.interfaces.HttpRequestTag;
import com.motorbike.anqi.main.che.CityPickerActivity;
import com.motorbike.anqi.util.LGImgCompressor;
import com.motorbike.anqi.util.OtherUtils;
import com.motorbike.anqi.util.PermissionsConstans;
import com.motorbike.anqi.util.UserPreference;
import com.motorbike.anqi.view.CircleImageView;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 编辑个人信息
 */
public class EditUserinfoActivity extends BaseActivity implements View.OnClickListener, HttpRequestTag, LGImgCompressor.CompressListener {
    private LinearLayout llDiQu, llSex, llBirthday, llCheKu, llReceiveAdd, llBack;
    private TextView tvUpdateHead, tvAddress, tvSex, tvBirthday, tvMotoType, tvReceiveAdd, tvOk, tvTitle;
    private CircleImageView ivHead;
    private EditText etName, etPhone;
    ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
    private Dialog selectPhotoDialog;
    private static int REQUEST_CODE_CHOOSE = 210;
    private OptionsPickerView optionsPickerView;
    private ArrayList<String> sex = new ArrayList<>();
    private String updateSex, timeStr, area, carBand, carModels, address;
    private TimePickerView pvTime;
    //    private static int REQUEST_CAMERA = 100;
    private File mTmpFile;
    private Map<String, Object> etUsermap, getUserMap;
    private UserPreference preference;
    private EditUserInfoReques editUserInfoReques;
    private List<Uri> mSelected;
    private List<File> fileList;
    private List<MediaBean> mPhotoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_userinfo);
        preference = UserPreference.getUserPreference(this);
        getNoLinkData();
        initView();
    }

    private void initView() {
        llBack = findViewById(R.id.llBack);
        llBack.setOnClickListener(this);
        llDiQu = findViewById(R.id.llDiQu);
        llDiQu.setOnClickListener(this);
        llSex = findViewById(R.id.llSex);
        llSex.setOnClickListener(this);
        llBirthday = findViewById(R.id.llBirthday);
        llBirthday.setOnClickListener(this);
        llCheKu = findViewById(R.id.llCheKu);
        llCheKu.setOnClickListener(this);
        llReceiveAdd = findViewById(R.id.llReceiveAdd);
        llReceiveAdd.setOnClickListener(this);
        tvUpdateHead = findViewById(R.id.tvUpdateHead);
        tvUpdateHead.setOnClickListener(this);
        etName = findViewById(R.id.etName);
        tvAddress = findViewById(R.id.tvAddress);
        tvSex = findViewById(R.id.tvSex);
        tvBirthday = findViewById(R.id.tvBirthday);
        tvMotoType = findViewById(R.id.tvMotoType);
        etPhone = findViewById(R.id.etPhone);
        tvReceiveAdd = findViewById(R.id.tvReceiveAdd);
        tvOk = findViewById(R.id.tvOk);
        tvOk.setOnClickListener(this);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("编辑个人信息");
        ivHead = findViewById(R.id.ivHead);
        ivHead.setOnClickListener(this);
        etUsermap = new HashMap<>();
        getUserMap = new HashMap<>();
        mPhotoList = new ArrayList<>();
        getUserInfo(preference.getUserId());
    }

    private void getUserInfo(String userId) {
        if (getUserMap != null) {
            getUserMap.clear();
        }
        getUserMap.put("userId", userId);
        okHttp(this, BaseRequesUrl.PersonalCenter, HttpTagUtil.PersonalCenter, getUserMap);
    }

    private void getNoLinkData() {
        sex.add("男");
        sex.add("女");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llBack://返回
                finish();
                break;
            case R.id.llDiQu://地区
                Intent intent = new Intent(this, CityPickerActivity.class);
                startActivityForResult(intent, HttpTagUtil.CYQ_CITY);
                break;
            case R.id.llSex://性别
                selectSex();
                optionsPickerView.show();
                break;
            case R.id.llBirthday://生日
                timeSelector();
                break;
            case R.id.llCheKu://车库
                Intent carType = new Intent(this, ManageCarActivity.class);
                startActivityForResult(carType, 104);
                break;
            case R.id.llReceiveAdd://收货地址
                startActivityForResult(new Intent(this, ManageAddActivity.class), 102);
                break;
            case R.id.tvOk://完成   1 男   0 女
                String phone = etPhone.getText().toString();
                String nickName = etName.getText().toString();
                showLoading();
                if (mPhotoList.size() > 0) {
                    zhaox = false;
                    LGImgCompressor.getInstance(this).withListener(this).
                            starCompress(mPhotoList.get(0).getUri().toString(), 600, 800, 500);
                } else {
                    http(preference.getUserId()
                            , nickName
                            , area
                            , updateSex
                            , timeStr
                            , carModels
                            , carBand
                            , phone
                            , address
                            , null);
                }
                break;
            case R.id.tvUpdateHead://更换头像
            case R.id.ivHead:
                photoDialog();
                break;
            case R.id.btn_report: // 相机
                if (selectPhotoDialog != null) {
                    selectPhotoDialog.dismiss();
                }
                if (hasPermission(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    doCameraPermission();
                } else {
                    requestPermission(PermissionsConstans.INSEPCT_CAMERA, android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                break;
            case R.id.btn_error: // 相册
                if (selectPhotoDialog != null) {
                    selectPhotoDialog.dismiss();
                }
                goToAlbum();
                break;
            case R.id.cancleSelected: // 取消
                if (selectPhotoDialog != null) {
                    selectPhotoDialog.dismiss();
                }
                break;
        }
    }

    @Override
    protected void doCameraPermission() {
        super.doCameraPermission();
        takePhoto();
    }
    /**
     * 打开相机
     */
    private void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mTmpFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/." + System.currentTimeMillis() + ".jpg");
        mTmpFile.getParentFile().mkdirs();
        Uri uri;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            uri = Uri.fromFile(mTmpFile);
        } else {
            /**
             * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
             * 并且这样可以解决MIUI系统上拍照返回size为0的情况
             */
            uri = FileProvider.getUriForFile(this, "com.motorbike.anqi.FileProvider", mTmpFile);
        }
        //添加权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 310);
    }
    /**
     * 打开相册
     */
    private void goToAlbum() {
        Matisse.from(EditUserinfoActivity.this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))//选择mime的类型
                .countable(true)//设置从1开始的数字
                .maxSelectable(1)//选择图片的最大数量限制
                .capture(false)//启用相机
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)//屏幕显示方向
                .thumbnailScale(0.85f) // 缩略图的比例
                .imageEngine(new PicassoEngine()) // 使用的图片加载引擎
                .theme(R.style.Matisse_Dracula) // 黑色背景
                .forResult(REQUEST_CODE_CHOOSE); // 设置作为标记的请求码
    }

    private boolean zhaox = false;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=RESULT_OK){
            return;
        }
        switch (requestCode) {
            case 210:
                if (data != null) {

                    if (mPhotoList != null) {
                        mPhotoList.clear();
                    }
                    mSelected = Matisse.obtainResult(data);
                    Log.d("Matisse", "mSelected: " + mSelected);
                    Uri uri = null;
                    for (int x = 0; x < mSelected.size(); x++) {
                        uri = mSelected.get(x);
                        mPhotoList.add(new MediaBean(x + "", mSelected.get(x)));
                    }
                    Picasso.with(EditUserinfoActivity.this).invalidate(new File(uri.toString()));
                    Picasso.with(EditUserinfoActivity.this)
                            .load(uri)
                            .config(Bitmap.Config.RGB_565)
                            .centerCrop()
                            .fit()
                            .networkPolicy(NetworkPolicy.NO_STORE)
                            .into(ivHead);
//            headImage=uri.toString();
                    Log.e("aaa", mPhotoList.size() + "  xiangce");
                }
                break;
            case 310:
                //请求相机
                Log.e("ccc", "1111111111");
                if (mTmpFile != null) {
                    Log.e("ccc", "2222222222");
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                        Picasso.with(EditUserinfoActivity.this).invalidate(mTmpFile);
                        Picasso.with(EditUserinfoActivity.this)
                                .load(mTmpFile)
                                .config(Bitmap.Config.RGB_565)
                                .centerCrop()
                                .fit()
                                .networkPolicy(NetworkPolicy.NO_STORE)
                                .into(ivHead);
                    } else {
                        zhaox = true;
                        LGImgCompressor.getInstance(this).withListener(this).
                                starCompress(Uri.fromFile(new File(mTmpFile.getAbsolutePath())).toString(), 300, 300, 500);

                    }
                    if (mPhotoList != null) {
                        mPhotoList.clear();
                    }
                    mPhotoList.add(new MediaBean("1", Uri.fromFile(new File(mTmpFile.getAbsolutePath()))));
                    Log.e("aaa", mPhotoList.size() + "   paizhao");
                }
                break;
            case HttpTagUtil.CYQ_CITY:
                if (data != null) {
                    area = data.getExtras().getString(CityPickerActivity.KEY_PICKED_CITY);
                    if (!TextUtils.isEmpty(area)) {
                        tvAddress.setText(area);
                        Log.e("aaa", area + "  area");
                    }
                }
                break;
            case 102:
                if (data != null) {
                    address = data.getExtras().getString("data");
                    tvReceiveAdd.setText(address);
                    Log.e("mmm", address);
                }
                break;
            case 104:
                if (data != null) {
                    carBand = data.getExtras().getString("carBand");
                    carModels = data.getExtras().getString("carModels");
                    tvMotoType.setText(carBand + carModels);
                }
                break;

        }
    }



    /**
     * 相册弹出框
     */
    private void photoDialog() {

        View view = View.inflate(this, R.layout.select_photo_dialog, null);
        Button btn_report;
        Button btn_error;
        Button cancleSelected;
        btn_report = view.findViewById(R.id.btn_report);
        btn_error = view.findViewById(R.id.btn_error);
        cancleSelected = view.findViewById(R.id.cancleSelected);
        btn_report.setOnClickListener(this);
        btn_error.setOnClickListener(this);
        cancleSelected.setOnClickListener(this);
        selectPhotoDialog = new Dialog(this);
        selectPhotoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectPhotoDialog.setContentView(view);
        Window regionWindow = selectPhotoDialog.getWindow();
        regionWindow.setGravity(Gravity.BOTTOM);
        regionWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        regionWindow.setWindowAnimations(R.style.view_animation);
        regionWindow.setBackgroundDrawable(dw);
        selectPhotoDialog.setCanceledOnTouchOutside(true);
        selectPhotoDialog.show();
    }

    /**
     * 性别选择
     */
    private void selectSex() {
        optionsPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String str = sex.get(options1);
                if (str.equals("男")) {
                    tvSex.setText("男");
                    tvSex.setTextColor(Color.parseColor("#ffffff"));
                    updateSex = "1";
                } else if (str.equals("女")) {
                    tvSex.setText("女");
                    tvSex.setTextColor(Color.parseColor("#ffffff"));
                    updateSex = "0";
                }
            }
        }).setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
            @Override
            public void customLayout(View v) {
                final TextView tvSubmit = v.findViewById(R.id.tv_finish);
                TextView ivCancel = v.findViewById(R.id.iv_cancel);
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        optionsPickerView.returnData();
                    }
                });
                ivCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        optionsPickerView.dismiss();
                    }
                });

            }
        }).setBgColor(Color.parseColor("#242424"))
                .setTextColorCenter(Color.parseColor("#ffffff"))
                .isDialog(false)
                .build();
        optionsPickerView.setPicker(sex);
    }

    /**
     * 会议时间
     */
    private void timeSelector() {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        //时间选择器
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                timeStr = getTime(date);
                Log.e("+++++++++++++++++", timeStr);
                tvBirthday.setText(timeStr);
            }
        })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)//默认全部显示
                .setDate(selectedDate)
                .setDividerColor(0xFFB4E317)
                .setBgColor(0xFF242424)
                .setTextColorCenter(0xFFffffff)
                .setTextColorOut(0xFF999999)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = v.findViewById(R.id.tv_finish);
                        TextView ivCancel = v.findViewById(R.id.iv_cancel);
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private void http(String userId, String nickName, String area, String sex, String birthday,
                      String models, String brand, String phone, String address, List<File> headerImg) {

        showLoading();
        editUserInfoReques = new EditUserInfoReques(this, HttpTagUtil.EditoUserInfo, this);
        editUserInfoReques.addPoicomment(userId, phone, address, brand, models, birthday, sex, area, nickName, headerImg);
//        if (etUsermap!=null){
//            etUsermap.clear();
//        }
//        etUsermap.put("userId",userId);
//        etUsermap.put("nickName",nickName);
//        etUsermap.put("area",area);
//        etUsermap.put("sex",sex);
//        etUsermap.put("birthday",birthday);
//        etUsermap.put("models",models);
//        etUsermap.put("brand",brand);
//        etUsermap.put("phone",phone);
//        etUsermap.put("address",address);
//        etUsermap.put("headerImg",headerImg);
//        okHttp(this, BaseRequesUrl.EditoUserInfo, HttpTagUtil.EditoUserInfo,etUsermap);
    }

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag) {
            case HttpTagUtil.PersonalCenter:
                dismissLoading();
                if (data != null) {
                    Log.e("aaa", data.toString());
                    UserCenterBean userCenterBean = new Gson().fromJson(data, UserCenterBean.class);
                    if (userCenterBean != null) {
                        etName.setText(userCenterBean.getNickname());
                        tvAddress.setText(userCenterBean.getArea());
                        tvMotoType.setText(userCenterBean.getCarType());
                        String sex = userCenterBean.getSex();
                        if (!TextUtils.isEmpty(sex)) {
                            if (sex.equals("1")) {
                                tvSex.setText("男");
                            } else {
                                tvSex.setText("女");
                            }
                        }
                        tvBirthday.setText(userCenterBean.getBirthday());
                        etPhone.setText(userCenterBean.getPhone());
                        tvReceiveAdd.setText(userCenterBean.getDeliverAddress());
                        //清理缓存
//                        Picasso.with(U.this).invalidate(new File(uri.toString()));
                        if (!TextUtils.isEmpty(userCenterBean.getHeaderImg())) {
                            Picasso.with(EditUserinfoActivity.this)
                                    .load(userCenterBean.getHeaderImg())
                                    .config(Bitmap.Config.RGB_565)
                                    .centerCrop()
                                    .fit()
                                    .networkPolicy(NetworkPolicy.NO_STORE)
                                    .into(ivHead);
                        }
                    }
                }
                break;
        }
    }


    @Override
    public void requestComplete(Integer tag, Object result, String msg, boolean complete) {
        dismissLoading();
        switch (tag) {
            case HttpTagUtil.EditoUserInfo:
                dismissLoading();
                showTip(msg);
                if (!TextUtils.isEmpty(carBand) && !TextUtils.isEmpty(carModels)) {
                    preference.setCarType(carBand + carModels);
                    preference.save();
                }
                finish();
                break;
        }
    }

    @Override
    public void onCompressStart() {

    }

    @Override
    public void onCompressEnd(LGImgCompressor.CompressResult compressResult) {
        if (compressResult.getStatus() == LGImgCompressor.CompressResult.RESULT_ERROR)//压缩失败
            return;
        File file = new File(compressResult.getOutPath());
        Picasso.with(EditUserinfoActivity.this)
                .load(file)
                .config(Bitmap.Config.RGB_565)
                .centerCrop()
                .fit()
                .networkPolicy(NetworkPolicy.NO_STORE)
                .into(ivHead);
        if (!zhaox) {
            if (fileList == null) {
                fileList = new ArrayList<>();
            } else {
                fileList.clear();
            }
            fileList.add(file);
            if (fileList.size() == mPhotoList.size()) {
                String phone = etPhone.getText().toString();
                String nickName = etName.getText().toString();
                http(preference.getUserId()
                        , nickName
                        , area
                        , updateSex
                        , timeStr
                        , carModels
                        , carBand
                        , phone
                        , address
                        , fileList);
            }
        }
    }
}
