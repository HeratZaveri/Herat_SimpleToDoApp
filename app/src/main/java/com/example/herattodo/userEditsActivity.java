package com.example.herattodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class userEditsActivity extends AppCompatActivity {
    EditText edItem;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edits);

        save = findViewById(R.id.btnSave);
        edItem = findViewById(R.id.edItem);

        getSupportActionBar().setTitle("Edit Item");
        edItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TXT));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedItm = edItem.getText().toString();
                Intent data = new Intent();
                data.putExtra(MainActivity.KEY_ITEM_TXT, updatedItm);
                data.putExtra(MainActivity.KEY_ITEM_POS, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POS));
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }
}