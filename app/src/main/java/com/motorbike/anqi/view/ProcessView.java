package com.motorbike.anqi.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import static com.motorbike.anqi.util.Utils.dpToPixel;
import static com.motorbike.anqi.util.Utils.setColor;


/**
 * Created by dp on 2017/11/3 0003.
 */

public class ProcessView extends View {
    final float radius = dpToPixel(150);
    final  float sWith= dpToPixel(30);
    final  int mCount=50;
    float ang=180f/mCount;
    float progress = 0;

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    ObjectAnimator animator= ObjectAnimator.ofFloat(this, "progress", 0, 80);


    public ProcessView(Context context) {
        super(context);
    }

    public ProcessView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ProcessView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint.setTextSize(dpToPixel(40));
        paint.setTextAlign(Paint.Align.CENTER);

        animator.setDuration(2000);
     //   animator.setRepeatCount(-1);
        animator.setInterpolator(new FastOutSlowInInterpolator());

    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress)
    {
        this.progress = progress;
        invalidate();
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        animator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        animator.end();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float centerX=getWidth()/2;
        float centerY=getHeight()/2;

        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setStrokeWidth(dpToPixel(2));
        //刻度
        canvas.save();
        for (int i = 0; i < mCount+1; i++) {
            //渐变色 刻度盘划过部分
            paint.setColor(setColor(i,mCount));
            //刻度盘 未划过部分
           if (i>(int)(progress/2)){
               paint.setColor(Color.WHITE);
           }
            canvas.drawLine(centerX-radius,centerY,centerX-radius+sWith,centerY,paint);
            canvas.rotate(ang,centerX,centerY);
        }
        canvas.restore();

        //指针
        canvas.save();
        canvas.rotate(progress*1.8f,centerX,centerY);
        canvas.drawLine(centerX-radius/2,centerY,centerX,centerY,paint);
        canvas.restore();


        //下方文字
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(dpToPixel(1));
        paint.setTextSize(dpToPixel(40));
        canvas.save();
        canvas.drawText((int)progress+"%",centerX,centerY+dpToPixel(50),paint);


    }


    /**
     * 设置当前进度
     *
     * @param progress
     */
    private int mRealProgress;
//    public void setProgress(int progress)
//    {
//        // 进度100% = 控件的75%
//        this.mRealProgress = progress;
//        isRestart = false;
//        this.mProgress = ((mDottedLineCount * 3 / 4) * progress) / mProgressMax;
//        postInvalidate();
//    }
}
