package com.hp.dpp.icr.Pattern;

import java.text.DecimalFormat;

/**
 * Date: Oct 30, 2003
 * Time: 9:19:52 PM
 */
public class PatternView {

    static final int DEC_FORMAT=3;
    static DecimalFormat nf;

    static {
        nf = new DecimalFormat();
        nf.setMinimumIntegerDigits(DEC_FORMAT);
        nf.setPositivePrefix("+");
    }

    public static void showInterDifferences(Pattern[] pattern) {
        int len = pattern.length;
        int diff[][];
        System.out.println("****************** INTER-PATTERNS DIFFERENCES***********************");
        System.out.print(" ");
        for (int i = 0; i < len - 1; i++) {
            System.out.print("|    " + pattern[i].getDigit() + "  ");
        }
        System.out.println("| ");
        for (int j = 1; j < len; j++) {
            System.out.print(pattern[j].getDigit() + ":");
            for (int k = 0; k < j; k++) {
                diff = pattern[j].lengthCompare(pattern[k], Integer.MAX_VALUE);
                System.out.print(toString(diff) + "  |");
            }
            for (int k = j; k < len - 1; k++) {
                System.out.print("        |");
            }
            System.out.println();
        }

    }

    public static void showInterSimiliarity(Pattern[] pattern) {
        int len = pattern.length;
        int diff[][];
        int simil;
        int minSimil[] = new int[len];
        int maxSimil[] = new int[len];
        int whoMin[] = new int[len];
        int whoMax[] = new int[len];
        int digitJ,digitK;
        System.out.println("****************** INTER-PATTERNS SIMILARITY***********************");
        System.out.print("   ");
        for (int i = 0; i < len; i++) {
            System.out.print("|  " + nf.format(pattern[i].getDigit()) + "  ");
            minSimil[i] = Integer.MAX_VALUE;
            maxSimil[i] = -1;
        }
        System.out.println("|");
        for (int j = 1; j < len; j++) {
            digitJ = pattern[j].getDigit();
            System.out.print(" " + digitJ + " :");

            for (int k = 0; k < j; k++) {
                digitK = pattern[k].getDigit();

                diff = pattern[j].lengthCompare(pattern[k], Math.max(minSimil[j], minSimil[k]));
                simil = PatternImpl.similarity(diff);
                if (digitJ != digitK) {
                    if (simil < minSimil[j]) {
                        minSimil[j] = simil;
                        whoMin[j] = digitK;
                    }
                    if (simil < minSimil[k]) {
                        minSimil[k] = simil;
                        whoMin[k] = digitJ;
                    }
                } else if (simil < 99) {
                    if (simil > maxSimil[j]) {
                        maxSimil[j] = simil;
                        whoMax[j] = k;
                    }
                    if (simil > maxSimil[k]) {
                        maxSimil[k] = simil;
                        whoMax[k] = j;
                    }
                }
                System.out.print("  " + nf.format(simil) + "  |");
            }
            for (int k = j; k < len; k++) {
                System.out.print("        |"); // todo :parametrizar en funcion de nf
            }
            System.out.println();
        }

        System.out.print("MIN:");
        for (int i = 0; i < len; i++) {
            System.out.print("  " + nf.format(minSimil[i]) + "  |");
        }
        System.out.println();
        System.out.print("WHO:");
        for (int i = 0; i < len; i++) {
            System.out.print("  " + nf.format(whoMin[i]) + "  |");
        }
        System.out.println();

        System.out.print("MAX:");
        for (int i = 0; i < len; i++) {
            System.out.print("  " + nf.format(maxSimil[i]) + "  |");
        }
        System.out.println();
        System.out.print("POS:");
        for (int i = 0; i < len; i++) {
            System.out.print("  " + nf.format(whoMax[i]) + "  |");
        }
        System.out.println();

    }


    public static void validate(Pattern[] pattern, Pattern[] patternRef) {
        int[][] diff;
        int len = pattern.length;
        System.out.println("********************* PATTERNS VALIDATION ***********************");

        for (int m = 0; m < len; m++) {
            System.out.print(pattern[m].getDigit() + ":");
            diff = pattern[m].lengthCompare(patternRef[m], Integer.MAX_VALUE);
            System.out.println(toString(diff) + "|");
        }
    }

    public static void show(Pattern[] pattern) {
        for (int i = 0; i < pattern.length; i++) {
            System.out.println(pattern[i].getDigit() + ":L:" + pattern[i].lengthToString() + ";P:" + pattern[i].toString());
        }
    }

    public static String toString(int[][] diff) {
        String str = "";
        for (int i = 0; i < diff.length; i++) {
            str += "[";
            for (int j = 0; j < diff[i].length; j++) {
                if (j != 0) {
                    str += ",";
                }
                str += diff[i][j];
            }
            str += "]";
        }
        return str;
    }

    public static void compareArrays(Pattern[] patternHor, Pattern[] patternVer) {
        int lenHor = patternHor.length;
        int lenVer = patternVer.length;
        int diff[][];
        int simil;
        int horSimil;
        int who = -1;
        int minSimil[] = new int[lenHor];
        int whoSimil[] = new int[lenHor];
        System.out.println("****************** PATTERN SIMILARITY***********************");
        System.out.print("   ");
        for (int i = 0; i < lenHor; i++) {
            System.out.print("|  " + nf.format(patternHor[i].getDigit()) + "  ");
            minSimil[i] = Integer.MAX_VALUE;
        }
        System.out.println("|MIN |WHO |");
        for (int j = 0; j < lenVer; j++) {
            System.out.print(" " + patternVer[j].getDigit() + " :");
            horSimil = Integer.MAX_VALUE;
            who = -1;
            for (int k = 0; k < lenHor; k++) {
                diff = patternVer[j].lengthCompare(patternHor[k], Math.max(horSimil, minSimil[k]));
                simil = PatternImpl.similarity(diff);
                if (simil < minSimil[k]) {
                    minSimil[k] = simil;
                    whoSimil[k] = patternVer[j].getDigit();
                }
                if (simil < horSimil) {
                    horSimil = simil;
                    who = patternHor[k].getDigit();
                }
                System.out.print("  " + nf.format(simil) + "  |");
            }

            System.out.println(" " + nf.format(horSimil) + " | " + nf.format(who) + " |");
        }

        System.out.print("MIN:");
        for (int i = 0; i < lenHor; i++) {
            System.out.print("  " + nf.format(minSimil[i]) + "  |");
        }
        System.out.print("WHO:");
        System.out.println();
        for (int i = 0; i < lenHor; i++) {
            System.out.print("  " + nf.format(whoSimil[i]) + "  |");
        }
        System.out.println();

    }


}
