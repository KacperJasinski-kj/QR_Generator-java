package App;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
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

        setTitle("Selecciona posición del QR");
        setSize(600, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        cargarPreview();

        JLabel label = new JLabel(new ImageIcon(imagenPDF));
        JScrollPane scrollPane = new JScrollPane(label);

        add(scrollPane);

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                try {
                    File qrFile = GeneradorQR.generarQR(ssid, password);

                    float escalaX = (float) imagenPDF.getWidth() / label.getWidth();
                    float escalaY = (float) imagenPDF.getHeight() / label.getHeight();

                    float clickX = e.getX() * escalaX;
                    float clickY = e.getY() * escalaY;

                    /*
                     * Convertir coordenadas Swing a coordenadas PDF
                     * Swing: Y empieza arriba
                     * PDF: Y empieza abajo
                     */
                    float pdfX = clickX;
                    float pdfY = imagenPDF.getHeight() - clickY;

                    GeneradorPDF.generarPDF(
                            ssid,
                            password,
                            pdfFile,
                            qrFile,
                            pdfX,
                            pdfY
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
            }
        });
    }

    private void cargarPreview()     {
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
}