package com.qrproduct.qr_product_validator_server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configurar acceso a archivos estáticos de productos
        registry.addResourceHandler("/uploads/products/**")
                .addResourceLocations("file:uploads/products/")
                .setCachePeriod(3600); // Cache por 1 hora
        
        // Configurar acceso a archivos QR existentes
        registry.addResourceHandler("/uploads/qrs/**")
                .addResourceLocations("file:uploads/qrs/")
                .setCachePeriod(3600);
    }
}
