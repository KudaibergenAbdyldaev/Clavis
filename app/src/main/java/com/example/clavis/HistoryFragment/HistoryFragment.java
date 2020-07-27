package com.example.clavis.HistoryFragment;

import android.content.DialogInterface;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
public class HistoryFragment extends Fragment implements LifecycleOwner {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private FavouriteViewModel favouriteViewModel;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance()
            .getReference("BrandOrder")
            .child(user.getUid());
    private TextView txt_name;
    private int count;
    private int amount;
    private BasketViewModel basketViewModel;
    private ImageView imageView;
    private TextView empty_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        this.setHasOptionsMenu(true);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_history);
        HistoryViewModel viewModel = new ViewModelProvider(requireActivity()).get(HistoryViewModel.class);
        viewModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<HistoryData>>() {
            @Override
            public void onChanged(final ArrayList<HistoryData> userArrayList) {
                adapter = new HistoryAdapter(requireActivity(), userArrayList);
                recyclerView.setAdapter(adapter);
                adapter.setOnClickListener(new HistoryAdapter.OnItemClickListener() {
                                               @Override
                                               public void onItemClick(final int position) {
                                                   final HistoryData productData = userArrayList.get(position);
                                                   LayoutInflater inflater = LayoutInflater.from(getContext());
                                                   final View view = inflater.inflate(R.layout.item_dialog_history, null);
                                                   final DialogSheet dialogSheet = new DialogSheet(getContext())
                                                           .setSingleLineTitle(true)
                                                           .setColoredNavigationBar(true)
                                                           .setView(view);
                                                   txt_name = view.findViewById(R.id.txt_name);
                                                   final TextView txt_count = view.findViewById(R.id.txt_count);
                                                   final ImageView imageView = view.findViewById(R.id.img_product);
                                                   final TextView txt_plus = view.findViewById(R.id.txt_plus);
                                                   final TextView txt_minus = view.findViewById(R.id.txt_minus);
                                                   final TextView txt_amount = view.findViewById(R.id.txt_amount);
                                                   final Button add_basket = view.findViewById(R.id.add_basket);
                                                   final ImageView add_favourite_item = view.findViewById(R.id.add_favourite_item);
                                                   final ImageView delete_favourite_item = view.findViewById(R.id.delete_favourite_item);
                                                   final int totalPrice = ((Integer.parseInt(productData.getPrice())));
                                                   count = count + productData.getCount();
                                                   amount = productData.getAmount();
                                                   txt_amount.setText(String.valueOf(amount));

                                                   txt_name.setText(productData.getProduct_name());
                                                   txt_count.setText(String.valueOf(productData.getCount()));
                                                   Picasso.get().load(productData.getImageView()).into(imageView);

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
                                                           BasketData basketData = new BasketData(productData.getProduct_name(),
                                                                   productData.getImageView(),
                                                                   productData.getPrice(),
                                                                   count, amount, productData.getShop(), productData.getCategory()
                                                           );
                                                           Snackbar snackbar = Snackbar
                                                                   .make(getView(), R.string.deleted, Snackbar.LENGTH_SHORT);
                                                           snackbar.show();
                                                           basketViewModel.insert(basketData);
                                                           dialogSheet.dismiss();
                                                       }
                                                   });
                                                   dialogSheet.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                       @Override
                                                       public void onDismiss(DialogInterface dialog) {
                                                           if (add_favourite_item.getVisibility() == View.GONE) {
                                                               FavouriteData favouriteData = new FavouriteData(productData.getProduct_name(),
                                                                       productData.getImageView(),
                                                                       productData.getPrice(),
                                                                       count, amount,productData.getShop(), productData.getCategory()
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

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userArrayList.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            HistoryData historyData = postSnapshot.getValue(HistoryData.class);
                            userArrayList.add(historyData);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        imageView = view.findViewById(R.id.imageView11);
        empty_text = view.findViewById(R.id.empty_text_view3);
        favouriteViewModel = new ViewModelProvider(requireActivity()).get(FavouriteViewModel.class);
        basketViewModel = new ViewModelProvider(requireActivity()).get(BasketViewModel.class);
        return view;
    }

    private Observer<ArrayList<HistoryData>> userListUpdateObserver = new Observer<ArrayList<HistoryData>>() {
        @Override
        public void onChanged(final ArrayList<HistoryData> userArrayList) {
//            if (grandTotal(userArrayList) == a) {
//                imageView.setVisibility(View.VISIBLE);
//                empty_text.setVisibility(View.VISIBLE);
//                recyclerView.setVisibility(View.GONE);
//                setHasOptionsMenu(false);
//            } else {
//                imageView.setVisibility(View.GONE);
//                empty_text.setVisibility(View.GONE);
//                recyclerView.setVisibility(View.VISIBLE);
//                setHasOptionsMenu(true);
//            }


        }
    };

    private int grandTotal(List<HistoryData> items) {
        int totalPrice1 = 0;
        for (int i = 0; i < items.size(); i++) {
            totalPrice1 += items.get(i).getAmount();
        }
        return totalPrice1;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.basket_menu, menu);
    }
}
