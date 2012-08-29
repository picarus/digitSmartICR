
import com.hp.dpp.icr.DigitSet;
import com.hp.dpp.icr.Validation.Statistics;
import com.hp.dpp.icr.Validation.Validation;
import com.hp.dpp.icr.exception.ValidationException;
import com.hp.dpp.sdk.Form;
import com.hp.dpp.sdk.exception.AreaException;
import com.hp.dpp.sdk.exception.NoSuchAreaException;

import java.io.*;
import java.util.Vector;
import java.util.Map;

/**
 * Date: Nov 2, 2003
 * Time: 4:44:48 PM
 */
public class Recognize extends ProcessImpl implements Process {

    Recognize(Validation validation) {
        super(validation);
        processID_ = "CR";
    }

    Recognize(int factor, PrintStream printStream) {
        super(factor, printStream);
    }

    Statistics processValues(Statistics statistics, Form form, com.hp.dpp.icr.Digits.DigitSet digitSet, String fieldsTemplate, int[] validSequence) throws AreaException {
        Vector vecFields;
        vecFields = ASHTraining.getFilledFields(form, fieldsTemplate);
        Map values = ASHTraining.getPatterns(vecFields);
        validation_.guess(digitSet, values);
        try {
            validation_.validate(statistics, values, validSequence);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        ASHTraining.setPrefillValues(form, values);
        return statistics;
    }

    protected Statistics processForm(Form form, com.hp.dpp.icr.Digits.DigitSet digitSet) throws AreaException {
        Statistics statistics = new Statistics();
        try {
            // values
            statistics = processValues(statistics, form, digitSet, ASHBase.FIELDSINPUT_, Validation.idInput_);
            statistics = processValues(statistics, form, digitSet, ASHBase.FIELDSID_, Validation.idValue_);
            statistics = processValues(statistics, form, digitSet, ASHBase.FIELDSTABLE_, Validation.idInput_);
            // recognition
            ASHTraining.setRecognitionResults(form, statistics);
            // export xfdf
            // todo: export results individually ( name of the file ? )
            /*
            file = new File(ASHBase.rootPath_ + File.separator + PathInfo.resultsPath_ + File.separator + form.getSendBoxPage() + "R" + ChangeStrokeImpl.FACTOR + PathInfo.XFDF_EXT_);
            form.exportPrefillValuesXFDF(file);
            file = null;

        } catch (XFDFException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            */
        } catch (NoSuchAreaException e) {
            e.printStackTrace();
        }
        return statistics;
    }

    public Statistics process() {
        Form formI = null, formJ = null;
        FilenameFilter filter = new ASHBase.FilterByExtension(PathInfo.RQS_EXT_);
        File[] fileArray;
        File file;
        String path;
        Statistics lastStatistics = null;
        Statistics statistics;
        int i,j;
        File folder;
        String address;
        com.hp.dpp.icr.Digits.DigitSet digitSet = null;

        statistics = new Statistics();
        path = ASHBase.rootPath_ + PathInfo.trainingPath_;
        folder = new File(path);
        fileArray = folder.listFiles(filter);

        try {
            i = 0;
            while (i < fileArray.length) {
                j = 0;
                formI = ASHTraining.readForm(fileArray[i]);
                digitSet = ASHBase.createDigitSet(formI);
                ASHBase.mergeImage(formI);
                address = formI.getSendBoxPage();
                while (j < fileArray.length) {
                    if (i != j) {
                        formJ = ASHTraining.readForm(fileArray[j]);
                        lastStatistics = processForm(formJ, digitSet);
                        showResultsLastFile(lastStatistics, fileArray[j].getName());
                        statistics.importStatistics(lastStatistics);
                    }
                    j++;
                }
                if (lastStatistics != null) {
                    ASHTraining.exportStatistics(formI, lastStatistics);
                    file = new File(ASHBase.rootPath_ + File.separator + PathInfo.statisticsPath_ + File.separator + PathInfo.statsFile_ + getValidationID() + getFactor() + "_" + address + PathInfo.XFDF_EXT_);
                    formI.exportPrefillValuesXFDF(file);
                }
                i++;
            }
            ASHTraining.exportStatistics(formI, lastStatistics);
            file = new File(ASHBase.rootPath_ + File.separator + PathInfo.statisticsPath_ + File.separator + PathInfo.statsFile_ + getValidationID() + getFactor() + getProcessID() + PathInfo.XFDF_EXT_);
            formI.exportPrefillValuesXFDF(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statistics;
    }


}
