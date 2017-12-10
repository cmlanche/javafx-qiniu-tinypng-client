package com.cmlanche.bloghelper.model;

/**
 * Created by cmlanche on 2017/12/10.
 */
public class ProcessData {

    public static final int IDLE = -1;
    public static final int WAITING = 0;
    public static final int PROCESSING = 1;
    public static final int ERROR = -2;
    public static final int FINISH = 2;

    private int state = IDLE;

    public ProcessData(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
