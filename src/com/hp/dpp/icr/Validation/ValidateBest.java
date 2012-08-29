package com.hp.dpp.icr.Validation;

import com.hp.dpp.icr.Pattern.Pattern;
import com.hp.dpp.icr.DigitSet;
import com.hp.dpp.icr.DigitSimil;
import com.hp.dpp.icr.Digits.DigitCriteriaAll;
import com.hp.dpp.icr.Digits.DigitCriteriaSelect;
import com.hp.dpp.icr.exception.ValidationException;
import com.hp.dpp.utils.Misc;

import java.util.Map;
import java.util.Iterator;
import java.util.Vector;

/**
 * Date: Nov 2, 2003
 * Time: 7:57:53 PM
 */
public class ValidateBest implements Validation {

    Map bg = null;

     public String getIDPrefix(){
        return "B";
    }

    public void validate(Statistics statistics, Map values, int[] valid) throws ValidationException {
        Iterator itPat = values.keySet().iterator();
        String fieldName;
        Integer[] position;
        Pattern pattern;
        Vector bgVec;
        int goodValue;
        int guessValue;
        boolean result;

        if (bg == null) {
            throw new ValidationException("Best Guesses Map not created");
        }

        while (itPat.hasNext()) {
            fieldName = (String) itPat.next();
            position = Misc.getPosition(fieldName);
            goodValue = valid[position[0].intValue()];
            pattern = (Pattern) values.get(fieldName);
            bgVec = (Vector) bg.get(fieldName);
            if (containsValue(bgVec, goodValue)) {
                guessValue = goodValue;
                pattern.setDigit(goodValue);
            } else {
                guessValue = pattern.getDigit(); // first choice
            }
            result=statistics.isOK(goodValue, guessValue);
            pattern.setIsCorrect(result);
        }
    }

    private boolean containsValue(Vector vector, int digit) {
        boolean contains = false;
        int len = vector.size();
        int curDigit;
        int i = 0;
        com.hp.dpp.icr.Digits.DigitSimil ds;
        // Trace
        // if ( len < 1 ) System.out.println(len);

        while ( ( i < len) && ( ! contains ) ) {
            ds = (com.hp.dpp.icr.Digits.DigitSimil) vector.get(i);
            curDigit = ds.getDigit();
            contains = (digit == curDigit);
            i++;
        }
        return contains;
    }

    public void guess(com.hp.dpp.icr.Digits.DigitSet digitSet, Map values) {
        bg = digitSet.bestGuesses(values, new DigitCriteriaAll());
    }

    public void guessFromList(com.hp.dpp.icr.Digits.DigitSet digitSet, Map values, int[] digits){
        bg = digitSet.bestGuesses(values, new DigitCriteriaSelect(digits));
    }

}
