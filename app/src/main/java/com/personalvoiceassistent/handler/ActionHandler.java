package com.personalvoiceassistent.handler;

import android.content.Context;
import android.util.Log;

import com.personalvoiceassistent.actions.ActionTime;


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
        if (at.doesMatch(msg)) {
            result = at.runCommand(msg);
            Log.d(TAG, "matchCommand: " + result);
        }
        return result;

    }
}
