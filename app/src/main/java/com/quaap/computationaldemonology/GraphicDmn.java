package com.quaap.computationaldemonology;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 12/2/16.
 */

public class GraphicDmn extends SurfaceView implements  SurfaceHolder.Callback {

    private long step = 33;

    private GraphicDmnThread mThread;

    private final Paint mLinePaint;

    private final Paint mBgColor;

    Bitmap viewdata;
    Canvas passCanvas;

    List<Drawgorythm> drawers = new ArrayList<>();

    public GraphicDmn(Context context, AttributeSet attrs) {
        super(context, attrs);

        Log.d("GraphicDmn", "Constructor");
        final SurfaceHolder holder = getHolder();
        holder.addCallback(this);



        // Initialize paints for speedometer
        mLinePaint = new Paint();
        //mLinePaint.setAntiAlias(true);
        mLinePaint.setARGB(255, 0, 127, 127);

        mBgColor = new Paint();
       // mBgColor.setAntiAlias(true);
        mBgColor.setARGB(255, 0, 23, 12);

        Drawgorythm d1 = new FuzzyRing();
        d1.setPaints(mLinePaint, mBgColor);
        drawers.add(d1);

        Drawgorythm d2 = new BarbedRing();
        d2.setPaints(mLinePaint, mBgColor);
       // drawers.add(d2);

//        Synth synth = new Synth();
//        synth.genSaw(64);
       // blackCanvas(holder);

    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d("GraphicDmn", "surfaceCreated");

        mThread = new GraphicDmnThread(surfaceHolder, new Handler() {
            @Override
            public void handleMessage(final Message m) {
                // mStatusText.setVisibility(m.getData().getInt("viz"));
                //  mStatusText.setText(m.getData().getString("text"));
            }
        });


    }

    @Override
    public void surfaceChanged(final SurfaceHolder surfaceHolder, final int format, final int width, final int height) {
        Log.d("GraphicDmn", "surfaceChanged");

        if (mThread!=null) {
            if (!mThread.isRunning()) {
                mThread.start();
            }

            Canvas c = null;
            try {
                c = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder) {
                    viewdata = Bitmap.createBitmap(c.getWidth(), c.getHeight(), Bitmap.Config.ARGB_8888);
                    passCanvas = new Canvas(viewdata);
                    passCanvas.drawPaint(mBgColor);

                    for(Drawgorythm d: drawers) {
                        d.canvasChanged(passCanvas);
                    }
                }
            } finally {

                if (c != null) {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }

        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d("GraphicDmn", "surfaceDestroyed");
        mThread.stopRunning();
        try {
            mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        if (mThread!=null) mThread.pauseRunning();
    }

    public void unpause() {
        if (mThread!=null) mThread.unpauseRunning();
    }


    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                //System.out.println(e.getAction());

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

//                // reverse direction of rotation above the mid-line
//                if (y > getHeight() / 2) {
//                    dx = dx * -1 ;
//                }
//
//                // reverse direction of rotation to left of the mid-line
//                if (x < getWidth() / 2) {
//                    dy = dy * -1 ;
//                }
                for (Drawgorythm d: drawers) {
                    d.touched(e.getAction(), x, y, dx, dy);
                }
                break;
            case MotionEvent.ACTION_UP:
                for (Drawgorythm d: drawers) {
                    d.touched(e.getAction(), x, y, 0, 0);
                }
                break;

            case MotionEvent.ACTION_DOWN:

        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }


    class GraphicDmnThread extends Thread {

        /** Message handler used by thread to interact with TextView */
        private final Handler mHandler;
        /** Handle to the surface manager object we interact with */
        private final SurfaceHolder mSurfaceHolder;

        //private final Context mContext;


        private boolean mPaused = false;
        private boolean mRun = false;


        public GraphicDmnThread(final SurfaceHolder surfaceHolder,
                                final Handler handler) {
            // get handles to some important objects
            mSurfaceHolder = surfaceHolder;
            mHandler = handler;
        }

        @Override
        public void run() {
            Log.d("GraphicDmnThread", "run");
            mRun = true;
            long lasttime = System.currentTimeMillis();
            while (mRun) {
                if (mPaused) {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    final long now = System.currentTimeMillis();

                    if (now - lasttime > step) {
                        Canvas c = null;
                        try {
                            c = mSurfaceHolder.lockCanvas();
                            synchronized (mSurfaceHolder) {

                                doDraw(c, System.currentTimeMillis() - lasttime);
                            }
                        } finally {
                            if (c != null) {
                                mSurfaceHolder.unlockCanvasAndPost(c);
                            }
                        }
                        lasttime = System.currentTimeMillis();
                    } else {
//                        try {
//                            sleep(step);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                    }
                }
            }
        }


        public void pauseRunning() {
            mPaused = true;
        }

        public void unpauseRunning() {
            mPaused = false;
        }



        public void stopRunning() {
            Log.d("GraphicDmnThread", "stopRunning");
            mRun = false;
        }

        public boolean isRunning() {
            return mRun;
        }
    }


    private void doDraw(final Canvas canvas, long ticks) {
        // Log.d("GraphicDmn", "doDraw");

            canvas.drawPaint(mBgColor);
            //Log.d("GraphicDmn", "doDraw" + lasttime + " " + now);
            for (Drawgorythm d : drawers) {
                d.doDraw(canvas, ticks);
            }


       // canvas.drawBitmap(viewdata, 0, 0, null);

    }


///////////////////////////
////barbed circle
//    double r = 0;
//    double rad = 0;
//    private void doDraw(final Canvas canvas) {
//       // Log.d("GraphicDmn", "doDraw");
//
//
//
//        if (rad/Math.PI < Math.PI*10) {
//            float mx = canvas.getWidth() / 2;
//            float my = canvas.getHeight() / 2;
//
//            r = Math.min(canvas.getWidth(), canvas.getHeight()) / 3;
//
//            for (int i = 0; i < 2; i++) {
//                double rad1 = rad;
//                if (i == 1) rad1 = i * Math.PI - rad;
//                float x = (float) (r * Math.sin(rad1));
//                float y = (float) (r * Math.cos(rad1));
//                double rnd = Math.random();
//                int sizex = (int) (r / 10 * Math.sin(rad1*50)) + 1;
//                int sizey = (int) (r / 10 * Math.cos(rad1*50)) + 1;
//                canvas.drawLine(mx + x, my + y, mx + x + sizex, my + y + sizey, mLinePaint);
//            }
//
//            rad += .05;
//        }
//
//    }


/////////////////////////
//penta
//    double r = 0;
//    double rad = 0;
//    float lastX = 0;
//    float lastY = 0;
//    private void doDraw(final Canvas canvas) {
//        // Log.d("GraphicDmn", "doDraw");
//
//        float mx = canvas.getWidth() / 2;
//        float my = canvas.getHeight() / 2;
//
//
//        if (rad/Math.PI < Math.PI*10) {
//
//            r = Math.min(canvas.getWidth(), canvas.getHeight()) / 3;
//
//            float x = (float) (r * Math.sin(rad)) + mx;
//            float y = (float) (r * Math.cos(rad)) + my;
//            if (lastX != 0 && lastY != 0) {
//                canvas.drawLine(x,y, lastX, lastY, mLinePaint);
//               // canvas.drawLine(x,y, mx, my, mLinePaint);
//            }
//
//            lastX = x;
//            lastY = y;
//
//            rad += Math.PI * 2.0 / 5.0;
//
//        }
//
//
//    }

/////////////////////////
//fuzzy circle
//    double r = 0;
//    double rad = 0;
//    private void doDraw(final Canvas canvas) {
//       // Log.d("GraphicDmn", "doDraw");
//
//
//
//        if (rad/Math.PI < Math.PI*10) {
//            float mx = canvas.getWidth() / 2;
//            float my = canvas.getHeight() / 2;
//
//            r = Math.min(canvas.getWidth(), canvas.getHeight()) / 3;
//
//            for (int i = 0; i < 2; i++) {
//                double rad1 = rad;
//                if (i == 1) rad1 = i * Math.PI - rad;
//                float x = (float) (r * Math.sin(rad1));
//                float y = (float) (r * Math.cos(rad1));
//                double rnd = Math.random();
//                int sizex = (int) (rnd * 4 * r / 10 * Math.sin(rad1)) + 1;
//                int sizey = (int) (rnd * 4 * r / 10 * Math.cos(rad1)) + 1;
//                canvas.drawLine(mx + x, my + y, mx + x + sizex, my + y + sizey, mLinePaint);
//            }
//
//            rad += .05;
//        }
//
//    }

}
