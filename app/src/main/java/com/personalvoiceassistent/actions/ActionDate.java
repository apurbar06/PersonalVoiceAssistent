package com.personalvoiceassistent.actions;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ActionDate extends BaseAction {
    private String MATCH_STR = "date";

    public ActionDate(Context context) {
        super(context);
    }

    @Override
    public boolean doesMatch(String msg) {
        if (msg.toLowerCase().contains(MATCH_STR)) return true;
        return false;
    }

    @Override
    public String runCommand(String msg) {
        String output = "NOT_FOUND";
        output = new SimpleDateFormat("dd:MM").format(Calendar.getInstance().getTime());
        return output;
    }
}
