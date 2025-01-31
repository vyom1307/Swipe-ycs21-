package com.example.swipe;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.swipe.ViewModel.ListViewModel;
import com.example.swipe.databinding.FragmentProductListBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductList extends Fragment {

    private RecyclerView recyclerView;
    private Adapter adapter;
    private List<Products> products=new ArrayList<>();
    private ListViewModel viewModel;

    private FragmentProductListBinding binding;


    public ProductList() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_list, container, false);
        View view=binding.getRoot();
        recyclerView= binding.recView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter(products,this.getContext());
        //adapter=new Adapter(products,this.getContext());
        recyclerView.setAdapter(adapter);
        viewModel=new ViewModelProvider(this).get(ListViewModel.class);
        viewModel.getProductsList().observe(getViewLifecycleOwner(), newProducts -> {
            if(newProducts!=null&&!newProducts.isEmpty()){
                products.clear();
                products.addAll(newProducts);
                adapter.setProductList(products);
                adapter.notifyDataSetChanged();
            }else {
                Log.d("PRODUCT_LIST", "No products found or empty list");
                // You can show a message like "No products available" in the UI
            }


        });

        return view;
    }
}