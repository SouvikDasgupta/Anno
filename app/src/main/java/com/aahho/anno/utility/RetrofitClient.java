package com.aahho.anno.utility;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by souvikdas on 5/8/17.
 */

public class RetrofitClient {
    private final static String BASE_URL = "http://139.59.42.80/";

    public Retrofit createRetrofitClient(){
        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS);
        OkHttpClient client = httpBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create())).client(client)
                .build();

        return retrofit;
    }

}