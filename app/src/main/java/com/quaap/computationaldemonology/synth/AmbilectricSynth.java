package com.quaap.computationaldemonology.synth;

/**
 * Created by tom on 12/7/16.
 */

public class AmbilectricSynth extends Synth {

    int window = 100;
    long total = 0;

    @Override
    protected int getData(short[] data) {


        int doclick = (int)(Math.random()*data.length * mSnipsPerSample - data.length*(mSnipsPerSample-1));

        float periodchirp = (float)(sampleRate / (Math.random()*800+800));
        if (doclick>=window && doclick+window<data.length) {
            double vol = Math.random();
            for (int i = doclick - window; i < doclick + window; i++) {
                double sawval = 2 * ((i % periodchirp) / periodchirp - 1) * Short.MAX_VALUE/(vol*20 + 13);
                data[i] = (short) (sawval * (Math.abs(Math.sin(total/15000))+.2));
            }
        }

        float period1 = sampleRate / 61;
        float period2 = sampleRate / 52;


        for (int i=0; i<data.length; i++) {
            total++;
            double sawval1 = 2 * ((total % period1) / period1 - 1) * Short.MAX_VALUE/9;
            double sawval2 = 2 * ((total % period2) / period2 - 1) * Short.MAX_VALUE/9;
            double staticval = (Math.random() - .5) * Short.MAX_VALUE/25 ;

            double fval = sawval1/3 * (Math.abs(Math.sin(total/30000.0))+1)
                        + sawval2/3 * (Math.abs(Math.sin(total/35000.0)))
                        + staticval/3 * (Math.abs(Math.cos(total/20000.0))+1);

            data[i] += (short)fval;

        }

        return data.length;
    }
}
