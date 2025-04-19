package com.example.a5_3_project_two_by_dawson_kennedy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder> {

    private List<InventoryItem> inventoryItems;
    private OnItemClickListener listener;

    // Define the custom OnItemClickListener interface
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);

    }

    // Setter for the listener
    public void setOnItemClickListener(OnItemClickListener listener) {

        this.listener = listener;
    }

    // ViewHolder class for the RecyclerView items
    public static class InventoryViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewQuantity;
        public TextView deleteTextView;

        public InventoryViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewQuantity = itemView.findViewById(R.id.text_view_quantity);
            deleteTextView = itemView.findViewById(R.id.text_view_delete);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });

            deleteTextView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDeleteClick(position);
                    }
                }
            });
        }
    }

    public InventoryAdapter(List<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_item, parent, false);
        return new InventoryViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        InventoryItem currentItem = inventoryItems.get(position);
        holder.textViewName.setText(currentItem.getName());
        holder.textViewQuantity.setText(String.valueOf(currentItem.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return inventoryItems.size();
    }
}
