package com.qrproduct.qr_product_validator_server.repository;

import com.qrproduct.qr_product_validator_server.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
