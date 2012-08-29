package com.hp.dpp.icr.Validation;

import com.hp.dpp.icr.Digits.DigitSet;
import com.hp.dpp.icr.exception.ValidationException;

import java.util.Map;

/**
 * Date: Nov 2, 2003
 * Time: 7:58:06 PM
 */
public interface Validation {
    final int[] idValue_ = {5, 6, 7, 3, 8, 2, 0};
    final int[] idInput_ = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    String getIDPrefix();

    void validate(Statistics statistics, Map values, int[] valid) throws ValidationException;

    void guess(DigitSet digitSet, Map values);

    void guessFromList(DigitSet digitSet, Map values, int[] digits);

}
