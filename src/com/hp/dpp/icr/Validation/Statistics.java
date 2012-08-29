package com.hp.dpp.icr.Validation;

/**
 * Date: Nov 2, 2003
 * Time: 3:05:39 PM
 */
public class Statistics {
    static final int N_ELEMS_ = 10;
    float total_;
    float correct_;
    int[][] errors_ = new int[N_ELEMS_+1][N_ELEMS_];
    int[] oks_ = new int[N_ELEMS_];

    public Statistics() {
        total_ = 0;
        correct_ = 0;
        init();
    }

    void init() {
        int i = 0;
        setToZero(oks_);
        while (i < errors_.length) {
            setToZero(errors_[i]);
            i++;
        }
    }

    protected void setToZero(int[] array) {
        int i = 0;
        while (i < array.length) {
            array[i] = 0;
            i++;
        }
    }

    public float getTotal_() {
        return total_;
    }

     public float getCorrect() {
        return correct_;
    }

    void ok(int goodValue) {
        oks_[goodValue]++;
        ok();
    }

    void ok() {
        total_ += 1;
        correct_ += 1;
    }

    void update(float correct, float total) {
        correct_ += correct;
        total_ += total;
    }

    void fail() {
        total_ += 1;
    }

    void fail(int goodValue, int guessValue) {
        if (guessValue==-1){
          guessValue=10;
        }
        errors_[guessValue][goodValue]++;

        fail();
    }

     public boolean isOK(int goodValue, int guessValue) {
        boolean result = (goodValue == guessValue);
        if (result) {
            ok(goodValue);
        } else {
            fail(goodValue, guessValue);
        }
        return result;
    }

     public int[][] getErrors() {
        return errors_;
    }

     public int[] getOKs() {
        return oks_;
    }

    private void addOKs(int[] oks) {
        int j = 0;
        while (j < oks.length) {
            oks_[j] += oks[j];
            j++;
        }

    }

    private void addErrors(int[][] errors) {
        int j;
        int i = 0;
        while (i < errors.length) {
            j = 0;
            while (j < errors[i].length) {
                errors_[i][j] += errors[i][j];
                j++;
            }
            i++;
        }
    }

     public void importStatistics(Statistics stats) {
        int[] oks;
        int[][] errors;

        total_ += stats.getTotal_();
        correct_ += stats.getCorrect();
        oks = stats.getOKs();
        errors = stats.getErrors();

        addOKs(oks);
        addErrors(errors);
    }

    /*
    boolean isOK(boolean result) {
        if (result) {
            ok();
        } else {
            fail();
        }

        return result;
    }
    */
     public String percentage() {
        float percentage;
        String percStr = null;
        if (total_ > 0) {
            percentage = correct_ * 100 / total_;
        } else {
            percentage = 0;
        }
        percStr = String.valueOf(percentage);
        return percStr;
    }

    public String okOutOfTotal() {
        return correct_ + "/" + total_;
    }
}
