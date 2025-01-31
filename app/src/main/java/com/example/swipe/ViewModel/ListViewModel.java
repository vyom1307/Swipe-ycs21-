package com.example.swipe.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.swipe.Products;
import com.example.swipe.Repository;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListViewModel extends AndroidViewModel {

    private Repository repository;
    private LiveData<List<Products>> productsList;

    public ListViewModel(Application application) {
        super(application);
        repository = new Repository();
        productsList = repository.getProducts();
    }

    public LiveData<List<Products>> getProductsList() {
        return productsList;
    }


}

