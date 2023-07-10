package com.swifties.bahceden.Bahceden.service;

import com.swifties.bahceden.Bahceden.DTO.CommentDTO;
import com.swifties.bahceden.Bahceden.DTO.OrderWithoutProducerDTO;
import com.swifties.bahceden.Bahceden.DTO.ProducerDataDTO;
import com.swifties.bahceden.Bahceden.DTO.ProductWithoutProducerDTO;
import com.swifties.bahceden.Bahceden.entity.Customer;
import com.swifties.bahceden.Bahceden.entity.Order;
import com.swifties.bahceden.Bahceden.entity.Producer;
import com.swifties.bahceden.Bahceden.entity.Product;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Set;

public interface ProducerService {
    List<Producer> findAll();

    Producer findById(int theId);

    Producer findProducerByOrderId(int orderId);

    Producer findProducerByProductId(int productId);

    Producer findByEmail(String email);

    List<Producer> findByShopNameContaining(String keyword);

    Producer save(Producer theProducer);

    void deleteById(int theId);

    List<Producer> findByNameContaining(String keyword);

    List<Producer> findByNameContaining(String keyword, Sort sort);

     List<OrderWithoutProducerDTO> findOrders(int producerId);

    List<ProductWithoutProducerDTO> findProducts(int producerId);

    List<CommentDTO> findComments(int producerId);

    ProducerDataDTO findProductData(int categoryId);
}
