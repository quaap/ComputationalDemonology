package com.quaap.computationaldemonology.functions;

import android.content.Context;
import android.graphics.Canvas;

/**
 * Created by tom on 12/6/16.
 */

public class PentaRing extends PentaStar {


    public PentaRing(Context context) {
        super(context);
    }

    protected void incRad() {
        rad += Math.PI * 2.0 / 5.0;
    }

}