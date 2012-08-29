package com.hp.dpp.icr.Comparator;

import com.hp.dpp.icr.ChangeStroke.*;

import java.util.Vector;


/**
 * Date: Nov 9, 2003
 * Time: 9:08:21 PM
 */
public class ComparatorGradeOffsetImpl extends ComparatorGradeImpl implements Comparator {

    protected int[] compareGrade(ChangeStroke cs1, ChangeStroke cs2, int prune) {
        int[] changes;
        Point p1,p2,pDiff;
        changes = super.compareGrade(cs1, cs2, prune);
        p1 = cs1.getOffset();
        p2 = cs2.getOffset();
        if ((p1 != null) && (p2 != null)) {
            pDiff = p1.subs(p2);
            changes[0] += Math.abs(pDiff.getX());
            changes[1] += Math.abs(pDiff.getY());
        }
        return changes;
    }

    public ChangeStrokeDiff compareVec(ChangeStroke cs1, ChangeStroke cs2, int prune) {
        Vector[] vec;

        vec = compareGradeVec(cs1, cs2, prune);
        return new ChangeStrokeDiffImpl(vec);
    }


    protected Vector[] compareGradeVec(ChangeStroke cs1, ChangeStroke cs2, int prune) {
        Vector[] vec;

        Point p1,p2,pDiff;
        vec = super.compareGradeVec(cs1, cs2, prune);
        p1 = cs1.getOffset();
        p2 = cs2.getOffset();
        if ((p1 != null) && (p2 != null)) {
            pDiff = p1.subs(p2);
            vec[0].add(0, new Integer((int) Math.abs(pDiff.getX())));
            vec[1].add(0, new Integer((int) Math.abs(pDiff.getY())));
        }
        return vec;
    }

 /*
    protected int similarity(int[] r,
                             int i,
                             int[] s,
                             int j,
                             int prune,
                             Vector vec) {

        Vector rVec,sVec,rsVec,minVec;
        int diff = 0;
        int diffS,diffR,diffRS,diffMin;

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
            prune = prune - diff;
            if ((diff <= ChangeStrokeImpl.getZeroFactor()) && (r[i] * s[j] >= 0)) {
                diff += similarity(r, i + 1, s, j + 1, prune, vec);
            } else {
                minVec = new Vector();
                diffMin = similarity(r, i + 1, s, j + 1, prune, minVec);
                prune = Math.min(prune, diffMin + 1);

                sVec = new Vector();
                diffS = similarity(r, i, s, j + 1, prune, sVec);
                prune = Math.min(prune, diffS + 1);

                rVec = new Vector();
                diffR = similarity(r, i + 1, s, j, prune, rVec);

                if (diffS < diffR) {
                    diffRS = diffS;
                    rsVec = sVec;
                } else {
                    diffRS = diffR;
                    rsVec = rVec;
                }
                if (diffRS < diffMin) {
                    diffMin = diffRS;
                    minVec = rsVec;
                }
                diff += diffMin;
                vec.addAll(minVec);
            }
        }
        return diff;
    }
   */
}
