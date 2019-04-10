package com.example.destroyer.morsecodeflashlighttranslator;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GettingStartedActivity extends Activity{

    private TextView title;
    private Button toMainMenu;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gettingstartedlayout);

        title = (TextView) findViewById(R.id.title);
        toMainMenu = (Button) findViewById(R.id.toMainMenuButton);
        toMainMenu.setOnClickListener(startMainMenuActivity);
    }
    public void runMainMenuActivity(){
        Intent intent = new Intent(context, MainMenuActivity.class);
        context.startActivity(intent);
    }
    View.OnClickListener startMainMenuActivity = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            runMainMenuActivity();
        }
    };
}
