package com.example.chatapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chatapp.RoomsAdapter.DataTransferInterface;
import com.example.chatapp.RoomsAdapter.RoomAdapter;
import com.example.chatapp.databinding.ActivityChatRoomBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Room extends AppCompatActivity implements DataTransferInterface {

    ActivityChatRoomBinding binding;
    ArrayList<String> listOfRoom;
    String userName;
    RoomAdapter adapter;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        reference = FirebaseDatabase.getInstance().getReference().getRoot();
        listOfRoom = new ArrayList<>();
        userName = "root";
        initUi();
        getUserName();
        binding.imageSendChatRoom.setOnClickListener(x -> addRoomNameToDB());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfRoom.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    listOfRoom.add(dataSnapshot.getKey());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addRoomNameToDB() {
        Map<String, Object> map = new HashMap<>();
        String roomName = binding.textInputChatRoom.getText().toString();
        if (roomName.isEmpty()) {
            binding.textInputChatRoom.setError("You Must Enter A Room Name");
        } else {
            map.put(roomName, "");
            reference.updateChildren(map);
        }
        binding.textInputChatRoom.setText("");
        binding.textInputChatRoom.clearFocus();
    }

    private void getUserName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Your Name Please");
        EditText editText = new EditText(this);
        builder.setView(editText);
        builder.setPositiveButton("Ok", ((dialogInterface, i) -> userName = editText.getText().toString()));
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            dialogInterface.cancel();
            getUserName();
            Toast.makeText(Room.this, "You Must Enter Your Name !!!", Toast.LENGTH_SHORT).show();
        });
        builder.show();
    }

    private void initUi() {
        adapter = new RoomAdapter(listOfRoom, getLayoutInflater(), this);
        binding.recyclerViewChatRoom.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerViewChatRoom.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewChatRoom.setAdapter(adapter);
    }

    @Override
    public void onItemClicked(String roomName) {
        Intent intent = new Intent(this, Chat.class);
        intent.putExtra("roomName", roomName);
        if (userName.isEmpty())
            userName = "root";
        intent.putExtra("UserName", userName);
        startActivity(intent);
    }
}