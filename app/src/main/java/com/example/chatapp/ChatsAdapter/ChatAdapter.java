package com.example.chatapp.ChatsAdapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.databinding.ChatItemBinding;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyHolder> {

    LayoutInflater layoutInflater;
    ArrayList<String> list;

    public ChatAdapter(ArrayList<String> list, LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChatItemBinding binding = ChatItemBinding.inflate(layoutInflater);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.binding.chat.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ChatItemBinding binding;

        public MyHolder(ChatItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
