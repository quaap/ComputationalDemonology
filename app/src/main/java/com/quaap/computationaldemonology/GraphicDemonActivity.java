package com.quaap.computationaldemonology;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.os.Bundle;
import android.util.Log;


public class GraphicDemonActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mRotation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic_demon);

        GraphicDmn dmnview = (GraphicDmn) findViewById(R.id.dmnview);

        int which = getIntent().getIntExtra(GraphicDmn.GO, 7);
        dmnview.startDraw(which);

        //unpause();

    }


    private void pause() {
        mSensorManager.unregisterListener(this);
        GraphicDmn dmnview = (GraphicDmn) findViewById(R.id.dmnview);
        dmnview.pause();

    }

    private void unpause() {

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mRotation = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        if (mAccelerometer!=null) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        }

        if (mRotation!=null) {
            mSensorManager.registerListener(this, mRotation, SensorManager.SENSOR_DELAY_NORMAL);
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


    private static final int FROM_RADS_TO_DEGS = -57;

    private void update(float[] vectors) {
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, vectors);
        float[] adjustedRotationMatrix = new float[9];
        SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, adjustedRotationMatrix);
        float[] orientation = new float[3];
        SensorManager.getOrientation(adjustedRotationMatrix, orientation);


        float fac = (float)(180/Math.PI);
        float yaw = orientation[0]*fac;
        float pitch = orientation[1]*fac;
        float roll = orientation[2]*fac;

        GraphicDmn dmnview = (GraphicDmn) findViewById(R.id.dmnview);
        dmnview.deviceRotated(yaw, pitch, roll);

//        Log.d("dmn","Pitch: " + pitch);
//        Log.d("dmn","Roll: " + roll);
//        Log.d("dmn","Yaw: " + yaw);
    }


    private float mLastX=Float.MAX_VALUE, mLastY, mLastZ;

    private float minAcc = 0.1f;
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor == mAccelerometer) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            if (mLastX == Float.MAX_VALUE) {
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
        } else if (sensorEvent.sensor == mRotation) {
            if (sensorEvent.values.length > 4) {
                float[] truncatedRotationVector = new float[4];
                System.arraycopy(sensorEvent.values, 0, truncatedRotationVector, 0, 4);
                update(truncatedRotationVector);
            } else {
                update(sensorEvent.values);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
