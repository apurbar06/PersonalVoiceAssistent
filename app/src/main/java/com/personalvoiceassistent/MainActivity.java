package com.personalvoiceassistent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private ArrayList<Chat> chats = new ArrayList<>();
    private ChatAdapter chatAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        chats.add(new Chat("hello", Chat.BOT));
        chats.add(new Chat("hi", Chat.USER));
        chats.add(new Chat("hello", Chat.BOT));
        chats.add(new Chat("hi", Chat.USER));
        chats.add(new Chat("hello", Chat.BOT));
        chats.add(new Chat("hi", Chat.USER));


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

class ChatAdapter extends RecyclerView.Adapter {
    private static final String TAG = "MyAdapter";
    private ArrayList<Chat> chats;

    public ChatAdapter(ArrayList<Chat> chats) {
        Log.d(TAG, "MyAdapter: ");
        this.chats = chats;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        Chat chat = this.chats.get(viewType);
        int layout = chat.isBot() ? R.layout.chat_bot : R.layout.chat_user;
        View view = layoutInflater.inflate(layout, parent, false);
        Log.d(TAG, String.format("onCreateViewHolder: %s", viewType));
        return new MyViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return chats.get(position).getViewType();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, String.format("onBindViewHolder: %s", position));

    }


    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        View layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "MyViewHolder: ");
            layout = itemView;
        }
    }
}


class Chat {
    static final int BOT = 0;
    static final int USER = 1;
    private String msg;
    private int viewType;


    public Chat(String msg, int viewType) {
        this.msg = msg;
        this.viewType = viewType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public boolean isUser() {
        return this.viewType == USER;
    }

    public boolean isBot() {
        return this.viewType == BOT;
    }

}
