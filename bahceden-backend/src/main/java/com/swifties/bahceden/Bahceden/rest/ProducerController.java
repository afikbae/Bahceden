package com.swifties.bahceden.Bahceden.rest;

import com.swifties.bahceden.Bahceden.DTO.CommentDTO;
import com.swifties.bahceden.Bahceden.DTO.OrderWithoutProducerDTO;
import com.swifties.bahceden.Bahceden.DTO.ProducerDataDTO;
import com.swifties.bahceden.Bahceden.DTO.ProductWithoutProducerDTO;
import com.swifties.bahceden.Bahceden.entity.Customer;
import com.swifties.bahceden.Bahceden.entity.Order;
import com.swifties.bahceden.Bahceden.entity.Producer;
import com.swifties.bahceden.Bahceden.entity.Product;
import com.swifties.bahceden.Bahceden.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Set;

@RestController
public class ProducerController {

    private ProducerService producerService;

    @Autowired
    public ProducerController(ProducerService theProducerService){
        producerService = theProducerService;
    }

    @GetMapping("/producers")
    public List<Producer> findAll(){
        return producerService.findAll();
    }

    @GetMapping("/producers/{producerId}")
    public Producer getProducer(@PathVariable int producerId){

        Producer theProducer = producerService.findById(producerId);

        if(theProducer == null){
            throw new RuntimeException("Producer id did not found - " + producerId);
        }

        return theProducer;
    }

    @GetMapping("/producers/email")
    public Producer getProducer(@RequestParam String email) {
        return producerService.findByEmail(email);
    }

    @PostMapping("/producers")
    public Producer addProducer(@RequestBody Producer theProducer){

        // also just in case they pass an id in JSON ... set id to 0
        // this is to force a save of new item ... instead of update

        theProducer.setId(0);

        if(theProducer.getProfileImageURL() == null){
            theProducer.setProfileImageURL("src/resources/media/noProfile.png");
        }

        if(theProducer.getBackgroundImageURL() == null){
            theProducer.setBackgroundImageURL("src/resources/media/plainWhite.png");
        }

        Producer dbProducer = producerService.save(theProducer);

        return dbProducer;
    }

    @PutMapping("/producers")
    public Producer updateProducer(@RequestBody Producer theProducer){

        Producer dbProducer = producerService.save(theProducer);

        return dbProducer;
    }

    @DeleteMapping("/producers/{producerId}")
    public String deleteProducer(@PathVariable int producerId){

        Producer tempProducer = producerService.findById(producerId);

        // throw exception if null

        if(tempProducer == null){
            throw new RuntimeException("Producer id not found - " + producerId);
        }

        producerService.deleteById(producerId);

        return "Deleted producer id - " + producerId;
    }

    @GetMapping("/orders/{orderId}/producer")
    public Producer getProducerByOrderId(@PathVariable int orderId) {
        Producer producer = producerService.findProducerByOrderId(orderId);
        if (producer == null) {
            throw new RuntimeException("No producer found for order ID - " + orderId);
        }

        return producer;
    }

    @GetMapping("/products/{productId}/producer")
    public Producer getProducerByProductId(@PathVariable int productId) {
        Producer producer = producerService.findProducerByProductId(productId);

        if (producer == null) {
            throw new RuntimeException("No producer found for product ID - " + productId);
        }

        return producer;
    }

    @GetMapping("/producers/{keyword}/search")
    public ResponseEntity<List<Producer>> findByShopNameContaining(@PathVariable String keyword) {
        List<Producer> producers = producerService.findByShopNameContaining(keyword);
        return new ResponseEntity<>(producers, HttpStatus.OK);
    }

    @PutMapping("/producers/{producerId}/image")
    public Producer uploadProducerImage(@PathVariable int producerId, @RequestParam("image") MultipartFile multipartFile,
    @RequestParam("type") String type) {
        String fileName = multipartFile.getOriginalFilename();
        Path filePath;
        String fileUrl;

        if(type.equals("back")){
            fileUrl = "http://localhost:8080/images/" + "ProducerBackground" + producerId + ".jpeg";
            filePath = Paths.get("src/main/resources/media/"  + "ProducerBackground" + producerId + ".jpeg");
        }
        else{
            fileUrl = "http://localhost:8080/images/" + "ProducerProfile" + producerId + ".jpeg";
            filePath = Paths.get("src/main/resources/media/"  + "ProducerProfile" + producerId + ".jpeg");
        }
        try {
            Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Producer producer = producerService.findById(producerId);
        if (producer == null) {
            throw new RuntimeException("No Such Producer.");
        }

        if(type.equals("back")){
            producer.setBackgroundImageURL(fileUrl);
        }
        else {
            producer.setProfileImageURL(fileUrl);
        }
        producerService.save(producer);

        return producer;
    }



    @GetMapping("/producers/searchParam")
    public ResponseEntity<List<Producer>> findByNameContaining(@RequestParam("keyword") String keyword,
                                                              @RequestParam("ascending") boolean isAscending) {
        Sort sort;
        if (isAscending)
        {
            sort = Sort.by(Sort.Order.asc("rating"));
        }
        else{
            sort = Sort.by(Sort.Order.desc("rating"));
        }
        List<Producer> producers = producerService.findByNameContaining(keyword, sort);
        return new ResponseEntity<>(producers, HttpStatus.OK);
    }

    @GetMapping("producers/{producerId}/orders")
    public List<OrderWithoutProducerDTO> getProducerWithOrders(@PathVariable int producerId) {
        return producerService.findOrders(producerId);
    }

    @GetMapping("producers/{producerId}/products")
    public List<ProductWithoutProducerDTO> getProducerProducts(@PathVariable int producerId) {
        return producerService.findProducts(producerId);
    }

    @GetMapping("producers/{producerId}/comments")
    public List<CommentDTO> findProducerComments(@PathVariable int producerId) {
        return producerService.findComments(producerId);
    }

    @GetMapping("producers/data")
    public ProducerDataDTO getProductData(@RequestParam("subCategory") int categoryId) {
        return producerService.findProductData(categoryId);
    }
}
