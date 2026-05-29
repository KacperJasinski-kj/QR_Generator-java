package App;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.util.List;

public class VentanaFormulario extends JFrame {

    private JTextField txtSsid;
    private JTextField txtPassword;
    private JLabel lblPdf;
    private File pdfSeleccionado;

    public VentanaFormulario() {

        setTitle("Generador QR WiFi");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1));

        txtSsid = new JTextField();
        txtPassword = new JTextField();

        lblPdf = new JLabel("Arrastra aquí el PDF", SwingConstants.CENTER);
        lblPdf.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JButton btnContinuar = new JButton("Continuar");

        add(new JLabel("SSID:"));
        add(txtSsid);
        add(new JLabel("Contraseña:"));
        add(txtPassword);
        add(lblPdf);
        add(btnContinuar);

        lblPdf.setTransferHandler(new TransferHandler() {

            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            @Override
            public boolean importData(TransferSupport support) {

                try {
                    // Con esto se obtengo la informacion de los archivos arrastrados (archivo.txt, libro.pdf...)
                    List<File> archivos = (List<File>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

                    File archivo = archivos.get(0);

                    if (!archivo.getName().toLowerCase().endsWith(".pdf")) {
                        JOptionPane.showMessageDialog(null, "Solo se permiten archivos en PDF.");
                        return false;
                    } else if (txtSsid.getText().isEmpty() || txtSsid.getText().length() > 32) {
                        JOptionPane.showMessageDialog(null, "El ssid no puede estar vacio ni contener mas de 32 caracteres.");
                        return false;
                    }else if (txtPassword.getText().isEmpty() || txtPassword.getText().contains(" ") ) {
                        JOptionPane.showMessageDialog(null, "La contraseña no puede estar vacia ni contener espacios.");
                        return false;
                    }

                    pdfSeleccionado = archivo;
                    lblPdf.setText(archivo.getName());

                    return true;

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                    return false;
                }
            }
        });

        btnContinuar.addActionListener(e -> abrirPreview());
    }

    public void iniciar() {
        setVisible(true);
    }

    private void abrirPreview() {

        String ssid = txtSsid.getText();
        String password = new String(txtPassword.getText());

        validarCampos(ssid, password);

        VentanaPreview preview = new VentanaPreview(ssid, password, pdfSeleccionado);
        preview.setVisible(true);
    }

    public void validarCampos(String ssid, String password) {
        if (ssid.isEmpty() || ssid.length() > 32) {
            System.out.println("SSID no puede estar vacío ni tener más de 32 caracteres.");
            return;
        }else if (password.isEmpty() || password.contains(" ")) {
            System.out.println("La contraseña no puede estar vacía ni contener espacios.");
            return;
        } else if (pdfSeleccionado == null) {
            System.out.println("Debes arrastrar un PDF.");
            return;
        }
    }
}
