package com.quaap.computationaldemonology.functions;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by tom on 12/6/16.
 */

public class TouchLightning extends Drawgorythm {

    Paint fcircle;
    //Paint fsparks;
    float radius = 50;
    double r = 0;
    double rstart = Math.PI/2;

    public TouchLightning(Context context) {
        super(context);
    }

    public void canvasChanged(final Canvas canvas) {
        super.canvasChanged(canvas);
        fcircle = new Paint();
        fcircle.setStyle(Paint.Style.FILL);
        //fcircle.setStrokeWidth(15);
        fcircle.setARGB(127,100,100,164);

//        fsparks = new Paint();
//        fsparks.setStyle(Paint.Style.STROKE);
//        fsparks.setStrokeWidth(2);
//        fsparks.setARGB(127,100,100,164);

    }

    @Override
    public void doDraw(Canvas canvas, long ticks) {

        if (mTouchX!=0 && mTouchY!=0) {
            rstart += .07;
            r = rstart;
            float tx = mTouchX;
            float ty = mTouchY - radius/2;
            for (int j=0; j<5; j++) {

                //canvas.drawCircle(mTouchX, mTouchY, radius, fcircle);
                double x = (radius * Math.cos(r));
                double y = (radius * Math.sin(r));

                float x1 = (float)x + tx;
                float y1 = (float)y + ty;
                canvas.drawCircle(x1, y1, 8, fcircle);
                //canvas.drawCircle(tx, ty, radius, fsparks);

                int strikes = (int) (Math.random() * 3 + 3);
                for (int i = 0; i < strikes; i++) {
                    float x2 = (float) ((Math.random() - .5)*radius + x/2 + x1);
                    float y2 = (float) ((Math.random() - .5)*radius + y/2 + y1);
                    canvas.drawLine(x1, y1, x2, y2, getRandomForeground());
                }
                r += Math.PI * 2.0 / 5.0;
            }
        }
    }
}
