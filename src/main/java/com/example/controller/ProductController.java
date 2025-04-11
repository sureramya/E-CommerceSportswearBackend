package com.example.controller;
 
import com.example.entity.Product;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;
 
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {
 
    @Autowired
    private ProductService service;
 
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public Product addProduct(@RequestBody Product product) {
        return service.saveProduct(product);
    }
 
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        Product existingProduct = service.getProductById(id);
        if (existingProduct == null) {
            throw new RuntimeException("Product not found with ID: " + id);
        }
 
        // Update only fields that can be modified
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setBrand(updatedProduct.getBrand());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setImageUrl(updatedProduct.getImageUrl());
 
        return service.saveProduct(existingProduct);
    }
 
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    @Transactional
    public ResponseEntity<String> deleteProduct(@RequestParam String username, @RequestParam Long productId) {
        service.deleteProduct(productId);
        return ResponseEntity.ok("Item deleted successfully");
    }
  
 
    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return service.getAllProducts();
    }
 
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return service.getProductById(id);
    }
 
    @GetMapping("/category/{category}")
    public List<Product> getByCategory(@PathVariable String category) {
        return service.getByCategory(category);
    }
 
    @GetMapping("/brand/{brand}")
    public List<Product> getByBrand(@PathVariable String brand) {
        return service.getByBrand(brand);
    }
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String name) {
        List<Product> products =service.searchByName(name);
        return ResponseEntity.ok(products);
    }
    @GetMapping("/filter-by-rating")
    public ResponseEntity<List<Product>> filterByRating(@RequestParam double rating) {
        return ResponseEntity.ok(service.searchByRating(rating));
    }
    @GetMapping("/filter-by-tag")
    public ResponseEntity<List<Product>> searchBytag(@RequestParam String tag) {
        return ResponseEntity.ok(service.filterBytag(tag));
    }
    @GetMapping("/in-stock")
    public ResponseEntity<List<Product>> getInStockProducts() {
        return ResponseEntity.ok(service.filterByStock(0));
    }


}