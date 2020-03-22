package com.pouillos.monpilulier.activities.ui.listAllAnalyse;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.listallx.ListAllAnalyseActivity;
import com.pouillos.monpilulier.activities.newx.NewAnalyseActivity;
import com.pouillos.monpilulier.activities.ui.newAnalyse.NewAnalyseFragment;
import com.pouillos.monpilulier.views.adapter.ListAllAnalyseAdapter;

public class ListAllAnalyseFragment extends Fragment  {

    Dialog loginDialog;
    private ListAllAnalyseViewModel mListAllAnalyseViewModel;

    //2 - Declare callback
    private OnButtonClickedListener mCallback;

    // 1 - Declare our interface that will be implemented by any container activity
    public interface OnButtonClickedListener {
        public void onButtonClicked(View view);
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mListAllAnalyseViewModel =
                ViewModelProviders.of(this).get(ListAllAnalyseViewModel.class);
        View root = inflater.inflate(R.layout.fragment_list_all_analyse, container, false);

        final RecyclerView rv = (RecyclerView) root.findViewById(R.id.list_all_analyse_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rv.setAdapter(new ListAllAnalyseAdapter());

        //ImageButton buttonAddAnalyse = root.findViewById(R.id.buttonAddAnalyse);
        ImageButton buttonAddAnalyse = root.findViewById(R.id.buttonAddAnalyse);
       // buttonAddAnalyse.setOnClickListener(this);

        buttonAddAnalyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCallback.onButtonClicked(v);
            }
        });


        final TextView textView = root.findViewById(R.id.textTitre);
        mListAllAnalyseViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // 4 - Call the method that creating callback after being attached to parent activity
        this.createCallbackToParentActivity();
    }

    // --------------
    // ACTIONS
    // --------------

// --------------
    // FRAGMENT SUPPORT
    // --------------

    // 3 - Create callback to parent activity
    private void createCallbackToParentActivity(){
        try {
            //Parent activity will automatically subscribe to callback
            mCallback = (OnButtonClickedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString()+ " must implement OnButtonClickedListener");
        }
    }



}