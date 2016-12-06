package com.quaap.computationaldemonology.synth;

/**
 * Created by tom on 12/6/16.
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
