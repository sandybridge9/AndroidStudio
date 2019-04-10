package com.example.destroyer.morsecodeflashlighttranslator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainMenuActivity extends Activity{

    private TextView menuField;
    private Button startButton;
    private Button aboutButton;
    private Button exitButton;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenulayout);

        menuField = (TextView) findViewById(R.id.menuField);
        startButton = (Button) findViewById(R.id.startButton);
        aboutButton = (Button) findViewById(R.id.aboutButton);
        exitButton = (Button) findViewById(R.id.exitButton);

        startButton.setOnClickListener(startTranslatorActivity);
        aboutButton.setOnClickListener(startAboutActivity);
        exitButton.setOnClickListener(exitListener);
    }

    public void runTranslatorActivity(){
        Intent intent = new Intent(context, TranslatorActivity.class);
        context.startActivity(intent);
    }
    View.OnClickListener startTranslatorActivity = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            runTranslatorActivity();
        }
    };

    public void runAboutActivity(){
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }
    View.OnClickListener startAboutActivity = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            runAboutActivity();
        }
    };

    View.OnClickListener exitListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            finish();
            System.exit(0);
        }
    };
}
