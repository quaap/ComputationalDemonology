package com.quaap.computationaldemonology.synth;


import com.quaap.computationaldemonology.util.Rand;

/**
 * Created by tom on 12/6/16.
 */

public class StaticFilter implements Filter {
    @Override
    public int filter(short[] data, int length) {

        for (int i = 0; i < length; i++) {
            data[i] = (short) ( data[i]/1.5 +  data[i]*(Rand.getDoubleNeg1To1()/2)/4 );
        }

        return length;
    }
}
