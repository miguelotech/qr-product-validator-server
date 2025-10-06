package com.qrproduct.qr_product_validator_server.service;

import com.qrproduct.qr_product_validator_server.model.Product;
import com.qrproduct.qr_product_validator_server.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repo;

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

    public Product updateProduct(Long id, Product data) {
        Product existing = getProduct(id);
        existing.setName(data.getName());
        existing.setImage(data.getImage());
        existing.setRegistroSanitario(data.getRegistroSanitario());
        return repo.save(existing);
    }

    public void deleteProduct(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Producto no encontrado");
        }
        repo.deleteById(id);
    }
}

