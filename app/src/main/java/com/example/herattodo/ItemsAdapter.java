package com.example.herattodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

//Displays data from model into a row in the recycler view
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    public interface  OnClickListener{
        void onItemClicked(int position);
    }
    public interface OnLongClickListener{
        void onItemLongClicked(int position);
    }

    OnLongClickListener longClickListener;
    List<String> items;
    OnClickListener clickListener;
    public ItemsAdapter(List<String> items, OnLongClickListener longClickListener, OnClickListener clickListener){
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Layout Inflator to inflate view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(todoView) ;
    }
    // responsible for binding data to particular view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Grab item at position
        String item = items.get(position);
        //Bind into view holder
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //Container to provide access to views for each row list
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }
        public void bind(String item) {
            tvItem.setText(item);
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //Notify the listener which position was long clicked
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
