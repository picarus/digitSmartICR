package com.hp.dpp.icr.Digits;

import com.hp.dpp.icr.Pattern.Pattern;
import com.hp.dpp.icr.Pattern.PatternDiff;
import com.hp.dpp.icr.exception.DigitException;
import com.hp.dpp.icr.exception.DigitNotMatchException;

import java.util.*;
import java.io.*;

/**
 * Date: Oct 29, 2003
 * Time: 9:08:16 PM
 */
public class DigitSet implements Serializable {

    Map digitMap_; // package access

    public DigitSet() {
        digitMap_ = new TreeMap();
    }

    public void addPattern(Pattern[] pattern) throws DigitException {
        for (int i = 0; i < pattern.length; i++) {
            addPattern(pattern[i]);
        }
    }

    public boolean addPattern(Pattern pattern) throws DigitException {
        boolean existed = false;
        int value;
        com.hp.dpp.icr.Digits.Digit digit;
        value = pattern.getDigit();
        if (value < 0)
            throw new DigitException();

        digit = getDigit(value);

        if (digit != null) {
            try {
                existed = digit.addPattern(pattern);
            } catch (DigitNotMatchException e) {
                throw new Error();  // critical error
            }
        } else {
            try {
                digit = new com.hp.dpp.icr.Digits.Digit(value, pattern);
                digitMap_.put(new Integer(value), digit);
            } catch (DigitNotMatchException e) {
                throw new Error();
            }
        }
        return existed;
    }

    public com.hp.dpp.icr.Digits.Digit getDigit(int digit) {
        Integer digitKey;
        digitKey = new Integer(digit);
        return (com.hp.dpp.icr.Digits.Digit) digitMap_.get(digitKey);
    }

    public int getNumberOfPatterns(int digitValue) throws DigitException {
        int number = 0;
        com.hp.dpp.icr.Digits.Digit digit = getDigit(digitValue);
        if (digit != null) {
            number = digit.getPatternNumber();
        } else {
            throw new DigitException();
        }
        return number;
    }

    public int[][] getNumberOfPatterns() {
        int len = digitMap_.size();
        int[][] patterns = new int[len][2];
        int i = 0;
        Integer key;
        com.hp.dpp.icr.Digits.Digit digit;

        Iterator it = digitMap_.keySet().iterator();
        while (it.hasNext()) {
            key = (Integer) it.next();
            digit = (com.hp.dpp.icr.Digits.Digit) digitMap_.get(key);
            patterns[i][0] = key.intValue();
            patterns[i][1] = digit.getPatternNumber();
            i++;
        }

        return patterns;
    }

    Iterator getDigits() {
        return digitMap_.values().iterator();
    }

    public static DigitSet load(InputStream is) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(is);
        DigitSet digitSet;
        digitSet = (DigitSet) ois.readObject();
        ois.close();
        return digitSet;
    }

    public void save(OutputStream os) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(this);
        oos.close();
    }

    public int getNumberOfDigits() {
        return digitMap_.size();
    }

    public void guess(Map patternMap, DigitCriteria digitCriteria) {
        Pattern pattern;
        Object name;
        Iterator it = patternMap.keySet().iterator();
        while (it.hasNext()) {
            name = it.next();
            // Trace
            //System.out.println(name);
            pattern = (Pattern) patternMap.get(name);
            guess(pattern, digitCriteria);
        }
    }

    public int guess(Pattern pattern, DigitCriteria digitCriteria) {
        com.hp.dpp.icr.Digits.Digit digit;
        int digitValue = -1;
        int similarity = Integer.MAX_VALUE - 1;
        int distance = -1;
        boolean found = false;
        int curDigit;
        Iterator itDigits = digitMap_.values().iterator();

        while (itDigits.hasNext() && (!found)) {
            digit = (com.hp.dpp.icr.Digits.Digit) itDigits.next();

            if (digitCriteria.makesCriteria(digit)) {

                curDigit = digit.getDigit();

                if (digit.contains(pattern)) {
                    similarity = 0;
                    digitValue = curDigit;
                    found = true;
                }

                if (!found)
                    distance = digit.similarity(pattern, similarity);

                // Trace
                //System.out.println(";D"+curDigit+";"+distance+";"+similarity);
                if (distance < similarity) {
                    similarity = distance;
                    digitValue = curDigit;
                    found = (similarity == 0);
                }
            }
        }

        pattern.setDigit(digitValue, similarity);
        return digitValue;
    }

    public void guessNoEven(Map patternMap, DigitCriteria digitCriteria) {
        Object name;
        Pattern pattern;
        Map mapGuess = new TreeMap();
        int simil;
        Vector bgVec2;

        Iterator itMap = patternMap.keySet().iterator();

        while ( itMap.hasNext() ) {
            name = itMap.next();
            pattern = (Pattern) patternMap.get(name);
            mapGuess = bestGuessesNoEven(pattern, digitCriteria);
            simil = pattern.getSimilarity();
            bgVec2 = undoEven(pattern, mapGuess, simil);
            // todo: el resultado deberia ser unico
        }
    }

    private Set combPatterns(Vector iListPattern, Vector jListPattern) {
        Set combs = new TreeSet();
        Pattern iPattern;
        int iLen;
        int i;
        Set setCur;

        iLen = iListPattern.size();

        i = 0;
        while ( i < iLen ) {
            iPattern = (Pattern) iListPattern.get(i);
            setCur = combPattern(jListPattern, iPattern);
            combs.add(setCur);
            i++;
        }

        return combs;
    }

    private Set combPattern(Vector jListPattern, Pattern iPattern) {
        int j;
        Pattern jPattern;
        PatternDiff patternDiff;
        int jLen = jListPattern.size();
        Set set = new TreeSet();

        j = 0;
        while (j < jLen) {
            jPattern = (Pattern) jListPattern.get(j);
            patternDiff = iPattern.compareVec(jPattern, Integer.MAX_VALUE);
            set.add(patternDiff);
            j++;
        }
        return set;
    }

    protected Vector undoEven(Pattern pattern, Map mapGuess, int simil) {
        Vector guessEven = new Vector(); // final list of results
        int len;
        Vector guessComb;  // list of combination of results
        Vector candComb;

        len = mapGuess.size();
        if (len > 1) {
            // generar lista de vectores diferencias entre los candidatos
            guessComb = calculateCombinations(mapGuess);
            // calcular la lista de vectores diferenciasn con los candidatos
            candComb = calculateCandidateCombinations(pattern, mapGuess);
            // buscar el más parecido
            lookForBestCandidate(guessComb, candComb,guessEven);
            // TODO: GET THE BEST
        } else {
            // si hay uno o cero!
            guessEven.add(mapGuess.keySet());
        }

        return guessEven;
    }


    private int lookForBestCandidate(Vector guess, Vector candidate, Vector vecCandidate) {

        Combination combGuess,combCand;
        int cValue;
        int c;
        int g;
        Set setG,setC;
        int simil;
        int minSimil = Integer.MAX_VALUE;
        int other;

        c = 0;
        while (c < candidate.size()) {
            combCand = (Combination) candidate.get(c);
            cValue = combCand.getFirstValue(); // the two values are equal
            setC = combCand.getSet();
            g = 0;
            while (g < guess.size()) {
                combGuess = (Combination) guess.get(g);
                if (combGuess.containsValue(cValue)) {
                    // then it make sense to do the comparison
                    setG = combGuess.getSet();
                    simil = compareSets(setC, setG);
                    if (simil < minSimil) {
                        vecCandidate.clear();
                        minSimil = simil;
                    }
                    if (simil == minSimil) {
                        other = combGuess.getOtherValue(cValue);
                        vecCandidate.add(new Integer(other));
                    }
                }
                g++;
            }
            c++;
        }

        return minSimil;
    }

    private int compareSets(Set setC, Set setG) {
        PatternDiff pdC,pdG;
        Iterator itC,itG;
        int minSimil=Integer.MAX_VALUE;
        int simil;

        itC = setC.iterator();
        while (itC.hasNext()) {
            pdC = (PatternDiff) itC.next();
            itG = setG.iterator();
            while (itG.hasNext()) {
                pdG = (PatternDiff) itG.next();
                simil=pdC.similarity(pdG,minSimil);
                if (simil<minSimil){
                    minSimil=simil;
                }
            }
        }

        return minSimil;
    }

    private Vector calculateCandidateCombinations(Pattern candidate, Map mapGuess) {
        Vector guessComb;
        Iterator itI;
        Integer digitI;
        Vector iVecPattern;
        int idigit;
        Set combI;
        Combination comb;

        guessComb = new Vector();
        itI = mapGuess.keySet().iterator();
        while (itI.hasNext()) {
            digitI = (Integer) itI.next();
            iVecPattern = (Vector) mapGuess.get(digitI);
            idigit = digitI.intValue();
            combI = combPattern(iVecPattern, candidate);
            comb = new Combination(idigit, idigit, combI);
            guessComb.add(comb);
        }
        return guessComb;
    }


    private Vector calculateCombinations(Map mapGuess) {
        Vector guessComb;
        Iterator itI,itJ;
        Integer digitI,digitJ;
        Vector iVecPattern,jVecPattern;
        int idigit,jdigit;
        Set combIJ;
        Combination comb;

        guessComb = new Vector();
        itI = mapGuess.keySet().iterator();
        while (itI.hasNext()) {
            digitI = (Integer) itI.next();
            iVecPattern = (Vector) mapGuess.get(digitI);
            idigit = digitI.intValue();
            itJ = mapGuess.keySet().iterator();
            while (itJ.hasNext()) {
                digitJ = (Integer) itJ.next();
                jdigit = digitJ.intValue();
                if (idigit < jdigit) {
                    // only if the two are different the comparison is done
                    //  i,j is equal to j,i
                    jVecPattern = (Vector) mapGuess.get(digitJ);
                    combIJ = combPatterns(iVecPattern, jVecPattern);
                    comb = new Combination(idigit, jdigit, combIJ);
                    guessComb.add(comb);
                }
            }
        }
        return guessComb;
    }


    public Map bestGuessesNoEven(Pattern pattern, DigitCriteria digitCriteria) {
        Digit digit;
        Vector vecSimil = null;
        int digitValue;
        int minDigit = -1;
        int similarity = Integer.MAX_VALUE;
        int simil;
        Map map = new TreeMap();

        Iterator it = digitMap_.values().iterator();

        while (it.hasNext()) {
            digit = (Digit) it.next();
            if (digitCriteria.makesCriteria(digit)) {
                digitValue = digit.getDigit();
                vecSimil = new Vector();
                // no se comprueba si el pattern esta contenido.
                simil = digit.similarityList(pattern, similarity, vecSimil);
                if (simil < similarity) {
                    similarity = simil;
                    minDigit = digitValue;
                    map.clear();
                }

                if (simil == similarity) {
                    map.put(new Integer(digitValue), vecSimil);
                }
            }
        }

        pattern.setDigit(minDigit, similarity);
        return map;
    }

    public Map bestGuesses(Map patternMap, DigitCriteria digitCriteria) {

        Map bgMap = new TreeMap();
        Object name;
        Pattern pattern;
        Vector bgVec;

        Iterator itMap = patternMap.keySet().iterator();

        while (itMap.hasNext()) {

            name = itMap.next();
            // Trace
            //System.out.println(name);
            pattern = (Pattern) patternMap.get(name);
            bgVec = bestGuesses(pattern, digitCriteria);
            bgMap.put(name, bgVec);

        }

        return bgMap;

    }

    public Vector bestGuesses(Pattern pattern, DigitCriteria digitCriteria) {
        SortedMap sm = new TreeMap();
        com.hp.dpp.icr.Digits.Digit digit;
        int digitValue;
        Iterator itSM;
        DigitSimil ds;
        Vector bgVector = new Vector();
        int similarity = Integer.MAX_VALUE - 1;
        int minSimil = similarity;
        int minDigit = -1;
        int distance;
        boolean found = false;
        Iterator itDigits = digitMap_.values().iterator();

        while (itDigits.hasNext() && (!found)) {

            digit = (com.hp.dpp.icr.Digits.Digit) itDigits.next();

            if (digitCriteria.makesCriteria(digit)) {
                digitValue = digit.getDigit();
                found = digit.contains(pattern);

                if (!found) {
                    distance = digit.similarity(pattern, similarity);
                } else {
                    distance = 0;
                }

                if (distance < minSimil) {
                    minSimil = distance;
                    minDigit = digitValue;
                    // todo: hacer clear y eliminar el bucle de despues
                    // todo: cambiar sm por bgVector
                    //sm.clear();
                }

                if (distance == minSimil) {
                    sm.put(new Integer(digitValue), new DigitSimil(digitValue, distance));
                }
            }
            // Trace
            //System.out.println(";D"+digitValue+";"+distance+";"+similarity);
        }

        // default assignation
        pattern.setDigit(minDigit, minSimil);

        itSM = sm.values().iterator();
        while (itSM.hasNext()) {

            ds = (DigitSimil) itSM.next();
            distance = ds.getSimil();
            // Trace
            //System.out.println(ds.getDigit()+","+distance);
            if (minSimil >= distance) {
                bgVector.add(ds);
            }

        }

        return bgVector;
    }

}
