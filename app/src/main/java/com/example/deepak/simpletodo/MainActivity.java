package com.example.deepak.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter arrayAdapter;
    ArrayList<String> listViewContent;
    ItemsDatabaseHelper dbHelper;

    int currentItemPosition = -1;
    private final int REQUEST_CODE = 20;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.itemList);

        readDBItems();

        if(arrayAdapter == null) {
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listViewContent);
        }

        listView.setAdapter(arrayAdapter);

        setupItemClickListener();

        setupItemLongClickListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            //case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
            //    return true;

            case R.id.action_add:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
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

