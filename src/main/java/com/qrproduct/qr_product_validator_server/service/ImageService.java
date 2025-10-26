package com.qrproduct.qr_product_validator_server.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final Cloudinary cloudinary;
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".webp"};
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    /**
     * Guarda una imagen de producto en Cloudinary
     * @param file archivo de imagen a guardar
     * @return URL de la imagen guardada en Cloudinary
     */
    public String saveProductImage(MultipartFile file) {
        try {
            // Validar archivo
            validateImageFile(file);
            
            // Subir a Cloudinary
            Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                    "folder", "products",
                    "resource_type", "auto"
                )
            );
            
            // Retornar URL de Cloudinary
            return uploadResult.get("secure_url").toString();
            
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen del producto en Cloudinary", e);
        }
    }
    
    /**
     * Elimina una imagen de producto de Cloudinary
     * @param imageUrl URL de la imagen a eliminar
     */
    public void deleteProductImage(String imageUrl) {
        try {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                // Extraer el public_id de la URL de Cloudinary
                String publicId = extractPublicIdFromUrl(imageUrl);
                if (publicId != null) {
                    // Eliminar de Cloudinary
                    cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar la imagen del producto de Cloudinary", e);
        }
    }

    /**
     * Extrae el public_id de una URL de Cloudinary
     */
    private String extractPublicIdFromUrl(String imageUrl) {
        // Ejemplo de URL de Cloudinary: https://res.cloudinary.com/your-cloud-name/image/upload/v1234567890/products/image.jpg
        if (imageUrl.contains("/upload/")) {
            String[] parts = imageUrl.split("/upload/");
            if (parts.length > 1) {
                // Remover la versión y la extensión
                String path = parts[1].replaceAll("v\\d+/", "");
                return path.substring(0, path.lastIndexOf("."));
            }
        }
        return null;
    }
    
    /**
     * Valida que el archivo sea una imagen válida
     */
    private void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo de imagen no puede estar vacío");
        }
        
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("El archivo es demasiado grande. Máximo permitido: 5MB");
        }
        
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("El archivo debe tener un nombre válido");
        }
        
        String fileExtension = getFileExtension(originalFilename).toLowerCase();
        boolean isValidExtension = false;
        
        for (String allowedExt : ALLOWED_EXTENSIONS) {
            if (fileExtension.equals(allowedExt)) {
                isValidExtension = true;
                break;
            }
        }
        
        if (!isValidExtension) {
            throw new IllegalArgumentException("Formato de archivo no permitido. Formatos válidos: " + 
                String.join(", ", ALLOWED_EXTENSIONS));
        }
        
        // Validar tipo MIME
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("El archivo debe ser una imagen válida");
        }
    }
    
    /**
     * Obtiene la extensión de un archivo
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex);
    }
}

