package com.personalvoiceassistent;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.personalvoiceassistent.model.Chat;
import com.personalvoiceassistent.model.ChatAdapter;
import com.personalvoiceassistent.model.SpeechHandler;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView textView;
    private ChatAdapter chatAdapter;
    private AudioManager audioManager;
    private RecyclerView recyclerView;
    private SpeechHandler speechHandler;
    private ArrayList<Chat> chats = new ArrayList<>();
    private SpeechHandler.Callbacks speechHandlerCallbacks = new SpeechHandler.Callbacks() {
        @Override
        public void onResult(String msg) {
            textView.setText(msg);
            chats.add(new Chat(msg, Chat.USER));
            chatAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(chats.size() - 1);
            textView.setText("");
        }

        @Override
        public void partialResult(String msg) {
            Log.d(TAG, "partialResult: " + msg);
            textView.setText(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayoutManager layout = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerView);
        textView = findViewById(R.id.previewText);
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        chats.add(new Chat("hello", Chat.BOT));
        chats.add(new Chat("hi", Chat.USER));
        chats.add(new Chat("hello", Chat.BOT));
        chats.add(new Chat("hi", Chat.USER));
        chats.add(new Chat("hello", Chat.BOT));
        chats.add(new Chat("hasdi", Chat.USER));

        chatAdapter = new ChatAdapter(chats);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(chatAdapter);
        // speech handler config
        speechHandler = new SpeechHandler(this);
        speechHandler.requestRecordAudioPermission();
        speechHandler.enableRestart();
        speechHandler.initRecognition();
        speechHandler.addCallback(speechHandlerCallbacks);
        // speech handler is ready
        muteAudio(true);
        boolean success = speechHandler.startRecognition();
    }

    @Override
    protected void onPause() {
        super.onPause();
        muteAudio(false);
        speechHandler.disableRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // lock device audio
        muteAudio(true);
        speechHandler.enableRestart();
        if (!speechHandler.isRecognising) {
            speechHandler.startRecognition();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // release device audio
        muteAudio(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // release device audio
        muteAudio(false);
    }

    /**
     * mute audio for stop the default beep sound
     *
     * @param mute mute status
     */
    private void muteAudio(Boolean mute) {
        try {
            // mute (or) un mute audio based on status
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, mute ? AudioManager.ADJUST_MUTE : AudioManager.ADJUST_UNMUTE, 0);
            } else {
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, mute);
            }
        } catch (Exception e) {
            if (audioManager == null) return;

            // un mute the audio if there is an exception
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
            } else {
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            }
        }
    }

    public void handelClick(View view) {
        // test code
        chats.add(new Chat("hello", Chat.BOT));
        chats.add(new Chat("hi", Chat.USER));
        chatAdapter.notifyDataSetChanged();
    }

}

