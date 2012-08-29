package com.hp.dpp.utils;

import java.util.Vector;

/**
 * Date: Nov 9, 2003
 * Time: 6:48:11 PM
 */
public class Misc {

       public static Integer[] getPosition(String position) {
        int wherePoint;
        String value;
        Vector vSize = new Vector();
        int i = 0;

        wherePoint = position.indexOf(".");
        // the first string before the dot is skipped
        position = position.substring(wherePoint + 1);
        wherePoint = position.indexOf(".");

        while (wherePoint >= 0) {

            value = position.substring(0, wherePoint);
            vSize.add(i, new Integer(value));
            position = position.substring(wherePoint + 1);
            wherePoint = position.indexOf(".");
            i++;

        }

        vSize.add(i, new Integer(position));

        return vectorToIntegerArray(vSize);
    }

    private static Integer[] vectorToIntegerArray(Vector vSize) {
         Object[] obAr = vSize.toArray();

         Integer[] intAr = new Integer[obAr.length];
         for (int j = 0; j < obAr.length; j++) {
             intAr[j] = (Integer) obAr[j];
         }
         return intAr;
     }

}
