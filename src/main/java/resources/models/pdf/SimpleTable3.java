package resources.models.pdf;

/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2020 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/27884229/itextsharap-error-while-adding-pdf-table-to-document
 */

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class SimpleTable3 {
    private static final String DEST = "./target/sandbox/tables/simple_table3.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new SimpleTable3().manipulatePdf(DEST);
    }

    private void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, PageSize.A3.rotate());

        Table table = new Table(UnitValue.createPercentArray(35)).useAllAvailableWidth().setFixedLayout();
        table.setWidth(pdfDoc.getDefaultPageSize().getWidth() - 80);

        Cell contractor = new Cell(1, 5).add(new Paragraph("XXXXXXXXXXXXX"));
        table.addCell(contractor);

        Cell workType = new Cell(1, 5).add(new Paragraph("Refractory Works"));
        table.addCell(workType);

        Cell supervisor = new Cell(1, 4).add(new Paragraph("XXXXXXXXXXXXXX"));
        table.addCell(supervisor);

        Cell paySlipHead = new Cell(1, 10).add(new Paragraph("XXXXXXXXXXXXXXXX"));
        table.addCell(paySlipHead);

        Cell paySlipMonth = new Cell(1, 2).add(new Paragraph("XXXXXXX"));
        table.addCell(paySlipMonth);

        Cell blank = new Cell(1, 9).add(new Paragraph(""));
        table.addCell(blank);

        doc.add(table);

        doc.close();
    }
}
