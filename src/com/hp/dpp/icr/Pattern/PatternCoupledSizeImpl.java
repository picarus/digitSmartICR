package com.hp.dpp.icr.Pattern;

import com.hp.dpp.sdk.PenStrokes;
import com.hp.dpp.sdk.PenStroke;
import com.hp.dpp.icr.ChangeStroke.ChangeStroke;
import com.hp.dpp.icr.ChangeStroke.ChangeStrokeCoupledSize;
import com.hp.dpp.icr.ChangeStroke.Point;

/**
 * Date: Oct 26, 2003
 * Time: 6:00:05 PM
 */
public class PatternCoupledSizeImpl extends PatternImpl implements Pattern {

    public PatternCoupledSizeImpl(int digit, PenStrokes penStrokes) {
        super(digit, penStrokes);
    }

    public PatternCoupledSizeImpl(PenStrokes penStrokes) {
        super(penStrokes);
    }

    public PatternCoupledSizeImpl(int digit, int[][][] strokes) {
        super(digit, strokes);
    }

    public PatternCoupledSizeImpl(int[][][] differences) {
        super(differences);
    }

    public PatternCoupledSizeImpl(int digit, int[] x, int[] y) {
        super(digit, x, y);
    }

    protected ChangeStroke createChangeStroke(int[][] differences) {
        return new ChangeStrokeCoupledSize(differences);
    }

    protected ChangeStroke createChangeStroke(int[] x, int[] y, Point origin) {
        return new ChangeStrokeCoupledSize(x, y, dimension_, origin);
    }

    protected ChangeStroke createChangeStroke(PenStroke penStroke, Point origin) {
        return new ChangeStrokeCoupledSize(penStroke, dimension_, origin);
    }

    protected static Pattern createPattern(int digit, int[][][] list) {
        return new PatternCoupledSizeImpl(digit, list);
    }

    protected static Pattern createPattern(int[][][] list) {
        return new PatternCoupledSizeImpl(list);
    }

    public static Pattern[] run(int[] digits, int[][][][] list) {
        int len = list.length;
        Pattern[] pattern = new Pattern[len];
        for (int i = 0; i < len; i++) {
            pattern[i] = createPattern(digits[i], list[i]);
            System.out.println(pattern[i].getDigit() + "-->" + pattern[i].toString());
        }
        return pattern;
    }

    public static Pattern[] transform(int[][][][] list) {
        int len = list.length;
        Pattern[] pattern = new Pattern[len];
        for (int i = 0; i < len; i++) {
            pattern[i] = createPattern(list[i]);
            System.out.println(pattern[i].getDigit() + "-->" + pattern[i].toString());
        }
        return pattern;
    }

    public static Pattern[] runErrors(int[] values, int[][][][] all, int[][][][]ref, int[] errors) {
        int errorLen = errors.length;
        int index;
        Pattern[] pattern = new Pattern[errorLen];
        Pattern[] patternRef = new Pattern[errorLen];
        int diff[][];
        for (int i = 0; i < errorLen; i++) {
            index = errors[i];
            pattern[i] = createPattern(values[index], all[index]);
            patternRef[i] = createPattern(ref[index]);
            System.out.println(index + "-->" + pattern[i].toString());
            System.out.println(index + "-->" + patternRef[i].toString());
            diff=pattern[i].lengthCompare(patternRef[i],Integer.MAX_VALUE);
        }
        return pattern;
    }

}
