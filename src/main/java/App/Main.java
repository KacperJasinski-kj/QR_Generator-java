package App;

import javax.swing.JOptionPane;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

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

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.image.BufferedImage;

import java.awt.Desktop;

import java.io.File;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String ssid = JOptionPane.showInputDialog(
                null,
                "Introduce el SSID:",
                "Generador QR WiFi",
                JOptionPane.PLAIN_MESSAGE
        );

        if (ssid == null || ssid.isEmpty()) {

            JOptionPane.showMessageDialog(
                    null,
                    "SSID no puede estar vacío."
            );

            return;
        }

        if (ssid.length() > 32) {

            JOptionPane.showMessageDialog(
                    null,
                    "SSID no puede contener más de 32 caracteres."
            );

            return;
        }


        String password = JOptionPane.showInputDialog(
                null,
                "Introduce la contraseña:",
                "Generador QR WiFi",
                JOptionPane.PLAIN_MESSAGE
        );

        if (password == null || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    null,
                    "La contraseña no puede estar vacía."
            );

            return;
        }
        try {

            generarPDF(ssid, password);

            JOptionPane.showMessageDialog(
                    null,
                    "PDF generado correctamente."
            );
        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error: " + e.getMessage()
            );

        }
    }

    public static void generarPDF(String ssid, String password) throws Exception {

        /*
         * Datos estándar WiFi QR
         */
        String wifiData = "WIFI:T:WPA;S:" + ssid + ";P:" + password + ";;";

        /*
         * Tamaño QR
         */
        int tamanoX = 150;
        int tamanoY = 150;

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.MARGIN, 1);

        BitMatrix matrix = new MultiFormatWriter().encode(
                wifiData,
                BarcodeFormat.QR_CODE,
                tamanoX,
                tamanoY,
                hints
        );

        /*
         * Crear imagen QR
         */
        BufferedImage qrImage = new BufferedImage(
                tamanoX,
                tamanoY,
                BufferedImage.TYPE_INT_RGB
        );

        /*
         * Pintar QR
         */
        for (int x = 0; x < tamanoX; x++) {

            for (int y = 0; y < tamanoY; y++) {

                qrImage.setRGB(
                        x,
                        y,
                        matrix.get(x, y) ? 0x000000 : 0xFFFFFF
                );
            }
        }

        /*
         * Guardar QR temporal
         */
        File qrFile = new File("qr.png");

        ImageIO.write(qrImage, "png", qrFile);

        /*
         * PDF plantilla
         */
        File plantilla = new File("Router_clientes.pdf");

        /*
         * PDF salida
         */
        File salida = new File("Router_"+ssid+".pdf");

        /*
         * Abrir PDF existente
         */
        PdfReader reader = new PdfReader(plantilla.getAbsolutePath());

        /*
         * Crear PDF salida
         */
        PdfWriter writer = new PdfWriter(salida.getAbsolutePath());

        /*
         * Combinar lectura y escritura
         */
        PdfDocument pdf = new PdfDocument(reader, writer);

        /*
         * Cargar QR como imagen
         */
        ImageData qrData = ImageDataFactory.create(qrFile.getAbsolutePath());

        /*
         * Obtener página 1
         */

        PdfCanvas canvas = new PdfCanvas(pdf.getPage(1));

        Rectangle pageSize = pdf.getPage(1).getPageSize();

        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        float fontSize = 6f;

        // SSID
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

        // PASSWORD
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

        /*
         * Posición y tamaño QR
         *
         * x = izquierda -> derecha
         * y = abajo -> arriba
         */

        float x = 25f;
        float y = 43f;
        float ancho = 57f;
        float alto = 57f;

        Rectangle rect = new Rectangle(x, y, ancho, alto);

        /*
         * Insertar QR en el PDF
         */
        canvas.addImageFittedIntoRectangle(qrData, rect, false);

        float margen = 8f;

        // Dibujar el marco en la primera página

        canvas.setStrokeColor(ColorConstants.BLACK); // Color del marco
        canvas.setLineWidth(1f);                      // Grosor del marco
        canvas.roundRectangle(margen, 14f, 255,144.5,25f);
        canvas.stroke();

        /*
         * Guardar y cerrar PDF
         */
        pdf.close();

        /*
         * Abrir PDF automáticamente
         */
        Desktop desktop = Desktop.getDesktop();

        desktop.open(salida);

        /*
         * Borrar QR temporal
         */
        qrFile.delete();
    }
}
