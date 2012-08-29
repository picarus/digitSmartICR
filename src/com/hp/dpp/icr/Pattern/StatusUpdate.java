package com.hp.dpp.icr.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: JOMAG
 * Date: Oct 2, 2003
 * Time: 10:12:59 PM
 * To change this template use Options | File Templates.
 */
public class StatusUpdate {
    private float last_;
    private float lastDif_;

    public StatusUpdate(float last, float lastDiff) {
        last_ = last;
        lastDif_ = lastDiff;
    }

    public float getLast() {
        return last_;
    }

    public float getLastDif() {
        return lastDif_;
    }


}
