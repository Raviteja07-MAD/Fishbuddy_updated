package com.fishbuddy.circularimageview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.view.View;

import com.fishbuddy.R;

public class PieChart123 extends View {
    float start=0;
    int width;
    int[] data;
    int cx,cy;
    int numberOfparts;//it tells many data or item will be placed in chart
    private int[] color;

    public PieChart123(Context context, int numOfItems, int[] data, int[] color) {
        super(context);
        setFocusable(true);
        this.numberOfparts=numOfItems;
        this.data=data;
        this.color=color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#63FFFFFF"));
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(0);
        p.setStyle(Paint.Style.FILL);
        float[] scaledValues = scale();

        RectF rectF = new RectF(0,0,getWidth(),getWidth());

        p.setColor(Color.parseColor("#63000000"));
        for(int i=0;i<numberOfparts;i++){
            p.setColor(color[i]);
            p.setStyle(Paint.Style.STROKE);//Paint.Style.FILL
            //canvas.drawCircle(0,0,100,p);
            canvas.drawArc(rectF,start,scaledValues[i],true,p);
            start=start+scaledValues[i];
        }


        Paint paint=new Paint();
        int x = getWidth();
        int y = getHeight();
        int radius1;
        radius1 = 100;
        for(int i=0;i<numberOfparts;i++){

            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            // Use Color.parseColor to define HTML colors
            paint.setColor(color[i]);

            Shader shader = new SweepGradient(x / 2, x / 2, color, null);
            paint.setShader(shader);
            canvas.drawCircle(x / 2, x / 2, radius1, paint);


            //canvas.drawCircle(x / 2, y / 2, radius1, paint);

            start=start+scaledValues[i];
        }










        Paint cenPaint=new Paint();
        int radius=getWidth()/20-100;
        //int radius=getWidth()/200;
        cenPaint.setStyle(Paint.Style.FILL);
        cenPaint.setColor(Color.parseColor("#63000000"));
        cx=cy=getWidth()/2;
        canvas.drawCircle(cx,cy,radius,cenPaint);




    }
    private float[] scale() {
        float[] scaledValues = new float[this.data.length];
        float total = getTotal(); //Total all values supplied to the chart
        for (int i = 0; i < this.data.length; i++) {
            scaledValues[i] = (this.data[i] / total) * 360; //Scale each value
        }
        return scaledValues;
    }

    private float getTotal() {
        float total = 0;
        for (float val : this.data)
            total += val;
        return total;
    }


}