package com.personalvoiceassistent.actions;

import android.content.Context;

import java.util.ArrayList;

public abstract class BaseAction {
    public Context mContext;
    public ArrayList<String> MATCH_STR;

    public BaseAction(Context context) {
        this.mContext = context;
    }

    public void addList(ArrayList<String> str) {
        MATCH_STR = str;
    }

    /**
     * check if the action is eligible for running
     *
     * @return boolean when match
     */
    public boolean doesMatch(String base) {
        boolean match = false;
        for (String msg : MATCH_STR) {
            if (!match) {

                match = base.toLowerCase().contains(msg);
            }
        }
        return match;
    }

    /**
     * @return result of the command
     */
    abstract public String runCommand(String msg);

}
