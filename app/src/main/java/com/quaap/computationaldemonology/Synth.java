package com.quaap.computationaldemonology;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.PlaybackParams;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by tom on 12/2/16.
 */

public abstract class Synth extends Thread {

    protected int sampleRate = 16000;
    protected int mSnipsPerSample = 5;
    private AudioTrack mAudioTrack;

    private boolean mRun = false;

    protected float mVol = 1f;

    public Synth() {

    }

    @Override
    public void run() {
        mRun = true;
        int bufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        try {
            mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);
            mAudioTrack.play();
            int entries = (int)(sampleRate/mSnipsPerSample);
            short [] data = new short[entries];

            while (mRun) {
                getData(data);
                write(data);
            }
        } finally {
            close();
        }
    }

    public float getMaxVol() {
        return AudioTrack.getMaxVolume();
    }

    public float getVol() {
        return mVol;
    }

    public void setVol(float vol) {
        mVol = vol;
    }

    public void stopSynth() {
        mRun = false;
    }

    private void write(short[] buffer) {
        if (Build.VERSION.SDK_INT >= 21) {
            mAudioTrack.setVolume(mVol);
        } else {
            mAudioTrack.setStereoVolume(mVol,mVol);
        }
        mAudioTrack.write(buffer, 0, buffer.length);
    }

    public void close() {
        if (mAudioTrack!=null) {
            try {
                mAudioTrack.pause();
                mAudioTrack.flush();
            } finally {
                mAudioTrack.release();
            }
            mAudioTrack=null;
        }

    }


    protected abstract int getData(short[] data);

}

class StaticSynth extends Synth {
    protected int getData(short[] data) {

        for (int i = 0; i < data.length; i++) {
            data[i] = (short) ((Math.random() - .5) * Short.MAX_VALUE * 2 * i / (float) data.length);
        }
        return data.length;
    }

}


class MultiToneSynth extends Synth {

    private float [] mHzs;

    protected int [] lastPos;

    public MultiToneSynth(float ... hzs) {
        setHz(hzs);
    }

    public void setHz(float [] hzs) {
        mHzs = hzs;
        lastPos = new int[hzs.length];
    }

    protected int addData(short[] data, float hz, int trackNum, int numTacks) {
        float period = sampleRate / hz;
        for (int i = 0; i < data.length; i++) {
            double val = Math.sin((lastPos[trackNum]++) / period * 2 * Math.PI);
            data[i] += (short) (val * Short.MAX_VALUE / (double)numTacks);
        }
        return data.length;
    }


    protected int getData(short[] data) {

        if (lastPos[0]>Integer.MAX_VALUE-sampleRate*10) {
            Arrays.fill(lastPos,0);
        }
        Arrays.fill(data, (short)0);

        int length = 0;
        for (int i=0; i<mHzs.length; i++) {
            int written = addData(data, mHzs[i], i, mHzs.length);
            if (written>length) length = written;
        }

        return length;
    }


}


class SawSynth extends MultiToneSynth {

    public SawSynth(float ... hzs) {
        setHz(hzs);
    }

    @Override
    protected int addData(short[] data, float hz, int trackNum, int numTacks) {

        float period = sampleRate / hz;

        for (int i = 0; i < data.length; i++) {
            float val = 2 * ((lastPos[trackNum]++) % period) / period - 1;

            data[i] += (short) (val * Short.MAX_VALUE / numTacks);
        }

        return data.length;
    }


}
