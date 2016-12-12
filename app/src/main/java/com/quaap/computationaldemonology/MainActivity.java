package com.quaap.computationaldemonology;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import com.quaap.computationaldemonology.synth.MultiToneSynth;
import com.quaap.computationaldemonology.synth.SawSynth;
import com.quaap.computationaldemonology.synth.StaticFilter;
import com.quaap.computationaldemonology.synth.Synth;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

    Synth s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToggleButton toggle = (ToggleButton)findViewById(R.id.toggleButton);
        final ViewFlipper flip = (ViewFlipper)findViewById(R.id.flipper);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) flip.showPrevious(); else flip.showNext();

            }
        });



        Button summon = (Button)findViewById(R.id.button_summon);
        Button expel = (Button)findViewById(R.id.button_expel);
        Button seek = (Button)findViewById(R.id.button_seek);
        Button cast = (Button)findViewById(R.id.button_cast);
        Button cloud = (Button)findViewById(R.id.button_cloud);
        Button worms = (Button)findViewById(R.id.button_worms);

        seek.setOnClickListener(this);
        cast.setOnClickListener(this);
        summon.setOnClickListener(this);
        expel.setOnClickListener(this);
        worms.setOnClickListener(this);
        cloud.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(this, GraphicDemonActivity.class);
        switch (view.getId()) {
            case R.id.button_summon:
                intent.putExtra( GraphicDmn.GO, GraphicDmn.SUMMON); break;
            case R.id.button_expel:
                intent.putExtra(GraphicDmn.GO, GraphicDmn.EXPEL); break;
            case R.id.button_seek:
                intent.putExtra(GraphicDmn.GO, GraphicDmn.PROTECTION); break;
            case R.id.button_cast:
                intent.putExtra(GraphicDmn.GO, GraphicDmn.CAST); break;
            case R.id.button_cloud:
                intent.putExtra(GraphicDmn.GO, GraphicDmn.CLOUD); break;
            case R.id.button_worms:
                intent.putExtra(GraphicDmn.GO, GraphicDmn.WORMS); break;
        }
        startActivity(intent);
    }
}
