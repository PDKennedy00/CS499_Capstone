package com.example.a5_3_project_two_by_dawson_kennedy;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DBHelperTest {

    private DBHelper dbHelper;
    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        dbHelper = new DBHelper(context);
    }

    @After
    public void tearDown() {
        context.deleteDatabase("inventory.db");
    }

    @Test
    public void testAddItem() {
        InventoryItem item = new InventoryItem("Test Item", 10);
        dbHelper.addItem(item);
        List<InventoryItem> itemList = dbHelper.getAllItems();
        assertTrue(itemList.stream().anyMatch(i -> i.getName().equals("Test Item") && i.getQuantity() == 10));
    }

    @Test
    public void testUpdateItem() {
        InventoryItem item = new InventoryItem("Test Item", 5);
        dbHelper.addItem(item);
        List<InventoryItem> items = dbHelper.getAllItems();
        InventoryItem insertedItem = items.get(0);
        insertedItem.setQuantity(15);
        dbHelper.updateItem(insertedItem);
        List<InventoryItem> updatedList = dbHelper.getAllItems();
        assertEquals(15, updatedList.get(0).getQuantity());
    }

    @Test
    public void testDeleteItem() {
        InventoryItem item = new InventoryItem("Delete Me", 1);
        dbHelper.addItem(item);
        List<InventoryItem> items = dbHelper.getAllItems();
        InventoryItem insertedItem = items.get(0);
        dbHelper.deleteItem(insertedItem.getId());
        List<InventoryItem> afterDelete = dbHelper.getAllItems();
        assertTrue(afterDelete.isEmpty());
    }
}
