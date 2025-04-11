package com.example.service;
 
import com.example.entity.Product;
import com.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.util.List;
import java.util.Optional;
 
@Service
public class ProductService {
 
    @Autowired
    private ProductRepository repo;
 
    public List<Product> getAllProducts() {
        return repo.findAll();
    }
 
    public Product getProductById(Long productId) {
        Optional<Product> productOptional = repo.findById(productId);
        return productOptional.orElse(null);
    }
 
    public Product saveProduct(Product product) {
        return repo.save(product);
    }
 
    public void deleteProduct(Long productId) {
        repo.deleteById(productId);
    }
 
    public List<Product> getByCategory(String category) {
        return repo.findByCategory(category);
    }
 
    public List<Product> getByBrand(String brand) {
        return repo.findByBrand(brand);
    }
    public List<Product> searchByName(String name) {
        return repo.findByNameContainingIgnoreCase(name);
    }
    public List<Product> searchByRating(double rating) {
        return repo.findByRatingGreaterThanEqual(rating);
    }
    public List<Product> filterBytag(String tag) {
        return repo.findByTag(tag);
    }

    public List<Product> filterByStock(int stock) {
        return repo.findByStockGreaterThan(stock);
    }


}
 