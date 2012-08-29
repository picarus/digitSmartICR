package com.hp.dpp.icr.Pattern;

import com.hp.dpp.icr.ChangeStroke.ChangeStrokeDiff;

/**
 * Date: Nov 12, 2003
 * Time: 10:28:13 PM
 */
public interface PatternDiff {

    public ChangeStrokeDiff getChangeStrokeDiff(int index);

    public int[][] lengthCompare(PatternDiff pattern, int prune);

    public int getNumberOfStrokes();

    public int similarity(PatternDiff pattern, int prune);

}
