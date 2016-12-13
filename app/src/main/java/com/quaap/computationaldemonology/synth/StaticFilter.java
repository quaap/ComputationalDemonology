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

public class StaticFilter implements Filter {
    @Override
    public int filter(short[] data, int length) {

        for (int i = 0; i < length; i++) {
            data[i] = (short) ( data[i]/1.5 +  data[i]*(Rand.getDoubleNeg1To1()/2)/4 );
        }

        return length;
    }
}
