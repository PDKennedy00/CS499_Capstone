package com.example.a5_3_project_two_by_dawson_kennedy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DataDisplayActivity extends AppCompatActivity {

    private RecyclerView dataRecyclerView;
    private InventoryAdapter inventoryAdapter;
    private List<InventoryItem> inventoryList;
    private DBHelper dbHelper;

    private EditText inputName;
    private EditText inputQuantity;
    private Button addUpdateButton;
    private int selectedItemPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_display);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Data Display");

        inputName = findViewById(R.id.input_name);
        inputQuantity = findViewById(R.id.input_quantity);
        addUpdateButton = findViewById(R.id.addUpdateButton);

        dbHelper = new DBHelper(this);
        inventoryList = dbHelper.getAllItems();

        dataRecyclerView = findViewById(R.id.dataRecyclerView);
        dataRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        inventoryAdapter = new InventoryAdapter(inventoryList);
        dataRecyclerView.setAdapter(inventoryAdapter);


        inventoryAdapter.setOnItemClickListener(new InventoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // When an item is clicked, populate the inputs with the item's data
                InventoryItem item = inventoryList.get(position);
                inputName.setText(item.getName());
                inputQuantity.setText(String.valueOf(item.getQuantity()));
                selectedItemPosition = position;
                addUpdateButton.setText("Update"); // Change button text to "Update"
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });

        addUpdateButton.setOnClickListener(v -> {
            String name = inputName.getText().toString();
            int quantity = Integer.parseInt(inputQuantity.getText().toString());

            if (selectedItemPosition == -1) {
                // Add new item
                InventoryItem newItem = new InventoryItem(name, quantity);
                dbHelper.addItem(newItem);
                inventoryList.add(new InventoryItem(name, quantity));
                inventoryAdapter.notifyItemInserted(inventoryList.size() - 1);
            } else {
                // Update existing item
                InventoryItem currentItem = inventoryList.get(selectedItemPosition);
                currentItem.setName(name);
                currentItem.setQuantity(quantity);
                dbHelper.updateItem(currentItem);
                inventoryAdapter.notifyItemChanged(selectedItemPosition);

                // Reset after update
                selectedItemPosition = -1;
                addUpdateButton.setText("Add");
            }

            // Clear input fields
            inputName.setText("");
            inputQuantity.setText("");
        });

        // Set up the button to switch to the SMS Permission screen
        findViewById(R.id.switchToSMSPermissionButton).setOnClickListener(v -> {
            Intent intent = new Intent(DataDisplayActivity.this, SMSPermissionActivity.class);
            startActivity(intent);
        });
    }

    private void removeItem(int position) {
        InventoryItem itemToDelete = inventoryList.get(position);
        dbHelper.deleteItem(itemToDelete.getId());
        inventoryList.remove(position);
        inventoryAdapter.notifyItemRemoved(position);
    }
}