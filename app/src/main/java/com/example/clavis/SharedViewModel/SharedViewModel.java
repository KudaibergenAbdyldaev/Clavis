package com.example.clavis.SharedViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    private MutableLiveData<String> selectedKey = new MutableLiveData<>();
    public void setSelectedKey(String input) {
        selectedKey.setValue(input);
    }
    public LiveData<String> getSelectedKey() {
        return selectedKey;
    }
    private MutableLiveData<String> shopKey = new MutableLiveData<>();
    public void setShopKey(String input) {
        shopKey.setValue(input);
    }
    public LiveData<String> getShopKey() {
        return this.shopKey;
    }

}
