package com.personalvoiceassistent.actions;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ActionDate extends BaseAction {

    public ActionDate(Context context) {
        super(context);
        ArrayList<String> list = new ArrayList<String>();
        list.add("date");
        makeListToGlobalScope(list);
    }

    @Override
    public String runCommand(String msg) {
        String output = "NOT_FOUND";
        String output1 = new SimpleDateFormat("dd MMM").format(Calendar.getInstance().getTime());
        String output2 = new SimpleDateFormat("EEEE").format(Calendar.getInstance().getTime());
        output = " Today is " + output2 + " It's " + output1;
        return output;
    }
}
