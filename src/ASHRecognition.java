
import com.hp.dpp.sdk.Form;
import com.hp.dpp.sdk.FormHome;
import com.hp.dpp.sdk.exception.FormCreationException;
import com.hp.dpp.icr.Validation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.File;
import java.io.PrintStream;

/**
 * Date: Oct 10, 2003
 * Time: 7:51:45 PM
 */
public class ASHRecognition extends ASHBase {

   protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

       Form form;
       Validation validation = new ValidateBest();
       Process trainDigitSetDemo;
       FileOutputStream fos;
       PrintStream printStream;

        try {
            form = FormHome.create(httpServletRequest, httpServletResponse, PathInfo.applicationName_);
            serialize(form);
            trainDigitSetDemo = new TrainDigitSetDemo(validation,form);
            fos = new FileOutputStream(rootPath_ + File.separator + PathInfo.statisticsPath_ + File.separator + PathInfo.calculateFile_ + trainDigitSetDemo.getValidationID() +trainDigitSetDemo.getProcessID() + PathInfo.STAT_EXT_);
            printStream = new PrintStream(fos);
            trainDigitSetDemo.setPrintStream(printStream);
            trainDigitSetDemo.process();
            form.commitTransaction("OK",httpServletRequest.getRequestURL().toString());
        } catch (FormCreationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
