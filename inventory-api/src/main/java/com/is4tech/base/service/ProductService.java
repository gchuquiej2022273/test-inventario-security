package com.is4tech.base.service;

import com.is4tech.base.domain.Product;
import com.is4tech.base.dto.ProductDto;
import com.is4tech.base.exception.Exceptions;
import com.is4tech.base.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product saveProduct(ProductDto input) {
        //llamamos el metodo para validar los campos
        validateInput(input);

        Product product = new Product();
        product.setName(input.getName());
        product.setDeliveryTime(input.getDeliveryTime());
        product.setBarCode(input.getBarCode());
        product.setDescription(input.getDescription());
        product.setAvailableQuantity(input.getAvailableQuantity());
        product.setPrice(input.getPrice());
        product.setCompanyId(input.getCompanyId());
        product.setCategoryId(input.getCategoryId());
        product.setStatus(true);

        return productRepository.save(product);
    }

    public Product updateProduct(ProductDto input, Integer id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new Exceptions("Product not found"));

        validateInput(input);

        existingProduct.setName(input.getName());
        existingProduct.setDeliveryTime(input.getDeliveryTime());
        existingProduct.setBarCode(input.getBarCode());
        existingProduct.setDescription(input.getDescription());
        existingProduct.setAvailableQuantity(input.getAvailableQuantity());
        existingProduct.setPrice(input.getPrice());
        existingProduct.setCompanyId(input.getCompanyId());
        existingProduct.setCategoryId(input.getCategoryId());
        if (input.getStatus() != null) {
            existingProduct.setStatus(input.getStatus());
        }
        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Integer id) {
        Product existsProduct = productRepository.findById(id)
                .orElseThrow(() -> new Exceptions("Product not found"));

        productRepository.delete(existsProduct);
    }

    public List<Product> getProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new Exceptions("No Products found");
        }
        return products;
    }

    public Product getProductId(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new Exceptions("Product not found with id: " + id));
    }

    public void validateInput(ProductDto input){
        if (input.getName() == null || input.getName().trim().isEmpty()) {
            throw new Exceptions("Name cannot be empty");
        }
        if (input.getDeliveryTime() == null){
            throw new Exceptions("Delivery time cannot be empty");
        }
        if (input.getBarCode() == null || input.getBarCode().trim().isEmpty()) {
            throw new Exceptions("BarCode cannot be empty");
        }
        if (input.getDescription() == null || input.getDescription().trim().isEmpty()){
            throw new Exceptions("Description cannot be empty");
        }
        if (input.getAvailableQuantity() == null){
            throw new Exceptions("Available quantity cannot be empty");
        }
        if (input.getPrice() == null){
            throw new Exceptions("Price cannot be empty");
        }
        if (input.getCompanyId() == null){
            throw new Exceptions("CompanyId cannot be empty");
        }
        if (input.getCategoryId() == null){
            throw new Exceptions("CategoryId cannot be empty");
        }
        if (productRepository.existsByBarCode(input.getBarCode())) {
            throw new Exceptions("The BarCode already exists");
        }
    }
}
