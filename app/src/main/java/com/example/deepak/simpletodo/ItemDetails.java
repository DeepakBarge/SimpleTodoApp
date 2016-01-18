package com.example.deepak.simpletodo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.app.AlertDialog;

public class ItemDetails extends AppCompatActivity {

    TextView taskLabelText, dueDateText, statusText, priorityLevelText, notesText;

    Toolbar toolbar;

    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_appicon);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setTitle("Listly");

        Intent intent = getIntent();

        taskLabelText = (TextView) findViewById(R.id.taskLabelText);
        dueDateText = (TextView) findViewById(R.id.dueDateText);
        statusText = (TextView) findViewById(R.id.statusText);
        priorityLevelText = (TextView) findViewById(R.id.priorityLevelText);
        notesText = (TextView) findViewById(R.id.notesText);


        Item displayItem = (Item) intent.getSerializableExtra("item");

        taskLabelText.setText(displayItem.label);
        statusText.setText(displayItem.status);
        priorityLevelText.setText(displayItem.priorityLevel);
        notesText.setText(displayItem.notes);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Item temp = new Item(taskLabelText.getText().toString(),
                        notesText.getText().toString(),
                        priorityLevelText.getText().toString(),
                        statusText.getText().toString());

                Intent data = new Intent();
                data.putExtra("operation","update");
                data.putExtra("newItem", temp);
                setResult(RESULT_OK, data);
                finish();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_edit:

                Intent i = new Intent(getApplicationContext(), NewItem.class);

                Item editItem = new Item(taskLabelText.getText().toString(),
                        notesText.getText().toString(),
                        priorityLevelText.getText().toString(),
                        statusText.getText().toString());
                i.putExtra("operation","edit");
                i.putExtra("newItem", editItem);
                startActivityForResult(i, REQUEST_CODE);

                return true;

            case R.id.action_delete:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                alertDialogBuilder
                        .setTitle("Are you sure to want to delete this task?")
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_warning)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Item temp = new Item(taskLabelText.getText().toString(),
                                        notesText.getText().toString(),
                                        priorityLevelText.getText().toString(),
                                        statusText.getText().toString());

                                Intent data = new Intent();
                                data.putExtra("operation","delete");
                                data.putExtra("newItem", temp);
                                setResult(RESULT_OK, data);
                                finish();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

            Item newItem = (Item) data.getSerializableExtra("newItem");
            if (data.getStringExtra("operation").equalsIgnoreCase("save")) {
                taskLabelText.setText(newItem.label);
                statusText.setText(newItem.status);
                priorityLevelText.setText(newItem.priorityLevel);
                notesText.setText(newItem.notes);
            }
        }
    }

}
