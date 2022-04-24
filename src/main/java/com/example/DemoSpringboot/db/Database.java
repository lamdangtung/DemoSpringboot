package com.example.DemoSpringboot.db;

import com.example.DemoSpringboot.entity.Product;
import com.example.DemoSpringboot.repo.ProductRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class Database {
    @Bean
    CommandLineRunner initDatabase(ProductRepo productRepo){
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                List<Product> products = new ArrayList<>();
                for(int i = 0; i < 10; i++){
                    Product product = new Product("productName #" + Integer.toString(i), i, Double.valueOf(i),"url #" + Integer.toString(i));
                    products.add(product);
                }
                 productRepo.saveAll(products);
            }
        };

    }

}
