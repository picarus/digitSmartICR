
import com.hp.dpp.sdk.Form;
import com.hp.dpp.icr.Validation.Statistics;

import java.io.PrintStream;

/**
 * Date: Nov 1, 2003
 * Time: 12:15:41 AM
 */
public interface Process {

    Statistics process();

    Statistics getStatistics();

    void showResultsLastFile(Statistics statistics, String fileName);

    int setFactor(int factor);

    int getFactor();

    PrintStream setPrintStream(PrintStream printStream);

    PrintStream getPrintStream();

    String getValidationID();

    String getProcessID();

}
