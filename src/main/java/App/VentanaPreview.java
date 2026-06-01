package App;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class VentanaPreview extends JFrame {

    private String ssid;
    private String password;
    private File pdfFile;

    private BufferedImage imagenPDF;

    public VentanaPreview(String ssid, String password, File pdfFile) {

        this.ssid = ssid;
        this.password = password;
        this.pdfFile = pdfFile;

        setTitle("Selecciona posición y tamaño del QR");
        setSize(600, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        cargarPreview();

        PanelPreviewPDF panelPreview = new PanelPreviewPDF(imagenPDF);

        JScrollPane scrollPane = new JScrollPane(panelPreview);

        JButton btnGenerar = new JButton("Generar PDF");

        add(scrollPane, BorderLayout.CENTER);
        add(btnGenerar, BorderLayout.SOUTH);

        btnGenerar.addActionListener(e -> {

            try {
                Rectangle rect = panelPreview.getRectanguloSeleccionado();

                tamanoQRvalido(rect);


                    File qrFile = GeneradorQR.generarQR(ssid, password);

                    float pdfX = rect.x;

                    float pdfY = imagenPDF.getHeight() - rect.y - rect.height;

                    GeneradorPDF.generarPDF(
                            ssid,
                            password,
                            pdfFile,
                            qrFile,
                            pdfX,
                            pdfY,
                            rect.width,
                            rect.height
                    );

                    JOptionPane.showMessageDialog(
                            null,
                            "PDF generado correctamente."
                    );




            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        null,
                        "Error: " + ex.getMessage()
                );
            }
        });
    }

    private void cargarPreview() {
        try {

            PDDocument document = Loader.loadPDF(pdfFile);

            PDFRenderer renderer = new PDFRenderer(document);

            imagenPDF = renderer.renderImageWithDPI(0, 100);

            document.close();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error cargando preview: " + e.getMessage()
            );
        }
    }
    public boolean tamanoQRvalido(Rectangle rect) {
        if (imagenPDF.getHeight() < rect.getHeight() || imagenPDF.getWidth() < rect.getWidth()){
            JOptionPane.showMessageDialog(
                    null,
                    "El QR no puede ser mas grande que el pdf."
            );
            return false;

        } else if (rect.width < 10 || rect.height < 10) {
            JOptionPane.showMessageDialog(
                    null,
                    "Selecciona un área más grande para el QR."
            );
            return false;
        }
        return true;
    }
}