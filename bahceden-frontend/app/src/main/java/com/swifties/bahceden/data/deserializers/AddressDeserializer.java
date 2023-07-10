package com.swifties.bahceden.data.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.swifties.bahceden.models.Address;
import com.swifties.bahceden.models.Customer;

import java.lang.reflect.Type;

public class AddressDeserializer implements JsonDeserializer<Address> {
    @Override
    public Address deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();

        Customer c = new Customer();
        c.setId(obj.get("customerId").getAsInt());

        return new Address(obj.get("id").getAsInt(), obj.get("title").getAsString(), obj.get("fullAddress").getAsString(), obj.get("phoneNumber").getAsString(), c);
    }
}
