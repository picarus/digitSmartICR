package com.hp.dpp.icr.ChangeStroke;

import com.hp.dpp.icr.ChangeStroke.Point;

import java.util.Vector;
import java.awt.*;
import java.io.Serializable;

/**
 * Date: Oct 5, 2003
 * Time: 11:23:17 PM
 */
public interface ChangeStroke extends ChangeStrokeDiff,Serializable {
    // we assign values that are prime numbers so that if we multiply it is not possible to make a mistake
    static final int POS = 1;
    static final int NEG = -1;
    static final int X_TO_LEFT = NEG;
    static final int X_TO_RIGHT = POS;
    static final int NOWHERE = 0;
    static final int Y_TO_TOP = NEG;
    static final int Y_TO_BOTTOM = POS;

    static final int X_TO_LEFT_COMBINE = X_TO_LEFT * 2;
    static final int X_TO_RIGHT_COMBINE = X_TO_RIGHT * 3;
    static final int Y_TO_TOP_COMBINE = Y_TO_TOP * 5;
    static final int Y_TO_BOTTOM_COMBINE = Y_TO_BOTTOM * 7;

    String toString();

    String[] compare(ChangeStroke cs, int prune);



    Point length();

    Shape asShape();

    Point getOrigin();

    Point getOffset();

    boolean equals(Object object);

}
