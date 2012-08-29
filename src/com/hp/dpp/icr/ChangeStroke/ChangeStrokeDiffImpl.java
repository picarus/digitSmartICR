package com.hp.dpp.icr.ChangeStroke;

import com.hp.dpp.icr.Comparator.Comparator;
import com.hp.dpp.icr.Comparator.ComparatorGradeImpl;

import java.util.Vector;

/**
 * Date: Nov 14, 2003
 * Time: 9:05:56 PM
 */
public class ChangeStrokeDiffImpl implements ChangeStrokeDiff {

    protected Vector vectorX;
    protected Vector vectorY;
    protected Comparator comparator_;

    protected ChangeStrokeDiffImpl() {
        // the init is done at the class that inherits
    }

    public ChangeStrokeDiffImpl(Vector[] vec) {
        vectorX = vec[0];
        vectorY = vec[1];
    }

    public ChangeStrokeDiffImpl(Vector vecX, Vector vecY) {
        vectorX = vecX;
        vectorY = vecY;
    }

    public ChangeStrokeDiffImpl(int[][] differences) {
        vectorX = new Vector(differences[0].length);
        vectorY = new Vector(differences[1].length);
        dumpValues(vectorX, differences[0]);
        dumpValues(vectorY, differences[1]);
    }

    protected Comparator createComparator() {
        return new ComparatorGradeImpl();
    }

    protected static void dumpValues(Vector v, int[] values) {
        int i = 0;
        while (i < values.length) {
            v.add(i, new Integer(values[i]));
            i++;
        }
    }

    public Vector getChangeX() {
        return vectorX;
    }

    public Vector getChangeY() {
        return vectorY;
    }

    public int[] lengthCompare(ChangeStrokeDiff cs, int prune) {
        return comparator_.lengthCompare(this, cs, prune);
    }

}
