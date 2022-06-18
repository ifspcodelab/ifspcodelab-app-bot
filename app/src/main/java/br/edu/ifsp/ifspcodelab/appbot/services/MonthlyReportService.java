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
            Font fontTimesBold = new Font(Font.HELVETICA, Font.DEFAULTSIZE, Font.BOLD);
            Font fontDefault = new Font(Font.HELVETICA, Font.DEFAULTSIZE, Font.NORMAL);

            // Page Header
            PdfPTable pageHeader = new PdfPTable(5);
            pageHeader.setWidthPercentage(90);
            pageHeader.setSpacingAfter(15);
            pageHeader.getDefaultCell().setBorderColor(new Color(255, 0, 0));
            Image imageIf = Image.getInstance(getClass().getResource("/logofed_1.jpg"));
            PdfPCell imageIfCell = new PdfPCell();
            imageIfCell.setImage(imageIf);
            imageIfCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            imageIfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            imageIfCell.setFixedHeight(80);
            imageIfCell.setBorderColor(Color.WHITE);
            imageIfCell.setColspan(1);
            PdfPCell textCell = new PdfPCell();
            textCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            textCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            textCell.addElement(generatePageHeaderText("EDITAL Nº SPO.091, DE 1º DE DEZEMBRO DE 2021", fontTitle));
            textCell.addElement(generatePageHeaderText("PROGRAMA DE PROJETOS DE ENSINO", fontTitle));
            textCell.addElement(generatePageHeaderText("PARTICIPAÇÃO VOLUNTÁRIA", fontTitle));
            textCell.addElement(generatePageHeaderText("CHAMADA DE PROJETOS 2022", fontTitle));
            textCell.setBorderColor(Color.WHITE);
            textCell.setColspan(4);
            pageHeader.addCell(imageIfCell);
            pageHeader.addCell(textCell);
            document.add(pageHeader);

            // Titles
            document.add(generateTitle("ANEXO IV", fontTitle));
            document.add(generateTitle("Relatório Mensal de Frequência e Avaliação - 2022", fontTitle));

            // First Table
            PdfPTable headerTable = new PdfPTable(3);
            headerTable.setWidthPercentage(100);
            headerTable.setSpacingBefore(15);
            headerTable.setSpacingAfter(8);
            headerTable.getDefaultCell().setBorderColor(new Color(255, 0, 0));
            headerTable.addCell(generateCell("Título do Projeto", 1, fontTimesBold));
            headerTable.addCell(generateCell(appConfig.getProjectTitle(), 2, fontDefault));
            headerTable.addCell(generateCell("Professor Responsável", 1, fontTimesBold));
            headerTable.addCell(generateCell(appConfig.getCoordinatorName(), 2, fontDefault));
            headerTable.addCell(generateCell("Voluntário", 1, fontTimesBold));
            headerTable.addCell(generateCell(createMonthlyReport.getName(), 2, fontDefault));
            headerTable.addCell(generateCell("Data de entrega", 1, fontTimesBold));
            headerTable.addCell(generateCell(createMonthlyReport.getData().format(formatter), 2, fontDefault));
            document.add(headerTable);

            // Subtitle
            String month = createMonthlyReport.getData()
                .getMonth()
                .minus(1)
                .getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));

            document.add(generateTitle("Resumo das atividades desenvolvidas no mês de " + month + "/2022", fontTitle));

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
            String message = "Observação: Entregar este relatório via plataforma Moodle até o dia 05 de cada mês, conforme previsto no edital.";
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
            signaturesTable.addCell(generateCellCenter("Bolsista de Ensino", 1, fontDefault));
            signaturesTable.addCell(generateCellCenter("Professor Responsável", 1, fontDefault));
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
