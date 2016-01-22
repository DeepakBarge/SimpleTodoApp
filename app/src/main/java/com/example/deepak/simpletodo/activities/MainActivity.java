package com.example.deepak.simpletodo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import com.example.deepak.simpletodo.adapters.CustomItemListAdapter;
import com.example.deepak.simpletodo.utils.Item;
import com.example.deepak.simpletodo.utils.ItemsDatabaseHelper;
import com.example.deepak.simpletodo.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ItemsDatabaseHelper dbHelper;

    ArrayList<Item> listViewItems;
    CustomItemListAdapter customItemListAdapter;

    int currentItemPosition = 0;
    private final int REQUEST_CODE = 20;
    private final int REQUEST_CODE1 = 21;
    private final int REQUEST_CODE2 = 22;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_appicon);
        getSupportActionBar().setTitle("Listly");

        listView = (ListView) findViewById(R.id.itemList);

        readDBItems();

        if(customItemListAdapter == null){
            customItemListAdapter = new CustomItemListAdapter(this, listViewItems);
        }

        listView.setAdapter(customItemListAdapter);

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

        switch (item.getItemId()) {

            case R.id.action_add:

                Intent i = new Intent(getApplicationContext(), NewOrEditItemActivity.class);
                Item newItem = new Item("", "", "", "");
                i.putExtra("operation","add");
                i.putExtra("newItem", newItem);
                startActivityForResult(i, REQUEST_CODE2);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void setupItemClickListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentItemPosition = position;

                Intent i = new Intent(getApplicationContext(), ItemDetailsActivity.class);

                Item temp = new Item(listViewItems.get(position).label,
                        listViewItems.get(position).notes,
                        listViewItems.get(position).priorityLevel,
                        listViewItems.get(position).status);
                i.putExtra("item", temp);
                startActivityForResult(i, REQUEST_CODE1);
            }
        });

    }

    private void setupItemLongClickListener(){
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String itemLabel = listViewItems.get(position).label;
                listViewItems.remove(position);
                customItemListAdapter.notifyDataSetChanged();
                dbHelper.deleteItem(itemLabel);
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE1){
            Item newItem = (Item) data.getSerializableExtra("newItem");
            if(data.getStringExtra("operation").equalsIgnoreCase("update")){
                String oldLabel = listViewItems.get(currentItemPosition).label;
                listViewItems.set(currentItemPosition, newItem);
                customItemListAdapter.notifyDataSetChanged();
                dbHelper.updateItem(oldLabel, newItem);
            }
            if(data.getStringExtra("operation").equalsIgnoreCase("delete")){
                String itemLabel = listViewItems.get(currentItemPosition).label;
                listViewItems.remove(currentItemPosition);
                customItemListAdapter.notifyDataSetChanged();
                dbHelper.deleteItem(itemLabel);
            }
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE2) {

            Item newItem = (Item) data.getSerializableExtra("newItem");
            if (data.getStringExtra("operation").equalsIgnoreCase("save")) {

                if(!newItem.label.isEmpty()){
                    listViewItems.add(newItem);
                    customItemListAdapter.notifyDataSetChanged();
                    dbHelper.addItem(newItem);
                }

            }
        }
    }

    private void readDBItems(){

        dbHelper = ItemsDatabaseHelper.getInstance(this);
        listViewItems = dbHelper.getAllItems();
    }

}

