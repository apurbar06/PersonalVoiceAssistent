package com.personalvoiceassistent.handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.personalvoiceassistent.R;
import com.personalvoiceassistent.model.Chat;

import java.util.ArrayList;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
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
        RelativeLayout view = (RelativeLayout) layoutInflater.inflate(layout, parent, false);
        Log.d(TAG, String.format("onCreateViewHolder: %s", viewType));
        return new MyViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return chats.get(position).getViewType();
    }


    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TextView textView = holder.itemView.findViewById(R.id.txt);
        Chat chat = chats.get(position);
        textView.setText(chat.getMsg());
        Log.d(TAG, String.format("onBindViewHolder: %s", position));

    }


    @Override
    public int getItemCount() {
        return chats.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout;

        MyViewHolder(@NonNull RelativeLayout itemView) {
            super(itemView);
            Log.d(TAG, "MyViewHolder: ");
            layout = itemView;
        }
    }
}

