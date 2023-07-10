package com.swifties.bahceden.Bahceden.service;

import com.swifties.bahceden.Bahceden.entity.Address;

import java.util.Optional;
import com.swifties.bahceden.Bahceden.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository theAddressRepository){
        addressRepository = theAddressRepository;
    }
    @Override
    public List<Address> findAll() {
        return addressRepository.findAll();
    }

//    @Override
//    public List<Address> findByCustomerId(int customerId) {
//        return addressRepository.findByCustomerId(customerId);
//    }

    @Override
    public List<Address> findByOrderId(int orderId) {
        return addressRepository.findAddressByOrderId(orderId);
    }

    @Override
    public Address findById(int theId) {
        Optional<Address> result = addressRepository.findById(theId);

        Address theAddress = null;

        if(result.isPresent()){
            theAddress = result.get();
        }
        else{
            throw new RuntimeException("Did not find address id - " + theId);
        }

        return theAddress;
    }

    @Override
    public Address save(Address theAddress) {
        return addressRepository.save(theAddress);
    }

    @Override
    public void deleteById(int theId) {
        addressRepository.deleteById(theId);
    }
}
