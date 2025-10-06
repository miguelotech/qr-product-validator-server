package com.qrproduct.qr_product_validator_server.controller;

import com.qrproduct.qr_product_validator_server.model.ProductBatch;
import com.qrproduct.qr_product_validator_server.service.ProductBatchService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@Tag(name = "Público", description = "Consulta pública para validar sacos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PublicController {

    private final ProductBatchService batchService;

    @GetMapping("/{batchId}")
    @Operation(summary = "Validar QR", description = "Devuelve información pública del lote por UUID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lote válido"),
            @ApiResponse(responseCode = "404", description = "Lote no encontrado o inválido")
    })
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

