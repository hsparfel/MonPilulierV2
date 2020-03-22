package com.pouillos.monpilulier.activities.ui.listAllExamen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.pouillos.monpilulier.R;

public class ListAllExamenFragment extends Fragment {

    private ListAllExamenViewModel mListAllExamenViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mListAllExamenViewModel =
                ViewModelProviders.of(this).get(ListAllExamenViewModel.class);
        View root = inflater.inflate(R.layout.fragment_list_all_examen, container, false);
        final TextView textView = root.findViewById(R.id.text_share);
        mListAllExamenViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}