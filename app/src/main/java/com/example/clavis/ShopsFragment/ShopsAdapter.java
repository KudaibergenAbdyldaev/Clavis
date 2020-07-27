package com.example.clavis.ShopsFragment;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clavis.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShopsAdapter extends RecyclerView.Adapter<ShopsAdapter.Holder> {
    private Activity context;
    private List<ShopsData> userArrayList;
    private OnItemClickListener clickListener;

    public ShopsAdapter(Activity context, List<ShopsData> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnClickListener(OnItemClickListener listener){
        clickListener = listener;
    }
    @NonNull
    @Override
    public ShopsAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.item_shops,parent,false);
        return new Holder(rootView, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopsAdapter.Holder holder, int position) {
        ShopsData user = userArrayList.get(position);
        holder.txtView_name.setText(user.getShopName());
        holder.txtView_address.setText("Адрес: "+user.getShopAddress());
        holder.txt_description.setText(user.getDescription());
        Picasso.get()
                .load(user.getImageUrl())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        private TextView txtView_name;
        private TextView txtView_address;
        private TextView txt_description;
        private ImageView imageView;
        Holder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            txtView_name = itemView.findViewById(R.id.txt_name);
            txtView_address = itemView.findViewById(R.id.textViewAddress);
            txt_description = itemView.findViewById(R.id.txt_description);
            imageView = itemView.findViewById(R.id.imageView2);
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
