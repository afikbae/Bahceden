package com.swifties.bahceden.Bahceden.rest;

import com.swifties.bahceden.Bahceden.DTO.OrderRequestDTO;
import com.swifties.bahceden.Bahceden.DTO.OrderResponseDTO;
import com.swifties.bahceden.Bahceden.entity.*;
import com.swifties.bahceden.Bahceden.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ProducerService producerService;
    @Autowired
    private ProductService productService;

    @Autowired
    private OrderController(OrderService theOrderService){
        orderService = theOrderService;
    }

    @GetMapping("/orders")
    public List<Order> findAll(){
        return orderService.findAll();
    }

    @GetMapping("/orders/{orderId}")
    public Order getOrder(@PathVariable int orderId){

        Order theOrder = orderService.findById(orderId);

        if(theOrder == null){
            throw new RuntimeException("Order id did not found - " + orderId);
        }

        return theOrder;
    }

    @GetMapping("/orders/completed")
    public List<Order> getCompletedOrders() {
        return orderService.getCompletedOrders();
    }

    @PostMapping("/orders")
    public OrderResponseDTO addOrder(@RequestBody OrderRequestDTO orderRequest){

        Order order = new Order();
        // Set values from the request DTO to the Order entity
        order.setId(orderRequest.getId());
        order.setStatus(orderRequest.getStatus());
        order.setAmount(orderRequest.getAmount());
        order.setDateOfPurchase(orderRequest.getDateOfPurchase());
        order.setShipmentType(orderRequest.getShipmentType());

        // Fetch and set the associated Customer entity
        Customer customer = customerService.findById(orderRequest.getCustomerId());
        order.setCustomer(customer);

        // Fetch and set the associated Producer entity
        Producer producer = producerService.findById(orderRequest.getProducerId());
        order.setProducer(producer);

        // Fetch and set the associated Product entity
        Product product = productService.findById(orderRequest.getProductId());
        order.setProduct(product);

        // Fetch and set the associated Address entity
        Address address = addressService.findById(orderRequest.getDeliveryAddressId());
        order.setDeliveryAddress(address);

        Order dbOrder = orderService.save(order);

        // Create the response DTO with only the necessary fields
        OrderResponseDTO orderResponse = new OrderResponseDTO();
        orderResponse.setId(dbOrder.getId());
        orderResponse.setStatus(dbOrder.getStatus());
        orderResponse.setAmount(dbOrder.getAmount());
        orderResponse.setDateOfPurchase(dbOrder.getDateOfPurchase());
        orderResponse.setShipmentType(dbOrder.getShipmentType());

        return orderResponse;
    }

    @PutMapping("/orders/{orderId}")
    public OrderResponseDTO updateOrder(@PathVariable Integer orderId, @RequestBody OrderRequestDTO orderRequest) {
        // Fetch the existing Order entity by ID
        Order order = orderService.findById(orderId);

        // Update the values from the request DTO to the Order entity
        order.setStatus(orderRequest.getStatus());
        order.setAmount(orderRequest.getAmount());
        order.setDateOfPurchase(orderRequest.getDateOfPurchase());
        order.setShipmentType(orderRequest.getShipmentType());

        // Fetch and set the associated Customer entity
        Customer customer = customerService.findById(orderRequest.getCustomerId());
        order.setCustomer(customer);

        // Fetch and set the associated Producer entity
        Producer producer = producerService.findById(orderRequest.getProducerId());
        order.setProducer(producer);

        // Fetch and set the associated Product entity
        Product product = productService.findById(orderRequest.getProductId());
        order.setProduct(product);

        // Fetch and set the associated Address entity
        Address address = addressService.findById(orderRequest.getDeliveryAddressId());
        order.setDeliveryAddress(address);

        Order dbOrder = orderService.save(order);

        // Create the response DTO with only the necessary fields
        OrderResponseDTO orderResponse = new OrderResponseDTO();
        orderResponse.setId(dbOrder.getId());
        orderResponse.setStatus(dbOrder.getStatus());
        orderResponse.setAmount(dbOrder.getAmount());
        orderResponse.setDateOfPurchase(dbOrder.getDateOfPurchase());
        orderResponse.setShipmentType(dbOrder.getShipmentType());

        return orderResponse;
    }

    @DeleteMapping("/orders/{orderId}")
    public String deleteOrder(@PathVariable int orderId){

        Order tempOrder = orderService.findById(orderId);

        // throw exception if null

        if(tempOrder == null){
            throw new RuntimeException("Order id not found - " + orderId);
        }

        orderService.deleteById(orderId);

        return "{\"id\":"+orderId+"}";
    }

    @GetMapping("/customers/{customerId}/orders")
    public List<Order> getOrdersByCustomerId(@PathVariable Integer customerId) {
        return orderService.getOrdersByCustomerId(customerId);
    }
}
