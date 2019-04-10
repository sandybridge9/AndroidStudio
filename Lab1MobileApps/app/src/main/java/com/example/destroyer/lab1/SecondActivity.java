package com.example.destroyer.lab1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SecondActivity extends Activity {

    private ListView mylist;
    private ListAdapter adapter;
    private ArrayList<ListItem> items;
    private Button sortButton;
    private EditText listFilter;
    private TextView filterTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondactivitydesign);
        mylist = (ListView) findViewById(R.id.listView);
        sortButton = (Button) findViewById(R.id.sortButton);
        sortButton.setOnClickListener(sortList);
        listFilter = (EditText)findViewById(R.id.listFilter);
        filterTextField = (TextView) findViewById(R.id.filterTextField);
        //getting list of ListItems from the first activity
        Intent intent = getIntent();
        items = intent.getParcelableArrayListExtra ("array");
        //----------------------------------------------------
        adapter = new ListAdapter(this, items);
        mylist.setAdapter(adapter);
        listFilter.addTextChangedListener(new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence charSequence, int int1, int int2, int int3){
                //adapter.getFilter().filter(charSequence);
                Log.e(charSequence.toString(), charSequence.toString());
                ArrayList<ListItem> temp = new ArrayList<>();
                for(int i = 0; i < items.size(); i++){
                    if(items.get(i).getTitle().toLowerCase().startsWith(charSequence.toString().toLowerCase())){
                        temp.add(items.get(i));
                    }
                }
                Context context = adapter.getContext();
                ListAdapter adapter2 = new ListAdapter(context, temp);
                mylist.setAdapter(adapter2);
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int int1, int int2, int int3){

            }
            public void afterTextChanged(Editable editable){

            }

        });
    }

    View.OnClickListener sortList = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            sortList();
            mylist.setAdapter(adapter);
        }
    };

    public void sortList(){
        ArrayList<ListItem> arrayList = items;

        Collections.sort(arrayList, new Comparator<ListItem>() {
            @Override
            public int compare(ListItem o1, ListItem o2) {
                return o1.getTitle().toLowerCase().compareTo(o2.getTitle().toLowerCase());
            }
        });
        items = arrayList;
    }
}
