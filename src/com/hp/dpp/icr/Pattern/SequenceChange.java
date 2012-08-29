package com.hp.dpp.icr.Pattern;

import com.hp.dpp.icr.ChangeStroke.Point;

import java.io.Serializable;

/**
 * Date: Oct 5, 2003
 * Time: 11:45:01 AM
 */
public interface SequenceChange {

    void init();

    Point getFirstPoint();

    Point getNextPoint();

    boolean hasNext();

}
