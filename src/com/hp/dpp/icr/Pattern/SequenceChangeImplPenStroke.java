package com.hp.dpp.icr.Pattern;

import com.hp.dpp.sdk.PenStroke;
import com.hp.dpp.sdk.SampleIterator;
import com.hp.dpp.icr.Pattern.SequenceChange;
import com.hp.dpp.icr.ChangeStroke.Point;

/**
 * Date: Oct 5, 2003
 * Time: 11:32:55 PM
 */
public class SequenceChangeImplPenStroke implements SequenceChange {

    protected PenStroke penStroke_;
    protected SampleIterator si_;

    public SequenceChangeImplPenStroke(PenStroke penStroke) {
        penStroke_ = penStroke;
        si_ = penStroke.getSampleIterator();
        init();
    }

    public void init() {
        si_.first();
    }

    public Point getFirstPoint() {
        return getPoint();
    }

    public Point getNextPoint() {
        si_.next();
        return getPoint();
    }

    public Point getPoint() {
        return new com.hp.dpp.icr.ChangeStroke.Point(si_.getX(), si_.getY());
    }

    public boolean hasNext() {
        return si_.hasNext();
    }


}
