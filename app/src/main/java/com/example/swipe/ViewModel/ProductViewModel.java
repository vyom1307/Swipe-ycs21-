package com.example.swipe.ViewModel;


import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.ProgressDialog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.swipe.api.ProductAPIService;
import com.example.swipe.api.RetrofitInstance;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductViewModel extends ViewModel {
        private MutableLiveData<String> uploadResult = new MutableLiveData<>();

    private MutableLiveData<Boolean> showProgressDialog = new MutableLiveData<>(false);  // default to false

    public LiveData<String> getUploadResult() {
            return uploadResult;
        }
        public LiveData<Boolean>getShowProgressDialog() {
            return showProgressDialog;
        }

        public void uploadProduct(RequestBody nameBody, RequestBody typeBody, RequestBody priceBody, RequestBody taxBody, MultipartBody.Part imagePart) {
            // Show progress dialog
            showProgressDialog.setValue(true);
            ProductAPIService apiService = RetrofitInstance.getService();
            Call<ResponseBody> call = apiService.uploadProduct(nameBody, typeBody, priceBody, taxBody, imagePart);
            call.enqueue(new Callback<ResponseBody>() {


                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        showProgressDialog.setValue(false);
                        uploadResult.postValue("Product uploaded successfully");
                    } else {
                        uploadResult.postValue("Failed to upload product");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    showProgressDialog.setValue(false);
                    uploadResult.postValue("Error: " + t.getMessage());
                }
            });
            uploadResult.postValue("Product uploaded successfully");
            showProgressDialog.setValue(false);
        }
    }

