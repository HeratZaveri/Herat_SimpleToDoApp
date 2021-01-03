package com.example.herattodo;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_ITEM_TXT = "item_text";
    public  static  final String KEY_ITEM_POS = "item_position";
    public static final int REQUEST_CODE = 20;
    List<String> items;
    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAdd = findViewById(R.id.butnAdd);
        etItem = findViewById(R.id.edTask);
        rvItems = findViewById(R.id.tasks);

        loadItems();
        /*
        //Mock data for initial testing and running program
        items = new ArrayList<>();
        items.add("Go to Gym");
        items.add("Exam on Friday");
        items.add("Buy Milk");
         */
        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                //use intent here to display the activity
                Intent i  = new Intent(MainActivity.this, userEditsActivity.class);
                i.putExtra(KEY_ITEM_TXT, items.get(position));
                i.putExtra(KEY_ITEM_POS, position);
                //display activity
                startActivityForResult(i,REQUEST_CODE);
                Log.e("MainActivity", "Click at position " + position);
            }
        };

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                //Delete the item from the model
                items.remove(position);
                //Notify Adapter it was removed
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed from tasks", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String itemTemp = etItem.getText().toString();
               //Add item to model
                items.add(itemTemp);
               //Notify Adapter to render
                itemsAdapter.notifyItemInserted(items.size()-1);
                etItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added to tasks", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }
    //Handle data passed back
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            //retrieve value and position
            String editedItem = data.getStringExtra(KEY_ITEM_TXT);
            int position = data.getExtras().getInt(KEY_ITEM_POS);

            //Update model with value
            items.set(position, editedItem);
            //Notify Adapter
            itemsAdapter.notifyItemChanged(position);
            //Changes should be persisted for later access
            saveItems();
            Toast.makeText(getApplicationContext(), "Item has been updated", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity", "Unknown call to onActivity result");
        }
    }

    //get file
    private File getDataFile(){
        return new File(getFilesDir(),"data.txt");
    }
    //Read data file and load items from it
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(),Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("Main Activity", "Error reading Items", e);
            items = new ArrayList<>();
        }
    }
    //Function saves items by writing them into data file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(),items);
        } catch (IOException e) {
            Log.e("Main Activity", "Error writing Items to file", e);
        }
    }
}