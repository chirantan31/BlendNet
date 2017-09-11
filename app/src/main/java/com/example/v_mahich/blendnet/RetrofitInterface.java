package com.example.v_mahich.blendnet;

/**
 * Created by v-mahich on 9/4/2017.
 */

import com.example.v_mahich.blendnet.Models.Resource;

import java.util.ArrayList;
import java.util.List;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Aditya on 3/15/2017.
 */
public interface RetrofitInterface {

    @Streaming
    @GET("resources/download")
    Call<ResponseBody> downloadFile(@Query("fileName") String fileName);

    @GET("resources/")
    Call<List<Resource>> getResources();

    @GET("resources/request")
    Call<String> createRequest(@Query("id") int id);

}