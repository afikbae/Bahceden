package com.swifties.bahceden.data.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.swifties.bahceden.models.Customer;

import java.lang.reflect.Type;

public class CustomerSerializer implements JsonSerializer<Customer> {
    @Override
    public JsonElement serialize(Customer src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", src.getId());
        obj.addProperty("name", src.getName());
        obj.addProperty("email", src.getEmail());
        obj.addProperty("profileImageURL", src.getProfileImageURL());

        return obj;
    }
}
