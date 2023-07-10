package com.swifties.bahceden.data.deserializers;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.swifties.bahceden.models.Order;

import java.lang.reflect.Type;

public class OrderDeserializer implements JsonDeserializer<Order> {
    @Override
    public Order deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonObject())
        {
            if (json.getAsJsonObject().size() != 1)
            {
                if (json.getAsJsonObject().get("customer") != null && !json.getAsJsonObject().get("customer").isJsonNull())
                {
                    Order o = new Gson().fromJson(json, typeOfT);
                    o.setCustomerName(json.getAsJsonObject().get("customer").getAsString());
                }
                return new Gson().fromJson(json, typeOfT);
            }
            if (json.getAsJsonObject().has("id")) {
                Order o = new Order();
                o.setId(json.getAsJsonObject().get("id").getAsInt());
                return o;
            }
        }
        return null;
    }
}
