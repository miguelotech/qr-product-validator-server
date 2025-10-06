package com.qrproduct.qr_product_validator_server.controller;

import com.qrproduct.qr_product_validator_server.model.ProductBatch;
import com.qrproduct.qr_product_validator_server.service.ProductBatchService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/products/{productId}/batches")
@Tag(name = "Lotes", description = "Gestión de lotes por producto")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductBatchController {

    private final ProductBatchService service;

    @GetMapping
    @Operation(summary = "Listar lotes", description = "Obtiene todos los lotes de un producto")
    public ResponseEntity<List<ProductBatch>> getAllBatches(@PathVariable Long productId) {
        return ResponseEntity.ok(service.getBatches(productId));
    }

    @GetMapping("/{batchId}")
    @Operation(summary = "Obtener lote", description = "Obtiene un lote por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lote encontrado"),
            @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    })
    public ResponseEntity<ProductBatch> getBatch(
            @PathVariable Long productId,
            @PathVariable UUID batchId) {
        return ResponseEntity.ok(service.getBatch(batchId));
    }

    @PostMapping
    @Operation(summary = "Crear lote", description = "Crea un lote asociado a un producto")
    public ResponseEntity<ProductBatch> createBatch(
            @PathVariable Long productId,
            @RequestBody ProductBatch batch) {
        ProductBatch created = service.createBatch(productId, batch);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{batchId}")
    @Operation(summary = "Actualizar lote", description = "Actualiza un lote existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lote actualizado"),
            @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    })
    public ResponseEntity<ProductBatch> updateBatch(
            @PathVariable Long productId,
            @PathVariable UUID batchId,
            @RequestBody ProductBatch batch) {
        return ResponseEntity.ok(service.updateBatch(batchId, batch));
    }

    @DeleteMapping("/{batchId}")
    @Operation(summary = "Eliminar lote", description = "Elimina un lote por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Eliminado"),
            @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    })
    public ResponseEntity<Void> deleteBatch(
            @PathVariable Long productId,
            @PathVariable UUID batchId) {
        service.deleteBatch(batchId);
        return ResponseEntity.noContent().build();
    }
}
