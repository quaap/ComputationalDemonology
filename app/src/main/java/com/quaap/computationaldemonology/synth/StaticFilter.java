package com.quaap.computationaldemonology.synth;


/**
 * Created by tom on 12/6/16.
 */

public class StaticFilter implements Filter {
    @Override
    public int filter(short[] data, int length) {

        for (int i = 0; i < length; i++) {
            data[i] = (short) ( data[i]/2 +  data[i]*(Math.random() - .5)/4 );
        }

        return length;
    }
}
