package com.personalvoiceassistent.actions;

import android.content.Context;


import java.util.ArrayList;
import java.util.Random;


public class Greetings extends BaseAction {
    ArrayList<String> list =new ArrayList<String>();



    public Greetings(Context context) {
        super(context);
        list.add("hello");
        list.add("hi");
        list.add("hey there");
        list.add("hai");
        list.add("hey man");
        list.add("hey");
        this.MATCH_STR = list;

    }

    @Override
    public String runCommand(String msg) {
        Random r = new Random();
        int i =r.nextInt(list.size());
        String result = list.get(i);
        return result;

    }
}
