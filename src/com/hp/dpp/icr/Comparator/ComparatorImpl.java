package com.hp.dpp.icr.Comparator;

import com.hp.dpp.icr.ChangeStroke.ChangeStroke;
import com.hp.dpp.icr.ChangeStroke.ChangeStrokeDiff;
import com.hp.dpp.icr.ChangeStroke.ChangeStrokeDiffImpl;

import java.util.Vector;

/**
 * Date: Nov 8, 2003
 * Time: 8:56:02 PM
 */
public class ComparatorImpl implements Comparator {

    protected static final String EQUAL = "";
    protected static final char ONELESS = '-';
    protected static final char FIRST = '1';
    protected static final char SECOND = '2';
    protected static final char ENDFIRST = '*';
    protected static final char ENDSECOND = '+';
    protected static final char ENDPRUNEC = '.';
    protected static final String ENDPRUNE = "" + ENDPRUNEC;
    protected static final int ENDPRUNENUM = 2;

    public String similarity(Vector change1, int i, Vector change2, int j, int prune) {

        int[][] change = new int[2][];
        int maxlen;

        change[0] = convert(change1);
        change[1] = convert(change2);

        maxlen = Math.max(change[0].length, change[1].length) + 2;

        prune = Math.min(maxlen, prune);
        // Trace
        //updatePruneMonitor(prune,maxlen);

        return similarity(change[0], i, change[1], j, prune);
    }

    public String[] compare(ChangeStrokeDiff cs1, ChangeStrokeDiff cs2, int prune) {
        String[] changes = new String[2];
        changes[0] = similarity(cs1.getChangeX(), 0, cs2.getChangeX(), 0, prune);
        changes[1] = similarity(cs1.getChangeY(), 0, cs2.getChangeY(), 0, prune);
        return changes;
    }


    public int[] lengthCompare(ChangeStrokeDiff cs1, ChangeStrokeDiff cs2, int prune) {
        int[] length = new int[2];
        String[] str = compare(cs1, cs2, prune);
        length[0] = str[0].length();
        length[1] = str[1].length();
        return length;
    }

    public ChangeStrokeDiff compareVec(ChangeStroke cs1, ChangeStroke cs2, int prune) {
        int[][] length = new int[1][2];
        String[] str = compare(cs1, cs2, prune);
        length[0][0] = str[0].length();
        length[0][1] = str[1].length();
        return new ChangeStrokeDiffImpl(length);
    }


    protected static String repeatChar(int n, char c) {
        String str = "";

        for (int i = 0; i < n; i++) {
            str += c;
        }

        return str;
    }

    protected String similarity(int[] r,
                                int i,
                                int[] s,
                                int j,
                                int prune) {
        String diff = null;
        String diffS;
        int lenS;
        String diffR;
        int lenR;
        int ri,si,risi;

        boolean blenR = (i >= r.length);
        boolean blenS = (j >= s.length);

        ri = r.length - i;
        si = s.length - j;

        if (blenR && blenS) {
            diff = "";
        } else if (blenR) {
            diff = repeatChar(si, ENDSECOND);
        } else if (blenS) {
            diff = repeatChar(ri, ENDFIRST);
        } else if (prune == 0) {
            diff = ENDPRUNE;
        } else {
            risi = Math.abs(ri - si);
            //feedback(i,j);
            if (prune < risi) {
                diff = repeatChar(risi, ENDPRUNEC);
            } else if (r[i] == s[j]) {
                diff = similarity(r, i + 1, s, j + 1, prune);
                diff = EQUAL + diff;
            } else {
                diffS = similarity(r, i, s, j + 1, prune - 1);
                lenS = diffS.length();
                //prune = Math.min(prune, lenS) - 1;
                prune = Math.min(prune - 1, lenS + 1);
                diffR = similarity(r, i + 1, s, j, prune);
                lenR = diffR.length();
                if (lenS < lenR) {
                    diff = SECOND + diffS;
                } else {
                    diff = FIRST + diffR;
                }
            }
        }

        return diff;
    }


    protected static int[] convert(Vector change) {

        int len = change.size();
        int[] list = new int[len];
        Integer integer;

        for (int i = 0; i < len; i++) {
            integer = (Integer) change.get(i);
            list[i] = integer.intValue();
        }

        return list;
    }

}
