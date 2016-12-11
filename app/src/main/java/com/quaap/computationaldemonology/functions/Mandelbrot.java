package com.quaap.computationaldemonology.functions;

import android.content.Context;
import android.graphics.Canvas;

/**
 * Created by tom on 12/8/16.
 */

public class Mandelbrot extends Drawgorythm {

    public Mandelbrot(Context context) {
        super(context);
    }

    long totalticks=0;
    int max = 1000;
    @Override
    public void doDraw(Canvas canvas, long ticks) {
        int width = mWidth, height = mHeight;

        totalticks += ticks;
        if (totalticks>2000) {
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    double c_re = (col - width / 2) * 4.0 / width;
                    double c_im = (row - height / 2) * 4.0 / width;
                    double x = 0, y = 0;
                    int iterations = 0;
                    while (x * x + y * y < 4 && iterations < max) {
                        double x_new = x * x - y * y + c_re;
                        y = 2 * x * y + c_im;
                        x = x_new;
                        iterations++;
                    }
                    if (iterations < max) canvas.drawPoint(col, row, getRandomForeground());
                    //else image.setRGB(col, row, );
                }
            }
            totalticks=0;
        }
    }


}
