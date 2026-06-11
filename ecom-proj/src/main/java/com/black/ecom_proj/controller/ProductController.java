package com.black.ecom_proj.controller;



import com.black.ecom_proj.model.Product;
import com.black.ecom_proj.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProductController {

   private final ProductService productService;

   public ProductController(ProductService productService) {
       this.productService = productService;
   }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(),HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
       Product product = productService.getProductById(id);

       if(product != null) {
           return new ResponseEntity<>(product, HttpStatus.FOUND);
       }else  {
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
    }

    @PostMapping("/products")
    public ResponseEntity<?> addProduct(
            @RequestPart Product product,
            @RequestPart MultipartFile imageFile){
       try{
           Product savedProduct = productService.addProduct(product,imageFile);
           return new ResponseEntity<>(savedProduct,HttpStatus.CREATED);
       }
       catch(Exception e){
           return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
       }
    }

    @GetMapping("/products/{productId}/image")
    public ResponseEntity<byte[]>getProductImageById(@PathVariable int  productId){
       try {
           Product product = productService.getProductById(productId);

           if (product != null && product.getImageData() != null) {
               return ResponseEntity.ok().contentType(org.springframework.http.MediaType.parseMediaType(product.getImageType())).body(product.getImageData());
           }
       }catch (Exception e){
           System.out.println("Image Stream Failed: "+e.getMessage());
       }
       return new ResponseEntity<>(org.springframework.http.HttpStatus.NOT_FOUND);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<String> updateProduct(
            @PathVariable int id,
            @PathVariable Product product,
            @RequestPart(required = false) MultipartFile imageFile){

       try{
            Product updateProduct = productService.updateProduct(id,product,imageFile);
            if(updateProduct != null) {
                return new ResponseEntity<>("Product Updated" ,HttpStatus.OK);
            }else  {
                return new ResponseEntity<>("Product Not Updated",HttpStatus.BAD_REQUEST);
            }
       } catch (Exception e) {
           return new ResponseEntity<>("Update Failed"+e.getMessage(),HttpStatus.BAD_REQUEST);
       }
    }
    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
       Product product = productService.getProductById(id);
       if(product != null) {
           productService.deleteProduct(id);
           return new ResponseEntity<>("Product Deleted",HttpStatus.OK);
       }
       return new ResponseEntity<>("Product Not Deleted",HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword){
       List<Product> products = productService.searchProducts(keyword);
       return new ResponseEntity<>(products,HttpStatus.OK);
    }
}
