package com.hp.dpp.icr.Pattern;

import com.hp.dpp.sdk.PenStrokes;
import com.hp.dpp.sdk.PenStroke;
import com.hp.dpp.icr.ChangeStroke.ChangeStroke;
import com.hp.dpp.icr.ChangeStroke.ChangeStrokeIndep;
import com.hp.dpp.icr.ChangeStroke.Point;

/**
 * Date: Nov 1, 2003
 * Time: 9:03:44 PM
 */
public class PatternIndepSizeImpl extends PatternIndepImpl implements Pattern {
    public PatternIndepSizeImpl(int digit, PenStrokes penStrokes) {
        super(digit, penStrokes);
    }

    public PatternIndepSizeImpl(PenStrokes penStrokes) {
        super(penStrokes);
    }

    public PatternIndepSizeImpl(int digit, int[][][] strokes) {
        super(digit, strokes);
    }

    public PatternIndepSizeImpl(int[][][] differences) {
        super(differences);
    }

    public PatternIndepSizeImpl(int digit, int[] x, int[] y) {
        super(digit, x, y);
    }

    protected ChangeStroke createChangeStroke(int[][] differences) {
        return new ChangeStrokeIndep(differences);
    }

    protected ChangeStroke createChangeStroke(int[] x, int[] y, Point origin) {
        return new ChangeStrokeIndep(x, y, dimension_, origin);
    }

    protected ChangeStroke createChangeStroke(PenStroke penStroke, Point origin) {
        return new ChangeStrokeIndep(penStroke, dimension_, origin);
    }

    protected static Pattern createPattern(int digit, int[][][] list) {
        return new PatternIndepSizeImpl(digit, list);
    }

    protected static Pattern createPattern(int[][][] list) {
        return new PatternIndepSizeImpl(list);
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
