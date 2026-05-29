package App;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.kernel.colors.ColorConstants;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GeneradorQR {

    public static File generarQR(String ssid, String password) throws Exception {

        String wifiData = "WIFI:T:WPA;S:" + ssid + ";P:" + password + ";;";

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

        BufferedImage qrImage = new BufferedImage(
                tamanoX,
                tamanoY,
                BufferedImage.TYPE_INT_ARGB
        );

        for (int x = 0; x < tamanoX; x++) {
            for (int y = 0; y < tamanoY; y++) {
                qrImage.setRGB(
                        x,
                        y,
                        matrix.get(x, y) ? 0xFF000000 : 0x00FFFFFF
                );
            }
        }


        int padding = 10;
        int radius = 25;

        BufferedImage qrFinal = new BufferedImage(
                tamanoX + padding * 2,
                tamanoY + padding * 2,
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2 = qrFinal.createGraphics();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // Fondo blanco redondeado
        g2.setColor(Color.WHITE);

        g2.fillRoundRect(
                0,
                0,
                qrFinal.getWidth(),
                qrFinal.getHeight(),
                radius,
                radius
        );

        // QR encima
        g2.drawImage(
                qrImage,
                padding,
                padding,
                null
        );

        g2.dispose();

        File qrFile = new File("qr.png");

        ImageIO.write(qrFinal, "png", qrFile);

        return qrFile;
    }
}