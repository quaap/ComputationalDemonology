package com.quaap.computationaldemonology.synth;

import com.quaap.computationaldemonology.util.Rand;

/**
 * Created by tom on 12/6/16.
 */
public class StaticSynth extends Synth {
    protected int getData(short[] data) {

        for (int i = 0; i < data.length; i++) {
            data[i] = (short) (Rand.getDoubleNeg1To1() * Short.MAX_VALUE );
        }
        return data.length;
    }

}
