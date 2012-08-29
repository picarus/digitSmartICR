package com.hp.dpp.icr.Pattern;

import com.hp.dpp.sdk.PenStrokes;
import com.hp.dpp.sdk.PenStroke;
import com.hp.dpp.icr.ChangeStroke.ChangeStroke;
import com.hp.dpp.icr.ChangeStroke.ChangeStrokeCombined;
import com.hp.dpp.icr.ChangeStroke.ChangeStrokeCoupled;
import com.hp.dpp.icr.ChangeStroke.Point;

import java.util.Vector;

/**
 * Date: Oct 16, 2003
 * Time: 8:09:11 PM
 */
public class PatternCombinedImpl extends PatternCoupledImpl implements Pattern {

    Vector vectorXY;

    public PatternCombinedImpl(int digit, PenStrokes penStrokes) {
        super(digit, penStrokes);
        combine();
    }

    public PatternCombinedImpl(PenStrokes penStrokes) {
        super(penStrokes);
        combine();
    }

    public PatternCombinedImpl(int digit, int[][][] strokes) {
        super(digit, strokes);
        combine();
    }

    public PatternCombinedImpl(int[][][] differences) {
        super(differences);
        combine();
    }

    public PatternCombinedImpl(int digit, int[] x, int[] y) {
        super(digit, x, y);
        combine();
    }

    Vector getVectorXY() {
        return vectorXY;
    }

    public boolean equals(Object object) {
        PatternCombinedImpl pattern;
        boolean isEqual = super.equals(object);
        if (isEqual) {
            pattern = (PatternCombinedImpl) object;
            isEqual = vectorXY.equals(pattern.getVectorXY());
        }
        return isEqual;
    }

    protected ChangeStroke createChangeStroke(int[][] differences) {
        return new ChangeStrokeCombined(differences);
    }

    protected ChangeStroke createChangeStroke(int[] x, int[] y, Point origin) {
        return new ChangeStrokeCombined(x, y, dimension_, origin);
    }

    protected ChangeStroke createChangeStroke(PenStroke penStroke, Point origin) {
        return new ChangeStrokeCombined(penStroke, dimension_, origin);
    }

    protected void combine() {
        int i = 0;
        ChangeStrokeCombined csc;
        ChangeStrokeCoupled csCoupled;
        vectorXY = new Vector();

        while (i < changeStroke_.size()) {
            csCoupled = (ChangeStrokeCoupled) changeStroke_.get(i);
            csc = new ChangeStrokeCombined(csCoupled);
            vectorXY.add(csc);
            i++;
        }
    }

    protected static Pattern createPattern(int digit, int[][][] list) {
        return new PatternCombinedImpl(digit, list);
    }

    protected static Pattern createPattern(int[][][] list) {
        return new PatternCombinedImpl(list);
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

    public static Pattern[] runErrors(int[][][][] all, int[] errors) {
        int errorLen = errors.length;
        int index;
        Pattern[] pattern = new Pattern[errorLen];
        for (int i = 0; i < errorLen; i++) {
            index = errors[i];
            pattern[i] = createPattern(index, all[index]);
            System.out.println(pattern[i].getDigit() + "-->" + pattern[i].toString());
        }
        return pattern;
    }
}
