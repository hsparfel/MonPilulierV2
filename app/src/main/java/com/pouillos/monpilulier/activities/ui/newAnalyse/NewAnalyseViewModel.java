package com.pouillos.monpilulier.activities.ui.newAnalyse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewAnalyseViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public NewAnalyseViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Enregistrer une analyse");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
