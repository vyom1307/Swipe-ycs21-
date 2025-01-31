package com.example.swipe;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swipe.databinding.ItemListBinding;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ProductViewHolder> {
    private List<Products> productsList;
    private Context context;
    private List<Products>list=new ArrayList<>();

    public Adapter(List<Products> productsList, Context context) {
        this.productsList = productsList;
        this.context = context;
    }
    public void setProductList(List<Products> productList) {
        this.productsList = productList;
        list.addAll(productList);   // Add product items
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemListBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.item_list, parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Products product = productsList.get(position);
        holder.binding.setViewModel(product);
        holder.binding.tax.setText(String.valueOf(product.getTax())+" %");
        holder.binding.price.setText("$"+String.valueOf(product.getPrice()));


        holder.binding.executePendingBindings();

    }



    @Override
    public int getItemCount() {
         return productsList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private ItemListBinding binding;

        public ProductViewHolder(ItemListBinding binding){
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
