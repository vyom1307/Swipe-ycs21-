package com.example.swipe.api;
import com.example.swipe.Products;
import com.example.swipe.ViewModel.ListViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ProductAPIService {
    @GET("get")
    Call<List<Products>> GetProducts();

    @Multipart
    @POST("add")
    Call<UploadResponse> uploadProduct(
            @Part("product_name") RequestBody productName,
            @Part("product_type") RequestBody productType,
            @Part("price") RequestBody price,
            @Part("tax") RequestBody tax,
            @Part MultipartBody.Part image
    );
    class UploadResponse {
        @SerializedName("success")
        private boolean success;

        @SerializedName("message")
        private String message;

        @SerializedName("product_id")
        private String productId;

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getProductId() { return productId; }
    }
}
