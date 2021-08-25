package com.example.chatapp.RoomsAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.databinding.RoomItemBinding;

import java.util.ArrayList;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.MyHolder> {

    LayoutInflater layoutInflater;
    ArrayList<String> list;
    DataTransferInterface DataTransferInterface;

    public RoomAdapter(ArrayList<String> list, LayoutInflater layoutInflater, DataTransferInterface dataTransferInterface) {
        this.layoutInflater = layoutInflater;
        this.list = list;
        this.DataTransferInterface = dataTransferInterface;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RoomItemBinding binding = RoomItemBinding.inflate(layoutInflater);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.binding.room.setText(list.get(position));
        holder.binding.rootRoomItem.setOnClickListener(view -> {
            this.DataTransferInterface.onItemClicked(list.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {

        RoomItemBinding binding;

        public MyHolder(RoomItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
