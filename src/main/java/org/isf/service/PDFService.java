package org.isf.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.isf.dao.User;
import org.isf.dao.Visit;
import org.isf.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import javax.servlet.ServletContext;
import java.awt.*;
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

import static org.vandeseer.easytable.settings.HorizontalAlignment.CENTER;
import static org.vandeseer.easytable.settings.HorizontalAlignment.LEFT;

@Service
public class PDFService {

    @Autowired
    protected ServletContext mContext;

    @Autowired
    protected DeviceDetailsService deviceDetailsService;

    @Autowired
    protected UserService userService;

    public File createDocumentFromEntry(Visit visit, User doctor) {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            float heightCounter = 574;

            addHeader(contentStream, visit, page);
            addPatientData(contentStream, visit, doctor, document);
            heightCounter = addSymptomsAndDiagnosis(contentStream, visit, heightCounter);
            heightCounter = addMedications(contentStream, visit, heightCounter);


            if (heightCounter < 200) {
                contentStream.close();
                PDPage page2 = new PDPage();
                document.addPage(page2);
                contentStream = new PDPageContentStream(document, page2);
                heightCounter = 670;
            }

            addExaminations(contentStream, visit, heightCounter);
            addFooter(contentStream, visit);

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

            contentStream.drawImage(QRImage, 400, 575);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public float addSymptomsAndDiagnosis(PDPageContentStream contentStream, Visit visit, float heightCounter) {
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
            contentStream.newLineAtOffset(50, 500);
            contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
            contentStream.showText("Symptoms: ");
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.newLine();
            heightCounter = heightCounter - 30;

            if (mainCompSympt.equals("")) {
                contentStream.showText("no symptoms");
                contentStream.newLine();
                heightCounter = heightCounter - 17;
            } else {
                int index = 1;
                for (String symptom : symptomsList) {
                    contentStream.showText((index++) + "." + symptom);
                    contentStream.newLine();
                    heightCounter = heightCounter - 17;
                }
            }

            contentStream.newLine();
            contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
            contentStream.showText("Diagnosis: ");
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.newLine();
            heightCounter = heightCounter - 30;

            if (mainCompDia.equals("")) {
                contentStream.showText("no diagnosis");
                contentStream.newLine();
                heightCounter = heightCounter - 17;
            } else {
                int index = 1;
                for (String diagnosis : diagnosisList) {
                    contentStream.showText((index++) + "." + diagnosis);
                    contentStream.newLine();
                    heightCounter = heightCounter - 17;
                }
            }
            contentStream.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return heightCounter;
    }

    public float addMedications(PDPageContentStream contentStream, Visit visit, float heightCounter) {
        float parallelCounter = heightCounter;
        try {
            if (!visit.getMedication1().equals("")) {
                contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
                contentStream.showText("Prescribed Medication: ");
            }

            heightCounter = heightCounter - 24;
            parallelCounter = parallelCounter - 48;

            final Table.TableBuilder tableBuilder = Table.builder()
                    .addColumnsOfWidth(175, 70, 70, 150)
                    .fontSize(10)
                    .font(PDType1Font.TIMES_ROMAN)
                    .borderColor(Color.BLACK);

            tableBuilder.addRow(Row.builder()
                    .add(TextCell.builder().text("Name of Medicine").horizontalAlignment(LEFT).borderWidth(1).build())
                    .add(TextCell.builder().text("Time of use").borderWidth(1).build())
                    .add(TextCell.builder().text("Duration").borderWidth(1).build())
                    .add(TextCell.builder().text("Notes").borderWidth(1).build())
                    .backgroundColor(Color.WHITE)
                    .textColor(Color.BLACK)
                    .font(PDType1Font.TIMES_BOLD)
                    .fontSize(12)
                    .horizontalAlignment(CENTER)
                    .build());

            heightCounter = heightCounter - 24;
            parallelCounter = parallelCounter - 48;

            if (!visit.getMedication1().equals("")) {
                tableBuilder.addRow(Row.builder()
                        .add(TextCell.builder().text(visit.getMed1Name()).horizontalAlignment(LEFT).borderWidth(1).build())
                        .add(TextCell.builder().text(visit.getMed1Time()).borderWidth(1).build())
                        .add(TextCell.builder().text(visit.getMed1Duration()).borderWidth(1).build())
                        .add(TextCell.builder().text(visit.getMed1Caution()).borderWidth(1).build())
                        .backgroundColor(Color.WHITE)
                        .textColor(Color.BLACK)
                        .font(PDType1Font.TIMES_ROMAN)
                        .fontSize(10)
                        .horizontalAlignment(CENTER)
                        .build());
                heightCounter = heightCounter - 24;
                parallelCounter = parallelCounter - 48;
            }
            if (!visit.getMedication2().equals("")) {
                tableBuilder.addRow(Row.builder()
                        .add(TextCell.builder().text(visit.getMed2Name()).horizontalAlignment(LEFT).borderWidth(1).build())
                        .add(TextCell.builder().text(visit.getMed2Time()).borderWidth(1).build())
                        .add(TextCell.builder().text(visit.getMed2Duration()).borderWidth(1).build())
                        .add(TextCell.builder().text(visit.getMed2Caution()).borderWidth(1).build())
                        .backgroundColor(Color.WHITE)
                        .textColor(Color.BLACK)
                        .font(PDType1Font.TIMES_ROMAN)
                        .fontSize(10)
                        .horizontalAlignment(CENTER)
                        .build());
                heightCounter = heightCounter - 24;
                parallelCounter = parallelCounter - 48;
            }
            if (!visit.getMedication3().equals("")) {
                tableBuilder.addRow(Row.builder()
                        .add(TextCell.builder().text(visit.getMed3Name()).horizontalAlignment(LEFT).borderWidth(1).build())
                        .add(TextCell.builder().text(visit.getMed3Time()).borderWidth(1).build())
                        .add(TextCell.builder().text(visit.getMed3Duration()).borderWidth(1).build())
                        .add(TextCell.builder().text(visit.getMed3Caution()).borderWidth(1).build())
                        .backgroundColor(Color.WHITE)
                        .textColor(Color.BLACK)
                        .font(PDType1Font.TIMES_ROMAN)
                        .fontSize(10)
                        .horizontalAlignment(CENTER)
                        .build());
                heightCounter = heightCounter - 24;
                parallelCounter = parallelCounter - 48;
            }
            if (!visit.getMedication4().equals("")) {
                tableBuilder.addRow(Row.builder()
                        .add(TextCell.builder().text(visit.getMed4Name()).horizontalAlignment(LEFT).borderWidth(1).build())
                        .add(TextCell.builder().text(visit.getMed4Time()).borderWidth(1).build())
                        .add(TextCell.builder().text(visit.getMed4Duration()).borderWidth(1).build())
                        .add(TextCell.builder().text(visit.getMed4Caution()).borderWidth(1).build())
                        .backgroundColor(Color.WHITE)
                        .textColor(Color.BLACK)
                        .font(PDType1Font.TIMES_ROMAN)
                        .fontSize(10)
                        .horizontalAlignment(CENTER)
                        .build());
                heightCounter = heightCounter - 24;
                parallelCounter = parallelCounter - 48;
            }
            if (!visit.getMedication5().equals("")) {
                tableBuilder.addRow(Row.builder()
                        .add(TextCell.builder().text(visit.getMed5Name()).horizontalAlignment(LEFT).borderWidth(1).build())
                        .add(TextCell.builder().text(visit.getMed5Time()).borderWidth(1).build())
                        .add(TextCell.builder().text(visit.getMed5Duration()).borderWidth(1).build())
                        .add(TextCell.builder().text(visit.getMed5Caution()).borderWidth(1).build())
                        .backgroundColor(Color.WHITE)
                        .textColor(Color.BLACK)
                        .font(PDType1Font.TIMES_ROMAN)
                        .fontSize(10)
                        .horizontalAlignment(CENTER)
                        .build());
                heightCounter = heightCounter - 24;
                parallelCounter = parallelCounter - 48;
            }
            if (!visit.getMedication6().equals("")) {
                tableBuilder.addRow(Row.builder()
                        .add(TextCell.builder().text(visit.getMed6Name()).horizontalAlignment(LEFT).borderWidth(1).build())
                        .add(TextCell.builder().text(visit.getMed6Time()).borderWidth(1).build())
                        .add(TextCell.builder().text(visit.getMed6Duration()).borderWidth(1).build())
                        .add(TextCell.builder().text(visit.getMed6Caution()).borderWidth(1).build())
                        .backgroundColor(Color.WHITE)
                        .textColor(Color.BLACK)
                        .font(PDType1Font.TIMES_ROMAN)
                        .fontSize(10)
                        .horizontalAlignment(CENTER)
                        .build());
                heightCounter = heightCounter - 24;
                parallelCounter = parallelCounter - 48;
            }

            Table table = tableBuilder.build();

            TableDrawer tableDrawer = TableDrawer.builder()
                    .contentStream(contentStream)
                    .startX(50)
                    .startY(heightCounter)
                    .table(table)
                    .build();

            contentStream.endText();
            tableDrawer.draw();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parallelCounter;
    }

    public float addExaminations(PDPageContentStream contentStream, Visit visit, float heightCounter) {
        try {
            contentStream.beginText();
            contentStream.setLeading(17f);
            contentStream.newLineAtOffset(50, heightCounter);
            contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
            contentStream.showText("Pathological Examinations: ");
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.newLine();
            heightCounter = heightCounter - 17;

            if (visit.getExaminationsPrescribed().equals("")) {
                contentStream.showText("no pathological examinations");
                contentStream.newLine();
                heightCounter = heightCounter - 17;
            } else {
                String pathology = visit.getExaminationsPrescribed();
                List<String> pathologyList = Arrays.asList(pathology.split(","));
                int index = 1;
                for (String p : pathologyList) {
                    contentStream.showText((index++) + "." + p);
                    contentStream.newLine();
                    heightCounter = heightCounter - 17;
                }
            }

            contentStream.newLine();

            contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
            contentStream.showText("Radiology Examinations: ");
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.newLine();
            heightCounter = heightCounter - 17;

            if (visit.getRadiologyPrescribed().equals("")) {
                contentStream.showText("no radiology examinations");
                contentStream.newLine();
                heightCounter = heightCounter - 17;
            } else {
                String radiology = visit.getRadiologyPrescribed();
                List<String> radiologyList = Arrays.asList(radiology.split(","));
                int index = 1;
                for (String r : radiologyList) {
                    contentStream.showText((index++) + "." + r);
                    contentStream.newLine();
                    heightCounter = heightCounter - 12;
                }
            }
            contentStream.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return heightCounter;

    }

    public void addFooter(PDPageContentStream contentStream, Visit visit) {
        try {
            contentStream.showText("Advisory: " + visit.getAdvisory());
            contentStream.newLine();
            contentStream.newLine();
            if (visit.getNextVisitDate() != null) {
                String date = visit.getNextVisitDate().toString().substring(0, 10);
                String formattedDate = DateUtil.format(date);
                contentStream.showText("Next Visit Date: " + formattedDate);
                contentStream.newLine();
                contentStream.newLine();
            }
            contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
            contentStream.showText("Note: Check your contact details & Aadhaar details. If any change or variance please advise.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
