package com.hp.dpp.icr.Digits;

/**
 * Date: Oct 2, 2003
 * Time: 10:27:31 PM
 */

// todo: problemas con digitos pequeños hoja 1138
// todo: landscape/portrait / rotation invariant use angles
// todo: test scale
// todo: show in html
// todo: order of the strokes  matter/not matter
// todo: html form to train engine
// todo: comprimir el patron si dos cambios consecutivos se producen muy cercanos
// todo: consider the initial position/ final position
// todo: imagen con los puntos de los samples
// todo: changeStrokeIndepSize
// todo: review how file paths are created
/* todo: check if adding a pattern makes the DigitSet inconsistent
       ( the pattern is repited in other Digits than the one is added to)
*/
// todo: investigate comparable / comparator ( Java interface )
// todo: consider the relative position of the different strokes (offset is null sometimes!!!)
// todo: order the strokes so that not all of them needs to be checked
// todo: patternRef so that certain patterns can be discarded if they don´t add information
// todo: compare when the number of strokes it´s different
// todo: ordenar los strokes
// todo: no añadir todos los patterns al digitSet ( solo los representativos ),
//       los no representativos guardarlos por si en algun momento son necesarios
// todo: generar la imagen solo del pattern de referencia
// todo: update DigitAnalisys (pattern, int digit)
////////////////////////////////////////////////////////////////////////////////////////////////////////
// TODO: similarity between digits(TEST)
// TODO: en caso de empate comparar el patron diferencia entre los candidatos con la diferencia entre el patron y el candidato
// TODO: calculate time: generate pattern / compare training set
// TODO: ordenar segun el uso para validar dentro del digit
// TODO: cross recognition
// TODO: compare with Model ( theoretical )
// TODO: use float instead of int
// TODO: run comparison backwards
// TODO: D: como deshacer empates
// TODO: D: como hacer metodo independiente de tamaño
// TODO: D: caracterizacion de los patterns
// TODO: D: seleccion de patterns a incluir como referencia??
//////////////////////////////////////////////////////////////////////////////////////////////////////////

import com.hp.dpp.icr.Pattern.Pattern;
import com.hp.dpp.icr.exception.DigitNotMatchException;

import java.util.Vector;
import java.io.*;

public class Digit implements Serializable {

    protected Vector vectorPattern_;
    protected int digit_;

    public Digit(int digit) {
        init(digit);
    }

    public Digit(int digit, Pattern[] pattern) throws DigitNotMatchException {
        init(digit);
        addPattern(pattern);
    }

    public Digit(int digit, Pattern pattern) throws DigitNotMatchException {
        init(digit);
        addPattern(pattern);
    }

    protected void init(int digit) {
        vectorPattern_ = new Vector();
        digit_ = digit;
    }

    public int getDigit() {
        return digit_;
    }

    public boolean addPattern(Pattern pattern) throws DigitNotMatchException {
        boolean added = false;
        if (pattern.getDigit() == digit_) {
            if (!contains(pattern)) {
                vectorPattern_.add(pattern);
                added = true;
            }
        } else {
            throw new DigitNotMatchException(pattern.getDigit());
        }
        return added;
    }

    public boolean contains(Pattern pattern) {
        Pattern curPattern;
        boolean found = false;
        int len = vectorPattern_.size();
        int i = 0;

        while (!found & (i < len)) {
            curPattern = (Pattern) vectorPattern_.get(i);
            found = pattern.equals(curPattern);
            i++;
        }

        return found;
    }

    public void addPattern(Pattern[] pattern) throws DigitNotMatchException {
        for (int i = 0; i < pattern.length; i++) {
            addPattern(pattern[i]);
        }
    }

    public int getPatternNumber() {
        return vectorPattern_.size();
    }

    public int similarityList(Pattern pattern, int prune, Vector vecSimil) {
        int simil = prune + 1;
        int curSimil = -1;
        Pattern curPattern = null;
        int i = 0;
        int len = vectorPattern_.size();
        while (i < len) {
            curPattern = (Pattern) vectorPattern_.get(i);
            curSimil = pattern.similarity(curPattern, prune);
            // trace
            // System.out.print(";"+(simil+1));
            if (curSimil < simil) {
                simil = curSimil;
                vecSimil.clear();
                vecSimil.add(curPattern);
            } else if (curSimil == simil) {
                vecSimil.add(curPattern);
            }

            i++;
        }
        return simil;
    }

    public int similarity(Pattern pattern, int prune) {
        int simil = prune + 1;
        int curSimil = -1;
        Pattern curPattern = null;
        int i = 0;
        int len = vectorPattern_.size();
        while (i < len) {
            curPattern = (Pattern) vectorPattern_.get(i);
            curSimil = pattern.similarity(curPattern, prune);
            // trace
            // System.out.print(";"+(simil+1));
            if (curSimil < simil) {
                simil = curSimil;
            }
            i++;
        }
        return simil;
    }

}
