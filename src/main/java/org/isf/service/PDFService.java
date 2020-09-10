package org.isf.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.isf.dao.Visit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PDFService {

    @Autowired
    protected ServletContext mContext;

    protected static final String TMP_FOLDER = "tmp/";

    public File createDocumentFromEntry(Visit visit) {

        try {

            PDDocument document = new PDDocument();

            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            String mainCompSympt =
                    Stream.of(visit.getMainComplaintSymptom1(), visit.getMainComplaintSymptom2(), visit.getMainComplaintSymptom3(), visit.getMainComplaintSymptom4(), visit.getMainComplaintSymptom5())
                            .filter(s -> s != null && !s.isEmpty())
                            .collect(Collectors.joining(", "));

            String mainCompDia =
                    Stream.of(visit.getMainComplaintDiagnosis1(), visit.getMainComplaintDiagnosis2(), visit.getMainComplaintDiagnosis3())
                            .filter(s -> s != null && !s.isEmpty())
                            .collect(Collectors.joining(", "));

            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);

            contentStream.beginText();
            contentStream.setLeading(17f);
            contentStream.newLineAtOffset(100, 650);
            contentStream.showText("Patient Name: " + visit.getPatient().getName() + "               Age: " + visit.getPatient().getAge() + "             Gender: " + visit.getPatient().getSex());
            contentStream.newLine();
            contentStream.newLine();
            contentStream.showText("Address: " + visit.getPatient().getAddress() + ", " + visit.getPatient().getCity() + "             Date: " + visit.getDate().toString());
            contentStream.endText();

            contentStream.beginText();
            contentStream.setLeading(17f);
            contentStream.newLineAtOffset(100, 200);
            contentStream.showText("Symptoms: " + mainCompSympt);
            contentStream.newLine();
            contentStream.newLine();
            contentStream.showText("Diagnosis: " + mainCompDia);
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
}
