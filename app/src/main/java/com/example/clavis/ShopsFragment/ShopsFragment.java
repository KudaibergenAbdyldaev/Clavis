package com.example.clavis.ShopsFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopsFragment extends Fragment {
    private ShopsAdapter adapter;
    private ArrayList<ShopsData> userArrayList;
    private String selectedKey;
    public ShopsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v =  inflater.inflate(R.layout.fragment_shops, container, false);
        final SharedViewModel viewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
        viewModel.getSelectedKey().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                selectedKey = s;

                DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                        .getReference("Category").child(selectedKey).child("Shops");
                userArrayList = new ArrayList<>();
                RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewDetail);

                adapter = new ShopsAdapter(requireActivity(),userArrayList);
                recyclerView.setAdapter(adapter);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userArrayList.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            ShopsData historyData = postSnapshot.getValue(ShopsData.class);
                            assert historyData != null;
                            historyData.setKey(postSnapshot.getKey());
                            userArrayList.add(historyData);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                adapter.setOnClickListener(new ShopsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        ShopsData selectedItem = userArrayList.get(position);
                        final String shopKey = selectedItem.getKey();
                        viewModel.setShopKey(shopKey);
                        Navigation.findNavController(v).navigate(R.id.homeFragment);
                    }
                });
            }
        });
        return v;
    }
}
