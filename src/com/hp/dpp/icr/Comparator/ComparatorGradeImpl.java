package com.hp.dpp.icr.Comparator;

import com.hp.dpp.icr.ChangeStroke.ChangeStrokeImpl;
import com.hp.dpp.icr.ChangeStroke.ChangeStroke;
import com.hp.dpp.icr.ChangeStroke.ChangeStrokeDiff;
import com.hp.dpp.icr.ChangeStroke.ChangeStrokeDiffImpl;

import java.util.Vector;

/**
 * Date: Nov 8, 2003
 * Time: 8:55:32 PM
 */
public class ComparatorGradeImpl extends ComparatorImpl implements Comparator {

     public int[] lengthCompare(ChangeStroke cs1, ChangeStroke cs2, int prune) {
        int[] length;
        length = compareGrade(cs1, cs2, prune);
        return length;
    }

    protected int[] compareGrade(ChangeStroke cs1, ChangeStroke cs2, int prune) {
        Vector[] vec= new Vector[]{new Vector(),new Vector()};
        int[] changes = new int[2];

        changes[0] = similarity(cs1.getChangeX(), 0, cs2.getChangeX(), 0, prune, vec[0]);
        changes[1] = similarity(cs1.getChangeY(), 0, cs2.getChangeY(), 0, prune, vec[1]);

        return changes;
    }

    protected Vector[] compareGradeVec(ChangeStroke cs1, ChangeStroke cs2, int prune){
        Vector[] vec= new Vector[]{new Vector(),new Vector()};

        similarity(cs1.getChangeX(), 0, cs2.getChangeX(), 0, prune, vec[0]);
        similarity(cs1.getChangeY(), 0, cs2.getChangeY(), 0, prune, vec[1]);

        return vec;
    }


    public ChangeStrokeDiff compareVec(ChangeStroke cs1, ChangeStroke cs2, int prune){
        Vector[] vec= new Vector[]{new Vector(),new Vector()};

        similarity(cs1.getChangeX(), 0, cs2.getChangeX(), 0, prune, vec[0]);
        similarity(cs1.getChangeY(), 0, cs2.getChangeY(), 0, prune, vec[1]);

        return new ChangeStrokeDiffImpl(vec);
    }

    public int similarity(Vector change1, int i, Vector change2, int j, int prune, Vector vec) {

        int[][] change = new int[2][];
        int maxlen;

        change[0] = convert(change1);
        change[1] = convert(change2);

        maxlen = Math.max(change[0].length, change[1].length) * 3 * ChangeStrokeImpl.getFactor();

        prune = Math.min(maxlen, prune);
        // Trace
        //updatePruneMonitor(prune,maxlen);

        return similarity(change[0], i, change[1], j, prune, vec);
    }

    protected int similarity(int[] r,
                             int i,
                             int[] s,
                             int j,
                             int prune,
                             Vector vec) {

        Vector rVec,sVec;
        int diff = 0;
        int lenS;
        int lenR;

        boolean blenR = (i >= r.length);
        boolean blenS = (j >= s.length);

        if (blenR && blenS) {
            diff = 0;
        } else if (blenR || blenS) {
            if (blenR) {
                diff = moveAndSumElems(s, j, vec);
            } else if (blenS) {
                diff = moveAndSumElems(r, i, vec);
            }
            vec.add(new Integer(diff));
        } else if (prune < 0) {
            diff = ENDPRUNENUM;
        } else {
            diff = Math.abs(r[i] - s[j]);
            vec.add(new Integer(diff));
            prune -= diff;
            if ((diff <= ChangeStrokeImpl.getZeroFactor()) && (r[i] * s[j] >= 0)) {
                diff += similarity(r, i + 1, s, j + 1, prune, vec);
            } else {
                sVec = new Vector();
                lenS = similarity(r, i, s, j + 1, prune, sVec);
                prune = Math.min(prune, lenS + 1);
                rVec = new Vector();
                lenR = similarity(r, i + 1, s, j, prune, rVec);
                if (lenS < lenR) {
                    diff += lenS;
                    vec.addAll(sVec);
                } else {
                    diff += lenR;
                    vec.addAll(rVec);
                }
            }
        }
        return diff;
    }

    protected static int moveAndSumElems(int[] array, int index, Vector vec) {
        int sum = 0;
        int elem;
        int len = array.length;

        while (index < len) {
            elem = Math.abs(array[index]);
            sum += elem;
            vec.add(new Integer(elem));
            index++;
        }
        return sum;
    }

}
