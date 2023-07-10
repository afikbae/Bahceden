package com.swifties.bahceden;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.swifties.bahceden.data.RetrofitService;
import com.swifties.bahceden.data.apis.ProductApi;
import com.swifties.bahceden.data.deserializers.AddressDeserializer;
import com.swifties.bahceden.data.deserializers.CommentDeserializer;
import com.swifties.bahceden.data.deserializers.CustomerDeserializer;
import com.swifties.bahceden.data.deserializers.OrderStatusDeserializer;
import com.swifties.bahceden.data.deserializers.ShipmentTypeDeserializer;
import com.swifties.bahceden.data.deserializers.UnitTypeDeserializer;
import com.swifties.bahceden.data.serializers.AddressSerializer;
import com.swifties.bahceden.data.serializers.CustomerSerializer;
import com.swifties.bahceden.data.serializers.OrderSerializer;
import com.swifties.bahceden.data.serializers.OrderStatusSerializer;
import com.swifties.bahceden.data.serializers.ProducerSerializer;
import com.swifties.bahceden.data.serializers.ShipmentTypeSerializer;
import com.swifties.bahceden.models.Address;
import com.swifties.bahceden.models.Comment;
import com.swifties.bahceden.models.Customer;
import com.swifties.bahceden.models.Order;
import com.swifties.bahceden.models.Producer;
import com.swifties.bahceden.models.Product;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Test {
    public static void main(String[] args) {
        Gson gson = RetrofitService.getGson();



        Product p = gson.fromJson("{\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"Product 1\",\n" +
                "    \"description\": \"s\",\n" +
                "    \"category\": {\n" +
                "        \"id\": 1,\n" +
                "        \"name\": \"Category 1\"\n" +
                "    },\n" +
                "    \"unitType\": 1,\n" +
                "    \"pricePerUnit\": 9.99,\n" +
                "    \"producer\": {\n" +
                "        \"id\": 1,\n" +
                "        \"name\": \"Producer 1\",\n" +
                "        \"shopName\": \"Shop 1\",\n" +
                "        \"email\": \"emir@example.com\",\n" +
                "        \"phoneNumber\": \"+909876543222\",\n" +
                "        \"profileImageURL\": \"http://localhost:8080/images/ProducerProfile1.jpeg\",\n" +
                "        \"backgroundImageURL\": \"http://localhost:8080/images/ProducerBackground1.jpeg\",\n" +
                "        \"rating\": 4.5\n" +
                "    },\n" +
                "    \"imageURL\": \"http://localhost:8080/images/plainWhite.jpg\",\n" +
                "    \"comments\": [\n" +
                "        {\n" +
                "            \"id\": 1,\n" +
                "            \"product\": 1,\n" +
                "            \"author\": 1,\n" +
                "            \"countOfLikes\": 5,\n" +
                "            \"message\": \"Comment 1\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"availableAmount\": 10,\n" +
                "    \"rating\": 0.0\n" +
                "}", Product.class);

        System.out.println(p);
    }
}
