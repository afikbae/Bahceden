package com.swifties.bahceden.data;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.swifties.bahceden.activities.CustomerMainActivity;
import com.swifties.bahceden.activities.IntroActivity;
import com.swifties.bahceden.activities.ProducerMainActivity;
import com.swifties.bahceden.data.apis.CustomerApi;
import com.swifties.bahceden.data.apis.ProducerApi;
import com.swifties.bahceden.data.local.DBHelper;
import com.swifties.bahceden.models.Action;
import com.swifties.bahceden.models.Customer;
import com.swifties.bahceden.models.Producer;
import com.swifties.bahceden.models.User;

import java.util.function.Function;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthUser {
    private static AuthUser instance = null;
    private User user;
    private AuthUser() {}

    public static AuthUser getInstance() {
        if (instance == null) {
            instance = new AuthUser();
        }
        return instance;
    }

    public void updateUser(Action act){
        if (user instanceof Customer) {
            RetrofitService.getApi(CustomerApi.class).getCustomerById(user.getId()).enqueue(new Callback<Customer>() {
                @Override
                public void onResponse(Call<Customer> call, Response<Customer> response) {
                    if (response.body() != null) {
                        user = response.body();
                        if(act != null){
                            act.act();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Customer> call, Throwable t) {
                    Log.d("debug", "There was a problem updating the user: " + t.getMessage());
                }
            });
        } else {
            RetrofitService.getApi(ProducerApi.class).getProducerById(user.getId()).enqueue(new Callback<Producer>() {
                @Override
                public void onResponse(Call<Producer> call, Response<Producer> response) {
                    if (response.body() != null) {
                        user = response.body();
                        if(act != null){
                            act.act();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Producer> call, Throwable t) {
                    Log.d("debug", "There was a problem updating the user: " + t.getMessage());
                }
            });
        }
    }

    public void updateUser() {
        updateUser(null);
    }

    public void createUser(String email, int userType, Context context) {
        if (userType == IntroActivity.CUSTOMER_TYPE) {
            RetrofitService.getApi(CustomerApi.class).getCustomerByEmail(email).enqueue(new Callback<Customer>() {
                @Override
                public void onResponse(Call<Customer> call, Response<Customer> response) {
                    user = response.body();
                    context.startActivity(new Intent(context, CustomerMainActivity.class));
                }

                @Override
                public void onFailure(Call<Customer> call, Throwable t) {
                    throw new RuntimeException(t);
                }
            });
        } else if (userType == IntroActivity.PRODUCER_TYPE) {
            RetrofitService.getApi(ProducerApi.class).getProducerByEmail(email).enqueue(new Callback<Producer>() {
                @Override
                public void onResponse(Call<Producer> call, Response<Producer> response) {
                    user = response.body();
                    context.startActivity(new Intent(context, ProducerMainActivity.class));
                }

                @Override
                public void onFailure(Call<Producer> call, Throwable t) {
                }
            });
        } else {
            throw new RuntimeException("userType must be either customer or producer");
        }
    }

    public void deleteUser(Context context) {
        new DBHelper(context).deleteAllUsers();
        user = null;
    }

    public Customer getCustomerInstance() {
        if (user instanceof Customer) {
            return (Customer) user;
        } else {
            return null;
        }
    }

    public User getUser() {
        return user;
    }

    public static Customer getCustomer() {
        return getInstance().getCustomerInstance();
    }

    public Producer getProducerInstance() {
        if (user instanceof Producer) {
            return (Producer) user;
        } else {
            return null;
        }
    }

    public static Producer getProducer() {
        return getInstance().getProducerInstance();
    }
}
