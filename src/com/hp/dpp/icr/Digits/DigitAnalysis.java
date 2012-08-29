package com.hp.dpp.icr.Digits;

import com.hp.dpp.icr.Pattern.Pattern;

import java.util.Vector;
import java.util.Iterator;
import java.io.Serializable;

/**
 * Date: Nov 9, 2003
 * Time: 7:07:11 PM
 */
public class DigitAnalysis implements Serializable {

    public final int N_ELEMS_ = 2;
    protected final int MIN_SIMIL = 0;
    protected final int MAX_SIMIL = 1;

    DigitSet digitSet_;
    int interSimil[][][];
    //int intraSimil[][];

    public DigitAnalysis(DigitSet digitSet) {
        digitSet_ = digitSet;
        initInterSimil();
        //initIntraSimil();
    }

    private void initInterSimil() {
        int len = digitSet_.getNumberOfDigits();
        int i;
        int j;
        int k;

        interSimil = new int[len][len][N_ELEMS_];
        i = 0;
        while (i < len) {
            k = 0;
            while (k < len) {
                j = 0;
                while (j < N_ELEMS_) {
                    interSimil[i][k][j] = 0;
                    j++;
                }
                k++;
            }
            i++;
        }
    }

    /*
       private void initIntraSimil() {
           int len = digitSet_.getNumberOfDigits();
           int i;
           int j;

           intraSimil = new int[len][N_ELEMS_];
           i = 0;
           while (i < len) {
               j = 0;
               while (j < N_ELEMS_) {
                   intraSimil[i][j] = 0;
                   j++;
               }
               i++;
           }
       }
    */
    public void calcInterSimilarity() {
        Digit digitI;
        int digValueI,digValueJ;
        Digit digitJ;
        Iterator itI = digitSet_.getDigits();
        Iterator itJ;

        while (itI.hasNext()) {
            digitI = (Digit) itI.next();
            digValueI = digitI.getDigit();

            itJ = digitSet_.getDigits();
            while (itJ.hasNext()) {
                digitJ = (Digit) itJ.next();
                digValueJ = digitJ.getDigit();
                if (digValueI < digValueJ) {
                    calcInterSimilarity(digitI, digitJ);
                }
            }
        }
    }

    protected int calcInterSimilarity(Digit digitI, Digit digitJ) {
        int maxSimil = Integer.MIN_VALUE;
        int minSimil = Integer.MAX_VALUE;
        int curSimil;
        Vector vecPatternI,vecPatternJ;
        int lenI,lenJ;
        int i,j;
        vecPatternI = digitI.vectorPattern_;
        vecPatternJ = digitJ.vectorPattern_;
        Pattern patI,patJ;

        int digitIs = digitI.getDigit();
        int digitJs = digitJ.getDigit();

        lenI = vecPatternI.size();
        lenJ = vecPatternJ.size();
        i = 0;
        while (i < lenI) {
            patI = (Pattern) vecPatternI.get(i);
            j = 0;
            while (j < lenJ) {
                patJ = (Pattern) vecPatternJ.get(j);
                curSimil = patI.similarity(patJ, Integer.MAX_VALUE);
                if (curSimil > maxSimil) {
                    maxSimil = curSimil;
                }
                if (curSimil < minSimil) {
                    minSimil = curSimil;
                }
                j++;
            }
            i++;
        }
        setInterSimil(digitIs, digitJs, maxSimil, minSimil);
        return maxSimil;
    }


    public void calcIntraSimilarity() {

        Digit digit;
        Iterator it = digitSet_.getDigits();

        while (it.hasNext()) {
            digit = (Digit) it.next();
            calcIntraSimilarity(digit);
        }
    }

    protected int calcIntraSimilarity(Digit digit) {
        int maxSimil = Integer.MIN_VALUE;
        int minSimil = Integer.MAX_VALUE;
        int curSimil;
        Vector vecPattern;
        int len;
        int i,j;
        vecPattern = digit.vectorPattern_;
        Pattern patI,patJ;
        int digits = digit.getDigit();

        len = vecPattern.size();
        i = 0;
        while (i < len) {
            patI = (Pattern) vecPattern.get(i);
            j = 0;
            while (j < len) {
                if (i != j) {
                    patJ = (Pattern) vecPattern.get(j);
                    curSimil = patI.similarity(patJ, Integer.MAX_VALUE);
                    if (curSimil > maxSimil) {
                        maxSimil = curSimil;
                    }
                    if (curSimil < minSimil) {
                        minSimil = curSimil;
                    }
                }
                j++;
            }
            i++;
        }
        setIntraSimil(digits, maxSimil, minSimil);
        return maxSimil;
    }


    private void setIntraSimil(int digits, int maxSimil, int minSimil) {
        setInterSimil(digits, digits, maxSimil, minSimil);
    }

    private void setInterSimil(int digitI, int digitJ, int maxSimil, int minSimil) {
        interSimil[digitI][digitJ][MAX_SIMIL] = maxSimil;
        interSimil[digitI][digitJ][MIN_SIMIL] = minSimil;
    }


}
