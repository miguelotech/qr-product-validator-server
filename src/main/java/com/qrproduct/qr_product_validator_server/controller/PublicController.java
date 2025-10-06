package com.qrproduct.qr_product_validator_server.controller;

import com.qrproduct.qr_product_validator_server.model.ProductBatch;
import com.qrproduct.qr_product_validator_server.service.ProductBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PublicController {

    private final ProductBatchService batchService;

    @GetMapping("/{batchId}")
    public ResponseEntity<?> getProductInfo(@PathVariable UUID batchId) {
        try {
            ProductBatch batch = batchService.getBatch(batchId);

            // Devolver información pública combinada
            return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "id", batch.getId(),
                    "numeroMaquilla", batch.getNumeroMaquilla(),
                    "variedadArroz", batch.getVariedadArroz(),
                    "fechaProduccion", batch.getFechaProduccion(),
                    "fechaVencimiento", batch.getFechaVencimiento(),
                    "product", Map.of(
                            "id", batch.getProduct().getId(),
                            "name", batch.getProduct().getName(),
                            "image", batch.getProduct().getImage(),
                            "registroSanitario", batch.getProduct().getRegistroSanitario()
                    )
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("valid", false, "message", "El saco no es auténtico o no está registrado."));
        }
    }
}

