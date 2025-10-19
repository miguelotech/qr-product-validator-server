package com.qrproduct.qr_product_validator_server.service;

import com.qrproduct.qr_product_validator_server.model.Product;
import com.qrproduct.qr_product_validator_server.repository.ProductRepository;
import com.qrproduct.qr_product_validator_server.repository.ProductBatchRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repo;
    private final ProductBatchRepository batchRepo;
    private final ImageService imageService;

    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    public Product getProduct(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
    }

    public Product createProduct(Product product) {
        return repo.save(product);
    }

    /**
     * Crea un producto con imagen
     * @param product datos del producto
     * @param imageFile archivo de imagen
     * @return producto creado con imagen guardada
     */
    public Product createProductWithImage(Product product, MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = imageService.saveProductImage(imageFile);
            product.setImage(imagePath);
        }
        return repo.save(product);
    }

    public Product updateProduct(Long id, Product data) {
        Product existing = getProduct(id);
        existing.setName(data.getName());
        existing.setImage(data.getImage());
        existing.setRegistroSanitario(data.getRegistroSanitario());
        return repo.save(existing);
    }

    /**
     * Actualiza un producto con nueva imagen
     * @param id ID del producto
     * @param data datos del producto
     * @param imageFile archivo de imagen (opcional)
     * @return producto actualizado
     */
    public Product updateProductWithImage(Long id, Product data, MultipartFile imageFile) {
        Product existing = getProduct(id);
        
        // Si se proporciona una nueva imagen, guardarla y eliminar la anterior
        if (imageFile != null && !imageFile.isEmpty()) {
            // Eliminar imagen anterior si existe
            imageService.deleteProductImage(existing.getImage());
            
            // Guardar nueva imagen
            String imagePath = imageService.saveProductImage(imageFile);
            existing.setImage(imagePath);
        }
        
        // Actualizar otros campos
        existing.setName(data.getName());
        existing.setRegistroSanitario(data.getRegistroSanitario());
        
        return repo.save(existing);
    }

    public void deleteProduct(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Producto no encontrado");
        }
        if (batchRepo.existsByProductId(id)) {
            throw new IllegalStateException("No se puede borrar el producto: tiene lotes asociados");
        }
        
        // Eliminar imagen asociada antes de borrar el producto
        Product product = getProduct(id);
        imageService.deleteProductImage(product.getImage());
        
        repo.deleteById(id);
    }
}

