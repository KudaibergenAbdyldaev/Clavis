package com.example.clavis.CategoryFragment;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clavis.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.Holder> {
    private Activity context;
    private List<MainCategory> userArrayList;
    private OnItemClickListener clickListener;


    MainCategoryAdapter(Activity context, List<MainCategory> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    void setOnClickListener(OnItemClickListener listener){
        clickListener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.item_main_category,parent,false);
        return new Holder(rootView, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        MainCategory user = userArrayList.get(position);
        holder.txtView_title.setText(user.getName());
        Picasso.get()
                .load(user.getImageUrl())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView txtView_title;
        ImageView imageView;
        CardView cardView;
        Holder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            txtView_title = itemView.findViewById(R.id.txtView_title);
            imageView = itemView.findViewById(R.id.imageView4);
            cardView = itemView.findViewById(R.id.cardView);

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
