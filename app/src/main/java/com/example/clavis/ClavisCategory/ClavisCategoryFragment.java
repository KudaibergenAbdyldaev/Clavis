package com.example.clavis.ClavisCategory;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clavis.HomeFragment.ProductData;
import com.example.clavis.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marcoscg.dialogsheet.DialogSheet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClavisCategoryFragment extends Fragment {

    private List<ProductData> userArrayList;
    private ClavisCategoryAdapter recyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_clavis_category, container, false);

        userArrayList = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_calavis);
        recyclerViewAdapter = new ClavisCategoryAdapter(requireActivity(),userArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("Category").child("Услуги").child("Shops").child("shopName").child("ProductCategory").child("Услуги Клавис").child("Products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userArrayList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ProductData historyData = postSnapshot.getValue(ProductData.class);
                    userArrayList.add(historyData);
                }
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerViewAdapter.setOnClickListener(new ClavisCategoryAdapter.OnItemClickListener()
                                               {
                                                   @Override
                                                   public void onItemClick(final int position) {
                                                       final ProductData productData = userArrayList.get(position);
                                                       LayoutInflater inflater = LayoutInflater.from(getContext());
                                                       final View view = inflater.inflate(R.layout.item_property, null);
                                                       final DialogSheet dialogSheet = new DialogSheet(requireContext())
                                                               .setSingleLineTitle(true)
                                                               .setColoredNavigationBar(true)
                                                               .setView(view);
                                                       final TextView txt_count = view.findViewById(R.id.txt_count);
                                                       final TextView txt_description = view.findViewById(R.id.txt_description);
                                                       final ImageView imageView = view.findViewById(R.id.img_product);
                                                       final TextView txt_contacts = view.findViewById(R.id.txt_contacts);
                                                       final Button add_basket = view.findViewById(R.id.add_basket);


                                                       txt_description.setText(productData.getDescription());
                                                       txt_count.setText(productData.getPrice());
                                                       txt_contacts.setText("Наши контакты: "+productData.getPhone());
                                                       Picasso.get().load(productData.getImageUrl()).into(imageView);

                                                       txt_contacts.setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {
                                                               String url = "https://api.whatsapp.com/send?phone="+productData.getPhone();
                                                               Intent i = new Intent(Intent.ACTION_VIEW);
                                                               i.setData(Uri.parse(url));
                                                               startActivity(i);
                                                               dialogSheet.dismiss();
                                                           }
                                                       });

                                                       add_basket.setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {
                                                               String url = "https://api.whatsapp.com/send?phone="+productData.getPhone();
                                                               Intent i = new Intent(Intent.ACTION_VIEW);
                                                               i.setData(Uri.parse(url));
                                                               startActivity(i);
                                                               dialogSheet.dismiss();
                                                           }
                                                       });

                                                       dialogSheet.show();
                                                   }

                                               }
        );
        return view;
    }
}
