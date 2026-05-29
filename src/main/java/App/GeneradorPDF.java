package App;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import java.awt.Desktop;
import java.io.File;

public class GeneradorPDF {

    public static void generarPDF( String ssid, String password, File plantilla, File qrFile, float qrX, float qrY)
            throws Exception {

        File salida = new File("Router_" + ssid + ".pdf");

        PdfReader reader = new PdfReader(plantilla.getAbsolutePath());

        PdfWriter writer = new PdfWriter(salida.getAbsolutePath());

        PdfDocument pdf = new PdfDocument(reader, writer);

        ImageData qrData = ImageDataFactory.create(qrFile.getAbsolutePath());

        PdfCanvas canvas = new PdfCanvas(pdf.getPage(1));

        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        /*
        Opcional para añadir el ssid y el password al pdf

        float fontSize = 6f;

        float ssidBoxX = 165f;
        float ssidBoxY = 76f;
        float ssidBoxWidth = 80f;

        float ssidTextWidth = font.getWidth(ssid, fontSize);
        float ssidX = ssidBoxX + (ssidBoxWidth - ssidTextWidth) / 2;

        canvas.beginText();
        canvas.setFontAndSize(font, fontSize);
        canvas.setFillColor(ColorConstants.WHITE);
        canvas.moveText(ssidX, ssidBoxY);
        canvas.showText(ssid);
        canvas.endText();

        float passwordBoxX = 180f;
        float passwordBoxY = 42f;
        float passwordBoxWidth = 60f;

        float passwordTextWidth = font.getWidth(password, fontSize);
        float passwordX = passwordBoxX + (passwordBoxWidth - passwordTextWidth) / 2;

        canvas.beginText();
        canvas.setFontAndSize(font, fontSize);
        canvas.setFillColor(ColorConstants.WHITE);
        canvas.moveText(passwordX, passwordBoxY);
        canvas.showText(password);
        canvas.endText();
        */

        float x = qrX;
        float y = qrY;
        float ancho = 57f;
        float alto = 57f;

        Rectangle rect = new Rectangle(x, y, ancho, alto);

        canvas.addImageFittedIntoRectangle(qrData, rect, false);


        /*
        Opcional, para margen
        float margen = 8f;

        canvas.setStrokeColor(ColorConstants.BLACK);
        canvas.setLineWidth(1f);
        canvas.roundRectangle(margen, 14f, 255, 144.5f, 25f);
        canvas.stroke();
        */
        pdf.close();

        Desktop desktop = Desktop.getDesktop();
        desktop.open(salida);

        qrFile.delete();
    }
}