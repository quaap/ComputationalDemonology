package com.quaap.computationaldemonology.synth;

import java.util.Arrays;

/**
 * Created by tom on 12/6/16.
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
public class MultiToneSynth extends Synth {

    private float [] mHzs;
    private final Object mHzsSync = new Object();

    protected int [] lastPos;

    public MultiToneSynth(float ... hzs) {
        setHz(hzs);
    }

    public void setHz(float [] hzs) {
        synchronized(mHzsSync) {
            mHzs = hzs;
            lastPos = new int[hzs.length];
        }
    }

    public void modHz(float [] hzs) {
        for (int i = 0; i < mHzs.length; i++) {
            mHzs[i] += hzs[i];
        }
    }

    public void modHz(float hz) {
        for (int i = 0; i < mHzs.length; i++) {
            mHzs[i] += hz;
        }
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

        int length = 0;
        float [] hzs;
        synchronized(mHzsSync) {
            hzs = Arrays.copyOf(mHzs, mHzs.length); //
            for (int i = 0; i < hzs.length; i++) {
                int written = addData(data, hzs[i], i, hzs.length);
                if (written > length) length = written;
            }
        }

        return length;
    }


}
