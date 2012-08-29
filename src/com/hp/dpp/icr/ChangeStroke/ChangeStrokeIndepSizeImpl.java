package com.hp.dpp.icr.ChangeStroke;

import com.hp.dpp.sdk.PenStroke;
import com.hp.dpp.icr.Comparator.Comparator;
import com.hp.dpp.icr.Comparator.ComparatorImpl;
import com.hp.dpp.icr.ChangeStroke.ChangeStroke;
import com.hp.dpp.icr.ChangeStroke.ChangeStrokeIndep;
import com.hp.dpp.icr.ChangeStroke.Point;
import com.hp.dpp.icr.Pattern.ChangeStatus;

import java.util.Vector;

/**
 * Date: Nov 1, 2003
 * Time: 9:05:58 PM
 */
public class ChangeStrokeIndepSizeImpl extends ChangeStrokeIndep implements ChangeStroke {

    protected Vector vectorXPos;
    protected Vector vectorYPos;

    public ChangeStrokeIndepSizeImpl(int[] valueX, int[] valueY, Point minChange, Point origin) {
        super(valueX, valueY, minChange, origin);
    }

    public ChangeStrokeIndepSizeImpl(int[][] differences) {
        super(differences);
    }

    public ChangeStrokeIndepSizeImpl(PenStroke penStroke, Point minChange, Point origin) {
        super(penStroke, minChange, origin);
    }

    protected Comparator createComparator(){
        return new ComparatorImpl();
    }

    protected void initState(ChangeStatus lastChange, Point last, Point lastDiff) {
        vectorXPos = new Vector();
        vectorYPos = new Vector();
    }

    protected void postState(Point cur, Point lastDiff, boolean isChange) {

        vectorXPos.add(new Float(cur.x_));
        vectorYPos.add(new Float(cur.y_));

        applySizes(vectorXPos, vectorX, MIN_CHANGE_X);
        applySizes(vectorYPos, vectorY, MIN_CHANGE_Y);
    }

    private void applySizes(Vector vectorPos, Vector vector, float factor) {
        int i = 0;
        int len;
        float last;
        float cur;
        Float tempF;
        float size;

        len = vector.size();
        tempF = (Float) vectorPos.get(0);
        last = tempF.floatValue();
        while (i < len) {
            tempF = (Float) vectorPos.get(i + 1);
            cur = tempF.floatValue();
            size = (cur - last) / factor;
            vector.set(i, new Integer((int) size));
            last = cur;
            i++;
        }
    }
}
