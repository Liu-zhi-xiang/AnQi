package com.motorbike.anqi.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.motorbike.anqi.R;



/**
 * Created by vix on 2016/11/2.
 */
public class CustomDialogView extends Dialog {
    private View line;
    private Button dialog_yes;//确定按钮
    private Button dialog_no;//取消按钮
    private TextView dialog_title;//消息标题文本
    private TextView dialog_message;//消息提示文本
    private EditText dialog_edit;   //输入框
    private String editStr;  //从外界传过来的文本
    private String titleStr;//从外界设置的title文本
    private String messageStr;//从外界设置的消息文本
    //确定文本和取消文本的显示内容
    private String yesStr, noStr;

    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器

    private Context context;
    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param str
     * @param onNoOnclickListener
     */
    public void setNoOnclickListener(String str, onNoOnclickListener onNoOnclickListener) {
        if (str != null) {
            noStr = str;
        }
        this.noOnclickListener = onNoOnclickListener;
    }

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param str
     * @param onYesOnclickListener
     */
    public void setYesOnclickListener(String str, onYesOnclickListener onYesOnclickListener) {
        if (str != null) {
            yesStr = str;
        }
        this.yesOnclickListener = onYesOnclickListener;
    }

    public CustomDialogView(Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_layout);
        setCanceledOnTouchOutside(true);
        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();

    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        dialog_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        dialog_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        //如果用户自定了title和message
        if (titleStr != null) {
            dialog_title.setText(titleStr);
        }
        if (messageStr != null) {
            dialog_message.setVisibility(View.VISIBLE);
            dialog_edit.setVisibility(View.GONE);
            dialog_message.setText(messageStr);
        }else{
            dialog_message.setVisibility(View.GONE);
            dialog_edit.setVisibility(View.VISIBLE);
        }
        //如果设置按钮的文字
        if (yesStr != null) {
            dialog_yes.setText(yesStr);
        }else {
            dialog_yes.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }
        if (noStr != null) {
            dialog_no.setText(noStr);
        }else {
            dialog_no.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }

        if(dialog_edit != null){
            dialog_edit.setText(editStr);
        }

    }

    /**
     * 初始化界面控件
     */
    private void initView()
    {
        //设置页面背景色
        LinearLayout llDialog = (LinearLayout) findViewById(R.id.ll_dialog);
        GradientDrawable gradientDrawable = (GradientDrawable) llDialog.getBackground();
        dialog_yes = (Button) findViewById(R.id.dialog_yes);
        dialog_no = (Button) findViewById(R.id.dialog_no);
        dialog_title = (TextView) findViewById(R.id.dialog_title);
        dialog_message = (TextView) findViewById(R.id.dialog_message);
        dialog_edit = (EditText) findViewById(R.id.dialog_edit);
        line = findViewById(R.id.line);
    }

    public View getEditText(){
        if (dialog_edit!=null){
            return dialog_edit;
        }
        return null;
    }

    public void setEditText(String str){
        editStr = str;
    }

    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        titleStr = title;
    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param message
     */
    public void setMessage(String message) {
        messageStr = message;
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onYesOnclickListener  {
        void onYesClick();
    }

    public interface onNoOnclickListener {
        void onNoClick();
    }
}
