package App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class PanelPreviewPDF extends JPanel {

    private BufferedImage imagenPDF;

    private int startX;
    private int startY;
    private int endX;
    private int endY;

    private boolean dibujando = false;

    public PanelPreviewPDF(BufferedImage imagenPDF) {
        this.imagenPDF = imagenPDF;

        setPreferredSize(
                new Dimension(
                        imagenPDF.getWidth(),
                        imagenPDF.getHeight()
                )
        );

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                startX = e.getX();
                startY = e.getY();

                endX = startX;
                endY = startY;

                dibujando = true;

                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                endX = e.getX();
                endY = e.getY();

                dibujando = false;

                repaint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                endX = e.getX();
                endY = e.getY();

                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(imagenPDF, 0, 0, null);

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(2f));

        int x = Math.min(startX, endX);
        int y = Math.min(startY, endY);

        int ancho = Math.abs(endX - startX);
        int alto = Math.abs(endY - startY);

        if (ancho > 0 && alto > 0) {
            g2.drawRect(x, y, ancho, alto);
        }
    }

    public Rectangle getRectanguloSeleccionado() {

        int x = Math.min(startX, endX);
        int y = Math.min(startY, endY);

        int ancho = Math.abs(endX - startX);
        int alto = Math.abs(endY - startY);

        return new Rectangle(x, y, ancho, alto);
    }
}
