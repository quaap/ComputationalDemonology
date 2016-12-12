package com.quaap.computationaldemonology.functions;

import android.content.Context;

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

public class PentaRing extends PentaStar {


    public PentaRing(Context context) {
        super(context);
    }

    protected void incRad() {
        rad += Math.PI * 2.0 / 5.0;
    }

}