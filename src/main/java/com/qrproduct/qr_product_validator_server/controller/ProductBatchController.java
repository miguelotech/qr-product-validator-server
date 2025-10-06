package com.qrproduct.qr_product_validator_server.controller;

import com.qrproduct.qr_product_validator_server.model.ProductBatch;
import com.qrproduct.qr_product_validator_server.service.ProductBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/products/{productId}/batches")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductBatchController {

    private final ProductBatchService service;

    @GetMapping
    public ResponseEntity<List<ProductBatch>> getAllBatches(@PathVariable Long productId) {
        return ResponseEntity.ok(service.getBatches(productId));
    }

    @GetMapping("/{batchId}")
    public ResponseEntity<ProductBatch> getBatch(
            @PathVariable Long productId,
            @PathVariable UUID batchId) {
        return ResponseEntity.ok(service.getBatch(batchId));
    }

    @PostMapping
    public ResponseEntity<ProductBatch> createBatch(
            @PathVariable Long productId,
            @RequestBody ProductBatch batch) {
        ProductBatch created = service.createBatch(productId, batch);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{batchId}")
    public ResponseEntity<ProductBatch> updateBatch(
            @PathVariable Long productId,
            @PathVariable UUID batchId,
            @RequestBody ProductBatch batch) {
        return ResponseEntity.ok(service.updateBatch(batchId, batch));
    }

    @DeleteMapping("/{batchId}")
    public ResponseEntity<Void> deleteBatch(
            @PathVariable Long productId,
            @PathVariable UUID batchId) {
        service.deleteBatch(batchId);
        return ResponseEntity.noContent().build();
    }
}
