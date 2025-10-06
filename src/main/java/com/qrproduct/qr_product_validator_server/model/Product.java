package com.qrproduct.qr_product_validator_server.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // numérico secuencial
    private Long id;

    private String name;
    private String image;
    private String registroSanitario;
}

