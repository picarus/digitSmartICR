package com.hp.dpp.icr.Pattern;

import com.hp.dpp.icr.ChangeStroke.ChangeStroke;
import com.hp.dpp.icr.ChangeStroke.Point;

import java.io.Serializable;
import java.awt.*;

/**
 * Date: Oct 5, 2003
 * Time: 10:54:04 PM
 */
public interface Pattern extends PatternDiff,Serializable {

    public ChangeStroke getChangeStroke();

    public PatternDiff compareVec(Pattern pattern, int prune);

    public String[][] compare(Pattern pattern, int prune);

    public ChangeStroke getFirst();

    public Point getDimension();

    public String toString();

    public ChangeStroke getChangeStroke(int i);

    public int getDigit();

    public void setDigit(int digit, int similarity);

    public void setDigit(int digit);

    public int guessDigit(Pattern[] patternRef);

    public Point[] length();

    public String lengthToString();

    public boolean equals(Object pattern);

    public Shape asShape();

    public int setSimilarity(int similarity);

    public int getSimilarity();

    public boolean getIsCorrect();

    public void setIsCorrect(boolean isCorrect);

}
