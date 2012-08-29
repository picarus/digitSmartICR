package com.hp.dpp.icr.Pattern;

import com.hp.dpp.icr.ChangeStroke.ChangeStroke;
import com.hp.dpp.icr.ChangeStroke.Point;
import com.hp.dpp.icr.ChangeStroke.ChangeStrokeDiff;
import com.hp.dpp.icr.Comparator.Compare;
import com.hp.dpp.sdk.PenStrokes;
import com.hp.dpp.sdk.PenStroke;
import com.hp.dpp.sdk.Bounds;

import java.util.Vector;
import java.util.Iterator;
import java.util.Map;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;

/**
 * Date: Oct 5, 2003
 * Time: 11:00:03 PM
 */
public abstract class PatternImpl extends PatternDiffImpl implements Pattern {

    protected int digit_;
    protected Point dimension_;
    protected int similarity_ = -1; // default value
    protected boolean isCorrect_ = false;

    public PatternImpl(int digit, PenStrokes penStrokes) {
        digit_ = digit;
        similarity_ = -1; // the pattern was created with the distance
        changeStroke_ = new Vector();
        dimension_ = getDimension(penStrokes);
        init(penStrokes);
    }

    public PatternImpl(PenStrokes penStrokes) {
        digit_ = -1; // the digit is unknown
        similarity_ = -1; // initial value
        changeStroke_ = new Vector();
        dimension_ = getDimension(penStrokes);
        init(penStrokes);
    }

    public PatternImpl(int digit, int[][][] strokes) {
        digit_ = digit;
        similarity_ = -1; // initial value
        changeStroke_ = new Vector();
        dimension_ = getDimension(strokes);
        for (int i = 0; i < strokes.length; i++) {
            init(strokes[i][0], strokes[i][1]);
        }
    }

    public PatternImpl(int[][][] differences) {
        changeStroke_ = new Vector();
        similarity_ = -1; // initial value
        digit_ = -1; // the digit is unknown
        init(differences);
    }

    public PatternImpl(int digit, int[] x, int[] y) {
        digit_ = digit;
        similarity_ = -1; // initial value
        changeStroke_ = new Vector();
        init(x, y);
    }

    public int setSimilarity(int similarity) {
        similarity_ = similarity;
        return similarity;
    }

    public int getSimilarity() {
        return similarity_;
    }

    public boolean getIsCorrect() {
        return isCorrect_;
    }

    public ChangeStroke getChangeStroke(int i){
        return (ChangeStroke) getChangeStrokeDiff(i);
    }

    public void setIsCorrect(boolean isCorrect) {
        isCorrect_ = isCorrect;
    }

    Point getDimension(PenStrokes penStrokes) {
        Bounds bounds = penStrokes.getBounds();
        return new Point(bounds.getWidth(), bounds.getHeight());
    }

    Point getDimension(int[][][] diff) {

        int value;
        int[] dimMin = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE};
        int[] dimMax = new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE};

        for (int i = 0; i < diff.length; i++) {
            for (int j = 0; j < diff[i].length; j++) {
                for (int k = 0; k < diff[i][j].length; k++) {
                    value = diff[i][j][k];
                    if (dimMin[j] > value)
                        dimMin[j] = value;
                    if (dimMax[j] < value)
                        dimMax[j] = value;
                }
            }
        }

        return new Point(dimMax[0] - dimMin[0], dimMax[1] - dimMin[1]);
    }


    protected abstract ChangeStroke createChangeStroke(int[][] differences);

    protected abstract ChangeStroke createChangeStroke(int[] x, int[] y, Point origin);

    protected abstract ChangeStroke createChangeStroke(PenStroke penStroke, Point origin);

    protected void init(int[][][] differences) {
        for (int i = 0; i < differences.length; i++) {
            changeStroke_.add(createChangeStroke(differences[i]));
        }
    }

    protected void init(int[] x, int[] y) {
        changeStroke_.add(createChangeStroke(x, y, new Point()));
    }

    protected void init(PenStrokes penStrokes) {
        Iterator it = penStrokes.getIterator();
        PenStroke penStroke = null;
        Point origin = null;
        boolean isFirst = true;
        ChangeStroke cs;
        while (it.hasNext()) {
            penStroke = (PenStroke) it.next();
            cs = createChangeStroke(penStroke, origin);
            if (isFirst) {
                origin = cs.getOrigin();
                isFirst = false;
            }
            changeStroke_.add(cs);
        }
    }

    public ChangeStroke getChangeStroke() {
        return getFirst();
    }


    public Point getDimension() {
        return dimension_;
    }



    public boolean equals(Object object) {
        boolean isEqual;
        int i;
        int len;
        ChangeStrokeDiff cscur,csother;
        Pattern pattern = (Pattern) object;
        isEqual = pattern.getClass().equals(getClass());
        if (isEqual) {
            isEqual = digit_ == pattern.getDigit();
        }

        //*******************************************
        // dimension does not need to be equal
        //*******************************************

        if (isEqual) {
            // compare vector
            i = 0;
            len = getNumberOfStrokes();
            isEqual = (len == pattern.getNumberOfStrokes());
            while (isEqual && (i < len)) {
                cscur = getChangeStroke(i);
                csother = pattern.getChangeStroke(i);
                isEqual = cscur.equals(csother);
                i++;
            }
        }
        return isEqual;
    }

    public int similarityGrade(Pattern pattern, int prune){
        int[][] diff = lengthCompare(pattern, prune);
        return similarityGrade(diff);
    }

    protected static int similarityGrade(int diff[][]) {
        int[] sim;
        int simil;
        sim = new int[diff.length];

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



    public String[][] compare(Pattern pattern, int prune) {
        // the first thing to match is the number of strokes
        int len = getNumberOfStrokes();
        String[][] comp;
        ChangeStroke cs;
        int i;

        if (len == pattern.getNumberOfStrokes()) {
            comp = new String[len][];
            i = 0;
            while (i < len) {
                cs =(ChangeStroke) getChangeStroke(i);
                comp[i] = cs.compare(pattern.getChangeStroke(i), prune);
                i++;
            }
        } else {
            comp = new String[][]{{"-1-"}};
        }
        return comp;
    }



    public PatternDiff compareVec(Pattern pattern, int prune) {
        PatternDiff pd=new PatternDiffImpl();

        return null;
        // todo
    }

    public int[][] compareGrade(Pattern pattern, int prune) {
         // the first thing to match is the number of strokes
         int len = getNumberOfStrokes();
         int[][] comp;
         ChangeStroke cs;
         int i;

         if (len == pattern.getNumberOfStrokes()) {
             comp = new int[len][];
             i = 0;
             while (i < len) {
                 cs = (ChangeStroke) getChangeStroke(i);
                 // todo: recoger el int
                 comp[i]=cs.lengthCompare(pattern.getChangeStroke(i), prune);
                 i++;
             }
         } else {
             comp = new int[][]{{Compare.MAX_DIFF}};
             // default value when no comparison is done
         }
         return comp;
     }

    public ChangeStroke getFirst() {
        return (ChangeStroke) changeStroke_.get(0);
    }

    public String toString() {
        return "[" + digit_ + "," + changeStroke_.toString() + "]";
    }

    public int getDigit() {
        return digit_;
    }

    public void setDigit(int digit, int similarity) {
        setDigit(digit);
        setSimilarity(similarity);
    }

    public void setDigit(int digit) {
        digit_ = digit;
    }

    public Point[] length() {
        int len = getNumberOfStrokes();
        Point[] pointAr = new Point[len];
        ChangeStroke cs;
        int i = 0;
        while (i < len) {
            cs = (ChangeStroke) changeStroke_.get(i);
            pointAr[i] = cs.length();
            i++;
        }

        return pointAr;
    }

    public String lengthToString() {
        Point[] pointAr = this.length();
        String str = "";
        int i = 0;
        while (i < pointAr.length) {
            str += "[" + pointAr[i].getX() + "," + pointAr[i].getY() + "]";
            i++;
        }
        return str;
    }

    public boolean isDigit(com.hp.dpp.icr.Digits.Digit digit) {
        return digit.contains(this);
    }

    public static void guess(Map values, Pattern[] patternRef) {
        Pattern pattern;
        String value;
        Iterator it = values.keySet().iterator();
        while (it.hasNext()) {
            value = (String) it.next();
            pattern = (Pattern) values.get(value);
            pattern.guessDigit(patternRef); // the more similar digit is assigned inside the function
        }
    }

    public int guessDigit(Pattern[] patternRef) {
        int digit = -1;
        int similarity = Integer.MAX_VALUE;
        int distance;
        int i = 0;
        boolean found = false;

        while ((i < patternRef.length) && (!found)) {
            distance = similarity(patternRef[i], similarity);
            if (distance < similarity) {
                similarity = distance;
                digit = patternRef[i].getDigit();
                found = (similarity == 0);
            }
            i++;
        }
        setDigit(digit, similarity);
        return digit;
    }

    public Vector bestGuessDigit(Pattern[] patternRef) {
        int digit = -1;
        int similarity = Integer.MAX_VALUE;
        int distance;
        int i = 0;
        Vector bg = new Vector();

        while (i < patternRef.length) {
            distance = similarity(patternRef[i], similarity);
            digit = patternRef[i].getDigit();
            bg.add(new com.hp.dpp.icr.Digits.DigitSimil(digit, distance));
            i++;
        }
        return bg;
    }

    public Shape asShape() {
        GeneralPath gp;

        int i = 0;
        int len = 0;
        Shape sh,tsh;
        ChangeStroke cs;
        AffineTransform at;
        Point offset;

        gp = new GeneralPath();

        len = changeStroke_.size();

        while (i < len) {
            cs = (ChangeStroke) changeStroke_.get(i);
            sh = cs.asShape();
            offset = cs.getOffset();
            at = new AffineTransform(1, 0, 0, 1, offset.getX(), offset.getY());
            tsh = at.createTransformedShape(sh);
            gp.append(tsh, false);
            i++;
        }
        return gp;
    }

}
