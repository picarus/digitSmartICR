
import com.hp.dpp.sdk.Form;
import com.hp.dpp.sdk.exception.AreaException;
import com.hp.dpp.icr.Validation.Statistics;
import com.hp.dpp.icr.Validation.Validation;

import java.io.PrintStream;
import java.io.FilenameFilter;
import java.io.File;


/**
 * Date: Nov 3, 2003
 * Time: 1:05:21 AM
 */
public abstract class TrainImpl extends ProcessImpl implements Process {


    TrainImpl(Validation validation) {
        super(validation);
    }

    TrainImpl(int factor, PrintStream printStream) {
        super(factor, printStream);
    }

    protected abstract Statistics processForm(Form form) throws AreaException;


    public Statistics process() {
        Form form = null;
        FilenameFilter filter = new ASHBase.FilterByExtension(PathInfo.RQS_EXT_);
        File[] fileArray;
        File file;
        String path;
        Statistics lastStatistics;
        Statistics statistics;
        int i = 0;
        File folder;

        statistics = new Statistics();
        path = ASHBase.rootPath_ + PathInfo.trainingPath_;
        folder = new File(path);
        fileArray = folder.listFiles(filter);

        try {
            while ( i < fileArray.length ) {
                form = ASHTraining.readForm( fileArray[i] );
                lastStatistics = processForm( form );
                ASHBase.mergeImage( form );
                showResultsLastFile(lastStatistics, fileArray[i].getName());
                statistics.importStatistics(lastStatistics);
                i++;
            }
            if (form != null) {
                ASHTraining.exportStatistics(form, statistics);
                file = new File(ASHBase.rootPath_ + File.separator + PathInfo.statisticsPath_ + File.separator + PathInfo.statsFile_ + getValidationID() + getFactor() + PathInfo.XFDF_EXT_);
                form.exportPrefillValuesXFDF(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statistics;
    }


}
