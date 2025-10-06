package com.qrproduct.qr_product_validator_server.repository;

import com.qrproduct.qr_product_validator_server.model.ProductBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductBatchRepository extends JpaRepository<ProductBatch, UUID> {

    List<ProductBatch> findByProductId(Long productId);

}
