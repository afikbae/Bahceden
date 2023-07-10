package com.swifties.bahceden.Bahceden.rest;

import com.swifties.bahceden.Bahceden.DTO.ProductDTO;
import com.swifties.bahceden.Bahceden.DTO.ProductPostDTO;
import com.swifties.bahceden.Bahceden.repository.CategoryRepository;
import com.swifties.bahceden.Bahceden.repository.ProducerRepository;
import com.swifties.bahceden.Bahceden.service.ProductService;
import com.swifties.bahceden.Bahceden.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
public class ProductController {

    private ProductService productService;
    private CategoryRepository categoryRepository;
    private ProducerRepository producerRepository;

    @Autowired
    public ProductController(ProductService theProductService, CategoryRepository categoryRepository, ProducerRepository producerRepository) {
        this.productService = theProductService;
        this.categoryRepository = categoryRepository;
        this.producerRepository = producerRepository;
    }

    @GetMapping("/products")
    public List<Product> findAll(){
        return productService.findAll();
    }

    @GetMapping("/products/{productId}")
    public Product getProduct(@PathVariable int productId){

        Product theProduct = productService.findById(productId);

        if(theProduct == null){
            throw new RuntimeException("Product id did not found - " + productId);
        }

        return theProduct;
    }

    @GetMapping("/products/custom/{productId}")
    public ProductDTO getCustomProduct(@PathVariable int productId){
        return productService.findCustomById(productId);
    }

    @GetMapping("/orders/{orderId}/product")
    public Product getProductByOrderId(@PathVariable int orderId) {
        Product product = productService.findProductByOrderId(orderId);

        if (product == null) {
            throw new RuntimeException("No product found for order ID - " + orderId);
        }

        return product;
    }

    @GetMapping("/comments/{commentId}/product")
    public Product getProductByCommentId(@PathVariable int commentId) {
        Product product = productService.findProductByCommentId(commentId);

        if (product == null) {
            throw new RuntimeException("No product found for comment ID - " + commentId);
        }

        return product;
    }

    @PostMapping("/products")
    public Product addProduct(@RequestBody ProductPostDTO productDTO){
        Product product = new Product();

        product.setId(0);
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPricePerUnit(productDTO.getPricePerUnit());
        product.setImageURL(productDTO.getImageURL());
        product.setUnitType(productDTO.getUnitType());
        product.setAvailableAmount(productDTO.getAvailableAmount());
        product.setCategory(categoryRepository.findById(productDTO.getCategory()).get());
        product.setSubCategory(categoryRepository.findById(productDTO.getSubCategory()).get());
        product.setProducer(producerRepository.findById(productDTO.getProducer()).get());

        return productService.save(product);
    }

    @PutMapping("/products")
    public Product updateProduct(@RequestBody Product theProduct){

        Product dbProduct = productService.save(theProduct);

        return dbProduct;
    }

    @DeleteMapping("/products/{productId}")
    public String deleteProduct(@PathVariable int productId){

        Product tempProduct = productService.findById(productId);

        // throw exception if null

        if(tempProduct == null){
            throw new RuntimeException("Product id not found - " + productId);
        }

        productService.deleteById(productId);

        return "Deleted product id - " + productId;
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> findByNameContaining(@RequestParam String keyword) {
        List<Product> products = productService.findByNameContaining(keyword);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/products/searchParam")
    public ResponseEntity<List<Product>> findByNameContaining(@RequestParam("keyword") String keyword,
                                                                          @RequestParam("sortBy") String sortBy,
                                                                          @RequestParam("ascending") boolean isAscending) {
        Sort sort;
        if (isAscending)
        {
            sort = Sort.by(Sort.Order.asc(sortBy));
        }
        else{
            sort = Sort.by(Sort.Order.desc(sortBy));
        }
        List<Product> products = productService.findByNameContaining(keyword, sort);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("/products/{productId}/image")
    public Product uploadProductImage(@PathVariable int productId, @RequestParam("image") MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        Path filePath = Paths.get("src/main/resources/media/" + "Product" + productId + ".jpeg");
        String fileUrl = "http://localhost:8080/images/" + "Product" + productId + ".jpeg";

        try {
            Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Product product = productService.findById(productId);
        if (product == null) {
            throw new RuntimeException("No Such Product.");
        }

        product.setImageURL(fileUrl);
        productService.save(product);

        return product;
    }

    @GetMapping("/products/newArrivals")
    public List<Product> findNewArrivals(){
        Pageable topTen = PageRequest.of(0, 10, Sort.by("id").descending());
        return productService.findAll(topTen).getContent();
    }

    @GetMapping("/products/populars")
    public List<Product> findPopulars(){
        Pageable topFive = PageRequest.of(0, 5, Sort.by("rating").descending());
        return productService.findAll(topFive).getContent();
    }

    @GetMapping("products/similar")
    public List<Product> findSimilarProducts(@RequestParam("product") int productId) {
        return productService.findSimilarProducts(productId);
    }

    @GetMapping("categories/products")
    public List<Product> findProductsInACategory(@RequestParam("category") int categoryId)
    {
        return productService.findCategoryProducts(categoryId);
    }
}
