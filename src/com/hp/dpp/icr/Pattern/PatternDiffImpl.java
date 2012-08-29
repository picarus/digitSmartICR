package com.hp.dpp.icr.Pattern;

import com.hp.dpp.icr.ChangeStroke.ChangeStrokeDiff;
import com.hp.dpp.icr.ChangeStroke.ChangeStroke;
import com.hp.dpp.icr.ChangeStroke.Point;
import com.hp.dpp.icr.Comparator.Compare;
import com.hp.dpp.sdk.PenStroke;

import java.util.Vector;

/**
 * Date: Nov 14, 2003
 * Time: 8:56:27 PM
 */
public class PatternDiffImpl implements PatternDiff {

    protected Vector changeStroke_;

    PatternDiffImpl() {
        changeStroke_ = new Vector();
    }

    void addChangeStrokeDiff(ChangeStrokeDiff csDiff) {
        changeStroke_.add(csDiff);
    }

    public int similarity(PatternDiff pattern, int prune) {
        int[][] diff = lengthCompare(pattern, prune);
        return similarity(diff);
    }

    protected static int similarity(int diff[][]) {
        int[] sim;
        int simil;
        sim = new int[2];

        // we know all the arrays will have the same length (2) : one for x, one for y
        for (int j = 0; j < diff[0].length; j++) {
            sim[j] = 0;
        }

        for (int i = 0; i < diff.length; i++) {
            for (int j = 0; j < diff[i].length; j++) {
                sim[j] += diff[i][j];
            }
        }

        simil = 0;
        for (int j = 0; j < diff[0].length; j++) {
            simil += sim[j];
        }

        return simil;
    }

    public int[][] lengthCompare(PatternDiff pattern, int prune) {
        // the first thing to match is the number of strokes
        int len = getNumberOfStrokes();
        int[][] comp;
        ChangeStrokeDiff cs;
        int i;

        if (len == pattern.getNumberOfStrokes()) {
            comp = new int[len][];
            i = 0;
            while (i < len) {
                cs = getChangeStrokeDiff(i);
                // Trace
                //System.out.print("CS"+prune);
                comp[i] = cs.lengthCompare(pattern.getChangeStrokeDiff(i), prune);
                i++;
            }
        } else {
            comp = new int[][]{{Compare.MAX_DIFF}};
        }
        return comp;
    }

    public ChangeStrokeDiff getChangeStrokeDiff(int index) {
        return (ChangeStrokeDiff) changeStroke_.get(index);
    }

    public int getNumberOfStrokes() {
        return changeStroke_.size();
    }
}
