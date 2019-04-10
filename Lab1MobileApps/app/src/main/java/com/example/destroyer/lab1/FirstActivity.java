package com.example.destroyer.lab1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FirstActivity extends Activity {

    private TextView mytextField;
    private TextView mytextField2;
    private EditText editText1;
    private EditText editText2;
    private Button myButton;
    private  Button secondActivityButton;
    private  Button thirdActivityButton;
    private Context context = this;
    private ArrayList<ListItem> list;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstactivitydesign);
        list = new ArrayList<>();

        secondActivityButton = (Button) findViewById(R.id.secondActivityButton);
        thirdActivityButton = (Button) findViewById(R.id.thirdActivityButton);
        myButton = (Button) findViewById(R.id.button);
        mytextField = (TextView) findViewById(R.id.textfield);
        mytextField2 = (TextView) findViewById(R.id.textfield2);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);

        myButton.setOnClickListener(myButtonClick);
        secondActivityButton.setOnClickListener(startSecondActivity);
        secondActivityButton.setOnLongClickListener(startSecondActivityLong);
        thirdActivityButton.setOnClickListener(startThirdActivity);
    }

    View.OnClickListener myButtonClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            String title = editText1.getText().toString();
            String description = editText2.getText().toString();
            Log.e(title, description);

            ListItem item = new ListItem();
            item.setTitle(title);
            item.setDescription(description);
            item.setImageId(R.drawable.apple);
            list.add(item);

            mytextField2.setText(mytextField2.getText()+"\n"  +"Title: " +title +" Description: " +description +" was added to the list. " +" There are " +list.size() +" items in the list. ");
        }
    };
    public void runSecondActivity(){
        Intent intent = new Intent(context, SecondActivity.class);
        ArrayList<ListItem> arrayList = new ArrayList<>();
        addToList(arrayList);
        for(int i = 0; i < list.size(); i++){
            arrayList.add(list.get(i));
        }
        intent.putParcelableArrayListExtra("array", arrayList);
        context.startActivity(intent);
    }

    View.OnClickListener startSecondActivity = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            runSecondActivity();
        }
    };
    View.OnLongClickListener startSecondActivityLong = new View.OnLongClickListener(){
        @Override
        public boolean onLongClick(View v){
            runThirdActivity();
            return true;
        }
    };
    public void runThirdActivity(){
        Intent intent = new Intent(context, ThirdActivity.class);
        context.startActivity(intent);
    }
    View.OnClickListener startThirdActivity = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            runThirdActivity();
        }
    };

    public void addToList(ArrayList<ListItem> list){
        list.add(new ListItem("Mad", R.drawable.apple, "Mathematics ir the worse version of Chemistry"));
        list.add(new ListItem("Uncle", R.drawable.apple, "Chemistry is the worse version of Biology"));
        list.add(new ListItem("Ross", R.drawable.apple, "Mathematics is the worse version of Physics"));
        list.add(new ListItem("Jim", R.drawable.apple, "Mathematics and Chemistry are both equaly bad"));
        list.add(new ListItem("Frank", R.drawable.apple, "Mathematics, Chemistry and Biology are all sciences of the SATAN"));
        list.add(new ListItem("Caspar", R.drawable.apple, "Mathematics ir the worse version of Geography"));
        list.add(new ListItem("Zeek", R.drawable.apple, "Chemistry is the worse version of Neuroscience"));
        list.add(new ListItem("Milkman", R.drawable.apple, "Mathematics is the worse version of Polythology"));
        list.add(new ListItem("Chain", R.drawable.apple, "Mathematics and Religion are both equaly bad"));
        list.add(new ListItem("Moses", R.drawable.apple, "Mathematics, Chemistry and Paleonthology are all sciences of the SATAN"));
        list.add(new ListItem("Alpoint", R.drawable.apple, "Mathematics ir the worse version of Voodoo"));
        list.add(new ListItem("Albino", R.drawable.apple, "Chemistry is the worse version of Biology"));
        list.add(new ListItem("Kim", R.drawable.apple, "Ethics is the worse version of Physics"));
        list.add(new ListItem("Saloon", R.drawable.apple, "Dances and fortnite emotes are both equaly bad"));
        list.add(new ListItem("Fibby", R.drawable.apple, "Mathematics, Chemistry and Biology are all sciences of the SATAN"));
    }
}
