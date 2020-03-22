package com.pouillos.monpilulier.activities.ui.listAllExamen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListAllExamenViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ListAllExamenViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is share fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}