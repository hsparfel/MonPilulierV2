package com.pouillos.monpilulier.recycler.holder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Prescription;
import com.pouillos.monpilulier.entities.Rappel;
import com.pouillos.monpilulier.recycler.adapter.RecyclerAdapterPrescription;
import com.pouillos.monpilulier.recycler.adapter.RecyclerAdapterRappel;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewHolderPrescription extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.detail)
    TextView detail;

    private WeakReference<RecyclerAdapterPrescription.Listener> callbackWeakRef;

    public RecyclerViewHolderPrescription(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        //ButterKnife.bind(this);
    }

    public void updateWithPrescription(Prescription prescription, RecyclerAdapterPrescription.Listener callback){
        this.detail.setText(prescription.getMedicament().toString());
        this.detail.setOnClickListener(this);

        //4 - Create a new weak Reference to our Listener
        this.callbackWeakRef = new WeakReference<RecyclerAdapterPrescription.Listener>(callback);
    }


    @Override
    public void onClick(View view) {
        // 5 - When a click happens, we fire our listener.
        RecyclerAdapterPrescription.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.onClickPrescriptionButton(getAdapterPosition());
    }
}
