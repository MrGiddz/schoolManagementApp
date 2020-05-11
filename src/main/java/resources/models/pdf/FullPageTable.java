package resources.models.pdf;

/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2020 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */

/****
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/19873263/how-to-increase-the-width-of-pdfptable-in-itext-pdf
 */

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.sun.media.jai.codec.*;
import org.ghost4j.Ghostscript;
import org.ghost4j.document.DocumentException;
import org.ghost4j.document.PDFDocument;
import org.ghost4j.renderer.RendererException;
import org.ghost4j.renderer.SimpleRenderer;
import resources.models.TableData;
import resources.utilities.LoadData;

import javax.media.jai.JAI;
import javax.media.jai.NullOpImage;
import javax.media.jai.OpImage;
import javax.media.jai.PlanarImage;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FullPageTable {
    private static final String DEST = "./target/sandbox/tables/full_page_table.pdf";

    public static boolean manipulatePdf() throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc,  PageSize.A4.rotate());
        doc.setMargins(0, 0, 0, 0);

        Table table = new Table(new float[11]).useAllAvailableWidth();
        table.setMarginTop(0);
        table.setMarginBottom(0);
        table.setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA)).setFontSize(10F);

        // first row
        Cell cell = new Cell(1, 11).add(new Paragraph("Students").setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)).setFontSize(12F));
        cell.setTextAlignment(TextAlignment.CENTER);
        cell.setPadding(5);
        cell.setBackgroundColor(new DeviceRgb(140, 221, 8));
        table.addCell(cell);

        table.addCell("Full Name");
        table.addCell("Date of Birth");
        table.addCell("Gender");
        table.addCell("Disabled");
        table.addCell("Class");
        table.addCell("Nationality");
        table.addCell("Parent or Guardian");
        table.addCell("Phone Number");
        table.addCell("Email");
        table.addCell("Time Registered");
        table.addCell("Date Registered");


        for (Map.Entry<Integer, TableData> m : LoadData.getStudentFullInfo().entrySet()){
            table.addCell(m.getValue().fullnameProperty().get());
            table.addCell(m.getValue().getDate_of_birth());
            table.addCell(m.getValue().genderProperty().get());
            table.addCell(m.getValue().disabledProperty().get());
            table.addCell(m.getValue().classNameProperty().get());
            table.addCell(m.getValue().nationalityProperty().get());
            table.addCell(m.getValue().parent_nameProperty().get());
            table.addCell(m.getValue().phone_numberProperty().get());
            table.addCell(m.getValue().getStudentEmail().get());
            table.addCell(m.getValue().date_addedProperty().get());
            table.addCell(m.getValue().time_addedProperty().get());
        }

        doc.add(table);

        doc.close();
        return true;
    }

    public static void print() throws IOException {

        FileOutputStream fos = new FileOutputStream("tempFile.pdf");
        byte[] myByteArray = new byte[1024];
        fos.write(myByteArray);
        fos.close();
        fos.flush();

        List<Image> images = null;

        Ghostscript.getInstance(); // create gs instance

        PDFDocument lDocument = new PDFDocument();
        lDocument.load(new File(DEST));

        SimpleRenderer renderer = new SimpleRenderer();

        renderer.setResolution(300);

        try
        {
            images = renderer.render(lDocument);
        }
        catch (RendererException | DocumentException e)
        {
            e.printStackTrace();
        }

        int filename = 1;

        TIFFEncodeParam params = new TIFFEncodeParam();

        for (Image value : images) {
            BufferedImage image = (BufferedImage) value;

            FileOutputStream os = new FileOutputStream(/*outputDir + */ filename + ".tif");

            JAI.create("encode", image, os, "TIFF", params);

            filename++;
        }

        BufferedImage[] image = new BufferedImage[filename];
        for (int i = 0; i < filename; i++) {
            SeekableStream ss = new FileSeekableStream((i + 1) + ".tif");
            ImageDecoder decoder = ImageCodec.createImageDecoder("tiff", ss, null);
            PlanarImage pi = new NullOpImage(decoder.decodeAsRenderedImage(0),null,null, OpImage.OP_IO_BOUND);
            image[i] = pi.getAsBufferedImage();
            ss.close();
        }

        TIFFEncodeParam param = new TIFFEncodeParam();
        param.setCompression(TIFFEncodeParam.COMPRESSION_DEFLATE);
        OutputStream out = new FileOutputStream(filename +".tif");
        ImageEncoder encoder = ImageCodec.createImageEncoder("tiff", out, param);
        List <BufferedImage>list = new ArrayList<>(image.length);

        for (int i = 1; i < image.length; i++) {
            list.add(image[i]);
        }

        params.setExtraImages(list.iterator());
        encoder.encode(image[0]);
        out.close();

        System.out.println("Done.");
    }

}