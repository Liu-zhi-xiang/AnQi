package com.motorbike.anqi.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.motorbike.anqi.R;


/**
 * @author lzx
 * @date 2018/5/2
 * @info
 */

public class InputDialog extends Dialog{
    private EditText inputEdit;
    private TextView titleTv;
    private OnbutClion onbutClion;
    private String titleStr,editStr;
    private Button cancelbut,okbut;
    public void setOnbutClion(OnbutClion onbutClion) {
        this.onbutClion = onbutClion;
    }
    private Activity context;
    public InputDialog(@NonNull Activity context) {
        super(context, R.style.MyDialog);
          this.context=context;
    }

    public InputDialog(@NonNull Activity  context, int themeResId) {
        super(context, themeResId);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input);
        setCanceledOnTouchOutside(true);

        initView();
        initData();

    }



    private void initView()
    {
        inputEdit=findViewById(R.id.input);
        titleTv=findViewById(R.id.title);
        inputEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String edstr=s.toString();
                if (!TextUtils.isEmpty(edstr)){
                    editStr=edstr;
                    okbut.setClickable(true);
                    okbut.setTextColor(Color.parseColor("#B4E317"));
                }else {
                    okbut.setClickable(false);
                    okbut.setTextColor(Color.parseColor("#979797"));
                }
            }
        });

        cancelbut= findViewById(R.id.cancel);
        cancelbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onbutClion!=null){
                    onbutClion.onFinshclick();
                    dismiss();
                }
            }
        });

        okbut=findViewById(R.id.ok);
        okbut.setClickable(false);
        okbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbutClion.onokclick(editStr);
                if (!TextUtils.isEmpty(editStr)) {

                }else {

                }
            }
        });

         /*
         * 获取窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);
    }

    private void initData()
    {
        if (!TextUtils.isEmpty(titleStr)){
            titleTv.setText(titleStr);
        }
    }

    public View getEditText(){
        if (inputEdit!=null){
            return inputEdit;
        }
        return null;
    }

    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        titleStr = title;
    }
   public interface OnbutClion{
        void onokclick(String inputstr);
        void onFinshclick();
   }
}
