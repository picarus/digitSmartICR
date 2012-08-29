
import com.hp.dpp.sdk.*;
import com.hp.dpp.sdk.exception.*;
import com.hp.dpp.icr.DigitSet;
import com.hp.dpp.icr.Validation.Statistics;
import com.hp.dpp.icr.Pattern.Pattern;
import com.hp.dpp.icr.exception.DigitException;
import com.hp.dpp.utils.Misc;
import com.sun.media.jai.codec.JPEGEncodeParam;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import java.io.*;
import java.awt.image.BufferedImage;
import java.util.Vector;
import java.util.Iterator;
import java.util.Map;

/**
 * Date: Oct 31, 2003
 * Time: 11:46:07 PM
 */
public abstract class ASHBase extends HttpServlet {

    // calculated path´s
    protected static String rootPath_;
    protected static String imagePath_;

    // names of the fields
    static final String FIELDSINPUT_ = "Input";
    static final String FIELDSTABLE_ = "Table";
    static final String FIELDSID_ = "ID";
    static final String FIELDSNOP_ = "N_O_P";
    static final String FIELDPERCENTAGE_ = "Percentage";
    static final String FIELDOKOUTOFTOTAL_ = "OKOutOfTotal";

    // image global
    protected static BufferedImage backgroundImage_ = null;
    protected static JPEGEncodeParam jpegEncodeParam_ = null;

    static class FilterByExtension implements FilenameFilter {
        String extension;

        FilterByExtension(String ext) {
            extension = ext;
        }

        public boolean accept(File dir, String name) {
            return name.endsWith(extension);
        }

    }

    protected static void mergeImage(Form form){
        BufferedImage bi;
        String address;
        Page page;
        String outputImage;

        try {
            bi = Imaging.copyImage(backgroundImage_);
            address=form.getSendBoxPage();
            outputImage = imagePath_ + File.separator + address + PathInfo.IMAGE_EXT;
            page=form.getPage(address);
            page.renderMerge(bi,true,1,true);
            Imaging.saveImage(outputImage,Imaging.FORMAT_JPEG,bi,jpegEncodeParam_);
        } catch (PageException e) {
            e.printStackTrace();
        } catch (NoSuchPageException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        } catch (IllegalValueException e) {
            e.printStackTrace();
        } catch (ImageFormatFileException e) {
            e.printStackTrace();
        }
    }

    protected static void serialize(Form form) throws IOException {
        File dir = new File(rootPath_ + File.separator + PathInfo.trainingPath_);
        dir.mkdirs();
        String address = form.getSendBoxPage();
        File file = File.createTempFile( address + "_", PathInfo.RQS_EXT_, dir);
        FileOutputStream fos = new FileOutputStream(file);
        form.write(fos);
        fos.close();
        fos = null;
    }

    protected static Form readForm(File fileArray) throws IOException, FormCreationException {
        FileInputStream fis;
        Form form;
        fis = new FileInputStream(fileArray);
        form = FormHome.read(fis, PathInfo.applicationName_);
        //form = FormHome.readSessionSet(fis, applicationName_);
        fis.close();
        fis=null;
        return form;
    }

    protected static Vector getFields(Form form, String nameFieldsGroup) {
        Vector vector = new Vector();
        Iterator itFields;
        Area area;

        itFields = form.getAreas();
        while (itFields.hasNext()) {
            area = (Area) itFields.next();
            if ((area.isField()) && (area.getName().startsWith(nameFieldsGroup))) {
                vector.add(area);
            }
        }

        return vector;
    }

    protected static Vector getFilledFields(Form form, String nameFieldsGroup) {
        Vector vector = new Vector();
        Iterator itFields;
        Area area;
        Field field;

        itFields = form.getAreas();
        while (itFields.hasNext()) {
            area = (Area) itFields.next();
            if ((area.isField()) && (area.getName().startsWith(nameFieldsGroup))) {
                field = (Field) area;
                if (field.hasPenStrokes()) {
                    vector.add(area);
                }
            }
        }

        return vector;
    }




    private static void clearFields(Form form, String fields) {
        TextArea textArea;
        Vector vecFields;
        int len,i;

        vecFields = getFields(form, fields);
        len = vecFields.size();
        i = 0;
        while (i < len) {
            textArea = (TextArea) vecFields.get(i);
            textArea.setPrefillValue("");
            i++;
        }
    }

    protected static void exportStatistics(Form form, Statistics stats) {

        Vector vecFields;
        TextArea textArea;
        String name;
        Integer[] position;
        int len;
        int i;
        int ok;
        int[] oks;
        int error;
        int[][] errors;
        int posX,posY;

        // clear id´s
        clearFields(form, FIELDSID_);
        // clear n_o_p
        clearFields(form, FIELDSNOP_);

        // assign percentage
        try {
            textArea = (TextArea) form.getArea(FIELDPERCENTAGE_);
            textArea.setPrefillValue(stats.percentage());
        } catch (NoSuchAreaException e) {
            e.printStackTrace();
        }
        // assign okOutOfTotal
        try {
            textArea = (TextArea) form.getArea(FIELDOKOUTOFTOTAL_);
            textArea.setPrefillValue(stats.okOutOfTotal());
        } catch (NoSuchAreaException e) {
            e.printStackTrace();
        }
        // assign oks
        oks = stats.getOKs();
        vecFields = getFields(form, FIELDSINPUT_);
        len = vecFields.size();
        i = 0;
        while (i < len) {
            textArea = (TextArea) vecFields.get(i);
            name = textArea.getName();
            position = Misc.getPosition(name);
            posX = position[0].intValue();
            ok = oks[posX];
            textArea.setPrefillValue(String.valueOf(ok));
            i++;
        }
        // assign errors
        errors = stats.getErrors();
        vecFields = getFields(form, FIELDSTABLE_);
        len = vecFields.size();
        i = 0;
        while (i < len) {
            textArea = (TextArea) vecFields.get(i);
            name = textArea.getName();
            position = Misc.getPosition(name);
            posX = position[0].intValue();
            posY = position[1].intValue();
            error = errors[posX][posY];
            textArea.setPrefillValue(String.valueOf(error));
            i++;
        }

        vecFields = getFields(form, FIELDSNOP_);
        len = vecFields.size();
        i=0;
        while (i<len){
            textArea = (TextArea) vecFields.get(i);
            name = textArea.getName();
            position = Misc.getPosition(name);
            posX = position[0].intValue();
            error = errors[10][posX];
            textArea.setPrefillValue(String.valueOf(error));
            i++;
        }

    }

    protected static com.hp.dpp.icr.Digits.DigitSet createAndSaveDigitSet(Map trainingMap, Form form) throws DigitException {
        Pattern[] training;
        com.hp.dpp.icr.Digits.DigitSet digitSet = null;
        training = ASHTraining.vectorToPatternArray(trainingMap.values().toArray());
        digitSet = ASHTraining.generateDigitSet(training, form);
        return digitSet;
    }

    protected static com.hp.dpp.icr.Digits.DigitSet createDigitSet(Form form) throws AreaException, DigitException {
        Map trainingMap;
        Vector vecFieldsTableG;
        vecFieldsTableG = ASHTraining.getFilledFields(form, ASHBase.FIELDSTABLE_);
        trainingMap = ASHTraining.getPatternsRef(vecFieldsTableG);
        return createAndSaveDigitSet(trainingMap,form);
    }

    protected static com.hp.dpp.icr.Digits.DigitSet loadDigitSet() {
        String pathDigitSet = rootPath_ + File.separator + PathInfo.dataSetPath_ + File.separator + PathInfo.digitSetFile_;
        FileInputStream fis = null;
        com.hp.dpp.icr.Digits.DigitSet digitSet = null;
        try {
            fis = new FileInputStream(pathDigitSet);
            digitSet = com.hp.dpp.icr.Digits.DigitSet.load(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return digitSet;
    }

    public static void initForm(String path) {
        File file;
        try {
            file = new File(path + File.separator + PathInfo.fileForm_);
            FormHome.loadPdf(PathInfo.applicationName_, file);
            backgroundImage_ = Imaging.loadImage(path + PathInfo.fileImage_);
            imagePath_ = rootPath_ + File.separator + PathInfo.imageFolder_;
            jpegEncodeParam_ = new JPEGEncodeParam();
            jpegEncodeParam_.setQuality(0.75f);
        } catch (IllegalValueException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        } catch (NotAllowedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ImageFormatFileException e) {
            e.printStackTrace();
        }
    }

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        ServletContext sc = servletConfig.getServletContext();
        rootPath_ = sc.getRealPath(File.separator);
        initForm(rootPath_);
    }
}
