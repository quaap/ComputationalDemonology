package com.quaap.computationaldemonology.synth;

/**
 * Created by tom on 12/6/16.
 */
public class StaticSynth extends Synth {
    protected int getData(short[] data) {

        for (int i = 0; i < data.length; i++) {
            data[i] = (short) ((Math.random() - .5) * Short.MAX_VALUE * 2 * i / (float) data.length);
        }
        return data.length;
    }

}
