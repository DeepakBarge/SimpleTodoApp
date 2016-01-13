package com.example.deepak.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditItem extends AppCompatActivity {

    EditText saveText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        setTitle("Edit Item");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        String selectedItemText = intent.getStringExtra("currentText");

        saveText = (EditText) findViewById(R.id.saveText);

        saveText.setText(selectedItemText);
    }

    public void saveItem(View view) {

        Intent data = new Intent();
        data.putExtra("newText", saveText.getText().toString());
        setResult(RESULT_OK, data);
        finish();
    }
}
