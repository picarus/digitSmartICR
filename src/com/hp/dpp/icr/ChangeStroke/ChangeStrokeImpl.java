package com.hp.dpp.icr.ChangeStroke;

import com.hp.dpp.sdk.PenStroke;
import com.hp.dpp.icr.Comparator.Comparator;
import com.hp.dpp.icr.ChangeStroke.ChangeStroke;
import com.hp.dpp.icr.Pattern.*;
import com.hp.dpp.icr.ChangeStroke.Point;

import java.util.Vector;
import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Date: Oct 5, 2003
 * Time: 11:25:49 PM
 */
public abstract class ChangeStrokeImpl extends ChangeStrokeDiffImpl implements ChangeStroke {
    protected static final int DEFAULT_FACTOR = 4;
    protected static int FACTOR;
    protected static int ZERO_FACTOR;
    protected static final float PERCENTAGE=0.3f;
    protected float MIN_CHANGE_X;
    protected float MIN_CHANGE_Y;

    static{
        setFactor(DEFAULT_FACTOR);
    }

    protected com.hp.dpp.icr.ChangeStroke.Point origin_ = null;
    protected Point offset_ = null;

    ChangeStrokeImpl(Vector x, Vector y, Point min) {
        super(x,y);
        MIN_CHANGE_X = min.x_;
        MIN_CHANGE_Y = min.y_;
        comparator_=createComparator();
    }

    ChangeStrokeImpl(int[] valueX, int[] valueY, Point minChange, Point origin) {
        super();
        initVectors();
        SequenceChange seqChange = new SequenceChangeImplInt(valueX, valueY);
        origin_ = seqChange.getFirstPoint();
        setMinChange(minChange);
        offset_ = calculateOffset(origin);
        calcSequenceChange(seqChange);
        comparator_=createComparator();
    }

    ChangeStrokeImpl(int[][] differences) {
        super(differences);
        comparator_=createComparator();
    }

    ChangeStrokeImpl(PenStroke penStroke, Point minChange, Point origin) {
        super();
        initVectors();
        SequenceChange seqChange = new SequenceChangeImplPenStroke(penStroke);
        origin_ = seqChange.getFirstPoint();
        setMinChange(minChange);
        offset_ = calculateOffset(origin);
        calcSequenceChange(seqChange);
        comparator_=createComparator();
    }



    public static void setFactor(int factor){
        FACTOR = factor;
        ZERO_FACTOR = (int) Math.max ( 1, FACTOR * PERCENTAGE );
    }

    public static int getFactor(){
        return FACTOR;
    }

    public static int getZeroFactor(){
        return ZERO_FACTOR;
    }

    public static int getDefaultFactor(){
        return DEFAULT_FACTOR;
    }

    public String[] compare(ChangeStroke cs, int prune) {
        return comparator_.compare(this,cs,prune);
    }


    protected Point calculateOffset(Point origin) {
        Point offset;
        Point curOrigin = getOrigin();
        Point scale = new Point(MIN_CHANGE_X, MIN_CHANGE_Y);
        if (origin == null) {
            offset = new Point();
        } else {
            offset = new Point(curOrigin.x_ - origin.x_, curOrigin.y_ - origin.y_);
            offset.divide(scale);
            offset.toInt();
        }

        return offset;
    }

    public boolean equals(Object obj) {

        boolean isEqual = false;
        ChangeStrokeImpl csi = null;

        isEqual = obj.getClass().equals(getClass());
        if (isEqual) {
            csi = (ChangeStrokeImpl) obj;
        }
        /*
        if (isEqual) {
            isEqual = (MIN_CHANGE_X == csi.getMinChangeX()) && (MIN_CHANGE_Y == csi.getMinChangeY());
        }
        */
        if (isEqual) {
            isEqual = (getChangeX().equals(csi.getChangeX())) && (getChangeY().equals(csi.getChangeY()));
        }

        return isEqual;
    }

    protected void setMinChange(Point minChange) {
        MIN_CHANGE_X = minChange.x_ / FACTOR;
        MIN_CHANGE_Y = minChange.y_ / FACTOR;
    }

    protected void initVectors() {
        vectorX = new Vector();
        vectorY = new Vector();
    }

    public String toString() {
        String text="";
        if (offset_!=null){
             text="("+ offset_.getX()+","+offset_.getY()+")";
        }
        return text+"("+vectorX.toString() + "," + vectorY.toString()+")";
    }



    public float getMinChangeX() {
        return MIN_CHANGE_X;
    }

    public float getMinChangeY() {
        return MIN_CHANGE_Y;
    }

    public Point length() {
        return new Point(vectorX.size(), vectorY.size());
    }

    public Point getOrigin() {
        return origin_;
    }

    public Point getOffset() {
        return offset_;
    }



    protected abstract boolean updateState(ChangeStatus lastChange, ChangeStatus change, Point cur, Point last, Point diff, Point lastDiff);

    protected abstract void initState(ChangeStatus lastChange, Point last, Point lastDiff);

    protected abstract void postState(Point last, Point lastDiff, boolean isChange);

    protected void calcSequenceChange(SequenceChange seqChange) {

        ChangeStatus change,newChange = null;
        Point last,cur,lastDiff,diff;
        boolean isChange = false;

        seqChange.init();

        last = seqChange.getFirstPoint();   // position where the last change took place for each of the coordinates
        change = new ChangeStatus();        // initial position, no movement (0,0) only the sign is important
        lastDiff = new Point();             // the value is important
        cur = last;

        initState(change, last, lastDiff);

        // we look for the first direction the pen is moving
        while (seqChange.hasNext()) {
            cur = seqChange.getNextPoint();
            diff = cur.subs(last);       // calculate the deviation.
            newChange = detectChange(diff, lastDiff, change);
            isChange = updateState(change, newChange, cur, last, diff, lastDiff);
        }

        postState(cur, lastDiff, isChange);
    }

    protected StatusUpdate updateState(boolean isChange, float cur, float last, float diff, float lastDiff) {
        StatusUpdate su = null;

        if (isChange) {
            // there is a new transition
            // the origin must be updated
            su = new StatusUpdate(last + lastDiff, diff);
            //su = new StatusUpdate(cur-diff, diff);
        } else {
            if (diff * lastDiff > 0) {    // the two diff have the same sign
                lastDiff += diff;
            }
            su = new StatusUpdate(last, lastDiff);
        }

        return su;
    }


    protected void filter(Point p) {

        if (Math.abs(p.x_) < MIN_CHANGE_X) {
            p.x_ = 0f;
        }

        if (Math.abs(p.y_) < MIN_CHANGE_Y) {
            p.y_ = 0f;
        }
    }

    protected ChangeStatus detectChange(Point diff, Point lastDiff, ChangeStatus change) {
        // this will not depend on the coordinate
        diff.substract(lastDiff);
        int x = detectChange(diff.getX(), lastDiff.getX(), change.getX(), MIN_CHANGE_X);
        int y = detectChange(diff.getY(), lastDiff.getY(), change.getY(), MIN_CHANGE_Y);

        return new ChangeStatus(x, y);
    }

    protected int detectChange(float diff, float lastDiff, int change, float min) {
        int newChange = 0;

        // el estado 0 no se recupera nunca!!!!!
        // solo se recupera en las transiciones  con ChangeStrokeCoupledSize
        if (change == 0) {
            if (Math.abs(diff) < min) {
                newChange = ChangeStroke.NOWHERE;
            } else {
                newChange = (diff > 0.0f ? ChangeStroke.POS : ChangeStroke.NEG);
            }
        } else {
            newChange = (((diff * lastDiff <= 0.0f) && (Math.abs(diff) > min)) ? -change : change);
        }

        return newChange;
    }

    protected float toFloat(Vector v, int index) {
        Integer integer = (Integer) v.get(index);
        return integer.floatValue();
    }

    protected int toInt(Vector v, int index) {
        Integer integer = (Integer) v.get(index);
        return integer.intValue();
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

}


