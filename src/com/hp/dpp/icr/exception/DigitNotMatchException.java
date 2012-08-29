package com.hp.dpp.icr.exception;

/**
 * Date: Oct 11, 2003
 * Time: 7:30:20 PM
 */
public class DigitNotMatchException extends Exception {

    public static final String message = "Digits not match exception:";
    protected int digit_;

    public DigitNotMatchException(int digit) {
        super(message + digit);
        digit_ = digit;
    }

    public int getDigit() {
        return digit_;
    }

}
