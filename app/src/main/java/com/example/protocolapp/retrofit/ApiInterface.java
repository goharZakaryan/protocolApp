package com.example.protocolapp.retrofit;

import com.example.protocolapp.model.Protocol;
import com.example.protocolapp.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("server")
    Call<String> save(@Body Protocol requestModel);

    @POST("login")
    Call<User> login2(
            @Body String email

    );

    @GET("findProtocol")
    Call<List<Protocol>> findProtocolByEmail(@Query("email") String email);
    @GET("remove")
    Call<Void> remove(@Query("remove") Long id);
}
