package com.quaap.computationaldemonology;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
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
 */

public class GraphicDmn extends SurfaceView implements  SurfaceHolder.Callback, MediaPlayer.OnPreparedListener{

    private long step = 50;

    private GraphicDmnThread mThread;

    private final Paint mLinePaint;

    private final Paint mBgColor;

    List<Drawgorythm> drawers = new ArrayList<>();


    MediaPlayer[] mplayers;

    Random rand = new Random();

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


    public void startDraw(int which) {

        Drawgorythm d0 = new Code(getContext(), which);
        d0.setPaints(mLinePaint, mBgColor);
        drawers.add(d0);

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


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d("GraphicDmn", "surfaceCreated");

        synth = new AmbilectricSynth();
        synth.setVol(.01f);

        mThread = new GraphicDmnThread(surfaceHolder, new Handler() {
            @Override
            public void handleMessage(final Message m) {
                // mStatusText.setVisibility(m.getData().getInt("viz"));
                //  mStatusText.setText(m.getData().getString("text"));
            }
        });

        int [] rs = {R.raw.wheoh, R.raw.daddyphonerev, R.raw.getout, R.raw.touteg};
        mplayers = new MediaPlayer[rs.length];
        for (int r=0; r<rs.length; r++) {
            mplayers[r] = MediaPlayer.create(getContext(),rs[r]);
            mplayers[r].setAudioStreamType(AudioManager.STREAM_MUSIC);
            mplayers[r].setVolume(.4f,.4f);
//            mplayers[r].setOnPreparedListener(this);
//            mplayers[r].prepareAsync();
        }

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
        mThread.stopRunning();
        try {
            mThread.join();
            mThread = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (MediaPlayer m: mplayers) {
            if (m.isPlaying()) {
                m.stop();
            }
            m.release();
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
            MediaPlayer m = mplayers[rand.nextInt(mplayers.length)];
            if (!m.isPlaying()) {
                if (rand.nextFloat()>.8) {
                    m.setLooping(true);
                }
                m.start();
            }
            if (m.isLooping()) {
                m.setLooping(false);
            }
        }

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
