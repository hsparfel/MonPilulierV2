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

public class RecyclerViewHolderRappel extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.detail)
    TextView detail;

    @BindView(R.id.buttonDelete)
    ImageButton buttonDelete;

    private WeakReference<RecyclerAdapterRappel.Listener> callbackWeakRef;

    public RecyclerViewHolderRappel(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        //ButterKnife.bind(this);
    }

    public void updateWithRappel(Rappel rappel, RecyclerAdapterRappel.Listener callback){
        this.detail.setText(rappel.getHeure()+" - "+rappel.getQuantiteDose()+ " "+rappel.getDose().getName());
        this.buttonDelete.setOnClickListener(this);

        //4 - Create a new weak Reference to our Listener
        this.callbackWeakRef = new WeakReference<RecyclerAdapterRappel.Listener>(callback);
    }

    @Override
    public void onClick(View view) {
        // 5 - When a click happens, we fire our listener.
        RecyclerAdapterRappel.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.onClickDeleteButton(getAdapterPosition());
    }
}
