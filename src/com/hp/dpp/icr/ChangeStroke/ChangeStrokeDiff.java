package com.hp.dpp.icr.ChangeStroke;

import java.util.Vector;

/**
 * Date: Nov 14, 2003
 * Time: 8:58:33 PM
 */
public interface ChangeStrokeDiff {

    Vector getChangeX();

    Vector getChangeY();

    int[] lengthCompare(ChangeStrokeDiff cs, int prune);

}
