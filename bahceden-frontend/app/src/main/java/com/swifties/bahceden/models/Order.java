package com.swifties.bahceden.models;

import com.swifties.bahceden.data.RetrofitService;
import com.swifties.bahceden.data.apis.OrderApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Order {

    public enum ShipmentType {
        CUSTOMER_PICKUP(1), PRODUCER_DELIVERY(2), SHIPMENT(3);

        final int value;

        ShipmentType (int value) {
            this.value = value;
        }

        public int getValue()
        {
            return this.value;
        }
    }

    public enum OrderStatus {
        IN_CART(1), PENDING(2), ONGOING(3), DELIVERED(4), CANCELLED(5);

        final int value;

        OrderStatus (int value)
        {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private int id;
    private String dateOfPurchase;
    private Product product;
    private int amount;
    private double totalPrice;
    private ShipmentType shipmentType;
    private Address deliveryAddress;
    private OrderStatus status;
    private String customerName;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    private void calculateTotalPrice() {
        totalPrice = amount * product.getPricePerUnit();
    }

    public boolean offsetAmountBy(int offset) {
        if (amount + offset < 1) return false;
        amount += offset;
        return true;
    }

    public Customer getCustomer ()
    {
        return deliveryAddress.getCustomer();
    }

    public int getId() {
        return id;
    }

    public String getDateOfPurchase() {
        return dateOfPurchase;
    }

    public Product getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }

    public double getTotalPrice() {
        calculateTotalPrice();
        return totalPrice;
    }

    public ShipmentType getShipmentType() {
        return shipmentType;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDateOfPurchase(String dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setShipmentType(ShipmentType shipmentType) {
        this.shipmentType = shipmentType;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    public void changeStatusTo (OrderStatus status, Action action)
    {
        setStatus(status);
        RetrofitService.getApi(OrderApi.class).putOrder(this, getId()).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                action.act();
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }
}