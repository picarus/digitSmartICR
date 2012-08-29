
import com.hp.dpp.icr.ChangeStroke.ChangeStrokeImpl;
import com.hp.dpp.icr.Validation.Statistics;
import com.hp.dpp.icr.Validation.Validation;

import java.io.PrintStream;

/**
 * Date: Nov 2, 2003
 * Time: 4:47:01 PM
 */
public abstract class ProcessImpl implements Process {

    protected Statistics statisticsLast_ = null;
    protected PrintStream printStream_ = null;
    protected Validation validation_ = null;
    protected static String processID_;

    ProcessImpl(Validation validation) {
        validation_ = validation;
    }

    ProcessImpl(int factor, PrintStream printStream) {
        ChangeStrokeImpl.setFactor(factor);
        printStream_ = printStream;
    }

    public String getProcessID(){
        return processID_;
    }

    public String getValidationID() {
        return validation_.getIDPrefix();
    }

    public int setFactor(int factor) {
        ChangeStrokeImpl.setFactor(factor);
        return factor;
    }

    public PrintStream setPrintStream(PrintStream printStream) {
        printStream_ = printStream;
        return printStream;
    }

    public PrintStream getPrintStream() {
        return printStream_;
    }

    public int getFactor() {
        return ChangeStrokeImpl.getFactor();
    }

    public void showResultsLastFile(Statistics statistics, String fileName) {
        printStream_.println("File Name:" + fileName);                 // printstream is null
        printStream_.println("Percentage:" + statistics.percentage());
        printStream_.println("Ratio:" + statistics.okOutOfTotal());
    }

    public Statistics getStatistics() {
        return statisticsLast_;
    }
}
