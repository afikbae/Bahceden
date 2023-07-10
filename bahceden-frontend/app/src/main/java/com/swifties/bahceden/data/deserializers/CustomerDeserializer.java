package com.swifties.bahceden.data.deserializers;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.swifties.bahceden.data.RetrofitService;
import com.swifties.bahceden.models.Address;
import com.swifties.bahceden.models.Customer;
import com.swifties.bahceden.models.Order;
import com.swifties.bahceden.models.Producer;
import com.swifties.bahceden.models.Product;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CustomerDeserializer implements JsonDeserializer <Customer> {
    @Override
    public Customer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = RetrofitService.getGson();

        Customer customer = new Customer();

        customer.setId(json.getAsJsonObject().get("id").getAsInt());
        customer.setName(json.getAsJsonObject().get("name").getAsString());
        customer.setEmail(json.getAsJsonObject().get("email").getAsString());
        JsonElement profileImageElement = json.getAsJsonObject().get("profileImageURL");
        if(profileImageElement != null && !profileImageElement.isJsonNull()){
            customer.setProfileImageURL(profileImageElement.getAsString());
        } else {
            customer.setProfileImageURL("http://10.0.2.2:8080/images/noProfile.png");
        }

        JsonElement addressesJson = json.getAsJsonObject().get("addresses");
        if (addressesJson.isJsonArray()) {
            List<Address> addressList = new ArrayList<>();
            for (JsonElement addressElement : addressesJson.getAsJsonArray()) {
                Address address = gson.fromJson(addressElement, Address.class);
                addressList.add(address);
            }
            customer.setAddresses(addressList);
        }

        JsonElement favProductsJson = json.getAsJsonObject().get("favoriteProducts");
        if (favProductsJson.isJsonArray()) {
            List<Product> productList = new ArrayList<>();
            for (JsonElement productElement : favProductsJson.getAsJsonArray()) {
                Product product = gson.fromJson(productElement, Product.class);
                productList.add(product);
            }
            customer.setFavoriteProducts(productList);
        }

        JsonElement favProducersJson = json.getAsJsonObject().get("favoriteProducers");
        if (favProducersJson.isJsonArray()) {
            List<Producer> producerList = new ArrayList<>();
            for (JsonElement producerElement : favProducersJson.getAsJsonArray()) {
                Producer producer = gson.fromJson(producerElement, Producer.class);
                producerList.add(producer);
            }
            customer.setFavoriteProducers(producerList);
        }

        JsonElement ordersJson = json.getAsJsonObject().get("orders");
        if (ordersJson.isJsonArray()) {
            List<Order> orderList = new ArrayList<>();
            for (JsonElement orderElement : ordersJson.getAsJsonArray()) {
                Order order = gson.fromJson(orderElement, Order.class);
                orderList.add(order);
            }
            customer.setOrders(orderList);
        }

        return customer;
    }
}
