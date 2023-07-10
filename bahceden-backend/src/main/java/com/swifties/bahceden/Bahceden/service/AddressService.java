package com.swifties.bahceden.Bahceden.service;

import com.swifties.bahceden.Bahceden.entity.Address;

import java.util.List;


public interface AddressService {

    List<Address> findAll();

//    List<Address> findByCustomerId(int customerId);

    List<Address> findByOrderId(int orderId);

    Address findById(int theId);

    Address save(Address theAddress);

    void deleteById(int theId);
}
