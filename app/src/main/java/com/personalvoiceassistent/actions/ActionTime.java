package com.personalvoiceassistent.actions;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ActionTime extends BaseAction {
    public ActionTime(Context context) {
        super(context);
        ArrayList<String> list =new ArrayList<String>();
        list.add("time");
        makeListToGlobalScope(list);
    }


    @Override
    public String runCommand(String msg) {
        String output = "NOT_FOUND";
        output = new SimpleDateFormat("hh mm aa").format(Calendar.getInstance().getTime());
        return "It's " + output;
    }
}
