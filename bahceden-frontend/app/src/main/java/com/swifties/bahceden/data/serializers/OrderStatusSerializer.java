package com.swifties.bahceden.data.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.swifties.bahceden.models.Order;

import java.lang.reflect.Type;

public class OrderStatusSerializer implements JsonSerializer<Order.OrderStatus> {
    @Override
    public JsonElement serialize(Order.OrderStatus src, Type typeOfSrc, JsonSerializationContext context) {
        switch (src) {
            case IN_CART:
                return new JsonPrimitive(1);
            case PENDING:
                return new JsonPrimitive(2);
            case ONGOING:
                return new JsonPrimitive(3);
            case DELIVERED:
                return new JsonPrimitive(4);
            case CANCELLED:
                return new JsonPrimitive(5);
            default:
                throw new IllegalArgumentException("Invalid OrderStatus value: " + src);
        }
    }
}
