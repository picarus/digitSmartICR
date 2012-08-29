package com.hp.dpp.icr.ChangeStroke;

import com.hp.dpp.sdk.PenStroke;
import com.hp.dpp.icr.Comparator.Comparator;
import com.hp.dpp.icr.Comparator.ComparatorGradeOffsetImpl;
import com.hp.dpp.icr.ChangeStroke.ChangeStroke;
import com.hp.dpp.icr.ChangeStroke.ChangeStrokeCoupled;
import com.hp.dpp.icr.ChangeStroke.Point;
import com.hp.dpp.icr.Pattern.ChangeStatus;
import com.hp.dpp.icr.Pattern.StatusUpdate;

import java.util.Vector;
import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Date: Oct 26, 2003
 * Time: 5:12:18 PM
 */
public class ChangeStrokeCoupledSize extends ChangeStrokeCoupled implements ChangeStroke {

    protected Vector vectorXPos;
    protected Vector vectorYPos;

    public ChangeStrokeCoupledSize(Vector x, Vector y, Point min) {
        super(x, y, min);
    }

    public ChangeStrokeCoupledSize(int[] valueX, int[] valueY, Point minChange, Point origin) {
        super(valueX, valueY, minChange, origin);
    }

    public ChangeStrokeCoupledSize(int[][] differences) {
        super(differences);
        vectorXPos = new Vector();
        vectorYPos = new Vector();
    }

    public ChangeStrokeCoupledSize(PenStroke penStroke, Point minChange, Point origin) {
        super(penStroke, minChange, origin);
    }

    protected Comparator createComparator(){
        return new ComparatorGradeOffsetImpl();
    }

    protected Vector getVectorXPos() {
        return vectorXPos;
    }

    protected Vector getVectorYPos() {
        return vectorYPos;
    }

    public boolean equals(Object obj) {
        //ChangeStrokeCoupledSize cscs = null;
        // the posVectors are not required to be equal!!!
        return super.equals(obj);
    }

    protected void initState(ChangeStatus lastChange, Point last, Point lastDiff) {
        vectorXPos = new Vector();
        vectorYPos = new Vector();
        // the first element is not required it will be added when the first change is detected
        // vectorXPos.add(new Float(last.x_));
        // vectorYPos.add(new Float(last.y_));
    }

    protected void postState(Point cur, Point lastDiff, boolean isChange) {

        vectorXPos.add(new Float(cur.x_));
        vectorYPos.add(new Float(cur.y_));

        applySizes(vectorXPos, vectorX, MIN_CHANGE_X);
        applySizes(vectorYPos, vectorY, MIN_CHANGE_Y);
    }

    public int[] lengthCompare(ChangeStroke cs, int prune) {
        int[] length;
        length = comparator_.lengthCompare(this,cs, prune);
        return length;
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

    private void applySizes2(Vector vectorPos, Vector vector, float factor) {
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
            if (last * cur > 0) {
                size = cur - last;
            } else {
                size = cur;
            }
            size /= factor;
            vector.set(i, new Integer((int) size));
            last = cur;
            i++;
        }
    }


    protected boolean updateState(ChangeStatus lastChange, ChangeStatus change, Point cur, Point last, Point diff, Point lastDiff) {
        boolean isChange = false;

        isChange = !(lastChange.equals(change));

        StatusUpdate statusUpdateX = updateState(isChange, cur.x_, last.x_, diff.x_, lastDiff.x_);
        lastChange.setX(change.getX());
        last.setX(statusUpdateX.getLast());
        lastDiff.setX(statusUpdateX.getLastDif());

        StatusUpdate statusUpdateY = updateState(isChange, cur.y_, last.y_, diff.y_, lastDiff.y_);
        lastChange.setY(change.getY());
        last.setY(statusUpdateY.getLast());
        lastDiff.setY(statusUpdateY.getLastDif());

        if (isChange) {
            vectorX.add(new Integer(lastChange.getX()));
            vectorY.add(new Integer(lastChange.getY()));
            vectorXPos.add(new Float(last.x_));
            vectorYPos.add(new Float(last.y_));
        }
        return isChange;
    }

    public Point get(int index) {
        float x = toFloat(vectorX, index);
        float y = toFloat(vectorY, index);
        return new Point(x, y);
    }

    public Shape asShape() {
        GeneralPath gp = new GeneralPath();
        int i = 0;
        Point cur;
        int len = Math.min(vectorY.size(), vectorX.size());
        Point last = new Point(0, 0);

        gp.moveTo(last.x_, last.y_);

        while (i < len) {
            cur = get(i);
            last.addition(cur);
            gp.lineTo(last.x_, last.y_);
            i++;
        }

        return gp;
    }

    public String toString() {
        String text = super.toString();
        text += ";[" + vectorXPos.toString() + "," + vectorYPos.toString() + "]";
        return text;
    }

}
