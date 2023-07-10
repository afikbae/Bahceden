package com.swifties.bahceden.data.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.swifties.bahceden.models.Address;

import java.lang.reflect.Type;

public class AddressSerializer implements JsonSerializer<Address> {
    @Override
    public JsonElement serialize(Address src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("id", src.getId());
        object.addProperty("title", src.getTitle());
        object.addProperty("fullAddress", src.getFullAddress());
        object.addProperty("phoneNumber", src.getPhoneNumber());
        object.addProperty("customerId", src.getCustomer().getId());
        return object;
    }
}
