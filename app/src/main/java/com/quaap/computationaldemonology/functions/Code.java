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
            mTextPaint[t].setTypeface(Typeface.SANS_SERIF);
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
//    private CharRange mathLetters2 = new CharRange('Α', 'Ω');
//    private CharRange mathLetters3 = new CharRange('a', 'z');
//    private CharRange mathLetters4 = new CharRange('α', 'ω');

    private String lcgreek = "αβγδεζηθικλμνξοπρςστυφχψω";
    private String ucgreek = "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩ";
    private String eng = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxwy";


    private String greek =  ucgreek + lcgreek;


//    private CharRange mathLetters1 = new CharRange(0x1D400, 0x1D7FF);
//    private CharRange mathLetters2 = new CharRange(0x2100, 0x214F);
//    private CharRange mathLetters3 = new CharRange(0x1EE00, 0x1EE7E);
//
//    private CharRange mathArrows1 = new CharRange(0x2190, 0x21FF);
//    private CharRange mathArrows2 = new CharRange(0x27F0, 0x27FF);
//    private CharRange mathArrows3 = new CharRange(0x2900, 0x297F);

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



    int STACKMAX = 5;

    public void makeText() {
        if (hists.size() > numlines * 3) {
            hists.remove(0);
            return;
        }


        for (int i = 0; i < numlines*4; i++) {
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
                    if (Rand.chance(40) && pstack.size()<STACKMAX) {
                        String[] gpair = groupings.rand();
                        hist.append(gpair[0]);
                        pstack.push(gpair[1]);
                    }

                    if (p==mMethod) {

                        for (int k = 0; k < Rand.getInt(1,7); k++) {
                           hist.append(Rand.getBoolean() ? Rand.getChar(greek) : Rand.getChar(eng));
                        }
                        if (Rand.chance(50)) hist.append(" ").append(mathSyms2.rand()).append(" ");

                        if (Rand.chance(20)) hist.append(mathSyms1.rand());

                        if (Rand.chance(40)) hist.append(number());
                    }

                    if (Rand.chance(30)) hist.append(" ").append(mathSyms2.rand()).append(" ");

                    if (Rand.chance(20)) hist.append(mathSyms1.rand());


                    if (Rand.chance(20)) hist.append(number());

                    if (Rand.chance(20)) hist.append(Rand.getBoolean() ? Rand.getChar(greek) : Rand.getChar(eng)).append(" ");
//                    if (Rand.chance(10)) hist.append(mathLetters2.rand());
//                    if (Rand.chance(5)) hist.append(mathLetters3.rand());
//
//                    if (Rand.chance(3)) hist.append(" ").append(mathArrows1.rand()).append(" ");
//                    if (Rand.chance(1)) hist.append(" ").append(mathArrows2.rand()).append(" ");
//                    if (Rand.chance(.5)) hist.append(mathArrows3.rand());

                    if (Rand.chance(3.0 / mMethod)) hist.append(Rand.rand(endwords)).append(" ");
                    if (Rand.chance(2.0 / mMethod)) hist.append(Rand.rand(hwords)).append(" ");
                    if (Rand.chance(2.0 / mMethod)) hist.append(Rand.rand(lc)).append(" ");
                    if (Rand.chance(1.0 / mMethod)) hist.append(Rand.getSubstr(lc2,10,30)).append(" ");
                    if (Rand.chance(2.0 / mMethod)) hist.append(Rand.getSubstr(hp,10,30)).append(" ");


                    if (pstack.size() > 1 && Rand.chance(50) || pstack.size() > STACKMAX) {
                        hist.append(pstack.pop()).append(" ");
                    }

                }
            }

            if (hist.length()>6) {
                int r1 = Rand.getInt(2);

                for (int r = 0; r < r1; r++) {
                    int pos = Rand.getInt(hist.length() - 2) + 1;

                   // hist.insert(pos, txtAdditions.rand());
                }
            }
            hists.add(hist.toString());
        }

    }

    private RandomList<String> formats = new RandomList<>("%3.2e", "%10.7f", "%5.0f");
    private String number() {
        return String.format(Locale.getDefault(),formats.rand(),Rand.getDouble(1000));
    }


}
