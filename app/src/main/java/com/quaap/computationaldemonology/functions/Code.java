package com.quaap.computationaldemonology.functions;

import android.content.Context;
import android.graphics.Bitmap;
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
    String lc2;
    String [] endwords;
    String [] hwords;




    long tickspast = 0;

    int numlines = 11;
    List<String> hists = new LinkedList<>();
    Paint [] mTextPaint = new Paint[numlines];
    int [] theight = new int[numlines];
    int theighttot = 0;
    Bitmap textarea;
    Canvas textareaCanvas;

    public Code(Context context) {
        super(context);
        hp = context.getString(R.string.htmlparse);
        lsp = context.getString(R.string.lisp1);
        lc = context.getString(R.string.lovecraft).split(" ");
        lc2 = context.getString(R.string.lovecraft2);
        codes = context.getString(R.string.code).split(" ");
        nouns = context.getString(R.string.nouns).split(" ");
        endwords = context.getString(R.string.endwords).split(",\\s*");
        hwords = context.getString(R.string.hwords).split(" ");


        for (int t=0; t<numlines; t++) {
            mTextPaint[t] = new Paint();
            mTextPaint[t].setARGB(90, 128, 255, 128);
            mTextPaint[t].setTextSize(28);
            //mTextPaint[t].setTextSize(18 + t*2);
            //mTextPaint[t].setTextScaleX((numlines - t)/(float)numlines + 1);
            mTextPaint[t].setTypeface(Typeface.MONOSPACE);
            mTextPaint[t].setAlpha((t+1)*(230/(numlines+1)));
            theight[t] = (int) (mTextPaint[t].descent() - mTextPaint[t].ascent());
            theighttot += theight[t];
        }
        makeText();
        //
    }

    @Override
    public void canvasChanged(Canvas canvas) {
        super.canvasChanged(canvas);
        textarea = Bitmap.createBitmap(mWidth, theighttot, Bitmap.Config.ARGB_8888);
        textareaCanvas = new Canvas(textarea);
    }

    int partial = 0;
    @Override
    public void doDraw(Canvas canvas, long ticks) {

        tickspast += ticks;
        if (tickspast > 120) {
            makeText();
            tickspast = 0;
            textareaCanvas.drawPaint(mBackground);
            int y = 0;
            for (int i = 0; i < numlines; i++) {

                textareaCanvas.drawText(hists.get(i), 10, y, mTextPaint[i]);
                y += theight[i];
            }
            partial=0;
        }

        canvas.drawBitmap(textarea, 0, mHeight - theighttot - partial + theight[0]*2, null);

        partial+=theight[0]/3;

    }

    int low1=Integer.parseInt("2200", 16);
    int high1=Integer.parseInt("22FF", 16);

    int dia1 = 768;
//    int dia2 = 879;
    int dia2 = 2042;

    public void makeText() {
        if (hists.size()>numlines*3) {
            hists.remove(0);
            return;
        }

        for (int i = 0; i < numlines*10; i++) {
            StringBuilder hist = new StringBuilder(2048);

            if (Math.random() > .8) {
                hist.append(String.format("%2.1f", Math.random() * 10));
                hist.append(" ");
            }
            if (Math.random() > .5) {
                hist.append(new String(Character.toChars((int) (Math.random() * (high1 - low1) + low1))));
            }
            if (Math.random() > .6) {
                hist.append(String.format("%2.1f", Math.random() * 10));
                hist.append(" ");
            }

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
            } else if (Math.random() > .92) {
                int rnd = (int) (Math.random() * (hp.length() - 20));
                hist.append(hp.substring(rnd, rnd + 20));
                hist.append(" ");
            } else if (Math.random() > .92) {
                int rnd = (int) (Math.random() * (lc2.length() - 20));
                hist.append(lc2.substring(rnd, rnd + 20));
                hist.append(" ");
            } else if (Math.random() > .92) {
                    hist.append(lc[(int)(Math.random()*lc.length)]);
                    hist.append(" ");

            }

            if (Math.random() > .85) {
                    int rnd = (int) (Math.random() * (lsp.length() - 20));
                    hist.append(lsp.substring(rnd, rnd + 20));
                    hist.append(" ");
            }

            if (Math.random() > .5) {
                hist.append(new String(Character.toChars((int) (Math.random() * (high1 - low1) + low1))));
            }
            if (Math.random() > .5) {
                hist.append(hwords[(int) (Math.random() * hwords.length)]);
                hist.append(" ");
                hist.append(nouns[(int) (Math.random() * nouns.length)]);
                hist.append(" ");
            }
            if (Math.random() > .5) {
                hist.append(new String(Character.toChars((int) (Math.random() * (high1 - low1) + low1))));
                hist.append(new String(Character.toChars((int) (Math.random() * (high1 - low1) + low1))));
                hist.append(" ");
            }
            if (Math.random() > .3) {
                hist.append(new String(Character.toChars((int) (Math.random() * (high1 - low1) + low1))));
                hist.append((char) (Math.random() * 26 + 65));
                for (int j = 0; j < Math.random() * 6 + 1; j++) {
                    hist.append((char) (Math.random() * 56 + 65));
                    hist.append((char) (Math.random() * 56 + 65));
                }
                hist.append(" ");
            }
            if (Math.random() > .7) {
                hist.append(new String(Character.toChars((int) (Math.random() * (high1 - low1) + low1))));
                hist.append(endwords[(int) (Math.random() * endwords.length)].toUpperCase());
                hist.append(new String(Character.toChars((int) (Math.random() * (high1 - low1) + low1))));
                hist.append(" ");
            }
            if (Math.random() > .5) {
                hist.append(String.format("%6.3e", Math.random() * 100));
                hist.append(" ");
            }

            int r1 = (int)(Math.random()*2);

            for (int r=0; r<r1; r++) {
                int pos = (int) (Math.random() * (hist.length()-2) + 1);

                hist.insert(pos, new String(Character.toChars((int) (Math.random() * (dia2 - dia1) + dia1))));
            }


            hists.add(hist.toString());
        }

    }
}
