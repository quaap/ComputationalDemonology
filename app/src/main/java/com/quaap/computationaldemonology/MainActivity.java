package com.quaap.computationaldemonology;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.quaap.computationaldemonology.synth.MultiToneSynth;
import com.quaap.computationaldemonology.synth.StaticFilter;
import com.quaap.computationaldemonology.synth.Synth;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

    Synth s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button summon = (Button)findViewById(R.id.button_summon);
        Button expel = (Button)findViewById(R.id.button_expel);
        Button seek = (Button)findViewById(R.id.button_seek);
        Button cast = (Button)findViewById(R.id.button_cast);

        seek.setOnClickListener(this);
        cast.setOnClickListener(this);
        summon.setOnClickListener(this);
        expel.setOnClickListener(this);

        Button sound = (Button)findViewById(R.id.button);

        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (s==null) {
                    s = new MultiToneSynth(372.98f, 497.87f);
                    s.addFilter(new StaticFilter());
                    System.out.println(s.getMaxVol());
                    s.start();
                } else {
                    s.stopSynth();
                    s = null;
                }

            }
        });

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
        }
        startActivity(intent);
    }
}
