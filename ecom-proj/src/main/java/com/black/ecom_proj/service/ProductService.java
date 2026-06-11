package com.black.ecom_proj.service;


import com.black.ecom_proj.model.Product;
import com.black.ecom_proj.repository.ProductRepository;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductService {

   private final ProductRepository productRepository;

   public ProductService(ProductRepository productRepository) {
       this.productRepository = productRepository;
   }


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
       return productRepository.findById(id).orElse(null);
    }

    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
       product.setImageName(imageFile.getOriginalFilename());
       product.setImageType(imageFile.getContentType());
       product.setImageData(imageFile.getBytes());
       return productRepository.save(product);
    }

    public Product updateProduct(int id, Product product, MultipartFile imageFile) throws IOException {

       //to make sure item actually exists first
       if(!productRepository.existsById(id)) {
           return null;
       }
       //set the id so the jpa knows to update instead insert
       product.setId(id);

       //handles image if new one was provided
       if(imageFile != null && !imageFile.isEmpty()) {
           product.setImageName(imageFile.getOriginalFilename());
           product.setImageType(imageFile.getContentType());
           product.setImageData(imageFile.getBytes());
       }else {
           //if no new image was uploaded , preserve the  old image data
           Product existingProduct = productRepository.findById(id).orElse(null);
           if(existingProduct != null) {
               existingProduct.setImageName(existingProduct.getImageName());
               existingProduct.setImageType(existingProduct.getImageType());
               existingProduct.setImageData(existingProduct.getImageData());
           }
       }
       return productRepository.save(product);
    }

    public void deleteProduct(int id) {
       productRepository.deleteById(id);
    }

    public List<Product> searchProducts(String keyword) {
       return productRepository.searchProducts(keyword);
    }
}
