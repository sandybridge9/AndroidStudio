package com.example.destroyer.morsecodeflashlighttranslator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends Activity {

    private TextView aboutField;
    private TextView createdByField;
    private TextView appVersionField;
    private TextView summaryField;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutlayout);

        aboutField = (TextView) findViewById(R.id.aboutField);
        createdByField = (TextView) findViewById(R.id.createdByField);
        appVersionField = (TextView) findViewById(R.id.appVersionField);
        summaryField = (TextView) findViewById(R.id.summaryField);
    }

}
