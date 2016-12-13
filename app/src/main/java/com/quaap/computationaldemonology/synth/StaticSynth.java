package com.quaap.computationaldemonology.synth;

import com.quaap.computationaldemonology.util.Rand;

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
public class StaticSynth extends Synth {
    protected int getData(short[] data) {

        for (int i = 0; i < data.length; i++) {
            data[i] = (short) (Rand.getDoubleNeg1To1() * Short.MAX_VALUE );
        }
        return data.length;
    }

}
