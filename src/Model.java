
import com.hp.dpp.icr.ChangeStroke.ChangeStrokeImpl;

/**
 * Date: Oct 11, 2003
 * Time: 2:34:37 PM
 */
public class Model {

    public static final int F = ChangeStrokeImpl.getFactor();
    public static final int F2 = F / 2;
    public static final int F3 = F / 3;
    public static final int F4 = F / 4;
    // has a set of patterns plus a numeric value from 0 to 9
    // and also a set of ChangeStrokeIndep
    static int[][][] zero = {{{5, 0, 5, 10, 5}, {0, 5, 10, 5, 0}}}; // rombe
    static int[][][] zeroDiff = {{{-1, 1, -1}, {1, -1}}};
    static int[][][] zeroCoup = {{{-1, 1, 1, -1}, {1, 1, -1, -1}}};
    static int[][][] zeroCoupSize = {{{-F2, F2, F2, -F2}, {F2, F2, -F2, -F2}}};
    static int[][] zeroComb = {{-14, 21, -15, 10}}; //{{-2, 3, 3, -2}, {7, 7, -5, -5}};

    static int[][][] zero2 = {{{5, 0, 0, 5, 5}, {0, 0, 10, 10, 0}}};  // rectangle
    static int[][][] zero2Diff = {{{-1, 1}, {1, -1}}};
    static int[][][] zero2Coup = {{{-1, -1, 1, 1}, {0, 1, 1, -1}}};
    static int[][][] zero2CoupSize = {{{-F, 0, F, 0}, {0, F, 0, -F}}};
    static int[][] zero2Comb = {{-2, -14, 21, -15}}; //{{-2, -2, 3, 3}, {1, 7, 7, -5}};

    static int[][][] one = {{{0, 5, 5, 0, 10}, {5, 0, 10, 10, 10}}};
    static int[][][] oneDiff = {{{1, -1, 1}, {-1, 1}}};
    static int[][][] oneCoup = {{{1, 1, -1, 1}, {-1, 1, 1, 1}}};
    static int[][][] oneCoupSize = {{{F2, 0, -F2, F}, {-F2, F, 0, 0}}};
    static int[][] oneComb = {{-15, 21, -14, 21}}; //{{3, 3, -2, 3}, {-5, 7, 7, 7}};

    static int[][][] one2 = {{{0, 5, 5}, {5, 0, 10}}};
    static int[][][] one2Diff = {{{1}, {-1, 1}}};
    static int[][][] one2Coup = {{{1, 1}, {-1, 1}}};
    static int[][][] one2CoupSize = {{{F, 0}, {-F2, F}}};
    static int[][] one2Comb = {{-15, 21}};//{{3, 3}, {-5, 7}};

    static int[][][] one3 = {{{0, 5, 5}, {5, 0, 10}}, {{0, 5}, {10, 10}}};
    static int[][][] one3Diff = {{{1}, {-1, 1}}, {{1}, {}}};
    static int[][][] one3Coup = {{{1, 1}, {-1, 1}}, {{1}, {0}}};
    static int[][][] one3CoupSize = {{{F, 0}, {-F2, F}}, {{F}, {0}}};
    static int[][] one3Comb = {{-15, 21}, {3}};//{{{3, 3}, {-5, 7}}, {{3}, {1}}};

    static int[][][] two = {{{0, 5, 5, 0, 0, 5}, {0, 0, 5, 5, 10, 10}}};
    static int[][][] twoDiff = {{{1, -1, 1}, {1}}};
    static int[][][] twoCoup = {{{1, 1, -1, 1}, {0, 1, 1, 1}}};
    static int[][][] twoCoupSize = {{{F, 0, -F, 0, F}, {0, F2, 0, F2, 0}}};
    static int[][] twoComb = {{3, 21, -14, 21}};//{{{3, 3, -2, 3}, {1, 7, 7, 7}}};

    static int[][][] three = {{{0, 5, 5, 0, 5, 5, 0}, {0, 0, 5, 5, 5, 10, 10}}};
    static int[][][] threeDiff = {{{1, -1, 1, -1}, {1}}};
    static int[][][] threeCoup = {{{1, 1, -1, 1, -1}, {0, 1, 1, 1, 1}}};
    static int[][][] threeCoupSize = {{{F, 0, -F, F, 0, -F}, {0, F2, 0, 0, F2, 0}}};
    static int[][] threeComb = {{3, 21, -14, 21, -14}};//{{{3, 3, -2, 3, -2}, {1, 7, 7, 7, 7}}};

    static int[][][] four = {{{0, 0, 5, 5, 5}, {0, 5, 5, 0, 10}}};
    static int[][][] fourDiff = {{{1}, {1, -1, 1}}};
    static int[][][] fourCoup = {{{0, 1, 1, 1}, {1, 1, -1, 1}}};
    static int[][][] fourCoupSize = {{{0, F, 0, 0}, {F2, 0, -F2, F}}};
    static int[][] fourComb = {{7, 21, -15, 21}};//{{{1, 3, 3, 3}, {7, 7, -5, 7}}};

    static int[][][] four2 = {{{0, 0, 5}, {0, 5, 5}}, {{5, 5}, {0, 10}}};
    static int[][][] four2Diff = {{{1}, {1}}, {{}, {1}}};
    static int[][][] four2Coup = {{{0, 1}, {1, 1}}, {{0}, {1}}};
    static int[][][] four2CoupSize = {{{0, F}, {F2, 0}}, {{0}, {F}}};
    static int[][] four2Comb = {{7, 21}, {7}};//{{{1, 3}, {7, 7}}, {{1}, {7}}};

    static int[][][] five = {{{5, 0, 0, 5, 5, 0}, {0, 0, 5, 5, 10, 10}}};
    static int[][][] fiveDiff = {{{-1, 1, -1}, {1}}};
    static int[][][] fiveCoup = {{{-1, -1, 1, -1}, {0, 1, 1, 1}}};
    static int[][][] fiveCoupSize = {{{-F, 0, F, 0, -F}, {0, F2, 0, F2, 0}}};
    static int[][] fiveComb = {{-2, 14, 21, -14}};//{{{-2, -2, 3, -2}, {1, 7, 7, 7}}};

    static int[][][] five2 = {{{0, 0, 5, 5, 0}, {0, 5, 5, 10, 10}}, {{0, 5}, {0, 0}}};
    static int[][][] five2Diff = {{{1, -1}, {1}}, {{1}, {}}};
    static int[][][] five2Coup = {{{0, 1, -1}, {1, 1, 1}}, {{1}, {0}}};
    static int[][][] five2CoupSize = {{{0, F, 0, -F}, {F2, 0, F2, 0}}, {{F}, {0}}};
    static int[][] five2Comb = {{7, 21, -14}, {3}};//{{{1, 3, -2}, {7, 7, 7}}, {{3}, {1}}};

    static int[][][] six = {{{5, 0, 0, 5, 5, 0}, {0, 0, 10, 10, 5, 5}}};
    static int[][][] sixDiff = {{{-1, 1, -1}, {1, -1}}};
    static int[][][] sixCoup = {{{-1, -1, 1, 1, -1}, {0, 1, 1, -1, -1}}};
    static int[][][] sixCoupSize = {{{-F, 0, F, 0, -F}, {0, F, 0, -F2, 0}}};
    static int[][] sixComb = {{-2, -14, 21, -15, 10}};//{{{-2, -2, 3, 3, -2}, {1, 7, 7, -5, -5}}};

    static int[][][] seven = {{{0, 5, 0}, {0, 0, 10}}};
    static int[][][] sevenDiff = {{{1, -1}, {1}}};
    static int[][][] sevenCoup = {{{1, -1}, {0, 1}}};
    static int[][][] sevenCoupSize = {{{F, -F}, {0, F}}};
    static int[][] sevenComb = {{3, -14}};//{{{3, -2}, {1, 7}}};

    static int[][][] seven2 = {{{0, 5, 5}, {0, 0, 10}}};
    static int[][][] seven2Diff = {{{1}, {1}}};
    static int[][][] seven2Coup = {{{1, 1}, {0, 1}}};
    static int[][][] seven2CoupSize = {{{F, 0}, {0, F}}};
    static int[][] seven2Comb = {{3, 21}};//{{{3, 3}, {1, 7}}};

    static int[][][] seven3 = {{{0, 5, 0}, {0, 0, 10}}, {{0, 5}, {5, 5}}};
    static int[][][] seven3Diff = {{{1, -1}, {1}}, {{1}, {}}};
    static int[][][] seven3Coup = {{{1, -1}, {0, 1}}, {{1}, {0}}};
    static int[][][] seven3CoupSize = {{{F, -F}, {0, F}}, {{F}, {0}}};
    static int[][] seven3Comb = {{3, -14}, {3}};//{{{3, -2}, {1, 7}}, {{3}, {1}}};

    static int[][][] eight = {{{0, 5, 5, 0, 0, 5, 5, 0, 0}, {0, 0, 5, 5, 10, 10, 5, 5, 0}}};
    static int[][][] eightDiff = {{{1, -1, 1, -1}, {1, -1}}};
    static int[][][] eightCoup = {{{1, 1, -1, 1, 1, -1}, {0, 1, 1, 1, -1, -1}}};
    static int[][][] eightCoupSize = {{{F, 0, -F, 0, F, 0, -F, 0}, {0, F2, 0, F2, 0, -F2, 0, -F2}}};
    static int[][] eightComb = {{3, 21, -14, 21, -15, 10}};//{{{3, 3, -2, 3, 3, -2}, {1, 7, 7, 7, -5, -5}}};

    static int[][][] nine = {{{5, 0, 0, 5, 5, 5}, {0, 0, 5, 5, 0, 10}}};
    static int[][][] nineDiff = {{{-1, 1}, {1, -1, 1}}};
    static int[][][] nineCoup = {{{-1, -1, 1, 1, 1}, {0, 1, 1, -1, 1}}};
    static int[][][] nineCoupSize = {{{-F, 0, F, 0, 0}, {0, F2, 0, -F2, F}}};
    static int[][] nineComb = {{-2, -14, 21, -15, 21}};//{{{-2, -2, 3, 3, 3}, {1, 7, 7, -5, 7}}};

    static int[] values = {0, 0, 1, 1, 1, 2, 3, 4, 4, 5, 5, 6, 7, 7, 7, 8, 9};
    static int[][][][] all = {zero, zero2,  one, one2, one3, two, three, four, four2, five, five2, six, seven, seven2, seven3, eight, nine};
    static int[][][][] allDiff = {zeroDiff, zero2Diff,  oneDiff, one2Diff, one3Diff, twoDiff, threeDiff, fourDiff, four2Diff, fiveDiff, five2Diff, sixDiff, sevenDiff, seven2Diff, seven3Diff, eightDiff, nineDiff};
    static int[][][][] allCoup = {zeroCoup, zero2Coup,  oneCoup, one2Coup, one3Coup, twoCoup, threeCoup, fourCoup, four2Coup, fiveCoup, five2Coup, sixCoup, sevenCoup, seven2Coup, seven3Coup, eightCoup, nineCoup};
    static int[][][] allComb = {zeroComb, zero2Comb,  oneComb, one2Comb, one3Comb, twoComb, threeComb, fourComb, four2Comb, fiveComb, five2Comb, sixComb, sevenComb, seven2Comb, seven3Comb, eightComb, nineComb};
    static int[][][][] allCoupSize = {zeroCoupSize, zero2CoupSize,  oneCoupSize, one2CoupSize, one3CoupSize, twoCoupSize, threeCoupSize, fourCoupSize, four2CoupSize, fiveCoupSize, five2CoupSize, sixCoupSize, sevenCoupSize, seven2CoupSize, seven3CoupSize, eightCoupSize, nineCoupSize};
    static int[][][][][] digits = {{zero, zero2}, {one, one2, one3}, {two}, {three}, {four, four2}, {five, five2}, {six}, {seven, seven2, seven3}, {eight}, {nine}};

}
