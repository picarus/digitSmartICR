package com.hp.dpp.icr.Pattern;

import com.hp.dpp.icr.ChangeStroke.ChangeStroke;

/**
 * Created by IntelliJ IDEA.
 * User: JOMAG
 * Date: Oct 2, 2003
 * Time: 9:40:14 PM
 * To change this template use Options | File Templates.
 */
public class ChangeStatus {

    int x_;
    int y_;

    public ChangeStatus() {
        x_ = ChangeStroke.NOWHERE;
        y_ = ChangeStroke.NOWHERE;
    }

    public ChangeStatus(int x, int y) {
        x_ = x;
        y_ = y;
    }

    public int getX() {
        return x_;
    }

    public int getY() {
        return y_;
    }

    public void update(ChangeStatus cs) {
        x_ = cs.getX();
        y_ = cs.getY();
    }

    public void setX(int x) {
        x_ = x;
    }

    public void setY(int y) {
        y_ = y;
    }

    public boolean equals(Object obj) {
        ChangeStatus cs = (ChangeStatus) obj;
        return (x_ == cs.getX()) && (y_ == cs.getY());
    }

}
