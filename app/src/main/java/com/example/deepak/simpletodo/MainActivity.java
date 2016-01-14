package com.example.deepak.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter arrayAdapter;
    ArrayList<String> listViewContent;
    ItemsDatabaseHelper dbHelper;

    int currentItemPosition = -1;
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.itemList);

        readDBItems();

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
                String itemLabel = listViewContent.get(position);
                listViewContent.remove(position);
                arrayAdapter.notifyDataSetChanged();
                dbHelper.deleteItem(itemLabel);
                return true;
            }
        });
    }

    public void addItem(View view) {

        EditText editText = (EditText) findViewById(R.id.editText);
        String itemText = editText.getText().toString();

        if(!itemText.isEmpty()){
            listViewContent.add(itemText);
            editText.setText("");
            editText.setHint("Add a new item");
            arrayAdapter.notifyDataSetChanged();
            Item dbItem = new Item(itemText, itemText, "high", "started");
            dbHelper.addItem(dbItem);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String newItem = data.getExtras().getString("newText");
            String oldLabel = listViewContent.get(currentItemPosition);
            listViewContent.set(currentItemPosition, newItem);
            arrayAdapter.notifyDataSetChanged();
            Item dbItem = new Item(newItem, newItem, "low", "started");
            dbHelper.updateItem(oldLabel, dbItem);
        }
    }

    private void readDBItems(){
        dbHelper = ItemsDatabaseHelper.getInstance(this);
        listViewContent = new ArrayList<String>();
        List<Item> items = dbHelper.getAllItems();

        if(!items.isEmpty()){
            for (Item item : items) {
                listViewContent.add(item.label);
            }
        }
    }

}

