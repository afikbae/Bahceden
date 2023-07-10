package com.swifties.bahceden.Bahceden.rest;

import com.swifties.bahceden.Bahceden.entity.Address;
import com.swifties.bahceden.Bahceden.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AddressController {
    private AddressService addressService;

    @Autowired
    private AddressController(AddressService theAddressService){
        addressService = theAddressService;
    }

    @GetMapping("/addresses")
    public List<Address> findAll(){
        return addressService.findAll();
    }

//    @GetMapping("/customers/{customerId}/addresses")
//    public List<Address> getAddressesByCustomerId(@PathVariable int customerId) {
//        List<Address> addresses = addressService.findByCustomerId(customerId);
//
//        if (addresses.isEmpty()) {
//            throw new RuntimeException("No addresses found for customer ID - " + customerId);
//        }
//
//        return addresses;
//    }

    @GetMapping("/orders/{orderId}/addresses")
    public List<Address> getAddressesByOrderId(@PathVariable int orderId) {
        List<Address> addresses = addressService.findByOrderId(orderId);

        if (addresses.isEmpty()) {
            throw new RuntimeException("No addresses found for order ID - " + orderId);
        }

        return addresses;
    }

    @GetMapping("/addresses/{addressId}")
    public Address getAddress(@PathVariable int addressId){

        Address theAddress = addressService.findById(addressId);

        if(theAddress == null){
            throw new RuntimeException("Address id did not found - " + addressId);
        }

        return theAddress;
    }

    @PostMapping("/addresses")
    public Address addAddress(@RequestBody Address theAddress){

        // also just in case they pass an id in JSON ... set id to 0
        // this is to force a save of new item ... instead of update

        theAddress.setId(0);

        Address dbAddress = addressService.save(theAddress);

        return dbAddress;
    }

    @PutMapping("/addresses")
    public Address updateAddress(@RequestBody Address theAddress){

        Address dbAddress = addressService.save(theAddress);

        return dbAddress;
    }

    @DeleteMapping("/addresses/{addressId}")
    public String deleteAddress(@PathVariable int addressId){

        Address tempAddress = addressService.findById(addressId);

        // throw exception if null

        if(tempAddress == null){
            throw new RuntimeException("Address id not found - " + addressId);
        }

        addressService.deleteById(addressId);

        return "Deleted address id - " + addressId;
    }
}
