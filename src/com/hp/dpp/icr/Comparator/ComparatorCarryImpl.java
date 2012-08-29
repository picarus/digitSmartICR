package com.hp.dpp.icr.Comparator;

/**
 * Date: Nov 8, 2003
 * Time: 9:22:08 PM
 */
public class ComparatorCarryImpl extends ComparatorImpl implements Comparator {

    protected String similarity(int[] r,
                                int i,
                                int[] s,
                                int j,
                                int prune,
                                int carry) {
        String diff = null;
        String diffS;
        int lenS;
        String diffR;
        int lenR;
        int ri,si,risi;
        boolean match;
        boolean oneless;

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
            } else {
                match = false;
                oneless = false;
                if (r[i] == s[j]) {
                    match = true;
                } else if (carry != 0) {
                    if ((carry * r[i] == s[j]) || (carry * s[j] == r[i])) {
                        match = true;
                        oneless = true;
                        prune++;
                    }
                }

                if (!match) {
                    diffS = similarity(r, i, s, j + 1, prune - 1, s[j]);
                    lenS = diffS.length();
                    if (lenS > 0) {
                        if (diffS.charAt(0) == ONELESS) {
                            diffS = diffS.substring(1);
                            oneless = true;
                            lenS--;
                        }
                    }

                    prune = Math.min(prune - 1, lenS);
                    diffR = similarity(r, i + 1, s, j, prune, r[i]);
                    lenR = diffR.length();
                    if (lenR > 0) {
                        if (diffR.charAt(0) == ONELESS) {
                            diffS = diffR.substring(1);
                            oneless = true;
                            lenR--;
                        }
                    }

                    if (!oneless) {
                        if (lenS < lenR) {
                            diff = SECOND + diffS;
                        } else {
                            diff = FIRST + diffR;
                        }
                    } else {
                        if (lenS < lenR) {
                            diff = diffS;
                        } else {
                            diff = diffR;
                        }
                    }
                } else {
                    diff = similarity(r, i + 1, s, j + 1, prune, 0);
                    if (oneless) {
                        diff = ONELESS + diff;
                    } else {
                        diff = EQUAL + diff;
                    }
                }
            }
        }

        return diff;
    }
}
