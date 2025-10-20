package com.qrproduct.qr_product_validator_server.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    private static final String UPLOAD_DIR = "uploads/products";
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".webp"};
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    /**
     * Guarda una imagen de producto en la carpeta local
     * @param file archivo de imagen a guardar
     * @return ruta relativa de la imagen guardada
     */
    public String saveProductImage(MultipartFile file) {
        try {
            // Validar archivo
            validateImageFile(file);
            
            // Crear directorio si no existe
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Generar nombre único para el archivo
            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
            
            // Guardar archivo
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath);
            
            // Retornar ruta relativa para acceso web
            return "/" + UPLOAD_DIR + "/" + uniqueFilename;
            
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen del producto", e);
        }
    }
    
    /**
     * Elimina una imagen de producto
     * @param imagePath ruta de la imagen a eliminar
     */
    public void deleteProductImage(String imagePath) {
        try {
            if (imagePath != null && !imagePath.isEmpty()) {
                // Remover el "/" inicial si existe
                String cleanPath = imagePath.startsWith("/") ? imagePath.substring(1) : imagePath;
                Path filePath = Paths.get(cleanPath);
                
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar la imagen del producto", e);
        }
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

