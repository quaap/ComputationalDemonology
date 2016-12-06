package com.quaap.computationaldemonology.synth;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;

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


