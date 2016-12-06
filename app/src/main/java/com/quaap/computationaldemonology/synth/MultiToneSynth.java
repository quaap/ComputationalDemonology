package com.quaap.computationaldemonology.synth;

import java.util.Arrays;

/**
 * Created by tom on 12/6/16.
 */
public class MultiToneSynth extends Synth {

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
