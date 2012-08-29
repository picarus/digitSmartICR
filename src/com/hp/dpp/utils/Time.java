package com.hp.dpp.utils;

/**
 * Date: Oct 11, 2003
 * Time: 2:36:11 PM
 */
public class Time {
    static long time = 0;

    public static void initTime() {
        time = System.currentTimeMillis();
    }

    public static void calcTime() {
        long curtime = System.currentTimeMillis();
        time = curtime - time;
        System.out.println("The operation took:" + time + " msec.");
        time = curtime;
    }

}
