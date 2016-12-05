package com.quaap.computationaldemonology;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;

/**
 * Created by tom on 12/2/16.
 */

public class Synth {

    private int sampleRate = 16000;
    AudioTrack mAudioTrack;

    public Synth() {
        int bufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);

        mAudioTrack.play();
    }

    public void write(short[] buffer) {
        mAudioTrack.write(buffer, 0, buffer.length);
    }

    public void close() {
        try {
            mAudioTrack.pause();
            mAudioTrack.flush();
        } finally {
            mAudioTrack.release();
        }

    }



    public void genStatic() {
        int entries = sampleRate*10;
        short [] data = new short[entries];

        for (int i=0; i<entries; i++) {
            data[i] = (short)((Math.random()-.5)*Short.MAX_VALUE*2 * i/(float)entries);

        }

        write(data);
    }


    public void genSaw(float hz) {

        new AsyncTask<Float, Void, Void>() {
            @Override
            protected Void doInBackground(Float... hzs) {
                for(Float hz: hzs) {

                    float period = sampleRate / hz;

                    int samples = 10;
                    int entries = sampleRate;
                    short[] data = new short[entries];

                    for (int sample=0; sample<samples; sample++) {
                        for (int i = 0; i < entries; i++) {
                            float val = 2 * (i % period) / period - 1;

                            data[i] = (short) (val * Short.MAX_VALUE * (sample*entries+i)/(float)(samples * entries+1));
                        }
                        write(data);
                    }
                }
                return null;
            }
        }.execute(hz);

    }
}
