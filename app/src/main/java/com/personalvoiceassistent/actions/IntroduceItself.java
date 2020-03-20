package com.personalvoiceassistent.actions;

import android.content.Context;

public class IntroduceItself extends BaseAction {
    private String MATCH_STR = "who are you";

    public IntroduceItself(Context context) {
        super(context);
    }

    @Override
    public boolean doesMatch(String msg) {
        if (msg.toLowerCase().contains(MATCH_STR)) return true;
        return false;
    }

    @Override
    public String runCommand(String msg) {
        return "I am a personal virtual assistant.";
    }
}
