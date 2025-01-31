package com.example.swipe.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Upload {

        @Multipart
        @POST("add")
        Call<ResponseBody> uploadProduct(
                @Part("product_name") RequestBody productName,
                @Part("product_type") RequestBody productType,
                @Part("price") RequestBody price,
                @Part("tax") RequestBody tax,
                @Part MultipartBody.Part image
        );
    }

