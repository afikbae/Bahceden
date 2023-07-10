package com.swifties.bahceden.Bahceden.rest;

import com.swifties.bahceden.Bahceden.entity.*;
import com.swifties.bahceden.Bahceden.service.CustomerService;
import com.swifties.bahceden.Bahceden.service.OrderService;
import com.swifties.bahceden.Bahceden.service.ScrapedDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomerController {
    private CustomerService customerService;
    private OrderService orderService;
    private ScrapedDataService scrapedDataService;

    @Autowired
    private CustomerController(CustomerService theCustomerService, OrderService theOrderService, ScrapedDataService theScrapedDataService){
        customerService = theCustomerService;
        orderService = theOrderService;
        scrapedDataService = theScrapedDataService;
    }

    @GetMapping("/customers")
    public List<Customer> findAll(){
        return customerService.findAll();
    }

    @GetMapping("/customers/{customerId}")
    public Customer getCustomer(@PathVariable int customerId){

        Customer theCustomer = customerService.findById(customerId);

        if(theCustomer == null){
            throw new RuntimeException("Customer id did not found - " + customerId);
        }

        return theCustomer;
    }

    @GetMapping("/customers/email")
    public Customer getCustomer(@RequestParam String email) {
        return customerService.findByEmail(email);
    }

    @PostMapping("/customers")
    public Customer addCustomer(@RequestBody Customer theCustomer){
        // also just in case they pass an id in JSON ... set id to 0
        // this is to force a save of new item ... instead of update

        theCustomer.setId(0);

        if(theCustomer.getProfileImageURL() == null){
            theCustomer.setProfileImageURL("src/resources/media/noProfile.png");
        }

        Customer dbCustomer = customerService.save(theCustomer);

        return dbCustomer;
    }

    @PostMapping("/customers/favorites/products")
    public FavoriteProduct addFavoriteProduct(@RequestParam("customerId") int customerId, @RequestParam("productId") int productId) {
        return customerService.addFavoriteProduct(customerId, productId);
    }

    @DeleteMapping("/customers/favorites/products")
    public String removeFavoriteProduct(@RequestParam("customerId") int customerId, @RequestParam("productId") int productId) {
        return customerService.removeFavoriteProduct(customerId, productId);
    }

    @PostMapping("/customers/favorites/producers")
    public FavoriteProducer addFavoriteProducer(@RequestParam("customerId") int customerId, @RequestParam("producerId") int producerId) {
        return customerService.addFavoriteProducer(customerId, producerId);
    }

    @DeleteMapping("/customers/favorites/producers")
    public String removeFavoriteProducer(@RequestParam("customerId") int customerId, @RequestParam("producerId") int producerId) {
        return customerService.removeFavoriteProducer(customerId, producerId);
    }

    @PutMapping("/customers")
    public Customer updateCustomer(@RequestBody Customer theCustomer){

        Customer dbCustomer = customerService.save(theCustomer);

        return dbCustomer;
    }

    @DeleteMapping("/customers/{customerId}")
    public String deleteCustomer(@PathVariable int customerId){

        Customer tempCustomer = customerService.findById(customerId);

        // throw exception if null

        if(tempCustomer == null){
            throw new RuntimeException("Customer id not found - " + customerId);
        }

        customerService.deleteById(customerId);

        return "Deleted customer id - " + customerId;
    }

    @GetMapping("/orders/{orderId}/customer")
    public Customer getCustomerByOrderId(@PathVariable int orderId) {
        Customer customer = customerService.findCustomerByOrderId(orderId);

        if (customer == null) {
            throw new RuntimeException("No customer found for order ID - " + orderId);
        }

        return customer;
    }

    @GetMapping("/comments/{commentId}/customer")
    public Customer getCustomerByCommentId(@PathVariable int commentId) {
        Customer customer = customerService.findCustomerByCommentId(commentId);

        if (customer == null) {
            throw new RuntimeException("No customer found for comment ID - " + commentId);
        }

        return customer;
    }

    @PostMapping("customers/{customerId}/image")
    public Customer uploadCustomerImage(@PathVariable int customerId, @RequestParam("image") MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        Path filePath = Paths.get("src/main/resources/media/"  + "Customer" + customerId + ".jpeg");
        try {
            Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String fileUrl = "http://localhost:8080/images/" + "Customer" + customerId + ".jpeg";

        Customer customer = customerService.findById(customerId);
        if (customer == null) {
            throw new RuntimeException("No Such Customer.");
        }

        customer.setProfileImageURL(fileUrl);
        customerService.save(customer);

        return customer;
    }

    @PostMapping("customers/likes")
    public void addLike(@RequestParam("customerId") int customerId, @RequestParam("commentId") int commentId) {
        customerService.addLike(customerId, commentId);
    }

    @DeleteMapping("customers/likes")
    public void removeLike(@RequestParam("customerId") int customerId, @RequestParam("commentId") int commentId) {
        customerService.removeLike(customerId, commentId);
    }

    @GetMapping("customers/likes")
    public boolean getLike(@RequestParam("customerId") int customerId, @RequestParam("commentId") int commentId)
    {
        return customerService.getLike(customerId, commentId);
    }
    @GetMapping("customers/{customerId}/profit")
    public double[] CustomerProfit(@PathVariable int customerId){
        double market = 0;
        double bahceden = 0;
        double max = 0;
        double[] customerData = new double[3];
        List<Order> customerOrders = orderService.getOrdersByCustomerId(customerId);
        for(Order order : customerOrders){
            System.out.println(order.getProduct().getCategory().getId());
            List<ScrapedData> customerScrapedData = scrapedDataService.getScrapedDataByCategoryId(order.getProduct().getSubCategory().getId());
            for(ScrapedData scrapedData : customerScrapedData){
                if(scrapedData.getMaxPrice() > max){
                    max = scrapedData.getMaxPrice();
                }
            }
            market += order.getAmount() * max;
            bahceden += order.getAmount() * order.getProduct().getPricePerUnit();
        }
        customerData[0] = market;
        customerData[1] = bahceden;
        customerData[2] = market - bahceden;
        return customerData;
    }
}
