package com.example.swipe;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.swipe.Products;
import com.example.swipe.api.ProductAPIService;

import com.example.swipe.api.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Repository {



        private ProductAPIService apiService;

        public Repository() {
            apiService = RetrofitInstance.getService();
        }

        public LiveData<List<Products>> getProducts() {
            MutableLiveData<List<Products>> productsData = new MutableLiveData<>();
            apiService.GetProducts().enqueue(new Callback<List<Products>>() {
                @Override
                public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        productsData.setValue(response.body());
                        Log.e("API_RESPONSE_BODY", "Response body: " + response.body().toString());
                    }

                }



                @Override
                public void onFailure(Call<List<Products>> call, Throwable t) {
                    Log.e("API_FAILURE", "API call failed: " + t.getMessage());
                    productsData.setValue(null);
                }
            });
            return productsData;
        }
    }


