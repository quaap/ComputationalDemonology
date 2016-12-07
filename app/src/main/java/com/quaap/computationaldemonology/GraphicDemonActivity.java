package com.quaap.computationaldemonology;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class GraphicDemonActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic_demon);
        GraphicDmn dmnview = (GraphicDmn) findViewById(R.id.dmnview);

        int which = getIntent().getIntExtra(GraphicDmn.GO, 7);
        dmnview.startDraw(which);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (mAccelerometer!=null) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }


       // startTicker();

    }



//    public Handler mHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            TextView mathtxt = (TextView)findViewById(R.id.mathtxt);
//
//           // mathtxt.setText(hist);
//        }
//    };
//
//    private synchronized void startTicker() {
//        if (timer==null) {
//            timer = new Timer();
//            timer.scheduleAtFixedRate(new TimerTask() {
//                public void run() {
//
//                    mHandler.obtainMessage(1).sendToTarget();
//                }
//            }, 0, 100);
//        }
//    }
//
//    private synchronized void stopTicker() {
//        if (timer!=null) {
//            timer.cancel();
//            timer = null;
//        }
//    }

    @Override
    protected void onStart() {
        super.onStart();
        GraphicDmn dmnview = (GraphicDmn) findViewById(R.id.dmnview);
        dmnview.unpause();
     //   startTicker();
    }

    @Override
    protected void onStop() {
        GraphicDmn dmnview = (GraphicDmn) findViewById(R.id.dmnview);
        dmnview.pause();
        super.onStop();
    }
    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
    //    stopTicker();
        mSensorManager.unregisterListener(this);
        GraphicDmn dmnview = (GraphicDmn) findViewById(R.id.dmnview);
        dmnview.pause();
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        GraphicDmn dmnview = (GraphicDmn) findViewById(R.id.dmnview);
        dmnview.unpause();
        if (mAccelerometer!=null) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
     //   startTicker();
    }

    private float mLastX=Float.MAX_VALUE, mLastY, mLastZ;

    private float minAcc = 0.1f;
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        if (mLastX==Float.MAX_VALUE) {
            mLastX = x;
            mLastY = y;
            mLastZ = z;
        } else {
           // if (Math.abs(mLastX - x)>minAcc || Math.abs(mLastY - y)>minAcc || Math.abs(mLastZ - z)>minAcc ) {
                GraphicDmn dmnview = (GraphicDmn) findViewById(R.id.dmnview);
                dmnview.deviceMoved(mLastX - x, mLastY - y, mLastZ - z);
                mLastX = x;
                mLastY = y;
                mLastZ = z;
          //  }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
