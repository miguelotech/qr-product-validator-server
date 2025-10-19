package com.qrproduct.qr_product_validator_server.controller;

import com.qrproduct.qr_product_validator_server.model.Product;
import com.qrproduct.qr_product_validator_server.service.ProductService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin/products")
@Tag(name = "Productos", description = "Gestión de productos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // puedes limitar luego al dominio de tu frontend
public class ProductController {

    private final ProductService service;

    @GetMapping
    @Operation(summary = "Listar productos", description = "Obtiene el listado completo de productos")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(service.getAllProducts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto", description = "Obtiene un producto por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(service.getProduct(id));
    }

    @PostMapping
    @Operation(summary = "Crear producto", description = "Crea un nuevo producto")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product created = service.createProduct(product);
        return ResponseEntity.ok(created);
    }

    @PostMapping(value = "/with-image", consumes = "multipart/form-data")
    @Operation(summary = "Crear producto con imagen", description = "Crea un nuevo producto con imagen adjunta")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos o imagen inválida")
    })
    public ResponseEntity<Product> createProductWithImage(
            @Parameter(description = "Nombre del producto") @RequestParam("name") String name,
            @Parameter(description = "Registro sanitario") @RequestParam("registroSanitario") String registroSanitario,
            @Parameter(description = "Imagen del producto") @RequestParam("image") MultipartFile image) {
        
        Product product = Product.builder()
                .name(name)
                .registroSanitario(registroSanitario)
                .build();
        
        Product created = service.createProductWithImage(product, image);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return ResponseEntity.ok(service.updateProduct(id, product));
    }

    @PutMapping(value = "/{id}/with-image", consumes = "multipart/form-data")
    @Operation(summary = "Actualizar producto con imagen", description = "Actualiza un producto existente con nueva imagen")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "400", description = "Error en los datos o imagen inválida")
    })
    public ResponseEntity<Product> updateProductWithImage(
            @PathVariable Long id,
            @Parameter(description = "Nombre del producto") @RequestParam("name") String name,
            @Parameter(description = "Registro sanitario") @RequestParam("registroSanitario") String registroSanitario,
            @Parameter(description = "Nueva imagen del producto (opcional)") @RequestParam(value = "image", required = false) MultipartFile image) {
        
        Product productData = Product.builder()
                .name(name)
                .registroSanitario(registroSanitario)
                .build();
        
        Product updated = service.updateProductWithImage(id, productData, image);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Eliminado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "409", description = "No se puede borrar: tiene lotes asociados")
    })
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
