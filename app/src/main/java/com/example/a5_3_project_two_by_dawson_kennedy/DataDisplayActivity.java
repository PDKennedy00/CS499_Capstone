package com.example.a5_3_project_two_by_dawson_kennedy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataDisplayActivity extends AppCompatActivity {

    private RecyclerView dataRecyclerView;
    private InventoryAdapter inventoryAdapter;
    private List<InventoryItem> inventoryList;
    private DBHelper dbHelper;
    private Map<Integer, InventoryItem> inventoryMap;

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
        inventoryMap = new HashMap<>();
        for (InventoryItem item : inventoryList) {
            inventoryMap.put(item.getId(), item);
        }

        dataRecyclerView = findViewById(R.id.dataRecyclerView);
        dataRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        inventoryAdapter = new InventoryAdapter(inventoryList);
        dataRecyclerView.setAdapter(inventoryAdapter);

        EditText searchEditText = findViewById(R.id.searchEditText);
        Button sortByNameButton = findViewById(R.id.sortByNameButton);
        Button sortByQuantityButton = findViewById(R.id.sortByQuantityButton);

        searchEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inventoryAdapter.filterList(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        sortByNameButton.setOnClickListener(v -> inventoryAdapter.sortByName());
        sortByQuantityButton.setOnClickListener(v -> inventoryAdapter.sortByQuantity());



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
            String name = inputName.getText().toString().trim();
            String  quantityStr = inputQuantity.getText().toString().trim();

            if (name.isEmpty() || quantityStr.isEmpty()) {
                Toast.makeText(this, "Please enter a name and quantity.", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr);
                if(quantity <= 0) {
                    Toast.makeText(this, "Quantity must be a positive number.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Quantity must be a valid number.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedItemPosition == -1) {
                // Add new item
                InventoryItem newItem = new InventoryItem(name, quantity);
                dbHelper.addItem(newItem);
                inventoryList.add(new InventoryItem(name, quantity));
                inventoryAdapter.notifyItemInserted(inventoryList.size() - 1);
            } else {
                // Update existing item
                InventoryItem oldItem = inventoryList.get(selectedItemPosition);
                InventoryItem updatedItem = new InventoryItem(oldItem.getId(), name, quantity);
                dbHelper.updateItem(updatedItem);
                inventoryList.set(selectedItemPosition, updatedItem);
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

        Button backupButton = findViewById(R.id.backupButton);
        Button restoreButton = findViewById(R.id.restoreButton);

        backupButton.setOnClickListener(v -> {
            File file = new File(getFilesDir(), "inventory_backup.json");
            boolean success = dbHelper.backupInventoryToJson(file);
            Toast.makeText(this, success ? "Backup successful" : "Backup failed", Toast.LENGTH_SHORT).show();
        });

        restoreButton.setOnClickListener(v -> {
            File file = new File(getFilesDir(), "inventory_backup.json");
            boolean success = dbHelper.restoreInventoryFromJson(file);

            if (success) {
                inventoryList.clear();
                inventoryList.addAll(dbHelper.getAllItems());
                inventoryAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Restore successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Restore failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeItem(int position) {
        InventoryItem itemToDelete = inventoryList.get(position);
        dbHelper.deleteItem(itemToDelete.getId());
        inventoryList.remove(position);
        inventoryAdapter.notifyItemRemoved(position);
    }
}