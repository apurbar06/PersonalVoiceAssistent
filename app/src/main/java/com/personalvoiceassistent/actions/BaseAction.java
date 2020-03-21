package com.personalvoiceassistent.actions;

import android.content.Context;

import java.util.ArrayList;

public abstract class BaseAction {
    public Context mContext;
    public ArrayList<String> globalList;

    public BaseAction(Context context) {
        this.mContext = context;
    }

    public void makeListToGlobalScope(ArrayList<String> list) {
        globalList = list;
    }

    /**
     * check if the action is eligible for running
     *
     * @return boolean when match
     */
    public boolean doesMatch(String base) {
        boolean match = false;
        for (String item : globalList) {
            if (!match) {
                match = base.toLowerCase().contains(item);
            }
        }
        return match;
    }

    /**
     * @return result of the command
     */
    abstract public String runCommand(String msg);

}
