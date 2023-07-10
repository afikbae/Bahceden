package com.swifties.bahceden.Bahceden.service;

import com.swifties.bahceden.Bahceden.DTO.CommentDTO;
import com.swifties.bahceden.Bahceden.DTO.CustomerMainInfoDTO;
import com.swifties.bahceden.Bahceden.DTO.ProductDTO;
import com.swifties.bahceden.Bahceden.entity.Address;
import com.swifties.bahceden.Bahceden.entity.Category;
import com.swifties.bahceden.Bahceden.entity.Product;
import com.swifties.bahceden.Bahceden.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private CommentService commentService;
    private CustomerService customerService;
    private ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository theProductRepository, CommentService commentService, ModelMapper modelMapper, CustomerService customerService) {
        this.productRepository = theProductRepository;
        this.commentService = commentService;
        this.modelMapper = modelMapper;
        this.customerService = customerService;
    }


    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(int theId) {
        Optional<Product> result = productRepository.findById(theId);

        Product product = null;

        if(result.isPresent()){
            product = result.get();
        }
        else{
            throw new RuntimeException("Did not find employee id - " + theId);
        }

        return product;
    }

    @Override
    public ProductDTO findCustomById(int productId) {
        Optional<Product> result = productRepository.findById(productId);

        Product product = null;
        ProductDTO productDTO;

        if(result.isPresent()){
            product = result.get();
            productDTO = modelMapper.map(product, ProductDTO.class);

            productDTO.setComments(commentService.findCommentsByProductId(productId));
        }
        else{
            throw new RuntimeException("Product DNE");
        }

        return productDTO;
    }

    @Override
    public Product findProductByOrderId(int orderId) {
        return productRepository.findProductByOrderId(orderId);
    }

    @Override
    public Product findProductByCommentId(int commentId) {
        return productRepository.findProductByCommentId(commentId);
    }

    @Override
    public List<Product> findByNameContaining(String keyword) {
        return productRepository.findByNameContaining(keyword);
    }

    @Override
    public List<Product> findByNameContaining(String keyword, Sort sort) {
        return productRepository.findByNameContaining(keyword, sort);
    }

    @Override
    public Product save(Product theProduct) {
        return productRepository.save(theProduct);
    }

    @Override
    public void deleteById(int theId) {
        productRepository.deleteById(theId);
    }

    public Page<Product> findAll(Pageable pageable){
        return productRepository.findAll(pageable);
    }

    @Override
    public List<Product> findSimilarProducts(int productId) {
        int categoryId = productRepository.findById(productId).get().getCategory().getId();
        return productRepository.findByCategory(categoryId, productId);
    }

    @Override
    public List<Product> findCategoryProducts(int categoryId) {
        return productRepository.findByCategoryAll(categoryId);
    }
}
