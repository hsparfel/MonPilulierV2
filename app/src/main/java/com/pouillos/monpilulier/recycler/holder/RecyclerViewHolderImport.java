package com.pouillos.monpilulier.recycler.holder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.ImportContact;
import com.pouillos.monpilulier.entities.Prescription;
import com.pouillos.monpilulier.recycler.adapter.RecyclerAdapterImport;
import com.pouillos.monpilulier.recycler.adapter.RecyclerAdapterPrescription;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewHolderImport extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.detail)
    TextView detail;


    private WeakReference<RecyclerAdapterImport.Listener> callbackWeakRef;

    public RecyclerViewHolderImport(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        //ButterKnife.bind(this);
    }

    public void updateWithImport(ImportContact importContact, RecyclerAdapterImport.Listener callback){
        this.detail.setText(importContact.getId().toString()+importContact.getNbImportEffectue()+"/"+importContact.getNbImportIgnore());

        //4 - Create a new weak Reference to our Listener
        this.callbackWeakRef = new WeakReference<RecyclerAdapterImport.Listener>(callback);
    }


    @Override
    public void onClick(View view) {
        // 5 - When a click happens, we fire our listener.
        RecyclerAdapterImport.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.onClickDeleteButton(getAdapterPosition());
    }
}
