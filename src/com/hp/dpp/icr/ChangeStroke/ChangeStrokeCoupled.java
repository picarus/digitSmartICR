package com.hp.dpp.icr.ChangeStroke;

import com.hp.dpp.sdk.PenStroke;
import com.hp.dpp.icr.Comparator.Comparator;
import com.hp.dpp.icr.Comparator.ComparatorImpl;
import com.hp.dpp.icr.ChangeStroke.ChangeStroke;
import com.hp.dpp.icr.ChangeStroke.Point;
import com.hp.dpp.icr.Pattern.ChangeStatus;
import com.hp.dpp.icr.Pattern.StatusUpdate;

import java.util.Vector;


/**
 * Date: Oct 5, 2003
 * Time: 11:24:53 PM
 */
public class ChangeStrokeCoupled extends ChangeStrokeImpl implements ChangeStroke {

    public ChangeStrokeCoupled(Vector x, Vector y, Point min) {
        super(x, y, min);
    }

    public ChangeStrokeCoupled(int[] valueX, int[] valueY, Point minChange, Point origin) {
        super(valueX, valueY, minChange, origin);
    }

    public ChangeStrokeCoupled(int[][] differences) {
        super(differences);
    }

    public ChangeStrokeCoupled(PenStroke penStroke, Point minChange, Point origin) {
        super(penStroke, minChange, origin);
    }

    protected void initState(ChangeStatus lastChange, Point last, Point lastDiff) {

    }

    protected void postState(Point last, Point lastDiff, boolean isChange) {

    }

    protected Comparator createComparator(){
        return new ComparatorImpl();
    }

    protected boolean updateState(ChangeStatus lastChange, ChangeStatus change, Point cur, Point last, Point diff, Point lastDiff) {
        boolean isChange = false;
        boolean isChangeX;
        boolean isChangeY;
        // lastChange, last and lastDiff needs to be updated
        //

        isChangeX = (lastChange.getX() != change.getX());

        StatusUpdate statusUpdateX = updateState(isChangeX, cur.x_, last.x_, diff.x_, lastDiff.x_);
        lastChange.setX(change.getX());
        last.setX(statusUpdateX.getLast());
        lastDiff.setX(statusUpdateX.getLastDif());

        isChangeY = (lastChange.getY() != change.getY());
        StatusUpdate statusUpdateY = updateState(isChangeY, cur.y_, last.y_, diff.y_, lastDiff.y_);
        lastChange.setY(change.getY());
        last.setY(statusUpdateY.getLast());
        lastDiff.setY(statusUpdateY.getLastDif());

        isChange = isChangeX || isChangeY;

        if (isChange) {
            vectorX.add(new Integer(lastChange.getX()));
            vectorY.add(new Integer(lastChange.getY()));
        }
        return isChange;
    }




}

