package com.quaap.computationaldemonology.synth;

import com.quaap.computationaldemonology.util.Rand;

/**
 * Created by tom on 12/7/16.
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

public class AmbilectricSynth extends Synth {

    final int window = 100;
    long total = 0;

    @Override
    protected int getData(short[] data) {


        int doclick = (int)(Math.random()*data.length * mSnipsPerSample - data.length*(mSnipsPerSample-1));

        float periodchirp = (float)(sampleRate / Rand.getDouble(800,1600));
        if (doclick>=window && doclick+window<data.length) {
            double vol = Rand.getDouble();
            for (int i = doclick - window; i < doclick + window; i++) {
                double sawval = 2 * ((i % periodchirp) / periodchirp - 1) * Short.MAX_VALUE/(vol*20 + 13);
                data[i] = (short) (sawval * (Math.abs(Math.sin(total/15000))+.2));
            }
        }

        float period1 = sampleRate / 61;
        float period2 = sampleRate / 52;


        for (int i=0; i<data.length; i++) {
            total++;
            double sawval1 = 2 * ((total % period1) / period1 - 1) * Short.MAX_VALUE/13;
            double sawval2 = 2 * ((total % period2) / period2 - 1) * Short.MAX_VALUE/13;
            double staticval = (Rand.getDoubleNeg1To1()/2) * Short.MAX_VALUE/25 ;

            double fval = sawval1/3 * (Math.abs(Math.sin(total/30000.0))+1)
                        + sawval2/3 * (Math.abs(Math.sin(total/35000.0)))
                        + staticval/3 * (Math.abs(Math.cos(total/20000.0))+1);

            data[i] += (short)fval;

        }

        return data.length;
    }
}
