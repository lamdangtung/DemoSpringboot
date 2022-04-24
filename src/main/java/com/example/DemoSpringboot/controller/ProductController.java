package com.example.DemoSpringboot.controller;

import com.example.DemoSpringboot.entity.Product;
import com.example.DemoSpringboot.entity.ResponseObject;
import com.example.DemoSpringboot.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/products")
public class ProductController {

    @Autowired
    private ProductRepo productRepo;

    @GetMapping("")
    List<Product> getAllProducts(){
        return productRepo.findAll();
    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id){
        Optional<Product> foundProduct = productRepo.findById(id);
        if( foundProduct.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok","Query product successfully", foundProduct)
            );
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("false", "Cannot find product with id = " + id, null)
            );
        }
    }

    @PostMapping("")
    ResponseEntity<ResponseObject> insertProduct(@RequestBody Product newProduct){
        List<Product> foundProducts = productRepo.findByProductName(newProduct.getProductName().trim());
        if(foundProducts.size() > 0 ){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("failed", "Product name already taken", null)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Insert product successfully", productRepo.save(newProduct))
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateProduct(@RequestBody Product updateProduct, @PathVariable Long id){
        Product updatedProduct =  productRepo.findById(id).map(product -> {
            product.setProductName(updateProduct.getProductName());
            product.setPrice(updateProduct.getPrice());
            product.setYear(updateProduct.getYear());
            product.setUrl(updateProduct.getUrl());
            return product;
        }).orElseGet(() -> {
            updateProduct.setId(id);
            return  productRepo.save(updateProduct);
        });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "update product successfully", updatedProduct)
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id){
        boolean exists = productRepo.existsById(id);
        if(exists){
            productRepo.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "delete product successfully",null)
            );
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Cannot find product with id = " + id,null)
            );
        }
    }

}
