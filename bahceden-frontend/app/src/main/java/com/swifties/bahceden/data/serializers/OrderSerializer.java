package com.swifties.bahceden.data.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.swifties.bahceden.models.Order;

import java.lang.reflect.Type;

public class OrderSerializer implements JsonSerializer<Order> {
    @Override
    public JsonElement serialize(Order src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.addProperty("status", src.getStatus().getValue());
        obj.addProperty("amount", src.getAmount());
        obj.addProperty("dateOfPurchase", src.getDateOfPurchase());
        obj.addProperty("shipmentType", src.getShipmentType().getValue());
        obj.addProperty("customerId", src.getCustomer().getId());
        obj.addProperty("producerId", src.getProduct().getProducer().getId());
        obj.addProperty("productId", src.getProduct().getId());
        if(src.getDeliveryAddress() != null)
            obj.addProperty("deliveryAddressId", src.getDeliveryAddress().getId());

        return obj;
    }
}
