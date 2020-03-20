package com.personalvoiceassistent.actions;

import android.content.Context;

public abstract class BaseAction {
    public Context mContext;

    public BaseAction(Context context) {
        this.mContext = context;
    }

    ;

    /**
     * check if the action is eligible for running
     *
     * @return boolean when match
     */
    abstract public boolean doesMatch(String msg);

    /**
     * @return result of the command
     */
    abstract public String runCommand(String msg);

}
