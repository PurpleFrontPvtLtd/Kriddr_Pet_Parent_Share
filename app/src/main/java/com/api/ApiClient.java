package com.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {



    public static final String BASE_URL = "http://18.188.194.43/kriddr/web_service/home/";

    private static Retrofit retrofit = null;
    private static ApiInterface requestInterface;


    public static ApiInterface getClient() {
        /*if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }*/
        if (requestInterface == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            requestInterface = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build().create(ApiInterface.class);
        }
        return requestInterface;
    }
}
