package com.personalvoiceassistent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView textView;
    private SpeechRecognizer sr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.resultText);
        sr = SpeechRecognizer.createSpeechRecognizer(this);
        sr.setRecognitionListener(new listener());
        requestRecordAudioPermission();
    }

    private void requestRecordAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String requiredPermission = Manifest.permission.RECORD_AUDIO;

            // If the user previously denied this permission then show a message explaining why
            // this permission is needed
            if (checkCallingOrSelfPermission(requiredPermission) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{requiredPermission}, 101);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, String.format("onActivityResult: requestCode:%s, resultCode: %s, data:%s ", requestCode, resultCode, data));
        if (resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null) {
                String input = result.get(0);
                textView.setText(String.format("Result: %s", input));
            }
        } else {
            Toast.makeText(getApplicationContext(), "Failed to recognize speech!", Toast.LENGTH_LONG).show();
        }
    }

    public void recogniseSpeech(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "voice.recognition.test");

        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
        if (intent.resolveActivity(getPackageManager()) != null) {
            sr.startListening(intent);
            Log.i(TAG, "Started Intent");

        } else {
            Toast.makeText(MainActivity.this, "Your Device Does Not support Speech Input", Toast.LENGTH_SHORT).show();
        }
//        Log.d(TAG, "recogniseSpeech: ");
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(intent, 10);
//        } else {
//            Toast.makeText(MainActivity.this, "Your Device Does Not support Speech Input", Toast.LENGTH_SHORT).show();
//        }
    }

    class listener implements RecognitionListener {
        private boolean isEndOfSpeech = false;

        public void onReadyForSpeech(Bundle params) {
            Log.d(TAG, "onReadyForSpeech");
        }

        public void onBeginningOfSpeech() {
            Log.d(TAG, "onBeginningOfSpeech");
        }

        public void onRmsChanged(float rmsdB) {
            Log.d(TAG, "onRmsChanged");
        }

        public void onBufferReceived(byte[] buffer) {
            Log.d(TAG, "onBufferReceived");
        }

        public void onEndOfSpeech() {
            isEndOfSpeech = true;
            Log.d(TAG, "onEndofSpeech");
        }

        public void onError(int error) {
            Log.d(TAG, "error " + error);
            if (!isEndOfSpeech) {
                Log.d(TAG, "onError: ");
            }
            String message = "";
            if (error == SpeechRecognizer.ERROR_AUDIO) message = "audio";
            else if (error == SpeechRecognizer.ERROR_CLIENT) message = "client";
            else if (error == SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS)
                message = "insufficient permissions";
            else if (error == SpeechRecognizer.ERROR_NETWORK) message = "network";
            else if (error == SpeechRecognizer.ERROR_NETWORK_TIMEOUT) message = "network timeout";
            else if (error == SpeechRecognizer.ERROR_NO_MATCH) message = "no match found";
            else if (error == SpeechRecognizer.ERROR_RECOGNIZER_BUSY) message = "recognizer busy";
            else if (error == SpeechRecognizer.ERROR_SERVER) message = "server";
            else if (error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT) message = "speech timeout";

            Log.d(TAG, "onError: " + message);
        }

        public void onResults(Bundle results) {
            String str = "";
            Log.d(TAG, "onResults " + results);
            ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (data != null) {
                for (int i = 0; i < data.size(); i++) {
                    Log.d(TAG, "result " + data.get(i));
                    str += data.get(i);
                }
                textView.setText(String.format("results: %s", data.get(0)));
            } else {
                Toast.makeText(MainActivity.this, "Data Is Null", Toast.LENGTH_SHORT).show();
            }

        }

        public void onPartialResults(Bundle partialResults) {
            Log.d(TAG, "onPartialResults");
        }

        public void onEvent(int eventType, Bundle params) {
            Log.d(TAG, "onEvent " + eventType);
        }

    }
}
