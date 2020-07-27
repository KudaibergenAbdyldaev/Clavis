package com.example.clavis.HomeFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clavis.BasketFragment.BasketViewModel;
import com.example.clavis.BasketRoomData.BasketData;
import com.example.clavis.FavouriteFragment.FavouriteViewModel;
import com.example.clavis.FavouriteRoomData.FavouriteData;
import com.example.clavis.R;
import com.example.clavis.SharedViewModel.SharedViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marcoscg.dialogsheet.DialogSheet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment  extends Fragment implements LifecycleOwner{

    private DatabaseReference databaseReference;
    private List<ProductCategory> userArrayList;

    private DatabaseReference referenceProduct;
    private int count;
    private int amount = 1;
    private int totalPrice;
    private HomeAdapter homeAdapter;
    private BasketViewModel basketViewModel;
    private ArrayList<ProductData> list;
    private TextView txt_name;
    private BasketData basketData;
    private FavouriteViewModel favouriteViewModel;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(
                R.layout.fragment_home, container, false);
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
        homeAdapter = new HomeAdapter(requireActivity(),list);
        recyclerView2.setAdapter(homeAdapter);

        basketViewModel = new ViewModelProvider(requireActivity()).get(BasketViewModel.class);
        favouriteViewModel = new ViewModelProvider(requireActivity()).get(FavouriteViewModel.class);

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
                                            ProductData historyData = postSnapshot.getValue(ProductData.class);
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
                                    ProductData historyData = postSnapshot.getValue(ProductData.class);
                                    assert historyData != null;
                                    list.add(historyData);
                                }
                                homeAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        homeAdapter.setOnClickListener(new HomeAdapter.OnItemClickListener()
                                                       {
                                                           @Override
                                                           public void onItemClick(final int position) {
                                                               final ProductData productData = list.get(position);
                                                               LayoutInflater inflater = LayoutInflater.from(getContext());
                                                               final View view = inflater.inflate(R.layout.item_dialog, null);
                                                               final DialogSheet dialogSheet = new DialogSheet(requireContext())
                                                                       .setSingleLineTitle(true)
                                                                       .setColoredNavigationBar(true)
                                                                       .setView(view);
                                                               txt_name = view.findViewById(R.id.txt_name);
                                                               final TextView txt_count = view.findViewById(R.id.txt_count);
                                                               final CardView cardView = view.findViewById(R.id.cardView);
                                                               final CardView card_favourite = view.findViewById(R.id.card_favourite);
                                                               final TextView txt_description = view.findViewById(R.id.txt_description);
                                                               final TextView txt_phone = view.findViewById(R.id.txt_phone);
                                                               final ImageView imageView = view.findViewById(R.id.img_product);
                                                               final TextView txt_plus = view.findViewById(R.id.txt_plus);
                                                               final TextView txt_minus = view.findViewById(R.id.txt_minus);
                                                               final TextView txt_amount = view.findViewById(R.id.txt_amount);
                                                               final TextView textView7 = view.findViewById(R.id.textView7);

                                                               final Button add_basket = view.findViewById(R.id.add_basket);
                                                               final Button btn_call = view.findViewById(R.id.add_basket2);
                                                               final ImageView add_favourite_item = view.findViewById(R.id.add_favourite_item);
                                                               final ImageView delete_favourite_item = view.findViewById(R.id.delete_favourite_item);

                                                               txt_description.setText(productData.getDescription());
                                                               txt_count.setText(productData.getPrice());
                                                               txt_phone.setText("Наши контакты: "+productData.getPhone());
                                                               Picasso.get().load(productData.getImageUrl()).into(imageView);

                                                               switch (selectedKey) {
                                                                   case "Услуги":
                                                                   case "Машины":
                                                                   case "Объявление":
                                                                   case "Животные":
                                                                   case "Разное":
                                                                   case "Аптека":
                                                                       cardView.setVisibility(View.GONE);
                                                                       add_basket.setVisibility(View.GONE);
                                                                       textView7.setVisibility(View.GONE);
                                                                       btn_call.setVisibility(View.VISIBLE);
                                                                       card_favourite.setVisibility(View.GONE);
                                                                       break;
                                                                   default:
                                                                       totalPrice = ((Integer.parseInt(productData.getPrice())));
                                                                       count = count + totalPrice;
                                                                       txt_name.setText(productData.getName());
                                                                       card_favourite.setVisibility(View.VISIBLE);
                                                                       break;
                                                               }

                                                               btn_call.setOnClickListener(new View.OnClickListener() {
                                                                   @Override
                                                                   public void onClick(View v) {
                                                                       String url = "https://api.whatsapp.com/send?phone="+productData.getPhone();
                                                                       Intent i = new Intent(Intent.ACTION_VIEW);
                                                                       i.setData(Uri.parse(url));
                                                                       startActivity(i);
                                                                       dialogSheet.dismiss();
                                                                   }
                                                               });

                                                               txt_phone.setOnClickListener(new View.OnClickListener() {
                                                                   @Override
                                                                   public void onClick(View v) {
                                                                       String url = "https://api.whatsapp.com/send?phone="+productData.getPhone();
                                                                       Intent i = new Intent(Intent.ACTION_VIEW);
                                                                       i.setData(Uri.parse(url));
                                                                       startActivity(i);
                                                                       dialogSheet.dismiss();
                                                                   }
                                                               });

                                                               add_favourite_item.setOnClickListener(new View.OnClickListener() {
                                                                   @Override
                                                                   public void onClick(View v) {
                                                                       delete_favourite_item.setVisibility(View.VISIBLE);
                                                                       add_favourite_item.setVisibility(View.GONE);
                                                                   }
                                                               });
                                                               delete_favourite_item.setOnClickListener(new View.OnClickListener() {
                                                                   @Override
                                                                   public void onClick(View v) {
                                                                       delete_favourite_item.setVisibility(View.GONE);
                                                                       add_favourite_item.setVisibility(View.VISIBLE);
                                                                   }
                                                               });
                                                               txt_plus.setOnClickListener(new View.OnClickListener() {
                                                                   @Override
                                                                   public void onClick(View v) {
                                                                       count = count + totalPrice;
                                                                       amount++;
                                                                       txt_amount.setText(String.valueOf(amount));
                                                                       txt_count.setText(String.valueOf(count));
                                                                   }
                                                               });
                                                               txt_minus.setOnClickListener(new View.OnClickListener() {
                                                                   @Override
                                                                   public void onClick(View v) {
                                                                       if (amount == 1) {
                                                                       } else {
                                                                           count = count - totalPrice;
                                                                           amount--;
                                                                           txt_amount.setText(String.valueOf(amount));
                                                                           txt_count.setText(String.valueOf(count));
                                                                       }
                                                                   }
                                                               });
                                                               add_basket.setOnClickListener(new View.OnClickListener() {
                                                                   @Override
                                                                   public void onClick(View v) {
                                                                       basketData = new BasketData(productData.getName(),
                                                                               productData.getImageUrl(),
                                                                               productData.getPrice(),
                                                                               count, amount, shopKey, selectedKey
                                                                       );
                                                                       Snackbar snackbar = Snackbar
                                                                               .make(getView(), R.string.added, Snackbar.LENGTH_SHORT);
                                                                       snackbar.show();

                                                                       basketViewModel.insert(basketData);
                                                                       dialogSheet.dismiss();
                                                                   }
                                                               });
                                                               dialogSheet.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                                   @Override
                                                                   public void onDismiss(DialogInterface dialog) {
                                                                       if (add_favourite_item.getVisibility() == View.GONE){
                                                                           FavouriteData favouriteData = new FavouriteData(productData.getName(),
                                                                                   productData.getImageUrl(),
                                                                                   productData.getPrice(),
                                                                                   count, amount,shopKey, selectedKey
                                                                           );
                                                                           favouriteViewModel.insert(favouriteData);
                                                                       }
                                                                       count = 0;
                                                                       amount = 1;
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
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.basket_menu, menu);
    }
}
