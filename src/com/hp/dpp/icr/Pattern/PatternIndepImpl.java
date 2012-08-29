package com.hp.dpp.icr.Pattern;

import com.hp.dpp.sdk.PenStrokes;
import com.hp.dpp.sdk.PenStroke;
import com.hp.dpp.icr.ChangeStroke.ChangeStroke;
import com.hp.dpp.icr.ChangeStroke.ChangeStrokeIndep;
import com.hp.dpp.icr.ChangeStroke.Point;

/**
 * Date: Oct 1, 2003
 * Time: 10:02:58 PM
 */
public class PatternIndepImpl extends PatternImpl implements Pattern {

    public PatternIndepImpl(int digit, PenStrokes penStrokes) {
        super(digit, penStrokes);
    }

    public PatternIndepImpl(PenStrokes penStrokes) {
        super(penStrokes);
    }

    public PatternIndepImpl(int digit, int[][][] strokes) {
        super(digit, strokes);
    }

    public PatternIndepImpl(int[][][] differences) {
        super(differences);
    }

    public PatternIndepImpl(int digit, int[] x, int[] y) {
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
        return new PatternIndepImpl(digit, list);
    }

    protected static Pattern createPattern(int[][][] list) {
        return new PatternIndepImpl(list);
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
