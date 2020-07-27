package com.example.clavis.HomeFragment;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.clavis.R;

import java.util.List;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.Holder> {
    private Activity context;
    private List<ProductCategory> userArrayList;
    private ProductCategoryAdapter.OnItemClickListener clickListener;

    public ProductCategoryAdapter(Activity context, List<ProductCategory> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnClickListener(ProductCategoryAdapter.OnItemClickListener listener){
        clickListener = listener;
    }
    @NonNull
    @Override
    public ProductCategoryAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.item_product_category,parent,false);
        return new ProductCategoryAdapter.Holder(rootView, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCategoryAdapter.Holder holder, int position) {
        ProductCategory user = userArrayList.get(position);
        holder.txtView_name.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        private TextView txtView_name;
        Holder(@NonNull View itemView, final ProductCategoryAdapter.OnItemClickListener listener) {
            super(itemView);
            txtView_name = itemView.findViewById(R.id.txt_category_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}