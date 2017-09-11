package com.example.v_mahich.blendnet;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by v-mahich on 9/7/2017.
 */

public class RetrofitBuilder {

    final static String BASE_URL = "http://blendnetv0.azurewebsites.net/";


    public static RetrofitInterface createService(String url){
       return new Retrofit.Builder()
                .baseUrl(url == null ? BASE_URL : url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build())
                .build().create(RetrofitInterface.class);
    }

}
