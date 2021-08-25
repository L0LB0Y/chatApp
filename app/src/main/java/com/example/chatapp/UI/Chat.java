package com.example.chatapp.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.chatapp.ChatsAdapter.ChatAdapter;
import com.example.chatapp.databinding.ActivityMainBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class Chat extends AppCompatActivity {

    ActivityMainBinding binding;
    String roomName;
    String userName;
    ArrayList<String> listOfMessage;
    ChatAdapter adapter;
    String keyMessage;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        listOfMessage = new ArrayList<>();
        Intent intent = getIntent();
        roomName = intent.getStringExtra("roomName");
        userName = intent.getStringExtra("UserName");
        Log.d("LOG", userName);
        setTitle("Room - " + roomName);
        reference = FirebaseDatabase.getInstance().getReference().child(roomName);
        initUI();
        binding.imageSend.setOnClickListener(view -> {
            String sms = binding.textInput.getText().toString();
            if (!sms.isEmpty()) {
                Map<String, Object> map = new HashMap<>();
                keyMessage = reference.push().getKey();
                reference.updateChildren(map);

                // inside Message child
                DatabaseReference message = reference.child(keyMessage);

                Map<String, Object> map2 = new HashMap<>();
                map2.put("UserName", userName);
                map2.put("Message", sms);
                message.updateChildren(map2);
            } else {
                binding.textInput.setError("Write a Message");
            }
            binding.textInput.setText("");
        });

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                commitDataToAdapter(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                commitDataToAdapter(snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                commitDataToAdapter(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void commitDataToAdapter(DataSnapshot snapshot) {
        String user, sms;
        Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
        while (iterator.hasNext()) {
            sms = (String) iterator.next().getValue();
            user = (String) iterator.next().getValue();
            listOfMessage.add(user + ":   " + sms);
        }
        adapter.notifyDataSetChanged();

    }

    private void initUI() {
        adapter = new ChatAdapter(listOfMessage, getLayoutInflater());
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

    }
}