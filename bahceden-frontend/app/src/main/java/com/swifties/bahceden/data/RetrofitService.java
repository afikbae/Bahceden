package com.swifties.bahceden.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.swifties.bahceden.data.deserializers.AddressDeserializer;
import com.swifties.bahceden.data.deserializers.CategoryDeserializer;
import com.swifties.bahceden.data.deserializers.CommentDeserializer;
import com.swifties.bahceden.data.deserializers.CustomerDeserializer;
import com.swifties.bahceden.data.deserializers.OrderDeserializer;
import com.swifties.bahceden.data.deserializers.OrderStatusDeserializer;
import com.swifties.bahceden.data.deserializers.ProductDeserializer;
import com.swifties.bahceden.data.deserializers.ShipmentTypeDeserializer;
import com.swifties.bahceden.data.deserializers.UnitTypeDeserializer;
import com.swifties.bahceden.data.serializers.AddressSerializer;
import com.swifties.bahceden.data.serializers.CommentSerializer;
import com.swifties.bahceden.data.serializers.CustomerSerializer;
import com.swifties.bahceden.data.serializers.OrderSerializer;
import com.swifties.bahceden.data.serializers.OrderStatusSerializer;
import com.swifties.bahceden.data.serializers.ProducerSerializer;
import com.swifties.bahceden.data.serializers.ProductSerializer;
import com.swifties.bahceden.data.serializers.ShipmentTypeSerializer;
import com.swifties.bahceden.models.Address;
import com.swifties.bahceden.models.Category;
import com.swifties.bahceden.models.Comment;
import com.swifties.bahceden.models.Customer;
import com.swifties.bahceden.models.Order;
import com.swifties.bahceden.models.Producer;
import com.swifties.bahceden.models.Product;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private static Retrofit retrofit;
    private static Gson gson;
    private RetrofitService() {
    }

    private static Retrofit getRetrofit() {
        if (retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/")
                    .addConverterFactory(GsonConverterFactory.create(getGson()))
                    .build();
        }
        return retrofit;
    }

    public static Gson getGson()
    {
        if (gson == null)
        {
            gson = new GsonBuilder()
                    .registerTypeAdapter(Address.class, new AddressDeserializer())
                    .registerTypeAdapter(Category.class, new CategoryDeserializer())
                    .registerTypeAdapter(Comment.class, new CommentDeserializer())
                    .registerTypeAdapter(Customer.class, new CustomerDeserializer())
                    .registerTypeAdapter(Order.OrderStatus.class, new OrderStatusDeserializer())
//                    .registerTypeAdapter(Order.class, new OrderDeserializer())
                    .registerTypeAdapter(Product.class, new ProductDeserializer())
                    .registerTypeAdapter(Order.ShipmentType.class, new ShipmentTypeDeserializer())
                    .registerTypeAdapter(Product.UnitType.class, new UnitTypeDeserializer())
                    .registerTypeAdapter(Address.class, new AddressSerializer())
                    .registerTypeAdapter(Product.class, new ProductSerializer())
                    .registerTypeAdapter(Comment.class, new CommentSerializer())
                    .registerTypeAdapter(Customer.class, new CustomerSerializer())
                    .registerTypeAdapter(Order.class, new OrderSerializer())
                    .registerTypeAdapter(Order.OrderStatus.class, new OrderStatusSerializer())
                    .registerTypeAdapter(Producer.class, new ProducerSerializer())
                    .registerTypeAdapter(Order.ShipmentType.class, new ShipmentTypeSerializer())
                    .create();
        }
        return gson;
    }

    public static <T> T getApi (Class<T> clazz)
    {
        return (T) RetrofitService.getRetrofit().create(clazz);
    }
}
