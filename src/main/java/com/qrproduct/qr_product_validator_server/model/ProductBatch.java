package com.qrproduct.qr_product_validator_server.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "product_batches")
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductBatch {

    @Id
    @GeneratedValue
    @UuidGenerator // Hibernate genera UUID automáticamente
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private String numeroMaquilla;
    private String variedadArroz;
    private LocalDate fechaProduccion;
    private LocalDate fechaVencimiento;

    private String qrCodeUrl; // ruta/imagen QR generado
}
