package com.hp.dpp.icr.Comparator;

import com.hp.dpp.icr.ChangeStroke.ChangeStroke;
import com.hp.dpp.icr.ChangeStroke.ChangeStrokeDiff;

import java.io.Serializable;

/**
 * Date: Nov 8, 2003
 * Time: 8:55:18 PM
 */
public interface Comparator extends Serializable {

     public String[] compare(ChangeStrokeDiff cs1, ChangeStrokeDiff cs2, int prune);

     public int[] lengthCompare(ChangeStrokeDiff cs1, ChangeStrokeDiff cs2, int prune);

     public ChangeStrokeDiff compareVec(ChangeStroke cs1, ChangeStroke cs2, int prune);

}
