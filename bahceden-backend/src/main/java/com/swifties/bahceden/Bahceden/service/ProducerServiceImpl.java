package com.swifties.bahceden.Bahceden.service;

import com.swifties.bahceden.Bahceden.DTO.*;
import com.swifties.bahceden.Bahceden.entity.Producer;
import com.swifties.bahceden.Bahceden.entity.Product;
import com.swifties.bahceden.Bahceden.entity.ScrapedData;
import com.swifties.bahceden.Bahceden.repository.*;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProducerServiceImpl implements ProducerService{

    private ProducerRepository producerRepository;
    private CustomerService customerService;
    private ProductService productService;
    private OrderRepository orderRepository;
    private ModelMapper modelMapper;
    private ProductRepository productRepository;
    private CommentRepository commentRepository;
    ScrapedDataRepository scrapedDataRepository;

    @Autowired
    public ProducerServiceImpl(ProducerRepository theProducerRepository, OrderRepository orderRepository, ModelMapper modelMapper, ProductRepository productRepository, CommentRepository commentRepository, CustomerService customerService, ProductService productService, ScrapedDataRepository scrapedDataRepository){
        this.producerRepository = theProducerRepository;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
        this.commentRepository = commentRepository;
        this.customerService = customerService;
        this.productService = productService;
        this.scrapedDataRepository = scrapedDataRepository;
    }

    @Override
    public List<Producer> findAll() {
        return producerRepository.findAll();
    }

    @Override
    public Producer findById(int theId) {
        Optional<Producer> result = producerRepository.findById(theId);

        Producer theProducer = null;

        if(result.isPresent()){
            theProducer = result.get();
        }
        else{
            throw new RuntimeException("Did not find producer id - " + theId);
        }

        return theProducer;
    }

    @Override
    public Producer findProducerByOrderId(int orderId) {
        return producerRepository.findProducerByOrderId(orderId);
    }

   @Override
    public Producer findProducerByProductId(int productId) {
        return producerRepository.findProducerByProductId(productId);
    }

    @Override
    public Producer findByEmail(String email) {
        return producerRepository.findByEmail(email);
    }

    @Override
    public List<Producer> findByShopNameContaining(String keyword) {
        return producerRepository.findByShopNameContaining(keyword);
    }

    @Override
    public Producer save(Producer theProducer) {
        return producerRepository.save(theProducer);
    }

    @Override
    public void deleteById(int theId) {
        producerRepository.deleteById(theId);
    }

    @Override
    public List<Producer> findByNameContaining(String keyword) {
        return producerRepository.findByNameContaining(keyword);
    }

    @Override
    public List<Producer> findByNameContaining(String keyword, Sort sort) {
        return producerRepository.findByNameContaining(keyword, sort);
    }

    @Override
    public List<OrderWithoutProducerDTO> findOrders(int producerId) {
        List<OrderWithoutProducerDTO> orders = orderRepository.findOrders(producerId).stream()
                .map(order -> {
                    OrderWithoutProducerDTO orderDTO = modelMapper.map(order, OrderWithoutProducerDTO.class);
                    CustomerMainInfoDTO customerDTO = modelMapper.map(order.getCustomer(), CustomerMainInfoDTO.class);
                    ProductWithoutProducerDTO producerDTO = modelMapper.map(order.getProduct(), ProductWithoutProducerDTO.class);
                    orderDTO.setProduct(producerDTO);
                    orderDTO.setCustomer(customerDTO);
                    return orderDTO;
                }).toList();
        return orders;
    }

    @Override
    public List<ProductWithoutProducerDTO> findProducts(int producerId) {
        List<ProductWithoutProducerDTO> products = productRepository.findByProducerId(producerId).stream()
                .map(product -> {
                    return modelMapper.map(product, ProductWithoutProducerDTO.class);
                }).toList();

        return products;
    }

    @Override
    public List<CommentDTO> findComments(int producerId) {
        List<CommentDTO> comments = commentRepository.findCommentForProducer(producerId).stream()
                .map(comment -> {
                    if (comment.getAuthor() > 0) {
                        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
                        commentDTO.setAuthor(modelMapper.map(customerService.findById(comment.getAuthor()), CustomerMainInfoDTO.class));
                        commentDTO.setProductName(productService.findById(comment.getProduct()).getName());
                        return commentDTO;
                    } return null;
                }).toList();

        return comments;
    }

    @Override
    public ProducerDataDTO findProductData(int categoryId) {
        ProducerDataDTO producerDataDTO = new ProducerDataDTO();

//        Market Values
        List<ScrapedData> data = scrapedDataRepository.findBySubCategoryAll(categoryId);
        Double maxMarket = Double.MIN_VALUE;
        Double minMarket = Double.MAX_VALUE;
        Double avgMarket = 0.0;

        for (ScrapedData scrapedData : data) {
            if (scrapedData.getMaxPrice() > maxMarket)
                maxMarket = scrapedData.getMaxPrice();

            if (scrapedData.getMinPrice() < minMarket)
                minMarket = scrapedData.getMinPrice();

            avgMarket += (scrapedData.getMaxPrice() + scrapedData.getMinPrice()) / 2;
        }
        if(data.size() == 0)
            avgMarket = 0.0;
        else
            avgMarket /= data.size();

        producerDataDTO.setMarketPriceMAX(maxMarket);
        producerDataDTO.setMarketPriceMIN(minMarket);
        producerDataDTO.setMarketPriceAVG(avgMarket);

//        Other seller Values
        List<Product> products = productRepository.findBySubCategoryAll(categoryId);
        Double maxSellers = Double.MIN_VALUE;
        Double minSellers = Double.MAX_VALUE;
        Double avgSellers = 0.0;

        for (Product product : products) {
            if (product.getPricePerUnit() > maxSellers)
                maxSellers = product.getPricePerUnit();

            if (product.getPricePerUnit() < minSellers)
                minSellers = product.getPricePerUnit();

            avgSellers += product.getPricePerUnit();
        }

        if (products.size() == 0)
            avgSellers = 0.0;
        else
            avgSellers /= products.size();
        producerDataDTO.setSellersMAX(maxSellers);
        producerDataDTO.setSellersMIN(minSellers);
        producerDataDTO.setSellersAVG(avgSellers);

//        Price Prediction INSHALLAH
        /*
        n is the number of data points, sumXY is the sum of the product of sellers and market prices,
        sumX is the sum of sellers, sumY is the sum of market prices, and sumXSquare is the sum of squared sellers
         */
        double sumXY = (minSellers * minMarket) + (avgSellers * avgMarket) + (maxSellers * maxMarket);
        double sumX = minSellers + avgSellers + maxSellers;
        double sumY = minMarket + avgMarket + maxMarket;
        double sumXSquare = Math.pow(minSellers, 2) + Math.pow(avgSellers, 2) + Math.pow(maxSellers, 2);

        double n = 3;

        double beta1 = (n * sumXY - sumX * sumY) / (n * sumXSquare - sumX * sumX);
        double beta0 = (sumY - beta1 * sumX) / n;

        double sellersAvgInput = avgSellers;
        double predictedPrice = beta0 + beta1 * sellersAvgInput;

        if(Double.isNaN(predictedPrice))
            predictedPrice = 0;

        producerDataDTO.setRecommendedPrice(predictedPrice);
        return producerDataDTO;
    }
}
