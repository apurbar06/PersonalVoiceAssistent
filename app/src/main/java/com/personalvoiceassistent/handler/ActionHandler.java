package com.personalvoiceassistent.handler;

import android.content.Context;
import android.util.Log;

import com.personalvoiceassistent.actions.ActionDate;
import com.personalvoiceassistent.actions.ActionTime;
import com.personalvoiceassistent.actions.Greetings;


public class ActionHandler {
    private static final String TAG = "ActionHandler";
    private Context mContext;

    public ActionHandler(Context context) {
        super();
        this.mContext = context;
    }

    public String tryRunCommand(String msg) {
        String result = null;
        ActionTime at = new ActionTime(mContext);
        ActionDate ad = new ActionDate(mContext);
        Greetings gr = new Greetings(mContext);
        if (at.doesMatch(msg)) {
            result = at.runCommand(msg);
            Log.d(TAG, "matchCommand: " + result);
        }
        else if (ad.doesMatch(msg)) {
            result = ad.runCommand(msg);
            Log.d(TAG, "matchCommand: " + result);
        }
        else if (gr.doesMatch(msg)) {
            result = gr.runCommand(msg);
            Log.d(TAG, "matchCommand: " + result);
        }

        return result;

    }
}
