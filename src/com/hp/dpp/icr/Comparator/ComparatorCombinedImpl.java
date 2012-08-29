package com.hp.dpp.icr.Comparator;

import com.hp.dpp.icr.ChangeStroke.ChangeStroke;
import com.hp.dpp.icr.ChangeStroke.ChangeStrokeCombined;

/**
 * Date: Nov 8, 2003
 * Time: 9:38:53 PM
 */
public class ComparatorCombinedImpl extends ComparatorImpl implements Comparator {

    public String[] compare(ChangeStroke cs1, ChangeStroke cs2, int prune) {
         String[] changes = new String[1];
         ChangeStrokeCombined csc1 = (ChangeStrokeCombined) cs1;
         ChangeStrokeCombined csc2 = (ChangeStrokeCombined) cs2;

         changes[0] = similarity(csc1.getChangeXY(), 0, csc2.getChangeXY(), 0, prune);

         return changes;
     }


}
