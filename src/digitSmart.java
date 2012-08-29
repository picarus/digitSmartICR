
import com.hp.dpp.utils.Time;
import com.hp.dpp.icr.Pattern.*;
import com.hp.dpp.icr.exception.DigitException;

import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Date: Sep 30, 2003
 * Time: 10:33:27 PM
 */
public class digitSmart {

    protected static final String path = "c:\\java\\Development\\digitSmart\\" + PathInfo.digitSetPath_ + "\\test" + PathInfo.DIGITSET_EXT_;

    public static void main(String args[]) {
        Time.initTime();
        System.out.println("****************** PATTERN ERRORS **********************");

        int[] errors = {2};

        Pattern[] patternIndep = null;
        Pattern[] patternIndepRef = null;

        Pattern[] patternCoupled = null;
        Pattern[] patternCoupledRef = null;

        Pattern[] patternCoupledSize = null;
        Pattern[] patternCoupledSizeRef = null;

        Pattern[] patternCombined = null;
        Pattern[] patternCombinedRef = null;
        com.hp.dpp.icr.Digits.DigitSet digitSet;


        PatternCoupledSizeImpl.runErrors(Model.values, Model.all, Model.allCoupSize, errors);
        Time.calcTime();

        System.out.println("********************************************************");
        System.out.println("************************ INDEP PATTERNS ****************");
        System.out.println("********************************************************");

        patternIndep = PatternIndepImpl.run(Model.values, Model.all);
        Time.calcTime();
        // todo: pass as parameter the digit even if it is the model
        patternIndepRef = PatternIndepImpl.transform(Model.allDiff);
        Time.calcTime();
        viewResults(patternIndep, patternIndepRef);

        System.out.println("****************************************************************");
        System.out.println("********************* COUPLED PATTERNS *************************");
        System.out.println("****************************************************************");

        patternCoupled = PatternCoupledImpl.run(Model.values, Model.all);
        Time.calcTime();
        patternCoupledRef = PatternCoupledImpl.transform(Model.allCoup);
        Time.calcTime();
        viewResults(patternCoupled, patternCoupledRef);

        System.out.println("****************************************************************");
        System.out.println("********************* COMBINED PATTERNS ************************");
        System.out.println("****************************************************************");

        patternCombined = PatternCombinedImpl.run(Model.values, Model.all);
        Time.calcTime();
        patternCombinedRef = PatternCombinedImpl.transform(Model.allCoup);
        Time.calcTime();
        viewResults(patternCombined, patternCombinedRef);

        System.out.println("****************************************************************");
        System.out.println("********************* COUPLED SIZE PATTERNS ********************");
        System.out.println("****************************************************************");

        patternCoupledSize = PatternCoupledSizeImpl.run(Model.values, Model.all);
        Time.calcTime();
        patternCoupledSizeRef = PatternCoupledSizeImpl.transform(Model.allCoupSize);
        Time.calcTime();
        viewResults(patternCoupledSize, patternCoupledSizeRef);

        try {
            digitSet = createDigitSet(patternCoupledSize);
            digitSet.save(new FileOutputStream(path));
        } catch (DigitException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected static com.hp.dpp.icr.Digits.DigitSet createDigitSet(Pattern[] patternCoupledSize) throws DigitException {
        com.hp.dpp.icr.Digits.DigitSet digitSet = new com.hp.dpp.icr.Digits.DigitSet();
        digitSet.addPattern(patternCoupledSize);
        return digitSet;
    }

    private static void viewResults(Pattern[] pattern, Pattern[] patternRef) {
        PatternView.validate(pattern, patternRef);
        Time.calcTime();
        PatternView.showInterDifferences(pattern);
        Time.calcTime();
        PatternView.showInterSimiliarity(pattern);
        Time.calcTime();
    }


}
