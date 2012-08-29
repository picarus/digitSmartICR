package com.hp.dpp.icr.ChangeStroke;

import com.hp.dpp.sdk.PenStroke;
import com.hp.dpp.icr.Comparator.Comparator;
import com.hp.dpp.icr.Comparator.ComparatorImpl;
import com.hp.dpp.icr.ChangeStroke.ChangeStroke;
import com.hp.dpp.icr.ChangeStroke.ChangeStrokeImpl;
import com.hp.dpp.icr.ChangeStroke.Point;
import com.hp.dpp.icr.Pattern.ChangeStatus;
import com.hp.dpp.icr.Pattern.StatusUpdate;

import java.awt.*;


/**
 * Created by IntelliJ IDEA.
 * User: JOMAG
 * Date: Oct 1, 2003
 * Time: 10:52:46 PM
 * To change this template use Options | File Templates.
 */
public class ChangeStrokeIndep extends ChangeStrokeImpl implements ChangeStroke {

    public ChangeStrokeIndep(int[] valueX, int[] valueY, Point minChange, Point origin) {
        super(valueX, valueY, minChange, origin);
    }

    public ChangeStrokeIndep(int[][] differences) {
        super(differences);
    }

    public ChangeStrokeIndep(PenStroke penStroke, Point minChange, Point origin) {
        super(penStroke, minChange, origin);
    }

    protected Comparator createComparator(){
        return new ComparatorImpl();
    }

    protected boolean updateState(ChangeStatus lastChange, ChangeStatus change, Point cur, Point last, Point diff, Point lastDiff) {
        boolean isChangeX,isChangeY;
        // lastChange, last and lastDiff needs to be updated
        //
        isChangeX = (lastChange.getX() != change.getX());
        StatusUpdate statusUpdateX = updateState(isChangeX, cur.x_, last.x_, diff.x_, lastDiff.x_);
        lastChange.setX(change.getX());
        last.setX(statusUpdateX.getLast());
        lastDiff.setX(statusUpdateX.getLastDif());

        if (isChangeX) {
            vectorX.add(new Integer(lastChange.getX()));
        }

        isChangeY = (lastChange.getY() != change.getY());

        StatusUpdate statusUpdateY = updateState(isChangeY, cur.y_, last.y_, diff.y_, lastDiff.y_);
        lastChange.setY(change.getY());
        last.setY(statusUpdateY.getLast());
        lastDiff.setY(statusUpdateY.getLastDif());

        if (isChangeY) {
            vectorY.add(new Integer(lastChange.getY()));
        }
        return (isChangeX || isChangeY);
    }

    protected void initState(ChangeStatus lastChange, Point last, Point lastDiff) {
        // nothing to do
    }

    protected void postState(Point last, Point lastDiff, boolean isChange) {
        // nothing to do
    }

    public Shape asShape() {
        return null;
    }


}
