package org.example.hms2026.util;

import org.example.hms2026.model.Billing;

import java.io.File;
import java.lang.reflect.Method;

public class InvoicePDFGenerator {

    public static void generateInvoicePDF(Billing bill, String filePath) {
        try {
            // ប្រើប្រាស់ Reflection ដើម្បីចៀសវាងបញ្ហា Module System Restrictions របស់ iText 7
            Class<?> pdfWriterClass = Class.forName("com.itextpdf.kernel.pdf.PdfWriter");
            Class<?> pdfDocClass = Class.forName("com.itextpdf.kernel.pdf.PdfDocument");
            Class<?> documentClass = Class.forName("com.itextpdf.layout.Document");
            Class<?> paragraphClass = Class.forName("com.itextpdf.layout.element.Paragraph");

            // PdfWriter writer = new PdfWriter(new File(filePath));
            Object writer = pdfWriterClass.getConstructor(File.class).newInstance(new File(filePath));

            // PdfDocument pdfDoc = new PdfDocument(writer);
            Object pdfDoc = pdfDocClass.getConstructor(pdfWriterClass).newInstance(writer);

            // Document document = new Document(pdfDoc);
            Object document = documentClass.getConstructor(pdfDocClass).newInstance(pdfDoc);


            addParagraph(documentClass, document, paragraphClass, "HealthTech Hospital - Medical Invoice", true, 20);
            addParagraph(documentClass, document, paragraphClass, "--------------------------------------------------------------------------------", false, 12);

            addParagraph(documentClass, document, paragraphClass, "Bill ID: " + bill.getBillId(), false, 12);
            addParagraph(documentClass, document, paragraphClass, "Patient Name: " + bill.getPatientName(), false, 12);
            addParagraph(documentClass, document, paragraphClass, "Doctor Name: " + bill.getDoctorName(), false, 12);
            addParagraph(documentClass, document, paragraphClass, "Status: " + bill.getStatus(), false, 12);

            addParagraph(documentClass, document, paragraphClass, "\nService & Consultation Details:", true, 12);
            addParagraph(documentClass, document, paragraphClass, bill.getMedicineDetails(), false, 12);

            addParagraph(documentClass, document, paragraphClass, "\n--------------------------------------------------------------------------------", false, 12);

            addParagraph(documentClass, document, paragraphClass, "Grand Total: $" + String.format("%.2f", bill.getTotalAmount()), true, 14);
            addParagraph(documentClass, document, paragraphClass, "\nThank you for choosing HealthTech Hospital. Get well soon!", false, 10);

            // document.close();
            documentClass.getMethod("close").invoke(document);
            System.out.println("PDF Generated successfully at: " + filePath);

        } catch (Exception e) {
            System.err.println("❌ Error generating PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method សម្រាប់បង្កើត Paragraph និង Format តាម Reflection
    private static void addParagraph(Class<?> docClass, Object doc, Class<?> pClass, String text, boolean isBold, float fontSize) {
        try {
            Object p = pClass.getConstructor(String.class).newInstance(text);
            if (isBold) {
                Method setBold = pClass.getMethod("setBold");
                setBold.invoke(p);
            }
            if (fontSize > 0) {
                Method setFontSize = pClass.getMethod("setFontSize", float.class);
                setFontSize.invoke(p, fontSize);
            }
            Method add = docClass.getMethod("add", Class.forName("com.itextpdf.layout.element.IBlockElement"));
            add.invoke(doc, p);
        } catch (Exception e) {

            try {
                Method add = docClass.getMethod("add", Class.forName("com.itextpdf.layout.element.BlockElement"));
                Object p = pClass.getConstructor(String.class).newInstance(text);
                add.invoke(doc, p);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}