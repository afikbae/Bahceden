package com.swifties.bahceden.models;

import com.swifties.bahceden.data.RetrofitService;
import com.swifties.bahceden.data.apis.OrderApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cart {
    private List<Order> orders;
    private final int customerId;

    public Cart(List<Order> orders, int customerId) {
        this.orders = orders;
        this.customerId = customerId;
    }

    public Order get(int index) {
        return orders.get(index);
    }


    public Order getById(int id) {
        for (Order o : orders) {
            if (o.getId() == id) {
                return o;
            }
        }
        return null;
    }

    public boolean remove(int index) {
        Order order = orders.remove(index);
        if (order == null)
            return false;

        return true;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public double calculateTotalCost() {
        double totalPrice = 0;
        for (Order o : orders) {
            totalPrice += o.getTotalPrice();
        }
        return totalPrice;
    }

    public int size() {
        return orders.size();
    }
}
