package com.example.destroyer.lab2mobileapps;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.destroyer.lab2mobileapps.R;

public class MainActivity extends Activity implements RequestOperator.RequestOperatorListener {

    Button sendRequestButton;
    TextView title;
    TextView bodyText;
    private IndicatingView indicator;
    private IndicatingView indicator2;
    private ModelPost publication;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivitydesign);

        sendRequestButton = (Button) findViewById(R.id.send_request);
        sendRequestButton.setOnClickListener(requestButtonClicked);

        title = (TextView) findViewById(R.id.title);
        bodyText = (TextView) findViewById(R.id.body_text);
        indicator = (IndicatingView) findViewById(R.id.generated_graphic);
        indicator2 = (IndicatingView) findViewById(R.id.generated_graphic2);
    }

    View.OnClickListener requestButtonClicked = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            setIndicatorStatus(3);
            sendRequest();
        }
    };

    private void sendRequest(){
        RequestOperator ro = new RequestOperator();
        ro.setListener(this);
        ro.start();
    }

    public void updatePublication(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(publication != null){
                    title.setText(publication.getTitle());
                    bodyText.setText(publication.getBodyText());
                }else{
                    title.setText("");
                    bodyText.setText("");
                }
            }
        });
    }
/*
    @Override
    public void success(ModelPost publication){
        this.publication = publication;
        updatePublication();
        setIndicatorStatus(IndicatingView.SUCCESS);
    }
    */
    @Override
    public void success(int count){
        //updatePublication();
        System.out.println("Length before success tick: " +count);
        indicator2.setCount(count);
        setIndicatorStatus(IndicatingView.SUCCESS);
        setIndicatorStatus2(IndicatingView.CIRCLE);
    }

    public void failed(int responseCode){
        this.publication = null;
        updatePublication();
        setIndicatorStatus(IndicatingView.FAILED);
    }

    public void setIndicatorStatus(final int status){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                indicator.setState(status);
                indicator.invalidate();
            }
        });
    }
    public void setIndicatorStatus2(final int status){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                indicator2.setState(status);
                indicator2.invalidate();
            }
        });
    }
}
