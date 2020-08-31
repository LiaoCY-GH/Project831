package com.example.project831;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class HeartRateLine extends View {
    //脉冲个数
    private int pulses;
    //每个脉冲的点数
    private int pointsOfEachPulse;
    //心率线每移动一个点距所需的时间
    private int speed;
    //线色
    private int lineColor;
    //线宽
    private int lineWidth;
    //绘画板
    private Paint paint;

    private int[] pointsHeight;
    private int points;
    private boolean isInit = false;
    private int count;

    public HeartRateLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HeartRateLine);
        pulses = array.getInt(R.styleable.HeartRateLine_pulses, 5);
        pointsOfEachPulse = array.getInt(R.styleable.HeartRateLine_pointsOfEachPulse, 10);
        speed = array.getInt(R.styleable.HeartRateLine_speed, 100);
        lineColor = array.getColor(R.styleable.HeartRateLine_lineColor, Color.RED);
        lineWidth = array.getDimensionPixelSize(R.styleable.HeartRateLine_lineWidth, 1);

        array.recycle();

        paint = new Paint();
        paint.setColor(lineColor);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(lineWidth);
        paint.setStyle(Style.STROKE);

        //整个界面显示的点数
        points = pulses * pointsOfEachPulse;
        //防止出界
        pointsHeight = new int[points + pointsOfEachPulse + 1];

    }

    public HeartRateLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public HeartRateLine(Context context) {
        this(context, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //点与点之间的间隔
        int intervalBetweenPoints = getWidth() / (pulses * pointsOfEachPulse - 1);
        //初始化所有点的高，使之全部等于高的一半
        if (!isInit) {
            for (int i = 0; i < pointsHeight.length; i++) {
                pointsHeight[i] = getHeight() / 2;
            }
            isInit = true;
        }

        //创建一条路径，并按点与点的间隔递增和poitsHeight数据得到点的位置
        Path path = new Path();
        path.moveTo(0, getHeight() / 2);
        for (int i = 0; i < pointsHeight.length; i++) {
            path.lineTo(intervalBetweenPoints * i, pointsHeight[i]);
        }

        //路径
        canvas.drawPath(path, paint);

        //连接路径，使其形成不断的图形
        postDelayed(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < points + pointsOfEachPulse; i++) {
                    pointsHeight[i] = pointsHeight[i + 1];
                }
                if (count == pointsOfEachPulse) {
                    //随机一个高度以内的数值
                    int height = (int) (Math.random() * getHeight() / 2);
                    //绘制形状，并随机显示高度
                    for (int i = 0; i < pointsOfEachPulse; i++) {
                        if (i == 1) {
                            pointsHeight[points + i] = getHeight() / 2 - height;
                        } else if (i == 2) {
                            pointsHeight[points + i] = getHeight() / 2 + height;
                        } else {
                            pointsHeight[points + i] = getHeight() / 2;
                        }
                    }
                    count = 0;
                }
                count++;

                invalidate();
            }
        }, speed);
    }

}