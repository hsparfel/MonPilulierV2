package com.pouillos.monpilulier.activities.ui.newAnalyse;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.ui.listAllAnalyse.ListAllAnalyseViewModel;

public class NewAnalyseFragment extends Fragment {

    private NewAnalyseViewModel mViewModel;

    public static NewAnalyseFragment newInstance() {
        return new NewAnalyseFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel =
                ViewModelProviders.of(this).get(NewAnalyseViewModel.class);
        View root = inflater.inflate(R.layout.fragment_new_analyse, container, false);

        //final RecyclerView rv = (RecyclerView) root.findViewById(R.id.list_all_analyse_recycler_view);
        return root;
    }

}
