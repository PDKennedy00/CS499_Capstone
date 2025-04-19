package com.example.a5_3_project_two_by_dawson_kennedy;

public class InventoryItem {
    private int id;
    private String name;
    private int quantity;

    // Default Constructor
    public InventoryItem() {

    }

    public InventoryItem(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;

    }

    // Getter and Setter for ID
    public int getId() {
        return id;

    }

    public void setId(int id) {
        this.id = id;

    }

    // Getter and Setter for Name
    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;

    }

    // Getter and Setter for Quantity
    public int getQuantity() {
        return quantity;

    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
