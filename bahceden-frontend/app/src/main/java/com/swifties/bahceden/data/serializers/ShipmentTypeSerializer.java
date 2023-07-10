package com.swifties.bahceden.data.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.swifties.bahceden.models.Order;

import java.lang.reflect.Type;

public class ShipmentTypeSerializer implements JsonSerializer<Order.ShipmentType> {
    @Override
    public JsonElement serialize(Order.ShipmentType src, Type typeOfSrc, JsonSerializationContext context) {
        switch (src) {
            case CUSTOMER_PICKUP:
                return new JsonPrimitive(1);
            case PRODUCER_DELIVERY:
                return new JsonPrimitive(2);
            case SHIPMENT:
                return new JsonPrimitive(3);
            default:
                return new JsonPrimitive(-1);
        }
    }
}
