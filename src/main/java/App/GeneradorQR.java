package App;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

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
                BufferedImage.TYPE_INT_RGB
        );

        for (int x = 0; x < tamanoX; x++) {
            for (int y = 0; y < tamanoY; y++) {
                qrImage.setRGB(
                        x,
                        y,
                        matrix.get(x, y) ? 0x000000 : 0xFFFFFF
                );
            }
        }

        File qrFile = new File("qr.png");

        ImageIO.write(qrImage, "png", qrFile);

        return qrFile;
    }
}