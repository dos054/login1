package com.example.product.config;

import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer {

    private final ProductRepository productRepository;

    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {
        if (productRepository.count() == 0) {
            // 노트북
            Product laptop = new Product();
            laptop.setName("노트북");
            laptop.setDescription("고성능 노트북");
            laptop.setPrice(1200000.0);
            laptop.setStock(10);
            productRepository.save(laptop);

            // 스마트폰
            Product smartphone = new Product();
            smartphone.setName("스마트폰");
            smartphone.setDescription("최신형 스마트폰");
            smartphone.setPrice(800000.0);
            smartphone.setStock(15);
            productRepository.save(smartphone);

            // 헤드폰
            Product headphone = new Product();
            headphone.setName("헤드폰");
            headphone.setDescription("무선 헤드폰");
            headphone.setPrice(300000.0);
            headphone.setStock(20);
            productRepository.save(headphone);

            // 태블릿
            Product tablet = new Product();
            tablet.setName("태블릿");
            tablet.setDescription("10인치 태블릿");
            tablet.setPrice(500000.0);
            tablet.setStock(12);
            productRepository.save(tablet);
        }
    }
}

