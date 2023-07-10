package com.swifties.bahceden.data.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.swifties.bahceden.models.Order;

import java.lang.reflect.Type;

public class OrderStatusDeserializer implements JsonDeserializer<Order.OrderStatus> {
    @Override
    public Order.OrderStatus deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        int typeInt = json.getAsInt();
        switch (typeInt) {
            case 1:
                return Order.OrderStatus.IN_CART;
            case 2:
                return Order.OrderStatus.PENDING;
            case 3:
                return Order.OrderStatus.ONGOING;
            case 4:
                return Order.OrderStatus.DELIVERED;
            case 5:
                return Order.OrderStatus.CANCELLED;
            default:
                throw new IllegalArgumentException("Invalid ShipmentType value: " + typeInt);
        }
    }
}
