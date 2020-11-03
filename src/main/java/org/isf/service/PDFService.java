package org.isf.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.isf.dao.User;
import org.isf.dao.Visit;
import org.isf.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PDFService {

    @Autowired
    protected ServletContext mContext;

    @Autowired
    protected DeviceDetailsService deviceDetailsService;

    @Autowired
    protected UserService userService;

    public File createDocumentFromEntry(Visit visit, User doctor) {

        String hospitalName = deviceDetailsService.findAll().get(0).getHospitalName();

        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);


            addHeader(contentStream, visit, page);
            addPatientData(contentStream, visit, doctor, document);
            addSymptomsAndDiagnosis(contentStream, visit);

            if (!visit.getMedication1().equals("")) {
                contentStream.showText("Prescribed Medication: ");
            }
            if (!visit.getMedication1().equals("")) {
                contentStream.newLine();
                contentStream.showText("- " + visit.getMed1Name() + " (" + visit.getMed1Time() + ") for " + visit.getMed1Duration() + "; (" + visit.getMed1Caution() + ")");
            }
            if (!visit.getMedication2().equals("")) {
                contentStream.newLine();
                contentStream.showText("- " + visit.getMed2Name() + " (" + visit.getMed2Time() + ") for " + visit.getMed2Duration() + "; (" + visit.getMed2Caution() + ")");
            }
            if (!visit.getMedication3().equals("")) {
                contentStream.newLine();
                contentStream.showText("- " + visit.getMed3Name() + " (" + visit.getMed3Time() + ") for " + visit.getMed3Duration() + "; (" + visit.getMed3Caution() + ")");
            }
            if (!visit.getMedication4().equals("")) {
                contentStream.newLine();
                contentStream.showText("- " + visit.getMed4Name() + " (" + visit.getMed4Time() + ") for " + visit.getMed4Duration() + "; (" + visit.getMed4Caution() + ")");
            }
            if (!visit.getMedication5().equals("")) {
                contentStream.newLine();
                contentStream.showText("- " + visit.getMed5Name() + " (" + visit.getMed5Time() + ") for " + visit.getMed5Duration() + "; (" + visit.getMed5Caution() + ")");
            }
            if (!visit.getMedication6().equals("")) {
                contentStream.newLine();
                contentStream.showText("- " + visit.getMed6Name() + " (" + visit.getMed6Time() + ") for " + visit.getMed6Duration() + "; (" + visit.getMed6Caution() + ")");
            }

            contentStream.newLine();
            contentStream.newLine();

            if (!visit.getExaminationsPrescribed().equals("")) contentStream.showText("Examinations Prescribed: " + visit.getExaminationsPrescribed());

            contentStream.newLine();
            contentStream.newLine();

            if (visit.getNextVisitDate() != null) {
                String date = visit.getNextVisitDate().toString().substring(0, 10);
                String formattedDate = DateUtil.format(date);
                contentStream.showText("Next Visit Date: " + formattedDate);
            }

            contentStream.newLine();
            contentStream.newLine();
            contentStream.endText();
            contentStream.close();



            document.save(mContext.getRealPath("/") + visit.getVisitID() + ".pdf");
            document.close();

            File sourceFile = new File(mContext.getRealPath("/") + visit.getVisitID() + ".pdf");

            return sourceFile;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String createQRCodeForDocument(Visit visit, User user) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            String text = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/view/" + user.getUserName()).build().toUriString();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 125, 125);

            String dest = mContext.getRealPath("/") + user.getUserName() + "_QR.png";
            Path path = FileSystems.getDefault().getPath(dest);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
            return dest;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (WriterException es) {
            es.printStackTrace();
            return null;
        }

    }

    public void addHeader(PDPageContentStream contentStream, Visit visit, PDPage pdPage) throws IOException {
        try{
            String hospitalName = deviceDetailsService.findAll().get(0).getHospitalName();
            String hospitalAddress = deviceDetailsService.findAll().get(0).getHospitalAddress();
            int marginTop1 = 30;
            int marginTop2 = 50;

            PDFont font = PDType1Font.TIMES_BOLD; // Or whatever font you want.

            int fontSize = 16; // Or whatever font size you want.
            float title1Width = font.getStringWidth(hospitalName) / 1000 * fontSize;
            float title2Width = font.getStringWidth(hospitalAddress) / 1000 * fontSize;
            float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

            contentStream.beginText();
            contentStream.setFont(font, fontSize);

            contentStream.newLineAtOffset((pdPage.getMediaBox().getWidth() - title1Width) / 2, pdPage.getMediaBox().getHeight() - marginTop1 - titleHeight);
            contentStream.showText(hospitalName);
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLineAtOffset((pdPage.getMediaBox().getWidth() - title2Width) / 2, pdPage.getMediaBox().getHeight() - marginTop2 - titleHeight);
            contentStream.showText(hospitalAddress);
            contentStream.endText();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPatientData(PDPageContentStream contentStream, Visit visit, User doctor, PDDocument document) {
        try {
            String date = visit.getDate().toString().substring(0, 10);
            String formattedDate = DateUtil.format(date);

            contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
            contentStream.beginText();
            contentStream.setLeading(17f);
            contentStream.newLineAtOffset(50, 670);
            contentStream.showText("Date: " + formattedDate);
            contentStream.newLine();
            contentStream.showText("Doctor " + doctor.getName());
            contentStream.newLine();
            contentStream.showText("Patient Name: " + visit.getPatient().getName());
            contentStream.newLine();
            contentStream.showText("Age: " + visit.getPatient().getAge() + "       Gender: " + visit.getPatient().getSex());
            contentStream.newLine();

            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.showText("Patient Telephone Number: " + visit.getPatient().getTelephone());
            contentStream.newLine();
            contentStream.showText("Patient email address: " + visit.getPatient().getEmail());
            contentStream.newLine();
            contentStream.showText("Address: " + visit.getPatient().getAddress() + ", " + visit.getPatient().getCity());
            contentStream.newLine();
            contentStream.showText("Aadhaar ID: " + visit.getPatient().getTaxCode());

            String qr = createQRCodeForDocument(visit, doctor);
            PDImageXObject QRImage = PDImageXObject.createFromFile(qr, document);
            contentStream.endText();

            contentStream.drawImage(QRImage, 400, 620);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addSymptomsAndDiagnosis(PDPageContentStream contentStream, Visit visit) {
        try{
            String mainCompSympt = visit.getMainComplaintSymptom();
            List<String> symptomsList = new ArrayList<>();
            symptomsList = Arrays.asList(mainCompSympt.split(","));

            String mainCompDia =
                    Stream.of(visit.getMainComplaintDiagnosis1(), visit.getMainComplaintDiagnosis2(), visit.getMainComplaintDiagnosis3())
                            .filter(s -> s != null && !s.isEmpty())
                            .collect(Collectors.joining(","));
            List<String> diagnosisList = new ArrayList<>();
            diagnosisList = Arrays.asList(mainCompDia.split(","));

            contentStream.beginText();
            contentStream.setLeading(17f);
            contentStream.newLineAtOffset(50, 520);
            contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
            contentStream.showText("Symptoms: ");
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.newLine();

            if (mainCompSympt.equals("")) {
                contentStream.showText("no symptoms");
                contentStream.newLine();
            } else {
                int index = 1;
                for (String symptom : symptomsList) {
                    contentStream.showText((index++) + "." + symptom);
                    contentStream.newLine();
                }
            }

            contentStream.newLine();
            contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
            contentStream.showText("Diagnosis: ");
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.newLine();

            if (mainCompDia.equals("")) {
                contentStream.showText("no diagnosis");
                contentStream.newLine();
            } else {
                int index = 1;
                for (String diagnosis : diagnosisList) {
                    contentStream.showText((index++) + "." + diagnosis);
                    contentStream.newLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMedications(PDPageContentStream contentStream, Visit visit) {

    }
}
