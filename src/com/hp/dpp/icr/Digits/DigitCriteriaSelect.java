package com.hp.dpp.icr.Digits;

/**
 * Date: Nov 9, 2003
 * Time: 11:43:33 PM
 */
public class DigitCriteriaSelect implements DigitCriteria {
    int[] digits_;

    public DigitCriteriaSelect(int[] digits) {
        digits_ = digits;
    }

    private boolean contained(int digit) {
        boolean found = false;
        int i = 0;
        int len = digits_.length;
        while ((!found) && (i < len)) {
            found = digit == digits_[i];
            i++;
        }
        return found;
    }

    public boolean makesCriteria(Digit digit) {
        return contained(digit.getDigit());
    }
}

