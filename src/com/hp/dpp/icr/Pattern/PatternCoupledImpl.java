package com.hp.dpp.icr.Pattern;

import com.hp.dpp.sdk.PenStrokes;
import com.hp.dpp.sdk.PenStroke;
import com.hp.dpp.icr.ChangeStroke.ChangeStroke;
import com.hp.dpp.icr.ChangeStroke.ChangeStrokeCoupled;
import com.hp.dpp.icr.ChangeStroke.Point;

import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Date: Oct 5, 2003
 * Time: 10:54:34 PM
 */
public class PatternCoupledImpl extends PatternImpl implements Pattern {

    public PatternCoupledImpl(int digit, PenStrokes penStrokes) {
        super(digit, penStrokes);
    }

    public PatternCoupledImpl(PenStrokes penStrokes) {
        super(penStrokes);
    }

    public PatternCoupledImpl(int digit, int[][][] strokes) {
        super(digit, strokes);
    }

    public PatternCoupledImpl(int[][][] differences) {
        super(differences);
    }

    public PatternCoupledImpl(int digit, int[] x, int[] y) {
        super(digit, x, y);
    }

    protected ChangeStroke createChangeStroke(int[][] differences) {
        return new ChangeStrokeCoupled(differences);
    }

    protected ChangeStroke createChangeStroke(int[] x, int[] y, Point origin) {
        return new ChangeStrokeCoupled(x, y, dimension_, origin);
    }

    protected ChangeStroke createChangeStroke(PenStroke penStroke, Point origin) {
        return new ChangeStrokeCoupled(penStroke, dimension_, origin);
    }

    protected static Pattern createPattern(int digit, int[][][] list) {
        return new PatternCoupledImpl(digit, list);
    }

    protected static Pattern createPattern(int[][][] list) {
        return new PatternCoupledImpl(list);
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


