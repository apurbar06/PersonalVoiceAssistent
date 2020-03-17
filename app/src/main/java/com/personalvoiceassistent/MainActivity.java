package com.personalvoiceassistent;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.personalvoiceassistent.model.Chat;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ArrayList<Chat> chats = new ArrayList<>();
    private ChatAdapter chatAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        chats.add(new Chat("hello", Chat.BOT));
        chats.add(new Chat("hi", Chat.USER));
        chats.add(new Chat("hello", Chat.BOT));
        chats.add(new Chat("hi", Chat.USER));
        chats.add(new Chat("hello", Chat.BOT));
        chats.add(new Chat("hasdi", Chat.USER));


        chatAdapter = new ChatAdapter(chats);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

    }


    public void handelClick(View view) {
        chats.add(new Chat("hello", Chat.BOT));
        chats.add(new Chat("hi", Chat.USER));
        chatAdapter.notifyDataSetChanged();
    }
}

