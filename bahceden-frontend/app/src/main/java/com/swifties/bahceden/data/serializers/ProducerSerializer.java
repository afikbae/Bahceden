package com.swifties.bahceden.data.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.swifties.bahceden.models.Producer;

import java.lang.reflect.Type;

public class ProducerSerializer implements JsonSerializer<Producer> {
    @Override
    public JsonElement serialize(Producer src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", src.getId());
        obj.addProperty("name", src.getName());
        obj.addProperty("shopName", src.getShopName());
        obj.addProperty("email", src.getEmail());
        if (src.getProfileImageURL() != null)
            obj.addProperty("profileImageURL", src.getProfileImageURL());
        if (src.getBackgroundImageURL() != null)
            obj.addProperty("backgroundImageURL", src.getBackgroundImageURL());
        obj.addProperty("phoneNumber", src.getPhoneNumber());
        obj.addProperty("rating", src.getRating());

        return obj;
    }
}
