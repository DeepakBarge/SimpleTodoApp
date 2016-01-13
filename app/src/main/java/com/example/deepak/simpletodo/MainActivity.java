package com.example.deepak.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter arrayAdapter;
    ArrayList<String> listViewContent;

    int currentItemPosition = -1;
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.itemList);

        readItems();

        if(arrayAdapter == null) {
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listViewContent);
        }

        listView.setAdapter(arrayAdapter);

        setupItemClickListener();

        setupItemLongClickListener();
    }

    private void setupItemClickListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentItemPosition = position;
                Intent i = new Intent(getApplicationContext(), EditItem.class);
                i.putExtra("currentText", listViewContent.get(position));
                startActivityForResult(i, REQUEST_CODE);
            }
        });

    }

    private void setupItemLongClickListener(){
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listViewContent.remove(position);
                writeItems();
                arrayAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    public void addItem(View view) {

        EditText editText = (EditText) findViewById(R.id.editText);
        String itemText = editText.getText().toString();

        if(!itemText.isEmpty()){
            listViewContent.add(editText.getText().toString());
            editText.setText("");
            editText.setHint("Add a new item");
            writeItems();
            arrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String newItem = data.getExtras().getString("newText");
            listViewContent.set(currentItemPosition, newItem);
            writeItems();
            arrayAdapter.notifyDataSetChanged();
        }
    }

    private void readItems(){
        File fDir = getFilesDir();
        File todoFile = new File(fDir, "todo.txt");

        try{
            listViewContent = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            e.printStackTrace();
            listViewContent = new ArrayList<String>();
        }
    }

    private void writeItems(){
        File fDir = getFilesDir();
        File todoFile = new File(fDir, "todo.txt");

        try{
            FileUtils.writeLines(todoFile, listViewContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

