package com.personalvoiceassistent.actions;

import android.content.Context;

public class ActionTime extends BaseAction {
    private String MATCH_STR = "time";

    public ActionTime(Context context) {
        super(context);

    }

    @Override
    public boolean doesMatch(String msg) {
        if (msg.toLowerCase().contains(MATCH_STR)) return true;
        return false;
    }

    @Override
    public String runCommand(String msg) {
        return "TIME";
    }
}
