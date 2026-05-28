package App;

import javax.swing.JOptionPane;
import java.io.File;

public class VentanaFormulario {

    public void iniciar() {

        String ssid = JOptionPane.showInputDialog(
                null,
                "Introduce el SSID:",
                "Generador QR WiFi",
                JOptionPane.PLAIN_MESSAGE
        );

        if (ssid == null || ssid.isEmpty()) {
            JOptionPane.showMessageDialog(null, "SSID no puede estar vacío.");
            return;
        }

        if (ssid.length() > 32) {
            JOptionPane.showMessageDialog(null, "SSID no puede contener más de 32 caracteres.");
            return;
        }

        String password = JOptionPane.showInputDialog(
                null,
                "Introduce la contraseña:",
                "Generador QR WiFi",
                JOptionPane.PLAIN_MESSAGE
        );

        if (password == null || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "La contraseña no puede estar vacía.");
            return;
        }

        try {
            File qrFile = GeneradorQR.generarQR(ssid, password);

            GeneradorPDF.generarPDF(ssid, password, qrFile);

            JOptionPane.showMessageDialog(null, "PDF generado correctamente.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}
