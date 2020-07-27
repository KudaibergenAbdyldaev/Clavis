package com.example.clavis.CategoryFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clavis.R;
import com.example.clavis.SharedViewModel.SharedViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryFragment extends Fragment implements LifecycleOwner {
    private SharedViewModel viewModel;
    private MainCategoryAdapter recyclerViewAdapter;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance()
            .getReference("Category");
    private RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_category, container, false);
        this.setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.rv_main_category);

        CategoryViewModel categoryViewModel  = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
        categoryViewModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<MainCategory>>() {
            @Override
            public void onChanged(final ArrayList<MainCategory> userArrayList) {
                recyclerViewAdapter = new MainCategoryAdapter(requireActivity(),userArrayList);
                recyclerView.setAdapter(recyclerViewAdapter);

                viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
                recyclerViewAdapter.setOnClickListener(new MainCategoryAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        MainCategory selectedItem = userArrayList.get(position);
                        final String selectedKey = selectedItem.getKey();

                        viewModel.setSelectedKey(selectedKey);
                        if (selectedKey.equals("Еда")){
                            Navigation.findNavController(view).navigate(R.id.shopsFragment);
                        }else if (selectedKey.equals("Недвижимость")) {
                            Navigation.findNavController(view).navigate(R.id.propertyFragment);
                            viewModel.setShopKey("shopName");
                        }
                        else {
                            Navigation.findNavController(view).navigate(R.id.homeFragment);
                            viewModel.setShopKey("shopName");
                        }
                    }
                });

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userArrayList.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            MainCategory historyData = postSnapshot.getValue(MainCategory.class);
                            assert historyData != null;
                            historyData.setKey(postSnapshot.getKey());
                            userArrayList.add(historyData);
                        }
                        recyclerViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

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
