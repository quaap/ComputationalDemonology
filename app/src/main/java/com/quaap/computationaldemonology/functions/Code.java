package com.quaap.computationaldemonology.functions;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.quaap.computationaldemonology.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by tom on 12/6/16.
 */

public class Code extends Drawgorythm  {
    String hp;
    String lsp;
    String [] codes;
    String [] nouns;
    String [] lc;
    String [] occult;
    String [] hwords;


    Paint mTextPaint;

    int theight;

    long tickspast = 0;

    int numlines = 7;
    List<String> hists = new LinkedList<>();

    public Code(Context context) {
        super(context);
        hp = context.getString(R.string.htmlparse);
        lsp = context.getString(R.string.lisp1);
        lc = context.getString(R.string.lovecraft).split(" ");
        codes = context.getString(R.string.code).split(" ");
        nouns = context.getString(R.string.nouns).split(" ");
        occult = context.getString(R.string.occult).split(" ");
        hwords = context.getString(R.string.hwords).split(" ");


        mTextPaint = new Paint();
        mTextPaint.setARGB(90, 128, 255, 128);
        mTextPaint.setTextSize(28);
        mTextPaint.setTypeface(Typeface.MONOSPACE);
        theight = (int) (mTextPaint.descent() - mTextPaint.ascent());
        makeText();
    }
    @Override
    public void doDraw(Canvas canvas, long ticks) {

        tickspast += ticks;
        if (tickspast > 100) {
            makeText();
            tickspast = 0;
        }
/*        int pos1 = 0;
        int pos2 = 0;
        int y = mHeight - theight * numlines;
        for (int i = 0; i < numlines; i++) {
            pos2 = hist.indexOf("\n", pos1) + 1;
            if (pos2 > 0) {
                canvas.drawText(hist, pos1, pos2, 10, y, mTextPaint);
                y += theight;
                pos1 = pos2;
            } else {
                break;
            }
        }*/

        int y = mHeight - theight * numlines;
        for (int i = 0; i < numlines; i++) {

            canvas.drawText(hists.get(i), 10, y, mTextPaint);
            y += theight;
        }
    }

    int low1=Integer.parseInt("2200", 16);
    int high1=Integer.parseInt("22FF", 16);

    public void makeText() {
        if (hists.size()>numlines*3) {
            hists.remove(0);
            return;
        }

        for (int i = 0; i < numlines*10; i++) {
            StringBuilder hist = new StringBuilder(2048);

            if (Math.random() > .98) {
                hist.append("-{{{");
                hist.append(codes[(int) (Math.random() * codes.length)].toUpperCase());
                hist.append("  ");
                hist.append(nouns[(int) (Math.random() * nouns.length)].toUpperCase());
                if (Math.random() > .3) {
                    hist.append("  ");
                    hist.append(nouns[(int) (Math.random() * nouns.length)].toUpperCase());
                }
                hist.append("}}}- ");
            } else if (Math.random() > .8) {
                    int rnd = (int) (Math.random() * (hp.length() - 20));
                    hist.append(hp.substring(rnd, rnd + 20));
                    hist.append(" ");
            } else if (Math.random() > .9) {
                    hist.append(lc[(int)(Math.random()*lc.length)]);
                    hist.append(" ");

            } else if (Math.random() > .85) {
                    int rnd = (int) (Math.random() * (lsp.length() - 20));
                    hist.append(lsp.substring(rnd, rnd + 20));
                    hist.append(" ");
            }

            if (Math.random() > .5) {
                hist.append(occult[(int) (Math.random() * occult.length)]);
                hist.append(" ");
                hist.append(hwords[(int) (Math.random() * hwords.length)]);
                hist.append(" ");
            }
            if (Math.random() > .3) {
                hist.append(new String(Character.toChars((int) (Math.random() * (high1 - low1) + low1))));
                hist.append((char) (Math.random() * 26 + 65));
                for (int j = 0; j < Math.random() * 6 + 3; j++) {
                    hist.append((char) (Math.random() * 56 + 65));
                    hist.append((char) (Math.random() * 56 + 65));
                }
                hist.append(" ");
            }
            if (Math.random() > .5) {
                if (Math.random() > .5) {
                    hist.append(new String(Character.toChars((int) (Math.random() * (high1 - low1) + low1))));
                    hist.append(occult[(int) (Math.random() * occult.length)]);
                    hist.append(new String(Character.toChars((int) (Math.random() * (high1 - low1) + low1))));
                    hist.append(" ");
                }
                hist.append(String.format("%6.3e", Math.random() * 100));
                hist.append(" ");
            }
            if (Math.random() > .5) {
                hist.append(new String(Character.toChars((int) (Math.random() * (high1 - low1) + low1))));
                hist.append(new String(Character.toChars((int) (Math.random() * (high1 - low1) + low1))));
                hist.append(" ");
            }



            hists.add(hist.toString());
        }

    }
}
