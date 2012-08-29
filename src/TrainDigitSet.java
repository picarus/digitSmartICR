
import com.hp.dpp.icr.ChangeStroke.ChangeStrokeImpl;
import com.hp.dpp.icr.DigitSet;
import com.hp.dpp.icr.Validation.Statistics;
import com.hp.dpp.icr.Validation.Validation;
import com.hp.dpp.icr.exception.DigitException;
import com.hp.dpp.icr.exception.ValidationException;
import com.hp.dpp.sdk.Form;
import com.hp.dpp.sdk.exception.AreaException;
import com.hp.dpp.sdk.exception.XFDFException;
import com.hp.dpp.sdk.exception.NoSuchAreaException;

import java.io.*;
import java.util.Vector;
import java.util.Map;

/**
 * Date: Nov 2, 2003
 * Time: 3:02:42 PM
 */
public class TrainDigitSet extends TrainImpl implements Process {

    TrainDigitSet(Validation validation) {
        super(validation);
        processID_="DS";
    }

    //  new version using DigitSet
    Statistics processValues(Statistics statistics, Form form, com.hp.dpp.icr.Digits.DigitSet digitSet, String fieldsTemplate, int[] validSequence) throws AreaException {
        Vector vecFields;
        vecFields = ASHTraining.getFilledFields(form, fieldsTemplate);
        Map values = ASHTraining.getPatterns(vecFields);
        validation_.guess(digitSet,values);
        try {
            validation_.validate(statistics, values, validSequence);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        ASHTraining.setPrefillValues(form, values);
        return statistics;
    }

    protected Statistics processForm(Form form ) throws AreaException{
        return processForm(form,ASHBase.FIELDSTABLE_,ASHBase.FIELDSINPUT_);
    }

    protected Statistics processForm( Form form, String refFields, String comFields) throws AreaException {
        Statistics statistics = new Statistics();
        File file;
        Map trainingMap;
        com.hp.dpp.icr.Digits.DigitSet digitSet;
        Vector vecFieldsTableG;

        try {
            vecFieldsTableG = ASHTraining.getFilledFields(form, refFields);
            trainingMap = ASHTraining.getPatternsRef(vecFieldsTableG);
            digitSet = ASHBase.createAndSaveDigitSet(trainingMap, form);

            ASHTraining.setPrefillValues(form, trainingMap);
            // Trace
            //PatternView.show(training);
            // Trace
            //PatternView.showInterSimiliarity(training);
            ASHTraining.showPatterns(trainingMap, vecFieldsTableG, form);
            // values
            statistics = processValues(statistics, form, digitSet, comFields, Validation.idInput_);
            statistics = processValues(statistics, form, digitSet, ASHBase.FIELDSID_, Validation.idValue_);
            // characterize DigitSet
            ASHTraining.showDigitSet(form, digitSet);
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

}
