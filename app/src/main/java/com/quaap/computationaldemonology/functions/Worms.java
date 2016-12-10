package com.quaap.computationaldemonology.functions;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by tom on 12/8/16.
 */

public class Worms extends Drawgorythm {

    List<Worm> worms = new ArrayList<>();


    public Worms(Context context) {
        super(context);

    }

    @Override
    public void canvasChanged(Canvas canvas) {
        super.canvasChanged(canvas);

        worms.clear();
        for (int i=1; i<17;i++) {
            worms.add(new Worm(9));
        }
    }

    public void setPaints(Paint foreground, Paint background) {
        mForeground = new Paint[16];
        int color = background.getColor();
        int alpha = background.getAlpha();
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        for (int i=0; i<mForeground.length; i++) {
            mForeground[i] = new Paint();
            mForeground[i].setARGB(alpha, red+i, green, blue+32 - i);
        }
        mBackground = background;
    }

    @Override
    public void doDraw(Canvas canvas, long ticks) {
        if (mTouchX>0 && mTouchY>0) {
            for (Worm worm: worms) {
                worm.setDest(mTouchX, mTouchY);
            }
        }
        for (Worm worm: worms) {
            worm.moveToward();
            worm.draw(canvas);
        }
    }

    class Worm {
        PointF [] points;
        PointF [] pointDeltas;
        PointF dest;
        Paint color;
        Random r = new Random();
        float maxseglen;

        public Worm(int segments) {

            color = new Paint(getRandomForeground());
            color.setStrokeWidth(15);
            color.setAlpha(250);

            points = new PointF[segments];
            pointDeltas = new PointF[segments];

            maxseglen = Math.min(mWidth,mHeight)/30;
            points[0] = new PointF(r.nextFloat()*mWidth, r.nextFloat()*mHeight/2f);
            dest = new PointF(r.nextFloat()*mWidth, r.nextFloat()*mHeight*2/3.0f);

            float xdir = (r.nextFloat()-.5f);
            float ydir = (r.nextFloat()-.5f);

            pointDeltas[0] = new PointF(xdir, ydir);


            for (int i=1; i<segments;i++) {
                points[i] = new PointF(
                        points[i-1].x + ((r.nextFloat()-.5f)+xdir)*maxseglen,
                        points[i-1].y + ((r.nextFloat()-.5f)+ydir)*maxseglen
                );
               // pointDeltas[i] = new PointF((r.nextFloat()-.5f)*2, (r.nextFloat()-.5f)*2);
                pointDeltas[i] = new PointF((r.nextFloat()-.5f)/5, (r.nextFloat()-.5f)/5);
            }
        }

        public void draw(Canvas canvas) {

            for (int i=1; i<points.length; i++) {
                PointF w0 = points[i-1];
                PointF w1 = points[i];
                canvas.drawLine(w0.x, w0.y, w1.x, w1.y, color);
                canvas.drawCircle(w1.x, w1.y, 8, color);
            }
            canvas.drawCircle(points[0].x, points[0].y, 9, color);
        }

        void setDest(float x, float y) {
            dest.x = x;
            dest.y = y;

        }

        void moveToward() {
            pointDeltas[0].x +=  (dest.x - points[0].x)/mCenterX;
            pointDeltas[0].y +=  (dest.y - points[0].y)/mCenterY;


            if (pointDeltas[0].x>mD) pointDeltas[0].x = mD;
            if (pointDeltas[0].x<-mD) pointDeltas[0].x = -mD;
            if (pointDeltas[0].y>mD) pointDeltas[0].y = mD;
            if (pointDeltas[0].y<-mD) pointDeltas[0].y = -mD;

            for (int i=0; i<points.length; i++) {
                points[i].x += pointDeltas[i].x;
                points[i].y += pointDeltas[i].y;
            }
            for (int i=1; i<pointDeltas.length; i++) {
                pointDeltas[i].x += (float)((points[i-1].x-points[i].x)/(double)maxseglen/10);
                pointDeltas[i].y += (float)((points[i-1].y-points[i].y)/(double)maxseglen/10);

                if (r.nextFloat()>.95) {
                    pointDeltas[i].x += (r.nextFloat()-.5f)*mD;
                    pointDeltas[i].y += (r.nextFloat()-.5f)*mD;
                }

                if (pointDeltas[i].x>mD) pointDeltas[i].x = mD;
                if (pointDeltas[i].x<-mD) pointDeltas[i].x = -mD;
                if (pointDeltas[i].y>mD) pointDeltas[i].y = mD;
                if (pointDeltas[i].y<-mD) pointDeltas[i].y = -mD;
            }
            if ( Math.abs(points[0].x-dest.x) < maxseglen*3 && Math.abs(points[0].y-dest.y) < maxseglen*3 ) {
                dest.x = r.nextFloat()*mWidth;
                dest.y = r.nextFloat()*mHeight*2/3;
            }
        }

        float mD = 1.5f;
        void constrainDelta(PointF p) {
            if (p.x>mD) p.x = mD;
            if (p.x<-mD) p.x = -mD;
            if (p.y>mD) p.y = mD;
            if (p.y<-mD) p.y = -mD;

        }

    }
}
