package com.personalvoiceassistent.actions;

import android.content.Context;

import java.util.ArrayList;

public class Greetings extends BaseAction {

    public Greetings(Context context) {
        super(context);
        ArrayList<String> list =new ArrayList<String>();
        list.add("hello");
        list.add("hi");
        this.MATCH_STR = list;
    }

    @Override
    public String runCommand(String msg) {
        return "Hi";
    }
}
