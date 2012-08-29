
import com.hp.dpp.sdk.Form;
import com.hp.dpp.sdk.exception.AreaException;
import com.hp.dpp.sdk.exception.XFDFException;
import com.hp.dpp.sdk.exception.NoSuchAreaException;
import com.hp.dpp.icr.Pattern.Pattern;
import com.hp.dpp.icr.Pattern.PatternImpl;
import com.hp.dpp.icr.ChangeStroke.ChangeStrokeImpl;
import com.hp.dpp.icr.exception.DigitException;
import com.hp.dpp.icr.exception.ValidationException;
import com.hp.dpp.icr.Validation.Statistics;
import com.hp.dpp.icr.Validation.Validation;

import java.io.*;
import java.util.Vector;
import java.util.Map;

/**
 * Date: Nov 2, 2003
 * Time: 9:29:24 PM
 */
public class TrainWithPattern extends TrainImpl implements Process {

    TrainWithPattern(Validation validation) {
        super(validation);
        processID_="WP";
    }

    // old version
    Statistics processValues(Statistics statistics, Form form, Pattern[] patternRef, String fieldsTemplate, int[] validSequence) throws AreaException {
        Vector vecFields;
        vecFields = ASHTraining.getFilledFields(form, fieldsTemplate);
        Map values = ASHTraining.getPatterns(vecFields);
        PatternImpl.guess(values, patternRef);
        try {
            validation_.validate(statistics, values, validSequence);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        ASHTraining.setPrefillValues(form, values);
        return statistics;
    }

    protected Statistics processForm(Form form) throws AreaException {
        Statistics statistics = new Statistics();
        File file;
        Map trainingMap;
        Vector vecFieldsTableG;
        Pattern[] patternRef;

        try {
            vecFieldsTableG = ASHTraining.getFilledFields(form, ASHBase.FIELDSTABLE_);
            trainingMap = ASHTraining.getPatternsRef(vecFieldsTableG);
            ASHTraining.setPrefillValues(form, trainingMap);
            patternRef = saveDigitSet(trainingMap, form);
            //PatternView.show(training);
            //PatternView.showInterSimiliarity(training);
            ASHTraining.showPatterns(trainingMap, vecFieldsTableG, form);
            // values
            statistics = processValues(statistics, form, patternRef, ASHBase.FIELDSINPUT_, Validation.idInput_);
            statistics = processValues(statistics, form, patternRef, ASHBase.FIELDSID_, Validation.idValue_);
            // characterize DigitSet
            // showDigitSet(form, digitSet);
            // recognition
            ASHTraining.setRecognitionResults(form, statistics);
            // export xfdf
            file = new File(ASHBase.rootPath_ + File.separator + PathInfo.resultsPath_ + File.separator + form.getSendBoxPage() + "P" + ChangeStrokeImpl.getFactor() + PathInfo.XFDF_EXT_);
            form.exportPrefillValuesXFDF(file);
            file = null;
        } catch (XFDFException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DigitException e) {
            e.printStackTrace();
        } catch (NoSuchAreaException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return statistics;
    }


    private static Pattern[] saveDigitSet(Map trainingMap, Form form) throws DigitException {
        Pattern[] training;
        training = ASHTraining.vectorToPatternArray(trainingMap.values().toArray());
        ASHTraining.generateDigitSet(training, form);
        return training;
    }

}
