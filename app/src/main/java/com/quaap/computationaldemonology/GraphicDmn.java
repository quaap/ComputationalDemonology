package com.quaap.computationaldemonology;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.quaap.computationaldemonology.functions.BarbedRing;
import com.quaap.computationaldemonology.functions.CloudChamber;
import com.quaap.computationaldemonology.functions.Code;
import com.quaap.computationaldemonology.functions.Drawgorythm;
import com.quaap.computationaldemonology.functions.FuzzyRing;
import com.quaap.computationaldemonology.functions.PentaRing;
import com.quaap.computationaldemonology.functions.PentaStar;
import com.quaap.computationaldemonology.functions.TouchLightning;
import com.quaap.computationaldemonology.functions.Worms;
import com.quaap.computationaldemonology.synth.AmbilectricSynth;
import com.quaap.computationaldemonology.synth.Synth;
import com.quaap.computationaldemonology.util.Rand;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by tom on 12/2/16.
 *
 *    Copyright (C) 2016   Tom Kliethermes
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 */

public class GraphicDmn extends SurfaceView implements  SurfaceHolder.Callback, MediaPlayer.OnPreparedListener{

    private final long step = 50;

    private GraphicDmnThread mThread;

    private final Paint mLinePaint;

    private final Paint mBgColor;

    final List<Drawgorythm> drawers = new ArrayList<>();


    MediaPlayer[] mplayers;

    final Random rand = new Random();

    private Synth synth;
    private long iterations = 0;

    private long totalticks = 0;
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



//        Synth mSynth = new Synth();
//        mSynth.genSaw(64);
       // blackCanvas(holder);

    }

    public static final String GO = "go";
    public static final int PROTECTION = 1;
    public static final int SUMMON = 2;
    public static final int EXPEL = 3;
    public static final int CAST = 4;
    public static final int WORMS = 5;
    public static final int CLOUD = 6;
    public static final int METHODS = 7;

    private static final int [] soundRes = {
            R.raw.ahohow,
            R.raw.breath1,
            R.raw.breath1_slow,
            R.raw.cleak1,
            R.raw.cleak1_slow,
            R.raw.cleak2,
            R.raw.cleak2_slow,
            R.raw.cleak3,
            R.raw.cleak3_slow,
            R.raw.daddyphone1,
            R.raw.daddyphone2,
            R.raw.daddyphonerev,
            R.raw.daddyphonerev_slow,
            R.raw.eeeeh1,
            R.raw.getout,
            R.raw.getout_slow,
            R.raw.growl1,
            R.raw.growl1_slow,
            R.raw.growl2,
            R.raw.growl2_slow,
            R.raw.moan1,
            R.raw.moan1_slow,
            R.raw.moan2,
            R.raw.moan2_slow,
            R.raw.ooooowh,
            R.raw.ooouuh,
            R.raw.skreeooo,
            R.raw.skreeooo_slow,
            R.raw.touteg,
            R.raw.touteg_slow,
            R.raw.wheoh,
            R.raw.wheoh_slow
    };

    private int mMethod = 0;
    public void startDraw(int which) {
        mMethod = which;

        if (mMethod!=CLOUD) {
            Drawgorythm d0 = new Code(getContext(), mMethod);
            d0.setPaints(mLinePaint, mBgColor);
            drawers.add(d0);
        }

        Drawgorythm d = null;

        switch (which) {
            case PROTECTION: d = new BarbedRing(getContext()); break;
            case EXPEL: d = new FuzzyRing(getContext()); break;
            case CAST: d = new PentaRing(getContext()); break;
            case SUMMON: d = new PentaStar(getContext()); break;
            case WORMS: d = new Worms(getContext()); break;
            case CLOUD: d = new CloudChamber(getContext()); break;
            default:
                throw new IllegalArgumentException("No such drawgorithm " + which);
        }
        d.setPaints(mLinePaint, mBgColor);
        drawers.add(d);
        Drawgorythm d2 = new TouchLightning(getContext());
        d2.setPaints(mLinePaint, mBgColor);
        drawers.add(d2);
    }


    private AsyncTask<Void,Void,Void> audioloader;
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d("GraphicDmn", "surfaceCreated");

        synth = new AmbilectricSynth();
        synth.setVol(.008f);

        mThread = new GraphicDmnThread(surfaceHolder);

        final Context context = getContext();

        audioloader = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mplayers = new MediaPlayer[soundRes.length];
                for (int r = 0; r< soundRes.length; r++) {
                    mplayers[r] = MediaPlayer.create(context, soundRes[r]);
                    mplayers[r].setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mplayers[r].setVolume(.3f,.3f);
                }
                return null;
            }
        };
        audioloader.execute();


    }

    @Override
    public void surfaceChanged(final SurfaceHolder surfaceHolder, final int format, final int width, final int height) {
        Log.d("GraphicDmn", "surfaceChanged");
        iterations = 0;

        totalticks = 0;
        if (mThread!=null) {
            if (!mThread.isRunning()) {
                mThread.start();
            }
            if (!synth.isRunning()) {
                synth.start();
            }

            Canvas c = null;
            try {
                c = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder) {
//                    viewdata = Bitmap.createBitmap(c.getWidth(), c.getHeight(), Bitmap.Config.ARGB_8888);
//                    passCanvas = new Canvas(viewdata);
//                    passCanvas.drawPaint(mBgColor);

                    for(Drawgorythm d: drawers) {
                        d.canvasChanged(c);
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

        if (audioloader!=null) {
            audioloader.cancel(true);
        }

        mThread.stopRunning();
        try {
            mThread.join();
            mThread = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (MediaPlayer m: mplayers) {
            if (m!=null) {
                if (m.isPlaying()) {
                    m.stop();
                }
                m.release();
            }
        }

        synth.stopSynth();
        synth = null;

    }

    public void pause() {
        if (mThread!=null) mThread.pauseRunning();
        if (synth!=null) {
            synth.pauseSynth();
        }
    }

    public void unpause() {
        if (mThread!=null) mThread.unpauseRunning();


        if (synth!=null) {
            if (!synth.isRunning()) {
                synth.start();
            } else {
                synth.unpauseSynth();
            }
        }
    }


    public void deviceMoved(float x, float y, float z) {
        for (Drawgorythm d: drawers) {
            d.deviceMoved(x, y, z);
        }
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

                for (Drawgorythm d: drawers) {
                    d.touched(e.getAction(), x, y, dx, dy);
                }
                break;
            case MotionEvent.ACTION_UP:
                for (Drawgorythm d: drawers) {
                    d.touched(e.getAction(), 0, 0, 0, 0);
                }
                break;

            case MotionEvent.ACTION_DOWN:

        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

    }


    class GraphicDmnThread extends Thread {

        /** Handle to the surface manager object we interact with */
        private final SurfaceHolder mSurfaceHolder;

        //private final Context mContext;


        private boolean mPaused = false;
        private boolean mRun = false;


        public GraphicDmnThread(final SurfaceHolder surfaceHolder) {
            // get handles to some important objects
            mSurfaceHolder = surfaceHolder;
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
        iterations++;
        totalticks += ticks;

        canvas.drawPaint(mBgColor);
        //Log.d("GraphicDmn", "doDraw" + lasttime + " " + now);
        for (Drawgorythm d : drawers) {
            d.doDraw(canvas, ticks);
        }

//        if (Math.random()>(1.0/iterations+.8)) {
//            canvas.drawColor(mLinePaint.getColor(), PorterDuff.Mode.MULTIPLY);
//        }

        if (synth.getVol()<.2f) {
            synth.setVol(.2f * totalticks/10000);
//            if (totalticks > 2000 && synth.getVol() < .04f) {
//                synth.setVol(.05f);
//            }
//            if (totalticks > 4000 && synth.getVol() < .09f) {
//                synth.setVol(.1f);
//            }
//            if (totalticks > 8000 && synth.getVol() < .19f) {
//                synth.setVol(.2f);
//            }
        }
      //  synth.setVol(1 - 100.0f/iterations);
       // canvas.drawBitmap(viewdata, 0, 0, null);

        if (totalticks> 10000 && Rand.chance(.5)) {
            //int num = (Rand.getInt(METHODS)+1) + mplayers.length/METHODS * mMethod;
            int num = Rand.getInt(mplayers.length);
            MediaPlayer m = mplayers[num];
            if (!m.isPlaying()) {
                if (rand.nextFloat()>.8) {
                    for (MediaPlayer m2: mplayers) {
                        m2.setLooping(false);
                    }
                    m.setLooping(true);
                }
                m.start();
            } else if (m.isLooping()) {
                m.setLooping(false);
            }
        }

    }


}
