package com.swifties.bahceden.models;

import com.swifties.bahceden.data.AuthUser;
import com.swifties.bahceden.data.RetrofitService;
import com.swifties.bahceden.data.apis.CustomerApi;
import com.swifties.bahceden.data.apis.OrderApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Customer extends User {
    List<Product> favoriteProducts;
    List<Producer> favoriteProducers;
    List<Order> orders;
    List<Address> addresses;

    public Customer() {
        super();
        favoriteProducts = new ArrayList<>();
        favoriteProducers = new ArrayList<>();
        orders = new ArrayList<>();
        addresses = new ArrayList<>();
    }

    public void setFavoriteProducts(List<Product> favoriteProducts) {
        this.favoriteProducts = favoriteProducts;
    }

    public void setFavoriteProducers(List<Producer> favoriteProducers) {
        this.favoriteProducers = favoriteProducers;
    }


    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<Order> getCart() {
        return orders.stream().filter(o -> o.getStatus().equals(Order.OrderStatus.IN_CART)).collect(Collectors.toList());
    }
    public boolean deleteFromCart (int id) {
        return orders.removeIf(o -> o.getId() == id && o.getStatus() == Order.OrderStatus.IN_CART);
    }

    public List<Product> getFavoriteProducts() {
        return favoriteProducts;
    }

    public void addNewOrder (Product p, int amount)
    {
        Optional<Order> orderOptional = this.orders.stream().filter(o -> o.getStatus() == Order.OrderStatus.IN_CART && o.getProduct().equals(p)).findFirst();
        if (orderOptional.isPresent())
        {
            Order oldOrder = orderOptional.get();
            oldOrder.setAmount(amount);
            RetrofitService.getApi(OrderApi.class).putOrder(oldOrder, oldOrder.getId()).enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {

                }

                @Override
                public void onFailure(Call<Order> call, Throwable t) {
                    throw new RuntimeException(t);
                }
            });
        }
        else
        {
            Order newOrder = new Order();
            newOrder.setProduct(p);
            newOrder.setDeliveryAddress(AuthUser.getCustomer().getAddresses().get(0));
            newOrder.setStatus(Order.OrderStatus.IN_CART);
            newOrder.setShipmentType(Order.ShipmentType.CUSTOMER_PICKUP);
            newOrder.setAmount(amount);
            newOrder.setDateOfPurchase(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
            orders.add(newOrder);
            RetrofitService.getApi(OrderApi.class).postOrder(newOrder).enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    newOrder.setId(response.body().getId());
                }

                @Override
                public void onFailure(Call<Order> call, Throwable t) {
                    throw new RuntimeException(t);
                }
            });
        }
    }


    public boolean addNewFavProduct (Product product)
    {
        if (getFavoriteProducts().add(product))
        {
            RetrofitService.getApi(CustomerApi.class).postNewFavoriteProduct(getId(), product.getId()).enqueue(new Callback<Customer>() {
                @Override
                public void onResponse(Call<Customer> call, Response<Customer> response) {

                }

                @Override
                public void onFailure(Call<Customer> call, Throwable t) {

                }
            });
            return true;
        }
        return false;
    }
    public boolean removeFavProduct (Product product)
    {
        if (getFavoriteProducts().remove(product))
        {
            RetrofitService.getApi(CustomerApi.class).deleteNewFavoriteProduct(getId(), product.getId()).enqueue(new Callback<Customer>() {
                @Override
                public void onResponse(Call<Customer> call, Response<Customer> response) {

                }

                @Override
                public void onFailure(Call<Customer> call, Throwable t) {

                }
            });
            return true;
        }
        return false;
    }

    public boolean addNewFavProducer (Producer producer)
    {
        if (getFavoriteProducers().add(producer))
        {
            RetrofitService.getApi(CustomerApi.class).postNewFavoriteProducer(getId(), producer.getId()).enqueue(new Callback<Customer>() {
                @Override
                public void onResponse(Call<Customer> call, Response<Customer> response) {

                }

                @Override
                public void onFailure(Call<Customer> call, Throwable t) {

                }
            });
            return true;
        }
        return false;
    }
    public boolean removeFavProducer (Producer producer)
    {
        if (getFavoriteProducers().remove(producer))
        {
            RetrofitService.getApi(CustomerApi.class).deleteNewFavoriteProducer(getId(), producer.getId()).enqueue(new Callback<Customer>() {
                @Override
                public void onResponse(Call<Customer> call, Response<Customer> response) {

                }

                @Override
                public void onFailure(Call<Customer> call, Throwable t) {

                }
            });
            return true;
        }
        return false;
    }

    public List<Producer> getFavoriteProducers() {
        return favoriteProducers;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<Address> getAddresses() {
        return addresses;
    }
}
