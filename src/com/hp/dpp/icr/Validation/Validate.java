package com.hp.dpp.icr.Validation;

import com.hp.dpp.icr.Pattern.Pattern;
import com.hp.dpp.icr.Digits.DigitSet;
import com.hp.dpp.icr.Digits.DigitCriteriaAll;
import com.hp.dpp.icr.Digits.DigitCriteriaSelect;
import com.hp.dpp.utils.Misc;

import java.util.Map;
import java.util.Iterator;

/**
 * Date: Nov 2, 2003
 * Time: 7:57:47 PM
 */
public class Validate implements Validation {

    public String getIDPrefix(){
        return "V";
    }

    public void validate(Statistics statistics, Map values, int[] valid) {
        Iterator itPat = values.keySet().iterator();
        String fieldName;
        Integer[] position;
        Pattern pattern;
        int goodValue;
        int guessValue;
        boolean isCorrect;
        while ( itPat.hasNext() ) {
            fieldName = (String) itPat.next();
            position = Misc.getPosition(fieldName);
            goodValue = valid[position[0].intValue()];
            pattern = (Pattern) values.get(fieldName);
            guessValue = pattern.getDigit();
            isCorrect = statistics.isOK(goodValue, guessValue);
            pattern.setIsCorrect(isCorrect);
        }
    }

    public void guess(DigitSet digitSet, Map values){
        digitSet.guess(values, new DigitCriteriaAll());
    }

    public void guessFromList(DigitSet digitSet, Map values, int[] digits){
        digitSet.guess(values, new DigitCriteriaSelect(digits));
    }

}
