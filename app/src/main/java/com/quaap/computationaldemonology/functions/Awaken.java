package com.quaap.computationaldemonology.functions;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;


import com.quaap.computationaldemonology.R;
import com.quaap.computationaldemonology.util.Rand;

import java.util.ArrayList;
import java.util.List;

public class Awaken extends Drawgorythm {

    private float alpha = 10;
    private float scale = .0001f;
    private float scalev = .000008f;

    private Rect gradrect;

    private Bitmap [] gradient;
    private Paint alphatweak;
    private Paint white = new Paint();

    private double draws = 0;

    private Matrix m = new Matrix();

    //private UpdaterThread mUpdaterThread;

    private final Object imgsync = new Object();
    private List<RectF> poss = new ArrayList<>();
    private List<RectF> dVs = new ArrayList<>();
    private List<Bitmap> bitmaps = new ArrayList<>();

    private int num;
    //private Canvas c;

    public Awaken(Context context) {
        super(context);

        gradient = new Bitmap[4];
        gradient[0] = BitmapFactory.decodeResource(context.getResources(),R.drawable.gradient);
        gradient[1] = BitmapFactory.decodeResource(context.getResources(),R.drawable.gradient2);
        gradient[2] = BitmapFactory.decodeResource(context.getResources(),R.drawable.gradient5);
        gradient[3] = BitmapFactory.decodeResource(context.getResources(),R.drawable.gradient4);
        gradrect = new Rect(0,0,gradient[0].getWidth(),gradient[0].getHeight());

        alphatweak = new Paint();
        alphatweak.setColor(Color.WHITE);
        white.setColor(Color.WHITE);
        num = Rand.getInt(2,4);

    }


    @Override
    public void canvasChanged(Canvas canvas) {
        super.canvasChanged(canvas);

        for (int i = 0; i < num; i++) {

            int x = Rand.getInt(mCenterX - mCenterX / 5, mCenterX);
            int y = Rand.getInt(mCenterY*3/4 - mCenterY / 5, mCenterY*3/4);
            int w = Rand.getInt(mWidth / 15, mWidth / 11);
            int h = w + 2; // + Rand.getInt(mHeight/7);
            RectF dst = new RectF(x, y, x + w, y + h);

            poss.add(dst);
            dVs.add(new RectF(Rand.getFloatNeg1To1(), Rand.getFloatNeg1To1(), 0, 0));

        }

    }



    Matrix r = new Matrix();
    int count = 0;
    @Override
    public void doDraw(Canvas canvas, long ticks) {
        canvas.rotate(-mRoll, mCenterX, mCenterY+mCenterY/2);

        int sign = 1;
        if (alpha > 250) {
            sign = -1;
            //alpha = 250;
        }
        if (alpha<5) {
            sign = 1;
            //alpha = 5;
        }

        alpha += .01 * sign + Math.sin(draws/2);

        draws+=.15;

        alphatweak.setAlpha((int)alpha);

        for (int i = 0; i < num; i++) {
            RectF d =  poss.get(i);
            RectF dV = dVs.get(i);

            if (d.left<=10)  dV.left = -dV.left;
            if (d.top<=10)  dV.top = -dV.top;
            if (d.right>=mWidth-10)  dV.left = -dV.left;
            if (d.bottom>=mHeight*3/4)  dV.top = -dV.top;

            d.set(d.left+dV.left - d.left*scale, d.top+dV.top - d.top*scale,
                    d.right+dV.left+ d.left*scale, d.bottom+dV.top  + d.top*scale);

            //m.setRectToRect(new RectF(gradrect), d, Matrix.ScaleToFit.CENTER);

            canvas.drawBitmap(gradient[i%gradient.length], d.left, d.top, alphatweak);

            if (mTouchDY>0 && i%2==0 || mTouchDY<0 && i%2!=0) {
                //draws += .001*dY;
                dV.left = Math.signum(mTouchX - d.left) * Math.abs(dV.left)  * (float)Rand.getDouble(.5,1.5);
                dV.top = Math.signum(mTouchY - d.top) *  Math.abs(dV.top) * (float)Rand.getDouble(.5,1.5);
                //Log.d("Awake", "" + scale);
            }

            if (scale>.004) {
                scalev = -.00008f; // + Math.sin(draws/5)/1000;
            }
            else if (scale<-.002) {
                scalev = .000008f;
            }
            scale += scalev;

        }

        //r.reset();
        //r.postRotate(-mRoll, mCenterX, mCenterY+mCenterY/2);

        //canvas.setMatrix(r);
//
//        synchronized (imgsync) {
//            canvas.drawBitmap(img, r, alphatweak);
//        }

    }
}
