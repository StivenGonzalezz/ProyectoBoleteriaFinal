package co.edu.uniquindio.edu.co.centroeventosuq.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class GenerarQr {

     public  static final  String RUTA_QRS="C:\\Users\\ANDREY\\Desktop\\Gestion Prom3\\CentroEventosUq\\src\\main\\resources\\QRS\\";
     public static final int tamañoDefinido=300;
    /**
     * Método estático para generar un código QR y guardarlo como una imagen en formato PNG.
     *
     * @param qrCodeText Texto o URL que se desea codificar en el código QR.
     * @param fileName   como se va llamar el codigo al momento de guardarlo en la ruta especifica).
     * @throws WriterException   Si hay un error al generar el código QR.
     * @throws IOException       Si hay un error al guardar la imagen del código QR en el archivo.
     */
    public static void generateQRCode(String qrCodeText, String fileName) throws WriterException, IOException {
        // Configuración de los parámetros para generar el código QR
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        // Crear el objeto QRCodeWriter
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, tamañoDefinido, tamañoDefinido, hints);

        // Convertir el BitMatrix en una imagen BufferedImage
        BufferedImage qrImage = toBufferedImage(bitMatrix);

        // Guardar la imagen en el archivo especificado
        File qrFile = new File(RUTA_QRS+fileName+".jpg");
        ImageIO.write(qrImage, "PNG", qrFile);
    }

    /**
     * Método auxiliar para convertir un BitMatrix en una imagen BufferedImage.
     *
     * @param matrix BitMatrix que representa el código QR.
     * @return La imagen BufferedImage generada a partir del BitMatrix.
     */
    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF); // Color negro para el código y blanco para el fondo
            }
        }

        return image;
    }


}
