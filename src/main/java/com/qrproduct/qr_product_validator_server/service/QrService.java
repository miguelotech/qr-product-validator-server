package com.qrproduct.qr_product_validator_server.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QrService {

    private final Cloudinary cloudinary;

    @Value("${app.frontend-url:http://localhost:3000}")
    private String frontendUrl;

    public String generateQrForBatch(UUID batchId) {
        // Construir la URL pública del frontend a usar en el QR
        String base = frontendUrl == null ? "http://localhost:3000" : frontendUrl.trim();
        // remover slash final si existe
        if (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        String publicUrl = base + "/validate/" + batchId; // URL que irá en el QR

        try {
            // Generar el QR
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix matrix = qrCodeWriter.encode(publicUrl, BarcodeFormat.QR_CODE, 300, 300);
            
            // Convertir la matriz a BufferedImage
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(matrix);
            
            // Convertir BufferedImage a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "PNG", baos);
            byte[] imageBytes = baos.toByteArray();

            // Subir a Cloudinary
            Map uploadResult = cloudinary.uploader().upload(
                imageBytes,
                ObjectUtils.asMap(
                    "folder", "qrs",
                    "public_id", batchId.toString(),
                    "resource_type", "auto"
                )
            );

            // Retornar URL de Cloudinary
            return uploadResult.get("secure_url").toString();
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Error generando QR", e);
        }
    }
}
