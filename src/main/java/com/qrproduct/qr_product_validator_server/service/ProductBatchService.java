package com.qrproduct.qr_product_validator_server.service;

import com.qrproduct.qr_product_validator_server.model.Product;
import com.qrproduct.qr_product_validator_server.model.ProductBatch;
import com.qrproduct.qr_product_validator_server.repository.ProductBatchRepository;
import com.qrproduct.qr_product_validator_server.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductBatchService {

    private final ProductBatchRepository batchRepo;
    private final ProductRepository productRepo;
    private final QrService qrService;

    public List<ProductBatch> getBatches(Long productId) {
        return batchRepo.findByProductId(productId);
    }

    public ProductBatch getBatch(UUID id) {
        return batchRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lote no encontrado"));
    }

    public ProductBatch createBatch(Long productId, ProductBatch data) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        data.setProduct(product);
        ProductBatch saved = batchRepo.save(data);

        // generar QR luego de guardar (tiene UUID)
        String qrUrl = qrService.generateQrForBatch(saved.getId());
        saved.setQrCodeUrl(qrUrl);

        return batchRepo.save(saved);
    }

    public ProductBatch updateBatch(UUID batchId, ProductBatch data) {
        ProductBatch existing = getBatch(batchId);

        existing.setNumeroMaquilla(data.getNumeroMaquilla());
        existing.setVariedadArroz(data.getVariedadArroz());
        existing.setFechaProduccion(data.getFechaProduccion());
        existing.setFechaVencimiento(data.getFechaVencimiento());

        return batchRepo.save(existing);
    }

    public void deleteBatch(UUID batchId) {
        if (!batchRepo.existsById(batchId)) {
            throw new EntityNotFoundException("Lote no encontrado");
        }
        batchRepo.deleteById(batchId);
    }
}
