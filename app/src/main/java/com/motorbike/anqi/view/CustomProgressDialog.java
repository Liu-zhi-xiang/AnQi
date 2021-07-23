package com.motorbike.anqi.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.motorbike.anqi.R;


/**
 * Created by ICE AGE on 2016/7/23.
 */
public  class CustomProgressDialog extends Dialog {
    private static int theme = R.style.CustomProgressDialog;
    private Context context = null;
    private  AnimationDrawable animationDrawable;
    private String mesaaae;
    private TextView tvMsg;

    public CustomProgressDialog(Context context){
        super(context,theme);
        this.context = context;
        createDialog(context);
    }

    public CustomProgressDialog(Context context, String mesaaae)
    {
        super(context, theme);
        this.mesaaae=mesaaae;
        createDialog(context);
    }

    public  void createDialog(final Context context)
    {
        View view= LayoutInflater.from(context).inflate(R.layout.progress_dialog, null);
        setContentView(view);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        imageView = view.findViewById(R.id.loadingIv);
        tvMsg = view.findViewById(R.id.loadingTv);
        imageView.setImageResource(R.drawable.frame);
        if (!TextUtils.isEmpty(mesaaae)){
            tvMsg.setVisibility(View.VISIBLE);
            tvMsg.setText(mesaaae);
        }else {
            tvMsg.setVisibility(View.GONE);
        }
    }

    @Override
    public void dismiss(){
        if (animationDrawable != null){
            animationDrawable.stop();
            animationDrawable = null;
        }
        super.dismiss();
    }

    private ImageView imageView;
    public void onWindowFocusChanged(boolean hasFocus)
    {
        if (imageView!=null) {
            animationDrawable = (AnimationDrawable) imageView.getDrawable();
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    if (animationDrawable != null) {
                        animationDrawable.start();
                    }
                }
            });
        }

    }
    @Override
    public void show() {
        super.show();
    }

    /**
     *
     * [Summary]
     *       setMessage 提示内容
     * @param strMessage
     * @return
     *
     */
    public void setMessage(String strMessage)
    {
        if (!TextUtils.isEmpty(strMessage)){
            this.mesaaae=strMessage;
            tvMsg.setVisibility(View.VISIBLE);
            tvMsg.setText(mesaaae);
        }
    }

}