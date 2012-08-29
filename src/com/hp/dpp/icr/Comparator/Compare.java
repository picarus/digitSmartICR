package com.hp.dpp.icr.Comparator;


/**
 * Date: Oct 4, 2003
 * Time: 10:15:19 AM
 */
public class Compare {

    public static final int MAX_DIFF = 999;

    static int n = 0;
    static int l = 0;
    static int c = 0;

    private static int minPrune;
    private static int maxPrune;

    public static void initPruneMonitor() {
        minPrune = Integer.MAX_VALUE;
        maxPrune = Integer.MIN_VALUE;
    }

    static void updatePruneMonitor(int prune, int maxlen) {

        System.out.println("P:" + prune + "ML:" + maxlen);

        if (prune < 0) {
            System.out.println(prune);
        }
        if (prune < minPrune)
            minPrune = prune;
        if (prune > maxPrune)
            maxPrune = prune;
    }

    public static void dumpPruneMonitor() {
        System.out.println("Min:" + minPrune);
        System.out.println("Max:" + maxPrune);
    }


    private static int feedback(int i, int j) {
        n++;
        if (n % 50 == 0) {
            n = 0;
            System.out.print("." + i + "," + j);
            l++;
            if (l % 50 == 0) {
                l = 0;
                c++;
                System.out.println(c);
            }
        }
        return i;
    }

}
