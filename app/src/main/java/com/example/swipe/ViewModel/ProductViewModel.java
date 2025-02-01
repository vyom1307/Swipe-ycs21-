package com.example.swipe.ViewModel;

import static android.content.ContentValues.TAG;

import android.util.Log;
import com.example.swipe.api.ProductAPIService.UploadResponse;





import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.swipe.api.ProductAPIService;
import com.example.swipe.api.RetrofitInstance;

import java.io.IOException;

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
            Call<UploadResponse> call = apiService.uploadProduct(nameBody, typeBody, priceBody, taxBody, imagePart);
            Log.d(TAG, "Starting upload request to: " + call.request().url());
            call.enqueue(new Callback<UploadResponse>() {
                @Override
                public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        UploadResponse result = response.body();
                        Log.d(TAG, "Upload success: " + result.getMessage());

                        showProgressDialog.setValue(false);
                        uploadResult.postValue(result.getMessage());
                    } else {
                        String errorMsg = "Upload failed with code: " + response.code();
                        Log.e(TAG, errorMsg);
                        uploadResult.postValue(errorMsg);
                    }
                }

                @Override
                public void onFailure(Call<UploadResponse> call, Throwable throwable) {
                    showProgressDialog.setValue(false);
                    uploadResult.postValue("Error: " + throwable.getMessage());
                }




//
        });
        }
}

