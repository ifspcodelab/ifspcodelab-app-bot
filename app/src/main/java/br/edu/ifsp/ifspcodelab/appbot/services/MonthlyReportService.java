package br.edu.ifsp.ifspcodelab.appbot.services;

import br.edu.ifsp.ifspcodelab.appbot.config.AppConfig;
import br.edu.ifsp.ifspcodelab.appbot.models.CreateMonthlyReport;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

@Log
@AllArgsConstructor
public class MonthlyReportService {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private AppConfig appConfig;

    public ByteArrayOutputStream generateReport(CreateMonthlyReport createMonthlyReport) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // Create and open document
            Document document = new Document(PageSize.A4, 60, 60, 20, 60);
            PdfWriter.getInstance(document, baos);
            document.open();

            // Define common fonts
            Font fontTitle = new Font(Font.HELVETICA, 13, Font.BOLD);
            Font fontHeader = new Font(Font.HELVETICA, 10, Font.BOLD);
            Font fontTimesBold = new Font(Font.HELVETICA, Font.DEFAULTSIZE, Font.BOLD);
            Font fontDefault = new Font(Font.HELVETICA, Font.DEFAULTSIZE, Font.NORMAL);

            // New Header
            Image imageIf = Image.getInstance(getClass().getResource("/logofed_1.jpg"));
            imageIf.setSpacingBefore(6.0f);
            imageIf.setAlignment(Element.ALIGN_CENTER);
            imageIf.scalePercent(65);
            document.add(imageIf);
            document.add(generateTitle("MINISTÉRIO DA EDUCAÇÃO", fontHeader));
            Paragraph p2 = generateTitle("INSTITUTO FEDERAL DE EDUCAÇÃO, CIÊNCIA E TECNOLOGIA DE SÃO PAULO", fontHeader);
            p2.setSpacingAfter(6.0f);
            document.add(p2);
            document.add(generateTitle("EDITAL Nº SPO.009, DE 1º DE FEVEREIRO DE 2023", fontHeader));
            document.add(generateTitle("ANEXO IV- RELATÓRIO MENSAL DE FREQUÊNCIA E AVALIAÇÃO – 2023", fontHeader));

            // First Table
            PdfPTable headerTable = new PdfPTable(10);
            headerTable.setWidthPercentage(100);
            headerTable.setSpacingBefore(15);
            headerTable.setSpacingAfter(8);
            headerTable.getDefaultCell().setBorderColor(new Color(255, 0, 0));
            headerTable.addCell(generateCell("Título do Projeto", 4, fontTimesBold));
            headerTable.addCell(generateCell(appConfig.getProjectTitle(), 6, fontDefault));
            headerTable.addCell(generateCell("Professor(a) Responsável", 4, fontTimesBold));
            headerTable.addCell(generateCell(appConfig.getCoordinatorName(), 6, fontDefault));
            headerTable.addCell(generateCell("Voluntário(a)", 4, fontTimesBold));
            headerTable.addCell(generateCell(createMonthlyReport.getName(), 6, fontDefault));
            headerTable.addCell(generateCell("Data de entrega", 4, fontTimesBold));
            headerTable.addCell(generateCell(createMonthlyReport.getData().format(formatter), 6, fontDefault));
            document.add(headerTable);

            // Subtitle
            String month = createMonthlyReport.getData()
                .getMonth()
                .minus(1)
                .getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));

            document.add(generateTitle("Resumo das atividades desenvolvidas no Mês de " + month + "/2023", fontTitle));

            // Content Table
            PdfPTable contentTable = new PdfPTable(1);
            contentTable.setWidthPercentage(100);
            contentTable.setSpacingBefore(15);
            contentTable.setSpacingAfter(15);
            contentTable.getDefaultCell().setBorderColor(new Color(255, 0, 0));
            contentTable.addCell(generateCell("Atividades planejadas:", 1, fontTimesBold));
            contentTable.addCell(generateCell(createMonthlyReport.getPlanActivities(), 1, fontDefault));
            contentTable.addCell(generateCell("Atividades realizadas:", 1, fontTimesBold));
            contentTable.addCell(generateCell(createMonthlyReport.getExecutedActivities(), 1, fontDefault));
            contentTable.addCell(generateCell("Resultados obtidos:", 1, fontTimesBold));
            contentTable.addCell(generateCell(createMonthlyReport.getResults(), 1, fontDefault));
            document.add(contentTable);

            //Message
            String message = "Observação: Entregar este relatório via plataforma Moodle até o dia 5º de cada mês, conforme previsto no edital.";
            Paragraph messageParagraph = new Paragraph(message, fontDefault);
            messageParagraph.setSpacingAfter(6);
            document.add(messageParagraph);

            // Signature Table
            PdfPTable signaturesTable = new PdfPTable(2);
            signaturesTable.setWidthPercentage(100);
            signaturesTable.setSpacingBefore(40);
            signaturesTable.getDefaultCell().setBorderColor(new Color(255, 255, 255));
            signaturesTable.addCell(generateCellCenter("________________________________", 1, fontTimesBold));
            signaturesTable.addCell(generateCellCenter("________________________________", 1, fontTimesBold));
            signaturesTable.addCell(generateCellCenter("Voluntário(a)", 1, fontDefault));
            signaturesTable.addCell(generateCellCenter("Professor(a) Responsável", 1, fontDefault));
            document.add(signaturesTable);
            document.close();
            return baos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private PdfPCell generateCell(String text, int colSpan, Font font) {
        Paragraph paragraph = new Paragraph(text, font);
        paragraph.setMultipliedLeading(1.4f);
        PdfPCell cell = new PdfPCell();
        cell.addElement(paragraph); //https://stackoverflow.com/questions/25850566/paragraph-leading-inside-table-cell
        cell.setColspan(colSpan);
        cell.setPaddingLeft(4);
        cell.setPaddingRight(4);
        cell.setPaddingBottom(10);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    private PdfPCell generateCellCenter(String text, int colSpan, Font font) {
        Paragraph paragraph = new Paragraph(text, font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        PdfPCell cell = new PdfPCell();
        cell.addElement(paragraph);
        cell.setBorderColor(Color.WHITE);
        cell.setColspan(colSpan);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private Paragraph generateTitle(String text, Font font) {
        Paragraph paragraph = new Paragraph(text, font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingAfter(3);
        return paragraph;
    }

    private Paragraph generatePageHeaderText(String text, Font font) {
        Paragraph paragraph = new Paragraph(text, font);
        paragraph.setMultipliedLeading(1.1f);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        return paragraph;
    }

    public String getMonth(CreateMonthlyReport createMonthlyReport) {
        String month = createMonthlyReport.getData().getMonth().minus(1).getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));
        return month;
    }

    public String getFileName(CreateMonthlyReport createMonthlyReport) {
        String month = createMonthlyReport.getData().getMonth().minus(1).getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));
        return "relatório-"
            .concat(getMonth(createMonthlyReport).toLowerCase()).concat("-")
            .concat(String.valueOf(createMonthlyReport.getData().getYear())).concat("-")
            .concat(createMonthlyReport.getName()
                .toLowerCase()
                .substring(0, createMonthlyReport.getName().indexOf(" ")))
            .concat("-")
            .concat(String.valueOf(Instant.now().toEpochMilli()));
    }

}
