package com.personalvoiceassistent.handler;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;


public class SpeechHandler {
    private static final String TAG = "SpeechHandler";
    public boolean isRecognising = false;
    private boolean try_restart = false;
    private Intent recognizerIntent;
    private Activity pContext;
    private Callbacks callbacks;
    private SpeechRecognizer sr;

    public SpeechHandler(Activity c) {
        pContext = c;
        Listener listener = new Listener();
        sr = SpeechRecognizer.createSpeechRecognizer(pContext);
        sr.setRecognitionListener(listener);
    }

    public void enableRestart() {
        try_restart = true;
    }

    public void disableRestart() {
        try_restart = false;
    }


    /**
     * creates a recognition intent
     */
    public void initRecognition() {
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "voice.recognition.test");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
    }

    public void addCallback(Callbacks cb) {
        callbacks = cb;
    }


    public boolean startRecognition() {
        Log.d(TAG, "startRecognition: ");
        initRecognition();
        if (recognizerIntent.resolveActivity(pContext.getPackageManager()) != null) {
            isRecognising = true;
            sr.startListening(recognizerIntent);
            Log.i(TAG, "StartRecognition: Started Intent");
            return true;
        } else {
            Log.d(TAG, "StartRecognition: Your Device Does Not support Speech Input");
            return false;
        }
    }

    public void requestRecordAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String requiredPermission = Manifest.permission.RECORD_AUDIO;
            // If the user previously denied this permission then show a message explaining why
            // this permission is needed
            if (pContext.checkCallingOrSelfPermission(requiredPermission) == PackageManager.PERMISSION_DENIED) {
                pContext.requestPermissions(new String[]{requiredPermission}, 101);
            }
        }
    }


    public interface Callbacks {
        /**
         * @param msg message when full sentence is complete
         */
        void onResult(String msg);
        /**
         * @param msg when user is saying the words
         */
        void partialResult(String msg);
    }

    class Listener implements RecognitionListener {
        private static final String TAG = "RecognitionListener";

        String convertBundle(Bundle bundle) {
            Log.i(TAG, "convertBundle");
            ArrayList<String> matches = bundle
                    .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String text = "";
            if (matches != null) {
                for (String result : matches) {
                    text = result;
                }
            }
            return text;
        }

        @Override
        public void onBeginningOfSpeech() {
            Log.i(TAG, "onBeginningOfSpeech");
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            Log.i(TAG, "onBufferReceived: " + Arrays.toString(buffer));
        }

        @Override
        public void onEndOfSpeech() {
            Log.i(TAG, "onEndOfSpeech");
        }

        @Override
        public void onResults(Bundle results) {
            Log.i(TAG, "onResults");
            isRecognising = false;
            String text = convertBundle(results);
            callbacks.onResult(text);
            if (try_restart) {
                startRecognition();
            }
        }

        @Override
        public void onError(int errorCode) {
            String errorMessage = getErrorText(errorCode);
            isRecognising = false;
            if (try_restart) {
                startRecognition();
            }
            Log.i(TAG, "FAILED " + errorMessage);
        }

        @Override
        public void onEvent(int arg0, Bundle arg1) {
            Log.i(TAG, "onEvent");
        }

        @Override
        public void onPartialResults(Bundle bundle) {
            Log.i(TAG, "onPartialResults");
            String text = convertBundle(bundle);
            callbacks.partialResult(text);
        }

        @Override
        public void onReadyForSpeech(Bundle arg0) {
            Log.i(TAG, "onReadyForSpeech");
        }

        @Override
        public void onRmsChanged(float rms) {
        }

        String getErrorText(int errorCode) {
            String message;
            switch (errorCode) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "Audio recording error";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "Client side error";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "Insufficient permissions";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "Network error";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "Network timeout";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "No match";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RecognitionService busy";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "error from server";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "No speech input";
                    break;
                default:
                    message = "Didn't understand, please try again.";
                    break;
            }
            return message;
        }


    }

}

