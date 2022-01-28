package com.zjw.mvvm_demo.view.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.utils.DisplayUtil;

/**
 * @author dengdayi
 * @date: 2017-12-09 14:18
 * @packageName: com.test.progressbutton
 */

@SuppressLint("AppCompatCustomView")
public class ProgressButton extends AppCompatButton {
    private static final String TAG = "ProgressButton";
    Paint mPaint, mTextPaint;
    private int width, height, space, radius, progress;
    private int progressbtn_backgroud_color, progressbtn_backgroud_second_color;
    //动画执行100.
    private boolean isStart = false;
    private String text;

    public ProgressButton(Context context) {
        super(context);
    }

    public ProgressButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public ProgressButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initPaint() {
        mPaint = new Paint();
        progressbtn_backgroud_color = getContext().getResources().getColor(R.color.progressbtn_backgroud_color);
        progressbtn_backgroud_second_color = getContext().getResources().getColor(R.color.progressbtn_backgroud_color);
        mPaint.setColor(progressbtn_backgroud_color);
        mPaint.setAntiAlias(true);

        radius = DisplayUtil.dip2px(getContext(), 35);
        //文字画笔
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(DisplayUtil.sp2px(getContext(), 15));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //背景
        mPaint.setColor(progressbtn_backgroud_color);
        RectF r1 = new RectF(0, 0, getWidth(), getHeight());
        canvas.drawRoundRect(r1, radius, radius, mPaint);
        //进度
        mPaint.setColor(progressbtn_backgroud_second_color);
        int newWidth = progress * space;
        if (newWidth >= width) {
            newWidth = width;
        }

        RectF r2 = new RectF(0, 0, newWidth, height);
        canvas.drawRoundRect(r2, radius, radius, mPaint);
        //字体
        if (progress == 0) {
            text = "立即更新";
        } else if (progress >= 100) {
            text = "结束";
        } else {
            text = "下载中 " + progress + " % ";
        }
        //获取文字的高宽
        Rect rect = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), rect);

        int textWidth = rect.width();//文本的宽度
        int textHeight = rect.height();
        canvas.drawText(text, (width - textWidth) / 2, (height - textHeight) / 2, mTextPaint);
        postDelayed(runnable, 30);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isStart) {
                return;
            }
            if (progress > 100) {
                return;
            }
            progress++;
            postInvalidate();
        }
    };

    public void start() {
        //进度
        isStart = true;
        post(runnable);
    }

    public void pause() {
        //进度
        isStart = false;
        post(runnable);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        space = width % 100 == 0 ? w / 100 : (w / 100) + 1;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStart) {
                    pause();
                } else {
                    start();
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (runnable != null) {
            removeCallbacks(runnable);
        }
    }
}
