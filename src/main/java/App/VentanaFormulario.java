package App;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

public class VentanaFormulario extends JFrame {

    private JTextField txtSsid;
    private JTextField txtPassword;
    private JLabel lblPdf;
    private File pdfSeleccionado;

    public VentanaFormulario() {

        setTitle("Generador QR WiFi");
        setSize(480, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout(15, 15));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        panelPrincipal.setBackground(new Color(245, 247, 250));

        JLabel titulo = new JLabel("Generador QR", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(new Color(35, 45, 65));

        JPanel panelCampos = new JPanel(new GridLayout(4, 1, 8, 8));
        panelCampos.setOpaque(false);

        JLabel lblSsid = new JLabel("SSID:");
        lblSsid.setFont(new Font("Arial", Font.BOLD, 13));

        txtSsid = new JTextField();
        txtSsid.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Arial", Font.BOLD, 13));

        txtPassword = new JTextField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));

        panelCampos.add(lblSsid);
        panelCampos.add(txtSsid);
        panelCampos.add(lblPassword);
        panelCampos.add(txtPassword);

        lblPdf = new JLabel("<html><center>Arrastra un PDF aquí<br>o haz click para seleccionarlo</center></html>", SwingConstants.CENTER);
        lblPdf.setFont(new Font("Arial", Font.BOLD, 14));
        lblPdf.setForeground(new Color(80, 90, 110));
        lblPdf.setOpaque(true);
        lblPdf.setBackground(Color.WHITE);
        lblPdf.setBorder(BorderFactory.createDashedBorder(new Color(90, 120, 180), 2, 6));


        JButton btnContinuar = new JButton("Continuar");
        btnContinuar.setFont(new Font("Arial", Font.BOLD, 14));
        btnContinuar.setBackground(new Color(45, 110, 220));
        btnContinuar.setForeground(Color.WHITE);
        btnContinuar.setFocusPainted(false);

        panelPrincipal.add(titulo, BorderLayout.NORTH);
        panelPrincipal.add(panelCampos, BorderLayout.CENTER);
        panelPrincipal.add(lblPdf, BorderLayout.WEST);
        panelPrincipal.add(btnContinuar, BorderLayout.SOUTH);

        lblPdf.setPreferredSize(new Dimension(170, 100));

        add(panelPrincipal);

        lblPdf.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                JFileChooser fileChooser = new JFileChooser();

                fileChooser.setDialogTitle("Seleccionar PDF");

                int resultado = fileChooser.showOpenDialog(null);

                if (resultado == JFileChooser.APPROVE_OPTION) {

                    File archivo = fileChooser.getSelectedFile();

                    if (!archivo.getName().toLowerCase().endsWith(".pdf")) {

                        JOptionPane.showMessageDialog(null, "Solo se permiten archivos PDF.");

                        return;
                    }

                    pdfSeleccionado = archivo;

                    lblPdf.setText("<html><center>PDF seleccionado:<br>" + archivo.getName() + "</center></html>");

                    lblPdf.setForeground(new Color(20, 120, 70));
                }
            }
        });

        lblPdf.setTransferHandler(new TransferHandler() {

            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            @Override
            public boolean importData(TransferSupport support) {

                try {
                    List<File> archivos = (List<File>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

                    File archivo = archivos.get(0);

                    if (!archivo.getName().toLowerCase().endsWith(".pdf")) {
                        JOptionPane.showMessageDialog(null, "Solo se permiten archivos en PDF.");
                        return false;
                    }

                    pdfSeleccionado = archivo;
                    lblPdf.setText("<html><center>PDF seleccionado:<br>" + archivo.getName() + "</center></html>");
                    lblPdf.setForeground(new Color(20, 120, 70));
                    lblPdf.setBorder(BorderFactory.createLineBorder(new Color(20, 120, 70), 2));

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
        String password = txtPassword.getText();

        if (!validarCampos(ssid, password)) {
            return;
        }

        VentanaPreview preview = new VentanaPreview(ssid, password, pdfSeleccionado);
        preview.setVisible(true);
    }

    public boolean validarCampos(String ssid, String password) {

        if (ssid.isEmpty() || ssid.length() > 32) {
            JOptionPane.showMessageDialog(null, "SSID no puede estar vacío ni tener más de 32 caracteres.");
            return false;
        }

        if (password.isEmpty() || password.contains(" ")) {
            JOptionPane.showMessageDialog(null, "La contraseña no puede estar vacía ni contener espacios.");
            return false;
        }

        if (pdfSeleccionado == null) {
            JOptionPane.showMessageDialog(null, "Debes arrastrar un PDF.");
            return false;
        }

        return true;
    }
}