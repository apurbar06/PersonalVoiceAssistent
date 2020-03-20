package com.personalvoiceassistent;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.personalvoiceassistent.handler.ActionHandler;
import com.personalvoiceassistent.handler.ChatAdapter;
import com.personalvoiceassistent.handler.SpeechHandler;
import com.personalvoiceassistent.model.Chat;

import java.util.ArrayList;
import java.util.Locale;


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
            ActionHandler ah = new ActionHandler(MainActivity.this);
            String result = ah.tryRunCommand(msg);
            chats.add(new Chat(msg, Chat.USER));
            if (result != null) {
                chats.add(new Chat(result, Chat.BOT));
                int status = textToSpeech.speak(result,TextToSpeech.QUEUE_FLUSH, null);
                if (status == TextToSpeech.ERROR) {
                    Log.d(TAG,"TTS: error in converting Text to Speech!");
                }else {
                    Log.d(TAG,"TTS: success");

                }
            }
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

    TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                muteAudio(false);
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = textToSpeech.setLanguage(Locale.US);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                } else {
                    Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
                muteAudio(true);
            }
        });



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

