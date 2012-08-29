package com.hp.dpp.icr.Pattern;

import com.hp.dpp.icr.ChangeStroke.Point;
import com.hp.dpp.icr.Pattern.SequenceChange;

/**
 * Date: Oct 5, 2003
 * Time: 11:49:12 PM
 */
public class SequenceChangeImplInt implements SequenceChange {

    protected int[] x_;
    protected int[] y_;
    protected int i;
    protected int len;

    public SequenceChangeImplInt(int[] x, int[] y) {
        x_ = x;
        y_ = y;
        init();
    }

    public void init() {
        i = 0;
        len = Math.min(x_.length, y_.length);
    }

    public com.hp.dpp.icr.ChangeStroke.Point getFirstPoint() {
        init();
        return getPoint();
    }

    public com.hp.dpp.icr.ChangeStroke.Point getPoint() {
        com.hp.dpp.icr.ChangeStroke.Point p = new com.hp.dpp.icr.ChangeStroke.Point(x_[i], y_[i]);
        i++;
        return p;
    }

    public com.hp.dpp.icr.ChangeStroke.Point getNextPoint() {
        return getPoint();
    }

    public boolean hasNext() {
        return i < len;
    }
}

