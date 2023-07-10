package com.swifties.bahceden.models;

import java.util.Objects;

public class Address {
    private int id;
    private String title;
    private String fullAddress;
    private String phoneNumber;
    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public void setCustomerId(int id) {
        Customer c = new Customer();
        c.setId(id);
        this.customer = c;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return id == address.id && Objects.equals(title, address.title) && Objects.equals(fullAddress, address.fullAddress) && Objects.equals(phoneNumber, address.phoneNumber) && Objects.equals(customer, address.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, fullAddress, phoneNumber, customer);
    }

    public Address(String title, String fullAddress, String phoneNumberOfRecipient, Customer customer) {
        this.title = title;
        this.fullAddress = fullAddress;
        this.phoneNumber = phoneNumberOfRecipient;
        this.customer = customer;
    }

    public Address(Customer customer) {
        this.customer = customer;
    }

    public Address(int id, String title, String fullAddress, String phoneNumber, Customer customer) {
        this.id = id;
        this.title = title;
        this.fullAddress = fullAddress;
        this.phoneNumber = phoneNumber;
        this.customer = customer;
    }
}
