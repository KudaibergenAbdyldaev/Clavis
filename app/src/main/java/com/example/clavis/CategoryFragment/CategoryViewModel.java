package com.example.clavis.CategoryFragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryViewModel extends ViewModel {
    private MutableLiveData<ArrayList<MainCategory>> historyViewModel;
    private ArrayList<MainCategory> historyArrayList;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance()
            .getReference("Category");
    public CategoryViewModel() {
        historyViewModel = new MutableLiveData<>();

        // call your Rest API in init method
        init();
    }

    public MutableLiveData<ArrayList<MainCategory>> getUserMutableLiveData() {
        return historyViewModel;
    }

    private void init(){
        populateList();
        historyViewModel.setValue(historyArrayList);
    }

    private void populateList(){
        historyArrayList = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                historyArrayList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    MainCategory historyData = postSnapshot.getValue(MainCategory.class);
                    historyArrayList.add(historyData);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
