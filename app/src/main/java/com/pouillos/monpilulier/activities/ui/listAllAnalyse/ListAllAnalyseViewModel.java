package com.pouillos.monpilulier.activities.ui.listAllAnalyse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListAllAnalyseViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ListAllAnalyseViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Liste de toutes les analyses");
    }

    public LiveData<String> getText() {
        return mText;
    }
}