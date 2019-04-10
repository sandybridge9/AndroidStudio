package com.example.destroyer.morsecodeflashlighttranslator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class SettingsActivity extends Activity {

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutlayout);
    }
}
