package com.quaap.computationaldemonology;

/**
 * Created by tom on 12/2/16.
 *
 *    Copyright (C) 2016  Tom Kliethermes
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 */

// Note: you'l notice a lot of magic numbers used throughout this code.  Yeah, that's the point.

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.quaap.computationaldemonology.synth.Synth;

public class MainActivity extends Activity implements Button.OnClickListener {

    Synth s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button summon = (Button)findViewById(R.id.button_summon);
        Button expel = (Button)findViewById(R.id.button_expel);
        Button protect = (Button)findViewById(R.id.button_protect);
        Button cast = (Button)findViewById(R.id.button_cast);
       // Button cloud = (Button)findViewById(R.id.button_cloud);
        Button worms = (Button)findViewById(R.id.button_worms);

        protect.setOnClickListener(this);
        cast.setOnClickListener(this);
        summon.setOnClickListener(this);
        expel.setOnClickListener(this);
        worms.setOnClickListener(this);
        //cloud.setOnClickListener(this);

        TextView txtnamelink = (TextView)findViewById(R.id.name_link);
        txtnamelink.setMovementMethod(LinkMovementMethod.getInstance());
        txtnamelink.setText(Html.fromHtml(getString(R.string.name_link)));

    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(this, GraphicDemonActivity.class);
        switch (view.getId()) {
            case R.id.button_summon:
                intent.putExtra( GraphicDmn.GO, GraphicDmn.SUMMON); break;
            case R.id.button_expel:
                intent.putExtra(GraphicDmn.GO, GraphicDmn.EXPEL); break;
            case R.id.button_protect:
                intent.putExtra(GraphicDmn.GO, GraphicDmn.PROTECTION); break;
            case R.id.button_cast:
                intent.putExtra(GraphicDmn.GO, GraphicDmn.CAST); break;
           // case R.id.button_cloud:
           //     intent.putExtra(GraphicDmn.GO, GraphicDmn.CLOUD); break;
            case R.id.button_worms:
                intent.putExtra(GraphicDmn.GO, GraphicDmn.WORMS); break;
        }
        startActivity(intent);
    }
}
