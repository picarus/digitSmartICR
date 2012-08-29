
import com.hp.dpp.sdk.*;
import com.hp.dpp.sdk.TextArea;
import com.hp.dpp.sdk.exception.*;
import com.hp.dpp.icr.Pattern.*;
import com.hp.dpp.icr.Validation.Statistics;
import com.hp.dpp.icr.ChangeStroke.ChangeStrokeImpl;
import com.hp.dpp.icr.Validation.Validation;
import com.hp.dpp.icr.Validation.Validate;
import com.hp.dpp.icr.Validation.ValidateBest;
import com.hp.dpp.icr.exception.DigitException;
import com.hp.dpp.utils.Misc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.*;
import java.util.Iterator;
import java.util.Vector;
import java.util.TreeMap;
import java.util.Map;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;

/**
 * Date: Oct 10, 2003
 * Time: 7:51:36 PM
 */
public class ASHTraining extends ASHBase {

    static String[] toProcess = new String[]{"7.0", "7.1", "7.2", "7.3"};
    String VALID_ = "validate";
    String BEST_ = "best";
    String NORMAL_ = "normal";
    String METHOD_ = "method";
    String RECOGNIZE_ = "recognize";
    String TRAIN_ = "train";

    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        Form form;

        try {
            form = FormHome.create(httpServletRequest, httpServletResponse, PathInfo.applicationName_);
            serialize(form);
            form.commitTransaction("OK",httpServletRequest.getRequestURL().toString());
        } catch (FormCreationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    protected static void processList(Form form) throws NoSuchAreaException, AreaException {
        int i = 0;
        String position;
        CheckBox cb;
        Pattern pattern;

        while (i < toProcess.length) {
            position = toProcess[i];
            cb = (CheckBox) form.getArea(FIELDSTABLE_ + "#" + position);
            ShowStrokes(cb);
            pattern = new PatternCoupledImpl(cb.getPenStrokes());
            System.out.println(pattern.toString());
            i++;
        }
    }

    protected static void ShowStrokes(PhysicalArea physicalArea) {
        PenStrokes penStrokes;
        PenStroke penStroke;
        Iterator itStroke;
        boolean follow;
        SampleIterator si;
        try {
            if (physicalArea.hasPenStrokes()) {
                penStrokes = physicalArea.getPenStrokes();
                itStroke = penStrokes.getIterator();
                while (itStroke.hasNext()) {
                    penStroke = (PenStroke) itStroke.next();
                    si = penStroke.getSampleIterator();
                    si.first();
                    System.out.println("**************************");
                    do {
                        System.out.println(";" + si.getX() + ";" + si.getY());
                        follow = si.hasNext();
                        if (follow) {
                            si.next();
                        }
                    } while (follow);
                }
            }
        } catch (AreaException e) {
            e.printStackTrace();
        }

    }

    protected static void showPatterns(Map patternMap, Vector vecFields, Form form) {

        String outputImage;
        String address;
        Page page;
        Iterator itFields;
        TextArea ta;
        Bounds bounds;
        String name;
        Pattern pattern;
        Shape shape,transformedShape;
        BufferedImage bi;
        Rectangle2D origin,destination;
        Graphics2D g2D;
        Rectangle2D pageBounds;
        double width,height;

        try {
            itFields = vecFields.iterator();
            address = form.getSendBoxPage();
            page = form.getPage(address);
            pageBounds = page.getBounds().asRectangle2D();
            outputImage = imagePath_ + File.separator + address + "_" + ChangeStrokeImpl.getFactor() + PathInfo.IMAGE_EXT;
            bi = Imaging.copyImage(backgroundImage_);
            g2D = bi.createGraphics();
            width = bi.getWidth();
            height = bi.getHeight();
            g2D.setColor(Color.RED);

            while (itFields.hasNext()) {

                ta = (TextArea) itFields.next();
                name = ta.getName();
                bounds = ta.getBounds();
                destination = bounds.asRectangle2D();
                pattern = (Pattern) patternMap.get(name);
                shape = pattern.asShape();
                origin = shape.getBounds2D();
                transformedShape = transformedShape(shape, origin, destination, pageBounds, width, height);
                g2D.draw(transformedShape);
            }

            Imaging.saveImage(outputImage, Imaging.FORMAT_JPEG, bi, jpegEncodeParam_);

        } catch (ImageFormatFileException e) {
            e.printStackTrace();
        } catch (PageException e) {
            e.printStackTrace();
        } catch (NoSuchPageException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        } catch (IllegalValueException e) {
            e.printStackTrace();
        }
    }

    private static Shape transformedShape(Shape shape, Rectangle2D origin, Rectangle2D destination, Rectangle2D pageBounds, double width, double height) {
        Shape transformedShape = null;
        double scaleX = destination.getWidth() / origin.getWidth();
        double scaleY = destination.getHeight() / origin.getHeight();
        double offsetX = destination.getMinX() - origin.getMinX() * scaleX;
        double offsetY = destination.getMinY() - origin.getMinY() * scaleY;
        double pWidth = pageBounds.getWidth();
        double pHeight = pageBounds.getHeight();
        AffineTransform at = new AffineTransform(scaleX, 0, 0, scaleY, offsetX * width / pWidth, offsetY * height / pHeight);
        transformedShape = at.createTransformedShape(shape);
        return transformedShape;
    }


    static void setRecognitionResults(Form form, Statistics statistics) throws NoSuchAreaException {
        TextArea textArea;
        textArea = (TextArea) form.getArea(FIELDPERCENTAGE_);
        textArea.setPrefillValue(statistics.percentage());
        textArea = (TextArea) form.getArea(FIELDOKOUTOFTOTAL_);
        textArea.setPrefillValue(statistics.okOutOfTotal());
    }

    protected static void showDigitSet(Form form, com.hp.dpp.icr.Digits.DigitSet digitSet) {
        TextArea ta;
        Vector vector = getFields(form, FIELDSNOP_);
        Iterator itFields = vector.iterator();
        int digit;
        int n_o_p;
        String name;
        Integer[] position;

        while (itFields.hasNext()) {
            ta = (TextArea) itFields.next();
            name = ta.getName();
            position = Misc.getPosition(name);
            digit = position[0].intValue();
            try {
                n_o_p = digitSet.getNumberOfPatterns(digit);
            } catch (DigitException e) {
                n_o_p = -1;
            }
            ta.setPrefillValue(String.valueOf(n_o_p));
        }
    }

    protected static void setPrefillValues(Form form, Map patterns) {
        Iterator it = patterns.keySet().iterator();
        String nameField;
        TextArea ta;
        String digitStr;
        int digit;
        Pattern pat;
        int simil;
        boolean isCorrect;

        try {
            while (it.hasNext()) {
                nameField = (String) it.next();
                ta = (TextArea) form.getArea(nameField);
                pat = (Pattern) patterns.get(nameField);
                digit = pat.getDigit();
                simil = pat.getSimilarity();
                isCorrect = pat.getIsCorrect();

                digitStr = String.valueOf(digit);

                if (simil != -1) {
                    digitStr += "(" + String.valueOf(simil) + ")";
                    if (!isCorrect) {
                        digitStr = "!" + digitStr;
                    }
                }

                ta.setPrefillValue(digitStr);
            }
        } catch (NoSuchAreaException e) {
            e.printStackTrace();
        }
    }


    protected static SelectionArea[][] GrouptoArray(SelectionAreaGroup sag) {

        SelectionArea[][] saArray = null;
        Vector vsa = new Vector();
        Integer[] pos;
        int[] max = null;
        Iterator it = sag.getSelectionAreas();

        SelectionArea sa;
        String value;

        while (it.hasNext()) {

            sa = (SelectionArea) it.next();
            value = sa.getValue();
            pos = Misc.getPosition(value);
            max = calcSize(max, pos);
            addToVector(vsa, pos, sa);

        }

        return saArray;
    }

    protected static int[] calcSize(int[] max, Integer[] pos) {
        int len = pos.length;
        int val;
        if (max == null) {
            max = new int[len];
            for (int i = 0; i < len; i++) {
                max[i] = pos[i].intValue();
            }
        } else {
            for (int i = 0; i < len; i++) {
                val = pos[i].intValue();
                if (max[i] < val) {
                    max[i] = val;
                }
            }
        }
        return max;
    }

    protected static void addToVector(Vector vsa, Integer[] pos, SelectionArea sa) {
        int i = 0;
        int len = pos.length;
        Vector temp = vsa;
        Vector tempCur;

        while (i < (len - 1)) {
            tempCur = (Vector) temp.get(pos[i].intValue());
            if (tempCur == null) {
                tempCur = new Vector();
                temp.add(pos[i].intValue(), tempCur);
            }
            temp = tempCur;
            i++;
        }
        temp.add(pos[i].intValue(), sa);
    }


    protected static Pattern[] vectorToPatternArray(Object[] obAr) {
        Pattern[] patAr = new Pattern[obAr.length];
        for (int j = 0; j < obAr.length; j++) {
            patAr[j] = (Pattern) obAr[j];
        }
        return patAr;
    }

    protected static Map getPatternsRef(Vector vecFields) throws AreaException {

        Map patterns = new TreeMap();
        Iterator itFields;
        TextArea ta;
        String value;
        Integer[] position;
        int digit;

        itFields = vecFields.iterator();

        while (itFields.hasNext()) {

            ta = (TextArea) itFields.next();
            value = ta.getName();
            position = Misc.getPosition(value);
            digit = position[0].intValue();
            patterns.put(value, new PatternCoupledSizeImpl(digit, ta.getPenStrokes()));

        }

        return patterns;
    }

    protected static Map getPatterns(Vector vecFields) throws AreaException {

        Map patterns = new TreeMap();
        Iterator itFields;
        TextArea ta;
        String value;

        itFields = vecFields.iterator();

        while (itFields.hasNext()) {

            ta = (TextArea) itFields.next();
            value = ta.getName();
            patterns.put(value, new PatternCoupledSizeImpl(ta.getPenStrokes()));
        }

        return patterns;
    }
/*
    public static void main(String args[]) {
        TrainDigitSet training = new TrainDigitSet(ChangeStrokeImpl.DEFAULT_FACTOR, System.out);
        rootPath_ = System.getProperty("user.dir");
        initForm(rootPath_);
        processSingle(training);
    }
*/
    protected static com.hp.dpp.icr.Digits.DigitSet generateDigitSet(Pattern[] training, Form form) throws DigitException {
        com.hp.dpp.icr.Digits.DigitSet digitSet;
        FileOutputStream fos;
        digitSet = new com.hp.dpp.icr.Digits.DigitSet();
        digitSet.addPattern(training);
        // save values
        try {
            // do not differentiate the file of digitSet because it is the same ( do not depend on the Process )
            fos = new FileOutputStream(ASHTraining.rootPath_ + File.separator + PathInfo.digitSetPath_ + File.separator + form.getSendBoxPage() + "_" + ChangeStrokeImpl.getFactor() + PathInfo.DIGITSET_EXT_);
            digitSet.save(fos);
            fos.close();
            fos = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return digitSet;
    }


    protected void doRecognize(Process recognize,OutputStream os) throws FileNotFoundException {
        int i;
        int defFactor = ChangeStrokeImpl.getDefaultFactor();
        int iFactor = defFactor - 1;
        int fFactor = defFactor + 7;
        int len = fFactor - iFactor;
        int[] factor = new int[len];
        Statistics[] statistics = new Statistics[len];
        FileOutputStream fos;
        PrintStream printStream;
        PrintStream userOutput;
        fos = new FileOutputStream(rootPath_ + File.separator + PathInfo.statisticsPath_ + File.separator + PathInfo.recognizeFile_ + PathInfo.STAT_EXT_);
        printStream = new PrintStream(fos);
        userOutput = new PrintStream(os);

        recognize.setPrintStream(printStream);

        i = 0 ;
        while ( i < len ) {

            recognize.setFactor(iFactor);
            factor[i] = iFactor;
            statistics[i] = recognize.process();
            showGlobalResults(userOutput, factor[i], statistics[i]);
            showGlobalResults(printStream, factor[i], statistics[i]);
            i++;
            iFactor++;

        }

        showGlobalResults(printStream, factor, statistics);
    }

    protected void doCalculate(Process train, OutputStream os) throws FileNotFoundException {
        int i = 0;
        int defFactor = ChangeStrokeImpl.getDefaultFactor();
        int iFactor = defFactor - 1;
        int fFactor = defFactor + 7;
        int len = fFactor - iFactor;
        int[] factor = new int[len];
        Statistics[] statistics = new Statistics[len];
        FileOutputStream fos;
        PrintStream printStream;
        PrintStream userStream=new PrintStream(os);

        fos = new FileOutputStream(rootPath_ + File.separator + PathInfo.statisticsPath_ + File.separator + PathInfo.calculateFile_ + train.getValidationID() +train.getProcessID() + PathInfo.STAT_EXT_);
        printStream = new PrintStream(fos);
        train.setPrintStream(printStream);

        while ( i < len ) {
            train.setFactor(iFactor);
            factor[i] = iFactor;
            statistics[i] = train.process();
            showGlobalResults(printStream, factor[i], statistics[i]);
            showGlobalResults(userStream, factor[i], statistics[i]);
            i++;
            iFactor++;
        }

        showGlobalResults(printStream, factor, statistics);
    }

    protected void doCalculateWithPattern(OutputStream os) throws FileNotFoundException {
        Validation validation;
        // Trace
        //Compare.initPruneMonitor();
        //validation = new com.hp.dpp.icr.Validation.Validate();
        validation = new ValidateBest();
        doCalculate(new TrainWithPattern(validation), os);
        // Trace
        //Compare.dumpPruneMonitor();
    }


    protected void doCalculateDigitSet(OutputStream os) throws FileNotFoundException {
        Validation validation;
        // Trace
        //Compare.initPruneMonitor();
        //validation = new com.hp.dpp.icr.Validation.Validate();
        validation = new ValidateBest();
        doCalculate(new TrainDigitSet(validation), os);
        // Trace
        //Compare.dumpPruneMonitor();
    }

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String method=null;
        String valid=null;
        Validation validate;
        Process process;
        // todo: use parameter

        method = httpServletRequest.getParameter(METHOD_);
        valid = httpServletRequest.getParameter(VALID_);

        //doCalculateDigitSet(httpServletResponse.getOutputStream());
        //  doCalculateWithPattern(httpServletResponse.getOutputStream());
        if ((valid!=null)&&(valid.toLowerCase().equals(NORMAL_))){
            validate = new Validate();
        }
        else {
            validate = new ValidateBest();
        }

        if ((method!=null)&&(method.toLowerCase().equals(TRAIN_))){
            process = new TrainDigitSet(validate);
            doCalculate(process,httpServletResponse.getOutputStream());
        }
        else {
            process = new Recognize(validate);
            doRecognize(process,httpServletResponse.getOutputStream());
        }

    }

    protected static void showGlobalResults(PrintStream printStream, int factor, Statistics statistics) {
        printStream.println("Factor:" + factor);
        printStream.println("Percentage:" + statistics.percentage());
        printStream.println("Ratio:" + statistics.okOutOfTotal());
    }

    protected static void showGlobalResults(PrintStream printStream, int factor[], Statistics statistics[]) {
        int i = 0;
        int len = factor.length;
        while (i < len) {
            showGlobalResults(printStream, factor[i], statistics[i]);
            i++;
        }
    }

}
