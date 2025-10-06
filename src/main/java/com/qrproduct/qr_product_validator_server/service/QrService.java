package com.qrproduct.qr_product_validator_server.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class QrService {

    public String generateQrForBatch(UUID batchId) {
        // url del frontend
        String publicUrl = "http://localhost:3000/products/" + batchId; // URL que irá en el QR

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix matrix = qrCodeWriter.encode(publicUrl, BarcodeFormat.QR_CODE, 300, 300);

            String folder = "uploads/qrs";
            Files.createDirectories(Paths.get(folder));

            String filePath = folder + "/" + batchId + ".png";
            Path path = Paths.get(filePath);
            MatrixToImageWriter.writeToPath(matrix, "PNG", path);

            return "/" + filePath; // ruta accesible para frontend
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Error generando QR", e);
        }
    }
}
