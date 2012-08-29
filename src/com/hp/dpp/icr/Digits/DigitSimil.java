package com.hp.dpp.icr.Digits;

/**
 * Date: Nov 2, 2003
 * Time: 8:23:50 PM
 */
public class DigitSimil implements Comparable {

    private int digit_;
    private int simil_;

    public DigitSimil(int digit, int simil) {
        digit_ = digit;
        simil_ = simil;
    }

    public int getDigit() {
        return digit_;
    }

    public int getSimil() {
        return simil_;
    }

    public boolean isDigit(int digit) {
        return digit_ == digit;
    }

    public int compareTo(Object o) {
        DigitSimil ds = (DigitSimil) o;
        int compare;
        int simil = ds.getSimil();
        if (simil_ == simil) {
            compare = 0;
        } else {
            compare = simil_ - simil;
        }

        return compare;
    }

}
