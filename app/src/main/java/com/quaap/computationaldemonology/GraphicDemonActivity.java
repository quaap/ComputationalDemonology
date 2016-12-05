package com.quaap.computationaldemonology;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GraphicDemonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic_demon);
        GraphicDmn dmnview = (GraphicDmn) findViewById(R.id.dmnview);


    }

    @Override
    protected void onStart() {
        super.onStart();
        GraphicDmn dmnview = (GraphicDmn) findViewById(R.id.dmnview);
        dmnview.unpause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        GraphicDmn dmnview = (GraphicDmn) findViewById(R.id.dmnview);
        dmnview.pause();
    }
    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        GraphicDmn dmnview = (GraphicDmn) findViewById(R.id.dmnview);
        dmnview.pause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        GraphicDmn dmnview = (GraphicDmn) findViewById(R.id.dmnview);
        dmnview.unpause();
    }
}
