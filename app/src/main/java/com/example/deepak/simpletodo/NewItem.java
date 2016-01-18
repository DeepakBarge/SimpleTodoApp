package com.example.deepak.simpletodo;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class NewItem extends AppCompatActivity {

    Toolbar toolbar;
    Spinner priorityDropdown, statusDropdown;
    EditText taskText, notesText;
    TextView currentDate;
    String[] pItems, sItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_appicon);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setTitle("Listly");

        priorityDropdown = (Spinner)findViewById(R.id.spinner1);
        pItems = new String[]{"Low", "Medium", "High"};
        ArrayAdapter<String> pAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, pItems);
        priorityDropdown.setAdapter(pAdapter);

        statusDropdown = (Spinner)findViewById(R.id.spinner2);
        sItems = new String[]{"Started", "Not Started", "In Progress", "Done"};
        ArrayAdapter<String> sAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sItems);
        statusDropdown.setAdapter(sAdapter);

        taskText = (EditText)findViewById(R.id.taskText);
        notesText = (EditText)findViewById(R.id.notesText);
        currentDate = (TextView)findViewById(R.id.currentDate);

        Intent intent = getIntent();
        Item displayItem = (Item) intent.getSerializableExtra("newItem");

        if (intent.getStringExtra("operation").equalsIgnoreCase("edit")) {
            taskText.setText(displayItem.label);
            notesText.setText(displayItem.notes);
            priorityDropdown.setSelection(getPosition(displayItem.priorityLevel, pItems));
            statusDropdown.setSelection(getPosition(displayItem.status, sItems));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {

            case R.id.action_save:
                Item temp = new Item(taskText.getText().toString(),
                        notesText.getText().toString(),
                        priorityDropdown.getSelectedItem().toString(),
                        statusDropdown.getSelectedItem().toString());

                Intent data = new Intent();
                data.putExtra("operation","save");
                data.putExtra("newItem", temp);
                setResult(RESULT_OK, data);
                finish();
                //return true;

            case R.id.action_cancel:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public int getPosition(String findItem, String[] arrayStr){
        int i = 0;
        for(String str : arrayStr ){
            if(findItem.equalsIgnoreCase(str)){
                return i;
            }
            i++;
        }
        return i;
    }

    public void showDatePickerDialog(View v){
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getFragmentManager(), "start_date_picker");
    }
}
