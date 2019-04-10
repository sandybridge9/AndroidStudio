package com.example.destroyer.lab1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ThirdActivity extends Activity {
    private ImageView image;
    private TextView title;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thirdactivitydesign);

        title = (TextView) findViewById(R.id.title3);
        description = (TextView) findViewById(R.id.description3);
        image = (ImageView) findViewById(R.id.image3);

        image.setImageResource(R.drawable.thunderbird);
        String titleString = "Bird";
        String descriptionString = "A beautiful thunderbird holding a letter... Im pretty sure it's ripped off some logo";
        title.setText(titleString);
        description.setText(descriptionString);
    }
}
