package com.personalvoiceassistent;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
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
    TextToSpeech textToSpeech;
    private TextView textView;
    private ChatAdapter chatAdapter;
    private AudioManager audioManager;
    private RecyclerView recyclerView;
    private SpeechHandler speechHandler;
    private ArrayList<Chat> chats = new ArrayList<>();
    ActionHandler ah;

    private SpeechHandler.Callbacks speechHandlerCallbacks = new SpeechHandler.Callbacks() {
        @Override
        public void onResult(String msg) {
            // try to run command
            // if successful return result
            String result = ah.tryRunCommand(msg);
            // add user command to chat history
            chats.add(new Chat(msg, Chat.USER));
            if (result != null) {
                // add bot command to chat history
                chats.add(new Chat(result, Chat.BOT));
                // speak bot command loudly
                speak(result);
            }
            // notify our chat adapter data has changed so it can update
            chatAdapter.notifyDataSetChanged();
            // scroll to bottom chat
            recyclerView.smoothScrollToPosition(chats.size() - 1);
            // clear text preview of user command
            textView.setText("");
            // restart speech handler
            speechHandler.startRecognition();
        }

        @Override
        public void partialResult(String msg) {
            Log.d(TAG, "partialResult: " + msg);
            textView.setText(msg);
        }
    };


    private int speak(String text) {
        // param is necessary for utterance
        Bundle params = new Bundle();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "45a4sd");
        int status = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            status = textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, params, "UniqueID");
        } else {
            status = textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }

        if (status == TextToSpeech.ERROR) {
            Log.d(TAG, "TTS: error in converting Text to Speech!");
        } else {
            Log.d(TAG, "TTS: success");
        }
        return status;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get action handler where all action is located
        ah = new ActionHandler(MainActivity.this);
        // init text to speech
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
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
            }
        });
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                muteAudio(false);
                // running ui on thread or the speech recognition service will not start
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        speechHandler.destroyRecognition();
                    }
                });
                Log.d(TAG, "onStart: ");
            }

            @Override
            public void onDone(String utteranceId) {
                muteAudio(true);
                // running ui on thread or the speech recognition service will not start
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        speechHandler.startRecognition();
                    }
                });
                Log.d(TAG, "onDone: ");
            }

            @Override
            public void onError(String utteranceId) {
                muteAudio(true);
                // running ui on thread or the speech recognition service will not start
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        speechHandler.startRecognition();
                    }
                });
                Log.d(TAG, "onError: ");
            }
        });


        LinearLayoutManager layout = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerView);
        textView = findViewById(R.id.previewText);
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);


        chatAdapter = new ChatAdapter(chats);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(chatAdapter);
        // speech handler config
        speechHandler = new SpeechHandler(this);
        speechHandler.requestRecordAudioPermission();
        speechHandler.addCallback(speechHandlerCallbacks);
        // speech handler is ready
        muteAudio(true);
        speechHandler.startRecognition();
        chatAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        muteAudio(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // lock device audio
        muteAudio(true);
        speechHandler.startRecognition();

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
        speechHandler.destroyRecognition();
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


}

