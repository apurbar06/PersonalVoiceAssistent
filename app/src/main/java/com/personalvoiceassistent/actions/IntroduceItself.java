package com.personalvoiceassistent.actions;

import android.content.Context;

import java.util.ArrayList;

public class IntroduceItself extends BaseAction {

    public IntroduceItself(Context context) {
        super(context);
        ArrayList<String> list =new ArrayList<String>();
        list.add("who are you");
        this.MATCH_STR = list;
    }


    @Override
    public String runCommand(String msg) {
        return "I am a personal virtual assistant.";
    }
}
