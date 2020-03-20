package com.personalvoiceassistent.model;

public class Chat {
    public static final int BOT = 0;
    public static final int USER = 1;
    private String msg;
    private int speakerType;


    public Chat(String msg, int speakerType) {
        this.msg = msg;
        this.speakerType = speakerType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getSpeakerType() {
        return speakerType;
    }

    public void setSpeakerType(int speakerType) {
        this.speakerType = speakerType;
    }

    public boolean isUser() {
        return this.speakerType == USER;
    }

    public boolean isBot() {
        return this.speakerType == BOT;
    }

}
