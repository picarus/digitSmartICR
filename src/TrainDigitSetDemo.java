
import com.hp.dpp.sdk.Form;
import com.hp.dpp.sdk.exception.AreaException;
import com.hp.dpp.icr.Validation.Statistics;
import com.hp.dpp.icr.Validation.Validation;

import java.io.File;

/**
 * Date: Nov 5, 2003
 * Time: 10:19:44 PM
 */
public class TrainDigitSetDemo extends TrainDigitSet implements Process {

    Form form_;

    TrainDigitSetDemo(Validation validation, Form form) {
        super(validation);
        form_ = form;
        processID_ = "DSD";
    }

    protected Statistics processForm(Form form) throws AreaException {
        return processForm(form, ASHBase.FIELDSINPUT_, ASHBase.FIELDSTABLE_);
    }

    public Statistics process() {

        File file;
        Statistics statistics;
        statistics = new Statistics();

        try {
            statistics = processForm(form_);
            ASHBase.mergeImage(form_);
            showResultsLastFile(statistics, form_.getSendBoxPage());
            ASHTraining.exportStatistics(form_, statistics);
            file = new File(ASHBase.rootPath_ + File.separator + PathInfo.statisticsPath_ + File.separator + PathInfo.statsFile_ + getValidationID() + getFactor() + PathInfo.XFDF_EXT_);
            form_.exportPrefillValuesXFDF(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return statistics;
    }


}
