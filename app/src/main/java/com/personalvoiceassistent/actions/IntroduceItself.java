package com.personalvoiceassistent.actions;

import android.content.Context;

import java.util.ArrayList;

public class IntroduceItself extends BaseAction {
    ArrayList<String> list =new ArrayList<String>();

    public IntroduceItself(Context context) {
        super(context);
        list.add("who are you");
        list.add("what is you");
        list.add("tell something about yourself");
        list.add("who made you");
        list.add("who created you");
        list.add("who programmed you");
        list.add("what is your identity");
        makeListToGlobalScope(list);
    }


    @Override
    public String runCommand(String msg) {
        return "I am a personal virtual assistant.As of now I am in my initial stage.";
    }
}
