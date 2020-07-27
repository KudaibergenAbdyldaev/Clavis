package com.example.clavis.PropertyFragment;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clavis.HomeFragment.ProductCategory;
import com.example.clavis.HomeFragment.ProductCategoryAdapter;
import com.example.clavis.R;
import com.example.clavis.SharedViewModel.SharedViewModel;
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
public class PropertyFragment extends Fragment {

    private DatabaseReference databaseReference;
    private List<ProductCategory> userArrayList;

    private DatabaseReference referenceProduct;
    private PropertyAdapter homeAdapter;
    private ArrayList<PropertyData> list;
    private TextView txt_name;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_property, container, false);

        this.setHasOptionsMenu(true);

        //Using code from
        //https://developer.android.com/topic/libraries/architecture/viewmodel

        //for Category Products
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        userArrayList = new ArrayList<>();
        final ProductCategoryAdapter categoryAdapter = new ProductCategoryAdapter(requireActivity(),userArrayList);
        recyclerView.setAdapter(categoryAdapter);

        //for Products
        list = new ArrayList<>();
        final RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.recyclerViewDetail3);
        homeAdapter = new PropertyAdapter(requireActivity(),list);
        recyclerView2.setAdapter(homeAdapter);

        final SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getSelectedKey().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(final String selectedKey) {
                sharedViewModel.getShopKey().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(final String shopKey) {
                        databaseReference = FirebaseDatabase.getInstance()
                                .getReference("Category").child(selectedKey).child("Shops").child(shopKey).child("ProductCategory");

                        categoryAdapter.setOnClickListener(new ProductCategoryAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                ProductCategory selectedItem = userArrayList.get(position);
                                String selectedKeyCategory = selectedItem.getKey();
                                referenceProduct = FirebaseDatabase.getInstance()
                                        .getReference("Category")
                                        .child(selectedKey).child("Shops")
                                        .child(shopKey).child("ProductCategory")
                                        .child(selectedKeyCategory).child("Products");
                                referenceProduct.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        list.clear();
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            PropertyData historyData = postSnapshot.getValue(PropertyData.class);
                                            assert historyData != null;
                                            list.add(historyData);
                                        }
                                        homeAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        });

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                userArrayList.clear();
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    ProductCategory historyData = postSnapshot.getValue(ProductCategory.class);
                                    assert historyData != null;
                                    historyData.setKey(postSnapshot.getKey());
                                    userArrayList.add(historyData);
                                }
                                categoryAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        referenceProduct = FirebaseDatabase.getInstance()
                                .getReference("Category")
                                .child(selectedKey).child("Shops")
                                .child(shopKey).child("ProductCategory")
                                .child("Все").child("Products");
                        referenceProduct.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                list.clear();
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    PropertyData historyData = postSnapshot.getValue(PropertyData.class);
                                    assert historyData != null;
                                    list.add(historyData);
                                }
                                homeAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        homeAdapter.setOnClickListener(new PropertyAdapter.OnItemClickListener()
                                                       {
                                                           @Override
                                                           public void onItemClick(final int position) {
                                                               final PropertyData productData = list.get(position);
                                                               LayoutInflater inflater = LayoutInflater.from(getContext());
                                                               final View view = inflater.inflate(R.layout.item_property, null);
                                                               final DialogSheet dialogSheet = new DialogSheet(requireContext())
                                                                       .setSingleLineTitle(true)
                                                                       .setColoredNavigationBar(true)
                                                                       .setView(view);
                                                               txt_name = view.findViewById(R.id.txt_name);
                                                               final TextView txt_count = view.findViewById(R.id.txt_count);
                                                               final TextView txt_description = view.findViewById(R.id.txt_description);
                                                               final ImageView imageView = view.findViewById(R.id.img_product);
                                                               final TextView txt_contacts = view.findViewById(R.id.txt_contacts);
                                                               final Button add_basket = view.findViewById(R.id.add_basket);


                                                               txt_name.setText(productData.getName());
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
                    }
                });
            }

        });

        return view;
    }
}
