package com.hp.dpp.icr.ChangeStroke;

import com.hp.dpp.sdk.PenStroke;
import com.hp.dpp.icr.Comparator.Comparator;
import com.hp.dpp.icr.Comparator.ComparatorCombinedImpl;
import com.hp.dpp.icr.ChangeStroke.ChangeStroke;
import com.hp.dpp.icr.ChangeStroke.Point;

import java.util.Vector;

/**
 * Date: Oct 16, 2003
 * Time: 8:10:52 PM
 */
public class ChangeStrokeCombined extends ChangeStrokeCoupled implements ChangeStroke {

    protected Vector vectorXY;

    public ChangeStrokeCombined(int[] valueX, int[] valueY, Point minChange, Point origin) {
        super(valueX, valueY, minChange, origin);
        combine();
    }

    public ChangeStrokeCombined(int[][] differences) {
        super(differences);
        combine();
    }

    public ChangeStrokeCombined(PenStroke penStroke, Point minChange, Point origin) {
        super(penStroke, minChange, origin);
        combine();
    }

    public ChangeStrokeCombined(ChangeStrokeCoupled csCoupled) {
        super(csCoupled.vectorX, csCoupled.vectorY, new Point(csCoupled.getMinChangeX(), csCoupled.getMinChangeY()));
        combine();
    }

    protected Comparator createComparator(){
        return new ComparatorCombinedImpl();
    }

    public boolean equals(Object obj) {
        ChangeStrokeCombined csc;
        boolean isEqual = super.equals(obj);
        if (isEqual) {
            csc = (ChangeStrokeCombined) obj;
            isEqual = csc.getChangeXY().equals(getChangeXY());
        }
        return isEqual;
    }

    protected int encode(int x, int y) {
        // encode x
        switch (x) {
            case ChangeStroke.NOWHERE:
                x = 1;
                break;
            case ChangeStroke.X_TO_LEFT:
                x = ChangeStroke.X_TO_LEFT_COMBINE;
                break;
            case ChangeStroke.X_TO_RIGHT:
                x = ChangeStroke.X_TO_RIGHT_COMBINE;
                break;
        }
        // encode y
        switch (y) {
            case ChangeStroke.NOWHERE:
                y = 1;
                break;
            case ChangeStroke.Y_TO_BOTTOM:
                y = ChangeStroke.Y_TO_BOTTOM_COMBINE;
                break;
            case ChangeStroke.Y_TO_TOP:
                y = ChangeStroke.Y_TO_TOP_COMBINE;
                break;
        }
        return x * y;
    }

    public String toString() {
        return vectorXY.toString();
    }

    public int[] lengthCompare(ChangeStroke cs, int prune) {
        int[] length = new int[1];
        String[] str = compare(cs, prune);
        length[0] = str[0].length();
        return length;
    }

    public Vector getChangeXY() {
        return vectorXY;
    }

    protected void combine() {
        int i = 0;
        int x,y;
        int value;
        int len = vectorX.size();

        vectorXY = new Vector();

        while (i < len) {
            x = toInt(vectorX, i);
            y = toInt(vectorY, i);
            value = encode(x, y);
            vectorXY.add(i, new Integer(value));
            i++;
        }
    }

}
