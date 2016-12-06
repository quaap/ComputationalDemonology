package com.quaap.computationaldemonology;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

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
