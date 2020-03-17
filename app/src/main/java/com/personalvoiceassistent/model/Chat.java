package com.personalvoiceassistent.model;

public class Chat {
    public static final int BOT = 0;
    public static final int USER = 1;
    private String msg;
    private int viewType;


    public Chat(String msg, int viewType) {
        this.msg = msg;
        this.viewType = viewType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public boolean isUser() {
        return this.viewType == USER;
    }

    public boolean isBot() {
        return this.viewType == BOT;
    }

}
