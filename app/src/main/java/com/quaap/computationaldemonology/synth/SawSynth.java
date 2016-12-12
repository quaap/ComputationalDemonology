package com.quaap.computationaldemonology.synth;

/**
 * Created by tom on 12/6/16.
 *
 *    Copyright (C) 2016  tom
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
public class SawSynth extends MultiToneSynth {

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
