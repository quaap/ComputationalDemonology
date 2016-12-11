package com.quaap.computationaldemonology.functions;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.quaap.computationaldemonology.R;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Stack;

import com.quaap.computationaldemonology.util.Rand;
import com.quaap.computationaldemonology.util.Rand.*;

/**
 * Created by tom on 12/6/16.
 */

public class Code extends Drawgorythm  {
    String hp;
    //String lsp;
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

    int mMethod;

    public Code(Context context, int method) {
        super(context);
        hp = context.getString(R.string.htmlparse);
        //lsp = context.getString(R.string.lisp1);
        lc = context.getString(R.string.lovecraft).split(" ");
        lc2 = context.getString(R.string.lovecraft2);
        codes = context.getString(R.string.code).split(" ");
        nouns = context.getString(R.string.nouns).split(" ");
        endwords = context.getString(R.string.endwords).split(",\\s*");
        hwords = context.getString(R.string.hwords).split(" ");

        mMethod = method;

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


    private RandomList<String> mathSyms1 = new RandomList<>("+", "-", "\u00D7", "\u00F7", "=", "0", "1", "NIL");

    private CharRange mathSyms2 = new CharRange(0x2200, 0x22FF);

//    private CharRange mathLetters1 = new CharRange('A', 'Z');
//    private CharRange mathLetters2 = new CharRange('a', 'z');
//    private CharRange mathLetters3 = new CharRange('Α', 'Ω');
//    private CharRange mathLetters4 = new CharRange('α', 'α');

    private CharRange mathLetters1 = new CharRange(0x1D400, 0x1D7FF);
    private CharRange mathLetters2 = new CharRange(0x2100, 0x214F);
    private CharRange mathLetters3 = new CharRange(0x1EE00, 0x1EE7E);

    private CharRange mathShapes1 = new CharRange(0x25A0, 0x25FF);

    private CharRange mathArrows1 = new CharRange(0x2190, 0x21FF);
    private CharRange mathArrows2 = new CharRange(0x27F0, 0x27FF);
    private CharRange mathArrows3 = new CharRange(0x2900, 0x297F);

    private CharRange txtAdditions = new CharRange(768, 2042);



    private Stack<String> pstack = new Stack<>();


    private RandomList<String[]> groupings =
            new RandomList<String[]>(new String [][]
                {
                    {"(",")"}, {"{","}"}, {"[","]"},
                    {"(",")"}, {"{","}"}, {"[","]"},
                    {"⦃","⦄"}, {"⟦","⟧"}, {"⟨","⟩"},
                    {"⟪","⟫"}, {"⦇", "⦈"}
                });



    public void makeText() {
        if (hists.size() > numlines * 3) {
            hists.remove(0);
            return;
        }
        for (int i = 0; i < numlines*10; i++) {
            StringBuilder hist = new StringBuilder(2048);
            if (Rand.chance(1)) {
                hist.append("-{{{");
                hist.append(Rand.rand(codes).toUpperCase());
                hist.append("  ");
                hist.append(Rand.rand(nouns).toUpperCase());
                if (Rand.chance(70)) {
                    hist.append("  ");
                    hist.append(Rand.rand(nouns).toUpperCase());
                }
                hist.append("}}}- ");
            } else {

                for (String d: pstack) {
                    hist.append(" ");
                }
                for (int p = 0; p < 4; p++) {
                    if (Rand.chance(40)) {
                        String[] gpair = groupings.rand();
                        hist.append(gpair[0]);
                        pstack.push(gpair[1]);
                    }

                    if (Rand.chance(10)) hist.append(mathSyms2.rand());

                    if (Rand.chance(20)) hist.append(mathSyms1.rand());

                    if (Rand.chance(10)) hist.append(number());

                    if (Rand.chance(10)) hist.append(mathSyms2.rand());

                    if (Rand.chance(20)) hist.append(mathSyms1.rand());


                    if (Rand.chance(20)) hist.append(number());

                    if (Rand.chance(20)) hist.append(mathLetters1.rand()).append(" ");
                    if (Rand.chance(10)) hist.append(mathLetters2.rand());
                    if (Rand.chance(5)) hist.append(mathLetters3.rand());
                    if (Rand.chance(2)) hist.append(mathShapes1.rand());

                    if (Rand.chance(1)) hist.append(mathArrows1.rand());
                    if (Rand.chance(.5)) hist.append(mathArrows2.rand());
                    if (Rand.chance(.5)) hist.append(mathArrows3.rand());

                    if (Rand.chance(1)) hist.append(Rand.rand(endwords)).append(" ");
                    if (Rand.chance(2)) hist.append(Rand.rand(hwords)).append(" ");
                    if (Rand.chance(2)) hist.append(Rand.rand(lc)).append(" ");
                    if (Rand.chance(1)) hist.append(Rand.getSubstr(lc2,10,30)).append(" ");
                    if (Rand.chance(2)) hist.append(Rand.getSubstr(hp,10,30)).append(" ");


                    if (pstack.size() > 1 && Rand.chance(50) || pstack.size() > 5) {
                        hist.append(pstack.pop()).append(" ");
                    }

                }
            }

            if (hist.length()>6) {
                int r1 = Rand.getInt(2);

                for (int r = 0; r < r1; r++) {
                    int pos = Rand.getInt(hist.length() - 2) + 1;

                    hist.insert(pos, txtAdditions.rand());
                }
            }
            hists.add(hist.toString());
        }

    }


    private RandomList<String> formats = new RandomList<>("%3.2e", "%10.7f", "%5.0f");
    private String number() {
        return String.format(Locale.getDefault(),formats.rand(),Rand.getDouble(1000));
    }

//
//        int dia1 = 768;
////    int dia2 = 879;
//    int dia2 = 2042;
//
//    Random rand = new Random();
//
//    public void makeText() {
//        if (hists.size()>numlines*3) {
//            hists.remove(0);
//            return;
//        }
//
//
//
//        for (int i = 0; i < numlines*10; i++) {
//            StringBuilder hist = new StringBuilder(2048);
//
//            if (rand.nextInt(6)% mMethod == 0) {
//                hist.append(String.format("%2.1f", Math.random() * 10));
//                hist.append(" ");
//            }
//            if (rand.nextInt(6) <= mMethod) {
//                hist.append(new String(Character.toChars((int) (Math.random() * (high1 - low1) + low1))));
//            }
//            if (rand.nextInt(6) <= mMethod) {
//                hist.append(String.format("%2.1f", Math.random() * 10));
//                hist.append(" ");
//            }
//
//            if (Math.random() > .98) {
//                hist.append("-{{{");
//                hist.append(codes[(int) (Math.random() * codes.length)].toUpperCase());
//                hist.append("  ");
//                hist.append(nouns[(int) (Math.random() * nouns.length)].toUpperCase());
//                if (Math.random() > .3) {
//                    hist.append("  ");
//                    hist.append(nouns[(int) (Math.random() * nouns.length)].toUpperCase());
//                }
//                hist.append("}}}- ");
//            } else if (Math.random() > .92) {
//                int rnd = (int) (Math.random() * (hp.length() - 20));
//                hist.append(hp.substring(rnd, rnd + 20));
//                hist.append(" ");
//            } else if (Math.random() > .92) {
//                int rnd = (int) (Math.random() * (lc2.length() - 20));
//                hist.append(lc2.substring(rnd, rnd + 20));
//                hist.append(" ");
//            } else if (Math.random() > .92) {
//                    hist.append(lc[(int)(Math.random()*lc.length)]);
//                    hist.append(" ");
//
//            }
//
//            if (rand.nextInt(6) <= mMethod) {
//                    int rnd = (int) (Math.random() * (lsp.length() - 20));
//                    hist.append(lsp.substring(rnd, rnd + 20));
//                    hist.append(" ");
//            }
//
//            if (Math.random() > .5) {
//                hist.append(new String(Character.toChars((int) (Math.random() * (high1 - low1) + low1))));
//            }
//            if (Math.random() > .5) {
//                hist.append(hwords[(int) (Math.random() * hwords.length)]);
//                hist.append(" ");
//                hist.append(nouns[(int) (Math.random() * nouns.length)]);
//                hist.append(" ");
//            }
//            if (Math.random() > .5) {
//                hist.append(new String(Character.toChars((int) (Math.random() * (high1 - low1) + low1))));
//                hist.append(new String(Character.toChars((int) (Math.random() * (high1 - low1) + low1))));
//                hist.append(" ");
//            }
//            if (Math.random() > .3) {
//                hist.append(new String(Character.toChars((int) (Math.random() * (high1 - low1) + low1))));
//                hist.append((char) (Math.random() * 26 + 65));
//                for (int j = 0; j < Math.random() * 6 + 1; j++) {
//                    hist.append((char) (Math.random() * 56 + 65));
//                    hist.append((char) (Math.random() * 56 + 65));
//                }
//                hist.append(" ");
//            }
//            if (Math.random() > .7) {
//                hist.append(new String(Character.toChars((int) (Math.random() * (high1 - low1) + low1))));
//                hist.append(endwords[(int) (Math.random() * endwords.length)].toUpperCase());
//                hist.append(new String(Character.toChars((int) (Math.random() * (high1 - low1) + low1))));
//                hist.append(" ");
//            }
//            if (Math.random() > .5) {
//                eNum(hist);
//            }
//
//            int r1 = (int)(Math.random()*2);
//
//            for (int r=0; r<r1; r++) {
//                int pos = (int) (Math.random() * (hist.length()-2) + 1);
//
//                hist.insert(pos, new String(Character.toChars((int) (Math.random() * (dia2 - dia1) + dia1))));
//            }
//
//
//            hists.add(hist.toString());
//        }
//
//
//    }
//
//
//
//
//
//    int low1=Integer.parseInt("2200", 16);
//    int high1=Integer.parseInt("22FF", 16);
//
//
//    private void eNum(StringBuilder hist) {
//        hist.append(String.format("%6.3e", rand.nextFloat()*1000));
//    }
//
//    private void num(StringBuilder hist) {
//        hist.append(String.format("%6.1f", rand.nextFloat()*1000));
//    }
//
//    private void mathSym2(StringBuilder hist) {
//        hist.append(new String(Character.toChars(rand.nextInt(high1 - low1) + low1)));
//    }
//
//
//    private void constName(StringBuilder hist) {
//        for (int j = 0; j < Math.random() * 6 + 1; j++) {
//            hist.append((char) (rand.nextInt(56) + 65));
//            hist.append((char) (rand.nextInt(56) + 65));
//        }
//    }


}
