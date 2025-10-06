//package com.qrproduct.qr_product_validator_server.service;
//
//import com.qrproduct.qr_product_validator_server.model.ProductBatch;
//import com.qrproduct.qr_product_validator_server.repository.ProductBatchRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class ValidationService {
//
//    private final ProductBatchRepository batchRepo;
//
//    public Map<String, Object> validateBatch(UUID batchId) {
//        Optional<ProductBatch> batch = batchRepo.findById(batchId);
//        Map<String, Object> result = new HashMap<>();
//
//        if (batch.isPresent()) {
//            result.put("valid", true);
//            result.put("batch", batch.get());
//        } else {
//            result.put("valid", false);
//            result.put("message", "El saco no es auténtico o no está registrado.");
//        }
//        return result;
//    }
//}
