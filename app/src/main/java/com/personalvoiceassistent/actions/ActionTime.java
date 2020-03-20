package com.personalvoiceassistent.actions;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

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
        String output = "NOT_FOUND";
        output = new SimpleDateFormat("hh mm aa").format(Calendar.getInstance().getTime());
        return output;
    }
}
