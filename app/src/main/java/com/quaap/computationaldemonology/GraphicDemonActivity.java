package com.quaap.computationaldemonology;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.quaap.computationaldemonology.synth.AmbilectricSynth;
import com.quaap.computationaldemonology.synth.Synth;


public class GraphicDemonActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic_demon);

        GraphicDmn dmnview = (GraphicDmn) findViewById(R.id.dmnview);

        int which = getIntent().getIntExtra(GraphicDmn.GO, 7);
        dmnview.startDraw(which);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //unpause();



    }


    protected void pause() {
        GraphicDmn dmnview = (GraphicDmn) findViewById(R.id.dmnview);
        dmnview.pause();
        mSensorManager.unregisterListener(this);

    }

    protected void unpause() {
        if (mAccelerometer!=null) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        GraphicDmn dmnview = (GraphicDmn) findViewById(R.id.dmnview);
        dmnview.unpause();

    }

    @Override
    protected void onPause() {

        try {
            pause();
        } finally {
            super.onPause();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        unpause();
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
